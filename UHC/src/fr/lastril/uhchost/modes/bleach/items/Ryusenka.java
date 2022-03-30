package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.ToshiroHitsugaya;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class Ryusenka extends QuickItem {
    public Ryusenka(UhcHost main) {
        super(Material.BONE);
        super.setName("§fRyusenka");
        super.setLore("",
                "§7Inflige 20% des dégâts infligés",
                "§7au dernier joueur frappé durant",
                "§7les 5 dernières secondes.",
                "§75(Cooldown - 10 minutes)");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof ToshiroHitsugaya){
                ToshiroHitsugaya toshiroHitsugaya = (ToshiroHitsugaya) playerManager.getRole();
                if(bleachPlayerManager.canUsePower()){
                    if(playerManager.getRoleCooldownRyusenka() <= 0){
                        toshiroHitsugaya.applyDamages();
                        playerManager.setRoleCooldownRyusenka(10*60);
                        if(toshiroHitsugaya.getDamaged() != null){
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§b" + toshiroHitsugaya.getDamaged().getName() + " a subit 20% de " + toshiroHitsugaya.getHealthDamaged());
                        }
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownRyusenka()));
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            }

        });
    }
}
