package fr.lastril.uhchost.modes.naruto;

import java.io.Serializable;

public class NarutoV2Config implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -324602394095067677L;
	
	public int hokageAnnoncement, roleAnnouncement;

	public boolean biju;

	public NarutoV2Config(int hokageAnnoncement, boolean biju, int roleAnnouncement) {
		this.hokageAnnoncement = hokageAnnoncement * 60;
		this.biju = biju;
		this.roleAnnouncement = roleAnnouncement;
	}

	public int getHokageAnnoncement() {
		return this.hokageAnnoncement;
	}

	public void setHokageAnnoncement(int hokageAnnoncement) {
		this.hokageAnnoncement = hokageAnnoncement;
	}

	public int getRoleAnnouncement() {
		return roleAnnouncement;
	}

	public void setRoleAnnouncement(int roleAnnouncement) {
		this.roleAnnouncement = roleAnnouncement;
	}

	public boolean isBiju() {
		return this.biju;
	}

	public void setBiju(boolean biju) {
		this.biju = biju;
	}

}
