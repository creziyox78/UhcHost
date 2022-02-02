package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.ZetsuBlanc;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.ZetsuNoir;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdZetsu implements ModeSubCommand {

	private final UhcHost main;
	
	public CmdZetsu(UhcHost main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(main.getGamemanager().getModes() != Modes.NARUTO) return false;
		NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
		Player player = (Player) sender;
	    PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
	    if(!joueur.isAlive())
        	return false;
	    if (joueur.hasRole()) {
	    	if(joueur.getRole() instanceof ZetsuNoir) {
	    		if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
	    			if (args.length >= 2) {
	    				String message = "";
	    				for (int i = 1; i < args.length; i++) {
                            message += args[i] + " ";
                        }
	    				player.sendMessage("§8[§c§lZetsu Noir§8] §7" + message);
	    				for(PlayerManager targetJoueur : narutoV2Manager.getPlayerManagersWithRole(ZetsuBlanc.class)) {
		    				if(targetJoueur.isAlive()) {
		    					targetJoueur.getPlayer().sendMessage("§8[§c§lZetsu Noir§8] §7" + message);
		    				}
		    			}
						main.sendMessageToModsInModeration("§8[§c§lZetsu Noir§8] §7" + message);
						if(joueur.getRole() instanceof NarutoV2Role){
							NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
							narutoRole.usePower(joueur);
							narutoRole.usePowerSpecific(joueur, "/ns zetsu");
						}
	    				return true;
	    			} else {
	    				Messages.error("Précisez un message.");
	    			}
	    			
	    		} else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
	    	} else {
	    		player.sendMessage(Messages.not("Zetsu Noir"));
	    	}
	    } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
		return false;
	}

	@Override
	public String getSubCommandName() {
		return "zetsu";
	}

	@Override
	public List<String> getSubArgs() {
		return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
	}

}
