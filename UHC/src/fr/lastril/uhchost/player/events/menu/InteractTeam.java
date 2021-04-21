package fr.lastril.uhchost.player.events.menu;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.team.TeamsGui;
import fr.lastril.uhchost.scoreboard.TeamUtils;
import fr.lastril.uhchost.tools.I18n;

public class InteractTeam implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory().getName().equalsIgnoreCase(I18n.tl("guis.teams.name", new String[0]))) {
			ItemStack is = event.getCurrentItem();
			if (is == null || is.getType() == Material.AIR)
				return;
			event.setCancelled(true);
			if (is.getType() == Material.BARRIER) {
				event.getWhoClicked().closeInventory();
			} else {
				for (TeamUtils.Teams teams : TeamUtils.Teams.values()) {
					if ((teams.getBannerCreator().getName() + teams.getName())
							.equalsIgnoreCase(is.getItemMeta().getDisplayName())) {
						if (teams.getTeam().getEntries().size() >= (UhcHost.getInstance()).teamUtils
								.getPlayersPerTeams()) {
							event.getWhoClicked().closeInventory();
							event.getWhoClicked().sendMessage(teams.getPrefix() + I18n.tl("teamFull", new String[0]));
							break;
						}
						(UhcHost.getInstance()).teamUtils.setTeam((Player) event.getWhoClicked(), teams.getTeam());
						for (UUID uuid : (UhcHost.getInstance()).gameManager.getPlayers()) {
							if (Bukkit.getPlayer(uuid).getOpenInventory() != null
									&& Bukkit.getPlayer(uuid).getOpenInventory().getTitle()
											.equalsIgnoreCase(event.getClickedInventory().getName())) {
								Bukkit.getPlayer(uuid).closeInventory();
								new TeamsGui(Bukkit.getPlayer(uuid));
							}
						}
						event.getWhoClicked().sendMessage("§7" + I18n.tl("joinTeam", is.getItemMeta().getDisplayName().replace("Equipe ", "")));
						break;
					}
				}
			}
		}
	}

}
