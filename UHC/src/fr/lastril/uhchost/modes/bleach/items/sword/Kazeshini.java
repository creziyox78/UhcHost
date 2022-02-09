package fr.lastril.uhchost.modes.bleach.items.sword;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Hisagi;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class Kazeshini extends QuickItem {
    public Kazeshini(UhcHost main) {
        super(Material.IRON_SWORD);
        super.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
        super.setName("§6Kazeshini");
        super.setLore("",
                "§7Permet d'échanger l'épée du joueur ciblé",
                "§7avec un autre objet de son inventaire.",
                "§7(Cooldown - 3 minutes 30 secondes");
        super.setInfinityDurability();
        super.addItemFlag(ItemFlag.HIDE_UNBREAKABLE);
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.isAlive()){
                if(playerManager.getRole() instanceof Hisagi){
                    if(bleachPlayerManager.canUsePower()){
                        if(playerManager.getRoleCooldownKazeshini() <= 0){
                            Player target = ClassUtils.getTargetPlayer(player, 10);
                            if(target != null){
                                ClassUtils.changeSlotItemRandomlyInInventory(target, new QuickItem(Material.IRON_SWORD).toItemStack(), false);
                                ClassUtils.changeSlotItemRandomlyInInventory(target, new QuickItem(Material.DIAMOND_SWORD).toItemStack(), false);
                                playerManager.setRoleCooldownKazeshini(60*3 + 30);
                                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§a" + target.getName() + " ne sait plus où se trouve son épée !");
                                target.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§a Votre épée vient de changer de place dans votre inventaire !");
                            } else {
                                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous devez visé un joueur !");
                            }
                        } else {
                            player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownKazeshini()));
                        }
                    } else {
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                    }
                } else {
                    player.sendMessage(Messages.not("Hisagi"));
                }
            }
        });
    }
}
