package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Sakura;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Tsunade;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KatsuyuItem extends QuickItem {

    private static final int DISTANCE = 30;
    private NarutoV2Manager narutoV2Manager;

    public KatsuyuItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§bKastsuyu");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player playerClick = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());
            if (joueur.hasRole()) {
                if (joueur.getRole() instanceof Tsunade || joueur.getRole() instanceof Sakura) {
                    if (!narutoV2Manager.isInSamehada(playerClick.getUniqueId())) {
                        if (joueur.getRoleCooldownKatsuyu() == 0) {
                            if(playerClick.hasPotionEffect(PotionEffectType.REGENERATION)){
                                playerClick.removePotionEffect(PotionEffectType.REGENERATION);
                            }
                        	playerClick.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 60, 2, false, false));
                            for (Entity entity : playerClick.getNearbyEntities(DISTANCE, DISTANCE, DISTANCE)) {
                                if (entity instanceof Player) {
                                    Player nearPlayer = (Player) entity;
                                    if (nearPlayer.getGameMode() != GameMode.SPECTATOR) {
                                        if(nearPlayer.hasPotionEffect(PotionEffectType.REGENERATION)){
                                            nearPlayer.removePotionEffect(PotionEffectType.REGENERATION);
                                        }
                                        nearPlayer.addPotionEffect(
                                                new PotionEffect(PotionEffectType.REGENERATION, 20 * 60, 2, false, false));
                                    }
                                }
                            }

                            playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                            joueur.setRoleCooldownKatsuyu(20 * 60);
                            if(joueur.getRole() instanceof NarutoV2Role){
                                NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                                narutoRole.usePower(joueur);
                                narutoRole.usePowerSpecific(joueur, super.getName());
                            }
                            joueur.sendTimer(playerClick, joueur.getRoleCooldownKatsuyu(), playerClick.getItemInHand());
                        } else {
                            playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownKatsuyu()));
                        }
                    } else {
                        playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                }else{
                    playerClick.sendMessage(Messages.not("Tsunade ou Sakura"));
                }
            }else{
                playerClick.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            }
        });
    }
}
