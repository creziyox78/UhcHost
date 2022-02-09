package fr.lastril.uhchost.modes.bleach.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.ToshiroHitsugaya;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.Cuboid;
import fr.lastril.uhchost.world.tasks.ChunkLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CmdHiver implements ModeSubCommand {

    private final UhcHost main;
    private BukkitTask task;
    int indexList = 0, indexMap =0;

    public CmdHiver(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "hiver";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if(playerManager.getRole() instanceof ToshiroHitsugaya){
            ToshiroHitsugaya toshiroHitsugaya = (ToshiroHitsugaya) playerManager.getRole();
            if(bleachPlayerManager.canUsePower()){
                if(playerManager.getRoleCooldownHiver() <= 0){
                    if(!toshiroHitsugaya.hasReachedUse()){
                        toshiroHitsugaya.addHiverUse();
                        Cuboid cuboid = new Cuboid(player.getLocation(), 25, 2);
                        toshiroHitsugaya.setHiverSpace(cuboid);
                        List<Block> blocks = cuboid.getFullBlocks();
                        Map<Block, Material> locationBlockMap = new HashMap<>();
                        Map<Block, Byte> blockByteMap = new HashMap<>();
                        blocks.forEach(block -> {
                            if(block != null && block.getType() != Material.AIR ){
                                locationBlockMap.put(block, block.getType());
                                blockByteMap.put(block, block.getData());
                                block.setType(Material.PACKED_ICE);
                            }
                        });
                        task = Bukkit.getScheduler().runTaskTimer(main, new Runnable() {
                            int timer = 60*20;
                            @Override
                            public void run() {
                                if(timer == 0){
                                    locationBlockMap.keySet().forEach(block -> {
                                        block.setType(locationBlockMap.get(block));
                                        block.setData(blockByteMap.get(block));
                                        indexList++;
                                    });
                                    toshiroHitsugaya.setHiverSpace(null);
                                    task.cancel();
                                }
                                ActionBar.sendMessage(player, "§bHiver : [" + ChunkLoader.getProgressBar(timer/20, 60, 60, '|', ChatColor.AQUA, ChatColor.WHITE) + "§7]");

                                timer--;
                            }
                        }, 0, 1);
                    } else {
                        player.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                    }
                } else {
                    player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownHiver()));
                }
            } else{
                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
            }
        }
        return false;
    }
}
