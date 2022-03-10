package fr.lastril.uhchost.modes.sm;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.config.modes.SlaveMarketConfig;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.inventory.guis.modes.sm.SlaveMarketGui;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeCommand;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.sm.commands.CmdBuy;
import fr.lastril.uhchost.modes.sm.commands.CmdOwner;
import fr.lastril.uhchost.modes.sm.task.MarketTask;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.MarketPlayerManager;
import fr.lastril.uhchost.tools.API.BungeeAPI;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.WorldUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class SlaveMarketMode extends Mode implements ModeConfig, ModeCommand {

    private final UhcHost main;
    private final SlaveMarketConfig slaveMarketConfig;
    private int decountStart = 10, timeBuy, cooldown = 5;
    private PlayerManager buyedPlayer, ownerGoingPick;

    public SlaveMarketMode() {
        super(Modes.SM);
        this.main = UhcHost.getInstance();
        this.slaveMarketConfig = new SlaveMarketConfig();
        main.getServer().createWorld(WorldCreator.name("sm_world"));
        Bukkit.getWorld("sm_world").setGameRuleValue("doMobSpawning", "false");
        Bukkit.getWorld("sm_world").setGameRuleValue("showDeathMessages", "false");
        Bukkit.getScheduler().runTaskTimer(main, new MarketTask(main, this), 20, 20);
    }

    @Override
    public void tick(int timer) {
        if(timer == 1){
            for(PlayerManager playerManager : main.getPlayerManagerAlives()){
                MarketPlayerManager marketPlayerManager = playerManager.getMarketPlayerManager();
                Player player = playerManager.getPlayer();
                if(marketPlayerManager.isOwner() && player != null){
                    if(marketPlayerManager.getDiamonds() > 0)
                        main.getInventoryUtils().giveItemSafely(player, new QuickItem(Material.DIAMOND, marketPlayerManager.getDiamonds()).toItemStack());
                }
            }
        }
    }


    @Override
    public void onPvp() {

    }

    @Override
    public void onBorder() {

    }

    @Override
    public void onTeleportation() {

    }

    @Override
    public void onNewEpisode() {

    }

    public int getCooldown() {
        return cooldown;
    }

    public int getDecountStart() {
        return decountStart;
    }

    public int getTimeBuy() {
        return timeBuy;
    }

    public PlayerManager getBuyedPlayer() {
        return buyedPlayer;
    }

    public void setBuyedPlayer(PlayerManager buyedPlayer) {
        this.buyedPlayer = buyedPlayer;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void setDecountStart(int decountStart) {
        this.decountStart = decountStart;
    }

    public void setTimeBuy(int timeBuy) {
        this.timeBuy = timeBuy;
    }

    @Override
    public void onDeath(OfflinePlayer player, Player killer) {
        if(killer != null){
            PlayerManager killerManager = main.getPlayerManager(killer.getUniqueId());
            killerManager.addKill(player.getUniqueId());
        }
        for(Player players : Bukkit.getOnlinePlayers()){
            if(player.getPlayer() != null && player.isOnline()){
                WorldUtils.spawnFakeLightning(players, player.getPlayer().getLocation(), true);
            }

        }

        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(joueur != null){
            Player onlinePlayer = player.getPlayer();
            if(player.isOnline()){
                joueur.setDeathLocation(onlinePlayer .getLocation());
                joueur.setItems(onlinePlayer .getInventory().getContents());
                joueur.setArmors(onlinePlayer.getInventory().getArmorContents());
            }

            joueur.setAlive(false);
            if(main.teamUtils.getTeam(player.getPlayer()) != null){
                Bukkit.broadcastMessage(Messages.PREFIX_WITH_ARROW.getMessage() + main.teamUtils.getTeam(player.getPlayer()).getPrefix() + player.getName() + " §cest mort.");
            }


            new BukkitRunnable() {

                @Override
                public void run() {
                    onlinePlayer.spigot().respawn();
                }
            }.runTaskLater(main, 5);

            new BukkitRunnable() {

                @Override
                public void run() {
                    onlinePlayer.setGameMode(GameMode.ADVENTURE);
                    onlinePlayer.setGameMode(GameMode.SPECTATOR);
                    onlinePlayer.teleport(joueur.getDeathLocation());
                }
            }.runTaskLater(main, 10);
        }



        /* DROPING INVENTORY */
        System.out.println("Droping inventory !");
        main.getInventoryUtils().dropInventory(joueur.getDeathLocation(), joueur.getItems(), joueur.getArmors());
        checkWin();

    }

    public boolean allPlayersIsPicked(){
        for(PlayerManager playerManager : main.getPlayerManagerOnlines()){
            MarketPlayerManager marketPlayerManager = playerManager.getMarketPlayerManager();
            if(!marketPlayerManager.isOwner() && !marketPlayerManager.isPicked()){
                return false;
            }
        }
        return true;
    }

    public PlayerManager getOwnerGoingPick() {
        return ownerGoingPick;
    }

    public void setOwnerGoingPick(PlayerManager ownerGoingPick) {
        this.ownerGoingPick = ownerGoingPick;
    }

    public PlayerManager getRandomPlayer(){
        List<PlayerManager> managerList = new ArrayList<>();
        for(PlayerManager playerManager : main.getPlayerManagerOnlines()){
            MarketPlayerManager marketPlayerManager = playerManager.getMarketPlayerManager();
            if(!marketPlayerManager.isOwner() && !marketPlayerManager.isPicked()){
                managerList.add(playerManager);
            }
        }
        return managerList.get(UhcHost.getRANDOM().nextInt(managerList.size()));
    }

    @Override
    public boolean isScheduledDeath() {
        return false;
    }

    @Override
    public boolean isEpisodeMode() {
        return false;
    }

    @Override
    public void onDamage(Player target, Player damager) {

    }

    @Override
    public void checkWin() {

        if(GameState.isState(GameState.STARTED)){
            if(main.teamUtils.getAllTeamsAlives().size() == 1){
                Team team = main.teamUtils.getAllTeamsAlives().get(0);
                GameState.setCurrentState(GameState.ENDED);
                Map<PlayerManager, Integer> damages = new HashMap<>();
                for (PlayerManager playerManager : main.getAllPlayerManager().values()) {
                    damages.put(playerManager, playerManager.getDamages());
                }
                Map<PlayerManager, Integer> kills = new HashMap<>();
                for (PlayerManager playerManager : main.getAllPlayerManager().values()) {
                    kills.put(playerManager, playerManager.getKills().size());
                }
                PlayerManager mostKills = kills.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .findFirst().get().getKey();

                Bukkit.broadcastMessage("§7§m-------------------------------------------------\n" +
                                "\n " +
                                "§e§L Victoire de l'équipe : "+ team.getName() + "\n " +
                                "\n " +
                                "§a Meilleur Joueur de la Partie§f┃\n" +
                                "\n " +
                                "§e " + mostKills.getPlayerName() + " §c§l(" + mostKills.getKills().size() + ") joueur(s) tué(es)\n " +
                        "\n " +
                                "§aMerci de votre participation !\n" +
                                "§7Le serveur s'éteindra automatiquement dans 1 minute !\n" +
                                "\n " +
                                "§7§m-------------------------------------------------");

                
                Bukkit.getScheduler().runTaskLater(this.main, () -> {
                    if (this.main.getConfig().getBoolean("bungeecord")) {
                        if (this.main.getConfig().getString("server-redirection") != null && !this.main
                                .getConfig().getString("server-redirection").equalsIgnoreCase("null"))
                            Bukkit.getOnlinePlayers().forEach(p -> BungeeAPI.ConnectBungeeServer(p,
                                    this.main.getConfig().getString("server-redirection")));
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
                }, 60 * 20L);
            }
        }

    }

    @Override
    public ModeManager getModeManager() {
        return null;
    }

    @Override
    public void onNight() {

    }

    @Override
    public void onDay() {

    }

    public SlaveMarketConfig getSlaveMarketConfig() {
        return slaveMarketConfig;
    }

    @Override
    public IQuickInventory getGui() {
        return new SlaveMarketGui(this);
    }

    @Override
    public String getCommandName() {
        return "sm";
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        List<ModeSubCommand> subCommands = new ArrayList<>();
        subCommands.add(new CmdBuy(main));
        subCommands.add(new CmdOwner(main));
        return subCommands;
    }
}
