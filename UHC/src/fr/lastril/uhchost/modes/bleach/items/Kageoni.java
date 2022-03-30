package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Kyoraku;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Kageoni extends QuickItem {
    public Kageoni(UhcHost main) {
        super(Material.SUGAR);
        super.setName("§fKageoni");
        super.setLore("",
                "§7L'utilisateur devient invisible (avec son armure)",
                "§7pendant 5 secondes. L'effet s'estompe avant",
                "§7si l'utilisateur frappe un joueur.",
                "§7Le prochain coup infligera 50% de dégâts",
                "§7supplémentaires.",
                "§7(Cooldown - 2 minutes)");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof Kyoraku){
                Kyoraku kyoraku = (Kyoraku) playerManager.getRole();
                if(bleachPlayerManager.canUsePower()){
                    if(playerManager.getRoleCooldownKageoni() <= 0){
                        kyoraku.setInInvisible(true);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*5, 0, false, false));
                        Bukkit.getScheduler().runTaskLater(main, () -> kyoraku.setInInvisible(false), 20*5);
                        playerManager.setRoleCooldownKageoni(2*60);
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownKageoni()));
                    }
                }
            }
        });
    }
}
