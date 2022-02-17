package fr.lastril.uhchost.modes.tpg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.config.modes.TaupeGunConfig;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.TaupePlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class TaupeGunMode extends Mode {
    
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
            for(Team team : main.teamUtils.getAllTeamsAlives()){
                List<Player> players = main.teamUtils.getPlayersInTeam(team);
                List<PlayerManager> playerManagers = new ArrayList<>();
                players.forEach(player -> playerManagers.add(main.getPlayerManager(player.getUniqueId())));
                PlayerManager playerManager = playerManagers.get(UhcHost.getRANDOM().nextInt(playerManagers.size()));
                TaupePlayerManager taupePlayerManager = playerManager.getTaupePlayerManager();
                for(TaupeTeams teams : TaupeTeams.values()){
                    if(!teams.isSuperTeamTaupe() && teams.getTeams().getPlayers().size() < 3){
                        teams.getTeams().addPlayerInTeam(playerManager);
                        taupePlayerManager.setOriginalTeam(main.teamUtils.getTeams(playerManager.getPlayer()));
                        taupePlayerManager.setMoleTeam(teams.getTeams());
                        break;
                    }
                }
                playerManager.getPlayer().sendMessage("§8§m--------------------------------------------------§r");
                playerManager.getPlayer().sendMessage("§c");
                playerManager.getPlayer().sendMessage("§cVous êtes Taupe, votre but est de trahir votre team est de rejoindre vos nouveaux coéquipier.");
                playerManager.getPlayer().sendMessage("§cVotre kit est : §6 <kit>§c.");
                playerManager.getPlayer().sendMessage("§e/t <message> :§c Permet de communiquer avec les autres taupes de ton équipe.");
                playerManager.getPlayer().sendMessage("§e/reveal :§c Permet de te révéler aux yeux de tous que tu es une taupe. Une pomme d'or te sera donné.");
                playerManager.getPlayer().sendMessage("§c");
                playerManager.getPlayer().sendMessage("§8§m--------------------------------------------------§r");
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
}
