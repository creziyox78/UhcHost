
package fr.lastril.uhchost.modes.bleach.roles.shinigamis;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.commands.CmdLynx;
import fr.lastril.uhchost.modes.bleach.items.Kido;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Yoruichi extends Role implements ShinigamiRole, RoleCommand, RoleListener {

    private boolean transformed, transform, inKido;
    private int sneakTime, transformCooldown = 120, ticks = 7;
    private long lastCombat;

    public Yoruichi() {
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false), When.START);
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        long delayLastCombat = System.currentTimeMillis() - this.lastCombat;
        transform = delayLastCombat <= transformCooldown * 1000L;
        if(inKido){
            if(player.getHealth() > 2D*5D){
                setInKido(false);
                if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous perdez votre effet de Kido car vous avez plus de 5 coeurs.");
            }
        }
        if(transformed){
            for(Player players : Bukkit.getOnlinePlayers()){
                if(players != player){
                    if(players.getLocation().distance(player.getLocation()) < 64){
                        ClassUtils.packetMobForPlayers(players);
                    }
                }
            }
            if(player.isSneaking()){

                ticks--;
                if(ticks == 0){
                    sneakTime++;
                    ticks = 7;
                    if(sneakTime == 5){
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*4, 1, false, false));
                    }
                }

            } else {
                sneakTime = 0;
            }
        }
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Kido(main).toItemStack());
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
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                .setName(getCamp().getCompoColor() + getRoleName())
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2M5MTk4MzA4ZGM0NWQ0NTI5OTI3MWJiMWNiZmY5YmU1ZGE3MDFkMzEwMDU0MmNkZmVjMDIwZDU0NjI5MGE3YyJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Yoruichi";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdLynx(main));
    }

    @EventHandler
    public void onDamagePlayer(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            PlayerManager damagerManager = main.getPlayerManager(damager.getUniqueId());
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if(damagerManager.hasRole() && damagerManager.getRole() instanceof Yoruichi){
                Yoruichi yoruichi = (Yoruichi) damagerManager.getRole();
                if(yoruichi.transformed){
                    yoruichi.setTransformed(false);
                    damager.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVotre transformation prend fin car vous avez frappé un joueur.");
                    if(damager.isSneaking()){
                        if(player.hasPotionEffect(PotionEffectType.BLINDNESS))
                            player.removePotionEffect(PotionEffectType.BLINDNESS);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*3, 0, false, false));
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cUn chat vient de vous frapper en plein visage, vous êtes aveuglés pendant 3 secondes.");
                    }
                }
            }
            if(playerManager.hasRole() && playerManager.getRole() instanceof Yoruichi){
                Yoruichi yoruichi = (Yoruichi) playerManager.getRole();
                yoruichi.lastCombat = System.currentTimeMillis();
                if(yoruichi.transformed){
                    yoruichi.setTransformed(false);
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVotre transformation prend fin car vous avez été frappé un joueur.");
                }
            }
        }
    }

    public boolean isTransformed() {
        return transformed;
    }

    public void setTransformed(boolean transformed) {
        if(!transformed){
            for(Player players : Bukkit.getOnlinePlayers()){
                if(players.getLocation().distance(main.getPlayerManager(players.getUniqueId()).getPlayer().getLocation()) < 64){
                    players.hidePlayer(super.getPlayer());
                    Bukkit.getScheduler().runTaskLater(main, () -> players.showPlayer(super.getPlayer()), 2);
                }
            }
        }
        this.transformed = transformed;
    }

    public boolean canTransform(){
        return transform;
    }

    public void setInKido(boolean inKido) {
        this.inKido = inKido;
    }
}
