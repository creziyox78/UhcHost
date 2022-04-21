package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.Sado;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Brazo extends QuickItem {
    public Brazo(UhcHost main) {
        super(Material.BRICK);
        super.setName("§cBrazo");
        super.glow(true);
        super.setLore("§7Permet de changer de forme.",
                "",
                "§f§l+ §7Forme : §a§lBras du Géant",
                "§7   - §9Résistance 1 | §aNoFall",
                "",
                "§f§l+ §7Forme : §4§lBras du Diable",
                "§7   - §cForce 1 | §bSpeed +10%",
                "",
                "§7(Cooldown - 3 minutes)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof Sado){
                Sado sado = (Sado) playerManager.getRole();
                if(bleachPlayerManager.canUsePower()){
                    if(playerManager.getRoleCooldownBrazo() <= 0){
                        if(sado.getForm() == Sado.ARMS_FORM.ARM_GEANT){
                            sado.setForm(Sado.ARMS_FORM.ARM_DIABLE);
                            playerManager.setRoleCooldownBrazo(60*3);
                            bleachPlayerManager.setNoFall(true);
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§6Vous activez la forme “§4Bras du Diable§6”.");
                        } else if (sado.getForm() == Sado.ARMS_FORM.ARM_DIABLE){
                            sado.setForm(Sado.ARMS_FORM.ARM_GEANT);
                            playerManager.setRoleCooldownBrazo(60*3);
                            bleachPlayerManager.setNoFall(false);
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§6Vous activez la forme “§aBras du Géant§6”.");
                        }
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownBrazo()));
                    }
                }
            }
        });
    }
}
