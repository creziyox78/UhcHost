package fr.lastril.uhchost.game;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.BiomeState;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.game.rules.EnchantmentRules;
import fr.lastril.uhchost.game.rules.StuffRules;
import fr.lastril.uhchost.game.rules.world.BlocsRules;
import fr.lastril.uhchost.inventory.CustomInv;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeCommand;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.GameStartEvent;
import fr.lastril.uhchost.player.events.TeamUnregisteredEvent;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.scenario.Scenarios;
import fr.lastril.uhchost.scoreboard.TeamUtils;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.TitleAPI;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.Potion;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.stream.Collectors;

public class GameManager {

	private boolean border;

	private boolean pvp;

	private boolean gameIsEnding;

	private boolean damage;

	private boolean pregen;

	private boolean allPotionsEnable;

	private List<Potion> deniedPotions;

	private boolean potionsEditMode;

	private boolean notchApple = false;

	private boolean validateWorld;

	private boolean playerCheckingWorld;

	private Modes modes;

	public int episode = 1;
	
	public int episodeEvery = 60*20;

	private boolean editInv;

	private String winnerName;

	private int slot;

	private BiomeState biomeState;

	private int playersBeforeStart = 10;

	private final Map<UUID, UUID> lastDamager;

	private long elapsedTime;

	private long pvpTime;

	private double cycleTime = 10*60;

	private String hostName;

	private String gameName;

	private UUID host;

	private List<Player> cohost;

	private final UhcHost pl;

	private List<Scenario> scenarios;

	private boolean nether;

	public Location spawn;

	private int maxPlayers = 50;

	private int groupes = 5;

	private Map<UUID, Location> teleportations;

	//private boolean fightTeleport;

	private final BlocsRules blocsRules;

	private final EnchantmentRules enchantmentRules;

	private final StuffRules stuffRules;

	private boolean viewHealth;

	private final List<String> composition;

	public boolean isViewHealth() {
		return viewHealth;
	}

	public void setViewHealth(boolean viewHealth) {
		this.viewHealth = viewHealth;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	/*public boolean isFightTeleport() {
		return fightTeleport;
	}

	public void setFightTeleport(boolean b) {
		fightTeleport = b;
	}*/

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
		//this.fightTeleport = false;
		this.viewHealth = false;
		this.damage = false;
		this.potionsEditMode = false;
		this.gameIsEnding = false;
		this.border = false;
		this.pregen = false;
		this.deniedPotions = new ArrayList<>();
		this.scenarios = new ArrayList<>();
		this.composition = new ArrayList<>();
		this.cohost = new ArrayList<>();
		this.gameName = ChatColor.GOLD + "UHC Host";
		this.playerCheckingWorld = false;
		this.setNether(true);
		this.validateWorld = false;
		this.biomeState = BiomeState.ROOFED_FOREST;
		this.lastDamager = new HashMap<>();
		this.spawn = new Location(Bukkit.getWorld(pl.getConfig().getString("world_lobby")), 669.5, 36, 405.5);
		this.modes = Modes.CLASSIC;
		this.blocsRules = new BlocsRules(pl);
		this.enchantmentRules = new EnchantmentRules();
		this.stuffRules = new StuffRules();
	}

	public void addScenario(Scenario scenario) {
		if (!this.scenarios.contains(scenario)) {
			this.scenarios.add(scenario);
		}
	}

	public void addCoHost(Player player){
		if(!isCoHost(player)){
			player.sendMessage("§aVous avez été ajouté en tant que co-host.");
			cohost.add(player);
		} else {
			player.sendMessage("Erreur: §c" + player.getName() + " est déjà co-host de la partie !");
		}
	}

	public boolean isCoHost(Player player){
		return cohost.contains(player);
	}

	public void removeCoHost(Player player){
		if(isCoHost(player)){
			player.sendMessage("§cVous n'êtes plus co-host.");
			cohost.remove(player);
		} else {
			player.sendMessage("Erreur: §c" + player.getName() + " ne fait pas partie des co-hosts.");
		}

	}

	public List<Player> getCohost() {
		return cohost;
	}

	public void removeScenario(Scenario scenario) {
		this.scenarios.remove(scenario);
	}

	public boolean hasScenario(Scenario scenario) {
		return this.scenarios.contains(scenario);
	}

