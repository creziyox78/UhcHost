package fr.lastril.uhchost.modes.naruto.v2.tasks;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.FormatTime;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class EpeeOsTask extends BukkitRunnable {

    private final UUID playerID;
    private final ItemStack item;

    private int timer = 1*60;

    public EpeeOsTask(UUID playerID, ItemStack item) {
        this.playerID = playerID;
        this.item = item;
    }

    @Override
    public void run() {
        Player player = Bukkit.getPlayer(playerID);
        if(this.timer == 0){
            if(player != null){
                player.getInventory().remove(Material.BONE);
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Votre épée s'est détruite !");
            }
            cancel();
        }
        if(player != null){
            ActionBar.sendMessage(player, "§cÉpée en Os : "+new FormatTime(this.timer));
        }
        this.timer--;
    }
}
