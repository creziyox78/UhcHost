package fr.lastril.uhchost.modes.naruto.v2.gui.kamui;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.FormatTime;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.KamuiItem;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KamuiGUI extends IQuickInventory {

    private final UhcHost main;
    private final KamuiItem.KamuiUser kamuiUser;
    private final Player sender;

    public KamuiGUI(UhcHost main, KamuiItem.KamuiUser kamuiUser, Player sender) {
        super("§6Kamui", 9 * 1);
        this.main = main;
        this.sender = sender;
        this.kamuiUser = kamuiUser;

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
                PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
                if (PlayerManager.getRoleCooldownArisuma() == 0) {
                    PlayerManager.setStunned(false);
                    Location initialLocation = player.getLocation();
                    kamuiUser.getInitialsLocation().put(player.getUniqueId(), initialLocation);
                    if (PlayerManager.getRole() instanceof NarutoV2Role) {
                        NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                        narutoRole.usePower(PlayerManager);
                    }
                    main.getNarutoV2Manager().sendToKamui(player, initialLocation, sender);
                    main.getSoundUtils().playSoundDistance(player.getLocation(), 3, "atlantis.kamui");
                    PlayerManager.setRoleCooldownArisuma(kamuiUser.getArimasuCooldown());
                    inv.close(player);
                } else {
                    player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownArisuma()));
                    inv.close(player);
                }
            }, 3);

            inv.setItem(new QuickItem(Material.EYE_OF_ENDER).setName("§6Sonohoka")
                    .setLore("",
                            "§7Permet de téléporter§e un PlayerManager§7 dans le monde",
                            "§7de Kamui pendant 5 minutes.",
                            "§7",
                            "§7Cooldown:§c " + new FormatTime(main.getPlayerManager(inv.getOwner().getUniqueId()).getRoleCooldownSonohoka())
                    )
                    .toItemStack(), onClick -> {
                Player player = onClick.getPlayer();
                PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
                if (PlayerManager.getRoleCooldownSonohoka() == 0) {
                    new SonohokaGUI(main, player, kamuiUser, sender).open(player);
                } else {
                    player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownSonohoka()));
                    inv.close(player);
                }
            }, 5);
        });


    }
}
