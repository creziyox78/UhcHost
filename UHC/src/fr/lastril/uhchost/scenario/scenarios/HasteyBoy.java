package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class HasteyBoy extends Scenario {
	
	public HasteyBoy() {
		super("Hastey Boy", Arrays.asList(I18n.tl("scenarios.hasteyboy.lore", ""), I18n.tl("scenarios.hasteyboy.lore1", "")), Material.GOLD_PICKAXE);
	}
	
	@EventHandler
	public void Craft(PrepareItemCraftEvent event) {
		if (event.getRecipe().getResult().getType() == Material.DIAMOND_PICKAXE
				|| event.getRecipe().getResult().getType() == Material.GOLD_PICKAXE
				|| event.getRecipe().getResult().getType() == Material.IRON_PICKAXE
				|| event.getRecipe().getResult().getType() == Material.STONE_PICKAXE
				|| event.getRecipe().getResult().getType() == Material.WOOD_PICKAXE ||

				event.getRecipe().getResult().getType() == Material.DIAMOND_AXE
				|| event.getRecipe().getResult().getType() == Material.GOLD_AXE
				|| event.getRecipe().getResult().getType() == Material.IRON_AXE
				|| event.getRecipe().getResult().getType() == Material.STONE_AXE
				|| event.getRecipe().getResult().getType() == Material.WOOD_AXE ||

				event.getRecipe().getResult().getType() == Material.DIAMOND_SPADE
				|| event.getRecipe().getResult().getType() == Material.GOLD_SPADE
				|| event.getRecipe().getResult().getType() == Material.IRON_SPADE
				|| event.getRecipe().getResult().getType() == Material.STONE_SPADE
				|| event.getRecipe().getResult().getType() == Material.WOOD_SPADE) {
			event.getInventory().setResult(HasteyTools(event.getRecipe().getResult()));
		}
	}
	
	public ItemStack HasteyTools(ItemStack current) {
		ItemMeta currentM = current.getItemMeta();
		currentM.addEnchant(Enchantment.DIG_SPEED, 3, true);
		currentM.addEnchant(Enchantment.DURABILITY, 3, true);
		current.setItemMeta(currentM);
		return current;

	}

}
