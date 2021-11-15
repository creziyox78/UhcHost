package fr.lastril.uhchost.modes.naruto.v2.items.biju;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.FormatTime;
import fr.atlantis.api.utils.Title;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.When;
import fr.maygo.uhc.modes.naruto.v2.biju.bijus.ChomeiBiju;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ChomeiItem extends QuickItem {

    private int timer = 10 * 60;

    public ChomeiItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§aChômei");
        super.setLore("",
                "§eDémon à queue",
                "",
                "§7Permet à son utilisateur de s'envoler pendant 10 secondes",
                "§7ainsi que§f Vitesse 2§7 pendant 3 minutes.");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (PlayerManager.getRoleCooldownBiju() <= 0) {
                        if (main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(ChomeiBiju.class) == PlayerManager
                                || (main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(ChomeiBiju.class) == null
                                && !main.getNarutoV2Manager().getBijuManager().isAlreadyHote(PlayerManager))) {
                            main.getNarutoV2Manager().getBijuManager().getHotesBiju().put(ChomeiBiju.class, PlayerManager);
                            main.getNarutoV2Manager().getBijuManager().getNoFall().add(PlayerManager);

                            player.setAllowFlight(true);
                            player.setFlying(true);
                            player.setFlySpeed((float) 0.1);
                            PlayerManager.setRoleCooldownBiju(10 * 60);
                            if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                                player.removePotionEffect(PotionEffectType.SPEED);
                            }
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 3, 1, false, false));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.setAllowFlight(false);
                                    player.setFlying(false);
                                }
                            }.runTaskLater(main, 20 * 10);

                            Bukkit.getScheduler().runTaskLater(main, () -> PlayerManager.getRole().getEffects().entrySet().stream().filter(e -> e.getValue() == When.START).forEach(e -> {
                                if (PlayerManager.getPlayer() != null) {
                                    if (PlayerManager.getPlayer().hasPotionEffect(e.getKey().getType())) {
                                        PlayerManager.getPlayer().removePotionEffect(e.getKey().getType());
                                    }
                                    PlayerManager.getPlayer().addPotionEffect(e.getKey());
                                }
                            }), 20 * 60 * 3 + 3);

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ActionBar.sendMessage(player, "§ePouvoir de §aChômei : " + new FormatTime(timer));
                                    if (timer <= 0) {
                                        timer = 10 * 60;
                                        main.getNarutoV2Manager().getBijuManager().getNoFall().remove(PlayerManager);
                                        PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownBiju(), ChomeiItem.super.toItemStack());
                                        cancel();
                                    }
                                    timer--;
                                }
                            }.runTaskTimer(main, 0, 20);


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
