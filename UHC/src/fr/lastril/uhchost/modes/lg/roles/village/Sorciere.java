package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.ResurectType;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.clickable_messages.ClickableMessage;
import fr.lastril.uhchost.tools.API.items.PotionItem;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionType;

public class Sorciere extends Role implements LGRole, RoleListener {

    private boolean hasRez;

    @Override
    public void giveItems(Player player) {
        player.getInventory().addItem(new PotionItem(PotionType.INSTANT_HEAL, 1, true).toItemStack(3),
                new PotionItem(PotionType.REGEN, 1, true).toItemStack(1),
                new PotionItem(PotionType.INSTANT_DAMAGE, 1, true).toItemStack(3));
    }

    @Override
    public void onNight(Player player) {
    }

    @Override
    public void onDay(Player player) {
    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {
    }

    @Override
    public String getRoleName() {
        return "Sorcière";
    }

    @Override
    public String getDescription() {
        return main.getLGRoleDescription(this,this.getClass().getName());
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName("§a" + getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBlMTNkMTg0NzRmYzk0ZWQ1NWFlYjcwNjk1NjZlNDY4N2Q3NzNkYWMxNmY0YzNmODcyMmZjOTViZjlmMmRmYSJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!hasRez) {
            Player killer = event.getEntity().getKiller();
            Player player = event.getEntity();
            if (killer != null) {
                UhcHost main = UhcHost.getInstance();
                PlayerManager playerManagerKiller = main.getPlayerManager(killer.getUniqueId());
                PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                if (playerManagerKiller.hasRole() && playerManager.hasRole()) {
                    if (playerManager.getRole() instanceof Ancien) {
                        Ancien ancien = (Ancien) playerManager.getRole();
                        if (!ancien.isRevived()) {
                            return;
                        }
                    }

                }
            }
            if (super.getPlayer() != null) {
                Player soso = super.getPlayer();
                new ClickableMessage(soso, onClick -> {
                    main.getPlayerManager(player.getUniqueId()).getWolfPlayerManager()
                            .setResurectType(ResurectType.SORCIERE);
                    onClick.sendMessage("Vous avez bien ressuscité "
                            + player.getName() + " !");
                    hasRez = true;
                }, "§c" + player.getName()
                        + " est mort vous pouvez le ressusciter en cliquant sur le message ! ",
                        "§a Pour ressusciter §c" + player.getName());
            }
        }
    }

    public String getSkullValue() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjU1MjU2ODkyM2U1NDU2YWIzMWRhOThlYzMyN2RiMGFjMDY5YTY4NzZkZTRhOWZkZDZlMGJjNWQwMTI3YzMxOSJ9fX0=";
    }

}
