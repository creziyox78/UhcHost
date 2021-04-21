package fr.lastril.uhchost.scenario;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.tools.inventory.Gui;

public abstract class Scenario implements Listener {
	private String name;

	private List<String> descritpion;

	private Material type;

	private byte data;

	private Class<? extends Gui> gui;

	public Scenario(String name, List<String> descritpion, Material type) {
		this.name = ChatColor.AQUA + name;
		this.descritpion = descritpion;
		this.type = type;
		Bukkit.getPluginManager().registerEvents(this, (Plugin) UhcHost.getInstance());
	}

	public Scenario(String name, List<String> descritpion, Material type, byte data) {
		this.name = ChatColor.AQUA +name;
		this.descritpion = descritpion;
		this.type = type;
		this.data = data;
		Bukkit.getPluginManager().registerEvents(this, (Plugin) UhcHost.getInstance());
	}

	public Scenario(String name, List<String> descritpion, Material type, Class<? extends Gui> gui) {
		this.name = ChatColor.AQUA +name;
		this.descritpion = descritpion;
		this.type = type;
		this.gui = gui;
		Bukkit.getPluginManager().registerEvents(this, (Plugin) UhcHost.getInstance());
	}

	public Scenario(String name, List<String> descritpion, Material type, byte data, Class<? extends Gui> gui) {
		this.name = ChatColor.AQUA + name;
		this.descritpion = descritpion;
		this.type = type;
		this.data = data;
		this.gui = gui;
		Bukkit.getPluginManager().registerEvents(this, (Plugin) UhcHost.getInstance());
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

	public Class<? extends Gui> getGui() {
		return this.gui;
	}
}
