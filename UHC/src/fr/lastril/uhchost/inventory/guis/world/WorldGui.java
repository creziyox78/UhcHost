package fr.lastril.uhchost.inventory.guis.world;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.tools.Items;
import fr.lastril.uhchost.tools.NotStart;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import fr.lastril.uhchost.world.tasks.ChunkLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class WorldGui extends Gui {

    private final UhcHost pl = UhcHost.getInstance();

    public WorldGui(Player player) {
        super(player, 3*9, "§bParamètre du monde");
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, Items.getItemColored(Material.STAINED_GLASS_PANE, " ", (byte) 7, true));
        }
        ItemStack is = pl.gameManager.getBiomeState().getItemBiome();
        inventory.setItem(10, is);

        if(pl.gameManager.isValidateWorld()){
            if(!pl.getGamemanager().isPregen())
                inventory.setItem(13, new ItemsCreator(Material.EMPTY_MAP, "§aPrégénération", Arrays.asList("", "§7Configurer sur:§b " + pl.worldBorderUtils.getStartSize() +"x" + pl.worldBorderUtils.getStartSize()), 1).create());
        } else {
            inventory.setItem(13, new ItemsCreator(Material.BARRIER, "§cMonde non validé !", Arrays.asList("§7Vous devez valider le monde", "§7en le pré-visualisant (bouton à droite)."), 1).create());
        }
        if(!pl.getGamemanager().isPregen() && !pl.gameManager.isValidateWorld())
            inventory.setItem(16, new ItemsCreator(Material.GRASS, "§ePré-visualisation", Arrays.asList("§7Vérifiez que le centre", "§7est celui dont vous voulez !"), 1).create());

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;
        if (event.getClickedInventory().equals(inventory)) {
            ItemsCreator ic;
            ItemStack is = event.getCurrentItem();
            if (is == null || is.getType() == Material.AIR)
                return;
            event.setCancelled(true);
            switch (is.getType()){
                case EMPTY_MAP:
                    player.closeInventory();
                    Bukkit.broadcastMessage("§6Prégénération du monde ! Risque de lags !");
                    new ChunkLoader(pl, pl.worldBorderUtils.getStartSize());
                    break;
                case SAPLING:
                    if(pl.gameManager.isPregen() || pl.gameManager.isValidateWorld()){
                        player.closeInventory();
                        player.sendMessage("§cErreur: Le monde a déjà été crée. Vous ne pouvez plus changer de biome.");
                    } else {
                        player.closeInventory();
                        new BiomeChooseGui(player).show();
                    }
                    break;
                case GRASS:
                    player.closeInventory();
                    player.sendMessage("§eCréation du monde... Merci de patienter.");
                    pl.gameManager.setPlayerCheckingWorld(true);
                    WorldCreator.name("game").createWorld();
                    player.sendMessage("§aMonde créé ! Téléportation au centre...");
                    player.teleport(new Location(Bukkit.getWorld("game"), 0, 100, 0));
                    NotStart.checkingWorld(player);
                    break;
            }
        }

    }

    @EventHandler
    public void onClick(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory))
            HandlerList.unregisterAll(this);
    }
}
