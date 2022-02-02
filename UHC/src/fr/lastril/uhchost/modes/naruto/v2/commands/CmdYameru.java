package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.KamuiItem;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CmdYameru implements ModeSubCommand {

    private final UhcHost main;

    public CmdYameru(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "yameru";
    }

    @Override
    public List<String> getSubArgs() {
        return new ArrayList<>();
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
            if (joueur.getRole() instanceof KamuiItem.KamuiUser) {
                KamuiItem.KamuiUser kamuiUser = (KamuiItem.KamuiUser) joueur.getRole();
                if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {

                    for (Map.Entry<UUID, Location> e : kamuiUser.getInitialsLocation().entrySet()) {
                        Player target = Bukkit.getPlayer(e.getKey());
                        if(target != null && target.getWorld() == narutoV2Manager.getKamuiWorld()){
                            e.getValue().getChunk().load();
                            target.teleport(e.getValue());
                        }
                    }
                    kamuiUser.getInitialsLocation().clear();
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Tout le monde a été téléporté dans le monde normal.");
                    if(joueur.getRole() instanceof NarutoV2Role){
                        NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                        narutoRole.usePower(joueur);
                        narutoRole.usePowerSpecific(joueur, "/ns yameru");
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Kakashi ou Obito"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
        return false;
    }
}
