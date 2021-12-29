package fr.lastril.uhchost.modes.naruto.v2.izanami.event;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.naruto.v2.izanami.IzanamiGoal;
import fr.lastril.uhchost.modes.naruto.v2.izanami.IzanamiGoalTarget;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class WalkTarget implements Listener {

    private final PlayerManager target;
    private final IzanamiGoal izanamiGoal;
    private final UhcHost main;

    public WalkTarget(UhcHost main, PlayerManager joueur, IzanamiGoal izanamiGoal) {
        this.main = main;
        this.target = joueur;
        this.izanamiGoal = izanamiGoal;
    }

    @EventHandler
    public void onWalk(PlayerMoveEvent event){
        if(event.getTo() != event.getFrom()){
            Player player = event.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if(joueur == target){
                double distance = event.getFrom().distance(event.getTo());
                izanamiGoal.setWalkTarget(izanamiGoal.getWalkTarget() + distance);
                if(izanamiGoal.getWalkTarget() >= 1000){
                    izanamiGoal.getIzanamiGoalTargetList()
                            .get(izanamiGoal.getIzanamiGoalTargetList().indexOf(IzanamiGoalTarget.WALKONEKILOMETER)).setDone(true);
                    HandlerList.unregisterAll(this);
                }
            }
        }
    }

}
