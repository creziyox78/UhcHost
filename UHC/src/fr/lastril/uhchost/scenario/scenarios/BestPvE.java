package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.events.GameStartEvent;
import fr.lastril.uhchost.player.events.PlayerKillEvent;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.scenario.Scenarios;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class BestPvE extends Scenario implements Runnable {

	private final HashMap<UUID, Long> map;

	public BestPvE() {
		super("BestPvE", Arrays.asList(I18n.tl("scenarios.bestpve.lore"),
				I18n.tl("scenarios.bestpve.lore1", ""), I18n.tl("scenarios.bestpve.lore2"),
				I18n.tl("scenarios.bestpve.lore3"), I18n.tl("scenarios.bestpve.lore4")),
				Material.ROTTEN_FLESH);
		this.map = new HashMap<>();
	}

	@EventHandler
	public void onGameStart(GameStartEvent e) {
		for (Player player : e.getPlayers()) {
			this.map.put(player.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
		}
		Bukkit.getScheduler().runTaskTimer(UhcHost.getInstance(), this, 20L, 20L);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (!(e.isCancelled()) && e.getEntity() instanceof Player && this.map.containsKey(e.getEntity().getUniqueId())) {
			if(UhcHost.getInstance().getGamemanager().hasScenario(Scenarios.NOFALL.getScenario()))
				if(e.getCause() == DamageCause.FALL)
					return;
			this.map.remove(e.getEntity().getUniqueId());
			e.getEntity().sendMessage(I18n.tl("scenarios.bestpve.removeList"));
		}
	}

	@EventHandler
	public void onPlayerKill(PlayerKillEvent e) {
		if (!this.map.containsKey(e.getKiller().getUniqueId())) {
			this.map.put(e.getKiller().getUniqueId(), Long.valueOf(System.currentTimeMillis()));
			e.getKiller().sendMessage(I18n.tl("scenarios.bestpve.addList"));
		}
	}

	@Override
	public void run() {
		if (this.map == null)
			return;
		this.map.forEach((k, v) -> {
			if (System.currentTimeMillis() / 1000L - 600L == v.longValue() / 1000L) {
				Player p = Bukkit.getPlayer(k);
				if (p.getHealth() + 2.0D > p.getMaxHealth()) {
					p.setMaxHealth(p.getMaxHealth() + 2.0D);
					p.setHealth(p.getMaxHealth());
				} else {
					p.setHealth(p.getHealth() + 2.0D);
				}
				p.sendMessage(I18n.tl("scenarios.bestpve.regainHealth"));
				this.map.replace(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
			}
		});
	}

}
