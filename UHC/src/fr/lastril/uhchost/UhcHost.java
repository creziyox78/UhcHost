package fr.lastril.uhchost;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import fr.lastril.uhchost.bungeecord.PluginMessage;
import fr.lastril.uhchost.commands.CommandHost;
import fr.lastril.uhchost.commands.CommandRules;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.game.mode.Warzone;
import fr.lastril.uhchost.game.tasks.TaskManager;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.custom.GameStart;
import fr.lastril.uhchost.player.events.menu.InteractEnchant;
import fr.lastril.uhchost.player.events.menu.InteractMain;
import fr.lastril.uhchost.player.events.menu.InteractRules;
import fr.lastril.uhchost.player.events.menu.InteractTeam;
import fr.lastril.uhchost.player.events.normal.BreakBlock;
import fr.lastril.uhchost.player.events.normal.Damage;
import fr.lastril.uhchost.player.events.normal.DeathPlayer;
import fr.lastril.uhchost.player.events.normal.Drop;
import fr.lastril.uhchost.player.events.normal.FoodLevel;
import fr.lastril.uhchost.player.events.normal.Interact;
import fr.lastril.uhchost.player.events.normal.Join;
import fr.lastril.uhchost.player.events.normal.Move;
import fr.lastril.uhchost.player.events.normal.OnPingServer;
import fr.lastril.uhchost.player.events.normal.PlaceBlock;
import fr.lastril.uhchost.player.events.normal.Portal;
import fr.lastril.uhchost.player.events.normal.Quit;
import fr.lastril.uhchost.player.events.normal.RespawnPlayer;
import fr.lastril.uhchost.scoreboard.ScoreboardUtils;
import fr.lastril.uhchost.scoreboard.TeamUtils;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.Lang;
import fr.lastril.uhchost.tools.inventory.CustomInv;
import fr.lastril.uhchost.tools.inventory.NotStart;
import fr.lastril.uhchost.tools.server.Tablist;
import fr.lastril.uhchost.tools.server.TpsServer;
import fr.lastril.uhchost.world.WorldBorderUtils;
import fr.lastril.uhchost.world.WorldSettings;
import fr.lastril.uhchost.world.WorldUtils;
import fr.lastril.uhchost.world.schematics.LobbyPopulator;

public class UhcHost extends JavaPlugin {

	public GameManager gameManager;

	private PlayerManager playermanager;

	public ScoreboardUtils scoreboardUtil;

	private Warzone warzone;

	private NotStart notstart;

	public WorldUtils worldUtils;

	public WorldBorderUtils worldBorderUtils;

	private ScheduledExecutorService executorMonoThread;

	private ScheduledExecutorService scheduledExecutorService;

	public TeamUtils teamUtils;

	public TaskManager taskManager;

	public static UhcHost instance;

	public static UhcHost getInstance() {
		return instance;
	}

	public void onEnable() {
		instance = this;
		File file = new File(getDataFolder() + "/schematics/");
		if (!file.exists()) {
			Bukkit.getConsoleSender().sendMessage("Creating schematics folder...");
			file.mkdir();
		}
		saveDefaultConfig();
		if (getConfig().getBoolean("bungeecord")) {
			getServer().getMessenger().registerOutgoingPluginChannel((Plugin) this, "BungeeCord");
			getServer().getMessenger().registerIncomingPluginChannel((Plugin) this, "BungeeCord",
					(PluginMessageListener) new PluginMessage());
		}
		this.scheduledExecutorService = Executors.newScheduledThreadPool(16);
		this.executorMonoThread = Executors.newScheduledThreadPool(1);
		
		
		setupI18n();
		taskRegister();
		setWorld();
		commandsRegister();
		eventsRegister();

		Bukkit.getScheduler().runTaskLater((Plugin) this, new Runnable() {
			public void run() {
				Bukkit.getOnlinePlayers()
						.forEach(p -> Bukkit.getPluginManager().callEvent((Event) new PlayerJoinEvent(p, null)));
			}
		}, 60L);
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[" + getName() + "] ON !");
	}

	private void setupI18n() {
		I18n.supportTranslate(this, Lang.FRENCH);
		I18n.setLang(Lang.valueOf(getConfig().getString("lang")));
	}

