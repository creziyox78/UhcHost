package fr.lastril.uhchost.modes.naruto.v2.roles.jubi;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.WorldUtils;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.events.player.PlayerKillEvent;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.*;
import fr.maygo.uhc.modes.naruto.v2.biju.Biju;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdIzanagi;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.*;
import fr.maygo.uhc.modes.naruto.v2.items.biju.JubiItem;
import fr.maygo.uhc.modes.naruto.v2.roles.JubiRole;
import fr.maygo.uhc.obj.PlayerManager;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Obito extends Role implements NarutoV2Role, RoleCommand, KamuiItem.KamuiUser, CmdIzanagi.IzanagiUser, SusanoItem.SusanoUser, JubiItem.JubiUser, RoleListener, JubiRole, GenjutsuItem.GenjutsuUser {

    private final Map<UUID, Location> intialLocations;
    private boolean usedIzanami, useNinjutsu, hasUseNinjutsu, hasUsedIzanagi;
    private int tsukuyomiUses, timeInvisibleRemining = 60;
    private double damageResistance;
    private Biju bijuTracked;

    //private double recupKuramaPoint = 0, recupShukakuPoint = 0, recupGyukiPoint = 0;

    public Obito() {
        this.intialLocations = new HashMap<>();
        super.addEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false),
                When.START);
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addRoleToKnow(Madara.class);
    }

    public Biju getBijuTracked() {
        return bijuTracked;
    }

    public void setBijuTracked(Biju bijuTracked) {
        this.bijuTracked = bijuTracked;
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new KamuiItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new NinjutsuItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new GenjutsuItem(main).toItemStack());
        if (main.getConfiguration().getNarutoV2Config().isBiju())
            main.getInventoryUtils().giveItemSafely(player, new TrackerBijuItem(main).toItemStack());
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.isAlive() && PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Obito) {
                damageResistance = (player.getMaxHealth() - player.getHealth());
            }
        }
    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {
        Player killer = event.getPlayer();
        PlayerManager PlayerManager = main.getPlayerManager(killer.getUniqueId());
        if (PlayerManager.isAlive() && PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Obito) {
                if (killer.getHealth() + (3D * 2D) >= killer.getMaxHealth())
                    killer.setHealth(killer.getMaxHealth());
                else
                    PlayerManager.getPlayer().setHealth(killer.getHealth() + (3D * 2D));
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.isAlive() && PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Obito) {
                    Obito obito = (Obito) PlayerManager.getRole();
                    if (obito.isUseNinjutsu() && !event.isCancelled()) {
                        double newDamage = (event.getDamage() - ((damageResistance / 40) * event.getDamage()));
                        if (newDamage <= 0) {
                            newDamage = 1;
                        }
                        event.setDamage(newDamage);
                    }
                }
            }

        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                PlayerManager damagerPlayerManager = UhcHost.getInstance().getPlayerManager(damager.getUniqueId());
                if (damagerPlayerManager.getRole() instanceof Obito) {
                    Obito obito = (Obito) damagerPlayerManager.getRole();
                    if (obito.isUseNinjutsu()) {
                        event.setCancelled(true);
                    }
                }
                if (PlayerManager.getRole() instanceof Obito) {
                    double newDamage = (event.getDamage() - ((damageResistance / 40) * event.getDamage()));
                    if (newDamage <= 0) {
                        newDamage = 1;
                    }
                    event.setDamage(newDamage);
                }

            } else if (event.getDamager() instanceof Projectile) {
                Projectile damager = (Projectile) event.getDamager();
                if (damager.getShooter() instanceof Player) {
                    Player shooter = (Player) damager.getShooter();
                    PlayerManager damagerPlayerManager = main.getPlayerManager(shooter.getUniqueId());
                    if (damagerPlayerManager.getRole() instanceof Obito) {
                        Obito obito = (Obito) damagerPlayerManager.getRole();
                        if (obito.isUseNinjutsu()) {
                            event.setCancelled(true);
                        } else {
                            double newDamage = (event.getDamage() - ((damageResistance / 40) * event.getDamage()));
                            if (newDamage <= 0) {
                                newDamage = 1;
                            }
                            event.setDamage(newDamage);
                        }

                    }
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBucket(PlayerBucketEmptyEvent event) {
        Player damager = event.getPlayer();
        if (event.getBucket() == Material.WATER_BUCKET)
            return;
        PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(damager.getUniqueId());
        if (PlayerManager.getRole() instanceof Obito) {
            Obito obito = (Obito) PlayerManager.getRole();
            event.setCancelled(obito.isUseNinjutsu());
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Obito && player.getGameMode() == GameMode.SURVIVAL) {
                Obito obito = (Obito) PlayerManager.getRole();
                if (obito.isUseNinjutsu()) {
                    if (event.getTo().distance(event.getFrom()) > 0.2) {
                        WorldUtils.spawnParticle(player.getLocation(), EnumParticle.REDSTONE);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
            if (PlayerManager.getRole() instanceof Obito) {
                Obito obito = (Obito) PlayerManager.getRole();
                event.setCancelled(obito.isUseNinjutsu());
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
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWRiYWEzMDI4OGE3MTBhMjE0MjhiOWQwNzY5NjQ0NWVlM2UxMzIxNzg4MDczOGM3OGUyOGU5YjhjOWM5MmQ5ZSJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.JUBI;
    }

    @Override
    public String getRoleName() {
        return "Obito";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    @Override
    public String sendList() {
        String list = Messages.NARUTO_PREFIX.getMessage() + "Voici la liste entière de l'Akatsuki : \n";
        for (PlayerManager PlayerManager : UhcHost.getInstance().getNarutoManager().getPlayerManagersWithCamps(Camps.AKATSUKI)) {
            if (PlayerManager.isAlive()) list += "§c- " + PlayerManager.getPlayerName() + "\n";
        }
        return list;
    }

    @Override
    public void afterRoles(Player player) {
        player.sendMessage(sendList());
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




            /*if (!UhcHost.getInstance().getConfiguration().containsRoleInComposition(Minato.class) && !UhcHost.getInstance().getConfiguration().containsRoleInComposition(Naruto.class)) {
                main.getNarutoV2Manager().getBijuManager().setRecupKurama(true);
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aMinato et Naruto§e ne font pas partie de la composition. Vous n'avez pas besoin de récupérer l'ADN de leur Bijû.");
            } else {
                for(PlayerManager PlayerManager : main.getNarutoV2Manager().getPlayerManagersWithRole(Minato.class)){
                    if(super.getPlayer() != null){
                        new KuramaRecupTask(main.getPlayerManager(this.getPlayer().getUniqueId()), PlayerManager, main).runTaskTimer(main, 0, 1);
                    }
                }
                for(PlayerManager PlayerManager : main.getNarutoV2Manager().getPlayerManagersWithRole(Naruto.class)){
                    if(super.getPlayer() != null){
                        new KuramaRecupTask(main.getPlayerManager(this.getPlayer().getUniqueId()), PlayerManager, main).runTaskTimer(main, 0, 1);
                    }
                }
            }

            if (!UhcHost.getInstance().getConfiguration().containsRoleInComposition(KillerBee.class)) {
                main.getNarutoV2Manager().getBijuManager().setRecupGyuki(true);
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aKiller Bee§e ne fait pas partie de la composition. Vous n'avez pas besoin de récupérer l'ADN de son Bijû.");
            } else {
                for(PlayerManager PlayerManager : main.getNarutoV2Manager().getPlayerManagersWithRole(KillerBee.class)){
                    if(super.getPlayer() != null){
                        new GyukiRecupTask(main.getPlayerManager(this.getPlayer().getUniqueId()), PlayerManager, main).runTaskTimer(main, 0, 1);
                    }
                }

            }

            if (!UhcHost.getInstance().getConfiguration().containsRoleInComposition(Gaara.class)) {
                main.getNarutoV2Manager().getBijuManager().setRecupShukaku(true);
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eGaara ne fait pas partie de la composition. Vous n'avez pas besoin de récupérer l'ADN de son Bijû.");
            } else {
                for(PlayerManager PlayerManager : main.getNarutoV2Manager().getPlayerManagersWithRole(Gaara.class)){
                    if(super.getPlayer() != null){
                        new ShukakuRecupTask(main.getPlayerManager(this.getPlayer().getUniqueId()), PlayerManager, main).runTaskTimer(main, 0, 1);
                    }
                }
            }*/
        }
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdIzanagi(main));
    }

    @Override
    public int getArimasuCooldown() {
        return 5 * 60;
    }

    @Override
    public int getSonohokaCooldown() {
        return 15 * 60;
    }

    @Override
    public Map<UUID, Location> getInitialsLocation() {
        return this.intialLocations;
    }

    @Override
    public double getSonohokaDistance() {
        return 20;
    }

    @Override
    public boolean hasUsedIzanagi() {
        return hasUsedIzanagi;
    }

    @Override
    public void setHasUsedIzanagi(boolean hasUsedIzanagi) {
        this.hasUsedIzanagi = hasUsedIzanagi;
    }

    public boolean isUseNinjutsu() {
        return useNinjutsu;
    }

    public void setUseNinjutsu(boolean useNinjutsu) {

        this.useNinjutsu = useNinjutsu;
        if (useNinjutsu) {
            main.getInvisibleTeam().addEntry(this.getPlayer().getName());
        } else {
            main.getInvisibleTeam().removeEntry(this.getPlayer().getName());
        }
    }

    public int getTimeInvisibleRemining() {
        return timeInvisibleRemining;
    }

    public void setTimeInvisibleRemining(int timeInvisibleRemining) {
        this.timeInvisibleRemining = timeInvisibleRemining;
    }

    public boolean isHasUseNinjutsu() {
        return hasUseNinjutsu;
    }

    public void setHasUseNinjutsu(boolean hasUseNinjutsu) {
        this.hasUseNinjutsu = hasUseNinjutsu;
    }

    public double getDamageResistance() {
        return damageResistance;
    }

    public void setDamageResistance(double damageResistance) {
        this.damageResistance = damageResistance;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.KATON;
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
        usedIzanami = true;
    }

    @Override
    public boolean hasUsedIzanami() {
        return usedIzanami;
    }
}
