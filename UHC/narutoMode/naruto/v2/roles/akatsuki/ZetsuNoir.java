package fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.maygo.uhc.api.clickable_messages.ClickableMessage;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.events.player.PlayerKillEvent;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleListener;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdZetsu;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class ZetsuNoir extends Role implements NarutoV2Role, RoleCommand, RoleListener {

    private int timeBeforeInvisible = 20 * 10;

    private boolean hasKilled;

    @Override
    public void giveItems(Player player) {

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

    @EventHandler
    public void onKill(PlayerKillEvent event) {
        Player killer = event.getPlayer();
        Player deadPlayer = event.getDeadPlayer();
        PlayerManager killerPlayerManager = main.getPlayerManager(killer.getUniqueId());
        PlayerManager deadPlayerManager = main.getPlayerManager(deadPlayer.getUniqueId());
        if (killerPlayerManager.hasRole()) {
            if (killerPlayerManager.getRole() instanceof ZetsuNoir) {
                ZetsuNoir zetsuNoir = (ZetsuNoir) killerPlayerManager.getRole();
                if (!zetsuNoir.isHasKilled()) {

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            for (PotionEffect effect : deadPlayerManager.getRole().getEffects().keySet()) {
                                killer.addPotionEffect(effect);
                            }
                            zetsuNoir.setHasKilled(true);
                            killer.setMaxHealth(deadPlayer.getMaxHealth());
                            killer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§6Vous venez de faire votre 1er kill. Vous obtenez les effets de ce dernier.");
                        }

                    }.runTaskLater(main, 2);

                } else {
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            new ClickableMessage(killer, onClick -> {
                                for (PotionEffect effect : killer.getActivePotionEffects()) {
                                    killer.removePotionEffect(effect.getType());
                                }

                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        for (PotionEffect effect : deadPlayerManager.getRole().getEffects().keySet()) {
                                            killer.addPotionEffect(effect);
                                        }
                                        killer.setMaxHealth(deadPlayer.getMaxHealth());
                                        onClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous venez de récupérer les effets de " + deadPlayer.getName() + ".");

                                    }
                                }.runTaskLater(main, 5);

                            }, Messages.NARUTO_PREFIX.getMessage() + "§cVoulez-vous copier les effets de " + deadPlayer.getName() + " ? Les anciens effets seront enlevés (sauf les vôtres). "
                                    + Messages.CLICK_HERE.getMessage());
                        }
                    }.runTaskLater(main, 3);

                }
            }
        }
    }

    @Override
    public void onDamage(Player player, Player target) {
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.isAlive() && PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof ZetsuNoir) {
                ZetsuNoir zetsuNoir = (ZetsuNoir) PlayerManager.getRole();
                zetsuNoir.timeBeforeInvisible = 20 * 20;
            }
        }
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        if (player.getEquipment() != null) {
            EntityEquipment equipement = player.getEquipment();
            if (equipement.getHelmet() == null && equipement.getChestplate() == null && equipement.getLeggings() == null
                    && equipement.getBoots() == null && correctBlock(player)) {
                if (timeBeforeInvisible <= 0) {
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
                    for (PotionEffect effect : player.getActivePotionEffects()) {
                        if (effect.getType() == PotionEffectType.SPEED && effect.getDuration() <= 20 * 4) {
                            player.removePotionEffect(PotionEffectType.SPEED);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 4, 1, false, false));
                        }
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 4, 1, false, false));
                } else {
                    if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
                        player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    timeBeforeInvisible--;
                }
            } else {
                if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                timeBeforeInvisible = 0;
            }
        }
    }

    private boolean correctBlock(Player player) {
        Block block = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ()).getBlock();
        return block.isEmpty() || block.getType() == Material.DIRT || block.getType() == Material.GRASS || block.getType() == Material.AIR || block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2;
    }

    @Override
    public void onNewDay(Player player) {

    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName())
                .setTexture(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjVmZjQ5ZmQ2MTcwN2Y0OTg1NmE5NGU4ZjhkY2U1M2MyODk3YmExMGFmNDc5NzAyOGFhZWY2MTU5MjAxYTE4ZiJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.AKATSUKI;
    }

    @Override
    public String getRoleName() {
        return "Zetsu Noir";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdZetsu(main));
    }

    public boolean isHasKilled() {
        return hasKilled;
    }

    public void setHasKilled(boolean hasKilled) {
        this.hasKilled = hasKilled;
    }

    @Override
    public Chakra getChakra() {
        return null;
    }

}
