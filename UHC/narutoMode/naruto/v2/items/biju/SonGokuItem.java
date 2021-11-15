package fr.lastril.uhchost.modes.naruto.v2.items.biju;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.FormatTime;
import fr.atlantis.api.utils.Title;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.biju.bijus.SonGokuBiju;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class SonGokuItem extends QuickItem {

    private int timer = 5 * 60;

    public SonGokuItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§cSon Gokû");
        super.setLore("",
                "§eDémon à queue",
                "",
                "§7Permet à son utilisateur d'avoir les effets§aJump Boost 4,§9Speed I",
                "§7ainsi que§6 Résistance au Feu§7 pendant 5 minutes. A chaque coup, l'utilisateur",
                "§7enflamme le PlayerManager frappé et les PlayerManagers autour de lui ne peuvent pas posé",
                "§7de§6 seau de lave§7 dans un rayon de§f 15 blocs§7.");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (PlayerManager.getRoleCooldownBiju() <= 0) {
                        if (main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(SonGokuBiju.class) == PlayerManager
                                || (main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(SonGokuBiju.class) == null
                                && !main.getNarutoV2Manager().getBijuManager().isAlreadyHote(PlayerManager))) {
                            main.getNarutoV2Manager().getBijuManager().getHotesBiju().put(SonGokuBiju.class, PlayerManager);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 60 * 5, 0, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 5, 0, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 60 * 5, 3, false, false));
                            main.getNarutoV2Manager().getBijuManager().getSanGokuNoLava().add(PlayerManager);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ActionBar.sendMessage(player, "§ePouvoir de §cSon Gokû : " + new FormatTime(timer));
                                    if (timer <= 0) {
                                        timer = 5 * 60;
                                        main.getNarutoV2Manager().getBijuManager().getSanGokuNoLava().remove(PlayerManager);
                                        PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownJubi(), SonGokuItem.super.toItemStack());
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
