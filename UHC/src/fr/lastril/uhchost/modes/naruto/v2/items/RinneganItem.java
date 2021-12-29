package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Sasuke;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RinneganItem extends QuickItem {
	
	private final int distance = 50;
	private NarutoV2Manager narutoV2Manager;

	public RinneganItem(UhcHost main) {
		super(Material.NETHER_STAR);
		super.setName("§6Rinnegan");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player player = onClick.getPlayer();
			PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
			if(joueur.hasRole()) {
				if(joueur.getRole() instanceof Sasuke) {
					Sasuke sasuke = (Sasuke) joueur.getRole();
					if(!narutoV2Manager.isInSamehada(player.getUniqueId())) {
						if(joueur.getRoleCooldownRinnegan() <= 0) {
							for(Entity entity : player.getNearbyEntities(distance, distance, distance)) {
								if(entity instanceof Player) {
									Player players = (Player) entity;
									PlayerManager joueurs = main.getPlayerManager(players.getUniqueId());
									if(getLookingAt(player, players) && joueurs.isAlive()) {
										Location loc1 = player.getLocation();
										Location loc2 = players.getLocation();
										player.teleport(loc2);
										players.teleport(loc1);
										players.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§6Sasuke vient d'échanger sa position avec vous.");
										player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
										if(player.hasPotionEffect(PotionEffectType.SPEED))
											player.removePotionEffect(PotionEffectType.SPEED);
										player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*2*60, 1, false, false));
										new BukkitRunnable() {
											@Override
											public void run() {
												if(player.hasPotionEffect(PotionEffectType.SPEED))
													player.removePotionEffect(PotionEffectType.SPEED);
												player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
											}
										}.runTaskLater(main, 20*2*60);
										sasuke.usePower(joueur);
										sasuke.usePowerSpecific(joueur, super.getName());
										joueur.setRoleCooldownRinnegan(5*60);
										joueur.sendTimer(player, joueur.getRoleCooldownRinnegan(), player.getItemInHand());
									}
								}
							}
							
						} else {
							player.sendMessage(Messages.cooldown(joueur.getRoleCooldownRinnegan()));
						}
					} else {
						player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
					}
				} else {
					player.sendMessage(Messages.not("Sasuke"));
				}
			}
		});
	}

	public interface RinneganUser{

	}
	
	private boolean getLookingAt(Player player, LivingEntity livingEntity){
	    Location eye = player.getEyeLocation();
	    Vector toEntity = livingEntity.getEyeLocation().toVector().subtract(eye.toVector());
	    double dot = toEntity.normalize().dot(eye.getDirection());

	    return dot > 0.99D;
	}
	
}
