package fr.lastril.uhchost.player.events.menu;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.game.team.ToGui;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.inventory.CustomInv;
import fr.lastril.uhchost.tools.inventory.guis.BorderGui;
import fr.lastril.uhchost.tools.inventory.guis.HostConfig;
import fr.lastril.uhchost.tools.inventory.guis.NetherGui;
import fr.lastril.uhchost.tools.inventory.guis.PvpTimeGui;

public class InteractRules implements Listener {
	@EventHandler
	public void onInteract(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		ItemStack current = e.getCurrentItem();
		Player player = (Player) e.getWhoClicked();
		GameManager gamemanager = UhcHost.getInstance().getGamemanager();
		if (gamemanager.getGameState() != GameState.LOBBY && gamemanager.getGameState() != GameState.STARTING)
			return;
		if (current == null)
			return;
		if (!current.hasItemMeta())
			return;
		if (inv.getName() == null)
			return;
		if (inv.getName().equalsIgnoreCase(ChatColor.AQUA + "Règles")) {
			e.setCancelled(true);
			if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Inventaire de départ")) {
				if (gamemanager.isEditInv()) {
					player.sendMessage(ChatColor.RED + "L'inventaire est en cours d'édition !");
					return;
				}
				player.closeInventory();
				gamemanager.setEditInv(true);
				player.sendMessage(ChatColor.AQUA + "/h enchant: Permet d'enchanter l'objet dans la main.");
				player.sendMessage(ChatColor.AQUA + "/h save: Sauvegarde l'inventaire.");
				player.setGameMode(GameMode.CREATIVE);
				CustomInv.restoreInventory(player);
			} else if (current.getItemMeta().getDisplayName().equalsIgnoreCase(I18n.tl("guis.main.border", new String[0]))) {
				new BorderGui(player).show();
			} else if (current.getItemMeta().getDisplayName().equalsIgnoreCase(I18n.tl("guis.main.pvp", new String[0]))) {
				new PvpTimeGui(player).show();
			} else if (current.getItemMeta().getDisplayName().equalsIgnoreCase(I18n.tl("guis.main.teams", new String[0]))) {
				new ToGui(player).show();
			} else if(current.getItemMeta().getDisplayName().equalsIgnoreCase(I18n.tl("guis.main.nether", new String[0]))) {
				(new NetherGui(player)).show();
			} else if(current.getItemMeta().getDisplayName().equalsIgnoreCase(I18n.tl("guis.back", "")))
				player.openInventory(HostConfig.Main());
		}
	}
}
