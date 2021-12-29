package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.GaiMaito;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.API.particles.DoubleCircleEffect;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class GaiNuitItem extends QuickItem {

	private NarutoV2Manager narutoV2Manager;
	private final UhcHost main = UhcHost.getInstance();

	public GaiNuitItem() {
		super(Material.NETHER_STAR);
		super.setName("§4Gaï de la Nuit");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player playerClick = onClick.getPlayer();
			PlayerManager joueur = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
			if(onClick.getAction() == Action.RIGHT_CLICK_AIR || onClick.getAction() == Action.RIGHT_CLICK_BLOCK){
				if (joueur.getRole() instanceof GaiMaito) {
					GaiMaito gaiMaito = (GaiMaito) joueur.getRole();
					if (!narutoV2Manager.isInSamehada(playerClick.getUniqueId())) {
						if (joueur.isUseHuitimePorte()) {
							playerClick.setItemInHand(null);
							gaiMaito.setInGaiNuit(true);
							if(playerClick.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
								playerClick.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
							if(playerClick.hasPotionEffect(PotionEffectType.SPEED))
								playerClick.removePotionEffect(PotionEffectType.SPEED);
							playerClick.addPotionEffect(
									new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 10, 2, false, false));
							playerClick.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 2, false, false));
							new DoubleCircleEffect(20 * 10, EnumParticle.REDSTONE).start(playerClick);

							new BukkitRunnable() {

								int timer = 10;

								@Override
								public void run() {
									PlayerManager joueur = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
									if(joueur.isAlive()) {
										ActionBar.sendMessage(playerClick, "§aTemps restant : §e" + new FormatTime(timer));
										if (timer == 0) {
											gaiMaito.setInGaiNuit(false);
											if(gaiMaito.isMustDie()) playerClick.damage(100);
											cancel();
										}
										timer--;
									}
									else {
										timer = 2 * 60;
										cancel();
									}
								}
							}.runTaskTimer(UhcHost.getInstance(), 0, 20);
							gaiMaito.usePower(joueur);
							gaiMaito.usePowerSpecific(joueur, super.getName());
						} else {
							playerClick.sendMessage(Messages.error("Vous ne pouvez pas utiliser votre item après avoir utiliser la Huitième Porte."));
						}
					} else {
						playerClick.sendMessage(
								Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
					}
				} else {
					playerClick.sendMessage(Messages.not("Gaï Maito"));
				}
			}
		});
	}
}
