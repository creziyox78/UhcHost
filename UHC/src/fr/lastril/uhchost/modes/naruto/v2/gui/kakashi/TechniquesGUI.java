package fr.lastril.uhchost.modes.naruto.v2.gui.kakashi;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Kakashi;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class TechniquesGUI extends IQuickInventory {

    private final UhcHost main;
    private final Kakashi kakashi;

    public TechniquesGUI(UhcHost main, Kakashi kakashi) {
        super(ChatColor.GOLD+"Techniques", 9*6);
        this.main = main;
        this.kakashi = kakashi;
    }

    @Override
    public void contents(QuickInventory inv) {

        for (UUID player : kakashi.getCopieds()) {
            PlayerManager joueur = main.getPlayerManager(player);
            List<String> listPotions = new ArrayList<>();
            listPotions.add("");
            List<PotionEffect> effects = joueur.getRole().getEffects().entrySet().stream().filter(e -> e.getValue() == When.START).map(Map.Entry::getKey).collect(Collectors.toList());
            joueur.getRole().getEffects().keySet().forEach(potionEffect -> listPotions.add("§e" + potionEffect.getType().getName() + " " + potionEffect.getAmplifier() + 1));
            inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(joueur.getPlayerName()).setSkullOwner(joueur.getPlayerName())
                    .setLore(listPotions)
                    .toItemStack(), onClick -> {
                Player playerClick = onClick.getPlayer();

                playerClick.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(playerClick::removePotionEffect);
                effects.forEach(playerClick::addPotionEffect);
                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"§aVous avez récupéré les effets de "+joueur.getPlayerName()+" !");

            });
        }
    }
}
