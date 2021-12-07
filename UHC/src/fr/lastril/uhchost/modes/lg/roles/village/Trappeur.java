package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.commands.CmdTrapper;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class Trappeur extends Role implements LGRole, RoleCommand {

    private PlayerManager tracked;
    private boolean change;

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
        if(change){
            change = false;
            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§bVous pouvez changer de cible si vous le souhaitez avec la commande /lg trapper <pseudo>.");
        }
    }

    @Override
    public void onNewDay(Player player) {
    }

    @Override
    public String getRoleName() {
        return "Trappeur";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this,this.getClass().getName());
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE5NTUxNjhlZjUzZjcxMjBjMDg5ZGFmZTNlNmU0MzdlOTUyNDA1NTVkOGMzYWNjZjk0NGQ2YzU2Yjc0MDQ3NSJ9fX0=");
    }

    @Override
    public void checkRunnable(Player player) {
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.hasRole() && playerManager.isAlive()){
            if(playerManager.getRole() instanceof Trappeur) {
                Trappeur trappeur = (Trappeur) playerManager.getRole();
                PlayerManager tracked = trappeur.getTracked();
                if(tracked != null && tracked.isAlive()){
                    ActionBar.sendMessage(player, ClassUtils.getDirectionOf(player.getLocation(), tracked.getPlayer().getLocation()));
                }
            }
        }
    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }

    public PlayerManager getTracked() {
        return tracked;
    }

    public void setTracked(PlayerManager tracked) {
        this.tracked = tracked;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdTrapper(main));
    }

    public boolean canChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }
}
