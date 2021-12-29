package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.Kabuto;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SenpoKabutoItem extends QuickItem {

	private NarutoV2Manager narutoV2Manager;
	private int distance = 15;
	
	public SenpoKabutoItem(UhcHost main) {
		super(Material.NETHER_STAR);
		super.setName("§dSenpô");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player player = onClick.getPlayer();
			PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
			if(joueur.hasRole()) {
				if(joueur.getRole() instanceof Kabuto) {
					Kabuto kabuto = (Kabuto) joueur.getRole();
					if(!narutoV2Manager.isInSamehada(player.getUniqueId())) {
						if(joueur.getRoleCooldownSenpo() <= 0) {
							for(Entity entity : player.getNearbyEntities(distance, distance, distance)) {
								if(entity instanceof Player) {
									Player target = (Player) entity;
									PlayerManager targetJoueur = main.getPlayerManager(target.getUniqueId());
									if(targetJoueur.isAlive() && targetJoueur != joueur) {
										target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cKabuto vient d'utiliser son senpô sur vous.");
										target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*10, 0, false, false));
										target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*10, 3, false, false));
									}
									
								}
							}
							player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
							kabuto.usePower(joueur);
							joueur.setRoleCooldownSenpo(20*60);
							kabuto.usePowerSpecific(joueur, super.getName());
						} else {
							player.sendMessage(Messages.cooldown(joueur.getRoleCooldownSenpo()));
						}
					} else {
						player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
					}
				} else {
					player.sendMessage(Messages.not("Kabuto"));
				}
			}
		});
	}

}
