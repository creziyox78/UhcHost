package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.WorldUtils;
;
import fr.maygo.uhc.api.clickable_messages.ClickableMessage;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.gui.minato.PlayersGUI;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Minato;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ShurikenJutsuItem extends QuickItem {


    public ShurikenJutsuItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§6ShurikenJutsu");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Minato) {
                    Minato minato = (Minato) PlayerManager.getRole();
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {

                        if (player.isSneaking()) {
                            //VEUT POSER UNE BALISE
                            if (minato.getBalises().size() < Minato.BALISES_MAX) {
                                if (!player.getWorld().getName().equalsIgnoreCase("kamui")) {
                                    new ClickableMessage(player, onClickMessage -> {

                                        Location baliseLocation = player.getLocation();

                                        WorldUtils.spawnFakeLightning(player, baliseLocation, true);

                                        minato.getBalises().add(baliseLocation);
                                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez bien posé une balise en x: " + baliseLocation.getBlockX() + ", y:" + baliseLocation.getBlockY() + ", z: " + baliseLocation.getBlockZ());

                                    }, Messages.NARUTO_PREFIX.getMessage() + "§aSouhaitez-vous poser une balise ici ? " + Messages.CLICK_HERE.getMessage(), "§aClique ici pour poser une balise");
                                } else {
                                    player.sendMessage(Messages.error("Vous ne pouvez pas poser de balises dans le monde kamui !"));
                                }
                            } else {
                                //PLUS DE BALISES
                                player.sendMessage(Messages.error("Vous avez atteint votre nombre de balises max (" + Minato.BALISES_MAX + ") !"));
                            }
                        } else {
                            if (PlayerManager.getRoleCooldownShurikenJustuTP() == 0) {
                                //GUI BALISES
                                List<Player> players = new ArrayList<>();
                                players.add(player);

                                for (Entity entity : player.getNearbyEntities(Minato.DISTANCE_SHURIKENJUTSU, Minato.DISTANCE_SHURIKENJUTSU, Minato.DISTANCE_SHURIKENJUTSU)) {
                                    if (entity instanceof Player) {
                                        Player playersNearby = (Player) entity;
                                        if (playersNearby.getGameMode() != GameMode.SPECTATOR && PlayerManager.isAlive()) {
                                            players.add(playersNearby);
                                        }
                                    }
                                }

                                new PlayersGUI(main, players, minato.getBalises()).open(player);
                            } else {
                                player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownShurikenJustuTP()));
                            }
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    player.sendMessage(Messages.not("Naruto ou Jiraya"));
                    return;
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }
}
