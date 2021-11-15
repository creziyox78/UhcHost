package fr.lastril.uhchost.modes.naruto.v2.tasks;

;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.Nagato;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NagatoCooldownsTask extends BukkitRunnable {

    private final UhcHost main;
    private final Nagato nagato;

    public NagatoCooldownsTask(UhcHost main, Nagato nagato) {
        this.main = main;
        this.nagato = nagato;
    }

    @Override
    public void run() {
        if (nagato.getPlayer() != null) {
            Player player = nagato.getPlayer();
            for (Entity nearbyEntity : player.getNearbyEntities(nagato.getCooldownDistance(), nagato.getCooldownDistance(), nagato.getCooldownDistance())) {
                if (nearbyEntity instanceof Player) {
                    Player target = (Player) nearbyEntity;
                    if (target.getGameMode() != GameMode.SPECTATOR) {
                        PlayerManager PlayerManager = main.getPlayerManager(target.getUniqueId());
                        if (PlayerManager.getCamps() != Camps.AKATSUKI) {
                            PlayerManager.addCooldowns();
                        }
                    }
                }
            }
        }
    }
}
