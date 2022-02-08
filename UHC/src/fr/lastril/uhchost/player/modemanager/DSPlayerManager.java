package fr.lastril.uhchost.player.modemanager;

import fr.lastril.uhchost.modes.ds.speciality.blades.Blades;
import fr.lastril.uhchost.modes.ds.speciality.train.TrainLocation;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DSPlayerManager {

    private final PlayerManager playerManager;

    private final PlayerManager targetManager;

    private final List<Location> blockPlaced;

    private final List<Location> yushiroArrows;

    private List<String> playerListName;

    private final ArrayList<Role> roleKilled;

    private String playerMoon;

    private TrainLocation currentWagon;

    private boolean isDead;

    private boolean infected;

    private boolean antidote;

    private boolean inCage;

    private boolean inTrain;

    private boolean inTemple;

    private boolean inCavern;

    private boolean killedByDaki;

    private boolean enteredTrainThisEpisode;

    private boolean enteredTempleThisEpisode;

    private boolean isEscapingAsMuichiro;

    private boolean asleep;

    private boolean takeKit;

    private boolean objectifGaved;

    private boolean hasKilledNakimeAsYushiro;

    private boolean nearFromYoriichi;

    private boolean listening;

    private boolean inBat;

    private boolean inventoryBlocked;

    private boolean convocation;

    private boolean craftedBlade;

    private boolean wantToGoConvocation;

    private boolean killedTarget;

    private int resistance;

    private int strenght;

    private int healthLostByShinobu;

    private int joinedTrainCount;

    private int nakimeBiwaLevel;

    private int blood;

    private long lastFight;

    private long deadTime;

    private long lastHitByNezuko;

    private long lastHitByYoriichi;

    private Location initialLocation;

    private Location initialLocationInEnd;

    private Blades blades;

    public DSPlayerManager(PlayerManager playerManager){
        this.playerManager = playerManager;
        this.infected = false;
        this.antidote = false;
        this.resistance = 0;
        this.strenght = 0;
        this.roleKilled = new ArrayList<>();
        this.blockPlaced = new ArrayList<>();
        this.inCage = false;
        this.inTrain = false;
        this.inTemple = false;
        this.inCavern = false;
        this.inBat = false;
        this.killedByDaki = false;
        this.isEscapingAsMuichiro = false;
        this.currentWagon = null;
        this.enteredTrainThisEpisode = false;
        this.enteredTempleThisEpisode = false;
        this.asleep = false;
        this.healthLostByShinobu = 0;
        this.takeKit = false;
        this.objectifGaved = false;
        this.lastHitByYoriichi = 0L;
        this.lastHitByNezuko = 0L;
        this.nearFromYoriichi = false;
        this.listening = false;
        this.inventoryBlocked = false;
        this.initialLocation = null;
        this.initialLocationInEnd = null;
        this.convocation = false;
        this.craftedBlade = false;
        this.blades = null;
        this.killedTarget = false;
        this.targetManager = null;
        this.nakimeBiwaLevel = 0;
        this.joinedTrainCount = 0;
        this.playerMoon = null;
        this.playerListName = new ArrayList<>();
        this.yushiroArrows = new ArrayList<>();
        this.hasKilledNakimeAsYushiro = false;
        this.wantToGoConvocation = false;
        this.blood = 0;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public PlayerManager getTargetManager() {
        return targetManager;
    }

    public List<Location> getBlockPlaced() {
        return blockPlaced;
    }

    public List<Location> getYushiroArrows() {
        return yushiroArrows;
    }

    public List<String> getPlayerListName() {
        return playerListName;
    }

    public void setPlayerListName(List<String> playerListName) {
        this.playerListName = playerListName;
    }

    public ArrayList<Role> getRoleKilled() {
        return roleKilled;
    }

    public String getPlayerMoon() {
        return playerMoon;
    }

    public void setPlayerMoon(String playerMoon) {
        this.playerMoon = playerMoon;
    }

    public TrainLocation getCurrentWagon() {
        return currentWagon;
    }

    public void setCurrentWagon(TrainLocation currentWagon) {
        this.currentWagon = currentWagon;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isInfected() {
        return infected;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }

    public boolean isAntidote() {
        return antidote;
    }

    public void setAntidote(boolean antidote) {
        this.antidote = antidote;
    }

    public boolean isInCage() {
        return inCage;
    }

    public void setInCage(boolean inCage) {
        this.inCage = inCage;
    }

    public boolean isInTrain() {
        return inTrain;
    }

    public boolean isInTemple() {
        return inTemple;
    }

    public void setInTemple(boolean inTemple) {
        this.inTemple = inTemple;
    }

    public boolean isInCavern() {
        return inCavern;
    }

    public void setInCavern(boolean inCavern) {
        this.inCavern = inCavern;
    }

    public boolean isKilledByDaki() {
        return killedByDaki;
    }

    public void setKilledByDaki(boolean killedByDaki) {
        this.killedByDaki = killedByDaki;
    }

    public boolean isEnteredTrainThisEpisode() {
        return enteredTrainThisEpisode;
    }

    public void setEnteredTrainThisEpisode(boolean enteredTrainThisEpisode) {
        this.enteredTrainThisEpisode = enteredTrainThisEpisode;
    }

    public boolean isEnteredTempleThisEpisode() {
        return enteredTempleThisEpisode;
    }

    public void setEnteredTempleThisEpisode(boolean enteredTempleThisEpisode) {
        this.enteredTempleThisEpisode = enteredTempleThisEpisode;
    }

    public boolean isEscapingAsMuichiro() {
        return isEscapingAsMuichiro;
    }

    public void setEscapingAsMuichiro(boolean escapingAsMuichiro) {
        isEscapingAsMuichiro = escapingAsMuichiro;
    }

    public boolean isAsleep() {
        return asleep;
    }

    public void setAsleep(boolean asleep) {
        this.asleep = asleep;
    }

    public boolean isTakeKit() {
        return takeKit;
    }

    public void setTakeKit(boolean takeKit) {
        this.takeKit = takeKit;
    }

    public boolean isObjectifGaved() {
        return objectifGaved;
    }

    public void setObjectifGaved(boolean objectifGaved) {
        this.objectifGaved = objectifGaved;
    }

    public boolean isHasKilledNakimeAsYushiro() {
        return hasKilledNakimeAsYushiro;
    }

    public void setHasKilledNakimeAsYushiro(boolean hasKilledNakimeAsYushiro) {
        this.hasKilledNakimeAsYushiro = hasKilledNakimeAsYushiro;
    }

    public boolean isNearFromYoriichi() {
        return nearFromYoriichi;
    }

    public void setNearFromYoriichi(boolean nearFromYoriichi) {
        this.nearFromYoriichi = nearFromYoriichi;
    }

    public boolean isListening() {
        return listening;
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }

    public boolean isInBat() {
        return inBat;
    }

    public void setInBat(boolean inBat) {
        this.inBat = inBat;
    }

    public boolean isInventoryBlocked() {
        return inventoryBlocked;
    }

    public void setInventoryBlocked(boolean inventoryBlocked) {
        this.inventoryBlocked = inventoryBlocked;
    }

    public boolean isConvocation() {
        return convocation;
    }

    public void setConvocation(boolean convocation) {
        this.convocation = convocation;
    }

    public boolean isCraftedBlade() {
        return craftedBlade;
    }

    public void setCraftedBlade(boolean craftedBlade) {
        this.craftedBlade = craftedBlade;
    }

    public boolean isWantToGoConvocation() {
        return wantToGoConvocation;
    }

    public void setWantToGoConvocation(boolean wantToGoConvocation) {
        this.wantToGoConvocation = wantToGoConvocation;
    }

    public boolean isKilledTarget() {
        return killedTarget;
    }

    public void setKilledTarget(boolean killedTarget) {
        this.killedTarget = killedTarget;
    }

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public int getStrenght() {
        return strenght;
    }

    public void setStrenght(int strenght) {
        this.strenght = strenght;
    }

    public int getHealthLostByShinobu() {
        return healthLostByShinobu;
    }

    public void setHealthLostByShinobu(int healthLostByShinobu) {
        this.healthLostByShinobu = healthLostByShinobu;
    }

    public int getJoinedTrainCount() {
        return joinedTrainCount;
    }

    public void setJoinedTrainCount(int joinedTrainCount) {
        this.joinedTrainCount = joinedTrainCount;
    }

    public int getNakimeBiwaLevel() {
        return nakimeBiwaLevel;
    }

    public void setNakimeBiwaLevel(int nakimeBiwaLevel) {
        this.nakimeBiwaLevel = nakimeBiwaLevel;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public long getLastFight() {
        return lastFight;
    }

    public void setLastFight(long lastFight) {
        this.lastFight = lastFight;
    }

    public long getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(long deadTime) {
        this.deadTime = deadTime;
    }

    public long getLastHitByNezuko() {
        return lastHitByNezuko;
    }

    public void setLastHitByNezuko(long lastHitByNezuko) {
        this.lastHitByNezuko = lastHitByNezuko;
    }

    public long getLastHitByYoriichi() {
        return lastHitByYoriichi;
    }

    public void setLastHitByYoriichi(long lastHitByYoriichi) {
        this.lastHitByYoriichi = lastHitByYoriichi;
    }

    public Location getInitialLocation() {
        return initialLocation;
    }

    public void setInitialLocation(Location initialLocation) {
        this.initialLocation = initialLocation;
    }

    public Location getInitialLocationInEnd() {
        return initialLocationInEnd;
    }

    public void setInitialLocationInEnd(Location initialLocationInEnd) {
        this.initialLocationInEnd = initialLocationInEnd;
    }

    public Blades getBlades() {
        return blades;
    }

    public void setBlades(Blades blades) {
        this.blades = blades;
    }

    public void addStrenght(int amount) {
        this.strenght += amount;
    }

    public void removeStrenght(int amount) {
        this.strenght -= amount;
        if (this.strenght < 0)
            this.strenght = 0;
    }

    public void addResistance(int amount) {
        this.resistance += amount;
    }

    public void removeResistance(int amount) {
        this.resistance -= amount;
        if (this.resistance < 0)
            this.resistance = 0;
    }

    public void setInTrain(boolean inTrain) {
        this.inTrain = inTrain;
        if (!this.inTrain)
            setCurrentWagon(null);
    }

    public Player getPlayer() {
        return getPlayerManager().getPlayer();
    }

    private static final List<DSPlayerManager> dsPlayers = new ArrayList<>();

    public static DSPlayerManager getPlayer(PlayerManager playerManager) {
        DSPlayerManager player = null;
        for (DSPlayerManager dsPlayer : dsPlayers) {
            if (dsPlayer.getPlayer().equals(playerManager.getPlayer()))
                player = dsPlayer;
        }
        return player;
    }

    public static void addPlayer(DSPlayerManager dsPlayer) {
        dsPlayers.add(dsPlayer);
    }

    public static boolean havePlayer(PlayerManager playerManager) {
        for (DSPlayerManager gamePlayer : dsPlayers) {
            if (gamePlayer.getPlayer().equals(playerManager.getPlayer()))
                return true;
        }
        return false;
    }



}
