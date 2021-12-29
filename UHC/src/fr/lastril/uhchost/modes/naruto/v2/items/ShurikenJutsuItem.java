package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.gui.minato.PlayersGUI;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Minato;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.clickable_messages.ClickableMessage;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.WorldUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ShurikenJutsuItem extends QuickItem {


    private NarutoV2Manager narutoV2Manager;

    public ShurikenJutsuItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§6ShurikenJutsu");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
                if (joueur.getRole() instanceof Minato) {
                    Minato minato = (Minato) joueur.getRole();
                    if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {

                        if (player.isSneaking()) {
                            //VEUT POSER UNE BALISE
                            if (minato.getBalises().size() < Minato.BALISES_MAX) {
                                if(!player.getWorld().getName().equalsIgnoreCase("kamui")){
                                    new ClickableMessage(player, onClickMessage -> {

                                        Location baliseLocation = player.getLocation();

                                        WorldUtils.spawnFakeLightning(player, baliseLocation, true);

                                        minato.getBalises().add(baliseLocation);
                                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez bien posé une balise en x: " + baliseLocation.getBlockX() + ", y:" + baliseLocation.getBlockY() + ", z: " + baliseLocation.getBlockZ());
                                        minato.usePower(joueur);
                                        minato.usePowerSpecific(joueur, super.getName());
                                    }, Messages.NARUTO_PREFIX.getMessage() + "§aSouhaitez-vous poser une balise ici ? " + Messages.CLICK_HERE.getMessage(), "§aClique ici pour poser une balise");
                                }else{
                                    player.sendMessage(Messages.error("Vous ne pouvez pas poser de balises dans le monde kamui !"));
                                }
                            } else {
                                //PLUS DE BALISES
                                player.sendMessage(Messages.error("Vous avez atteint votre nombre de balises max (" + Minato.BALISES_MAX + ") !"));
                            }
                        } else {
                            if(joueur.getRoleCooldownShurikenJustuTP() == 0){
                                //GUI BALISES
                                List<Player> players = new ArrayList<>();
                                players.add(player);

                                for (Entity entity : player.getNearbyEntities(Minato.DISTANCE_SHURIKENJUTSU, Minato.DISTANCE_SHURIKENJUTSU, Minato.DISTANCE_SHURIKENJUTSU)) {
                                    if(entity instanceof Player){
                                        Player playersNearby = (Player) entity;
                                        if(playersNearby.getGameMode() != GameMode.SPECTATOR && joueur.isAlive()){
                                            players.add(playersNearby);
                                        }
                                    }
                                }
                                minato.usePower(joueur);
                                minato.usePowerSpecific(joueur, super.getName());
                                new PlayersGUI(main, players, minato.getBalises()).open(player);
                            }else{
                                player.sendMessage(Messages.cooldown(joueur.getRoleCooldownShurikenJustuTP()));
                            }
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                } else {
                    player.sendMessage(Messages.not("Naruto ou Jiraya"));
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            }
        });
    }
}
