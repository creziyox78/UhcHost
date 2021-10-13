package fr.lastril.uhchost.tools;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Items {
	public static ItemStack getItemColored(Material material, String customName, byte color, boolean unbreakable) {
		ItemStack customItem = new ItemStack(material, 1, color);
		ItemMeta customMItem = customItem.getItemMeta();
		customMItem.spigot().setUnbreakable(unbreakable);
		customMItem.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
		if (customName != null)
			customMItem.setDisplayName(customName);
		customItem.setItemMeta(customMItem);
		return customItem;
	}

	public static ItemStack getItem(Material material, String customName, boolean unbreakable) {
		ItemStack customItem = new ItemStack(material, 1);
		ItemMeta customMItem = customItem.getItemMeta();
		customMItem.spigot().setUnbreakable(unbreakable);
		customMItem.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
		if (customName != null)
			customMItem.setDisplayName(customName);
		customItem.setItemMeta(customMItem);
		return customItem;
	}

	public static ItemStack customItem(ItemStack Materia, String customName, List<String> customLore,
			Enchantment ench) {
		ItemStack it = Materia;
		ItemMeta itM = it.getItemMeta();
		if (customName != null) {
			itM.setDisplayName(customName);
			if (customLore != null)
				itM.setLore(customLore);
			if (ench != null)
				itM.addEnchant(ench, 1, true);
			itM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			itM.addItemFlags(ItemFlag.HIDE_DESTROYS);
			itM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			itM.addItemFlags(ItemFlag.HIDE_PLACED_ON);
			itM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			itM.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		}
		it.setItemMeta(itM);
		return it;
	}

	public static ItemStack Banner(String motif, DyeColor color) {
		ItemStack banner = new ItemStack(Material.BANNER);
		BannerMeta bannerM = (BannerMeta) banner.getItemMeta();
		bannerM.setBaseColor(color);
		List<Pattern> patterns = new ArrayList<>();
		String str;
		switch ((str = motif).hashCode()) {
		case -1271629221:
			if (!str.equals("flower"))
				break;
			patterns.add(new Pattern(DyeColor.WHITE, PatternType.FLOWER));
			break;
		case 99151942:
			if (!str.equals("heart"))
				break;
			patterns.add(new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE));
			patterns.add(new Pattern(color, PatternType.TRIANGLE_TOP));
			break;
		case 106674728:
			if (!str.equals("pique"))
				break;
			patterns.add(new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE));
			patterns.add(new Pattern(color, PatternType.TRIANGLE_BOTTOM));
			break;
		case 353189147:
			if (!str.equals("losange"))
				break;
			patterns.add(new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE));
			break;
		}
		bannerM.setPatterns(patterns);
		banner.setItemMeta((ItemMeta) bannerM);
		return banner;
	}

	public static ItemStack createHead(String owner, String name, int amount, List<String> lores) {
		ItemStack is = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		PropertyMap propertyMap = profile.getProperties();
		propertyMap.put("textures", new Property("textures", owner));
		SkullMeta skullMeta = (SkullMeta) is.getItemMeta();
		Class<?> c_skullMeta = skullMeta.getClass();
		try {
			Field f_profile = c_skullMeta.getDeclaredField("profile");
			f_profile.setAccessible(true);
			f_profile.set(skullMeta, profile);
			f_profile.setAccessible(false);
			if (lores != null)
				skullMeta.setLore(lores);
			skullMeta.setDisplayName(name);
			is.setItemMeta(skullMeta);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return is;
	}

}
