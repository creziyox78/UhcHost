package fr.lastril.uhchost.modes.lg.roles.lg;

import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.roles.LGChatRole;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.lg.roles.RealLG;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LoupGarouPisteur extends Role implements LGRole, RealLG, LGChatRole {

    private final int distance = 200;


    public LoupGarouPisteur() {
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.NIGHT);
        super.addEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 1, 0, false, false), When.AT_KILL);
        super.addEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 1, 0, false, false), When.AT_KILL);
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
    }

    @Override
    public void onNewDay(Player player) {
    }

    @Override
    public String getRoleName() {
        return "Loup-Garou Pisteur";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "lg.yml");
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY5MjQ4NmM5ZDZlNGJiY2UzZDVlYTRiYWFmMGNmN2JiZDQ5OTQ3OWQ4ZTM5YTM1NjBiYjZjOGM4YmYxYjZkYSJ9fX0===");
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.hasRole() && playerManager.isAlive()){
            if(playerManager.getRole() instanceof LoupGarouPisteur) {
                PlayerManager tracked = getNearLG(player);
                if(tracked != null && tracked.isAlive()){
                    if(tracked.getPlayer() != null){
                        ActionBar.sendMessage(player, "§c§lLoup-Garou le plus proche §a┃ "
                                + ClassUtils.getDirectionOf(player.getLocation(), tracked.getPlayer().getLocation()));
                    } else {
                        ActionBar.sendMessage(player, "§c§lLoup-Garou le plus proche §a┃ (Déconnecté)");
                    }
                }
            }
        }
    }

    private PlayerManager getNearLG(Player pisteur){
        for(Entity entity : pisteur.getNearbyEntities(distance, distance, distance)){
            if(entity instanceof Player){
                Player player = (Player) entity;
                if(player != pisteur){
                    PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                    if(playerManager.hasRole() && playerManager.isAlive()){
                        if(playerManager.getRole() instanceof RealLG){
                            return playerManager;
                        }
                    }
                }
            }
        }
        return null;
    }


    @Override
    public void afterRoles(Player player) {
        player.sendMessage(sendList());
    }

    @Override
    public String sendList() {
        if (main.gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager) {
            LoupGarouManager loupGarouManager = (LoupGarouManager) main.gameManager.getModes().getMode().getModeManager();
            return loupGarouManager.sendLGList();
        }
        return null;
    }

    @Override
    public Camps getCamp() {
        return Camps.LOUP_GAROU;
    }

    @Override
    public boolean canSee() {
        return true;
    }

    @Override
    public boolean canSend() {
        return true;
    }

    @Override
    public boolean sendPlayerName() {
        return false;
    }


}
