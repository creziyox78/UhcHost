package fr.lastril.uhchost.modes.lg;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.lg.roles.solo.LoupGarouBlanc;
import fr.lastril.uhchost.modes.lg.roles.village.Cupidon;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class LoupGarouManager extends ModeManager {

	private final UhcHost main;

	private final List<UUID> waitingRessurect;

	private boolean randomCouple = false;

	public LoupGarouManager(UhcHost main) {
		this.main = main;
		this.waitingRessurect = new ArrayList<>();
	}

	public void startDeathTask(Player player) {
		Location deathLocation = player.getLocation().clone();
		player.sendMessage("§bVous avez toujours une chance de vous faire réssusciter. Merci de patienter.");
		this.waitingRessurect.add(player.getUniqueId());
		new BukkitRunnable() {

			@Override
			public void run() {
				PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
				if(joueur.getWolfPlayerManager().getResurectType() != null) {
					if(player.getPlayer() != null) {
						Player onlinePlayer = player.getPlayer();
						onlinePlayer.setGameMode(GameMode.SURVIVAL);
						onlinePlayer.getInventory().setContents(player.getInventory().getContents());
						onlinePlayer.getInventory().setArmorContents(player.getInventory().getArmorContents());
						onlinePlayer.updateInventory();
						switch (joueur.getWolfPlayerManager().getResurectType()) {
						case INFECT:{
							onlinePlayer.sendMessage("Vous avez été infecté par l'Infect Pères des loups.");
							joueur.getWolfPlayerManager().setResurectType(null);
							main.gameManager.generateLocationOnGround(player);
							break;
						}
						case ANCIEN:{
							onlinePlayer.sendMessage("Vous avez utilisé votre deuxième vie, par conséquant, vous ne pourrez plus réssuciter.");
							onlinePlayer.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
							joueur.getWolfPlayerManager().setResurectType(null);
							main.gameManager.generateLocationOnGround(player);
							break;
						}
						case SORCIERE:{
							onlinePlayer.sendMessage("Vous avez été réssucité par la Sorcière.");
							joueur.getWolfPlayerManager().setResurectType(null);
							main.gameManager.generateLocationOnGround(player);
							break;
						}
						default:
							break;
						}
					}
					joueur.getWolfPlayerManager().setResurectType(null);
				}else {
					kill(player, player.getInventory().getContents(), player.getInventory().getArmorContents(), player.getKiller(), deathLocation);
				}
				waitingRessurect.remove(player.getUniqueId());
			}
		}.runTaskLater(main, 20 * 10);
	}

	public void kill(OfflinePlayer player, ItemStack[] items, ItemStack[] armors, Player killer,
			Location deathLocation) {
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		joueur.setAlive(false);
		
		for(PlayerManager joueurs : main.getPlayerManagerAlives().stream().filter(PlayerManager::hasRole).collect(Collectors.toList())) {
			joueurs.getRole().onPlayerDeathRealy(joueur, items, armors, killer, deathLocation);
		}

		/* DROPING INVENTORY */

		main.getInventoryUtils().dropInventory(deathLocation, items, armors);

		if (killer != null) {
			PlayerManager joueurKiller = main.getPlayerManager(killer.getUniqueId());
			if (joueurKiller.getRole() != null)
				joueurKiller.getRole().onKill(player, killer);
		}

		for (Player players : Bukkit.getOnlinePlayers()) {
			players.playSound(players.getLocation(), Sound.WOLF_HOWL, 1f, 1f);
		}

		if(joueur.getPlayer() != null) {
			Player onlinePlayer = joueur.getPlayer();
			onlinePlayer.setGameMode(GameMode.SPECTATOR);
			onlinePlayer.getInventory().clear();
		}
		
		Bukkit.broadcastMessage("§8§m----------------------------------");
		Bukkit.broadcastMessage(" ");
		String message = "§2§l" + joueur.getPlayerName() +
				" est mort, il était §o" + joueur.getRole().getRoleName();
		if (joueur.getWolfPlayerManager().isInfected()) {
			message += "§2 (infecté)";
		}
		if (joueur.getWolfPlayerManager().isTransformed()) {
			message += "§2 (transformé)";
		}
		if (joueur.getWolfPlayerManager().isInCouple()) {
			for (PlayerManager cupidonJoueur : super.getJoueursWithRole(Cupidon.class)) {
				cupidonJoueur.getWolfPlayerManager().setCamp(Camps.VILLAGEOIS);
			}
		}
		Bukkit.broadcastMessage(message);
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage("§8§m----------------------------------");
		this.main.gameManager.getModes().getMode().checkWin();
	}

	public List<PlayerManager> getLoupGarous() {
		List<PlayerManager> lgs = new ArrayList<>(super.getJoueursWithCamps(Camps.LOUP_GAROU));
		lgs.addAll(super.getJoueursWithRole(LoupGarouBlanc.class));
		return lgs;
	}

	public boolean isLoupGarou(UUID id) {
		return this.getLoupGarous().contains(main.getPlayerManager(id));
	}


	public boolean isRandomCouple() {
		return randomCouple;
	}

	public void setRandomCouple(boolean randomCouple) {
		this.randomCouple = randomCouple;
	}
}
