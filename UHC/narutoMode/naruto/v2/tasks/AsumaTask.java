package fr.lastril.uhchost.modes.naruto.v2.tasks;

;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Asuma;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Choji;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Ino;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Shikamaru;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AsumaTask extends BukkitRunnable {

    private final UhcHost main;
    private final Asuma asuma;

    public AsumaTask(UhcHost main, Asuma asuma) {
        this.main = main;
        this.asuma = asuma;
    }

    @Override
    public void run() {
        if (asuma.getPlayer() != null) {
            Player player = asuma.getPlayer();

            if (asuma.getInoPoints() >= Asuma.FIND_POINTS) {
                //A TROUVE Ino
                asuma.setInoPoints(-1);

                String playerName = main.getNarutoV2Manager().getPlayerManagersWithRole(Ino.class).stream().findFirst().get().getPlayerName();

                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous avez terminé l'une de vos progression, vous connaissez donc l'indentité de " + playerName + " qui est l'un des rôles suivants : Ino, Chôji ou Shikamaru !");

                return;
            } else if (asuma.getInoPoints() != -1) {
                asuma.setInoPoints(asuma.getInoPoints() + getPointsWithRole(player, Ino.class));
            }

            if (asuma.getShikamaruPoints() >= Asuma.FIND_POINTS) {
                //A TROUVE Shikamaru
                asuma.setShikamaruPoints(-1);

                String playerName = main.getNarutoV2Manager().getPlayerManagersWithRole(Shikamaru.class).stream().findFirst().get().getPlayerName();

                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous avez terminé l'une de vos progression, vous connaissez donc l'indentité de " + playerName + " qui est l'un des rôles suivants : Ino, Chôji ou Shikamaru !");

                return;
            } else if (asuma.getShikamaruPoints() != -1) {
                asuma.setShikamaruPoints(asuma.getShikamaruPoints() + getPointsWithRole(player, Shikamaru.class));
            }

            if (asuma.getChojiPoints() >= Asuma.FIND_POINTS) {
                //A TROUVE Ino
                asuma.setChojiPoints(-1);

                String playerName = main.getNarutoV2Manager().getPlayerManagersWithRole(Choji.class).stream().findFirst().get().getPlayerName();

                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous avez terminé l'une de vos progression, vous connaissez donc l'indentité de " + playerName + " qui est l'un des rôles suivants : Ino, Chôji ou Shikamaru !");

                return;
            } else if (asuma.getChojiPoints() != -1) {
                asuma.setChojiPoints(asuma.getChojiPoints() + getPointsWithRole(player, Choji.class));
            }

        }
    }

    public int getPointsWithRole(Player asuma, Class<? extends Role> role) {
        int points = 0;

        Player playerRole = null;

        for (PlayerManager PlayerManager : main.getNarutoV2Manager().getPlayerManagersWithRole(role)) {
            if (PlayerManager.getPlayer() != null) playerRole = PlayerManager.getPlayer();
        }

        if (playerRole == null) return 0;

        if (playerRole.getLocation().getWorld() != asuma.getLocation().getWorld()) {
            return 0;
        }
        double distance = playerRole.getLocation().distance(asuma.getLocation());

        if (distance <= 20 && distance > 10) {
            points = 2;
        } else if (distance <= 10 && distance > 5) {
            points = 5;
        } else if (distance <= 5) {
            points = 10;
        }

        return points;
    }
}
