package fr.lastril.uhchost.modes.naruto.v2.roles.solo;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdFix;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.ManipulationDuSableItem;
import fr.lastril.uhchost.modes.naruto.v2.items.ShukakuItem;
import fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.SandShape;
import fr.lastril.uhchost.modes.naruto.v2.roles.BijuUser;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Naruto;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Temari;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Gaara extends Role implements NarutoV2Role, BijuUser, RoleCommand, RoleListener {

    private static final int FLY_TIME = 20;

    private Location originalLocation;

    private boolean inShukaku, inArmure, respawned;
    private final List<Block> bouclierBlock;

    private long lastNarutoAttack = 0;
    private boolean damaged;

    private UUID inJump;
    private BukkitTask task;
    private Location jumpLocaton;

    private SandShape sandShape;

    public Gaara() {
        this.bouclierBlock = new ArrayList<>();
        if(main.getGamemanager().getModes() != Modes.NARUTO) return;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        this.jumpLocaton = new Location(narutoV2Manager.getKamuiWorld(), 24972.5, 12, 25058.5, 180.0f, 0.0f);
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new ItemStack(Material.SAND, 128));
        main.getInventoryUtils().giveItemSafely(player, new ShukakuItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new ManipulationDuSableItem(main).toItemStack());
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
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmFkYmQxMTQ0ZDZlZmM5NjMyMzYzOWU1MjUyNmI4MjkwYWQ0ZjA0OGQwMTFkMDRmYzhjMTRmMThmNjYzMTA0YSJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public void afterRoles(Player player) {
        Bukkit.getScheduler().runTaskTimer(main, () -> {
            this.lastNarutoAttack++;
        }, 0, 20);
    }

    @Override
    public Camps getCamp() {
        return Camps.GAARA;
    }

    @Override
    public String getRoleName() {
        return "Gaara";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "naruto.yml");
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

    @Override
    public Chakra getChakra() {
        return Chakra.FUTON;
    }

    public boolean isInShukaku() {
        return inShukaku;
    }

    public void setInShukaku(boolean inShukaku) {
        this.inShukaku = inShukaku;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdFix(main));
    }

    public boolean isThereAnybodyInJump() {
        return this.getInJump() != null;
    }

    public UUID getInJump() {
        return inJump;
    }

    public void teleportInJump(Player player) {
        this.inJump = player.getUniqueId();
        this.originalLocation = player.getLocation();
        player.teleport(this.jumpLocaton);
        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous vous êtes téléporté dans le jump de Gaara.");

        this.task = new BukkitRunnable() {

            int timer = 60 * 2;

            @Override
            public void run() {
                Player players = Bukkit.getPlayer(player.getUniqueId());
                if (players != null) {
                    ActionBar.sendMessage(players, "§9Jump §7: §3" + new FormatTime(timer));
                    if (players.getWorld() != Gaara.this.jumpLocaton.getWorld()) {

                        originalLocation.getChunk().load();
                        players.teleport(originalLocation);
                        players.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez été retéléporté dans le monde normal.");
                        cancel();
                    } else {
                        if (players.getLocation().getY() < 7) {
                            players.teleport(Gaara.this.jumpLocaton);
                        }
                    }
                }

                if (timer == 0) {
                    if (players != null) {
                        if (players.getWorld() == Gaara.this.jumpLocaton.getWorld()) {
                            originalLocation.getChunk().load();
                            players.teleport(originalLocation);
                            players.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez été retéléporté dans le monde normal.");
                        }
                    }
                    Gaara.this.inJump = null;
                    Gaara.this.originalLocation = null;
                    cancel();
                }

                timer--;
            }
        }.runTaskTimer(main, 0, 20);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block != null) {
            if (block.getWorld() == this.jumpLocaton.getWorld()) {
                if (block.getType() == Material.GOLD_PLATE) {
                    if (this.inJump != null) {
                        if (player.getUniqueId().equals(this.inJump)) {
                            originalLocation.getChunk().load();
                            player.teleport(originalLocation);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez terminé le jump, vous avez donc été retéléporté dans le monde normal.");
                            this.task.cancel();
                            this.task = null;
                            this.inJump = null;
                            this.originalLocation = null;
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if(main.getGamemanager().getModes() != Modes.NARUTO) return;
            NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            if (joueur.hasRole()) {
                if (joueur.getRole() instanceof Gaara) {
                    Gaara gaara = (Gaara) joueur.getRole();
                    if (player.getHealth() - event.getFinalDamage() <= 0.1 && gaara.mustRespawn()) {
                        if(gaara.mustRespawn() && !isRespawned()){
                            event.setCancelled(true);
                            setRespawned(true);
                            player.setHealth(player.getMaxHealth());
                            int x = UhcHost.getRANDOM().nextInt(60)- 30;
                            int z = UhcHost.getRANDOM().nextInt(60)- 30;
                            Location loc = new Location(player.getWorld(), player.getLocation().getBlockX() + x, 110, player.getLocation().getBlockZ() + z);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous êtes mort, mais Naruto vous a tappé dans les 30 dernières secondes. Vous gagnez donc avec les Shinobis.");
                            for(PlayerManager naruto : narutoV2Manager.getPlayerManagersWithRole(Naruto.class)){
                                if(naruto.getPlayer() != null){
                                    naruto.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eGaara§a est mort mais vous l'aviez tappé dans les 30 secondes précédant sa mort, il ressuscite et gagne donc avec les Shinobis.");
                                }
                            }
                            for(PlayerManager temari : narutoV2Manager.getPlayerManagersWithRole(Temari.class)){
                                if(temari.getPlayer() != null){
                                    temari.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eGaara§a vient de passer dans le camp des Shinobis.");
                                }
                            }
                            joueur.setCamps(Camps.SHINOBI);
                            player.teleport(loc);
                            narutoV2Manager.getNofallJoueur().add(joueur);
                            Bukkit.getScheduler().runTaskLater(main, () -> {
                                narutoV2Manager.getNofallJoueur().remove(joueur);
                                player.sendMessage("§7Vous n'êtes plus invincible.");
                            }, 20*10);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBreakBouclier(BlockDamageEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(bouclierBlock.contains(block)){
            if(joueur.hasRole()){
                if(joueur.getRole() instanceof Gaara){
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20*2, 1, false, false));
                }
            }
        }
    }

    @EventHandler
    public void onBreakBouclier(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(bouclierBlock.contains(block)){
            bouclierBlock.remove(block);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamageByPlayer(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if(main.getGamemanager().getModes() != Modes.NARUTO) return;
            NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            if(joueur.hasRole()){
                if(joueur.getRole() instanceof Gaara){
                    Gaara gaara = (Gaara) joueur.getRole();
                    if(gaara.mustRespawn() && !isRespawned()){
                        if(player.getHealth() - event.getFinalDamage() <= 0.1 && gaara.mustRespawn()) {
                            event.setCancelled(true);
                            setRespawned(true);
                            player.setHealth(player.getMaxHealth());
                            int x = UhcHost.getRANDOM().nextInt(60)- 30;
                            int z = UhcHost.getRANDOM().nextInt(60)- 30;
                            Location loc = new Location(player.getWorld(), player.getLocation().getBlockX() + x, 110, player.getLocation().getBlockZ() + z);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous êtes mort, mais Naruto vous a tappé dans les 30 dernières secondes. Vous gagnez donc avec les Shinobis.");
                            for(PlayerManager naruto : narutoV2Manager.getPlayerManagersWithRole(Naruto.class)){
                                if(naruto.getPlayer() != null){
                                    naruto.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eGaara§a est mort mais vous l'aviez tappé dans les 30 secondes précédant sa mort, il ressuscite et gagne donc avec les Shinobis.");
                                }
                            }
                            for(PlayerManager naruto : narutoV2Manager.getPlayerManagersWithRole(Temari.class)){
                                if(naruto.getPlayer() != null){
                                    naruto.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eGaara§a est mort mais Naruto l'a tappé dans les 30 secondes précédant sa mort, il ressuscite et gagne donc avec les Shinobis.");
                                }
                            }
                            joueur.setCamps(Camps.SHINOBI);
                            player.teleport(loc);
                            narutoV2Manager.getNofallJoueur().add(joueur);
                            Bukkit.getScheduler().runTaskLater(main, () -> {
                                narutoV2Manager.getNofallJoueur().remove(joueur);
                                player.sendMessage("§7Vous n'êtes plus invincible.");
                            }, 20*10);
                        }
                    }
                }
            }
            if(event.getDamager() instanceof Player){
                if(joueur.hasRole()){
                    if(joueur.getRole() instanceof Gaara){
                        Gaara gaara = (Gaara) joueur.getRole();
                        if(gaara.isInArmure()){
                            if(getSandsInInventory(player.getInventory()) < 5){
                                if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
                                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                                }
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVotre armure de sable§c ne fais plus§e effet car vous n'avez plus assez de sable. Vous devez réactivez votre pouvoir pour qu'il fasse effet.");
                                inArmure = false;
                            } else {
                                removeSandsInInventory(player.getInventory(), 5);
                            }
                        }
                        setDamaged(true);
                        Bukkit.getScheduler().runTaskLater(main, () -> {
                            setDamaged(false);
                        }, 10L);
                    }
                }
            }
        }

    }

    public Location getJumpLocaton() {
        return jumpLocaton;
    }

    public int getSandsInInventory(PlayerInventory playerInventory) {
        int sands = 0;
        for (ItemStack item : playerInventory.getContents()) {
            if (item != null) {
                if (item.getType() == Material.SAND) {
                    sands += item.getAmount();
                }
            }
        }
        return sands;
    }

    public void removeSandsInInventory(PlayerInventory inventory, int amount) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (is.getType() == Material.SAND) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }

    public SandShape getSandShape() {
        return sandShape;
    }

    public void setSandShape(SandShape sandShape) {
        this.sandShape = sandShape;
    }

    public static int getFlyTime() {
        return FLY_TIME;
    }

    public boolean mustRespawn(){
        return this.lastNarutoAttack < 30;
    }

    @Override
    public void onDamage(Player damager, Player target) {
        PlayerManager damagerJoueur = main.getPlayerManager(damager.getUniqueId());
        if (damagerJoueur.isAlive()) {
            if (damagerJoueur.getRole() instanceof Naruto) {
                this.lastNarutoAttack = 0;
            }
        }
    }

    public boolean isInArmure() {
        return inArmure;
    }

    public void setInArmure(boolean inArmure) {
        this.inArmure = inArmure;
    }

    public List<Block> getBouclierBlock() {
        return bouclierBlock;
    }

    public boolean isDamaged() {
        return damaged;
    }

    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    public boolean isRespawned() {
        return respawned;
    }

    public void setRespawned(boolean respawned) {
        this.respawned = respawned;
    }
}
