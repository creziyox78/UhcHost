package fr.lastril.uhchost.player.events.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.lastril.uhchost.tools.inventory.EnchantBook;
import fr.lastril.uhchost.tools.inventory.guis.Enchant;

public class InteractEnchant implements Listener {
	@EventHandler
	public void menuEnchant(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		Player player = (Player) e.getWhoClicked();
		ItemStack current = e.getCurrentItem();
		ItemStack hand = player.getItemInHand();
		ClickType click = e.getClick();
		if (inv.getName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Categories")) {
			e.setCancelled(true);
			if (current != null)
				if (current.getType() == Material.BEDROCK) {
					if (current.hasItemMeta() && current.getItemMeta().hasDisplayName() && current.getItemMeta().getDisplayName().contains(ChatColor.AQUA + "Unbreakable:")) {
						if (hand.getItemMeta().spigot().isUnbreakable()) {
							EnchantBook.SetUnbreakable(hand, false);
						} else {
							EnchantBook.SetUnbreakable(hand, true);
						}
					} else {
						return;
					}
					player.openInventory(Enchant.Categories(player, player.getItemInHand()));
				} else if (current.getType() == Material.DIAMOND_SWORD) {
					if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Sword")) {
						player.closeInventory();
						player.openInventory(Enchant.Sword(player, player.getItemInHand()));
					}
				} else if (current.getType() == Material.DIAMOND_CHESTPLATE) {
					if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Armor")) {
						player.closeInventory();
						player.openInventory(Enchant.Armor(player, player.getItemInHand()));
					}
				} else if (current.getType() == Material.BOW) {
					if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Bow")) {
						player.closeInventory();
						player.openInventory(Enchant.Bow(player, player.getItemInHand()));
					}
				} else if (current.getType() == Material.DIAMOND_PICKAXE) {
					if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Tool")) {
						player.closeInventory();
						player.openInventory(Enchant.Tool(player, player.getItemInHand()));
					}
				} else if (current.getType() == Material.INK_SACK
						&& current.getItemMeta().getDisplayName().contains(ChatColor.RED + "Close")) {
					player.closeInventory();
				}
		}
		if (inv.getName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Sword")) {
			e.setCancelled(true);
			if (current.getType() == Material.ENCHANTED_BOOK)
				if (click == ClickType.LEFT) {
					if (current.getItemMeta().getDisplayName().contains("Sharpness")) {
						EnchantBook.AddEnchantment(hand, Enchantment.DAMAGE_ALL);
					} else if (current.getItemMeta().getDisplayName().contains("Smite")) {
						EnchantBook.AddEnchantment(hand, Enchantment.DAMAGE_UNDEAD);
					} else if (current.getItemMeta().getDisplayName().contains("Bane of Arthropods")) {
						EnchantBook.AddEnchantment(hand, Enchantment.DAMAGE_ARTHROPODS);
					} else if (current.getItemMeta().getDisplayName().contains("Knockback")) {
						EnchantBook.AddEnchantment(hand, Enchantment.KNOCKBACK);
					} else if (current.getItemMeta().getDisplayName().contains("Fire Aspect")) {
						EnchantBook.AddEnchantment(hand, Enchantment.FIRE_ASPECT);
					} else if (current.getItemMeta().getDisplayName().contains("Looting")) {
						EnchantBook.AddEnchantment(hand, Enchantment.LOOT_BONUS_MOBS);
					} else if (current.getItemMeta().getDisplayName().contains("Unbreaking")) {
						EnchantBook.AddEnchantment(hand, Enchantment.DURABILITY);
					}
				} else if (click == ClickType.RIGHT) {
					if (current.getItemMeta().getDisplayName().contains("Sharpness")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.DAMAGE_ALL);
					} else if (current.getItemMeta().getDisplayName().contains("Smite")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.DAMAGE_UNDEAD);
					} else if (current.getItemMeta().getDisplayName().contains("Bane of Arthropods")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.DAMAGE_ARTHROPODS);
					} else if (current.getItemMeta().getDisplayName().contains("Knockback")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.KNOCKBACK);
					} else if (current.getItemMeta().getDisplayName().contains("Fire Aspect")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.FIRE_ASPECT);
					} else if (current.getItemMeta().getDisplayName().contains("Looting")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.LOOT_BONUS_MOBS);
					} else if (current.getItemMeta().getDisplayName().contains("Unbreaking")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.DURABILITY);
					}
				}
			player.openInventory(Enchant.Sword(player, player.getItemInHand()));
			if (current.getType() == Material.INK_SACK
					&& current.getItemMeta().getDisplayName().contains(ChatColor.RED + "Back")) {
				player.openInventory(Enchant.Categories(player, player.getItemInHand()));
			}
		}
		if (inv.getName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Armor")) {
			e.setCancelled(true);
			if (current.getType() == Material.ENCHANTED_BOOK)
				if (click == ClickType.LEFT) {
					if (current.getItemMeta().getDisplayName().contains("Fire Protection")) {
						EnchantBook.AddEnchantment(hand, Enchantment.PROTECTION_FIRE);
					} else if (current.getItemMeta().getDisplayName().contains("Feather Falling")) {
						EnchantBook.AddEnchantment(hand, Enchantment.PROTECTION_FALL);
					} else if (current.getItemMeta().getDisplayName().contains("Blast Protection")) {
						EnchantBook.AddEnchantment(hand, Enchantment.PROTECTION_EXPLOSIONS);
					} else if (current.getItemMeta().getDisplayName().contains("Projectile Protection")) {
						EnchantBook.AddEnchantment(hand, Enchantment.PROTECTION_PROJECTILE);
					} else if (current.getItemMeta().getDisplayName().contains("Protection")) {
						EnchantBook.AddEnchantment(hand, Enchantment.PROTECTION_ENVIRONMENTAL);
					} else if (current.getItemMeta().getDisplayName().contains("Respiration")) {
						EnchantBook.AddEnchantment(hand, Enchantment.OXYGEN);
					} else if (current.getItemMeta().getDisplayName().contains("Aqua Affinity")) {
						EnchantBook.AddEnchantment(hand, Enchantment.WATER_WORKER);
					} else if (current.getItemMeta().getDisplayName().contains("Thorns")) {
						EnchantBook.AddEnchantment(hand, Enchantment.THORNS);
					} else if (current.getItemMeta().getDisplayName().contains("Depth Strider")) {
						EnchantBook.AddEnchantment(hand, Enchantment.DEPTH_STRIDER);
					} else if (current.getItemMeta().getDisplayName().contains("Unbreaking")) {
						EnchantBook.AddEnchantment(hand, Enchantment.DURABILITY);
					}
				} else if (click == ClickType.RIGHT) {
					if (current.getItemMeta().getDisplayName().contains("Fire Protection")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.PROTECTION_FIRE);
					} else if (current.getItemMeta().getDisplayName().contains("Feather Falling")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.PROTECTION_FALL);
					} else if (current.getItemMeta().getDisplayName().contains("Blast Protection")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.PROTECTION_EXPLOSIONS);
					} else if (current.getItemMeta().getDisplayName().contains("Projectile Protection")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.PROTECTION_PROJECTILE);
					} else if (current.getItemMeta().getDisplayName().contains("Protection")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.PROTECTION_ENVIRONMENTAL);
					} else if (current.getItemMeta().getDisplayName().contains("Respiration")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.OXYGEN);
					} else if (current.getItemMeta().getDisplayName().contains("Aqua Affinity")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.WATER_WORKER);
					} else if (current.getItemMeta().getDisplayName().contains("Thorns")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.THORNS);
					} else if (current.getItemMeta().getDisplayName().contains("Depth Strider")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.DEPTH_STRIDER);
					} else if (current.getItemMeta().getDisplayName().contains("Unbreaking")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.DURABILITY);
					}
				}
			player.openInventory(Enchant.Armor(player, player.getItemInHand()));
			if (current.getType() == Material.INK_SACK
					&& current.getItemMeta().getDisplayName().contains(ChatColor.RED + "Back")) {
				player.openInventory(Enchant.Categories(player, player.getItemInHand()));
			}
		}
		if (inv.getName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Bow")) {
			e.setCancelled(true);
			if (current.getType() == Material.ENCHANTED_BOOK)
				if (click == ClickType.LEFT) {
					if (current.getItemMeta().getDisplayName().contains("Power")) {
						EnchantBook.AddEnchantment(hand, Enchantment.ARROW_DAMAGE);
					} else if (current.getItemMeta().getDisplayName().contains("Punch")) {
						EnchantBook.AddEnchantment(hand, Enchantment.ARROW_KNOCKBACK);
					} else if (current.getItemMeta().getDisplayName().contains("Flame")) {
						EnchantBook.AddEnchantment(hand, Enchantment.ARROW_FIRE);
					} else if (current.getItemMeta().getDisplayName().contains("Infinity")) {
						EnchantBook.AddEnchantment(hand, Enchantment.ARROW_INFINITE);
					} else if (current.getItemMeta().getDisplayName().contains("Unbreaking")) {
						EnchantBook.AddEnchantment(hand, Enchantment.DURABILITY);
					}
				} else if (click == ClickType.RIGHT) {
					if (current.getItemMeta().getDisplayName().contains("Power")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.ARROW_DAMAGE);
					} else if (current.getItemMeta().getDisplayName().contains("Punch")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.ARROW_KNOCKBACK);
					} else if (current.getItemMeta().getDisplayName().contains("Flame")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.ARROW_FIRE);
					} else if (current.getItemMeta().getDisplayName().contains("Infinity")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.ARROW_INFINITE);
					} else if (current.getItemMeta().getDisplayName().contains("Unbreaking")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.DURABILITY);
					}
				}
			player.openInventory(Enchant.Bow(player, player.getItemInHand()));
			if (current.getType() == Material.INK_SACK
					&& current.getItemMeta().getDisplayName().contains(ChatColor.RED + "Back")) {
				player.openInventory(Enchant.Categories(player, player.getItemInHand()));
			}
		}
		if (inv.getName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Tool")) {
			e.setCancelled(true);
			if (current.getType() == Material.ENCHANTED_BOOK)
				if (click == ClickType.LEFT) {
					if (current.getItemMeta().getDisplayName().contains("Efficiency")) {
						EnchantBook.AddEnchantment(hand, Enchantment.DIG_SPEED);
					} else if (current.getItemMeta().getDisplayName().contains("Silk Touch")) {
						EnchantBook.AddEnchantment(hand, Enchantment.SILK_TOUCH);
					} else if (current.getItemMeta().getDisplayName().contains("Unbreaking")) {
						EnchantBook.AddEnchantment(hand, Enchantment.DURABILITY);
					} else if (current.getItemMeta().getDisplayName().contains("Fortune")) {
						EnchantBook.AddEnchantment(hand, Enchantment.LOOT_BONUS_BLOCKS);
					} else if (current.getItemMeta().getDisplayName().contains("Luck of the Sea")) {
						EnchantBook.AddEnchantment(hand, Enchantment.LUCK);
					} else if (current.getItemMeta().getDisplayName().contains("Lure")) {
						EnchantBook.AddEnchantment(hand, Enchantment.LURE);
					}
				} else if (click == ClickType.RIGHT) {
					if (current.getItemMeta().getDisplayName().contains("Efficiency")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.DIG_SPEED);
					} else if (current.getItemMeta().getDisplayName().contains("Silk Touch")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.SILK_TOUCH);
					} else if (current.getItemMeta().getDisplayName().contains("Unbreaking")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.DURABILITY);
					} else if (current.getItemMeta().getDisplayName().contains("Fortune")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.LOOT_BONUS_BLOCKS);
					} else if (current.getItemMeta().getDisplayName().contains("Luck of the Sea")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.LUCK);
					} else if (current.getItemMeta().getDisplayName().contains("Lure")) {
						EnchantBook.RemoveEnchantment(hand, Enchantment.LURE);
					}
				}
			player.openInventory(Enchant.Tool(player, player.getItemInHand()));
			if (current.getType() == Material.INK_SACK
					&& current.getItemMeta().getDisplayName().contains(ChatColor.RED + "Back")) {
				player.openInventory(Enchant.Categories(player, player.getItemInHand()));
			}
		}
	}
}
