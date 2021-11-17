package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.commands.CmdSwitch;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class Trublion extends Role implements LGRole, RoleCommand {

    private boolean teleported = false, switched = false;

    @Override
    public void giveItems(Player player) {
    }

    @Override
    public void onNight(Player player) {
    }

    @Override
    public void onDay(Player player) {
    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {

    }

    @Override
    public void onPlayerDeathRealy(PlayerManager player, ItemStack[] items, ItemStack[] armors, Player killer, Location deathLocation) {
        super.onPlayerDeathRealy(player, items, armors, killer, deathLocation);
        if(player.getRole() instanceof Trublion){
            Trublion trublion = (Trublion) player.getRole();
            if(!trublion.isTeleported()){
                trublion.teleportPower(player.getPlayer());
            }
        }
    }

    @Override
    public String getRoleName() {
        return "Trublion";
    }

    @Override
    public String getDescription() {
        return main.getLGRoleDescription(this,this.getClass().getName());
    }


    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDFiODMwZWI0MDgyYWNlYzgzNmJjODM1ZTQwYTExMjgyYmI1MTE5MzMxNWY5MTE4NDMzN2U4ZDM1NTU1ODMifX19");
    }

    @Override
    public void checkRunnable(Player player) {

    }

    @Override
    public Camps getCamp() {
        return Camps.TRUBLION;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdSwitch(main));
    }

    public void applySwitch(PlayerManager playerManager, PlayerManager targetManager1, PlayerManager targetManager2){
        boolean sameCamp = targetManager1.getCamps() == targetManager2.getCamps();
        Role tempRole = targetManager2.getRole();
        targetManager2.setRole(targetManager1.getRole());
        targetManager1.setRole(tempRole);
        targetManager1.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§eLe trublion vient d'échanger votre rôle. Faites /lg me pour voir votre nouveau rôle.");
        targetManager2.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§eLe trublion vient d'échanger votre rôle. Faites /lg me pour voir votre nouveau rôle.");
        if(sameCamp){
            playerManager.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aLes 2 joueurs que vous avez ciblé sont dans le même camp. Vous gagner avec le village");
            playerManager.setCamps(Camps.VILLAGEOIS);
        } else {
            playerManager.setCamps(Camps.TRUBLION);
            playerManager.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cLes 2 joueurs que vous avez ciblé ne sont pas dans le même camp. Vous gagner seul.");
        }
        setSwitched(true);
    }

    public void setTeleported(boolean teleported) {
        this.teleported = teleported;
    }

    public boolean isTeleported() {
        return teleported;
    }

    public void setSwitched(boolean switched) {
        this.switched = switched;
    }

    public boolean isSwitched() {
        return switched;
    }

    public void teleportPower(Player player) {
        if (main.gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager) {
            LoupGarouManager loupGarouManager = (LoupGarouManager) main.gameManager.getModes().getMode().getModeManager();
            loupGarouManager.teleportAllPlayerExept(player);
            setTeleported(true);
            Bukkit.broadcastMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§eLe Trublion vient de re-téléporter tous les joueurs dans la map.");
        }
    }
}
