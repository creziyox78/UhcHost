package fr.lastril.uhchost.modes.bleach.gui;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.ArrancarRole;
import fr.lastril.uhchost.modes.bleach.roles.arrancars.espada.Zommari;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

public class ZommariEffect extends IQuickInventory {

    private final Zommari zommari;

    private int index = 0;

    private final UhcHost main = UhcHost.getInstance();
    private final PlayerManager target;

    public ZommariEffect(Zommari zommari, PlayerManager target) {
        super("§eEffet de " + target.getPlayerName(), 9*1);
        this.zommari = zommari;
        this.target = target;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("effets", taskUpdate -> {
            Player targetPlayer = target.getPlayer();
            if (targetPlayer != null) {
                targetPlayer.getActivePotionEffects().forEach(effect -> {
                    ItemStack potion = new Potion(PotionType.getByEffect(effect.getType()), effect.getAmplifier()).toItemStack(1);
                    QuickItem item = new QuickItem(potion);
                    inv.setItem(item.setName(effect.getType().getName())
                            .setLore("",
                                    "§fEffet: §b" + effect.getType().getName(),
                                    "§fNiveau: §b" + effect.getAmplifier(),
                                    "",
                                    "§fCliquez pour voler cet effet.")
                            .toItemStack(), onClick -> {
                        Player player = onClick.getPlayer();
                        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
                        if(zommari.getCopiedEffect() != null) {
                            player.removePotionEffect(zommari.getCopiedEffect().getType());
                        }
                        if(bleachPlayerManager.isInFormeLiberer()) {
                            if(zommari.getCopiedEffect().getType() == effect.getType()) {
                                if(zommari.getCopiedEffect().getAmplifier() == effect.getAmplifier()) {
                                    zommari.setCopiedEffect(new PotionEffect(effect.getType(), 20*60*5, effect.getAmplifier() + 1, false, false));
                                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§aVous avez copié l'effet§e " + effect.getType().getName() + "§a.");
                                    return;
                                }
                            }
                            zommari.setCopiedEffect(effect);
                            return;
                        } else{
                            zommari.setCopiedEffect(effect);
                        }
                        player.addPotionEffect(zommari.getCopiedEffect());

                    },index);
                    index++;
                });
            }
        });
    }
}
