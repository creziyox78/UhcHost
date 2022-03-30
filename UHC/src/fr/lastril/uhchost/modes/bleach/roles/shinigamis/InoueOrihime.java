package fr.lastril.uhchost.modes.bleach.roles.shinigamis;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.items.ShunShun;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class InoueOrihime extends Role implements ShinigamiRole, RoleListener {

    private boolean secondUse = false;

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new ShunShun(main).toItemStack());
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
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDUzYjRmZjMyZTRkOTEyYWQ1ODk1YjZjMzdhMzUyZjYxYWY5ZTQxZDI0N2E4NzliNWY0OWE2MzUyZmM4NiJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Inoue Orihime";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @EventHandler
    public void onDamageWithShunShun(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            PlayerManager playerManager = main.getPlayerManager(damager.getUniqueId());
            if(playerManager.hasRole() && playerManager.getRole() instanceof InoueOrihime) {
                if(damager.getItemInHand().isSimilar(new ShunShun(main).toItemStack())) {
                    if(damager.getHealth() + 2D > damager.getMaxHealth()) {
                        damager.setHealth(damager.getMaxHealth());
                    } else {
                        damager.setHealth(damager.getHealth() + 2D);
                    }
                    if(!secondUse) {
                        secondUse = true;
                    } else {
                        damager.damage(1D);
                    }
                    damager.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§a“Shun Shun” vous régénère 1 coeur.");
                }
            }
        }
    }

}
