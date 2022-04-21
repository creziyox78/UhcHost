package fr.lastril.uhchost.modes.bleach.roles.arrancars.espada;

import fr.lastril.uhchost.modes.bleach.ceros.AbstractCero;
import fr.lastril.uhchost.modes.bleach.ceros.CeroFaible;
import fr.lastril.uhchost.modes.bleach.items.Okami;
import fr.lastril.uhchost.modes.bleach.roles.ArrancarRole;
import fr.lastril.uhchost.modes.bleach.roles.CeroUser;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Stark extends Role implements ArrancarRole, CeroUser {

    private boolean lilynetteDead;

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

    @Override
    public boolean canUseCero() {
        Player stark = super.getPlayer();
        if(stark != null) {
            PlayerManager playerManager = main.getPlayerManager(stark.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(bleachPlayerManager.isInFormeLiberer()) {
                return true;
            }
        }
        return false;
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
    public AbstractCero getCero() {
        return new CeroFaible();
    }
}
