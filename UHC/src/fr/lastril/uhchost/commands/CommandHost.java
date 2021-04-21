package fr.lastril.uhchost.commands;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.inventory.CustomInv;
import fr.lastril.uhchost.tools.inventory.NotStart;
import fr.lastril.uhchost.tools.inventory.guis.Enchant;

public class CommandHost implements CommandExecutor {

	private UhcHost pl;

	public CommandHost(UhcHost pl) {
		this.pl = pl;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			GameManager gameManager = UhcHost.getInstance().getGamemanager();
			if (gameManager.getHostname() != player.getName()) {
				ActionBar.sendMessage(player, "n'es pas l'host de la partie !");
				return false;
			}
			if ((cmd.getName().equalsIgnoreCase("h") || cmd.getName().equalsIgnoreCase("host")) && args.length >= 1) {
				if (args[0].equalsIgnoreCase("test"))
					player.sendMessage("");
				if (args[0].equalsIgnoreCase("enchant")) {
					if (!gameManager.isEditInv())
						return false;
					if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
						player.openInventory(Enchant.Categories(player, player.getItemInHand()));
						return true;
					}
					player.sendMessage("ne possrien dans ta main !");
					return false;
				}
				if (args[0].equalsIgnoreCase("save")) {
					if (!gameManager.isEditInv())
						return false;
					CustomInv.saveInventory(player);
					player.setGameMode(GameMode.ADVENTURE);
					gameManager.setEditInv(false);
					NotStart.PreHosting(player);
					player.sendMessage("sauvegard!");
				}

				if (args[0].equalsIgnoreCase("bypass")) {

					if (args[1].equalsIgnoreCase("pvp")) {
						this.pl.gameManager.setPvp(!this.pl.gameManager.isPvp());
						if (this.pl.gameManager.isPvp()) {
							sender.sendMessage("Le pvp est maintenant sur §cOff");
						} else {
							sender.sendMessage("Le pvp est maintenant sur §aOn");
						}
						return true;
					}
					
				}
			}

		}
		return false;
	}
}
