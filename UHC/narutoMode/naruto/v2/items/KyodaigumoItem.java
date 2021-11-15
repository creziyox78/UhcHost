package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.orochimaru.Kidomaru;
import fr.maygo.uhc.obj.PlayerManager;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.scheduler.BukkitRunnable;

public class KyodaigumoItem extends QuickItem {

    private boolean spiderAlive;

    private Player nearestPlayer = null;

    private Spider spider;

    public KyodaigumoItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§5Kyodaigumo");
        super.onClick(onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Kidomaru) {
                    if (!main.getNarutoV2Manager().isInSamehada(playerClick.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownKyodaigumo() <= 0) {
                            double nearestDistance = Integer.MAX_VALUE;
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                PlayerManager playersPlayerManager = main.getPlayerManager(players.getUniqueId());
                                if (players.getGameMode() != GameMode.SPECTATOR && players != playerClick && playersPlayerManager.isAlive()) {
                                    if (playerClick.getLocation().distance(players.getLocation()) < nearestDistance) {
                                        nearestPlayer = players;
                                        nearestDistance = playerClick.getLocation().distance(players.getLocation());
                                    }
                                }
                            }

                            World world = playerClick.getLocation().getWorld();
                            spider = world.spawn(playerClick.getLocation().add(0, 3, 0), Spider.class);

                            EntityLiving nmsEntity = ((CraftLivingEntity) spider).getHandle();
                            NBTTagCompound tag = nmsEntity.getNBTTag();
                            if (tag == null) {
                                tag = new NBTTagCompound();
                            }
                            spider.setCustomName("§5Kyodaigumo");
                            nmsEntity.c(tag);
                            tag.setInt("NoAI", 1);
                            tag.setInt("NoGravity", 0);
                            tag.setBoolean("Invulnerable", true);
                            nmsEntity.f(tag);

                            spiderAlive = true;
                            playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                            new BukkitRunnable() {
                                int tickSpawnSpider = 1;

                                @Override
                                public void run() {
                                    if (spiderAlive) {
                                        if (tickSpawnSpider <= 0) {
                                            for (int x = -10; x <= 10; x += 5) {
                                                for (int z = -10; z <= 10; z += 5) {
                                                    CaveSpider caveSpider = world.spawn(new Location(world, spider.getLocation().getBlockX() + x, world.getHighestBlockYAt(spider.getLocation()) + 2, spider.getLocation().getBlockZ() + z), CaveSpider.class);
                                                    caveSpider.setCustomName("§5Enfant de Kyodaigumo");
                                                    caveSpider.setTarget(nearestPlayer);
                                                }
                                            }
                                            tickSpawnSpider = 20 * 20;
                                        }
                                    } else {
                                        spider.remove();
                                        cancel();
                                    }
                                    tickSpawnSpider--;
                                }
                            }.runTaskTimer(main, 0, 1);

                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    spiderAlive = false;
                                    spider.remove();
                                }
                            }.runTaskLater(main, 20 * 60);

                            PlayerManager.setRoleCooldownKyodaigumo(60 * 15);
                            PlayerManager.sendTimer(playerClick, PlayerManager.getRoleCooldownKyodaigumo(), this.toItemStack());
                        } else {
                            playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownKyodaigumo()));
                        }
                    } else {
                        playerClick.sendMessage(
                                Messages.NARUTO_PREFIX.getMessage() + "§eVous êtes sous l'effet de §cSamehada§e.");
                    }
                } else {
                    playerClick.sendMessage(Messages.not("Kidomaru"));
                }
            }
        });
    }

}
