package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.village.EnfantSauvage;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdModele implements ModeSubCommand {

    private final UhcHost pl;

    public CmdModele(UhcHost pl) {
        this.pl = pl;
    }

    @Override
    public String getSubCommandName() {
        return "modele";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if (playerManager.getRole() instanceof EnfantSauvage) {
            EnfantSauvage enfantSauvage = (EnfantSauvage) playerManager.getRole();
            if(enfantSauvage.getModele() == null){
                if(args.length == 2){
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);
                    if(target != null){
                        PlayerManager targetManager = pl.getPlayerManager(target.getUniqueId());
                        if(targetManager.isAlive()){
                            enfantSauvage.setModele(targetManager);
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§bVous venez de choisir " + target.getName() + " comme modèle. Si ce dernier vient à mourir, alors vous devrez gagner avec les Loups-Garous.");
                        } else {
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cCe joueur n'est pas en vie !");
                        }
                    } else {
                        player.sendMessage(Messages.error("§cPrécisez un joueur en ligne !"));
                    }
                } else {
                    player.sendMessage(Messages.use("/lg modele <pseudo>"));
                }
            } else {
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous avez déjà choisie votre modèle.");
            }
        } else {
            player.sendMessage(Messages.not("Enfant Sauvage"));
        }
        return false;
    }

}
