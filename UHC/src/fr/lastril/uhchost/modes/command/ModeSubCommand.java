package fr.lastril.uhchost.modes.command;

import org.bukkit.command.CommandExecutor;

import java.util.List;

public interface ModeSubCommand extends CommandExecutor {

    String getSubCommandName();

    List<String> getSubArgs();

}
