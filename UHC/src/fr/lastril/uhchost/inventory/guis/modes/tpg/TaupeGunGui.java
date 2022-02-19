package fr.lastril.uhchost.inventory.guis.modes.tpg;

import fr.lastril.uhchost.config.modes.TaupeGunConfig;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;

public class TaupeGunGui extends IQuickInventory {

    private final TaupeGunConfig taupeGunConfig;

    public TaupeGunGui(TaupeGunConfig taupeGunConfig) {
        super("§BTaupe Gun Configuration", 3*9);
        this.taupeGunConfig = taupeGunConfig;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("taupgun", taskUpdate -> {
            inv.setItem(new QuickItem(Material.WATCH).setName("§aAnnonces des taupes")
                    .setLore("§b" + new FormatTime(taupeGunConfig.getMolesTime()).toFormatString()).toItemStack(), onClick -> {
                new TaupeTimeGui(taupeGunConfig).open(onClick.getPlayer());
            },9);
            int index = 1;
            for(TaupeGunConfig.TaupePresets presets : TaupeGunConfig.TaupePresets.values()){

                inv.setItem(new QuickItem(Material.NETHER_STAR).setName("§bPréset : " + index).setLore("",
                        "§7To:§b " + presets.getTeamOf(),
                        "§7Nombre de slots:§b " + presets.getSlots(),
                        "§7Taille des équipes de taupes:§b " + presets.getMolesPerTeam(),
                        "§7Nombre de taupe par équipe:§b " + presets.getMolesPerTeam(),
                        "§7Super-Taupe : " + (presets.isSuperMoles() ? "§aActivé" : "§cDésactivé")).toItemStack(), onClick -> {
                    taupeGunConfig.setPreset(presets);
                    onClick.getPlayer().sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§aPréset choisis avec succès !");
                },index + 10);
                index++;
            }
        });
    }
}
