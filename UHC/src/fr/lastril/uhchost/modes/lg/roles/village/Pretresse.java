package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.commands.CmdVoirPretresse;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pretresse extends Role implements LGRole, RoleCommand {

    private int distance = 10;
    private boolean seeRole = true;
    private final List<PlayerManager> playerLgSee = new ArrayList<>();

    @Override
    public void giveItems(Player player) {
        player.getInventory().addItem(new ItemStack(Material.OBSIDIAN, 4));
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
    public void onPlayerDeathRealy(PlayerManager player, ItemStack[] items, ItemStack[] armors, Player killer, Location deathLocation) {
        if(playerLgSee.contains(player)){
            Player pretresse = super.getPlayer();
            if(pretresse != null){
                pretresse.setMaxHealth(pretresse.getMaxHealth() + 2D*1D);
                pretresse.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cUn loup-garou que vous avez espionné est mort. Vous récupérez 1 coeur.");
            }
            playerLgSee.remove(player);
        }
        if(player.hasRole()){
            if(player.getRole() instanceof Pretresse){
                if (main.gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager) {
                    LoupGarouManager loupGarouManager = (LoupGarouManager) main.gameManager.getModes().getMode().getModeManager();
                    loupGarouManager.setLoupValue(80);
                    loupGarouManager.setVillageValue(20);
                }

            }
        }

    }

    @Override
    public void onNewDay(Player player) {
    }

    @Override
    public String getRoleName() {
        return "Prêtresse";
    }

    @Override
    public String getDescription() {
        return main.getLGRoleDescription(this,this.getClass().getName());
    }


    @Override
    public QuickItem getItem(){
        return new QuickItem(Material.SKULL_ITEM, 1,SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTAyZWZmYWNmNTg5NjFjNWRjNzVlOTU2ZWFhY2QzODM1ZDg2OWE4Y2Q1NzM2MDJlZWE0ODMxZTRjZThlZDM3MyJ9fX0=");
    }

    @Override
    public void checkRunnable(Player player) {

    }

    public void addPlayerLg(PlayerManager playerManager){
        playerLgSee.add(playerManager);
    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }


    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdVoirPretresse(main));
    }

    public boolean canSeeRole() {
        return seeRole;
    }

    public void setSeeRole(boolean seeRole) {
        this.seeRole = seeRole;
    }
}
