package fr.lastril.uhchost.modes.naruto.v2.gui.shikigaminomai;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.swords.YariSword;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Konan;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.API.particles.WingsEffect;
import fr.lastril.uhchost.world.tasks.ChunkLoader;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ShikigamiNoMaiGUI extends IQuickInventory {

    private final UhcHost main;
    private final Konan konan;

    public ShikigamiNoMaiGUI(UhcHost main, Konan konan) {
        super("§cShikigami no Mai", 9*1);
        this.main = main;
        this.konan = konan;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);

        inv.setItem(new QuickItem(Material.DIAMOND_SWORD).setName("§cYari")
                .setLore("",
                        "§7Permet de recevoir une épée§c Tranchant 7§7",
                        "§7avec§f 1 utilisation possible§7.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());

            if(joueur.getRoleCooldownYari() <= 0){
                main.getInventoryUtils().giveItemSafely(playerClick, new YariSword().toItemStack());
                if(joueur.getRole() instanceof NarutoV2Role){
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }
                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous avez reçu votre Yari !");
                joueur.setRoleCooldownYari(5*60);
            }else{
                playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownYari()));
            }
            inv.close(playerClick);
        }, 1);

        inv.setItem(new QuickItem(Material.EYE_OF_ENDER).setName("§cBatafurai")
                .setLore("",
                        "§7Permet de vous rendre en§c mode spectateur§7 dans un rayon",
                        "§7de 60 blocs de votre position initiale pendant 45 secondes.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());

            if(joueur.getRoleCooldownBatafurai() <= 0){
                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous êtes désormais en mode spectateur !");
                joueur.setRoleCooldownBatafurai(10*60);
                if(joueur.getRole() instanceof NarutoV2Role){
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }
                playerClick.setGameMode(GameMode.SPECTATOR);
                Location initialLocation = playerClick.getLocation();

                for (PlayerManager joueurs : main.getPlayerManagerAlives()) {
                    if(joueurs.getPlayer() != null) joueurs.getPlayer().hidePlayer(playerClick);
                }

                //main.getInvisibleTeam().addEntry(playerClick.getName());

                new BukkitRunnable() {

                    int timer = Konan.getSpectatorTime()*20;

                    @Override
                    public void run() {
                        ActionBar.sendMessage(playerClick, "§7Batafurai : [" + ChunkLoader.getProgressBar(timer, Konan.getSpectatorTime()*20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                        if(playerClick.getLocation().distance(initialLocation) > 60) playerClick.teleport(initialLocation);

                        if(timer == 0){

                            //main.getInvisibleTeam().removeEntry(playerClick.getName());
                            for (PlayerManager joueurs : main.getPlayerManagerAlives()) {
                                if(joueurs.getPlayer() != null) joueurs.getPlayer().showPlayer(playerClick);
                            }

                            playerClick.setGameMode(GameMode.SURVIVAL);
                            playerClick.teleport(initialLocation);
                            playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous êtes de nouveau visible !");

                            cancel();
                        }
                        timer--;
                    }
                }.runTaskTimer(main, 0, 1);
            }else{
                playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownBatafurai()));
            }
            inv.close(playerClick);
        }, 3);

        inv.setItem(new QuickItem(Material.FEATHER).setName("§cKami Toku")
                .setLore("",
                        "§7Permet de vous envoler pendant§f 8 secondes§7.",
                        "§7Des ailes de§6 feu§7 émaneront de vous.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());

            if(joueur.getRoleCooldownKamiToku() <= 0){
                playerClick.setAllowFlight(true);
                playerClick.setVelocity(playerClick.getVelocity().add(new Vector(0, 2, 0)));
                playerClick.setFlying(true);
                playerClick.setFlySpeed((float) 0.1);
                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous pouvez désormais voler !");
                joueur.setRoleCooldownKamiToku(10*60);

                if(joueur.getRole() instanceof NarutoV2Role){
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }

                new WingsEffect(Konan.getFlyTime()*20, EnumParticle.FLAME).start(playerClick);

                new BukkitRunnable() {

                    int timer = Konan.getFlyTime()*20;

                    @Override
                    public void run() {
                        if(joueur.isAlive() || playerClick.getGameMode() != GameMode.SPECTATOR){
                            ActionBar.sendMessage(playerClick, "§7Kami Toku : [" + ChunkLoader.getProgressBar(timer, Konan.getFlyTime()*20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                            if(timer == 0){
                                playerClick.setFlying(false);
                                playerClick.setAllowFlight(false);
                                cancel();
                            }
                            timer--;
                        } else {
                            cancel();
                        }
                    }
                }.runTaskTimer(main, 0, 1);
            }else{
                playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownKamiToku()));
            }
            inv.close(playerClick);
        }, 5);

        inv.setItem(new QuickItem(Material.FERMENTED_SPIDER_EYE).setName("§cChissoku")
                .setLore("",
                        "§7Permet d'immobiliser un joueur pendant",
                        "§7§f5 secondes§7. Ce dernier aura§a Poison 1§7 en plus.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());

            if(joueur.getRoleCooldownChissoku() <= 0){
                List<Player> players = new ArrayList<>();

                for (Entity entity : playerClick.getNearbyEntities(Konan.getChissokuDistance(), Konan.getChissokuDistance(), Konan.getChissokuDistance())) {
                    if(entity instanceof Player){
                        Player playersNearby = (Player) entity;
                        if(playersNearby.getGameMode() != GameMode.SPECTATOR){
                            players.add(playersNearby);
                        }
                    }
                }

                new ChissokuGUI(main, konan, players).open(playerClick);
            }else{
                playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownChissoku()));
                inv.close(playerClick);
            }
        }, 7);
    }
}
