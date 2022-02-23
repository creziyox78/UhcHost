package fr.lastril.uhchost.modes.lg.commands.trublion;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.solo.Trublion;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdTp implements ModeSubCommand {

    private final UhcHost main;

    public CmdTp(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "tp";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.isAlive() && playerManager.hasRole()){
            if(playerManager.getRole() instanceof Trublion){
                Trublion trublion = (Trublion) playerManager.getRole();
                if(trublion.getSwitchedCampsResult() == Camps.TRUBLION){
                    if(args.length == 2){
                        if(trublion.getTeleportedSpecificPlayer() < 2){
                            String targetName = args[1];
                            Player target = Bukkit.getPlayer(targetName);
                            if(target != null){
                                trublion.setTeleportedSpecificPlayer(trublion.getTeleportedSpecificPlayer() + 1);
                                main.getGamemanager().teleportPlayerOnGround(target);
                                target.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§eLe Trublion vient de vous téléporter aléatoirement dans la map.");
                            } else {
                                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cCe joueur n'est pas en ligne !");
                            }
                        } else {
                            player.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                        }
                    } else {
                        player.sendMessage(Messages.use("/lg tp <pseudo>"));
                    }
                }
            } else {
                player.sendMessage(Messages.not("Trublion"));
            }
        }
        return false;
    }
}
