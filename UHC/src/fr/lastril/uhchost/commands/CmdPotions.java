package fr.lastril.uhchost.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.guis.rules.see.items.RulesPotionsGui;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdPotions implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {

            Player player = (Player)sender;

            if (!(UhcHost.getInstance()).gameManager.isAllPotionsEnable()) {
                player.sendMessage(I18n.tl("allPotionsDisable", new String[0]));
                return true;
            }
            new RulesPotionsGui().open(player);
        }
        return false;
    }
}
