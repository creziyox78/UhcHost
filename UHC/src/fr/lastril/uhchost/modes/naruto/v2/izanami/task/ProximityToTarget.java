package fr.lastril.uhchost.modes.naruto.v2.izanami.task;

import fr.lastril.uhchost.modes.naruto.v2.izanami.IzanamiGoal;
import fr.lastril.uhchost.modes.naruto.v2.izanami.IzanamiGoalAuthor;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ProximityToTarget extends BukkitRunnable {

    private final IzanamiGoal izanamiGoal;
    private final PlayerManager author, target;

    public ProximityToTarget(IzanamiGoal izanamiGoal) {
        this.izanamiGoal = izanamiGoal;
        this.author = izanamiGoal.getAuthor();
        this.target = izanamiGoal.getTarget();
    }


    @Override
    public void run() {
        if(target.isAlive() && author.isAlive()){
            Player player = author.getPlayer();
            Player targetPlayer = target.getPlayer();
            if(player != null && targetPlayer != null){
                if(player.getWorld() == targetPlayer.getWorld()){
                    if(player.getLocation().distance(targetPlayer.getLocation()) <= 20){
                        izanamiGoal.setTimeCloseToTarget(izanamiGoal.getTimeCloseToTarget() + 1);
                        if(izanamiGoal.getTimeCloseToTarget() == 5*60){
                            izanamiGoal.getIzanamiGoalAuthorList()
                                    .get(izanamiGoal.getIzanamiGoalAuthorList().indexOf(IzanamiGoalAuthor.PROXIMITYTARGET)).setDone(true);
                            izanamiGoal.moveTargetCamps();
                            cancel();
                        }
                    }
                }
            }
        }

    }
}
