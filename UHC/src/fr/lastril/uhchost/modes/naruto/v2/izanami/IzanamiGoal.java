package fr.lastril.uhchost.modes.naruto.v2.izanami;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.items.GenjutsuItem;
import fr.lastril.uhchost.modes.naruto.v2.izanami.event.WalkTarget;
import fr.lastril.uhchost.modes.naruto.v2.izanami.task.ProximityToTarget;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.PlayerKillEvent;
import fr.lastril.uhchost.tools.API.FormatTime;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.ArrayList;
import java.util.List;

public class IzanamiGoal implements Listener {

    private final UhcHost main;
    private final PlayerManager target, author;
    private final List<IzanamiGoalAuthor> izanamiGoalAuthorList;
    private final List<IzanamiGoalTarget> izanamiGoalTargetList;
    private final GenjutsuItem.GenjutsuUser genjutsuUser;
    private int damagedTarget = 0, timeCloseToTarget = 0, eatGappleTarget = 0;
    private double walkTarget = 0;
    private boolean gappleTarget, gappleAuthor, placeLaveOnTarget, targetKill, damagedAuthor, giveGappleToTarget, giveGappleToAuthor, finishWalk;


    public IzanamiGoal(UhcHost main, PlayerManager target, PlayerManager author, GenjutsuItem.GenjutsuUser genjutsuUser){
        this.main = main;
        this.genjutsuUser = genjutsuUser;
        this.target = target;
        this.author = author;
        this.izanamiGoalAuthorList = new ArrayList<>();
        this.izanamiGoalTargetList = new ArrayList<>();
        selectGoalAuthor();
        selectGoalTarget();
        if(izanamiGoalTargetList.contains(IzanamiGoalTarget.WALKONEKILOMETER)){
            main.getServer().getPluginManager().registerEvents(new WalkTarget(main, target, this), main);
        }
        if(izanamiGoalAuthorList.contains(IzanamiGoalAuthor.PROXIMITYTARGET)){
            main.getServer().getScheduler().runTaskTimer(main, new ProximityToTarget(this), 0, 20);
        }
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    private void selectGoalAuthor(){
        for (int i = 0; i < 2; i++) {
            IzanamiGoalAuthor[]izanamiGoal=IzanamiGoalAuthor.values();
            IzanamiGoalAuthor goal = izanamiGoal[UhcHost.getRANDOM().nextInt(IzanamiGoalAuthor.values().length)];
            while (izanamiGoalAuthorList.contains(goal)){
                goal = izanamiGoal[UhcHost.getRANDOM().nextInt(IzanamiGoalAuthor.values().length)];
            }
            izanamiGoalAuthorList.add(goal);
        }
    }

    public void moveTargetCamps(){
        if(completeAuthorGoal() && completeTargetGoal()){
            if(author.isAlive() && target.isAlive()){
                target.setCamps(author.getCamps());
                genjutsuUser.setCompleteIzanami(true);
                if(target.getPlayer() != null){
                    target.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage()
                            + "§6Sans vous en rendre compte, vous avez accomplis les objectifs de votre infection par l'Izanami. Vous devez gagner maintenant avec§e " + author.getPlayerName() + "§6 qui fait partie du camp " + author.getCamps().getCompoColor() +author.getCamps() + "§6.");
                }
                if(author.getPlayer() != null){
                    author.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage()
                            + "§aLes objectifs ont été accomplies. " + target.getPlayerName() + " à rejoint votre camp actuel et gagne donc avec vous.");
                    author.getPlayer().sendMessage("§eVous recevrez§7 Blindness I§6 toutes les 1 minutes§e pendant§c 5 secondes§e. Pour enlevez cet effet, vous devez tuer un Uchiwa (Madara, Obito, Itachi, Sasuke).");

                }
            }
        }
    }

    private boolean completeAuthorGoal(){
        for (IzanamiGoalAuthor goalAuthor : izanamiGoalAuthorList) {
            if(!goalAuthor.isDone())
                return false;
        }
        return true;
    }

    private boolean completeTargetGoal(){
        for (IzanamiGoalTarget goalTarget : izanamiGoalTargetList) {
            if(!goalTarget.isDone())
                return false;
        }
        return true;
    }

    private void selectGoalTarget(){
        IzanamiGoalTarget[]izanamiGoal=IzanamiGoalTarget.values();
        IzanamiGoalTarget goal = izanamiGoal[UhcHost.getRANDOM().nextInt(IzanamiGoalTarget.values().length)];
        izanamiGoalTargetList.add(goal);
    }

    @EventHandler
    public void placeLavaBucket(PlayerInteractEvent event){
        if(event.getItem() != null && event.getItem().getType() == Material.LAVA_BUCKET){
            Player player = event.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if(izanamiGoalAuthorList.contains(IzanamiGoalAuthor.PLACELAVA) && !izanamiGoalAuthorList.get(izanamiGoalAuthorList.indexOf(IzanamiGoalAuthor.PLACELAVA)).isDone()){
                if(joueur == author && !placeLaveOnTarget){
                    Block block = event.getClickedBlock();
                    Location location = block.getLocation().add(0, 1, 0);
                    for(Entity entity : location.getWorld().getNearbyEntities(location,1, 1, 1)){
                        if(entity instanceof Player){
                            Player nearPlayer = (Player) entity;
                            PlayerManager targetJoueur = main.getPlayerManager(nearPlayer.getUniqueId());
                            if(targetJoueur == target){
                                placeLaveOnTarget = true;
                                izanamiGoalAuthorList.get(izanamiGoalAuthorList.indexOf(IzanamiGoalAuthor.PLACELAVA)).setDone(true);
                                moveTargetCamps();
                            }
                        }
                    }
                }
            }
        }
    }

    /*@EventHandler
    public void dropGapple(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getType() == Material.GOLDEN_APPLE) {
            if(izanamiGoalAuthorList.contains(IzanamiGoalAuthor.GIVEJOUEUR) && !izanamiGoalAuthorList.get(izanamiGoalAuthorList.indexOf(IzanamiGoalAuthor.GIVEJOUEUR)).isDone()){
                if(main.getJoueur(e.getPlayer().getUniqueId()) == author){
                    gappleAuthor = true;
                    Bukkit.getServer().getScheduler().runTaskLater(main, () -> gappleAuthor = false, 20 * 30L);
                } else  if(main.getJoueur(e.getPlayer().getUniqueId()) == target){
                    gappleTarget = true;
                    Bukkit.getServer().getScheduler().runTaskLater(main, () -> gappleTarget = false, 20 * 30L);
                }
            }
        }
    }*/

    /*@EventHandler
    public void pickupGapple(PlayerPickupItemEvent event){
        if (event.getItem().getItemStack().getType() == Material.GOLDEN_APPLE) {
            if(izanamiGoalAuthorList.contains(IzanamiGoalAuthor.GIVEJOUEUR) && !izanamiGoalAuthorList.get(izanamiGoalAuthorList.indexOf(IzanamiGoalAuthor.GIVEJOUEUR)).isDone()){
                if(main.getJoueur(event.getPlayer().getUniqueId()) == author && gappleTarget){
                    izanamiGoalAuthorList.get(izanamiGoalAuthorList.indexOf(IzanamiGoalAuthor.GIVEJOUEUR)).setDone(true);
                    giveGappleToAuthor = true;
                    moveTargetCamps();
                } else  if(main.getJoueur(event.getPlayer().getUniqueId()) == target && gappleAuthor){
                    giveGappleToTarget = true;
                    izanamiGoalAuthorList.get(izanamiGoalAuthorList.indexOf(IzanamiGoalAuthor.GIVETARGET)).setDone(true);
                    moveTargetCamps();
                }
            }
        }
    }*/


    @EventHandler
    public void onIzanamiConsumeGapple(PlayerItemConsumeEvent event){

        if(izanamiGoalTargetList.contains(IzanamiGoalTarget.EATGOLDENAPPLE) && !izanamiGoalTargetList.get(izanamiGoalTargetList.indexOf(IzanamiGoalTarget.EATGOLDENAPPLE)).isDone()){
            Player player = event.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if(event.getItem().getType() == Material.GOLDEN_APPLE){
                if(joueur == target){
                    eatGappleTarget++;
                    if(eatGappleTarget == 5){
                        izanamiGoalTargetList.get(izanamiGoalTargetList.indexOf(IzanamiGoalTarget.EATGOLDENAPPLE)).setDone(true);
                        moveTargetCamps();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamageIzanamiTarget(EntityDamageByEntityEvent event){
        if(izanamiGoalAuthorList.contains(IzanamiGoalAuthor.DAMAGEPLAYER) || izanamiGoalAuthorList.contains(IzanamiGoalAuthor.DAMAGETARGET)){
            if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
                Player damager = (Player) event.getDamager();
                Player player = (Player) event.getEntity();
                PlayerManager damagerJoueur = main.getPlayerManager(damager.getUniqueId());
                PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
                if(damagerJoueur == target && joueur == author){
                    if(!damagedAuthor) {
                        damagedAuthor = true;
                        izanamiGoalAuthorList.get(izanamiGoalAuthorList.indexOf(IzanamiGoalAuthor.DAMAGEPLAYER)).setDone(true);
                        moveTargetCamps();
                    }
                } else if(damagerJoueur == author && joueur == target){
                    if(damagedTarget < 15){
                        damagedTarget++;
                        if(damagedTarget == 15){
                            izanamiGoalAuthorList.get(izanamiGoalAuthorList.indexOf(IzanamiGoalAuthor.DAMAGETARGET)).setDone(true);
                            moveTargetCamps();
                        }
                    }
                }
            }
        }
    }

    public String getAuthorValueGoal(IzanamiGoalAuthor izanamiGoalAuthor){
        switch (izanamiGoalAuthor){
            case DAMAGETARGET:
                return damagedTarget + "/15";
            case PROXIMITYTARGET:
                return new FormatTime(timeCloseToTarget) + "";
            case DAMAGEPLAYER:
                return damagedAuthor ? "§aTerminé":"§cNon-terminé";
            case PLACELAVA:
                return placeLaveOnTarget ? "§aTerminé":"§cNon-terminé";
            /*case GIVETARGET:
                return giveGappleToTarget ? "§aTerminé":"§cNon-terminé";
            case GIVEJOUEUR:
                return giveGappleToAuthor ? "§aTerminé":"§cNon-terminé";*/
            default:
                return "";
        }
    }

    public String getTargetValueGoal(IzanamiGoalTarget izanamiGoalTarget){
        switch (izanamiGoalTarget){
            case EATGOLDENAPPLE:
                return eatGappleTarget + "/5";
            case WALKONEKILOMETER:
                return finishWalk ? "§aTerminé": walkTarget + "";
            case KILLPLAYER:
                return targetKill ? "§aTerminé":"§cNon-terminé";
            default:
                return "";
        }
    }

    @EventHandler
    public void onKillPlayer(PlayerKillEvent event){
        if(event.getKiller() != null){
            Player killer = event.getKiller();
            PlayerManager joueur = main.getPlayerManager(killer.getUniqueId());
            if(joueur == target && !targetKill){
                targetKill = true;
                izanamiGoalTargetList.get(izanamiGoalTargetList.indexOf(IzanamiGoalTarget.KILLPLAYER)).setDone(true);
                moveTargetCamps();
            }
        }
    }

    public int getTimeCloseToTarget() {
        return timeCloseToTarget;
    }

    public void setTimeCloseToTarget(int timeCloseToTarget) {
        this.timeCloseToTarget = timeCloseToTarget;
    }

    public List<IzanamiGoalAuthor> getIzanamiGoalAuthorList() {
        return izanamiGoalAuthorList;
    }

    public List<IzanamiGoalTarget> getIzanamiGoalTargetList() {
        return izanamiGoalTargetList;
    }

    public PlayerManager getAuthor() {
        return author;
    }

    public PlayerManager getTarget() {
        return target;
    }

    public void setWalkTarget(double walkTarget) {
        this.walkTarget = walkTarget;
    }

    public double getWalkTarget() {
        return walkTarget;
    }

    public void setFinishWalk(boolean finishWalk) {
        this.finishWalk = finishWalk;
    }

    public boolean isFinishWalk() {
        return finishWalk;
    }
}
