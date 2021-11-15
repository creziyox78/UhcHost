package fr.lastril.uhchost.modes.naruto.v2.tasks;

import fr.atlantis.api.utils.FormatTime;
import fr.atlantis.api.utils.IdentityChanger;
import fr.atlantis.api.utils.Title;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.ZetsuBlanc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MetamorphoseTask extends BukkitRunnable {

    private final UhcHost main;
    private final ZetsuBlanc zetsu;
    private int timer = ZetsuBlanc.getMetamorphoseTime();


    public MetamorphoseTask(UhcHost main, ZetsuBlanc zetsu) {
        this.main = main;
        this.zetsu = zetsu;
    }

    @Override
    public void run() {
        Player zetsuPlayer = Bukkit.getPlayer(zetsu.getPlayerId());
        if (timer == 0) {
            if (zetsuPlayer != null) {
                IdentityChanger.changePlayerName(zetsuPlayer, zetsu.getOriginalName());
                IdentityChanger.changeSkin(zetsuPlayer, zetsu.getOriginalSkin(), false);
                zetsu.setCopiedName(null);
                zetsu.setCopiedSkin(null);
                zetsuPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous avez perdu votre métamorphose !");
            }
            cancel();
        }
        if (zetsuPlayer != null) {
            ActionBar.sendMessage(zetsuPlayer, "§7Métamorphose : " + new FormatTime(this.timer));
        }
        timer--;
    }
}
