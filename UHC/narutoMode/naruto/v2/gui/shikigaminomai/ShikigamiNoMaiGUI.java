package fr.lastril.uhchost.modes.naruto.v2.gui.shikigaminomai;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.Title;
;
import fr.maygo.uhc.api.particles.WingsEffect;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.swords.YariSword;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.Konan;
import fr.maygo.uhc.obj.PlayerManager;
import fr.maygo.uhc.task.ChunkLoader;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ShikigamiNoMaiGUI extends IQuickInventory {

    private final UhcHost main;
    private final Konan konan;

    public ShikigamiNoMaiGUI(UhcHost main, Konan konan) {
        super("§cShikigami no Mai", 9 * 1);
        this.main = main;
        this.konan = konan;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);

        inv.setItem(new QuickItem(Material.DIAMOND_SWORD).setName("§cYari")
                .setLore("",
                        "§7Permet de recevoir une épée§c Tranchant 7§7",
                        "§7avec§f 1 utilisation possible§7.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());

            if (PlayerManager.getRoleCooldownYari() <= 0) {
                main.getInventoryUtils().giveItemSafely(playerClick, new YariSword().toItemStack());
                if (PlayerManager.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                    narutoRole.usePower(PlayerManager);
                }
                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez reçu votre Yari !");
                PlayerManager.setRoleCooldownYari(5 * 60);
            } else {
                playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownYari()));
            }
            inv.close(playerClick);
        }, 1);

        inv.setItem(new QuickItem(Material.EYE_OF_ENDER).setName("§cBatafurai")
                .setLore("",
                        "§7Permet de vous rendre en§c mode spectateur§7 dans un rayon",
                        "§7de 60 blocs de votre position initiale pendant 45 secondes.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());

            if (PlayerManager.getRoleCooldownBatafurai() <= 0) {
                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous êtes désormais en mode spectateur !");
                PlayerManager.setRoleCooldownBatafurai(10 * 60);
                if (PlayerManager.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                    narutoRole.usePower(PlayerManager);
                }
                playerClick.setGameMode(GameMode.SPECTATOR);
                Location initialLocation = playerClick.getLocation();

                for (PlayerManager PlayerManagers : main.getPlayerManagersAlives()) {
                    if (PlayerManagers.getPlayer() != null) PlayerManagers.getPlayer().hidePlayer(playerClick);
                }

                main.getInvisibleTeam().addEntry(playerClick.getName());

                new BukkitRunnable() {

                    int timer = Konan.getSpectatorTime() * 20;

                    @Override
                    public void run() {
                        ActionBar.sendMessage(playerClick, "§7Batafurai : [" + ChunkLoader.getProgressBar(timer, Konan.getSpectatorTime() * 20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                        if (playerClick.getLocation().distance(initialLocation) > 60)
                            playerClick.teleport(initialLocation);

                        if (timer == 0) {

                            main.getInvisibleTeam().removeEntry(playerClick.getName());
                            for (PlayerManager PlayerManagers : main.getPlayerManagersAlives()) {
                                if (PlayerManagers.getPlayer() != null) PlayerManagers.getPlayer().showPlayer(playerClick);
                            }

                            playerClick.setGameMode(GameMode.SURVIVAL);
                            playerClick.teleport(initialLocation);
                            playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous êtes de nouveau visible !");

                            cancel();
                        }
                        timer--;
                    }
                }.runTaskTimer(main, 0, 1);
            } else {
                playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownBatafurai()));
            }
            inv.close(playerClick);
        }, 3);

        inv.setItem(new QuickItem(Material.FEATHER).setName("§cKami Toku")
                .setLore("",
                        "§7Permet de vous envoler pendant§f 8 secondes§7.",
                        "§7Des ailes de§6 feu§7 émaneront de vous.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());

            if (PlayerManager.getRoleCooldownKamiToku() <= 0) {
                playerClick.setAllowFlight(true);
                playerClick.setVelocity(playerClick.getVelocity().add(new Vector(0, 2, 0)));
                playerClick.setFlying(true);
                playerClick.setFlySpeed((float) 0.1);
                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous pouvez désormais voler !");
                PlayerManager.setRoleCooldownKamiToku(10 * 60);

                if (PlayerManager.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                    narutoRole.usePower(PlayerManager);
                }

                new WingsEffect(Konan.getFlyTime() * 20, EnumParticle.FLAME).start(playerClick);

                new BukkitRunnable() {

                    int timer = Konan.getFlyTime() * 20;

                    @Override
                    public void run() {
                        if (PlayerManager.isAlive() || playerClick.getGameMode() != GameMode.SPECTATOR) {
                            ActionBar.sendMessage(playerClick, "§7Kami Toku : [" + ChunkLoader.getProgressBar(timer, Konan.getFlyTime() * 20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                            if (timer == 0) {
                                playerClick.setFlying(false);
                                playerClick.setAllowFlight(false);
                                cancel();
                            }
                            timer--;
                        } else {
                            cancel();
                        }
                    }
                }.runTaskTimer(main, 0, 1);
            } else {
                playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownKamiToku()));
            }
            inv.close(playerClick);
        }, 5);

        inv.setItem(new QuickItem(Material.FERMENTED_SPIDER_EYE).setName("§cChissoku")
                .setLore("",
                        "§7Permet d'immobiliser un PlayerManager pendant",
                        "§7§f5 secondes§7. Ce dernier aura§a Poison 1§7 en plus.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());

            if (PlayerManager.getRoleCooldownChissoku() <= 0) {
                List<Player> players = new ArrayList<>();

                for (Entity entity : playerClick.getNearbyEntities(Konan.getChissokuDistance(), Konan.getChissokuDistance(), Konan.getChissokuDistance())) {
                    if (entity instanceof Player) {
                        Player playersNearby = (Player) entity;
                        if (playersNearby.getGameMode() != GameMode.SPECTATOR) {
                            players.add(playersNearby);
                        }
                    }
                }

                new ChissokuGUI(main, konan, players).open(playerClick);
            } else {
                playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownChissoku()));
                inv.close(playerClick);
            }
        }, 7);
    }
}
