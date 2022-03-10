package fr.lastril.uhchost.player.modemanager;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.sm.MarketOwnerLocation;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.scoreboard.TeamUtils;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class MarketPlayerManager {

    private boolean owner;

    private boolean picked;

    private TeamUtils.Teams teams;

    private MarketOwnerLocation marketOwnerLocation;

    private final PlayerManager playerManager;

    private int diamonds, tradedDiamonds;

    public MarketPlayerManager(PlayerManager playerManager){
        this.playerManager = playerManager;
        this.picked = false;
        this.owner = false;
    }

    public void setRandomTeams(){
        TeamUtils teamUtils = UhcHost.getInstance().teamUtils;
        UhcHost.debug("Checking 1");
        int neededTeam = teamUtils.getNeededTeam();
        if(neededTeam == 0){
            teamUtils.setNeededTeam(9);
            for (TeamUtils.Teams teams : TeamUtils.Teams.values()) {
                teamUtils.registerTeam(teams);
            }
        }
        neededTeam = teamUtils.getNeededTeam();
        UhcHost.debug("Checking 2 : " + neededTeam);
        List<TeamUtils.Teams> tab = new ArrayList<>();
        int index = 0;
        for(TeamUtils.Teams teams : TeamUtils.Teams.values()){
            UhcHost.debug("Checking 3 : " + teams.getName());
            if(teamUtils.getPlayersInTeam(teams.getTeam()).size() == 0){
                tab.add(teams);
                index++;
                if(index == neededTeam){
                    break;
                }
            }
        }
        teams = tab.get(UhcHost.getRANDOM().nextInt(tab.size()));
        if(playerManager.getPlayer() != null){
            teamUtils.setTeam(playerManager.getPlayer(), teams.getTeam());
            for(MarketOwnerLocation marketOwnerLocation : MarketOwnerLocation.values()){
                if(marketOwnerLocation.getTeams() == teamUtils.getTeams(playerManager.getPlayer())){
                    UhcHost.debug("Set owner location");
                    this.marketOwnerLocation = marketOwnerLocation;
                }
            }
        }
    }

    public int getDiamonds() {
        return diamonds;
    }

    public int getTradedDiamonds() {
        return tradedDiamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }

    public void setTradedDiamonds(int tradedDiamonds) {
        this.tradedDiamonds = tradedDiamonds;
    }

    public void resetTeam(){
        TeamUtils teamUtils = UhcHost.getInstance().teamUtils;
        if(playerManager.getPlayer() != null){
            if(teamUtils.getTeam(playerManager.getPlayer())!=null){
                teamUtils.unsetTeam(playerManager.getPlayer(), teamUtils.getTeam(playerManager.getPlayer()));
            }

        }
    }

    public void setInOwnerTeam(PlayerManager ownerManager){
        TeamUtils teamUtils = UhcHost.getInstance().teamUtils;
        if(playerManager.getPlayer() != null && ownerManager.getPlayer() != null){
            teamUtils.setTeam(playerManager.getPlayer(), teamUtils.getTeam(ownerManager.getPlayer()));
            teams = ownerManager.getMarketPlayerManager().teams;
            Location ownerLocation = ownerManager.getMarketPlayerManager().getMarketOwnerLocation().getOwnerLocation();
            playerManager.getPlayer().teleport(new Location(ownerLocation.getWorld(), ownerLocation.getX(), ownerLocation.getY() + 5, ownerLocation.getZ()));
        }
    }

    public void setMarketOwnerLocation(MarketOwnerLocation marketOwnerLocation) {
        this.marketOwnerLocation = marketOwnerLocation;
    }

    public MarketOwnerLocation getMarketOwnerLocation() {
        return marketOwnerLocation;
    }

    public TeamUtils.Teams getTeams() {
        return teams;
    }

    public boolean isOwner() {
        return owner;
    }

    public boolean isPicked() {
        return picked;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }
}
