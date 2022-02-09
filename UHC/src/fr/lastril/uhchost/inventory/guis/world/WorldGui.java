package fr.lastril.uhchost.inventory.guis.world;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.tools.API.inventory.GUIYesNo;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.NotStart;
import fr.lastril.uhchost.world.tasks.ChunkLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public class WorldGui extends IQuickInventory {

    private final UhcHost pl = UhcHost.getInstance();

    public WorldGui() {
        super("§bParamètre du monde", 3*9);
    }
    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("update", taskUpdate -> {
            for (int i = 0; i < inv.getInventory().getSize(); i++) {
                inv.setItem(new QuickItem(Material.STAINED_GLASS_PANE, 1,(byte) 7).toItemStack(), i);
            }

            if(!pl.gameManager.isValidateWorld()){
                ItemStack is = pl.gameManager.getBiomeState().getItemBiome();
                inv.setItem(is, onClick -> {
                    new BiomeChooseGui().open(onClick.getPlayer());
                },10);
            } else {
                inv.setItem(new QuickItem(Material.BARRIER).setName("§aCentre choisi: " + pl.getGamemanager().getBiomeState().getItemBiome().getItemMeta().getDisplayName())
                        .setLore("",
                                "§cPour changer ce paramètre, ","§cvous devez redémarrez le serveur.")
                        .toItemStack(), onClick -> {
                },10);
            }
            if(pl.gameManager.isValidateWorld()){
                if(!pl.getGamemanager().isPregen())
                    inv.setItem(new ItemsCreator(Material.EMPTY_MAP, "§aPrégénération", Arrays.asList("", "§7Configurer sur:§b "
                                                + pl.worldBorderUtils.getStartSize()/2 +"x" + pl.worldBorderUtils.getStartSize()/2), 1).create(), onClick -> {
                        onClick.getPlayer().closeInventory();
                        new ChunkLoader(pl, pl.worldBorderUtils.getStartSize()/2);
                        Bukkit.broadcastMessage("§6Prégénération du monde ! Risque de lags !");
                    },12);
            } else {
                inv.setItem(new ItemsCreator(Material.BARRIER, "§cMonde non validé !", Arrays.asList("§7Vous devez valider le monde", "§7en le pré-visualisant (bouton à droite)."), 1).create(), 12);
            }

            if(!pl.gameManager.isValidateWorld()){
                inv.setItem(new QuickItem(Material.DIAMOND_ORE).setName("§aBoost de minerais: "
                        + (pl.getGamemanager().isBoostOres() ? "§aActivé" : "§cDésactivé")).toItemStack(), onClick -> {
                    pl.getGamemanager().setBoostOres(!pl.getGamemanager().isBoostOres());
                },14);
            } else {
                inv.setItem(new QuickItem(Material.BARRIER).setName("§aBoost de minerais: "
                        + (pl.getGamemanager().isBoostOres() ? "§aActivé" : "§cDésactivé"))
                                .setLore("",
                                        "§cPour changer ce paramètre, ","§cvous devez redémarrez le serveur.")
                        .toItemStack(), onClick -> {
                },14);
            }


            if(!pl.getGamemanager().isPregen() && !pl.gameManager.isValidateWorld()){
                inv.setItem(new ItemsCreator(Material.GRASS, "§ePré-visualisation", Arrays.asList("§7Vérifiez que le centre", "§7est celui dont vous voulez !"), 1).create(), onClick -> {
                    onClick.getPlayer().closeInventory();
                    Bukkit.broadcastMessage("§eCréation du monde... Merci de patienter.");
                    pl.gameManager.setPlayerCheckingWorld(true);
                    WorldCreator.name("game").createWorld();
                    Bukkit.getWorld("game").setGameRuleValue("doDaylightCycle", "false");
                    Bukkit.getWorld("game").setGameRuleValue("showDeathMessages", "false");
                    Bukkit.getWorld("game").setGameRuleValue("keepInventory", "true");

                    onClick.getPlayer().sendMessage("§aMonde créé ! Téléportation au centre...");
                    onClick.getPlayer().teleport(new Location(Bukkit.getWorld("game"), 0, 100, 0));

                    NotStart.checkingWorld(onClick.getPlayer());
                },16);
            }

            else {
                inv.setItem(new ItemsCreator(Material.GRASS, "§ePré-visualisation", Arrays.asList("§7Vérifiez que le centre", "§7est celui dont vous voulez !"), 1)
                                .create(),onClick -> {
                    pl.gameManager.setPlayerCheckingWorld(true);
                    onClick.getPlayer().sendMessage("§aTéléportation au centre...");
                    onClick.getPlayer().teleport(new Location(Bukkit.getWorld("game"), 0, 100, 0));
                    NotStart.checkingWorld(onClick.getPlayer());
                },16);
            }

            inv.setItem((new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), Collections.singletonList(""))).create(), onClick -> {
                new HostConfig().open(onClick.getPlayer());
            },inv.getInventory().getSize() - 1);
        });
    }
}
