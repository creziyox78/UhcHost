package fr.lastril.uhchost.modes.naruto.v2.gui.genjutsu;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.GenjutsuItem;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.KillerBee;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GenjutsuGUI extends IQuickInventory {

    public static final int DISTANCE_TSUKUYOMI = 20;
    public static final int DISTANCE_ATTAQUE = 20;
    public static final int DISTANCE_IZANAMI = 20;

    private final UhcHost main;
    private final GenjutsuItem.GenjutsuUser genjutsuUser;

    public GenjutsuGUI(UhcHost main, GenjutsuItem.GenjutsuUser genjutsuUser) {
        super("§cGenjutsu", 9 * 1);
        this.main = main;
        this.genjutsuUser = genjutsuUser;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);


        inv.setItem(new QuickItem(Material.EYE_OF_ENDER).setName("§cTsukuyomi")
                .setLore("",
                        "§7Permet d'immobiliser tous les PlayerManagers",
                        "§7dans un rayon de 20 blocs. Ces derniers seront invinsibles",
                        "§7pendant 8 secondes.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());
            if (genjutsuUser.getTsukuyomiUses() < 2) {
                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                List<Player> players = new ArrayList<>();

                for (Entity entity : inv.getOwner().getNearbyEntities(DISTANCE_TSUKUYOMI, DISTANCE_TSUKUYOMI, DISTANCE_TSUKUYOMI)) {
                    if (entity instanceof Player) {
                        Player target = (Player) entity;
                        PlayerManager targetPlayerManager = main.getPlayerManager(target.getUniqueId());
                        if (target.getGameMode() != GameMode.SPECTATOR && targetPlayerManager.isAlive()) {
                            if (targetPlayerManager.hasRole()) {
                                if (targetPlayerManager.getRole() instanceof KillerBee) {
                                    target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous êtes immunisé de Tsukuyomi. Vous pouvez toujours bouger.");
                                    continue;
                                }
                            }
                            players.add(target);

                            targetPlayerManager.stun(target.getLocation());
                            main.shuffled.add(target);
                            target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous venez d'être immobilisé avec Tsukuyomi.");
                        }
                    }
                }
                if (PlayerManager.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                    narutoRole.usePower(PlayerManager);
                }

                genjutsuUser.useTsukuyomi();
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        for (Player player : players) {
                            PlayerManager targetPlayerManager = main.getPlayerManager(player.getUniqueId());
                            targetPlayerManager.setStunned(false);
                            main.shuffled.remove(player);
                        }
                    }
                }.runTaskLater(main, 20 * 8);
            } else {
                playerClick.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                return;
            }
        }, 2);


        inv.setItem(new QuickItem(Material.ENDER_PEARL).setName("§cAttaque")
                .setLore("",
                        "§7Permet de vous téléporter",
                        "§7dans le dos d'un PlayerManager.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());
            if (PlayerManager.getRoleCooldownAttaque() <= 0) {
                List<Player> players = new ArrayList<>();

                for (Entity entity : inv.getOwner().getNearbyEntities(DISTANCE_ATTAQUE, DISTANCE_ATTAQUE, DISTANCE_ATTAQUE)) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        if (player.getGameMode() != GameMode.SPECTATOR) {
                            players.add(player);
                        }
                    }
                }

                new AttaqueGUI(main, players).open(playerClick);
            } else {
                playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownAttaque()));
            }
        }, 4);


        inv.setItem(new QuickItem(Material.FERMENTED_SPIDER_EYE).setName("§aIzanami")
                .setLore("",
                        "§7Permet de cibler un PlayerManager qui,",
                        "§7s'il le souhaite, rejoindra votre camp.",
                        "Sinon, il recevra un gros malus.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());

            if (!genjutsuUser.hasUsedIzanami()) {
                List<Player> players = new ArrayList<>();

                for (Entity entity : inv.getOwner().getNearbyEntities(DISTANCE_ATTAQUE, DISTANCE_ATTAQUE, DISTANCE_ATTAQUE)) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        if (player.getGameMode() != GameMode.SPECTATOR) {
                            players.add(player);
                        }
                    }
                }
                new IzanamiGUI(main, genjutsuUser, players).open(playerClick);
            } else {
                playerClick.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                return;
            }
        }, 6);

    }
}
