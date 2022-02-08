package fr.lastril.uhchost.modes.ds.speciality.blades;

import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.inventory.ItemStack;

public abstract class Blades {

    public PlayerManager playerManager;

    public ItemStack itemStack;

    public int durablity;

    public boolean attribute;

    private boolean equiped;

    public Blades(PlayerManager playerManager) {
        this.playerManager = playerManager;
        this.durablity = 30;
        this.attribute = false;
    }

    public abstract ItemStack getItem();

    public abstract void onEquip();

    public abstract void onUnEquipOrBreak();

    public abstract BladeType getType();

    public void setDurability(int durablity) {
        this.durablity = durablity;
    }

    public void removeDurability() {
        if (this.durablity != 1000)
            this.durablity--;
    }

    public int getDurablity() {
        return this.durablity;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public boolean isAttribute() {
        return this.attribute;
    }

    public void setAttribute(boolean attribute) {
        this.attribute = attribute;
    }

    public boolean isEquiped() {
        return this.equiped;
    }

    public void setEquiped(boolean equiped) {
        this.equiped = equiped;
    }

}
