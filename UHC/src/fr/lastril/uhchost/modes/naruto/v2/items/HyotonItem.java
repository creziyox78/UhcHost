package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.zabuza.Haku;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class HyotonItem extends QuickItem {

    private NarutoV2Manager narutoV2Manager;

    public HyotonItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§bHyôton");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if(joueur.hasRole()){
                if(joueur.getRole() instanceof Haku){
                    if(!narutoV2Manager.isInSamehada(player.getUniqueId())){
                        if(joueur.getRoleCooldownHyoton() <= 0){
                            Haku haku = (Haku) joueur.getRole();
                            if(haku.getPhase() == 1){
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§bVous venez de créer une sphère, vous pouvez maintenant vous téléporter (avec le même item) sur le bloque que vous regarder.");
                                haku.setLoc(player.getLocation());
                                haku.addPhase();
                                for(Location location : haku.sphere(haku.getLoc(), 18, true)){
                                    if(location.getBlock().getType() == Material.AIR){
                                        location.getBlock().setType(Material.PACKED_ICE);
                                        haku.getIceUnbreakabke().add(location);
                                    }
                                }
                                for(Location location : haku.sphere(haku.getLoc(), 17, true)){
                                    if(location.getBlock().getType() == Material.AIR){
                                        location.getBlock().setType(Material.PACKED_ICE);
                                        haku.getIceUnbreakabke().add(location);
                                    }
                                }
                                for(Location location : haku.sphere(haku.getLoc(), 16, true)){
                                    if(location.getBlock().getType() == Material.AIR){
                                        location.getBlock().setType(Material.PACKED_ICE);
                                        haku.getIceUnbreakabke().add(location);
                                    }
                                }
                                Bukkit.getScheduler().runTaskLater(main, () -> {
                                    for(Location location : haku.sphere(haku.getLoc(), 18, true)){
                                        if(location.getBlock().getType() == Material.PACKED_ICE){
                                            location.getBlock().setType(Material.AIR);
                                            haku.getIceUnbreakabke().remove(location);
                                            haku.resetPhase();
                                        }
                                    }
                                    for(Location location : haku.sphere(haku.getLoc(), 17, true)){
                                        if(location.getBlock().getType() == Material.PACKED_ICE){
                                            location.getBlock().setType(Material.AIR);
                                            haku.getIceUnbreakabke().remove(location);
                                            haku.resetPhase();
                                        }
                                    }
                                    for(Location location : haku.sphere(haku.getLoc(), 16, true)){
                                        if(location.getBlock().getType() == Material.PACKED_ICE){
                                            location.getBlock().setType(Material.AIR);
                                            haku.getIceUnbreakabke().remove(location);
                                            haku.resetPhase();
                                        }
                                    }
                                    joueur.setRoleCooldownHyoton(15*60);
                                }, 20*60*2);


                            }
                            if(joueur.getRole() instanceof NarutoV2Role){
                                NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                                narutoRole.usePower(joueur);
                                narutoRole.usePowerSpecific(joueur, super.getName());
                            }
                        } else {
                            player.sendMessage(Messages.cooldown(joueur.getRoleCooldownHyoton()));
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                }
            }
        });
    }
}
