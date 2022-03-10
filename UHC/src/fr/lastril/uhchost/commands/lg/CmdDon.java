package fr.lastril.uhchost.commands.lg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.WolfPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdDon implements CommandExecutor {

    private final UhcHost main;

    public CmdDon(UhcHost main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(main.getGamemanager().getModes() == Modes.LG){
                PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                WolfPlayerManager wolfPlayerManager = playerManager.getWolfPlayerManager();
                if(wolfPlayerManager.isInCouple()){
                    if(args.length == 1){
                        UUID uuid = wolfPlayerManager.getOtherCouple();
                        Player otherCouple = Bukkit.getPlayer(uuid);
                        int pourcentage = Integer.parseInt(args[0]);
                        if(player.getHealth() - pourcentage/5 <= 0){
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous n'avez pas assez de vie !");
                        } else {
                            if(otherCouple.getHealth() + (pourcentage/5) > otherCouple.getMaxHealth()){
                                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cIl ne manque pas autant à votre âme-soeur.");
                            } else {
                                player.setHealth(player.getHealth() - (pourcentage/5));
                                otherCouple.setHealth(otherCouple.getHealth() + (pourcentage/5));
                                otherCouple.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§dVous venez de recevoir un don de " + pourcentage + "% de votre âme-soeur.");
                                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§dVous venez de faire un don de " + pourcentage + "% à votre âme-soeur.");
                            }
                        }
                    }


                }
            }
        }
        return false;
    }

}
