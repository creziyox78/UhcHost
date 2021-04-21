package fr.lastril.uhchost.scenario.scenarios;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.events.GameStartEvent;
import fr.lastril.uhchost.player.events.PlayerKillEvent;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.scenario.Scenarios;
import fr.lastril.uhchost.tools.I18n;

public class BestPvE extends Scenario implements Runnable {

	private HashMap<UUID, Long> map;

	public BestPvE() {
		super("BestPvE", Arrays.asList(I18n.tl("scenarios.bestpve.lore", new String[0]),
				I18n.tl("scenarios.bestpve.lore1", ""), I18n.tl("scenarios.bestpve.lore2", new String[0]),
				I18n.tl("scenarios.bestpve.lore3", new String[0]), I18n.tl("scenarios.bestpve.lore4", new String[0])),
				Material.ROTTEN_FLESH);
		this.map = new HashMap<>();
	}

	@EventHandler
	public void onGameStart(GameStartEvent e) {
		for (Player player : e.getPlayers()) {
			this.map.put(player.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
		}
		Bukkit.getScheduler().runTaskTimer((Plugin) UhcHost.getInstance(), (Runnable) this, 20L, 20L);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (!(e.isCancelled()) && e.getEntity() instanceof Player && this.map.containsKey(e.getEntity().getUniqueId())) {
			if(UhcHost.getInstance().getGamemanager().hasScenario(Scenarios.NOFALL.getScenario()))
				if(e.getCause() == DamageCause.FALL)
					return;
			this.map.remove(e.getEntity().getUniqueId());
			e.getEntity().sendMessage(I18n.tl("scenarios.bestpve.removeList", new String[0]));
		}
	}

	@EventHandler
	public void onPlayerKill(PlayerKillEvent e) {
		if (!this.map.containsKey(e.getPlayer().getUniqueId())) {
			this.map.put(e.getPlayer().getUniqueId(), Long.valueOf(System.currentTimeMillis()));
			e.getPlayer().sendMessage(I18n.tl("scenarios.bestpve.addList", new String[0]));
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
				p.sendMessage(I18n.tl("scenarios.bestpve.regainHealth", new String[0]));
				this.map.replace(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
			}
		});
	}

}
