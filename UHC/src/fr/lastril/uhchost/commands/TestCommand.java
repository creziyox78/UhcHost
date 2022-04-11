package fr.lastril.uhchost.commands;

import fr.lastril.uhchost.enums.Messages;
import net.minecraft.server.v1_8_R3.EntityOcelot;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(player.isOp()){
                PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving(new EntityOcelot(((CraftPlayer) player).getHandle().getWorld()));
                try {
                    Field idField = spawn.getClass().getDeclaredField("a");
                    idField.setAccessible(true);
                    idField.set(spawn, player.getEntityId());
                } catch (Exception e) {
                }
                PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(player.getEntityId());

                for (Player p : Bukkit.getOnlinePlayers()) {
                    if(player != p){
                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawn);
                    }
                }
                player.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§a§lFin du test.");
            }
        }
        return false;
    }
}
