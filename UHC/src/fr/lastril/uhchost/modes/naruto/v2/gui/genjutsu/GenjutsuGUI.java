package fr.lastril.uhchost.modes.naruto.v2.gui.genjutsu;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.gui.genjutsu.izanami.IzanamiGUI;
import fr.lastril.uhchost.modes.naruto.v2.gui.genjutsu.izanami.IzanamiGoalGUI;
import fr.lastril.uhchost.modes.naruto.v2.items.GenjutsuItem;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.KillerBee;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GenjutsuGUI extends IQuickInventory {

    public static final int DISTANCE_TSUKUYOMI = 20;
    public static final int DISTANCE_ATTAQUE = 20;
    public static final int DISTANCE_IZANAMI = 20;

    private final UhcHost main;
    private final GenjutsuItem.GenjutsuUser genjutsuUser;

    public GenjutsuGUI(UhcHost main, GenjutsuItem.GenjutsuUser genjutsuUser) {
        super("§cGenjutsu", 9 * 1);
        this.main = main;
        this.genjutsuUser = genjutsuUser;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);


        inv.setItem(new QuickItem(Material.EYE_OF_ENDER).setName("§cTsukuyomi")
                .setLore("",
                        "§7Permet d'immobiliser tous les joueurs",
                        "§7dans un rayon de 20 blocs. Ces derniers seront invinsibles",
                        "§7pendant 8 secondes.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());
            if(genjutsuUser.getTsukuyomiUses() < 2){
                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                List<Player> players = new ArrayList<>();

                for (Entity entity : inv.getOwner().getNearbyEntities(DISTANCE_TSUKUYOMI, DISTANCE_TSUKUYOMI, DISTANCE_TSUKUYOMI)) {
                    if(entity instanceof Player){
                        Player target = (Player) entity;
                        PlayerManager targetJoueur = main.getPlayerManager(target.getUniqueId());
                        if(target.getGameMode() != GameMode.SPECTATOR && targetJoueur.isAlive()){
                            if(targetJoueur.hasRole()){
                                if(targetJoueur.getRole() instanceof KillerBee){
                                    target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous êtes immunisé de Tsukuyomi. Vous pouvez toujours bouger.");
                                    continue;
                                }
                            }
                            players.add(target);

                            targetJoueur.stun(target.getLocation());
                            //main.shuffled.add(target);
                            target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous venez d'être immobilisé avec Tsukuyomi.");
                        }
                    }
                }
                if (joueur.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }

                genjutsuUser.useTsukuyomi();
                new BukkitRunnable(){

                    @Override
                    public void run() {
                        for (Player player : players) {
                            PlayerManager targetJoueur = main.getPlayerManager(player.getUniqueId());
                            targetJoueur.setStunned(false);
                            //main.shuffled.remove(player);
                        }
                    }
                }.runTaskLater(main, 20*8);
                onClick.getPlayer().closeInventory();
            }else{
                playerClick.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
            }
        }, 2);


        inv.setItem(new QuickItem(Material.ENDER_PEARL).setName("§cAttaque")
                .setLore("",
                        "§7Permet de vous téléporter",
                        "§7dans le dos d'un joueur.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());
            if(joueur.getRoleCooldownAttaque() <= 0){
                List<Player> players = new ArrayList<>();

                for (Entity entity : inv.getOwner().getNearbyEntities(DISTANCE_ATTAQUE, DISTANCE_ATTAQUE, DISTANCE_ATTAQUE)) {
                    if(entity instanceof Player){
                        Player player = (Player) entity;
                        if(player.getGameMode() != GameMode.SPECTATOR){
                            players.add(player);
                        }
                    }
                }

                new AttaqueGUI(main, players).open(playerClick);
            }else{
                playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownAttaque()));
            }
        }, 4);

        if(!genjutsuUser.hasUsedIzanami()){
            inv.setItem(new QuickItem(Material.FERMENTED_SPIDER_EYE).setName("§aIzanami")
                    .setLore("",
                            "§7Permet de cibler un joueur qui,",
                            "§7rejoindra votre camp",
                            "§esous certaines conditions.")
                    .toItemStack(), onClick -> {
                Player playerClick = onClick.getPlayer();

                List<Player> players = new ArrayList<>();

                for (Entity entity : inv.getOwner().getNearbyEntities(DISTANCE_ATTAQUE, DISTANCE_ATTAQUE, DISTANCE_ATTAQUE)) {
                    if(entity instanceof Player){
                        Player player = (Player) entity;
                        if(player.getGameMode() != GameMode.SPECTATOR){
                            players.add(player);
                        }
                    }
                }
                new IzanamiGUI(main, genjutsuUser, players).open(playerClick);

            }, 6);
        } else {
            inv.setItem(new QuickItem(Material.FERMENTED_SPIDER_EYE).setName("§aIzanami")
                    .setLore("",
                            "§7Consulter les§e objectifs nécessaires§7,",
                            "§7afin que le joueur ciblé rejoigne",
                            "§evotre camp.")
                    .toItemStack(), onClick -> {
                if(genjutsuUser.getIzanamiGoal()!= null){
                    new IzanamiGoalGUI(main, genjutsuUser).open(genjutsuUser.getIzanamiGoal().getAuthor().getPlayer());
                }
            }, 6);
        }


    }
}
