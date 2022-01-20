package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.YondameRaikage;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ArmureFoudre extends QuickItem {
	
	private final UhcHost main = UhcHost.getInstance();
	private NarutoV2Manager narutoV2Manager;
	
	public ArmureFoudre() {
		super(Material.NETHER_STAR);
		super.setName("§eArmure de foudre");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player playerClick = onClick.getPlayer();
			PlayerManager joueur = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
			if(onClick.getAction() == Action.RIGHT_CLICK_AIR || onClick.getAction() == Action.RIGHT_CLICK_BLOCK){
				if(joueur.hasRole()) {
					if (!narutoV2Manager.isInSamehada(playerClick.getUniqueId())) {
						if(joueur.getRole() instanceof YondameRaikage) {
							YondameRaikage yondame = (YondameRaikage) joueur.getRole();
							yondame.setUseFoudreArmor(!yondame.isUseFoudreArmor());
							if(yondame.isUseFoudreArmor())
								playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVotre armure de foudre est de nouveau activée.");
							else
								playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVotre armure de foudre est désactivée.");
							if(!yondame.isHasUse()) {

								yondame.setUseFoudreArmor(true);
								yondame.setHasUse(true);
								new BukkitRunnable() {

									@Override
									public void run() {
										ActionBar.sendMessage(playerClick, "§eArmure de foudre: " + new FormatTime(yondame.getTimer()).toString());
										if(yondame.isUseFoudreArmor()) {
											if(yondame.getTimer() > 0) {
												yondame.setTimer(yondame.getTimer() - 1);
												playerClick.addPotionEffect(
														new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*3, 0, false, false));

											} else {
												if(playerClick.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
													playerClick.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
											}
										}
										else {
											if(playerClick.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
												playerClick.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
										}

									}
								}.runTaskTimer(main, 0, 20);
							}
							yondame.usePower(joueur);
							yondame.usePowerSpecific(joueur, super.getName());
						}
					} else {
						playerClick
								.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
					}
				}
			}

		});
	}

}
