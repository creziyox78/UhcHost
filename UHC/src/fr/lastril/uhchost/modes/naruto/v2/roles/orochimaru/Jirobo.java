package fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.MarqueMauditeItem;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.PlayerKillEvent;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Jirobo extends Role implements NarutoV2Role, RoleListener, MarqueMauditeItem.MarqueMauditeUser {

	private boolean useMarqueMaudite, hasKill;

	private int durationHunger = 0;

	public Jirobo() {
		super.addRoleToKnow(Orochimaru.class);
		super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false),
				When.START);
	}

	@EventHandler
	public void onKill(PlayerKillEvent event) {
		Player player = event.getKiller();
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if (joueur.hasRole()) {
			if (joueur.getRole() instanceof Jirobo) {
				Jirobo jirobo = (Jirobo) joueur.getRole();
				if (jirobo.isUseMarqueMaudite()) {
					if (!jirobo.isHasKill()) {
						jirobo.setHasKill(true);
						for (PotionEffect effect : player.getActivePotionEffects()) {
							if (player.hasPotionEffect(PotionEffectType.HUNGER)) {
								durationHunger = effect.getDuration();
								player.removePotionEffect(PotionEffectType.HUNGER);
							}
						}
						player.sendMessage(Messages.NARUTO_PREFIX.getMessage()
								+ "§6Vous venez de tuer quelqu'un. Vous perdez pendant 1 minute votre effet d'Hunger.");
						player.setFoodLevel(20);
						new BukkitRunnable() {

							@Override
							public void run() {
								player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, (durationHunger - 60*20),
										0, false, false));
								jirobo.setHasKill(false);
								player.sendMessage(Messages.NARUTO_PREFIX.getMessage() 
										+ "§6L'effet d'Hunger revient pendant la durée de votre pouvoir restant.");
							}
						}.runTaskLater(main, 20 * 60);
					}
				}

			}
		}
	}

	@Override
	public void giveItems(Player player) {
		main.getInventoryUtils().giveItemSafely(player, new MarqueMauditeItem(main).toItemStack());
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
				.setName(getCamp().getCompoColor() + getRoleName()).setTexture(
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTM0ZWIxNDk1YjE4ZjI5MGIzMGJmYjRjZTUxZDIyZTYwZDAwOGQ5OTBhOTk4MzUwZjE0NGE2MzQwYjE3ZWM3ZSJ9fX0=");
	}

	@Override
	public Camps getCamp() {
		return Camps.OROCHIMARU;
	}

	@Override
	public String getRoleName() {
		return "Jirôbô";
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
		return Chakra.DOTON;
	}

	public boolean isUseMarqueMaudite() {
		return useMarqueMaudite;
	}

	public void setUseMarqueMaudite(boolean useMarqueMaudite) {
		this.useMarqueMaudite = useMarqueMaudite;
	}

	public boolean isHasKill() {
		return hasKill;
	}

	public void setHasKill(boolean hasKill) {
		this.hasKill = hasKill;
	}

	@Override
	public void onUseMarqueMaudite(UhcHost main, Player player, PlayerManager joueur) {
		if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
			player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);

		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60 * 5, 1, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 20 * 60 * 5, 0, false, false));
		joueur.setRoleCooldownMarqueMaudite(60 * 30);
		joueur.sendTimer(player, joueur.getRoleCooldownMarqueMaudite(), player.getItemInHand());
		useMarqueMaudite = true;

		new BukkitRunnable() {

			@Override
			public void run() {
				if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
					player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
				player.setHealth(player.getMaxHealth() - (2D * 2D));
				new BukkitRunnable() {

					@Override
					public void run() {
						player.setHealth(player.getMaxHealth() + (2D * 2D));
					}
				}.runTaskLater(main, 20 * 60 * 15);
			}
		}.runTaskLater(main, 20 * 60 * 5);
		player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
	}
}
