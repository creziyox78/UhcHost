package fr.lastril.uhchost.inventory.guis.timer;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.inventory.CustomInv;
import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.inventory.guis.items.PotionsGui;
import fr.lastril.uhchost.inventory.guis.loots.LootsGui;
import fr.lastril.uhchost.inventory.guis.nether.NetherGui;
import fr.lastril.uhchost.inventory.guis.world.border.BorderGui;
import fr.lastril.uhchost.team.ToGui;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public class RulesGuiHost extends IQuickInventory {

    private final UhcHost pl = UhcHost.getInstance();

    public RulesGuiHost() {
        super(ChatColor.GOLD + "Règles UHC", 9*3);
    }


    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("update", taskUpdate -> {
            GameManager gamemanager = this.pl.getGamemanager();
            ItemStack ic = new ItemsCreator(Material.BLAZE_ROD, I18n.tl("guis.main.teams"),
                    Collections.singletonList(I18n.tl("guis.main.teamsLore"))).create();
            inv.setItem(ic, onClick ->  {
                new ToGui().open(onClick.getPlayer());
            },0);
            ic = new ItemsCreator(Material.DIAMOND_SWORD, I18n.tl("guis.main.pvp"), Arrays
                    .asList(I18n.tl("guis.main.pvpLore"), I18n.tl("guis.main.pvpLore1")))
                    .create();
            inv.setItem(ic, onClick -> {
                new PvpTimeGui().open(onClick.getPlayer());
            },2);
            ic = new ItemsCreator(Material.BEDROCK, I18n.tl("guis.main.border"),
                    Collections.singletonList(I18n.tl("guis.main.borderLore"))).create();
            inv.setItem(ic, onClick -> {
                new BorderGui().open(onClick.getPlayer());
            },4);
            ic = new ItemsCreator(Material.OBSIDIAN, I18n.tl("guis.main.nether"), Collections.singletonList(I18n.tl("guis.main.netherLore"))).create();
            inv.setItem(ic, onClick -> {
                new NetherGui().open(onClick.getPlayer());
            },6);

            ic = new ItemsCreator(Material.APPLE, I18n.tl("guis.main.loots"), Collections.singletonList(I18n.tl("guis.main.lootsLore"))).create();
            inv.setItem(ic, onClick -> {
                new LootsGui().open(onClick.getPlayer());
            },8);

            inv.setItem(new QuickItem(Material.CHEST).setName(ChatColor.YELLOW + "Inventaire de départ").toItemStack(), onClick -> {
                if (gamemanager.isEditInv()) {
                    onClick.getPlayer().sendMessage(ChatColor.RED + "L'inventaire est en cours d'édition !");
                    return;
                }
                onClick.getPlayer().closeInventory();
                gamemanager.setEditInv(true);
                onClick.getPlayer().sendMessage(ChatColor.AQUA + "/h enchant: Permet d'enchanter l'objet dans la main.");
                onClick.getPlayer().sendMessage(ChatColor.AQUA + "/h save: Sauvegarde l'inventaire.");
                onClick.getPlayer().setGameMode(GameMode.CREATIVE);
                CustomInv.restoreInventory(onClick.getPlayer());
            },18);


            ic = new ItemsCreator(Material.WATCH, I18n.tl("guis.main.cycle"), Collections.singletonList(I18n.tl("guis.main.cycleLore"))).create();

            inv.setItem(ic, onClick -> {
                new CycleTimeGui().open(onClick.getPlayer());
            },20);

            ic = new ItemsCreator(Material.CARROT_ITEM, "§dVie dans le tab", Arrays.asList("", "§cDésactivé"), 1).create();

            if(pl.gameManager.isViewHealth())
                ic = new ItemsCreator(Material.GOLDEN_CARROT, I18n.tl("guis.main.healthtab"), Arrays.asList("", "§aActivé"), 1).create();
            inv.setItem(ic, onClick -> {
                gamemanager.setViewHealth(!gamemanager.isViewHealth());
            },22);

            ic = new QuickItem(Material.BARRIER).setName(I18n.tl("guis.back")).toItemStack();

            inv.setItem(ic, onClick -> {
                new HostConfig().open(onClick.getPlayer());
            },26);
            ic = new ItemsCreator(Material.POTION, I18n.tl("guis.main.potions", ""), Collections.singletonList(I18n.tl("guis.main.potionsLore"))).create();
            inv.setItem(ic, onClick -> {
                new PotionsGui(onClick.getPlayer()).open(onClick.getPlayer());
            },24);
        });

    }
}
