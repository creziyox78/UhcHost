package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Madara;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MadaraItem extends QuickItem {

	private NarutoV2Manager narutoV2Manager;

	public MadaraItem(UhcHost main) {
		super(Material.NETHER_STAR);
		super.setName("§dMadara");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player player = onClick.getPlayer();
			PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
			if(onClick.getAction() == Action.RIGHT_CLICK_AIR || onClick.getAction() == Action.RIGHT_CLICK_BLOCK){
				if (joueur.hasRole()) {
					if (joueur.getRole() instanceof Madara) {
						if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {

							boolean lost = false;

							if(player.hasPotionEffect(PotionEffectType.SPEED)){
								lost = true;
								player.removePotionEffect(PotionEffectType.SPEED);
							}else{
								player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
							}

							if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)){
								lost = true;
								player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
							}else{
								player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
							}

							if(joueur.getRole() instanceof NarutoV2Role){
								NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
								narutoRole.usePower(joueur);
								narutoRole.usePowerSpecific(joueur, super.getName());
							}
							player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous avez "+(lost ? "perdu" : "reçu")+" vos effets.");
						} else {
							player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
						}
					} else {
						player.sendMessage(Messages.not("Madara"));
					}
				} else {
					player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
				}
			}

		});
	}
	
}
