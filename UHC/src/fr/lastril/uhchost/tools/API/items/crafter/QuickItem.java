package fr.lastril.uhchost.tools.API.items.crafter;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import fr.lastril.uhchost.enums.HeadTextures;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class QuickItem {

    private ItemStack is;
    private Consumer<QuickEvent> consumer;

    public QuickItem(Material m) {
        this(m, 1);
    }

    public QuickItem(ItemStack is) { 
        this.is = is;
    }

    public QuickItem(Material m, int amount) {
    	this(m, amount, 0);
    }

    public QuickItem(Material m, int amount, int meta){
        is = new ItemStack(m, amount, (short) meta);
    }


    public QuickItem clone() {
        return new QuickItem(is);
    }

    public QuickItem setDurability(short dur) {
        is.setDurability(dur);
        return this;
    }

    public QuickItem glow(boolean state){
        if(state) {
            this.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
            ItemMeta itemMeta = is.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            is.setItemMeta(itemMeta);
        }
        return this;
    }

    public QuickItem glow(){
        return this.glow(true);
    }

    public QuickItem setName(String name) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);
        return this;
    }

    public String getName(){
        ItemMeta im = is.getItemMeta();
        return im.getDisplayName();
    }

    public QuickItem addUnsafeEnchantment(Enchantment ench, int level) {
        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public QuickItem removeEnchantment(Enchantment ench) {
        is.removeEnchantment(ench);
        return this;
    }

    public QuickItem setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            im.setOwner(owner);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    public QuickItem addEnchant(Enchantment ench, int level, boolean show) {
        ItemMeta im = is.getItemMeta();
        im.addEnchant(ench, level, show);
        is.setItemMeta(im);
        return this;
    }

    public Map<Enchantment, Integer> getEnchants(){
        ItemMeta im = is.getItemMeta();
        return im.getEnchants();
    }

    public QuickItem addItemFlag(ItemFlag... itemFlags){
        ItemMeta im = is.getItemMeta();
        im.addItemFlags(itemFlags);
        is.setItemMeta(im);
        return this;
    }

    public QuickItem hideEnchant(){
        ItemMeta im = is.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(im);
        return this;
    }

    public QuickItem setInfinityDurability() {
        is.setDurability(Short.MIN_VALUE);
        return this;
    }

    public QuickItem setLore(String... lore) {
        return this.setLore(Arrays.asList(lore));
    }

    public QuickItem setLore(List<String> lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public List<String> getLore(){
        return is.getItemMeta().getLore();
    }

    public QuickItem setLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
            im.setColor(color);
            is.setItemMeta(im);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return this;
    }

    public QuickItem setAmount(int amount){
        is.setAmount(amount);
        return this;
    }

    public Color getLeatherArmorColor(){
        LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
        return im.getColor();
    }

    /**
	 * Set head texture.
	 * 
	 * @param hash The hash of the head for the GameProfile.
	 */
	public QuickItem setTexture(String hash) {
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		PropertyMap propertyMap = profile.getProperties();
		propertyMap.put("textures", new Property("textures", hash));
		SkullMeta skullMeta = (SkullMeta) this.is.getItemMeta();
		Class<?> c_skullMeta = skullMeta.getClass();
		try {
			Field f_profile = c_skullMeta.getDeclaredField("profile");
			f_profile.setAccessible(true);
			f_profile.set(skullMeta, profile);
			f_profile.setAccessible(false);
			this.is.setItemMeta(skullMeta);
			return this;
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
        return this;
	}
	
	public QuickItem setTexture(HeadTextures texture) {
		return setTexture(texture.getHash());
	}

    public QuickItem onClick(Consumer<QuickEvent> consumer){
        this.consumer = consumer;
        return this;
    }

    public ItemStack toItemStack(){
        if(consumer != null) QuickItemManager.registerItem(this, this.is, consumer);
        return this.is;
    }

}
