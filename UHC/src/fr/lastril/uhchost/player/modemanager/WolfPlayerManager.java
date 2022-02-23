package fr.lastril.uhchost.player.modemanager;

import fr.lastril.uhchost.enums.ResurectType;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.player.PlayerManager;

import java.util.UUID;

public class WolfPlayerManager implements Comparable<WolfPlayerManager> {

	private final PlayerManager playerManager;
	private LGRole lgRole;
	private Camps camp, zizanied;
	private UUID otherCouple;
	
	private boolean infected, salvation, transformed, voted, protect, solitaire, steal, talkInLGChat;
	
	private int votes;
	
	private ResurectType resurectType;
	
	public WolfPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
		if(playerManager.getRole() instanceof LGRole) {
			this.lgRole = (LGRole) playerManager.getRole();
			this.camp = lgRole.getCamp();
		}
	}
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public UUID getOtherCouple() {
		return otherCouple;
	}
	
	public boolean isInCouple() {
		return getOtherCouple() != null;
	}

	public LGRole getLgRole() {
		return this.lgRole;
	}

	public void setLgRole(LGRole lgRole) {
		this.lgRole = lgRole;
	}

	public Camps getCamp() {
		return this.camp;
	}

	public void setCamp(Camps camp) {
		this.camp = camp;
	}

	public boolean isInfected() {
		return this.infected;
	}

	public void setInfected(boolean infected) {
		this.infected = infected;
		if(infected) setCamp(Camps.LOUP_GAROU);
	}

	public boolean isTalkInLGChat() {
		return talkInLGChat;
	}

	public void setTalkInLGChat(boolean talkInLGChat) {
		this.talkInLGChat = talkInLGChat;
	}

	public boolean isSalvation() {
		return this.salvation;
	}

	public void setSalvation(boolean salvation) {
		this.salvation = salvation;
	}

	public boolean isTransformed() {
		return this.transformed;
	}

	public boolean isSolitaire() {
		return solitaire;
	}

	public void setSolitaire(boolean solitaire) {
		this.solitaire = solitaire;
	}

	public boolean isSteal() {
		return steal;
	}

	public void setSteal(boolean steal) {
		this.steal = steal;
	}

	public void setTransformed(boolean transformed) {
		this.transformed = transformed;
	}

	public int getVotes() {
		return this.votes;
	}

	public void resetVote(){
		this.votes = 0;
	}

	public void addVote(){
		this.votes++;
	}

	public ResurectType getResurectType() {
		return this.resurectType;
	}

	public void setResurectType(ResurectType resurectType) {
		this.resurectType = resurectType;
	}

	public void setOtherCouple(UUID otherCouple) {
		this.otherCouple = otherCouple;
		if(otherCouple != null) this.setCamp(Camps.COUPLE);
	}

	@Override
	public int compareTo(WolfPlayerManager o) {
		return this.getVotes() - o.getVotes();
	}

	public boolean hasVoted() {
		return voted;
	}

	public boolean isProtect() {
		return protect;
	}

	public void setProtect(boolean protect) {
		this.protect = protect;
	}

	public void setVoted(boolean voted) {
		this.voted = voted;
	}

	public boolean isZizanied() {
		return zizanied != null;
	}

	public boolean isZizanied(Camps zizanied){
		return this.zizanied == zizanied;
	}

	public void setZizanied(Camps zizanied) {
		this.zizanied = zizanied;
	}
}
