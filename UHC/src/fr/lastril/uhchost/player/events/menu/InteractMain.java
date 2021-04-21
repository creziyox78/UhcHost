package fr.lastril.uhchost.player.events.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.scenario.ScenariosGui;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.inventory.guis.HostConfig;
import fr.lastril.uhchost.tools.inventory.guis.MaxPlayersGui;

public class InteractMain implements Listener {
	private UhcHost pl;

	public InteractMain(UhcHost plugin) {
		this.pl = plugin;
	}

	@EventHandler
	public void onInteract(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		ItemStack current = e.getCurrentItem();
		Player player = (Player) e.getWhoClicked();
		final GameManager gameManager = UhcHost.getInstance().getGamemanager();
		if (gameManager.getGameState() != GameState.LOBBY && gameManager.getGameState() != GameState.STARTING)
			return;
		if (current == null)
			return;
		if (!current.hasItemMeta())
			return;
		if (inv.getName() == null)
			return;
		if (inv.getName().equalsIgnoreCase(ChatColor.GOLD + "Configuration")) {
			e.setCancelled(true);
			if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Lancer la partie")) {
				GameState.setCurrentState(GameState.STARTING);
				player.openInventory(HostConfig.Main());
				(new BukkitRunnable() {
					private int timer = 10;
					
					@Override
					public void run() {
						if(GameState.isState(GameState.STARTING)) {
							ActionBar.broadcastMessage(ChatColor.YELLOW + "Lancement dans " + this.timer + " secondes.");
							if(timer == 0) {
								ActionBar.broadcastMessage(ChatColor.AQUA + "Lancement de la partie !");
								for(Player player: Bukkit.getOnlinePlayers()) {
									player.getInventory().clear();
								}
								UhcHost.getInstance().gameManager.preStart();
								
								cancel();
							}
							timer--;
						} else if(GameState.isState(GameState.LOBBY)) {
							cancel();
						}
						
					}
				}).runTaskTimer((Plugin) this.pl, 0L, 20L);
				
			}
			if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Annuler le lancement")) {
				GameState.setCurrentState(GameState.LOBBY);
				player.openInventory(HostConfig.Main());
				
			}
			if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "Whitelist")) {
				Bukkit.setWhitelist(!Bukkit.hasWhitelist());
				player.openInventory(HostConfig.Main());
			}
			if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Règles"))
				player.openInventory(HostConfig.Rules());
			if(current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Scenarios")) {
				player.closeInventory();
				(new ScenariosGui(player)).show();
			}
			if(current.getItemMeta().getDisplayName().equalsIgnoreCase(I18n.tl("guis.main.maxPlayers", new String[0]))) {
				 (new MaxPlayersGui(player)).show();
			}
				
		}
	}
}
