package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.Kabuto;
import fr.lastril.uhchost.player.PlayerManager;
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

    private final UhcHost main;

    private static final double distance = 5D;

    private static final PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 2, true, true);

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
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return false;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        Player player = (Player) sender;
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(!joueur.isAlive()){
            player.sendMessage(Messages.error("Vous n'êtes plus vivant."));
            return false;
        }
        if (joueur.hasRole()) {
            if (joueur.getRole() instanceof ShosenJutsuUser) {
                ShosenJutsuUser shosenJutsuUser = (ShosenJutsuUser) joueur.getRole();
                if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                    if(joueur.getRoleCooldownShosenjutsu() == 0) {
                        if (args.length == 2) {
                            String playerName = args[1];
                            Player target = Bukkit.getPlayer(playerName);
                            if (target != null) {
                                if (target != player) {
                                    if (target.getLocation().distance(player.getLocation()) <= distance || (joueur.getRole() instanceof Kabuto && target.getLocation().distance(player.getLocation()) <= 20)) {
                                        PlayerManager targetJoueur = main.getPlayerManager(target.getUniqueId());
                                        if (targetJoueur.hasRole()) {
                                            target.addPotionEffect(regen);
                                            shosenJutsuUser.setTargetId(target.getUniqueId());
                                            shosenJutsuUser.addInShosenJutsu(target.getUniqueId());
                                            shosenJutsuUser.addInShosenJutsu(player.getUniqueId());
                                            target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + player.getName() + " est en train de vous soigner, ne bougez pas pour que l'effet persiste !");
                                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous êtes en train de soigner " + target.getName() + ", ne bougez pas pour que l'effet persiste !");

                                            joueur.setRoleCooldownShosenjutsu(10 * 60);
                                            if (joueur.getRole() instanceof NarutoV2Role) {
                                                NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                                                narutoRole.usePower(joueur);
                                                narutoRole.usePowerSpecific(joueur, "/ns shosenjutsu");
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
                    }else{
                        player.sendMessage(Messages.cooldown(joueur.getRoleCooldownShosenjutsu()));
                        return false;
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            }else{
                player.sendMessage(Messages.not("Kabuto ou Sakura"));
                return false;
            }
        }
        return false;
    }

    public interface ShosenJutsuUser {

        UUID getTargetId();
        void setTargetId(UUID uuid);

        default void addInShosenJutsu(UUID uuid){
            UhcHost main = UhcHost.getInstance();
            if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
            NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            narutoV2Manager.addInShosenJutsu(uuid);
        }


    }
}
