package fr.lastril.uhchost.modes.naruto.v2.biju.bijus;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.biju.Biju;
import fr.lastril.uhchost.modes.naruto.v2.items.biju.MatatabiItem;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class MatatabiBiju extends Biju {

	private Blaze blaze;
	
	private final UhcHost main = UhcHost.getInstance();
	
	private int timerRespawn = 60, distance = 15;

	private String nameBiju = "§6Matatabi";

	private Location spawn;

	private boolean alive;

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public MatatabiBiju() {
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
		if(event.getEntity() instanceof Blaze){
			Blaze blaze = (Blaze) event.getEntity();
			Player killer = blaze.getKiller();
			if(blaze.getCustomName() != null && blaze.getCustomName().equalsIgnoreCase(nameBiju)){
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
	public void run() {
		if(timerRespawn == 30){
			Bukkit.broadcastMessage(Messages.NARUTO_PREFIX.getMessage() + nameBiju +" va bientôt apparaître.");
		}
		if(timerRespawn == 0){
			Bukkit.broadcastMessage(Messages.NARUTO_PREFIX.getMessage() + nameBiju+" vient d'apparaître.");
			blaze = getSafeSpawnLocation().getWorld().spawn(getSafeSpawnLocation(), Blaze.class);
			setupBiju(nameBiju);
			blaze.getLocation().getChunk().load(true);
		}
		if(isAlive() || narutoV2Manager.getBijuManager().getHotesBiju().get(this.getClass()) != null
				|| itemInInventory(getItem().toItemStack(), nameBiju)){
			timerRespawn = 60*5;
		}
		if(blaze != null){
			if(!blaze.isDead()){
				Location loc = blaze.getLocation();
				if(loc.distance(getSafeSpawnLocation()) >= 50){
					blaze.teleport(getSafeSpawnLocation());
				}
			}
		}
		timerRespawn--;
	}


	@Override
	public void setupBiju(String name) {
		super.setupBiju(name);
		blaze.setMaxHealth(2D*100D);
		blaze.setHealth(blaze.getMaxHealth());
		blaze.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
		blaze.setCustomName(name);
		blaze.setCustomNameVisible(true);
		EntityLiving nmsEntity = ((CraftLivingEntity) blaze).getHandle();
		((CraftLivingEntity) nmsEntity.getBukkitEntity()).setRemoveWhenFarAway(false);
		setAlive(true);
		
		
		new BukkitRunnable() {
			int timeAttack = 60;
			@Override
			public void run() {
				if(!blaze.isDead()) {
					if(timeAttack == 0) {
						for(Entity entity : blaze.getNearbyEntities(distance, distance, distance)) {
							if(entity instanceof Player) {
								Player player = (Player) entity;
								player.setFireTicks(20*16);
								player.sendMessage(name+" vient de vous mettre en feu.");
								UhcHost.debug(name + " use power against " + player.getName());
							}
						}
						timeAttack = 60;
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

	@Override
	public Location getSpawnLocation() {
		return spawn;
	}

	@Override
	public Entity getBijuEntity() {
		return blaze;
	}



	@Override
	public QuickItem getItem() {
		return new MatatabiItem(main);
	}

}
