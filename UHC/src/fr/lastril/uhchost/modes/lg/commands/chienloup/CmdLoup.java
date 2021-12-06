package fr.lastril.uhchost.modes.lg.commands.chienloup;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.village.ChienLoup;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.stream.Collectors;

public class CmdLoup implements ModeSubCommand {

    private final UhcHost main;

    public CmdLoup(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "loup";
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
                chienLoup.setChoosenCamp(Camps.LOUP_GAROU);
                playerManager.setCamps(Camps.LOUP_GAROU);
                chienLoup.setChoosen(true);
                chienLoup.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.NIGHT);
                chienLoup.addEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 1, 0, false, false), When.AT_KILL);
                chienLoup.addEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 1, 0, false, false), When.AT_KILL);

                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous avez décidé de gagner avec les Loups-Garous. " +
                        "Vous avez les effets §cForce I§7 et§f Night Vision§7 la nuit. Vous n'apparaîtrez pas dans la liste des Loups-Garou.");
            }
        }
        return false;
    }
}
