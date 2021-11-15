package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleCommand;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdPaume;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdSmell;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.KuramaItem;
import fr.maygo.uhc.modes.naruto.v2.items.RasenganItem;
import fr.maygo.uhc.modes.naruto.v2.items.SenjutsuItem;
import fr.maygo.uhc.modes.naruto.v2.roles.BijuUser;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Naruto extends Role implements NarutoV2Role, RoleCommand, KuramaItem.KuramaUser, SenjutsuItem.SenjutsuUser, RasenganItem.RasenganUser, BijuUser {

    private static final double healthWhenJirayaDeath = 3D * 2D;
    private boolean canUseKurama, attackBoosted, jirayaDeath = false;
    private int paumeUses;

    public Naruto() {
        super.addRoleToKnow(Jiraya.class);
        Consumer<Player> coordsEvent = event -> {
            for (PlayerManager akatsuki : UhcHost.getInstance().getNarutoV2Manager().getPlayerManagersWithCamps(Camps.AKATSUKI)) {
                if (akatsuki.getPlayer() != null) {
                    Player akatsukiPlayer = akatsuki.getPlayer();
                    String message = Messages.NARUTO_PREFIX.getMessage() + "Voici les coordonées de Naruto :\n";
                    message += "§ex: " + event.getLocation().getBlockX() + "\n";
                    message += "§ey: " + event.getLocation().getBlockY() + "\n";
                    message += "§ez: " + event.getLocation().getBlockZ();
                    akatsukiPlayer.sendMessage(message);
                }
            }
        };
        super.addTimeEvent(30 * 60, coordsEvent);
        super.addTimeEvent(40 * 60, coordsEvent);
    }

    @Override
    public String getRoleName() {
        return "Naruto";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new KuramaItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new SenjutsuItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new RasenganItem(main).toItemStack());
    }

    @Override
    public void afterRoles(Player player) {
        player.setMaxHealth(24);
        player.setHealth(player.getMaxHealth());
    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    protected void onDay(Player player) {

    }

    @Override
    public void onNewDay(Player player) {

    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName())
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA2ZTM0YzFjOTRjN2I4MjljNTlkMDFjNzQ1M2Q1ZjNlODI1OWYzODljMmFjYTJmYTMxNGRjYTQwODY5M2NlIn19fQ==");
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public void onNewEpisode(Player player) {
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdSmell(UhcHost.getInstance()), new CmdPaume(UhcHost.getInstance()));
    }

    @Override
    public void setCanUseSmell(boolean state) {
        this.canUseKurama = state;
    }

    @Override
    public boolean canUseSmell() {
        return canUseKurama;
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Jiraya) {
                if (super.getPlayer() != null) {
                    if (!isJirayaDeath()) {
                        setJirayaDeath(true);
                        Player naruto = super.getPlayer();
                        naruto.setMaxHealth(naruto.getMaxHealth() + healthWhenJirayaDeath);
                        naruto.setHealth(naruto.getHealth() + (3D * 2D));
                        naruto.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Jiraya est mort, vous obtenez donc " + (healthWhenJirayaDeath / 2) + " cœurs supplémentaires.");
                    }
                }
            }
        }
    }

    @Override
    public boolean isAttackBoosted() {
        return attackBoosted;
    }

    @Override
    public void setAttackBoosted(boolean state) {
        this.attackBoosted = state;
    }

    public boolean canUsePaume() {
        return paumeUses < 2;
    }

    public void usePaume() {
        paumeUses++;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.FUTON;
    }

    public boolean isJirayaDeath() {
        return jirayaDeath;
    }

    public void setJirayaDeath(boolean jirayaDeath) {
        this.jirayaDeath = jirayaDeath;
    }
}
