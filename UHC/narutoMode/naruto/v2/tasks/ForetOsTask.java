package fr.lastril.uhchost.modes.naruto.v2.tasks;

import fr.atlantis.api.utils.FormatTime;
import fr.atlantis.api.utils.Title;
import fr.maygo.uhc.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class ForetOsTask extends BukkitRunnable {

    private final UUID playerID;
    private final List<Block> blocks;

    private int timer = 3 * 60;

    public ForetOsTask(UUID playerID, List<Block> blocks) {
        this.playerID = playerID;
        this.blocks = blocks;
    }

    @Override
    public void run() {
        Player player = Bukkit.getPlayer(playerID);
        if (this.timer == 0) {
            blocks.forEach(block -> block.setType(Material.AIR));
            if (player != null) {
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Votre forêt s'est détruite !");
            }
            cancel();
        }
        if (player != null) {
            ActionBar.sendMessage(player, "§cForêt en Os : " + new FormatTime(this.timer));
        }
        this.timer--;
    }
}
