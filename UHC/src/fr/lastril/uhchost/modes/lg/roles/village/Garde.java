package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.commands.CmdProteger;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Garde extends Role implements LGRole, RoleCommand {

    private final List<PlayerManager> protectedList = new ArrayList<>();
    private boolean protect;

    @Override
    public void giveItems(Player player) {
        ItemStack stack = new Potion(PotionType.NIGHT_VISION).toItemStack(1);
        PotionMeta meta = (PotionMeta) stack.getItemMeta();
        meta.clearCustomEffects();
        meta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*60, 0), true);
        stack.setItemMeta(meta);

        main.getInventoryUtils().giveItemSafely(player, stack);
    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    protected void onDay(Player player) {

    }

    @Override
    public void onNewEpisode(Player player) {
        protect = false;
    }

    @Override
    public void onNewDay(Player player) {

    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNmZDMyMDMyODY2ZThkYTI2ZWVmZGZlOGU5M2Y1MWQ1YTE0MjAzN2YxZGJhMjljNTJjMTYxZDc5YjNmOWYifX19");
    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }

    @Override
    public String getRoleName() {
        return "Garde";
    }

    @Override
    public String getDescription() {
        return main.getLGRoleDescription(this,this.getClass().getName());
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdProteger(main));
    }

    public void addProtected(PlayerManager playerManager){
        protectedList.add(playerManager);
    }

    public boolean alreayProtected(PlayerManager playerManager){
        return protectedList.contains(playerManager);
    }

    public void setProtect(boolean protect) {
        this.protect = protect;
    }

    public boolean hasProtect() {
        return protect;
    }
}
