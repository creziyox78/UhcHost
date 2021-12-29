package fr.lastril.uhchost.modes.naruto.v2.items.biju;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.biju.bijus.MatatabiBiju;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class MatatabiItem extends QuickItem {
	
	private int timer = 5*60;
	private NarutoV2Manager narutoV2Manager;
	
	public MatatabiItem(UhcHost main) {
		super(Material.NETHER_STAR);
		super.setName("§6Matatabi");
		super.setLore("",
				"§eDémon à queue",
				"",
				"§7Permet à son utilisateur de recevoir les effet§cForce 1,",
				"§6Résistance au Feu§7, que ses coups §eenflamme le joueur tappé§7,",
				"§7ainsi que§f NoFall§7 pendant 5 minutes.");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player player = onClick.getPlayer();
			PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
			if(joueur.hasRole()) {
				if(!narutoV2Manager.isInSamehada(player.getUniqueId())) {
					if(joueur.getRoleCooldownBiju() <= 0) {
						if(narutoV2Manager.getBijuManager().getHotesBiju().get(MatatabiBiju.class) == joueur
								|| (narutoV2Manager.getBijuManager().getHotesBiju().get(MatatabiBiju.class) == null
								&& !narutoV2Manager.getBijuManager().isAlreadyHote(joueur))) {
							narutoV2Manager.getBijuManager().getHotesBiju().put(MatatabiBiju.class, joueur);
							player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*5*60, 0, false, false));
							player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*5*60, 0, false ,false));
							narutoV2Manager.getBijuManager().getNoFall().add(joueur);
							narutoV2Manager.getSusano().add(player.getUniqueId());
							player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§6Vous venez d'utiliser les pouvoirs de Matatabi.");
							new BukkitRunnable() {
								
								@Override
								public void run() {
									ActionBar.sendMessage(player, "§6Pouvoir de Matatabi : " + new FormatTime(timer));
									if(timer <= 0) {
										narutoV2Manager.getBijuManager().getNoFall().remove(joueur);
										narutoV2Manager.getSusano().remove(player.getUniqueId());
										timer = 5*60;
										joueur.sendTimer(player, joueur.getRoleCooldownJubi(), MatatabiItem.super.toItemStack());
										cancel();
									}
									timer--;
								}
							}.runTaskTimer(main, 0, 20);
							joueur.setRoleCooldownBiju(20*60);
							if(joueur.getRole() instanceof NarutoV2Role){
								NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
								narutoRole.usePower(joueur);
								narutoRole.usePowerSpecific(joueur, super.getName());
							}
						} else {
							player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§6Vous ne pouvez pas utiliser ce Bijû.");
						}
					} else {
						player.sendMessage(Messages.cooldown(joueur.getRoleCooldownBiju()));
					} 
				} else {
					player.sendMessage(
							Messages.NARUTO_PREFIX.getMessage() + "§eVous êtes sous l'effet de §cSamehada§e.");
				}
				
			}
		});
	}

}
