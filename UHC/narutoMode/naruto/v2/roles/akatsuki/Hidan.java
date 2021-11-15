package fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki;


import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class Hidan extends Role implements NarutoV2Role, RoleListener {

    private final Set<Block> plateforme;
    private Player target;

    public Hidan() {
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
        this.plateforme = new HashSet<>();
        super.addRoleToKnow(Kakuzu.class);
    }

    @Override
    public void giveItems(Player player) {
        QuickItem plateforme = new QuickItem(Material.REDSTONE_BLOCK, 5).setName("§bPlateforme");
        QuickItem hoe = new QuickItem(Material.DIAMOND_HOE).setName("§4Rituel").addEnchant(Enchantment.DAMAGE_ALL, 5,
                true);
        player.getInventory().addItem(plateforme.toItemStack(), hoe.toItemStack());
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.isAlive() && PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Hidan) {
                for (PlayerManager targetPlayerManager : UhcHost.getInstance().getNarutoV2Manager().getPlayerManagersWithRole(Kakuzu.class)) {
                    if (targetPlayerManager.getPlayer() != null) {
                        if (player.getWorld() == targetPlayerManager.getPlayer().getWorld()) {
                            if (player.getLocation().distance(targetPlayerManager.getPlayer().getLocation()) <= 10) {
                                if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                                }
                                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 4, 0, false, false));
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
        ItemStack item = event.getItemInHand();
        if (item.getType() == Material.REDSTONE_BLOCK) {
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§bPlateforme")) {
                    if (PlayerManager != null && PlayerManager.hasRole()) {
                        if (PlayerManager.getRole() instanceof Hidan) {
                            Hidan hidan = (Hidan) PlayerManager.getRole();
                            event.getBlockPlaced().setType(Material.AIR);
                            sphereblock(event.getBlockPlaced().getLocation().clone().subtract(0, 1, 0), 2);
                            for (Block bloc : hidan.plateforme) {
                                boolean isObsidian = UhcHost.getRANDOM().nextBoolean();
                                if (isObsidian) {
                                    bloc.setType(Material.BEDROCK);
                                } else {
                                    bloc.setType(Material.REDSTONE_BLOCK);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Hidan) {
                    Hidan hidan = (Hidan) PlayerManager.getRole();
                    if (hidan.isInPlateforme(player) && hidan.getTarget() != null) {
                        if (hidan.getNewTarget() != null) {
                            if (player.getLocation().distance(hidan.getNewTarget().getLocation()) <= 100) {
                                double damages = event.getFinalDamage();
                                event.setDamage(0);
                                hidan.getNewTarget().damage(damages);
                            } else {
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§6Votre cible à plus de 100 blocs. Vous subissez quand même les dégâts.");
                            }
                        }
                    }
                }
            }
        }

    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Hidan) {
                    Hidan hidan = (Hidan) PlayerManager.getRole();
                    if (hidan.isInPlateforme(player) && hidan.getTarget() != null) {
                        if (hidan.getNewTarget() != null) {
                            if (player.getLocation().distance(hidan.getNewTarget().getLocation()) <= 100) {
                                double damages = event.getFinalDamage();
                                event.setDamage(0);
                                hidan.getNewTarget().damage(damages);
                            } else {
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§6Votre cible à plus de 100 blocs. Vous subissez quand même les dégâts.");
                            }
                        }
                    }
                }
            }
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                PlayerManager damagerPlayerManager = UhcHost.getInstance().getPlayerManager(damager.getUniqueId());
                if (damagerPlayerManager.hasRole()) {
                    if (damagerPlayerManager.getRole() instanceof Hidan) {
                        Hidan hidan = (Hidan) damagerPlayerManager.getRole();
                        if (damager.getItemInHand() != null) {
                            if (damager.getItemInHand().getType() == Material.DIAMOND_HOE) {
                                if (damager.getItemInHand().hasItemMeta()
                                        && damager.getItemInHand().getItemMeta().hasDisplayName()) {
                                    if (damager.getItemInHand().getItemMeta().getDisplayName()
                                            .equalsIgnoreCase("§4Rituel")) {
                                        if (!UhcHost.getInstance().getNarutoV2Manager()
                                                .isInSamehada(player.getUniqueId())) {
                                            if (damagerPlayerManager.getRoleCooldownRituel() == 0) {
                                                hidan.usePower(damagerPlayerManager);
                                                hidan.setTarget(player);
                                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage()
                                                        + "Votre vie est liée à celle d'Hidan");
                                                damagerPlayerManager.getPlayer()
                                                        .sendMessage(Messages.NARUTO_PREFIX.getMessage()
                                                                + "Votre vie a bien été liée avec " + player.getName()
                                                                + " !");
                                                damagerPlayerManager.setRoleCooldownRituel(20 * 60);
                                                new BukkitRunnable() {

                                                    int timer = 60 * 5;

                                                    @Override
                                                    public void run() {
                                                        String messageTarget = "§aLiaison avec Hidan : §e"
                                                                + new FormatTime(timer).toString();
                                                        String messageHidan = "§aLiaison avec votre cible : §e"
                                                                + new FormatTime(timer).toString();
                                                        if (damagerPlayerManager.isAlive() && UhcHost.getInstance()
                                                                .getPlayerManager(player.getUniqueId()).isAlive()) {
                                                            ActionBar.sendMessage(damager, messageHidan);
                                                            ActionBar.sendMessage(player, messageTarget);
                                                        } else {
                                                            ActionBar.sendMessage(damager, "§cVotre cible est morte !");
                                                            ActionBar.sendMessage(player, "§cVous êtes mort !");
                                                            cancel();
                                                        }

                                                        if (timer == 0) {
                                                            hidan.getNewTarget().sendMessage(Messages.NARUTO_PREFIX
                                                                    .getMessage()
                                                                    + "Votre vie n'est plus liée à celle d'Hidan");
                                                            hidan.setTarget(null);
                                                            hidan.getPlayer().sendMessage(Messages.NARUTO_PREFIX
                                                                    .getMessage()
                                                                    + "Votre vie n'est plus liée à celle de votre cible !");
                                                            damagerPlayerManager.sendTimer(damagerPlayerManager.getPlayer(), damagerPlayerManager.getRoleCooldownRituel(), damager.getItemInHand());
                                                            cancel();
                                                        }
                                                        timer--;
                                                    }
                                                }.runTaskTimer(UhcHost.getInstance(), 0, 20);
                                            } else {
                                                damagerPlayerManager.getPlayer()
                                                        .sendMessage("§cVous ne pouvez pas lier votre vie : "
                                                                + new FormatTime(damagerPlayerManager.getRoleCooldownRituel()));
                                            }
                                        } else {
                                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage()
                                                    + "§cVous êtes sous l'effet de Samehada.");
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void sphereblock(final Location center, final double radius) {
        final Set<Block> blocks = new HashSet<Block>();
        for (double x = -radius; x <= radius; x++)
            for (double z = -radius; z <= radius; z++)
                if (center.clone().add(x, 0, z).distance(center) <= radius)
                    plateforme.add(center.clone().add(x, 0, z).getBlock());
    }

    private boolean isInPlateforme(Player damager) {
        return this.plateforme.stream().filter(block -> block.getLocation().distance(damager.getLocation()) < 1.5D)
                .count() > 0;
    }

    public Player getNewTarget() {
        return Bukkit.getPlayer(this.target.getUniqueId());
    }

    public Player getTarget() {
        return this.target;
    }

    public void setTarget(Player target) {
        this.target = target;
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName())
                .setTexture(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTFkZjczZjQ5NWZiNWE2YjUzZjE2YzM4ZjQ2OWVmZDg0NTIxNzVjMTA2MTFhNTc4ZjYwYTAyN2E4YzJjOTI1In19fQ==");
    }

    @Override
    public Camps getCamp() {
        return Camps.AKATSUKI;
    }

    @Override
    public String getRoleName() {
        return "Hidan";
    }

    @Override
    public String getDescription() {
        return I18n.tl(""+this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    @Override
    public Chakra getChakra() {
        return null;
    }
}
