package fr.lastril.uhchost.inventory.guis.world.ores;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.game.rules.BlocksRule;
import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OresGui extends IQuickInventory {
    public OresGui() {
        super(I18n.tl("guis.ores.name"), 9);
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("ores", taskUpdate -> {
            int index = 0;
            for (BlocksRule blocksRule : (UhcHost.getInstance()).gameManager.getBlocksRules()) {
                ItemsCreator itemsCreator = new ItemsCreator(blocksRule.id, null, new ArrayList<>());
                List<String> lores = itemsCreator.getLores();
                lores.add(I18n.tl("guis.ores.lore", String.valueOf(blocksRule.round)));
                lores.add(I18n.tl("guis.ores.lore1", String.valueOf(blocksRule.minY)));
                lores.add(I18n.tl("guis.ores.lore2",String.valueOf(blocksRule.maxY)));
                lores.add(I18n.tl("guis.ores.lore3",String.valueOf(blocksRule.size)));
                itemsCreator.setLores(lores);
                inv.setItem(itemsCreator.create(), onClick -> new OresActionGui(UhcHost.getInstance().gameManager.getRule(blocksRule.id)).open(onClick.getPlayer()),index);
                index++;
            }
            ItemsCreator ic = new ItemsCreator(Material.PAPER, I18n.tl("guis.ores.important"), Arrays.asList(I18n.tl("guis.ores.importantLore"), I18n.tl("guis.ores.importantLore1"),
                    I18n.tl("guis.ores.importantLore2"), I18n.tl("guis.ores.importantLore3")));
            inv.setItem(ic.create(),7);

            inv.setItem(new QuickItem(Material.GRASS).setName("§aRegénérer les chunks").setLore("",
                    "§7Permet de regénérer les chunks",
                    "§7du monde pour appliquer les",
                    "§7paramètres de chaque minerais.").toItemStack(), onClick -> {
                if(Bukkit.getWorld("game") != null){
                    onClick.getPlayer().closeInventory();
                    Bukkit.broadcastMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§eApplications des changements de minerais en cours... Patienter.");
                    for (Chunk c : Bukkit.getWorld("game").getLoadedChunks()) {
                        Bukkit.getWorld("game").unloadChunk(c);
                        Bukkit.getWorld("game").regenerateChunk(c.getX(), c.getZ());
                    }
                    Bukkit.broadcastMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§aRegénération des minerais terminés !");
                } else {
                    onClick.getPlayer().closeInventory();
                    onClick.getPlayer().sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§cLe monde n'a pas encore été pré-visualisé !");
                }
            },6);

            ic = new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), Arrays.asList(""));
            inv.setItem(ic.create(), onClick -> {
                new HostConfig().open(onClick.getPlayer());
            },8);
        });

    }
}
