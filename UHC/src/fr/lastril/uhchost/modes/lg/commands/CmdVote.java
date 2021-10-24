package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdVote implements ModeSubCommand {

    private final LoupGarouManager loupGarouManager;
    private final UhcHost pl;

    public CmdVote(UhcHost pl, LoupGarouManager loupGarouManager){
        this.pl = pl;
        this.loupGarouManager = loupGarouManager;
    }

    @Override
    public String getSubCommandName() {
        return "vote";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }
}
