package fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleListener;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.FluteDemoniaque;
import fr.maygo.uhc.modes.naruto.v2.items.MarqueMauditeItem;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Tayuya extends Role implements NarutoV2Role, RoleListener, MarqueMauditeItem.MarqueMauditeUser {

    public Tayuya() {
        super.addRoleToKnow(Kimimaro.class);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getEntity() instanceof IronGolem) {
            IronGolem golem = (IronGolem) event.getEntity();
            if (golem.getCustomName().equalsIgnoreCase("§dServant de Tayuya")) {
                if (golem.getTarget() != null) {
                    if (golem.getTarget() instanceof Player) {
                        PlayerManager PlayerManager = main.getPlayerManager(golem.getTarget().getUniqueId());
                        if (PlayerManager.hasRole()) {
                            if (PlayerManager.getRole() instanceof Tayuya) {
                                golem.setTarget(null);
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }

        } else if (event.getEntity() instanceof Endermite) {
            Endermite endermite = (Endermite) event.getEntity();
            if (endermite.getCustomName().equalsIgnoreCase("§dServant de Tayuya")) {
                if (endermite.getTarget() != null) {
                    if (endermite.getTarget() instanceof Player) {
                        PlayerManager PlayerManager = main.getPlayerManager(endermite.getTarget().getUniqueId());
                        if (PlayerManager.hasRole()) {
                            if (PlayerManager.getRole() instanceof Tayuya) {
                                endermite.setTarget(null);
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }

        }
    }

    @EventHandler
    public void onSilverFishDamageSai(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (event.getDamager() instanceof IronGolem) {
                IronGolem ironGolem = (IronGolem) event.getDamager();
                if (PlayerManager.hasRole()) {
                    if (PlayerManager.getRole() instanceof Tayuya) {
                        if (ironGolem.getCustomName().equalsIgnoreCase("§dServant de Tayuya")) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
            if (event.getDamager() instanceof Endermite) {
                Endermite endermite = (Endermite) event.getDamager();
                if (PlayerManager.hasRole()) {
                    if (PlayerManager.getRole() instanceof Tayuya) {
                        if (endermite.getCustomName().equalsIgnoreCase("§dServant de Tayuya")) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }


    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new FluteDemoniaque(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new MarqueMauditeItem(main).toItemStack());
        player.setWalkSpeed((float) 0.21);
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
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTYyMjVmZTUxMTFkZjYzYTM5NjNmZDIzODI4YmY4OTYwZGNhODNmMThhNmYwN2UyZmRjZDFjZDk3NzMwZTgzZCJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.OROCHIMARU;
    }

    @Override
    public String getRoleName() {
        return "Tayuya";
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

    @Override
    public void onUseMarqueMaudite(UhcHost main, Player player, PlayerManager PlayerManager) {
        if (player.getMaxHealth() - 2D <= 0) {
            player.sendMessage(Messages.error("Vous n'avez plus assez de coeurs permanents."));
        } else {
            if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 60 * 5, 0, false, false));
            PlayerManager.setRoleCooldownMarqueMaudite(60 * 10);
            PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownMarqueMaudite(), player.getItemInHand());
            new BukkitRunnable() {

                @Override
                public void run() {

                    player.setMaxHealth(player.getMaxHealth() - (2D * 1));
                }
            }.runTaskLater(main, 20 * 60 * 5);
            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
        }

    }
}
