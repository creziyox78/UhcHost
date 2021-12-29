package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.Tayuya;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;

public class FluteDemoniaque extends QuickItem {

	private NarutoV2Manager narutoV2Manager;
	private int nbGolemSpawn = 3;
	private int nbEndermiteSpawn= 5;

	public FluteDemoniaque(UhcHost main) {
		super(Material.NETHER_STAR);
		super.setName("§dFlûte Démoniaque");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
            	if(joueur.getRole() instanceof Tayuya) {
            		Tayuya tayuya = (Tayuya) joueur.getRole();
            		if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
            			if(joueur.getRoleCooldownFluteDemoniaque() <= 0) {
            				Player nearestPlayer = null;
                            double nearestDistance = Integer.MAX_VALUE;
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                if (players.getGameMode() != GameMode.SPECTATOR && players != player) {
                                    if (player.getLocation().distance(players.getLocation()) < nearestDistance) {
                                        nearestPlayer = players;
                                        nearestDistance = player.getLocation().distance(players.getLocation());
                                    }
                                }
                            }
            				for(int i = 0; i<nbGolemSpawn ; i++) {
            					IronGolem golem = player.getLocation().getWorld().spawn(player.getLocation(), IronGolem.class);
                				golem.setCustomName("§dServant de Tayuya");
                				golem.setPlayerCreated(false);
                				if(nearestPlayer != null)
                					golem.setTarget(nearestPlayer);
            				}
            				for(int i = 0; i<nbEndermiteSpawn; i++) {
            					Endermite endermite = player.getLocation().getWorld().spawn(player.getLocation(), Endermite.class);
                				endermite.setCustomName("§dServant de Tayuya");
                				if(nearestPlayer != null)
                					endermite.setTarget(nearestPlayer);
                				 
            				}
            				player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
            				joueur.setRoleCooldownFluteDemoniaque(60*30);
            				joueur.sendTimer(player, joueur.getRoleCooldownFluteDemoniaque(), player.getItemInHand());
            				tayuya.usePower(joueur);
            				tayuya.usePowerSpecific(joueur, super.getName());
            			} else {
            				player.sendMessage(Messages.cooldown(joueur.getRoleCooldownFluteDemoniaque()));
            			}
            		} else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
            	} else {
                    player.sendMessage(Messages.not("Tayuya"));
                    return;
                }
            }
		});
	}

}
