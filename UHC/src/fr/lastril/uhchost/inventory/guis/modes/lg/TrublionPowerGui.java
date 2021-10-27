package fr.lastril.uhchost.inventory.guis.modes.lg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.scenario.gui.TimerGui;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class TrublionPowerGui extends TimerGui {
    public TrublionPowerGui(Player player) {
        super(player, I18n.tl("guis.lg.vote.name"));
        if(UhcHost.getInstance().gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager){
            LoupGarouManager loupGarouManager = (LoupGarouManager)
                    UhcHost.getInstance().gameManager.getModes().getMode().getModeManager();

            ItemsCreator ic = new ItemsCreator(Material.PAPER,
                    "§e" + loupGarouManager.getStartVoteEpisode(),
                    Arrays.asList(I18n.tl("guis.lg.vote.lore"), I18n.tl("guis.lg.vote.lore1")));
            inventory.setItem(4, ic.create());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;
        if (event.getClickedInventory().equals(inventory)) {
            String name;
            int value;
            ItemsCreator ic;
            ItemStack is = event.getCurrentItem();
            if (is == null || is.getType() == Material.AIR)
                return;
            event.setCancelled(true);
            switch (is.getType()) {
                case PAPER:
                    this.player.closeInventory();
                    if(UhcHost.getInstance().gameManager.getModes().getMode() instanceof ModeConfig){
                        ModeConfig modeConfig = (ModeConfig) UhcHost.getInstance().gameManager.getModes().getMode();
                        modeConfig.getGui(player).show();
                    }
                    break;
                case BANNER:
                    if(UhcHost.getInstance().gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager){
                        name = ChatColor.stripColor(is.getItemMeta().getDisplayName());
                        LoupGarouManager loupGarouManager = (LoupGarouManager)
                                UhcHost.getInstance().gameManager.getModes().getMode().getModeManager();
                        value = loupGarouManager.getStartVoteEpisode() + Integer.parseInt(name);
                        if (value < 0)
                            break;
                        loupGarouManager.setStartVoteEpisode(value);
                        ic = new ItemsCreator(Material.PAPER,
                                "§e" + loupGarouManager.getStartVoteEpisode(),
                                Arrays.asList(I18n.tl("guis.lg.vote.lore"), I18n.tl("guis.lg.vote.lore1")));
                        inventory.setItem(4, ic.create());
                    }
                    break;
                default:
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
