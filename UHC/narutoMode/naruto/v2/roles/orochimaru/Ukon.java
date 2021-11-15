package fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.BarriereItem;
import fr.maygo.uhc.modes.naruto.v2.items.MarqueMauditeSakonUkonItem;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Ukon extends Role implements NarutoV2Role {

    public Ukon() {
        super.addRoleToKnow(Kimimaro.class);
        super.addRoleToKnow(Sakon.class);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new MarqueMauditeSakonUkonItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new BarriereItem(main).toItemStack());
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);

        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.isAlive()) {
            for (PlayerManager targetPlayerManager : main.getNarutoV2Manager().getPlayerManagersWithRole(Sakon.class)) {
                if (targetPlayerManager.isAlive()) {
                    Player target = targetPlayerManager.getPlayer();
                    if (target != null) {
                        if (player.getWorld() == target.getWorld()) {
                            if (player.getLocation().distance(target.getLocation()) <= 15) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 4, 0, false, false));
                            }
                        }

                    }
                }
            }
        }
    }

    @Override
    public void onDead(PlayerDeathEvent event) {
        Player player = event.getEntity();
        for (PlayerManager targetPlayerManager : main.getNarutoV2Manager().getPlayerManagersWithRole(Sakon.class)) {
            if (targetPlayerManager.isAlive()) {
                Player target = targetPlayerManager.getPlayer();
                if (target != null) {
                    if (player.getLocation().distance(target.getLocation()) <= 15) {
                        target.damage(100);
                    }
                }
            }
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName())
                .setTexture(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU0M2ZjYTExNjYzMzAwOGY0NjMyYWEyYzgzMDVjNjM5YTBjYzY1YmU2OGIxZTAyMzFjNTE0YzRmMTM0Nzg4NiJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.OROCHIMARU;
    }

    @Override
    public String getRoleName() {
        return "Ukon";
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
}
