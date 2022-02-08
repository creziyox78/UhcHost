package fr.lastril.uhchost.world;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.BiomeState;
import fr.lastril.uhchost.game.rules.BlocksRule;
import fr.lastril.uhchost.world.ores.OresGenerator;
import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.WorldChunkManager;
import net.minecraft.server.v1_8_R3.WorldProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

import java.lang.reflect.Field;

public class WorldInit implements Listener {

    private final UhcHost main;
    private final int x;
    private final int z;
    public WorldInit(UhcHost main, int x, int z) {
        this.main = main;
        this.x = x;
        this.z = z;
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onWorldInit(WorldInitEvent event) {
        World world = event.getWorld();
        if(!world.getName().equalsIgnoreCase("game"))
            return;
        UhcHost.debug("§eChecking boost ores...");
        if(main.getGamemanager().isBoostOres()){
            OresGenerator oresGenerator = new OresGenerator();
            for (BlocksRule rule : main.gameManager.getBlocksRules()){
                oresGenerator.registerRule(rule);
            }
            world.getPopulators().add(oresGenerator);
            UhcHost.debug("§aBoosted ores !");
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Changing biome center...");
        net.minecraft.server.v1_8_R3.World craftWorld = ((CraftWorld) world).getHandle();
        WorldProvider worldProvider = craftWorld.worldProvider;
        try {
            Field field = WorldProvider.class.getDeclaredField("c");
            field.setAccessible(true);
            field.set(worldProvider, new WorldChunkManagerPatched((WorldChunkManager) field.get(worldProvider), x, z, main.gameManager.getBiomeState()));
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Changed biome center !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static class WorldChunkManagerPatched extends WorldChunkManager {

        private final int cx;
        private final int cz;
        private final BiomeState biomeChoosed;

        public WorldChunkManagerPatched(WorldChunkManager worldChunkManager, int x, int z, BiomeState biomeChoosed) {
            this.biomeChoosed = biomeChoosed;
            this.cx = x;
            this.cz = z;
            Field field = null;
            try {
                field = WorldChunkManager.class.getDeclaredField("b");
                field.setAccessible(true);
                field.set(this, field.get(worldChunkManager));

                field = WorldChunkManager.class.getDeclaredField("c");
                field.setAccessible(true);
                field.set(this, field.get(worldChunkManager));

                field = WorldChunkManager.class.getDeclaredField("d");
                field.setAccessible(true);
                field.set(this, field.get(worldChunkManager));

                field = WorldChunkManager.class.getDeclaredField("e");
                field.setAccessible(true);
                field.set(this, field.get(worldChunkManager));

                field = WorldChunkManager.class.getDeclaredField("f");
                field.setAccessible(true);
                field.set(this, field.get(worldChunkManager));


            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public BiomeBase getBiome(BlockPosition blockPosition, BiomeBase biomeBase) {

            BiomeBase biome = super.getBiome(blockPosition, biomeBase);

            if ((blockPosition.getX()-cx) * (blockPosition.getX()-cx) + (blockPosition.getX()-cz) * (blockPosition.getZ()-cz) < 300 * 300) {
                if(BiomeBase.RIVER != biome)
                    biome = biomeChoosed.getBiomeBase();
            }

            return biome;
        }


        @Override
        public BiomeBase[] getBiomeBlock(BiomeBase[] paramArrayOfBiomeBase, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {

            paramArrayOfBiomeBase = super.getBiomeBlock(paramArrayOfBiomeBase, paramInt1, paramInt2, paramInt3, paramInt4);

            for (int x = 0; x < paramInt3; x++) {
                for (int z = 0; z < paramInt4; z++) {
                    if ((paramInt1 + x-cx) * (paramInt1 + x-cx) + (paramInt2 + z-cz) * (paramInt2 + z-cz) < 300 * 300) {
                        if(BiomeBase.RIVER != paramArrayOfBiomeBase[x + z * paramInt4])
                            paramArrayOfBiomeBase[x + z * paramInt4] = biomeChoosed.getBiomeBase();
                    }
                }
            }

            return paramArrayOfBiomeBase;

        }
    }

}
