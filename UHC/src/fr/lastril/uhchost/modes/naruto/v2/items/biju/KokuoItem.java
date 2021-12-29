package fr.lastril.uhchost.modes.naruto.v2.items.biju;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.biju.bijus.KokuoBiju;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class KokuoItem extends QuickItem {

    private int timer = 5*60;
    private NarutoV2Manager narutoV2Manager;

    public KokuoItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§9Kokuô");
        super.setLore("",
                "§eDémon à queue",
                "",
                "§7Permet à son utilisateur de recevoir l'effet§f Vitesse 2,",
                "§725% de chance en tappant, que l'utilisateur reçoit§f Vitesse 3§7",
                "§7pendant 5 secondes et §8aveugle le joueur ciblé§7.");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if(joueur.hasRole()) {
                if(!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                    if(joueur.getRoleCooldownBiju() <= 0) {
                        if(narutoV2Manager.getBijuManager().getHotesBiju().get(KokuoBiju.class) == joueur
                                || (narutoV2Manager.getBijuManager().getHotesBiju().get(KokuoBiju.class) == null
                                && !narutoV2Manager.getBijuManager().isAlreadyHote(joueur))) {
                            narutoV2Manager.getBijuManager().getHotesBiju().put(KokuoBiju.class, joueur);
                            narutoV2Manager.getBijuManager().getKokuoHote().add(joueur);
                            if(player.hasPotionEffect(PotionEffectType.SPEED)){
                                player.removePotionEffect(PotionEffectType.SPEED);
                            }
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*60*5, 1, false, false));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ActionBar.sendMessage(player, "§ePouvoirs de §9Kokuô : " + new FormatTime(timer));
                                    if(timer <= 0) {
                                        timer = 5*60;
                                        narutoV2Manager.getBijuManager().getKokuoHote().remove(joueur);
                                        joueur.getRole().getEffects().entrySet().stream().filter(e -> e.getValue() == When.START).forEach(e -> {
                                            if (joueur.getPlayer() != null) {
                                                if(joueur.getPlayer().hasPotionEffect(e.getKey().getType())){
                                                    joueur.getPlayer().removePotionEffect(e.getKey().getType());
                                                }
                                                joueur.getPlayer().addPotionEffect(e.getKey());
                                            }
                                        });
                                        joueur.sendTimer(player, joueur.getRoleCooldownBiju(), KokuoItem.super.toItemStack());
                                        cancel();
                                    }
                                    timer--;
                                }
                            }.runTaskTimer(main, 0, 20);
                            joueur.setRoleCooldownBiju(20*60);
                            if(joueur.getRole() instanceof NarutoV2Role){
                                NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                                narutoRole.usePower(joueur);
                                narutoRole.usePowerSpecific(joueur, super.getName());
                            }
                        } else {
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§6Vous ne pouvez pas utiliser ce Bijû.");
                        }
                    } else {
                        player.sendMessage(Messages.cooldown(joueur.getRoleCooldownBiju()));
                    }
                } else {
                    player.sendMessage(
                            Messages.NARUTO_PREFIX.getMessage() + "§eVous êtes sous l'effet de §cSamehada§e.");
                }
            }
        });
    }
}
