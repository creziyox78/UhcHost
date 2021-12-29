package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.HuitPorteUser;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.GaiMaito;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.RockLee;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TroisPortesItem extends QuickItem {

	private NarutoV2Manager narutoV2Manager;
	private UhcHost main = UhcHost.getInstance();

	public TroisPortesItem() {
		super(Material.NETHER_STAR);
		super.setName("§2Trois portes");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player playerClick = onClick.getPlayer();
			PlayerManager joueur = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
			if(onClick.getAction() == Action.RIGHT_CLICK_AIR || onClick.getAction() == Action.RIGHT_CLICK_BLOCK){
				if (isRightRole(joueur.getRole())) {
					HuitPorteUser user = (HuitPorteUser) joueur.getRole();
					if(user.hasUsePorte()){
						playerClick.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
						return;
					}
					if (!narutoV2Manager.isInSamehada(playerClick.getUniqueId())) {
						if (joueur.getRoleCooldownTroisPortes() <= 0) {
							if(playerClick.getHealth() <= playerClick.getHealth() - 2D) {
								playerClick.damage(100);
							} else {
								playerClick.setHealth(playerClick.getHealth() - 2D);
								if(playerClick.hasPotionEffect(PotionEffectType.SPEED))
									playerClick.removePotionEffect(PotionEffectType.SPEED);
								playerClick.addPotionEffect(
										new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 2, 0, false, false));
								joueur.setRoleCooldownTroisPortes(10);
							}
							if(joueur.getRole() instanceof NarutoV2Role){
								NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
								narutoRole.usePower(joueur);
								narutoRole.usePowerSpecific(joueur, super.getName());
							}

						} else {
							playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownTroisPortes()));
						}
					} else {
						playerClick
								.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
					}

				}
			}
		});
	}
	
	private boolean isRightRole(Role role) {
		return role instanceof RockLee || role instanceof GaiMaito;
	}
	


}
