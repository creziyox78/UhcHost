package fr.lastril.uhchost.modes.naruto.v2.items.biju;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.FormatTime;
import fr.atlantis.api.utils.Title;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.biju.bijus.IsobuBiju;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class IsobuItem extends QuickItem {

    private int timer = 5 * 60;

    public IsobuItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§eIsobu");
        super.setLore("",
                "§eDémon à queue",
                "",
                "§7Permet à son utilisateur d'avoir§5 coeurs§7 en plus",
                "§7d'avoir 10% de chance qu'un coup n'inflige pas de dégâts",
                "§7ainsi que§9 Résistance I§7 pendant 5 minutes.");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (PlayerManager.getRoleCooldownBiju() <= 0) {
                        if (main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(IsobuBiju.class) == PlayerManager
                                || (main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(IsobuBiju.class) == null
                                && !main.getNarutoV2Manager().getBijuManager().isAlreadyHote(PlayerManager))) {
                            main.getNarutoV2Manager().getBijuManager().getHotesBiju().put(IsobuBiju.class, PlayerManager);
                            main.getNarutoV2Manager().getBijuManager().getIsobuResistance().add(PlayerManager);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous venez d'utiliser les pouvoirs d'Isobu.");
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 60 * 5, 0, false, false));
                            player.setMaxHealth(player.getMaxHealth() + (2D * 5D));
                            player.setHealth(player.getHealth() + (2D * 5D));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ActionBar.sendMessage(player, "§ePouvoir de Isobu : " + new FormatTime(timer));
                                    if (timer <= 0) {
                                        timer = 5 * 60;
                                        player.setMaxHealth(player.getMaxHealth() - (5D * 2D));
                                        main.getNarutoV2Manager().getBijuManager().getIsobuResistance().remove(PlayerManager);
                                        PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownBiju(), IsobuItem.super.toItemStack());
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
