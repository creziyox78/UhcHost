package fr.lastril.uhchost.config.modes;

import fr.lastril.uhchost.UhcHost;

import java.io.Serializable;

public class TaupeGunConfig implements Serializable {


    private boolean superMoles, apocalypseMoles;

    private final UhcHost main = UhcHost.getInstance();

    private int molesTime, molesInApocalypse, molesPerTeams, molesTeamSize;

    public TaupeGunConfig(){
        this.molesTime = 1*60;
        this.superMoles = false;
        this.apocalypseMoles = false;
        this.molesInApocalypse = 15;
        this.molesTeamSize = 3;
    }

    public int getMolesPerTeams() {
        return molesPerTeams;
    }

    public void setMolesPerTeams(int molesPerTeams) {
        this.molesPerTeams = molesPerTeams;
    }

    public boolean isSuperMoles() {
        return superMoles;
    }

    public void setSuperMoles(boolean superMoles) {
        this.superMoles = superMoles;
    }

    public boolean isApocalypseMoles() {
        return apocalypseMoles;
    }

    public void setApocalypseMoles(boolean apocalypseMoles) {
        this.apocalypseMoles = apocalypseMoles;
    }

    public int getMolesTime() {
        return molesTime;
    }

    public void setMolesTime(int molesTime) {
        this.molesTime = molesTime;
    }

    public int getMolesInApocalypse() {
        return molesInApocalypse;
    }

    public void setMolesInApocalypse(int molesInApocalypse) {
        this.molesInApocalypse = molesInApocalypse;
    }

    public int getMolesTeamSize() {
        return molesTeamSize;
    }

    public void setMolesTeamSize(int molesTeamSize) {
        this.molesTeamSize = molesTeamSize;
    }

    public void setPreset(TaupePresets taupePresets){
        setMolesTeamSize(taupePresets.molesTeamSize);
        setMolesPerTeams(taupePresets.molesPerTeam);
        setSuperMoles(taupePresets.superMoles);
        main.teamUtils.setPlayersPerTeams(taupePresets.teamOf);
        main.getGamemanager().setMaxPlayers(taupePresets.getSlots());
    }


    public enum TaupePresets{
        SLOTS_12(12,4, 3, 1, false),
        SLOTS_20_V1(20, 5, 4, 1, false),
        SLOTS_24_V1(24, 4, 3, 1, false),
        SLOTS_36_V1(36, 4, 3, 1, false),
        SLOTS_48_V1(48, 4, 3, 1, false),


        ;

        private final int teamOf, molesTeamSize, molesPerTeam, slots;
        private final boolean superMoles;

        TaupePresets(int slots, int teamOf, int molesTeamSize, int molesPerTeam, boolean superMoles) {
            this.teamOf = teamOf;
            this.slots = slots;
            this.molesTeamSize = molesTeamSize;
            this.molesPerTeam = molesPerTeam;
            this.superMoles = superMoles;
        }

        public int getMolesTeamSize() {
            return molesTeamSize;
        }

        public int getMolesPerTeam() {
            return molesPerTeam;
        }

        public int getTeamOf() {
            return teamOf;
        }

        public boolean isSuperMoles() {
            return superMoles;
        }

        public int getSlots() {
            return slots;
        }
    }

}
