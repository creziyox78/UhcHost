package fr.lastril.uhchost.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdHelp implements CommandExecutor {

    private final UhcHost main;

    public CmdHelp(UhcHost main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Modes modes = main.getGamemanager().getModes();
            Player player = (Player) sender;
            player.sendMessage("§8§m--------------------------------------------------§r");
            player.sendMessage("§c         Listes des commandes de partie");
            player.sendMessage("§c");
            player.sendMessage("§c •§f /rules : Voir les règles de la partie.");
            player.sendMessage("§c •§f /scenarios : Voir les scénarios de la partie.");
            player.sendMessage("§c •§f /doc : Voir le document du mode de jeu.");
            if(modes.getMode() instanceof ModeCommand){
                ModeCommand modeCommand = (ModeCommand) modes.getMode();
                player.sendMessage("§c •§f /"+modeCommand.getCommandName()+" compo : Voir la composition de la partie.");
                player.sendMessage("§c •§f /"+modeCommand.getCommandName()+" me : Voir son propre rôle.");
            }
            player.sendMessage("§c •§f /h help : Voir les commandes d'host.");
            player.sendMessage("§c •§f /mumble : Se connecter au serveur Mumble.");
            player.sendMessage("§c •§f /discord : Rejoindre le serveur discord.");
            player.sendMessage("§c");
            player.sendMessage("§8§m--------------------------------------------------§r");
        }
        return false;
    }

}
