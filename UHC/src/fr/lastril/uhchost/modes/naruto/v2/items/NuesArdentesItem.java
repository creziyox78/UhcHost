package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Asuma;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.WorldUtils;
import fr.lastril.uhchost.world.tasks.ChunkLoader;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NuesArdentesItem extends QuickItem {

    private NarutoV2Manager narutoV2Manager;

    public NuesArdentesItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§6Nuées Ardentes");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
                if (joueur.getRole() instanceof Asuma) {
                    if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                        if (joueur.getRoleCooldownNueesArdentes() == 0) {
                            Location explosionLocation = player.getLocation().clone().add(0, 1, 0).add(player.getLocation().getDirection().clone().multiply(6));

                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                            joueur.setRoleCooldownNueesArdentes(5 * 60);
                            joueur.sendTimer(player, joueur.getRoleCooldownNueesArdentes(), player.getItemInHand());
                            if (joueur.getRole() instanceof NarutoV2Role) {
                                NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                                narutoRole.usePower(joueur);
                                narutoRole.usePowerSpecific(joueur, super.getName());
                            }

                            new BukkitRunnable() {

                                int timer = 1*20;

                                @Override
                                public void run() {
                                    WorldUtils.spawnParticle(explosionLocation, EnumParticle.CLOUD, 0.5f, 1f, 0.5f, 0.1f, 100);
                                    ActionBar.sendMessage(player, "§7Explosion : [" + ChunkLoader.getProgressBar(timer, 5*20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                                    if(timer == 0){
                                        explosionLocation.getWorld().createExplosion(explosionLocation, 3.0f);

                                        cancel();
                                    }
                                    timer--;
                                }
                            }.runTaskTimer(main, 0, 1);
                        } else {
                            player.sendMessage(Messages.cooldown(joueur.getRoleCooldownNueesArdentes()));
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                } else {
                    player.sendMessage(Messages.not("Asuma"));
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            }
        });
    }
}
