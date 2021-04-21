package fr.lastril.uhchost.scenario.gui;

import java.util.Arrays;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import fr.lastril.uhchost.tools.creators.BannerCreator;
import fr.lastril.uhchost.tools.inventory.Gui;

public class TimerGui extends Gui {

	public TimerGui(Player player, String name) {
		    super(player, 9, name);
		    BannerCreator bc = new BannerCreator("§c-10", Arrays.asList(""), 1, true);
		    bc.setBaseColor(DyeColor.RED);
		    inventory.setItem(0, bc.create());
		    bc = new BannerCreator("§c-5", Arrays.asList(""), 1, true);
		    bc.setBaseColor(DyeColor.RED);
		    inventory.setItem(1, bc.create());
		    bc = new BannerCreator("§c-1", Arrays.asList(""), 1, true);
		    bc.setBaseColor(DyeColor.RED);
		    inventory.setItem(2, bc.create());
		    bc = new BannerCreator("§a+1", Arrays.asList(""), 1, true);
		    bc.setBaseColor(DyeColor.GREEN);
		    inventory.setItem(6, bc.create());
		    bc = new BannerCreator("§a+5", Arrays.asList(""), 1, true);
		    bc.setBaseColor(DyeColor.GREEN);
		    inventory.setItem(7, bc.create());
		    bc = new BannerCreator("§a+10", Arrays.asList(""), 1, true);
		    bc.setBaseColor(DyeColor.GREEN);
		    inventory.setItem(8, bc.create());
		  }

}
