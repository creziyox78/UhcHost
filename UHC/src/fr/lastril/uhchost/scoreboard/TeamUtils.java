package fr.lastril.uhchost.scoreboard;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.BannerCreator;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.stream.Collectors;

public class TeamUtils {

	private final UhcHost pl;

	private final Scoreboard scoreboard;

	private int playersPerTeams = 1;

	private int neededTeam;

	private boolean setup = false;

	public TeamUtils(UhcHost pl, Scoreboard scoreboard) {
		this.pl = pl;
		this.scoreboard = scoreboard;
	}

	public int getNeededTeam() {
		return this.neededTeam;
	}

	public int getPlayersPerTeams() {
		return this.playersPerTeams;
	}

	public void setPlayersPerTeams(int playersPerTeams) {
		this.playersPerTeams = playersPerTeams;
	}

	public void resetTeams() {
		this.scoreboard.getTeams().forEach(Team::unregister);
		this.setup = false;
		for (UUID uuid : this.pl.getPlayerManagerOnlines().stream().map(PlayerManager::getUuid).collect(Collectors.toList())) {
			Player player = Bukkit.getPlayer(uuid);
			for (ListIterator<ItemStack> listIterator = Bukkit.getPlayer(uuid).getInventory().iterator(); listIterator
					.hasNext();) {
				ItemStack itemStack = listIterator.next();
				if (itemStack != null && itemStack.getType() == Material.BANNER)
					player.getInventory().remove(itemStack);
			}
			player.setPlayerListName(player.getName());
			player.setCustomNameVisible(true);
			player.setCustomName(player.getName());
			player.setDisplayName(player.getName());
		}
	}

	public void setupTeams() {
		this.neededTeam = this.pl.gameManager.getMaxPlayers() / this.playersPerTeams;
		if (this.pl.gameManager.getMaxPlayers() % this.playersPerTeams != 0)
			this.neededTeam++;
		if (this.setup)
			return;
		this.setup = true;
		for (Teams teams : Teams.values()) {
			teams.setTeam(this.scoreboard.registerNewTeam(teams.getName()));
			teams.getTeam().setPrefix(teams.getPrefix());
			teams.getTeam().setSuffix("§r");
			teams.getTeam().setNameTagVisibility(NameTagVisibility.ALWAYS);
			teams.getTeam().setAllowFriendlyFire(false);
		}
		if (this.playersPerTeams != 1)
			for (UUID uuid : this.pl.getAllPlayerManager().keySet()) {
				Bukkit.getPlayer(uuid).getInventory()
						.addItem((new ItemsCreator(Material.BANNER, "Equipes", null)).create());
			}
	}

	public void registerTeam(Teams teams) {
		teams.setTeam(this.scoreboard.registerNewTeam(teams.getName()));
		teams.getTeam().setPrefix(teams.getPrefix());
		teams.getTeam().setSuffix("§r");
		teams.getTeam().setNameTagVisibility(NameTagVisibility.ALWAYS);
		teams.getTeam().setAllowFriendlyFire(false);
	}

	public Team getTeam(Player player) {
		Team result = null;
		for (Teams teams : Teams.values()) {
			if (teams.getTeam() != null && this.pl.scoreboardUtil.getBoard().getTeams().contains(teams.getTeam())
					&& teams.getTeam().hasEntry(player.getName())) {
				result = teams.getTeam();
				break;
			}
		}
		return result;
	}

	public Teams getTeams(Player player) {
		Teams result = null;
		for (Teams teams : Teams.values()) {
			if (teams.getTeam() != null && this.pl.scoreboardUtil.getBoard().getTeams().contains(teams.getTeam())
					&& teams.getTeam().hasEntry(player.getName())) {
				result = teams;
				break;
			}
		}
		return result;
	}

	public void setTeam(Player player, Team team) {
		team.addEntry(player.getName());
		setPlayerName(player, getTeams(player));
	}
	
	public void unsetTeam(Player player, Team team) {
		team.removeEntry(player.getName());
		resetPlayerName(player);
	}
	
	public void resetPlayerName(Player player) {
		player.setPlayerListName(player.getName());
		player.setCustomNameVisible(true);
		player.setCustomName(player.getName());
		player.setDisplayName(player.getName());
	}

