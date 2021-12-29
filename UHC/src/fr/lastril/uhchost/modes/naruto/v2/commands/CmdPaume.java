package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.GaiMaito;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Naruto;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.stream.Collectors;

public class CmdPaume implements ModeSubCommand {

    private final UhcHost main;
    private static final int EFFECTS_TIME = 5 * 20;

    public CmdPaume(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "paume";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return false;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        Player player = (Player) sender;
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if (joueur.hasRole() && joueur.isAlive()) {
            if (joueur.getRole() instanceof Naruto) {
                Naruto naruto = (Naruto) joueur.getRole();
                if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                    if (naruto.canUsePaume()) {
                        if (args.length == 2) {
                            String playerName = args[1];
                            Player target = Bukkit.getPlayer(playerName);
                            if (target != null) {
                                if (target != player) {
                                    if (player.getLocation().distance(target.getLocation()) <= 10) {
                                        PlayerManager targetJoueur = main.getPlayerManager(target.getUniqueId());
                                        if (targetJoueur.hasRole()) {
                                            target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, EFFECTS_TIME, 9, false, false));
                                            target.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, EFFECTS_TIME, 1, false, false));
                                            if (targetJoueur.getRole() instanceof GaiMaito) {
                                                GaiMaito gaiMaito = (GaiMaito) targetJoueur.getRole();
                                                if(gaiMaito.isInGaiNuit()){
                                                    gaiMaito.setMustDie(false);
                                                    target.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Naruto a utilisé son paume sur vous donc vous ne mourrez pas.");
                                                }
                                            }
                                            naruto.usePaume();
                                            if(joueur.getRole() instanceof NarutoV2Role){
                                                NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                                                narutoRole.usePower(joueur);
                                                narutoRole.usePowerSpecific(joueur, "/ns paume");
                                            }
                                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous avez utilisé votre paume sur "+target.getName()+" !");
                                        } else {
                                            player.sendMessage(Messages.NOT_INGAME.getMessage());
                                        }
                                    } else {
                                        player.sendMessage(Messages.error("Vous êtes à plus de 10 blocs de " + target.getName() + " !"));
                                    }
                                } else {
                                    player.sendMessage(Messages.error("Vous ne pouvez pas faire votre /paume sur vous !"));
                                }
                            } else {
                                player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
                            }
                        }
                    } else {
                        player.sendMessage(Messages.error("Vous ne pouvez plus utiliser votre paume !"));
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Naruto"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
        return false;
    }
}
