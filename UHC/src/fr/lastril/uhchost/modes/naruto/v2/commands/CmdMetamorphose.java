package fr.lastril.uhchost.modes.naruto.v2.commands;

import com.mojang.authlib.properties.Property;
import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.ZetsuBlanc;
import fr.lastril.uhchost.modes.naruto.v2.tasks.MetamorphoseTask;
import fr.lastril.uhchost.player.PlayerManager;
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
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(!joueur.isAlive()){
            player.sendMessage(Messages.error("Vous n'êtes plus vivant."));
            return false;
        }
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return false;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        if (joueur.hasRole()) {
            if (joueur.getRole() instanceof ZetsuBlanc) {
                ZetsuBlanc zetsuBlanc = (ZetsuBlanc) joueur.getRole();
                if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                    if (joueur.getRoleCooldownMetamorphose() <= 0) {
                        if (args.length == 2) {
                            String playerName = args[1];
                            Player target = Bukkit.getPlayer(playerName);
                            if (target != null) {
                                PlayerManager targetJoueur = main.getPlayerManager(target.getUniqueId());
                                if (targetJoueur.hasRole()) {

                                    String copiedName = target.getName();
                                    //Property copiedSkin = main.getAPI().getNpcManager().getPlayerTextures(target);


                                    zetsuBlanc.setCopiedName(copiedName);
                                    //zetsuBlanc.setCopiedSkin(copiedSkin);

                                    //IdentityChanger.changePlayerName(player, copiedName);
                                    //IdentityChanger.changeSkin(player, copiedSkin, false);

                                    new MetamorphoseTask(main, zetsuBlanc).runTaskTimer(main, 0, 20);

                                    joueur.setRoleCooldownMetamorphose(20 * 60);
                                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() +"§aVous êtes maintenant métamorphosé en "+target.getName()+". Evitez de vous déconnecter du serveur en étant transformé. La personne ciblé aura votre skin (et vous aussi).");
                                    zetsuBlanc.usePower(joueur);
                                    zetsuBlanc.usePowerSpecific(joueur, "/ns métamorphose");
                                } else {
                                    player.sendMessage(Messages.NOT_INGAME.getMessage());
                                }
                            } else {
                                player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
                            }
                        }
                    } else {
                        player.sendMessage(Messages.cooldown(joueur.getRoleCooldownMetamorphose()));
                        return false;
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Zetsu Blanc"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
        return false;
    }

}
