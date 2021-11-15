package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.events.player.PlayerKillEvent;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleListener;
import fr.maygo.uhc.modes.naruto.v1.tasks.SakuraTask;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdShosenJutsu;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.KatsuyuItem;
import fr.maygo.uhc.modes.naruto.v2.roles.taka.Sasuke;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Arrays;
import java.util.List;

public class Sakura extends Role implements NarutoV2Role, CmdShosenJutsu.ShosenJutsuUser, RoleCommand, RoleListener {

    private static final double healthWhenSasukeDeath = 4D * 2D;

    private boolean knowSasuke, sasukeKill;

    public Sakura() {
    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {
        Player killer = event.getPlayer();
        PlayerManager PlayerManager = main.getPlayerManager(killer.getUniqueId());
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Sasuke) {
                sasukeKill = true;
            }
        }
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        for (PlayerManager PlayerManager : main.getNarutoV2Manager().getPlayerManagersWithRole(Sasuke.class)) {
            if (PlayerManager.isAlive()) {
                if (PlayerManager.getPlayer() != null) {
                    Player sasuke = PlayerManager.getPlayer();
                    if (player.getLocation().distance(sasuke.getLocation()) <= 20) {
                        if (knowSasuke == false && sasukeKill) {
                            knowSasuke = true;
                            super.addRoleToKnow(Sasuke.class);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§6Sasuke§e a tué quelqu'un et vous venez de le croiser. Voic son identité: " + sasuke.getName());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void giveItems(Player player) {

    }


    @Override
    protected void onNight(Player player) {

    }

    @Override
    public void afterRoles(Player player) {
        new SakuraTask(player.getUniqueId()).runTask(main);
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
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjUxYjc2ZTA2MTJmMTk4NDJmN2JkM2IyMzEyMzU2Y2QxZDRkYzRkN2E0ZWY2OTM1ZTQ4MjY2YjRiMWI0NDcxNSJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Sakura";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (super.getPlayer() != null) {
            Player sakura = super.getPlayer();
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Sasuke) {
                    sakura.setMaxHealth(sakura.getMaxHealth() - healthWhenSasukeDeath);
                    sakura.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Sasuke est mort, vous perdez donc " + (healthWhenSasukeDeath / 2) + " cœurs.");
                } else if (PlayerManager.getRole() instanceof Tsunade) {
                    main.getInventoryUtils().giveItemSafely(sakura, new KatsuyuItem(main).toItemStack());
                    sakura.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Tsunade est mort, vous héritez donc de l'item Katsuyu.");
                }
            }
        }

    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdShosenJutsu(main));
    }

    @Override
    public Chakra getChakra() {
        return Chakra.DOTON;
    }
}
