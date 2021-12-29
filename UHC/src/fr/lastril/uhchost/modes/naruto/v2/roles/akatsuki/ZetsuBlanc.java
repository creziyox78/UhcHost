package fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki;

import com.mojang.authlib.properties.Property;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdMetamorphose;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdReset;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.List;

public class ZetsuBlanc extends Role implements NarutoV2Role, RoleListener, RoleCommand {

    private static final int METAMORPHOSE_TIME = 5*60;

    private Chakra chakra = getRandomChakra();

    private boolean hasReset;

    private String originalName, copiedName;
    private Property originalSkin, copiedSkin;

    @Override
    public void giveItems(Player player) {
        this.originalName = player.getName();
        //this.originalSkin = main.getAPI().getNpcManager().getPlayerTextures(player);
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
        return main.getRoleDescription(this, this.getClass().getName());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }
    
    @Override
	public Chakra getChakra() {
		return chakra;
	}

    public static int getMetamorphoseTime() {
        return METAMORPHOSE_TIME;
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
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(joueur.hasRole() && joueur.getRole() instanceof ZetsuBlanc){
            ZetsuBlanc zetsuBlanc = (ZetsuBlanc) joueur.getRole();

            //if(zetsuBlanc.getCopiedName() != null) IdentityChanger.changePlayerName(player, zetsuBlanc.getCopiedName());
            //if(zetsuBlanc.getCopiedSkin() != null) IdentityChanger.changeSkin(player, zetsuBlanc.getCopiedSkin(), false);
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
