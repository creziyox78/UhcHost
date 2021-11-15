package fr.lastril.uhchost.modes.naruto.v2.gui;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.Sasori;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class SasoriGUI extends IQuickInventory {

    private final UhcHost main;
    private final Sasori sasori;
    private final List<UUID> kills;

    public SasoriGUI(UhcHost main, Sasori sasori, List<UUID> kills) {
        super(ChatColor.DARK_RED + "Marionnettisme", 9 * 3);
        this.main = main;
        this.sasori = sasori;
        this.kills = kills;
        this.kills.removeAll(sasori.getRevived());
    }

    @Override
    public void contents(QuickInventory inv) {
        for (UUID target : kills) {
            Player onlineTarget = Bukkit.getPlayer(target);
            if (onlineTarget != null) {
                inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setSkullOwner(onlineTarget.getName()).setName(onlineTarget.getName()).toItemStack(), onClick -> {
                    Player playerClick = onClick.getPlayer();
                    PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());

                    PlayerManager targetPlayerManager = main.getPlayerManager(onlineTarget.getUniqueId());
                    targetPlayerManager.setAlive(true);
                    targetPlayerManager.setSpectator(false);
                    onlineTarget.setGameMode(GameMode.SURVIVAL);
                    onlineTarget.teleport(playerClick);

                    UhcHost.getInstance().getSpectatorTeam().removeEntry(onlineTarget.getName());
                    this.giveStuff(onlineTarget);
                    targetPlayerManager.setCamps(PlayerManager.getCamps());
                    UhcHost.getInstance().checkWin();

                    playerClick.setMaxHealth(playerClick.getMaxHealth() - 2);

                    main.getMumbleManager().onPlayerRevive(targetPlayerManager);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (targetPlayerManager.getMumbleData().getMumbleUser() != null) {
                                targetPlayerManager.getMumbleData().getMumbleUser().mute(false);
                                UhcHost.debug("Mumble Manager: demuted player");
                            }
                        }
                    }.runTaskLater(main, 40);

                    sasori.getRevived().add(onlineTarget.getUniqueId());
                    sasori.getMarionnetteAlive().add(onlineTarget.getUniqueId());
                    sasori.addMarionnetteEffect(onlineTarget);
                    inv.close(playerClick);
                });
            }
        }
    }

    private void giveStuff(Player player) {
        player.getInventory().clear();
        player.getInventory().addItem(new QuickItem(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1, true).toItemStack());
        player.getInventory().addItem(new QuickItem(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 1, true).toItemStack());
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 3));
        player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
        player.getInventory().addItem(new ItemStack(Material.LOG, 64));
        player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
        player.getInventory().setHelmet(new QuickItem(Material.IRON_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).toItemStack());
        player.getInventory().setChestplate(new QuickItem(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).toItemStack());
        player.getInventory().setLeggings(new QuickItem(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).toItemStack());
        player.getInventory().setBoots(new QuickItem(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).toItemStack());

        PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
        if (PlayerManager.hasRole()) {
            PlayerManager.getDisplayRole().stuff(player);
        }

        player.setMaxHealth(6 * 2);

    }
}
