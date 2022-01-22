package fr.lastril.uhchost.scenario;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ScenariosGui extends IQuickInventory {

	public ScenariosGui() {
		super(I18n.tl("guis.scenarios.name"), 54);
	}


	@Override
	public void contents(QuickInventory inv) {
		inv.updateItem("scenarios", taskUpdate -> {
			int index = -1;
			for (Scenarios scenario : Scenarios.values()) {
				index++;
				if ((UhcHost.getInstance()).getGamemanager().hasScenario(scenario.getScenario())) {
					if (scenario.getScenario().getData() != 0) {
						inv.setItem(
								(new ItemsCreator(scenario.getScenario().getType(), scenario.getScenario().getName(),scenario.getScenario().getDescritpion(), 1, scenario.getScenario().getData())).create(), onClick -> {
									(UhcHost.getInstance()).getGamemanager().removeScenario(scenario.getScenario());
									onClick.getEvent().getCurrentItem().setAmount(-1);
									Bukkit.broadcastMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§cDésactivation du scénario " + scenario.getScenario().getName() + ".");
								}, index);

					} else {
						inv.setItem((new ItemsCreator(scenario.getScenario().getType(),
								scenario.getScenario().getName(), scenario.getScenario().getDescritpion(), 1)).create(), onClick -> {
							(UhcHost.getInstance()).getGamemanager().removeScenario(scenario.getScenario());
							onClick.getEvent().getCurrentItem().setAmount(-1);
							Bukkit.broadcastMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§cDésactivation du scénario " + scenario.getScenario().getName() + ".");
						},index);
					}

				} else if (scenario.getScenario().getData() != 0) {
					inv.setItem(
							(new ItemsCreator(scenario.getScenario().getType(), scenario.getScenario().getName(),
									scenario.getScenario().getDescritpion(), -1, scenario.getScenario().getData()))
									.create(), onClick -> {
								if (scenario.getScenario().getGui() != null) {
									onClick.getPlayer().closeInventory();
									try {
										Class<?> clazz = scenario.getScenario().getGui();
										Constructor<?> ctor;
										ctor = clazz.getConstructor(Player.class);
										Object object = ctor.newInstance(onClick.getPlayer());
										Method method = object.getClass().getMethod("show");
										method.invoke(object);
									} catch (NoSuchMethodException
											| IllegalAccessException
											| InstantiationException
											| InvocationTargetException e) {
										e.printStackTrace();
									}
								}
								UhcHost.getInstance().getGamemanager().addScenario(scenario.getScenario());
								onClick.getEvent().getCurrentItem().setAmount(1);
								Bukkit.broadcastMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§aActivation du scénario " + scenario.getScenario().getName() + ".");
							}, index);
				} else {
					inv.setItem((new ItemsCreator(scenario.getScenario().getType(),
							scenario.getScenario().getName(), scenario.getScenario().getDescritpion(), -1)).create(), onClick -> {
						if (scenario.getScenario().getGui() != null) {
							try {
								scenario.getScenario().getGui().newInstance().open(onClick.getPlayer());
							} catch (InstantiationException | IllegalAccessException e) {
								e.printStackTrace();
							}
						}
						UhcHost.getInstance().getGamemanager().addScenario(scenario.getScenario());
						onClick.getEvent().getCurrentItem().setAmount(1);
						Bukkit.broadcastMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§aActivation du scénario " + scenario.getScenario().getName() + ".");
					}, index);
				}
			}

			inv.setItem((new ItemsCreator(Material.DIODE, I18n.tl("Recharger les scénarios"),
					Arrays.asList("", "§7Met à jour les scénarios activés",
							"§7et désactivés en pleine partie."))).create(), onClick -> {
				onClick.getPlayer().sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§6Rechargement des scénarios...");
				UhcHost.debug("Reloading scenarios...");
				UhcHost main = UhcHost.getInstance();
				for(Scenarios scenario : Scenarios.values()){
					HandlerList.unregisterAll(scenario.getScenario());
				}
				main.getGamemanager().getScenarios().forEach(scenario -> main.getServer().getPluginManager().registerEvents(scenario, main));
				onClick.getPlayer().sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§aScénarios mise à jour !");
				UhcHost.debug("Reloaded all scenarios !");
			},52);
			inv.setItem((new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"),
					Arrays.asList(""))).create(), onClick -> {
				new HostConfig().open(onClick.getPlayer());
			},53);
		});

	}
}
