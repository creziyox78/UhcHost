package fr.lastril.uhchost.modes.naruto.v2.biju.bijus;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.biju.Biju;
import fr.lastril.uhchost.modes.naruto.v2.items.biju.IsobuItem;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class IsobuBiju extends Biju {

    private final UhcHost main = UhcHost.getInstance();
    private final Location spawn;
    private final String nameBiju = "§eIsobu";
    private final int distance = 15;
    private Guardian guardian;
    private boolean alive;
    private int timerRespawn = 60 * 5;

    public IsobuBiju() {
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

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @EventHandler
    public void onBijuDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Guardian) {
            Guardian guardian = (Guardian) event.getEntity();
            Player killer = guardian.getKiller();
            if (guardian.getCustomName() != null && guardian.getCustomName().equalsIgnoreCase(nameBiju)) {
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
    public void onDamageBiju(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Guardian) {
            Guardian guardian = (Guardian) event.getEntity();
            if (guardian.getCustomName() != null && guardian.getCustomName().equalsIgnoreCase(nameBiju)) {
                int value = UhcHost.getRANDOM().nextInt(100);
                if (value <= 5) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void pickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (event.getItem().getItemStack().getType() == Material.NETHER_STAR) {
            if (event.getItem().getItemStack().isSimilar(this.getItem().toItemStack())) {
                sendToStaffSpec(Messages.PREFIX_SPEC_STAFF.getMessage() + player.getName() + " vient de récupérer " + nameBiju + ".");
                if (main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(this.getClass()) == null && !main.getNarutoV2Manager().getBijuManager().isAlreadyHote(playerManager)) {
                    main.getNarutoV2Manager().getBijuManager().getHotesBiju().put(this.getClass(), main.getPlayerManager(event.getPlayer().getUniqueId()));
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous êtes devenu l'hôte de " + nameBiju + ".");
                    sendToStaffSpec(Messages.PREFIX_SPEC_STAFF.getMessage() + player.getName() + " est devenue l'hôte de " + nameBiju + ".");
                }
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

    @Override
    public void run() {
        if (timerRespawn == 30) {
            Bukkit.broadcastMessage(Messages.NARUTO_PREFIX.getMessage() + nameBiju + " va bientôt apparaître.");
        }
        if (timerRespawn == 0) {
            Bukkit.broadcastMessage(Messages.NARUTO_PREFIX.getMessage() + nameBiju + " vient d'apparaître.");
            guardian = getSafeSpawnLocation().getWorld().spawn(getSafeSpawnLocation(), Guardian.class);
            setupBiju(nameBiju);
            guardian.getLocation().getChunk().load(true);
        }
        if (isAlive() || main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(this.getClass()) != null || itemInInventory(getItem().toItemStack())) {
            timerRespawn = 60 * 5;
        }
        if (guardian != null) {
            if (!guardian.isDead()) {
                Location loc = guardian.getLocation();
                if (loc.distance(getSafeSpawnLocation()) >= 50) {
                    guardian.teleport(getSafeSpawnLocation());
                }
            }
        }
        timerRespawn--;
    }

    @Override
    public void setupBiju(String name) {
        super.setupBiju(name);
        guardian.setMaxHealth(2D * 100D);
        guardian.setHealth(guardian.getMaxHealth());
        guardian.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
        guardian.setCustomName(name);
        guardian.setCustomNameVisible(true);
        guardian.setElder(true);
        EntityLiving nmsEntity = ((CraftLivingEntity) guardian).getHandle();
        ((CraftLivingEntity) nmsEntity.getBukkitEntity()).setRemoveWhenFarAway(false);
        setAlive(true);

        new BukkitRunnable() {
            int timeAttack = 10;

            @Override
            public void run() {
                if (!guardian.isDead()) {
                    if (timeAttack == 0) {
                        for (Entity entity : guardian.getNearbyEntities(distance, distance, distance)) {
                            if (entity instanceof Player) {
                                Player player = (Player) entity;
                                Guardian sbire = player.getWorld().spawn(player.getLocation(), Guardian.class);
                                sbire.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
                                sbire.setHealth(10D);
                            }
                        }
                        timeAttack = 10;
                    }
                } else {
                    setAlive(false);
                    cancel();
                }
                timeAttack--;

            }
        }.runTaskTimer(main, 20, 20);
    }

    @Override
    public QuickItem getItem() {
        return new IsobuItem(main);
    }

    @Override
    public Location getSpawnLocation() {
        return spawn;
    }

    @Override
    public Entity getBijuEntity() {
        return guardian;
    }

}
