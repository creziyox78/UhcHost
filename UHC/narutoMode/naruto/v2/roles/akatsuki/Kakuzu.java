package fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleListener;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.CorpsRapiece;
import fr.maygo.uhc.obj.PlayerManager;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Kakuzu extends Role implements NarutoV2Role, RoleListener {

    private final ArrayList<Player> corpsRapieceImmobilise;
    private int life = 4;
    private Chakra chakra = Chakra.DOTON;
    private boolean vulnerable;
    private int timeVulnerable = 5 * 60;

    public Kakuzu() {
        super.addRoleToKnow(Hidan.class);
        this.corpsRapieceImmobilise = new ArrayList<>();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.isAlive() && PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Kakuzu) {
                    Kakuzu kakuzu = (Kakuzu) PlayerManager.getRole();
                    if (!kakuzu.isVulnerable()) {
                        if (player.getHealth() <= 1 || player.getHealth() - event.getFinalDamage() <= 1) {
                            kakuzu.setVulnerable(true);
                            if (kakuzu.getLife() > 0) {
                                event.setCancelled(true);
                                // player.teleport(StartTask.getRandomLocation());
                                kakuzu.setLife(kakuzu.getLife() - 1);
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage()
                                        + "§cVous venez de mourir. Il vous reste " + kakuzu.getLife() + " vie(s).");
                                setChakraAfterDeath(kakuzu.getLife());
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Votre nature de chakra a changé. Chakra: " + chakra.toString());
                                if (player.hasPotionEffect(PotionEffectType.REGENERATION))
                                    player.removePotionEffect(PotionEffectType.REGENERATION);
                                if (player.hasPotionEffect(PotionEffectType.ABSORPTION))
                                    player.removePotionEffect(PotionEffectType.ABSORPTION);
                                if (player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE))
                                    player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                                player.addPotionEffect(
                                        new PotionEffect(PotionEffectType.ABSORPTION, 20 * 6, 1, false, false));
                                player.addPotionEffect(
                                        new PotionEffect(PotionEffectType.REGENERATION, 20 * 41, 1, false, false));
                                player.addPotionEffect(
                                        new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 41, 1, false, false));
                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        kakuzu.setVulnerable(false);
                                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage()
                                                + "§aVous pouvez à nouveau résister à la mort.");
                                    }
                                }.runTaskLater(main, timeVulnerable * 20);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.isAlive() && PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Kakuzu) {
                    Kakuzu kakuzu = (Kakuzu) PlayerManager.getRole();
                    if (!kakuzu.isVulnerable()) {
                        if (player.getHealth() <= 1 || (player.getHealth() - event.getFinalDamage() <= 1 && !main.getNarutoV2Manager().getBijuManager().getNoFall().contains(PlayerManager))) {
                            kakuzu.setVulnerable(true);
                            if (kakuzu.getLife() > 0) {
                                event.setCancelled(true);
                                // player.teleport(StartTask.getRandomLocation());
                                kakuzu.setLife(kakuzu.getLife() - 1);
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage()
                                        + "§cVous venez de mourir. Il vous reste " + kakuzu.getLife() + " vie(s).");
                                setChakraAfterDeath(kakuzu.getLife());
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Votre nature de chakra a changé. Chakra: " + StringUtils.capitalize(chakra.toString().toLowerCase()));
                                if (player.hasPotionEffect(PotionEffectType.REGENERATION))
                                    player.removePotionEffect(PotionEffectType.REGENERATION);
                                if (player.hasPotionEffect(PotionEffectType.ABSORPTION))
                                    player.removePotionEffect(PotionEffectType.ABSORPTION);
                                if (player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE))
                                    player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                                player.addPotionEffect(
                                        new PotionEffect(PotionEffectType.ABSORPTION, 20 * 6, 1, false, false));
                                player.addPotionEffect(
                                        new PotionEffect(PotionEffectType.REGENERATION, 20 * 41, 1, false, false));
                                player.addPotionEffect(
                                        new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 41, 1, false, false));
                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        kakuzu.setVulnerable(false);
                                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage()
                                                + "§aVous pouvez à nouveau résister à la mort.");
                                    }
                                }.runTaskLater(main, timeVulnerable * 20);
                            }
                        }
                    }

                }
            }
        }
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.isAlive() && PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Kakuzu) {
                for (PlayerManager targetPlayerManager : UhcHost.getInstance().getNarutoV2Manager().getPlayerManagersWithRole(Hidan.class)) {
                    if (targetPlayerManager.isAlive()) {
                        if (targetPlayerManager.getPlayer() != null) {
                            if (player.getWorld() == targetPlayerManager.getPlayer().getWorld()) {
                                if (player.getLocation().distance(targetPlayerManager.getPlayer().getLocation()) <= 10) {
                                    if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                                        player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                                    }
                                    player.addPotionEffect(
                                            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 4, 0, false, false));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new CorpsRapiece(main).toItemStack());
    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    protected void onDay(Player player) {

    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {

    }

    private Chakra setChakraAfterDeath(int life) {
        switch (life) {
            case 3:
                chakra = Chakra.RAITON;
                break;
            case 2:
                chakra = Chakra.FUTON;
                break;
            case 1:
                chakra = Chakra.KATON;
                break;
            case 0:
                chakra = Chakra.SUITON;
                break;
            default:
                chakra = Chakra.DOTON;
                break;
        }
        return chakra;
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                .setName(getCamp().getCompoColor() + getRoleName()).setTexture(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2ZmMmIzOTJlOGQ2YTVkNGVlZjY4MGNjMzZiNmU4MzhmZmEwZDQxNzExMWZjNDEzZjJjZWFmOTdmNTgwMDFlMiJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.AKATSUKI;
    }

    @Override
    public String getRoleName() {
        return "Kakuzu";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public Chakra getChakra() {
        return chakra;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
    }

    public int getTimeVulnerable() {
        return timeVulnerable;
    }

    public void setTimeVulnerable(int timeVulnerable) {
        this.timeVulnerable = timeVulnerable;
    }

    public ArrayList<Player> getCorpsRapieceImmobilise() {
        return corpsRapieceImmobilise;
    }
}
