package fr.lastril.uhchost.inventory.guis;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.inventory.guis.items.PotionsGui;
import fr.lastril.uhchost.inventory.guis.world.border.BorderGui;
import fr.lastril.uhchost.team.ToGui;
import fr.lastril.uhchost.inventory.CustomInv;
import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.Items;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public class RulesGui extends Gui {

    private final UhcHost pl = UhcHost.getInstance();

    public RulesGui(Player player) {
        super(player, 9*3, ChatColor.AQUA + "Règles");
        ItemStack ic = new ItemsCreator(Material.BLAZE_ROD, I18n.tl("guis.main.teams"),
                Collections.singletonList(I18n.tl("guis.main.teamsLore"))).create();
        inventory.setItem(0, ic);
        ic = new ItemsCreator(Material.DIAMOND_SWORD, I18n.tl("guis.main.pvp"), Arrays
                .asList(I18n.tl("guis.main.pvpLore"), I18n.tl("guis.main.pvpLore1")))
                .create();
        inventory.setItem(3, ic);
        ic = new ItemsCreator(Material.BARRIER, I18n.tl("guis.main.border"),
                Collections.singletonList(I18n.tl("guis.main.borderLore"))).create();
        inventory.setItem(5, ic);
        ic = new ItemsCreator(Material.OBSIDIAN, I18n.tl("guis.main.nether"), Collections.singletonList(I18n.tl("guis.main.netherLore"))).create();
        inventory.setItem(8, ic);

        inventory.setItem(18, Items.getItem(Material.CHEST, ChatColor.YELLOW + "Inventaire de départ", true));

        inventory.setItem(22, new ItemsCreator(Material.CARROT_ITEM, "§dVie dans le tab", Arrays.asList("", "§cDésactivé"), 1).create());
        if(pl.gameManager.isViewHealth())
            inventory.setItem(22, new ItemsCreator(Material.GOLDEN_CARROT, "§dVie dans le tab", Arrays.asList("", "§aActivé"), 1).create());
        inventory.setItem(26, Items.getItem(Material.ARROW, I18n.tl("guis.back"), false));
        ic = new ItemsCreator(Material.POTION, I18n.tl("guis.main.potions", ""), Collections.singletonList(I18n.tl("guis.main.potionsLore"))).create();
        inventory.setItem(24, ic);
    }

    @EventHandler
    public void onInteract(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        ItemStack current = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();
        GameManager gamemanager = this.pl.getGamemanager();
        if (gamemanager.getGameState() != GameState.LOBBY && gamemanager.getGameState() != GameState.STARTING)
            return;
        if (current == null)
            return;
        if (!current.hasItemMeta())
            return;
        if (inv.getName() == null)
            return;
        if (inv.getName().equalsIgnoreCase(ChatColor.AQUA + "Règles")) {
            e.setCancelled(true);
            if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Inventaire de départ")) {
                if (gamemanager.isEditInv()) {
                    player.sendMessage(ChatColor.RED + "L'inventaire est en cours d'édition !");
                    return;
                }
                player.closeInventory();
                gamemanager.setEditInv(true);
                player.sendMessage(ChatColor.AQUA + "/h enchant: Permet d'enchanter l'objet dans la main.");
                player.sendMessage(ChatColor.AQUA + "/h save: Sauvegarde l'inventaire.");
                player.setGameMode(GameMode.CREATIVE);
                CustomInv.restoreInventory(player);
            } else if (current.getItemMeta().getDisplayName().equalsIgnoreCase(I18n.tl("guis.main.border"))) {
                player.closeInventory();
                new BorderGui(player).show();
            } else if (current.getItemMeta().getDisplayName().equalsIgnoreCase(I18n.tl("guis.main.pvp"))) {
                player.closeInventory();
                new PvpTimeGui(player).show();
            } else if (current.getItemMeta().getDisplayName().equalsIgnoreCase(I18n.tl("guis.main.teams"))) {
                player.closeInventory();
                new ToGui(player).show();
            } else if(current.getItemMeta().getDisplayName().equalsIgnoreCase(I18n.tl("guis.main.nether"))) {
                player.closeInventory();
                new NetherGui(player).show();
            } else if(current.getItemMeta().getDisplayName().equalsIgnoreCase(I18n.tl("guis.back"))){
                player.closeInventory();
                new HostConfig(player).show();
            } else if(current.getType() == Material.GOLDEN_CARROT || current.getType() == Material.CARROT_ITEM){
                gamemanager.setViewHealth(!gamemanager.isViewHealth());
                player.closeInventory();
                new RulesGui(player).show();
            } else if(current.getType() == Material.POTION){
                player.closeInventory();
                new PotionsGui(player).show();
            }

        }
    }


    @EventHandler
    public void onClick(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory))
            HandlerList.unregisterAll(this);
    }


}
