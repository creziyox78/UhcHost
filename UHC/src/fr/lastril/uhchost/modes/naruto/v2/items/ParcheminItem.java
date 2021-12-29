package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.TenTen;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.MathL;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParcheminItem extends QuickItem {
	
	private final double radius = 1D;
	private NarutoV2Manager narutoV2Manager;

	private double powerHorizontal = 1;
	private double powerVertical = 0.25;
	private final UhcHost main = UhcHost.getInstance();
	
	public ParcheminItem() {
		super(Material.NETHER_STAR);
		super.setName("§7Parchemin");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player player = onClick.getPlayer();
			PlayerManager joueur = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
			if(!joueur.hasRole() || !(joueur.getRole() instanceof TenTen)) {
				player.sendMessage(Messages.not("TenTen"));
				return;
			}
			if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
				if (joueur.getRoleCooldownParchemin() <= 0) {
					
					int arrowNb = 20;
					
					double slice = 2 * Math.PI / arrowNb;
					for (int i = 0; i < arrowNb; i++) {
						double angle = slice * i;
						double dx = this.radius * MathL.cos(angle);
						double dy = 1.25;
						double dz = this.radius * MathL.sin(angle);
                        Arrow arrow = player.getWorld().spawn(player.getLocation().clone().add(dx, dy, dz), Arrow.class);
                        arrow.setVelocity(new Vector(dx/powerHorizontal,powerVertical,dz/powerHorizontal));
                        arrow.setShooter(player);
					}
					joueur.setRoleCooldownParchemin(60 * 5);
					if(joueur.getRole() instanceof NarutoV2Role){
                        NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                        narutoRole.usePower(joueur);
                        narutoRole.usePowerSpecific(joueur, super.getName());
                    }
				} else {
					player.sendMessage(Messages.cooldown(joueur.getRoleCooldownParchemin()));
				}
				
			} else {
				player.sendMessage(
						Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
			}
			
		});
	}

}
