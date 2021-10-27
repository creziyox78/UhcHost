package fr.lastril.uhchost.inventory.guis.modes.lg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.inventory.guis.modes.CompositionGui;
import fr.lastril.uhchost.inventory.guis.modes.ModesGui;
import fr.lastril.uhchost.modes.lg.LoupGarouMode;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public class LoupGarouGui extends Gui {

    private final LoupGarouMode lgMode;

    public LoupGarouGui(Player player, LoupGarouMode lgMode) {
        super(player, 9*2, I18n.tl("guis.lg.main.name"));
        this.lgMode = lgMode;
        inventory.setItem(1, new ItemsCreator(Material.BOOK, I18n.tl("guis.lg.main.composition"), null, 1).create());
        inventory.setItem(3, new ItemsCreator(Material.COMPASS, I18n.tl("guis.lg.main.episodetime", String.valueOf(UhcHost.getInstance().gameManager.episodeEvery / 60)), null, 1).create());
        inventory.setItem(5, new ItemsCreator(Material.YELLOW_FLOWER, I18n.tl("guis.lg.main.randomcouple"),
                Collections.singletonList(lgMode.getLoupGarouManager().isRandomCouple() ? "§aActivé" : "§cDésactivé"), 1).create());
        inventory.setItem(7, new ItemsCreator(Material.PAPER, I18n.tl("guis.lg.main.votetime", String.valueOf(lgMode.getLoupGarouManager().getStartVoteEpisode())), null, 1).create());
        inventory.setItem(inventory.getSize() - 1, (new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), Collections.singletonList(""))).create());
    }

    @EventHandler
    public void onClick(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory))
            HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;
        if (event.getClickedInventory().equals(inventory)) {
            ItemStack is = event.getCurrentItem();
            if (is == null || is.getType() == Material.AIR)
                return;
            event.setCancelled(true);
            switch (is.getType()){
                case BOOK:
                    player.closeInventory();
                    new CompositionGui(player).show();
                    break;
                case YELLOW_FLOWER:
                    lgMode.getLoupGarouManager().setRandomCouple(!lgMode.getLoupGarouManager().isRandomCouple());
                    new LoupGarouGui(player, lgMode).show();
                    break;
                case COMPASS:
                    player.closeInventory();
                    new TimerPerEpisodeGui(player).show();
                    break;
                case EMPTY_MAP:
                    player.closeInventory();
                    new RegionChatGui(player).show();
                    break;
                case PAPER:
                    player.closeInventory();
                    new VoteStartGui(player).show();
                    break;
                case DIAMOND_SWORD:
                    player.closeInventory();
                    new TimeLoupListGui(player).show();
                    break;
                case BARRIER:
                    player.closeInventory();
                    new ModesGui(player).show();
                    break;
            }
        }

    }


}
