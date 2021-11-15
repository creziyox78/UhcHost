package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CmdDesc implements ModeSubCommand{

    private final UhcHost pl;

    public CmdDesc(UhcHost pl) {
        this.pl = pl;
    }

    @Override
    public String getSubCommandName() {
        return "desc";
    }

    @Override
    public List<String> getSubArgs() {
        List<String> rolesName = new ArrayList<>();
        if(pl.gameManager.getModes().getMode() instanceof RoleMode<?>){
            RoleMode<?> mode = (RoleMode<?>) pl.getGamemanager().getModes().getMode();
            for (Role roles : mode.getRoles()) {
                rolesName.add(roles.getClass().getName());
            }
        }
        return rolesName;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if(args.length >= 2){
            String pathRole = "";
            for(int i = 1; i < args.length; i++){
                pathRole += args[i] + " ";
            }
            if(pl.gameManager.getModes().getMode() instanceof RoleMode<?>){
                RoleMode<?> mode = (RoleMode<?>) pl.getGamemanager().getModes().getMode();
                for (Role roles : mode.getRoles()) {
                    System.out.println(roles.getRoleName() + " : " + pathRole);
                    if(pathRole.equalsIgnoreCase(roles.getRoleName() + " ")){
                        player.sendMessage("------- §aDescription --------");
                        player.sendMessage("§6Vous êtes <nom du rôle>.");
                        player.sendMessage(UhcHost.getInstance().getLGRoleDescription(roles.getClass().getName()));
                        player.sendMessage("------- §aFin de description --------");
                        return true;
                    }
                }
            }

        }
        return false;
    }
}
