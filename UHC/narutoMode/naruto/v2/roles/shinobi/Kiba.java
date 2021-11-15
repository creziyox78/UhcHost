package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.*;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdSniff;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.AkamaruItem;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Kiba extends Role implements NarutoV2Role, RoleListener, RoleCommand {

    private final int distanceSniff = 20;
    private Wolf wolf;
    private boolean canSniff = true;
    private UUID playerSniffed;

    public Kiba() {
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @EventHandler
    public void onHitWolf(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Wolf && event.getDamager() instanceof LivingEntity) {
            if (this.wolf != null) {
                if (event.getEntity() == wolf) {
                    wolf.setTarget((LivingEntity) event.getDamager());
                }
            }
        }
    }


    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new AkamaruItem().toItemStack());
    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    protected void onDay(Player player) {

    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {

    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName())
                .setTexture(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjk1ZTQ1ZWE4OGQ4OWQ2NGI3ZGQ5ZmQ3NmMxM2UwMWNhMTNmYTEwNDBhOTY0NmE3NjE4MTlmZjlkYzg0ZmUifX19");
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Kiba";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    public boolean isCanSniff() {
        return canSniff;
    }

    public void setCanSniff(boolean canSniff) {
        this.canSniff = canSniff;
    }

    public UUID getPlayerSniffed() {
        return playerSniffed;
    }

    public void setPlayerSniffed(UUID playerSniffed) {
        this.playerSniffed = playerSniffed;
    }

    public int getDistanceSniff() {
        return distanceSniff;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdSniff(main));
    }

    @Override
    public Chakra getChakra() {
        return Chakra.DOTON;
    }
}
