package fr.lastril.uhchost.modes.naruto.v2.gui.manipulationdusable;

import fr.atlantis.api.enums.HeadTextures;
import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.modes.naruto.v2.roles.solo.Gaara;
import org.bukkit.Material;
import org.bukkit.SkullType;

public class ManipulationDuSableGUI extends IQuickInventory {

    private final UhcHost main;
    private final Gaara gaara;

    public ManipulationDuSableGUI(UhcHost main, Gaara gaara) {
        super("§6Manipulation du sable", 9 * 1);
        this.main = main;
        this.gaara = gaara;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);


        inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                .setName("§cAttaque")
                .setLore("",
                        "§7Vous redirige vers un menu comportant",
                        "§7les techniques d'attaque§e \"Tsunami de Sable\"§7,",
                        "§e\"Sarcophage\"§7 et §e\"Lance\"§7.",
                        "",
                        "§8Usage: §6Une fois une technique sélectionné, vous pouvez",
                        "§6faire des cliques gauches à répétition pour l'utiliser,",
                        "§6sans passer par le menu.")
                .setTexture(fr.maygo.uhc.enums.HeadTextures.EPEE.getHash())
                .toItemStack(), onClick -> {
            new AttaqueGUI(main, gaara).open(onClick.getPlayer());
        }, 3);


        inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                .setName("§aDéfense")
                .setLore("",
                        "§7Vous redirige vers un menu comportant",
                        "§7les techniques de défense§e \"Bouclier de Sable\"§7,",
                        "§e\"Armure de Sable\"§7 et §e\"Suspension du Désert\"§7.",
                        "",
                        "§8Usage: §6Une fois une technique sélectionné, vous pouvez",
                        "§6faire des cliques gauches à répétition pour l'utiliser,",
                        "§6sans passer par le menu.")
                .setTexture(HeadTextures.BOUCLIER)
                .toItemStack(), onClick -> {
            new DefenseGUI(main, gaara).open(onClick.getPlayer());
        }, 5);
    }
}
