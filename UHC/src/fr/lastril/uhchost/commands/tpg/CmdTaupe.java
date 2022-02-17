package fr.lastril.uhchost.commands.tpg;

import com.mojang.authlib.properties.Property;
import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.TaupePlayerManager;
import fr.lastril.uhchost.tools.API.npc.NPC;
import fr.lastril.uhchost.tools.API.npc.NPCManager;
import fr.lastril.uhchost.world.WorldUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

public class CmdTaupe implements CommandExecutor {

    private final UhcHost main = UhcHost.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(main.getGamemanager().getModes() == Modes.TAUPEGUN){
                PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                TaupePlayerManager taupePlayerManager = playerManager.getTaupePlayerManager();
                if(taupePlayerManager.getMoleTeam() != null){
                    if (args.length > 0) {
                        String message = "";
                        for (int i = 0; i < args.length; i++) {
                            message += args[i] + " ";
                        }
                        for(PlayerManager taupes : taupePlayerManager.getMoleTeam().getPlayers()){
                            if(taupes.getPlayer() != null){
                                taupes.getPlayer().sendMessage( "§4§lTaupe§c " + player.getName() + "§8 »§c " + message);
                            }
                        }
                    }
                } else {
                    player.sendMessage(Messages.TAUPE_GUN_PREFIX.getMessage() + "§cVous n'êtes pas taupe !");
                }
            }
        }
        return false;
    }

}
