package fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleListener;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdBakuhatsu;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.ShikigamiNoMaiItem;
import fr.maygo.uhc.modes.naruto.v2.roles.jubi.Obito;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Konan extends Role implements NarutoV2Role, RoleListener, RoleCommand {

    private static final int CHISSOKU_DISTANCE = 20, CHISSOKU_STUN_TIME = 5, FLY_TIME = 8, SPECTATOR_TIME = 45, EXPLOSIONS_TIME = 60;

    private final List<PlayerManager> akatsukiList = new ArrayList<>();

    public Konan() {
        super.addRoleToKnow(Nagato.class);
    }

    /**
     * @return
     * @deprecated {@link Konan#getChissokuDistance()}
     */
    @Deprecated
    public static int getDistanceChissoku() {
        return CHISSOKU_DISTANCE;
    }

    public static int getChissokuDistance() {
        return CHISSOKU_DISTANCE;
    }

    public static int getChissokuStunTime() {
        return CHISSOKU_STUN_TIME;
    }

    public static int getFlyTime() {
        return FLY_TIME;
    }

    public static int getSpectatorTime() {
        return SPECTATOR_TIME;
    }

    public static int getExplosionsTime() {
        return EXPLOSIONS_TIME;
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new ShikigamiNoMaiItem(main).toItemStack());
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
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmViMjVhZmM1NzJkNDk4MDcyMTg0NTNiYzQ2ZDBjZmQ5NzFlODYwYjRhMmQ5NTY3NGRmYWM0Zjk2MGM1MjM2MCJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.AKATSUKI;
    }

    @Override
    public String getRoleName() {
        return "Konan";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    @Override
    public void afterRoles(Player player) {
        player.sendMessage(sendList());
    }

    @Override
    public String sendList() {
        String list = Messages.NARUTO_PREFIX.getMessage() + "Voici la liste entière de l'Akatsuki : \n";
        for (PlayerManager PlayerManager : UhcHost.getInstance().getNarutoManager().getPlayerManagersWithCamps(Camps.AKATSUKI)) {
            akatsukiList.add(PlayerManager);
        }
        for (PlayerManager PlayerManager : main.getNarutoV2Manager().getPlayerManagersWithRole(Obito.class)) {
            akatsukiList.add(PlayerManager);
        }
        int numberOfElements = akatsukiList.size();
        for (int i = 0; i < numberOfElements; i++) {
            int index = UhcHost.getRandom().nextInt(akatsukiList.size());
            list += "§c- " + akatsukiList.get(index).getPlayerName() + "\n";
            akatsukiList.remove(index);
        }

        return list;
    }

    @Override
    public Chakra getChakra() {
        return null;
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == DamageCause.FALL) {
            Player player = (Player) event.getEntity();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole() && PlayerManager.getRole() instanceof Konan) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onRepairYuri(InventoryClickEvent e) {
        if (e.getInventory().getType().equals(InventoryType.ANVIL)) {
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                if (e.getCurrentItem().hasItemMeta()) {
                    if (e.getCurrentItem().getItemMeta().hasDisplayName()) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cYari")) {
                            e.setResult(Event.Result.DENY);
                            e.setCancelled(true);
                            e.getWhoClicked().sendMessage(Messages.error("Vous ne pouvez pas réparer Yari."));
                        }
                    }
                }
            }
        }

    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdBakuhatsu(main));
    }
}
