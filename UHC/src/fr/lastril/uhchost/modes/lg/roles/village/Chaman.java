package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.commands.CmdChaman;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.TextComponentBuilder;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chaman extends Role implements LGRole, RoleCommand {

    private List<PlayerManager> playersSpec = new ArrayList<>();

    public void addPlayerSpec(PlayerManager playerManager){
        playersSpec.add(playerManager);
    }

    public boolean containsPlayerSpec(PlayerManager playerManager){
        return playersSpec.contains(playerManager);
    }

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
        Player chaman = super.getPlayer();
        if(chaman != null){
            Bukkit.getScheduler().runTaskLater(main, () -> {
                if(!containsPlayerSpec(player)){
                    addPlayerSpec(player);
                    chaman.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§c");
                }
            }, 20*30);
            TextComponent textComponent = new TextComponent(Messages.LOUP_GAROU_PREFIX.getMessage() + "§7§oRecevoir des informations: ");
            textComponent.addExtra(new TextComponent(new TextComponentBuilder("§a§l[Clique ici]").setClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg chaman " + player.getPlayerName()).toText()));
            chaman.spigot().sendMessage(textComponent);
        }

    }

    @Override
    public String getRoleName() {
        return "Chaman";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this,this.getClass().getName());
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJlNmU0NTEwMGQzM2RjMDcyMDE4YzU1OGFiNDkyNTU1ZGE1NDRkYjJjNDBkNjRhMjY2ZTJlNTlkNzUwZjY0NiJ9fX0=");
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
        return Arrays.asList(new CmdChaman(main));
    }
}
