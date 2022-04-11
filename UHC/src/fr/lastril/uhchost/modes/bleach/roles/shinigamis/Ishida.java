package fr.lastril.uhchost.modes.bleach.roles.shinigamis;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.BleachManager;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Ishida extends Role implements ShinigamiRole, RoleListener {

    private boolean usedInvulnerability = false, inInvulnerability = false;
    private int ticks = 7;

    public Ishida(){
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false ,false), When.START);
    }

    @Override
    public void afterRoles(Player player) {
        player.setMaxHealth(2D*8D);
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.hasRole() && playerManager.getRole() instanceof Ishida){
            Ishida ishida = (Ishida) playerManager.getRole();
            if(ishida.inInvulnerability){
                ticks--;
                if(ticks == 0){
                    if(player.getFoodLevel() - 1 > 1.5){
                        player.setFoodLevel(player.getFoodLevel() - 1);
                    }
                }
                if(player.getFoodLevel() <= 0.5){
                    ishida.inInvulnerability = false;
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous n'êtes plus invulnérable !");
                }
            }
        }
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new QuickItem(Material.ARROW, 64).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new QuickItem(Material.BOW, 1).addEnchant(Enchantment.ARROW_DAMAGE, 4, true).toItemStack());
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
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTY0NWEwYWUyZTU3NmU5MWIzOTg5MTNjMDkwOTVkYmUyMDU2NzczNDA0OGNiNjQwNGQxODNmZjJjOTMyZmU4NiJ9fX0=");

    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Ishida";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @EventHandler
    public void onConsumeItem(PlayerItemConsumeEvent event){
        Player player = event.getPlayer();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.hasRole() && playerManager.getRole() instanceof Ishida){
            Ishida ishida = (Ishida) playerManager.getRole();
            if(ishida.inInvulnerability){
                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§7Vous ne pouvez pas manger car vous êtes invulnérable pour le moment.");
                event.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onDamageByArrow(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof Ishida){
                Ishida ishida = (Ishida) playerManager.getRole();
                if(ishida.inInvulnerability){
                    if(event.getDamager() instanceof Player){
                        Player damager = (Player) event.getDamager();
                        damager.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous ne pouvez pas frappé ce joueur pour le moment.");
                        event.setCancelled(true);
                    }
                }
                if(bleachPlayerManager.canUsePower()) {
                    if(!ishida.usedInvulnerability) {
                        ishida.inInvulnerability = true;
                        event.setCancelled(true);
                    }
                }
            }
            if(event.getDamager() instanceof Arrow){
                Arrow arrow = (Arrow) event.getDamager();
                if(arrow.getShooter() instanceof Player){
                    Player shooter = (Player) arrow.getShooter();
                    PlayerManager shooterManager = main.getPlayerManager(shooter.getUniqueId());
                    BleachPlayerManager bleachShooterManager = shooterManager.getBleachPlayerManager();
                    if(bleachShooterManager.canUsePower()){
                        if(shooterManager.hasRole() && shooterManager.getRole() instanceof Ishida){
                            int chance = UhcHost.getRANDOM().nextInt(100);
                            if(chance < 15){
                                if(player.hasPotionEffect(PotionEffectType.SLOW))
                                    player.removePotionEffect(PotionEffectType.SLOW);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 1, false, false));
                                shooter.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§b" + player.getName() + " a été ralenti.");
                            }
                        }
                    }
                }
            }
        }
    }
}
