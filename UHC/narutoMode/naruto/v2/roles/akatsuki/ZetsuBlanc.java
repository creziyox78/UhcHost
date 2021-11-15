package fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki;

import com.mojang.authlib.properties.Property;
import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.IdentityChanger;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleListener;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdMetamorphose;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdReset;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.obj.PlayerManager;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.List;

public class ZetsuBlanc extends Role implements NarutoV2Role, RoleListener, RoleCommand {

    private static final int METAMORPHOSE_TIME = 5 * 60;

    private final Chakra chakra = getRandomChakra();

    private boolean hasReset;

    private String originalName, copiedName;
    private Property originalSkin, copiedSkin;

    public static int getMetamorphoseTime() {
        return METAMORPHOSE_TIME;
    }

    @Override
    public void giveItems(Player player) {
        this.originalName = player.getName();
        this.originalSkin = main.getAPI().getNpcManager().getPlayerTextures(player);
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
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjE2NGNjNzYwODE1OTlhMjBhNjg5NmEzODE4MzIyYTBjZGMwODA2OWZmOTYwYmJkNzlmZjU0ZmM0NWUzNWVlNiJ9fX0=");
    }

    @Override
    public void afterRoles(Player player) {
        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Voici votre nature de chakra : " + StringUtils.capitalize(getChakra().toString().toLowerCase()));
    }

    @Override
    public Camps getCamp() {
        return Camps.AKATSUKI;
    }

    @Override
    public String getRoleName() {
        return "Zetsu Blanc";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    @Override
    public Chakra getChakra() {
        return chakra;
    }

    public String getCopiedName() {
        return copiedName;
    }

    public void setCopiedName(String copiedName) {
        this.copiedName = copiedName;
    }

    public Property getCopiedSkin() {
        return copiedSkin;
    }

    public void setCopiedSkin(Property copiedSkin) {
        this.copiedSkin = copiedSkin;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Property getOriginalSkin() {
        return originalSkin;
    }

    public void setOriginalSkin(Property originalSkin) {
        this.originalSkin = originalSkin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.hasRole() && PlayerManager.getRole() instanceof ZetsuBlanc) {
            ZetsuBlanc zetsuBlanc = (ZetsuBlanc) PlayerManager.getRole();

            if (zetsuBlanc.getCopiedName() != null)
                IdentityChanger.changePlayerName(player, zetsuBlanc.getCopiedName());
            if (zetsuBlanc.getCopiedSkin() != null)
                IdentityChanger.changeSkin(player, zetsuBlanc.getCopiedSkin(), false);
        }
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdMetamorphose(main), new CmdReset(main));
    }

    public boolean hasReset() {
        return hasReset;
    }

    public void setHasReset(boolean hasReset) {
        this.hasReset = hasReset;
    }
}
