package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.commands.chasseur.CmdChasseurInspect;
import fr.lastril.uhchost.modes.lg.commands.chasseur.CmdShot;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.Livre;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chasseur extends Role implements LGRole, RoleCommand {

    private boolean shot;
    private final int reminingTime = 30;
    private final List<PlayerManager> playersInvestigated = new ArrayList<>();

    @Override
    public void giveItems(Player player) {
        player.getInventory().addItem(
                new Livre(Enchantment.ARROW_DAMAGE, 3).toItemStack(),
                new QuickItem(Material.STRING, 3).toItemStack(),
                new ItemStack(Material.ARROW, 64));
    }

    @Override
    public void onNight(Player player) {
    }

    @Override
    public void onDay(Player player) {
    }

    @Override
    public void onPlayerDeathRealy(PlayerManager player, ItemStack[] items, ItemStack[] armors, Player killer, Location deathLocation) {
        UhcHost.debug("Chasseur checking...");
        if(main.getGamemanager().getModes().getMode().getModeManager() instanceof LoupGarouManager){
            LoupGarouManager loupGarouManager = (LoupGarouManager) main.getGamemanager().getModes().getMode().getModeManager();
            if(player.hasRole()){
                UhcHost.debug("Chasseur checking role...");
                if(player.getRole() instanceof Chasseur){
                    UhcHost.debug("Player is chasseur, sended !");
                    TextComponent message = new TextComponent("§aTir : " + Messages.CLICK_HERE.getMessage());
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg tir"));
                    Bukkit.getScheduler().runTaskLater(main, () -> setShot(true), 20*reminingTime);
                    player.getPlayer().sendMessage(message.getText());
                }
                if(loupGarouManager.isRandomSeeRole()){
                    Player chasseur = super.getPlayer();
                    if(chasseur != null){
                        if(chasseur.getLocation().distance(player.getDeathLocation()) <= 50){
                            if(playersInvestigated.size() < 3){
                                TextComponent message = new TextComponent("§aUn joueur vient de mourir dans un rayon de 50 blocs : " + Messages.CLICK_HERE.getMessage());
                                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg chasseur_inspect " + player.getPlayerName()));
                                chasseur.spigot().sendMessage(message);
                            }
                        }
                    }
                }
            }
        }


    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {
    }

    @Override
    public String getRoleName() {
        return "Chasseur";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "lg.yml");
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTA2YzE2ODE3YzczZmY2NGE0YTQ5YjU5MGQyY2RiMjViY2ZhNTJjNjMwZmU3MjgxYTE3N2VhYmFjZGFhODU3YiJ9fX0=");
    }

    @Override
    public void checkRunnable(Player player) {

    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdShot(main), new CmdChasseurInspect(main));
    }

    public void setShot(boolean shot) {
        this.shot = shot;
    }

    public boolean isShot() {
        return shot;
    }

    public void shot(Player player){
        if(player.getHealth() - 2*6 <= 0){
            player.setHealth(0.5);
        } else {
            player.setHealth(player.getHealth() - 2*6);
        }
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§eLe chasseur vient de tirer sur " + player.getName() + ". Il perd 6 coeurs.");
        Bukkit.broadcastMessage(" ");
        setShot(true);
    }

    public void sendInvestigateMessage(Player player, PlayerManager targetManager){
        if(!playersInvestigated.contains(targetManager)){
            List<Role> investigateRoles = new ArrayList<>();
            investigateRoles.add(targetManager.getRole());
            playersInvestigated.add(targetManager);
            main.getPlayerManagerAlives().forEach(playerManager -> {
                    Role role = playerManager.getRole();
                    if(!role.getRoleName().equalsIgnoreCase(targetManager.getRole().getRoleName()) && !investigateRoles.contains(role))
                        investigateRoles.add(role);
            });
            StringBuilder message = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                int index = UhcHost.getRANDOM().nextInt(investigateRoles.size());
                message.append(investigateRoles.get(index).getRoleName()).append(", ");
                investigateRoles.remove(index);
            }
            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§2Son rôle est présente dans la liste suivante: " + message);
        } else {
            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous avez déjà utiliser votre pouvoir sur ce joueur !");
        }

    }

}
