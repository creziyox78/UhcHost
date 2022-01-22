package fr.lastril.uhchost.inventory.guis.modes.lg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.inventory.guis.modes.CompositionGui;
import fr.lastril.uhchost.modes.lg.LoupGarouMode;
import fr.lastril.uhchost.modes.lg.roles.village.Pretresse;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.SkullType;

import java.util.Collections;

public class LoupGarouGui extends IQuickInventory {

    private final LoupGarouMode lgMode;

    public LoupGarouGui(LoupGarouMode lgMode) {
        super(I18n.tl("guis.lg.main.name"), 9*3);
        this.lgMode = lgMode;

    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("lgMode", taskUpdate -> {
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

            inv.setItem(new ItemsCreator(Material.EMPTY_MAP, I18n.tl("guis.lg.main.lglisttime", new FormatTime(lgMode.getLoupGarouManager().getSendWerewolfListTime()).toFormatString()),
                    null, 1).create(), onClick-> {
                new SendLGListGui().open(onClick.getPlayer());
            }, 10);

            inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setName(I18n.tl("guis.lg.main.specialsevents"))
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTAzNWM1MjgwMzZiMzg0YzUzYzljOGExYTEyNTY4NWUxNmJmYjM2OWMxOTdjYzlmMDNkZmEzYjgzNWIxYWE1NSJ9fX0=")
                    .toItemStack(), onClick -> {
                new SpecialsEventsGui(lgMode).open(onClick.getPlayer());
            },12);

            inv.setItem(new ItemsCreator(Material.PAPER, I18n.tl("guis.lg.main.compositionhide"), Collections.singletonList(lgMode.getModeManager().compositionHide ? "§aActivée" : "§cDésactivée"), 1).create(), onClick -> {
                lgMode.getModeManager().compositionHide = !lgMode.getModeManager().compositionHide;
            },14);

            inv.setItem(new ItemsCreator(Material.PAPER, I18n.tl("guis.lg.main.masquedrole"), Collections.singletonList(lgMode.getLoupGarouManager().isRandomSeeRole() ? "§aActivée" : "§cDésactivée"), 1).create(), onClick -> {
                if(UhcHost.getInstance().gameManager.getComposition().contains(Pretresse.class)){
                    lgMode.getLoupGarouManager().setRandomSeeRole(true);
                    onClick.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cLa Prêtresse fait partie de la composition. Retirez ce rôle afin de modifier ce paramètre.");
                } else {
                    lgMode.getLoupGarouManager().setRandomSeeRole(!lgMode.getLoupGarouManager().isRandomSeeRole());
                }

            },16);

            inv.setItem(new QuickItem(Material.WATCH).setName(I18n.tl("guis.lg.main.rolestime"))
                    .setLore("§b" + new FormatTime(lgMode.getRoleAnnouncement()).toFormatString())
                    .toItemStack(), onClick -> {
                new RolesTimeGui(lgMode).open(onClick.getPlayer());
            }, 19);

            inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setName(I18n.tl("guis.lg.main.solitaire"))
                            .setLore(lgMode.isLgSolitaire() ? "§aActivé" : "§cDésactivé")
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzBmZGYyZTg4ODM5OTNiYWE4Njc1M2Y3ZTdiMTMxM2NkMmE3NjljN2VjN2JhYTY4Mjc5NDIyNzdjYjdiYWJjMCJ9fX0=")
                    .toItemStack(), onClick -> {
                lgMode.setLgSolitaire(!lgMode.isLgSolitaire());
            },21);

            inv.setItem((new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), null)).create(), onClick -> {
                new HostConfig().open(onClick.getPlayer());
            },inv.getInventory().getSize() - 1);
        });

    }
}
