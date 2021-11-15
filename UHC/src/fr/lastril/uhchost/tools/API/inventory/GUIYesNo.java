package fr.lastril.uhchost.tools.API.inventory;

import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.function.Consumer;


public class GUIYesNo extends IQuickInventory {

	public IQuickInventory parentInv;
	public Consumer<ClickEvent> accept;
	
	public GUIYesNo(IQuickInventory parentInv, Consumer<ClickEvent> accept) {
		super(ChatColor.GREEN+"Êtes vous sûr de votre choix ?", 9);
		this.parentInv = parentInv;
		this.accept = accept;
	}

	@Override
	public void contents(QuickInventory inv) {
		inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);
		inv.setItem(new QuickItem(Material.STAINED_GLASS_PANE, 1, (byte) 5).setName("§aOui").toItemStack(), onClick -> {
			if(parentInv != null) {
				parentInv.open(onClick.getPlayer());
			}else{
				inv.close(onClick.getPlayer());
			}
			accept.accept(new ClickEvent(onClick.getPlayer(), onClick.getClickType()));
		}, 3);
		inv.setItem(new QuickItem(Material.STAINED_GLASS_PANE, 1, (byte) 14).setName("§cNon").toItemStack(), onClick -> {
			if(parentInv != null) {
				parentInv.open(onClick.getPlayer());
			}else{
				inv.close(onClick.getPlayer());
			}
		}, 5);
	}

	public class ClickEvent {

		public Player player;
		public ClickType type;

		public ClickEvent(Player player, ClickType type) {
			this.player = player;
			this.type = type;
		}

		public Player getPlayer() {
			return player;
		}

		public ClickType getType() {
			return type;
		}

	}

}
