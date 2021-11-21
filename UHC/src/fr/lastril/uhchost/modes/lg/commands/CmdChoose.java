package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.gui.ChooseCampGui;
import fr.lastril.uhchost.modes.lg.roles.village.ChienLoup;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdChoose implements ModeSubCommand {

    private final UhcHost main;

    public CmdChoose(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "choose";
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
        if(playerManager.getRole() instanceof ChienLoup){
            ChienLoup chienLoup = (ChienLoup) playerManager.getRole();
            if(!chienLoup.isChoosen()){
                new ChooseCampGui(chienLoup).open(player);
            } else {
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous ne pouvez plus choisir votre camp.");
            }
        } else {
            player.sendMessage(Messages.not("Chien-Loup"));
        }
        return false;
    }
}
