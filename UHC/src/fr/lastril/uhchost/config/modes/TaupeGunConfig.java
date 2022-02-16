package fr.lastril.uhchost.config.modes;

import java.io.Serializable;

public class TaupeGunConfig implements Serializable {


    private boolean superMoles, apocalypseMoles;

    private int molesTime, molesInApocalypse;

    public TaupeGunConfig(){
        this.molesTime = 20*60;
        this.superMoles = false;
        this.apocalypseMoles = false;
        this.molesInApocalypse = 15;
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
}
