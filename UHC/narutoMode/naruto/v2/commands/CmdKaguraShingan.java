package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.atlantis.api.utils.Title;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.naruto.v2.roles.taka.Karin;
import fr.maygo.uhc.obj.PlayerManager;
import fr.maygo.uhc.utils.DynamicArrow;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class CmdKaguraShingan implements ModeSubCommand {

    private final UhcHost main;

    public CmdKaguraShingan(UhcHost main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (!PlayerManager.isAlive())
            return false;
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Karin) {
                Karin karin = (Karin) PlayerManager.getRole();
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (PlayerManager.getRoleCooldownKaguraShingan() <= 0) {
                        if (args.length == 2) {
                            String playerName = args[1];
                            Player target = Bukkit.getPlayer(playerName);
                            if (target != null) {
                                PlayerManager targetPlayerManager = main.getPlayerManager(target.getUniqueId());
                                if (targetPlayerManager.isAlive()) {
                                    if (karin.getTracked() == null) {
                                        new BukkitRunnable() {

                                            @Override
                                            public void run() {
                                                String arrow = DynamicArrow.getDirectionOf(player.getLocation(), karin.getTracked().getPlayer().getLocation());
                                                if (karin.getPlayer() != null) {
                                                    if (PlayerManager.isAlive() && main.getPlayerManager(karin.getPlayer().getUniqueId()).isAlive() && karin.getTracked().isAlive()) {
                                                        if (player.getLocation().distance(karin.getTracked().getPlayer().getLocation()) <= 200) {
                                                            ActionBar.sendMessage(player, "§6" + karin.getTracked().getPlayer().getName() + " " + arrow);
                                                        } else {
                                                            ActionBar.sendMessage(player, "§cLe PlayerManager se trouve à plus de 200 blocs.");
                                                        }
                                                    } else {
                                                        karin.setTracked(null);
                                                        cancel();
                                                    }
                                                }


                                            }
                                        }.runTaskTimer(main, 5, 1);
                                    }
                                    karin.setTracked(targetPlayerManager);
                                    PlayerManager.setRoleCooldownKaguraShingan(5 * 60);
                                    karin.usePower(PlayerManager);
                                }
                            } else {
                                player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
                            }
                        }
                    } else {
                        player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownKaguraShingan()));
                    }

                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public String getSubCommandName() {
        return "kagurashingan";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

}
