package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.TenTen;
import fr.maygo.uhc.obj.PlayerManager;
import fr.maygo.uhc.utils.MathL;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParcheminItem extends QuickItem {

    private final double radius = 1D;

    private final double powerHorizontal = 1;
    private final double powerVertical = 0.25;

    public ParcheminItem() {
        super(Material.NETHER_STAR);
        super.setName("§7Parchemin");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
            if (!PlayerManager.hasRole() || !(PlayerManager.getRole() instanceof TenTen)) {
                player.sendMessage(Messages.not("TenTen"));
                return;
            }
            if (!UhcHost.getInstance().getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                if (PlayerManager.getRoleCooldownParchemin() <= 0) {

                    int arrowNb = 20;

                    double slice = 2 * Math.PI / arrowNb;
                    for (int i = 0; i < arrowNb; i++) {
                        double angle = slice * i;
                        double dx = this.radius * MathL.cos(angle);
                        double dy = 1.25;
                        double dz = this.radius * MathL.sin(angle);
                        Arrow arrow = player.getWorld().spawn(player.getLocation().clone().add(dx, dy, dz), Arrow.class);
                        arrow.setVelocity(new Vector(dx / powerHorizontal, powerVertical, dz / powerHorizontal));
                        arrow.setShooter(player);
                    }
                    PlayerManager.setRoleCooldownParchemin(60 * 5);
                    if (PlayerManager.getRole() instanceof NarutoV2Role) {
                        NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                        narutoRole.usePower(PlayerManager);
                    }
                } else {
                    player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownParchemin()));
                }

            } else {
                player.sendMessage(
                        Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
            }

        });
    }

}
