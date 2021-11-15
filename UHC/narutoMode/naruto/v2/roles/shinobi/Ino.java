package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.WorldUtils;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleListener;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdChat;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdTransfer;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.InoItem;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Ino extends Role implements NarutoV2Role, RoleCommand, RoleListener {

    private static final int TIME_AFTER_TRANSFER = 20 * 60;
    private static final int WRITE_MESSAGE_TIME = 2 * 60;
    private static final int MESSAGE_DISTANCE = 200;

    private final List<UUID> inChat;

    private UUID transfered;
    private long transferTime, deathTime;
    private Location transferDeathLocation;

    public Ino() {
        super.addRoleToKnow(Shikamaru.class);

        this.inChat = new ArrayList<>();
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new InoItem(main).toItemStack());
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
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDUzNWI5MWZkOTgxODFiYjY2YThlOGY0NzAwNGJkNzA0YjUzNzBkZTViOTUzOTE3YmI4MmNlZDhjOTI1NjdmMyJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Ino";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    public List<UUID> getInChat() {
        return inChat;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdChat(main), new CmdTransfer(main));
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (this.transfered != null && this.transfered.equals(player.getUniqueId())) {
            long diffTime = System.currentTimeMillis() - this.transferTime;
            UhcHost.debug("transfer for " + player.getName() + " (" + this.transfered + ") time: " + (diffTime / 1000) + " seconds");
            if (diffTime < TIME_AFTER_TRANSFER * 1000) {
                //AVANT 20 MINS
                this.deathTime = System.currentTimeMillis();
                this.transferDeathLocation = player.getLocation();
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous disposez de " + (WRITE_MESSAGE_TIME / 60) + " minutes pour écrire un message.");
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (this.transfered != null && this.transfered.equals(player.getUniqueId())) {
            if (this.transferDeathLocation != null) {
                long diffTime = System.currentTimeMillis() - this.deathTime;
                UhcHost.debug("transfer chat for " + player.getName() + " (" + this.transfered + ") time: " + (diffTime / 1000) + " seconds");
                if (diffTime < WRITE_MESSAGE_TIME * 1000) {

                    player.sendMessage("§7" + player.getName() + " » " + event.getMessage());

                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (WorldUtils.getDistanceBetweenTwoLocations(transferDeathLocation, players.getLocation()) <= MESSAGE_DISTANCE) {
                            players.sendMessage("§7" + player.getName() + " » " + event.getMessage());
                        }
                    }

                    event.setCancelled(true);
                    this.transferDeathLocation = null;
                }
            }
        }
    }

    public UUID getTransfered() {
        return transfered;
    }

    public void setTransfered(UUID transfered) {
        this.transfered = transfered;
        this.transferTime = System.currentTimeMillis();
    }

    @Override
    public Chakra getChakra() {
        return Chakra.DOTON;
    }
}
