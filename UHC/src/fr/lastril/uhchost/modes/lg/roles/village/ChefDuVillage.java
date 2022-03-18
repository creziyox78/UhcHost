package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.commands.CmdCancelVoteChef;
import fr.lastril.uhchost.modes.lg.commands.CmdEnquete;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.items.Livre;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChefDuVillage extends Role implements LGRole, RoleCommand {

    private boolean cancelledVote, used, enquete;
    private PlayerManager villagerManager, enqueted;
    private int timer = 5*60, ticks = 20;

    @Override
    public void afterRoles(Player player) {

        List<PlayerManager> villagers = main.getPlayerManagerAlives().stream().filter(playerManager -> playerManager.getCamps() == Camps.VILLAGEOIS).collect(Collectors.toList());
        if(!villagers.isEmpty() && villagers.size() != 1){
            villagers.remove(main.getPlayerManager(player.getUniqueId()));
            villagerManager = villagers.get(UhcHost.getRANDOM().nextInt(villagers.size()));
        }
    }

    @Override
    public void giveItems(Player player) {

    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        ticks--;
        if(ticks == 0){
            if(enqueted != null){
                Player enquetedPlayer = enqueted.getPlayer();
                if(enquetedPlayer != null){
                    if(player.getWorld() == enquetedPlayer.getWorld()){
                        if(player.getLocation().distance(enquetedPlayer.getLocation()) <= 30){
                            timer--;
                        }
                    }
                }
                if(timer == 0){
                    player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aVous avez terminé votre enquête, le rôle du joueur fait partie de la liste suivante: " + sendEnqueteResult() + "\n.§a Vous pourrez enquêter à nouveau un joueur d'ici 20 minutes.");
                    enqueted = null;
                    Bukkit.getScheduler().runTaskLater(main, () -> {
                        if(player != null){
                            setEnquete(false);
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aVous pouvez à nouveau enquêter un joueur.");
                        }
                    }, 20*20*60);
                }
                ActionBar.sendMessage(player, "§aEnquête sur " + enqueted.getPlayerName() + " §7:§e " + new FormatTime(timer));
            }
            ticks = 20;
        }
    }

    private String sendEnqueteResult(){
        int size = 3;
        LoupGarouManager loupGarouManager = (LoupGarouManager) main.getGamemanager().getModes().getMode().getModeManager();
        List<Role> roles = loupGarouManager.getPlayerRoleWithRandomRoles(enqueted, size);
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int index = UhcHost.getRANDOM().nextInt(roles.size());
            message.append(roles.get(index).getRoleName()).append(", ");
            roles.remove(index);
        }
        return message.toString();
    }

    @Override
    public String sendList() {
        if(villagerManager != null){
            return "Voici le pseudo d'un villageois: " + villagerManager.getPlayerName();
        }
        return null;
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
    public String getRoleName() {
        return "Chef du Village";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "lg.yml");
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE5NTUxNjhlZjUzZjcxMjBjMDg5ZGFmZTNlNmU0MzdlOTUyNDA1NTVkOGMzYWNjZjk0NGQ2YzU2Yjc0MDQ3NSJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }

    public void setCancelledVote(boolean cancelledVote) {
        this.cancelledVote = cancelledVote;
    }

    public boolean hasCancelledVote() {
        return cancelledVote;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isUsed() {
        return used;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdCancelVoteChef(main), new CmdEnquete(main));
    }

    public PlayerManager getEnqueted() {
        return enqueted;
    }

    public void setEnqueted(PlayerManager enqueted) {
        this.enqueted = enqueted;
    }

    public boolean hasEnquete() {
        return enquete;
    }

    public void setEnquete(boolean enquete) {
        this.enquete = enquete;
    }
}
