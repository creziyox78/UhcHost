package fr.lastril.uhchost.modes.naruto.v2.crafter;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.roles.CustomRoleClass;
import fr.lastril.uhchost.player.PlayerManager;

public interface NarutoV2Role extends CustomRoleClass {

	
	Chakra getChakra();

	/**
	 * @deprecated {@link NarutoV2Role#getRandomChakra()} edited by Maygo
	 * @return
	 */
	@Deprecated
	default Chakra randomChakra() {
		return getRandomChakra();
	}

	default Chakra getRandomChakra() {
		return Chakra.getRandomChakra();
	}

	void onPlayerUsedPower(PlayerManager joueur);
	default void onPlayerUsedSpecificPower(PlayerManager joueur, String technique){

	}
	default void usePower(PlayerManager joueur) {
		for(PlayerManager joueur2 : UhcHost.getInstance().getPlayerManagerAlives()) {
			if(joueur2.hasRole()) {
				if(joueur2.getRole() instanceof NarutoV2Role) {
					((NarutoV2Role) joueur2.getRole()).onPlayerUsedPower(joueur);
				}
			}
		}
	}

	default void usePowerSpecific(PlayerManager joueur, String technique) {
		for(PlayerManager joueur2 : UhcHost.getInstance().getPlayerManagerAlives()) {
			if(joueur2.hasRole()) {
				if(joueur2.getRole() instanceof NarutoV2Role) {
					((NarutoV2Role) joueur2.getRole()).onPlayerUsedSpecificPower(joueur, technique);
				}
			}
		}
	}

}
