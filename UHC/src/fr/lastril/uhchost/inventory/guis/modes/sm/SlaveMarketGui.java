package fr.lastril.uhchost.inventory.guis.modes.sm;

import fr.lastril.uhchost.config.modes.SlaveMarketConfig;
import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.modes.sm.MarketStatus;
import fr.lastril.uhchost.modes.sm.SlaveMarketMode;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;

public class SlaveMarketGui extends IQuickInventory {

    private final SlaveMarketConfig slaveMarketConfig;
    private final SlaveMarketMode slaveMarketMode;

    public SlaveMarketGui(SlaveMarketMode slaveMarketMode) {
        super("§BSlave Market Configuration", 3*9);
        this.slaveMarketMode = slaveMarketMode;
        this.slaveMarketConfig = slaveMarketMode.getSlaveMarketConfig();
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("slavemarket", taskUpdate -> {
            inv.setItem(new QuickItem(Material.DIAMOND).setName("§9Diamants par owner:§b " + slaveMarketConfig.getDiamondsPerOwners())
                    .setLore("",
                            "§cClique droit : -1",
                            "§aClique gauche : +1")
                    .toItemStack(), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    slaveMarketConfig.setDiamondsPerOwners(slaveMarketConfig.getDiamondsPerOwners() + 1);
                } else if(onClick.getClickType() == ClickType.RIGHT){
                    slaveMarketConfig.setDiamondsPerOwners(slaveMarketConfig.getDiamondsPerOwners() - 1);
                }
            },10);

            inv.setItem(new QuickItem(Material.WATCH).setName("§eTemps d'achat par joueur:§b " + slaveMarketConfig.getTimePerBuy() + " secondes")
                    .setLore("",
                            "§cClique droit : -1",
                            "§aClique gauche : +1")
                    .toItemStack(), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    slaveMarketConfig.setTimePerBuy(slaveMarketConfig.getTimePerBuy() + 1);
                } else if(onClick.getClickType() == ClickType.RIGHT){
                    slaveMarketConfig.setTimePerBuy(slaveMarketConfig.getTimePerBuy() - 1);
                }
            },12);
            if(MarketStatus.getInstance().isMarketStatus(MarketStatus.WAITING)){
                inv.setItem(new QuickItem(Material.EMERALD_BLOCK).setName("§a§lDémarrer les achats.")
                        .setLore("",
                                "§cAssurez-vous que les owners choisis",
                                "§csont bien tous présent !")
                        .toItemStack(), onClick -> {
                    MarketStatus.setMarketStatus(MarketStatus.STARTING);
                },14);
            }
            if(MarketStatus.getInstance().isMarketStatus(MarketStatus.STARTING)){
                inv.setItem(new QuickItem(Material.REDSTONE_BLOCK).setName("§c§lAnnuler le décompte")
                        .setLore("")
                        .toItemStack(), onClick -> {
                    MarketStatus.setMarketStatus(MarketStatus.WAITING);
                },14);
            }


            inv.addRetourItem(new HostConfig());
        });
    }
}
