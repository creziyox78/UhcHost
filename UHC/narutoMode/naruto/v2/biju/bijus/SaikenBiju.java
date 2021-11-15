package fr.lastril.uhchost.modes.naruto.v2.biju.bijus;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.biju.Biju;
import fr.lastril.uhchost.modes.naruto.v2.items.biju.SaikenItem;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class SaikenBiju extends Biju {

    private final UhcHost main = UhcHost.getInstance();
    private final Location spawn;
    private final String nameBiju = "§5Saiken";
    private final int distance = 15;
    private Slime slime;
    private boolean alive;
    private int timerRespawn = 60 * 5;

    public SaikenBiju() {
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
        if (event.getEntity() instanceof Slime) {
            Slime slime = (Slime) event.getEntity();
            Player killer = slime.getKiller();
            if (slime.getCustomName() != null && slime.getCustomName().equalsIgnoreCase(nameBiju)) {
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
    public void onDeathSlime(SlimeSplitEvent event) {
        event.setCancelled(true);
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
            slime = getSafeSpawnLocation().getWorld().spawn(getSafeSpawnLocation(), Slime.class);
            setupBiju(nameBiju);
            slime.getLocation().getChunk().load(true);
        }
        if (isAlive() || main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(this.getClass()) != null || itemInInventory(getItem().toItemStack())) {
            timerRespawn = 60 * 5;
        }
        if (slime != null) {
            if (!slime.isDead()) {
                Location loc = slime.getLocation();
                if (loc.distance(getSafeSpawnLocation()) >= 50) {
                    slime.teleport(getSafeSpawnLocation());
                }
            }
        }

        timerRespawn--;
    }

    @EventHandler
    public void onDamageBiju(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Slime && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Slime slime = (Slime) event.getDamager();
            if (slime.getCustomName() != null && slime.getCustomName().equalsIgnoreCase(nameBiju)) {
                int value = UhcHost.getRANDOM().nextInt(100);
                if (value <= 25) {
                    if (slime.getHealth() + 2D >= slime.getHealth()) {
                        slime.setHealth(slime.getHealth());
                    } else {
                        slime.setHealth(slime.getHealth() + 2D);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2, 0));
                    player.getWorld().createExplosion(player.getLocation(), 1.0f);
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + nameBiju + " vous a touché.");
                }
            }
        }
    }

    @Override
    public void setupBiju(String name) {
        super.setupBiju(name);
        slime.setMaxHealth(2D * 100D);
        slime.setHealth(slime.getMaxHealth());
        slime.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
        slime.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
        slime.setCustomName(nameBiju);
        slime.setSize(8);
        slime.setCustomNameVisible(true);
        EntityLiving nmsEntity = ((CraftLivingEntity) slime).getHandle();
        ((CraftLivingEntity) nmsEntity.getBukkitEntity()).setRemoveWhenFarAway(false);
        setAlive(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (slime.isDead()) {
                    setAlive(false);
                    cancel();
                }
            }
        }.runTaskTimer(main, 20, 20);
    }


    @Override
    public QuickItem getItem() {
        return new SaikenItem(main);
    }

    @Override
    public Location getSpawnLocation() {
        return spawn;
    }

    @Override
    public Entity getBijuEntity() {
        return slime;
    }

}
