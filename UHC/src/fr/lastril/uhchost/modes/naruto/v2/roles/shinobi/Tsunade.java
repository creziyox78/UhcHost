package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.ByakugoItem;
import fr.lastril.uhchost.modes.naruto.v2.items.KatsuyuItem;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Tsunade extends Role implements NarutoV2Role, RoleListener {

    private double healthLost = 0;
	private final int chance = 5;
    private final int maxHealthLost = 30;

	private boolean useByakugo;

    public Tsunade() {
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new ByakugoItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new KatsuyuItem(main).toItemStack());
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(joueur.hasRole()){
            if(joueur.getRole() instanceof Tsunade){
                Tsunade tsunade = (Tsunade) joueur.getRole();
                if(joueur.getPlayer() != null) {
                    if(tsunade.isUseByakugo()){
                        ActionBar.sendMessage(joueur.getPlayer(), "§dDégâts reçu: " + tsunade.getHealthLost() + "/" + maxHealthLost);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamageWithPower(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.getRole() instanceof Tsunade) {
                Tsunade tsunade = (Tsunade) joueur.getRole();
                if (tsunade.isUseByakugo()) {
                    tsunade.healthLost += event.getFinalDamage();
                    if (tsunade.healthLost >= maxHealthLost) {
                        tsunade.healthLost = 0;
                        tsunade.setUseByakugo(false);
                        player.setMaxHealth(player.getMaxHealth() - 4);
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                player.setMaxHealth(player.getMaxHealth() + 4);
                            }
                        }.runTaskLater(main, 20 * 60 * 15);

                        if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                            player.removePotionEffect(PotionEffectType.REGENERATION);
                        }
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§6Vous venez de perdre l'effet de byakugô.");
                        joueur.setRoleCooldownByakugo(20*60);
                        joueur.sendTimer(player, joueur.getRoleCooldownByakugo(), new ByakugoItem(main).toItemStack());
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
    	if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
    		Player target = (Player) event.getEntity();
    		Player damager = (Player) event.getDamager();
    		PlayerManager damagerJoueur = main.getPlayerManager(damager.getUniqueId());
    		if(damagerJoueur.getRole() instanceof Tsunade) {
    			int value = UhcHost.getRANDOM().nextInt(100);
    			UhcHost.debug("Chance Tsunade: " + value);
    			if(value <= chance) {
    				target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*3, 0, false, false));
    				target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*3, 3, false, false));
    				target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous venez de vous faire étourdir.");
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2I0MTM3Mjc4ZGY3NmRiM2M0MzU1MTVmNDdlYTc4NGE2ODc2M2I5MGJmZDE3YjdmYWU1NGZhNWZlYWJkNDhlOSJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Tsunade";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

    public boolean isUseByakugo(){
        return useByakugo;
    }

    public void setUseByakugo(boolean useByakugo){
        this.useByakugo = useByakugo;
    }

    public double getHealthLost() {
        return healthLost;
    }

    @Override
	public Chakra getChakra() {
		return Chakra.DOTON;
	}
}
