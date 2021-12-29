package fr.lastril.uhchost.modes.naruto.v2.gui.kamui;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.KamuiItem;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KamuiGUI extends IQuickInventory {

    private final UhcHost main;
    private final KamuiItem.KamuiUser kamuiUser;
    private final Player sender;
    private NarutoV2Manager narutoV2Manager;

    public KamuiGUI(UhcHost main, KamuiItem.KamuiUser kamuiUser, Player sender) {
        super("§6Kamui", 9*1);
        this.main = main;
        this.sender = sender;
        this.kamuiUser = kamuiUser;
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
        narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);

        inv.updateItem("kamui", taskUpdate -> {
            inv.setItem(new QuickItem(Material.ENDER_PEARL).setName("§6Arimasu")
                    .setLore("",
                            "§7Permet de§e vous§7 téléporter dans le monde",
                            "§7de Kamui pendant 5 minutes.",
                            "§7",
                            "§7Cooldown:§c " + new FormatTime(main.getPlayerManager(inv.getOwner().getUniqueId()).getRoleCooldownArisuma())
                    )
                    .toItemStack(), onClick -> {
                Player player = onClick.getPlayer();
                PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
                if (joueur.getRoleCooldownArisuma() == 0) {
                    joueur.setStunned(false);
                    Location initialLocation = player.getLocation();
                    kamuiUser.getInitialsLocation().put(player.getUniqueId(), initialLocation);
                    if (joueur.getRole() instanceof NarutoV2Role) {
                        NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                        narutoRole.usePower(joueur);
                    }
                    narutoV2Manager.sendToKamui(player, initialLocation, sender);
                    joueur.setRoleCooldownArisuma(kamuiUser.getArimasuCooldown());
                    inv.close(player);
                } else {
                    player.sendMessage(Messages.cooldown(joueur.getRoleCooldownArisuma()));
                    inv.close(player);
                }
            }, 3);

            inv.setItem(new QuickItem(Material.EYE_OF_ENDER).setName("§6Sonohoka")
                    .setLore("",
                            "§7Permet de téléporter§e un joueur§7 dans le monde",
                            "§7de Kamui pendant 5 minutes.",
                            "§7",
                            "§7Cooldown:§c " + new FormatTime(main.getPlayerManager(inv.getOwner().getUniqueId()).getRoleCooldownSonohoka())
                    )
                    .toItemStack(), onClick -> {
                Player player = onClick.getPlayer();
                PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
                if (joueur.getRoleCooldownSonohoka() == 0) {
                    new SonohokaGUI(main, player, kamuiUser, sender).open(player);
                } else {
                    player.sendMessage(Messages.cooldown(joueur.getRoleCooldownSonohoka()));
                    inv.close(player);
                }
            }, 5);
        });


    }
}
