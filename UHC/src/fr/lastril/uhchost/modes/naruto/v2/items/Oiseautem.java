package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Nagato;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.tasks.ChunkLoader;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Oiseautem extends QuickItem {

    private NarutoV2Manager narutoV2Manager;

    public Oiseautem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§cOiseau");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
                if (joueur.getRole() instanceof Nagato) {
                    Nagato nagato = (Nagato) joueur.getRole();
                    if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                        if (joueur.getRoleCooldownOiseau() <= 0) {
                            player.setAllowFlight(true);
                            player.setVelocity(player.getVelocity().add(new Vector(0, 2, 0)));
                            player.setFlying(true);
                            player.setFlySpeed((float) 0.1);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous pouvez désormais voler !");
                            joueur.setRoleCooldownOiseau(20*60);
                            joueur.sendTimer(player, joueur.getRoleCooldownOiseau(), player.getItemInHand());
                            if(joueur.getRole() instanceof NarutoV2Role){
                                NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                                narutoRole.usePower(joueur);
                                narutoRole.usePowerSpecific(joueur, super.getName());
                            }

                            new BukkitRunnable() {

                                int timer = Nagato.getFlyTime()*20;

                                @Override
                                public void run() {
                                    ActionBar.sendMessage(player, "§7Oiseau : [" + ChunkLoader.getProgressBar(timer, Nagato.getFlyTime()*20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                                    if(timer == 0){
                                        player.setFlying(false);
                                        player.setAllowFlight(false);
                                        narutoV2Manager.getNofallJoueur().add(joueur);
                                        cancel();
                                    }
                                    timer--;
                                }
                            }.runTaskTimer(main, 0, 1);

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    narutoV2Manager.getNofallJoueur().remove(joueur);
                                }
                            }.runTaskLater(main, 20*20);

                        }else{
                            player.sendMessage(Messages.cooldown(joueur.getRoleCooldownOiseau()));
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                } else {
                    player.sendMessage(Messages.not("Nagato"));
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            }
        });
    }
}
