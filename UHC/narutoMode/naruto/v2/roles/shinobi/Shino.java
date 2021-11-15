package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleListener;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdFeed;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdTracking;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.KikaichuItem;
import fr.maygo.uhc.modes.naruto.v2.tasks.TrackingTask;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Shino extends Role implements NarutoV2Role, RoleListener, RoleCommand {

    public static final int MAX_TRACKS = 5;
    public static final int HEALTH_STOLE = 1;

    private final Map<UUID, Boolean> linkeds;
    private final List<UUID> trackeds;

    public Shino() {
        this.linkeds = new HashMap<>();
        this.trackeds = new ArrayList<>();
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new KikaichuItem().toItemStack());

        new TrackingTask(main, this).runTaskTimer(main, 0, 20);
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
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWNkY2E0OGY0YTZlYTg4ZGE4YTJmZmQ0MDQ2MGZiNmFhNWFlZThlNjZkYzdiZWVmODU0MGRlNmQ4NmU2N2M0YyJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Shino";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    @EventHandler
    public void onPlayerInteratAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof Player) {
            Player target = (Player) event.getRightClicked();
            ItemStack item = player.getItemInHand();
            if (item != null) {
                if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                    if (item.getItemMeta().getDisplayName().equals(new KikaichuItem().getName())) {
                        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
                        if (PlayerManager.hasRole()) {
                            if (PlayerManager.getRole() instanceof Shino) {
                                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                                    if (this.linkeds.size() < MAX_TRACKS) {
                                        if (!this.linkeds.containsKey(target.getUniqueId())) {
                                            this.linkeds.put(target.getUniqueId(), true);

                                            if (PlayerManager.getRole() instanceof NarutoV2Role) {
                                                NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                                                narutoRole.usePower(PlayerManager);
                                            }

                                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez infiltré " + target.getName() + " avec votre " + new KikaichuItem().getName() + ".");
                                        } else {
                                            player.sendMessage(Messages.error("Vous avez déjà utilisé votre pouvoir sur " + target.getName() + " !"));
                                            return;
                                        }
                                    } else {
                                        player.sendMessage(Messages.error("Vous avez atteint votre limite de " + new KikaichuItem().getName() + "§c (" + MAX_TRACKS + ") !"));
                                        return;
                                    }
                                } else {
                                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                                    return;
                                }
                            } else {
                                player.sendMessage(Messages.not(this.getRoleName()));
                                return;
                            }
                        } else {
                            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player target = (Player) event.getEntity();

            if (this.linkeds.containsKey(target.getUniqueId())) {
                if (this.linkeds.get(target.getUniqueId())) {
                    if (event.getDamager() instanceof Player) {
                        Player damager = (Player) event.getDamager();
                        this.sendInfoToShino(target.getName() + " se fait attaquer par " + damager.getName());
                    } else {
                        this.sendInfoToShino(target.getName() + " se fait attaquer par PVE");
                    }
                }
            }

            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                if (this.linkeds.containsKey(damager.getUniqueId())) {
                    if (this.linkeds.get(damager.getUniqueId())) {
                        this.sendInfoToShino(damager.getName() + " attaque " + target.getName());
                    }
                }
            }

        }
    }

    @EventHandler
    public void onPlayerConsumeGapple(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (this.linkeds.containsKey(player.getUniqueId())) {
            if (this.linkeds.get(player.getUniqueId())) {
                if (event.getItem().getType() == Material.GOLDEN_APPLE) {
                    this.sendInfoToShino(player.getName() + " mange une pomme en or");
                }
            }
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity(), killer = player.getKiller();

        if (this.linkeds.containsKey(player.getUniqueId())) {
            if (this.linkeds.get(player.getUniqueId())) {
                if (killer != null) {
                    this.sendInfoToShino(player.getName() + " à été tué par " + killer.getName());
                } else {
                    this.sendInfoToShino(player.getName() + " à été tué par PVE");
                }
            }
        }
    }

    public void sendInfoToShino(String message) {
        if (super.getPlayer() != null) {
            Player shino = super.getPlayer();
            shino.sendMessage("§8[§a§lKikaichû§8] §7" + message + "§7.");
        }
    }

    public Map<UUID, Boolean> getLinkeds() {
        return linkeds;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdTracking(main), new CmdFeed(main));
    }

    public List<UUID> getTrackeds() {
        return trackeds;
    }

    @Override
    public Chakra getChakra() {
        return null;
    }
}
