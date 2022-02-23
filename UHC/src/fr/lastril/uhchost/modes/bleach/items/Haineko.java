package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.BleachRoles;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.RangikuMatsumoto;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class Haineko extends QuickItem {
    public Haineko(UhcHost main) {
        super(Material.ENDER_PORTAL_FRAME);
        super.setName("§9Haineko");
        super.setLore("",
                "§7Créer une zone de 20x20",
                "§7A l'intérieur, tous les joueurs",
                "§7sauf l'utilisateur possède Blindness 1.",
                "§7L'utilisateur possède§b Vitesse 1§7dans",
                "§7cette zone. (Cooldown - 10 minutes)");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof RangikuMatsumoto){
                if(bleachPlayerManager.canUsePower()){
                    if(playerManager.getRoleCooldownHaineko() <= 0){
                        RangikuMatsumoto rangikuMatsumoto = (RangikuMatsumoto) playerManager.getRole();
                        rangikuMatsumoto.deleteZone();
                        rangikuMatsumoto.createZone(player.getLocation());
                        playerManager.setRoleCooldownHaineko(60*10);
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownHaineko()));
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            }
        });
    }
}
