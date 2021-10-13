package fr.lastril.uhchost.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.command.ModeCommand;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CmdMode implements TabExecutor {

    private final UhcHost main;
    private final ModeCommand modeCommand;

    public CmdMode(UhcHost main, ModeCommand modeCommand) {
        this.main = main;
        this.modeCommand = modeCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length > 0) {
                String subCommandName = args[0];

                if(modeCommand.getSubCommand(subCommandName).isPresent()){
                    ModeSubCommand subCommand = modeCommand.getSubCommand(subCommandName).get();
                    subCommand.onCommand(sender, command, label, args);
                }else{
                    this.sendUse(player, label);
                }
            } else {
                this.sendUse(player, label);
            }

        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> possibilities = new ArrayList<>();

        if (args.length == 1) {
            modeCommand.getSubCommands().stream().map(ModeSubCommand::getSubCommandName).filter(subCommand -> subCommand.startsWith(args[0])).forEach(possibilities::add);
        } else if (args.length == 2) {
            String subCommandName = args[0];
            modeCommand.getSubCommand(subCommandName).ifPresent(subCommand -> {
                subCommand.getSubArgs().stream().filter(subArg -> subArg.startsWith(args[0])).forEach(possibilities::add);
            });
        }

        return possibilities;
    }

    private final void sendUse(Player player, String labelUsed){
        String commands = Strings.join(modeCommand.getSubCommands().stream().map(ModeSubCommand::getSubCommandName).collect(Collectors.toList()), "/");

        player.sendMessage(("§cUtilisation: /" + labelUsed + " <" + commands + ">"));
    }
}
