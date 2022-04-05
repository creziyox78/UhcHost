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
import org.bukkit.entity.Player;

import java.util.List;

public class CmdScellerEnd implements ModeSubCommand {

    private final UhcHost main;

    public CmdScellerEnd(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "end";
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
                if(kisukeUrahara.hasScelled() && kisukeUrahara.isScelle()) {
                    kisukeUrahara.setScelle(false);
                    kisukeUrahara.deleteCage();
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "Votre cage a été détruite.");
                    if(player.getMaxHealth() <= 2D*5D) {
                        player.setMaxHealth(player.getMaxHealth() + 2D*5D);
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§eVous avez perdu plus de la moitié de votre vie. Par conséquent, vous restez à 5 coeurs permanents jusqu'à la fin de la partie.");
                    } else {
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "Vous récupérez vos coeurs perdues.");
                        player.setMaxHealth(20D);
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous devez d'abord sceller un joueur.");
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
