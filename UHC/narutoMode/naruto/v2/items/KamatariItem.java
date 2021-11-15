package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Temari;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KamatariItem extends QuickItem {

    public KamatariItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§bKamatari");
        super.onClick(onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
            if (PlayerManager.getRole() instanceof Temari) {
                Temari temari = (Temari) PlayerManager.getRole();
                if (!UhcHost.getInstance().getNarutoManager().containsSamehada(playerClick.getUniqueId())) {
                    Wolf wolf = playerClick.getWorld().spawn(playerClick.getLocation(), Wolf.class);
                    wolf.setTamed(true);
                    wolf.setOwner(playerClick);
                    wolf.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1, false, false));
                    wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
                    wolf.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2, false, false));

                    temari.setWolf(wolf);
                    if (PlayerManager.getRole() instanceof NarutoV2Role) {
                        NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                        narutoRole.usePower(PlayerManager);
                    }

                    playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                    playerClick.setItemInHand(null);
                } else {
                    playerClick
                            .sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                }

            }
        });
    }

}
