package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.*;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdDeath;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.GyukiItem;
import fr.maygo.uhc.modes.naruto.v2.roles.BijuUser;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class KillerBee extends Role implements NarutoV2Role, BijuUser, RoleCommand, RoleListener {

    private boolean fakeDeath, useFakeDeath;

    public KillerBee() {
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void giveItems(Player player) {
        ItemStack t5 = new QuickItem(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 5, true).toItemStack();
        main.getInventoryUtils().giveItemSafely(player, new GyukiItem().toItemStack());
        main.getInventoryUtils().giveItemSafely(player, t5);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
            if (PlayerManager.getRole() instanceof KillerBee) {
                KillerBee killerBee = (KillerBee) PlayerManager.getRole();
                event.setCancelled(killerBee.fakeDeath);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBucket(PlayerBucketEmptyEvent event) {
        Player damager = event.getPlayer();
        if (event.getBucket() == Material.WATER_BUCKET)
            return;
        PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(damager.getUniqueId());
        if (PlayerManager.getRole() instanceof KillerBee) {
            KillerBee killerBee = (KillerBee) PlayerManager.getRole();
            event.setCancelled(killerBee.isFakeDeath());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                PlayerManager damagerPlayerManager = UhcHost.getInstance().getPlayerManager(damager.getUniqueId());
                if (damagerPlayerManager.getRole() instanceof KillerBee) {
                    KillerBee killerBee = (KillerBee) damagerPlayerManager.getRole();
                    if (killerBee.isFakeDeath()) {
                        event.setCancelled(true);
                    }
                }

            } else if (event.getDamager() instanceof Projectile) {
                Projectile damager = (Projectile) event.getDamager();
                if (damager.getShooter() instanceof Player) {
                    Player shooter = (Player) damager.getShooter();
                    PlayerManager damagerPlayerManager = main.getPlayerManager(shooter.getUniqueId());
                    if (damagerPlayerManager.getRole() instanceof KillerBee) {
                        KillerBee killerBee = (KillerBee) damagerPlayerManager.getRole();
                        if (killerBee.isFakeDeath()) {
                            event.setCancelled(true);
                        }
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWVmNzhmNDNkMDA4NjgxZGI2NGM0ZTYxMjU3MjE4NDNiMjY5OTJkOTQzMTI2NzkwNDM3NTg2MDFjZDcyZTc2NyJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Killer Bee";
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
        return Chakra.RAITON;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdDeath(main));
    }

    public boolean isFakeDeath() {
        return fakeDeath;
    }

    public void setFakeDeath(boolean fakeDeath) {
        this.fakeDeath = fakeDeath;
    }

    public boolean isUseFakeDeath() {
        return useFakeDeath;
    }

    public void setUseFakeDeath(boolean useFakeDeath) {
        this.useFakeDeath = useFakeDeath;
    }
}
