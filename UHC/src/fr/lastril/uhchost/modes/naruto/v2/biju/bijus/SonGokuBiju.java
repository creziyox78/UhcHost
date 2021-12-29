package fr.lastril.uhchost.modes.naruto.v2.biju.bijus;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.biju.Biju;
import fr.lastril.uhchost.modes.naruto.v2.items.biju.SonGokuItem;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class SonGokuBiju extends Biju {

    private MagmaCube magmaCube;

    private final UhcHost main = UhcHost.getInstance();

    private boolean alive;

    private Location spawn;

    private String nameBiju = "§cSon Gokû";

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    private int timerRespawn = 60*5, distanceLava = 30, distance = 10;

    public SonGokuBiju(){
        super(UhcHost.getInstance());
        timerRespawn = 1;
        main.getServer().getPluginManager().registerEvents(this, main);
        int value = UhcHost.getRANDOM().nextInt(3);
        if(value == 0){
            spawn = new Location(Bukkit.getWorld(super.getWorld()), UhcHost.getRANDOM().nextInt(150,300), 50, UhcHost.getRANDOM().nextInt(150,300));
        } else if(value == 1){
            spawn = new Location(Bukkit.getWorld(super.getWorld()), -UhcHost.getRANDOM().nextInt(150,300), 50, UhcHost.getRANDOM().nextInt(150,300));
        } else if(value == 2){
            spawn = new Location(Bukkit.getWorld(super.getWorld()), UhcHost.getRANDOM().nextInt(150,300), 50, -UhcHost.getRANDOM().nextInt(150,300));
        } else {
            spawn = new Location(Bukkit.getWorld(super.getWorld()), -UhcHost.getRANDOM().nextInt(150,300), 50, -UhcHost.getRANDOM().nextInt(150,300));
        }
    }

    @EventHandler
    public void onBijuDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof MagmaCube){
            MagmaCube magmaCube = (MagmaCube) event.getEntity();
            Player killer = magmaCube.getKiller();
            if(magmaCube.getCustomName() != null && magmaCube.getCustomName().equalsIgnoreCase(nameBiju)){
                event.getDrops().clear();
                if(killer != null && getItem() != null){
                    main.getInventoryUtils().giveItemSafely(killer, getItem().toItemStack());
                    super.setHote(killer.getPlayer());
                }
                setAlive(false);
                Bukkit.broadcastMessage(Messages.NARUTO_PREFIX.getMessage() + nameBiju + " vient d'être vaincu !");
            }
        }
    }

    @EventHandler
    public void pickupItem(PlayerPickupItemEvent event){
        Player player = event.getPlayer();
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(event.getItem().getItemStack().getType() == Material.NETHER_STAR){
            if(event.getItem().getItemStack().isSimilar(this.getItem().toItemStack())){
                sendToStaffSpec(Messages.PREFIX_SPEC_STAFF.getMessage() + player.getName() + " vient de récupérer " + nameBiju +".");
                if(narutoV2Manager.getBijuManager().getHotesBiju().get(this.getClass()) == null && !narutoV2Manager.getBijuManager().isAlreadyHote(joueur)){
                    narutoV2Manager.getBijuManager().getHotesBiju().put(this.getClass(), main.getPlayerManager(event.getPlayer().getUniqueId()));
                    UhcHost.debug(nameBiju + " have new hote, is " + event.getPlayer().getName());
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous êtes devenu l'hôte de "+ nameBiju + ".");
                    sendToStaffSpec(Messages.PREFIX_SPEC_STAFF.getMessage() + player.getName() + " est devenue l'hôte de " + nameBiju +".");
                }
                joueurPicked = joueur;
            }
        }
    }

    @EventHandler
    public void onPlayerBucket(PlayerBucketEmptyEvent event) {
        Block liquid = event.getBlockClicked().getRelative(event.getBlockFace());
        Player player = event.getPlayer();
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(magmaCube != null && !magmaCube.isDead()){
            if(event.getBucket() == Material.LAVA_BUCKET && liquid.getLocation().distance(magmaCube.getLocation()) <= distanceLava) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDeathSlime(SlimeSplitEvent event){
        event.setCancelled(true);
    }


    @Override
    public void run() {
        if(timerRespawn == 30){
            Bukkit.broadcastMessage(Messages.NARUTO_PREFIX.getMessage() + nameBiju +" va bientôt apparaître.");
        }
        if(timerRespawn == 0){
            Bukkit.broadcastMessage(Messages.NARUTO_PREFIX.getMessage() + nameBiju+" vient d'apparaître.");
            magmaCube = getSafeSpawnLocation().getWorld().spawn(getSafeSpawnLocation(), MagmaCube.class);
            setupBiju(nameBiju);
            magmaCube.getLocation().getChunk().load(true);
        }
        if(isAlive() || narutoV2Manager.getBijuManager().getHotesBiju().get(this.getClass()) != null
                || itemInInventory(getItem().toItemStack(), nameBiju)){
            timerRespawn = 60*5;
        }
        if(magmaCube != null){
            if(!magmaCube.isDead()){
                Location loc = magmaCube.getLocation();
                if(loc.distance(getSafeSpawnLocation()) >= 50){
                    magmaCube.teleport(getSafeSpawnLocation());
                }
            }
        }

        timerRespawn--;
    }

    @Override
    public void setupBiju(String name) {
        super.setupBiju(name);
        magmaCube.setMaxHealth(2D*100D);
        magmaCube.setHealth(magmaCube.getMaxHealth());
        magmaCube.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
        magmaCube.setCustomName(name);
        magmaCube.setCustomNameVisible(true);
        magmaCube.setSize(7);
        EntityLiving nmsEntity = ((CraftLivingEntity) magmaCube).getHandle();
        ((CraftLivingEntity) nmsEntity.getBukkitEntity()).setRemoveWhenFarAway(false);
        setAlive(true);

        new BukkitRunnable() {
            int timeAttack = 10;
            @Override
            public void run() {
                if(!magmaCube.isDead()) {
                    if(timeAttack == 0) {
                        Player target = null;
                        for(Entity entity : magmaCube.getNearbyEntities(distance, distance, distance)) {
                            if(entity instanceof Player) {
                                Player player = (Player) entity;
                                PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
                                if(joueur.isAlive() && player.getGameMode() != GameMode.SPECTATOR){
                                    target = player;
                                }
                            }
                        }
                        if(target != null){
                            target.getLocation().add(0, 1, 0).getBlock().setType(Material.LAVA);
                            UhcHost.debug(name + " use power against " + target.getName());
                        }
                        timeAttack = 10;
                    }
                } else {
                    setAlive(false);
                    UhcHost.debug(name + " cancelled attack task.");
                    UhcHost.debug(name + " has been defeated.");
                    cancel();
                }
                timeAttack--;

            }
        }.runTaskTimer(main, 20, 20);
    }

    @EventHandler
    public void onDeathHote(PlayerDeathEvent event){
        Player player = event.getEntity();
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(narutoV2Manager.getBijuManager().getHotesBiju().get(this.getClass()) != null &&
                narutoV2Manager.getBijuManager().getHotesBiju().get(this.getClass()) == joueur){
            narutoV2Manager.getBijuManager().getHotesBiju().put(this.getClass(), null);
            UhcHost.debug(nameBiju + " owner is dead.");
        }
    }

    @Override
    public QuickItem getItem() {
        return new SonGokuItem(main);
    }

    @Override
    public Location getSpawnLocation() {
        return spawn;
    }

    @Override
    public Entity getBijuEntity() {
        return magmaCube;
    }

}
