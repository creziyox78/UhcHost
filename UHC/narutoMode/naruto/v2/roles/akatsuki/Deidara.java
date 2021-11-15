package fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.BakutonItem;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Deidara extends Role implements NarutoV2Role, RoleListener {

    private final List<Integer> explosivesArrows;

    private BowMode bowMode = BowMode.C1;

    private boolean artUltime, cancelArtUltimeExplosion;

    public Deidara() {
        super.addRoleToKnow(Sasori.class);
        this.explosivesArrows = new ArrayList<>();
    }

    @Override
    public void giveItems(Player player) {
        QuickItem arc = new QuickItem(Material.BOW);
        arc.setName("§c" + getRoleName());

        main.getInventoryUtils().giveItemSafely(player, new BakutonItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, arc.toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new ItemStack(Material.ARROW, 32));
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName())
                .setTexture(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjJmMGIyMDMzM2ZmOTJkMWZjZTNiYTcxN2U1ODIyMDRlNjlkODg1NmQ4ZDY1YTJmYTZlODNmMDY1YWVmOTVlYyJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.AKATSUKI;
    }

    @Override
    public String getRoleName() {
        return "Deidara";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onDead(PlayerDeathEvent event) {
        if (this.artUltime) this.cancelArtUltimeExplosion = true;
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager playerManager = this.main.getPlayerManager(player.getUniqueId());
            if (playerManager.hasRole()) {
                if (playerManager.getRole() instanceof Deidara) {
                    ItemStack bow = event.getBow();
                    if (bow.hasItemMeta()) {
                        if (bow.getItemMeta().hasDisplayName() && bow.getItemMeta().getDisplayName().equalsIgnoreCase("§c" + getRoleName())) {
                            if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                                if (bowMode == BowMode.C1) {
                                    if (playerManager.getRoleCooldownC1() > 0) {
                                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownC1()));
                                        return;
                                    } else {
                                        playerManager.setRoleCooldownC1(bowMode.getCooldown());
                                        playerManager.sendTimer(player, playerManager.getRoleCooldownC1(), bow);
                                    }
                                } else if (bowMode == BowMode.C3) {
                                    if (playerManager.getRoleCooldownC3() > 0) {
                                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownC3()));
                                        return;
                                    } else {
                                        playerManager.setRoleCooldownC3(bowMode.getCooldown());
                                    }
                                }
                                this.explosivesArrows.add(event.getProjectile().getEntityId());
                            } else {
                                player.sendMessage(
                                        Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        ItemStack item = event.getItem();
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§c" + getRoleName())) {
                event.setCancelled(true);
                player.closeInventory();
                player.sendMessage(Messages.error("Vous ne pouvez pas enchanter votre Deidara !"));
            }
        }
    }

    @EventHandler
    public void onProjectileHitGround(ProjectileHitEvent event) {
        if (this.explosivesArrows.contains(event.getEntity().getEntityId())) {
            Location explosion = event.getEntity().getLocation();
            final World world = explosion.getWorld();
            if (this.bowMode == BowMode.C3) {
                for (int x = -20; x <= 20; x += 5) {
                    for (int z = -20; z <= 20; z += 5) {
                        world.spawn(new Location(world, explosion.getBlockX() + x, world.getHighestBlockYAt(explosion) + 64, explosion.getBlockZ() + z), TNTPrimed.class);
                    }
                }
            } else if (bowMode == BowMode.C1) {
                world.createExplosion(explosion, 3.0f);
            }

            this.explosivesArrows.remove(new Integer(event.getEntity().getEntityId()));
        }
    }


    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    public BowMode getBowMode() {
        return bowMode;
    }

    public void setBowMode(BowMode bowMode) {
        this.bowMode = bowMode;
    }

    public double getPlayersDistance() {
        return 20;
    }

    public boolean isArtUltime() {
        return artUltime;
    }

    public void setArtUltime(boolean artUltime) {
        this.artUltime = artUltime;
    }

    public boolean isCancelArtUltimeExplosion() {
        return cancelArtUltimeExplosion;
    }

    public void setCancelArtUltimeExplosion(boolean cancelArtUltimeExplosion) {
        this.cancelArtUltimeExplosion = cancelArtUltimeExplosion;
    }

    @Override
    public Chakra getChakra() {
        return null;
    }

    public enum BowMode {

        C1(15),
        C3(5 * 60);

        private final int cooldown;

        BowMode(int cooldown) {
            this.cooldown = cooldown;
        }

        public int getCooldown() {
            return cooldown;
        }
    }
}
