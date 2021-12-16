
package fr.lastril.uhchost.inventory.guis;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.inventory.guis.modes.ModesGui;
import fr.lastril.uhchost.inventory.guis.rules.RulesCategoriesGui;
import fr.lastril.uhchost.inventory.guis.timer.RulesGuiHost;
import fr.lastril.uhchost.inventory.guis.world.WorldGui;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.scenario.ScenariosGui;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;

public class HostConfig extends IQuickInventory {
	public HostConfig() {
		super(ChatColor.GOLD + "Configuration", 9*6);

	}

	@Override
	public void contents(QuickInventory inv) {
		inv.updateItem("update", taskUpdate -> {

			inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE, 1, (byte)1).setName("").toItemStack(), 0, 8);
			inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE, 1, (byte)1).setName("").toItemStack(), 9, 17);

			inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE, 1, (byte)1).setName("").toItemStack(), 36, 44);
			inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE, 1, (byte)1).setName("").toItemStack(), 45, 53);

			if (!GameState.isState(GameState.STARTING)) {
				inv.setItem(new QuickItem(Material.STAINED_CLAY, 1, (byte)5).setName(ChatColor.GOLD + "Lancer la partie").toItemStack(), onClick -> {
					final GameManager gameManager = UhcHost.getInstance().getGamemanager();
					if(!gameManager.isPregen()){
						onClick.getPlayer().sendMessage("§cErreur: La map n'a pas été prégénéré !");
						return;
					}
					GameState.setCurrentState(GameState.STARTING);
					new BukkitRunnable() {
						private int timer = 10;
						private int ticks = 20;
						@Override
						public void run() {
							if(GameState.isState(GameState.STARTING)) {

								ActionBar.broadcastMessage(ChatColor.YELLOW + "Lancement dans " + this.timer + " secondes.");
								if(timer == 0) {
									ActionBar.broadcastMessage(ChatColor.AQUA + "Lancement de la partie !");
									for(Player player: Bukkit.getOnlinePlayers()) {
										player.getInventory().clear();
									}
									UhcHost.getInstance().gameManager.preStart();
									cancel();
								}
								if(ticks <= 0){
									timer--;
									ticks = 20;
									for(Player player: Bukkit.getOnlinePlayers()) {
										player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
									}
								}
								ticks--;
							} else if(GameState.isState(GameState.LOBBY)) {
								cancel();
							}

						}
					}.runTaskTimer(UhcHost.getInstance(), 0L, 1L);
				},49);
			} else {
				inv.setItem(new QuickItem(Material.STAINED_CLAY, 1, (byte)14).setName(ChatColor.RED + "Annuler le lancement").toItemStack(), onClick -> {
					GameState.setCurrentState(GameState.LOBBY);
				},49);
			}
			Modes modes = UhcHost.getInstance().getGamemanager().getModes();
			inv.setItem(modes.getItem().toItemStack(), onClick -> {
				if(onClick.getEvent().getCurrentItem().isSimilar(modes.getItem().toItemStack()) && modes.getMode() instanceof ModeConfig){
					ModeConfig config = (ModeConfig) modes.getMode();
					config.getGui().open(onClick.getPlayer());
				} else {
					onClick.getPlayer().sendMessage(Messages.error("Ce mode n'est pas configurable ou vous ne l'avez pas séléctionné !"));
				}
			},22);

			inv.setItem(new QuickItem(Material.WATCH).setName(ChatColor.GOLD + "Modes de jeu").toItemStack(), onClick -> {
				new ModesGui().open(onClick.getPlayer());
			},4);
			inv.setItem(new QuickItem(Material.HOPPER_MINECART).setName(ChatColor.GOLD + "Paramètres UHC").toItemStack(), onClick -> {
				new RulesGuiHost().open(onClick.getPlayer());
			},28);

			inv.setItem(new QuickItem(Material.ITEM_FRAME).setName(ChatColor.GOLD + "Règles UHC").toItemStack(), onClick -> {
				new RulesCategoriesGui().open(onClick.getPlayer());
			},31);

			inv.setItem(new QuickItem(Material.NAME_TAG).setName(ChatColor.GOLD + "Scenarios").toItemStack(), onClick -> {
				new ScenariosGui().open(onClick.getPlayer());
			},34);


			ItemsCreator ic =
					new ItemsCreator(Material.SKULL_ITEM, I18n.tl("guis.main.maxPlayers"),
							Collections.singletonList(I18n.tl("guis.main.maxPlayersLore")));
			inv.setItem(ic.createHead("MHF_Golem"), onClick -> {
				new MaxPlayersGui().open(onClick.getPlayer());
			},1);
			inv.setItem(new QuickItem(Material.DIODE).setName(ChatColor.YELLOW + "Nom du serveur").toItemStack(), onClick -> {
				new SetNameUHCGui(UhcHost.getInstance()).open(onClick.getPlayer());
			},52);
			inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY5MTk2YjMzMGM2Yjg5NjJmMjNhZDU2MjdmYjZlY2NlNDcyZWFmNWM5ZDQ0Zjc5MWY2NzA5YzdkMGY0ZGVjZSJ9fX0=")
					.setName("§6Paramètre du monde").toItemStack(), onClick -> {
				new WorldGui().open(onClick.getPlayer());
			},7);


		});
	}
}