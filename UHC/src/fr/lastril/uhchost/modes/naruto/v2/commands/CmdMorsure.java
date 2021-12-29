package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Karin;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdMorsure implements ModeSubCommand {

	private final UhcHost main;

	public CmdMorsure(UhcHost main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if (!joueur.isAlive())
			return false;
		if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return false;
		NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
		if (joueur.hasRole()) {
			if (joueur.getRole() instanceof Karin) {
				Karin karin = (Karin) joueur.getRole();
				if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
					if (args.length == 2) {
						String playerName = args[1];
						Player target = Bukkit.getPlayer(playerName);
						if (target != null) {
							PlayerManager targetJoueur = main.getPlayerManager(target.getUniqueId());
							if (targetJoueur.isAlive()) {
								if (karin.isUseMorsure()) {
									if(player.getMaxHealth() - 2D <= 0) {
										player.sendMessage(Messages.error("Vous n'avez plus assez de coeur permanent."));
										return false;
									}
									player.setMaxHealth(player.getMaxHealth() - 2D);
									player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous avez utilisé votre pouvoir 1 fois cette épisode. Vous perdez 1 coeur permanent.");
								}
								target.setHealth(target.getMaxHealth());
								player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous venez de soigner "
										+ target.getName());
								target.sendMessage(
										Messages.NARUTO_PREFIX.getMessage() + "§6Karin vient de vous soigner.");
								karin.setUseMorsure(true);
								karin.usePower(joueur);
								karin.usePowerSpecific(joueur, "/ns morsure");
							}
						} else {
							player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
						}
					}
				} else {
					player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public String getSubCommandName() {
		return "morsure";
	}

	@Override
	public List<String> getSubArgs() {
		return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
	}

}
