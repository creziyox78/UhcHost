
package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.commands.CmdAshisogi;
import fr.lastril.uhchost.modes.bleach.commands.CmdReviveNemu;
import fr.lastril.uhchost.modes.bleach.items.Shikai;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.TextComponentBuilder;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.Format;
import java.util.Arrays;
import java.util.List;

public class Mayuri extends Role implements ShinigamiRole, RoleListener, RoleCommand {

    private boolean reanimedNemu;

    private boolean inShikai, spawnedWither;
    private Wither wither;
    private int messageCooldown = 0;

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Shikai(main).toItemStack());
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.isAlive()){
            if(playerManager.hasRole() && playerManager.getRole() instanceof Mayuri){
                Mayuri mayuri = (Mayuri) playerManager.getRole();
                if(!mayuri.hasReanimedNemu()){
                    for(PlayerManager nemus : main.getGamemanager().getModes().getMode().getModeManager().getPlayerManagersWithRole(Nemu.class)){
                        Nemu nemu = (Nemu) nemus.getRole();
                        Player nemuPlayer = nemus.getPlayer();
                        if(nemu.isDead() && nemuPlayer != null){
                            messageCooldown--;
                            ActionBar.sendMessage(player, "§9Nemu (" + new FormatTime(nemu.getDeathTime()) + ") : " + ClassUtils.getDirectionOf(player.getLocation(), nemuPlayer.getLocation()));
                            if(player.getLocation().distance(nemus.getDeathLocation()) <= 5){
                                if(messageCooldown <= 0){
                                    messageCooldown = 20*5;
                                    sendMessageRevive(player);
                                }
                            }
                        }
                    }
                }
            }
        }


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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                .setName(getCamp().getCompoColor() + getRoleName())
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ2ODBjMGFjN2YzMWFiZWZlNjVhMWU2M2YwODJhMTQ5NDFhMDU1NzQxMDE0NWM1MWEyZDQ2OTEwYTNhZmRiNCJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Mayuri";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdAshisogi(main), new CmdReviveNemu(main));
    }

    @EventHandler
    public void onDamageInShikai(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            PlayerManager damagerManager = main.getPlayerManager(damager.getUniqueId());
            if(damagerManager.hasRole() && damagerManager.getRole() instanceof Mayuri){
                Mayuri mayuri = (Mayuri) damagerManager.getRole();
                if(mayuri.inShikai){
                    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20*5, 0, false, false));
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§9Mayuri§e vous inflige un effet de§2 poison§e et de§8 slowness§e avec son Shikai.");
                }
            }
        }
    }



    @EventHandler
    public void onWitherShoot(EntityExplodeEvent event){
        if(event.getEntity() instanceof WitherSkull){
            WitherSkull witherSkull = (WitherSkull) event.getEntity();
            Entity shooter = (Entity) witherSkull.getShooter();
            if(shooter instanceof Wither){
                for(Entity entity : witherSkull.getNearbyEntities(2, 2, 2)){
                    if(entity instanceof Player){
                        Player player = (Player) entity;
                        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20*5, 0));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 0));
                    }
                }
            }

        }
    }

    @EventHandler
    public void onTargetSummoner(EntityTargetEvent event){
        UhcHost.debug("Entity:" + event.getEntity().getName() + " Target:" + event.getTarget().getName());
        if(event.getTarget() instanceof Player && event.getEntity() instanceof Wither){
            Wither entity = (Wither) event.getEntity();
            if(entity == wither){
                Player player = (Player) event.getTarget();
                UhcHost.debug("Target: " + player.getName());
                if(super.getPlayer() != null){
                    if(player == super.getPlayer()){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    private void sendMessageRevive(Player player){
        TextComponent textComponent = new TextComponent(Messages.BLEACH_PREFIX.getMessage() + "§aRéssusciter Nemu : ");
        textComponent.addExtra(new TextComponent(new TextComponentBuilder("§a[Cliquez ici]").setClickEvent(ClickEvent.Action.RUN_COMMAND, "/b revive_nemu").toText()));
        player.spigot().sendMessage(textComponent);
    }

    public void spawnWither(Player player){
        wither = player.getWorld().spawn(player.getLocation(), Wither.class);
        wither.setCustomName("§6Ashisogi");
        wither.setCustomNameVisible(true);
        wither.setMaxHealth(60);
        wither.setHealth(60);
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) wither).getHandle();
        NBTTagCompound tag = nmsEntity.getNBTTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        nmsEntity.c(tag);
        tag.setInt("Invul", 600);
        nmsEntity.f(tag);
        Bukkit.getScheduler().runTaskLater(main, () -> {
            if(!wither.isDead()){
                wither.remove();
            }
        } , 20*5*60);
    }

    public void setInShikai(boolean inShikai) {
        this.inShikai = inShikai;
    }

    public boolean isSpawnedWither() {
        return spawnedWither;
    }

    public void setSpawnedWither(boolean spawnedWither) {
        this.spawnedWither = spawnedWither;
    }

    public boolean hasReanimedNemu() {
        return reanimedNemu;
    }

    public void setReanimedNemu(boolean reanimedNemu) {
        this.reanimedNemu = reanimedNemu;
    }

    public void reviveNemu() {
        if(!reanimedNemu){
            if(super.getPlayer() != null){
                Player player = super.getPlayer();
                PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                if(playerManager.hasRole() && playerManager.getRole() instanceof Mayuri){
                    Mayuri mayuri = (Mayuri) playerManager.getRole();
                    for(PlayerManager nemus : main.getGamemanager().getModes().getMode().getModeManager().getPlayerManagersWithRole(Nemu.class)){
                        if(nemus.getPlayer() != null){
                            nemus.getPlayer().setHealth(20);
                            nemus.getPlayer().setFoodLevel(20);
                            nemus.getPlayer().setFireTicks(0);
                            nemus.getPlayer().setSaturation(20);
                            nemus.getPlayer().setGameMode(GameMode.SURVIVAL);
                            nemus.getPlayer().setExhaustion(0);
                            nemus.getPlayer().teleport(nemus.getDeathLocation());
                            Nemu nemu = (Nemu) nemus.getRole();
                            nemu.destroyFakePlayer();
                            nemu.setDead(false);
                            mayuri.setReanimedNemu(true);
                        }
                    }
                }
            }

        }
    }
}
