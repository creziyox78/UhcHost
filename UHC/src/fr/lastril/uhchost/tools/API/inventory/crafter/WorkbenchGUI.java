package fr.lastril.uhchost.tools.API.inventory.crafter;

import fr.lastril.uhchost.UhcHost;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.function.Consumer;

public class WorkbenchGUI {
	
	private class WorkbenchContainer extends ContainerWorkbench {
		public WorkbenchContainer(EntityHuman entity) {
			super(entity.inventory, entity.world, new BlockPosition(0, 0, 0));
		}
		@Override
		public boolean a(EntityHuman entityhuman) {
			return true;
		}
	}
	
	public enum WorkbenchSlot {
		RESULT(0),
		HIGH_LEFT(1), HIGH_CENTER(2), HIGH_RIGHT(3),
		MIDDLE_LEFT(4), MIDDLE_CENTER(5), MIDDLE_RIGHT(6),
		DOWN_LEFT(7), DOWN_CENTER(8), DOWN_RIGHT(9);
		
		
		private int slot;

		private WorkbenchSlot(int slot) {
			this.slot = slot;
		}


		public int getSlot() {
			return slot;
		}
		
		public static WorkbenchSlot bySlot(int slot) {
			for (WorkbenchSlot workbenchSlot : values()) {
				if (workbenchSlot.getSlot() == slot) {
					return workbenchSlot;
				}
				else {
					return null;
				}
			}

			return null;
		}

		
		
	}
	
	public class WorkbenchClickEvent {
		private final Player player;
		private final WorkbenchSlot slot;

		private final ItemStack item;

		public WorkbenchClickEvent(Player player, WorkbenchSlot slot, ItemStack item) {
			this.player = player;
			this.slot = slot;
			this.item = item;
		}

		public Player getPlayer() {
			return this.player;
		}

		public WorkbenchSlot getSlot() {
			return this.slot;
		}

		public ItemStack getItem() {
			return this.item;
		}
	}
	
	private final Consumer<WorkbenchClickEvent> onClick;
	private Player player;

	private HashMap<WorkbenchSlot, ItemStack> items = new HashMap<>();

	private Inventory inv;

	private static Listener listener;

	public WorkbenchGUI(Consumer<WorkbenchClickEvent> onClick) {
		this.onClick = onClick;
		this.listener = new Listener() {
			@EventHandler
			public void onInventoryClick(InventoryClickEvent event) {
				if (event.getWhoClicked() instanceof Player) {

					if (event.getInventory().equals(inv)) {
						event.setCancelled(true);

						ItemStack item = event.getCurrentItem();
						int slot = event.getRawSlot();

						onClick.accept(
								new WorkbenchClickEvent((Player) event.getWhoClicked(), WorkbenchSlot.bySlot(slot), item));
					}
				}
			}

			@EventHandler
			public void onInventoryClose(InventoryCloseEvent event) {
				if (event.getPlayer() instanceof Player) {
					Inventory inv = event.getInventory();

					if (inv.equals(WorkbenchGUI.this.inv)) {
						inv.clear();
						destroy((Player) event.getPlayer());
					}
				}
			}

			@EventHandler
			public void onPlayerQuit(PlayerQuitEvent event) {
				if (event.getPlayer().equals(player)) {
					destroy(event.getPlayer());
				}
			}
		};

		Bukkit.getPluginManager().registerEvents(listener, UhcHost.getInstance());
	}

	public void setSlot(WorkbenchSlot slot, ItemStack item) {
		items.put(slot, item);
	}

	public void open(Player player) {
		this.player = player;
		EntityPlayer p = ((CraftPlayer) player).getHandle();

		WorkbenchContainer container = new WorkbenchContainer(p);

		// Set the items to the items from the inventory given
		inv = container.getBukkitView().getTopInventory();

		for (WorkbenchSlot slot : items.keySet()) {
			inv.setItem(slot.getSlot(), items.get(slot));
		}

		// Counter stuff that the game uses to keep track of inventories
		int c = p.nextContainerCounter();

		// Send the packet
		p.playerConnection.sendPacket(
				new PacketPlayOutOpenWindow(c, "minecraft:crafting_table", new ChatMessage("WorkBench", new Object[] {}), 0));

		// Set their active container to the container
		p.activeContainer = container;

		// Set their active container window id to that counter stuff
		p.activeContainer.windowId = c;

		// Add the slot listener
		p.activeContainer.addSlotListener(p);
	}

	public static void destroy(Player player) {
		HandlerList.unregisterAll(listener);
		player.getOpenInventory().getTopInventory().clear();
		player.closeInventory();
	}

}
