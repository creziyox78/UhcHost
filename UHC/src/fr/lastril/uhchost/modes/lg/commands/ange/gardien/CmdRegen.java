package fr.lastril.uhchost.modes.lg.commands.ange.gardien;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.solo.Ange;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.WolfPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.stream.Collectors;

public class CmdRegen implements ModeSubCommand {

    private final UhcHost main;

    public CmdRegen(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "regen";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        WolfPlayerManager wolfPlayerManager = playerManager.getWolfPlayerManager();
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if(playerManager.getRole() instanceof Ange){
            Ange ange = (Ange) playerManager.getRole();
            if(ange.getForm() == Ange.Form.GARDIEN && !ange.hasRegen()){
                if(wolfPlayerManager.isSarbacaned()){
                    player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cLa sarcabane de l'Indigène vous empêche d'utiliser votre pouvoir !");
                    return false;
                }
                Player target = ange.getCible().getPlayer();
                if(target != null){
                    if(target.hasPotionEffect(PotionEffectType.REGENERATION))
                        target.removePotionEffect(PotionEffectType.REGENERATION);
                    target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*60, 0));
                    ange.setRegen(true);
                    target.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§L'Ange Gardien vient de vous donner sa bénédiction.");

                }
            }
        }
        return false;
    }
}
