package fr.lastril.uhchost.modes.bleach.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Kyoraku;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CmdDuel implements ModeSubCommand {

    private final UhcHost main;

    private final List<Integer> integers = Arrays.asList(1, 2, 3);

    public CmdDuel(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "duel";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if(playerManager.getRole() instanceof Kyoraku){
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            Kyoraku kyoraku = (Kyoraku) playerManager.getRole();
            if(bleachPlayerManager.canUsePower()){
                if(args.length == 3){
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);
                    if(target != null && target != player){
                        if(target.getWorld() == player.getWorld()){
                            if(target.getLocation().distance(player.getLocation()) <= 30){
                                PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
                                if(targetManager.isAlive()){
                                    if(ClassUtils.isInt(args[2])){
                                        int number = Integer.parseInt(args[2]);
                                        if(integers.contains(number)){
                                            kyoraku.getKyorakuDuelManager().setRulesFight(number);
                                            kyoraku.getKyorakuDuelManager().generateArena(player.getLocation().add(0, 80, 0));
                                            kyoraku.getKyorakuDuelManager().teleportPlayersInArena(player, target);
                                        } else {
                                            player.sendMessage(Messages.error("Veuillez choisir un nombre entre <1|2|3>."));
                                        }
                                    }
                                } else {
                                    player.sendMessage(Messages.error("Le joueur ciblé n'est pas en vie !"));
                                }
                            } else {
                                player.sendMessage(Messages.error("Ce joueur se trouve à plus de 30 blocs !"));
                            }
                        } else {
                            player.sendMessage(Messages.error("Ce joueur se trouve à plus de 30 blocs !"));
                        }
                    } else {
                        player.sendMessage(Messages.error("La cible est incorrecte !"));
                    }
                } else {
                    player.sendMessage(Messages.use("/b duel <pseudo> <1|2|3>"));
                }
            } else {
                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
            }


        }
        return false;
    }
}
