package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.KillerBee;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GyukiItem extends QuickItem {
	
	private final int time = 60 * 5 * 20;
	private final UhcHost main = UhcHost.getInstance();
	private NarutoV2Manager narutoV2Manager;

	public GyukiItem() {
		super(Material.NETHER_STAR);
		super.setName("§eGyûki");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player playerClick = onClick.getPlayer();
			PlayerManager joueur = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
			if(joueur.hasRole()) {
				if(joueur.getRole() instanceof KillerBee) {
					if(joueur.getRoleCooldownGyuki() <= 0) {
						KillerBee killerBee = (KillerBee) joueur.getRole();
						killerBee.usePower(joueur);
						playerClick.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, time, 0, false, false));
						playerClick.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, time, 3, false, false));
						playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
						joueur.setRoleCooldownGyuki(20 * 60);
						joueur.sendTimer(playerClick, joueur.getRoleCooldownGyuki(), playerClick.getItemInHand());
						killerBee.usePowerSpecific(joueur, super.getName());
					} else {
							playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownGyuki()));
					}
				}
			}
		});
	}

}
