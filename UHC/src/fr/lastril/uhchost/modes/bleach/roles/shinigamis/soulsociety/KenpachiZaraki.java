package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class KenpachiZaraki extends Role implements ShinigamiRole, RoleListener {

    private int lastDamageSeconds = 0;

    public KenpachiZaraki(){
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @EventHandler
    public void onEatApple(PlayerItemConsumeEvent event){
        if(event.getItem().getType() == Material.GOLDEN_APPLE) {
            Player player = event.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if(joueur.hasRole()){
                if(joueur.getRole() instanceof KenpachiZaraki){
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            EntityLiving el = ((CraftPlayer)player).getHandle();
                            el.setAbsorptionHearts(2F);
                        }
                    }.runTaskLater(main, 3);
                }
            }
        }
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        if(player.getHealth() <= 7D*2D){
            bleachPlayerManager.setSpeedPourcentage(7);

            if(player.getHealth() <= 5D*2D){
                bleachPlayerManager.setStrengthPourcentage(10);
                if(player.getHealth() <= 3D*2D){
                    bleachPlayerManager.setResistancePourcentage(10);
                }
            }
        } else {
            player.setWalkSpeed(0.2F);
            bleachPlayerManager.setStrengthPourcentage(0);
            bleachPlayerManager.setResistancePourcentage(0);
            bleachPlayerManager.setSpeedPourcentage(0);
        }
    }

    @Override
    public void afterRoles(Player player) {
        Bukkit.getScheduler().runTaskTimer(main, () -> {
            UhcHost.debug("Seconds : " + lastDamageSeconds);
            lastDamageSeconds--;
        },0, 20);
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
        return null;
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Kenpachi Zaraki";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @EventHandler
    public void onDamageKenpachiZaraki(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
            Player player = (Player) event.getEntity();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if(playerManager.hasRole() && playerManager.getRole() instanceof KenpachiZaraki){
                KenpachiZaraki kenpachiZaraki = (KenpachiZaraki) playerManager.getRole();
                kenpachiZaraki.lastDamageSeconds = 5;
            }
        }
    }

    public boolean didntGetDamageIn5Seconds(){
        return lastDamageSeconds <= 0;
    }

}
