package fr.lastril.uhchost.inventory.guis.modes.lg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.BannerCreator;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;

import java.util.Arrays;

public class RegionChatGui extends IQuickInventory {

    private BannerCreator bc;
    private ItemsCreator ic;
    public RegionChatGui() {
        super(I18n.tl("guis.lg.region.name"), 1*9);
    }

    @Override
    public void contents(QuickInventory inv) {


        inv.updateItem("update", taskUpdate -> {
            if(UhcHost.getInstance().gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager){

                LoupGarouManager loupGarouManager = (LoupGarouManager)
                        UhcHost.getInstance().gameManager.getModes().getMode().getModeManager();

                bc = new BannerCreator("§c-10", Arrays.asList(""), 1, true);
                bc.setBaseColor(DyeColor.RED);


                inv.setItem(bc.create(), onClick -> {
                    String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                    int value = loupGarouManager.getStartVoteEpisode() + Integer.parseInt(bannerName);
                    loupGarouManager.setStartVoteEpisode(value);

                }, 0);
                bc = new BannerCreator("§c-5", Arrays.asList(""), 1, true);
                bc.setBaseColor(DyeColor.RED);
                inv.setItem(bc.create(), onClick -> {
                    String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                    int value = loupGarouManager.getStartVoteEpisode() + Integer.parseInt(bannerName);
                    loupGarouManager.setStartVoteEpisode(value);

                }, 1);
                bc = new BannerCreator("§c-1", Arrays.asList(""), 1, true);
                bc.setBaseColor(DyeColor.RED);
                inv.setItem(bc.create(), onClick -> {
                    String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                    int value = loupGarouManager.getStartVoteEpisode() + Integer.parseInt(bannerName);
                    loupGarouManager.setStartVoteEpisode(value);
                }, 2);
                bc = new BannerCreator("§a+1", Arrays.asList(""), 1, true);
                bc.setBaseColor(DyeColor.GREEN);



                ic = new ItemsCreator(Material.PAPER,
                        "§e" + loupGarouManager.getStartVoteEpisode(),
                        Arrays.asList(I18n.tl("guis.lg.region.lore"), I18n.tl("guis.lg.region.lore1")));
                inv.setItem(ic.create(), onClick-> {
                    if(UhcHost.getInstance().gameManager.getModes().getMode() instanceof ModeConfig){
                        ModeConfig modeConfig = (ModeConfig) UhcHost.getInstance().gameManager.getModes().getMode();
                        modeConfig.getGui().open(onClick.getPlayer());
                    }
                },4);
                inv.setItem(bc.create(), onClick -> {
                    String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                    int value = loupGarouManager.getStartVoteEpisode() + Integer.parseInt(bannerName);
                    loupGarouManager.setStartVoteEpisode(value);
                }, 6);
                bc = new BannerCreator("§a+5", Arrays.asList(""), 1, true);
                bc.setBaseColor(DyeColor.GREEN);
                inv.setItem(bc.create(), onClick -> {
                    String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                    int value = loupGarouManager.getStartVoteEpisode() + Integer.parseInt(bannerName);
                    loupGarouManager.setStartVoteEpisode(value);
                }, 7);
                bc = new BannerCreator("§a+10", Arrays.asList(""), 1, true);
                bc.setBaseColor(DyeColor.GREEN);
                inv.setItem(bc.create(), onClick -> {
                    String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                    int value = loupGarouManager.getStartVoteEpisode() + Integer.parseInt(bannerName);
                    loupGarouManager.setStartVoteEpisode(value);
                }, 8);
            }

        });
    }
}
