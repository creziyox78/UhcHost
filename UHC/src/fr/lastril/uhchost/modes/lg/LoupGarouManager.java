package fr.lastril.uhchost.modes.lg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.enums.ResurectType;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.lg.roles.LGChatRole;
import fr.lastril.uhchost.modes.lg.roles.LGHideDeath;
import fr.lastril.uhchost.modes.lg.roles.RealLG;
import fr.lastril.uhchost.modes.lg.roles.lg.LoupGarouGrimeur;
import fr.lastril.uhchost.modes.lg.roles.solo.LoupGarouBlanc;
import fr.lastril.uhchost.modes.lg.roles.solo.Rival;
import fr.lastril.uhchost.modes.lg.roles.solo.Voleur;
import fr.lastril.uhchost.modes.lg.roles.village.*;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.WolfPlayerManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LoupGarouManager extends ModeManager implements Listener {

    private final UhcHost main;

    private final List<UUID> waitingRessurect;
    private final List<PlayerManager> loupGarouList = new ArrayList<>();
    private boolean randomCouple = false, voteTime, sendedlist = false, randomSeeRole, lgChatTime, vaccination;
    private int startVoteEpisode = 3;
    private int soloValue = 95, loupValue = 20, villageValue = 80;
    private int sendWerewolfListTime = 50*60;
    private double originalVotedHealth;
    private WolfPlayerManager currentVotedPlayer;

    public LoupGarouManager(UhcHost main) {
        this.main = main;
        this.waitingRessurect = new ArrayList<>();
        this.main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onLgChat(AsyncPlayerChatEvent event){
        if(GameState.isState(GameState.STARTED) && main.gameManager.getModes() == Modes.LG){
            Player player = event.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            event.setCancelled(true);
            if(playerManager.hasRole() && playerManager.isAlive()){
                if(playerManager.getRole() instanceof LGChatRole){
                    LGChatRole lgChatRole = (LGChatRole) playerManager.getRole();
                    if(lgChatRole.canSend() && lgChatTime){
                        main.getPlayerManagerOnlines().forEach(playerManagers -> {
                            if(playerManagers.isAlive() && playerManagers.hasRole()){
                                if(playerManagers.getRole() instanceof LGChatRole){
                                    LGChatRole lgChatRoles = (LGChatRole) playerManagers.getRole();
                                    if(lgChatRoles.canSee()){
                                        playerManagers.getPlayer().sendMessage(Camps.LOUP_GAROU.getCompoColor() + (lgChatRole.sendPlayerName() ? playerManager.getPlayerName() + " » " : "Loup-garou » ") + event.getMessage());
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    public void startDeathTask(Player player, Player killer) {
        Location deathLocation = player.getLocation().clone();
        player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§bVous avez toujours une chance de vous faire réssusciter. Merci de patienter.");
        this.waitingRessurect.add(player.getUniqueId());
        System.out.println("Starting death task !");
        Bukkit.getScheduler().runTaskLater(main, () -> {
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            System.out.println("WolfPlayerManager not null !");
            if (playerManager.getWolfPlayerManager().getResurectType() != null) {
                System.out.println("Ressurect type not null !");
                if (player.getPlayer() != null) {
                    System.out.println("Player not null !");
                    Player onlinePlayer = player.getPlayer();
                    onlinePlayer.setGameMode(GameMode.SURVIVAL);
                    onlinePlayer.getInventory().setContents(player.getInventory().getContents());
                    onlinePlayer.getInventory().setArmorContents(player.getInventory().getArmorContents());
                    onlinePlayer.updateInventory();
                    if(killer != null){
                        System.out.println("Checking killer not null !");
                        PlayerManager killerManager = main.getPlayerManager(killer.getUniqueId());
                        if(playerManager.getWolfPlayerManager().isProtect() && killerManager.hasRole() && killerManager.getRole() instanceof RealLG){
                            System.out.println("Garde protection !");
                            playerManager.getWolfPlayerManager().setResurectType(ResurectType.GARDE);
                        }
                    }
                    switch (playerManager.getWolfPlayerManager().getResurectType()) {
                        case INFECT: {
                            System.out.println("Player set ressurect type Infect !");
                            onlinePlayer.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "Vous avez été infecté par l'Infect Pères des loups.");
                            playerManager.getWolfPlayerManager().setResurectType(null);
                            main.gameManager.teleportPlayerOnGround(player);
                            break;
                        }
                        case ANCIEN: {
                            System.out.println("Player set ressurect type Ancien !");
                            Ancien ancien = (Ancien) playerManager.getRole();
                            ancien.setRevived(true);
                            onlinePlayer.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "Vous avez utilisé votre deuxième vie, par conséquant, vous ne pourrez plus réssuciter.");
                            onlinePlayer.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                            playerManager.getWolfPlayerManager().setResurectType(null);
                            main.gameManager.teleportPlayerOnGround(player);
                            break;
                        }
                        case SORCIERE: {
                            System.out.println("Player set ressurect type Soso !");
                            onlinePlayer.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "Vous avez été réssuscité par la Sorcière.");
                            playerManager.getWolfPlayerManager().setResurectType(null);
                            main.gameManager.teleportPlayerOnGround(player);
                            break;
                        }
                        case GARDE:
                            System.out.println("Player set ressurect type Garde !");
                            onlinePlayer.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "Vous avez été réssuscité par le Garde.");
                            playerManager.getWolfPlayerManager().setResurectType(null);
                            playerManager.getWolfPlayerManager().setProtect(false);
                            main.gameManager.teleportPlayerOnGround(player);
                            break;
                        default:
                            break;
                    }
                }

            } else {
                System.out.println("Ressurect type is null ! Killed player");
                kill(player, player.getInventory().getContents(), player.getInventory().getArmorContents(), killer, deathLocation);
                if (playerManager.getWolfPlayerManager().isInCouple()) {
                    System.out.println("Checking couple...");
                    if(killer != null){

                        PlayerManager killerManager = main.getPlayerManager(killer.getUniqueId());
                        if(killerManager.hasRole() && !(killerManager.getRole() instanceof Voleur || killerManager.getRole() instanceof Rival)){
                            killCouple();
                            System.out.println("Killed coupled player !");
                        }
                        System.out.println("Killer role stile couple !");
                    }

                }
            }
            waitingRessurect.remove(player.getUniqueId());
        }, 20 * 13);
    }

    public void randomRole(PlayerManager playerManager){
        Bukkit.broadcastMessage("§8§m----------------------------------");
        Bukkit.broadcastMessage(" ");
        main.getPlayerManagerOnlines().forEach(playerManagers -> {
            String message = "";
            if(playerManagers.hasRole()){
                if(playerManagers.getRole() instanceof Pretresse || playerManagers.getRole() instanceof Chaman){
                    message = "§2§l" + playerManager.getPlayerName() +
                            " est mort, il était §o" + playerManager.getRole().getRoleName();
                    if (playerManager.getWolfPlayerManager().isInfected()) {
                        message += "§2 (infecté)";
                    }
                    if (playerManager.getWolfPlayerManager().isTransformed()) {
                        message += "§2 (transformé)";
                    }
                } else {
                    int value = UhcHost.getRANDOM().nextInt(100);
                    System.out.println("Pretresse role value : " + value + " for " + playerManagers.getPlayerName() + " in camp : " + playerManagers.getCamps().name());
                    if(playerManagers.getCamps() == Camps.VILLAGEOIS){
                        if(value <= getVillageValue()){
                            System.out.println(playerManagers.getPlayer() + " see role !");
                            message = "§2§l" + playerManager.getPlayerName() +
                                    " est mort, il était §o" + playerManager.getRole().getRoleName();
                            if (playerManager.getWolfPlayerManager().isInfected()) {
                                message += "§2 (infecté)";
                            }
                            if (playerManager.getWolfPlayerManager().isTransformed()) {
                                message += "§2 (transformé)";
                            }
                        } else {
                            System.out.println(playerManagers.getPlayer() + " not see role !");
                            message = "§2§l" + playerManager.getPlayerName() +
                                    " est mort, il était §o§kInconnue";
                        }

                    } else if(playerManagers.getCamps() == Camps.LOUP_GAROU){
                        if(value <= getLoupValue()){
                            System.out.println(playerManagers.getPlayer() + " see role !");
                            message = "§2§l" + playerManager.getPlayerName() +
                                    " est mort, il était §o" + playerManager.getRole().getRoleName();
                            if (playerManager.getWolfPlayerManager().isInfected()) {
                                message += "§2 (infecté)";
                            }
                            if (playerManager.getWolfPlayerManager().isTransformed()) {
                                message += "§2 (transformé)";
                            }
                        } else {
                            System.out.println(playerManagers.getPlayer() + " not see role !");
                            message = "§2§l" + playerManager.getPlayerName() +
                                    " est mort, il était §o§kInconnue";
                        }
                    } else {
                        if(value <= getSoloValue()){
                            System.out.println(playerManagers.getPlayer() + " see role !");
                            message = "§2§l" + playerManager.getPlayerName() +
                                    " est mort, il était §o" + playerManager.getRole().getRoleName();
                            if (playerManager.getWolfPlayerManager().isInfected()) {
                                message += "§2 (infecté)";
                            }
                            if (playerManager.getWolfPlayerManager().isTransformed()) {
                                message += "§2 (transformé)";
                            }
                        } else {
                            System.out.println(playerManagers.getPlayer() + " not see role !");
                            message = "§2§l" + playerManager.getPlayerName() +
                                    " est mort, il était §o§kInconnue";
                        }
                    }
                }
                playerManagers.getPlayer().sendMessage(message);

            }
        });
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage("§8§m----------------------------------");
    }

    public void kill(OfflinePlayer player, ItemStack[] items, ItemStack[] armors, Player killer,
                     Location deathLocation) {
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        playerManager.setAlive(false);

        System.out.println("On Realy Death Role !");
        for (PlayerManager PlayerManagers : main.getPlayerManagerAlives().stream().filter(PlayerManager::hasRole).collect(Collectors.toList())) {
            PlayerManagers.getRole().onPlayerDeathRealy(playerManager, items, armors, killer, deathLocation);
        }

        /* DROPING INVENTORY */
        System.out.println("Droping inventory !");
        main.getInventoryUtils().dropInventory(deathLocation, items, armors);

        System.out.println("Set spectator !");
        if (playerManager.getPlayer() != null) {
            Player onlinePlayer = playerManager.getPlayer();
            onlinePlayer.setGameMode(GameMode.SPECTATOR);
            onlinePlayer.getInventory().clear();
        }


        System.out.println("On Kill Role !");
        if(killer != null){
            PlayerManager playerManagerKiller = main.getPlayerManager(killer.getUniqueId());
            if(playerManagerKiller.hasRole()){
                playerManagerKiller.getRole().onKill(player, killer);
            }
            playerManagerKiller.addKill(player.getUniqueId());
        }


        if (playerManager.getWolfPlayerManager().isInCouple()) {
            System.out.println("Player in couple, cupidon win changed !");
            for (PlayerManager cupidonPlayerManager : super.getPlayerManagersWithRole(Cupidon.class)) {
                cupidonPlayerManager.setCamps(Camps.VILLAGEOIS);
                if(cupidonPlayerManager.getWolfPlayerManager().isInfected())
                    cupidonPlayerManager.setCamps(Camps.LOUP_GAROU);
                /*for (PlayerManager angePlayer : super.getPlayerManagersWithRole(Ange.class)) {
                    Ange ange = (Ange) angePlayer.getRole();
                    if(ange.getCible() == cupidonPlayerManager)
                        cupidonPlayerManager.setCamps(Camps.ANGE);
                }*/
            }
        }

        System.out.println("Hide Death Role !");
        if(playerManager.hasRole()){
            if(playerManager.getRole() instanceof LGHideDeath){
                main.gameManager.getModes().getMode().checkWin();
                return;
            }
        }




        for (Player players : Bukkit.getOnlinePlayers()) {
            players.playSound(players.getLocation(), Sound.WOLF_HOWL, 1f, 1f);
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

                        Bukkit.broadcastMessage(message);
                        Bukkit.broadcastMessage(" ");
                        Bukkit.broadcastMessage("§8§m----------------------------------");
                        main.gameManager.getModes().getMode().checkWin();
                        if (playerManager.getWolfPlayerManager().isInCouple()) {
                            for (PlayerManager cupidonPlayerManager : super.getPlayerManagersWithRole(Cupidon.class)) {
                                cupidonPlayerManager.getWolfPlayerManager().setCamp(Camps.VILLAGEOIS);
                            }
                        }
                        System.out.println(" ");
                        System.out.println("!!! Fake LG dead, cause of LG Grimeur !!! ");
                        System.out.println(" ");
                        return;
                    }
                }
            }

        }
        if(!isRandomSeeRole()){

            Bukkit.broadcastMessage("§8§m----------------------------------");
            Bukkit.broadcastMessage(" ");
            String message = "";
            if(playerManager.hasRole()){
                message = "§2§l" + playerManager.getPlayerName() +
                        " est mort, il était §o" + playerManager.getRole().getRoleName();
                if (playerManager.getWolfPlayerManager().isInfected()) {
                    message += "§2 (infecté)";
                }
                if (playerManager.getWolfPlayerManager().isTransformed()) {
                    message += "§2 (transformé)";
                }
            } else {
                message = "§2§l" + playerManager.getPlayerName() +
                        " est mort";
            }


            Bukkit.broadcastMessage(message);
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage("§8§m----------------------------------");
        } else {
            randomRole(playerManager);
        }



        main.gameManager.getModes().getMode().checkWin();
    }

    public void killCouple() {
        for (PlayerManager playerManager : main.getPlayerManagerAlives()) {
            if (playerManager.getWolfPlayerManager().isInCouple()) {
                Player player = playerManager.getPlayer();
                if (player != null) {
                    Bukkit.broadcastMessage("§dCe joueur était en couple, il meurt également.");
                    kill(player, player.getInventory().getContents(), player.getInventory().getArmorContents(), player.getKiller(), player.getLocation());
                }
            }
        }
    }

    public void resetVote() {
        main.getPlayerManagerAlives().forEach(playerManager -> {
            playerManager.getWolfPlayerManager().resetVote();
            playerManager.getWolfPlayerManager().setVoted(false);
        });
    }

    public WolfPlayerManager getMostVoted() {
        List<WolfPlayerManager> players = new ArrayList<>(main.getAllWolfPlayerManager().values());
        for(WolfPlayerManager wolfPlayerManager: players){
            System.out.println("Player: " + wolfPlayerManager.getPlayerManager().getPlayerName() + " | vote: " + wolfPlayerManager.getVotes());
        }
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
            Bukkit.broadcastMessage("§cPersonne n'a réussi à se mettre d'accord.");
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
            StringBuilder list = new StringBuilder(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVoici la liste entière des Loups-Garous : \n");
            main.getPlayerManagerAlives().forEach(playerManager -> {
                if(playerManager.hasRole() && playerManager.getRole() instanceof RealLG)
                    loupGarouList.add(playerManager);
                if(playerManager.getWolfPlayerManager().isInfected() && !loupGarouList.contains(playerManager))
                    loupGarouList.add(playerManager);
                if(playerManager.getWolfPlayerManager().isZizanied() && !loupGarouList.contains(playerManager))
                    loupGarouList.add(playerManager);
            });
            for(PlayerManager playerManager : super.getPlayerManagersWithRole(ChienLoup.class)){
                ChienLoup chienLoup = (ChienLoup) playerManager.getRole();
                if(chienLoup.getChoosenCamp() == Camps.VILLAGEOIS){
                    loupGarouList.add(playerManager);
                } else if(chienLoup.getChoosenCamp() == Camps.LOUP_GAROU){
                    loupGarouList.remove(playerManager);
                }
            }
            int numberOfElements = loupGarouList.size();
            for (int i = 0; i < numberOfElements; i++) {
                int index = UhcHost.getRANDOM().nextInt(loupGarouList.size());
                list.append("§c- ").append(loupGarouList.get(index).getPlayerName()).append("\n");
                loupGarouList.remove(index);
            }
            return list.toString();
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

    public List<UUID> getWaitingRessurect() {
        return waitingRessurect;
    }

    public void addInfect(PlayerManager playerManager, boolean vaccination) {
        playerManager.getWolfPlayerManager().setInfected(true);
        playerManager.getWolfPlayerManager().setResurectType(ResurectType.INFECT);
        if (playerManager.getPlayer() != null) {
            if(!vaccination){
                playerManager.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous venez d'être infecté par l'Infect Père des Loups-Garou ! Vous devez gagner avec les loups, et vous garder vos pouvoirs d'origine.");
                playerManager.setCamps(Camps.LOUP_GAROU);
            } else {
                playerManager.getPlayer().sendMessage("§9[Vaccination]§b Vous venez d'être infecté par l'Infect Père des Loups-Garou mais vous devez gagner toujours avec votre camp actuelle !");
            }
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

    public void randomCouple(){
        List<PlayerManager> alivesPlayer = main.getPlayerManagerAlives();
        for(PlayerManager playerManager : getPlayerManagersWithRole(Cupidon.class)){
            alivesPlayer.remove(playerManager);
        }
        int value1 = UhcHost.getRANDOM().nextInt(alivesPlayer.size());
        PlayerManager playerManager1 = alivesPlayer.get(value1);
        alivesPlayer.remove(value1);
        int value2 = UhcHost.getRANDOM().nextInt(alivesPlayer.size());
        PlayerManager playerManager2 = alivesPlayer.get(value2);
        alivesPlayer.remove(value2);
        playerManager1.getWolfPlayerManager().setOtherCouple(playerManager2.getUuid());
        playerManager2.getWolfPlayerManager().setOtherCouple(playerManager1.getUuid());
        playerManager1.setCamps(Camps.COUPLE);
        playerManager2.setCamps(Camps.COUPLE);
        playerManager1.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() +
                "§dLe cupidon vient de vous lié d'amour avec " + playerManager2.getPlayer().getName()
                + ". Si l'un d'entre vous vient à mourir, l'autre mourra alors par amour pour l'autre.");
        playerManager2.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() +
                "§dLe cupidon vient de vous lié d'amour avec " + playerManager1.getPlayer().getName()
                + ". Si l'un d'entre vous vient à mourir, l'autre mourra alors par amour pour l'autre.");
        for(PlayerManager playerManager : getPlayerManagersWithRole(Cupidon.class)){
            if(playerManager.getPlayer() != null){
                playerManager.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aVos flèches ont atteints le coeur de " + playerManager1.getPlayerName() + " et " + playerManager2.getPlayerName() + ". Désormais, si l'un d'eux viennent à mourir, l'autre mourra également.");
            }
        }
        for(PlayerManager playerManager : getPlayerManagersWithRole(Rival.class)){
            if(playerManager.getPlayer() != null){
                playerManager.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aLe couple a été selectionné, voici le rôle des 2 amoureux: " + playerManager1.getRole().getRoleName() + " et " + playerManager2.getRole().getRoleName() + ".");
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
            Bukkit.broadcastMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§eVous avez 30 secondes pour voter pour le joueur de votre choix avec la commande§6 \"/lg vote <pseudo>\"§e. " +
                    "Le joueur ciblé obtenant le plus de vote contre lui perdra la moitié de sa vie jusqu'au prochain épisode.");
        } else {
            Bukkit.broadcastMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cLes votes sont désormais fermés !");
        }
        Bukkit.broadcastMessage("§8§m----------------------------");

    }

    public int getStartVoteEpisode() {
        return startVoteEpisode;
    }

    public void setStartVoteEpisode(int startVoteEpisode) {
        if(sendWerewolfListTime < 1)
            return;
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
        if(sendWerewolfListTime < 60)
            return;
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

    public boolean isRandomSeeRole() {
        return randomSeeRole;
    }

    public void setRandomSeeRole(boolean randomSeeRole) {
        this.randomSeeRole = randomSeeRole;
    }

    public void setLoupValue(int loupValue) {
        this.loupValue = loupValue;
    }

    public void setSoloValue(int soloValue) {
        this.soloValue = soloValue;
    }

    public void setVillageValue(int villageValue) {
        this.villageValue = villageValue;
    }

    public int getLoupValue() {
        return loupValue;
    }

    public int getSoloValue() {
        return soloValue;
    }

    public int getVillageValue() {
        return villageValue;
    }

    public void setLgChatTime(boolean lgChatTime) {
        this.lgChatTime = lgChatTime;
    }

    public boolean isLgChatTime() {
        return lgChatTime;
    }

    public boolean isVaccination() {
        return vaccination;
    }

    public void setVaccination(boolean vaccination) {
        this.vaccination = vaccination;
    }
}
