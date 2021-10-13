package fr.lastril.uhchost.modes.roles;


import fr.lastril.uhchost.modes.command.ModeSubCommand;

import java.util.List;

public interface RoleCommand {

    List<ModeSubCommand> getSubCommands();

}
