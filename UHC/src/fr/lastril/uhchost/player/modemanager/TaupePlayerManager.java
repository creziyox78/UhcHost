package fr.lastril.uhchost.player.modemanager;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.tpg.KitTaupe;
import fr.lastril.uhchost.modes.tpg.Kits;
import fr.lastril.uhchost.modes.tpg.TaupeTeam;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.inventory.scoreboard.TeamUtils;

public class TaupePlayerManager {

    private final PlayerManager playerManager;

    private TaupeTeam moleTeam;

    private TeamUtils.Teams originalTeam;

    private boolean revealed, superRevealed, claimedKit;

    private final KitTaupe kitTaupe;

    public TaupePlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
        this.moleTeam = null;
        this.kitTaupe = setRandomKitTaupe();
    }

    public KitTaupe getKitTaupe() {
        return kitTaupe;
    }

    public KitTaupe setRandomKitTaupe(){
        Kits[] kits = Kits.values();
        return kits[UhcHost.getRANDOM().nextInt(Kits.values().length)].getKitTaupe();
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public void setOriginalTeam(TeamUtils.Teams originalTeam) {
        this.originalTeam = originalTeam;
    }

    public TeamUtils.Teams getOriginalTeam() {
        return originalTeam;
    }

    public void setSuperRevealed(boolean superRevealed) {
        this.superRevealed = superRevealed;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public boolean isSuperRevealed() {
        return superRevealed;
    }

    public void setMoleTeam(TaupeTeam moleTeam) {
        this.moleTeam = moleTeam;
    }

    public TaupeTeam getMoleTeam() {
        return moleTeam;
    }

    public void setClaimedKit(boolean claimedKit) {
        this.claimedKit = claimedKit;
    }

    public boolean isClaimedKit() {
        return claimedKit;
    }
}
