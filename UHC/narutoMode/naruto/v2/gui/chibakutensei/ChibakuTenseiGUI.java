package fr.lastril.uhchost.modes.naruto.v2.gui.chibakutensei;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.HeadTextures;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v1.roles.gentils.*;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.generation.MeteoresGenerator;
import fr.maygo.uhc.modes.naruto.v2.items.ChibakuTenseiItem;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.Konan;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.Nagato;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ChibakuTenseiGUI extends IQuickInventory {

    private final UhcHost main;
    private final ChibakuTenseiItem.ChibakuTenseiUser chibakuTenseiUser;

    public ChibakuTenseiGUI(UhcHost main, ChibakuTenseiItem.ChibakuTenseiUser chibakuTenseiUser) {
        super(ChatColor.RED + "Chibaku Tensei", 1 * 9);
        this.main = main;
        this.chibakuTenseiUser = chibakuTenseiUser;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);

        inv.setItem(new QuickItem(Material.ENDER_PEARL).setName("§cBanshô Ten’in")
                .setLore("",
                        "§7Permet de téléporter un PlayerManager",
                        "§7sur votre position.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());
            if (PlayerManager.getRoleCooldownBanshoTenin() == 0) {
                List<Player> players = new ArrayList<>();

                for (Entity entity : playerClick.getNearbyEntities(Konan.getChissokuDistance(), Konan.getChissokuDistance(), Konan.getChissokuDistance())) {
                    if (entity instanceof Player) {
                        Player playersNearby = (Player) entity;
                        if (playersNearby.getGameMode() != GameMode.SPECTATOR) {
                            players.add(playersNearby);
                        }
                    }
                }

                new BanshoTeninGUI(main, players, chibakuTenseiUser).open(playerClick);
            } else {
                playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownBanshoTenin()));
                inv.close(playerClick);
            }
        }, 2);

        inv.setItem(new QuickItem(Material.EYE_OF_ENDER).setName("§cShinra Tensei")
                .setLore("",
                        "§7Permet de repousser les PlayerManagers",
                        "§7de votre position.").toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());
            if (PlayerManager.getRoleCooldownShinraTensei() == 0) {

                for (Entity entity : playerClick.getNearbyEntities(Nagato.getVelocityDistance(), Nagato.getVelocityDistance(), Nagato.getVelocityDistance())) {
                    Vector fromPlayerToTarget = entity.getLocation().toVector().clone().subtract(playerClick.getLocation().toVector());
                    entity.setVelocity(new Vector(0, 0.3, 0));
                    fromPlayerToTarget.multiply(6);
                    fromPlayerToTarget.setY(2);
                    entity.setVelocity(fromPlayerToTarget.normalize());
                }
                if (PlayerManager.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                    narutoRole.usePower(PlayerManager);
                }
                PlayerManager.setRoleCooldownShinraTensei(5 * 60);
                main.getSoundUtils().playSoundDistanceVolume(playerClick.getLocation(), "atlantis.shinratensei", 10);
                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
            } else {
                playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownShinraTensei()));
            }
            inv.close(playerClick);
        }, 4);

        if (chibakuTenseiUser.hasTengaiShinsei() && (!chibakuTenseiUser.hasUsedTengaiShinsei())) {
            inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(HeadTextures.METEORE.getHash()).setName("§cTengai Shinsei")
                    .setLore("",
                            "§7Faites apparaître une météorite qui",
                            "§7s'écrase au bout de 10 secondes.").toItemStack(), onClick -> {
                Player playerClick = onClick.getPlayer();
                PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());
                main.getSoundUtils().playSoundDistanceVolume(playerClick.getLocation(), "atlantis.tengaishinsei", 10);
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (players.getWorld() == playerClick.getWorld()) {
                        if (players.getLocation().distance(playerClick.getLocation()) <= 50) {
                            players.playSound(players.getLocation(), Sound.WITHER_SPAWN, 1f, 1f);
                        }
                    }
                }

                if (PlayerManager.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                    narutoRole.usePower(PlayerManager);
                }
                chibakuTenseiUser.useTengaiShinsei();

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        MeteoresGenerator.spawnMeteore(playerClick.getLocation(), 5, 100);
                    }
                }.runTaskLater(main, 20 * 6);

                inv.close(playerClick);
            }, 6);
        }
    }
}
