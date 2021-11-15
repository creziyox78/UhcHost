package fr.lastril.uhchost.modes.naruto.v2.roles.jubi;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleCommand;
import fr.maygo.uhc.modes.naruto.v2.biju.Biju;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdDetect;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.ChibakuTenseiItem;
import fr.maygo.uhc.modes.naruto.v2.items.MadaraItem;
import fr.maygo.uhc.modes.naruto.v2.items.SusanoItem;
import fr.maygo.uhc.modes.naruto.v2.items.biju.JubiItem.JubiUser;
import fr.maygo.uhc.modes.naruto.v2.items.swords.GunbaiSword;
import fr.maygo.uhc.modes.naruto.v2.roles.JubiRole;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Madara extends Role implements NarutoV2Role, JubiUser, SusanoItem.SusanoUser, ChibakuTenseiItem.ChibakuTenseiUser, RoleCommand, JubiRole {

    private static final int DETECTION_TIME = 5 * 60;

    private int detectUses;
    private boolean usedTengaiShinsei;
    private Biju bijuTracked;

    //private double recupKuramaPoint = 0, recupShukakuPoint = 0, recupGyukiPoint = 0;

    public Madara() {
        super.addEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        super.addRoleToKnow(Obito.class);
    }

    public static int getDetectionTime() {
        return DETECTION_TIME;
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new SusanoItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new GunbaiSword().toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new MadaraItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new ChibakuTenseiItem(main).toItemStack());
    }

    @Override
    public void afterRoles(Player player) {
        player.setMaxHealth(24);
        player.setHealth(player.getMaxHealth());
        if (main.getConfiguration().getNarutoV2Config().isBiju()) {
            setBijuTracked(main.getNarutoV2Manager().getBijuManager().getBijuListClass().get(0));
            Bukkit.getScheduler().runTaskTimer(main, () -> {
                for (PlayerManager PlayerManager : main.getNarutoV2Manager().getPlayerManagersWithRole(Obito.class)) {
                    if (PlayerManager.getPlayer() != null) {
                        Player tracker = PlayerManager.getPlayer();
                        Biju biju = getBijuTracked();
                        if (biju != null) {
                            if (biju.getBijuEntity() != null) {
                                if (biju.getBijuEntity().isDead()) {
                                    if (biju.itemInInventory(biju.getItem().toItemStack())) {
                                        for (Player hote : Bukkit.getOnlinePlayers()) {
                                            if (hote.getInventory().contains(biju.getItem().toItemStack())) {
                                                tracker.setCompassTarget(hote.getLocation());
                                            }
                                        }
                                    }
                                } else {
                                    tracker.setCompassTarget(biju.getBijuEntity().getLocation());
                                }
                            } else {
                                tracker.setCompassTarget(biju.getSafeSpawnLocation());
                            }
                        }
                    }

                }
            }, 20, 10);
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName())
                .setTexture(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODcwYzQzNWI1NDgwNmIyZDU4NzhlMDk3NWExM2ZiOTU1ZGFiMmZiNjg4ZjUwYjAxMTM1NzY0OGY4N2E0Y2QxIn19fQ==");
    }

    @Override
    public Camps getCamp() {
        return Camps.JUBI;
    }

    @Override
    public String getRoleName() {
        return "Madara";
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
        return Chakra.KATON;
    }

    @Override
    public boolean hasTengaiShinsei() {
        return true;
    }

    @Override
    public boolean hasUsedTengaiShinsei() {
        return usedTengaiShinsei;
    }

    @Override
    public void useTengaiShinsei() {
        usedTengaiShinsei = true;
    }

    @Override
    public String getPlayerName() {
        return this.getRoleName();
    }

    public int getDetectUses() {
        return detectUses;
    }

    public void useDetect() {
        this.detectUses++;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdDetect(main));
    }

    public Biju getBijuTracked() {
        return bijuTracked;
    }

    public void setBijuTracked(Biju bijuTracked) {
        this.bijuTracked = bijuTracked;
    }
}
