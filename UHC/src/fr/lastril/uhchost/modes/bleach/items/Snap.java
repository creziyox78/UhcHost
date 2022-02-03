package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Hinamori;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class Snap extends QuickItem {
    public Snap(UhcHost main) {
        super(Material.FIREBALL);
        super.setName("§6Snap");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.setLore("",
                "§7Provoque une explosion de 3 blocs.",
                "§7Les joueurs touchés§c ne peuvent pas",
                "§7s'éteindre pendant 15 secondes.",
                "§7(Cooldown - 3 minutes)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            onClick.cancelOriginalUse(true);
            if(playerManager.hasRole() && playerManager.getRole() instanceof Hinamori){
                if(playerManager.getRoleCooldownSnap() <= 0){
                    Fireball fireball = player.launchProjectile(Fireball.class);
                    fireball.setVelocity(player.getVelocity());
                    fireball.setIsIncendiary(true);
                    fireball.setYield(player.getLocation().getYaw());
                    fireball.setCustomName("§6Snap");
                    fireball.setCustomNameVisible(true);
                    playerManager.setRoleCooldownSnap(3*60);
                }else {
                    player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownSnap()));
                }
            }
        });
    }
}
