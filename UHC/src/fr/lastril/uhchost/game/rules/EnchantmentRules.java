package fr.lastril.uhchost.game.rules;

import fr.lastril.uhchost.inventory.guis.rules.EnchantmentRulesGui;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;

public class EnchantmentRules {

    /*
     * Armor Rules
     *
     */


    /*
     * Diamond
     */
    //Protection
    private int diamondProtectionLimit;


    /*
     * Iron
     */
    //Protection
    private int IronProtectionLimit;

    private int depthStriderLimit;
    private int thornsLimit;

    /*
     * Sword Rules
     *
     */
    private int diamondSharpnessLimit;
    private int ironSharpnessLimit;

    private int knockbackLimit;
    private boolean autorisedFireAspect;

    /*
     * Bow Rules
     */
    private int powerLimit;

    private boolean autorisedFlame;
    private boolean autorisedInfinity;
    private int maxPunch;


    public IQuickInventory getGui(){
        return new EnchantmentRulesGui(this);
    }

    public int getDepthStriderLimit() {
        return depthStriderLimit;
    }

    public void setDepthStriderLimit(int depthStriderLimit) {
        this.depthStriderLimit = depthStriderLimit;
    }

    public int getThornsLimit() {
        return thornsLimit;
    }

    public void setThornsLimit(int thornsLimit) {
        this.thornsLimit = thornsLimit;
    }

    public int getDiamondSharpnessLimit() {
        return diamondSharpnessLimit;
    }

    public void setDiamondSharpnessLimit(int diamondSharpnessLimit) {
        this.diamondSharpnessLimit = diamondSharpnessLimit;
    }

    public int getIronSharpnessLimit() {
        return ironSharpnessLimit;
    }

    public void setIronSharpnessLimit(int ironSharpnessLimit) {
        this.ironSharpnessLimit = ironSharpnessLimit;
    }

    public int getKnockbackLimit() {
        return knockbackLimit;
    }

    public void setKnockbackLimit(int knockbackLimit) {
        this.knockbackLimit = knockbackLimit;
    }

    public boolean isAutorisedFireAspect() {
        return autorisedFireAspect;
    }

    public void setAutorisedFireAspect(boolean autorisedFireAspect) {
        this.autorisedFireAspect = autorisedFireAspect;
    }

    public int getPowerLimit() {
        return powerLimit;
    }

    public void setPowerLimit(int powerLimit) {
        this.powerLimit = powerLimit;
    }

    public boolean isAutorisedFlame() {
        return autorisedFlame;
    }

    public void setAutorisedFlame(boolean autorisedFlame) {
        this.autorisedFlame = autorisedFlame;
    }

    public boolean isAutorisedInfinity() {
        return autorisedInfinity;
    }

    public void setAutorisedInfinity(boolean autorisedInfinity) {
        this.autorisedInfinity = autorisedInfinity;
    }

    public int getMaxPunch() {
        return maxPunch;
    }

    public void setMaxPunch(int maxPunch) {
        this.maxPunch = maxPunch;
    }

    public int getDiamondProtectionLimit() {
        return diamondProtectionLimit;
    }

    public void setDiamondProtectionLimit(int diamondProtectionLimit) {
        this.diamondProtectionLimit = diamondProtectionLimit;
    }

    public int getIronProtectionLimit() {
        return IronProtectionLimit;
    }

    public void setIronProtectionLimit(int ironProtectionLimit) {
        IronProtectionLimit = ironProtectionLimit;
    }
}
