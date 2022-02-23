package fr.lastril.uhchost.modes.bleach.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.bleach.BleachManager;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Kyoraku;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.stream.Collectors;

public class CmdNuit implements ModeSubCommand {

    private final UhcHost main;

    public CmdNuit(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "nuit";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachManager bleachManager = (BleachManager) main.getGamemanager().getModes().getMode().getModeManager();
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if(playerManager.getRole() instanceof Kyoraku){
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            Kyoraku kyoraku = (Kyoraku) playerManager.getRole();
            if(bleachPlayerManager.canUsePower()){
                if(!kyoraku.isUsedNuit()){
                    kyoraku.setUsedNuit(true);
                    main.getPlayerManagerOnlines().forEach(playerManagers -> {
                        Player players = playerManagers.getPlayer();
                        if(playerManager.hasRole() && playerManagers.isAlive()){
                            if(!(playerManagers.getRole() instanceof Kyoraku)){
                                if(players.hasPotionEffect(PotionEffectType.BLINDNESS))
                                    players.removePotionEffect(PotionEffectType.BLINDNESS);
                                players.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*30, 0, false, false));
                            } else {
                                if(player.hasPotionEffect(PotionEffectType.SPEED))
                                    player.removePotionEffect(PotionEffectType.SPEED);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*30, 1, false, false));
                            }
                        }
                    });
                    Bukkit.getScheduler().runTaskLater(main, () -> bleachManager.setNuitNoir(false), 20*30);
                    bleachManager.setNuitNoir(true);
                    Bukkit.broadcastMessage("§9§l");
                    Bukkit.broadcastMessage("§9§lKyoraku : Nuit noir !");
                    Bukkit.broadcastMessage("§9§l");
                }
            }
        }

        return false;
    }
}
