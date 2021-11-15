package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Kiba;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class AkamaruItem extends QuickItem {

    private Wolf wolf;

    public AkamaruItem() {
        super(Material.NETHER_STAR);
        super.setName("§bAkamaru");
        super.onClick(onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
            if (PlayerManager.getRole() instanceof Kiba) {
                if (!UhcHost.getInstance().getNarutoManager().containsSamehada(playerClick.getUniqueId())) {
                    if (PlayerManager.getRoleCooldownAkamaru() == 0) {
                        Kiba kiba = (Kiba) PlayerManager.getRole();
                        kiba.usePower(PlayerManager);

                        wolf = playerClick.getWorld().spawn(playerClick.getLocation(), Wolf.class);
                        wolf.setCollarColor(DyeColor.CYAN);
                        wolf.setTamed(true);
                        wolf.setOwner(playerClick);
                        wolf.addPotionEffect(
                                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 10 * 60, 0, false, false));
                        playerClick.addPotionEffect(
                                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 10 * 60, 0, false, false));

                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                wolf.remove();
                                playerClick.sendMessage(
                                        Messages.NARUTO_PREFIX.getMessage() + "Vous avez perdu votre pouvoir !");
                            }
                        }.runTaskLater(UhcHost.getInstance(), 20 * 60 * 10);

                        playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                        PlayerManager.setRoleCooldownAkamaru(20 * 60);
                        PlayerManager.sendTimer(playerClick, PlayerManager.getRoleCooldownAkamaru(), playerClick.getItemInHand());
                    } else {
                        playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownAkamaru()));
                    }
                } else {
                    playerClick
                            .sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                }

            }
        });
    }

}
