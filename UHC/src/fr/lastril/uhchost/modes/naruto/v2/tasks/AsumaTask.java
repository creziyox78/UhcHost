package fr.lastril.uhchost.modes.naruto.v2.tasks;


import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Asuma;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Choji;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Ino;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Shikamaru;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AsumaTask extends BukkitRunnable {

    private final UhcHost main;
    private final Asuma asuma;
    private NarutoV2Manager narutoV2Manager;

    public AsumaTask(UhcHost main, Asuma asuma) {
        this.main = main;
        this.asuma = asuma;
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
        narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
    }

    @Override
    public void run() {
        if (asuma.getPlayer() != null) {
            Player player = asuma.getPlayer();

            if (asuma.getInoPoints() >= Asuma.FIND_POINTS) {
                //A TROUVE Ino
                asuma.setInoPoints(-1);

                String playerName = narutoV2Manager.getPlayerManagersWithRole(Ino.class).stream().findFirst().get().getPlayerName();

                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous avez terminé l'une de vos progression, vous connaissez donc l'indentité de " + playerName + " qui est l'un des rôles suivants : Ino, Chôji ou Shikamaru !");

                return;
            } else if (asuma.getInoPoints() != -1) {
                asuma.setInoPoints(asuma.getInoPoints() + getPointsWithRole(player, Ino.class));
            }

            if (asuma.getShikamaruPoints() >= Asuma.FIND_POINTS) {
                //A TROUVE Shikamaru
                asuma.setShikamaruPoints(-1);

                String playerName = narutoV2Manager.getPlayerManagersWithRole(Shikamaru.class).stream().findFirst().get().getPlayerName();

                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous avez terminé l'une de vos progression, vous connaissez donc l'indentité de " + playerName + " qui est l'un des rôles suivants : Ino, Chôji ou Shikamaru !");

                return;
            } else if (asuma.getShikamaruPoints() != -1) {
                asuma.setShikamaruPoints(asuma.getShikamaruPoints() + getPointsWithRole(player, Shikamaru.class));
            }

            if (asuma.getChojiPoints() >= Asuma.FIND_POINTS) {
                //A TROUVE Ino
                asuma.setChojiPoints(-1);

                String playerName = narutoV2Manager.getPlayerManagersWithRole(Choji.class).stream().findFirst().get().getPlayerName();

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

        for (PlayerManager joueur : narutoV2Manager.getPlayerManagersWithRole(role)) {
            if (joueur.getPlayer() != null) playerRole = joueur.getPlayer();
        }

        if (playerRole == null) return 0;

        if(playerRole.getLocation().getWorld() != asuma.getLocation().getWorld()){
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
