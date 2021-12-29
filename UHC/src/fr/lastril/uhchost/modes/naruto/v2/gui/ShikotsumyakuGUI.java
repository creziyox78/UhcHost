package fr.lastril.uhchost.modes.naruto.v2.gui;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.generation.ForetOsGenerator;
import fr.lastril.uhchost.modes.naruto.v2.items.swords.EpeeOsItem;
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.Kimimaro;
import fr.lastril.uhchost.modes.naruto.v2.tasks.EpeeOsTask;
import fr.lastril.uhchost.modes.naruto.v2.tasks.ForetOsTask;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShikotsumyakuGUI extends IQuickInventory {

    private final UhcHost main;
    private final Kimimaro kimimaro;

    public ShikotsumyakuGUI(UhcHost main, Kimimaro kimimaro) {
        super("§6Shikotsumyaku", 9*1);
        this.main = main;
        this.kimimaro = kimimaro;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE, 1, 0).setName(" ").toItemStack(), 0, 8);

        inv.setItem(new QuickItem(Material.BONE).setName("§cEpée en os")
                .setLore("",
                        "§7Permet de récupérer un os aussi puissant",
                        "§7qu'une épée en diamant Tranchant 5")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            if(kimimaro.getEpeeOsUses() < 5){
                ItemStack os = new QuickItem(Material.BONE).setName("§cEpée en os").toItemStack();
                ItemStack epee = new EpeeOsItem(os).getItem();

                main.getInventoryUtils().giveItemSafely(playerClick, epee);
                kimimaro.addEpeeOsUse();
                new EpeeOsTask(playerClick.getUniqueId(), epee).runTaskTimer(main, 0, 20);
                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous avez reçu votre Épée en Os.");
            }else{
                playerClick.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
            }
            inv.close(playerClick);
        }, 3);

        inv.setItem(new QuickItem(Material.SAPLING, 1, (byte) 3).setName("§cForêt d’os")
                .setLore("",
                        "§7Permet de créer une forêt en quartz",
                        "§7qui disparaîtra au bout de 3 minutes.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            if(!kimimaro.hasUsedForetOs()){
                List<Block> blocks = ForetOsGenerator.spawnForetOs(playerClick.getLocation(), 50);

                new ForetOsTask(playerClick.getUniqueId(), blocks).runTaskTimer(main, 0, 20);

                kimimaro.setUsedForetOs(true);
                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous avez fait apparaître votre forêt d'os !");
            }else{
                playerClick.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
            }
            inv.close(playerClick);
        }, 5);
    }
}
