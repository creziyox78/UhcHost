package fr.lastril.uhchost.modes.naruto.v2.items.biju;


import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Madara;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Obito;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class JubiItem extends QuickItem {

	private NarutoV2Manager narutoV2Manager;

	public JubiItem(UhcHost main) {
		super(Material.NETHER_STAR);
		super.setName("§dJûbi");
		super.setLore("",
				"§c§lLe plus puissant démon à queue",
				"",
				"§c§lLa fin du monde arrive...");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player player = onClick.getPlayer();
			PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
			if(joueur.hasRole()) {
				if(joueur.getRole() instanceof JubiUser) {
					if(joueur.getRoleCooldownJubi() <= 0){
						narutoV2Manager.setJubiUsed(true);
						narutoV2Manager.setJubi(joueur);
						new BukkitRunnable() {
							@Override
							public void run() {
								if(narutoV2Manager.isJubiUsed()){
									if(player.hasPotionEffect(PotionEffectType.REGENERATION))
										player.removePotionEffect(PotionEffectType.REGENERATION);
									player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*5, 0, false, false));
								} else{
									cancel();
								}
							}
						}.runTaskTimer(main, 0, 20*2L);
						if (player.hasPotionEffect(PotionEffectType.SPEED))
							player.removePotionEffect(PotionEffectType.SPEED);
						player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*5*60, 0, false, false));
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*5*60, 1, false, false));
						player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*5*60, 0, false, false));
						player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*5*60, 0, false, false));
						player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*5*60, 3, false, false));
						player.setMaxHealth(player.getMaxHealth() + 2*3D);
						player.setHealth(player.getHealth() + 2*3D);
						Bukkit.broadcastMessage(" ");
						Bukkit.broadcastMessage("§c§lLe receptacle de Jubi obtient une puissance phénoménale !");
						Bukkit.broadcastMessage(" ");
						player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§dVous possédez désormais Jubi en vous et obtenez sa pleine puissance.");
						for(Player players : Bukkit.getOnlinePlayers()) {
							players.playSound(players.getLocation(), "atlantis.jubicri", Integer.MAX_VALUE, 1);
						}
						for(PlayerManager obito : narutoV2Manager.getPlayerManagersWithRole(Obito.class)){
							obito.setRoleCooldownJubi(15*60);
						}
						for(PlayerManager madara: narutoV2Manager.getPlayerManagersWithRole(Madara.class)){
							madara.setRoleCooldownJubi(15*60);
						}
						narutoV2Manager.getNofallJoueur().add(joueur);
						Bukkit.getScheduler().runTaskLater(main, () -> {
							if(joueur.getRole() instanceof Obito) {
								if (player.hasPotionEffect(PotionEffectType.SPEED))
									player.removePotionEffect(PotionEffectType.SPEED);
								player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
							}
							player.setMaxHealth(player.getMaxHealth() - 2*3D);
							narutoV2Manager.setJubiUsed(false);
							narutoV2Manager.getNofallJoueur().remove(joueur);
						}, 20*5*60 + 5);
						if(joueur.getRole() instanceof NarutoV2Role){
							NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
							narutoRole.usePower(joueur);
							narutoRole.usePowerSpecific(joueur, super.getName());
						}
					} else {
						player.sendMessage(Messages.cooldown(joueur.getRoleCooldownJubi()));
					}
				}
			}
		});
	}
	
	public interface JubiUser{
		
	}

}
