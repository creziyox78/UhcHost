package fr.lastril.uhchost.modes.lg.roles.lg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.ResurectType;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.lg.roles.village.Ancien;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.PlayerKillEvent;
import fr.lastril.uhchost.tools.API.clickable_messages.ClickableMessage;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class InfectPereDesLoups extends Role implements LGRole, RoleListener {

	private boolean hasInfected;

	public InfectPereDesLoups() {
		super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false),
				When.NIGHT);
		super.addEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false),
				When.START);
		super.addEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 1, 0, false, false), When.AT_KILL);
		super.addEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 1, 0, false, false), When.AT_KILL);
	}

	@Override
	public String getSkullValue() {
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzBmZGYyZTg4ODM5OTNiYWE4Njc1M2Y3ZTdiMTMxM2NkMmE3NjljN2VjN2JhYTY4Mjc5NDIyNzdjYjdiYWJjMCJ9fX0= ";
	}

	@Override
	public String getRoleName() {
		return "Infect Père des Loups";
	}

	@Override
	public String getDescription() {
		return " Pour ce faire, vous disposez de l'effet Force I la nuit et Night Vision. "
				+ "A chaque kill, vous gagnez 1 minute de Speed et 2 coeurs d'absorbtion pendant 2 minutes."
				+ "Une fois dans la partie, vous pouvez décider de ressusciter un joueur mort des griffes des Loups-Garous."
				+ "Ce joueur deviendra alors un Loup-Garou, tout en gardant ses pouvoirs d'origine.";
	}

	@Override
	public void giveItems(Player player) {
	}

	@Override
	public void onNight(Player player) {
	}

	@Override
	public void onDay(Player player) {
	}

	@Override
	public void onNewEpisode(Player player) {

	}

	@Override
	public void onNewDay(Player player) {
	}

	@Override
	public void checkRunnable(Player player) {
	}

	@Override
	public ItemsCreator getItem() {
		return new ItemsCreator(Material.SKULL_ITEM, getRoleName(), Arrays.asList(""), 1, (byte) 3);
		//return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName("§4" + getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY5MjQ4NmM5ZDZlNGJiY2UzZDVlYTRiYWFmMGNmN2JiZDQ5OTQ3OWQ4ZTM5YTM1NjBiYjZjOGM4YmYxYjZkYSJ9fX0=");
	}

	@Override
	public Camps getCamp() {
		return Camps.LOUP_GAROU;
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!hasInfected) {
			Player killer = event.getEntity().getKiller();
			Player player = event.getEntity();
			if (killer != null) {
				UhcHost main = UhcHost.getInstance();
				PlayerManager joueurKiller = main.getPlayerManager(killer.getUniqueId());
				PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
				if (joueurKiller.hasRole() && joueur.hasRole()) {
					if(joueur.getRole() instanceof Ancien){
						Ancien ancien = (Ancien) joueur.getRole();
						if(!ancien.isRevived()){
							return;
						}
					}
					if (main.gameManager.getLoupGarouManager().isLoupGarou(killer.getUniqueId())) {
						if (super.getPlayer() != null) {
							Player infect = super.getPlayer();
							new ClickableMessage(infect, onClick -> {
								main.getPlayerManager(player.getUniqueId()).getWolfPlayerManager().setInfected(true);
								main.getPlayerManager(player.getUniqueId()).getWolfPlayerManager()
										.setResurectType(ResurectType.INFECT);
								onClick.sendMessage( "Vous avez bien infecté "
										+ player.getName() + " !");
								hasInfected = true;
							}, "§c" + player.getName()
									+ " est mort vous pouvez l'infecter en cliquant sur le message ! ",
									"§a Pour infecter §c" + player.getName());
						}
					}
				}
			}
		}
	}

}
