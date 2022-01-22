package fr.lastril.uhchost.modes.naruto;


import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.inventory.guis.modes.naruto.NarutoGUI;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.CmdCompo;
import fr.lastril.uhchost.modes.command.CmdMe;
import fr.lastril.uhchost.modes.command.ModeCommand;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.command.CmdDesc;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Roles;
import fr.lastril.uhchost.modes.naruto.v2.biju.BijuManager;
import fr.lastril.uhchost.modes.naruto.v2.commands.hokage.CmdBoost;
import fr.lastril.uhchost.modes.naruto.v2.commands.hokage.CmdReveal;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.BungeeAPI;
import fr.lastril.uhchost.tools.API.TitleAPI;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class NarutoV2 extends Mode implements ModeConfig, RoleMode<NarutoV2Role>, ModeCommand, RoleAnnounceMode {

	private final UhcHost main;
	private final NarutoV2Manager narutoV2Manager;
	private int announceRoles = 30;
	
	public NarutoV2() {
		super(Modes.NARUTO_V2);
		main = UhcHost.getInstance();
		this.narutoV2Manager = new NarutoV2Manager(main);
	}


	@Override
	public IQuickInventory getGui() {
		return new NarutoGUI(main);
	}

	@Override
	public void tick(int timer) {
		if (announceRoles == 0) {
			annonceRoles();
		}
		if(timer == 1 && narutoV2Manager.getNarutoV2Config().isBiju()) {
			UhcHost.debug("Registered BijuManager");
			narutoV2Manager.setBijuManager(new BijuManager(main, narutoV2Manager));
		}
		if(timer == narutoV2Manager.getNarutoV2Config().getHokageAnnoncement()){
			narutoV2Manager.annonceHokage();
		}
	}

	public void annonceRoles() {
		List<Class<? extends Role>> compo = main.getGamemanager().getComposition();
		long missedRoles = main.getPlayerManagerAlives().stream().filter(PlayerManager -> PlayerManager.isAlive() && PlayerManager.isPlayedGame()).count() - compo.size();
		Bukkit.broadcastMessage("§6§lAttribution des rôles !");
		if (missedRoles > 0) {
			for (int i = 0; i < missedRoles; i++) {
				Role role = getRoles().get(UhcHost.getRANDOM().nextInt(getRoles().size()));
				compo.add(role.getClass());
			}
		}
		List<Role> roles = new ArrayList<>();
		for (PlayerManager playerManager : main.getPlayerManagerAlives().stream().filter(PlayerManager -> PlayerManager.isAlive() && PlayerManager.isPlayedGame()).collect(Collectors.toList())) {
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerManager.getUuid());
			int index = UhcHost.getRANDOM().nextInt(compo.size());
			Bukkit.getConsoleSender().sendMessage("index " + index);
			Role role = null;
			try {
				role = compo.get(index).newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			Bukkit.getConsoleSender().sendMessage("role " + role.getRoleName());
			if (offlinePlayer.isOnline()) {
				Player player = offlinePlayer.getPlayer();
				//player.sendMessage("§7§m------------------------------------------");
				//player.sendMessage("§7Vous êtes " + role.getCamp().getCompoColor() + "" + role.getRoleName() + "§7.");
				player.sendMessage(role.getDescription() != null ? "§7" + role.getDescription() : "§c§lDESCRIPTION NULL");
				//player.sendMessage("§7§m------------------------------------------");
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 10);
				role.stuff(player);
			}
			role.setPlayerID(offlinePlayer.getUniqueId());
			playerManager.setRole(role);
			compo.remove(index);
			roles.add(role);
		}

		getModeManager().sendRoleToKnow();


		for (Role role : roles) {
			if (role instanceof RoleListener) {
				RoleListener roleListener = (RoleListener) role;
				roleListener.registerListener(main, main.getServer().getPluginManager());
				Bukkit.getConsoleSender().sendMessage("Register roleListener: " + role.getRoleName());
			}
		}

	}

	@Override
	public void onPvp() {
		
		
	}

	@Override
	public void onBorder() {
		
		
	}

	@Override
	public void onTeleportation() {
		
		
	}

	@Override
	public void onDeath(Player player, Player killer) {
		if(killer != null){
			PlayerManager killerManager = main.getPlayerManager(killer.getUniqueId());
			killerManager.addKill(player.getUniqueId());
		}
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		joueur.setDeathLocation(player.getLocation());
		joueur.setItems(player.getInventory().getContents());
		joueur.setArmors(player.getInventory().getArmorContents());
		joueur.setAlive(false);
		for (Player players : Bukkit.getOnlinePlayers()) {
			players.playSound(players.getLocation(), Sound.WITHER_DEATH, 1f, 1f);
			PlayerManager playerManager = main.getPlayerManager(players.getUniqueId());
			if(playerManager.isAlive() && playerManager.hasRole()){
				playerManager.getRole().onPlayerDeath(player);
			}
		}

		if (joueur.hasRole()) {
			Bukkit.broadcastMessage("§3§m----------------------------------");
			Bukkit.broadcastMessage("§b§l" + player.getName() + "§7 est mort, son rôle était "+joueur.getRole().getCamp().getCompoColor()+joueur.getRole().getRoleName()+"§7.");
			Bukkit.broadcastMessage("§3§m----------------------------------");
			new BukkitRunnable() {

				@Override
				public void run() {
					player.spigot().respawn();
				}
			}.runTaskLater(main, 2);
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					player.setGameMode(GameMode.ADVENTURE);
					player.setGameMode(GameMode.SPECTATOR);
					player.teleport(joueur.getDeathLocation());
				}
			}.runTaskLater(main, 5);
		} else {
			Bukkit.broadcastMessage("§3§m----------------------------------");
			Bukkit.broadcastMessage("§b§l" + player.getName() + "§7 est mort.");
			Bukkit.broadcastMessage("§3§m----------------------------------");
		}
		checkWin();
		/* DROPING INVENTORY */
		System.out.println("Droping inventory !");
		main.getInventoryUtils().dropInventory(joueur.getDeathLocation(), joueur.getItems(), joueur.getArmors());
	}

	@Override
	public boolean isScheduledDeath() {
		
		return false;
	}

	@Override
	public boolean isEpisodeMode() {
		
		return true;
	}


	@Override
	public void onNewEpisode() {

	}

	@Override
	public void giveRoles() {

	}

	@Override
	public List<Role> getRoles() {
		List<Role> roles = new ArrayList<>();
		for (NarutoV2Roles role : NarutoV2Roles.values()) {
			try {
				roles.add(role.getRole().newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return roles;
	}

	@Override
	public String getDocLink() {
		return "https://docs.mcatlantis.fr/naruto-uhc-v2/presentation";
	}

	@Override
	public String getCommandName() {
		return "narutos";
	}

	@Override
	public List<ModeSubCommand> getSubCommands() {
		List<ModeSubCommand> subCommands = new ArrayList<>();

		this.getRoles().stream().filter(role -> role instanceof RoleCommand).map(role -> ((RoleCommand) role).getSubCommands()).forEach(subCommands::addAll);
		subCommands.add(new CmdCompo(main));
		subCommands.add(new CmdBoost(main));
		subCommands.add(new CmdDesc(main));
		subCommands.add(new CmdReveal(main));
		subCommands.add(new CmdMe(main));
		return subCommands;
	}

	@Override
	public void onDamage(Player target, Player damager) {
		narutoV2Manager.onDamage(target, damager);
	}

	@Override
	public void checkWin() {
		Set<Camps> lastCamps = new HashSet<>();
		for (Player players : Bukkit.getOnlinePlayers()) {
			PlayerManager playerManager = main.getPlayerManager(players.getUniqueId());
			if (playerManager.isAlive()) {
				lastCamps.add(playerManager.getCamps());
			}
		}
		if (lastCamps.size() == 1) {
			Camps winners = lastCamps.stream().findFirst().get();
			Bukkit.getConsoleSender().sendMessage("Founded 1 last camp (" + winners + ")");
			win(winners);
		} else if (lastCamps.size() == 0) {
			win(Camps.EGALITE);
		}
	}

	public void win(Camps winner) {
		if(GameState.isState(GameState.STARTED)){
			this.main.gameManager.setDamage(true);

			Map<PlayerManager, Integer> damages = new HashMap<>();
			for (PlayerManager playerManager : main.getAllPlayerManager().values()) {
				damages.put(playerManager, playerManager.getDamages());
			}
			Map<PlayerManager, Integer> kills = new HashMap<>();
			for (PlayerManager playerManager : main.getAllPlayerManager().values()) {
				kills.put(playerManager, playerManager.getKills().size());
			}
			PlayerManager mostDamages = damages.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
					.findFirst().get().getKey();
			PlayerManager mostKills = kills.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
					.findFirst().get().getKey();

			Bukkit.broadcastMessage("§8§m-------------------------------------------------");
			Bukkit.broadcastMessage("§5");
			Bukkit.broadcastMessage("       " + winner.getWinMessage());
			Bukkit.broadcastMessage("       §cTop dégâts : " + mostDamages.getPlayerName() + " §l("
					+ mostDamages.getDamages() / 2 + " §4❤§c§l)");
			Bukkit.broadcastMessage(
					"       §cTop kills : " + mostKills.getPlayerName() + " §l(" + mostKills.getKills().size() + ")");
			Bukkit.broadcastMessage("§5");
			Bukkit.broadcastMessage("       §6Merci d'avoir participé à cet host de §e§l" + main.gameManager.getHostname());
			Bukkit.broadcastMessage("       §8Arrêt du serveur dans 5 minutes !");
			Bukkit.broadcastMessage("§8§m-------------------------------------------------");
			Bukkit.getOnlinePlayers().forEach(player -> TitleAPI.sendTitle(player, 20, 20, 20, winner.getWinMessage(), ""));

			Map<PlayerManager,Role> playersRoles = new HashMap<>();

			if (main.gameManager.getModes().getMode() instanceof RoleMode<?>) {
				for (PlayerManager joueurs : main.getAllPlayerManager().values()) {
					if (joueurs.hasRole()) {
						playersRoles.put(joueurs, joueurs.getRole());
					}
				}
			}
			for (Map.Entry<PlayerManager, Role> e : playersRoles.entrySet()) {
				Bukkit.broadcastMessage((e.getKey().isAlive() ? "§6§l" : "§6§m") + e.getKey().getPlayerName() + " : " + e.getValue().getRoleName() + e.getKey().getCamps().getCompoColor() +" (Camps: " + e.getKey().getCamps().name() + ")");
			}

			Bukkit.getScheduler().runTaskLater(this.main, () -> {
				if (this.main.getConfig().getBoolean("bungeecord")) {
					if (this.main.getConfig().getString("server-redirection") != null && !this.main
							.getConfig().getString("server-redirection").equalsIgnoreCase("null"))
						Bukkit.getOnlinePlayers().forEach(p -> BungeeAPI.ConnectBungeeServer(p,
								this.main.getConfig().getString("server-redirection")));
				}
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
			}, 5*60*20L);

			GameState.setCurrentState(GameState.ENDED);
		}

	}

	@Override
	public ModeManager getModeManager() {
		return narutoV2Manager;
	}

	@Override
	public void onNight() {

	}

	@Override
	public void onDay() {

	}

	@Override
	public int getRoleAnnouncement() {
		return announceRoles;
	}

	@Override
	public void setRoleAnnouncement(int roleAnnouncement) {
		announceRoles = roleAnnouncement;
	}
}
