package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Danzo;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CmdHokage implements ModeSubCommand {

	private final UhcHost main;

	public CmdHokage(UhcHost main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return false;
		NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
		Player player = (Player) sender;
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if (joueur.isAlive() && joueur.hasRole()) {
			if (joueur.getRole() instanceof Danzo) {
				Danzo danzo = (Danzo) joueur.getRole();
				if (!danzo.isWeakHokage()) {
					if (narutoV2Manager.getHokage() != null) {
						if (narutoV2Manager.getHokage().getPlayer() != null) {
							Player hokage = narutoV2Manager.getHokage().getPlayer();
							hokage.setMaxHealth(hokage.getMaxHealth() - (2D*2D));
							hokage.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*60*2, 0, false, false));
							hokage.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*60*2, 0, false, false));
							hokage.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§7Danzo vient d'utiliser son pouvoir sur vous, car vous êtes l'Hokage.");
							danzo.setWeakHokage(true);
							new BukkitRunnable() {
								
								@Override
								public void run() {
									hokage.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aL'effet de Danzo s'est dissipé, vous récuperez votre vie.");
									hokage.setMaxHealth(hokage.getHealth() + (2D*2D));
								}
							}.runTaskLater(main, 20*60*2);
							player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
							danzo.usePower(joueur);
							danzo.usePowerSpecific(joueur, "/ns hokage");
						} 
					} else {
						player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cAttendez que l'Hokage soit annoncé.");
					}
				} else {
					player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.CANT_USE_MORE_POWER.getMessage());
				}
			} else {
				player.sendMessage(Messages.not("Danzo"));
			}
		}
		return false;
	}

	@Override
	public String getSubCommandName() {
		return "hokage";
	}

	@Override
	public List<String> getSubArgs() {
		return null;
	}

}
