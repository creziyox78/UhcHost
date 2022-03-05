package fr.lastril.uhchost.player.events.interact;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.events.GameStartEvent;
import fr.lastril.uhchost.inventory.scoreboard.TeamUtils;
import fr.lastril.uhchost.team.TeamsGui;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class InteractTeam implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getClickedInventory() != null){
			if (event.getClickedInventory().getName().equalsIgnoreCase(I18n.tl("guis.teams.name"))) {
				ItemStack is = event.getCurrentItem();
				if (is == null || is.getType() == Material.AIR)
					return;
				event.setCancelled(true);
				if (is.getType() == Material.BARRIER) {
					event.getWhoClicked().closeInventory();
				} else {
					for (TeamUtils.Teams teams : TeamUtils.Teams.values()) {
						if(teams.getBannerCreator() != null){
							if ((teams.getBannerCreator().getName() + teams.getName())
									.equalsIgnoreCase(is.getItemMeta().getDisplayName())) {
								if (teams.getTeam().getEntries().size() >= (UhcHost.getInstance()).teamUtils
										.getPlayersPerTeams()) {
									event.getWhoClicked().closeInventory();
									event.getWhoClicked().sendMessage(teams.getPrefix() + I18n.tl("teamFull"));
									break;
								}
								(UhcHost.getInstance()).teamUtils.setTeam((Player) event.getWhoClicked(), teams.getTeam());
								for (UUID uuid : (UhcHost.getInstance()).getAllPlayerManager().keySet()) {
									if (Bukkit.getPlayer(uuid) != null && Bukkit.getPlayer(uuid).getOpenInventory() != null
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
	}

	@EventHandler
	public void onGameStart(GameStartEvent event){
		HandlerList.unregisterAll(this);
	}

}
