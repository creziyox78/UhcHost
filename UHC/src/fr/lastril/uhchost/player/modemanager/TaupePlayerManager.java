package fr.lastril.uhchost.player.modemanager;

import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.scoreboard.TeamUtils;

public class TaupePlayerManager {

    private final PlayerManager playerManager;

    private TeamUtils.Teams moleTeam;

    public TaupePlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
        this.moleTeam = null;
    }

    public void setMoleTeam(TeamUtils.Teams moleTeam) {
        this.moleTeam = moleTeam;
    }

    public TeamUtils.Teams getMoleTeam() {
        return moleTeam;
    }
}
