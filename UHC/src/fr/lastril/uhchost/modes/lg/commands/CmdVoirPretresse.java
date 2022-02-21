package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.LGFacadeRole;
import fr.lastril.uhchost.modes.lg.roles.RealLG;
import fr.lastril.uhchost.modes.lg.roles.village.Pretresse;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdVoirPretresse implements ModeSubCommand {

    private final UhcHost pl;

    public CmdVoirPretresse(UhcHost pl) {
        this.pl = pl;
    }

    @Override
    public String getSubCommandName() {
        return "voir";
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
        if (playerManager.getRole() instanceof Pretresse) {
            Pretresse pretresse = (Pretresse) playerManager.getRole();
            if (pretresse.canSeeRole()) {
                if (args.length == 2) {
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);
                    if (target != null) {
                        PlayerManager targetManager = pl.getPlayerManager(target.getUniqueId());
                        if (targetManager.isAlive() && targetManager.hasRole()) {
                            sendRole(player, pretresse,targetManager);
                            pretresse.setSeeRole(false);
                        } else {
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cCe joueur n'est pas/plus dans la partie !");
                        }
                    } else {
                        player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cCe joueur n'est pas en ligne.");
                    }
                } else {
                    player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§6Précisez un joueur.");
                }
            } else {
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous ne pouvez pas utiliser votre pouvoir pour le moment.");
            }
        }
        return false;
    }

    private void sendRole(Player player, Pretresse pretresse, PlayerManager targetManager) {
        if (targetManager.getRole() instanceof LGFacadeRole) {
            LGFacadeRole lgFacadeRole = (LGFacadeRole) targetManager.getRole();
            if(lgFacadeRole.getRoleFacade() instanceof RealLG){
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage()
                        + "§bVous venez d'espionner " + targetManager.getPlayerName()
                        + ". Ce joueur est: " + lgFacadeRole.getRoleFacade().getRoleName());
                pretresse.addPlayerLg(targetManager);
            } else {
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§bVous venez d'espionner " + targetManager.getPlayerName() + " qui est villageois.");
            }
            player.setMaxHealth(player.getMaxHealth() - 2D*2D);
        } else {
            if(targetManager.getRole() instanceof RealLG){
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage()
                        + "§bVous venez d'espionner " + targetManager.getPlayerName()
                        + ". Ce joueur est: " + targetManager.getRole().getRoleName());
                pretresse.addPlayerLg(targetManager);
            } else {
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§bVous venez d'espionner " + targetManager.getPlayerName() + " qui est villageois.");
            }
            player.setMaxHealth(player.getMaxHealth() - 2D*2D);

        }


    }

}
