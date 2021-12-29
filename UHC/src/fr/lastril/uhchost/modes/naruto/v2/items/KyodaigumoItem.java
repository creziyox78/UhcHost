package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.Kidomaru;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.scheduler.BukkitRunnable;

public class KyodaigumoItem extends QuickItem {

	private NarutoV2Manager narutoV2Manager;
	private boolean spiderAlive;

	private Player nearestPlayer = null;

	private Spider spider;

	public KyodaigumoItem(UhcHost main) {
		super(Material.NETHER_STAR);
		super.setName("§5Kyodaigumo");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player playerClick = onClick.getPlayer();
			PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());
			if (joueur.hasRole()) {
				if (joueur.getRole() instanceof Kidomaru) {
					if (!narutoV2Manager.isInSamehada(playerClick.getUniqueId())) {
						if (joueur.getRoleCooldownKyodaigumo() <= 0) {
							double nearestDistance = Integer.MAX_VALUE;
							for (Player players : Bukkit.getOnlinePlayers()) {
								PlayerManager playersJoueur = main.getPlayerManager(players.getUniqueId());
								if (players.getGameMode() != GameMode.SPECTATOR && players != playerClick && playersJoueur.isAlive()) {
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
							}.runTaskLater(main, 20*60);
							if(joueur.getRole() instanceof NarutoV2Role){
								NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
								narutoRole.usePower(joueur);
								narutoRole.usePowerSpecific(joueur, super.getName());
							}
							joueur.setRoleCooldownKyodaigumo(60 * 15);
							joueur.sendTimer(playerClick, joueur.getRoleCooldownKyodaigumo(), this.toItemStack());
						} else {
							playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownKyodaigumo()));
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
