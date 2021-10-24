package fr.lastril.uhchost.team;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.scoreboard.TeamUtils;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.BannerCreator;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamsGui {

	public TeamsGui(Player player) {
		Inventory inventory = Bukkit.createInventory(null, 54, I18n.tl("guis.teams.name"));
		int add = 0;
		for (TeamUtils.Teams team : TeamUtils.Teams.values()) {
			if (add < (UhcHost.getInstance()).teamUtils.getNeededTeam()) {
				BannerCreator bc = team.getBannerCreator();
				List<String> lores = new ArrayList<>();
				for (String s : team.getTeam().getEntries())
					lores.add("§8- "+ team.getPrefix() + s);
				bc.setLores(lores);
				bc.setBaseColor(team.getBaseColor());
				bc.setPatterns(team.getPatterns());
				ItemStack is = bc.create();
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(bc.getName() + team.getName());
				is.setItemMeta(im);
				inventory.addItem(is);
				add++;
			} else {
				add = 0;
				break;
			}
		}
		inventory.setItem(53, (new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), null)).create());
		player.openInventory(inventory);
	}

}
