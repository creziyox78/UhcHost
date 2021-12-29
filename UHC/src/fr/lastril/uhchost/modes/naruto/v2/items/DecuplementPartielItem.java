package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Choji;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DecuplementPartielItem extends QuickItem {
	
	private final UhcHost main = UhcHost.getInstance();
	private NarutoV2Manager narutoV2Manager;

	public DecuplementPartielItem() {
		super(Material.NETHER_STAR);
		super.setName("§bDécuplement Partiel");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player playerClick = onClick.getPlayer();
			PlayerManager joueur = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
			if(joueur.hasRole()) {
				if (!narutoV2Manager.isInSamehada(playerClick.getUniqueId())) {
					if(joueur.getRole() instanceof Choji) {
						Choji choji = (Choji) joueur.getRole();
						if(joueur.getRoleCooldownDecuplementPartiel() <= 0) {
							if(playerClick.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
								playerClick.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
							}
							playerClick.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*20, 1, false, false));
							playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
							choji.usePower(joueur);
							choji.usePowerSpecific(joueur, super.getName());
							joueur.setRoleCooldownDecuplementPartiel(60*10);
							joueur.sendTimer(playerClick, joueur.getRoleCooldownDecuplementPartiel(), playerClick.getItemInHand());
						} else {
							playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownDecuplementPartiel()));
						}
					} else {
						playerClick.sendMessage(Messages.not("Choji"));
					}
				} else {
					playerClick.sendMessage(
							Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
				}
			}
			
		});
	}

}
