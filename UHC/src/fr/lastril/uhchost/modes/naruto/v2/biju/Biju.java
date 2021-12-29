package fr.lastril.uhchost.modes.naruto.v2.biju;


import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Obito;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Ino;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Biju extends BukkitRunnable implements Listener {

	private final String world = "game";

	private final UhcHost main;
	protected String nameBiju;

	private Biju biju;

	private int firstSpawn;
	
	protected NarutoV2Manager narutoV2Manager;
	private BijuManager bijuManager;


	protected PlayerManager joueurPicked;

	public Biju(){
		this.main = UhcHost.getInstance();
		narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
		this.bijuManager = narutoV2Manager.getBijuManager();
	}
	
	public void sendToStaffSpec(String message){
		for(Player player : Bukkit.getOnlinePlayers()){
			PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
			if(!joueur.isAlive() && player.isOp()){
				player.sendMessage(message);
			}
		}
	}

	protected void setHote(Player player){
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if(narutoV2Manager.getBijuManager().getHotesBiju().get(this.getClass()) == null && !narutoV2Manager.getBijuManager().isAlreadyHote(joueur)){
			narutoV2Manager.getBijuManager().getHotesBiju().put(this.getClass(), main.getPlayerManager(player.getUniqueId()));
			UhcHost.debug(getItem().getName() + " have new hote, is " + player.getName());
			player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous êtes devenu l'hôte de "+ getItem().getName() + ".");
			sendToStaffSpec(Messages.PREFIX_SPEC_STAFF.getMessage() + player.getName() + " est devenue l'hôte de " + getItem().getName() +".");
		}
	}

	public Biju getBiju() {
		return biju;
	}

	public abstract QuickItem getItem();

	public Biju(UhcHost main){
		biju = this;
		this.main = main;
	}

	public void setupBiju(String nameBiju) {
		for(PlayerManager joueur : narutoV2Manager.getPlayerManagersWithRole(Ino.class)){
			if(joueur.getPlayer() != null){
				new BukkitRunnable() {
					@Override
					public void run() {
						joueur.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage()
								+ nameBiju + "§e est en x:" + getSafeSpawnLocation().getX() + " ,y:" + getSafeSpawnLocation().getY() + " ,z:" + getSafeSpawnLocation().getZ() + " .");
					}
				}.runTaskLater(UhcHost.getInstance(), 20*60*2);
			}
		}
		sendToStaffSpec(Messages.PREFIX_SPEC_STAFF.getMessage()
				+ nameBiju + "§e est en x:" + getSafeSpawnLocation().getX() + " ,y:" + getSafeSpawnLocation().getY() + " ,z:" + getSafeSpawnLocation().getZ() + " .");
		for(PlayerManager joueur : narutoV2Manager.getPlayerManagersWithRole(Obito.class)){
			if(joueur.getPlayer() != null){
				new BukkitRunnable() {
					@Override
					public void run() {
						joueur.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage()
								+ nameBiju + "§e est en x:" + getSafeSpawnLocation().getX() + " ,y:" + getSafeSpawnLocation().getY() + " ,z:" + getSafeSpawnLocation().getZ() + " .");
					}
				}.runTaskLater(UhcHost.getInstance(), 10);
			}
		}
	}

	@Deprecated
	public abstract Location getSpawnLocation();

	public Location getSafeSpawnLocation() {
		return new Location(getSpawnLocation().getWorld(), getSpawnLocation().getX(), getSpawnLocation().getWorld().getHighestBlockYAt(getSpawnLocation()) + 5, getSpawnLocation().getZ());
	}

	public boolean itemInInventory(ItemStack item, String nameBiju){
		if(bijuManager.isCraftedJubi())
			return true;
		for(Player player : Bukkit.getOnlinePlayers()){
			if(item != null){
				if(item.getItemMeta().hasDisplayName()){
					if(item.getType() == Material.NETHER_STAR && item.getItemMeta().getDisplayName().equalsIgnoreCase(nameBiju)){
						if(player.getInventory().contains(item)){
							return true;
						}
					}
				}
			}


		}
		return false;
	}

	public String getNameBiju() {
		return nameBiju;
	}

	public abstract Entity getBijuEntity();

	public void setFirstSpawn(int firstSpawn){
		this.firstSpawn = firstSpawn;
	}

	public int getFirstSpawn() {
		return firstSpawn;
	}

	public String getWorld() {
		return world;
	}

	public PlayerManager getJoueurPicked() {
		return joueurPicked;
	}
}
