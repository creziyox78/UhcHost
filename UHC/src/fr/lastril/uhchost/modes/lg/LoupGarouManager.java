package fr.lastril.uhchost.modes.lg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.enums.ResurectType;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.lg.roles.lg.LoupGarouGrimeur;
import fr.lastril.uhchost.modes.lg.roles.solo.LoupGarouBlanc;
import fr.lastril.uhchost.modes.lg.roles.village.Cupidon;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.WolfPlayerManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;

public class LoupGarouManager extends ModeManager implements Listener {

    private final UhcHost main;

    private final List<UUID> waitingRessurect;
    private final List<PlayerManager> loupGarouList = new ArrayList<>();
    private final Map<PlayerManager, Integer> playerVote;
    private boolean randomCouple = false, voteTime, sendedlist = false;
    private int startVoteEpisode = 3;
    private int sendWerewolfListTime = 50*60;
    private double originalVotedHealth;
    private WolfPlayerManager currentVotedPlayer;

    public LoupGarouManager(UhcHost main) {
        this.main = main;
        this.waitingRessurect = new ArrayList<>();
        this.playerVote = new HashMap<>();
        this.main.getServer().getPluginManager().registerEvents(this, main);
    }

    public void startDeathTask(Player player) {
        Location deathLocation = player.getLocation().clone();
        player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§bVous avez toujours une chance de vous faire réssusciter. Merci de patienter.");
        this.waitingRessurect.add(player.getUniqueId());
        Bukkit.getScheduler().runTaskLater(main, () -> {
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if (playerManager.getWolfPlayerManager().getResurectType() != null) {
                if (player.getPlayer() != null) {
                    Player onlinePlayer = player.getPlayer();
                    onlinePlayer.setGameMode(GameMode.SURVIVAL);
                    onlinePlayer.getInventory().setContents(player.getInventory().getContents());
                    onlinePlayer.getInventory().setArmorContents(player.getInventory().getArmorContents());
                    onlinePlayer.updateInventory();
                    switch (playerManager.getWolfPlayerManager().getResurectType()) {
                        case INFECT: {
                            onlinePlayer.sendMessage("Vous avez été infecté par l'Infect Pères des loups.");
                            playerManager.getWolfPlayerManager().setResurectType(null);
                            main.gameManager.teleportPlayerOnGround(player);
                            break;
                        }
                        case ANCIEN: {
                            onlinePlayer.sendMessage("Vous avez utilisé votre deuxième vie, par conséquant, vous ne pourrez plus réssuciter.");
                            onlinePlayer.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                            playerManager.getWolfPlayerManager().setResurectType(null);
                            main.gameManager.teleportPlayerOnGround(player);
                            break;
                        }
                        case SORCIERE: {
                            onlinePlayer.sendMessage("Vous avez été réssucité par la Sorcière.");
                            playerManager.getWolfPlayerManager().setResurectType(null);
                            main.gameManager.teleportPlayerOnGround(player);
                            break;
                        }
                        default:
                            break;
                    }
                }
                playerManager.getWolfPlayerManager().setResurectType(null);
            } else {
                kill(player, player.getInventory().getContents(), player.getInventory().getArmorContents(), player.getKiller(), deathLocation);
                if (playerManager.getWolfPlayerManager().isInCouple()) {
                    killCouple();
                }
            }
            waitingRessurect.remove(player.getUniqueId());
        }, 20 * 13);
    }

