package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.items.WabisukeFishin;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.Rukia;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Renji extends Role implements RoleListener, ShinigamiRole {

    public Renji(){
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new WabisukeFishin(main).toItemStack());
    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    protected void onDay(Player player) {

    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {

    }

    @Override
    public void onPlayerDeath(Player player) {
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.hasRole() && playerManager.getRole() instanceof Rukia){
            Player renji = super.getPlayer();
            if(renji != null){
                renji.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*60*15, 0, false, false));
                renji.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§9Rukia§e est morte. Vous recevez donc l’effet§9 Résistance 1§e pendant 15 minutes.");
            }
        }
    }

    @Override
    public void onKill(OfflinePlayer player, Player killer) {
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        PlayerManager killerManager = main.getPlayerManager(killer.getUniqueId());
        if(playerManager.hasRole() && playerManager.getRole() instanceof Rukia){
            Player renji = super.getPlayer();
            if(renji != null){
               renji.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§9Le tueur de Rukia est§6 " + killerManager.getPlayerName() + " (" + killerManager.getRole().getRoleName() + ").");
            }
        }
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                .setName(getCamp().getCompoColor() + getRoleName())
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWE1MmQ1MzBhZTdhM2E3MDc0YTcxOWEyODYyY2U1ZTdhODViMjU0MjYyZjM4YTRkNzg5Y2I2NTBlZjJmIn19fQ==");

    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Renji";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @EventHandler
    public void onFish(PlayerFishEvent event){
        Entity entity = event.getCaught();
        if(entity != null){
            if(event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY && entity instanceof Player){
                Player player = event.getPlayer();
                Player target = (Player) entity;
                PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                if(playerManager.hasRole() && playerManager.getRole() instanceof Renji){
                    if(playerManager.getRoleCooldownWabisuke() <= 0){
                        ClassUtils.setCorrectHealth(target, target.getHealth() - 4D, false);
                        ClassUtils.pullEntityToLocation(entity, player.getLocation(), 0.05, 0.03, 0.05);
                        playerManager.setRoleCooldownWabisuke(30);
                    }
                    else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownWabisuke()));
                    }
                }
            }
        }
    }
}
