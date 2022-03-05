package fr.lastril.uhchost.modes.tpg;

import fr.lastril.uhchost.inventory.scoreboard.TeamUtils;

public enum TaupeTeams {

    TAUPE1(new TaupeTeam("Taupe 1", "§6T1", TeamUtils.TeamsTaupes.TAUPE1),false),
    TAUPE2(new TaupeTeam("Taupe 2", "§6T2", TeamUtils.TeamsTaupes.TAUPE2),false),
    TAUPE3(new TaupeTeam("Taupe 3", "§6T3", TeamUtils.TeamsTaupes.TAUPE3),false),
    TAUPE4(new TaupeTeam("Taupe 4", "§6T4", TeamUtils.TeamsTaupes.TAUPE4),false),
    ST1(new TaupeTeam("SuperTaupe 1", "§CST1", TeamUtils.TeamsTaupes.SUPERTAUPE1),true),
    ST2(new TaupeTeam("SuperTaupe 2", "§CST2", TeamUtils.TeamsTaupes.SUPERTAUPE2),true),
    ST3(new TaupeTeam("SuperTaupe 3", "§CST3", TeamUtils.TeamsTaupes.SUPERTAUPE3),true),
    ST4(new TaupeTeam("SuperTaupe 4", "§CST4", TeamUtils.TeamsTaupes.SUPERTAUPE4),true),
    ;

    private final TaupeTeam teams;
    private final boolean superTeamTaupe;

    TaupeTeams(TaupeTeam teams, boolean superTeamTaupe) {
        this.teams = teams;
        this.superTeamTaupe = superTeamTaupe;
    }

    public boolean isSuperTeamTaupe() {
        return superTeamTaupe;
    }

    public TaupeTeam getTeams() {
        return teams;
    }
}