    public void kill(OfflinePlayer player, ItemStack[] items, ItemStack[] armors, Player killer,
                     Location deathLocation) {
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        playerManager.setAlive(false);

        for (PlayerManager PlayerManagers : main.getPlayerManagerAlives().stream().filter(PlayerManager::hasRole).collect(Collectors.toList())) {
            PlayerManagers.getRole().onPlayerDeathRealy(playerManager, items, armors, killer, deathLocation);
        }

        /* DROPING INVENTORY */

        main.getInventoryUtils().dropInventory(deathLocation, items, armors);

        for (Player players : Bukkit.getOnlinePlayers()) {
            players.playSound(players.getLocation(), Sound.WOLF_HOWL, 1f, 1f);
        }

        if (playerManager.getPlayer() != null) {
            Player onlinePlayer = playerManager.getPlayer();
            onlinePlayer.setGameMode(GameMode.SPECTATOR);
            onlinePlayer.getInventory().clear();
        }

        if(killer != null){
            PlayerManager playerManagerKiller = main.getPlayerManager(killer.getUniqueId());
            if (playerManagerKiller.hasRole()){
                if(playerManagerKiller.getRole() instanceof LoupGarouGrimeur){
                    LoupGarouGrimeur loupGarouGrimeur = (LoupGarouGrimeur) playerManagerKiller.getRole();
                    if(!loupGarouGrimeur.isFirstKill()){
                        Bukkit.broadcastMessage("§8§m----------------------------------");
                        Bukkit.broadcastMessage(" ");
                        String message = "§2§l" + playerManager.getPlayerName() +
                                " est mort, il était §oLoup-Garou";
                        if (playerManager.getWolfPlayerManager().isInCouple()) {
                            for (PlayerManager cupidonPlayerManager : super.getPlayerManagersWithRole(Cupidon.class)) {
                                cupidonPlayerManager.getWolfPlayerManager().setCamp(Camps.VILLAGEOIS);
                            }
                        }
                        Bukkit.broadcastMessage(message);
                        Bukkit.broadcastMessage(" ");
                        Bukkit.broadcastMessage("§8§m----------------------------------");
                        return;
                    }
                }
            }
            playerManagerKiller.getRole().onKill(player, killer);
        }
        Bukkit.broadcastMessage("§8§m----------------------------------");
        Bukkit.broadcastMessage(" ");
        String message = "§2§l" + playerManager.getPlayerName() +
                " est mort, il était §o" + playerManager.getRole().getRoleName();
        if (playerManager.getWolfPlayerManager().isInfected()) {
            message += "§2 (infecté)";
        }
        if (playerManager.getWolfPlayerManager().isTransformed()) {
            message += "§2 (transformé)";
        }
        if (playerManager.getWolfPlayerManager().isInCouple()) {
            for (PlayerManager cupidonPlayerManager : super.getPlayerManagersWithRole(Cupidon.class)) {
                cupidonPlayerManager.getWolfPlayerManager().setCamp(Camps.VILLAGEOIS);
            }
        }
        Bukkit.broadcastMessage(message);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage("§8§m----------------------------------");
        main.gameManager.getModes().getMode().checkWin();
    }

    public void killCouple() {
        for (PlayerManager playerManager : main.getPlayerManagerAlives()) {
            if (playerManager.getWolfPlayerManager().isInCouple()) {
                Player player = playerManager.getPlayer();
                if (player != null) {
                    kill(player, player.getInventory().getContents(), player.getInventory().getArmorContents(), player.getKiller(), player.getLocation());
                }
            }
        }
    }

    public void resetVote() {
        main.getPlayerManagerAlives().forEach(playerManager -> {
            if (!playerVote.containsKey(playerManager))
                playerVote.put(playerManager, 0);
            playerManager.getWolfPlayerManager().setVoted(false);
        });
        playerVote.forEach((playerManager, integer) -> integer = 0);
    }

    public WolfPlayerManager getMostVoted() {
        List<WolfPlayerManager> players = new ArrayList<>(main.getAllWolfPlayerManager().values());
        if(calculateTopVoted(players).size() >= 2){
            if(calculateTopVoted(players).get(0) == calculateTopVoted(players).get(1) || calculateTopVoted(players).get(0).getVotes() == 0){
                return null;
            }
            return calculateTopVoted(players).get(0);
        }
        return null;
    }

    private List<WolfPlayerManager> calculateTopVoted(List<WolfPlayerManager> players) {
        players.sort(WolfPlayerManager::compareTo);
        return players;
    }

    public void teleportAllPlayerExept(Player exempted){
        for(PlayerManager playerManager : main.getPlayerManagerAlives()){
            Player player = playerManager.getPlayer();
            if(player != null){
                if(exempted != null){
                    if(player == exempted){
                        continue;
                     }
                }
                main.gameManager.teleportPlayerOnGround(player);
            }
        }

    }

    public void applyVote(WolfPlayerManager mostVoted) {
        Bukkit.broadcastMessage("§8§m----------------------------");
        Bukkit.broadcastMessage("§eRésultat du vote");
        if (mostVoted == null) {
            Bukkit.broadcastMessage("§cPersonne n'a pas réussi à se mettre d'accord.");
        } else {
            Bukkit.broadcastMessage("§bLa personne ayant reçu le plus de vote est " + mostVoted.getPlayerManager().getPlayerName()
                    + ", il perd donc la moitié de sa vie jusqu'au prochain épisode.");
            setCurrentVotedPlayer(mostVoted.getPlayerManager().getWolfPlayerManager());
            Player target = mostVoted.getPlayerManager().getPlayer();
            if (target != null) {
                setOriginalVotedHealth(target.getMaxHealth());
                target.setMaxHealth(target.getMaxHealth() / 2);
            }
        }
        Bukkit.broadcastMessage("§8§m----------------------------");
        resetVote();
    }


