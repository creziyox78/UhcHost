package fr.lastril.uhchost.modes.naruto.v2.gui.shikigaminomai;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Konan;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.tasks.ChunkLoader;
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
        super("§cChissoku", 9*6);
        this.main = main;
        this.konan = konan;
        this.players = players;
    }

    @Override
    public void contents(QuickInventory inv) {

        for (Player player : players) {
            inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(player.getName()).setSkullOwner(player.getName())
                    .setLore("",
                            "§7Cliquez pour immobiliser ce joueur.")
                    .toItemStack(), onClick -> {
                Player konanPlayer = onClick.getPlayer();
                PlayerManager joueur = main.getPlayerManager(konanPlayer.getUniqueId()), targetJoueur = main.getPlayerManager(player.getUniqueId());

                joueur.setRoleCooldownChissoku(20*60);

                targetJoueur.stun(player.getLocation());
                konanPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage()+Messages.USED_POWER.getMessage());
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Konan vous a immobilisé avec Chissoku.");
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, Konan.getChissokuStunTime()*20, 0, false, false));

                if(joueur.getRole() instanceof NarutoV2Role){
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }
                inv.close(player);

                new BukkitRunnable() {

                    int timer = Konan.getChissokuStunTime()*20;

                    @Override
                    public void run() {
                        ActionBar.sendMessage(konanPlayer, "§7Chissoku : [" + ChunkLoader.getProgressBar(timer, Konan.getChissokuStunTime()*20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                        if(timer == 0){
                            targetJoueur.setStunned(false);
                            cancel();
                        }
                        timer--;
                    }
                }.runTaskTimer(main, 0, 1);
            });
        }
    }
}
