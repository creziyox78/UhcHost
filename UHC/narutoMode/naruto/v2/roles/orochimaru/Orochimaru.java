package fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.events.player.PlayerKillEvent;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.*;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdMarqueMaudite;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.EdoTenseiItem;
import fr.maygo.uhc.modes.naruto.v2.items.EdoTenseiItem.EdoTenseiUser;
import fr.maygo.uhc.obj.PlayerManager;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class Orochimaru extends Role implements NarutoV2Role, RoleListener, EdoTenseiUser, RoleCommand {

    private final Chakra chakra = getRandomChakra();
    private boolean kill;
    private int timeCycle = 60 * 10;

    private PlayerManager targetMarque;

    public Orochimaru() {
        super.addRoleToKnow(Kabuto.class);
        super.addEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {
        Player player = event.getPlayer();
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Orochimaru) {
                Orochimaru orochimaru = (Orochimaru) PlayerManager.getRole();
                orochimaru.setKill(true);
                player.setMaxHealth(20);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamageMarque(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            PlayerManager damagerPlayerManager = main.getPlayerManager(damager.getUniqueId());
            if (PlayerManager == getTargetMarque()) {
                event.setDamage(event.getDamage() / 1.05);
            }
            if (damagerPlayerManager == getTargetMarque()) {
                event.setDamage(event.getDamage() + ((5 / 100) * event.getDamage()));
            }
        }
    }

    @EventHandler
    public void onEatApple(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.GOLDEN_APPLE) {
            Player player = event.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Orochimaru) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                                player.removePotionEffect(PotionEffectType.REGENERATION);
                            }
                            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 7, 1));
                        }
                    }.runTaskLater(main, 3);
                }
            }
        }
    }

    @Override
    public void giveItems(Player player) {
        QuickItem epee = new QuickItem(Material.DIAMOND_SWORD).setName("§5Kusanagi").addEnchant(Enchantment.DAMAGE_ALL, 4, true);
        main.getInventoryUtils().giveItemSafely(player, new EdoTenseiItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, epee.toItemStack());
    }

    @Override
    public void afterRoles(Player player) {
        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Voici votre nature de chakra : " + StringUtils.capitalize(getChakra().toString().toLowerCase()));
        player.sendMessage(sendList());
        new BukkitRunnable() {
            @Override
            public void run() {
                for (PlayerManager PlayerManager : main.getNarutoV2Manager().getPlayerManagersWithRole(Orochimaru.class)) {
                    if (PlayerManager.getPlayer() != null) {
                        if (kill) {
                            kill = false;
                            setTimeCycle(10 * 60);
                            PlayerManager.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§dVotre cycle de vie a été réinitialisé.");
                        }
                        if (timeCycle <= 0) {
                            if (PlayerManager.getPlayer().getMaxHealth() - 2D <= 0D) {
                                PlayerManager.getPlayer().damage(100);
                            } else {
                                PlayerManager.getPlayer().setMaxHealth(PlayerManager.getPlayer().getMaxHealth() - 2D);
                                PlayerManager.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous venez de perdre 1 coeur car vous n'avez pas tué quelqu'un durant les 10 dernières minutes.");
                                setTimeCycle(10 * 60);
                            }
                        }
                        timeCycle--;
                    }
                }

            }
        }.runTaskTimer(main, 20 * 60 * 10, 20);
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        if (player.getHealth() <= (2D * 4D)) {
            if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 4, 1, false, false));
        } else {
            if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 3, 0, false, false));
        }
    }

    @Override
    public String sendList() {
        String list = Messages.NARUTO_PREFIX.getMessage() + "Voici la liste entière du camp Taka : \n";
        for (PlayerManager PlayerManager : UhcHost.getInstance().getNarutoManager().getPlayerManagersWithCamps(Camps.TAKA)) {
            if (PlayerManager.isAlive()) list += "§c- " + PlayerManager.getPlayerName() + "\n";
        }
        list += Messages.NARUTO_PREFIX.getMessage() + "Voici la liste entière du camp Orochimaru : \n";
        for (PlayerManager PlayerManager : UhcHost.getInstance().getNarutoManager().getPlayerManagersWithCamps(Camps.OROCHIMARU)) {
            if (PlayerManager.isAlive()) list += "§c- " + PlayerManager.getPlayerName() + "\n";
        }
        return list;
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
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ2YjgzZmU3MTBlOTg3NzdkMjlhZTgwNGVmNmQzYjhkMjRiYzVjNTlmZDM3YjViYTA4NDc1YmQ3Njc4ZWQwYSJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.OROCHIMARU;
    }

    @Override
    public String getRoleName() {
        return "Orochimaru";
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
        return chakra;
    }

    public boolean isKill() {
        return kill;
    }

    public void setKill(boolean kill) {
        this.kill = kill;
    }

    public int getTimeCycle() {
        return timeCycle;
    }

    public void setTimeCycle(int timeCycle) {
        this.timeCycle = timeCycle;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdMarqueMaudite(main));
    }

    public PlayerManager getTargetMarque() {
        return targetMarque;
    }

    public void setTargetMarque(PlayerManager targetMarque) {
        this.targetMarque = targetMarque;
    }
}
