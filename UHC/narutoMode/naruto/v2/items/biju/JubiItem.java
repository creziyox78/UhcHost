package fr.lastril.uhchost.modes.naruto.v2.items.biju;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.jubi.Obito;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JubiItem extends QuickItem {

    public JubiItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§dJûbi");
        super.setLore("",
                "§c§lLe plus puissant démon à queue",
                "",
                "§c§lVous en perdrez votre raison...");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof JubiUser) {
                    if (main.getNarutoV2Manager().isCanUseJubi()) {
                        main.getNarutoV2Manager().setJubiUsed(true);
                        main.getNarutoV2Manager().setCanUseJubi(false);
                        main.getNarutoV2Manager().setJubi(PlayerManager);
                        if (player.hasPotionEffect(PotionEffectType.SPEED))
                            player.removePotionEffect(PotionEffectType.SPEED);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 5 * 60, 0, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5 * 60, 1, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5 * 60, 0, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 5 * 60, 0, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 5 * 60, 3, false, false));
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§dVous possédez désormais Jubi en vous et obtenez sa pleine puissance.");
                        Bukkit.broadcastMessage(" ");
                        Bukkit.broadcastMessage("§c§lLe receptacle de Jubi obtient une puissance phénoménale !");
                        Bukkit.broadcastMessage(" ");
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            players.playSound(players.getLocation(), "atlantis.jubicri", Integer.MAX_VALUE, 1);
                        }
                        main.getNarutoV2Manager().getNofallPlayerManager().add(PlayerManager);
                        Bukkit.getScheduler().runTaskLater(main, () -> {
                            if (PlayerManager.getRole() instanceof Obito) {
                                if (player.hasPotionEffect(PotionEffectType.SPEED))
                                    player.removePotionEffect(PotionEffectType.SPEED);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
                            }
                            main.getNarutoV2Manager().setJubiUsed(false);
                        }, 20 * 5 * 60 + 5);
                        Bukkit.getScheduler().runTaskLater(main, () -> {
                            main.getNarutoV2Manager().setCanUseJubi(true);
                            if (PlayerManager.getPlayer() != null) {
                                PlayerManager.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aJûbi peut à nouveau être utiliser !");
                            }
                        }, 20 * 10 * 60);
                    } else {
                        player.sendMessage(Messages.error("Vous devez encore attendre avant de réutiliser Jûbi."));
                    }
                }
            }
        });
    }

    public interface JubiUser {

    }

}
