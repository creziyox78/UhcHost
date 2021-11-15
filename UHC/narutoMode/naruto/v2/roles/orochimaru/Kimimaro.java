package fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleListener;
import fr.maygo.uhc.modes.crafter.role.crafter.When;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.MarqueMauditeItem;
import fr.maygo.uhc.modes.naruto.v2.items.ShikotsumyakuItem;
import fr.maygo.uhc.modes.naruto.v2.items.swords.EpeeOsItem;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Kimimaro extends Role implements NarutoV2Role, MarqueMauditeItem.MarqueMauditeUser, RoleListener {

    private int epeeOsUses;
    private boolean usedForetOs;

    public Kimimaro() {
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addRoleToKnow(Sakon.class, Ukon.class, Kidomaru.class, Tayuya.class, Jirobo.class);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new MarqueMauditeItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new ShikotsumyakuItem(main).toItemStack());
    }

    @Override
    public void afterRoles(Player player) {
        player.setMaxHealth(24);
        player.setHealth(player.getMaxHealth());
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWFiMjMxMTU4NDU1ZThiNDlhNzUxZWQ3YTBlM2U1NWM0MWZkZWJmODRlOWRhMGQwMGE3ZDY1ZDRkN2U2ZTU2YiJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.OROCHIMARU;
    }

    @Override
    public String getRoleName() {
        return "Kimimaro";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    @Override
    public Chakra getChakra() {
        return null;
    }

    /**
     * @param main
     * @param player
     * @param PlayerManager
     * @author Maygo
     */
    @Override
    public void onUseMarqueMaudite(UhcHost main, Player player, PlayerManager PlayerManager) {

        if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 60 * 2, 0, false, false));

        if (player.hasPotionEffect(PotionEffectType.SPEED))
            player.removePotionEffect(PotionEffectType.SPEED);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 60 * 7, 0, false, false));

        if (player.hasPotionEffect(PotionEffectType.REGENERATION))
            player.removePotionEffect(PotionEffectType.REGENERATION);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 60 * 2, 0, false, false));

        player.setMaxHealth(player.getMaxHealth() + (3D * 2D));
        player.setHealth(player.getHealth() + (3D * 2D));

        PlayerManager.setRoleCooldownMarqueMaudite(60 * 20);
        PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownMarqueMaudite(), player.getItemInHand());
        new BukkitRunnable() {

            @Override
            public void run() {
                if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));


                if (player.hasPotionEffect(PotionEffectType.WEAKNESS))
                    player.removePotionEffect(PotionEffectType.WEAKNESS);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 60 * 5, 0, false, false));

                player.setMaxHealth(player.getMaxHealth() - (3D * 2D));

                new BukkitRunnable() {

                    @Override
                    public void run() {
                        Kimimaro.super.givePermanentEffects(player);
                    }
                }.runTaskLater(main, 20 * 60 * 5);
            }
        }.runTaskLater(main, 20 * 60 * 2);

        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
    }

    public int getEpeeOsUses() {
        return epeeOsUses;
    }

    public void addEpeeOsUse() {
        this.epeeOsUses++;
    }

    public boolean hasUsedForetOs() {
        return usedForetOs;
    }

    public void setUsedForetOs(boolean usedForetOs) {
        this.usedForetOs = usedForetOs;
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        ItemStack os = new QuickItem(Material.BONE).setName("§cEpée en os").toItemStack();
        ItemStack epee = new EpeeOsItem(os).getItem();
        if (event.getItemDrop().getItemStack().isSimilar(new EpeeOsItem(epee).getItem())) {
            event.setCancelled(true);
        }
    }
}
