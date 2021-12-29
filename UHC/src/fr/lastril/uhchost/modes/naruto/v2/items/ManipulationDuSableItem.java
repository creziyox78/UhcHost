package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.gui.manipulationdusable.ManipulationDuSableGUI;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Gaara;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class ManipulationDuSableItem extends QuickItem {

    private NarutoV2Manager narutoV2Manager;

    public ManipulationDuSableItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§6Manipulation du sable");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
                if (joueur.getRole() instanceof Gaara) {
                    Gaara gaara = (Gaara) joueur.getRole();
                    if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                        if(onClick.getAction() == Action.LEFT_CLICK_AIR || onClick.getAction() == Action.LEFT_CLICK_BLOCK){
                            if(gaara.getSandShape() != null){
                                int sandsInInventory = gaara.getSandsInInventory(player.getInventory());
                                int price = gaara.isInShukaku() ? gaara.getSandShape().getSandPrice() / 2 : gaara.getSandShape().getSandPrice();
                                if (price <= sandsInInventory) {
                                    if(gaara.getSandShape().useCapacity(player, gaara)){
                                        gaara.removeSandsInInventory(player.getInventory(), price);
                                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+Messages.USED_POWER.getMessage());
                                    }
                                    gaara.usePower(joueur);
                                    gaara.usePowerSpecific(joueur, super.getName());
                                } else {
                                    player.sendMessage(Messages.error("Vous n'avez pas assez de sable (manque "+(price-sandsInInventory)+" sables)"));
                                }
                            }else{
                                player.sendMessage(Messages.error("Vous n'avez pas choisi de sort !"));
                            }
                        }else{
                            new ManipulationDuSableGUI(main, gaara).open(player);
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                } else {
                    player.sendMessage(Messages.not("Gaara"));
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            }
        });
    }
}