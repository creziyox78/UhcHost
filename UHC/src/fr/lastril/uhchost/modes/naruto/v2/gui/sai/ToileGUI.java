package fr.lastril.uhchost.modes.naruto.v2.gui.sai;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Sai;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.tasks.ChunkLoader;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ToileGUI extends IQuickInventory {

    private final UhcHost main;
    private final Sai sai;

    public static final double DISTANCE_FUINJUTSU = 20D;

    public ToileGUI(UhcHost main, Sai sai) {
        super(ChatColor.GOLD + "Toile", 9 * 1);
        this.main = main;
        this.sai = sai;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);

        inv.setItem(new QuickItem(Material.SADDLE).setName("§6Monture")
                .setLore("",
                        "§7Permet d'invoquer un cheval.")
                .toItemStack(), onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.getRoleCooldownSaiMonture() == 0) {
                joueur.stun(player.getLocation());
                inv.close(player);
                if (joueur.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }
                new BukkitRunnable() {

                    int timer = 5*20;

                    @Override
                    public void run() {
                        ActionBar.sendMessage(player, "§7Dessin : [" + ChunkLoader.getProgressBar(timer, 5*20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                        if(timer == 0){
                            joueur.setStunned(false);
                            Horse horse = player.getWorld().spawn(player.getLocation(), Horse.class);
                            horse.setOwner(onClick.getPlayer());
                            horse.setAdult();
                            horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                            horse.setTamed(true);

                            sai.setHorseID(horse.getEntityId());

                            joueur.setRoleCooldownSaiMonture(10 * 60);
                            cancel();
                        }
                        timer--;
                    }
                }.runTaskTimer(main, 0, 1);
            } else {
                player.sendMessage(Messages.cooldown(joueur.getRoleCooldownSaiMonture()));
                inv.close(player);
            }
        }, 2);

        inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                .setName("§6Tigres")
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA2MzEwYTg5NTJiMjY1YzZlNmJlZDQzNDgyMzlkZGVhOGU1NDgyYzhjNjhiZTZmZmY5ODFiYTgwNTZiZjJlIn19fQ==")
                .setLore("",
                        "§7Permet d'invoquer des poissons d'argents",
                        "§7qui attaque le joueur le plus proche.")
                .toItemStack(), onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.getRoleCooldownSaiTigres() == 0) {
                joueur.stun(player.getLocation());
                inv.close(player);
                if (joueur.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }
                new BukkitRunnable() {

                    int timer = 3*20;

                    @Override
                    public void run() {
                        ActionBar.sendMessage(player, "§7Dessin : [" + ChunkLoader.getProgressBar(timer, 3*20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                        if(timer == 0){
                            joueur.setStunned(false);
                            Player nearestPlayer = null;
                            double nearestDistance = Integer.MAX_VALUE;
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                if (players.getGameMode() != GameMode.SPECTATOR && players != player) {
                                    if(players.getWorld() == player.getWorld()){
                                        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
                                        if (joueur.isAlive()) {
                                            if (player.getLocation().distance(players.getLocation()) < nearestDistance) {
                                                nearestPlayer = players;
                                                nearestDistance = player.getLocation().distance(players.getLocation());
                                            }
                                        }
                                    }
                                }
                            }
                            for (int i = 0; i < 5; i++) {
                                Silverfish silverfish = player.getWorld().spawn(player.getLocation(), Silverfish.class);
                                silverfish.setCustomName("§7Poisson d'argents");
                                silverfish.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, true, true));
                                silverfish.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2, true, true));
                                silverfish.setTarget(nearestPlayer);
                            }

                            joueur.setRoleCooldownSaiTigres(5 * 60);
                            cancel();
                        }
                        timer--;
                    }
                }.runTaskTimer(main, 0, 1);
            } else {
                player.sendMessage(Messages.cooldown(joueur.getRoleCooldownSaiTigres()));
                inv.close(player);
            }
        }, 4);

        if (!((Sai) main.getPlayerManager(inv.getOwner().getUniqueId()).getRole()).hasUsedFuinjutsu()) {

            inv.setItem(new QuickItem(Material.PAPER)
                    .setName("§6Fûinjutsu")
                    .glow()
                    .setLore("",
                            "§7Permet de sceller quelqu'un (rendre spectateur)",
                            "§7un joueur tant que vous êtes en vie. §7Vous ne pourrez pas",
                            "§7bouger pendant le scellement (1 minute).")
                    .toItemStack(), onClick -> {
                List<Player> players = new ArrayList<>();

                for (Entity entity : inv.getOwner().getNearbyEntities(DISTANCE_FUINJUTSU, DISTANCE_FUINJUTSU, DISTANCE_FUINJUTSU)) {
                    if(entity instanceof Player){
                        Player player = (Player) entity;
                        if(player.getGameMode() != GameMode.SPECTATOR){
                            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
                            if (joueur.isAlive()) {
                                players.add(player);
                            }
                        }
                    }
                }

                new FuinjutsuGUI(main, players).open(onClick.getPlayer());
            }, 6);
        }
    }
}
