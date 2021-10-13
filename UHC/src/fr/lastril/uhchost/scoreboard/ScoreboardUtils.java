package fr.lastril.uhchost.scoreboard;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.tasks.TaskManager;
import fr.lastril.uhchost.modes.roles.RoleAnnounceMode;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardUtils {

	private UhcHost pl;

	private Scoreboard board;

	private Map<UUID, ScoreboardSign> sbs = new HashMap<>();

	private String name;

	private String startIn;

	private String players;

	private String credit;

	private String ip;

	private String time;

	private String border;

	private String spawn;

	private String pvp;

	private String actived;

	private String activedFem;

	private String teleport;

	private String team;

	private String host;

	private String waitForPlayers;

	private String playersInTeam;

	private String playersInTeamEnd;

	public ScoreboardUtils(UhcHost pl) {
		this.pl = pl;
		this.startIn = I18n.tl("scoreboard.startIn", new String[0]);
		this.players = I18n.tl("scoreboard.players", new String[0]);
		this.credit = "§cPlugin by Lastril";
		this.ip = I18n.tl("scoreboard.ip", new String[0]);
		this.time = I18n.tl("scoreboard.time", new String[0]);
		this.border = I18n.tl("scoreboard.border", new String[0]);
		this.spawn = I18n.tl("scoreboard.spawn", new String[0]);
		this.pvp = I18n.tl("scoreboard.pvp", new String[0]);
		this.actived = I18n.tl("scoreboard.actived", new String[0]);
		this.activedFem = I18n.tl("scoreboard.activedFem", new String[0]);
		this.teleport = I18n.tl("scoreboard.teleport", new String[0]);
		this.team = I18n.tl("scoreboard.team", new String[0]);
		this.host = I18n.tl("scoreboard.host", new String[0]);
		this.waitForPlayers = I18n.tl("scoreboard.waitForPlayers", new String[0]);
		this.playersInTeam = I18n.tl("scoreboard.playersInTeam", new String[0]);
		this.playersInTeamEnd = I18n.tl("scoreboard.playersInTeamEnd", new String[0]);
		this.board = Bukkit.getScoreboardManager().getMainScoreboard();
		this.board.getTeams().forEach(t -> t.unregister());
		this.board.getObjectives().forEach(o -> o.unregister());
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
		sb.setLine(line++, "§1");
		sb.setLine(line++, "§bMode: " + pl.gameManager.getModes().getName());
		if (this.pl.getPlayerManagerAlives().size() < this.pl.gameManager.getPlayersBeforeStart()) {
			sb.setLine(line++, this.waitForPlayers);
		} else {
			sb.setLine(line++, this.startIn + count);
		}
		sb.setLine(line++, "§2");
		if (this.pl.gameManager.getHost() != null)
			sb.setLine(line++, this.host + this.pl.gameManager.getHost().getName());
		if (this.pl.teamUtils.getPlayersPerTeams() != 1) {
			Team t = this.pl.teamUtils.getTeam(player);
			sb.setLine(line++, this.team + ((t == null) ? "-"
					: (t.getName() + this.playersInTeam + t.getEntries().size() + this.playersInTeamEnd)));
			sb.setLine(line++, "§3");
		}
		sb.setLine(line++, this.players + this.pl.getPlayerManagerOnlines().size() + "/" + this.pl.gameManager.getMaxPlayers());

		sb.setLine(line++, "§8§m                   §r");
		sb.setLine(line++, this.credit);
		sb.setLine(line++, this.ip);
	}

	public void updatePreGame(Player player, int count) {
		ScoreboardSign sb;
		this.name = pl.getGamemanager().getGameName();
		if (this.sbs.containsKey(player.getUniqueId())) {
			sb = this.sbs.get(player.getUniqueId());
			/*if (sb.getObjectiveName()
					.equalsIgnoreCase(this.name + this.teamsOf + this.pl.teamUtils.getPlayersPerTeams())
					&& this.pl.teamUtils.getPlayersPerTeams() == 1) {
				sb.setObjectiveName(this.name + this.solo);
			} else if (sb.getObjectiveName().equalsIgnoreCase(this.name + this.solo)
					&& this.pl.teamUtils.getPlayersPerTeams() != 1) {
				sb.setObjectiveName(this.name + this.teamsOf + this.pl.teamUtils.getPlayersPerTeams());
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
		sb.setLine(line++, "§1");
		sb.setLine(line++, this.startIn + count);
		if (this.pl.gameManager.getHost() != null)
			sb.setLine(line++, this.host + this.pl.gameManager.getHost().getName());
		sb.setLine(line++, "§2");
		if (this.pl.teamUtils.getPlayersPerTeams() != 1) {
			sb.setLine(line++,
					this.team + ((this.pl.teamUtils.getTeam(player) == null) ? "-"
							: (this.pl.teamUtils.getTeam(player).getName() + this.playersInTeam
									+ this.pl.teamUtils.getTeam(player).getEntries().size() + this.playersInTeamEnd)));
			sb.setLine(line++, "");
		}

		sb.setLine(line++, this.players + this.pl.getPlayerManagerOnlines().size());
		sb.setLine(line++, "§8§m                   §r");
		sb.setLine(line++, this.credit);
		sb.setLine(line++, this.ip);
		this.sbs.replace(player.getUniqueId(), sb);
	}

	public void updateGame(Player player, int count) {
		ScoreboardSign sb;
		this.name = pl.getGamemanager().getGameName();
		if (this.sbs.containsKey(player.getUniqueId())) {
			sb = this.sbs.get(player.getUniqueId());
			sb.setObjectiveName(this.name);
			/*if (sb.getObjectiveName()
					.equalsIgnoreCase(this.name + this.teamsOf + this.pl.teamUtils.getPlayersPerTeams())
					&& this.pl.teamUtils.getPlayersPerTeams() == 1) {
				sb.setObjectiveName(this.name + this.solo);
			} else if (sb.getObjectiveName().equalsIgnoreCase(this.name + this.solo)
					&& this.pl.teamUtils.getPlayersPerTeams() != 1) {
				sb.setObjectiveName(this.name + this.teamsOf + this.pl.teamUtils.getPlayersPerTeams());
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
		if (this.pl.gameManager.getHost() != null)
			sb.setLine(line++, this.host + this.pl.gameManager.getHost().getName());

		sb.setLine(line++, "§r");
		Location loc = this.pl.worldUtils.getCenter().getWorld().getHighestBlockAt(
				this.pl.worldUtils.getCenter().getBlockX(), this.pl.worldUtils.getCenter().getBlockZ()).getLocation();
		loc.setY(player.getLocation().getY());
		if(player.getWorld() == loc.getWorld())
			sb.setLine(line++, this.spawn + getDirectionOf(player.getLocation(), loc) + " (" + (int) player.getLocation().distance(loc.add(0, player.getLocation().getY(), 0)) + ")");
		else
			sb.setLine(line++, this.spawn + "?");
		sb.setLine(line++, this.players + this.pl.getPlayerManagerAlives().size());
		if (this.pl.teamUtils.getPlayersPerTeams() != 1)
			sb.setLine(line++,
					this.team + ((this.pl.teamUtils.getTeam(player) == null) ? "-"
							: (this.pl.teamUtils.getTeam(player).getName() + this.playersInTeam
									+ this.pl.teamUtils.getTeam(player).getEntries().size() + this.playersInTeamEnd)));
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
		if (this.pl.gameManager.getModes().getMode() instanceof RoleAnnounceMode) {
			RoleAnnounceMode roleAnnounceMode = (RoleAnnounceMode) this.pl.gameManager.getModes().getMode();
			int roleTime = roleAnnounceMode.getRoleAnnouncement();

			if(!roleAnnounceMode.isRoleAnnonced(roleTime)){
				if (roleTime / 60 < 10 && roleTime % 60 < 10) {
					sb.setLine(line++, "§3Rôles:§b " + "0" + (roleTime / 60) + ":0" + (roleTime % 60));
				} else if (roleTime / 60 < 10) {
					sb.setLine(line++, "§3Rôles:§b " + "0" + (roleTime / 60) + ":" + (roleTime % 60));
				} else if (roleTime % 60 < 10) {
					sb.setLine(line++, "§3Rôles:§b " + (roleTime / 60) + ":0" + (roleTime % 60));
				} else {
					sb.setLine(line++, "§3Rôles:§b " + (roleTime / 60) + ":" + (roleTime % 60));
				}
			} else {
				sb.setLine(line++, "§3Rôles:§b " + this.actived);
			}
		}
		if (!this.pl.gameManager.isPvp()) {
			int pvpTime = this.pl.taskManager.getPvpTime() - count;
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
		if (this.pl.gameManager.isFightTeleport()) {
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
		sb.setLine(line++, "§r  ");
		sb.setLine(line++, this.border + (int) (this.pl.worldUtils.getWorld().getWorldBorder().getSize() / 2.0D) + "/-"
				+ (int) (this.pl.worldUtils.getWorld().getWorldBorder().getSize() / 2.0D));
		sb.setLine(line++, "§8§m                   §r");
		sb.setLine(line++, this.credit);
		sb.setLine(line++, this.ip);
		this.sbs.replace(player.getUniqueId(), sb);
	}

	public Scoreboard getBoard() {
		return this.board;
	}

	public String getDirectionOf(Location ploc, Location to) {
		if(ploc.getWorld() != to.getWorld()) return "?";
		ploc.setY(0.0D);
		to.setY(0.0D);

		String[] arrows = {"⬆", "⬈", "➡", "⬊", "⬇", "⬋", "⬅", "⬉", "⬆"};
		Vector d = ploc.getDirection();

		Vector v = to.subtract(ploc).toVector().normalize();

		double a = Math.toDegrees(Math.atan2(d.getX(), d.getZ()));
		a -= Math.toDegrees(Math.atan2(v.getX(), v.getZ()));

		a = ((int)(a + 22.5D) % 360);

		if (a < 0.0D) {
			a += 360.0D;
		}

		/*String color = "§4";
		if(ploc.distance(to) > 750 && ploc.distance(to) < 1000) {
			color = "§c";
		}else if(ploc.distance(to) > 500 && ploc.distance(to) < 750) {
			color = "§6";
		}else if(ploc.distance(to) > 250 && ploc.distance(to) < 500) {
			color = "§e";
		}else if(ploc.distance(to) < 250) {
			color = "§a";
		}*/

		return arrows[((int) a / 45)];
	}

}
