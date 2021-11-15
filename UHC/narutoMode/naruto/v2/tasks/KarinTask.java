package fr.lastril.uhchost.modes.naruto.v2.tasks;

;
import fr.maygo.uhc.modes.naruto.v2.roles.taka.Jugo;
import fr.maygo.uhc.modes.naruto.v2.roles.taka.Karin;
import fr.maygo.uhc.modes.naruto.v2.roles.taka.Sasuke;
import fr.maygo.uhc.modes.naruto.v2.roles.taka.Suigetsu;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.scheduler.BukkitRunnable;

public class KarinTask extends BukkitRunnable {

    private final UhcHost main;
    private final Karin karin;

    public KarinTask(UhcHost main, Karin karin) {
        this.main = main;
        this.karin = karin;
    }

    @Override
    public void run() {
        for (PlayerManager PlayerManager : main.getPlayerManagersAlives()) {
            if (PlayerManager.hasRole() && PlayerManager.isAlive()) {
                if (PlayerManager.getPlayer() != null) {
                    if (karin.getPlayer() != null) {
                        if (PlayerManager.getPlayer() != karin.getPlayer()) {
                            if (karin.getPlayer().getWorld() == PlayerManager.getPlayer().getWorld()) {
                                if (PlayerManager.getPlayer().getLocation().distance(karin.getPlayer().getLocation()) <= 20) {

                                    if (PlayerManager.getRole() instanceof Sasuke || PlayerManager.getRole() instanceof Jugo || PlayerManager.getRole() instanceof Suigetsu) {
                                        karin.getPlayerManagerUnknow().putIfAbsent(PlayerManager, 2 * 60);
                                    } else {
                                        karin.getPlayerManagerUnknow().putIfAbsent(PlayerManager, karin.getTimer());
                                    }
                                    karin.getPlayerManagerUnknow().put(PlayerManager, karin.getPlayerManagerUnknow().get(PlayerManager) - 1);
                                    if (karin.getPlayerManagerUnknow().get(PlayerManager) != null) {
                                        if (karin.getPlayerManagerUnknow().get(PlayerManager) == 0) {
                                            UhcHost.debug("Karin player (" + karin.getPlayer().getName() + ") know: " + PlayerManager.getPlayerName());
                                            karin.getPlayer().sendMessage("§6" + PlayerManager.getPlayerName() + " est " + PlayerManager.getRoleName() + ".");
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

}
