package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdSmell;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.KuramaItem;
import fr.lastril.uhchost.modes.naruto.v2.items.ShurikenJutsuItem;
import fr.lastril.uhchost.modes.naruto.v2.roles.BijuUser;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Minato extends Role implements NarutoV2Role, KuramaItem.KuramaUser, RoleCommand, RoleListener, BijuUser {

    private boolean canUseKurama;

    /**
     * ShurikenJutsu Arc
     */
    private Player toTp;
    private int lastProjectileID = -1;

    /**
     * ShurikenJutsu Item
     */
    public static final int BALISES_MAX = 5;
    public static final int DISTANCE_SHURIKENJUTSU = 20;
    private final List<Location> balises;

    public Minato() {
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false), When.START);
        super.addRoleToKnow(Naruto.class);
        this.balises = new ArrayList<>();
    }

    @Override
    public void giveItems(Player player) {
        QuickItem arc = new QuickItem(Material.BOW);
        arc.setName("§cShurikenJutsu");
        main.getInventoryUtils().giveItemSafely(player, arc.toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new ItemStack(Material.ARROW, 32));

        main.getInventoryUtils().giveItemSafely(player, new ShurikenJutsuItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new KuramaItem(main).toItemStack());
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
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

    @Override
    public void onKill(OfflinePlayer player, Player killer) {
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if (joueur.getCamps() != main.getPlayerManager(killer.getUniqueId()).getCamps()) {
            if (killer.hasPotionEffect(PotionEffectType.SPEED)) {
                killer.removePotionEffect(PotionEffectType.SPEED);
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if(killer.hasPotionEffect(PotionEffectType.SPEED))
                            killer.removePotionEffect(PotionEffectType.SPEED);
                        killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
                    }
                }.runTaskLater(main, 2 * 60 * 20 + 20 * 2);
            }
            killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2 * 60 * 20, 2, false, false));
        }
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU1MTY5YWYzNWViMjJkODZiNzY4MzY2NjI3N2ZkMTlkZTg0YjUyYzU4N2Y2M2I4ZGI4Y2U5NTIxNWFkMSJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Minato";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "naruto.yml");
    }

    @Override
    public void setCanUseSmell(boolean state) {
        this.canUseKurama = state;
    }

    @Override
    public boolean canUseSmell() {
        return canUseKurama;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdSmell(main));
    }

    /**
     * ShurikenJutsu Arc
     */
    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager joueur = this.main.getPlayerManager(player.getUniqueId());
            if (joueur.getRole() instanceof Minato) {
                ItemStack bow = event.getBow();
                if (joueur.getRoleCooldownShurikenJustu() == 0) {
                    if (bow.hasItemMeta() && bow.getItemMeta().hasDisplayName()) {
                        if (bow.getItemMeta().getDisplayName().equalsIgnoreCase("§cShurikenJutsu")) {
                            this.toTp = player;
                            this.lastProjectileID = event.getProjectile().getEntityId();
                            joueur.setRoleCooldownShurikenJustu(45);
                            joueur.sendTimer(player, joueur.getRoleCooldownShurikenJustu(), bow);
                        }
                    }
                } else {
                    player.sendMessage(Messages.cooldown(joueur.getRoleCooldownShurikenJustu()));
                }
            }
        }
    }

    /**
     * ShurikenJutsu Arc
     */
    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        ItemStack item = event.getItem();
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§cShurikenJutsu")) {
                event.setCancelled(true);
                player.closeInventory();
                player.sendMessage(Messages.error("Vous ne pouvez enchanter votre ShurikenJutsu !"));
            }
        }
    }

    /**
     * ShurikenJutsu Arc
     */
    @EventHandler
    public void onProjectileHitGround(ProjectileHitEvent event) {
        if (this.toTp != null && event.getEntity().getEntityId() == this.lastProjectileID) {
            Location loc = event.getEntity().getLocation();
            loc.setYaw(this.toTp.getLocation().getYaw());
            loc.setPitch(this.toTp.getLocation().getPitch());
            this.toTp.teleport(loc);
            usePower(main.getPlayerManager(this.toTp.getUniqueId()));
            this.toTp = null;
            this.lastProjectileID = -1;
        }
    }

    /**
     * ShurikenJutsu Item
     */
    public List<Location> getBalises() {
        return balises;
    }

	@Override
	public Chakra getChakra() {
		return Chakra.KATON;
	}
}
