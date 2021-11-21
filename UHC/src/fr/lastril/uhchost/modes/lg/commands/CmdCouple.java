package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.roles.village.Cupidon;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdCouple implements ModeSubCommand {

    private final UhcHost pl;

    public CmdCouple(UhcHost pl) {
        this.pl = pl;
    }


    @Override
    public String getSubCommandName() {
        return "couple";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if (pl.gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager) {
            LoupGarouManager loupGarouManager = (LoupGarouManager) pl.gameManager.getModes().getMode().getModeManager();
            if (playerManager.getRole() instanceof Cupidon) {
                Cupidon cupidon = (Cupidon) playerManager.getRole();
                if (!cupidon.isUsedPower() && !loupGarouManager.isRandomCouple()) {
                    if (args.length == 3) {
                        String targetName1 = args[1];
                        Player target1 = Bukkit.getPlayer(targetName1);
                        String targetName2 = args[2];
                        Player target2 = Bukkit.getPlayer(targetName2);
                        if (target1 == player || target2 == player) {
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cJe sais que t'es en manque d'affection mais tu ne peux pas être en couple ici aussi ! Force à toi.");
                            return false;
                        }
                        if (target1 != null && target2 != null) {
                            PlayerManager targetManager1 = pl.getPlayerManager(target2.getUniqueId());
                            PlayerManager targetManager2 = pl.getPlayerManager(target1.getUniqueId());
                            if (targetManager1.isAlive() && targetManager1.hasRole() && targetManager2.isAlive() && targetManager2.hasRole()) {
                                targetManager1.setCamps(Camps.COUPLE);
                                targetManager2.getWolfPlayerManager().setOtherCouple(target1.getUniqueId());
                                targetManager1.getWolfPlayerManager().setOtherCouple(target2.getUniqueId());
                                targetManager2.setCamps(Camps.COUPLE);
                                target1.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() +
                                        "§dLe cupidon vient de vous lié d'amour avec " + target2.getName()
                                        + ". Si l'un d'entre vous vient à mourir, l'autre mourra alors par amour pour l'autre.");
                                target2.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() +
                                        "§dLe cupidon vient de vous lié d'amour avec " + target1.getName()
                                        + ". Si l'un d'entre vous vient à mourir, l'autre mourra alors par amour pour l'autre.");
                                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aVos flèches ont atteints le coeur de " + targetName1 + " et " + targetName2 + ". Désormais, si l'un d'eux viennent à mourir, l'autre mourra également.");
                                cupidon.setUsedPower(true);
                            }
                        }
                    }

                } else {
                    player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVos flèches ont déjà été tirés !");
                }
            }
        }
        return false;
    }
}
