package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.commands.CmdSarbacane;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.Livre;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Indigene extends Role implements LGRole, RoleCommand {

    private boolean sarbacaned;
    private PlayerManager targetManager;

    public Indigene(){
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Livre(Enchantment.ARROW_DAMAGE, 3).toItemStack());
    }

    @Override
    public void onNight(Player player) {
    }

    @Override
    public void onDay(Player player) {
    }

    @Override
    public void onNewEpisode(Player player) {
        if(getTargetManager() != null && getTargetManager().getWolfPlayerManager().isSarbacaned()){
            getTargetManager().getWolfPlayerManager().setSarbacaned(false);
            if(getTargetManager().getPlayer() != null){
                getTargetManager().getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§bL'effet de la sarbacane s'estompe...");
            }
        }
    }

    @Override
    public void onNewDay(Player player) {
    }

    @Override
    public String getRoleName() {
        return "Indigène";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "lg.yml");
    }


    @Override
    public QuickItem getItem() {
        return null;
        //return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDFiODMwZWI0MDgyYWNlYzgzNmJjODM1ZTQwYTExMjgyYmI1MTE5MzMxNWY5MTE4NDMzN2U4ZDM1NTU1ODMifX19");
    }

    @Override
    public void checkRunnable(Player player) {

    }

    public void setSarbacaned(boolean sarbacaned) {
        this.sarbacaned = sarbacaned;
    }

    public boolean hasSarbacaned() {
        return sarbacaned;
    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdSarbacane(main));
    }

    public PlayerManager getTargetManager() {
        return targetManager;
    }

    public void setTargetManager(PlayerManager targetManager) {
        this.targetManager = targetManager;
    }
}
