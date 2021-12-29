package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.biju.Biju;
import fr.lastril.uhchost.modes.naruto.v2.gui.TrackerBijuGUI;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Madara;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Obito;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class TrackerBijuItem extends QuickItem {

    private int index = 0;

    private Biju bijuTracked;

    public TrackerBijuItem(UhcHost main) {
        super(Material.COMPASS);
        super.setName("§6Tracker de Bijû");
        super.setLore();
        super.onClick(onClick -> {
           Player player = onClick.getPlayer();
           PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
           if(onClick.getAction() == Action.RIGHT_CLICK_BLOCK || onClick.getAction() == Action.RIGHT_CLICK_AIR){
               if(joueur.hasRole()){
                   if(joueur.getRole() instanceof Obito){
                       Obito obito = (Obito) joueur.getRole();
                       new TrackerBijuGUI(obito, null,main).open(player);
                   } else if(joueur.getRole() instanceof Madara){
                       Madara madara = (Madara) joueur.getRole();
                       new TrackerBijuGUI(null, madara,main).open(player);
                   } else {
                       player.sendMessage(Messages.not("Obito ou Madara"));
                   }
               }
           }

        });
    }
}
