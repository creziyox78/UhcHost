package fr.lastril.uhchost.scenario.scenarios;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.events.TeamUnregisteredEvent;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;

public class OnlyOneWinner extends Scenario {

	public OnlyOneWinner() {
		super("Only One Winner", Arrays.asList(I18n.tl("scenarios.onlyonewinner.lore", new String[0]),
				I18n.tl("scenarios.onlyonewinner.lore1", new String[0])), Material.NETHER_STAR);
	}

	@EventHandler
	public void onTeamUnregistered(TeamUnregisteredEvent event) {
		if ((UhcHost.getInstance()).scoreboardUtil.getBoard().getTeams().size() == 1) {
			OnlyOneWinnerState.setCurrentState(OnlyOneWinnerState.CROSSTEAM);
			(UhcHost.getInstance()).scoreboardUtil.getBoard().getTeams().forEach(t -> t.setAllowFriendlyFire(true));
			Bukkit.broadcastMessage(I18n.tl("scenarios.onlyonewinner.message", new String[0]));
			Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 1.0F, 1.0F));
		}
	}

	public enum OnlyOneWinnerState {
		NORMAL, CROSSTEAM;

		private static OnlyOneWinnerState currentState = NORMAL;

		static {

		}

		public static boolean isState(OnlyOneWinnerState state) {
			return (currentState == state);
		}

		public static void setCurrentState(OnlyOneWinnerState state) {
			currentState = state;
		}
	}

}
