package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.WolfPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdVote implements ModeSubCommand {

    private final LoupGarouManager loupGarouManager;
    private final UhcHost pl;

    public CmdVote(UhcHost pl, LoupGarouManager loupGarouManager) {
        this.pl = pl;
        this.loupGarouManager = loupGarouManager;
    }

    @Override
    public String getSubCommandName() {
        return "vote";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if (args.length == 2) {
            String targetName = args[1];
            Player target = Bukkit.getPlayer(targetName);
            if (target != null) {
                if(loupGarouManager.isVoteTime()){
                    WolfPlayerManager wolfPlayerManager = pl.getPlayerManager(player.getUniqueId()).getWolfPlayerManager();
                    if (!wolfPlayerManager.hasVoted()) {
                        wolfPlayerManager.setVoted(true);
                        WolfPlayerManager wolfTargetManager = pl.getPlayerManager(target.getUniqueId()).getWolfPlayerManager();
                        if (wolfTargetManager != null && pl.getPlayerManager(target.getUniqueId()).isAlive()) {
                            UhcHost.debug("Checking if player is already voted...");
                            if(!loupGarouManager.playerAlreadyVoted(wolfTargetManager)){
                                UhcHost.debug("Checking if player is already voted...");
                                wolfTargetManager.addVote();
                            }
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aVous venez de voter pour " + target.getName() + ".");
                        } else {
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous ne pouvez pas voter pour ce joueur !");
                        }
                    } else {
                        player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous avez déjà voté !");
                    }
                } else {
                    player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cLes votes sont clos !");
                }
            } else {
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cCe joueur n'est pas en ligne.");
            }
        }

        return false;
    }
}
