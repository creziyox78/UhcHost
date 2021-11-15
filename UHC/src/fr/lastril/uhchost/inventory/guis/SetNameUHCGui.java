package fr.lastril.uhchost.inventory.guis;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.AnvilGUI;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;

public class SetNameUHCGui extends AnvilGUI {

    public SetNameUHCGui(UhcHost main) {
        super(onClick -> {
            if (onClick.getItem() != null) {
                if (onClick.getItem().getItemMeta().getDisplayName().length() > 30) {
                    onClick.getPlayer().sendMessage("§cLe nom de l'uhc ne peut pas être plus grand que 30 caractères !");
                } else {
                    main.gameManager.setGameName(onClick.getItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
                    onClick.getPlayer()
                            .sendMessage("§aVous avez bien changé le nom de l'UHC pour : "
                                    + onClick.getItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
                    destroy(onClick.getPlayer());
                }
            }
        });
        super.setSlot(AnvilSlot.INPUT_LEFT, new QuickItem(Material.PAPER).setName("§eNom du serveur").toItemStack());
    }
}
