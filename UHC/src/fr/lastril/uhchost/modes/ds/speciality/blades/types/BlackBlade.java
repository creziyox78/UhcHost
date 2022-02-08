package fr.lastril.uhchost.modes.ds.speciality.blades.types;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.ds.speciality.blades.BladeType;
import fr.lastril.uhchost.modes.ds.speciality.blades.Blades;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.DSPlayerManager;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlackBlade extends Blades {
    public BlackBlade(PlayerManager playerManager) {
        super(playerManager);
    }

    public ItemStack getItem() {
        return (new QuickItem(Material.INK_SACK)).setDurability((short) getType().getData())
                .setName(getType().getName())
                .toItemStack();
    }

    public void onEquip() {
        Player player = getPlayerManager().getPlayer();
        Role role = UhcHost.getInstance().getPlayerManager(player.getUniqueId()).getRole();
        if (role == null)
            return;
        boolean give = true;
        if (role.getCamp() == Camps.DEMONS)
            give = false;
        if (give) {
            DSPlayerManager dsPlayer = getPlayerManager().getDSPlayerManager();
            dsPlayer.addStrenght(5);
            player.sendMessage("avez re+5% de Force grau Sabre Noir.");
            setAttribute(true);
            setEquiped(true);
        }
    }

    public void onUnEquipOrBreak() {
        if (!this.attribute || !isEquiped())
            return;
        DSPlayerManager dsPlayer = playerManager.getDSPlayerManager();
        Player player = playerManager.getPlayer();
        dsPlayer.removeStrenght(5);
        player.sendMessage("avez perdu -5% de Force car votre Sabre Noir est endommagou n'est plus dans votre inventaire.");
        setEquiped(false);
    }

    public BladeType getType() {
        return BladeType.BLACK;
    }
}
