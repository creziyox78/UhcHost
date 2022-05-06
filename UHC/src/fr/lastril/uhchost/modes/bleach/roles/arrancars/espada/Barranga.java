package fr.lastril.uhchost.modes.bleach.roles.arrancars.espada;

import fr.lastril.uhchost.modes.bleach.roles.ArrancarRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.WorldUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Barranga extends Role implements ArrancarRole {

    private boolean usedAura;
    private final List<Player> players = new ArrayList<>();

    public Barranga() {
        super.addEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public int getNbQuartz() {
        return 80;
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        if(usedAura) {
            List<Location> locations = ClassUtils.getCircle(player.getLocation(), 10, 20);
            for(Location location : locations) {
                WorldUtils.spawnColoredParticle(location, EnumParticle.REDSTONE, Color.GRAY);
            }
            for(Player p : Bukkit.getOnlinePlayers()) {
                if(p != player) {
                    PlayerManager pm = main.getPlayerManager(p.getUniqueId());
                    if(pm.isAlive()) {
                        if(p.getWorld() == player.getWorld()) {
                            if(p.getLocation().distance(player.getLocation()) < 10) {
                                if(!players.contains(p)){
                                    players.add(p);
                                    p.setMaxHealth(p.getMaxHealth() - 2D*3D);
                                }
                            } else {
                                if(players.contains(p)){
                                    players.remove(p);
                                    p.setMaxHealth(p.getMaxHealth() + 2D*3D);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onTransformationFirst() {
        Player barranga = super.getPlayer();
        if(barranga != null) {
            PlayerManager playerManager = main.getPlayerManager(barranga.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            bleachPlayerManager.setStrengthPourcentage(10);
        }
    }

    @Override
    public void onUnTransformationFirst() {
        Player barranga = super.getPlayer();
        if(barranga != null) {
            PlayerManager playerManager = main.getPlayerManager(barranga.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            bleachPlayerManager.setStrengthPourcentage(0);
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
        return null;
    }

    @Override
    public Camps getCamp() {
        return Camps.ARRANCARS;
    }

    @Override
    public String getRoleName() {
        return "Barranga";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    public boolean isUsedAura() {
        return usedAura;
    }

    public void setUsedAura(boolean usedAura) {
        this.usedAura = usedAura;
    }
}
