package fr.lastril.uhchost.commands.tpg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.TaupePlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdReveal implements CommandExecutor {

    private final UhcHost main = UhcHost.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(main.getGamemanager().getModes() == Modes.TAUPEGUN){
                PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                TaupePlayerManager taupePlayerManager = playerManager.getTaupePlayerManager();
                if(taupePlayerManager.getMoleTeam() != null){
                    if(!taupePlayerManager.isRevealed()){
                        taupePlayerManager.getMoleTeam().updateDisplayPlayer(playerManager);
                        for (final Player r : Bukkit.getServer().getOnlinePlayers()) {
                            r.playSound(r.getLocation(), Sound.GHAST_SCREAM, 1.0f, 1.0f);
                        }
                        taupePlayerManager.setRevealed(true);
                        main.getInventoryUtils().giveItemSafely(player, new QuickItem(Material.GOLDEN_APPLE).toItemStack());
                        Bukkit.broadcastMessage(Messages.TAUPE_GUN_PREFIX.getMessage() + "§4§l" + player.getName() + " §c se révèle être une taupe !");
                    }
                } else {
                    player.sendMessage(Messages.TAUPE_GUN_PREFIX.getMessage() + "§cVous n'êtes pas taupe !");
                }
            }
        }
        return false;
    }

}
