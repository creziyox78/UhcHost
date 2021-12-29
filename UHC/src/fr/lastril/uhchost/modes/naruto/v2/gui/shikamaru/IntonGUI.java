package fr.lastril.uhchost.modes.naruto.v2.gui.shikamaru;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Shikamaru;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.tasks.ChunkLoader;
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
        super("§6Inton", 9*1);
        this.main = main;
        this.shikamaru = shikamaru;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);

        inv.setItem(new QuickItem(Material.OBSIDIAN).setName("§6Manipulation")
                .setLore("",
                        "§7Permet d'immobiliser un joueur pendant 10 secondes.",
                        "§7Vous et le joueur ciblés aurez résistance 2",
                        "§7pendant l'immobilisation.")
                .toItemStack(), onClick -> {
            if(shikamaru.getManipulationUses() >= 3){
                onClick.getPlayer().sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                return;
            }
            new SelectPlayersGUI(main, shikamaru, onSelect -> {
                Player shikamaruPlayer = onSelect.getShikamaru(), targetPlayer = onSelect.getTarget();
                PlayerManager joueur = main.getPlayerManager(shikamaruPlayer.getUniqueId()), targetJoueur = main.getPlayerManager(targetPlayer.getUniqueId());

                shikamaru.addManipulationUses();
                shikamaru.useTechnique(shikamaruPlayer);
                joueur.stun(shikamaruPlayer.getLocation());
                targetJoueur.stun(targetPlayer.getLocation());
                shikamaruPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage()+Messages.USED_POWER.getMessage());
                targetPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous êtes immobilisé par la manipulation des ombres.");
                shikamaruPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Shikamaru.STUN_TIME*20, 1, false, false));
                targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Shikamaru.STUN_TIME*20, 1, false, false));

                if(joueur.getRole() instanceof NarutoV2Role){
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }

                new BukkitRunnable() {

                    int timer = Shikamaru.STUN_TIME*20;

                    @Override
                    public void run() {
                        ActionBar.sendMessage(shikamaruPlayer, "§7Manipulation : [" + ChunkLoader.getProgressBar(timer, Shikamaru.STUN_TIME*20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                        if(timer == 0){
                            joueur.setStunned(false);
                            targetJoueur.setStunned(false);
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
                        "§7Permet d'immobiliser un joueur pendant 10 secondes.",
                        "§7Vous et le joueur ciblés aurez§f Résistance 2",
                        "§7pendant l'immobilisation. Le joueur ciblé",
                        "aura en plus§a Poison 2§7 pendant l'immobilisation.")
                .toItemStack(), onClick -> {
            if(shikamaru.hasUsedEtreinteMortelle()){
                onClick.getPlayer().sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                return;
            }
            new SelectPlayersGUI(main, shikamaru, onSelect -> {
                Player shikamaruPlayer = onSelect.getShikamaru(), targetPlayer = onSelect.getTarget();
                PlayerManager joueur = main.getPlayerManager(shikamaruPlayer.getUniqueId()), targetJoueur = main.getPlayerManager(targetPlayer.getUniqueId());

                shikamaru.setUsedEtreinteMortelle(true);
                shikamaru.useTechnique(shikamaruPlayer);
                joueur.stun(shikamaruPlayer.getLocation());
                targetJoueur.stun(targetPlayer.getLocation());
                shikamaruPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage()+Messages.USED_POWER.getMessage());
                targetPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous êtes immobilisé et asphyxié par l'étreinte mortelle.");
                shikamaruPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Shikamaru.STUN_TIME*20, 1, false, false));
                targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Shikamaru.STUN_TIME*20, 1, false, false));
                targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.POISON, Shikamaru.STUN_TIME*20, 1, false, false));

                if(joueur.getRole() instanceof NarutoV2Role){
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }

                new BukkitRunnable() {

                    int timer = Shikamaru.STUN_TIME*20;

                    @Override
                    public void run() {
                        ActionBar.sendMessage(shikamaruPlayer, "§7Etreinte Mortelle : [" + ChunkLoader.getProgressBar(timer, Shikamaru.STUN_TIME*20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                        if(timer == 0){
                            joueur.setStunned(false);
                            targetJoueur.setStunned(false);
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
                        "§7Permet de voir la vie d'un joueur.")
                .toItemStack(), onClick -> {
            if(shikamaru.getInOeil().size() >= 5){
                onClick.getPlayer().sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                return;
            }
            new SelectPlayersGUI(main, shikamaru, onSelect -> {
                Player shikamaruPlayer = onSelect.getShikamaru(), targetPlayer = onSelect.getTarget();
                PlayerManager joueur = main.getPlayerManager(shikamaruPlayer.getUniqueId());
                if(shikamaru.getInOeil().containsKey(targetPlayer.getUniqueId())){
                    shikamaruPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cCe joueur est déjà sous l'emprise de votre oeil.");
                    return;
                }
                shikamaru.oeil(shikamaruPlayer, targetPlayer);
                shikamaruPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage()+Messages.USED_POWER.getMessage());
                if(joueur.getRole() instanceof NarutoV2Role){
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }
                inv.close(onClick.getPlayer());
            }).open(onClick.getPlayer());
        }, 5);

        inv.setItem(new QuickItem(Material.FERMENTED_SPIDER_EYE).setName("§6Recherche")
                .setLore("",
                        "§7Permet de voir si le joueur ciblé à",
                        "§ftué quelqu'un§7, et si c'est le cas",
                        "§fl'identité des joueurs tués.")
                .toItemStack(), onClick -> {
            if(shikamaru.hasUsedRecherche()){
                onClick.getPlayer().sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                return;
            }
            new SelectPlayersGUI(main, shikamaru, onSelect -> {
                Player shikamaruPlayer = onSelect.getShikamaru(), targetPlayer = onSelect.getTarget();
                PlayerManager joueur = main.getPlayerManager(shikamaruPlayer.getUniqueId()), targetJoueur = main.getPlayerManager(targetPlayer.getUniqueId());

                if(targetJoueur.getKills().isEmpty()){
                    //N'A PAS FAIT DE KILLS
                    shikamaruPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"§c"+targetPlayer.getName()+" n'a tué personne.");
                }else {
                    shikamaruPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage()+targetPlayer.getName()+" à tué : ");
                    for (UUID kill : targetJoueur.getKills()) {
                        shikamaruPlayer.sendMessage("§6- "+ Bukkit.getOfflinePlayer(kill).getName());
                    }
                }

                shikamaru.setUsedRecherche(true);
                shikamaru.useTechnique(shikamaruPlayer);
                if(joueur.getRole() instanceof NarutoV2Role){
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }
                inv.close(onClick.getPlayer());
            }).open(onClick.getPlayer());
        }, 7);
    }
}
