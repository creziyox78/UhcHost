package fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.When;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdIzanagi;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdSharingan;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.GenjutsuItem;
import fr.maygo.uhc.modes.naruto.v2.items.SusanoItem;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Naruto;
import fr.maygo.uhc.modes.naruto.v2.roles.taka.Sasuke;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Itachi extends Role implements NarutoV2Role, CmdIzanagi.IzanagiUser, SusanoItem.SusanoUser, RoleCommand, GenjutsuItem.GenjutsuUser, CmdSharingan.SharinganUser {

    private static final int HEALTH_WHEN_SASUKE_DEATH = 2 * 2;
    private static final int DISTANCE_NARUTO_OROCHIMARU = 5;

    private int sharinganUses;
    private boolean hasUsedIzanagi;

    private boolean hasUsedIzanami;

    private int tsukuyomiUses;

    public Itachi() {
        super.addRoleToKnow(Kisame.class);
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new SusanoItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new GenjutsuItem(main).toItemStack());
    }

    @Override
    public void afterRoles(Player player) {
        player.setMaxHealth(24);
        player.setHealth(player.getMaxHealth());
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
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTY0YjkyMWNhOGQ1NjIwMzk4MGU4MDcxNDNlYjZmM2NjMzAwNDExY2JkNDY0MDY1YmNjZGU1Njc4ZDJlYTE4YiJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.AKATSUKI;
    }

    @Override
    public String getRoleName() {
        return "Itachi";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.getCamps() == Camps.OROCHIMARU) {
            for (Entity entity : player.getNearbyEntities(DISTANCE_NARUTO_OROCHIMARU, DISTANCE_NARUTO_OROCHIMARU, DISTANCE_NARUTO_OROCHIMARU)) {
                if (entity instanceof Player) {
                    Player players = (Player) entity;
                    if (players.getGameMode() != GameMode.SPECTATOR) {
                        PlayerManager PlayerManagers = main.getPlayerManager(players.getUniqueId());
                        if (PlayerManagers.hasRole() && PlayerManagers.getRole() instanceof Naruto) {
                            PlayerManager.setCamps(Camps.SHINOBI);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous avez rencontré Naruto, donc vous rejoignez le camps des Shinobi.");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Sasuke) {
                if (super.getPlayer() != null) {
                    Player itachi = super.getPlayer();
                    itachi.setMaxHealth(itachi.getMaxHealth() - HEALTH_WHEN_SASUKE_DEATH);
                    itachi.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false));
                    itachi.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Sasuke est mort, vous perdez donc " + (HEALTH_WHEN_SASUKE_DEATH / 2) + " cœurs et vous obtenez l'effet Faiblesse 1");
                }
            }
        }
    }

    public int getSharinganUses() {
        return sharinganUses;
    }

    public void addSharinganUse() {
        this.sharinganUses++;
    }

    @Override
    public boolean hasUsedIzanagi() {
        return hasUsedIzanagi;
    }

    @Override
    public void setHasUsedIzanagi(boolean hasUsedIzanagi) {
        this.hasUsedIzanagi = hasUsedIzanagi;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdIzanagi(main), new CmdSharingan(main));
    }

    @Override
    public void useTsukuyomi() {
        tsukuyomiUses++;
    }

    @Override
    public int getTsukuyomiUses() {
        return tsukuyomiUses;
    }

    @Override
    public void useIzanami() {
        hasUsedIzanami = true;
    }

    @Override
    public boolean hasUsedIzanami() {
        return hasUsedIzanami;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.KATON;
    }

    public boolean isHasUsedIzanami() {
        return hasUsedIzanami;
    }

    public void setHasUsedIzanami(boolean hasUsedIzanami) {
        this.hasUsedIzanami = hasUsedIzanami;
    }


}

