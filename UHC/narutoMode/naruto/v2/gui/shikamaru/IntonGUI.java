package fr.lastril.uhchost.modes.naruto.v2.gui.shikamaru;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.Title;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Shikamaru;
import fr.maygo.uhc.obj.PlayerManager;
import fr.maygo.uhc.task.ChunkLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class IntonGUI extends IQuickInventory {

    private final UhcHost main;
    private final Shikamaru shikamaru;

    public IntonGUI(UhcHost main, Shikamaru shikamaru) {
        super("§6Inton", 9 * 1);
        this.main = main;
        this.shikamaru = shikamaru;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);

        inv.setItem(new QuickItem(Material.OBSIDIAN).setName("§6Manipulation")
                .setLore("",
                        "§7Permet d'immobiliser un PlayerManager pendant 10 secondes.",
                        "§7Vous et le PlayerManager ciblés aurez résistance 2",
                        "§7pendant l'immobilisation.")
                .toItemStack(), onClick -> {
            if (shikamaru.getManipulationUses() >= 3) {
                onClick.getPlayer().sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                return;
            }
            new SelectPlayersGUI(main, shikamaru, onSelect -> {
                Player shikamaruPlayer = onSelect.getShikamaru(), targetPlayer = onSelect.getTarget();
                PlayerManager PlayerManager = main.getPlayerManager(shikamaruPlayer.getUniqueId()), targetPlayerManager = main.getPlayerManager(targetPlayer.getUniqueId());

                shikamaru.addManipulationUses();
                shikamaru.useTechnique(shikamaruPlayer);
                PlayerManager.stun(shikamaruPlayer.getLocation());
                targetPlayerManager.stun(targetPlayer.getLocation());
                shikamaruPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                targetPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous êtes immobilisé par la manipulation des ombres.");
                shikamaruPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Shikamaru.STUN_TIME * 20, 1, false, false));
                targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Shikamaru.STUN_TIME * 20, 1, false, false));

                if (PlayerManager.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                    narutoRole.usePower(PlayerManager);
                }

                new BukkitRunnable() {

                    int timer = Shikamaru.STUN_TIME * 20;

                    @Override
                    public void run() {
                        ActionBar.sendMessage(shikamaruPlayer, "§7Manipulation : [" + ChunkLoader.getProgressBar(timer, Shikamaru.STUN_TIME * 20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                        if (timer == 0) {
                            PlayerManager.setStunned(false);
                            targetPlayerManager.setStunned(false);
                            cancel();
                        }
                        timer--;
                    }
                }.runTaskTimer(main, 0, 1);
                inv.close(onClick.getPlayer());
            }).open(onClick.getPlayer());
        }, 1);

        inv.setItem(new QuickItem(Material.IRON_SWORD).setName("§6Etreinte Mortelle")
                .setLore("",
                        "§7Permet d'immobiliser un PlayerManager pendant 10 secondes.",
                        "§7Vous et le PlayerManager ciblés aurez§f Résistance 2",
                        "§7pendant l'immobilisation. Le PlayerManager ciblé",
                        "aura en plus§a Poison 2§7 pendant l'immobilisation.")
                .toItemStack(), onClick -> {
            if (shikamaru.hasUsedEtreinteMortelle()) {
                onClick.getPlayer().sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                return;
            }
            new SelectPlayersGUI(main, shikamaru, onSelect -> {
                Player shikamaruPlayer = onSelect.getShikamaru(), targetPlayer = onSelect.getTarget();
                PlayerManager PlayerManager = main.getPlayerManager(shikamaruPlayer.getUniqueId()), targetPlayerManager = main.getPlayerManager(targetPlayer.getUniqueId());

                shikamaru.setUsedEtreinteMortelle(true);
                shikamaru.useTechnique(shikamaruPlayer);
                PlayerManager.stun(shikamaruPlayer.getLocation());
                targetPlayerManager.stun(targetPlayer.getLocation());
                shikamaruPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                targetPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous êtes immobilisé et asphyxié par l'étreinte mortelle.");
                shikamaruPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Shikamaru.STUN_TIME * 20, 1, false, false));
                targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Shikamaru.STUN_TIME * 20, 1, false, false));
                targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.POISON, Shikamaru.STUN_TIME * 20, 1, false, false));

                if (PlayerManager.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                    narutoRole.usePower(PlayerManager);
                }

                new BukkitRunnable() {

                    int timer = Shikamaru.STUN_TIME * 20;

                    @Override
                    public void run() {
                        ActionBar.sendMessage(shikamaruPlayer, "§7Etreinte Mortelle : [" + ChunkLoader.getProgressBar(timer, Shikamaru.STUN_TIME * 20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                        if (timer == 0) {
                            PlayerManager.setStunned(false);
                            targetPlayerManager.setStunned(false);
                            cancel();
                        }
                        timer--;
                    }
                }.runTaskTimer(main, 0, 1);
                inv.close(onClick.getPlayer());
            }).open(onClick.getPlayer());
        }, 3);

        inv.setItem(new QuickItem(Material.EYE_OF_ENDER).setName("§6Oeil")
                .setLore("",
                        "§7Permet de voir la vie d'un PlayerManager.")
                .toItemStack(), onClick -> {
            if (shikamaru.getInOeil().size() >= 5) {
                onClick.getPlayer().sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                return;
            }
            new SelectPlayersGUI(main, shikamaru, onSelect -> {
                Player shikamaruPlayer = onSelect.getShikamaru(), targetPlayer = onSelect.getTarget();
                PlayerManager PlayerManager = main.getPlayerManager(shikamaruPlayer.getUniqueId());
                if (shikamaru.getInOeil().containsKey(targetPlayer.getUniqueId())) {
                    shikamaruPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cCe PlayerManager est déjà sous l'emprise de votre oeil.");
                    return;
                }
                shikamaru.oeil(shikamaruPlayer, targetPlayer);
                shikamaruPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                if (PlayerManager.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                    narutoRole.usePower(PlayerManager);
                }
                inv.close(onClick.getPlayer());
            }).open(onClick.getPlayer());
        }, 5);

        inv.setItem(new QuickItem(Material.FERMENTED_SPIDER_EYE).setName("§6Recherche")
                .setLore("",
                        "§7Permet de voir si le PlayerManager ciblé à",
                        "§ftué quelqu'un§7, et si c'est le cas",
                        "§fl'identité des PlayerManagers tués.")
                .toItemStack(), onClick -> {
            if (shikamaru.hasUsedRecherche()) {
                onClick.getPlayer().sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                return;
            }
            new SelectPlayersGUI(main, shikamaru, onSelect -> {
                Player shikamaruPlayer = onSelect.getShikamaru(), targetPlayer = onSelect.getTarget();
                PlayerManager PlayerManager = main.getPlayerManager(shikamaruPlayer.getUniqueId()), targetPlayerManager = main.getPlayerManager(targetPlayer.getUniqueId());

                if (targetPlayerManager.getKills().isEmpty()) {
                    //N'A PAS FAIT DE KILLS
                    shikamaruPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§c" + targetPlayer.getName() + " n'a tué personne.");
                } else {
                    shikamaruPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + targetPlayer.getName() + " à tué : ");
                    for (UUID kill : targetPlayerManager.getKills()) {
                        shikamaruPlayer.sendMessage("§6- " + Bukkit.getOfflinePlayer(kill).getName());
                    }
                }

                shikamaru.setUsedRecherche(true);
                shikamaru.useTechnique(shikamaruPlayer);
                if (PlayerManager.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                    narutoRole.usePower(PlayerManager);
                }
                inv.close(onClick.getPlayer());
            }).open(onClick.getPlayer());
        }, 7);
    }
}
