package fr.lastril.uhchost;

import fr.lastril.uhchost.bungeecord.PluginMessage;
import fr.lastril.uhchost.commands.CmdMode;
import fr.lastril.uhchost.commands.CommandHost;
import fr.lastril.uhchost.commands.CommandRules;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.game.tasks.TaskManager;
import fr.lastril.uhchost.inventory.CustomInv;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeCommand;
import fr.lastril.uhchost.player.events.interact.InteractCheckWorld;
import fr.lastril.uhchost.tools.API.clickable_messages.ClickableMessageManager;
import fr.lastril.uhchost.tools.InventoryUtils;
import fr.lastril.uhchost.tools.NotStart;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.custom.GameStart;
import fr.lastril.uhchost.player.events.interact.InteractTeam;
import fr.lastril.uhchost.player.events.normal.*;
import fr.lastril.uhchost.scoreboard.ScoreboardUtils;
import fr.lastril.uhchost.scoreboard.TeamUtils;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.Lang;
import fr.lastril.uhchost.tools.server.Tablist;
import fr.lastril.uhchost.tools.server.TpsServer;
import fr.lastril.uhchost.world.*;
import fr.lastril.uhchost.world.schematics.LobbyPopulator;
import org.bukkit.*;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

public class UhcHost extends JavaPlugin {

	private static final Random RANDOM = new Random();
	public GameManager gameManager;

	public ScoreboardUtils scoreboardUtil;

	private InventoryUtils inventoryUtils;

	private ClickableMessageManager clickableMessageManager;

	private NotStart notstart;

