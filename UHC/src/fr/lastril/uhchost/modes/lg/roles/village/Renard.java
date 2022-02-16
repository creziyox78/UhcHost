package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.commands.CmdFlairer;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renard extends Role implements LGRole, RoleCommand {

    private final int maxRenifled = 3;
    private boolean canRenifle;
    private int renifledUse = 0;
    private final List<PlayerManager> renifledPlayer = new ArrayList<>();

    public Renard() {
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    public String getSkullValue() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdjZmM4MzI5NTdiZDU4NTczODA4YWE3ZjdkZTc1MWY1ZGM5NWIzNDU2MTkxNmE4YjgzOTU3N2M1YjVjMTAifX19";
    }

    @Override
    public void giveItems(Player player) {
    }

    @Override
    public void onNight(Player player) {
        canRenifle = true;
    }

    @Override
    public void onDay(Player player) {
        renifledPlayer.forEach(playerManager -> {
            if(playerManager.getPlayer() != null){
                playerManager.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§eVous avez été reniflé par le renard.");
            }
        });
        renifledPlayer.stream().map(renifledPlayer::remove);
        canRenifle = false;
    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {
    }

    @Override
    public String getRoleName() {
        return "Renard";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "lg.yml");
    }


    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDk5OTM0Y2RmYTkwZTZiNGE0MDAzMzk2YmJiZmU5MTk5N2VkYTFhYzA0NWRmM2IyMjEzZjM2NzA2ZjMxMjZjMiJ9fX0=");
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
        return Collections.singletonList(new CmdFlairer(main));
    }

    public boolean isCanRenifle() {
        return canRenifle;
    }

    public void addUse() {
        renifledUse++;
    }

    public boolean notReached() {
        return renifledUse < maxRenifled;
    }

    public void addRenifled(PlayerManager playerManager){
        renifledPlayer.add(playerManager);
    }

}
