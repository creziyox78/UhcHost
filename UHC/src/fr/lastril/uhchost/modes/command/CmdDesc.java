package fr.lastril.uhchost.modes.command;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
                rolesName.add(roles.getRoleName());
            }
        }
        return rolesName;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if(args.length >= 2){
            String roleName = "";
            for(int i = 1; i < args.length; i++){
                roleName += args[i] + " ";
            }
            if(pl.gameManager.getModes().getMode() instanceof RoleMode<?>){
                RoleMode<?> mode = (RoleMode<?>) pl.getGamemanager().getModes().getMode();
                for (Role roles : mode.getRoles()) {
                    if(roleName.equalsIgnoreCase(roles.getRoleName() + " ")){
                        player.sendMessage(UhcHost.getInstance().getRoleDescription(roles, roles.getClass().getName(),  pl.getGamemanager().getModes().name().toLowerCase(Locale.ROOT) + ".yml"));
                        return true;
                    }
                }
            }

        }
        return false;
    }
}
