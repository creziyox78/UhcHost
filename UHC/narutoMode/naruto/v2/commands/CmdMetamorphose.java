package fr.lastril.uhchost.modes.naruto.v2.commands;

import com.mojang.authlib.properties.Property;
import fr.atlantis.api.utils.IdentityChanger;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.ZetsuBlanc;
import fr.maygo.uhc.modes.naruto.v2.tasks.MetamorphoseTask;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CmdMetamorphose implements ModeSubCommand {

    private final UhcHost main;

    public CmdMetamorphose(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "métamorphose";
    }

    @Override
    public List<String> getSubArgs() {
        return new ArrayList<>();
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
            if (PlayerManager.getRole() instanceof ZetsuBlanc) {
                ZetsuBlanc zetsuBlanc = (ZetsuBlanc) PlayerManager.getRole();
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (PlayerManager.getRoleCooldownMetamorphose() <= 0) {
                        if (args.length == 2) {
                            String playerName = args[1];
                            Player target = Bukkit.getPlayer(playerName);
                            if (target != null) {
                                PlayerManager targetPlayerManager = main.getPlayerManager(target.getUniqueId());
                                if (targetPlayerManager.hasRole()) {

                                    String copiedName = target.getName();
                                    Property copiedSkin = main.getAPI().getNpcManager().getPlayerTextures(target);


                                    zetsuBlanc.setCopiedName(copiedName);
                                    zetsuBlanc.setCopiedSkin(copiedSkin);

                                    IdentityChanger.changePlayerName(player, copiedName);
                                    IdentityChanger.changeSkin(player, copiedSkin, false);

                                    new MetamorphoseTask(main, zetsuBlanc).runTaskTimer(main, 0, 20);

                                    PlayerManager.setRoleCooldownMetamorphose(20 * 60);
                                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous êtes maintenant métamorphosé en " + target.getName() + ". Evitez de vous déconnecter du serveur en étant transformé. La personne ciblé aura votre skin (et vous aussi).");
                                } else {
                                    player.sendMessage(Messages.NOT_INGAME.getMessage());
                                }
                            } else {
                                player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
                            }
                        }
                    } else {
                        player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownMetamorphose()));
                        return false;
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Konan"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
        return false;
    }

}
