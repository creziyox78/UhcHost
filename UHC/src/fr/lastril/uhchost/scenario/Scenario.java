package fr.lastril.uhchost.scenario;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;

import java.util.List;

public abstract class Scenario implements Listener {
	private final String name;

	private final List<String> descritpion;

	private final Material type;

	private byte data;

	private Class<? extends IQuickInventory> gui;

	public Scenario(String name, List<String> descritpion, Material type) {
		this.name = ChatColor.AQUA + name;
		this.descritpion = descritpion;
		this.type = type;
		Bukkit.getPluginManager().registerEvents(this, UhcHost.getInstance());
	}

	public Scenario(String name, List<String> descritpion, Material type, byte data) {
		this.name = ChatColor.AQUA +name;
		this.descritpion = descritpion;
		this.type = type;
		this.data = data;
		Bukkit.getPluginManager().registerEvents(this, UhcHost.getInstance());
	}

	public Scenario(String name, List<String> descritpion, Material type, Class<? extends IQuickInventory> gui) {
		this.name = ChatColor.AQUA +name;
		this.descritpion = descritpion;
		this.type = type;
		this.gui = gui;
		Bukkit.getPluginManager().registerEvents(this, UhcHost.getInstance());
	}

	public Scenario(String name, List<String> descritpion, Material type, byte data, Class<? extends IQuickInventory> gui) {
		this.name = ChatColor.AQUA + name;
		this.descritpion = descritpion;
		this.type = type;
		this.data = data;
		this.gui = gui;
		Bukkit.getPluginManager().registerEvents(this, UhcHost.getInstance());
	}

	public String getName() {
		return this.name;
	}

	public List<String> getDescritpion() {
		return this.descritpion;
	}

	public Material getType() {
		return this.type;
	}

	public byte getData() {
		return this.data;
	}

	public Class<? extends IQuickInventory> getGui() {
		return this.gui;
	}
}
