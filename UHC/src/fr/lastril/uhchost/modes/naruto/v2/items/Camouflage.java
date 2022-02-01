package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.zabuza.Zabuza;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Camouflage extends QuickItem {
    private NarutoV2Manager narutoV2Manager;

    public Camouflage(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§3Camouflage");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if(joueur.hasRole()) {
                if(joueur.getRole() instanceof Zabuza){
                    Zabuza zabuza = (Zabuza) joueur.getRole();
                    if(!narutoV2Manager.isInSamehada(player.getUniqueId())){
                        if(!zabuza.isCamoufled()){
                            if(joueur.getRoleCooldownCamouflage() <= 0){
                                zabuza.setCamoufled(true);
                                zabuza.hidePlayer(player);
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous êtes§c invisible§e (même avec votre armure) aux yeux de tous maintenant.");
                                zabuza.usePower(joueur);
                                zabuza.usePowerSpecific(joueur, super.getName());
                            } else {
                                player.sendMessage(Messages.cooldown(joueur.getRoleCooldownCamouflage()));
                            }
                        } else {
                            joueur.setRoleCooldownCamouflage(5*60);
                            zabuza.setCamoufled(false);
                            zabuza.showPlayer(player);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous êtes§a visible§e aux yeux de tous maintenant.");
                            zabuza.usePower(joueur);
                            zabuza.usePowerSpecific(joueur, super.getName());
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                }
            }
        });
    }
}
