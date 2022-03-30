package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.JushiroUkitake;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DanseDuVent extends QuickItem {
    public DanseDuVent(UhcHost main) {
        super(Material.RABBIT_FOOT);
        super.setName("§fDanse du Vent");
        super.setLore("",
                "§7Octroie§9 Résistance 1§7 pendant 30 secondes.",
                "§7A chaque joueur frappé, les autres joueurs" ,
                "§7dans un rayon de 5 blocs,",
                "§7sauf l'utilisateur, sont également frappé.",
                "§7(Cooldown - 7 minutes)");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof JushiroUkitake){
                JushiroUkitake jushiroUkitake = (JushiroUkitake) playerManager.getRole();
                if(bleachPlayerManager.canUsePower()){
                    if(playerManager.getRoleCooldownDanseDuVent() <= 0){
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*30, 0, false, false));
                        jushiroUkitake.setInDanseDuVent(true);
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                        Bukkit.getScheduler().runTaskLater(main, () -> jushiroUkitake.setInDanseDuVent(false), 20*30);
                        playerManager.setRoleCooldownDanseDuVent(7*60);
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownDanseDuVent()));
                    }

                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }

            }
        });
    }
}
