package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Hisagi;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemFlag;

public class Chaines extends QuickItem {
    public Chaines(UhcHost main) {
        super(Material.SNOW_BALL);
        super.setName("§7Chaînes");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.setLore("",
                "§7Permet d'immobiliser le joueur touché",
                "§7pendant 3 secondes.",
                "§7(Cooldown - 5 minutes)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            onClick.cancelOriginalUse(true);
            if(playerManager.hasRole() && playerManager.getRole() instanceof Hisagi){
                if(bleachPlayerManager.canUsePower()){
                    if(playerManager.getRoleCooldownChaines() <= 0){
                        if(onClick.getAction() == Action.RIGHT_CLICK_AIR || onClick.getAction() == Action.RIGHT_CLICK_BLOCK){
                            Snowball snowball = player.launchProjectile(Snowball.class);
                            snowball.setCustomName("§7Chaînes");
                            playerManager.setRoleCooldownChaines(5*60);
                        }
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownChaines()));
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            }
        });
    }
}
