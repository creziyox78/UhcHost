package fr.lastril.uhchost.modes.naruto.v2.items.biju;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.FormatTime;
import fr.atlantis.api.utils.Title;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.biju.bijus.SaikenBiju;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class SaikenItem extends QuickItem {

    private int timer = 5 * 60;

    public SaikenItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§5Saiken");
        super.setLore("",
                "§eDémon à queue",
                "",
                "§7Permet à son utilisateur d'avoir les effets§9 Speed 1",
                "§7ainsi que§cForce I§7 pendant 5 minutes. A chaque coup, l'utilisateur",
                "§7possède 15% de chance d'exploser et d'aveugler le PlayerManager frappé.");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (PlayerManager.getRoleCooldownBiju() <= 0) {
                        if (main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(SaikenBiju.class) == PlayerManager
                                || (main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(SaikenBiju.class) == null
                                && !main.getNarutoV2Manager().getBijuManager().isAlreadyHote(PlayerManager))) {
                            main.getNarutoV2Manager().getBijuManager().getHotesBiju().put(SaikenBiju.class, PlayerManager);
                            main.getNarutoV2Manager().getBijuManager().getSaikenHote().add(PlayerManager);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5 * 60, 0, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 5 * 60, 0, false, false));
                            PlayerManager.setRoleCooldownBiju(15 * 60);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ActionBar.sendMessage(player, "§ePouvoir de §5Saiken : " + new FormatTime(timer));
                                    if (timer <= 0) {
                                        timer = 5 * 60;
                                        main.getNarutoV2Manager().getBijuManager().getSaikenHote().remove(PlayerManager);
                                        PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownJubi(), SaikenItem.super.toItemStack());
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
