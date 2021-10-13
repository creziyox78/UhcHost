package fr.lastril.uhchost.world;

import fr.lastril.uhchost.UhcHost;
import org.bukkit.WorldBorder;

public class WorldBorderUtils {

	private int startSize = 1000, speed = 2, finalSize = 50;

	public WorldBorderUtils() {
		WorldBorder wb = (UhcHost.getInstance()).worldUtils.getNether().getWorldBorder();
		wb.reset();
		wb.setSize(startSize);
		wb = (UhcHost.getInstance()).worldUtils.getEnd().getWorldBorder();
		wb.reset();
		wb.setSize(startSize);
	}

	public void change(int size) {
		WorldBorder wb = (UhcHost.getInstance()).worldUtils.getWorld().getWorldBorder();
		wb.setCenter(0.0D, 0.0D);
		wb.setSize(size);
		wb.setDamageAmount(2.0D);
		wb.setDamageBuffer(0D);
		wb.setWarningDistance(20);
		wb = (UhcHost.getInstance()).worldUtils.getNether().getWorldBorder();
		wb.setCenter(0.0D, 0.0D);
		wb.setSize(size);
		wb.setDamageAmount(2.0D);
		wb.setDamageBuffer(0D);
		wb.setWarningDistance(20);
		wb = (UhcHost.getInstance()).worldUtils.getEnd().getWorldBorder();
		wb.setCenter(0.0D, 0.0D);
		wb.setSize(size);
		wb.setDamageAmount(2.0D);
		wb.setDamageBuffer(0D);
		wb.setWarningDistance(20);
	}

	public void change(int size, long time) {
		WorldBorder wb = (UhcHost.getInstance()).worldUtils.getWorld().getWorldBorder();
		wb.setCenter(0.0D, 0.0D);
		wb.setSize(size, time);
		wb.setDamageAmount(2.0D);
		wb.setDamageBuffer(0D);
		wb.setWarningDistance(20);
		wb = (UhcHost.getInstance()).worldUtils.getNether().getWorldBorder();
		wb.setCenter(0.0D, 0.0D);
		wb.setSize(size, time);
		wb.setDamageAmount(2.0D);
		wb.setDamageBuffer(0D);
		wb.setWarningDistance(20);
		wb = (UhcHost.getInstance()).worldUtils.getEnd().getWorldBorder();
		wb.setCenter(0.0D, 0.0D);
		wb.setSize(size, time);
		wb.setDamageAmount(2.0D);
		wb.setDamageBuffer(0D);
		wb.setWarningDistance(20);
	}

	public int getFinalSize() {
		return this.finalSize;
	}

	public void setFinalSize(int finalSize) {
		this.finalSize = finalSize;
	}

	public int getSpeed() {
		return this.speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getStartSize() {
		return this.startSize;
	}

	public void setStartSize(int startSize) {
		this.startSize = startSize;
	}

}
