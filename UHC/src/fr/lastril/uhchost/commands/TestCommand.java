package fr.lastril.uhchost.commands;

import com.mojang.authlib.properties.Property;
import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.tools.API.npc.NPC;
import fr.lastril.uhchost.tools.API.npc.NPCManager;
import fr.lastril.uhchost.world.WorldUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

public class TestCommand implements CommandExecutor {

    private final UhcHost main = UhcHost.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            new BukkitRunnable(){

                final NPCManager npcManager = main.getNpcManager();

                final Property skin = npcManager.getPlayerTextures(player);

                int clones = 10;

                double rand = 1;

                @Override
                public void run() {
                    if(clones == 0) cancel();
                    double x = ThreadLocalRandom.current().nextDouble(-rand, rand);
                    double z = ThreadLocalRandom.current().nextDouble(-rand, rand);


                    NPC npc = new NPC(player.getDisplayName(), player.getLocation().clone().add(x, 0, z), skin, null);
                    WorldUtils.spawnParticle(npc.getLocation().clone().add(0, 1, 0), EnumParticle.CLOUD, 0.5f, 1f, 0.5f, 0.1f, 1000);
                    player.getWorld().playSound(npc.getLocation().clone().add(0, 1, 0), Sound.CHICKEN_EGG_POP, 10f, 1f);
                    npc.sendNPC();

                    new BukkitRunnable(){

                        @Override
                        public void run() {
                            WorldUtils.spawnParticle(npc.getLocation().clone().add(0, 1, 0), EnumParticle.CLOUD, 0.5f, 1f, 0.5f, 0.1f, 1000);
                            player.getWorld().playSound(npc.getLocation().clone().add(0, 1, 0), Sound.CHICKEN_EGG_POP, 10f, 1f);
                            npc.removeNPC();
                        }
                    }.runTaskLater(main, 20*5);

                    rand += 1;
                    clones--;
                }
            }.runTaskTimer(main, 0, 5);
            player.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§a§lFin du test.");
        }
        return false;
    }

}
