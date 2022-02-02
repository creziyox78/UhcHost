package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Gaara;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.world.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdFix implements ModeSubCommand {

    private final UhcHost main;

    public CmdFix(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "fix";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(!joueur.isAlive()){
            player.sendMessage(Messages.error("Vous n'êtes plus vivant."));
            return false;
        }
        if(main.getGamemanager().getModes() != Modes.NARUTO) return false;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        if (joueur.hasRole()) {
            if (joueur.getRole() instanceof Gaara) {
                Gaara gaara = (Gaara) joueur.getRole();
                if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                    if(joueur.getRoleCooldownFix() <= 0){
                        if (args.length == 2) {
                            String playerName = args[1];
                            Player target = Bukkit.getPlayer(playerName);
                            if (target != null) {
                                PlayerManager targetJoueur = main.getPlayerManager(target.getUniqueId());
                                if (targetJoueur.isAlive()) {
                                    if(WorldUtils.getDistanceBetweenTwoLocations(player.getLocation(), target.getLocation()) < 30){
                                        gaara.teleportInJump(target);
                                        joueur.setRoleCooldownFix(15*60);
                                        gaara.usePower(joueur);
                                        gaara.usePowerSpecific(joueur, "/ns fix");
                                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous avez scellé "+target.getName()+" dans votre jump.");
                                    }else{
                                        player.sendMessage(Messages.error("Vous n'êtes pas à 30 blocs de "+target.getName()));
                                    }
                                } else {
                                    player.sendMessage(Messages.NOT_INGAME.getMessage());
                                }
                            } else {
                                player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
                            }
                        }
                    }else{
                        player.sendMessage(Messages.cooldown(joueur.getRoleCooldownFix()));
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Gaara"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
        return false;
    }
}
