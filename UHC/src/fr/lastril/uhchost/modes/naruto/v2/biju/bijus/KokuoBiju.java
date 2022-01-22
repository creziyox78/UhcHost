package fr.lastril.uhchost.modes.naruto.v2.biju.bijus;


import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.biju.Biju;
import fr.lastril.uhchost.modes.naruto.v2.items.biju.KokuoItem;
import fr.lastril.uhchost.modes.naruto.v2.pathfinder.kokuo.KokuoInvoker;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class KokuoBiju extends Biju {

    private Horse horse;

    private final UhcHost main = UhcHost.getInstance();

    private boolean alive;

    private Location spawn;

    private String nameBiju = "§9Kokuô";

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    private int timerRespawn = 60*5, distance = 10;

    public KokuoBiju() {
        timerRespawn = 1;
        main.getServer().getPluginManager().registerEvents(this, main);
        int value = UhcHost.getRANDOM().nextInt(4);
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
        if(event.getEntity() instanceof Horse){
            Horse horse = (Horse) event.getEntity();
            Player killer = horse.getKiller();
            if(horse.getCustomName() != null && horse.getCustomName().equalsIgnoreCase(nameBiju)){
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
    public void onDamageBiju(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Horse && event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            Horse horse = (Horse) event.getDamager();
            if(horse.getCustomName() != null && horse.getCustomName().equalsIgnoreCase(nameBiju)){
                int value = UhcHost.getRANDOM().nextInt(100);
                if(value <= 25){
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*8, 0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*8, 3));
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + nameBiju +" vous a touché.");
                    UhcHost.debug(nameBiju + " use power against " + player.getName());
                }
            }
        }
    }

    @Override
    public void run() {
        if(timerRespawn == 30){
            Bukkit.broadcastMessage(Messages.NARUTO_PREFIX.getMessage() + nameBiju +" va bientôt apparaître.");
        }
        if(timerRespawn == 0){
            Bukkit.broadcastMessage(Messages.NARUTO_PREFIX.getMessage() + nameBiju+" vient d'apparaître.");
            horse = KokuoInvoker.invokeKokuo(horse, getSafeSpawnLocation(), nameBiju);
            setupBiju(nameBiju);
        }
        if(isAlive() || narutoV2Manager.getBijuManager()
                .getHotesBiju()
                .get
                        (this.getClass()) != null
                || itemInInventory(new ItemStack(Material.NETHER_STAR), nameBiju)){
            timerRespawn = 60*5;
        }
        if(horse != null){
            if(!horse.isDead()){
                Location loc = horse.getLocation();
                if(loc.distance(getSafeSpawnLocation()) >= 50){
                    horse.teleport(getSafeSpawnLocation());
                }
            }
        }

        timerRespawn--;
    }

    @Override
    public void setupBiju(String name) {
        super.setupBiju(name);
        setAlive(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(horse.isDead()) {
                    setAlive(false);
                    UhcHost.debug(nameBiju + " cancelled attack task.");
                    UhcHost.debug(nameBiju + " has been defeated.");
                    cancel();
                }
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
        return new KokuoItem(main);
    }

    @Override
    public Location getSpawnLocation() {
        return spawn;
    }

    @Override
    public Entity getBijuEntity() {
        return horse;
    }

}
