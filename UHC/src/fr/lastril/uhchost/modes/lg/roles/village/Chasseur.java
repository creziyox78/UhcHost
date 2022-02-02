package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.commands.CmdShot;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.Oeuf;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class Chasseur extends Role implements LGRole, RoleCommand {

    private boolean shot;
    private int reminingTime = 30;

    @Override
    public void giveItems(Player player) {
        player.getInventory().addItem(
                new QuickItem(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 4, true).toItemStack(),
                new ItemStack(Material.ARROW, 128),
                new ItemStack(Material.BONE, 15),
                new Oeuf(EntityType.WOLF).toItemStack(3));
    }

    @Override
    public void onNight(Player player) {
    }

    @Override
    public void onDay(Player player) {
    }

    @Override
    public void onPlayerDeathRealy(PlayerManager player, ItemStack[] items, ItemStack[] armors, Player killer, Location deathLocation) {
        if(player.hasRole()){
            if(player.getRole() instanceof Chasseur){
                TextComponent message = new TextComponent("§aTir : " + Messages.CLICK_HERE.getMessage());
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg tir"));
                Bukkit.getScheduler().runTaskLater(main, () -> setShot(true), 20*reminingTime);
                player.getPlayer().sendMessage(message.getText());
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
        return Arrays.asList(new CmdShot(main));
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

}
