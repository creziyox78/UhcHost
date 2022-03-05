package fr.lastril.uhchost.commands;

import com.mojang.authlib.properties.Property;
import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.npc.NPC;
import fr.lastril.uhchost.tools.API.npc.NPCManager;
import fr.lastril.uhchost.world.WorldUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

public class CmdAdmin implements CommandExecutor {

    private final UhcHost main;

    public CmdAdmin(UhcHost main){
        this.main = main;
    }



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length >= 1){
                if(args[0].equalsIgnoreCase("help")){
                    sendUse(player);
                    return true;
                } else if(args[0].equalsIgnoreCase("disperse")){
                    if(args.length == 2){
                        String targetName = args[1];
                        Player target = Bukkit.getPlayer(targetName);
                        if(target != null){
                            PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
                            int distance = 15;
                            if(targetManager.isAlive()){
                                for(Entity entity : target.getNearbyEntities(distance, distance, distance)){
                                    if(entity instanceof Player){
                                        Player nearPlayer = (Player) entity;
                                        UhcHost.debug("§cDispersing " + nearPlayer.getName());
                                        PlayerManager nearManager = main.getPlayerManager(nearPlayer.getUniqueId());
                                        if(nearManager.isAlive()){
                                            main.getGamemanager().teleportPlayerOnGround(nearPlayer);
                                            nearPlayer.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§cVous venez d'être téléporté aléatoirement sur la map par un modérateur, car vous n'avez pas respecté les groupes !");
                                            player.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§cTéléportation de " + nearPlayer.getName() +".");
                                            nearManager.setInvinsible(true);
                                            Bukkit.getScheduler().runTaskLater(main, () -> {
                                                nearManager.setInvinsible(false);
                                                nearPlayer.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§7Vous n'êtes plus invinsible.");
                                            }, 20*5);
                                        }
                                    }
                                }
                                UhcHost.debug("§cDispersing " +target.getName());
                                main.getGamemanager().teleportPlayerOnGround(target);
                                target.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§cVous venez d'être téléporté aléatoirement sur la map par un modérateur, car vous n'avez pas respecté les groupes !");
                                player.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§cTéléportation de " + target.getName() +".");
                                Bukkit.getScheduler().runTaskLater(main, () -> {
                                    targetManager.setInvinsible(false);
                                    target.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§7Vous n'êtes plus invinsible.");
                                }, 20*5);
                                return true;
                            } else {
                                player.sendMessage(Messages.error("Précisez un joueur en vie !"));
                                return false;
                            }
                        } else {
                            player.sendMessage(Messages.error("Précisez un joueur en ligne !"));
                            return false;
                        }
                    }
                }
            }
            sendUse(player);
        }
        return false;
    }

    private void sendUse(Player player) {
        player.sendMessage("§8§m--------------------------------------------------§r");
        player.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage()+"§cListe des commandes d'admin :");
        player.sendMessage(" ");
        player.sendMessage("§f• /a help §7: §eVoir cette page.");
        player.sendMessage("§f• /a disperse <pseudo>: Disperser les joueurs autour d'un autre joueur dans un rayon de 15 blocs.");
        player.sendMessage("§8§m--------------------------------------------------§r");
    }

}
