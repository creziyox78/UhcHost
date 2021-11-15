package fr.lastril.uhchost.modes.naruto.v2.commands;

;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.GaiMaito;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Naruto;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.stream.Collectors;

public class CmdPaume implements ModeSubCommand {

    private static final int EFFECTS_TIME = 5 * 20;
    private final UhcHost main;

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
        Player player = (Player) sender;
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.hasRole() && PlayerManager.isAlive()) {
            if (PlayerManager.getRole() instanceof Naruto) {
                Naruto naruto = (Naruto) PlayerManager.getRole();
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (naruto.canUsePaume()) {
                        if (args.length == 2) {
                            String playerName = args[1];
                            Player target = Bukkit.getPlayer(playerName);
                            if (target != null) {
                                if (target != player) {
                                    if (player.getLocation().distance(target.getLocation()) <= 10) {
                                        PlayerManager targetPlayerManager = main.getPlayerManager(target.getUniqueId());
                                        if (targetPlayerManager.hasRole()) {
                                            target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, EFFECTS_TIME, 9, false, false));
                                            target.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, EFFECTS_TIME, 1, false, false));
                                            if (targetPlayerManager.getRole() instanceof GaiMaito) {
                                                GaiMaito gaiMaito = (GaiMaito) targetPlayerManager.getRole();
                                                if (gaiMaito.isInGaiNuit()) {
                                                    gaiMaito.setMustDie(false);
                                                    target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Naruto a utilisé son paume sur vous donc vous ne mourrez pas.");
                                                }
                                            }
                                            naruto.usePaume();
                                            if (PlayerManager.getRole() instanceof NarutoV2Role) {
                                                NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                                                narutoRole.usePower(PlayerManager);
                                            }
                                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez utilisé votre paume sur " + target.getName() + " !");
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
