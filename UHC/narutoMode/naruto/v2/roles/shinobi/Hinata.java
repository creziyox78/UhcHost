package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.When;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.ByakuganItem;
import fr.maygo.uhc.modes.naruto.v2.items.HakkeItem;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class
Hinata extends Role implements NarutoV2Role {

    private final int DISTANCE = 20;
    private final double distanceNaruto = 15;
    private boolean closeToNaruto;
    private boolean nejiDead;

    public Hinata() {
        closeToNaruto = false;
        super.addRoleToKnow(Naruto.class);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new ByakuganItem().toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new HakkeItem().toItemStack());
    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    protected void onDay(Player player) {

    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Neji) {
                for (PlayerManager PlayerManagers : main.getNarutoV2Manager().getPlayerManagersWithRole(Hinata.class)) {
                    if (PlayerManagers.getPlayer() != null) {
                        Player hinata = PlayerManagers.getPlayer();
                        if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                        }
                        hinata.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
                        hinata.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aNeji§3 vient de mourir, vous avez§c Force I§3 de façon permanent.");
                        nejiDead = true;
                    }
                }
            }
        }
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        if (nejiDead) {
            if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 4, 0, false, false));
        }
        for (PlayerManager targetPlayerManager : UhcHost.getInstance().getNarutoV2Manager().getPlayerManagersWithRole(Naruto.class)) {
            if (targetPlayerManager.getPlayer() != null && targetPlayerManager.isAlive()) {
                Player naruto = targetPlayerManager.getPlayer();
                PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
                if (PlayerManager.isAlive() && PlayerManager.hasRole()) {
                    if (PlayerManager.getRole() instanceof Hinata) {
                        if (player.getWorld() == naruto.getWorld()) {
                            if (player.getLocation().distance(naruto.getLocation()) <= distanceNaruto) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 4, 0, false, false));
                                closeToNaruto = true;
                            } else {
                                closeToNaruto = false;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDamage(Player damager, Player target) {
        PlayerManager targetPlayerManager = main.getPlayerManager(target.getUniqueId());
        if (targetPlayerManager.hasRole() && targetPlayerManager.getRole() instanceof Hinata) {
            if (closeToNaruto) {
                target.damage(target.getLastDamage() * 10 / 100);
            }
        }
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
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZjODg0MTA5MjFlMWNhNjc0MWI1NDljMjU0YmZhNGY0YzE1Y2NmMzU4YzhkNzRmMTY5Yzg3YWU4YTU4M2E2OSJ9fX0=");
    }

    @Override
    public void afterRoles(Player player) {
        if (!UhcHost.getInstance().getConfiguration().containsRoleInComposition(Neji.class)) {
            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aNeji§e ne fait pas partie de la composition. Vous venez de recevoir votre effet.");
            for (PlayerManager targetPlayerManager : UhcHost.getInstance().getNarutoManager().getPlayerManagersWithRole(Hinata.class)) {
                Hinata hinata = (Hinata) targetPlayerManager.getRole();
                hinata.getEffects().clear();
                hinata.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false),
                        When.START);
                if (targetPlayerManager.getPlayer() != null) {
                    Player target = targetPlayerManager.getPlayer();
                    if (!target.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                        target.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
                    }
                }
            }
        }
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Hinata";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {
        Player player = super.getPlayer();
        Player target = PlayerManager.getPlayer();

        if (player != null && target != null) {
            if (player.getWorld() == target.getWorld()) {
                double distance = player.getLocation().distance(target.getLocation());
                if (distance <= DISTANCE) {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§a" + PlayerManager.getRole().getRoleName()
                            + " vient d'utiliser une technique !");
                }
            }
        }
    }

    @Override
    public Chakra getChakra() {
        return Chakra.KATON;
    }
}
