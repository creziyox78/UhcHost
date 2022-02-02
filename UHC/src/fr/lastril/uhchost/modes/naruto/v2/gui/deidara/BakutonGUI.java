package fr.lastril.uhchost.modes.naruto.v2.gui.deidara;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Deidara;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Danzo;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.API.particles.DoubleCircleEffect;
import fr.lastril.uhchost.world.WorldUtils;
import fr.lastril.uhchost.world.tasks.ChunkLoader;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BakutonGUI extends IQuickInventory {

    private final UhcHost main;
    private final Deidara deidara;
    private NarutoV2Manager narutoV2Manager;

    public BakutonGUI(UhcHost main, Deidara deidara) {
        super("§cBakuton", 9*1);
        this.main = main;
        this.deidara = deidara;
        if(main.getGamemanager().getModes() != Modes.NARUTO) return;
        narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);

        inv.setItem(new QuickItem(Material.ARROW).setName("§cC1")
                .setLore("",
                        "§7Vos flèches explosent à l'impact.")
                .toItemStack(), onClick -> {
            deidara.setBowMode(Deidara.BowMode.C1);
            onClick.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVotre arc est passé en C1.");
            inv.close(onClick.getPlayer());
        }, 1);

        inv.setItem(new QuickItem(Material.BONE).setName("§cC2")
                .setLore("",
                        "§7Fais apparaître un chien qui",
                        "§7explosera au contact du joueur ciblé.")
                .toItemStack(), onClick -> {
            new C2GUI(main, deidara).open(onClick.getPlayer());
        }, 3);

        inv.setItem(new QuickItem(Material.SULPHUR).setName("§cC3")
                .setLore("",
                        "§7Fais apparaître une pluie de tnt",
                        "§7à l'impact de votre flèche.")
                .toItemStack(), onClick -> {
            deidara.setBowMode(Deidara.BowMode.C3);
            onClick.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVotre arc est passé en C3.");
            inv.close(onClick.getPlayer());
        }, 5);

        inv.setItem(new QuickItem(Material.TNT).setName("§cL'Art Ultime")
                .setLore("",
                        "§7Permet de créer une énorme explosion en vous sucidant.",
                        "§7Vous ne pourrez pas bouger pendant 10 secondes.",
                        "",
                        "§c§lEn cliquant dessus, cela activera instantanément votre pouvoir.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());
            onClick.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous venez de déclanché votre Art Ultime. Vous ne pouvez pas bouger pendant son activation.");

            new DoubleCircleEffect(20*10, EnumParticle.SMOKE_NORMAL).start(playerClick);


            joueur.stun(playerClick.getLocation());
            inv.close(playerClick);
            playerClick.setItemInHand(null);
            deidara.setArtUltime(true);
            deidara.setCancelArtUltimeExplosion(false);
            for(Player players : Bukkit.getOnlinePlayers()){
                if(players.getWorld() == playerClick.getWorld()){
                    if(players.getLocation().distance(playerClick.getLocation()) <= 100){
                        players.playSound(players.getLocation(), Sound.WITHER_SPAWN, 1f, 1f);
                    }
                }
            }

            if (joueur.getRole() instanceof NarutoV2Role) {
                NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                narutoRole.usePower(joueur);
            }
            new BukkitRunnable() {

                int timer = 10*20;

                @Override
                public void run() {
                    ActionBar.sendMessage(playerClick, "§7Explosion : [" + ChunkLoader.getProgressBar(timer, 10*20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                    if(timer == 0){

                        joueur.setStunned(false);

                        if(!deidara.isCancelArtUltimeExplosion()){
                            playerClick.damage(100D);
                            if(playerClick.getWorld() != narutoV2Manager.getKamuiWorld()) WorldUtils.createBeautyExplosion(playerClick.getLocation(), 30);
                            for(Player players : Bukkit.getOnlinePlayers()){
                                PlayerManager targetJoueur = main.getPlayerManager(players.getUniqueId());
                                if(players.getWorld() == playerClick.getWorld()){
                                    if(players.getGameMode() != GameMode.SPECTATOR && targetJoueur.isAlive()){
                                        if(players.getLocation().distance(playerClick.getLocation()) <= 30){
                                            if(targetJoueur.getRole() instanceof Danzo){
                                                Danzo danzo = (Danzo) targetJoueur.getRole();
                                                danzo.useVie(targetJoueur, 100);
                                            } else {
                                                players.damage(100D);
                                            }

                                        }
                                    }
                                }
                            }
                        }
                        cancel();
                    }
                    timer--;
                }
            }.runTaskTimer(main, 0, 1);

        }, 7);
    }
}
