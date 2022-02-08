package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Iba;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class Ryodan extends QuickItem {
    public Ryodan(UhcHost main) {
        super(Material.IRON_BLOCK);
        super.setName("§9Ryodan");
        super.setLore("",
                "§7Octroie l'effet§8 Slowness II§7 pendant",
                "§77 secondes. Le joueur recevant un coup pendant",
                "§7ce laps de temps sera éjecté en arrière. Si le joueur",
                "§etouche un mur§7, il sera immobilisé pendant 3 secondes",
                "§7et recevra§8 Blindness III§7 également.");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof Iba){
                Iba iba = (Iba) playerManager.getRole();
                if(bleachPlayerManager.canUsePower()){
                    if(playerManager.getRoleCooldownRyodan() <= 0){
                        iba.setInRyodan(true);
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                        Bukkit.getScheduler().runTaskLater(main, () -> {
                            if(iba.isInRyodan()){
                                iba.setInRyodan(false);
                                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cRyodan s'estompe...");
                            }
                        }, 20*7);
                        playerManager.setRoleCooldownRyodan(30);
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownRyodan()));
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            }
        });
    }
}
