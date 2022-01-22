package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.game.tasks.TaskManager;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.TimerTask;

public class EntitySpawn implements Listener {

	public UhcHost main;

	private EntityType[] blacklist = {
			EntityType.CREEPER,
			EntityType.SKELETON,
			EntityType.ZOMBIE,
			EntityType.WITCH,
	};

	public EntitySpawn(UhcHost main) {
		this.main = main;
	}
	
	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event) {
		if(!GameState.isState(GameState.STARTED)){
			event.setCancelled(true);
		} else {
			if(TaskManager.timeGame >= 60*60){
				for (EntityType entityType : blacklist) {
					if (event.getEntityType() == entityType) {
						event.setCancelled(true);
					}
				}
			}
		}

	}
}
