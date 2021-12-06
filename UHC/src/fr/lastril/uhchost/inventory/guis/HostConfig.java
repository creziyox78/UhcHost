
package fr.lastril.uhchost.inventory.guis;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.inventory.guis.modes.ModesGui;
import fr.lastril.uhchost.inventory.guis.timer.RulesGuiHost;
import fr.lastril.uhchost.inventory.guis.world.WorldGui;
import fr.lastril.uhchost.scenario.ScenariosGui;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;

public class HostConfig extends IQuickInventory {
	public HostConfig() {
		super(ChatColor.GOLD + "Configuration", 9*5);

	}

	@Override
	public void contents(QuickInventory inv) {
		inv.updateItem("update", taskUpdate -> {
			if (!GameState.isState(GameState.STARTING)) {
				inv.setItem(new QuickItem(Material.EMERALD_BLOCK).setName(ChatColor.GREEN + "Lancer la partie").toItemStack(), onClick -> {
					final GameManager gameManager = UhcHost.getInstance().getGamemanager();
					if(!gameManager.isPregen()){
						onClick.getPlayer().sendMessage("§cErreur: La map n'a pas été prégénéré !");
						return;
					}
					GameState.setCurrentState(GameState.STARTING);
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
										player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
									}
								}
								ticks--;
							} else if(GameState.isState(GameState.LOBBY)) {
								cancel();
							}

						}
					}.runTaskTimer(UhcHost.getInstance(), 0L, 1L);
				},40);
			} else {
				inv.setItem(new QuickItem(Material.REDSTONE_BLOCK).setName(ChatColor.RED + "Annuler le lancement").toItemStack(), onClick -> {
					GameState.setCurrentState(GameState.LOBBY);
				},40);
			}

			inv.setItem(new QuickItem(Material.PAPER).setName(ChatColor.AQUA + "Modes de jeu").toItemStack(), onClick -> {
				new ModesGui().open(onClick.getPlayer());
			},11);
			inv.setItem(new QuickItem(Material.PAPER).setName(ChatColor.AQUA + "Règles").toItemStack(), onClick -> {
				new RulesGuiHost().open(onClick.getPlayer());
			},13);
			inv.setItem(new QuickItem(Material.NAME_TAG).setName(ChatColor.AQUA + "Scenarios").toItemStack(), onClick -> {
				new ScenariosGui().open(onClick.getPlayer());
			},15);
			inv.setItem(new QuickItem(Material.EMERALD).setName(ChatColor.WHITE + "Whitelist")
								.setLore(ChatColor.GRAY + "Status: " + ChatColor.RED + "OFF").toItemStack(), onClick -> {
				Bukkit.setWhitelist(!Bukkit.hasWhitelist());
			},44);
			if (Bukkit.hasWhitelist())
				inv.setItem(new QuickItem(Material.REDSTONE).setName(ChatColor.WHITE + "Whitelist")
										.setLore(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "ON").toItemStack(), onClick -> {
					Bukkit.setWhitelist(!Bukkit.hasWhitelist());
				},44);


			ItemsCreator ic =
					new ItemsCreator(Material.SKULL_ITEM, I18n.tl("guis.main.maxPlayers"),
							Collections.singletonList(I18n.tl("guis.main.maxPlayersLore")));
			inv.setItem(ic.createHead("MHF_Golem"), onClick -> {
				new MaxPlayersGui().open(onClick.getPlayer());
			},19);
			inv.setItem(new QuickItem(Material.PAPER).setName(ChatColor.YELLOW + "Nom du serveur").toItemStack(), onClick -> {
				new SetNameUHCGui(UhcHost.getInstance()).open(onClick.getPlayer());
			},29);
			inv.setItem(new ItemsCreator(Material.GRASS, "§eParamètre du monde",
								Collections.singletonList(""), 1).create(), onClick -> {
				new WorldGui().open(onClick.getPlayer());
			},33);
		});
	}
}