package fr.lastril.uhchost.modes.lg.commands.ange;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.solo.Ange;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdDechu implements ModeSubCommand {

    private final UhcHost main;

    public CmdDechu(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "ange_dechu";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if(playerManager.getRole() instanceof Ange){
            Ange ange = (Ange) playerManager.getRole();
            if(!ange.hasChoose()){
                ange.setForm(Ange.Form.DECHU);
                ange.setChoose(true);
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§eVouc avez choisi \"§cAnge Déchu§e\", vous devez donc tuer : " + ange.getCible().getPlayerName());
                player.setMaxHealth(player.getMaxHealth() + 2D*2D);
                player.setHealth(player.getMaxHealth());
            }
        }
        return false;
    }
}
