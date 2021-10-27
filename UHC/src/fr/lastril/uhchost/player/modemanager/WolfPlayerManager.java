package fr.lastril.uhchost.player.modemanager;

import java.util.UUID;

import fr.lastril.uhchost.enums.ResurectType;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.player.PlayerManager;

public class WolfPlayerManager implements Comparable<WolfPlayerManager> {

	private final PlayerManager joueur;
	private LGRole lgRole;
	private Camps camp;
	private UUID otherCouple;
	
	private boolean infected, salvation, transformed, voted;
	
	private int votes;
	
	private ResurectType resurectType;
	
	public WolfPlayerManager(PlayerManager joueur) {
		this.joueur = joueur;
		if(joueur.getRole() instanceof LGRole) {
			this.lgRole = (LGRole) joueur.getRole();
			this.camp = lgRole.getCamp();
		}
	}
	
	public PlayerManager getJoueur() {
		return joueur;
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

	public boolean isSalvation() {
		return this.salvation;
	}

	public void setSalvation(boolean salvation) {
		this.salvation = salvation;
	}

	public boolean isTransformed() {
		return this.transformed;
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

	public void setVoted(boolean voted) {
		this.voted = voted;
	}
}
