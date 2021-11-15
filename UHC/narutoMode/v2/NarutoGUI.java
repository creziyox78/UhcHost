package fr.lastril.uhchost.inventory.guis.modes.naruto.v2;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.NarutoV2Config;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NarutoGUI extends IQuickInventory {

	private final UhcHost main;
	private final NarutoV2Config narutoV2Config;
	
	public NarutoGUI(UhcHost main, NarutoV2Config narutoV2Config) {
		super("§6Naruto V2", 9*3);
		this.main = main;
		this.narutoV2Config = narutoV2Config;
	}

	@Override
	public void contents(QuickInventory inv) {
		inv.updateItem("update", onUpdate -> {

			for (int i = 0; i < inv.getInventory().getSize() - 1; i++) {
				inv.setItem(new ItemStack(Material.AIR), i);
			}

			inv.setItem(new QuickItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)3)).setName(" ").toItemStack(), 0);
			inv.setItem(new QuickItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)3)).setName(" ").toItemStack(), 1);
			inv.setItem(new QuickItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)3)).setName(" ").toItemStack(), 9);

			inv.setItem(new QuickItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)3)).setName(" ").toItemStack(), 7);
			inv.setItem(new QuickItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)3)).setName(" ").toItemStack(), 8);
			inv.setItem(new QuickItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)3)).setName(" ").toItemStack(), 17);

			inv.setItem(new QuickItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)3)).setName(" ").toItemStack(), getSize()-2);

			inv.setItem(new QuickItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)3)).setName(" ").toItemStack(), getSize()-8);
			inv.setItem(new QuickItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)3)).setName(" ").toItemStack(), getSize()-9);

			inv.setItem(new QuickItem(Material.SKULL_ITEM, 1,SkullType.PLAYER.ordinal())
					.setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU4ZmQwYmM3ZTUyOTA0NzQ3MWIwZmI3MTY0Mjg0NTU2NjY2NGFlMmJkN2JmZmNlMGUxNzNjOTUyNWEwNTIzOSJ9fX0=")
					.setName("§6Bijû")
					.setLore(narutoV2Config.isBiju() ? "§aActivé" : "§cDésactivé")
					.toItemStack(), onClick -> {
				narutoV2Config.setBiju(!narutoV2Config.isBiju());

			},10);

			inv.setItem(new QuickItem(Material.WATCH).setName("§aAnnonce du Hokage")
					.setLore("§2Temps : " + new FormatTime(narutoV2Config.getHokageAnnoncement()).toString() +" minutes.",
							"§7Clique gauche +5 minute", "§7Clique droit -5 minute")
					.toItemStack(), onClick -> {
						int interval = narutoV2Config.getHokageAnnoncement();
						if (onClick.getClickType() == ClickType.LEFT) {
							if(narutoV2Config.getHokageAnnoncement() < 60*60){
								narutoV2Config.setHokageAnnoncement(interval + 5*60);
							}
						} else if (onClick.getClickType() == ClickType.RIGHT) {
							if(narutoV2Config.getHokageAnnoncement() > narutoV2Config.getRoleAnnouncement()){
								narutoV2Config.setHokageAnnoncement(interval - 5*60);
							}
							else {
								onClick.getPlayer().sendMessage(Messages.error("L'Hokage ne peut pas être désigné avant les rôles."));
							}
						}

					}, 14);

			List<String> roles = new ArrayList<>();
			roles.add("§aRôles ajoutés : " + main.gameManager.getComposition().size());
			roles.add("§8§m-----------------");
			for (Map.Entry<Class<? extends Role>, Integer> role : main.gameManager.getCompositionSorted().entrySet()) {
				Role roleInstance = null;
				try {
					roleInstance = role.getKey().newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}

				try {
					roles.add(roleInstance.getCamp().getCompoColor() + roleInstance.getRoleName() + " §cx" + role.getValue());
				} catch (Exception e) {
				}
			}

		}, 1);
	}

}
