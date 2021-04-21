package fr.lastril.uhchost.scenario.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import fr.lastril.uhchost.player.events.GameStartEvent;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;

public class RandomCraft extends Scenario {

	public RandomCraft() {
		super("Random Craft", Arrays.asList(I18n.tl("scenarios.randomcraft.lore", new String[0]), I18n.tl("scenarios.randomcraft.lore1", new String[0])), Material.WORKBENCH);
	}

	public Map<Material, Material> rdmCrafts = new HashMap<>();

	@EventHandler
	public void onRandomiseCraft(GameStartEvent e) {
		randomize(rdmCrafts);
	}

	@EventHandler
	public void OnFurnaceSmelt(FurnaceSmeltEvent e) {
		if (rdmCrafts.containsKey(e.getResult().getType())) {
			ItemStack it = e.getResult();
			it.setType(rdmCrafts.get(e.getResult().getType()));
			e.setResult(it);
		}
	}

	@EventHandler
	public void OnItemCraft(PrepareItemCraftEvent e) {
		ItemStack item = e.getInventory().getItem(0);
		if (rdmCrafts.containsKey(item.getType())) {
			ItemStack newit = item;
			newit.setType(rdmCrafts.get(newit.getType()));
			e.getInventory().setItem(0, newit);
		}
	}

	public void randomize(Map<Material, Material> rdmizer) {
		Material[] values = Material.values();
		List<Material> keys = new ArrayList<>();
		List<Material> finalloots = new ArrayList<>();
		for (int i = 0; i <= values.length - 1; i++) {
			if (isItem(values[i])) {
				keys.add(values[i]);
				finalloots.add(values[i]);
			}
		}
		int size = keys.size();
		for (int j = 0; j <= size - 1; j++) {
			int rdm = (new Random()).nextInt(keys.size());
			rdmizer.put(keys.get(0), finalloots.get(rdm));
			keys.remove(0);
			finalloots.remove(rdm);
		}
	}

	public boolean isItem(Material mat) {
		boolean result = true;
		switch (mat) {
		case AIR:
		case BEDROCK:
		case WATER:
		case STATIONARY_WATER:
		case LAVA:
		case STATIONARY_LAVA:
		case BED_BLOCK:
		case PISTON_EXTENSION:
		case PISTON_MOVING_PIECE:
		case DOUBLE_STEP:
		case FIRE:
		case MOB_SPAWNER:
		case REDSTONE_WIRE:
		case CROPS:
		case SOIL:
		case BURNING_FURNACE:
		case SIGN_POST:
		case WALL_SIGN:
		case IRON_DOOR_BLOCK:
		case GLOWING_REDSTONE_ORE:
		case REDSTONE_TORCH_OFF:
		case SUGAR_CANE_BLOCK:
		case PORTAL:
		case CAKE_BLOCK:
		case DIODE_BLOCK_OFF:
		case DIODE_BLOCK_ON:
		case PUMPKIN_STEM:
		case MELON_STEM:
		case NETHER_WARTS:
		case BREWING_STAND:
		case CAULDRON:
		case ENDER_PORTAL:
		case REDSTONE_LAMP_ON:
		case WOOD_DOUBLE_STEP:
		case COCOA:
		case TRIPWIRE:
		case COMMAND:
		case FLOWER_POT:
		case CARROT:
		case POTATO:
		case SKULL:
		case REDSTONE_COMPARATOR_OFF:
		case REDSTONE_COMPARATOR_ON:
		case BARRIER:
		case STANDING_BANNER:
		case WALL_BANNER:
		case DAYLIGHT_DETECTOR_INVERTED:
		case DOUBLE_STONE_SLAB2:
		case SPRUCE_DOOR:
		case BIRCH_DOOR:
		case JUNGLE_DOOR:
		case DARK_OAK_DOOR:
		case COMMAND_MINECART:
			result = false;
			break;
		default:
			break;
		}
		return result;
	}

}
