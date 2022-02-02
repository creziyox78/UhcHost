package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class CmdIzanagi implements ModeSubCommand {

    private final UhcHost main;

    public CmdIzanagi(UhcHost main) {
        this.main = main;
    }


    @Override
    public String getSubCommandName() {
        return "izanagi";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(!joueur.isAlive()){
            player.sendMessage(Messages.error("Vous n'êtes plus vivant."));
            return false;
        }
        if(main.getGamemanager().getModes() != Modes.NARUTO) return false;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        if (joueur.hasRole()) {
            if (joueur.getRole() instanceof IzanagiUser) {
                IzanagiUser izanagiUser = (IzanagiUser) joueur.getRole();
                if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                    if(!izanagiUser.hasUsedIzanagi()){
                        if(joueur.getRole() instanceof NarutoV2Role){
                            NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                            narutoRole.usePower(joueur);
                            narutoRole.usePowerSpecific(joueur, "/ns izanagi");
                        }
                        player.setMaxHealth(player.getMaxHealth() - 2);
                        player.setHealth(player.getMaxHealth());
                        main.getInventoryUtils().giveItemSafely(player, new ItemStack(Material.GOLDEN_APPLE, 5));
                        izanagiUser.setHasUsedIzanagi(true);
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                    }else{
                        player.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                        return false;
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Itachi ou Danzo"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
        return false;
    }

    public interface IzanagiUser{

        boolean hasUsedIzanagi();

        void setHasUsedIzanagi(boolean hasUsedIzanagi);
    }
}
