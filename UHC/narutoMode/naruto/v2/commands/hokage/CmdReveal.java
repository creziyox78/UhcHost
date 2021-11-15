package fr.lastril.uhchost.modes.naruto.v2.commands.hokage;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdReveal implements ModeSubCommand {

    private final UhcHost main;

    public CmdReveal(UhcHost main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (playerManager.isAlive()) {
            if (main.getNarutoV2Manager().getHokage() == playerManager) {
                if (!main.getNarutoV2Manager().isReveal()) {
                    String revealMessage = Messages.NARUTO_PREFIX.getMessage() + "Voici les 4 pseudos: \n";
                    if (UhcHost.getRANDOM().nextInt(main.getNarutoV2Manager().getPlayerManagersWithCamps(Camps.SHINOBI).size()) >= 0) {
                        revealMessage += main.getNarutoV2Manager().getPlayerManagersWithCamps(Camps.SHINOBI).get(UhcHost.getRANDOM().nextInt(main.getNarutoV2Manager().getPlayerManagersWithCamps(Camps.SHINOBI).size())).getPlayerName() + " \n";
                        revealMessage += main.getNarutoV2Manager().getPlayerManagersWithCamps(Camps.SHINOBI).get(UhcHost.getRANDOM().nextInt(main.getNarutoV2Manager().getPlayerManagersWithCamps(Camps.SHINOBI).size())).getPlayerName() + " \n";
                    }
                    revealMessage += main.getRandomPlayerManagerAlive().getPlayerName() + " \n";
                    if (UhcHost.getRANDOM().nextInt(main.getNarutoV2Manager().getPlayerManagersNotInCamps(Camps.SHINOBI).size()) >= 0) {
                        revealMessage += main.getNarutoV2Manager().getPlayerManagersNotInCamps(Camps.SHINOBI).get(UhcHost.getRANDOM().nextInt(main.getNarutoV2Manager().getPlayerManagersNotInCamps(Camps.SHINOBI).size())).getPlayerName() + " \n";
                    }

                    revealMessage += " \n";
                    player.sendMessage(revealMessage);

                    main.getNarutoV2Manager().setReveal(true);
                }
            }
        }
        return false;
    }

    @Override
    public String getSubCommandName() {
        return "reveal";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

}
