package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Byakuya;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class Senbonzakura extends QuickItem {
    public Senbonzakura(UhcHost main) {
        super(Material.INK_SACK, 1,(byte)9);
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.setName("§dSenbonzakura");
        super.setLore("",
                "§7Créer une zone (30x30) qui se",
                "§7dissipe au bout de 3 minutes.",
                "§7A l'interieur, l'utilisateur possède§c Force 1",
                "§7et §9Résistance 1§7. Les possèdes malus en",
                "§7entrant/sortant de la zone.",
                "§7(Cooldown - 20 minutes)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof Byakuya){
                Byakuya byakuya = (Byakuya) playerManager.getRole();
                if(bleachPlayerManager.canUsePower()){
                    if(playerManager.getRoleCooldownSenbonzakura() <= 0){
                        byakuya.createZone(player.getLocation());
                        playerManager.setRoleCooldownSenbonzakura(20*60);
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownSenbonzakura()));
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }

            }
        });
    }


}
