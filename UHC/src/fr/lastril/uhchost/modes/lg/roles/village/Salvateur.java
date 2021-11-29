package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.commands.CmdSalvation;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.tools.API.items.PotionItem;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionType;

import java.util.Collections;
import java.util.List;

public class Salvateur extends Role implements LGRole, RoleCommand {

    private boolean salvate = false;

    public String getSkullValue() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg5MjFhZTQ0ZjBhN2FlMjZhOGFjMWVhZWU2ZjA5ZWI2NjhkZjI4NzI4YWZiYWEzMThlZmQ4ZmVkYmRmNjkifX19";
    }

    @Override
    public void giveItems(Player player) {
        player.getInventory().addItem(new PotionItem(PotionType.INSTANT_HEAL, 1, true).toItemStack(2));
    }

    @Override
    public void onNight(Player player) {
    }

    @Override
    public void onDay(Player player) {
        salvate = false;
        player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§BVous avez 2 minutes pour choisir la personne que vous souhaiter protéger.");
    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {
    }

    @Override
    public String getRoleName() {
        return "Salvateur";
    }

    @Override
    public String getDescription() {
        return main.getLGRoleDescription(this,this.getClass().getName());
    }


    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGM4NTBkZTYzYWI0YmZiNDg0NTdiMWM3NTY2OWQ1YjdjNDliNWUwOGRhMjk5NzdiYjgwZGJjNDNkMjlkNzc2ZiJ9fX0=");
    }

    @Override
    public void checkRunnable(Player player) {

    }

    public boolean isSalvate() {
        return salvate;
    }

    public void setSalvate(boolean salvate) {
        this.salvate = salvate;
    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Collections.singletonList(new CmdSalvation(main));
    }
}
