package fr.lastril.uhchost.modes.naruto.v2.gui.shikigaminomai;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.Title;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.Konan;
import fr.maygo.uhc.obj.PlayerManager;
import fr.maygo.uhc.task.ChunkLoader;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ChissokuGUI extends IQuickInventory {

    private final UhcHost main;
    private final Konan konan;
    private final List<Player> players;

    public ChissokuGUI(UhcHost main, Konan konan, List<Player> players) {
        super("§cChissoku", 9 * 6);
        this.main = main;
        this.konan = konan;
        this.players = players;
    }

    @Override
    public void contents(QuickInventory inv) {

        for (Player player : players) {
            inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(player.getName()).setSkullOwner(player.getName())
                    .setLore("",
                            "§7Cliquez pour immobiliser ce PlayerManager.")
                    .toItemStack(), onClick -> {
                Player konanPlayer = onClick.getPlayer();
                PlayerManager PlayerManager = main.getPlayerManager(konanPlayer.getUniqueId()), targetPlayerManager = main.getPlayerManager(player.getUniqueId());

                PlayerManager.setRoleCooldownChissoku(20 * 60);

                targetPlayerManager.stun(player.getLocation());
                konanPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Konan vous a immobilisé avec Chissoku.");
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, Konan.getChissokuStunTime() * 20, 0, false, false));

                if (PlayerManager.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                    narutoRole.usePower(PlayerManager);
                }
                inv.close(player);

                new BukkitRunnable() {

                    int timer = Konan.getChissokuStunTime() * 20;

                    @Override
                    public void run() {
                        ActionBar.sendMessage(konanPlayer, "§7Chissoku : [" + ChunkLoader.getProgressBar(timer, Konan.getChissokuStunTime() * 20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                        if (timer == 0) {
                            targetPlayerManager.setStunned(false);
                            cancel();
                        }
                        timer--;
                    }
                }.runTaskTimer(main, 0, 1);
            });
        }
    }
}
