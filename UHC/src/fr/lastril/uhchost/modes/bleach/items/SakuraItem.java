package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Byakuya;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class SakuraItem extends QuickItem {
    public SakuraItem(UhcHost main) {
        super(Material.YELLOW_FLOWER);
        super.setName("§dSakura");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.setLore("",
                "§7Rend invisible§e avec l'armure (sauf l'item en main)",
                "§7pendant 3 secondes.",
                "§7(Cooldown - 1 minute");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            onClick.cancelOriginalUse(true);
            if(playerManager.hasRole() && playerManager.getRole() instanceof Byakuya){
                if(playerManager.getRoleCooldownSakura() <= 0){
                    ClassUtils.hidePlayerWithArmor(player, false, 3, true);
                } else {
                    player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownSakura()));
                }
            }
        });
    }
}
