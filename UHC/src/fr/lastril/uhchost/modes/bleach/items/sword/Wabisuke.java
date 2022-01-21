package fr.lastril.uhchost.modes.bleach.items.sword;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Kira;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemFlag;

import static fr.lastril.uhchost.tools.API.ClassUtils.getLookingAt;

public class Wabisuke extends QuickItem {

    private final int distance = 8;

    public Wabisuke(UhcHost main) {
        super(Material.DIAMOND_SWORD);
        super.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
        super.setName("§eWabisuke");
        super.setLore("",
                "§7Tout les 5 coups infligés à",
                "§7un joueur lui donne&8 Slowness 1&7",
                "§7puis&8 Slowness 2&7 au delà de",
                "&710 coups.");
        super.setInfinityDurability();
        super.addItemFlag(ItemFlag.HIDE_UNBREAKABLE);
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if(playerManager.hasRole() && playerManager.getRole() instanceof Kira){
                if(onClick.getAction() == Action.RIGHT_CLICK_AIR || onClick.getAction() == Action.RIGHT_CLICK_BLOCK){
                    for(Entity entity : player.getNearbyEntities(distance, distance, distance)) {
                        if(playerManager.getRoleCooldownWabisuke() <= 0){
                            if(entity instanceof Player) {
                                Player players = (Player) entity;
                                PlayerManager targetManager = main.getPlayerManager(player.getUniqueId());
                                if(getLookingAt(player, players) && targetManager.isAlive()){
                                    Location loc1 = player.getLocation();
                                    players.teleport(loc1);
                                    playerManager.setRoleCooldownWabisuke(60+30);
                                }
                            }
                        } else{
                            player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownWabisuke()));
                        }
                    }
                }
            }
        });
    }
}
