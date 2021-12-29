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

public class BouletHumainItem extends QuickItem {
	
	private final UhcHost main = UhcHost.getInstance();
	private NarutoV2Manager narutoV2Manager;
	
	public BouletHumainItem() {
		super(Material.NETHER_STAR);
		super.setName("§bBoulet Humain");

		super.onClick(onClick -> {
			Player playerClick = onClick.getPlayer();
			PlayerManager joueur = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			if(joueur.hasRole()) {
				if (!narutoV2Manager.isInSamehada(playerClick.getUniqueId())) {
					if(joueur.getRole() instanceof Choji) {
						Choji choji = (Choji) joueur.getRole();
						if(joueur.getRoleCooldownBouletHumain() <= 0) {
							if(playerClick.hasPotionEffect(PotionEffectType.SPEED)) {
								playerClick.removePotionEffect(PotionEffectType.SPEED);
							}
							playerClick.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*60, 1, false, false));
							playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
							choji.usePower(joueur);
							choji.usePowerSpecific(joueur, super.getName());
							joueur.setRoleCooldownBouletHumain(60*15);
							joueur.sendTimer(playerClick, joueur.getRoleCooldownBouletHumain(), playerClick.getItemInHand());
						} else {
							playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownBouletHumain()));
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
