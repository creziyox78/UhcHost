package fr.lastril.uhchost.modes.naruto.v2.gui.genjutsu;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.items.GenjutsuItem;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class IzanamiSelectGUI extends IQuickInventory {

    private final UhcHost main;
    private final GenjutsuItem.GenjutsuUser genjutsuUser;
    private final Player izanamiPlayer;

    public IzanamiSelectGUI(UhcHost main, GenjutsuItem.GenjutsuUser genjutsuUser, Player izanamiPlayer) {
        super("§cIzanami", 9 * 1);
        super.setClosable(false);
        this.main = main;
        this.genjutsuUser = genjutsuUser;
        this.izanamiPlayer = izanamiPlayer;
    }

    @Override
    public void contents(QuickInventory inv) {
        /* CONTOUR GLASS */
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE, 1, 0).setName(" ").toItemStack(), 0, 8);

        inv.setItem(new QuickItem(Material.PAPER).setName("§eRejoindre le camps du porteur d'Izanami")
                .setLore(" ", "§7Vous abandonner votre ancien camp,", "§7et rejoignez celui du porteur d'Izanami")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId()), izanamiPlayerManager = main.getPlayerManager(izanamiPlayer.getUniqueId());
            PlayerManager.setCamps(izanamiPlayerManager.getCamps());
            izanamiPlayer.setMaxHealth(izanamiPlayer.getMaxHealth() - (5D * 2D));
            izanamiPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + playerClick.getName() + " a choisi de rejoindre votre camp.");
            playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez décidé de rejoindre le camp du porteur d'Izanami. Voici son pseudo: " + izanamiPlayer.getName() + ".");
            inv.close(playerClick);
        }, 3);

        inv.setItem(new QuickItem(Material.PAPER).setName("§6Rester dans son camps")
                .setLore(" ", "§7Vous restez dans votre camp,", "§7mais vous avez §8Weakness et", "§7Slowness permanent ainsi que", "§C5 coeurs permanents.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            playerClick.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false));
            playerClick.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0, false, false));
            playerClick.setMaxHealth(playerClick.getMaxHealth() - (5D * 2D));

            izanamiPlayer.setMaxHealth(izanamiPlayer.getMaxHealth() - (5D * 2D));
            izanamiPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + playerClick.getName() + " a choisi de rester dans son camp.");
            playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous avez choisi de rester dans votre camp mais vous avez§7 Slowness I§c et§7 Weakness I§c ainsi que 5 coeurs permanent");
            inv.close(playerClick);
        }, 5);
    }
}
