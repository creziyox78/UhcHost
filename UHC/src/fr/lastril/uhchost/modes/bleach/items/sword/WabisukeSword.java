package fr.lastril.uhchost.modes.bleach.items.sword;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Kira;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemFlag;

import static fr.lastril.uhchost.tools.API.ClassUtils.getLookingAt;

public class WabisukeSword extends QuickItem {

    public WabisukeSword(UhcHost main) {
        super(Material.DIAMOND_SWORD);
        super.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
        super.setName("§eWabisuke");
        super.setLore("",
                "§7Tout les 5 coups infligés à",
                "§7un joueur lui donne§8 Slowness 1§7",
                "§7puis§8 Slowness 2§7 au delà de",
                "§710 coups.",
                "",
                "§fClique droit - (Cooldown 1 minute 30 secondes)",
                "§7Permet de téléporter un joueur sur soi-même.");
        super.setInfinityDurability();
        super.addItemFlag(ItemFlag.HIDE_UNBREAKABLE);
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof Kira){
                if(onClick.getAction() == Action.RIGHT_CLICK_AIR || onClick.getAction() == Action.RIGHT_CLICK_BLOCK){
                    if(bleachPlayerManager.canUsePower()){
                        if(playerManager.getRoleCooldownWabisuke() <= 0){
                            Player target = ClassUtils.getTargetPlayer(player, 8);
                            PlayerManager targetManager = main.getPlayerManager(player.getUniqueId());
                            if(target != null && targetManager.isAlive()){
                                Location loc1 = player.getLocation();
                                target.teleport(loc1);
                                playerManager.setRoleCooldownWabisuke(60+30);
                            } else {
                                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous devez cibler un joueur !");
                            }
                        } else {
                            player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownWabisuke()));
                        }
                    } else {
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                    }
                }
            }
        });
    }
}
