package fr.lastril.uhchost.modes.naruto.v2.gui.sai;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.Title;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Sai;
import fr.maygo.uhc.obj.PlayerManager;
import fr.maygo.uhc.task.ChunkLoader;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class FuinjutsuGUI extends IQuickInventory {

    private final UhcHost main;
    private final List<Player> players;

    public FuinjutsuGUI(UhcHost main, List<Player> players) {
        super(ChatColor.GOLD + "Fûinjutsu", 9 * 6);
        this.main = main;
        this.players = players;
    }

    @Override
    public void contents(QuickInventory inv) {

        for (Player player : players) {
            inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(player.getName()).setSkullOwner(player.getName()).toItemStack(), onClick -> {
                Player playerClick = onClick.getPlayer();
                if (playerClick.getLocation().distance(player.getLocation()) > ToileGUI.DISTANCE_FUINJUTSU) {
                    playerClick.sendMessage(Messages.error(player.getName() + " n'est pas à " + ToileGUI.DISTANCE_FUINJUTSU + " blocs de vous !"));
                    inv.close(playerClick);
                    return;
                }
                playerClick.closeInventory();
                PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());
                PlayerManager.stun(playerClick.getLocation());

                Sai sai = (Sai) PlayerManager.getRole();
                sai.setHasUsedFuinjutsu(true);

                inv.close(playerClick);
                if (PlayerManager.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                    narutoRole.usePower(PlayerManager);
                }
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Saï est en train de vous sceller. Pour l'en empêcher, vous avez une minute pour l'éliminer !");
                new BukkitRunnable() {

                    int timer = 60 * 20;

                    @Override
                    public void run() {
                        ActionBar.sendMessage(playerClick, "§7Dessin : [" + ChunkLoader.getProgressBar(timer, 60 * 20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                        if (timer == 0) {
                            if (PlayerManager.isAlive()) {
                                //IL EST ENCORE EN VIE
                                sai.setScelled(player.getUniqueId());
                                PlayerManager.setStunned(false);
                                PlayerManager targetPlayerManager = main.getPlayerManager(player.getUniqueId());
                                targetPlayerManager.setAlive(false);
                                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous avez réussi à scellé " + player.getName());
                                player.setGameMode(GameMode.SPECTATOR);
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cSaï vous a scellé. S'il vient à mourir vous en serez libéré sur sa position.");
                            }

                            cancel();
                        }
                        timer--;
                    }
                }.runTaskTimer(main, 0, 1);
            });
        }
    }
}
