package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.modes.bleach.commands.CmdHeal;
import fr.lastril.uhchost.modes.bleach.items.Itegumo;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Isane extends Role implements RoleListener, RoleCommand {

    private PlayerManager healedManager;
    private boolean inItegumo;
    private final int distance = 10;

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Itegumo(main).toItemStack());
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

    @EventHandler
    public void onIsaneDamage(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
            Player damager = (Player) event.getDamager();
            Player target = (Player) event.getEntity();
            PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
            PlayerManager damagerManager = main.getPlayerManager(damager.getUniqueId());
            if(targetManager == healedManager){
                Player isane = super.getPlayer();
                if(isane != null){
                    if(target.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                        target.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    target.sendMessage("§cVous avez perdu la bénédiction d'§9Isane§c.");
                    isane.sendMessage("§3Votre protégé vient de recevoir un coup de la part de " + damagerManager.getRole().getRoleName() + " dans le monde \""+ target.getWorld().getName()+"\" en x:" + target.getLocation().getX() + " ,y:" + target.getLocation().getY() + " ,z:" + target.getLocation().getZ());
                }
            }
            if(damagerManager.getRole() instanceof Isane){
                Isane isane = (Isane) damagerManager.getRole();
                if(isane.isInItegumo()){
                    double minHeal = 50;
                    Player nearPlayer = null;
                    for(Entity entity : damager.getNearbyEntities(distance, distance, distance)){
                        if(entity instanceof Player){
                            nearPlayer = (Player) entity;
                            PlayerManager nearManager = main.getPlayerManager(nearPlayer.getUniqueId());
                            if(nearManager.getCamps() == damagerManager.getCamps()){
                                if(nearPlayer.getHealth() < minHeal){
                                    minHeal = nearPlayer.getHealth();
                                }
                            }
                        }
                    }
                    if(nearPlayer != null){
                        ClassUtils.setCorrectHealth(nearPlayer, nearPlayer.getHealth() + event.getFinalDamage(), false);
                    }
                }
            }
        }
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                .setName(getCamp().getCompoColor() + getRoleName())
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDliZjY2NTg0MmZkNTdkNjNhMzlhOGI0OWI4YzE3MjgxODZkZTRkMzIzZGJjNTljNGE3ODk3OGI3Njg3YWJmIn19fQ==");

    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Isane";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdHeal(main));
    }

    public void setHealedManager(PlayerManager healedManager) {
        this.healedManager = healedManager;
    }

    public boolean isInItegumo() {
        return inItegumo;
    }

    public void setInItegumo(boolean inItegumo) {
        this.inItegumo = inItegumo;
    }
}