    public List<PlayerManager> getLoupGarous() {
        List<PlayerManager> lgs = new ArrayList<>(super.getPlayerManagersWithCamps(Camps.LOUP_GAROU));
        lgs.addAll(super.getPlayerManagersWithRole(LoupGarouBlanc.class));
        return lgs;
    }

    public String sendLGList() {
        if(isSendedlist()){
            String list = Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVoici la liste entière des Loups-Garous : \n";
            loupGarouList.addAll(main.gameManager.getModes().getMode().getModeManager().getPlayerManagersWithCamps(Camps.LOUP_GAROU));
            loupGarouList.addAll(main.gameManager.getModes().getMode().getModeManager().getPlayerManagersWithRole(LoupGarouBlanc.class));
            int numberOfElements = loupGarouList.size();
            for (int i = 0; i < numberOfElements; i++) {
                int index = UhcHost.getRANDOM().nextInt(loupGarouList.size());
                list += "§c- " + loupGarouList.get(index).getPlayerName() + "\n";
                loupGarouList.remove(index);
            }
            return list;
        } else {
            return Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous devez attendre " + sendWerewolfListTime/60 + " minutes de jeu avant de recevoir la liste des Loups-Garous.";
        }
    }

    public boolean isLoupGarou(UUID id) {
        return this.getLoupGarous().contains(main.getPlayerManager(id));
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                if (playerManager.getWolfPlayerManager().isSalvation() || waitingRessurect.contains(player.getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public void addInfect(PlayerManager playerManager) {
        playerManager.getWolfPlayerManager().setInfected(true);
        playerManager.getWolfPlayerManager().setResurectType(ResurectType.INFECT);
        if (playerManager.getPlayer() != null) {
            playerManager.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous venez d'être infecté par l'Infect Père des Loups-Garou ! Vous devez gagner avec les loups, et vous garder vos pouvoirs d'origine.");
        }
        if (playerManager.getWolfPlayerManager().isInCouple())
            playerManager.setCamps(Camps.COUPLE);

        sendNewLG();
    }

    public void sendNewLG() {
        for (PlayerManager playerManagers : main.getPlayerManagerOnlines()) {
            if (playerManagers.hasRole()) {
                if (playerManagers.getCamps() == Camps.LOUP_GAROU || playerManagers.getCamps() == Camps.LOUP_GAROU_BLANC) {
                    if (playerManagers.getPlayer() != null) {
                        playerManagers.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cUn joueur vient de rejoindre le camp des Loup-Garou.");
                    }
                }
            }
        }
    }

    public boolean isVoteTime() {
        return voteTime;
    }

    public void setVoteTime(boolean voteTime) {
        this.voteTime = voteTime;
        Bukkit.broadcastMessage("§8§m----------------------------");
        if (voteTime) {
            Bukkit.broadcastMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§eVous avez 30 secondes pour voter pour le joueur de votre choix avec la commande§6 \"/lg vote <pseudo>\". " +
                    "Le joueur ayant le plus de voix sur lui perdra la moitié de sa vie jusqu'à la prochaine journée.");
        } else {
            Bukkit.broadcastMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cLes votes sont désormais fermés !");
        }
        Bukkit.broadcastMessage("§8§m----------------------------");

    }

    public int getStartVoteEpisode() {
        return startVoteEpisode;
    }

    public void setStartVoteEpisode(int startVoteEpisode) {
        this.startVoteEpisode = startVoteEpisode;
    }

    public boolean isRandomCouple() {
        return randomCouple;
    }

    public void setRandomCouple(boolean randomCouple) {
        this.randomCouple = randomCouple;
    }

    public WolfPlayerManager getCurrentVotedPlayer() {
        return currentVotedPlayer;
    }

    public void setCurrentVotedPlayer(WolfPlayerManager currentVotedPlayer) {
        this.currentVotedPlayer = currentVotedPlayer;
    }

    public int getSendWerewolfListTime() {
        return sendWerewolfListTime;
    }

    public void setSendWerewolfListTime(int sendWerewolfListTime) {
        this.sendWerewolfListTime = sendWerewolfListTime;
    }

    public void setSendedlist(boolean sendedlist) {
        this.sendedlist = sendedlist;
    }

    public boolean isSendedlist() {
        return sendedlist;
    }

    public double getOriginalVotedHealth() {
        return originalVotedHealth;
    }

    public void setOriginalVotedHealth(double originalVotedHealth) {
        this.originalVotedHealth = originalVotedHealth;
    }
}
