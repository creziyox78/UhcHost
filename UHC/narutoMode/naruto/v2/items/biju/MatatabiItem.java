package fr.lastril.uhchost.modes.naruto.v2.items.biju;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.FormatTime;
import fr.atlantis.api.utils.Title;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.biju.bijus.MatatabiBiju;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class MatatabiItem extends QuickItem {

    private int timer = 5 * 60;

    public MatatabiItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§6Matatabi");
        super.setLore("",
                "§eDémon à queue",
                "",
                "§7Permet à son utilisateur de recevoir les effet§cForce 1,",
                "§6Résistance au Feu§7, que ses coups §eenflamme le PlayerManager tappé§7,",
                "§7ainsi que§f NoFall§7 pendant 5 minutes.");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (PlayerManager.getRoleCooldownBiju() <= 0) {
                        if (main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(MatatabiBiju.class) == PlayerManager
                                || (main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(MatatabiBiju.class) == null
                                && !main.getNarutoV2Manager().getBijuManager().isAlreadyHote(PlayerManager))) {
                            main.getNarutoV2Manager().getBijuManager().getHotesBiju().put(MatatabiBiju.class, PlayerManager);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 5 * 60, 0, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 5 * 60, 0, false, false));
                            main.getNarutoV2Manager().getBijuManager().getNoFall().add(PlayerManager);
                            main.getNarutoV2Manager().getSusano().add(player.getUniqueId());
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§6Vous venez d'utiliser les pouvoirs de Matatabi.");
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    ActionBar.sendMessage(player, "§6Pouvoir de Matatabi : " + new FormatTime(timer));
                                    if (timer <= 0) {
                                        main.getNarutoV2Manager().getBijuManager().getNoFall().remove(PlayerManager);
                                        main.getNarutoV2Manager().getSusano().remove(player.getUniqueId());
                                        timer = 5 * 60;
                                        PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownJubi(), MatatabiItem.super.toItemStack());
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
