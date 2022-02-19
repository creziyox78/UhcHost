package fr.lastril.uhchost.modes.tpg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.config.modes.TaupeGunConfig;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.inventory.guis.modes.tpg.TaupeGunGui;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.TaupePlayerManager;
import fr.lastril.uhchost.tools.API.BungeeAPI;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.world.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class TaupeGunMode extends Mode implements ModeConfig {
    
    private final UhcHost main;
    private final TaupeGunConfig taupeGunConfig;
    
    public TaupeGunMode() {
        super(Modes.TAUPEGUN);
        this.main = UhcHost.getInstance();
        this.taupeGunConfig = new TaupeGunConfig();
    }

    @Override
    public void tick(int timer) {
        if(timer == taupeGunConfig.getMolesTime()){
            annoucingMoles();
            Bukkit.broadcastMessage(Messages.TAUPE_GUN_PREFIX.getMessage() + "§cLes Taupes ont été annoncés, gare à vous !");
        }
    }

    public void annoucingMoles(){
        if(!taupeGunConfig.isApocalypseMoles()){
            for(int i = 0; i < taupeGunConfig.getMolesPerTeams(); i++){
                for(Team team : main.teamUtils.getAllTeamsAlives()){
                    List<Player> players = main.teamUtils.getPlayersInTeam(team);
                    List<PlayerManager> playerManagers = new ArrayList<>();
                    players.forEach(player -> {
                        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                        if(playerManager.getTaupePlayerManager().getMoleTeam() == null){
                            playerManagers.add(playerManager);
                        }
                    });
                    PlayerManager playerManager = playerManagers.get(UhcHost.getRANDOM().nextInt(playerManagers.size()));
                    TaupePlayerManager taupePlayerManager = playerManager.getTaupePlayerManager();
                    for(TaupeTeams teams : TaupeTeams.values()){
                        if(!teams.isSuperTeamTaupe() && teams.getTeams().getPlayers().size() < taupeGunConfig.getMolesTeamSize()){
                            teams.getTeams().addPlayerInTeam(playerManager);
                            taupePlayerManager.setOriginalTeam(main.teamUtils.getTeams(playerManager.getPlayer()));
                            taupePlayerManager.setMoleTeam(teams.getTeams());
                            break;
                        }
                    }
                    playerManager.getPlayer().sendMessage("§8§m--------------------------------------------------§r");
                    playerManager.getPlayer().sendMessage("§c");
                    playerManager.getPlayer().sendMessage("§cVous êtes Taupe, votre but est de trahir votre team est de rejoindre vos nouveaux coéquipier.");
                    playerManager.getPlayer().sendMessage("§cVotre kit est : §6 " + taupePlayerManager.getKitTaupe().getName() + "§c.");
                    playerManager.getPlayer().sendMessage("§e/claim :§c Permet de récupérer votre kit.");
                    playerManager.getPlayer().sendMessage("§e/t <message> :§c Permet de communiquer avec les autres taupes de ton équipe.");
                    playerManager.getPlayer().sendMessage("§e/reveal :§c Permet de te révéler aux yeux de tous que tu es une taupe. Une pomme d'or te sera donné.");
                    playerManager.getPlayer().sendMessage("§c");
                    playerManager.getPlayer().sendMessage("§8§m--------------------------------------------------§r");
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

    @Override
    public void onDeath(OfflinePlayer player, Player killer) {
        if(killer != null){
            PlayerManager killerManager = main.getPlayerManager(killer.getUniqueId());
            killerManager.addKill(player.getUniqueId());
        }
        for(Player players : Bukkit.getOnlinePlayers()){
            WorldUtils.spawnFakeLightning(players, player.getPlayer().getLocation(), true);
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
            } else if(main.teamUtils.getTaupes(player.getPlayer()) != null){
                if(main.teamUtils.getTaupes(player.getPlayer()).getTeam() != null){
                    Bukkit.broadcastMessage(Messages.PREFIX_WITH_ARROW.getMessage() + main.teamUtils.getTaupes(player.getPlayer()).getTeam().getPrefix() + player.getName() + " §cest mort.");
                }
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

    @Override
    public boolean isScheduledDeath() {
        return false;
    }

    @Override
    public boolean isEpisodeMode() {
        return true;
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

    public TaupeGunConfig getTaupeGunConfig() {
        return taupeGunConfig;
    }

    @Override
    public IQuickInventory getGui() {
        return new TaupeGunGui(taupeGunConfig);
    }
}
