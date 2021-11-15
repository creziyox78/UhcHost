package fr.lastril.uhchost.modes.naruto.v2.biju.bijus;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.biju.Biju;
import fr.lastril.uhchost.modes.naruto.v2.items.biju.ChomeiItem;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChomeiBiju extends Biju {

    private final Location spawn;
    private final String nameBiju = "§aChômei";
    private final UhcHost main = UhcHost.getInstance();
    private final int distance = 15;
    private Ghast ghast;
    private boolean alive;
    private int timerRespawn = 60 * 5;


    public ChomeiBiju() {
        super(UhcHost.getInstance());
        timerRespawn = 1;
        main.getServer().getPluginManager().registerEvents(this, main);
        int value = UhcHost.getRANDOM().nextInt(3);
        if (value == 0) {
            spawn = new Location(Bukkit.getWorld(super.getWorld()), UhcHost.getRANDOM().nextInt(150, 300), 50, UhcHost.getRANDOM().nextInt(150, 300));
        } else if (value == 1) {
            spawn = new Location(Bukkit.getWorld(super.getWorld()), -UhcHost.getRANDOM().nextInt(150, 300), 50, UhcHost.getRANDOM().nextInt(150, 300));
        } else if (value == 2) {
            spawn = new Location(Bukkit.getWorld(super.getWorld()), UhcHost.getRANDOM().nextInt(150, 300), 50, -UhcHost.getRANDOM().nextInt(150, 300));
        } else {
            spawn = new Location(Bukkit.getWorld(super.getWorld()), -UhcHost.getRANDOM().nextInt(150, 300), 50, -UhcHost.getRANDOM().nextInt(150, 300));
        }
    }

    @EventHandler
    public void onBijuDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Ghast) {
            Ghast ghast = (Ghast) event.getEntity();
            Player killer = ghast.getKiller();
            if (ghast.getCustomName() != null && ghast.getCustomName().equalsIgnoreCase(nameBiju)) {
                event.getDrops().clear();
                if (killer != null && getItem() != null) {
                    main.getInventoryUtils().giveItemSafely(killer, getItem().toItemStack());
                    super.setHote(killer.getPlayer());
                }
                setAlive(false);
                Bukkit.broadcastMessage(Messages.NARUTO_PREFIX.getMessage() + nameBiju + " vient d'être vaincu !");
            }
        }
    }

    @EventHandler
    public void pickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (event.getItem().getItemStack().getType() == Material.NETHER_STAR) {
            if (event.getItem().getItemStack().isSimilar(this.getItem().toItemStack())) {
                super.setHote(player);
            }
        }

    }

    @EventHandler
    public void onDeathHote(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(this.getClass()) == playerManager) {
            main.getNarutoV2Manager().getBijuManager().getHotesBiju().put(this.getClass(), null);
        }
    }

    @EventHandler
    public void hitFireball(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Fireball) {
            Fireball f = (Fireball) event.getEntity();
            if (f.getShooter() instanceof Ghast) {
                Ghast ghast = (Ghast) f.getShooter();
                if (ghast == this.ghast) {
                    Location location = f.getLocation();
                    location.getWorld().spawn(location, Creeper.class);
                }
            }
        }
    }

    @Override
    public void run() {
        if (timerRespawn == 30) {
            Bukkit.broadcastMessage(Messages.NARUTO_PREFIX.getMessage() + nameBiju + " va bientôt apparaître.");
        }
        if (timerRespawn == 0) {
            Bukkit.broadcastMessage(Messages.NARUTO_PREFIX.getMessage() + nameBiju + " vient d'apparaître.");
            ghast = getSafeSpawnLocation().getWorld().spawn(getSafeSpawnLocation(), Ghast.class);
            setupBiju(nameBiju);
            ghast.getLocation().getChunk().load(true);
        }
        if (isAlive() || main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(this.getClass()) != null || itemInInventory(getItem().toItemStack())) {
            timerRespawn = 60 * 5;
        }
        timerRespawn--;

        if (ghast != null) {
            if (!ghast.isDead()) {
                Location loc = ghast.getLocation();
                if (loc.distance(getSafeSpawnLocation()) >= 50) {
                    ghast.teleport(getSafeSpawnLocation());
                }
            }
        }
    }

    @Override
    public void setupBiju(String name) {
        super.setupBiju(name);
        ghast.setMaxHealth(2D * 100D);
        ghast.setHealth(ghast.getMaxHealth());
        ghast.setCustomName(name);
        ghast.setCustomNameVisible(true);
        EntityLiving nmsEntity = ((CraftLivingEntity) ghast).getHandle();
        ((CraftLivingEntity) nmsEntity.getBukkitEntity()).setRemoveWhenFarAway(false);
        setAlive(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (ghast.isDead()) {
                    setAlive(false);
                    cancel();
                }
            }
        }.runTaskTimer(main, 20, 20);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public QuickItem getItem() {
        return new ChomeiItem(main);
    }

    @Override
    public Location getSpawnLocation() {
        return spawn;
    }

    @Override
    public Entity getBijuEntity() {
        return ghast;
    }

}
