package fr.lastril.uhchost.modes.naruto.v2.gui.edotensei;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.EdoTenseiItem.EdoTenseiUser;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class EdoTenseiGUI extends IQuickInventory {

    private final UhcHost main;
    private final EdoTenseiUser user;

    public EdoTenseiGUI(UhcHost main, EdoTenseiUser user) {
        super(ChatColor.DARK_PURPLE + "Edo Tensei", 5 * 9);
        this.main = main;
        this.user = user;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);
        Role user = (Role) this.user;
        if (user.getPlayerId() != null) {
            PlayerManager userPlayerManager = main.getPlayerManager(user.getPlayerId());
            for (PlayerManager PlayerManagers : main.getPlayerManagers().values()) {
                if (!PlayerManagers.isAlive() && PlayerManagers != userPlayerManager) {
                    if (PlayerManagers.getDeathLocation() != null && PlayerManagers.getPlayer() != null) {
                        if (PlayerManagers.getDeathLocation().getWorld() == userPlayerManager.getPlayer().getWorld()) {
                            if (PlayerManagers.getDeathLocation().distance(userPlayerManager.getPlayer().getLocation()) <= 15) {
                                inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                                        .setName(PlayerManagers.getPlayerName()).setSkullOwner(PlayerManagers.getPlayerName())
                                        .setLore("",
                                                "§7Cliquez pour ressusciter ce PlayerManager.")
                                        .toItemStack(), onClick -> {
                                    Player edoUser = onClick.getPlayer();
                                    PlayerManager PlayerManager = main.getPlayerManager(edoUser.getUniqueId());
                                    PlayerManagers.getPlayer().teleport(edoUser);
                                    PlayerManagers.setAlive(true);
                                    PlayerManagers.setSpectator(false);
                                    PlayerManagers.getPlayer().setGameMode(GameMode.SURVIVAL);
                                    UhcHost.getInstance().getSpectatorTeam().removeEntry(PlayerManagers.getPlayer().getName());
                                    PlayerManagers.setCamps(PlayerManager.getCamps());
                                    user.getPlayer().setItemInHand(null);
                                    giveStuff(PlayerManagers.getPlayer());

                                    edoUser.sendMessage(
                                            Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                                    PlayerManagers.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage()
                                            + "Vous venez d'être réssussité et vous gagnez désormais avec le camp d'§5Orochimaru§e.");

                                    main.getMumbleManager().onPlayerRevive(PlayerManagers);
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            main.shuffled.remove(PlayerManagers);
                                            if (PlayerManagers.getMumbleData().getMumbleUser() != null) {
                                                PlayerManagers.getMumbleData().getMumbleUser().mute(false);
                                                UhcHost.debug("Mumble Manager: demuted player");
                                            }
                                        }
                                    }.runTaskLater(main, 40);

                                    if (PlayerManager.getRole() instanceof NarutoV2Role) {
                                        NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                                        narutoRole.usePower(PlayerManager);
                                    }
                                    inv.close(PlayerManagers.getPlayer());
                                });
                            }
                        }
                    }
                }
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
            PlayerManager.getRole().stuff(player);
        }

    }
}
