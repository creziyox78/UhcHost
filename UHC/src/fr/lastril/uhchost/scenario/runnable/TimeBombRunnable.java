package fr.lastril.uhchost.scenario.runnable;

import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

public class TimeBombRunnable extends BukkitRunnable {

	private final Chest chest;

	private ArmorStand armorStand;

	private int count;

	public TimeBombRunnable(Chest chest, int count) {
		this.chest = chest;
		this.count = count;
		this.armorStand = (ArmorStand) chest.getWorld().spawnEntity(chest.getLocation(), EntityType.ARMOR_STAND);
		this.armorStand.setVisible(false);
	}

	public void run() {
		if (this.chest.getBlock().isEmpty()) {
			this.armorStand.remove();
			cancel();
			return;
		}
		if (this.count == 0) {
			this.chest.getLocation().getWorld().createExplosion(this.chest.getLocation(), 5.0F);
			this.armorStand.remove();
			cancel();
			return;
		}
		changeName();
		this.count--;
	}

	private void changeName() {
	    this.armorStand = (ArmorStand)this.chest.getWorld().spawnEntity(this.chest.getLocation(), EntityType.ARMOR_STAND);
	    for (Entity entity : this.armorStand.getNearbyEntities(1.0D, 1.0D, 1.0D)) {
	      if (entity != null && entity instanceof ArmorStand)
	        entity.remove(); 
	    } 
	    this.armorStand.setVisible(false);
	    this.armorStand.setGravity(false);
	    this.armorStand.setCustomNameVisible(true);
	    this.armorStand.setCustomName("§e"+ this.count);
	  }

}
