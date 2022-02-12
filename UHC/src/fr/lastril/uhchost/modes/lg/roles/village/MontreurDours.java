package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.lg.roles.LGFacadeRole;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.lg.roles.solo.LoupGarouBlanc;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class MontreurDours extends Role implements LGRole {

    private static final int DISTANCE = 50;


    public String getSkullValue() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmRhMzg4NjQ1Y2Y1YTdhOTNhY2NhODQyM2YxZGM2NzRlZDIxN2Q3NjJhOWZkMmZiNjIxYWYyZTY2OTRjNTcifX19";
    }

    @Override
    public void giveItems(Player player) {
    }

    @Override
    public void onNight(Player player) {
    }

    @Override
    public void onDay(Player player) {

    }

    @Override
    public void onNewEpisode(Player player) {
        boolean founded = false;
        for (Entity entity : player.getNearbyEntities(DISTANCE, DISTANCE, DISTANCE)) {
            if (entity instanceof Player) {
                Player target = (Player) entity;
                if (target.getGameMode() != GameMode.SPECTATOR) {
                    if (UhcHost.getInstance().getPlayerManager(target.getUniqueId()) != null) {
                        PlayerManager playerManager = UhcHost.getInstance().getPlayerManager(target.getUniqueId());
                        if (playerManager.isAlive() && playerManager.hasRole()) {
                            if (!(playerManager.getRole() instanceof LGFacadeRole)) {
                                if (playerManager.getCamps() == Camps.LOUP_GAROU || playerManager.getRole() instanceof LoupGarouBlanc) {
                                    Bukkit.broadcastMessage("§6Grrrrrrrrrrrrrr !");
                                    founded = true;
                                }
                            } else {
                                LGFacadeRole lgFacadeRole = (LGFacadeRole) playerManager.getRole();
                                if (lgFacadeRole.getRoleFacade().getCamp() == Camps.LOUP_GAROU || lgFacadeRole.getRoleFacade() instanceof LoupGarouBlanc) {
                                    Bukkit.broadcastMessage("§6Grrrrrrrrrrrrrr !");
                                }
                            }

                        }
                    }
                }
            }
        }
        if (founded) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.playSound(players.getLocation(), Sound.WOLF_GROWL, 1, 1);
            }
        }
    }

    @Override
    public void onNewDay(Player player) {


    }

    @Override
    public String getRoleName() {
        return "Montreur d'Ours";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "lg.yml");
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQ5NThkN2M2OWFlNDg0YzY1ZjMxMzQ3Y2RjYzkyYzY5ZjU0MDY4MDViNTM2NTJhNzVhOGVkNzk5ZGY3In19fQ==");
    }

    @Override
    public void checkRunnable(Player player) {

    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }

}
