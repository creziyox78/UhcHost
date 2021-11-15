package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdIzanagi.IzanagiUser;
import fr.maygo.uhc.modes.naruto.v2.items.bow.SusanoBow;
import fr.maygo.uhc.modes.naruto.v2.items.swords.SusanoSword;
import fr.maygo.uhc.modes.naruto.v2.roles.taka.Sasuke;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SusanoItem extends QuickItem {

    private static final int SUSANO_COOLDOWN = 20 * 60;

    public SusanoItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§cSusano");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof SusanoUser) {
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownSusano() <= 0) {
                            if (PlayerManager.getRole() instanceof IzanagiUser) {
                                IzanagiUser izanagiUser = (IzanagiUser) PlayerManager.getRole();
                                if (izanagiUser.hasUsedIzanagi()) {
                                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.CANT_USE_MORE_POWER.getMessage());
                                    return;
                                }
                            }
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5 * 60 * 20, 0, false, false));
                            if (PlayerManager.getRole() instanceof Sasuke) {
                                Sasuke sasuke = (Sasuke) PlayerManager.getRole();
                                if (sasuke.isKilledItachi()) {
                                    main.getInventoryUtils().giveItemSafely(player, new SusanoSword().toItemStack());
                                    main.getNarutoV2Manager().addInSusanoDouble(player.getUniqueId());
                                } else {
                                    main.getNarutoV2Manager().addInSusano(player.getUniqueId());
                                }
                                PlayerManager.setRoleCooldownSusano(SUSANO_COOLDOWN);
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                                main.getInventoryUtils().giveItemSafely(player, new SusanoBow().toItemStack());
                                PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownSusano(), player.getItemInHand());
                                return;
                            } else {
                                main.getInventoryUtils().giveItemSafely(player, new SusanoSword().toItemStack());
                                main.getNarutoV2Manager().addInSusano(player.getUniqueId());
                            }
                            PlayerManager.setRoleCooldownSusano(SUSANO_COOLDOWN);
                            PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownSusano(), player.getItemInHand());
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                        } else {
                            player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownSusano()));
                            return;
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    player.sendMessage(Messages.not("Sasuke, Obito, Itachi, ou Madara"));
                    return;
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }

    public interface SusanoUser {
    }
}
