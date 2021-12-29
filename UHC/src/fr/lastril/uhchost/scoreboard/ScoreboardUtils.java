package fr.lastril.uhchost.scoreboard;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.bleach.BleachMode;
import fr.lastril.uhchost.modes.roles.RoleAnnounceMode;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardUtils {

	private final UhcHost pl;

	private final Scoreboard board;

	private final Map<UUID, ScoreboardSign> sbs = new HashMap<>();

	private String name;

	private final String startIn;

	private final String players;

	private final String roles;

	private final String credit;

	private final String ip;

	private final String time;

	private final String border;

	private final String spawn;

	private final String pvp;

	private final String actived;

	private final String activedFem;

	private final String teleport;

	private final String team;

	private final String host;

	private final String waitForPlayers;

	private final String playersInTeam;

	private final String playersInTeamEnd;

	private int pvpTime;

	public ScoreboardUtils(UhcHost pl) {
		this.pl = pl;
		this.startIn = I18n.tl("scoreboard.startIn");
		this.players = I18n.tl("scoreboard.players");
		this.credit = "§cPlugin by Lastril";
		this.ip = I18n.tl("scoreboard.ip");
		this.time = I18n.tl("scoreboard.time");
		this.border = I18n.tl("scoreboard.border");
		this.roles = I18n.tl("scoreboard.roles");
		this.spawn = I18n.tl("scoreboard.spawn");
		this.pvp = I18n.tl("scoreboard.pvp");
		this.actived = I18n.tl("scoreboard.actived");
		this.activedFem = I18n.tl("scoreboard.activedFem");
		this.teleport = I18n.tl("scoreboard.teleport");
		this.team = I18n.tl("scoreboard.team");
		this.host = I18n.tl("scoreboard.host");
		this.waitForPlayers = I18n.tl("scoreboard.waitForPlayers");
		this.playersInTeam = I18n.tl("scoreboard.playersInTeam");
		this.playersInTeamEnd = I18n.tl("scoreboard.playersInTeamEnd");
		this.board = Bukkit.getScoreboardManager().getMainScoreboard();
		this.board.getTeams().forEach(Team::unregister);
		this.board.getObjectives().forEach(Objective::unregister);
	}

	public void reset(Player player) {
		ScoreboardSign sb = new ScoreboardSign(player, "");
		sb.create();
		this.sbs.remove(player.getUniqueId());
	}

	public void updateLobby(Player player, int count) {
		this.name = pl.getGamemanager().getGameName();
		ScoreboardSign sb;
		if (this.sbs.containsKey(player.getUniqueId())) {
			sb = this.sbs.get(player.getUniqueId());
			sb.setObjectiveName(this.name);

			/*if (sb.getObjectiveName().contains(this.name + this.teamsOf)
					&& this.pl.teamUtils.getPlayersPerTeams() == 1) {
				sb.setObjectiveName(this.name + this.solo);
			} else if (sb.getObjectiveName().contains(this.name + this.teamsOf)
					&& this.pl.teamUtils.getPlayersPerTeams() != this.lastTo) {
				sb.setObjectiveName(this.name + this.teamsOf + this.pl.teamUtils.getPlayersPerTeams());
				this.lastTo = this.pl.teamUtils.getPlayersPerTeams();
			} else if (sb.getObjectiveName().equalsIgnoreCase(this.name + this.solo)
					&& this.pl.teamUtils.getPlayersPerTeams() != 1) {
				sb.setObjectiveName(this.name + this.teamsOf + this.pl.teamUtils.getPlayersPerTeams());
				this.lastTo = this.pl.teamUtils.getPlayersPerTeams();
			}*/

		} else {
			sb = new ScoreboardSign(player, this.name);
			/*if (this.pl.teamUtils.getPlayersPerTeams() != 1) {
				sb = new ScoreboardSign(player, this.name + this.teamsOf + this.pl.teamUtils.getPlayersPerTeams());
			} else {
				sb = new ScoreboardSign(player, this.name + this.solo);
			}*/
			sb.create();
			this.sbs.put(player.getUniqueId(), sb);
		}
		int line = 0;
		File file = new File(pl.getDataFolder() + "/scoreboard.yml");
		YamlConfiguration lgYaml = YamlConfiguration.loadConfiguration(file);

		for(String lines : lgYaml.getStringList("status.lobby")){
			sb.setLine(line++, formatLine(lines, player, count));
		}
		//TODO DELETE IF WORK
		/*

		sb.setLine(line++, "§1");
		sb.setLine(line++, "§bMode: " + pl.gameManager.getModes().getName());
		if (this.pl.getPlayerManagerAlives().size() < this.pl.gameManager.getPlayersBeforeStart()) {
			sb.setLine(line++, this.waitForPlayers);
		} else {
			if(pl.getConfig().getBoolean("scoreboard.startIn"))
				sb.setLine(line++, this.startIn + count);
		}
		if(pl.getConfig().getBoolean("scoreboard.host")){
			sb.setLine(line++, "§2");
			if (this.pl.gameManager.getHost() != null)
				sb.setLine(line++, this.host + this.pl.gameManager.getHost().getName());
		}

		if(pl.getConfig().getBoolean("scoreboard.team")){
			if (this.pl.teamUtils.getPlayersPerTeams() != 1) {
				Team t = this.pl.teamUtils.getTeam(player);
				sb.setLine(line++, this.team + ((t == null) ? "-"
						: (t.getName() + this.playersInTeam + t.getEntries().size() + this.playersInTeamEnd)));
				sb.setLine(line++, "§3");
			}
		}

		if(pl.getConfig().getBoolean("scoreboard.players"))
			sb.setLine(line++, this.players + Bukkit.getOnlinePlayers().size() + "/" + this.pl.gameManager.getMaxPlayers());

		sb.setLine(line++, "§8§m                   §r");
		sb.setLine(line++, this.credit);
		if(pl.getConfig().getBoolean("scoreboard.ip"))
			sb.setLine(line++, this.ip);

		 */
		this.sbs.replace(player.getUniqueId(), sb);
	}

	public void updatePreGame(Player player, int count) {
		ScoreboardSign sb;
		this.name = pl.getGamemanager().getGameName();
		if (this.sbs.containsKey(player.getUniqueId())) {
			sb = this.sbs.get(player.getUniqueId());
			sb.setObjectiveName(this.name);
		} else {
			sb = new ScoreboardSign(player, this.name);
			sb.create();
			this.sbs.put(player.getUniqueId(), sb);
		}
		int line = 0;
		File file = new File(pl.getDataFolder() + "/scoreboard.yml");
		YamlConfiguration lgYaml = YamlConfiguration.loadConfiguration(file);

		for(String lines : lgYaml.getStringList("status.lobby")){
			sb.setLine(line++, formatLine(lines, player, count));
		}
		this.sbs.replace(player.getUniqueId(), sb);
	}

	public void updateGame(Player player, int count) {
		ScoreboardSign sb;
		this.name = pl.getGamemanager().getGameName();
		if (this.sbs.containsKey(player.getUniqueId())) {
			sb = this.sbs.get(player.getUniqueId());
			sb.setObjectiveName(this.name);
		} else {
			sb = new ScoreboardSign(player, this.name);
			sb.create();
			this.sbs.put(player.getUniqueId(), sb);
		}
		int line = 0;
		File file = new File(pl.getDataFolder() + "/scoreboard.yml");
		YamlConfiguration lgYaml = YamlConfiguration.loadConfiguration(file);

		for(String lines : lgYaml.getStringList("status.started.gamemode."
				+ pl.gameManager.getModes().name().toLowerCase())){
			sb.setLine(line++, formatLine(lines, player, count));
		}
		/* TODO DELETE IF WORK
		int line = 0;
		if(pl.getConfig().getBoolean("scoreboard.host")){
			if (this.pl.gameManager.getHost() != null)
				sb.setLine(line++, this.host + this.pl.gameManager.getHost().getName());

			sb.setLine(line++, "§r");
		}

		if(pl.getConfig().getBoolean("scoreboard.spawn")){
			Location loc = this.pl.worldUtils.getCenter().getWorld().getHighestBlockAt(
					this.pl.worldUtils.getCenter().getBlockX(), this.pl.worldUtils.getCenter().getBlockZ()).getLocation();
			loc.setY(player.getLocation().getY());
			if(player.getWorld() == loc.getWorld())
				sb.setLine(line++, this.spawn + getDirectionOf(player.getLocation(), loc) + " (" + (int) player.getLocation().distance(loc.add(0, player.getLocation().getY(), 0)) + ")");
			else
				sb.setLine(line++, this.spawn + "?");
		}

		if(pl.getConfig().getBoolean("scoreboard.players")){
			sb.setLine(line++, this.players + this.pl.getPlayerManagerAlives().size());
		}
		if(pl.getConfig().getBoolean("scoreboard.team")){
			if (this.pl.teamUtils.getPlayersPerTeams() != 1)
				sb.setLine(line++,
						this.team + ((this.pl.teamUtils.getTeam(player) == null) ? "-"
								: (this.pl.teamUtils.getTeam(player).getName() + this.playersInTeam
								+ this.pl.teamUtils.getTeam(player).getEntries().size() + this.playersInTeamEnd)));
		}


		if(pl.getConfig().getBoolean("scoreboard.time")){
			sb.setLine(line++, "§r");
			if (count / 60 < 10 && count % 60 < 10) {
				sb.setLine(line++, this.time + "0" + (count / 60) + ":0" + (count % 60));
			} else if (count / 60 < 10) {
				sb.setLine(line++, this.time + "0" + (count / 60) + ":" + (count % 60));
			} else if (count % 60 < 10) {
				sb.setLine(line++, this.time + (count / 60) + ":0" + (count % 60));
			} else {
				sb.setLine(line++, this.time + (count / 60) + ":" + (count % 60));
			}
		}

		if(pl.getConfig().getBoolean("scoreboard.roles")){
			if (this.pl.gameManager.getModes().getMode() instanceof RoleAnnounceMode) {
				RoleAnnounceMode roleAnnounceMode = (RoleAnnounceMode) this.pl.gameManager.getModes().getMode();
				int roleTime = roleAnnounceMode.getRoleAnnouncement();

				if(!roleAnnounceMode.isRoleAnnonced(roleTime)){
					if (roleTime / 60 < 10 && roleTime % 60 < 10) {
						sb.setLine(line++, this.roles + "0" + (roleTime / 60) + ":0" + (roleTime % 60));
					} else if (roleTime / 60 < 10) {
						sb.setLine(line++, this.roles + "0" + (roleTime / 60) + ":" + (roleTime % 60));
					} else if (roleTime % 60 < 10) {
						sb.setLine(line++, this.roles + (roleTime / 60) + ":0" + (roleTime % 60));
					} else {
						sb.setLine(line++, this.roles + (roleTime / 60) + ":" + (roleTime % 60));
					}
				} else {
					sb.setLine(line++, this.roles + this.actived);
				}
			}
		}

		if(pl.getConfig().getBoolean("scoreboard.pvp")){
			if (!this.pl.gameManager.isPvp()) {
				pvpTime = this.pl.taskManager.getPvpTime() - count;
				if (pvpTime / 60 < 10 && pvpTime % 60 < 10) {
					sb.setLine(line++, this.pvp + "0" + (pvpTime / 60) + ":0" + (pvpTime % 60));
				} else if (pvpTime / 60 < 10) {
					sb.setLine(line++, this.pvp + "0" + (pvpTime / 60) + ":" + (pvpTime % 60));
				} else if (pvpTime % 60 < 10) {
					sb.setLine(line++, this.pvp + (pvpTime / 60) + ":0" + (pvpTime % 60));
				} else {
					sb.setLine(line++, this.pvp + (pvpTime / 60) + ":" + (pvpTime % 60));
				}
			} else {
				sb.setLine(line++, this.pvp + this.actived);
			}
		}


		/*if (this.pl.gameManager.isFightTeleport()) {
			int teleportTime = this.pl.taskManager.getTeleportTime() - count;
			if (teleportTime / 60 < 10 && teleportTime % 60 < 10) {
				sb.setLine(line++, this.teleport + "0" + (teleportTime / 60) + ":0" + (teleportTime % 60));
			} else if (teleportTime / 60 < 10) {
				sb.setLine(line++, this.teleport + "0" + (teleportTime / 60) + ":" + (teleportTime % 60));
			} else if (teleportTime % 60 < 10) {
				sb.setLine(line++, this.teleport + (teleportTime / 60) + ":0" + (teleportTime % 60));
			} else {
				sb.setLine(line++, this.teleport + (teleportTime / 60) + ":" + (teleportTime % 60));
			}
		} else {
			sb.removeLine(line++);
		}
		if(pl.getConfig().getBoolean("scoreboard.border")){
			if (!this.pl.gameManager.isBorder()) {
				int b = this.pl.taskManager.getBorderTime() - count;
				if (b / 60 < 10 && b % 60 < 10) {
					sb.setLine(line++, this.border + "0" + (b / 60) + ":0" + (b % 60));
				} else if (b / 60 < 10) {
					sb.setLine(line++, this.border + "0" + (b / 60) + ":" + (b % 60));
				} else if (b % 60 < 10) {
					sb.setLine(line++, this.border + (b / 60) + ":0" + (b % 60));
				} else {
					sb.setLine(line++, this.border + (b / 60) + ":" + (b % 60));
				}
			} else {
				sb.setLine(line++, this.border + this.activedFem);
			}
		}


		if(pl.getConfig().getBoolean("scoreboard.border")){
			sb.setLine(line++, "§r  ");
			sb.setLine(line++, this.border + (int) (this.pl.worldUtils.getWorld().getWorldBorder().getSize() / 2.0D) + "/-"
					+ (int) (this.pl.worldUtils.getWorld().getWorldBorder().getSize() / 2.0D));
		}

		sb.setLine(line++, "§8§m                   §r");
		sb.setLine(line++, this.credit);
		if(pl.getConfig().getBoolean("scoreboard.ip"))
			sb.setLine(line++, this.ip);
		*/
		this.sbs.replace(player.getUniqueId(), sb);
	}

	public Scoreboard getBoard() {
		return this.board;
	}

	public String formatLine(String lines, Player player, int count) {
		pl.checkingScoreboardUpdate();
		PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
		String newLine = lines;
		Modes modes = pl.getGamemanager().getModes();
		if (modes.getMode() instanceof RoleAnnounceMode) {
			RoleAnnounceMode roleAnnounceMode = (RoleAnnounceMode) modes.getMode();
			int roleTime = roleAnnounceMode.getRoleAnnouncement();
			if(pl.getGamemanager().getModes() == Modes.BLEACH){
				BleachMode bleachMode = (BleachMode) modes.getMode();
				if(playerManager.hasRole()){
					newLine = newLine.replace("{roles}", roleTime <= 0 ? playerManager.getRole().getRoleName() : new FormatTime(roleTime).toString());
					newLine = newLine.replace("{phase}", String.valueOf(bleachMode.getPhase()));
				} else {
					newLine = newLine.replace("{roles}", roleTime <= 0 ? "Aucun" : new FormatTime(roleTime).toString());
				}
			} else {
				newLine = newLine.replace("{roles}", roleTime <= 0 ? "§a✔" : new FormatTime(roleTime).toString());
			}
		}
		if (GameState.isState(GameState.STARTED)) {
			Location loc = this.pl.worldUtils.getCenter().getWorld().getHighestBlockAt(
					this.pl.worldUtils.getCenter().getBlockX(), this.pl.worldUtils.getCenter().getBlockZ()).getLocation();
			loc.setY(player.getLocation().getY());
			if (loc.getWorld() == player.getWorld())
				newLine = newLine.replace("{spawn_direction}", ClassUtils.getDirectionOf(player.getLocation(), loc) + "(" + (int) player.getLocation().distance(loc.add(0, player.getLocation().getY(), 0)) + ")").replace("{border}", (int) (this.pl.worldUtils.getWorld().getWorldBorder().getSize() / 2.0D) + "/-"
						+ (int) (this.pl.worldUtils.getWorld().getWorldBorder().getSize() / 2.0D));

		}
		pvpTime = this.pl.taskManager.getPvpTime() - count;
		int borderTime = this.pl.taskManager.getBorderTime() - count;
		return newLine.replace("{pvp}", pvpTime <= 0 ? "§a✔" : new FormatTime(pvpTime).toString())
				.replace("{time}", new FormatTime(count).toString())
				.replace("{border_time}", borderTime <= 0 ? "§a✔" : new FormatTime(borderTime).toString())
				.replace("{gamemode}", modes.getName())
				.replace("{player_host_name}", pl.gameManager.getHost() != null ? pl.gameManager.getHost().getName() : "Aucun")
				.replace("{waitting_players}", String.valueOf(Bukkit.getOnlinePlayers().size()))
				.replace("{max_waitting_players}", String.valueOf(pl.gameManager.getMaxPlayers()))
				.replace("{players_ingame}", String.valueOf(this.pl.getPlayerManagerAlives().size()))
				.replace("{player_kills}", playerManager != null && playerManager.getKills() != null? String.valueOf(playerManager.getKills().size()) : "")
				.replace("{episode}", String.valueOf(pl.gameManager.episode))
				.replace("{groupes}", String.valueOf(pl.gameManager.getGroupes()))
				.replace("&", "§");
	}

}
