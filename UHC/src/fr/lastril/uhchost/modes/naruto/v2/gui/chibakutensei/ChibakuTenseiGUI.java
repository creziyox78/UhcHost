package fr.lastril.uhchost.modes.naruto.v2.gui.chibakutensei;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.HeadTextures;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.generation.MeteoresGenerator;
import fr.lastril.uhchost.modes.naruto.v2.items.ChibakuTenseiItem;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Konan;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Nagato;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ChibakuTenseiGUI extends IQuickInventory {

    private final UhcHost main;
    private final ChibakuTenseiItem.ChibakuTenseiUser chibakuTenseiUser;

    public ChibakuTenseiGUI(UhcHost main, ChibakuTenseiItem.ChibakuTenseiUser chibakuTenseiUser) {
        super(ChatColor.RED+"Chibaku Tensei", 1*9);
        this.main = main;
        this.chibakuTenseiUser = chibakuTenseiUser;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);

        inv.setItem(new QuickItem(Material.ENDER_PEARL).setName("§cBanshô Ten’in")
                .setLore("",
                        "§7Permet de téléporter un joueur",
                        "§7sur votre position.")
                .toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());
            if (joueur.getRoleCooldownBanshoTenin() == 0) {
                List<Player> players = new ArrayList<>();

                for (Entity entity : playerClick.getNearbyEntities(Konan.getChissokuDistance(), Konan.getChissokuDistance(), Konan.getChissokuDistance())) {
                    if(entity instanceof Player){
                        Player playersNearby = (Player) entity;
                        if(playersNearby.getGameMode() != GameMode.SPECTATOR){
                            players.add(playersNearby);
                        }
                    }
                }

                new BanshoTeninGUI(main, players, chibakuTenseiUser).open(playerClick);
            } else {
                playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownBanshoTenin()));
                inv.close(playerClick);
            }
        }, 2);

        inv.setItem(new QuickItem(Material.EYE_OF_ENDER).setName("§cShinra Tensei")
                .setLore("",
                        "§7Permet de repousser les joueurs",
                        "§7de votre position.").toItemStack(), onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());
            if (joueur.getRoleCooldownShinraTensei() == 0){

                for(Entity entity : playerClick.getNearbyEntities(Nagato.getVelocityDistance(), Nagato.getVelocityDistance(), Nagato.getVelocityDistance())){
                    Location initialLocation = playerClick.getLocation().clone();
                    initialLocation.setPitch(0.0f);
                    Vector origin = initialLocation.toVector();
                    Vector fromPlayerToTarget = entity.getLocation().toVector().clone().subtract(origin);
                    fromPlayerToTarget.multiply(4); //6
                    fromPlayerToTarget.setY(1); // 2
                    entity.setVelocity(fromPlayerToTarget);
                }
                if (joueur.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }
                joueur.setRoleCooldownShinraTensei(5*60);
                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
            } else {
                playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownShinraTensei()));
            }
            inv.close(playerClick);
        }, 4);

        if(chibakuTenseiUser.hasTengaiShinsei() && (!chibakuTenseiUser.hasUsedTengaiShinsei())){
            inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(HeadTextures.METEORE.getHash()).setName("§cTengai Shinsei")
                    .setLore("",
                            "§7Faites apparaître une météorite qui",
                            "§7s'écrase au bout de 10 secondes.").toItemStack(), onClick -> {
                Player playerClick = onClick.getPlayer();
                PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());
                Location location = playerClick.getLocation();
                for(Player players : Bukkit.getOnlinePlayers()){
                    if(players.getWorld() == playerClick.getWorld()){
                        if(players.getLocation().distance(playerClick.getLocation()) <= 50){
                            players.playSound(players.getLocation(), Sound.WITHER_SPAWN, 1f, 1f);
                        }
                    }
                }

                if (joueur.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }

                chibakuTenseiUser.useTengaiShinsei();

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        MeteoresGenerator.spawnMeteore(location, 5, 100);
                    }
                }.runTaskLater(main, 20*6);

                inv.close(playerClick);
            }, 6);
        }
    }
}
