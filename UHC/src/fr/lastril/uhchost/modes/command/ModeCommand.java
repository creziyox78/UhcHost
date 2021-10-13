package fr.lastril.uhchost.modes.command;

import java.util.List;
import java.util.Optional;

public interface ModeCommand {

    String getCommandName();

    List<ModeSubCommand> getSubCommands();

    default Optional<ModeSubCommand> getSubCommand(String name){
        return getSubCommands().stream().filter(modSubCommand -> modSubCommand.getSubCommandName().equalsIgnoreCase(name)).findAny();
    }

}