	public void setPlayerName(Player player, Teams teams) {
		if (teams.getName().equalsIgnoreCase("Vert") || teams.getName().contains("Bleu")) {
			player.setPlayerListName(teams.getPrefix() + " " + player.getName());
			player.setCustomNameVisible(true);
			player.setCustomName(teams.getPrefix() + " - " + player.getName());
			player.setDisplayName(teams.getPrefix() + " - " + player.getName());
		} else {
			player.setPlayerListName(teams.getPrefix() + " " + player.getName());
			player.setCustomNameVisible(true);
			player.setCustomName(teams.getPrefix() + " - " + player.getName());
			player.setDisplayName(teams.getPrefix() + " - " + player.getName());
		}
	}

	public void setAutoTeam(Player player) {
		if (getTeam(player) != null)
			return;
		for (Teams teams : Teams.values()) {
			if (teams.getTeam().getEntries().size() < this.playersPerTeams) {
				teams.getTeam().addEntry(player.getName());
				setPlayerName(player, teams);
				break;
			}
		}
	}

	public List<Player> getPlayersInTeam(Team team){
		List<Player> players = new ArrayList<>();
		for (String playerName : team.getEntries()) {
			Player player = Bukkit.getPlayer(playerName);
			if(player != null){
				players.add(player);
			}
		}
		return players;
	}

	public List<Team> getAllTeamsAlives(){
		List<Team> teams = new ArrayList<>();
		for(PlayerManager playerManager : pl.getPlayerManagerAlives()){
			if(playerManager.isAlive()){
				Player player = playerManager.getPlayer();
				if(player != null){
					Team playerTeam = getTeam(player);
					if(playerTeam != null && !teams.contains(playerTeam)){
						teams.add(playerTeam);
					}
				}
			}
		}
		return teams;
	}

