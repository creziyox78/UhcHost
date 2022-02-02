package fr.lastril.uhchost.modes.naruto.v2.commands.hokage;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
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
		if(main.getGamemanager().getModes() != Modes.NARUTO) return false;
		NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
		Player player = (Player) sender;
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(joueur.isAlive()) {
        	if(narutoV2Manager.getHokage() == joueur) {
        		if(!narutoV2Manager.isReveal()) {
        			String revealMessage = Messages.NARUTO_PREFIX.getMessage() + "Voici les 4 pseudos: \n";
        			if(UhcHost.getRANDOM().nextInt(narutoV2Manager.getPlayerManagersWithCamps(Camps.SHINOBI).size()) >= 0){
						revealMessage += narutoV2Manager.getPlayerManagersWithCamps(Camps.SHINOBI).get(UhcHost.getRANDOM().nextInt(narutoV2Manager.getPlayerManagersWithCamps(Camps.SHINOBI).size())).getPlayerName() + " \n";
						revealMessage += narutoV2Manager.getPlayerManagersWithCamps(Camps.SHINOBI).get(UhcHost.getRANDOM().nextInt(narutoV2Manager.getPlayerManagersWithCamps(Camps.SHINOBI).size())).getPlayerName() + " \n";
					}
        			revealMessage += main.getRandomPlayerManagerAlive().getPlayerName() + " \n";
        			if(UhcHost.getRANDOM().nextInt(narutoV2Manager.getPlayerManagersNotInCamps(Camps.SHINOBI).size()) >= 0){
						revealMessage += narutoV2Manager.getPlayerManagersNotInCamps(Camps.SHINOBI).get(UhcHost.getRANDOM().nextInt(narutoV2Manager.getPlayerManagersNotInCamps(Camps.SHINOBI).size())).getPlayerName() + " \n";
					}

        			revealMessage += " \n";
        			player.sendMessage(revealMessage);
					if(joueur.getRole() instanceof NarutoV2Role){
						NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
						narutoRole.usePower(joueur);
						narutoRole.usePowerSpecific(joueur, "/ns reveal");
					}
        			narutoV2Manager.setReveal(true);
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
