package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.items.Chaines;
import fr.lastril.uhchost.modes.bleach.items.sword.Kazeshini;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Hisagi extends Role implements ShinigamiRole, RoleListener {

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Kazeshini(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Chaines(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Chaines(main).toItemStack());
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

    @EventHandler
    public void onChainesHit(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Snowball){
            Player player = (Player) event.getEntity();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            Snowball snowball = (Snowball) event.getDamager();
            if(snowball.getCustomName() != null && snowball.getCustomName().equalsIgnoreCase("§7Chaînes")){
                playerManager.stun(player.getLocation());
                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§eVous êtes immobilisés pendant 3 secondes à cause d'une§7 chaîne§e.");
                Bukkit.getScheduler().runTaskLater(main, () -> {
                    playerManager.setStunned(false);
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§aVous pouvez à nouveau bouger !");
                }, 20*3);
            }
        }
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Hisagi";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }
}