	private Map<UUID, PlayerManager> playerManagers = new HashMap<>();

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
		saveDefaultConfig();
		if (getConfig().getBoolean("bungeecord")) {
			getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
			getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord",
					new PluginMessage());
		}

		this.scheduledExecutorService = Executors.newScheduledThreadPool(16);
		this.executorMonoThread = Executors.newScheduledThreadPool(1);

		getServer().getPluginManager().registerEvents(new WorldInit(this, 0, 0), this);
		setupI18n();
		taskRegister();
		setWorld();
		commandsRegister();
		eventsRegister();

		Bukkit.getScheduler().runTaskLater(this, () -> Bukkit.getOnlinePlayers()
				.forEach(p -> Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(p, null))), 60L);
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[" + getName() + "] ON !");
		this.gameManager.setGameState(GameState.LOBBY);
	}

	@Override
	public void onLoad() {
		new BiomeManager().removeBiomes();
		super.onLoad();
	}

	private void setupI18n() {
		I18n.supportTranslate(this, Lang.FRENCH);
		I18n.setLang(Lang.valueOf(getConfig().getString("lang")));
	}

	public void onDisable() {
		Bukkit.getScheduler().cancelAllTasks();
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Deleting worlds...");
		if(Bukkit.getWorld("game").getWorldFolder().exists())
			WorldSettings.deleteWorld(Bukkit.getWorld("game").getWorldFolder());
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + getName() + "] OFF !");
	}

	private void commandsRegister() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Register commands...");
		getCommand("h").setExecutor(new CommandHost(this));
		getCommand("scenarios").setExecutor(new CommandRules());

		for (Modes mode : Modes.values()) {
			if (mode.getMode() instanceof ModeCommand) {
				ModeCommand modeCommand = (ModeCommand) mode.getMode();
				PluginCommand command = getCommand(modeCommand.getCommandName());
				CmdMode cmdMode = new CmdMode(this, modeCommand);
				command.setExecutor(cmdMode);
				command.setTabCompleter(cmdMode);
			}
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Commands registered !");

	}

	private void utilsRegister() {
		this.notstart = new NotStart();
		this.gameManager = new GameManager(this);
		this.taskManager = new TaskManager(this);
		this.inventoryUtils = new InventoryUtils(this);
		this.taskManager.lobbyTask();
		this.scoreboardUtil = new ScoreboardUtils(this);
		this.clickableMessageManager = new ClickableMessageManager(this);
		this.gameManager.setGameState(GameState.REBUILDING);
		this.gameManager.setHostname(ChatColor.RED + "" + ChatColor.BOLD + "Personne");
		this.gameManager.setSlot(50);
		CustomInv.createInventory();

		this.scoreboardUtil = new ScoreboardUtils(this);
		this.teamUtils = new TeamUtils(this, this.scoreboardUtil.getBoard());
	}

	private void taskRegister() {
		try {
			(new Tablist()).runTaskTimer(this, 20L, 20L);
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TpsServer(),
					100L, 1L);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void eventsRegister() {
		getServer().getPluginManager().registerEvents(new Join(this), this);
		getServer().getPluginManager().registerEvents(new Interact(this.notstart, this), this);
		getServer().getPluginManager().registerEvents(new FoodLevel(), this);
		getServer().getPluginManager().registerEvents(new Damage(this), this);
		getServer().getPluginManager().registerEvents(new OnPingServer(this), this);
		getServer().getPluginManager().registerEvents(new DeathPlayer(this), this);
		getServer().getPluginManager().registerEvents(new Quit(this), this);
		getServer().getPluginManager().registerEvents(new Drop(), this);
		getServer().getPluginManager().registerEvents(new PreparePotion(this), this);
		getServer().getPluginManager().registerEvents(new BreakBlock(), this);
		getServer().getPluginManager().registerEvents(new PlaceBlock(), this);
		getServer().getPluginManager().registerEvents(new InteractTeam(), this);
		getServer().getPluginManager().registerEvents(new RespawnPlayer(), this);
		getServer().getPluginManager().registerEvents(new Move(), this);
		getServer().getPluginManager().registerEvents(new GameStart(this), this);
		getServer().getPluginManager().registerEvents(new Portal(this), this);
		getServer().getPluginManager().registerEvents(new InteractCheckWorld(this), this);
	}

	private void setWorld() {
		try {
			Bukkit.getConsoleSender().sendMessage("Checking world: " + getConfig().getString("world_lobby"));
			WorldCreator spawn = new WorldCreator(getConfig().getString("world_lobby"));
			spawn.environment(World.Environment.NORMAL);
			spawn.type(WorldType.NORMAL);
			Bukkit.createWorld(spawn);
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Generation of the lobby, successful !");
			utilsRegister();
			Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Loading schematic...");
			File schematic = new File(getDataFolder() + "/schematics");
			if(schematic.exists()){
				File lobby = new File(getDataFolder() + "/schematics/lobby.schematic");
				if (lobby.exists()) {
					LobbyPopulator.generate((getGamemanager()).spawn, "/schematics/lobby.schematic");
				} else {
					Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "File 'lobby.schematic' not exist !");
				}
			} else{
				Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "The file 'schematic' doesn't exist. Creating...");
				schematic.mkdir();
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "File created !");
			}
			WorldSettings.setSettings(Bukkit.getWorld("world"));
			Bukkit.getScheduler().runTaskLater(this, () -> {
				new Location(Bukkit.getWorld("world"), 0.0D, 200.0D, 0.0D).getChunk().load(true);
				Bukkit.setWhitelist(true);
			}, 40L);
			this.worldUtils = new WorldUtils(this, Bukkit.getWorld("world_nether"), Bukkit.getWorld("world_the_end"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Set settings on worlds...");
			Bukkit.getWorld(getConfig().getString("world_lobby")).setGameRuleValue("doMobSpawning", "false");
			Bukkit.getWorld("world").setGameRuleValue("naturalRegeneration", "false");
			Bukkit.getWorld("world_nether").setGameRuleValue("naturalRegeneration", "false");
			Bukkit.getWorld("world_the_end").setGameRuleValue("naturalRegeneration", "false");
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Settings set !");
			this.worldBorderUtils = new WorldBorderUtils();
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error when generating world !");
			Bukkit.getConsoleSender().sendMessage("");
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "End of error !");
			Bukkit.getConsoleSender().sendMessage("");
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

	public List<PlayerManager> getPlayerManagerAlives() {
		return this.playerManagers.values().stream().filter(PlayerManager::isAlive).collect(Collectors.toList());
	}

	public List<PlayerManager> getPlayerManagerOnlines() {
		return this.playerManagers.values().stream().filter(PlayerManager::isOnline).collect(Collectors.toList());
	}

	public PlayerManager getRandomPlayerManager() {
		return new ArrayList<>(this.playerManagers.values()).get(RANDOM.nextInt(this.playerManagers.values().size()));
	}

	public PlayerManager getRandomPlayerManagerAlive() {
		return this.getPlayerManagerAlives().get(RANDOM.nextInt(this.getPlayerManagerAlives().size()));
	}

	public static Random getRANDOM() {
		return RANDOM;
	}

	public Map<UUID, PlayerManager> getAllPlayerManager() {
		return playerManagers;
	}

	public PlayerManager getPlayerManager(UUID uuid) {
		return playerManagers.get(uuid);
	}

	public InventoryUtils getInventoryUtils() {
		return inventoryUtils;
	}
}
