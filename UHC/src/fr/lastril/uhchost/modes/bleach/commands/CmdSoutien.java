package fr.lastril.uhchost.modes.bleach.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.KenpachiZaraki;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Yachiru;
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

public class CmdSoutien implements ModeSubCommand {

    private final UhcHost main;

    public CmdSoutien(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "soutien";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if(playerManager.getRole() instanceof Yachiru){
            Yachiru yachiru = (Yachiru) playerManager.getRole();
            if(yachiru.isCloseToKenpachiZaraki(playerManager)){
                if(bleachPlayerManager.canUsePower()){
                    if(playerManager.getRoleCooldownSoutien() <= 0){
                        PlayerManager targetManager = yachiru.getKenPachiZaraki(playerManager, true);
                        KenpachiZaraki kenpachiZaraki = (KenpachiZaraki) targetManager.getRole();
                        if(kenpachiZaraki.didntGetDamageIn5Seconds()){
                            Player target = targetManager.getPlayer();
                            if(target != null){
                                if(target.hasPotionEffect(PotionEffectType.REGENERATION))
                                    target.removePotionEffect(PotionEffectType.REGENERATION);
                                target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*30, 1));
                                playerManager.setRoleCooldownSoutien(10*60);
                                target.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§9Yachiru§d vous apporte son soutien et vous faire regagner de la force !");
                                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§9Le soutien que vous apporter à Kenpachi Zaraki lui fait récupérer des forces !");
                            } else {
                                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§9Kenpachi Zaraki§c n'est pas connecté !");
                            }
                        } else {
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§9Kenpachi Zaraki§c a reçu des dégâts ces 5 dernières secondes. Il ne vous écoutera pas !");
                        }
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownSoutien()));
                    }


                } else{
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            } else {
                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous n'êtes pas à 15 blocs de Kenpachi Zaraki.");
            }
        } else {
            player.sendMessage(Messages.not("Yachiru"));
        }
        return false;
    }
}
