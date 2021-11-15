package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Temari;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;

public class EventailItem extends QuickItem {

    public EventailItem(UhcHost main) {
        super(Material.DIAMOND_SWORD);
        super.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
        super.setName("§bÉventail");
        super.toItemStack().getItemMeta().spigot().setUnbreakable(true);
        super.onClick(onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
            if (PlayerManager.getRole() instanceof Temari) {
                if (onClick.getAction() == Action.RIGHT_CLICK_AIR || onClick.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!UhcHost.getInstance().getNarutoV2Manager().isInSamehada(playerClick.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownEventail() == 0) {
                            for (Entity entity : playerClick.getNearbyEntities(Temari.getEventailDistance(), Temari.getEventailDistance(), Temari.getEventailDistance())) {
                                Vector fromPlayerToTarget = entity.getLocation().toVector().clone().subtract(playerClick.getLocation().toVector());
                                entity.setVelocity(new Vector(0, 0.3, 0));
                                fromPlayerToTarget.multiply(6);
                                fromPlayerToTarget.setY(2);
                                entity.setVelocity(fromPlayerToTarget.normalize());
                            }

                            if (PlayerManager.getRole() instanceof NarutoV2Role) {
                                NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                                narutoRole.usePower(PlayerManager);
                            }
                            PlayerManager.setRoleCooldownEventail(5 * 60);
                            PlayerManager.sendTimer(playerClick, PlayerManager.getRoleCooldownEventail(), playerClick.getItemInHand());
                            playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                        } else {
                            playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownEventail()));
                        }
                    } else {
                        playerClick
                                .sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                }

            }
        });
    }

}
