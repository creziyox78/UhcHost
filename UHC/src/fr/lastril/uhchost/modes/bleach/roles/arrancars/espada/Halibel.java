package fr.lastril.uhchost.modes.bleach.roles.arrancars.espada;

import fr.lastril.uhchost.modes.bleach.ceros.AbstractCero;
import fr.lastril.uhchost.modes.bleach.ceros.CeroMoyen;
import fr.lastril.uhchost.modes.bleach.items.Cascada;
import fr.lastril.uhchost.modes.bleach.roles.ArrancarRole;
import fr.lastril.uhchost.modes.bleach.roles.CeroUser;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.Livre;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Halibel extends Role implements ArrancarRole, CeroUser, RoleListener {

    private final List<Location> waterLocs = new ArrayList<>();

    public Halibel() {
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public int getNbQuartz() {
        return 70;
    }

    @Override
    public void onTransformationFirst() {

    }

    @Override
    public void onUnTransformationSecond() {

    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();
        if(waterLocs.contains(location)) {
            if(block.getTypeId() == 8 || block.getTypeId() == 9) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public boolean canUseCero() {
        Player halibel = super.getPlayer();
        if(halibel != null) {
            PlayerManager playerManager = main.getPlayerManager(halibel.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(bleachPlayerManager.isInFormeLiberer() && playerManager.getRoleCooldownCeroMoyen() <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onUseCero() {
        Player halibel = super.getPlayer();
        if(halibel != null) {
            PlayerManager playerManager = main.getPlayerManager(halibel.getUniqueId());
            if(playerManager.getRoleCooldownCeroMoyen() <= 0) {
                playerManager.setRoleCooldownCeroMoyen(60*2);
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
    public AbstractCero getCero() {
        return new CeroMoyen();
    }

    @Override
    public void giveItems(Player player) {
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        if(bleachPlayerManager.isInFormeLiberer()) {
            main.getInventoryUtils().giveItemSafely(player, new Cascada(main).toItemStack());
        }
        main.getInventoryUtils().giveItemSafely(player, new Livre(Enchantment.DEPTH_STRIDER, 3).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Livre(Enchantment.OXYGEN, 3).toItemStack());
        player.setLevel(player.getLevel() + 15);
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
        return "Halibel";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    public void createWater(Location location) {
        List<Location> waterSphere = ClassUtils.getNewSphere(location.clone(), 10, false);
        for (Location locations : waterSphere) {
            if(locations.getBlock().getType() == Material.AIR) {
                waterLocs.add(locations);
                Block block = locations.getBlock();
                block.setType(Material.WATER);
            }
        }
    }

    public void deleteWater() {
        for(Location loc : waterLocs) {
            loc.getBlock().setType(Material.AIR);
        }
        waterLocs.clear();
    }

}
