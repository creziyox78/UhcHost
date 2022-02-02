package fr.lastril.uhchost.modes.naruto.v2.roles.jubi;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.biju.Biju;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdIzanagi;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.*;
import fr.lastril.uhchost.modes.naruto.v2.items.biju.JubiItem;
import fr.lastril.uhchost.modes.naruto.v2.izanami.IzanamiGoal;
import fr.lastril.uhchost.modes.naruto.v2.roles.JubiRole;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Itachi;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Sasuke;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.PlayerKillEvent;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.WorldUtils;
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

    private boolean usedIzanami, useNinjutsu, hasUseNinjutsu, hasUsedIzanagi;

    private int tsukuyomiUses, timeInvisibleRemining = 60;

    private double damageResistance;

    private Biju bijuTracked;

    private boolean killedUchiwa, completeIzanami = false;

    private final Map<UUID, Location> intialLocations;
    private IzanamiGoal izanamiGoal;

    //private double recupKuramaPoint = 0, recupShukakuPoint = 0, recupGyukiPoint = 0;

    public Obito() {
        this.intialLocations = new HashMap<>();
        super.addEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false),
                When.START);
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addRoleToKnow(Madara.class);
    }

    public void setBijuTracked(Biju bijuTracked) {
        this.bijuTracked = bijuTracked;
    }

    public Biju getBijuTracked() {
        return bijuTracked;
    }

    @Override
    public void giveItems(Player player) {
        if(main.getGamemanager().getModes() != Modes.NARUTO) return;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        main.getInventoryUtils().giveItemSafely(player, new KamuiItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new NinjutsuItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new GenjutsuItem(main).toItemStack());
        if(narutoV2Manager.getNarutoV2Config().isBiju())
            main.getInventoryUtils().giveItemSafely(player, new TrackerBijuItem(main).toItemStack());
    }

    @Override
    public void checkRunnable(Player player) {
    	super.checkRunnable(player);
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if (joueur.isAlive() && joueur.hasRole()) {
            if (joueur.getRole() instanceof Obito) {
                damageResistance = (player.getMaxHealth() - player.getHealth());
            }
        }
    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {
        Player killer = event.getKiller();
        PlayerManager joueur = main.getPlayerManager(killer.getUniqueId());
        Player deadPlayer = event.getDeadPlayer();
        PlayerManager deadJoueur = main.getPlayerManager(deadPlayer.getUniqueId());
        if (joueur.isAlive() && joueur.hasRole()) {
            if (joueur.getRole() instanceof Obito) {
                if (killer.getHealth() + (3D * 2D) >= killer.getMaxHealth())
                    killer.setHealth(killer.getMaxHealth());
                else
                    joueur.getPlayer().setHealth(killer.getHealth() + (3D * 2D));

                if(deadJoueur.getRole() instanceof Madara || deadJoueur.getRole() instanceof Itachi || deadJoueur.getRole() instanceof Sasuke || deadJoueur.getRole() instanceof Obito){
                    killedUchiwa = true;
                    killer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous venez de tuer un Uchiwa, vous n'avez plus§7Blindness I§a.");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.isAlive() && joueur.hasRole()) {
                if (joueur.getRole() instanceof Obito) {
                    Obito obito = (Obito) joueur.getRole();
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
            PlayerManager joueur = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                PlayerManager damagerJoueur = UhcHost.getInstance().getPlayerManager(damager.getUniqueId());
                if (damagerJoueur.getRole() instanceof Obito) {
                    Obito obito = (Obito) damagerJoueur.getRole();
                    if (obito.isUseNinjutsu()) {
                        event.setCancelled(true);
                    }
                }
                if (joueur.getRole() instanceof Obito) {
                    double newDamage = (event.getDamage() - ((damageResistance / 40) * event.getDamage()));
                    if (newDamage <= 0) {
                        newDamage = 1;
                    }
                    event.setDamage(newDamage);
                }

            } else if (event.getDamager() instanceof Projectile) {
                Projectile damager = (Projectile) event.getDamager();
                if(damager.getShooter() instanceof Player){
                    Player shooter = (Player) damager.getShooter();
                    PlayerManager damagerJoueur = main.getPlayerManager(shooter.getUniqueId());
                    if (damagerJoueur.getRole() instanceof Obito) {
                        Obito obito = (Obito) damagerJoueur.getRole();
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
        PlayerManager joueur = UhcHost.getInstance().getPlayerManager(damager.getUniqueId());
        if (joueur.getRole() instanceof Obito) {
            Obito obito = (Obito) joueur.getRole();
            event.setCancelled(obito.isUseNinjutsu());
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerManager joueur = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
        if (joueur.hasRole()) {
            if (joueur.getRole() instanceof Obito && player.getGameMode() == GameMode.SURVIVAL) {
                Obito obito = (Obito) joueur.getRole();
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
            PlayerManager joueur = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
            if (joueur.getRole() instanceof Obito) {
                Obito obito = (Obito) joueur.getRole();
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
        return main.getRoleDescription(this, this.getClass().getName(), "naruto.yml");
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

    @Override
    public String sendList() {
        String list = Messages.NARUTO_PREFIX.getMessage() + "Voici la liste entière de l'Akatsuki : \n";
        if(main.getGamemanager().getModes() != Modes.NARUTO) return null;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        for (PlayerManager joueur : narutoV2Manager.getPlayerManagersWithCamps(Camps.AKATSUKI)) {
            if (joueur.isAlive()) list += "§c- " + joueur.getPlayerName() +"\n";
        }
        return list;
    }

    @Override
    public void afterRoles(Player player) {
        player.sendMessage(sendList());
        Bukkit.getScheduler().runTaskTimer(main, () -> {
            if(!killedUchiwa && isCompleteIzanami()){
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*5, 0, false, false));
            }
        }, 0, 20*60);
        if(main.getGamemanager().getModes() != Modes.NARUTO) return;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        if(narutoV2Manager.getNarutoV2Config().isBiju()){
            setBijuTracked(narutoV2Manager.getBijuManager().getBijuListClass().get(0));
            Bukkit.getScheduler().runTaskTimer(main, () -> {
                for(PlayerManager joueur : narutoV2Manager.getPlayerManagersWithRole(Obito.class)){
                    if(joueur.getPlayer() != null){
                        Player tracker = joueur.getPlayer();
                        Biju biju = getBijuTracked();
                        if(biju != null){
                            if(biju.getJoueurPicked() != null && biju.getJoueurPicked().isAlive()){
                                tracker.setCompassTarget(biju.getJoueurPicked().getPlayer().getLocation());
                            } else {
                                tracker.setCompassTarget(biju.getSafeSpawnLocation());
                            }
                        }
                    }

                }
            }, 20, 10);
        }




            /*if (!UhcHost.getInstance().getConfiguration().containsRoleInComposition(Minato.class) && !UhcHost.getInstance().getConfiguration().containsRoleInComposition(Naruto.class)) {
                main.getNarutoV2Manager().getBijuManager().setRecupKurama(true);
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aMinato et Naruto§e ne font pas partie de la composition. Vous n'avez pas besoin de récupérer l'ADN de leur Bijû.");
            } else {
                for(Joueur joueur : main.getNarutoV2Manager().getJoueursWithRole(Minato.class)){
                    if(super.getPlayer() != null){
                        new KuramaRecupTask(main.getJoueur(this.getPlayer().getUniqueId()), joueur, main).runTaskTimer(main, 0, 1);
                    }
                }
                for(Joueur joueur : main.getNarutoV2Manager().getJoueursWithRole(Naruto.class)){
                    if(super.getPlayer() != null){
                        new KuramaRecupTask(main.getJoueur(this.getPlayer().getUniqueId()), joueur, main).runTaskTimer(main, 0, 1);
                    }
                }
            }

            if (!UhcHost.getInstance().getConfiguration().containsRoleInComposition(KillerBee.class)) {
                main.getNarutoV2Manager().getBijuManager().setRecupGyuki(true);
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aKiller Bee§e ne fait pas partie de la composition. Vous n'avez pas besoin de récupérer l'ADN de son Bijû.");
            } else {
                for(Joueur joueur : main.getNarutoV2Manager().getJoueursWithRole(KillerBee.class)){
                    if(super.getPlayer() != null){
                        new GyukiRecupTask(main.getJoueur(this.getPlayer().getUniqueId()), joueur, main).runTaskTimer(main, 0, 1);
                    }
                }

            }

            if (!UhcHost.getInstance().getConfiguration().containsRoleInComposition(Gaara.class)) {
                main.getNarutoV2Manager().getBijuManager().setRecupShukaku(true);
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eGaara ne fait pas partie de la composition. Vous n'avez pas besoin de récupérer l'ADN de son Bijû.");
            } else {
                for(Joueur joueur : main.getNarutoV2Manager().getJoueursWithRole(Gaara.class)){
                    if(super.getPlayer() != null){
                        new ShukakuRecupTask(main.getJoueur(this.getPlayer().getUniqueId()), joueur, main).runTaskTimer(main, 0, 1);
                    }
                }
            }*/

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
        if(useNinjutsu){
            //main.getInvisibleTeam().addEntry(this.getPlayer().getName());
        }else{
            //main.getInvisibleTeam().removeEntry(this.getPlayer().getName());
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

    @Override
    public boolean hasKilledUchiwa() {
        return killedUchiwa;
    }

    @Override
    public boolean isCompleteIzanami() {
        return completeIzanami;
    }

    @Override
    public void setCompleteIzanami(boolean completeIzanami) {
        this.completeIzanami = completeIzanami;
    }

    @Override
    public IzanamiGoal getIzanamiGoal() {
        return izanamiGoal;
    }

    @Override
    public void setIzanamiGoal(IzanamiGoal izanamiGoal) {
        this.izanamiGoal = izanamiGoal;
    }
}