	public enum Teams {
		RED("Rouge", "§c", new BannerCreator("§c", null, 1, false), DyeColor.RED, null, null),
		BLUE("Bleu", "§9", new BannerCreator("§9", null, 1, false), DyeColor.BLUE, null, null),
		GREEN("Vert", "§2", new BannerCreator("§2", null, 1, false), DyeColor.GREEN, null, null),
		YELLOW("Jaune", "§e", new BannerCreator("§e", null, 1, false), DyeColor.YELLOW, null, null),
		MAGENTA("Magenta", "§5", new BannerCreator("§5", null, 1, false), DyeColor.MAGENTA, null, null),
		ORANGE("Orange", "§6", new BannerCreator("§6", null, 1, false), DyeColor.ORANGE, null, null),
		PINK("Rose", "§d", new BannerCreator("§d", null, 1, false), DyeColor.PINK, null, null),
		LIGHT_BLUE("Bleu Clair", "§b", new BannerCreator("§b", null, 1, false), DyeColor.LIGHT_BLUE, null, null),
		LIGHT_GREEN("Vert Clair", "§a", new BannerCreator("§a", null, 1, false), DyeColor.LIME, null, null),
		FLOWER_RED("✿Rouge", "§c✿", new BannerCreator("§c", null, 1, false), DyeColor.RED,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.FLOWER),
						new Pattern(DyeColor.YELLOW, PatternType.CIRCLE_MIDDLE)),
				null),
		FLOWER_BLUE("✿Bleu", "§9✿", new BannerCreator("§9", null, 1, false), DyeColor.BLUE,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.FLOWER),
						new Pattern(DyeColor.YELLOW, PatternType.CIRCLE_MIDDLE)),
				null),
		FLOWER_GREEN("✿Vert", "§2✿", new BannerCreator("§2", null, 1, false), DyeColor.GREEN,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.FLOWER),
						new Pattern(DyeColor.YELLOW, PatternType.CIRCLE_MIDDLE)),
				null),
		FLOWER_YELLOW("✿Jaune", "§e✿", new BannerCreator("§e", null, 1, false), DyeColor.YELLOW,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.FLOWER),
						new Pattern(DyeColor.RED, PatternType.CIRCLE_MIDDLE)),
				null),
		FLOWER_MAGENTA("✿Magenta", "§5✿", new BannerCreator("§5", null, 1, false), DyeColor.MAGENTA,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.FLOWER),
						new Pattern(DyeColor.YELLOW, PatternType.CIRCLE_MIDDLE)),
				null),
		FLOWER_ORANGE("✿Orange", "§6✿", new BannerCreator("§6", null, 1, false), DyeColor.ORANGE,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.FLOWER),
						new Pattern(DyeColor.YELLOW, PatternType.CIRCLE_MIDDLE)),
				null),
		FLOWER_PINK("✿Rose", "§d✿", new BannerCreator("§d", null, 1, false), DyeColor.PINK,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.FLOWER),
						new Pattern(DyeColor.YELLOW, PatternType.CIRCLE_MIDDLE)),
				null),
		FLOWER_LIGHT_BLUE("✿Bleu Clair", "§b✿", new BannerCreator("§b", null, 1, false), DyeColor.LIGHT_BLUE,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.FLOWER),
						new Pattern(DyeColor.YELLOW, PatternType.CIRCLE_MIDDLE)),
				null),
		FLOWER_LIGHT_GREEN("✿Vert Clair", "§a✿", new BannerCreator("§a", null, 1, false), DyeColor.LIME,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.FLOWER),
						new Pattern(DyeColor.YELLOW, PatternType.CIRCLE_MIDDLE)),
				null),
		SKELETON_RED("☠Rouge", "§c☠", new BannerCreator("§c", null, 1, false), DyeColor.RED,
				Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.SKULL)), null),
		SKELETON_BLUE("☠Bleu", "§9☠", new BannerCreator("§9", null, 1, false), DyeColor.BLUE,
				Arrays.asList(new Pattern(DyeColor.RED, PatternType.SKULL)), null),
		SKELETON_GREEN("☠Vert", "§2☠", new BannerCreator("§2", null, 1, false), DyeColor.GREEN,
				Arrays.asList(new Pattern(DyeColor.RED, PatternType.SKULL)), null),
		SKELETON_YELLOW("☠Jaune", "§e☠", new BannerCreator("§e", null, 1, false), DyeColor.YELLOW,
				Arrays.asList(new Pattern(DyeColor.RED, PatternType.SKULL)), null),
		SKELETON_MAGENTA("☠Magenta", "§5☠", new BannerCreator("§5", null, 1, false), DyeColor.MAGENTA,
				Arrays.asList(new Pattern(DyeColor.RED, PatternType.SKULL)), null),
		SKELETON_ORANGE("☠Orange", "§6☠", new BannerCreator("§6", null, 1, false), DyeColor.ORANGE,
				Arrays.asList(new Pattern(DyeColor.RED, PatternType.SKULL)), null),
		SKELETON_PINK("☠Rose", "§d☠", new BannerCreator("§d", null, 1, false), DyeColor.PINK,
				Arrays.asList(new Pattern(DyeColor.RED, PatternType.SKULL)), null),
		SKELETON_LIGHT_BLUE("☠Bleu Clair", "§b☠", new BannerCreator("§b", null, 1, false), DyeColor.LIGHT_BLUE,
				Arrays.asList(new Pattern(DyeColor.RED, PatternType.SKULL)), null),
		SKELETON_LIGHT_GREEN("☠Vert Clair", "§a☠", new BannerCreator("§a", null, 1, false), DyeColor.LIME,
				Arrays.asList(new Pattern(DyeColor.RED, PatternType.SKULL)), null),
		YINYANG_RED("☯Rouge", "§c☯", new BannerCreator("§c", null, 1, false), DyeColor.RED,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT),
						new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_LEFT),
						new Pattern(DyeColor.RED, PatternType.SQUARE_BOTTOM_RIGHT),
						new Pattern(DyeColor.RED, PatternType.TRIANGLES_TOP),
						new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_BOTTOM),
						new Pattern(DyeColor.RED, PatternType.STRIPE_LEFT),
						new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT)),
				null),
		YINYANG_BLUE("☯Bleu", "§9☯", new BannerCreator("§9", null, 1, false), DyeColor.BLUE,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT),
						new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_LEFT),
						new Pattern(DyeColor.BLUE, PatternType.SQUARE_BOTTOM_RIGHT),
						new Pattern(DyeColor.BLUE, PatternType.TRIANGLES_TOP),
						new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_BOTTOM),
						new Pattern(DyeColor.BLUE, PatternType.STRIPE_LEFT),
						new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT)),
				null),
		YINYANG_GREEN("☯Vert", "§2☯", new BannerCreator("§2", null, 1, false), DyeColor.GREEN,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT),
						new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_LEFT),
						new Pattern(DyeColor.GREEN, PatternType.SQUARE_BOTTOM_RIGHT),
						new Pattern(DyeColor.GREEN, PatternType.TRIANGLES_TOP),
						new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_BOTTOM),
						new Pattern(DyeColor.GREEN, PatternType.STRIPE_LEFT),
						new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT)),
				null),
		YINYANG_YELLOW("☯Jaune", "§e☯", new BannerCreator("§e", null, 1, false), DyeColor.YELLOW,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT),
						new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_LEFT),
						new Pattern(DyeColor.YELLOW, PatternType.SQUARE_BOTTOM_RIGHT),
						new Pattern(DyeColor.YELLOW, PatternType.TRIANGLES_TOP),
						new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_BOTTOM),
						new Pattern(DyeColor.YELLOW, PatternType.STRIPE_LEFT),
						new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT)),
				null),
		YINYANG_MAGENTA("☯Mangeta", "§5☯", new BannerCreator("§5", null, 1, false), DyeColor.MAGENTA,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT),
						new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_LEFT),
						new Pattern(DyeColor.MAGENTA, PatternType.SQUARE_BOTTOM_RIGHT),
						new Pattern(DyeColor.MAGENTA, PatternType.TRIANGLES_TOP),
						new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_BOTTOM),
						new Pattern(DyeColor.MAGENTA, PatternType.STRIPE_LEFT),
						new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT)),
				null),
		YINYANG_ORANGE("☯Orange", "§6☯", new BannerCreator("§6", null, 1, false), DyeColor.ORANGE,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT),
						new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_LEFT),
						new Pattern(DyeColor.ORANGE, PatternType.SQUARE_BOTTOM_RIGHT),
						new Pattern(DyeColor.ORANGE, PatternType.TRIANGLES_TOP),
						new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_BOTTOM),
						new Pattern(DyeColor.ORANGE, PatternType.STRIPE_LEFT),
						new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT)),
				null),
		YINYANG_PINK("☯Rose", "§d☯", new BannerCreator("§d", null, 1, false), DyeColor.PINK,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT),
						new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_LEFT),
						new Pattern(DyeColor.PINK, PatternType.SQUARE_BOTTOM_RIGHT),
						new Pattern(DyeColor.PINK, PatternType.TRIANGLES_TOP),
						new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_BOTTOM),
						new Pattern(DyeColor.PINK, PatternType.STRIPE_LEFT),
						new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT)),
				null),
		YINYANG_LIGHT_BLUE("☯Bleu Clair", "§b☯", new BannerCreator("§b", null, 1, false), DyeColor.LIGHT_BLUE,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT),
						new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_LEFT),
						new Pattern(DyeColor.LIGHT_BLUE, PatternType.SQUARE_BOTTOM_RIGHT),
						new Pattern(DyeColor.LIGHT_BLUE, PatternType.TRIANGLES_TOP),
						new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_BOTTOM),
						new Pattern(DyeColor.LIGHT_BLUE, PatternType.STRIPE_LEFT),
						new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT)),
				null),
		YINYANG_LIGHT_GREEN("☯Vert Clair", "§a☯", new BannerCreator("§a", null, 1, false), DyeColor.LIME,
				Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT),
						new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_LEFT),
						new Pattern(DyeColor.LIME, PatternType.SQUARE_BOTTOM_RIGHT),
						new Pattern(DyeColor.LIME, PatternType.TRIANGLES_TOP),
						new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_BOTTOM),
						new Pattern(DyeColor.LIME, PatternType.STRIPE_LEFT),
						new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT)),
				null),
		PEACE_RED("❂Rouge", "§c❂", new BannerCreator("§c", null, 1, false), DyeColor.RED,
				Arrays.asList(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_DOWNLEFT),
						new Pattern(DyeColor.YELLOW, PatternType.STRIPE_DOWNRIGHT),
						new Pattern(DyeColor.RED, PatternType.HALF_HORIZONTAL),
						new Pattern(DyeColor.YELLOW, PatternType.STRIPE_CENTER)),
				null),
		PEACE_BLUE("❂Bleu", "§9❂", new BannerCreator("§9", null, 1, false), DyeColor.BLUE,
				Arrays.asList(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_DOWNLEFT),
						new Pattern(DyeColor.YELLOW, PatternType.STRIPE_DOWNRIGHT),
						new Pattern(DyeColor.BLUE, PatternType.HALF_HORIZONTAL),
						new Pattern(DyeColor.YELLOW, PatternType.STRIPE_CENTER)),
				null),
		PEACE_GREEN("❂Vert", "§2❂", new BannerCreator("§2", null, 1, false), DyeColor.GREEN,
				Arrays.asList(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_DOWNLEFT),
						new Pattern(DyeColor.YELLOW, PatternType.STRIPE_DOWNRIGHT),
						new Pattern(DyeColor.GREEN, PatternType.HALF_HORIZONTAL),
						new Pattern(DyeColor.YELLOW, PatternType.STRIPE_CENTER)),
				null),
		PEACE_YELLOW("❂Jaune", "§e❂", new BannerCreator("§e", null, 1, false), DyeColor.YELLOW,
				Arrays.asList(new Pattern(DyeColor.RED, PatternType.STRIPE_DOWNLEFT),
						new Pattern(DyeColor.RED, PatternType.STRIPE_DOWNRIGHT),
						new Pattern(DyeColor.YELLOW, PatternType.HALF_HORIZONTAL),
						new Pattern(DyeColor.RED, PatternType.STRIPE_CENTER)),
				null),
		PEACE_MAGENTA("❂Magenta", "§5❂", new BannerCreator("§5", null, 1, false), DyeColor.MAGENTA,
				Arrays.asList(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_DOWNLEFT),
						new Pattern(DyeColor.YELLOW, PatternType.STRIPE_DOWNRIGHT),
						new Pattern(DyeColor.MAGENTA, PatternType.HALF_HORIZONTAL),
						new Pattern(DyeColor.YELLOW, PatternType.STRIPE_CENTER)),
				null),
		PEACE_ORANGE("❂Orange", "§6❂", new BannerCreator("", null, 1, false), DyeColor.ORANGE,
				Arrays.asList(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_DOWNLEFT),
						new Pattern(DyeColor.YELLOW, PatternType.STRIPE_DOWNRIGHT),
						new Pattern(DyeColor.ORANGE, PatternType.HALF_HORIZONTAL),
						new Pattern(DyeColor.YELLOW, PatternType.STRIPE_CENTER)),
				null),
		PEACE_PINK("❂Rose", "§d❂", new BannerCreator("§d", null, 1, false), DyeColor.PINK,
				Arrays.asList(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_DOWNLEFT),
						new Pattern(DyeColor.YELLOW, PatternType.STRIPE_DOWNRIGHT),
						new Pattern(DyeColor.PINK, PatternType.HALF_HORIZONTAL),
						new Pattern(DyeColor.YELLOW, PatternType.STRIPE_CENTER)),
				null),
		PEACE_LIGHT_BLUE("❂Bleu Clair", "§b❂", new BannerCreator("§b", null, 1, false), DyeColor.LIGHT_BLUE,
				Arrays.asList(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_DOWNLEFT),
						new Pattern(DyeColor.YELLOW, PatternType.STRIPE_DOWNRIGHT),
						new Pattern(DyeColor.LIGHT_BLUE, PatternType.HALF_HORIZONTAL),
						new Pattern(DyeColor.YELLOW, PatternType.STRIPE_CENTER)),
				null),
		PEACE_LIGHT_GREEN("❂Vert Clair", "§a❂", new BannerCreator("§a", null, 1, false), DyeColor.LIME,
				Arrays.asList(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_DOWNLEFT),
						new Pattern(DyeColor.YELLOW, PatternType.STRIPE_DOWNRIGHT),
						new Pattern(DyeColor.LIME, PatternType.HALF_HORIZONTAL),
						new Pattern(DyeColor.YELLOW, PatternType.STRIPE_CENTER)),
				null)

		;

		private String name;

		private String prefix;

		private BannerCreator bannerCreator;

		private DyeColor baseColor;

		private List<Pattern> patterns;

		private Team team;

		Teams(String name, String prefix, BannerCreator bannerCreator, DyeColor baseColor, List<Pattern> patterns,
				Team team) {
			this.name = name;
			this.prefix = prefix;
			this.bannerCreator = bannerCreator;
			this.baseColor = baseColor;
			this.patterns = patterns;
			this.team = team;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPrefix() {
			return this.prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}

		public BannerCreator getBannerCreator() {
			return this.bannerCreator;
		}

		public void setBannerCreator(BannerCreator bannerCreator) {
			this.bannerCreator = bannerCreator;
		}

		public DyeColor getBaseColor() {
			return this.baseColor;
		}

		public void setBaseColor(DyeColor baseColor) {
			this.baseColor = baseColor;
		}

		public List<Pattern> getPatterns() {
			return this.patterns;
		}

		public void setPatterns(List<Pattern> patterns) {
			this.patterns = patterns;
		}

		public Team getTeam() {
			return this.team;
		}

		public void setTeam(Team team) {
			this.team = team;
		}
	}

}
