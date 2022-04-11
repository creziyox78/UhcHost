
package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import com.mojang.authlib.properties.Property;
import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.ReflectCorpse;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.API.npc.NPCManager;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutBed;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Nemu extends Role implements ShinigamiRole, RoleListener {

    private int deathTime = 3*60, ticks = 7;
    private boolean dead;
    private ReflectCorpse rp;

    public Nemu(){
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        if(player.hasPotionEffect(PotionEffectType.POISON))
            player.removePotionEffect(PotionEffectType.POISON);
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.hasRole() && playerManager.getRole() instanceof Nemu){
            Nemu nemu = (Nemu) playerManager.getRole();
            if(nemu.dead){
                UhcHost.debug("Nemu is dead :" + ticks);
                ticks--;
                if(ticks == 0){
                    deathTime--;
                    UhcHost.debug("Nemu is dead :" + deathTime);
                    ticks = 7;
                }
                player.teleport(playerManager.getDeathLocation());
                for(PlayerManager mayuri : main.getGamemanager().getModes().getMode().getModeManager().getPlayerManagersWithRole(Mayuri.class)){
                    Mayuri mayuri1 = (Mayuri) mayuri.getRole();
                    Player mayuriPlayer = mayuri.getPlayer();
                    if(mayuriPlayer != null){
                        ActionBar.sendMessage(player, "§eVous êtes à l'agonie (" + new FormatTime(deathTime) +") : " + ClassUtils.getDirectionOf(player.getLocation(), mayuriPlayer.getLocation()));
                    }
                    if(deathTime == 0){
                        mayuri1.setReanimedNemu(true);
                    }
                }
                if(deathTime == 0){
                    main.getGamemanager().getModes().getMode().onDeath(player, null);
                    destroyFakePlayer();
                }

            }

        }
    }

    @Override
    public void giveItems(Player player) {

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
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNThlY2ZmZjdkMDFhM2VkMzFlMzM3MDRhYTQ3MWQ3Y2Q4Zjc5ZWQ1OTI1YjA1Mzc5NWUwN2I4NzVmMzhlOWRjZSJ9fX0=");

    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Nemu";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        playerManager.setDeathLocation(player.getLocation());
        if(playerManager.hasRole() && playerManager.getRole() instanceof Nemu){
            Nemu nemu = (Nemu) playerManager.getRole();
            Mayuri mayuri = null;
            for(PlayerManager playerManagers : main.getGamemanager().getModes().getMode().getModeManager().getPlayerManagersWithRole(Mayuri.class)){
                if(playerManagers.isAlive()){
                    mayuri = (Mayuri) playerManagers.getRole();
                }
            }
            if(!nemu.isDead() && mayuri != null && !mayuri.hasReanimedNemu()){
                nemu.setDead(true);
                try {
                    rp = new ReflectCorpse(player.getName(), playerManager.getDeathLocation());
                    rp.spawn();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        player.setGameMode(GameMode.ADVENTURE);
                        player.setGameMode(GameMode.SPECTATOR);
                        player.teleport(playerManager.getDeathLocation());
                    }
                }.runTaskLater(main, 30);
            }
        }
    }

    @EventHandler
    public void onDamageWithHand(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
            Player entity = (Player) event.getEntity();
            Player player = (Player) event.getDamager();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if(playerManager.hasRole() && playerManager.getRole() instanceof Nemu){
                if(player.getItemInHand() != null && player.getItemInHand().getType() == Material.AIR){
                    if(playerManager.getRoleCooldownHakuda() <= 0){
                        playerManager.setRoleCooldownHakuda(60*3);
                        ClassUtils.ripulseSpecificEntityFromLocation(entity, player.getLocation(), 2, 2);
                        if(entity.getHealth() - 3D > 0D)
                            entity.setHealth(entity.getHealth() - 3D);
                        else
                            entity.setHealth(1D);
                    }
                }
            }
        }
    }

    public void setDeathTime(int deathTime) {
        this.deathTime = deathTime;
    }

    public int getDeathTime() {
        return deathTime;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void destroyFakePlayer(){
        try {
            if(rp != null){
                rp.destroy();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
