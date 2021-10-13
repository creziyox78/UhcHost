package fr.lastril.uhchost.game.tasks;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.WorldState;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.roles.RoleAnnounceMode;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.PvpEnableEvent;
import fr.lastril.uhchost.scenario.Scenarios;
import fr.lastril.uhchost.tools.API.TitleAPI;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.stream.Collectors;

public class TaskManager {

	private final UhcHost pl;

	private boolean lobby = false;

	private boolean preGame = false;

	private boolean game = false;

	private int count;

	private int pvpTime = 1200, borderTime = 3600, netherEndTime = 2400,
			teleportTime = 3600;

	public int getCount() {
		return this.count;
	}

	public int getPvpTime() {
		return this.pvpTime;
	}

	public int getBorderTime() {
		return this.borderTime;
	}

	public int getNetherEndTime() {
		return this.netherEndTime;
	}

	public int getTeleportTime() {
		return this.teleportTime;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setPvpTime(int pvpTime) {
		this.pvpTime = pvpTime;
	}

	public void setBorderTime(int borderTime) {
		this.borderTime = borderTime;
	}

	public void setNetherEndTime(int netherEndTime) {
		this.netherEndTime = netherEndTime;
	}

	public void setTeleportTime(int teleportTime) {
		this.teleportTime = teleportTime;
	}

	private int _x = 0;

	private int _y = 0;

	private int _z = 0;

	private BukkitTask lobbyTask;

	private Mode mode;

	public TaskManager(UhcHost pl) {
		this.pl = pl;
	}

	public void lobbyTask() {
		if (this.lobby)
			return;
		this.lobby = true;
		this.count = 60;
		this.lobbyTask = (new BukkitRunnable() {
			public void run() {
				Bukkit.getOnlinePlayers()
						.forEach(o -> TaskManager.this.pl.scoreboardUtil.updateLobby(o, TaskManager.this.count));
				if (GameState.getCurrentState() != GameState.LOBBY
						|| Bukkit.getOnlinePlayers().size() < TaskManager.this.pl.gameManager.getPlayersBeforeStart()) {
					TaskManager.this.count = 60;
					return;
				}
				if ((TaskManager.this.count == 60 || TaskManager.this.count == 30 || TaskManager.this.count == 20
						|| TaskManager.this.count <= 10) && TaskManager.this.count != 0)
					if (TaskManager.this.count == 0) {
						TaskManager.this.pl.gameManager.preStart();
						cancel();
					}
				TaskManager.this.count--;
			}
		}).runTaskTimer(this.pl, 20L, 20L);
	}

	public void preGame() {
		if (this.preGame)
			return;
		this.lobbyTask.cancel();
		this.preGame = true;
		this.count = 15;
		new BukkitRunnable() {
			public void run() {
				if (TaskManager.this.count == 15 || (TaskManager.this.count <= 10 && TaskManager.this.count != 0)) {
					for (Player player : Bukkit.getOnlinePlayers()) {
						TitleAPI.sendTitle(player, Integer.valueOf(5), Integer.valueOf(20), Integer.valueOf(5),
								"§b" + TaskManager.this.count, "");
						player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
					}
				}

				if (TaskManager.this.count == 0) {
					TaskManager.this.pl.gameManager.start();
					cancel();
				}
				for (Player player : Bukkit.getOnlinePlayers())
					TaskManager.this.pl.scoreboardUtil.updatePreGame(player, TaskManager.this.count);
				TaskManager.this.count--;
			}
		}.runTaskTimer(this.pl, 20L, 20L);
	}

	public void game() {
		if (this.game)
			return;
		this.game = true;
		this.count = 0;
		new BukkitRunnable() {
			public void run() {
				long time = Bukkit.getWorld("world").getTime();

				if (time >= 13000) {
					if (pl.getGamemanager().getWorldState() == WorldState.DAY) {
						pl.getGamemanager().setWorldState(WorldState.NIGHT);
						for (PlayerManager joueur : pl.getAllPlayerManager().values()) {
							if (joueur.getPlayer() != null) {
								Player player = joueur.getPlayer();
								if (joueur.getRole() != null) {
									joueur.getRole().night(player);
								}
							}
						}
					}
				} else {
					if (pl.getGamemanager().getWorldState() == WorldState.NIGHT) {
						pl.getGamemanager().setWorldState(WorldState.DAY);
						for (PlayerManager joueur : pl.getAllPlayerManager().values()) {
							if (joueur.getPlayer() != null) {
								Player player = joueur.getPlayer();
								if (joueur.getRole() != null) {
									joueur.getRole().day(player);
									joueur.getRole().onNewDay(player);
								}
							}
						}
					}
				}
				TaskManager.this.count++;
				TaskManager.this.pl.gameManager.getModes().getMode().tick(count);
				if(TaskManager.this.pl.gameManager.getModes().getMode() instanceof RoleAnnounceMode){
					RoleAnnounceMode roleAnnounceMode = (RoleAnnounceMode) TaskManager.this.pl.gameManager.getModes().getMode();
					roleAnnounceMode.setRoleAnnouncement(roleAnnounceMode.getRoleAnnouncement() - 1);
				}

				if (TaskManager.this.count == 60) {
					Bukkit.broadcastMessage(I18n.tl("damageActived"));
					TaskManager.this.pl.gameManager.setDamage(true);
				}
				if (TaskManager.this.pl.gameManager.isNether()
						&& !TaskManager.this.pl.gameManager.hasScenario(Scenarios.GOTOHELL.getScenario())) {
					if (TaskManager.this.count == TaskManager.this.netherEndTime - 60
							|| TaskManager.this.count == TaskManager.this.netherEndTime - 120
							|| TaskManager.this.count == TaskManager.this.netherEndTime - 180
							|| TaskManager.this.count == TaskManager.this.netherEndTime - 240
							|| TaskManager.this.count == TaskManager.this.netherEndTime - 300
							|| TaskManager.this.count == TaskManager.this.netherEndTime - 600)
						if (TaskManager.this.count == TaskManager.this.netherEndTime) {
							Bukkit.broadcastMessage(I18n.tl("netherEnd"));
							TaskManager.this.pl.gameManager.setNether(false);
						}
				}
				if (TaskManager.this.pl.gameManager.isFightTeleport()) {
					if (TaskManager.this.count == TaskManager.this.teleportTime - 60
							|| TaskManager.this.count == TaskManager.this.teleportTime - 120
							|| TaskManager.this.count == TaskManager.this.teleportTime - 180
							|| TaskManager.this.count == TaskManager.this.teleportTime - 240
							|| TaskManager.this.count == TaskManager.this.teleportTime - 300
							|| TaskManager.this.count == TaskManager.this.teleportTime - 600)
						if (TaskManager.this.count == TaskManager.this.teleportTime) {
							Bukkit.broadcastMessage(I18n.tl("teleport"));
							TaskManager.this.pl.gameManager.reTeleport();
							TaskManager.this.pl.gameManager.setFightTeleport(false);
						}
				}
				if (TaskManager.this.count == TaskManager.this.pvpTime) {
					Bukkit.broadcastMessage(I18n.tl("pvpActived"));
					TaskManager.this.pl.gameManager.setPvp(true);
					Bukkit.getPluginManager()
							.callEvent(new PvpEnableEvent(pl.getPlayerManagerAlives()));
				}
				if (TaskManager.this.count == TaskManager.this.borderTime) {
					Bukkit.broadcastMessage(I18n.tl("borderStart"));
					TaskManager.this.pl.worldBorderUtils.change(
							(int) TaskManager.this.pl.gameManager.getFinalBorderSize(),
							((TaskManager.this.pl.worldBorderUtils.getStartSize()
									- TaskManager.this.pl.worldBorderUtils.getFinalSize())
									/ TaskManager.this.pl.worldBorderUtils.getSpeed()));
					TaskManager.this.pl.gameManager.setBorder(true);
				}
				for (Player player : Bukkit.getOnlinePlayers())
					TaskManager.this.pl.scoreboardUtil.updateGame(player, TaskManager.this.count);
			}
		}.runTaskTimer(this.pl, 20L, 20L);
	}

}
