package fr.lastril.uhchost.modes.naruto.v2.gui.edotensei;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.EdoTenseiItem;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EdoTenseiGUI extends IQuickInventory {

	private final UhcHost main;
	private final EdoTenseiItem.EdoTenseiUser user;

	public EdoTenseiGUI(UhcHost main, EdoTenseiItem.EdoTenseiUser user) {
		super(ChatColor.DARK_PURPLE + "Edo Tensei", 5 * 9);
		this.main = main;
		this.user = user;
	}

	@Override
	public void contents(QuickInventory inv) {
		inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);
		Role user = (Role) this.user;
		if (user.getPlayerId() != null) {
			PlayerManager userJoueur = main.getPlayerManager(user.getPlayerId());
			for (PlayerManager joueurs : main.getAllPlayerManager().values()) {
				if (!joueurs.isAlive() && joueurs != userJoueur) {
					if (joueurs.getDeathLocation() != null && joueurs.getPlayer() != null) {
						if(joueurs.getDeathLocation().getWorld() == userJoueur.getPlayer().getWorld()){
							if (joueurs.getDeathLocation().distance(userJoueur.getPlayer().getLocation()) <= 15) {
								inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
										.setName(joueurs.getPlayerName()).setSkullOwner(joueurs.getPlayerName())
										.setLore("",
												"§7Cliquez pour ressusciter ce joueur.")
										.toItemStack(), onClick -> {
									Player edoUser = onClick.getPlayer();
									PlayerManager joueur = main.getPlayerManager(edoUser.getUniqueId());
									joueurs.getPlayer().teleport(edoUser);
									joueurs.setAlive(true);
									//joueurs.setSpectator(false);
									joueurs.getPlayer().setGameMode(GameMode.SURVIVAL);
									//UhcHost.getInstance().getSpectatorTeam().removeEntry(joueurs.getPlayer().getName());
									joueurs.setCamps(joueur.getCamps());
									user.getPlayer().setItemInHand(null);
									giveStuff(joueurs.getPlayer());

									edoUser.sendMessage(
											Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
									joueurs.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage()
											+ "Vous venez d'être réssussité et vous gagnez désormais avec le camp d'§5Orochimaru§e.");

									/*main.getMumbleManager().onPlayerRevive(joueurs);
									new BukkitRunnable() {
										@Override
										public void run() {
											main.shuffled.remove(joueurs);
											if(joueurs.getMumbleData().getMumbleUser() != null) {
												joueurs.getMumbleData().getMumbleUser().mute(false);
												Main.debug("Mumble Manager: demuted player");
											}
										}
									}.runTaskLater(main, 40);*/

									if (joueur.getRole() instanceof NarutoV2Role) {
										NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
										narutoRole.usePower(joueur);
									}
									inv.close(joueurs.getPlayer());
								});
							}
						}
					}
				}
			}

		}

	}
	
	private void giveStuff(Player player){
        player.getInventory().clear();
        player.getInventory().addItem(new QuickItem(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1, true).toItemStack());
        player.getInventory().addItem(new QuickItem(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 1, true).toItemStack());
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 3));
        player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
        player.getInventory().addItem(new ItemStack(Material.LOG, 64));
        player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
        player.getInventory().setHelmet(new QuickItem(Material.IRON_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).toItemStack());
        player.getInventory().setChestplate(new QuickItem(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).toItemStack());
        player.getInventory().setLeggings(new QuickItem(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).toItemStack());
        player.getInventory().setBoots(new QuickItem(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).toItemStack());

        PlayerManager joueur = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
        if(joueur.hasRole()){
            joueur.getRole().stuff(player);
        }

    }
}
