package fr.lastril.uhchost.modes.lg.roles.solo;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.commands.trublion.CmdDispersion;
import fr.lastril.uhchost.modes.lg.commands.trublion.CmdSwitch;
import fr.lastril.uhchost.modes.lg.commands.trublion.CmdTp;
import fr.lastril.uhchost.modes.lg.roles.LGChatRole;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.When;
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

public class Trublion extends Role implements LGRole, RoleCommand, LGChatRole {

    private boolean teleported = false, switched = false;
    private int teleportedSpecificPlayer = 0;
    private Camps switchedCampsResult = Camps.NEUTRES;

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
        return main.getRoleDescription(this, this.getClass().getName(), "lg.yml");
    }


    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2FkZDczODkyMjFhZTM0MDZhYzA3ODU3MmE5MWQyOTU1NDcyN2NmYWJiNzNjYjM3Y2MyZDMyYzVlZmUzNjQ3MyJ9fX0=");
    }

    @Override
    public void checkRunnable(Player player) {

    }

    @Override
    public Camps getCamp() {
        return Camps.NEUTRES;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdSwitch(main), new CmdTp(main), new CmdDispersion(main));
    }

    public void applySwitch(PlayerManager playerManager, PlayerManager targetManager1, PlayerManager targetManager2){
        boolean sameCamp = targetManager1.getCamps() == targetManager2.getCamps();

        Role tempRole = targetManager2.getRole();
        tempRole.setPlayerID(targetManager1.getUuid());
        targetManager2.setRole(targetManager1.getRole());
        targetManager1.setRole(tempRole);
        targetManager2.getRole().setPlayerID(targetManager2.getUuid());
        targetManager1.getPlayer().setMaxHealth(20);
        targetManager2.getPlayer().setMaxHealth(20);
        targetManager1.getRole().afterRoles(targetManager1.getPlayer());
        targetManager2.getRole().afterRoles(targetManager2.getPlayer());
        targetManager1.getPlayer().getActivePotionEffects().forEach(potionEffect -> targetManager1.getPlayer().removePotionEffect(potionEffect.getType()));
        targetManager2.getPlayer().getActivePotionEffects().forEach(potionEffect -> targetManager2.getPlayer().removePotionEffect(potionEffect.getType()));

        targetManager2.getRole().getEffects().entrySet().stream().filter(e -> e.getValue() == When.START).forEach(e -> targetManager1.getPlayer().addPotionEffect(e.getKey()));
        targetManager1.getRole().getEffects().entrySet().stream().filter(e -> e.getValue() == When.START).forEach(e -> targetManager2.getPlayer().addPotionEffect(e.getKey()));
        targetManager1.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§eLe trublion vient d'échanger votre rôle. Faites /lg me pour voir votre nouveau rôle.");
        targetManager2.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§eLe trublion vient d'échanger votre rôle. Faites /lg me pour voir votre nouveau rôle.");
        if(sameCamp){
            UhcHost.debug("Trublion switch 2 players in same camps.");
            playerManager.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aLes 2 joueurs que vous avez ciblé sont dans le même camp. Vous gagner avec le village");
            setSwitchedCampsResult(Camps.VILLAGEOIS);
            playerManager.setCamps(Camps.VILLAGEOIS);
        } else {
            UhcHost.debug("Trublion switch 2 players in differents camps.");
            playerManager.setCamps(Camps.TRUBLION);
            setSwitchedCampsResult(Camps.TRUBLION);
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

    @Override
    public boolean canSee() {
        return false;
    }

    @Override
    public boolean canSend() {
        return true;
    }

    @Override
    public boolean sendPlayerName() {
        return false;
    }

    public void setTeleportedSpecificPlayer(int teleportedSpecificPlayer) {
        this.teleportedSpecificPlayer = teleportedSpecificPlayer;
    }

    public int getTeleportedSpecificPlayer() {
        return teleportedSpecificPlayer;
    }

    public void setSwitchedCampsResult(Camps switchedCampsResult) {
        this.switchedCampsResult = switchedCampsResult;
    }

    public Camps getSwitchedCampsResult() {
        return switchedCampsResult;
    }
}