	public void removePlayer(Player player, boolean dead) {
		PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
		playerManager.setAlive(!dead);
		if (this.pl.teamUtils.getTeams(player) != null) {
			TeamUtils.Teams t = this.pl.teamUtils.getTeams(player);
			t.getTeam().removeEntry(player.getName());
			if (t.getTeam().getSize() == 0 && GameState.isState(GameState.STARTED)) {
				Bukkit.broadcastMessage(I18n.tl("teamDead", t.getTeam().getDisplayName()));
				t.getTeam().unregister();
				Bukkit.getPluginManager().callEvent(new TeamUnregisteredEvent(t.getTeam()));
			}
		}
		checkWin();
	}

	public List<Scenario> getScenarios() {
		return scenarios;
	}

	public void setScenarios(List<Scenario> scenarios) {
		this.scenarios = scenarios;
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

	public UUID getHost() {
		return this.host;
	}

	public void setHost(UUID host) {
		this.host = host;
	}

	public long getPvPTime() {
		return this.pvpTime;
	}

	public void setPvPTime(long pvptime) {
		this.pvpTime = pvptime;
	}

	public boolean isDamage() {
		return this.damage;
	}

	public void setDamage(boolean damage) {
		this.damage = damage;
	}


	public List<Potion> getDeniedPotions() {
		return this.deniedPotions;
	}

	public void setDeniedPotions(List<Potion> deniedPotions) {
		this.deniedPotions = deniedPotions;
	}

	public void removeDeniedPotion(Potion potion) {
		for (int i = this.deniedPotions.size() - 1; i >= 0; i--) {
			Potion pot = this.deniedPotions.get(i);
			if (pot.equals(potion))
				this.deniedPotions.remove(pot);
		}
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
		return winnerName;
	}

	public void setWinner(String winner) {
		this.winnerName = winner;
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
			for (PlayerManager playerManager : pl.getPlayerManagerOnlines())
				this.pl.teamUtils.setAutoTeam(playerManager.getPlayer());
			for (TeamUtils.Teams teams : TeamUtils.Teams.values()) {
				if (teams.getTeam() != null && !teams.getTeam().getEntries().isEmpty())
					this.count++;
			}
			this.task = Bukkit.getScheduler().runTaskTimer(this.pl, new Runnable() {

				final List<Location> locs = GameManager.this.generateLocations(GameManager.this.count);

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
									/*if (GameManager.this.fightTeleport)
										GameManager.this.teleportations.put(p.getUniqueId(),
												loc.clone().add(0.5D, 1.0D, 0.5D));*/
								});
								this.locs.remove(loc);
							}
						}
						GameManager.this.pl.taskManager.preGame();
						GameManager.this.task.cancel();
						return;
					}
					this.locs.get(GameManager.this.count).getChunk().load(true);
					GameManager.this.count++;
				}
			}, 10L, 10L);
		} else {

			this.task = Bukkit.getScheduler().runTaskTimer(this.pl, new Runnable() {
				int teleported = 0;
				final List<Player> players = pl.getPlayerManagerOnlines().stream().map(PlayerManager::getPlayer).collect(Collectors.toList());
				final List<Location> locs = GameManager.this.generateLocations(Bukkit.getOnlinePlayers().size());
				@Override
				public void run() {
					/*if (GameManager.this.count == this.locs.size()) {
						Bukkit.getOnlinePlayers().forEach(player -> {
							PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
							playerManager.setPlayedGame(true);
							playerManager.setAlive(true);
							Location loc = locs.stream().findAny().get();
							player.teleport(loc.clone().add(0.5D, 1.0D, 0.5D));
							locs.remove(loc);
						});
						GameManager.this.pl.taskManager.preGame();
						GameManager.this.task.cancel();
						return;
					}
					this.locs.get(GameManager.this.count).getChunk().load(true);
					GameManager.this.count++;*/

					players.forEach(player -> ActionBar.sendMessage(player.getPlayer(), "§3Téléportation: " + "§b"+teleported+"/"+players.size()));
					if (GameManager.this.count == this.locs.size()) {
						GameManager.this.pl.taskManager.preGame();
						GameManager.this.task.cancel();
					} else {
						Player player = players.get(count);
						PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
						playerManager.setPlayedGame(true);
						playerManager.setAlive(true);
						Location loc = locs.get(count);
						player.teleport(loc.clone().add(0.5D, 1.0D, 0.5D));
						this.locs.get(GameManager.this.count).getChunk().load(true);
						GameManager.this.count++;
						teleported++;
					}

				}
			}, 10L, 10L);

		}
	}

	private List<Location> generateLocations(int count) {
		List<Location> result = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				TitleAPI.sendTitle(player, 5, 20, 5,
						I18n.tl("spawnLoad"),
						I18n.tl("spawnLoadCount", String.valueOf(i + 1), String.valueOf(count)));
			}
			result.add(generateLocation());
		}
		return result;
	}

	private final List<Block> temp;


	public void teleportPlayerOnGround(Player player){
		Random r = UhcHost.getRANDOM();
		int x = r.nextInt((int) (Bukkit.getWorld("game").getWorldBorder().getSize() / 2)),
				z = r.nextInt((int) (Bukkit.getWorld("game").getWorldBorder().getSize() / 2));
		if (r.nextBoolean())
			x *= -1;
		if (r.nextBoolean())
			z *= -1;
		Location loc = new Location(Bukkit.getWorld("game"), x, Bukkit.getWorld("game").getHighestBlockYAt(player.getLocation()) + 1, z);
		player.teleport(loc);
	}

	private Location generateLocation() {
		Random r = UhcHost.getRANDOM();
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
			if (!hasScenario(scenario.getScenario())){
				Bukkit.getConsoleSender().sendMessage("Unregister scenarios: " + scenario.getScenario().getName());
				HandlerList.unregisterAll(scenario.getScenario());
			}
		}
		for (Team team : this.pl.scoreboardUtil.getBoard().getTeams()) {
			if (team.getSize() == 0)
				team.unregister();
		}
		for (Block block : this.temp)
			block.setType(Material.AIR);
		for (PlayerManager playerManager : pl.getPlayerManagerOnlines()) {
			Player player = playerManager.getPlayer();
			player.setGameMode(GameMode.SURVIVAL);
			player.setScoreboard(this.pl.scoreboardUtil.getBoard());
			player.getInventory().clear();
			CustomInv.restoreInventory(player);
		}
		List<UUID> listPlayer = new ArrayList<>();
		pl.getPlayerManagerOnlines().forEach(playerManager -> listPlayer.add(playerManager.getUuid()));
		Bukkit.getPluginManager().callEvent(new GameStartEvent(listPlayer));
		Bukkit.broadcastMessage("§8§m--------------------------------------------------§r");
		Bukkit.broadcastMessage("§c         Listes des commandes de partie");
		Bukkit.broadcastMessage("§c");
		Bukkit.broadcastMessage("§c •§f /rules : Voir les règles de la partie.");
		Bukkit.broadcastMessage("§c •§f /scenarios : Voir les scénarios de la partie.");
		Bukkit.broadcastMessage("§c •§f /doc : Voir le document du mode de jeu.");
		if(modes.getMode() instanceof ModeCommand){
			ModeCommand modeCommand = (ModeCommand) modes.getMode();
			Bukkit.broadcastMessage("§c •§f /"+modeCommand.getCommandName()+" compo : Voir la composition de la partie.");
			Bukkit.broadcastMessage("§c •§f /"+modeCommand.getCommandName()+" me : Voir son propre rôle.");
		}
		Bukkit.broadcastMessage("§c");
		Bukkit.broadcastMessage("§8§m--------------------------------------------------§r");
		Bukkit.broadcastMessage(Messages.PREFIX_WITH_ARROW.getMessage() + I18n.tl("damageWillBeActivated"));
		this.pl.taskManager.game();
		GameState.setCurrentState(GameState.STARTED);

	}

	public void reTeleport() {
		setDamage(false);
		setPvp(false);
		Bukkit.broadcastMessage(I18n.tl("damageAndPvpIn", new String[20]));
		pl.getPlayerManagerOnlines().forEach(playerManager -> {
			Player p = playerManager.getPlayer();
			if (p != null) {
				Location loc = this.teleportations.get(p.getUniqueId());
				p.teleport(loc);
				this.teleportations.remove(p.getUniqueId());
			}
		});
		new BukkitRunnable() {
			public void run() {
				GameManager.this.setDamage(true);
				GameManager.this.setPvp(true);
				Bukkit.broadcastMessage(I18n.tl("damageAndPvpActived"));
			}
		}.runTaskLater(this.pl, 400L);
	}

	public void checkWin() {
		if (GameState.isState(GameState.STARTED)){
			this.pl.gameManager.getModes().getMode().checkWin();
		}
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
		this.pl.teamUtils.setupTeams();
	}

	public Modes getModes() {
		return modes;
	}

	public void setModes(Modes modes) {
		this.modes = modes;
	}

	public List<Class<? extends Role>> getComposition() {
		List<Class<? extends Role>> roles = new ArrayList<>();
		for (String className : composition) {
			Class<? extends Role> role = null;
			try {
				role = (Class<? extends Role>) Class.forName(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			roles.add(role);
		}
		return roles;
	}

	public List<Role> getRolesComposition(){
		List<Role> roles = new ArrayList<>();
		getComposition().forEach(aClass -> {
			try {
				roles.add(aClass.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		});
		return roles;
	}

	public List<Class<? extends Role>> getRolesActivatedInCamps(Camps camps){
		return getComposition().stream().filter(aClass -> {
			try {
				return aClass.newInstance().getCamp() == camps;
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			return false;
		}).collect(Collectors.toList());
	}

	public Map<Class<? extends Role>, Integer> getCompositionSorted() {
		Map<Class<? extends Role>, Integer> rolesMap = new HashMap<>();
		for (Class<? extends Role> role : getComposition()) {
			int i = 1;
			if (rolesMap.containsKey(role)) {
				i = rolesMap.get(role) + 1;
			}
			rolesMap.put(role, i);
		}
		return rolesMap;
	}

	public int countRolesInComposition(Role role){
		return (int) getComposition().stream().filter(aClass -> {
			try {
				return aClass.newInstance().getRoleName().equalsIgnoreCase(role.getRoleName());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			return false;
		}).count();
	}

	public void addRoleToComposition(Role role) {
		this.composition.add(role.getClass().getName());
	}

	public void removeRoleToComposition(Role role) {
		this.composition.remove(role.getClass().getName());
	}

	public void clearComposition() {
		this.composition.clear();
	}

	public boolean isPregen() {
		return pregen;
	}

	public void setPregen(boolean pregen) {
		this.pregen = pregen;
	}

	public BiomeState getBiomeState() {
		return biomeState;
	}

	public void setBiomeState(BiomeState biomeState) {
		this.biomeState = biomeState;
	}

	public boolean isValidateWorld() {
		return validateWorld;
	}

	public void setValidateWorld(boolean validateWorld) {
		this.validateWorld = validateWorld;
	}

	public boolean isPlayerCheckingWorld() {
		return playerCheckingWorld;
	}

	public void setPlayerCheckingWorld(boolean playerCheckingWorld) {
		this.playerCheckingWorld = playerCheckingWorld;
	}

	public boolean isAllPotionsEnable() {
		return allPotionsEnable;
	}

	public void setAllPotionsEnable(boolean allPotionsEnable) {
		this.allPotionsEnable = allPotionsEnable;
	}

	public boolean isPotionsEditMode() {
		return potionsEditMode;
	}

	public void setPotionsEditMode(boolean potionsEditMode) {
		this.potionsEditMode = potionsEditMode;
	}

	public boolean isNotchApple() {
		return notchApple;
	}

	public void setNotchApple(boolean notchApple) {
		this.notchApple = notchApple;
	}

	public double getCycleTime() {
		return cycleTime;
	}

	public void setCycleTime(double cycleTime) {
		this.cycleTime = cycleTime;
	}

	public int getGroupes() {
		return groupes;
	}

	public void setGroupes(int groupes) {
		this.groupes = groupes;
	}

	public void rappelGroupes(Player player) {
		TitleAPI.sendTitle(player, 10, 20 * 2, 20, "§c§lGroupes", "§6" + groupes);
	}

	public BlocsRules getBlocsRules() {
		return blocsRules;
	}

	public StuffRules getStuffRules() {
		return stuffRules;
	}

	public EnchantmentRules getEnchantmentRules() {
		return enchantmentRules;
	}
}
