package fr.lastril.uhchost.modes.bleach.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.KisukeUrahara;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdSceller implements ModeSubCommand {

    private final UhcHost main;

    public CmdSceller(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "sceller";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if(playerManager.getRole() instanceof KisukeUrahara) {
            KisukeUrahara kisukeUrahara = (KisukeUrahara) playerManager.getRole();
            if(bleachPlayerManager.canUsePower()) {
                if(!kisukeUrahara.hasScelled()) {
                    if(args.length == 2) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if(target != null) {
                            PlayerManager targetPlayerManager = main.getPlayerManager(target.getUniqueId());
                            if(targetPlayerManager.isAlive()) {
                                kisukeUrahara.setScelled(true);
                                kisukeUrahara.setScelle(true);
                                kisukeUrahara.spawnCage(target);
                                target.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§eVous êtes scellé par Kisuke Urahara. Vous devez attendre qu'il vous libère.");
                                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§eVous avez scellé§6 " + target.getName() + ". Toutes les 30 secondes, vous perdrez 1 coeur permanent tant que vous scellé ce joueur.");
                            } else {
                                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cCe joueur n'est pas en vie.");
                            }
                        } else {
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cCe joueur n'est pas connecté.");
                        }
                    } else {
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous devez spécifier un joueur.");
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous avez déjà scellé un joueur.");
                }
            } else {
                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
            }
        } else{
            player.sendMessage(Messages.not("Kisuke Urahara"));
        }
        return false;
    }
}
