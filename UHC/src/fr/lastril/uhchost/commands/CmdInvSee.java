package fr.lastril.uhchost.commands;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.inventory.guis.InvSeeGui;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdInvSee implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(player.isOp()){
                if(args.length == 1){
                    Player target = Bukkit.getPlayer(args[0]);
                    if(target != null){
                        new InvSeeGui(target).open(player);
                    } else {
                        player.sendMessage(Messages.error("Ce joueur n'est pas connecté !"));
                    }
                } else {
                    player.sendMessage(Messages.use("/invsee <pseudo>"));
                }
            } else {
                player.sendMessage(Messages.not("Vous n'avez pas la permission de faire cette commande !"));
            }

        }
        return false;
    }

}
