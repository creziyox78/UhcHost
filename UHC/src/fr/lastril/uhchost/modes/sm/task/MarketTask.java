package fr.lastril.uhchost.modes.sm.task;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.sm.MarketStatus;
import fr.lastril.uhchost.modes.sm.SlaveMarketMode;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.MarketPlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.ClassUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class MarketTask extends BukkitRunnable {

    private final UhcHost main;

    private final SlaveMarketMode slaveMarketMode;

    public MarketTask(UhcHost main, SlaveMarketMode slaveMarketMode){
        this.main = main;
        this.slaveMarketMode = slaveMarketMode;
    }

    @Override
    public void run() {
        if(MarketStatus.getInstance().isMarketStatus(MarketStatus.WAITING)){
            slaveMarketMode.setDecountStart(10);
        }
        if(MarketStatus.getInstance().isMarketStatus(MarketStatus.STARTING)){
            Bukkit.broadcastMessage(Messages.SLAVE_MARKET_PREFIX.getMessage() + "§aDémarrage des enchères dans§e " + slaveMarketMode.getDecountStart() + "§a secondes !");
            slaveMarketMode.setDecountStart(slaveMarketMode.getDecountStart() - 1);
            if(slaveMarketMode.getDecountStart()==0){
                for(PlayerManager playerManager : main.getPlayerManagerOnlines()){
                    MarketPlayerManager marketPlayerManager = playerManager.getMarketPlayerManager();
                    if(marketPlayerManager.isOwner() && marketPlayerManager.getMarketOwnerLocation() != null){
                        playerManager.getPlayer().teleport(marketPlayerManager.getMarketOwnerLocation().getOwnerLocation());
                        marketPlayerManager.setDiamonds(slaveMarketMode.getSlaveMarketConfig().getDiamondsPerOwners());
                    } else {
                        playerManager.getPlayer().teleport(new Location(Bukkit.getWorld("lobby"), 1238.5, 74, 425.5, -90.0F, 0.0F));
                    }
                    Bukkit.getOnlinePlayers().forEach(player1 -> {
                        playerManager.getPlayer().hidePlayer(player1);
                        playerManager.getPlayer().hidePlayer(playerManager.getPlayer());
                        playerManager.getPlayer().showPlayer(player1);
                        player1.showPlayer(playerManager.getPlayer());
                    });
                }
                MarketStatus.setMarketStatus(MarketStatus.COOLDOWN);
                slaveMarketMode.setTimeBuy(slaveMarketMode.getSlaveMarketConfig().getTimePerBuy());
                slaveMarketMode.setCooldown(5);
            }
        }
        if(MarketStatus.getInstance().isMarketStatus(MarketStatus.COOLDOWN)){
            for(PlayerManager playerManager : main.getPlayerManagerOnlines()){
                MarketPlayerManager marketPlayerManager = playerManager.getMarketPlayerManager();
                if(marketPlayerManager.isOwner()){
                    ActionBar.sendMessage(playerManager.getPlayer(), "§bDiamants restants : " + marketPlayerManager.getDiamonds());
                }
            }
            slaveMarketMode.setCooldown(slaveMarketMode.getCooldown()-1);
            if(slaveMarketMode.getCooldown() == 0){
                if(!slaveMarketMode.allPlayersIsPicked()){
                    slaveMarketMode.setBuyedPlayer(slaveMarketMode.getRandomPlayer());
                    slaveMarketMode.getBuyedPlayer().getPlayer().teleport(new Location(Bukkit.getWorld("lobby"), 1249.5, 63, 425.0, -90.0F, 0.0F));
                    slaveMarketMode.setTimeBuy(slaveMarketMode.getSlaveMarketConfig().getTimePerBuy());
                    slaveMarketMode.setCooldown(5);
                    Bukkit.broadcastMessage(Messages.SLAVE_MARKET_PREFIX.getMessage() + "§aEnchérisez des§b Diamants§a sur§e " + slaveMarketMode.getBuyedPlayer().getPlayerName() + "§a avec la commande §c/sm buy (nombre)§a.");
                    MarketStatus.setMarketStatus(MarketStatus.STARTED);
                } else {
                    Bukkit.broadcastMessage(Messages.SLAVE_MARKET_PREFIX.getMessage() + "§c§lFin des enchères ! Tout les joueurs ont été achetés !");
                    for(PlayerManager playerManager : main.getPlayerManagerOnlines()){
                        playerManager.getPlayer().teleport(main.getGamemanager().spawn);
                        Bukkit.getOnlinePlayers().forEach(player1 -> {
                            playerManager.getPlayer().hidePlayer(player1);
                            playerManager.getPlayer().hidePlayer(playerManager.getPlayer());
                            playerManager.getPlayer().showPlayer(player1);
                            player1.showPlayer(playerManager.getPlayer());
                        });
                    }
                    MarketStatus.setMarketStatus(MarketStatus.WAITING);
                }

            }
        }


        if(MarketStatus.getInstance().isMarketStatus(MarketStatus.STARTED)){
            slaveMarketMode.setTimeBuy(slaveMarketMode.getTimeBuy() - 1);
            for(PlayerManager playerManager : main.getPlayerManagerOnlines()){
                MarketPlayerManager marketPlayerManager = playerManager.getMarketPlayerManager();
                if(marketPlayerManager.isOwner()){
                    ActionBar.sendMessage(playerManager.getPlayer(), "§bDiamants restants : " + marketPlayerManager.getDiamonds());
                }
            }
            if(slaveMarketMode.getTimeBuy() == 10){
                Bukkit.broadcastMessage(Messages.SLAVE_MARKET_PREFIX.getMessage() + slaveMarketMode.getBuyedPlayer().getPlayerName() + "§b sera acheté dans " + slaveMarketMode.getTimeBuy() + " secondes.");
            }
            if(slaveMarketMode.getTimeBuy() <= 5){
                Bukkit.broadcastMessage(Messages.SLAVE_MARKET_PREFIX.getMessage() + slaveMarketMode.getBuyedPlayer().getPlayerName() + "§b sera acheté dans " + slaveMarketMode.getTimeBuy() + " secondes.");
            }
            if(slaveMarketMode.getTimeBuy() == 0){
                PlayerManager playerManager = slaveMarketMode.getBuyedPlayer();
                MarketPlayerManager marketPlayerManager = playerManager.getMarketPlayerManager();
                marketPlayerManager.setInOwnerTeam(slaveMarketMode.getOwnerGoingPick());
                marketPlayerManager.setPicked(true);
                slaveMarketMode.getOwnerGoingPick().getMarketPlayerManager().setDiamonds(slaveMarketMode.getOwnerGoingPick().getMarketPlayerManager().getDiamonds() - slaveMarketMode.getOwnerGoingPick().getMarketPlayerManager().getTradedDiamonds());
                MarketStatus.setMarketStatus(MarketStatus.COOLDOWN);
                Bukkit.broadcastMessage(Messages.SLAVE_MARKET_PREFIX.getMessage() + slaveMarketMode.getBuyedPlayer().getPlayerName() + "§a a été acheté par " + slaveMarketMode.getOwnerGoingPick().getPlayerName() + " pour§b " + slaveMarketMode.getOwnerGoingPick().getMarketPlayerManager().getTradedDiamonds() + " diamants.");
                for(PlayerManager playerManagers : main.getPlayerManagerOnlines()){
                    MarketPlayerManager marketPlayerManagers = playerManagers.getMarketPlayerManager();
                    if(marketPlayerManagers.isOwner()){
                        marketPlayerManagers.setTradedDiamonds(0);
                    }
                }
            }

        }
    }
}
