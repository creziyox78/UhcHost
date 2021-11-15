package fr.lastril.uhchost.scenario;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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
			int index = 0;
			for (Scenarios scenario : Scenarios.values()) {
				index++;
				if ((UhcHost.getInstance()).getGamemanager().hasScenario(scenario.getScenario())) {
					if (scenario.getScenario().getData() != 0) {
						inv.setItem(
								(new ItemsCreator(scenario.getScenario().getType(), scenario.getScenario().getName(),scenario.getScenario().getDescritpion(), 1, scenario.getScenario().getData())).create(), onClick -> {
									(UhcHost.getInstance()).getGamemanager().removeScenario(scenario.getScenario());
									onClick.getEvent().getCurrentItem().setAmount(-1);
								}, index);

					} else {
						inv.setItem((new ItemsCreator(scenario.getScenario().getType(),
								scenario.getScenario().getName(), scenario.getScenario().getDescritpion(), 1)).create(), onClick -> {
							(UhcHost.getInstance()).getGamemanager().removeScenario(scenario.getScenario());
							onClick.getEvent().getCurrentItem().setAmount(-1);
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
					}, index);
				}
			}
			inv.setItem((new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"),
					Arrays.asList(""))).create(), onClick -> {
				new HostConfig().open(onClick.getPlayer());
			},53);
		});

	}
}
