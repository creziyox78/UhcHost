package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.SoiFon;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class Suzumebachi extends QuickItem {

    public Suzumebachi(UhcHost main) {
        super(Material.DOUBLE_PLANT, 1, (byte)2);
        super.setName("§6Suzumebachi");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.setLore("",
                "§f1ère utilisation (sur un joueur) :",
                "§7  - Inflige une marque qui est déposé",
                "§7    sur le joueur frappé avec.",
                "",
                "§f2ème utilisation (sur le joueur marqué après 15 secondes) :",
                "§7  - Immobilise le joueur durant pendant",
                "§7    pendant 3 secondes. Donne l'effet",
                "§7    §fWither 2§7 pendant 5 secondes.",
                "",
                "§7(Cooldown - 3 minutes)");
    }
}
