package fr.lastril.uhchost.modes;

import fr.lastril.uhchost.inventory.Gui;
import org.bukkit.entity.Player;

public interface ModeConfig {

    Gui getGui(Player player);

}
