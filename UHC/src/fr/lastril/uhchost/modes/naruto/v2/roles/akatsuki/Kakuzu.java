package fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.CorpsRapiece;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Kakuzu extends Role implements NarutoV2Role, RoleListener {

	private int life = 4;
	
	private Chakra chakra = Chakra.DOTON;

	private boolean vulnerable;

	private int timeVulnerable = 5 * 60;

	private final ArrayList<Player> corpsRapieceImmobilise;

	public Kakuzu() {
		super.addRoleToKnow(Hidan.class);
		this.corpsRapieceImmobilise = new ArrayList<>();
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
			if (joueur.isAlive() && joueur.hasRole()) {
				if (joueur.getRole() instanceof Kakuzu) {
					Kakuzu kakuzu = (Kakuzu) joueur.getRole();
					if (!kakuzu.isVulnerable()) {
						if (player.getHealth() <= 1 || player.getHealth() - event.getFinalDamage() <= 1) {
							kakuzu.setVulnerable(true);
							if (kakuzu.getLife() > 0) {
								event.setCancelled(true);
								// player.teleport(StartTask.getRandomLocation());
								kakuzu.setLife(kakuzu.getLife() - 1);
								player.sendMessage(Messages.NARUTO_PREFIX.getMessage()
										+ "§cVous venez de mourir. Il vous reste " + kakuzu.getLife() + " vie(s).");
								setChakraAfterDeath(kakuzu.getLife());
								player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Votre nature de chakra a changé. Chakra: " + chakra.toString());
								if (player.hasPotionEffect(PotionEffectType.REGENERATION))
									player.removePotionEffect(PotionEffectType.REGENERATION);
								if (player.hasPotionEffect(PotionEffectType.ABSORPTION))
									player.removePotionEffect(PotionEffectType.ABSORPTION);
								if (player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE))
									player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
								player.addPotionEffect(
										new PotionEffect(PotionEffectType.ABSORPTION, 20 * 6, 1, false, false));
								player.addPotionEffect(
										new PotionEffect(PotionEffectType.REGENERATION, 20 * 41, 1, false, false));
								player.addPotionEffect(
										new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 41, 1, false, false));
								new BukkitRunnable() {

									@Override
									public void run() {
										kakuzu.setVulnerable(false);
										player.sendMessage(Messages.NARUTO_PREFIX.getMessage()
												+ "§aVous pouvez à nouveau résister à la mort.");
									}
								}.runTaskLater(main, timeVulnerable * 20);
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
			if(main.getGamemanager().getModes() != Modes.NARUTO) return;
			NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			if (joueur.isAlive() && joueur.hasRole()) {
				if (joueur.getRole() instanceof Kakuzu) {
					Kakuzu kakuzu = (Kakuzu) joueur.getRole();
					if (!kakuzu.isVulnerable()) {
						if (player.getHealth() <= 1 || (player.getHealth() - event.getFinalDamage() <= 1 && !narutoV2Manager.getBijuManager().getNoFall().contains(joueur))) {
							kakuzu.setVulnerable(true);
							if (kakuzu.getLife() > 0) {
								event.setCancelled(true);
								// player.teleport(StartTask.getRandomLocation());
								kakuzu.setLife(kakuzu.getLife() - 1);
								player.sendMessage(Messages.NARUTO_PREFIX.getMessage()
										+ "§cVous venez de mourir. Il vous reste " + kakuzu.getLife() + " vie(s).");
								setChakraAfterDeath(kakuzu.getLife());
								player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Votre nature de chakra a changé. Chakra: " + StringUtils.capitalize(chakra.toString().toLowerCase()));
								if (player.hasPotionEffect(PotionEffectType.REGENERATION))
									player.removePotionEffect(PotionEffectType.REGENERATION);
								if (player.hasPotionEffect(PotionEffectType.ABSORPTION))
									player.removePotionEffect(PotionEffectType.ABSORPTION);
								if (player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE))
									player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
								player.addPotionEffect(
										new PotionEffect(PotionEffectType.ABSORPTION, 20 * 6, 1, false, false));
								player.addPotionEffect(
										new PotionEffect(PotionEffectType.REGENERATION, 20 * 41, 1, false, false));
								player.addPotionEffect(
										new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 41, 1, false, false));
								new BukkitRunnable() {

									@Override
									public void run() {
										kakuzu.setVulnerable(false);
										player.sendMessage(Messages.NARUTO_PREFIX.getMessage()
												+ "§aVous pouvez à nouveau résister à la mort.");
									}
								}.runTaskLater(main, timeVulnerable * 20);
							}
						}
					}

				}
			}
		}
	}

	@Override
	public void checkRunnable(Player player) {
		super.checkRunnable(player);
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if(main.getGamemanager().getModes() != Modes.NARUTO) return;
		NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
		if (joueur.isAlive() && joueur.hasRole()) {
			if (joueur.getRole() instanceof Kakuzu) {
				for (PlayerManager targetJoueur : narutoV2Manager.getPlayerManagersWithRole(Hidan.class)) {
					if(targetJoueur.isAlive()){
						if(targetJoueur.getPlayer() != null){
							if(player.getWorld() == targetJoueur.getPlayer().getWorld()){
								if (player.getLocation().distance(targetJoueur.getPlayer().getLocation()) <= 10) {
									if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)){
										player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
									}
									player.addPotionEffect(
											new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 4, 0, false, false));
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void giveItems(Player player) {
		main.getInventoryUtils().giveItemSafely(player, new CorpsRapiece(main).toItemStack());
	}

	@Override
	protected void onNight(Player player) {

	}

	@Override
	protected void onDay(Player player) {

	}

	@Override
	public void onPlayerUsedPower(PlayerManager joueur) {

	}

	@Override
	public void onNewEpisode(Player player) {

	}

	@Override
	public void onNewDay(Player player) {

	}
	
	private Chakra setChakraAfterDeath(int life) {
		switch (life) {
		case 3:
			chakra = Chakra.RAITON;
			break;
		case 2:
			chakra = Chakra.FUTON;
			break;
		case 1:
			chakra = Chakra.KATON;
			break;
		case 0:
			chakra = Chakra.SUITON;
			break;
		default:
			chakra = Chakra.DOTON;
			break;
		}
		return chakra;
	}

	@Override
	public QuickItem getItem() {
		return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
				.setName(getCamp().getCompoColor() + getRoleName()).setTexture(
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2ZmMmIzOTJlOGQ2YTVkNGVlZjY4MGNjMzZiNmU4MzhmZmEwZDQxNzExMWZjNDEzZjJjZWFmOTdmNTgwMDFlMiJ9fX0=");
	}

	@Override
	public Camps getCamp() {
		return Camps.AKATSUKI;
	}

	@Override
	public String getRoleName() {
		return "Kakuzu";
	}

	@Override
	public String getDescription() {
		return "\n§7Son but est de gagner avec l’§cAkatsuki§7. \n" +
				" \n " +
				"§7§lItems :\n" +
				" \n " +
				"§7• Il dispose d’un item nommé \"§8Corps rapiécé§7\", celui-ci lui permet d’immobiliser tous les joueurs ne faisant pas partie de son camp dans un rayon de 20 blocs pendant 5 secondes, il possède un délai de 10 minutes.\n" +
				" \n " +
				"§7§lParticularités : \n" +
				" \n " +
				"§7• Il dispose d’une particularité qui lui permet, lorsqu'il tombe à §c1 demi-coeur§7, de recevoir les effets §eAbsorption 2§7 pendant 5 secondes, §dRégénération 2§7 pendant 40 secondes et §6Résistance au feu 2§7 pendant 40 secondes, cette particularité peut avoir lieu seulement 4 fois, tant qu'il a cette particularité, il ne peut pas mourir. Cependant une fois après avoir utilisé son pouvoir, il sera vulnérable pendant 5 minutes, cela veut dire qu'il peut mourir.\n" +
				" \n " +
				"§7• Il connaît l’identité d’§cHidan§7 et obtient l’effet §cForce 1§7 lorsqu'il est dans un rayon de 10 blocs proches d’§cHidan§7. \n" +
				" \n " +
				"§7• Il possède une nature de chakra, cependant à chaque fois qu’il meurt, avec le pouvoir cité ci-dessus, il change de nature de Chakra, cela se fait dans cette ordre : §6Doton§8 → §eRaiton§8 → §aFûton§8 →§c Katon§8 → §bSuiton";
	}
	
	@Override
	public Chakra getChakra() {
		return chakra;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public boolean isVulnerable() {
		return vulnerable;
	}

	public void setVulnerable(boolean vulnerable) {
		this.vulnerable = vulnerable;
	}

	public int getTimeVulnerable() {
		return timeVulnerable;
	}

	public void setTimeVulnerable(int timeVulnerable) {
		this.timeVulnerable = timeVulnerable;
	}

	public ArrayList<Player> getCorpsRapieceImmobilise() {
		return corpsRapieceImmobilise;
	}
}
