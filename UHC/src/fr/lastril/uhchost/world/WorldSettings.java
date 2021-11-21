package fr.lastril.uhchost.world;

import org.bukkit.World;
import org.bukkit.WorldBorder;

import java.io.File;

public class WorldSettings {
	public static void setSettings(World world) {
		WorldBorder border = world.getWorldBorder();
		border.setCenter(0.0D, 0.0D);
		border.setDamageAmount(3.0D);
		border.setDamageBuffer(10.0D);
		border.setWarningDistance(20);
		border.setSize(200.0D);
		world.setPVP(true);
		world.setWeatherDuration(0);
		world.setThundering(false);
		world.setTime(0L);
		world.setGameRuleValue("reducedDebugInfo", "false");
		world.setGameRuleValue("keepInventory", "false");
		world.setGameRuleValue("naturalRegeneration", "false");
		world.getWorldBorder().setCenter(0.5D, 0.5D);
		world.setSpawnLocation(0, 151, 0);
	}

	public static boolean deleteWorld(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteWorld(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return path.delete();
	}
}
