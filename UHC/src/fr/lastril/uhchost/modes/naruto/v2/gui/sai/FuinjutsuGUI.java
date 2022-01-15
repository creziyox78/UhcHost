package fr.lastril.uhchost.modes.naruto.v2.gui.sai;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Sai;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.tasks.ChunkLoader;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class FuinjutsuGUI extends IQuickInventory {

    private final UhcHost main;
    private final List<Player> players;

    public FuinjutsuGUI(UhcHost main, List<Player> players) {
        super(ChatColor.GOLD+"Fûinjutsu", 9*6);
        this.main = main;
        this.players = players;
    }

    @Override
    public void contents(QuickInventory inv) {

        for (Player player : players) {
            inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(player.getName()).setSkullOwner(player.getName()).toItemStack(), onClick -> {
                Player playerClick = onClick.getPlayer();
                if(playerClick.getLocation().distance(player.getLocation()) > ToileGUI.DISTANCE_FUINJUTSU){
                    playerClick.sendMessage(Messages.error(player.getName()+" n'est pas à "+ToileGUI.DISTANCE_FUINJUTSU+" blocs de vous !"));
                    inv.close(playerClick);
                    return;
                }
                playerClick.closeInventory();
                PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());
                PlayerManager targetManager = main.getPlayerManager(player.getUniqueId());
                joueur.stun(playerClick.getLocation());

                Sai sai = (Sai) joueur.getRole();

                inv.close(playerClick);
                if (joueur.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Saï est en train de vous sceller. Pour l'en empêcher, vous avez une minute pour l'éliminer !");
                new BukkitRunnable() {

                    int timer = 60*20;

                    @Override
                    public void run() {
                        ActionBar.sendMessage(playerClick, "§7Dessin : [" + ChunkLoader.getProgressBar(timer, 60*20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");
                        if(!targetManager.isAlive()){
                            playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVotre cible est morte ! Utilisation du pouvoir échoué. Vous pouvez à nouveau bouger !");
                            cancel();
                        }
                        if(timer == 0){
                            if(joueur.isAlive()){
                                //IL EST ENCORE EN VIE
                                sai.setScelled(player.getUniqueId());
                                joueur.setStunned(false);
                                PlayerManager targetJoueur = main.getPlayerManager(player.getUniqueId());
                                targetJoueur.setAlive(false);
                                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"§aVous avez réussi à scellé "+player.getName());
                                sai.setHasUsedFuinjutsu(true);
                                player.setGameMode(GameMode.SPECTATOR);
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"§cSaï vous a scellé. S'il vient à mourir vous en serez libéré sur sa position.");
                            }
                            cancel();
                        }
                        timer--;
                    }
                }.runTaskTimer(main, 0, 1);
            });
        }
    }
}
