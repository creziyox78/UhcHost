package fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.*;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdOsama;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdSharingan;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.ChibakuTenseiItem;
import fr.maygo.uhc.modes.naruto.v2.items.Oiseautem;
import fr.maygo.uhc.modes.naruto.v2.roles.jubi.Obito;
import fr.maygo.uhc.modes.naruto.v2.tasks.NagatoCooldownsTask;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Nagato extends Role implements NarutoV2Role, RoleListener, RoleCommand, CmdSharingan.SharinganUser, ChibakuTenseiItem.ChibakuTenseiUser {

    private static final int REGENERATION_DELAY = 30, RESISTANCE_CHANCE = 5, VELOCITY_DISTANCE = 20, FLY_TIME = 10;
    private final List<PlayerManager> akatsukiList = new ArrayList<>();
    private long lastCombat;
    private int sharinganUses;
    private boolean useOsama;

    public Nagato() {
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    public static int getFlyTime() {
        return FLY_TIME;
    }

    public static int getVelocityDistance() {
        return VELOCITY_DISTANCE;
    }

    public boolean isUseOsama() {
        return useOsama;
    }

    public void setUseOsama(boolean useOsama) {
        this.useOsama = useOsama;
    }

    @Override
    public void giveItems(Player player) {
        new NagatoCooldownsTask(main, this).runTaskTimer(main, 0, 20 * 5);

        main.getInventoryUtils().giveItemSafely(player, new ChibakuTenseiItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Oiseautem(main).toItemStack());
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
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        long delayLastCombat = System.currentTimeMillis() - this.lastCombat;
        if (delayLastCombat > REGENERATION_DELAY * 1000) {
            if (!player.hasPotionEffect(PotionEffectType.REGENERATION))
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, false, false));
        } else {
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().getId() == 10) {
                    if (effect.getAmplifier() < 1) {
                        if (player.hasPotionEffect(PotionEffectType.REGENERATION))
                            player.removePotionEffect(PotionEffectType.REGENERATION);
                    }
                }
            }

        }
    }

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                PlayerManager damagerPlayerManager = main.getPlayerManager(damager.getUniqueId());
                if (PlayerManager.hasRole() && PlayerManager.getRole() instanceof Nagato) {
                    Nagato nagato = (Nagato) PlayerManager.getRole();
                    nagato.setLastCombat(System.currentTimeMillis());
                    int chance = UhcHost.RANDOM.nextInt(100);
                    UhcHost.debug("nagato's resistance chance : " + chance + "/" + RESISTANCE_CHANCE);
                    if (chance <= RESISTANCE_CHANCE) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 25 * 20, 0, false, false));
                    }
                }
                if (damagerPlayerManager.hasRole() && damagerPlayerManager.getRole() instanceof Nagato) {
                    Nagato nagato = (Nagato) damagerPlayerManager.getRole();
                    nagato.setLastCombat(System.currentTimeMillis());
                }
            } else if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();
                if (projectile.getShooter() instanceof Player) {
                    if (PlayerManager.hasRole() && PlayerManager.getRole() instanceof Nagato) {
                        Nagato nagato = (Nagato) PlayerManager.getRole();
                        nagato.setLastCombat(System.currentTimeMillis());
                    }
                }
            }
        }
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjkyMjY0NmE4OTQ1YjVjNDAwZTkwNDZjN2JhMjA4MGZjNmQ0N2ExNjUxMjEyNmI4OWJmNzc3ZDg0MjllZTU1NiJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public void afterRoles(Player player) {
        player.setMaxHealth(30);
        player.setHealth(player.getMaxHealth());
        player.sendMessage(sendList());
    }

    @Override
    public Camps getCamp() {
        return Camps.AKATSUKI;
    }

    @Override
    public String getRoleName() {
        return "Nagato";
    }

    @Override
    public String sendList() {
        String list = Messages.NARUTO_PREFIX.getMessage() + "Voici la liste entière de l'Akatsuki : \n";
        for (PlayerManager PlayerManager : UhcHost.getInstance().getNarutoManager().getPlayerManagersWithCamps(Camps.AKATSUKI)) {
            akatsukiList.add(PlayerManager);
        }
        for (PlayerManager PlayerManager : main.getNarutoV2Manager().getPlayerManagersWithRole(Obito.class)) {
            akatsukiList.add(PlayerManager);
        }
        int numberOfElements = akatsukiList.size();
        for (int i = 0; i < numberOfElements; i++) {
            int index = UhcHost.getRandom().nextInt(akatsukiList.size());
            list += "§c- " + akatsukiList.get(index).getPlayerName() + "\n";
            akatsukiList.remove(index);
        }

        return list;
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
        return Arrays.asList(new CmdSharingan(main), new CmdOsama(main));
    }

    public long getLastCombat() {
        return lastCombat;
    }

    public void setLastCombat(long lastCombat) {
        this.lastCombat = lastCombat;
    }

    @Override
    public int getSharinganUses() {
        return sharinganUses;
    }

    @Override
    public void addSharinganUse() {
        sharinganUses++;
    }

    public int getCooldownDistance() {
        return 20;
    }

    @Override
    public boolean hasTengaiShinsei() {
        return false;
    }

    @Override
    public boolean hasUsedTengaiShinsei() {
        //USELESS
        return false;
    }

    @Override
    public void useTengaiShinsei() {
        //USELESS
    }

    @Override
    public String getPlayerName() {
        return this.getRoleName();
    }
}
