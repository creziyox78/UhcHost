package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.naruto.v2.roles.orochimaru.Sakon;
import fr.maygo.uhc.modes.naruto.v2.roles.orochimaru.Ukon;
import fr.maygo.uhc.modes.naruto.v2.sakonukon.SakonUkonManager;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class MarqueMauditeSakonUkonItem extends QuickItem {

    private final UhcHost main;

    public MarqueMauditeSakonUkonItem(UhcHost main) {
        super(Material.NETHER_STAR);
        this.main = main;
        super.setName("§3Marque Maudite");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (isRightRole(PlayerManager.getRole())) {
                    SakonUkonManager sakonUkonManager = main.getNarutoV2Manager().getSakonUkonManager();
                    if (PlayerManager.getRoleCooldownMarqueMaudite() <= 0) {
                        if (sakonUkonManager.getPowersWaits().containsKey("MARQUE_MAUDITE")) {
                            UUID clicker = sakonUkonManager.getPowersWaits().get("MARQUE_MAUDITE");
                            if (!clicker.equals(player.getUniqueId())) {
                                Player first = Bukkit.getPlayer(clicker);
                                this.sendEffectsToPlayer(player);
                                this.sendEffectsToPlayer(first);
                                sakonUkonManager.getPowersWaits().remove("MARQUE_MAUDITE");
                            }
                        } else {
                            sakonUkonManager.addPowerWait("MARQUE_MAUDITE", player, PlayerManager.getRole() instanceof Sakon ? "Ukon" : "Sakon");
                        }
                    } else {
                        player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownMarqueMaudite()));
                    }
                } else {
                    player.sendMessage(Messages.not("Sakon ou Ukon"));
                    return;
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }

    private void sendEffectsToPlayer(Player player) {
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());

        if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60 * 5, 0, false, false));

        if (player.hasPotionEffect(PotionEffectType.SPEED)) player.removePotionEffect(PotionEffectType.SPEED);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 5, 0, false, false));

        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());

        new BukkitRunnable() {

            @Override
            public void run() {
                Player players = Bukkit.getPlayer(player.getUniqueId());
                if (players != null) {
                    players.setMaxHealth(players.getMaxHealth() - 2D);
                }
            }
        }.runTaskLater(main, 20 * 60 * 5);

        PlayerManager.setRoleCooldownMarqueMaudite(10 * 60);
        PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownMarqueMaudite(), player.getItemInHand());
    }


    private boolean isRightRole(Role role) {
        return role instanceof Sakon || role instanceof Ukon;
    }

}
