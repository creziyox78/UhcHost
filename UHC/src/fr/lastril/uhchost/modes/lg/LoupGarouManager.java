package fr.lastril.uhchost.modes.lg;

import java.util.*;
import java.util.stream.Collectors;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.enums.ResurectType;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.lg.roles.lg.LoupGarou;
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class LoupGarouManager extends ModeManager implements Listener {

	private final UhcHost main;

	private final List<UUID> waitingRessurect;

	private final List<PlayerManager> inCouple;

	private boolean randomCouple = false, voteTime;

	private int startVoteEpisode;

	private final LoupGarouMode loupGarouMode;
	private final List<PlayerManager> loupGarouList = new ArrayList<>();
	private final Map<PlayerManager, Integer> playerVote;

	public LoupGarouManager(UhcHost main, LoupGarouMode loupGarouMode) {
		this.main = main;
		this.inCouple = new ArrayList<>();
		this.waitingRessurect = new ArrayList<>();
		this.loupGarouMode = loupGarouMode;
		this.playerVote = new HashMap<>();
		this.main.getServer().getPluginManager().registerEvents(this, main);
	}

	public void startDeathTask(Player player) {
		Location deathLocation = player.getLocation().clone();
		player.sendMessage(Messages.LOUP_GAROU_PREFIX.getPrefix() + "§bVous avez toujours une chance de vous faire réssusciter. Merci de patienter.");
		this.waitingRessurect.add(player.getUniqueId());
		Bukkit.getScheduler().runTaskLater(main, () ->  {
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
				if(joueur.getWolfPlayerManager().isInCouple()){
					killCouple();
				}
			}
			waitingRessurect.remove(player.getUniqueId());
		}, 20 * 10);
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
		main.gameManager.getModes().getMode().checkWin();
	}

	public void killCouple(){
		for (PlayerManager playerManager : main.getPlayerManagerAlives()){
			if(playerManager.getWolfPlayerManager().isInCouple()){
				Player player = playerManager.getPlayer();
				if(player != null){
					kill(player, player.getInventory().getContents(), player.getInventory().getArmorContents(), player.getKiller(), player.getLocation());
				}
			}
		}
	}

	private void resetVote(){
		main.getPlayerManagerAlives().forEach(playerManager -> {
			if(!playerVote.containsKey(playerManager))
				playerVote.put(playerManager, 0);
		});
		playerVote.forEach((playerManager, integer) -> {
			integer = 0;
		});
	}

	public static List<PlayerManager> getKeysWithMaxValue(Map<PlayerManager, Integer> map){
		final List<PlayerManager> resultList = new ArrayList<>();
		int currentMaxValuevalue = Integer.MIN_VALUE;
		for (Map.Entry<PlayerManager, Integer> entry : map.entrySet()){
			if (entry.getValue() > currentMaxValuevalue){
				resultList.clear();
				resultList.add(entry.getKey());
				currentMaxValuevalue = entry.getValue();
			} else if (entry.getValue() == currentMaxValuevalue){
				resultList.add(entry.getKey());
			}
		}
		return resultList;
	}


	public List<PlayerManager> getLoupGarous() {
		List<PlayerManager> lgs = new ArrayList<>(super.getJoueursWithCamps(Camps.LOUP_GAROU));
		lgs.addAll(super.getJoueursWithRole(LoupGarouBlanc.class));
		return lgs;
	}

	public String sendLGList(){
		String list = Messages.LOUP_GAROU_PREFIX.getPrefix() + "Voici la liste entière des Loups-Garous : \n";
		for (PlayerManager joueur : main.gameManager.getModes().getMode().getModeManager().getJoueursWithCamps(Camps.LOUP_GAROU)) {
			loupGarouList.add(joueur);
		}
		for(PlayerManager joueur : main.gameManager.getModes().getMode().getModeManager().getJoueursWithRole(LoupGarouBlanc.class)){
			loupGarouList.add(joueur);
		}
		int numberOfElements = loupGarouList.size();
		for (int i = 0; i < numberOfElements; i++) {
			int index = UhcHost.getRANDOM().nextInt(loupGarouList.size());
			list += "§c- " + loupGarouList.get(index).getPlayerName() + "\n";
			loupGarouList.remove(index);
		}
		return list;
	}

	public boolean isLoupGarou(UUID id) {
		return this.getLoupGarous().contains(main.getPlayerManager(id));
	}

	@EventHandler
    public void onFallDamage(EntityDamageEvent event){
        if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
            if(event.getEntity() instanceof Player){
                Player player = (Player) event.getEntity();
                PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                if(playerManager.getWolfPlayerManager().isSalvation() || waitingRessurect.contains(player.getUniqueId())){
                    event.setCancelled(true);
                }
            }
        }
    }

	public void addInfect(PlayerManager playerManager){
		playerManager.getWolfPlayerManager().setInfected(true);
		playerManager.getWolfPlayerManager().setResurectType(ResurectType.INFECT);
		if(playerManager.getPlayer() != null){
			playerManager.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getPrefix() + "§cVous venez d'être infecté par l'Infect Père des Loups-Garou ! Vous devez gagner avec les loups, et vous garder vos pouvoirs d'origine.");
		}
		if(playerManager.getWolfPlayerManager().isInCouple())
			playerManager.setCamps(Camps.COUPLE);

		sendNewLG();
	}

	public void sendNewLG(){
		for(PlayerManager playerManagers : main.getPlayerManagerOnlines()){
			if(playerManagers.hasRole()){
				if(playerManagers.getCamps() == Camps.LOUP_GAROU || playerManagers.getCamps() == Camps.LOUP_GAROU_BLANC){
					if(playerManagers.getPlayer() != null){
						playerManagers.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getPrefix() + "§cUn joueur vient de rejoindre le camp des Loup-Garou.");
					}
				}
			}
		}
	}

	public void setVoteTime(boolean voteTime) {
		this.voteTime = voteTime;
		Bukkit.broadcastMessage(" ");
		if(voteTime){
			Bukkit.broadcastMessage(Messages.LOUP_GAROU_PREFIX.getPrefix() + "§eVous avez 1 minute pour voter pour le joueur de votre choix. " +
					"Le joueur ayant le plus de voix sur lui perdra la moitié de sa vie jusqu'à la prochaine journée.");
		} else {
			Bukkit.broadcastMessage(Messages.LOUP_GAROU_PREFIX.getPrefix() + "§cLes votes sont désormais fermés !");
		}
		Bukkit.broadcastMessage(" ");

	}

	public boolean isVoteTime() {
		return voteTime;
	}

	public void setStartVoteEpisode(int startVoteEpisode) {
		this.startVoteEpisode = startVoteEpisode;
	}

	public int getStartVoteEpisode() {
		return startVoteEpisode;
	}



	public boolean isRandomCouple() {
		return randomCouple;
	}

	public void setRandomCouple(boolean randomCouple) {
		this.randomCouple = randomCouple;
	}

}
