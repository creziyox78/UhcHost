package fr.lastril.uhchost.modes.naruto.v2.roles.solo;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdHokage;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdIzanagi;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.PlayerKillEvent;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Danzo extends Role implements NarutoV2Role, RoleCommand, RoleListener, CmdIzanagi.IzanagiUser {

	private boolean weakHokage, usedIzanagi;

	private int life = 1;

	public Danzo() {
		super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
		super.addEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), When.START);
		super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
	}

    @Override
    public void giveItems(Player player) {
    	QuickItem epee = new QuickItem(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 4, true);
    	main.getInventoryUtils().giveItemSafely(player, epee.toItemStack());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
   	public void onPlayerDamage(EntityDamageEvent event) {
    	if(event.getEntity() instanceof Player) {
    		Player player = (Player) event.getEntity();
			PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
			if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
			NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			if(joueur.hasRole()) {
				if(joueur.getRole() instanceof Danzo) {
					Danzo danzo = (Danzo) joueur.getRole();
					if(player.getHealth() - event.getFinalDamage() <= 0.1 && !narutoV2Manager.getNofallJoueur().contains(joueur)) {
						if(danzo.getLife() >= 1) {
							danzo.setLife(danzo.getLife() - 1);
							int x = UhcHost.getRANDOM().nextInt(60)- 30;
							int z = UhcHost.getRANDOM().nextInt(60)- 30;
							joueur.setStunned(false);
							Location loc = new Location(player.getWorld(), player.getLocation().getBlockX() + x, 110, player.getLocation().getBlockZ() + z);
							player.setMaxHealth(player.getMaxHealth() - 1);
							if(player.getMaxHealth() <= 10) {
								player.setHealth(player.getMaxHealth());
							} else {
								player.setHealth(10);
							}
							player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes mort, mais vous avez survécu et avez été téléporté dans un rayon de 30 blocs du lieu de votre mort.");
							player.sendMessage("§6Vous pouvez survivre encore " + danzo.getLife() + " fois.");
							player.teleport(loc);
							narutoV2Manager.getNofallJoueur().add(joueur);
							player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*5, 0, false, false));
							event.setCancelled(true);
							Bukkit.getScheduler().runTaskLater(main, () -> {
								narutoV2Manager.getNofallJoueur().remove(joueur);
							}, 20*5);
						}
					}
				}
			}
    	}
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
    	if(event.getEntity() instanceof Player) {
    		Player player = (Player) event.getEntity();
			PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
			if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
			NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			if(joueur.hasRole()) {
				if(joueur.getRole() instanceof Danzo) {
					Danzo danzo = (Danzo) joueur.getRole();
					if(player.getHealth() - event.getFinalDamage() <= 0.1 && !narutoV2Manager.getNofallJoueur().contains(joueur)) {
						if(danzo.getLife() >= 1) {
							danzo.setLife(danzo.getLife() - 1);
							int x = UhcHost.getRANDOM().nextInt(60)- 30;
							int z = UhcHost.getRANDOM().nextInt(60)- 30;
							Location loc = new Location(player.getWorld(), player.getLocation().getBlockX() + x, 110, player.getLocation().getBlockZ() + z);
							player.setMaxHealth(player.getMaxHealth() - 1);
							if(player.getMaxHealth() <= 10) {
								player.setHealth(player.getMaxHealth());
							} else {
								player.setHealth(10);
							}
							player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes mort, mais vous avez survécu et avez été téléporté dans un rayon de 30 blocs du lieu de votre mort.");
							player.sendMessage("§6Vous pouvez survivre encore " + danzo.getLife() + " fois.");
							player.teleport(loc);
							event.setCancelled(true);
							narutoV2Manager.getNofallJoueur().add(joueur);
							player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*5, 0, false, false));
							Bukkit.getScheduler().runTaskLater(main, () -> {
								narutoV2Manager.getNofallJoueur().remove(joueur);
								player.sendMessage("§7Vous n'êtes plus invincible.");
							}, 20*10);
						}

					}
				}
			} if (event.getDamager() instanceof Player) {
				Player damager = (Player) event.getDamager();
				PlayerManager damagerJoueur = main.getPlayerManager(damager.getUniqueId());
				
				if(damagerJoueur.hasRole()) {
					if(damagerJoueur.getRole() instanceof Danzo) {
						QuickItem epee = new QuickItem(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 4, true);
						if(damager.getItemInHand().isSimilar(epee.toItemStack()) && UhcHost.getRANDOM().nextInt(100) <= 5) {
							event.setDamage(event.getDamage() + ((25/100)*event.getDamage()));
						}
					}
				}
		}
		

		} 
    }


    @EventHandler
    public void onKillHokage(PlayerKillEvent event) {
    	Player killer = event.getKiller();
    	Player player = event.getDeadPlayer();
    	PlayerManager killerJoueur = main.getPlayerManager(killer.getUniqueId());
    	PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
		NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
    	if(killerJoueur.hasRole()) {
    		if(killerJoueur.getRole() instanceof Danzo) {
    			Danzo danzo = (Danzo) killerJoueur.getRole();
    			danzo.setLife(danzo.getLife() + 1);
    			killer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous venez de tuer quelqu'un. Vous pouvez survivre " + danzo.getLife() + " fois maintenant.");
    			if(narutoV2Manager.getHokage() != null && narutoV2Manager.getHokage() == joueur) {
    				narutoV2Manager.setDanzoKilledHokage(true);
					killer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous venez de tuer l'Hokage. Vous aurez donc les avantages d'Hokage dans 5 minutes.");
					Bukkit.getScheduler().runTaskLater(main, () -> {
						narutoV2Manager.danzoHokage();
					}, 20*5*60 + 5);
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
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGZlNjE5NmRkN2QyYTYyNjIzNDFiODQwYzMyYzRkZTE1NGUyYmFkZWI2ODY4ZDA1ZjkzZTVjZGU2YWY4NzJlOSJ9fX0=")
				.setName(getCamp().getCompoColor() + getRoleName());
	}

    @Override
    public Camps getCamp() {
        return Camps.DANZO;
    }

    @Override
    public String getRoleName() {
        return "Danzo";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

    @Override
    public void afterRoles(Player player) {
    	player.setMaxHealth(player.getMaxHealth() + (2D*2D));
    	player.setHealth(player.getMaxHealth());
    }

	@Override
	public Chakra getChakra() {
		return Chakra.FUTON;
	}

	@Override
	public List<ModeSubCommand> getSubCommands() {
		return Arrays.asList(new CmdIzanagi(main), new CmdHokage(main));
	}

	public boolean isWeakHokage() {
		return weakHokage;
	}

	public void setWeakHokage(boolean weakHokage) {
		this.weakHokage = weakHokage;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	@Override
	public boolean hasUsedIzanagi() {
		return usedIzanagi;
	}

	@Override
	public void setHasUsedIzanagi(boolean hasUsedIzanagi) {
		this.usedIzanagi = hasUsedIzanagi;
	}

	public void useVie(PlayerManager joueur, int damage){
		if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
		NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
		if(joueur.hasRole()) {
			if(joueur.getRole() instanceof Danzo) {
				Danzo danzo = (Danzo) joueur.getRole();
				if(joueur.getPlayer() != null){
					Player player = joueur.getPlayer();
					if(player.getHealth() - damage <= 0.1 && !narutoV2Manager.getNofallJoueur().contains(joueur)) {
						if(danzo.getLife() >= 1) {
							danzo.setLife(danzo.getLife() - 1);
							int x = UhcHost.getRANDOM().nextInt(60)- 30;
							int z = UhcHost.getRANDOM().nextInt(60)- 30;
							Location loc = new Location(player.getWorld(), player.getLocation().getBlockX() + x, 110, player.getLocation().getBlockZ() + z);
							player.setMaxHealth(player.getMaxHealth() - 1);
							if(player.getMaxHealth() <= 10) {
								player.setHealth(player.getMaxHealth());
							} else {
								player.setHealth(10);
							}
							player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes mort, mais vous avez survécu et avez été téléporté dans un rayon de 30 blocs du lieu de votre mort.");
							player.sendMessage("§6Vous pouvez survivre encore " + danzo.getLife() + " fois.");
							player.teleport(loc);
							narutoV2Manager.getNofallJoueur().add(joueur);
							Bukkit.getScheduler().runTaskLater(main, () -> {
								narutoV2Manager.getNofallJoueur().remove(joueur);
								player.sendMessage("§7Vous n'êtes plus invincible.");
							}, 20*10);
						} else {
							if(joueur.getPlayer() != null){
								joueur.getPlayer().damage(100);
							}
						}

					}
				}
			}
		}
	}
}
