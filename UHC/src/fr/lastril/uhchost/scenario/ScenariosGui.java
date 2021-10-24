package fr.lastril.uhchost.scenario;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ScenariosGui extends Gui {

	public ScenariosGui(Player player) {
		super(player, 54, I18n.tl("guis.scenarios.name").replace("&", "§"));
		for (Scenarios scenario : Scenarios.values()) {
			if ((UhcHost.getInstance()).getGamemanager().hasScenario(scenario.getScenario())) {
				if (scenario.getScenario().getData() != 0) {
					inventory.addItem(
							(new ItemsCreator(scenario.getScenario().getType(), scenario.getScenario().getName(),scenario.getScenario().getDescritpion(), 1, scenario.getScenario().getData())).create());
				} else {
					inventory.addItem((new ItemsCreator(scenario.getScenario().getType(),
							scenario.getScenario().getName(), scenario.getScenario().getDescritpion(), 1)).create());
				}
			} else if (scenario.getScenario().getData() != 0) {
				inventory.addItem(
						(new ItemsCreator(scenario.getScenario().getType(), scenario.getScenario().getName(),
								scenario.getScenario().getDescritpion(), -1, scenario.getScenario().getData()))
										.create());
			} else {
				inventory.addItem((new ItemsCreator(scenario.getScenario().getType(),
						scenario.getScenario().getName(), scenario.getScenario().getDescritpion(), -1)).create());
			}
		}
		inventory.setItem(53, (new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"),
				Arrays.asList(""))).create());
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getClickedInventory().equals(inventory)) {
			ItemStack is = event.getCurrentItem();
			if (is == null || is.getType() == Material.AIR)
				return;
			event.setCancelled(true);
			if (is.getType() == Material.BARRIER) {
				event.getWhoClicked().closeInventory();
				new HostConfig((Player) event.getWhoClicked()).show();
			} else {
				Scenarios scenario = Scenarios.getByName(event.getCurrentItem().getItemMeta().getDisplayName());
				if ((UhcHost.getInstance()).getGamemanager().hasScenario(scenario.getScenario())) {
					(UhcHost.getInstance()).getGamemanager().removeScenario(scenario.getScenario());
					event.getCurrentItem().setAmount(-1);
				} else {
					if (scenario.getScenario().getGui() != null) {
						this.player.closeInventory();
						try {
							Class<?> clazz = scenario.getScenario().getGui();
							Constructor<?> ctor = null;
							ctor = clazz.getConstructor(Player.class);
							Object object = ctor.newInstance(this.player);
							Method method = object.getClass().getMethod("show");
							method.invoke(object);
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
					UhcHost.getInstance().getGamemanager().addScenario(scenario.getScenario());
					event.getCurrentItem().setAmount(1);
				}
			}
		}
	}

	@EventHandler
	public void onClick(InventoryCloseEvent event) {
		if (event.getInventory().equals(inventory))
			HandlerList.unregisterAll(this);
	}

}
