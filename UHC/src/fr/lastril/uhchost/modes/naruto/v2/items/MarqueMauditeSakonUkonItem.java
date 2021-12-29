package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.Sakon;
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.Ukon;
import fr.lastril.uhchost.modes.naruto.v2.sakonukon.SakonUkonManager;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class MarqueMauditeSakonUkonItem extends QuickItem {

	private final UhcHost main;
	private NarutoV2Manager narutoV2Manager;

	public MarqueMauditeSakonUkonItem(UhcHost main) {
		super(Material.NETHER_STAR);
		this.main = main;
		super.setName("§3Marque Maudite");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
            	if(isRightRole(joueur.getRole())) {
					SakonUkonManager sakonUkonManager = narutoV2Manager.getSakonUkonManager();
            		if(joueur.getRoleCooldownMarqueMaudite() <= 0) {
            			if(sakonUkonManager.getPowersWaits().containsKey("MARQUE_MAUDITE")){
							UUID clicker = sakonUkonManager.getPowersWaits().get("MARQUE_MAUDITE");
							if(!clicker.equals(player.getUniqueId())){
								Player first = Bukkit.getPlayer(clicker);
								this.sendEffectsToPlayer(player);
								this.sendEffectsToPlayer(first);
								sakonUkonManager.getPowersWaits().remove("MARQUE_MAUDITE");
							}
						}else{
							sakonUkonManager.addPowerWait("MARQUE_MAUDITE", player, joueur.getRole() instanceof Sakon ? "Ukon" : "Sakon");
            			}
            		} else {
            			player.sendMessage(Messages.cooldown(joueur.getRoleCooldownMarqueMaudite()));
            		}
            	} else {
					player.sendMessage(Messages.not("Sakon ou Ukon"));
				}
			} else {
				player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
			}
		});
	}
	
	private void sendEffectsToPlayer(Player player) {
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());

		if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*60*5, 0, false, false));

		if(player.hasPotionEffect(PotionEffectType.SPEED)) player.removePotionEffect(PotionEffectType.SPEED);
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*60*5, 0, false, false));

		player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+Messages.USED_POWER.getMessage());

		new BukkitRunnable(){

			@Override
			public void run() {
				Player players = Bukkit.getPlayer(player.getUniqueId());
				if(players != null){
					players.setMaxHealth(players.getMaxHealth() - 2D);
				}
			}
		}.runTaskLater(main, 20*60*5);

		joueur.setRoleCooldownMarqueMaudite(10*60);
		joueur.sendTimer(player, joueur.getRoleCooldownMarqueMaudite(), player.getItemInHand());
	}
	
	
	private boolean isRightRole(Role role) {
		return role instanceof Sakon || role instanceof Ukon;
	}

}
