package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.LGFacadeRole;
import fr.lastril.uhchost.modes.lg.roles.village.voyante.Voyante;
import fr.lastril.uhchost.modes.lg.roles.village.voyante.VoyanteBavarde;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.WolfPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdVoirVoyante implements ModeSubCommand {

    private final UhcHost pl;

    public CmdVoirVoyante(UhcHost pl) {
        this.pl = pl;
    }

    @Override
    public String getSubCommandName() {
        return "espionner";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
        WolfPlayerManager wolfPlayerManager = playerManager.getWolfPlayerManager();
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if (playerManager.getRole() instanceof Voyante) {
            UhcHost.debug("Checking voyante...");
            Voyante voyante = (Voyante) playerManager.getRole();
            if (voyante.canSeeRole()) {
                UhcHost.debug("Voyante can see");
                if (args.length == 2) {
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);
                    UhcHost.debug("args is good 1");
                    if (target != null) {
                        PlayerManager targetManager = pl.getPlayerManager(target.getUniqueId());
                        if (targetManager.isAlive() && targetManager.hasRole()) {
                            if(wolfPlayerManager.isSarbacaned()){
                                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cLa sarcabane de l'Indigène vous empêche d'utiliser votre pouvoir !");
                                return false;
                            }
                            UhcHost.debug("target has role and is alive.");
                            sendRole(player, targetManager, false);
                            voyante.setSeeRole(false);
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
        if (playerManager.getRole() instanceof VoyanteBavarde) {
            VoyanteBavarde voyante = (VoyanteBavarde) playerManager.getRole();
            UhcHost.debug("Checking voyante bavarde...");
            if (voyante.canSeeRole()) {
                UhcHost.debug("Voyante bavarde can see");
                if (args.length == 2) {
                    UhcHost.debug("args is good 2");
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);
                    if (target != null) {
                        PlayerManager targetManager = pl.getPlayerManager(target.getUniqueId());
                        if (targetManager.isAlive() && targetManager.hasRole()) {
                            if(wolfPlayerManager.isSarbacaned()){
                                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cLa sarcabane de l'Indigène vous empêche d'utiliser votre pouvoir !");
                                return false;
                            }
                            UhcHost.debug("target bavarde has role and is alive.");
                            sendRole(player, targetManager, true);
                            voyante.setSeeRole(false);
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

    private void sendRole(Player player, PlayerManager targetManager, boolean broadcasted) {
        UhcHost.debug("Sending target role...");
        if (targetManager.getRole() instanceof LGFacadeRole) {
            UhcHost.debug("Role is LGFacade");
            LGFacadeRole lgFacadeRole = (LGFacadeRole) targetManager.getRole();
            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage()
                    + "§bVous venez d'espionner " + targetManager.getPlayerName()
                    + ". Ce joueur est: " + lgFacadeRole.getRoleFacade().getRoleName());
            UhcHost.debug("Checking if broadcast...");
            if (broadcasted) {
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage("§bLa Voyante Bavarde a espionner un joueur qui est " + lgFacadeRole.getRoleFacade().getRoleName());
                Bukkit.broadcastMessage("");
            }
            switch (lgFacadeRole.getRoleFacade().getCamp()){
                case LOUP_GAROU:
                case LOUP_GAROU_BLANC:
                    UhcHost.debug("Role seeing is LG.");
                    break;
                default:
                    player.setHealth(player.getHealth() - 6);
                    UhcHost.debug("Role seeing is not LG, apply damage");
                    break;
            }
        } else {
            UhcHost.debug("Send to voyante");
            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage()
                    + "§bVous venez d'espionner " + targetManager.getPlayerName()
                    + ". Ce joueur est: " + targetManager.getRole().getRoleName());
            UhcHost.debug("Checking if broadcast...");
            if (broadcasted) {
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage("§bLa Voyante Bavarde a espionner un joueur qui est " + targetManager.getRole().getRoleName());
                Bukkit.broadcastMessage("");
            }
            switch (targetManager.getRole().getCamp()){
                case LOUP_GAROU:
                case LOUP_GAROU_BLANC:
                    UhcHost.debug("Role seeing is LG.");
                    break;
                default:
                    player.setHealth(player.getHealth() - 6);
                    UhcHost.debug("Role seeing is not LG, apply damage");
                    break;
            }
        }


    }

}
