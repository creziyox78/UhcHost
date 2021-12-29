package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.KillerBee;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ReviveItem extends QuickItem {

    private NarutoV2Manager narutoV2Manager;

    public ReviveItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§aRevive");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if(joueur.hasRole()){
               if(joueur.getRole() instanceof KillerBee){
                   KillerBee killerBee = (KillerBee) joueur.getRole();
                   killerBee.setFakeDeath(false);
                   //main.getInvisibleTeam().removeEntry(player.getName());
                   for(Player players : Bukkit.getOnlinePlayers()) {
                       players.showPlayer(player);
                   }
                   player.setItemInHand(null);
                   killerBee.usePowerSpecific(joueur, super.getName());
               } else {
                   player.sendMessage(Messages.not("Killer Bee"));
               }
           }
        });
    }
}
