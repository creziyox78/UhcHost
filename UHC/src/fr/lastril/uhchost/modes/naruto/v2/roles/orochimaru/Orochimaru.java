package fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdMarqueMaudite;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.EdoTenseiItem;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.PlayerKillEvent;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class Orochimaru extends Role implements NarutoV2Role, RoleListener, EdoTenseiItem.EdoTenseiUser, RoleCommand {
	
	private boolean kill;

	private Chakra chakra = getRandomChakra();

	private int timeCycle = 60*10;
	
	private PlayerManager targetMarque;
	
	public Orochimaru() {
		super.addRoleToKnow(Kabuto.class);
		super.addEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), When.START);
		super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
	}
	
	@EventHandler
	public void onKill(PlayerKillEvent event) {
		Player player = event.getKiller();
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if(joueur.hasRole()) {
			if(joueur.getRole() instanceof Orochimaru) {
				Orochimaru orochimaru = (Orochimaru) joueur.getRole();
				orochimaru.setKill(true);
				player.setMaxHealth(20);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onDamageMarque(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			Player player = (Player) event.getEntity();
			Player damager = (Player) event.getDamager();
			PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
			PlayerManager damagerJoueur = main.getPlayerManager(damager.getUniqueId());
			if(joueur == getTargetMarque()) {
				event.setDamage(event.getDamage() / 1.05);
			}
			if(damagerJoueur == getTargetMarque()) {
				event.setDamage(event.getDamage() + ((5/100)*event.getDamage()));
			}
		}
	}

	@EventHandler
	public void onEatApple(PlayerItemConsumeEvent event){
		if(event.getItem().getType() == Material.GOLDEN_APPLE) {
			Player player = event.getPlayer();
			PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
			if(joueur.hasRole()){
				if(joueur.getRole() instanceof Orochimaru){
					new BukkitRunnable() {
						@Override
						public void run() {
							if(player.hasPotionEffect(PotionEffectType.REGENERATION)){
								player.removePotionEffect(PotionEffectType.REGENERATION);
							}
							player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*7, 1));
						}
					}.runTaskLater(main, 3);
				}
			}
		}
	}
	
    @Override
    public void giveItems(Player player) {
    	QuickItem epee = new QuickItem(Material.DIAMOND_SWORD).setName("§5Kusanagi").addEnchant(Enchantment.DAMAGE_ALL, 4, true);
    	main.getInventoryUtils().giveItemSafely(player, new EdoTenseiItem(main).toItemStack());
    	main.getInventoryUtils().giveItemSafely(player, epee.toItemStack());
    }
    
    @Override
    public void afterRoles(Player player) {
    	player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Voici votre nature de chakra : " + StringUtils.capitalize(getChakra().toString().toLowerCase()));
    	player.sendMessage(sendList());
		if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
		NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
    	new BukkitRunnable() {
			@Override
			public void run() {
				for(PlayerManager joueur : narutoV2Manager.getPlayerManagersWithRole(Orochimaru.class)){
					if(joueur.getPlayer() != null){
						if(kill) {
							kill = false;
							setTimeCycle(10*60);
							joueur.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§dVotre cycle de vie a été réinitialisé.");
						}
						if(timeCycle <= 0) {
							if(joueur.getPlayer().getMaxHealth() - 2D <= 0D){
								joueur.getPlayer().damage(100);
							} else {
								joueur.getPlayer().setMaxHealth(joueur.getPlayer().getMaxHealth() - 2D);
								joueur.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous venez de perdre 1 coeur car vous n'avez pas tué quelqu'un durant les 10 dernières minutes.");
								setTimeCycle(10*60);
							}
						}
						timeCycle--;
					}
				}

			}
		}.runTaskTimer(main, 20*60*10, 20);
    }

	@Override
	public void checkRunnable(Player player) {
		super.checkRunnable(player);
		if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
			player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		}
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*3, 0, false, false));
		/*if(player.getHealth() <= (2D* 4D)){
			if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
				player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*4, 1, false, false));
		} else {

		}*/
	}

	@Override
	public String sendList() {
		if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return null;
		NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
		String list = Messages.NARUTO_PREFIX.getMessage() + "Voici la liste entière du camp Taka : \n";
		for (PlayerManager joueur : narutoV2Manager.getPlayerManagersWithCamps(Camps.TAKA)) {
			if (joueur.isAlive()) list += "§c- " + joueur.getPlayerName() +"\n";
		}
		list += Messages.NARUTO_PREFIX.getMessage() + "Voici la liste entière du camp Orochimaru : \n";
		for (PlayerManager joueur : narutoV2Manager.getPlayerManagersWithCamps(Camps.OROCHIMARU)) {
			if (joueur.isAlive()) list += "§c- " + joueur.getPlayerName() +"\n";
		}
		return list;
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
				.setTexture(
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ2YjgzZmU3MTBlOTg3NzdkMjlhZTgwNGVmNmQzYjhkMjRiYzVjNTlmZDM3YjViYTA4NDc1YmQ3Njc4ZWQwYSJ9fX0=");
	}

    @Override
    public Camps getCamp() {
        return Camps.OROCHIMARU;
    }

    @Override
    public String getRoleName() {
        return "Orochimaru";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

	@Override
	public Chakra getChakra() {
		return chakra;
	}

	public boolean isKill() {
		return kill;
	}

	public void setKill(boolean kill) {
		this.kill = kill;
	}

	public int getTimeCycle() {
		return timeCycle;
	}

	public void setTimeCycle(int timeCycle) {
		this.timeCycle = timeCycle;
	}

	@Override
	public List<ModeSubCommand> getSubCommands() {
		return Arrays.asList(new CmdMarqueMaudite(main));
	}

	public PlayerManager getTargetMarque() {
		return targetMarque;
	}

	public void setTargetMarque(PlayerManager targetMarque) {
		this.targetMarque = targetMarque;
	}
}
