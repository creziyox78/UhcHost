package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleCommand;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdYameru;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.KamuiItem;
import fr.maygo.uhc.modes.naruto.v2.items.PakkunItem;
import fr.maygo.uhc.modes.naruto.v2.items.SharinganItem;
import fr.maygo.uhc.modes.naruto.v2.tasks.SharinganTask;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;

import java.util.*;

public class Kakashi extends Role implements NarutoV2Role, KamuiItem.KamuiUser, RoleCommand {

    public static final int DISTANCE_COPY = 20;
    public static final int COPY_POINTS = 2500;
    private final List<UUID> copieds;
    private final Map<UUID, Location> intialLocations;
    private UUID copying, actualCopy;
    private int copyPoints;

    public Kakashi() {
        this.copieds = new ArrayList<>();
        this.intialLocations = new HashMap<>();
    }

    @Override
    public void giveItems(Player player) {
        new SharinganTask(main, this).runTaskTimer(main, 0, 20);
        main.getInventoryUtils().giveItemSafely(player, new SharinganItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new PakkunItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new KamuiItem(main).toItemStack());
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWQyYWQ4NGI5YjRhMTljOTE4ZGVkOTc5YWVlNDAxNjg5ZThiY2ZiODU4Njk5NmQ3ZmExMTRlNWZhMmJlOGJlIn19fQ==")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Kakashi";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    public UUID getCopying() {
        return copying;
    }

    public void setCopying(UUID copying) {
        this.copying = copying;
    }

    public List<UUID> getCopieds() {
        return copieds;
    }

    public int getCopyPoints() {
        return copyPoints;
    }

    public void setCopyPoints(int copyPoints) {
        this.copyPoints = copyPoints;
    }

    public void addCopyPoints(int points) {
        this.copyPoints += points;
    }

    public UUID getActualCopy() {
        return actualCopy;
    }

    public void setActualCopy(UUID actualCopy) {
        this.actualCopy = actualCopy;
    }

    @Override
    public int getArimasuCooldown() {
        return 15 * 60;
    }

    @Override
    public int getSonohokaCooldown() {
        return 30 * 60;
    }

    @Override
    public Map<UUID, Location> getInitialsLocation() {
        return this.intialLocations;
    }

    @Override
    public double getSonohokaDistance() {
        return 20;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdYameru(main));
    }

    @Override
    public Chakra getChakra() {
        return Chakra.RAITON;
    }
}
