package fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.defense;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.SandShape;
import fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.ShapeType;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Gaara;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.world.tasks.ChunkLoader;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SuspensionDefense extends SandShape {

    public SuspensionDefense() {
        super("Suspension du Désert", ShapeType.DEFENSE);
    }

    @Override
    public boolean useCapacity(Player player, Gaara gaara) {

        player.setAllowFlight(true);
        player.setFlying(true);
        player.setFlySpeed((float) 0.1);
        player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous pouvez désormais voler !");

        //new TapisSableEffect(Gaara.getFlyTime()*20, EnumParticle.REDSTONE, 255, 183, 0).start(player);

        new BukkitRunnable() {

            int timer = Gaara.getFlyTime()*20;

            @Override
            public void run() {
                if(gaara.isDamaged()){
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous avez reçus/infligés des dégâts, votre pouvoir de vol s'estompe.");
                    gaara.setDamaged(false);
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    cancel();
                }
                if(player.getGameMode() != GameMode.SPECTATOR){
                    ActionBar.sendMessage(player, "§7Supension du Désert : [" + ChunkLoader.getProgressBar(timer, Gaara.getFlyTime()*20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                    if(timer == 0){
                        player.setFlying(false);
                        player.setAllowFlight(false);
                        cancel();
                    }
                    timer--;
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(main, 0, 1);


        return true;
    }

    @Override
    public int getSandPrice() {
        return 96;
    }
}
