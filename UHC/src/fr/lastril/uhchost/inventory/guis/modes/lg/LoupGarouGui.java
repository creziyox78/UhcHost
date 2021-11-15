package fr.lastril.uhchost.inventory.guis.modes.lg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.guis.modes.CompositionGui;
import fr.lastril.uhchost.modes.lg.LoupGarouMode;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;

import java.util.Collections;

public class LoupGarouGui extends IQuickInventory {

    private final LoupGarouMode lgMode;

    public LoupGarouGui(LoupGarouMode lgMode) {
        super(I18n.tl("guis.lg.main.name"), 9*2);
        this.lgMode = lgMode;

    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setItem(new ItemsCreator(Material.BOOK, I18n.tl("guis.lg.main.composition"), null, 1).create(), onClick -> {
            new CompositionGui().open(onClick.getPlayer());
        },1);
        inv.setItem(new ItemsCreator(Material.COMPASS, I18n.tl("guis.lg.main.episodetime", String.valueOf(UhcHost.getInstance().gameManager.episodeEvery / 60)), null, 1).create(), onClick ->{
            new TimerPerEpisodeGui().open(onClick.getPlayer());
        },3);
        inv.setItem(new ItemsCreator(Material.YELLOW_FLOWER, I18n.tl("guis.lg.main.randomcouple"),
                        Collections.singletonList(lgMode.getLoupGarouManager().isRandomCouple() ? "§aActivé" : "§cDésactivé"), 1).create(), onClick -> {
            lgMode.getLoupGarouManager().setRandomCouple(!lgMode.getLoupGarouManager().isRandomCouple());
        },5);
        inv.setItem(new ItemsCreator(Material.PAPER, I18n.tl("guis.lg.main.votetime", String.valueOf(lgMode.getLoupGarouManager().getStartVoteEpisode())), null, 1).create(), onClick -> {
            new VoteStartGui().open(onClick.getPlayer());
        },7);
        inv.setItem((new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), Collections.singletonList(""))).create(), inv.getInventory().getSize() - 1);
    }
}