	public void onDisable() {
		Bukkit.getScheduler().cancelAllTasks();

		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + getName() + "] OFF !");
	}

	private void commandsRegister() {
		getCommand("h").setExecutor((CommandExecutor) new CommandHost(this));
		getCommand("host").setExecutor((CommandExecutor) new CommandHost(this));
		getCommand("scenarios").setExecutor(new CommandRules());
	}

	private void utilsRegister() {
		this.notstart = new NotStart();
		this.gameManager = new GameManager(this);
		this.taskManager = new TaskManager(this);
		this.scoreboardUtil = new ScoreboardUtils(this);
		this.gameManager.setGameState(GameState.LOBBY);
		this.gameManager.setHostname(ChatColor.RED + "" + ChatColor.BOLD + "Personne");
		this.gameManager.setSlot(50);
		CustomInv.createInventory();

		UhcHost.this.scoreboardUtil = new ScoreboardUtils(UhcHost.instance);
		UhcHost.this.teamUtils = new TeamUtils(UhcHost.instance, UhcHost.this.scoreboardUtil.getBoard());
	}

	@SuppressWarnings("deprecation")
	private void taskRegister() {
		try {
			(new Tablist()).runTaskTimer((Plugin) this, 20L, 20L);
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin) this, (BukkitRunnable) new TpsServer(),
					100L, 1L);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void eventsRegister() {
		getServer().getPluginManager().registerEvents((Listener) new Join(this), (Plugin) this);
		getServer().getPluginManager().registerEvents((Listener) new Interact(this.notstart), (Plugin) this);
		getServer().getPluginManager().registerEvents((Listener) new FoodLevel(), (Plugin) this);
		getServer().getPluginManager().registerEvents((Listener) new Damage(this), (Plugin) this);
		getServer().getPluginManager().registerEvents((Listener) new OnPingServer(this), (Plugin) this);
		getServer().getPluginManager().registerEvents((Listener) new DeathPlayer(this), (Plugin) this);
		getServer().getPluginManager().registerEvents((Listener) new Quit(this), (Plugin) this);
		getServer().getPluginManager().registerEvents((Listener) new Drop(), (Plugin) this);
		getServer().getPluginManager().registerEvents((Listener) new BreakBlock(), (Plugin) this);
		getServer().getPluginManager().registerEvents((Listener) new PlaceBlock(), (Plugin) this);
		getServer().getPluginManager().registerEvents((Listener) new InteractMain(this), (Plugin) this);
		getServer().getPluginManager().registerEvents((Listener) new InteractRules(), (Plugin) this);
		getServer().getPluginManager().registerEvents((Listener) new InteractEnchant(), (Plugin) this);
		getServer().getPluginManager().registerEvents((Listener) new InteractTeam(), (Plugin) this);
		getServer().getPluginManager().registerEvents(new RespawnPlayer(), (Plugin) this);
		getServer().getPluginManager().registerEvents(new Move(), (Plugin) this);
		getServer().getPluginManager().registerEvents(new GameStart(this), (Plugin) this);
		getServer().getPluginManager().registerEvents((Listener) new Portal(this), this);
	}

	private void setWorld() {
		try {
			Bukkit.getConsoleSender().sendMessage("Checking world: " + getConfig().getString("world_lobby"));
			WorldCreator spawn = new WorldCreator(getConfig().getString("world_lobby"));
			spawn.environment(World.Environment.NORMAL);
			spawn.type(WorldType.NORMAL);
			Bukkit.createWorld(spawn);
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Generation of the world, successful !");
			utilsRegister();
			Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Loading schematic...");
			File file = new File(getDataFolder() + "/schematics/lobby.schematic");
			if (file.exists()) {
				LobbyPopulator.generate((getGamemanager()).spawn, "/schematics/lobby.schematic");
			} else {
				Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "File 'lobby.schematic' not exist !");
			}
			Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "Starting preloading...");
			WorldSettings.setSettings(Bukkit.getWorld("world"));
			Bukkit.getScheduler().runTaskLater(this, new Runnable() {
				public void run() {
					(new Location(Bukkit.getWorld("world"), 0.0D, 200.0D, 0.0D)).getChunk().load(true);
					Bukkit.setWhitelist(true);
				}
			}, 40L);
			UhcHost.this.worldUtils = new WorldUtils(UhcHost.instance, Bukkit.getWorld("world"),
					Bukkit.getWorld("world_nether"), Bukkit.getWorld("world_the_end"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "Set settings on worlds...");
			Bukkit.getWorld(getConfig().getString("world_lobby")).setGameRuleValue("doMobSpawning", "false");
			Bukkit.getWorld("world").setGameRuleValue("naturalRegeneration", "false");
			Bukkit.getWorld("world_nether").setGameRuleValue("naturalRegeneration", "false");
			Bukkit.getWorld("world_the_end").setGameRuleValue("naturalRegeneration", "false");
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Settings set !");
			UhcHost.this.worldBorderUtils = new WorldBorderUtils();

			return;
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error when generating world !");
			Bukkit.getConsoleSender().sendMessage("");
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "End of error !");
			Bukkit.getConsoleSender().sendMessage("");
			return;
		}
	}

	public ScheduledExecutorService getExecutorMonoThread() {
		return this.executorMonoThread;
	}

	public ScheduledExecutorService getScheduledExecutorService() {
		return this.scheduledExecutorService;
	}

	public GameManager getGamemanager() {
		return this.gameManager;
	}

	public Warzone getWarzone() {
		return warzone;
	}

	public PlayerManager getPlayermanager() {
		return playermanager;
	}
}
