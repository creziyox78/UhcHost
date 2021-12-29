package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Karin;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.ClassUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class CmdKaguraShingan implements ModeSubCommand {

	private final UhcHost main;

	public CmdKaguraShingan(UhcHost main) {
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
					if (joueur.getRoleCooldownKaguraShingan() <= 0) {
						if (args.length == 2) {
							String playerName = args[1];
							Player target = Bukkit.getPlayer(playerName);
							if (target != null) {
								PlayerManager targetJoueur = main.getPlayerManager(target.getUniqueId());
								if (targetJoueur.isAlive()) {
									if (karin.getTracked() == null) {
										new BukkitRunnable() {

											@Override
											public void run() {
												String arrow = ClassUtils.getDirectionOf(player.getLocation(),karin.getTracked().getPlayer().getLocation());
												if(karin.getPlayer() != null){
													if (joueur.isAlive() && main.getPlayerManager(karin.getPlayer().getUniqueId()).isAlive() && karin.getTracked().isAlive()) {
														if (player.getLocation().distance(karin.getTracked().getPlayer().getLocation()) <= 200) {
															ActionBar.sendMessage(player, "§6" + karin.getTracked().getPlayer().getName()+ " " + arrow);
														} else {
															ActionBar.sendMessage(player,"§cLe joueur se trouve à plus de 200 blocs.");
														}
													} else {
														karin.setTracked(null);
														cancel();
													}
												}


											}
										}.runTaskTimer(main, 5, 1);
									}
									karin.setTracked(targetJoueur);
									joueur.setRoleCooldownKaguraShingan(5*60);
									karin.usePower(joueur);
									karin.usePowerSpecific(joueur, "/ns kagurashingan");
								}
							} else {
								player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
							}
						}
					} else {
						player.sendMessage(Messages.cooldown(joueur.getRoleCooldownKaguraShingan()));
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
		return "kagurashingan";
	}

	@Override
	public List<String> getSubArgs() {
		return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
	}

}
