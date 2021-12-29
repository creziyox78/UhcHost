package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Temari;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KamatariItem extends QuickItem {

    private NarutoV2Manager narutoV2Manager;

    public KamatariItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§bKamatari");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player playerClick = onClick.getPlayer();
            PlayerManager joueur = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
            if (joueur.getRole() instanceof Temari) {
                Temari temari = (Temari) joueur.getRole();
                if (!narutoV2Manager.isInSamehada(playerClick.getUniqueId())) {
                    Wolf wolf = playerClick.getWorld().spawn(playerClick.getLocation(), Wolf.class);
                    wolf.setTamed(true);
                    wolf.setOwner(playerClick);
                    wolf.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1, false, false));
                    wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
                    wolf.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2, false, false));

                    temari.setWolf(wolf);
                    if (joueur.getRole() instanceof NarutoV2Role) {
                        NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                        narutoRole.usePower(joueur);
                        narutoRole.usePowerSpecific(joueur, super.getName());
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
