package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.village.Chaman;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdChaman implements ModeSubCommand {

    private final UhcHost main;

    public CmdChaman(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "chaman";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if(playerManager.getRole() instanceof Chaman){
            Chaman chaman = (Chaman) playerManager.getRole();
            if(args.length == 2){
                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);
                if(target != null && target != player){
                    PlayerManager targetManager = main.getPlayerManagerAlives().get(UhcHost.getRANDOM().nextInt(main.getPlayerManagerAlives().size()));
                    player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§eVoici le rôle du joueur " + targetManager.getPlayerName() + " : " + targetManager.getRole().getRoleName() + ".");
                    chaman.addPlayerSpec(targetManager);
                    player.setMaxHealth(player.getMaxHealth() - 2D);
                } else {
                    player.sendMessage(Messages.error("Vous ne pouvez pas utiliser votre pouvoir sur ce joueur."));
                }
            }
        } else {
            player.sendMessage(Messages.not("Chaman"));
        }
        return false;
    }
}
