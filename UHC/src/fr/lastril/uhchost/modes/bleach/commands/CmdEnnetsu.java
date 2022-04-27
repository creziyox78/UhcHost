package fr.lastril.uhchost.modes.bleach.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Yamamoto;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdEnnetsu implements ModeSubCommand {

    private final UhcHost main;

    public CmdEnnetsu(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "ennetsu";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if(playerManager.getRole() instanceof Yamamoto){
            Yamamoto yamamoto = (Yamamoto) playerManager.getRole();
            if(bleachPlayerManager.canUsePower()){
                if(!yamamoto.hasReachedUse()){
                    if(playerManager.getRoleCooldownEnnetsu() <= 0){
                        yamamoto.addUse();
                        yamamoto.ennetsuPower(player);
                        playerManager.setRoleCooldownEnnetsu(4*60);
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownEnnetsu()));
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§6Vous avez déjà épuisé cette capacité !");
                }
            } else {
                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
            }
        } else {
            player.sendMessage(Messages.not("Yamamoto"));
        }
        return false;
    }
}
