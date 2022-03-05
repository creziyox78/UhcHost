package fr.lastril.uhchost.modes.sm.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.sm.SlaveMarketMode;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.MarketPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdBuy implements ModeSubCommand {

    private final UhcHost main;

    public CmdBuy(UhcHost main){
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "buy";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(main.getGamemanager().getModes() == Modes.SM){
            SlaveMarketMode slaveMarketMode = (SlaveMarketMode) main.getGamemanager().getModes().getMode();
            MarketPlayerManager marketPlayerManager = playerManager.getMarketPlayerManager();
            if(slaveMarketMode.getBuyedPlayer() != null){
                if(marketPlayerManager.isOwner()){
                    if(args.length == 2){
                        int diamonds = Integer.parseInt(args[1]);
                        if(diamonds > 0){
                            if(marketPlayerManager.getDiamonds() < diamonds){
                                player.sendMessage(Messages.SLAVE_MARKET_PREFIX.getMessage() + "§cVous n'avez pas les fonds nécessaires pour enchérir autant !");
                            } else {
                                if(slaveMarketMode.getOwnerGoingPick() != null){
                                    if(diamonds > slaveMarketMode.getOwnerGoingPick().getMarketPlayerManager().getTradedDiamonds()){
                                        slaveMarketMode.setOwnerGoingPick(playerManager);
                                        if(slaveMarketMode.getTimeBuy() <= 5){
                                            slaveMarketMode.setTimeBuy(5);
                                        }
                                        slaveMarketMode.getOwnerGoingPick().getMarketPlayerManager().setTradedDiamonds(diamonds);
                                        Bukkit.broadcastMessage(Messages.SLAVE_MARKET_PREFIX.getMessage() + player.getName() + "§a a enchérit§b " + diamonds + " Diamants§a sur " + slaveMarketMode.getBuyedPlayer().getPlayerName() + "§a.");
                                    } else {
                                        player.sendMessage(Messages.SLAVE_MARKET_PREFIX.getMessage() + "§cLe montant que vous avez indiqué est inférieur à l'enchère précédente !");
                                    }
                                } else {
                                    slaveMarketMode.setOwnerGoingPick(playerManager);
                                }
                            }
                        } else {
                            player.sendMessage(Messages.SLAVE_MARKET_PREFIX.getMessage() + "§cMettez un nombre diamant supérieur à 0 !");
                        }
                    } else {
                        player.sendMessage(Messages.SLAVE_MARKET_PREFIX.getMessage() + "§c/sm buy (nombres)");
                    }
                } else {
                    player.sendMessage(Messages.SLAVE_MARKET_PREFIX.getMessage() + "§cVous n'êtes pas Owner, vous ne pouvez pas enchérir sur un joueur !");
                }
            }

        }


        return false;
    }
}
