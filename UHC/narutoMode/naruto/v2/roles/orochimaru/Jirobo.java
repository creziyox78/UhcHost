package fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.events.player.PlayerKillEvent;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleListener;
import fr.maygo.uhc.modes.crafter.role.crafter.When;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.MarqueMauditeItem;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Jirobo extends Role implements NarutoV2Role, RoleListener, MarqueMauditeItem.MarqueMauditeUser {

    private boolean useMarqueMaudite, hasKill;

    private int durationHunger = 0;

    public Jirobo() {
        super.addRoleToKnow(Kimimaro.class);
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false),
                When.START);
    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {
        Player player = event.getPlayer();
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Jirobo) {
                Jirobo jirobo = (Jirobo) PlayerManager.getRole();
                if (jirobo.isUseMarqueMaudite()) {
                    if (!jirobo.isHasKill()) {
                        jirobo.setHasKill(true);
                        for (PotionEffect effect : player.getActivePotionEffects()) {
                            if (player.hasPotionEffect(PotionEffectType.HUNGER)) {
                                durationHunger = effect.getDuration();
                                player.removePotionEffect(PotionEffectType.HUNGER);
                            }
                        }
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage()
                                + "§6Vous venez de tuer quelqu'un. Vous perdez pendant 1 minute votre effet d'Hunger.");
                        player.setFoodLevel(20);
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, (durationHunger - 60 * 20),
                                        0, false, false));
                                jirobo.setHasKill(false);
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage()
                                        + "§6L'effet d'Hunger revient pendant la durée de votre pouvoir restant.");
                            }
                        }.runTaskLater(main, 20 * 60);
                    }
                }

            }
        }
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new MarqueMauditeItem(main).toItemStack());
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                .setName(getCamp().getCompoColor() + getRoleName()).setTexture(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTM0ZWIxNDk1YjE4ZjI5MGIzMGJmYjRjZTUxZDIyZTYwZDAwOGQ5OTBhOTk4MzUwZjE0NGE2MzQwYjE3ZWM3ZSJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.OROCHIMARU;
    }

    @Override
    public String getRoleName() {
        return "Jirôbô";
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
        return Chakra.DOTON;
    }

    public boolean isUseMarqueMaudite() {
        return useMarqueMaudite;
    }

    public void setUseMarqueMaudite(boolean useMarqueMaudite) {
        this.useMarqueMaudite = useMarqueMaudite;
    }

    public boolean isHasKill() {
        return hasKill;
    }

    public void setHasKill(boolean hasKill) {
        this.hasKill = hasKill;
    }

    @Override
    public void onUseMarqueMaudite(UhcHost main, Player player, PlayerManager PlayerManager) {
        if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);

        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60 * 5, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 20 * 60 * 5, 0, false, false));
        PlayerManager.setRoleCooldownMarqueMaudite(60 * 30);
        PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownMarqueMaudite(), player.getItemInHand());
        useMarqueMaudite = true;

        new BukkitRunnable() {

            @Override
            public void run() {
                if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
                player.setHealth(player.getMaxHealth() - (2D * 2D));
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        player.setHealth(player.getMaxHealth() + (2D * 2D));
                    }
                }.runTaskLater(main, 20 * 60 * 15);
            }
        }.runTaskLater(main, 20 * 60 * 5);
        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
    }
}
