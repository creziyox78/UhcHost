package fr.lastril.uhchost.config.modes;

import java.io.Serializable;

public class NarutoV2Config implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -324602394095067677L;
	
	public int hokageAnnoncement;

	public boolean biju;

	public NarutoV2Config(int hokageAnnoncement, boolean biju) {
		this.hokageAnnoncement = hokageAnnoncement * 60;
		this.biju = biju;
	}

	public int getHokageAnnoncement() {
		return this.hokageAnnoncement;
	}

	public void setHokageAnnoncement(int hokageAnnoncement) {
		this.hokageAnnoncement = hokageAnnoncement;
	}

	public boolean isBiju() {
		return this.biju;
	}

	public void setBiju(boolean biju) {
		this.biju = biju;
	}

}
