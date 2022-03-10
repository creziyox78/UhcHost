package fr.lastril.uhchost.scoreboard;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.WorldState;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.bleach.BleachMode;
import fr.lastril.uhchost.modes.roles.RoleAnnounceMode;
import fr.lastril.uhchost.modes.tpg.TaupeGunMode;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.FormatTime;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardUtils {

	private final UhcHost pl;

	private final Scoreboard board;

	private final Map<UUID, ScoreboardSign> sbs = new HashMap<>();

	private String name;

	public ScoreboardUtils(UhcHost pl) {
		this.pl = pl;
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

		} else {
			sb = new ScoreboardSign(player, this.name);
			sb.create();
			this.sbs.put(player.getUniqueId(), sb);
		}
		int line = 0;
		sb.setLine(line++, formatLine("&r&8&m+------------------+", player, count));
		sb.setLine(line++, formatLine("   &f&l✯ &e&l&e&l{player_host_name} ", player, count));
		sb.setLine(line++, formatLine("&r&r&r", player, count));
		sb.setLine(line++, formatLine("   &f&l⚔ {gamemode} UHC", player, count));
		sb.setLine(line++, formatLine("&5", player, count));
		sb.setLine(line++, formatLine("   &f&l✔ &a&lJoueurs &f┃ &a&l{waitting_players}&7/&a&l{max_waitting_players}", player, count));
		sb.setLine(line++, formatLine("&r&r", player, count));
		sb.setLine(line++, formatLine("       &6&nmc.okenzai.com", player, count));
		sb.setLine(line++, formatLine("&8&m+-------------------+", player, count));
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
		sb.setLine(line++, formatLine("&r&8&m+------------------+", player, count));
		sb.setLine(line++, formatLine("   &f&l✯ &e&l&e&l{player_host_name} ", player, count));
		sb.setLine(line++, formatLine("&r&r&r", player, count));
		sb.setLine(line++, formatLine("   &f&l⚔ {gamemode} UHC", player, count));
		sb.setLine(line++, formatLine("&5", player, count));
		sb.setLine(line++, formatLine("   &f&l✔ &a&lJoueurs &f┃ &a&l{waitting_players}&7/&a&l{max_waitting_players}", player, count));
		sb.setLine(line++, formatLine("&r&r", player, count));
		sb.setLine(line++, formatLine("       &6&nmc.okenzai.com", player, count));
		sb.setLine(line++, formatLine("&8&m+-------------------+", player, count));
		/*File file = new File(pl.getDataFolder() + "/scoreboard.yml");
		YamlConfiguration lgYaml = YamlConfiguration.loadConfiguration(file);

		for(String lines : lgYaml.getStringList("status.lobby")){
			sb.setLine(line++, formatLine(lines, player, count));
		}*/
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

		if(pl.getGamemanager().getModes() == Modes.LG){
			sb.setLine(line++, formatLine("&r&7&m+------------------+", player, count));
			sb.setLine(line++, formatLine(" &8&l» &eTimer ┃ &r{time} &e&l{cycle}", player, count));
			sb.setLine(line++, formatLine(" &8&l» &eRôles ┃ &r{roles}", player, count));
			sb.setLine(line++, formatLine("&r", player, count));
			sb.setLine(line++, formatLine(" &8&l» &6Episode ┃ &r{episode}", player, count));
			sb.setLine(line++, formatLine(" &8&l» &6Bordure ┃ &r±{border}", player, count));
			sb.setLine(line++, formatLine("&r&r", player, count));
			sb.setLine(line++, formatLine(" &8&l» &c{players_ingame} &r&cJoueurs", player, count));
			sb.setLine(line++, formatLine(" &8&l» &cGroupe de &c{groupes}", player, count));
			sb.setLine(line++, formatLine("&1", player, count));
			sb.setLine(line++, formatLine("       &6&nmc.okenzai.com", player, count));
			sb.setLine(line++, formatLine("&r&r&7&m+------------------+", player, count));
		} else if(pl.getGamemanager().getModes() == Modes.TAUPEGUN){
			sb.setLine(line++, formatLine("&r&8&m⊱------------------⊰", player, count));
			sb.setLine(line++, formatLine(" &8&l» &eTimer ┃ &r{time} &7({nb_t})", player, count));
			sb.setLine(line++, formatLine(" &8&l» &eBordure ┃ &r±{border}", player, count));
			sb.setLine(line++, formatLine(" &8&l» &ePvP ┃ &r{pvp}", player, count));
			sb.setLine(line++, formatLine("&r&r&r&r", player, count));
			sb.setLine(line++, formatLine(" &8&l» &cTaupes ┃ &r{taupes}", player, count));
			sb.setLine(line++, formatLine("&1", player, count));
			sb.setLine(line++, formatLine(" &8&l» &cJoueurs ┃ &r{players_ingame}", player, count));
			sb.setLine(line++, formatLine(" &8&l» &cKills ┃ &r{player_kills}", player, count));
			sb.setLine(line++, formatLine("&r", player, count));
			sb.setLine(line++, formatLine("       &6&nmc.okenzai.com", player, count));
			sb.setLine(line++, formatLine("&r&r&8&m⊱------------------⊰", player, count));
		} else if(pl.getGamemanager().getModes() == Modes.CLASSIC){
			sb.setLine(line++, formatLine("&r&8&m+------------------+", player, count));
			sb.setLine(line++, formatLine(" &8&l» &eJoueurs ┃ &r{players_ingame} &7({nb_t})", player, count));
			sb.setLine(line++, formatLine(" &8&l» &eKills ┃ &r{player_kills}", player, count));
			sb.setLine(line++, formatLine("&a", player, count));
			sb.setLine(line++, formatLine(" &8&l» &6Timer ┃ &r{time}", player, count));
			sb.setLine(line++, formatLine(" &8&l» &6PvP ┃ &r{pvp}", player, count));
			sb.setLine(line++, formatLine("&o", player, count));
			sb.setLine(line++, formatLine(" &8&l» &aBordure ┃ &r{border_time}", player, count));
			sb.setLine(line++, formatLine(" &8&l» &aTaille ┃ &r±{border}", player, count));
			sb.setLine(line++, formatLine(" &8&l» &aCentre ┃ &r{spawn_direction}", player, count));
			sb.setLine(line++, formatLine("&d", player, count));
			sb.setLine(line++, formatLine("       &6&nmc.okenzai.com", player, count));
			sb.setLine(line++, formatLine("&r&r&8&m+------------------+", player, count));
		}  else if(pl.getGamemanager().getModes() == Modes.BLEACH){
			sb.setLine(line++, formatLine("&r&8&m+------------------+", player, count));
			sb.setLine(line++, formatLine(" &8&l» &eTimer ┃ &r{time}", player, count));
			sb.setLine(line++, formatLine(" &8&l» &eRôles ┃ &r{roles}", player, count));
			sb.setLine(line++, formatLine(" &8&l» &ePvP ┃ &r{pvp}", player, count));
			sb.setLine(line++, formatLine("&r", player, count));
			sb.setLine(line++, formatLine(" &8&l» &6Phase ┃ &r{phase}", player, count));
			sb.setLine(line++, formatLine("&3", player, count));
			sb.setLine(line++, formatLine(" &8&l» &cJoueurs Restant ┃ &f{players_ingame}", player, count));
			sb.setLine(line++, formatLine(" &8&l» &cGroupe de  ┃ &f{groupes}", player, count));
			sb.setLine(line++, formatLine("&r&r", player, count));
			sb.setLine(line++, formatLine("       &6&nmc.okenzai.com", player, count));
			sb.setLine(line++, formatLine("&r&r&8&m+------------------+", player, count));
		} else if(pl.getGamemanager().getModes() == Modes.SM){
			sb.setLine(line++, formatLine("&r&8&m+------------------+", player, count));
			sb.setLine(line++, formatLine(" &8&l» &eJoueurs ┃ &r{players_ingame} &7({nb_t})", player, count));
			sb.setLine(line++, formatLine(" &8&l» &eKills ┃ &r{player_kills}", player, count));
			sb.setLine(line++, formatLine("&7", player, count));
			sb.setLine(line++, formatLine(" &8&l» &6Timer ┃ &r{time}", player, count));
			sb.setLine(line++, formatLine(" &8&l» &6PvP ┃ &r{pvp}", player, count));
			sb.setLine(line++, formatLine("&2", player, count));
			sb.setLine(line++, formatLine(" &8&l» &aBordure ┃ &r{border_time}", player, count));
			sb.setLine(line++, formatLine(" &8&l» &aTaille ┃ &r±{border}", player, count));
			sb.setLine(line++, formatLine(" &8&l» &aCentre ┃ &r{spawn_direction}", player, count));
			sb.setLine(line++, formatLine("&4", player, count));
			sb.setLine(line++, formatLine("       &6&nmc.okenzai.com", player, count));
			sb.setLine(line++, formatLine("&r&r&8&m+------------------+", player, count));
		}
		this.sbs.replace(player.getUniqueId(), sb);
	}

	public Scoreboard getBoard() {
		return this.board;
	}

	public String formatLine(String lines, Player player, int count) {
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
				} else {
					newLine = newLine.replace("{roles}", roleTime <= 0 ? "Aucun" : new FormatTime(roleTime).toString());
				}
				newLine = newLine.replace("{phase}", String.valueOf(bleachMode.getPhase()));
			} else {
				newLine = newLine.replace("{roles}", roleTime <= 0 ? "§a✔" : new FormatTime(roleTime).toString());
			}
		}
		if(modes == Modes.TAUPEGUN){
			TaupeGunMode taupeGunMode = (TaupeGunMode) pl.getGamemanager().getModes().getMode();
			int taupesTime = taupeGunMode.getTaupeGunConfig().getMolesTime() - count;
			newLine = newLine.replace("{taupes}", taupesTime <= 0 ?"§a✔" : new FormatTime(taupesTime).toString());
		}
		if (GameState.isState(GameState.STARTED)) {
			Location loc = this.pl.worldUtils.getCenter().getWorld().getHighestBlockAt(
					this.pl.worldUtils.getCenter().getBlockX(), this.pl.worldUtils.getCenter().getBlockZ()).getLocation();
			loc.setY(player.getLocation().getY());
			if (loc.getWorld() == player.getWorld())
				newLine = newLine.replace("{spawn_direction}",
						ClassUtils.getDirectionOf(player.getLocation(), loc) +
								"(" + (int) player.getLocation().distance(loc.add(0, player.getLocation().getY(), 0))
								+ ")").replace("{border}",
						String.valueOf((int) this.pl.worldUtils.getWorld().getWorldBorder().getSize() / 2.0D));

		}
		int pvpTime = this.pl.taskManager.getPvpTime() - count;
		int borderTime = this.pl.taskManager.getBorderTime() - count;
		return newLine.replace("{pvp}", pvpTime <= 0 ? "§a✔" : new FormatTime(pvpTime).toString())
				.replace("{time}", new FormatTime(count).toString())
				.replace("{nb_t}", String.valueOf(pl.teamUtils.getAllTeamsAlives().size()))
				.replace("{border_time}", borderTime <= 0 ? "§a✔" : new FormatTime(borderTime).toString())
				.replace("{gamemode}", modes.getName())
				.replace("{player_host_name}", pl.gameManager.getHost() != null ? pl.gameManager.getHostname() : "Aucun")
				.replace("{waitting_players}", String.valueOf(Bukkit.getOnlinePlayers().size()))
				.replace("{max_waitting_players}", String.valueOf(pl.gameManager.getMaxPlayers()))
				.replace("{players_ingame}", String.valueOf(this.pl.getPlayerManagerAlives().size()))
				.replace("{player_kills}", playerManager != null && playerManager.getKills() != null? String.valueOf(playerManager.getKills().size()) : "")
				.replace("{episode}", String.valueOf(pl.gameManager.episode))
				.replace("{groupes}", String.valueOf(pl.gameManager.getGroupes()))
				.replace("{cycle}", WorldState.getWorldState() != null ? String.valueOf(WorldState.getWorldState()) : "?")
				.replace("&", "§");
	}

}
