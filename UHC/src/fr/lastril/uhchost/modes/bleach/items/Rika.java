package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.InoueOrihime;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Rika extends QuickItem {
    public Rika(UhcHost main) {
        super(Material.STAINED_GLASS, 1, (short) 4);
        super.glow(true);
        super.setLore("",
                "§fItem à posé au sol.",
                "",
                "§7Créer une zone de 5x5.",
                "§7A l'intérieur de la zone, les joueurs",
                "§drégénèrent 2 coeur§e par seconde§7.",
                "",
                "§fClique droit",
                "§7Une fois la zone créée, la cage",
                "§7disparait et ne fait plus effet.",
                "§7(Cooldown - 7 minutes)");
        super.setName("§eRika");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof InoueOrihime){
                InoueOrihime inoueOrihime = (InoueOrihime) playerManager.getRole();
                if(bleachPlayerManager.canUsePower()){
                    if(inoueOrihime.isAreaCreated()){
                        inoueOrihime.deleteArea();
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§6Vous venez de détruire votre zone.");
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            } else {
                player.sendMessage(Messages.not("InoueOrihime"));
            }
        });
    }
}
