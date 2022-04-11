package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.commands.CmdHiver;
import fr.lastril.uhchost.modes.bleach.items.Hyorinmaru;
import fr.lastril.uhchost.modes.bleach.items.Ryusenka;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.Cuboid;
import fr.lastril.uhchost.tools.API.items.Livre;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class ToshiroHitsugaya extends Role implements ShinigamiRole, RoleCommand, RoleListener {

    //private final EnumParticle e = EnumParticle.FIREWORKS_SPARK;

    //public List<Arrow> arrowTrailList = new ArrayList<>();

    private boolean inRyusenka;
    private Player damaged;
    private double healthDamaged;
    private int hiverUse;
    private Cuboid hiverSpace;

    public ToshiroHitsugaya() {
        super.addEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new QuickItem(Material.PACKED_ICE, 48).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Livre(Enchantment.DEPTH_STRIDER, 2).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Hyorinmaru(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Ryusenka(main).toItemStack());
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        /*for (Arrow arrow : this.arrowTrailList) {
            setParticles(arrow.getLocation());
        }*/
        if(player.getFireTicks() > 0){
            if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
        if(hiverSpace != null){
            if(hiverSpace.contains(player)){
                if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*3, 0, false, false));
            }
        }
        Location location = player.getLocation().add(0, -1, 0);
        if(location.getBlock().getType() == Material.PACKED_ICE || location.getBlock().getType() == Material.ICE){
            if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*3, 0, false, false));
        }
    }


    /*@EventHandler
    private void onShootSpecialArrow(EntityShootBowEvent event) {
        if ((event.getEntity() instanceof Player) || !(event.getProjectile() instanceof Arrow)){
            Player player = (Player)event.getEntity();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if(playerManager.hasRole() && playerManager.getRole() instanceof ToshiroHitsugaya){
                Arrow arrow = (Arrow) event.getProjectile();
                addArrowTrailList(arrow);
            }
        }
    }

    @EventHandler
    private void onArrowTouch(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow)event.getEntity();
            arrow.remove();
            removeArrowTrailList(arrow);
        }

    }*/

    @EventHandler
    public void onDamageByArrow(EntityDamageByEntityEvent event){
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    Player damager = (Player) arrow.getShooter();
                    PlayerManager damagerJoueur = main.getPlayerManager(damager.getUniqueId());
                    if(damagerJoueur.hasRole()) {
                        if(damagerJoueur.getRole() instanceof ToshiroHitsugaya) {
                            int value = UhcHost.getRANDOM().nextInt(100);
                            if(value <= 25) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 0, false, false));
                                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§9Hitsugaya§b vient de vous touché avec une flèche gelé. Vous recevez§7 Slowness 1§b.");
                                damager.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§bVous venez de touché " + player.getName() + " avec une flèche gelé !");
                            }
                        }
                    }
                }
            }

            if(event.getDamager() instanceof Player){
                Player damager = (Player) event.getDamager();
                PlayerManager damagerManager = main.getPlayerManager(damager.getUniqueId());
                if(damagerManager.hasRole() && damagerManager.getRole() instanceof ToshiroHitsugaya){
                    damaged = player;
                    UhcHost.debug("damager : " + damager.getName() + " player : " + damaged.getName());
                    healthDamaged += event.getFinalDamage();
                    Bukkit.getScheduler().runTaskLater(main, () -> {
                        healthDamaged -= event.getFinalDamage();
                    },20*5);
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
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjJhODc3MTJkYWM0YzQ1MWEwNmUxZjU2MmY0ODgzMmNiZjhmNjk2ZGM2ZWJiYzY2NTM1YTAyNzFkMDlhNzQ3NiJ9fX0=");

    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Toshiro Hitsugaya";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdHiver(main));
    }

    public void addHiverUse(){
        hiverUse++;
    }

    public boolean hasReachedUse(){
        return hiverUse >= 2;
    }

    public void setHiverSpace(Cuboid hiverSpace) {
        this.hiverSpace = hiverSpace;
    }

    /*public void setParticles(Location loc) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(this.e, true, (float)loc.getX(),
                (float)loc.getY(), (float)loc.getZ(), 0.0F, 0.0F, 0.0F, 0.0F, 15, null);
        for (Player p : Bukkit.getOnlinePlayers())
            (((CraftPlayer)p).getHandle()).playerConnection.sendPacket(packet);
    }

    public void addArrowTrailList(Arrow arrow) {
        this.arrowTrailList.add(arrow);
    }

    public void removeArrowTrailList(Arrow arrow) {
        this.arrowTrailList.remove(arrow);
    }*/

    public void setInRyusenka(boolean inRyusenka) {
        this.inRyusenka = inRyusenka;
    }

    public void applyDamages(){
        double damages = healthDamaged*0.2;
        if(damaged.getHealth() - damages <= 0){
            damaged.setHealth(0.1);
        } else {
            damaged.setHealth(damaged.getHealth() - damages);
        }
        healthDamaged = 0;
        damaged = null;
    }

    public Player getDamaged() {
        return damaged;
    }

    public double getHealthDamaged() {
        return healthDamaged;
    }
}
