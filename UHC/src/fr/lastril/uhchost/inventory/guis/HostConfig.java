
package fr.lastril.uhchost.inventory.guis;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.inventory.guis.modes.ModesGui;
import fr.lastril.uhchost.inventory.guis.timer.RulesGuiHost;
import fr.lastril.uhchost.inventory.guis.world.WorldGui;
import fr.lastril.uhchost.scenario.ScenariosGui;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.Items;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;

public class HostConfig extends Gui {
	public HostConfig(Player player) {
		super(player, 9*5,ChatColor.GOLD + "Configuration");
		for (int i = 0; i < 45; i++) {
			inventory.setItem(i, Items.getItemColored(Material.STAINED_GLASS_PANE, " ", (byte) 8, true));
		}
		for (int i = 0; i < 9; i++) {
			inventory.setItem(i, Items.getItemColored(Material.STAINED_GLASS_PANE, " ", (byte) 7, true));
		}
		for (int i = 9; i < 37; i += 9) {
			inventory.setItem(i, Items.getItemColored(Material.STAINED_GLASS_PANE, " ", (byte) 7, true));
		}
		for (int i = 17; i < 36; i += 9) {
			inventory.setItem(i, Items.getItemColored(Material.STAINED_GLASS_PANE, " ", (byte) 7, true));
		}
		if (!GameState.isState(GameState.STARTING)) {
			inventory.setItem(40, Items.getItem(Material.EMERALD_BLOCK, ChatColor.GREEN + "Lancer la partie", true));
		} else {
			inventory.setItem(40, Items.getItem(Material.REDSTONE_BLOCK, ChatColor.RED + "Annuler le lancement", true));
		}

		inventory.setItem(11, Items.getItem(Material.PAPER, ChatColor.AQUA + "Modes de jeu", true));
		inventory.setItem(13, Items.getItem(Material.PAPER, ChatColor.AQUA + "Règles", true));
		inventory.setItem(15, Items.getItem(Material.NAME_TAG, ChatColor.AQUA + "Scenarios", true));
		inventory.setItem(44, Items.customItem(new ItemStack(Material.EMERALD), ChatColor.WHITE + "Whitelist",
				Collections.singletonList(ChatColor.GRAY + "Status: " + ChatColor.RED + "OFF"), null));
		if (Bukkit.hasWhitelist())
			inventory.setItem(44, Items.customItem(new ItemStack(Material.REDSTONE), ChatColor.WHITE + "Whitelist",
					Collections.singletonList(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "ON"), null));


		ItemsCreator ic =
				new ItemsCreator(Material.SKULL_ITEM, I18n.tl("guis.main.maxPlayers"),
						Collections.singletonList(I18n.tl("guis.main.maxPlayersLore")));
		inventory.setItem(19, ic.createHead("MHF_Golem"));
		inventory.setItem(29, Items.getItem(Material.PAPER, ChatColor.YELLOW + "Nom du serveur", true));
		inventory.setItem(33, new ItemsCreator(Material.GRASS, "§eParamètre du monde", Collections.singletonList(""), 1).create());
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
				if(!gameManager.isPregen()){
					player.sendMessage("§cErreur: La map n'a pas été prégénéré !");
					return;
				}
				GameState.setCurrentState(GameState.STARTING);
				new HostConfig(player).show();
				new BukkitRunnable() {
					private int timer = 10;
					private int ticks = 20;
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
							if(ticks <= 0){
								timer--;
								ticks = 20;
								for(Player player: Bukkit.getOnlinePlayers()) {
									player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
								}
							}
							ticks--;
						} else if(GameState.isState(GameState.LOBBY)) {
							cancel();
						}

					}
				}.runTaskTimer(UhcHost.getInstance(), 0L, 1L);
			}
			if(current.getItemMeta().getDisplayName().equalsIgnoreCase("§eParamètre du monde")){
				player.closeInventory();
				new WorldGui(player).show();
			}
			if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Annuler le lancement")) {
				GameState.setCurrentState(GameState.LOBBY);
				new HostConfig(player).show();
			}
			if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "Whitelist")) {
				Bukkit.setWhitelist(!Bukkit.hasWhitelist());
				new HostConfig(player).show();
			}
			if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Règles")){
				player.closeInventory();
				new RulesGuiHost(player).show();
			}

			if(current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Scenarios")) {
				player.closeInventory();
				new ScenariosGui(player).show();
			}
			if(current.getItemMeta().getDisplayName().equalsIgnoreCase(I18n.tl("guis.main.maxPlayers"))) {
				player.closeInventory();
				new MaxPlayersGui(player).show();
			}
			if(current.getItemMeta().getDisplayName().equalsIgnoreCase("§eNom du serveur")) {
				player.closeInventory();
				new SetNameUHCGui(UhcHost.getInstance()).open(player);
			}
			if(current.getItemMeta().getDisplayName().equalsIgnoreCase("§bModes de jeu")) {
				player.closeInventory();
				new ModesGui(player).show();
			}

		}
	}


	@EventHandler
	public void onClick(InventoryCloseEvent event) {
		if (event.getInventory().equals(inventory))
			HandlerList.unregisterAll(this);
	}
}