package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Isane;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Itegumo extends QuickItem {
    public Itegumo(UhcHost main) {
        super(Material.GLOWSTONE_DUST);
        super.setName("§9Itegumo");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.setLore("",
                "§7Octroie§c Force 1§7 pendant 20 secondes.",
                "§750% des dégâts infligé à un joueur sont",
                "§7donné à l'allié le plus bas en vie",
                "§7dans un rayon de 10 blocs.",
                "§7(Cooldown - 5 minutes)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if(playerManager.hasRole() && playerManager.getRole() instanceof Isane){
                Isane isane = (Isane) playerManager.getRole();
                if(playerManager.getRoleCooldownItegumo() <= 0){
                    isane.setInItegumo(true);
                    if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                        player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*20, 0, false, false));
                    player.sendMessage("§9Vous utilisez \"Itegumo\".");
                } else {
                    player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownItegumo()));
                }
            } else {
                player.sendMessage(Messages.not("Isane"));
            }
        });
    }
}
