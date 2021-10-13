package fr.lastril.uhchost.inventory;

import java.util.HashMap;
import java.util.function.Consumer;

import fr.lastril.uhchost.UhcHost;
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

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.ContainerAnvil;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;

public class AnvilGUI {

    private class AnvilContainer extends ContainerAnvil {
        public AnvilContainer(EntityHuman entity) {
            super(entity.inventory, entity.world, new BlockPosition(0, 0, 0), entity);
        }

        @Override
        public boolean a(EntityHuman entityhuman) {
            return true;
        }
    }

    public enum AnvilSlot {
        INPUT_LEFT(0), INPUT_RIGHT(1), OUTPUT(2);

        private int slot;

        private AnvilSlot(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return slot;
        }

        public static AnvilSlot bySlot(int slot) {
            for (AnvilSlot anvilSlot : values()) {
                if (anvilSlot.getSlot() == slot) {
                    return anvilSlot;
                }
            }

            return null;
        }
    }

    public class AnvilClickEvent {
        private final Player player;
        private final AnvilSlot slot;

        private final ItemStack item;

        public AnvilClickEvent(Player player, AnvilSlot slot, ItemStack item) {
            this.player = player;
            this.slot = slot;
            this.item = item;
        }

        public Player getPlayer() {
            return this.player;
        }

        public AnvilSlot getSlot() {
            return this.slot;
        }

        public ItemStack getItem() {
            return this.item;
        }
    }

    private final Consumer<AnvilClickEvent> onClick;
    private Player player;

    private HashMap<AnvilSlot, ItemStack> items = new HashMap<>();

    private Inventory inv;

    private static Listener listener;

    public AnvilGUI(Consumer<AnvilClickEvent> onClick) {
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
                                new AnvilClickEvent((Player) event.getWhoClicked(), AnvilSlot.bySlot(slot), item));
                    }
                }
            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent event) {
                if (event.getPlayer() instanceof Player) {
                    Inventory inv = event.getInventory();

                    if (inv.equals(AnvilGUI.this.inv)) {
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

    public void setSlot(AnvilSlot slot, ItemStack item) {
        items.put(slot, item);
    }

    public void open(Player player) {
        this.player = player;
        EntityPlayer p = ((CraftPlayer) player).getHandle();

        AnvilContainer container = new AnvilContainer(p);

        // Set the items to the items from the inventory given
        inv = container.getBukkitView().getTopInventory();

        for (AnvilSlot slot : items.keySet()) {
            inv.setItem(slot.getSlot(), items.get(slot));
        }

        // Counter stuff that the game uses to keep track of inventories
        int c = p.nextContainerCounter();

        // Send the packet
        p.playerConnection.sendPacket(
                new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("Repairing", new Object[] {}), 0));

        // Set their active container to the container
        p.activeContainer = container;

        // Set their active container window id to that counter stuff
        p.activeContainer.windowId = c;

        // Add the slot listener
        p.activeContainer.addSlotListener(p);
    }

    public static void destroy(Player player) {
        HandlerList.unregisterAll(listener);
        if(player.getOpenInventory() != null) {
            player.getOpenInventory().getTopInventory().clear();
            player.closeInventory();
        }
    }

}
