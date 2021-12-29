package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Kiba;
import fr.lastril.uhchost.modes.naruto.v2.tasks.SniffTask;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdSniff implements ModeSubCommand {

	private final UhcHost main;
	
	public CmdSniff(UhcHost main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return false;
		NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
		Player player = (Player) sender;
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if(!joueur.isAlive()){
			player.sendMessage(Messages.error("Vous n'êtes plus vivant."));
			return false;
		}
        if (joueur.hasRole()) {
        	if(joueur.getRole() instanceof Kiba) {
        		Kiba kiba = (Kiba) joueur.getRole();
        		if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
        			if(kiba.isCanSniff()) {
        				if (args.length == 2) {
        					String playerName = args[1];
                            Player target = Bukkit.getPlayer(playerName);
                            if (target != null) {
                            	PlayerManager targetJoueur = main.getPlayerManager(target.getUniqueId());
                            	if(targetJoueur.isAlive()) {
                            		if(targetJoueur.getPlayer().getLocation().distance(player.getLocation()) <= kiba.getDistanceSniff()) {
                            			kiba.setPlayerSniffed(targetJoueur.getUuid());
                            			kiba.setCanSniff(false);
										new SniffTask(main, kiba, target.getUniqueId()).runTaskTimer(main, 0, 20);
										player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Kiba traque désormais "+target.getName()+" !");
                            			kiba.usePower(joueur);
                            			kiba.usePowerSpecific(joueur, "/ns sniff");
                            		}
                            	}
                            }
        				}
            		}
    			}
        		
        	}
        }
		return false;
	}

	@Override
	public String getSubCommandName() {
		return "sniff";
	}

	@Override
	public List<String> getSubArgs() {
		return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
	}


}
