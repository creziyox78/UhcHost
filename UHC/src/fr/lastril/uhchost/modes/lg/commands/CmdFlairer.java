package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.LGFacadeRole;
import fr.lastril.uhchost.modes.lg.roles.RealLG;
import fr.lastril.uhchost.modes.lg.roles.village.Renard;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdFlairer implements ModeSubCommand {

    private final UhcHost pl;

    public CmdFlairer(UhcHost pl) {
        this.pl = pl;
    }

    @Override
    public String getSubCommandName() {
        return "flairer";
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
        if (playerManager.getRole() instanceof Renard) {
            Renard renard = (Renard) playerManager.getRole();
            if (renard.notReached()) {
                if (renard.isCanRenifle()) {
                    if (args.length == 2) {
                        String targetName = args[1];
                        Player target = Bukkit.getPlayer(targetName);
                        if (target != null && target != player) {
                            PlayerManager targetManager = pl.getPlayerManager(target.getUniqueId());
                            if (targetManager.isAlive() && targetManager.hasRole()) {
                                sendCamps(player, targetManager);
                                renard.addUse();
                                renard.addRenifled(targetManager);
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
                    player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous devez attendre la nuit.");
                }
            } else {
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + Messages.CANT_USE_MORE_POWER.getMessage());
            }

        } else {
            player.sendMessage(Messages.not("Renard"));
        }
        return false;
    }

    private void sendCamps(Player player, PlayerManager targetManager) {
        if (targetManager.getRole() instanceof LGFacadeRole) {
            LGFacadeRole lgFacadeRole = (LGFacadeRole) targetManager.getRole();
            if (lgFacadeRole.getRoleFacade() instanceof RealLG){
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage()
                        + "§bVous venez de renifler " + targetManager.getPlayerName()
                        + ". Ce joueur est un loup-garou.");
                UhcHost.debug("Renard renifled façade LG");
            } else {
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage()
                        + "§bVous venez de renifler " + targetManager.getPlayerName()
                        + ". Ce joueur n'est pas un loup-garou.");
                UhcHost.debug("Renard renifled façade not LG");
            }

        } else {
            if (targetManager.getRole() instanceof RealLG) {
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage()
                        + "§bVous venez de renifler " + targetManager.getPlayerName()
                        + ". Ce joueur est un loup-garou.");
                UhcHost.debug("Renard renifled LG");
            } else {
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage()
                        + "§bVous venez de renifler " + targetManager.getPlayerName()
                        + ". Ce joueur n'est pas un loup-garou.");
                UhcHost.debug("Renard renifled not LG");
            }
        }
    }

}
