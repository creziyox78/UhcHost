package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.soulsociety.Omaeda;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemFlag;

public class Gegetsuburi extends QuickItem {
    public Gegetsuburi(UhcHost main) {
        super(Material.FISHING_ROD);
        super.setName("§bGegetsuburi");
        super.setLore("",
                "§f- Clique droit (Cooldown - 20 minutes)",
                "§7Permet d'attirer un joueur attrapé",
                "§7par cette canne à pêche.",
                "",
                "§f- Clique Gauche",
                "§7Retire 3 coeurs au joueur pris",
                "§7dans votre canne à pêche. (Cooldown - 45 secondes)");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.addItemFlag(ItemFlag.HIDE_UNBREAKABLE);
        super.setInfinityDurability();
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if(playerManager.hasRole() && playerManager.isAlive()){
                if(playerManager.getRole() instanceof Omaeda){
                    if(onClick.getAction() == Action.LEFT_CLICK_AIR || onClick.getAction() == Action.LEFT_CLICK_BLOCK){
                        Omaeda omaeda = (Omaeda) playerManager.getRole();
                        Entity entity = omaeda.getGrabed();
                        if(entity instanceof Player){
                            if(playerManager.getRoleCooldownGegetsuburiDamage() <= 0){
                                Player target = (Player) entity;
                                ClassUtils.setCorrectHealth(target, target.getHealth() - 3D*2D, false);
                                playerManager.setRoleCooldownGegetsuburiDamage(45);
                                target.sendMessage("§6Vous venez d'être touché par \"§bGegetsuburi\"§6 qui vous fait perdre 3 coeurs.");
                                player.sendMessage("§6Vous avez frappé " + target.getName() + " avec \"§bGegetsuburi\".");
                            } else {
                                player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownGegetsuburiDamage()));
                            }
                        }
                    }
                } else {
                    player.sendMessage(Messages.not("Omaeda"));
                }
            }
        });
    }
}
