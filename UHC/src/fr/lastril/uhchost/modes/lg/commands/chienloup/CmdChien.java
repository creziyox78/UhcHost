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

public class CmdChien implements ModeSubCommand {

    private final UhcHost main;

    public CmdChien(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "chien";
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
                chienLoup.setChoosenCamp(Camps.VILLAGEOIS);
                playerManager.setCamps(Camps.VILLAGEOIS);
                chienLoup.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.NIGHT);
                chienLoup.addEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 1, 0, false, false), When.AT_KILL);
                chienLoup.addEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 1, 0, false, false), When.AT_KILL);
                chienLoup.setChoosen(true);
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aVous avez décidé de gagner avec les villageois. " +
                        "Vous avez donc l'effet Force 1 la nuit, serez vu comme un LG par les rôles à informations et apparaîtrés dans la liste des LG.");
            }
        }
        return false;
    }
}
