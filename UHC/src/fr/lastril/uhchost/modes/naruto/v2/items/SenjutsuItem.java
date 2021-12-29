package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Jiraya;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Naruto;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class SenjutsuItem extends QuickItem {

    private static final int TIME_EFFECTS = 5 * 60 * 20;
    private NarutoV2Manager narutoV2Manager;

    public SenjutsuItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§6Senjutsu");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
                if (joueur.getRole() instanceof SenjutsuUser) {
                    if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                        if (joueur.getRoleCooldownSenjutsu() == 0) {

                            if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                            }
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, TIME_EFFECTS, 1, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, TIME_EFFECTS, 0, false, false));
                            if(joueur.getRole() instanceof Jiraya){
                                if (joueur.hasRole()) {
                                    if (joueur.getRole() instanceof Jiraya) {
                                        for (PlayerManager targetJoueur : narutoV2Manager.getPlayerManagersWithRole(Naruto.class)) {
                                            if (targetJoueur.getPlayer() != null) {
                                                targetJoueur.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + joueur.getRole().getRoleName()
                                                        + " vient d'utiliser son pouvoir, il se situe en x: "
                                                        + player.getLocation().getBlockX() + ", y: "
                                                        + player.getLocation().getBlockY() + ", z: "
                                                        + player.getLocation().getBlockZ());
                                            }
                                        }
                                    }
                                }
                            }
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                            joueur.setRoleCooldownSenjutsu(30 * 60);
                            joueur.sendTimer(player, joueur.getRoleCooldownSenjutsu(), player.getItemInHand());
                            if (joueur.getRole() instanceof NarutoV2Role) {
                                NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                                narutoRole.usePower(joueur);
                                narutoRole.usePowerSpecific(joueur, super.getName());
                            }

                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    joueur.getRole().getEffects().entrySet().stream().filter(e -> e.getValue() == When.START).forEach(e -> {
                                        if (joueur.getPlayer() != null) {
                                            joueur.getPlayer().addPotionEffect(e.getKey());
                                        }
                                    });
                                }

                            }.runTaskLater(main, TIME_EFFECTS + 40);
                        } else {
                            player.sendMessage(Messages.cooldown(joueur.getRoleCooldownSenjutsu()));
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                } else {
                    player.sendMessage(Messages.not("Naruto ou Jiraya"));
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            }
        });
    }

    public interface SenjutsuUser {
    }
}
