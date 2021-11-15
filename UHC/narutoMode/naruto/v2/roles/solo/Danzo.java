package fr.lastril.uhchost.modes.naruto.v2.roles.solo;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.events.player.PlayerKillEvent;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.*;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdHokage;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdIzanagi;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class Danzo extends Role implements NarutoV2Role, RoleCommand, RoleListener, CmdIzanagi.IzanagiUser {

    private boolean weakHokage, usedIzanagi;

    private int life = 1;

    public Danzo() {
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void giveItems(Player player) {
        QuickItem epee = new QuickItem(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 4, true);
        main.getInventoryUtils().giveItemSafely(player, epee.toItemStack());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Danzo) {
                    Danzo danzo = (Danzo) PlayerManager.getRole();
                    if (player.getHealth() - event.getFinalDamage() <= 0.1 && !main.shuffled.contains(player)) {
                        if (danzo.getLife() >= 1) {
                            danzo.setLife(danzo.getLife() - 1);
                            int x = UhcHost.getRandom().nextInt(60) - 30;
                            int z = UhcHost.getRandom().nextInt(60) - 30;
                            PlayerManager.setStunned(false);
                            Location loc = new Location(player.getWorld(), player.getLocation().getBlockX() + x, 110, player.getLocation().getBlockZ() + z);
                            player.setMaxHealth(player.getMaxHealth() - 1);
                            if (player.getMaxHealth() <= 10) {
                                player.setHealth(player.getMaxHealth());
                            } else {
                                player.setHealth(10);
                            }
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes mort, mais vous avez survécu et avez été téléporté dans un rayon de 30 blocs du lieu de votre mort.");
                            player.sendMessage("§6Vous pouvez survivre encore " + danzo.getLife() + " fois.");
                            player.teleport(loc);
                            main.shuffled.add(player);
                            event.setCancelled(true);
                            Bukkit.getScheduler().runTaskLater(main, () -> {
                                main.shuffled.remove(player);
                                player.sendMessage("§7Vous n'êtes plus invincible.");
                            }, 20 * 10);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Danzo) {
                    Danzo danzo = (Danzo) PlayerManager.getRole();
                    if (player.getHealth() - event.getFinalDamage() <= 0.1 && !main.shuffled.contains(player)) {
                        if (danzo.getLife() >= 1) {
                            danzo.setLife(danzo.getLife() - 1);
                            int x = UhcHost.getRandom().nextInt(60) - 30;
                            int z = UhcHost.getRandom().nextInt(60) - 30;
                            Location loc = new Location(player.getWorld(), player.getLocation().getBlockX() + x, 110, player.getLocation().getBlockZ() + z);
                            player.setMaxHealth(player.getMaxHealth() - 1);
                            if (player.getMaxHealth() <= 10) {
                                player.setHealth(player.getMaxHealth());
                            } else {
                                player.setHealth(10);
                            }
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes mort, mais vous avez survécu et avez été téléporté dans un rayon de 30 blocs du lieu de votre mort.");
                            player.sendMessage("§6Vous pouvez survivre encore " + danzo.getLife() + " fois.");
                            player.teleport(loc);
                            event.setCancelled(true);
                            main.shuffled.add(player);
                            event.setCancelled(true);
                            Bukkit.getScheduler().runTaskLater(main, () -> {
                                main.shuffled.remove(player);
                                player.sendMessage("§7Vous n'êtes plus invincible.");
                            }, 20 * 10);
                        }

                    }
                }
            }
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                PlayerManager damagerPlayerManager = main.getPlayerManager(damager.getUniqueId());

                if (damagerPlayerManager.hasRole()) {
                    if (damagerPlayerManager.getRole() instanceof Danzo) {
                        QuickItem epee = new QuickItem(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 4, true);
                        if (damager.getItemInHand().isSimilar(epee.toItemStack()) && UhcHost.getRandom().nextInt(100) <= 5) {
                            event.setDamage(event.getDamage() + ((25 / 100) * event.getDamage()));
                        }
                    }
                }
            }


        }
    }


    @EventHandler
    public void onKillHokage(PlayerKillEvent event) {
        Player killer = event.getPlayer();
        Player player = event.getDeadPlayer();
        PlayerManager killerPlayerManager = main.getPlayerManager(killer.getUniqueId());
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (killerPlayerManager.hasRole()) {
            if (killerPlayerManager.getRole() instanceof Danzo) {
                Danzo danzo = (Danzo) killerPlayerManager.getRole();
                danzo.setLife(danzo.getLife() + 1);
                killer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous venez de tuer quelqu'un. Vous pouvez survivre " + danzo.getLife() + " fois maintenant.");
                if (main.getNarutoV2Manager().getHokage() != null && main.getNarutoV2Manager().getHokage() == PlayerManager) {
                    main.getNarutoV2Manager().setDanzoKilledHokage(true);
                    killer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous venez de tuer l'Hokage. Vous aurez donc les avantages d'Hokage dans 5 minutes.");
                    Bukkit.getScheduler().runTaskLater(main, () -> {
                        main.getNarutoV2Manager().danzoHokage();
                    }, 20 * 5 * 60 + 5);
                }

            }
        }
    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    protected void onDay(Player player) {

    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {

    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGZlNjE5NmRkN2QyYTYyNjIzNDFiODQwYzMyYzRkZTE1NGUyYmFkZWI2ODY4ZDA1ZjkzZTVjZGU2YWY4NzJlOSJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.DANZO;
    }

    @Override
    public String getRoleName() {
        return "Danzo";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    @Override
    public void afterRoles(Player player) {
        player.setMaxHealth(player.getMaxHealth() + (2D * 2D));
        player.setHealth(player.getMaxHealth());
    }

    @Override
    public Chakra getChakra() {
        return Chakra.FUTON;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdIzanagi(main), new CmdHokage(main));
    }

    public boolean isWeakHokage() {
        return weakHokage;
    }

    public void setWeakHokage(boolean weakHokage) {
        this.weakHokage = weakHokage;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    @Override
    public boolean hasUsedIzanagi() {
        return usedIzanagi;
    }

    @Override
    public void setHasUsedIzanagi(boolean hasUsedIzanagi) {
        this.usedIzanagi = hasUsedIzanagi;
    }

    public void useVie(PlayerManager PlayerManager, int damage) {
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Danzo) {
                Danzo danzo = (Danzo) PlayerManager.getRole();
                if (PlayerManager.getPlayer() != null) {
                    Player player = PlayerManager.getPlayer();
                    if (player.getHealth() - damage <= 0.1 && !main.shuffled.contains(player)) {
                        if (danzo.getLife() >= 1) {
                            danzo.setLife(danzo.getLife() - 1);
                            int x = UhcHost.getRandom().nextInt(60) - 30;
                            int z = UhcHost.getRandom().nextInt(60) - 30;
                            Location loc = new Location(player.getWorld(), player.getLocation().getBlockX() + x, 110, player.getLocation().getBlockZ() + z);
                            player.setMaxHealth(player.getMaxHealth() - 1);
                            if (player.getMaxHealth() <= 10) {
                                player.setHealth(player.getMaxHealth());
                            } else {
                                player.setHealth(10);
                            }
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes mort, mais vous avez survécu et avez été téléporté dans un rayon de 30 blocs du lieu de votre mort.");
                            player.sendMessage("§6Vous pouvez survivre encore " + danzo.getLife() + " fois.");
                            player.teleport(loc);
                            main.shuffled.add(player);
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    main.shuffled.remove(player);
                                    player.sendMessage("§7Vous n'êtes plus invincible.");
                                }
                            }.runTaskLater(main, 20 * 5);
                        } else {
                            if (PlayerManager.getPlayer() != null) {
                                PlayerManager.getPlayer().damage(100);
                            }
                        }

                    }
                }
            }
        }
    }
}
