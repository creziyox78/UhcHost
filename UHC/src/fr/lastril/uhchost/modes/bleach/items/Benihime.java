package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.gui.BenihimeGui;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.KisukeUrahara;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class Benihime extends QuickItem {
    public Benihime(UhcHost main) {
        super(Material.GOLD_SWORD);
        super.setName("§6Benihime");
        super.setLore("",
                "§7Permet d'appliquer un des effets",
                "§7suivants aux joueurs frappés",
                "§7dans les 60 derniers secondes.",
                "",
                "§7 - Retirer§c 2 coeurs permanents§7 pendant 3 minutes",
                "§7 - Infliger§8 Slowness 2 pendant§7 20 secondes",
                "§7 - §dSoigner§c 7 coeurs §7instantanément",
                "§7 - Donner l’effet§c Force 1 §7pendant 1 minute 30 secondes",
                "§7 - §eEchanger sa position§7 avec le joueur ciblé",
                "",
                "§7(Cooldown - 10 minutes) (3 utilisations max)");
        super.glow(true);
        super.setInfinityDurability();
        super.addItemFlag(ItemFlag.HIDE_UNBREAKABLE);
        super.onClick(onCliclk -> {
            Player player = onCliclk.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof KisukeUrahara) {
                KisukeUrahara kisukeUrahara = (KisukeUrahara) playerManager.getRole();
                if(bleachPlayerManager.canUsePower()) {
                    if(playerManager.getRoleCooldownBenihime() <= 0) {
                        if(kisukeUrahara.getBenehimeUsages() < 3) {
                            new BenihimeGui(kisukeUrahara).open(player);
                        } else {
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous avez déjà utilisé votre pouvoir 3 fois.");
                        }
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownBenihime()));
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            }
        });

    }
}
