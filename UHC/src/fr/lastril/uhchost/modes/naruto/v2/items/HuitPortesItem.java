package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.HuitPorteUser;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.API.particles.DoubleCircleEffect;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class HuitPortesItem extends QuickItem {

	private final UhcHost main = UhcHost.getInstance();
	private NarutoV2Manager narutoV2Manager;

	public HuitPortesItem() {
		super(Material.NETHER_STAR);
		super.setName("§2Huit portes");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player playerClick = onClick.getPlayer();
			PlayerManager joueur = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
			if(onClick.getAction() == Action.RIGHT_CLICK_AIR || onClick.getAction() == Action.RIGHT_CLICK_BLOCK){
				if (joueur.getRole() instanceof HuitPorteUser) {
					HuitPorteUser user = (HuitPorteUser) joueur.getRole();
					if(user.hasUsePorte()){
						playerClick.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
						return;
					}
					if (!narutoV2Manager.isInSamehada(playerClick.getUniqueId())) {
						if (joueur.getRoleCooldownHuitieme() <= 0) {
							playerClick.setMaxHealth(playerClick.getMaxHealth() + (5D * 2D));
							if((playerClick.getMaxHealth() - playerClick.getHealth()) < (5D * 2D)) {
								playerClick.setHealth(playerClick.getMaxHealth());
							}
							else {
								playerClick.setHealth(playerClick.getHealth() + (5D * 2D));
							}

							if(playerClick.hasPotionEffect(PotionEffectType.SPEED))
								playerClick.removePotionEffect(PotionEffectType.SPEED);
							if(playerClick.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
								playerClick.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
							if(playerClick.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
								playerClick.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);

							playerClick.addPotionEffect(
									new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60 * 2, 0, false, false));
							playerClick.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 2, 1, false, false));
							playerClick.addPotionEffect(
									new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 60 * 2, 0, false, false));
							playerClick.addPotionEffect(
									new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 60 * 2, 0, false, false));
							new DoubleCircleEffect(20 * 2 * 60, EnumParticle.FLAME).start(playerClick);
							joueur.setRoleCooldownHuitieme(20 * 5 * 60);
							joueur.setUseHuitimePorte(true);
							playerClick.setItemInHand(new GaiNuitItem().toItemStack());
							user.setHasUsePorte(true);
							new BukkitRunnable() {

								int timer = 2 * 60;

								@Override
								public void run() {
									Player player = playerClick;
									if (Bukkit.getPlayer(playerClick.getUniqueId()) != null)
										player = Bukkit.getPlayer(playerClick.getUniqueId());
									PlayerManager joueur = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
									if(joueur.isAlive()) {
										ActionBar.sendMessage(player, "§aTemps restant : §e" + new FormatTime(timer));
										if (timer == 0) {
											player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 60 * 15, 0, false, false));
											player.setMaxHealth(2D*6D);
											joueur.setUseHuitimePorte(false);
											cancel();
										}
										timer--;
									}
									else {
										timer = 2 * 60;
										joueur.setUseHuitimePorte(false);
										cancel();
									}


								}
							}.runTaskTimer(UhcHost.getInstance(), 0, 20);
							if(joueur.getRole() instanceof NarutoV2Role){
								NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
								narutoRole.usePower(joueur);
								narutoRole.usePowerSpecific(joueur, super.getName());
							}
						} else {
							playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownHuitieme()));
						}
					}
					else {
						playerClick.sendMessage(
								Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
					}
				}
			}

		});
	}
	


}
