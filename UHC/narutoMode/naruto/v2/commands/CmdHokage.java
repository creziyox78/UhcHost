package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Danzo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CmdHokage implements ModeSubCommand {

    private final UhcHost main;

    public CmdHokage(UhcHost main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (main.getConfiguration().getMode() != Modes.NARUTO_V2)
            return false;
        Player player = (Player) sender;
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.isAlive() && PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Danzo) {
                Danzo danzo = (Danzo) PlayerManager.getRole();
                if (!danzo.isWeakHokage()) {
                    if (main.getNarutoV2Manager().getHokage() != null) {
                        if (main.getNarutoV2Manager().getHokage().getPlayer() != null) {
                            Player hokage = main.getNarutoV2Manager().getHokage().getPlayer();
                            hokage.setMaxHealth(hokage.getMaxHealth() - (2D * 2D));
                            hokage.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 60 * 2, 0, false, false));
                            hokage.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 60 * 2, 0, false, false));
                            hokage.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§7Danzo vient d'utiliser son pouvoir sur vous, car vous êtes l'Hokage.");
                            danzo.setWeakHokage(true);
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    hokage.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aL'effet de Danzo s'est dissipé, vous récuperez votre vie.");
                                    hokage.setMaxHealth(hokage.getHealth() + (2D * 2D));
                                }
                            }.runTaskLater(main, 20 * 60 * 2);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cAttendez que l'Hokage soit annoncé.");
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.CANT_USE_MORE_POWER.getMessage());
                }
            } else {
                player.sendMessage(Messages.not("Danzo"));
            }
        }
        return false;
    }

    @Override
    public String getSubCommandName() {
        return "hokage";
    }

    @Override
    public List<String> getSubArgs() {
        return null;
    }

}
