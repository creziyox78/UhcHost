package fr.lastril.uhchost.scenario.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;

public class Bookception extends Scenario {
	
	public Bookception() {
		super("Bookception", Arrays.asList(I18n.tl("scenarios.bookception.lore", ""), I18n.tl("scenarios.bookception.lore1", "")), Material.ENCHANTED_BOOK);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		ItemStack is = new ItemStack(Material.ENCHANTED_BOOK);
		EnchantmentStorageMeta m = (EnchantmentStorageMeta) is.getItemMeta();
		List<Enchantment> enchants = new ArrayList<>();
		for (Enchantment enchantment : Enchantment.values())
			enchants.add(enchantment);
		Random r = new Random();
		Enchantment enchantement = enchants.get(r.nextInt(enchants.size()));
		int level = r.nextInt(4)+1;
		if(level > enchantement.getMaxLevel())
			level = enchantement.getMaxLevel();
		m.addEnchant(enchantement, level, false);
		is.setItemMeta((ItemMeta) m);
		event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), is);
	}

}
