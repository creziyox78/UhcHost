package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.KuramaItem;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Itachi;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Obito;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Sai;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Karin;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Sasuke;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CmdSmell implements ModeSubCommand {

    private final UhcHost main;
    private final List<String> whitelistedSmell;
    private static final int DISTANCE_SAI_SASUKE = 30;

    public CmdSmell(UhcHost main) {
        this.main = main;
        this.whitelistedSmell = Arrays.asList(new Karin().getRoleName(), new Itachi().getRoleName(), new Obito().getRoleName());
    }


    @Override
    public String getSubCommandName() {
        return "smell";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(main.getGamemanager().getModes() != Modes.NARUTO) return false;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        Player player = (Player) sender;
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(!joueur.isAlive()){
            player.sendMessage(Messages.error("Vous n'êtes plus vivant."));
            return false;
        }
        if (joueur.hasRole()) {
            if (joueur.getRole() instanceof KuramaItem.KuramaUser) {
                KuramaItem.KuramaUser kuramaUser = (KuramaItem.KuramaUser) joueur.getRole();
                if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                    if (kuramaUser.canUseSmell()) {
                        if (args.length == 2) {
                            String playerName = args[1];
                            Player target = Bukkit.getPlayer(playerName);
                            if (target != null) {
                                PlayerManager targetJoueur = main.getPlayerManager(target.getUniqueId());
                                if (targetJoueur.hasRole() && targetJoueur.isAlive()) {
                                    if(player.getWorld() == target.getWorld() && player.getLocation().distance(target.getLocation()) <= 20){
                                        kuramaUser.setCanUseSmell(false);
                                        if (targetJoueur.getRole() instanceof Sai) {
                                            for (Entity entity : target.getNearbyEntities(DISTANCE_SAI_SASUKE, DISTANCE_SAI_SASUKE, DISTANCE_SAI_SASUKE)) {
                                                if (entity instanceof Player) {
                                                    Player nearby = (Player) entity;
                                                    PlayerManager nearbyJoueur = main.getPlayerManager(nearby.getUniqueId());
                                                    if (nearbyJoueur.hasRole()) {
                                                        if (nearbyJoueur.getRole() instanceof Sasuke) {
                                                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cCe joueur possède des intentions meurtrières.");
                                                            return false;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (targetJoueur.getCamps() != Camps.SHINOBI) {
                                            if (!this.whitelistedSmell.contains(targetJoueur.getRole().getRoleName())) {
                                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cCe joueur possède des intentions meurtrières.");
                                                return true;
                                            }
                                        }
                                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aCe joueur ne possède aucunes intentions meurtrières.");
                                        if(joueur.getRole() instanceof NarutoV2Role){
                                            NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                                            narutoRole.usePower(joueur);
                                            narutoRole.usePowerSpecific(joueur, "/ns smell");
                                        }
                                        return true;
                                    } else {
                                        player.sendMessage(Messages.error("Vous n'êtes pas à 20 blocs de ce joueur."));
                                        return false;
                                    }

                                } else {
                                    player.sendMessage(Messages.NOT_INGAME.getMessage());
                                }
                            } else {
                                player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
                            }
                        }
                    } else {
                        player.sendMessage(Messages.error("Vous ne pouvez pas utiliser votre /smell maintenant !"));
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Naruto ou Minato"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
        return false;
    }
}
