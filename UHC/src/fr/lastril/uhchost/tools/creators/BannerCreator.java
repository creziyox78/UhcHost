package fr.lastril.uhchost.tools.creators;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BannerCreator {

	private ItemStack banner;

	private BannerMeta meta;

	private Material m;

	private String name;

	private List<String> lores;

	private List<Pattern> patterns;

	private int amount;

	public BannerCreator(String name, List<String> lores, int amount, boolean wallBanner) {
		if (amount == 0)
			amount++;
		this.name = name;
		this.lores = lores;
		this.amount = amount;
		setM(Material.BANNER);
		if (wallBanner)
			setM(Material.WALL_BANNER);
		this.banner = new ItemStack(Material.BANNER, this.amount, (short) 0);
		this.meta = (BannerMeta) this.banner.getItemMeta();
	}

	public void setBaseColor(DyeColor color) {
		this.meta.setBaseColor(color);
	}

	public void setPatterns(Pattern... patterns) {
		for (Pattern pattern : patterns)
			this.meta.addPattern(pattern);
	}

	public ItemStack create() {
		if (this.name != null)
			this.meta.setDisplayName(this.name);
		if (this.lores != null)
			this.meta.setLore(this.lores);
		if (this.patterns != null)
			for (Pattern pattern : this.patterns)
				this.meta.addPattern(pattern);
		this.meta.addItemFlags(ItemFlag.values());
		this.banner.setItemMeta(this.meta);
		return this.banner;
	}

	public ItemStack getBanner() {
		return this.banner;
	}

	public void setBanner(ItemStack banner) {
		this.banner = banner;
	}

	public BannerMeta getMeta() {
		return this.meta;
	}

	public void setMeta(BannerMeta meta) {
		this.meta = meta;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getLores() {
		return this.lores;
	}

	public void setLores(List<String> lores) {
		this.lores = lores;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public List<Pattern> getPatterns() {
		return this.patterns;
	}

	public void setPatterns(List<Pattern> patterns) {
		this.patterns = patterns;
	}

	public Material getM() {
		return this.m;
	}

	public void setM(Material m) {
		this.m = m;
	}

}
