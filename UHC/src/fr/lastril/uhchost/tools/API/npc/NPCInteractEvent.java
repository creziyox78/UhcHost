package fr.lastril.uhchost.tools.API.npc;

import org.bukkit.entity.Player;

public interface NPCInteractEvent {

	void onClick(Player player, NPC npc);

	void onLeftClick(Player player, NPC npc);
	void onRightClick(Player player, NPC npc);
	
}
