package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Nanao extends Role implements ShinigamiRole, RoleListener {


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

    @EventHandler
    public void onTryNanaoInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getItem() != null && event.getItem().getType() != Material.AIR){
            if(event.getItem().getType() == Material.IRON_SWORD ||
                    event.getItem().getType() == Material.DIAMOND_SWORD){
                if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
                    PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                    BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
                    if(playerManager.hasRole() && playerManager.isAlive()){
                        if(playerManager.getRole() instanceof Nanao){
                            if(bleachPlayerManager.canUsePower()){
                                if(playerManager.getRoleCooldownNanao() <= 0){
                                    Player target = ClassUtils.getTargetPlayer(player, 15);
                                    if(target != null){
                                        PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
                                        BleachPlayerManager bleachTargetManager = targetManager.getBleachPlayerManager();
                                        if(targetManager.isAlive() && targetManager.hasRole()){
                                            bleachTargetManager.setNanaoEffect(true);
                                            playerManager.setRoleCooldownNanao(60*5);
                                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§6Vous avez rendu inutilisables les pouvoirs de "
                                                    +targetManager.getPlayerName()+" pendant 3 minutes.");
                                            target.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§6Vos pouvoirs sont inutilisables pendant 3 minutes à cause de§9 Nanao.");
                                            Bukkit.getScheduler().runTaskLater(main, () -> {
                                                bleachTargetManager.setNanaoEffect(false);
                                                target.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§aVous pouvez à nouveau utiliser vos pouvoirs.");
                                            }, 20*60*3);
                                        }
                                    } else {
                                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous devez visé un joueur !");
                                    }
                                } else {
                                    player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownNanao()));
                                }
                            } else {
                                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                            }

                        }
                    }
                }
            }
        }
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
        return "Nanao";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }
}
