package fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru;


import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdShosenJutsu;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.EdoTenseiItem;
import fr.lastril.uhchost.modes.naruto.v2.items.SenpoKabutoItem;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Kabuto extends Role implements NarutoV2Role, EdoTenseiItem.EdoTenseiUser, RoleListener, RoleCommand, CmdShosenJutsu.ShosenJutsuUser {

	private int tick = 20;
	private int timer = tick * 60;
	private Location userLoc, targetLoc;
	private UUID targetId;

	public Kabuto() {
		super.addRoleToKnow(Orochimaru.class);
		super.addRoleToKnow(Kimimaro.class);
		super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
		super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
	}

	@Override
	public void afterRoles(Player player) {
		if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
		NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
		new BukkitRunnable() {
			@Override
			public void run() {
				for(PlayerManager joueur : narutoV2Manager.getPlayerManagersWithRole(Kabuto.class)){
					if(joueur.getPlayer() != null){
						if(joueur.getPlayer().getHealth() + 2D >= player.getMaxHealth()){
							joueur.getPlayer().setHealth(player.getMaxHealth());
						} else {
							joueur.getPlayer().setHealth(player.getHealth() + 2D);
						}
					}

				}

			}
		}.runTaskTimer(main, 0, 20*60);
	}

	@Override
	public void onDamage(Player damager, Player target) {
		PlayerManager damagerJoueur = main.getPlayerManager(damager.getUniqueId());
		if (damagerJoueur.hasRole()) {
			if (damagerJoueur.getRole() instanceof Kabuto) {
				int value = UhcHost.getRANDOM().nextInt(100);
				if (value <= 5) {
					UhcHost.debug("Kabuto effect apply to player: " + target.getName());
					target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 0, false, false));
					target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 3, false, false));
					target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous venez de vous faire étourdir.");
				}
			}
		}
	}

	@Override
	public void onPlayerDeath(Player player) {
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if (joueur.hasRole()) {
			if (joueur.getRole() instanceof Orochimaru) {
				if (super.getPlayer() != null) {
					Player kabuto = super.getPlayer();
					main.getInventoryUtils().giveItemSafely(kabuto, new EdoTenseiItem(main).toItemStack());
					kabuto.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
					kabuto.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§5Orochimaru§e vient de mourir. Vous recevez l'effet §7Résistance I§e ainsi que l'item§5 \"Edo Tensei\"§e.");
				}
			}
		}
	}

	@Override
	public void giveItems(Player player) {
		main.getInventoryUtils().giveItemSafely(player, new SenpoKabutoItem(main).toItemStack());
	}

	@Override
	protected void onNight(Player player) {

	}

	@Override
	protected void onDay(Player player) {

	}

	@Override
	public void onNewEpisode(Player player) {
		new BukkitRunnable() {

			@Override
			public void run() {
				player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, tick * 5, 0, false, false));
			}
		}.runTaskLater(main, (long) tick * UhcHost.getRANDOM().nextInt(60*20));

		new BukkitRunnable() {

			@Override
			public void run() {
				player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, tick * 5, 0, false, false));
			}
		}.runTaskLater(main, (long) tick * UhcHost.getRANDOM().nextInt(60*20));
	}

	@Override
	public void onNewDay(Player player) {

	}

	@Override
	public QuickItem getItem() {
		return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
				.setName(getCamp().getCompoColor() + getRoleName()).setTexture(
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQyMjNhNmM0MWI5MGFmYzcyYmY2MDNiNjMzZGJhMzQxZGI0ZjM5OWQ0MDI4ZjE2NWI4NzViNjBkMmE0ZTJjNSJ9fX0=");
	}

	@Override
	public Camps getCamp() {
		return Camps.OROCHIMARU;
	}

	@Override
	public String getRoleName() {
		return "Kabuto";
	}

	@Override
	public String getDescription() {
		return main.getRoleDescription(this, this.getClass().getName());
	}

	@Override
	public void onPlayerUsedPower(PlayerManager joueur) {

	}

	@Override
	public void checkRunnable(Player player) {
		super.checkRunnable(player);
		if(userLoc != null){
			if(userLoc.getWorld() == player.getWorld()){
				if(userLoc.distance(player.getLocation()) >= 0.2){
					if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
					NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
					if(narutoV2Manager.isInShosenJutsu(player.getUniqueId()))
						narutoV2Manager.removeInShosenJutsu(player.getUniqueId());
				}
			}

		}
		if(targetLoc != null){
			if(targetLoc.getWorld() == player.getWorld()){
				if(targetLoc.distance(player.getLocation()) >= 0.2){
					if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
					NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
					if(narutoV2Manager.isInShosenJutsu(targetId))
						narutoV2Manager.removeInShosenJutsu(targetId);
				}
			}
		}
		if(Bukkit.getPlayer(targetId) == null){
			userLoc = player.getLocation();
			targetLoc = Bukkit.getPlayer(targetId).getLocation();
		}

	}

	@Override
	public Chakra getChakra() {
		return Chakra.SUITON;
	}

	@Override
	public List<ModeSubCommand> getSubCommands() {
		return Arrays.asList(new CmdShosenJutsu(main));
	}

	@Override
	public UUID getTargetId() {
		return targetId;
	}

	@Override
	public void setTargetId(UUID uuid) {
		targetId = uuid;
	}
}
