package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.gui.manipulationdusable.ManipulationDuSableGUI;
import fr.maygo.uhc.modes.naruto.v2.roles.solo.Gaara;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class ManipulationDuSableItem extends QuickItem {

    public ManipulationDuSableItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§6Manipulation du sable");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Gaara) {
                    Gaara gaara = (Gaara) PlayerManager.getRole();
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        if (onClick.getAction() == Action.LEFT_CLICK_AIR || onClick.getAction() == Action.LEFT_CLICK_BLOCK) {
                            if (gaara.getSandShape() != null) {
                                int sandsInInventory = gaara.getSandsInInventory(player.getInventory());
                                int price = gaara.isInShukaku() ? gaara.getSandShape().getSandPrice() / 2 : gaara.getSandShape().getSandPrice();
                                if (price <= sandsInInventory) {
                                    if (gaara.getSandShape().useCapacity(player, gaara)) {
                                        gaara.removeSandsInInventory(player.getInventory(), price);
                                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                                    }
                                } else {
                                    player.sendMessage(Messages.error("Vous n'avez pas assez de sable (manque " + (price - sandsInInventory) + " sables)"));
                                }
                            } else {
                                player.sendMessage(Messages.error("Vous n'avez pas choisi de sort !"));
                            }
                        } else {
                            new ManipulationDuSableGUI(main, gaara).open(player);
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    player.sendMessage(Messages.not("Gaara"));
                    return;
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }
}