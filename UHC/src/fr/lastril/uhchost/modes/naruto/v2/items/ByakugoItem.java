package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Tsunade;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ByakugoItem extends QuickItem {

    private NarutoV2Manager narutoV2Manager;

    public ByakugoItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§bByakugô");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player playerClick = onClick.getPlayer();
            PlayerManager joueur = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
            if (joueur.getRole() instanceof Tsunade) {
                Tsunade tsunade = (Tsunade) joueur.getRole();
                if (!narutoV2Manager.isInSamehada(playerClick.getUniqueId())) {
                    if (joueur.getRoleCooldownByakugo() == 0) {
                        if(tsunade.isUseByakugo()){
                            playerClick.sendMessage(Messages.error("Vous utilisez déjà votre Byakugo, attendez que son effet se dissipe."));
                            return;
                        }
                        if(playerClick.hasPotionEffect(PotionEffectType.REGENERATION)){
                            playerClick.removePotionEffect(PotionEffectType.REGENERATION);
                        }
                        playerClick.addPotionEffect(
                                new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 4, false, false));
                        playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                        tsunade.setUseByakugo(true);
                        tsunade.usePower(joueur);
                        tsunade.usePowerSpecific(joueur, super.getName());
                    } else {
                        playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownByakugo()));
                    }
                } else {
                    playerClick
                            .sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                }
            }
        });
    }
}
