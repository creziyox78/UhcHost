package fr.lastril.uhchost.modes.naruto.v2.items.biju;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.FormatTime;
import fr.atlantis.api.utils.Title;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.When;
import fr.maygo.uhc.modes.naruto.v2.biju.bijus.KokuoBiju;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class KokuoItem extends QuickItem {

    private int timer = 5 * 60;

    public KokuoItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§9Kokuô");
        super.setLore("",
                "§eDémon à queue",
                "",
                "§7Permet à son utilisateur de recevoir l'effet§f Vitesse 2,",
                "§725% de chance en tappant, que l'utilisateur reçoit§f Vitesse 3§7",
                "§7pendant 5 secondes et §8aveugle le PlayerManager ciblé§7.");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (PlayerManager.getRoleCooldownBiju() <= 0) {
                        if (main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(KokuoBiju.class) == PlayerManager
                                || (main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(KokuoBiju.class) == null
                                && !main.getNarutoV2Manager().getBijuManager().isAlreadyHote(PlayerManager))) {
                            main.getNarutoV2Manager().getBijuManager().getHotesBiju().put(KokuoBiju.class, PlayerManager);
                            main.getNarutoV2Manager().getBijuManager().getKokuoHote().add(PlayerManager);
                            if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                                player.removePotionEffect(PotionEffectType.SPEED);
                            }
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 5, 1, false, false));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ActionBar.sendMessage(player, "§ePouvoirs de §9Kokuô : " + new FormatTime(timer));
                                    if (timer <= 0) {
                                        timer = 5 * 60;
                                        main.getNarutoV2Manager().getBijuManager().getKokuoHote().remove(PlayerManager);
                                        PlayerManager.getRole().getEffects().entrySet().stream().filter(e -> e.getValue() == When.START).forEach(e -> {
                                            if (PlayerManager.getPlayer() != null) {
                                                if (PlayerManager.getPlayer().hasPotionEffect(e.getKey().getType())) {
                                                    PlayerManager.getPlayer().removePotionEffect(e.getKey().getType());
                                                }
                                                PlayerManager.getPlayer().addPotionEffect(e.getKey());
                                            }
                                        });
                                        PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownBiju(), KokuoItem.super.toItemStack());
                                        cancel();
                                    }
                                    timer--;
                                }
                            }.runTaskTimer(main, 0, 20);
                            PlayerManager.setRoleCooldownBiju(20 * 60);
                        } else {
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§6Vous ne pouvez pas utiliser ce Bijû.");
                        }
                    } else {
                        player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownBiju()));
                    }
                } else {
                    player.sendMessage(
                            Messages.NARUTO_PREFIX.getMessage() + "§eVous êtes sous l'effet de §cSamehada§e.");
                }
            }
        });
    }
}
