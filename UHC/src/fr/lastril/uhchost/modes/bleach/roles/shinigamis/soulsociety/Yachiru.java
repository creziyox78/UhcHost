
package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.modes.bleach.commands.CmdSoutien;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.PotionItem;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.List;

public class Yachiru extends Role implements ShinigamiRole, RoleCommand, RoleListener {

    private final int distance = 15;

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        if(playerManager.hasRole() && playerManager.isAlive()){
            if(playerManager.getRole() instanceof Yachiru){
                if(isCloseToKenpachiZaraki(playerManager)){
                    bleachPlayerManager.setResistancePourcentage(10);
                    if(player.hasPotionEffect(PotionEffectType.SPEED))
                        player.removePotionEffect(PotionEffectType.SPEED);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*4, 0, false, false));
                }
                PlayerManager kenpachiZaki = getKenPachiZaraki(playerManager);
                Player kenpachiZakiPlayer = kenpachiZaki.getPlayer();
                if(kenpachiZakiPlayer != null){
                    if(kenpachiZakiPlayer.getWorld() == player.getWorld()){
                        ActionBar.sendMessage(player, "Kenpachi Zaraki §b| " + ClassUtils.getDirectionOf(player.getLocation(), kenpachiZakiPlayer.getLocation()));
                    } else {
                        ActionBar.sendMessage(player, "Kenpachi Zaraki §b| ?");
                    }
                } else {
                    ActionBar.sendMessage(player, "Kenpachi Zaraki §b|§c Déconnecté");
                }
            }
        }
    }

    public boolean isCloseToKenpachiZaraki(PlayerManager playerManager){
        return getKenPachiZaraki(playerManager) != null;
    }

    public PlayerManager getKenPachiZaraki(PlayerManager yachiru){
        Player yachiruPlayer = yachiru.getPlayer();
        PlayerManager kenpachiZaraki = null;
        for(Entity entity : yachiruPlayer.getNearbyEntities(distance, distance, distance)){
            if(entity instanceof Player){
                Player nearPlayer = (Player) entity;
                PlayerManager nearManager = main.getPlayerManager(nearPlayer.getUniqueId());
                if(nearManager.isAlive() && nearManager.hasRole()){
                    if(nearManager.getRole() instanceof KenpachiZaraki){
                        kenpachiZaraki = nearManager;
                    }
                }
            }
        }
        return kenpachiZaraki;
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new PotionItem(PotionType.INSTANT_HEAL, 1).toItemStack(3));
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
        return "Yachiru";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdSoutien(main));
    }


}
