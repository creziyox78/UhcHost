package fr.lastril.uhchost.modes.lg.gui;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.lg.roles.village.ChienLoup;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ChooseCampGui extends IQuickInventory {

    private final ChienLoup chienLoup;

    public ChooseCampGui(ChienLoup chienLoup) {
        super("§eChien-Loup > Choix du camp", 1*9);
        this.chienLoup = chienLoup;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setItem(new QuickItem(Material.ROTTEN_FLESH).setName(Camps.LOUP_GAROU.getCompoColor() + "Loup-Garou")
                .setLore("",
                        "§7Vous devrez gagner avec les",
                        "§cLoups-Garous§7. Vous aurez les effets",
                        "§cForce I§7 et§f Night Vision§7 la nuit.",
                        "§7Vous§e ne serez pas§7 dans la liste des Loups-Garous",
                        "§7mais vous obtiendrez la liste.").toItemStack(), onClick -> {
            chienLoup.setChoosenCamp(Camps.LOUP_GAROU);
            chienLoup.setChoosen(true);
            chienLoup.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.NIGHT);
            chienLoup.addEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 1, 0, false, false), When.AT_KILL);
            chienLoup.addEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 1, 0, false, false), When.AT_KILL);

            onClick.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous avez décidé de gagner avec les Loups-Garous. " +
                    "Vous avez les effets §cForce I§7 et§f Night Vision§7 la nuit. Vous n'apparaîtrez pas dans la liste des Loups-Garou.");
        },3);

        inv.setItem(new QuickItem(Material.ROTTEN_FLESH).setName(Camps.LOUP_GAROU.getCompoColor() + "Loup-Garou")
                .setLore("",
                        "§7Vous devrez gagner avec les",
                        "§aVillageois§7?",
                        "§7Vous§e serez§7 dans la liste des Loups-Garous",
                        "§7mais vous n'obtiendrez pas la liste.").toItemStack(), onClick -> {
            chienLoup.setChoosenCamp(Camps.VILLAGEOIS);
            chienLoup.setChoosen(true);
            onClick.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aVous avez décidé de gagner avec les villageois. " +
                    "Vous n'avez donc aucun effet et vous apparaîtrez dans la liste des Loups-Garou.");
        }, 5);
    }
}
