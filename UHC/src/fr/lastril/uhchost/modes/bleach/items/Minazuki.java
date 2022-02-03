package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Unohana;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class Minazuki extends QuickItem {
    public Minazuki(UhcHost main) {
        super(Material.LEASH);
        super.setName("§6Minazuki");
        super.setLore("",
                "§7Permet de donner la possibilité de",
                "§eporter 3 joueurs au maximum (sur 10 secondes)",
                "§7sur la tête puis donne§d Régénération 3§7",
                "§7à ces joueurs pendant 1m30. Donne au porteur",
                "§bVitesse 3§7 et§a Jump Boost 4§7 pendant 1m30.",
                "§7(Cooldown - 15 minutes)");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            onClick.cancelOriginalUse(true);
            if(playerManager.hasRole() && playerManager.getRole() instanceof Unohana){
                Unohana unohana = (Unohana) playerManager.getRole();
                if(playerManager.getRoleCooldownMinazuki() <= 0){
                    Player target = ClassUtils.getTargetPlayer(player, 3);
                    if(target != null){
                        unohana.setRiding(true);
                        unohana.setDecountRiding(10);
                        unohana.addRidingPlayer(main.getPlayerManager(target.getUniqueId()), player);
                        player.sendMessage("§eVous pouvez porter encore " + unohana.getRidedRemining() + " personnes !");
                    }
                } else {
                    player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownMinazuki()));
                }
            } else {
                player.sendMessage(Messages.not("Unohana"));
            }
        });
    }
}
