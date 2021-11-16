package fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.*;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdShosenJutsu;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdShosenJutsu.ShosenJutsuUser;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.EdoTenseiItem;
import fr.maygo.uhc.modes.naruto.v2.items.EdoTenseiItem.EdoTenseiUser;
import fr.maygo.uhc.modes.naruto.v2.items.SenpoKabutoItem;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class Kabuto extends Role implements NarutoV2Role, EdoTenseiUser, RoleListener, RoleCommand, ShosenJutsuUser {

    private final int tick = 20;
    private final int timer = tick * 60;

    public Kabuto() {
        super.addRoleToKnow(Orochimaru.class);
        super.addRoleToKnow(Kimimaro.class);
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void afterRoles(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (PlayerManager PlayerManager : main.getNarutoV2Manager().getPlayerManagersWithRole(Kabuto.class)) {
                    if (PlayerManager.getPlayer() != null) {
                        if (PlayerManager.getPlayer().getHealth() + 2D >= player.getMaxHealth()) {
                            PlayerManager.getPlayer().setHealth(player.getMaxHealth());
                        } else {
                            PlayerManager.getPlayer().setHealth(player.getHealth() + 2D);
                        }
                    }

                }

            }
        }.runTaskTimer(main, 0, 20 * 60);
    }

    @Override
    public void onDamage(Player damager, Player target) {
        PlayerManager damagerPlayerManager = main.getPlayerManager(damager.getUniqueId());
        if (damagerPlayerManager.hasRole()) {
            if (damagerPlayerManager.getRole() instanceof Kabuto) {
                int value = UhcHost.getRandom().nextInt(100);
                if (value <= 5) {
                    UhcHost.debug("Kabuto effect apply to player: " + target.getName());
                    target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 0, false, false));
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 3, false, false));
                    target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous venez de vous faire étourdir.");
                }
            }
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Orochimaru) {
                if (super.getPlayer() != null) {
                    Player kabuto = super.getPlayer();
                    main.getInventoryUtils().giveItemSafely(kabuto, new EdoTenseiItem(main).toItemStack());
                    kabuto.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
                    kabuto.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§5Orochimaru§e vient de mourir. Vous recevez l'effet §7Résistance I§e ainsi que l'item§5 \"Edo Tensei\"§e.");
                }
            }
        }
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new SenpoKabutoItem(main).toItemStack());
    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    protected void onDay(Player player) {

    }

    @Override
    public void onNewEpisode(Player player) {
        new BukkitRunnable() {

            @Override
            public void run() {
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, tick * 5, 0, false, false));
            }
        }.runTaskLater(main, tick * UhcHost.getRandom().nextInt(60 * 20));

        new BukkitRunnable() {

            @Override
            public void run() {
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, tick * 5, 0, false, false));
            }
        }.runTaskLater(main, tick * UhcHost.getRandom().nextInt(60 * 20));
    }

    @Override
    public void onNewDay(Player player) {

    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                .setName(getCamp().getCompoColor() + getRoleName()).setTexture(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQyMjNhNmM0MWI5MGFmYzcyYmY2MDNiNjMzZGJhMzQxZGI0ZjM5OWQ0MDI4ZjE2NWI4NzViNjBkMmE0ZTJjNSJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.OROCHIMARU;
    }

    @Override
    public String getRoleName() {
        return "Kabuto";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    @Override
    public Chakra getChakra() {
        return Chakra.SUITON;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdShosenJutsu(main));
    }
}