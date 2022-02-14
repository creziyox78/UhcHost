package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.ds.roles.demon.Enmu;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class Oeil extends QuickItem {
    public Oeil(UhcHost main) {
        super(Material.SPIDER_EYE);
        super.setName("§6Oeil");
        super.setLore("",
                "§7Permet d'endormir pendant 10 secondes",
                "§7le joueur ciblé.");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if(playerManager.hasRole() && playerManager.getRole() instanceof Enmu){
                if(playerManager.getRoleCooldownOeil() <= 0){
                    Player target = ClassUtils.getTargetPlayer(player, 10);
                    if(target != null){
                        PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
                        targetManager.stun(target.getLocation());
                    }
                }
            }
        });
    }
}
