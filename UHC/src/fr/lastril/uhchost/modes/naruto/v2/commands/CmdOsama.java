package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Nagato;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.tasks.ChunkLoader;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CmdOsama implements ModeSubCommand {

    private final UhcHost main;

    public CmdOsama(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "osama";
    }

    @Override
    public List<String> getSubArgs() {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return false;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        Player player = (Player) sender;
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());

        if (joueur.hasRole() && joueur.isAlive()) {
            if (joueur.getRole() instanceof Nagato) {
                Nagato nagato = (Nagato) joueur.getRole();
                if(!nagato.isUseOsama()){
                    if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                        List<PlayerManager> deathsLast10Minutes = main.getAllPlayerManager().values().stream()
                                .filter(PlayerManager::isDead)
                                .filter(PlayerManager::isOnline)
                                .filter(joueur1 -> {
                                    long delay = System.currentTimeMillis() - joueur1.getDeathTime();
                                    return delay <= 10*60*1000L;
                                })
                                .collect(Collectors.toList());

                        if(!deathsLast10Minutes.isEmpty()){
                            nagato.setUseOsama(true);
                            for (PlayerManager revive : deathsLast10Minutes) {
                                Player revivePlayer = revive.getPlayer();
                                revivePlayer.setGameMode(GameMode.SURVIVAL);
                                revivePlayer.teleport(player.getLocation());
                                revive.setAlive(true);
                                //revive.setSpectator(false);
                                //main.shuffled.add(revivePlayer);
                                //main.getSpectatorTeam().removeEntry(revivePlayer.getName());
                                giveStuff(revivePlayer);
                                revive.setAlive(true);

                               /* main.getMumbleManager().onPlayerRevive(revive);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        main.shuffled.remove(revivePlayer);
                                        if(revive.getMumbleData().getMumbleUser() != null) {
                                            revive.getMumbleData().getMumbleUser().mute(false);
                                            Main.debug("Mumble Manager: demuted player");
                                        }
                                    }
                                }.runTaskLater(main, 40);*/
                            }

                            new BukkitRunnable() {

                                int timer = 60 * 20;

                                @Override
                                public void run() {
                                    ActionBar.sendMessage(player, "§7Osama : [" + ChunkLoader.getProgressBar(timer, 60 * 20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                                    if (timer == 0) {
                                        player.damage(100D);
                                        cancel();
                                    }
                                    timer--;
                                }
                            }.runTaskTimer(main, 0, 1);
                            nagato.usePower(joueur);
                            nagato.usePowerSpecific(joueur, "/ns osama");
                        }else{
                            player.sendMessage(Messages.error("Il n'y a personne à réssuciter !"));
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return false;
                    }
                } else {
                    player.sendMessage(Messages.error("Vous ne pouvez plus utilisez votre pouvoir."));
                }

            } else {
                player.sendMessage(Messages.not("Nagato"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle ou n'êtes pas en vie !"));
            return false;
        }
        return false;
    }

    private void giveStuff(Player player){
        player.getInventory().clear();
        player.getInventory().addItem(new QuickItem(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1, true).toItemStack());
        player.getInventory().addItem(new QuickItem(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 1, true).toItemStack());
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 3));
        player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
        player.getInventory().addItem(new ItemStack(Material.LOG, 64));
        player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
        player.getInventory().setHelmet(new QuickItem(Material.IRON_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).toItemStack());
        player.getInventory().setChestplate(new QuickItem(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).toItemStack());
        player.getInventory().setLeggings(new QuickItem(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).toItemStack());
        player.getInventory().setBoots(new QuickItem(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).toItemStack());

        PlayerManager joueur = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
        if(joueur.hasRole()){
            joueur.getRole().stuff(player);
        }

    }
}
