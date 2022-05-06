package fr.lastril.uhchost.modes.bleach.roles.arrancars.espada;

import com.avaje.ebeaninternal.server.core.Message;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.ceros.AbstractCero;
import fr.lastril.uhchost.modes.bleach.ceros.CeroFort;
import fr.lastril.uhchost.modes.bleach.ceros.CeroMoyen;
import fr.lastril.uhchost.modes.bleach.ceros.CeroType;
import fr.lastril.uhchost.modes.bleach.roles.ArrancarRole;
import fr.lastril.uhchost.modes.bleach.roles.CeroUser;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Nnoitra extends Role implements ArrancarRole, CeroUser, RoleListener {

    public Nnoitra(){
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public int getNbQuartz() {
        return 70;
    }

    @Override
    public void afterRoles(Player player) {
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        bleachPlayerManager.setResistancePourcentage(10);
    }

    @Override
    public void onTransformationFirst() {
        Player nnoitra = super.getPlayer();
        if(nnoitra != null) {
            PlayerManager playerManager = main.getPlayerManager(nnoitra.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            bleachPlayerManager.setResistancePourcentage(20);
            super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
            if(nnoitra.hasPotionEffect(PotionEffectType.SPEED))
                nnoitra.removePotionEffect(PotionEffectType.SPEED);
            nnoitra.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        }
    }

    @Override
    public void onUnTransformationFirst() {
        Player nnoitra = super.getPlayer();
        if(nnoitra != null) {
            PlayerManager playerManager = main.getPlayerManager(nnoitra.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            bleachPlayerManager.setResistancePourcentage(10);
            super.removeEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
            if(nnoitra.hasPotionEffect(PotionEffectType.SPEED))
                nnoitra.removePotionEffect(PotionEffectType.SPEED);
        }
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractEvent event){
        Player player = event.getPlayer();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        Action action = event.getAction();
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK){
            if(playerManager.hasRole() && playerManager.getRole() instanceof Nnoitra){

                if(event.getItem() != null){
                    if(event.getItem().getType() == Material.DIAMOND_SWORD ||
                            event.getItem().getType() == Material.GOLD_SWORD ||
                            event.getItem().getType() == Material.IRON_SWORD ||
                            event.getItem().getType() == Material.STONE_SWORD ||
                            event.getItem().getType() == Material.WOOD_SWORD){
                        if(bleachPlayerManager.canUsePower()){
                            if(playerManager.getRoleCooldownAttraction() <= 0){
                                if(!bleachPlayerManager.isInFormeLiberer()){
                                    Player target = ClassUtils.getTargetPlayer(player, 5);
                                    if(target != null){
                                        target.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§eVous avez été attiré par §cNnoitra§e !");
                                        ClassUtils.pullEntityToLocation(target, player.getLocation(), 0.07, 0.03, 0.07);
                                    } else {
                                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cAucune cible n'a été trouvée.");
                                    }
                                } else {
                                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous ne pouvez pas attirer de joueur quand vous êtes en forme libérée.");
                                }

                            } else {
                                player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownAttraction()));
                            }
                        } else {
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean canUseCero(CeroType ceroType) {
        Player nnoitra = super.getPlayer();
        if(nnoitra != null) {
            PlayerManager playerManager = main.getPlayerManager(nnoitra.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(bleachPlayerManager.canUsePower()) {
                if(bleachPlayerManager.isInFormeLiberer() && ceroType == CeroType.CEROS_FORT){
                    if(playerManager.getRoleCooldownCeroFort() <= 0) {
                        return true;
                    }
                } else {
                    if(playerManager.getRoleCooldownCeroMoyen() <= 0 && ceroType == CeroType.CEROS_MOYEN) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onUseCero(CeroType ceroType) {
        Player nnoitra = super.getPlayer();
        if(nnoitra != null) {
            PlayerManager playerManager = main.getPlayerManager(nnoitra.getUniqueId());
            if(ceroType == CeroType.CEROS_FORT){
                playerManager.setRoleCooldownCeroFort(60*6);
            } else if(ceroType == CeroType.CEROS_MOYEN){
                playerManager.setRoleCooldownCeroMoyen(60*4);
            }
        }
    }

    @Override
    public int getCeroRedValue() {
        return 0;
    }

    @Override
    public int getCeroGreenValue() {
        return 0;
    }

    @Override
    public int getCeroBlueValue() {
        return 0;
    }

    @Override
    public List<AbstractCero> getCero() {
        List<AbstractCero> ceros = new ArrayList<>();
        Player nnoitra = super.getPlayer();
        if(nnoitra != null) {
            PlayerManager playerManager = main.getPlayerManager(nnoitra.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(bleachPlayerManager.isInFormeLiberer()) {
                ceros.add(new CeroFort());
            }
        }
        ceros.add(new CeroMoyen());
        return ceros;
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
        return "Nnoitra";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }
}
