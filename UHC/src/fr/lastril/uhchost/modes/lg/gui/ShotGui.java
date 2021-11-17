package fr.lastril.uhchost.modes.lg.gui;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.lg.roles.village.Chasseur;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;

public class ShotGui extends IQuickInventory {

    private final Chasseur chasseur;
    private final UhcHost main = UhcHost.getInstance();

    public ShotGui(Chasseur chasseur) {
        super("§aTir de chasseur", 5*9);
        this.chasseur = chasseur;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("alivePlayer", taskUpdate -> {
            int index = -1;
            for(PlayerManager playerManager : main.getPlayerManagerAlives()){
                index++;
                if(playerManager.getPlayer() != null){
                    Player player = playerManager.getPlayer();
                    inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                            .setName(playerManager.getPlayerName()).setSkullOwner(playerManager.getPlayerName())
                            .setLore("",
                                    "§7Cliquez pour tirer sur ce joueur.")
                            .toItemStack(), onClick -> {
                        chasseur.shot(player);
                        onClick.getPlayer().closeInventory();
                    }, index);
                }
            }
        });
    }
}
