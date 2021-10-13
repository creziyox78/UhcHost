package fr.lastril.uhchost.world.schematics;

import fr.lastril.uhchost.UhcHost;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class LobbyPopulator {
	
	
	@SuppressWarnings("deprecation")
	public static ArrayList<Block> generate(Location loc, String filename) {
		ArrayList<Block> chestList = new ArrayList<>();
		try {
			FileInputStream fis = new FileInputStream(new File(UhcHost.instance.getDataFolder(), filename));
			Object nbtData = NBTCompressedStreamTools.class.getMethod("a", new Class[] { InputStream.class })
					.invoke(null, new Object[] { fis });
			Method getShort = nbtData.getClass().getMethod("getShort", new Class[] { String.class });
			Method getByteArray = nbtData.getClass().getMethod("getByteArray", new Class[] { String.class });
			short width = ((Short) getShort.invoke(nbtData, new Object[] { "Width" })).shortValue();
			short height = ((Short) getShort.invoke(nbtData, new Object[] { "Height" })).shortValue();
			short length = ((Short) getShort.invoke(nbtData, new Object[] { "Length" })).shortValue();
			byte[] blocks = (byte[]) getByteArray.invoke(nbtData, new Object[] { "Blocks" });
			byte[] data = (byte[]) getByteArray.invoke(nbtData, new Object[] { "Data" });
			loc.add(0.0D, -1.0D, 0.0D);
			fis.close();
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					for (int z = 0; z < length; z++) {
						int index = y * width * length + z * width + x;
						int b = blocks[index] & 0xFF;
						Material m = Material.getMaterial(b);
						if (m != Material.AIR) {
							Block block = (new Location(loc.getWorld(), (loc.getBlockX() - width / 2 + x),
									(loc.getBlockY() - 10 + y), (loc.getBlockZ() - length / 2 + z))).getBlock();
							block.setType(m, true);
							block.setData(data[index]);
							if (m == Material.CHEST)
								chestList.add(block);
						}
					}
				}
			}
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Loaded schematic!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chestList;
	}
}
