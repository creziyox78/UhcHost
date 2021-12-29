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
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CmdSharingan implements ModeSubCommand {

    private final UhcHost main;

    public CmdSharingan(UhcHost main) {
        this.main = main;
    }


    @Override
    public String getSubCommandName() {
        return "sharingan";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return false;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        Player player = (Player) sender;
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(!joueur.isAlive()){
            player.sendMessage(Messages.error("Vous n'êtes plus vivant."));
            return false;
        }
        if (joueur.hasRole()) {
            if (joueur.getRole() instanceof SharinganUser) {
                SharinganUser sharinganUser = (SharinganUser) joueur.getRole();
                if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                    if (args.length == 2) {
                        String playerName = args[1];
                        Player target = Bukkit.getPlayer(playerName);
                        if (target != null) {
                            PlayerManager targetJoueur = main.getPlayerManager(target.getUniqueId());
                            if (targetJoueur.hasRole()) {
                                if(sharinganUser.getSharinganUses() < 2){
                                    String role = targetJoueur.getRole().getCamp().getCompoColor()+targetJoueur.getRole().getRoleName();
                                    int gapples = countItemsInInventory(target.getInventory(), Material.GOLDEN_APPLE);
                                    List<UUID> kills = targetJoueur.getKills();

                                    player.sendMessage("§8[§c§lSharingan§8] §7"+target.getName()+" est "+role);
                                    if(kills.isEmpty()){
                                        player.sendMessage("§8[§c§lSharingan§8] §7"+target.getName()+" n'a tué personne.");
                                    }else{
                                        player.sendMessage("§8[§c§lSharingan§8] §7"+target.getName()+" à tué "+kills.size()+" personnes :");
                                        kills.forEach(killId -> {
                                            String killName = Bukkit.getOfflinePlayer(killId).getName();
                                            player.sendMessage("§7- "+killName);
                                        });
                                    }
                                    player.sendMessage("§8[§c§lSharingan§8] §7"+target.getName()+" a "+gapples+" pommes d'or.");
                                    sharinganUser.addSharinganUse();
                                    if(joueur.getRole() instanceof NarutoV2Role){
                                        NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                                        narutoRole.usePower(joueur);
                                        narutoRole.usePowerSpecific(joueur, "/ns sharingan");
                                    }
                                }else{
                                    player.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                                    return false;
                                }
                            } else {
                                player.sendMessage(Messages.NOT_INGAME.getMessage());
                            }
                        } else {
                            player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
                        }
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Itachi ou Nagato"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
        return false;
    }


    private int countItemsInInventory(PlayerInventory inventory, Material material){
        int count = 0;
        for(ItemStack item : inventory.getContents()) {
            if(item != null) {
                if(item.getType() == material) {
                    count += item.getAmount();
                }
            }
        }
        return count;
    }

    public interface SharinganUser{

        int getSharinganUses();
        void addSharinganUse();

    }
}
