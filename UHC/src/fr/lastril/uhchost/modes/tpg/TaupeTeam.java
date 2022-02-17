package fr.lastril.uhchost.modes.tpg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.scoreboard.TeamUtils;

import java.util.ArrayList;
import java.util.List;

public class TaupeTeam {

    private final String name;
    private final String prefix;
    private final List<PlayerManager> players;
    private final UhcHost main = UhcHost.getInstance();
    private final TeamUtils.TeamsTaupes teams;

    public TaupeTeam(String name, String prefix, TeamUtils.TeamsTaupes teams) {
        this.name = name;
        this.prefix = prefix;
        this.players = new ArrayList<>();
        this.teams = teams;
        main.teamUtils.registerTaupeTeam(teams);
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void addPlayerInTeam(PlayerManager playerManager){
        players.add(playerManager);
    }

    public void updateDisplayPlayer(PlayerManager playerManager){
        main.teamUtils.setTeamTaupe(playerManager.getPlayer(), teams.getTeam());
        main.teamUtils.unsetTeam(playerManager.getPlayer(), playerManager.getTaupePlayerManager().getOriginalTeam().getTeam());
    }

    public List<PlayerManager> getPlayers() {
        return players;
    }
}
