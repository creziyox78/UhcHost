package fr.lastril.uhchost.tools.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import fr.lastril.uhchost.UhcHost;

public abstract class Gui implements Listener {
	public Player player;

	public int size;

	public InventoryType type;

	public String name;

	public static Inventory inventory;

	public Gui(Player player, int size, String name) {
		this.player = player;
		this.size = size;
		this.name = name;
		inventory = Bukkit.createInventory(null, size, name);
		Bukkit.getPluginManager().registerEvents(this, (Plugin) UhcHost.getInstance());
	}

	public Gui(Player player, InventoryType type, String name) {
		this.player = player;
		this.type = type;
		this.name = name;
		inventory = Bukkit.createInventory(null, type, name);
		Bukkit.getPluginManager().registerEvents(this, (Plugin) UhcHost.getInstance());
	}

	public void show() {
		this.player.openInventory(inventory);
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getSize() {
		return this.size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public InventoryType getType() {
		return this.type;
	}

	public void setType(InventoryType type) {
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
	    Gui.inventory = inventory;
	}
}
