package fr.lastril.uhchost.modes.naruto.v2.crafter;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.roles.CustomRoleClass;
import fr.lastril.uhchost.player.PlayerManager;

public interface NarutoV2Role extends CustomRoleClass {


    Chakra getChakra();

    /**
     * @return
     * @deprecated {@link NarutoV2Role#getRandomChakra()} edited by Maygo
     */
    @Deprecated
    default Chakra randomChakra() {
        return getRandomChakra();
    }

    default Chakra getRandomChakra() {
        return Chakra.getRandomChakra();
    }

    void onPlayerUsedPower(PlayerManager PlayerManager);

    default void usePower(PlayerManager PlayerManager) {
        for (PlayerManager PlayerManager2 : UhcHost.getInstance().getPlayerManagerAlives()) {
            if (PlayerManager2.hasRole()) {
                if (PlayerManager2.getRole() instanceof NarutoV2Role) {
                    ((NarutoV2Role) PlayerManager2.getRole()).onPlayerUsedPower(PlayerManager);
                }
            }
        }
    }

}
