package fr.lastril.uhchost.modes.naruto.v2.items.biju;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.biju.bijus.IsobuBiju;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class IsobuItem extends QuickItem {

    private int timer = 5*60;
    private NarutoV2Manager narutoV2Manager;

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
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if(joueur.hasRole()) {
                if(!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                    if(joueur.getRoleCooldownBiju() <= 0) {
                        if(narutoV2Manager.getBijuManager().getHotesBiju().get(IsobuBiju.class) == joueur
                                || (narutoV2Manager.getBijuManager().getHotesBiju().get(IsobuBiju.class) == null
                                && !narutoV2Manager.getBijuManager().isAlreadyHote(joueur))) {
                            narutoV2Manager.getBijuManager().getHotesBiju().put(IsobuBiju.class, joueur);
                            narutoV2Manager.getBijuManager().getIsobuResistance().add(joueur);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous venez d'utiliser les pouvoirs d'Isobu.");
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*60*5, 0, false, false));
                            player.setMaxHealth(player.getMaxHealth() + (2D*5D));
                            player.setHealth(player.getHealth() + (2D*5D));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ActionBar.sendMessage(player, "§ePouvoir de Isobu : " + new FormatTime(timer));
                                    if(timer <= 0) {
                                        timer = 5*60;
                                        player.setMaxHealth(player.getMaxHealth() - (5D*2D));
                                        narutoV2Manager.getBijuManager().getIsobuResistance().remove(joueur);
                                        joueur.sendTimer(player, joueur.getRoleCooldownBiju(), IsobuItem.super.toItemStack());
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
