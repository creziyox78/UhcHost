package fr.lastril.uhchost.test;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.test.utils.EntityUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class TestCommand implements CommandExecutor {

    private final UhcHost plugin = UhcHost.getInstance();

    private final EntityUtil entityUtil = new EntityUtil(plugin);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            if(player.isOp()){
                new BukkitRunnable() {
                    int distance = 0;
                    final Location center = player.getLocation();
                    final Vector vector = player.getLocation().getDirection().normalize().multiply(0.3).setY(0);

                    @Override
                    public void run() {
                        if(distance < 20){
                            for (int i = 0; i < 2; i++) {
                                if (this.center.getBlock().getType().isSolid())
                                    this.center.add(0.0D, 1.0D, 0.0D);
                                if(!this.center.clone().subtract(0.0D, 1.0D, 0.0D).getBlock().getType().isSolid() && !this.center.clone().subtract(0.0D, 1.0D, 0.0D).getBlock().getType().toString().contains("SLAB"))
                                    this.center.subtract(0.0D, 1.0D, 0.0D);
                                entityUtil.sendBlizzard(player, center.add(vector));
                                entityUtil.sendBlizzard(player, center.add(vector));
                            }
                            distance++;
                        } else {
                            this.cancel();
                        }

                    }
                }.runTaskTimer(plugin, 0, 2);


                player.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§a§lFin du test.");
            }
        }
        return false;
    }
}
