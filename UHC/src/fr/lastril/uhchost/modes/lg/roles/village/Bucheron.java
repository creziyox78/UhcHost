package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.commands.CmdFatigue;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.lg.roles.RealLG;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Arrays;
import java.util.List;

public class Bucheron extends Role implements LGRole, RoleCommand, RoleListener {

    private PlayerManager nearLG;
    private boolean fatigued;

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new QuickItem(Material.GOLDEN_APPLE, 3).toItemStack());
    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        nearLG = getNearLG(player);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
            Player damager = (Player) event.getDamager();
            PlayerManager damagerManager = main.getPlayerManager(damager.getUniqueId());
            if(nearLG != null){
                if(nearLG == damagerManager){
                    event.setDamage(event.getDamage() * 0.9);
                }
            }
        }
    }

    private PlayerManager getNearLG(Player bucheron){
        double distance = 30;
        for(Entity entity : bucheron.getNearbyEntities(distance, distance, distance)){
            if(entity instanceof Player){
                Player player = (Player) entity;
                if(player != bucheron){
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
    protected void onDay(Player player) {

    }

    @Override
    public void onNewEpisode(Player player) {
        if(isFatigued()){
            setFatigued(false);
            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§BVous pouvez à nouveau utiliser votre pouvoir.");
        }
    }

    @Override
    public void onNewDay(Player player) {

    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJlNmU0NTEwMGQzM2RjMDcyMDE4YzU1OGFiNDkyNTU1ZGE1NDRkYjJjNDBkNjRhMjY2ZTJlNTlkNzUwZjY0NiJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }

    @Override
    public String getRoleName() {
        return "Bûcheron";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "lg.yml");
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdFatigue(main));
    }
    public void setFatigued(boolean fatigued) {
        this.fatigued = fatigued;
    }

    public boolean isFatigued() {
        return fatigued;
    }
}
