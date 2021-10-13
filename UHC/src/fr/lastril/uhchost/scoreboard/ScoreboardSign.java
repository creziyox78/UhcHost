package fr.lastril.uhchost.scoreboard;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class ScoreboardSign {

	private final String[] lines = new String[16];

	private final Player player;

	private boolean created = false;

	private String objectiveName;

	public String getObjectiveName() {
		return this.objectiveName;
	}

	public ScoreboardSign(Player player, String objectiveName) {
		this.player = player;
		this.objectiveName = objectiveName;
	}

	public void create() {
		if (this.created)
			return;
		PlayerConnection player = getPlayer();
		player.sendPacket(createObjectivePacket(1, null));
		player.sendPacket(createObjectivePacket(0, this.objectiveName));
		player.sendPacket(setObjectiveSlot());
		int i = 0;
		while (i < this.lines.length)
			sendLine(i++);
		this.created = true;
	}

	public void destroy() {
		if (!this.created)
			return;
		getPlayer().sendPacket(createObjectivePacket(1, null));
		this.created = false;
	}

	private PlayerConnection getPlayer() {
		return (((CraftPlayer) this.player).getHandle()).playerConnection;
	}

	private void sendLine(int line) {
		if (line > 15)
			return;
		if (line < 0)
			return;
		if (!this.created)
			return;
		int score = line * -1 - 1;
		String val = this.lines[line];
		getPlayer().sendPacket(sendScore(val, score));
	}

	public void setObjectiveName(String name) {
		this.objectiveName = name;
		if (this.created)
			getPlayer().sendPacket(createObjectivePacket(2, name));
	}

	public void setLine(int line, String value) {
		String oldLine = getLine(line);
		if (oldLine != null && this.created)
			getPlayer().sendPacket(removeLine(oldLine));
		this.lines[line] = value;
		sendLine(line);
	}

	public void removeLine(int line) {
		String oldLine = getLine(line);
		if (oldLine != null && this.created)
			getPlayer().sendPacket(removeLine(oldLine));
		this.lines[line] = null;
	}

	public String getLine(int line) {
		if (line > 15)
			return null;
		if (line < 0)
			return null;
		return this.lines[line];
	}

	private PacketPlayOutScoreboardObjective createObjectivePacket(int mode, String displayName) {
		PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
		try {
			Field name = packet.getClass().getDeclaredField("a");
			name.setAccessible(true);
			name.set(packet, this.player.getName());
			Field modeField = packet.getClass().getDeclaredField("d");
			modeField.setAccessible(true);
			modeField.set(packet, Integer.valueOf(mode));
			if (mode == 0 || mode == 2) {
				Field displayNameField = packet.getClass().getDeclaredField("b");
				displayNameField.setAccessible(true);
				displayNameField.set(packet, displayName);
				Field display = packet.getClass().getDeclaredField("c");
				display.setAccessible(true);
				display.set(packet, IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
			}
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return packet;
	}

	private PacketPlayOutScoreboardDisplayObjective setObjectiveSlot() {
		PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();
		try {
			Field position = packet.getClass().getDeclaredField("a");
			position.setAccessible(true);
			position.set(packet, Integer.valueOf(1));
			Field name = packet.getClass().getDeclaredField("b");
			name.setAccessible(true);
			name.set(packet, this.player.getName());
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return packet;
	}

	private PacketPlayOutScoreboardScore sendScore(String line, int score) {
		PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(line);
		try {
			Field name = packet.getClass().getDeclaredField("b");
			name.setAccessible(true);
			name.set(packet, this.player.getName());
			Field scoreField = packet.getClass().getDeclaredField("c");
			scoreField.setAccessible(true);
			scoreField.set(packet, Integer.valueOf(score));
			Field action = packet.getClass().getDeclaredField("d");
			action.setAccessible(true);
			action.set(packet, PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return packet;
	}

	private PacketPlayOutScoreboardScore removeLine(String line) {
		return new PacketPlayOutScoreboardScore(line);
	}

}
