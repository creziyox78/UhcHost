package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Kiba;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class AkamaruItem extends QuickItem {
	
	private Wolf wolf;
	private NarutoV2Manager narutoV2Manager;
	
	public AkamaruItem() {
		super(Material.NETHER_STAR);
		super.setName("§bAkamaru");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) UhcHost.getInstance().getGamemanager().getModes().getMode().getModeManager();
			Player playerClick = onClick.getPlayer();
			PlayerManager joueur = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
			if (joueur.getRole() instanceof Kiba) {
				if (!narutoV2Manager.isInSamehada(playerClick.getUniqueId())) {
					if (joueur.getRoleCooldownAkamaru() == 0) {
						Kiba kiba = (Kiba) joueur.getRole();
						kiba.usePower(joueur);

						wolf = playerClick.getWorld().spawn(playerClick.getLocation(), Wolf.class);
						wolf.setCollarColor(DyeColor.CYAN);
						wolf.setTamed(true);
						wolf.setOwner(playerClick);
						wolf.addPotionEffect(
								new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 10 * 60, 0, false, false));
						playerClick.addPotionEffect(
								new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 10 * 60, 0, false, false));

						new BukkitRunnable() {

							@Override
							public void run() {
								wolf.remove();
								playerClick.sendMessage(
										Messages.NARUTO_PREFIX.getMessage() + "Vous avez perdu votre pouvoir !");
							}
						}.runTaskLater(UhcHost.getInstance(), 20 * 60 * 10);

						playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
						joueur.setRoleCooldownAkamaru(20 * 60);
						joueur.sendTimer(playerClick, joueur.getRoleCooldownAkamaru(), playerClick.getItemInHand());
						kiba.usePower(joueur);
						kiba.usePowerSpecific(joueur, super.getName());
					} else {
						playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownAkamaru()));
					}
				} else {
					playerClick
							.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
				}
				
			}
		});
	}

}
