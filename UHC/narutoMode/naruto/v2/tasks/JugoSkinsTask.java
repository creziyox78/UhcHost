package fr.lastril.uhchost.modes.naruto.v2.tasks;

;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.taka.Jugo;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class JugoSkinsTask extends BukkitRunnable {

    private final UhcHost main;
    private final Jugo jugo;

    public JugoSkinsTask(UhcHost main, Jugo jugo) {
        this.main = main;
        this.jugo = jugo;
    }

    @Override
    public void run() {
        Player jugoPlayer = jugo.getPlayer();
        if (jugoPlayer != null) {
            jugoPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "L'indentité des PlayerManagers vous est brouillé.");
        }
        jugo.setSeeSkins(false);
        new BukkitRunnable() {

            @Override
            public void run() {
                Player jugoPlayer = jugo.getPlayer();
                if (jugoPlayer != null) {
                    jugoPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "L'indentité des PlayerManagers ne vous est plus brouillé.");
                }
                jugo.setSeeSkins(true);
            }
        }.runTaskLater(main, 20 * 3 * 60);
    }
}