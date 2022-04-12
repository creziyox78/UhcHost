package fr.lastril.uhchost.modes.bleach.roles.arrancars.espada;

import fr.lastril.uhchost.modes.bleach.items.Okami;
import fr.lastril.uhchost.modes.bleach.roles.ArrancarRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Stark extends Role implements ArrancarRole {

    private boolean lilynetteDead;

    public Stark(){
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Okami(main).toItemStack());
        PlayerManager pm = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = pm.getBleachPlayerManager();
        if(bleachPlayerManager.isInFormeLiberer()){

        }
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

    }

    @Override
    public void onUnTransformationSecond() {

    }

    public boolean isLilynetteDead() {
        return lilynetteDead;
    }

    public void setLilynetteDead(boolean lilynetteDead) {
        this.lilynetteDead = lilynetteDead;
    }
}
