package fr.lastril.uhchost.modes.bleach.roles.arrancars.espada;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.ceros.AbstractCero;
import fr.lastril.uhchost.modes.bleach.ceros.CeroFaible;
import fr.lastril.uhchost.modes.bleach.ceros.CeroType;
import fr.lastril.uhchost.modes.bleach.items.Okami;
import fr.lastril.uhchost.modes.bleach.roles.ArrancarRole;
import fr.lastril.uhchost.modes.bleach.roles.CeroUser;
import fr.lastril.uhchost.modes.bleach.roles.arrancars.fraccions.Lilynette;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Stark extends Role implements ArrancarRole, CeroUser {

    private boolean lilynetteDead;
    private int ceroUsed;

    public Stark(){
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Okami(main).toItemStack());
    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    protected void onDay(Player player) {

    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {

    }

    @Override
    public QuickItem getItem() {
        return null;
    }

    @Override
    public Camps getCamp() {
        return Camps.ARRANCARS;
    }

    @Override
    public String getRoleName() {
        return "Stark";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @Override
    public int getNbQuartz() {
        return 50;
    }

    @Override
    public void onTransformationFirst() {
        Player stark = super.getPlayer();
        if(stark != null) {
            PlayerManager playerManager = main.getPlayerManager(stark.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            bleachPlayerManager.setStrengthPourcentage(10);
        }
    }

    @Override
    public void onUnTransformationFirst() {
        Player stark = super.getPlayer();
        if(stark != null) {
            PlayerManager playerManager = main.getPlayerManager(stark.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            bleachPlayerManager.setStrengthPourcentage(0);
        }
    }

    @Override
    public void onPlayerDeath(Player player) {
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.getRole() instanceof Lilynette) {
            this.setLilynetteDead(true);
            Player stark = super.getPlayer();
            if(stark != null) {
                if(stark.hasPotionEffect(PotionEffectType.SPEED))
                    stark.removePotionEffect(PotionEffectType.SPEED);
                stark.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
                if(stark.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                    stark.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                stark.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
                stark.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "Lilynette est morte, vous ne pouvez plus utiliser l’item “Okami” mais recevez désormais les effets Speed  et Force 1 permanent");
            }
            super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
            super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        }
    }

    public boolean isLilynetteDead() {
        return lilynetteDead;
    }

    public void setLilynetteDead(boolean lilynetteDead) {
        this.lilynetteDead = lilynetteDead;
    }

    @Override
    public boolean canUseCero(CeroType ceroType) {
        Player stark = super.getPlayer();
        if(stark != null) {
            PlayerManager playerManager = main.getPlayerManager(stark.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(bleachPlayerManager.isInFormeLiberer() && playerManager.getRoleCooldownCeroFaible() <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onUseCero(CeroType ceroType) {
        if(this.ceroUsed == 5) {
            Player stark = super.getPlayer();
            if(stark != null) {
                PlayerManager playerManager = main.getPlayerManager(stark.getUniqueId());
                playerManager.setRoleCooldownCeroFaible(5);
            }
            this.ceroUsed = 0;
        }
        if(this.ceroUsed < 5) {
            this.ceroUsed++;
        }
    }

    @Override
    public int getCeroRedValue() {
        return -255;
    }

    @Override
    public int getCeroGreenValue() {
        return 0;
    }

    @Override
    public int getCeroBlueValue() {
        return 255;
    }

    @Override
    public List<AbstractCero> getCero() {
        return Arrays.asList(new CeroFaible());
    }



}
