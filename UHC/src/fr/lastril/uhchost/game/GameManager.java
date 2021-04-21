package fr.lastril.uhchost.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.GameStartEvent;
import fr.lastril.uhchost.player.events.TeamUnregisteredEvent;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.scenario.Scenarios;
import fr.lastril.uhchost.scoreboard.TeamUtils;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.API.BungeeAPI;
import fr.lastril.uhchost.tools.API.TitleAPI;
import fr.lastril.uhchost.tools.inventory.CustomInv;

public class GameManager {
	private boolean border;

	private boolean pvp;

	private boolean gameIsEnding;

	private boolean damage;

	private boolean editInv;

	private String winner;

	private int slot;

	private int playersBeforeStart = 10;

	private Map<UUID, UUID> lastDamager;

	private long elapsedTime;

	private long pvpTime;

	private long borderSize;

	private long finalBorderSize;

	private GameState gameState;

	private PlayerManager playerManager;

	private String hostName;

	private String gameName;

	private Player host;

	private UhcHost pl;

	private static GameManager gameManager;

	private List<Scenario> scenarios;

	private boolean nether;

	private List<UUID> players;

	public Location spawn;

	private int maxPlayers = 50;

	private Map<UUID, Location> teleportations;

	private boolean fightTeleport;

	private boolean viewHealth;

	public boolean isViewHealth() {
		return viewHealth;
	}

