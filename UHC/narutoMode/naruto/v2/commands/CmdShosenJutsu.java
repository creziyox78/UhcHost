package fr.lastril.uhchost.modes.naruto.v2.commands;

;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.roles.orochimaru.Kabuto;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CmdShosenJutsu implements ModeSubCommand {

    private static final double distance = 5D;
    private static final PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 2, true, true);
    private final UhcHost main;

    public CmdShosenJutsu(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "shosenjutsu";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (!PlayerManager.isAlive()) {
            player.sendMessage(Messages.error("Vous n'êtes plus vivant."));
            return false;
        }
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof ShosenJutsuUser) {
                ShosenJutsuUser shosenJutsuUser = (ShosenJutsuUser) PlayerManager.getRole();
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (PlayerManager.getRoleCooldownShosenjutsu() == 0) {
                        if (args.length == 2) {
                            String playerName = args[1];
                            Player target = Bukkit.getPlayer(playerName);
                            if (target != null) {
                                if (target != player) {
                                    if (target.getLocation().distance(player.getLocation()) <= distance || (PlayerManager.getRole() instanceof Kabuto && target.getLocation().distance(player.getLocation()) <= 20)) {
                                        PlayerManager targetPlayerManager = main.getPlayerManager(target.getUniqueId());
                                        if (targetPlayerManager.hasRole()) {
                                            target.addPotionEffect(regen);
                                            shosenJutsuUser.addInShosenJutsu(target.getUniqueId());
                                            shosenJutsuUser.addInShosenJutsu(player.getUniqueId());
                                            target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + player.getName() + " est en train de vous soigner, ne bougez pas pour que l'effet persiste !");
                                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous êtes en train de soigner " + target.getName() + ", ne bougez pas pour que l'effet persiste !");

                                            PlayerManager.setRoleCooldownShosenjutsu(10 * 60);
                                            if (PlayerManager.getRole() instanceof NarutoV2Role) {
                                                NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                                                narutoRole.usePower(PlayerManager);
                                            }

                                            return true;
                                        } else {
                                            player.sendMessage(Messages.NOT_INGAME.getMessage());
                                            return false;
                                        }
                                    } else {
                                        player.sendMessage(Messages.error("Vous n'êtes pas à " + distance + " blocs de " + target.getName()));
                                        return false;
                                    }
                                } else {
                                    player.sendMessage(Messages.NOT_FOR_YOU.getMessage());
                                    return false;
                                }
                            } else {
                                player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
                                return false;
                            }
                        }
                    } else {
                        player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownShosenjutsu()));
                        return false;
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Kabuto ou Sakura"));
                return false;
            }
        }
        return false;
    }

    public interface ShosenJutsuUser {

        default void addInShosenJutsu(UUID uuid) {
            UhcHost.getInstance().getNarutoV2Manager().addInShosenJutsu(uuid);
        }


    }
}
