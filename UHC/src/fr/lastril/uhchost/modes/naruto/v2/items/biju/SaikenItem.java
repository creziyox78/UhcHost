package fr.lastril.uhchost.modes.naruto.v2.items.biju;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.biju.bijus.SaikenBiju;
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

public class SaikenItem extends QuickItem {

    private int timer = 5*60;
    private NarutoV2Manager narutoV2Manager;

    public SaikenItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§5Saiken");
        super.setLore("",
                "§eDémon à queue",
                "",
                "§7Permet à son utilisateur d'avoir les effets§9 Speed 1",
                "§7ainsi que§cForce I§7 pendant 5 minutes. A chaque coup, l'utilisateur",
                "§7possède 15% de chance d'exploser et d'aveugler le joueur frappé.");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if(joueur.hasRole()) {
                if(!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                    if(joueur.getRoleCooldownBiju() <= 0) {
                        if(narutoV2Manager.getBijuManager().getHotesBiju().get(SaikenBiju.class) == joueur
                                || (narutoV2Manager.getBijuManager().getHotesBiju().get(SaikenBiju.class) == null
                                && !narutoV2Manager.getBijuManager().isAlreadyHote(joueur))) {
                            narutoV2Manager.getBijuManager().getHotesBiju().put(SaikenBiju.class, joueur);
                            narutoV2Manager.getBijuManager().getSaikenHote().add(joueur);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*5*60, 0, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*5*60, 0, false, false));
                            joueur.setRoleCooldownBiju(15*60);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ActionBar.sendMessage(player, "§ePouvoir de §5Saiken : " + new FormatTime(timer));
                                    if(timer <= 0) {
                                        timer = 5*60;
                                        narutoV2Manager.getBijuManager().getSaikenHote().remove(joueur);
                                        joueur.sendTimer(player, joueur.getRoleCooldownJubi(), SaikenItem.super.toItemStack());
                                        cancel();
                                    }
                                    timer--;
                                }
                            }.runTaskTimer(main, 0, 20);
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
