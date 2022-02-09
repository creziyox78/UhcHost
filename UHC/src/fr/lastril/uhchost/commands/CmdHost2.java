package fr.lastril.uhchost.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.inventory.CustomInv;
import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.inventory.guis.enchant.CategoriesGui;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.NotStart;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdHost2 implements CommandExecutor {

	private final UhcHost pl;

	public CmdHost2(UhcHost pl) {
		this.pl = pl;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			GameManager gameManager = pl.getGamemanager();
			if(gameManager.getHostname() == null){
				return false;
			}
			if(!gameManager.getHostname().equalsIgnoreCase(player.getName()) && !(gameManager.isCoHost(player)) && !player.isOp()){
				ActionBar.sendMessage(player, "§cTu n'es pas host de la partie !");
				return false;
			}
			if ((cmd.getName().equalsIgnoreCase("h") || cmd.getName().equalsIgnoreCase("host")) && args.length >= 1) {
				if (args[0].equalsIgnoreCase("test")){
					player.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§aFin du test !");

				} else if (args[0].equalsIgnoreCase("enchant")) {
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
						if(target != null && pl.getGamemanager().getHost() != target.getUniqueId()){
							pl.gameManager.addCoHost(target);
							if(GameState.isState(GameState.LOBBY)){
								NotStart.PreHosting(target);
							}
						}
						player.sendMessage("§a" + targetName + " a bien été ajouté des co-host.");
					}
				} else if (args[0].equalsIgnoreCase("heal")) {
					if (GameState.isState(GameState.STARTED)) {
						for (Player players : Bukkit.getOnlinePlayers()) {
							players.setHealth(players.getMaxHealth());
						}
						Bukkit.broadcastMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "Tout le monde a été soigné !");
					} else {
						player.sendMessage(Messages.NOT_NOW.getMessage());
						return false;
					}
				} else if(args[0].equalsIgnoreCase("reset")){
					if (player.isOp()) {
						if(args.length > 1){
							Player target = Bukkit.getPlayer(args[1]);
							if(target != null){
								PlayerManager targetJoueur = pl.getPlayerManager(target.getUniqueId());
								targetJoueur.clearCooldowns();
								target.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "Un modérateur vient de réduire vos temps de rechargement à 0.");
								player.sendMessage(Messages.PREFIX_SPEC_STAFF.getMessage() + "§6Vous venez de réinitialiser les cooldowns de " + target.getName() + ".");

							} else {
								player.sendMessage(Messages.error("Ce joueur n'est pas en ligne."));
							}
						} else {
							player.sendMessage(Messages.use("/h reset <pseudo>"));
						}
					} else {
						player.sendMessage(Messages.NOT_PERM.getMessage());
					}
				} else if(args[0].equalsIgnoreCase("clear")){
					if (player.isOp()) {
						if(args.length > 1){
							Player target = Bukkit.getPlayer(args[1]);
							if(target != null){
								PlayerManager targetJoueur = pl.getPlayerManager(target.getUniqueId());
								BleachPlayerManager bleachPlayerManager = targetJoueur.getBleachPlayerManager();
								bleachPlayerManager.clearCancellablePower();
								target.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "Un modérateur vient d'enlever tous vos malus.");
								player.sendMessage(Messages.PREFIX_SPEC_STAFF.getMessage() + "§6Vous venez d'enlever les malus de " + target.getName() + ".");

							} else {
								player.sendMessage(Messages.error("Ce joueur n'est pas en ligne."));
							}
						} else {
							player.sendMessage(Messages.use("/h reset <pseudo>"));
						}
					} else {
						player.sendMessage(Messages.NOT_PERM.getMessage());
					}
				} else if (args[0].equalsIgnoreCase("kill")) {
					if (pl.getGamemanager().isCoHost(player) || pl.getGamemanager().getHost() == player.getUniqueId()) {
						if (GameState.isState(GameState.STARTED)) {
							OfflinePlayer target = pl.getServer().getOfflinePlayer(args[1]);
							if (target != null) {
								pl.getGamemanager().getModes().getMode().onDeath(target, null);
							} else {
								player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
								return false;
							}
						} else {
							player.sendMessage(Messages.NOT_NOW.getMessage());
							return false;
						}
					} else {
						player.sendMessage(Messages.NOT_PERM.getMessage());
						return false;
					}
				} else if (args[0].equalsIgnoreCase("recup")) {
					if (GameState.isState(GameState.STARTED)) {
						Player target = Bukkit.getPlayer(args[1]);
						if (target != null) {
							PlayerManager joueur = pl.getPlayerManager(target.getUniqueId());
							if (joueur.hasRole()) {
								Role role = joueur.getRole();
								role.giveItems(target);
								player.sendMessage("§a" + target.getName() + " a récupéré ses items.");
								target.sendMessage(
										Messages.PREFIX_WITH_ARROW.getMessage() + "Vous avez récupéré vos items.");
							} else {
								player.sendMessage(Messages.error("Ce joueur n'a pas de rôle."));
								return false;
							}
						} else {
							player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
							return false;
						}
					} else {
						player.sendMessage(Messages.NOT_NOW.getMessage());
						return false;
					}
				} else if (args[0].equalsIgnoreCase("deop")) {
					if(pl.gameManager.isCoHost(player)){
						return false;
					}
					if(args.length == 2){
						String targetName = args[1];
						Player target = Bukkit.getPlayer(targetName);
						if(target != null){
							pl.gameManager.removeCoHost(target);
							if(!GameState.isState(GameState.STARTED))
								NotStart.PreHosting(target);
							player.sendMessage("§c" + targetName + " a bien été retiré des co-host.");
						}
					}
				} else if (args[0].equalsIgnoreCase("force")) {
					if(args.length == 2){
						if (args[1].equalsIgnoreCase("pvp")) {
							pl.getGamemanager().setPvPTime(20*20);
							return true;
						}
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
		player.sendMessage("§f• /say §7: §eFaire une annonce.");
		player.sendMessage("§f• /revive <pseudo> §7: §eResscucité un joueur.");
		player.sendMessage("§f• /h help §7: §eVoir cette page.");
		//player.sendMessage("§6• /h force <invincibility/pvp/border/tp/roles> §7: §eForcer un événement de la partie.");
		/*player.sendMessage("§6• /h heal §7: §eSoigner tous les joueurs de la partie.");
		player.sendMessage("§6• /h wl list §7: §eVoir tous les joueurs présents dans la liste blanche.");
		player.sendMessage("§6• /h wl clear §7: §eVider la liste blanche.");
		player.sendMessage("§6• /h wl open §7: §eOuvrir la partie.");
		player.sendMessage("§6• /h wl close §7: §eFermer la partie.");
		player.sendMessage("§6• /h wl add <pseudo> §7: §eAjouter un joueur de la liste blanche.");
		player.sendMessage("§6• /h wl remove <pseudo> §7: §eRetirer un joueur de la liste blanche.");
		player.sendMessage("§6• /h kick <pseudo> <raison> §7: §eExpulser un joueur de la partie.");
		player.sendMessage("§6• /h give <item> <nombre> §7: §eDonner un item aux joueurs de la partie.");*/
		player.sendMessage("§f• /h kill <pseudo> §7: §eTue un joueur qui est en pleine partie (Fonctionne avec les joueurs déconnectés).");
		player.sendMessage("§f• /h recup <pseudo> §7: §eRedonne les items spéciaux à un joueur.");
		player.sendMessage("§f• /h heal §7: §eSoigner tout les joueurs de la partie.");
		player.sendMessage("§f• /h op <pseudo> §7: §eAjout un host à la partie.");
		player.sendMessage("§f• /h deop <pseudo> §7: §eSupprimer un host à la partie.");
		player.sendMessage("§f• /h reset <pseudo> §7: §eRéduire à 0 les cooldowns d'un joueur.");
		player.sendMessage("§f• /h clear <pseudo> §7: §eEnlève les malus spéciaux d'un joueur.");
		player.sendMessage("§f• /h config §7: §eOuvrir le menu de configuration de la partie.");
	}

}
