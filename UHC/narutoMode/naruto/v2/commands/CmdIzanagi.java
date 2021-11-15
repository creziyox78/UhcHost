package fr.lastril.uhchost.modes.naruto.v2.commands;

;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.obj.PlayerManager;
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
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (!PlayerManager.isAlive()) {
            player.sendMessage(Messages.error("Vous n'êtes plus vivant."));
            return false;
        }
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof IzanagiUser) {
                IzanagiUser izanagiUser = (IzanagiUser) PlayerManager.getRole();
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (!izanagiUser.hasUsedIzanagi()) {
                        player.setMaxHealth(player.getMaxHealth() - 2);
                        player.setHealth(player.getMaxHealth());
                        main.getInventoryUtils().giveItemSafely(player, new ItemStack(Material.GOLDEN_APPLE, 5));
                        izanagiUser.setHasUsedIzanagi(true);
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                    } else {
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

    public interface IzanagiUser {

        boolean hasUsedIzanagi();

        void setHasUsedIzanagi(boolean hasUsedIzanagi);
    }
}
