package fr.lastril.uhchost.modes.tpg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.config.modes.TaupeGunConfig;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class TaupeGunMode extends Mode {
    
    private final UhcHost main;
    private final TaupeGunManager taupeGunManager;
    private final TaupeGunConfig taupeGunConfig;
    
    public TaupeGunMode() {
        super(Modes.TAUPEGUN);
        this.main = UhcHost.getInstance();
        this.taupeGunManager = new TaupeGunManager(this);
        this.taupeGunConfig = new TaupeGunConfig();
    }

    @Override
    public void tick(int timer) {
        if(timer == taupeGunConfig.getMolesTime()){

        }
    }

    public void annoucingMoles(){
        if(!taupeGunConfig.isApocalypseMoles()){
            for(Team team : main.teamUtils.getAllTeamsAlives()){
                List<Player> players = main.teamUtils.getPlayersInTeam(team);
                List<PlayerManager> playerManagers = new ArrayList<>();
                players.forEach(player -> playerManagers.add(main.getPlayerManager(player.getUniqueId())));
                PlayerManager playerManager = playerManagers.get(UhcHost.getRANDOM().nextInt(playerManagers.size()));

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
}
