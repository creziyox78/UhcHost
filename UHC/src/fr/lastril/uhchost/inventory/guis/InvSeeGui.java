package fr.lastril.uhchost.inventory.guis;

import java.util.ArrayList;
import java.util.List;

import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class InvSeeGui extends IQuickInventory {

	public Player player;

	public InvSeeGui(Player player) {
		super("§eInventaire", 6 * 9);
		this.player = player;
	}

	@Override
	public void contents(QuickInventory inv) {
		for (ItemStack armor : player.getInventory().getArmorContents()) {
			inv.addItem(armor);
		}
		
		List<String> lore = new ArrayList<>();

		lore.add("§4Vie: §c"+player.getHealth()+"/"+player.getMaxHealth());
		lore.add("§6XP: §e"+player.getLevel());
		lore.add("§3Kills: §b"+player.getStatistic(Statistic.PLAYER_KILLS));
		
		if(!player.getActivePotionEffects().isEmpty()) {
			lore.add("§eEffets:");
			for(PotionEffect effect : player.getActivePotionEffects()) {
				lore.add("§6- "+effect.getType().getName()+" : "+(effect.getDuration() < Integer.MAX_VALUE / 3 ? new FormatTime(effect.getDuration()).toString() : "**:**"));
			}
		}
		
		inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(player.getDisplayName()).setSkullOwner(player.getName()).setLore(lore).toItemStack(), 8);
		
		int i = 9;
		for (ItemStack stacks : player.getInventory().getContents()) {
			inv.setItem(stacks, i);
			i++;
		}
	}

}
