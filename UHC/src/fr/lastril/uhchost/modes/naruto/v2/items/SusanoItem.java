package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdIzanagi;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.bow.SusanoBow;
import fr.lastril.uhchost.modes.naruto.v2.items.swords.SusanoSword;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Sasuke;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SusanoItem extends QuickItem {

    private static final int SUSANO_COOLDOWN = 20*60;
    private NarutoV2Manager narutoV2Manager;

    public SusanoItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§cSusano");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
                if (joueur.getRole() instanceof SusanoUser) {
                    if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                        if (joueur.getRoleCooldownSusano() <= 0) {
                            if(joueur.getRole() instanceof CmdIzanagi.IzanagiUser) {
                        		CmdIzanagi.IzanagiUser izanagiUser = (CmdIzanagi.IzanagiUser) joueur.getRole();
                        		if(izanagiUser.hasUsedIzanagi()) {
                        			player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.CANT_USE_MORE_POWER.getMessage());
                        			return;
                        		}
                        	}
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5*60*20, 0, false, false));
                            if(joueur.getRole() instanceof Sasuke) {
                            	Sasuke sasuke = (Sasuke) joueur.getRole();
                            	if(sasuke.isKilledItachi()) {
                            		main.getInventoryUtils().giveItemSafely(player, new SusanoSword().toItemStack());
                            		narutoV2Manager.addInSusanoDouble(player.getUniqueId());
                            	} else {
                            		narutoV2Manager.addInSusano(player.getUniqueId());
                            	}
                            	joueur.setRoleCooldownSusano(SUSANO_COOLDOWN);
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+Messages.USED_POWER.getMessage());
                            	main.getInventoryUtils().giveItemSafely(player, new SusanoBow().toItemStack());
                                joueur.sendTimer(player, joueur.getRoleCooldownSusano(), player.getItemInHand());
                            	return;
                            } else {
                            	main.getInventoryUtils().giveItemSafely(player, new SusanoSword().toItemStack());
                                narutoV2Manager.addInSusano(player.getUniqueId());
                            }
                            joueur.setRoleCooldownSusano(SUSANO_COOLDOWN);
                            joueur.sendTimer(player, joueur.getRoleCooldownSusano(), player.getItemInHand());
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+Messages.USED_POWER.getMessage());
                            if(joueur.getRole() instanceof NarutoV2Role){
                                NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                                narutoRole.usePower(joueur);
                                narutoRole.usePowerSpecific(joueur, super.getName());
                            }
                        } else {
                            player.sendMessage(Messages.cooldown(joueur.getRoleCooldownSusano()));
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                } else {
                    player.sendMessage(Messages.not("Sasuke, Obito, Itachi, ou Madara"));
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            }
        });
    }

    public interface SusanoUser {
    }
}
