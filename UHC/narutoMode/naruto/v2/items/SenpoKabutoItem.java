package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.orochimaru.Kabuto;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SenpoKabutoItem extends QuickItem {

    private final int distance = 15;

    public SenpoKabutoItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§dSenpô");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Kabuto) {
                    Kabuto kabuto = (Kabuto) PlayerManager.getRole();
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownSenpo() <= 0) {
                            for (Entity entity : player.getNearbyEntities(distance, distance, distance)) {
                                if (entity instanceof Player) {
                                    Player target = (Player) entity;
                                    PlayerManager targetPlayerManager = main.getPlayerManager(target.getUniqueId());
                                    if (targetPlayerManager.isAlive() && targetPlayerManager != PlayerManager) {
                                        target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cKabuto vient d'utiliser son senpô sur vous.");
                                        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 10, 0, false, false));
                                        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 10, 3, false, false));
                                    }

                                }
                            }
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                            kabuto.usePower(PlayerManager);
                            PlayerManager.setRoleCooldownSenpo(20 * 60);
                        } else {
                            player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownSenpo()));
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                } else {
                    player.sendMessage(Messages.not("Kabuto"));
                }
            }
        });
    }

}
