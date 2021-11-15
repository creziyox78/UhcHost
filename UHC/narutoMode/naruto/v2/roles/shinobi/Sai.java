package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleListener;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.ToileItem;
import fr.maygo.uhc.modes.naruto.v2.roles.taka.Sasuke;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.UUID;

public class Sai extends Role implements NarutoV2Role, RoleListener {

    private boolean hasUsedFuinjutsu;
    private UUID scelled;
    private int horseID = -1;

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new ToileItem(main, this).toItemStack());
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
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWY2NGRhZWUzNTVkNTMxNDI0OWQyYTgzMTZmNjU5ZjEyZjU1MzdjNzdjMjE0ZDJkYzI1MDc1MmU1YWY4M2NjMyJ9fX0");
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Saï";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {
    }

    @Override
    public void onKill(OfflinePlayer player, Player killer) {
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.getRole() instanceof Sasuke) {
            killer.setMaxHealth(killer.getMaxHealth() + (2D * 2D));
            killer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez tué Sasuke, par conséquant, vous obtenez 2 cœurs supplémentaires.");
            if (!main.getNarutoManager().getPlayerManagersWithRole(Sakura.class).isEmpty()) {
                killer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVoici l'identité de Sakura :");
                for (PlayerManager PlayerManagerHasRole : main.getNarutoManager().getPlayerManagersWithRole(Sakura.class)) {
                    killer.sendMessage("§6- " + PlayerManagerHasRole.getPlayerName());
                }
            }
        }
    }

    @EventHandler
    public void onSilverFishDamageSai(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (event.getDamager() instanceof Silverfish) {
                Silverfish silverfish = (Silverfish) event.getDamager();
                if (PlayerManager.hasRole()) {
                    if (PlayerManager.getRole() instanceof Sai) {
                        if (silverfish.getCustomName().equalsIgnoreCase("§7Poisson d'argents")) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }


    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.getMumbleData().getMumbleUser() != null) {
            if (player.getHealth() <= (2D * 2D)) {
                if (PlayerManager.getMumbleData().getMumbleUser() != null) {
                    if (!PlayerManager.getMumbleData().getMumbleUser().isMuted()) {
                        PlayerManager.getMumbleData().getMumbleUser().mute(true);
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous être trop affaibli pour parler.");
                    }

                }
            } else {
                if (PlayerManager.getMumbleData().getMumbleUser().isMuted()) {
                    PlayerManager.getMumbleData().getMumbleUser().mute(false);
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous avez retrouvez votre parole.");
                }
            }
        }


        if (this.scelled != null) {
            Player scelled = Bukkit.getPlayer(this.scelled);
            if (scelled != null) {
                if (player.getLocation().distance(scelled.getLocation()) > 30) {
                    scelled.teleport(player.getLocation());
                }
            }
        }
    }

    public boolean hasUsedFuinjutsu() {
        return hasUsedFuinjutsu;
    }

    public void setHasUsedFuinjutsu(boolean hasUsedFuinjutsu) {
        this.hasUsedFuinjutsu = hasUsedFuinjutsu;
    }

    public UUID getScelled() {
        return scelled;
    }

    public void setScelled(UUID scelled) {
        this.scelled = scelled;
    }

    @Override
    public void onDead(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (this.scelled != null) {
            Player scelled = Bukkit.getPlayer(this.scelled);
            if (scelled != null) {
                PlayerManager scelledPlayerManager = main.getPlayerManager(scelled.getUniqueId());
                scelledPlayerManager.setAlive(true);
                scelledPlayerManager.setSpectator(false);
                scelled.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aSaï est mort, vous êtes libéré !");
                scelled.setGameMode(GameMode.SURVIVAL);
                scelled.teleport(player.getLocation());
                main.getMumbleManager().onPlayerRevive(scelledPlayerManager);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (scelledPlayerManager.getMumbleData().getMumbleUser() != null) {
                            scelledPlayerManager.getMumbleData().getMumbleUser().mute(false);
                        }
                    }
                }.runTaskLater(main, 25);

            }
        }
    }

    @EventHandler
    public void onPlayerMountHorse(EntityMountEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (event.getMount().getEntityId() == this.horseID) {
                if (!(PlayerManager.getRole() instanceof Sai)) {
                    player.sendMessage(Messages.error("Vous ne pouvez pas monter le cheval de Saï !"));
                    event.setCancelled(true);
                }
            }
        }
    }

    public int getHorseID() {
        return horseID;
    }

    public void setHorseID(int horseID) {
        this.horseID = horseID;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.SUITON;
    }
}
