package fr.lastril.uhchost.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class CmdRevive implements CommandExecutor {

	private final UhcHost main;
	
	public CmdRevive(UhcHost main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.isOp()) {
				if (args.length > 0) {
					Player target = main.getServer().getPlayer(args[0]);
					if(target != null) {
						PlayerManager targetJoueur = main.getPlayerManager(target.getUniqueId());
						targetJoueur.setAlive(true);
						target.setGameMode(GameMode.SURVIVAL);
						main.getGamemanager().teleportPlayerOnGround(target);
						if(targetJoueur.getArmors() != null){
							target.getInventory().setArmorContents(targetJoueur.getArmors());
						}
						if(targetJoueur.getItems() != null){
							target.getInventory().setContents(targetJoueur.getItems());
						}
						if(targetJoueur.hasRole()){
							for(PotionEffect effect : targetJoueur.getRole().getEffects().keySet()){
								target.addPotionEffect(effect);
							}
						}
						player.sendMessage("§aVous avez bien réssucité "+target.getName());
						Bukkit.broadcastMessage(Messages.PREFIX_WITH_SEPARATION.getMessage()+target.getName()+" a été réssucité.");

					}else{
						player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
					}
				} else {
					player.sendMessage(Messages.use("/revive <message>"));
				} 
			} else {
				player.sendMessage(Messages.NOT_PERM.getMessage());
			}
		} else {
			sender.sendMessage(Messages.ONLY_PLAYER.getMessage());
		}
		return false;
	}

}
