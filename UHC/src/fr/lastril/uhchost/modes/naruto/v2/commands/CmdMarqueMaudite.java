package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.Orochimaru;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CmdMarqueMaudite implements ModeSubCommand {

	private final UhcHost main;
	
	public CmdMarqueMaudite(UhcHost main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player)sender;
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return false;
		NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
		if(joueur.isAlive() && joueur.hasRole()) {
			if(joueur.getRole() instanceof Orochimaru) {
				Orochimaru orochimaru = (Orochimaru) joueur.getRole();
				if(!narutoV2Manager.isInSamehada(player.getUniqueId())) {
					if(args.length == 2) {
						if(orochimaru.getTargetMarque() == null){
							String playerName = args[1];
							Player target = Bukkit.getPlayer(playerName);
							if (target != null) {
								if (target != player) {
									PlayerManager targetJoueur = main.getPlayerManager(target.getUniqueId());
									if (targetJoueur.isAlive()) {
										orochimaru.setTargetMarque(targetJoueur);
										player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + target.getName() + " est maintenant imprégné de la §5Marque Maudite.");
										target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§5Orochimaru§e vient de vous imprégner la §5Marque Maudite.");
										orochimaru.usePower(joueur);
										orochimaru.usePowerSpecific(joueur, "/ns marquemaudite");
									} else {
										player.sendMessage(Messages.NOT_INGAME.getMessage());
									}
								} else {
									player.sendMessage(Messages.NOT_FOR_YOU.getMessage());
								}
							} else {
								player.sendMessage(Messages.NOT_INGAME.getMessage());
							}
						} else {
							player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.CANT_USE_MORE_POWER.getMessage());
						}
					}
				} else {
					player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
				}
				
			} else {
				player.sendMessage(Messages.not("Orochimaru"));
			}
		}
		return false;
	}

	@Override
	public String getSubCommandName() {
		return "marquemaudite";
	}

	@Override
	public List<String> getSubArgs() {
		return new ArrayList<>();
	}

}
