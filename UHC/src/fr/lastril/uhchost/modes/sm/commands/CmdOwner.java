package fr.lastril.uhchost.modes.sm.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.MarketPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdOwner implements ModeSubCommand {

    private final UhcHost main;

    public CmdOwner(UhcHost main){
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "owner";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(main.getGamemanager().getHost() == player.getUniqueId()||main.getGamemanager().isCoHost(player)){
            if(args.length == 2){
                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);
                if(target != null){
                    PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
                    MarketPlayerManager marketTargetManager = targetManager.getMarketPlayerManager();
                    if(!marketTargetManager.isOwner()){
                        marketTargetManager.setOwner(true);
                        marketTargetManager.setPicked(true);
                        marketTargetManager.setRandomTeams();
                        Bukkit.broadcastMessage(Messages.SLAVE_MARKET_PREFIX.getMessage() + "§e"+target.getName() + "§a est à présent Owner !");
                    } else {
                        marketTargetManager.setOwner(false);
                        marketTargetManager.setPicked(false);
                        marketTargetManager.resetTeam();
                        Bukkit.broadcastMessage(Messages.SLAVE_MARKET_PREFIX.getMessage() + "§e"+target.getName() + "§c n'est désormais plus Owner !");
                    }
                }
            }
        } else {
            player.sendMessage(Messages.NOT_PERM.getMessage());
        }

        return false;
    }
}
