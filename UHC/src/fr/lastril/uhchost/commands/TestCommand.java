package fr.lastril.uhchost.commands;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.tools.API.ClassUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.List;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(player.isOp()){
                List<Location> blockLocations = ClassUtils.getCircle(player.getLocation(), 25, 10);
                blockLocations.forEach(location -> {
                    location.getBlock().setType(Material.BEDROCK);
                });
                Firework f = player.getWorld().spawn(player.getLocation(), Firework.class);
                FireworkMeta fm = f.getFireworkMeta();
                fm.addEffect(FireworkEffect.builder()
                        .flicker(false)
                        .trail(false)
                        .withColor(Color.BLUE)
                        .withColor(Color.fromRGB(5592575))
                        .withFade(Color.fromRGB(5592575))
                        .build());
                fm.setPower(0);
                f.setFireworkMeta(fm);
                f.detonate();
                player.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§a§lFin du test.");
            }
        }
        return false;
    }

}
