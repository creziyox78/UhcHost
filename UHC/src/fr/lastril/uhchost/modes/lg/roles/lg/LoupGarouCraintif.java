package fr.lastril.uhchost.modes.lg.roles.lg;

import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LoupGarouCraintif extends Role implements LGRole {

    private int distance = 30;
    private boolean time;

    public LoupGarouCraintif() {
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 1, 0, false, false), When.AT_KILL);
        super.addEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 1, 0, false, false), When.AT_KILL);
    }

    public String getSkullValue() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmNjYzI5NmNkMTcxYzE1OGVlYzkzZWMwM2M1YTY1ZWFkYzUzODA3ZTM0N2VkYTJhMzM0YjY3MDM0NTg5N2E1OCJ9fX0=";
    }

    @Override
    public String getRoleName() {
        return "Loup-Garou Craintif";
    }

    @Override
    public String getDescription() {
        return main.getLGRoleDescription(this,this.getClass().getName());
    }

    @Override
    public void giveItems(Player player) {
    }

    @Override
    public void onNight(Player player) {
        time = true;
    }

    @Override
    public void onDay(Player player) {
        time = false;
    }

    @Override
    public void onNewEpisode(Player player) {

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
    public void onNewDay(Player player) {
    }

    @Override
    public void checkRunnable(Player player) {
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        int nearLgPlayer = 0;
        if(playerManager.hasRole() && playerManager.isAlive()){
            for(Entity entity : player.getNearbyEntities(distance, distance, distance)){
                if(entity instanceof Player){
                    Player nearPlayer = (Player) entity;
                    if(nearPlayer != player){
                        PlayerManager nearPlayerManager = main.getPlayerManager(nearPlayer.getUniqueId());
                        if(nearPlayerManager.isAlive()){
                            if(nearPlayerManager.getCamps() == Camps.LOUP_GAROU || nearPlayerManager.getCamps() == Camps.LOUP_GAROU_BLANC){
                                nearLgPlayer++;
                            }
                        }
                    }
                }
            }
            giveManagerEffect(player, nearLgPlayer);
        }

    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY5MjQ4NmM5ZDZlNGJiY2UzZDVlYTRiYWFmMGNmN2JiZDQ5OTQ3OWQ4ZTM5YTM1NjBiYjZjOGM4YmYxYjZkYSJ9fX0===");
    }

    @Override
    public Camps getCamp() {
        return Camps.LOUP_GAROU;
    }

    private void giveManagerEffect(Player player, int nearLgPlayer){
        switch (nearLgPlayer){
            case 1:
                if(player.hasPotionEffect(PotionEffectType.SPEED))
                    player.removePotionEffect(PotionEffectType.SPEED);
                break;
        }
    }

}