	public void setViewHealth(boolean viewHealth) {
		this.viewHealth = viewHealth;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public boolean isFightTeleport() {
		return fightTeleport;
	}

	public void setFightTeleport(boolean b) {
		fightTeleport = b;
	}

	public UUID getLastDamager(Player player) {
		if (!this.lastDamager.containsKey(player.getUniqueId()))
			this.lastDamager.put(player.getUniqueId(), null);
		return this.lastDamager.get(player.getUniqueId());
	}

	public void setLastDamager(Player player, Player damager) {
		this.lastDamager.replace(player.getUniqueId(), damager.getUniqueId());
	}

	public GameManager(UhcHost pl) {
		this.pl = pl;
		this.temp = new ArrayList<>();
		this.pvpTime = 60000L;
		this.pvp = false;
		this.fightTeleport = false;
		this.viewHealth = true;
		this.borderSize = 2000L;
		this.finalBorderSize = 200L;
		this.damage = false;
		this.gameIsEnding = false;
		this.border = false;
		this.players = new ArrayList<>();
		this.scenarios = new ArrayList<>();
		this.setGameState(GameState.LOBBY);
		this.gameName = ChatColor.AQUA + "UHC Host";
		this.setNether(true);
		this.lastDamager = new HashMap<>();
		this.spawn = new Location(Bukkit.getWorld(pl.getConfig().getString("world_lobby")), 200, 200, 200);
	}

	public void addScenario(Scenario scenario) {
		if (!this.scenarios.contains(scenario)) {
			this.scenarios.add(scenario);
		}
	}

	public void removeScenario(Scenario scenario) {
		if (this.scenarios.contains(scenario)) {
			this.scenarios.remove(scenario);
		}
	}

	public boolean hasScenario(Scenario scenario) {
		return this.scenarios.contains(scenario);
	}

	public void addPlayer(Player player) {
		if (!this.players.contains(player.getUniqueId()))
			this.players.add(player.getUniqueId());
	}

	public void removePlayer(Player player, boolean dead) {
		if (this.players.contains(player.getUniqueId()))
			this.players.remove(player.getUniqueId());
		if (this.pl.teamUtils.getTeams(player) != null) {
			TeamUtils.Teams t = this.pl.teamUtils.getTeams(player);
			t.getTeam().removeEntry(player.getName());
			if (t.getTeam().getSize() == 0 && GameState.isState(GameState.STARTED)) {
				Bukkit.broadcastMessage(I18n.tl("teamDead", t.getTeam().getDisplayName()));
				t.getTeam().unregister();
				Bukkit.getPluginManager().callEvent((Event) new TeamUnregisteredEvent(t.getTeam()));
			}
		}
		checkWin();
	}

	public void removePlayer(Player player) {
		if (this.players.contains(player.getUniqueId()))
			this.players.remove(player.getUniqueId());
	}

	public List<UUID> getPlayers() {
		return players;
	}

	public List<Scenario> getScenarios() {
		return scenarios;
	}

	public void setScenarios(List<Scenario> scenarios) {
		this.scenarios = scenarios;
	}

	public GameState getGameState() {
		return this.gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public UhcHost getUhcHost() {
		return this.pl;
	}

	public String getHostname() {
		return this.hostName;
	}

	public void setHostname(String hostname) {
		this.hostName = hostname;
	}

	public int getSlot() {
		return this.slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public boolean isPvp() {
		return this.pvp;
	}

	public void setPvp(boolean pvp) {
		this.pvp = pvp;
	}

	public boolean isBorder() {
		return this.border;
	}

	public void setBorder(boolean border) {
		this.border = border;
	}

	public boolean isGameIsEnding() {
		return this.gameIsEnding;
	}

	public void setGameIsEnding(boolean gameIsEnding) {
		this.gameIsEnding = gameIsEnding;
	}

	public long getElapsedTime() {
		return this.elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public static GameManager getGameManager() {
		return gameManager;
	}

	public PlayerManager getPlayerManager() {
		return this.playerManager;
	}

	public Player getHost() {
		return this.host;
	}

	public void setHost(Player host) {
		this.host = host;
	}

	public long getPvPTime() {
		return this.pvpTime;
	}

	public void setPvPTime(long pvptime) {
		this.pvpTime = pvptime;
	}

	public long getFinalBorderSize() {
		return this.finalBorderSize;
	}

	public void setFinalBorderSize(long finalBorderSize) {
		this.finalBorderSize = finalBorderSize;
	}

	public boolean isDamage() {
		return this.damage;
	}

	public void setDamage(boolean damage) {
		this.damage = damage;
	}

	public long getBordersize() {
		return this.borderSize;
	}

	public void setBordersize(long bordersize) {
		this.borderSize = bordersize;
	}

	public boolean isEditInv() {
		return this.editInv;
	}

	public void setEditInv(boolean editInv) {
		this.editInv = editInv;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public boolean isNether() {
		return nether;
	}

	public void setNether(boolean nether) {
		this.nether = nether;
	}

	public int getPlayersBeforeStart() {
		return playersBeforeStart;
	}

	public void setPlayersBeforeStart(int playersBeforeStart) {
		this.playersBeforeStart = playersBeforeStart;
	}

	private int count;

	private BukkitTask task;

	public void preStart() {
		GameState.setCurrentState(GameState.PRESTART);
		this.pl.worldUtils.getWorld().setTime(0L);
		this.pl.worldBorderUtils.change(this.pl.worldBorderUtils.getStartSize());
		if (this.pl.teamUtils.getPlayersPerTeams() != 1) {
			for (UUID player : this.players)
				this.pl.teamUtils.setAutoTeam(Bukkit.getPlayer(player));
			for (TeamUtils.Teams teams : TeamUtils.Teams.values()) {
				if (teams.getTeam() != null && !teams.getTeam().getEntries().isEmpty())
					this.count++;
			}
			this.task = Bukkit.getScheduler().runTaskTimer((Plugin) this.pl, new Runnable() {

				List<Location> locs = GameManager.this.generateLocations(GameManager.this.count);

				@Override
				public void run() {
					if (GameManager.this.count >= this.locs.size()) {
						System.out.println("locs.size() = " + this.locs.size());
						for (TeamUtils.Teams teams : TeamUtils.Teams.values()) {
							if (teams != null && !teams.getTeam().getEntries().isEmpty()) {
								Location loc = this.locs.stream().findAny().get();
								teams.getTeam().getEntries().forEach(s -> {
									Player p = Bukkit.getPlayer(s);
									p.teleport(loc.clone().add(0.5D, 1.0D, 0.5D));
									if (GameManager.this.fightTeleport)
										GameManager.this.teleportations.put(p.getUniqueId(),
												loc.clone().add(0.5D, 1.0D, 0.5D));
								});
								this.locs.remove(loc);
							}
						}
						GameManager.this.pl.taskManager.preGame();
						GameManager.this.task.cancel();
						return;
					}
					((Location) this.locs.get(GameManager.this.count)).getChunk().load(true);
					GameManager.this.count++;
				}
			}, 10L, 10L);
		} else {
			this.task = Bukkit.getScheduler().runTaskTimer((Plugin) this.pl, new Runnable() {
				List<Location> locs = GameManager.this.generateLocations(GameManager.this.players.size());

				@Override
				public void run() {
					if (GameManager.this.count == this.locs.size()) {
						for (UUID uuid : GameManager.this.players) {
							Player p = Bukkit.getPlayer(uuid);
							Location loc = this.locs.stream().findAny().get();
							p.teleport(loc.clone().add(0.5D, 1.0D, 0.5D));
							if (GameManager.this.fightTeleport)
								GameManager.this.teleportations.put(p.getUniqueId(), loc.clone().add(0.5D, 1.0D, 0.5D));
							this.locs.remove(loc);
						}
						GameManager.this.pl.taskManager.preGame();
						GameManager.this.task.cancel();
						return;
					}
					((Location) this.locs.get(GameManager.this.count)).getChunk().load(true);
					GameManager.this.count++;
				}
			}, 10L, 10L);
		}
	}

	private List<Location> generateLocations(int count) {
		List<Location> result = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				TitleAPI.sendTitle(player, Integer.valueOf(5), Integer.valueOf(20), Integer.valueOf(5),
						I18n.tl("spawnLoad", new String[0]),
						I18n.tl("spawnLoadCount", new String[] { String.valueOf(i + 1), String.valueOf(count) }));
			}
			result.add(generateLocation());
		}
		return result;
	}

	private List<Block> temp;

	private Location generateLocation() {
		Random r = new Random();
		int x = r.nextInt(this.pl.worldBorderUtils.getStartSize() / 2),
				z = r.nextInt(this.pl.worldBorderUtils.getStartSize() / 2);
		if (r.nextBoolean())
			x *= -1;
		if (r.nextBoolean())
			z *= -1;
		Location loc = new Location(this.pl.worldUtils.getWorld(), x, 200.0D, z);
		loc.getChunk().load(true);
		loc.getBlock().setType(Material.STAINED_GLASS);
		this.temp.add(loc.getBlock());
		for (int j = -1; j < 2; j++) {
			for (int k = -1; k < 2; k++) {
				if (loc.getBlock() != null) {
					loc.getBlock().getRelative(j, 0, k).setType(Material.STAINED_GLASS);
					this.temp.add(loc.getBlock().getRelative(j, 0, k));
				}
			}
		}
		return loc;
	}

	public void start() {
		for (Scenarios scenario : Scenarios.values()) {
			if (!hasScenario(scenario.getScenario()))
				HandlerList.unregisterAll((Listener) scenario.getScenario());
		}
		for (Team team : this.pl.scoreboardUtil.getBoard().getTeams()) {
			if (team.getSize() == 0)
				team.unregister();
		}
		for (Block block : this.temp)
			block.setType(Material.AIR);
		for (UUID uuid : this.players) {
			Player player = Bukkit.getPlayer(uuid);
			player.setGameMode(GameMode.SURVIVAL);
			player.setScoreboard(this.pl.scoreboardUtil.getBoard());
			player.getInventory().clear();
			CustomInv.restoreInventory(player);
		}
		Bukkit.getPluginManager().callEvent((Event) new GameStartEvent(this.players));
		Bukkit.broadcastMessage(I18n.tl("damageWillBeActivated", new String[0]));
		this.pl.taskManager.game();
		GameState.setCurrentState(GameState.STARTED);
	}

	public void reTeleport() {
		setDamage(false);
		setPvp(false);
		Bukkit.broadcastMessage(I18n.tl("damageAndPvpIn", new String[] { String.valueOf(20) }));
		this.players.forEach(uuid -> {
			Player p = Bukkit.getPlayer(uuid);
			if (p != null) {
				Location loc = this.teleportations.get(p.getUniqueId());
				p.teleport(loc);
				this.teleportations.remove(p.getUniqueId());
			}
		});
		(new BukkitRunnable() {
			public void run() {
				GameManager.this.setDamage(true);
				GameManager.this.setPvp(true);
				Bukkit.broadcastMessage(I18n.tl("damageAndPvpActived", new String[0]));
			}
		}).runTaskLater((Plugin) this.pl, 400L);
	}

	public void checkWin() {
		if (GameState.isState(GameState.STARTED))
			if (this.pl.teamUtils.getPlayersPerTeams() != 1 && !hasScenario(Scenarios.ONLYONEWINNER.getScenario())) {
				if (this.pl.scoreboardUtil.getBoard().getTeams().size() == 1) {
					Team winner = null;
					for (Team team : this.pl.scoreboardUtil.getBoard().getTeams())
						winner = team;
					win(winner);
				}
			} else if (this.players.size() == 1) {
				Player winner = Bukkit.getPlayer(this.players.get(0));
				win(winner);
			}
	}

	public void win(Player winner) {
		setDamage(false);
		Bukkit.broadcastMessage(I18n.tl("endGame", new String[0]));
		Bukkit.broadcastMessage(I18n.tl("winOfPlayer", winner.getName()));
		Bukkit.broadcastMessage(I18n.tl("rebootSoon", new String[0]));
		doParticle(winner.getLocation(), Effect.HEART, 4.0D, 10.0D, 0.5D, 1.0D, 1000.0D, 0.1D, 1.0D, 1.0D);
		Bukkit.getScheduler().runTaskLater((Plugin) this.pl, new Runnable() {
			public void run() {
				if (GameManager.this.pl.getConfig().getBoolean("bungeecord")) {
					if (GameManager.this.pl.getConfig().getString("server-redirection") != null && !GameManager.this.pl
							.getConfig().getString("server-redirection").equalsIgnoreCase("null"))
						Bukkit.getOnlinePlayers().forEach(p -> BungeeAPI.ConnectBungeeServer(p,
								GameManager.this.pl.getConfig().getString("server-redirection")));
				}

				Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), "restart");
			}
		}, 30 * 20L);
	}

	public void win(Team winner) {
		setDamage(false);
		Bukkit.broadcastMessage(I18n.tl("endGame", new String[0]));
		Bukkit.broadcastMessage(I18n.tl("winOfTeam", winner.getName()));
		Bukkit.broadcastMessage(I18n.tl("endGame", new String[0]));
		Bukkit.getScheduler().runTaskLater((Plugin) this.pl, new Runnable() {
			public void run() {
				if (GameManager.this.pl.getConfig().getBoolean("bungeecord")) {
					if (GameManager.this.pl.getConfig().getString("server-redirection") != null && !GameManager.this.pl
							.getConfig().getString("server-redirection").equalsIgnoreCase("null"))
						Bukkit.getOnlinePlayers().forEach(p -> BungeeAPI.ConnectBungeeServer(p,
								GameManager.this.pl.getConfig().getString("server-redirection")));
				}

				Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), "restart");
			}
		}, 30 * 20L);
	}

	public void doParticle(final Location center, final Effect effect, double curve, double radius, double radiusChange,
			double height, double particles, double delay, double amount, double separation) {
		center.add(0.0D, 0.25D, 0.0D);
		for (int i = 0; i < amount; i++) {
			double degrees;
			for (degrees = i * separation; degrees <= 360.0D * curve; degrees += 360.0D / particles) {
				final double y = degrees / 360.0D * curve / height;
				final double x = Math.cos(degrees) * (radius - radiusChange * y);
				final double z = Math.sin(degrees) * (radius - radiusChange * y);
				if (radius - radiusChange * y >= 0.0D)
					(new BukkitRunnable() {
						public void run() {
							center.getWorld().playEffect(center.clone().add(x, y, z), effect, 0);
						}
					}).runTaskLater((Plugin) this.pl, (long) Math.floor(degrees / 360.0D / particles * delay));
			}
		}
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
		this.pl.teamUtils.setupTeams();
	}

}
