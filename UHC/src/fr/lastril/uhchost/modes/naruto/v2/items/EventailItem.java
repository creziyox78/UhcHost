package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Temari;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;

public class EventailItem extends QuickItem {

    private NarutoV2Manager narutoV2Manager;

    public EventailItem(UhcHost main) {
        super(Material.DIAMOND_SWORD);
        super.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
        super.setName("§bÉventail");
        super.toItemStack().getItemMeta().spigot().setUnbreakable(true);
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player playerClick = onClick.getPlayer();
            PlayerManager joueur = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
            if (joueur.getRole() instanceof Temari) {
                if(onClick.getAction() == Action.RIGHT_CLICK_AIR || onClick.getAction() == Action.RIGHT_CLICK_BLOCK){
                    if (!narutoV2Manager.isInSamehada(playerClick.getUniqueId())) {
                        if(joueur.getRoleCooldownEventail() == 0){
                            for(Entity entity : playerClick.getNearbyEntities(Temari.getEventailDistance(), Temari.getEventailDistance(), Temari.getEventailDistance())){
                                Location initialLocation = playerClick.getLocation().clone();
                                initialLocation.setPitch(0.0f);
                                Vector origin = initialLocation.toVector();
                                Vector fromPlayerToTarget = entity.getLocation().toVector().clone().subtract(origin);
                                fromPlayerToTarget.multiply(4); //6
                                fromPlayerToTarget.setY(1); // 2
                                entity.setVelocity(fromPlayerToTarget);
                            }

                            if (joueur.getRole() instanceof NarutoV2Role) {
                                NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                                narutoRole.usePower(joueur);
                                narutoRole.usePowerSpecific(joueur, super.getName());
                            }
                            joueur.setRoleCooldownEventail(5*60);
                            joueur.sendTimer(playerClick, joueur.getRoleCooldownEventail(), playerClick.getItemInHand());
                            playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                        }else{
                            playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownEventail()));
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
