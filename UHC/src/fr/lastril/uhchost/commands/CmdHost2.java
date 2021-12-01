package fr.lastril.uhchost.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.inventory.CustomInv;
import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.inventory.guis.enchant.CategoriesGui;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.NotStart;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdHost2 implements CommandExecutor {

	private final UhcHost pl;

	public CmdHost2(UhcHost pl) {
		this.pl = pl;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			GameManager gameManager = UhcHost.getInstance().getGamemanager();
			if (gameManager.getHostname() != player.getName() && !gameManager.isCoHost(player)) {
				ActionBar.sendMessage(player, "§cTu n'es pas host de la partie !");
				return false;
			}
			if ((cmd.getName().equalsIgnoreCase("h") || cmd.getName().equalsIgnoreCase("host")) && args.length >= 1) {
				if (args[0].equalsIgnoreCase("test"))
					player.sendMessage("");
				else if (args[0].equalsIgnoreCase("enchant")) {
					if (!gameManager.isEditInv())
						return false;
					if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
						new CategoriesGui(player).open(player);
						return true;
					}
					player.sendMessage("§cTu ne possède rien dans ta main !");
					return false;
				}
				else if (args[0].equalsIgnoreCase("save")) {
					if (!gameManager.isEditInv())
						return false;
					CustomInv.saveInventory(player);
					player.setGameMode(GameMode.ADVENTURE);
					gameManager.setEditInv(false);
					NotStart.PreHosting(player);
					player.sendMessage("§aInventaire sauvegardé !");
				}
				else if (args[0].equalsIgnoreCase("config")) {
					new HostConfig().open(player);
				}
				else if (args[0].equalsIgnoreCase("op")) {
					if(pl.gameManager.isCoHost(player)){
						return false;
					}
					if(args.length == 2){
						String targetName = args[1];
						Player target = Bukkit.getPlayer(targetName);
						if(target != null){
							pl.gameManager.addCoHost(target);
							NotStart.PreHosting(target);
							player.sendMessage("§a" + targetName + " a bien été ajouté des co-host.");
						}
					}
				}
				else if (args[0].equalsIgnoreCase("deop")) {
					if(pl.gameManager.isCoHost(player)){
						return false;
					}
					if(args.length == 2){
						String targetName = args[1];
						Player target = Bukkit.getPlayer(targetName);
						if(target != null){
							pl.gameManager.removeCoHost(target);
							NotStart.PreHosting(target);
							player.sendMessage("§c" + targetName + " a bien été retiré des co-host.");
						}
					}
				}
				else if (args[0].equalsIgnoreCase("force")) {
					if (args[1].equalsIgnoreCase("pvp")) {
						this.pl.gameManager.setPvp(!this.pl.gameManager.isPvp());
						if (this.pl.gameManager.isPvp()) {
							sender.sendMessage("Le pvp est maintenant sur §cOff");
						} else {
							sender.sendMessage("Le pvp est maintenant sur §aOn");
						}
						return true;
					}
					
				} else{
					sendUse(player);
				}
			} else {
				sendUse(player);
			}

		}
		return false;
	}


	private void sendUse(Player player) {
		player.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage()+"Liste des commandes d'host :");
		player.sendMessage(" ");
		player.sendMessage("§6• /say §7: §eFaire une annonce.");
		player.sendMessage("§6• /h help §7: §eVoir la liste des commandes d'host.");
		player.sendMessage("§6• /h force <invincibility/pvp/border/tp/roles> §7: §eForcer un événement de la partie.");
		player.sendMessage("§6• /h heal §7: §eSoigner tous les joueurs de la partie.");
		player.sendMessage("§6• /h wl list §7: §eVoir tous les joueurs présents dans la liste blanche.");
		player.sendMessage("§6• /h wl clear §7: §eVider la liste blanche.");
		player.sendMessage("§6• /h wl open §7: §eOuvrir la partie.");
		player.sendMessage("§6• /h wl close §7: §eFermer la partie.");
		player.sendMessage("§6• /h wl add <pseudo> §7: §eAjouter un joueur de la liste blanche.");
		player.sendMessage("§6• /h wl remove <pseudo> §7: §eRetirer un joueur de la liste blanche.");
		player.sendMessage("§6• /h kick <pseudo> <raison> §7: §eExpulser un joueur de la partie.");
		player.sendMessage("§6• /h give <item> <nombre> §7: §eDonner un item aux joueurs de la partie.");
		player.sendMessage("§6• /h op <pseudo> §7: §eAjout un host à la partie.");
		player.sendMessage("§6• /h deop <pseudo> §7: §eSupprimer un host à la partie.");
	}

}