package fr.lastril.uhchost.modes.lg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.inventory.guis.modes.lg.LoupGarouGui;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.CmdCompo;
import fr.lastril.uhchost.modes.command.CmdMe;
import fr.lastril.uhchost.modes.command.ModeCommand;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.command.CmdDesc;
import fr.lastril.uhchost.modes.lg.commands.CmdList;
import fr.lastril.uhchost.modes.lg.commands.CmdVote;
import fr.lastril.uhchost.modes.lg.roles.LGChatRole;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.lg.roles.solo.Trublion;
import fr.lastril.uhchost.modes.lg.roles.village.Pretresse;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.WolfPlayerManager;
import fr.lastril.uhchost.tools.API.BungeeAPI;
import fr.lastril.uhchost.tools.API.TitleAPI;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class LoupGarouMode extends Mode implements ModeCommand, RoleMode<LGRole>, ModeConfig, RoleAnnounceMode {

    private final UhcHost pl;
    private final LoupGarouManager loupGarouManager;
    private final List<LoupGarouSpecialEvent> specialEventList = new ArrayList<>();
    private int announceRoles = 20*60;
    private boolean lgSolitaire;
    private int annonceSolitaire = 20*55;

    public LoupGarouMode() {
        super(Modes.LG);
        this.pl = UhcHost.getInstance();
        this.loupGarouManager = new LoupGarouManager(pl);
        for(SpecialsEvents specialsEvents : SpecialsEvents.values()){
            try {
                LoupGarouSpecialEvent loupGarouSpecialEvent = specialsEvents.getSpecialEvent().newInstance();
                specialEventList.add(loupGarouSpecialEvent);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void tick(int timer) {
        if(timer == 0){
            setupParameter();
        }
        if(timer == annonceSolitaire && lgSolitaire){
            chooseSolitaire();
        }
        if (announceRoles == 0) {
            annonceRoles();
        }
        if(loupGarouManager.getSendWerewolfListTime() == timer){
            sendLgList();
        }

    }

    public void setupParameter() {
        for(LoupGarouSpecialEvent loupGarouSpecialEvent: specialEventList){
            loupGarouSpecialEvent.runTask();
        }
        if(loupGarouManager.isRandomCouple()){
            Bukkit.getScheduler().runTaskLater(pl, () -> loupGarouManager.randomCouple(), 20*60*25);
        }
        if(pl.gameManager.getComposition().contains(Pretresse.class)){
            loupGarouManager.setRandomSeeRole(true);
        }
        Bukkit.getWorld("game").setGameRuleValue("keepInventory", "true");
    }

    public void chooseSolitaire(){
        List<PlayerManager> loupGarouPlayers = loupGarouManager.getLoupGarous().stream().filter(PlayerManager::isAlive).collect(Collectors.toList());
        int random = UhcHost.getRANDOM().nextInt(loupGarouPlayers.size());
        PlayerManager lgSolitaire = loupGarouPlayers.get(random);
        WolfPlayerManager wolfPlayerManager = lgSolitaire.getWolfPlayerManager();
        lgSolitaire.setCamps(Camps.LOUP_GAROU_SOLITAIRE);
        wolfPlayerManager.setSolitaire(true);
        if(wolfPlayerManager.isInCouple())
            lgSolitaire.setCamps(Camps.COUPLE);
        Player player = lgSolitaire.getPlayer();
        if(player != null){
            player.setMaxHealth(player.getMaxHealth() + 2D*4D);
            TitleAPI.sendTitle(player, 20, 20, 20, "§cLoup-Garou Solitaire", "§4Vous êtes seul !");
            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous avez été choisis comme Loup-Garou Solitaire ! Vous devez désormais gagné seul ! Voici 4 coeurs supplémentaires pour vous aidé !");
        }
    }


    public void annonceRoles() {
        List<Class<? extends Role>> compo = pl.getGamemanager().getComposition();
        long missedRoles = pl.getPlayerManagerAlives().stream().filter(PlayerManager -> PlayerManager.isAlive() && PlayerManager.isPlayedGame()).count() - compo.size();
        Bukkit.broadcastMessage("§6§lAttribution des rôles !");
        if (missedRoles > 0) {
            for (int i = 0; i < missedRoles; i++) {
                Role role = getRoles().get(UhcHost.getRANDOM().nextInt(getRoles().size()));
                compo.add(role.getClass());
            }
        }
        List<Role> roles = new ArrayList<>();
        for (PlayerManager playerManager : pl.getPlayerManagerAlives().stream().filter(PlayerManager -> PlayerManager.isAlive() && PlayerManager.isPlayedGame()).collect(Collectors.toList())) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerManager.getUuid());
            int index = UhcHost.getRANDOM().nextInt(compo.size());
            Bukkit.getConsoleSender().sendMessage("index " + index);
            Role role = null;
            try {
                role = compo.get(index).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            Bukkit.getConsoleSender().sendMessage("role " + role.getRoleName());
            if (offlinePlayer.isOnline()) {
                Player player = offlinePlayer.getPlayer();
                //player.sendMessage("§7§m------------------------------------------");
                //player.sendMessage("§7Vous êtes " + role.getCamp().getCompoColor() + "" + role.getRoleName() + "§7.");
                player.sendMessage(role.getDescription() != null ? "§7" + role.getDescription() : "§c§lDESCRIPTION NULL");
                //player.sendMessage("§7§m------------------------------------------");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 10);
                role.stuff(player);
            }
            role.setPlayerID(offlinePlayer.getUniqueId());
            playerManager.setRole(role);
            compo.remove(index);
            roles.add(role);
        }

        getModeManager().sendRoleToKnow();


        for (Role role : roles) {
            if (role instanceof RoleListener) {
                RoleListener roleListener = (RoleListener) role;
                roleListener.registerListener(pl, pl.getServer().getPluginManager());
                Bukkit.getConsoleSender().sendMessage("Register roleListener: " + role.getRoleName());
            }
        }

    }

    public void sendLgList(){
        loupGarouManager.setSendedlist(true);
        for(PlayerManager playerManager : getLoupGarouManager().getPlayerManagersWithCamps(Camps.LOUP_GAROU)){
            Player player = playerManager.getPlayer();
            if(playerManager.getPlayer() != null){
                player.sendMessage(getLoupGarouManager().sendLGList());
            }
        }
        for(PlayerManager playerManager : getLoupGarouManager().getPlayerManagersWithCamps(Camps.LOUP_GAROU_BLANC)){
            Player player = playerManager.getPlayer();
            if(playerManager.getPlayer() != null){
                player.sendMessage(getLoupGarouManager().sendLGList());
            }
        }
        for(PlayerManager playerManager : getLoupGarouManager().getPlayerManagersWithRole(Trublion.class)){
            Trublion trublion = (Trublion) playerManager.getRole();
            trublion.setSwitched(true);
        }
    }

    @Override
    public void onPvp() {

    }

    @Override
    public void onBorder() {

    }

    @Override
    public void onTeleportation() {

    }

    @Override
    public void onNewEpisode() {
        if (pl.gameManager.episode >= getLoupGarouManager().getStartVoteEpisode()) {
            loupGarouManager.setVoteTime(true);
            Bukkit.getScheduler().runTaskLater(pl, () -> loupGarouManager.setVoteTime(false), 20 * 30);
            Bukkit.getScheduler().runTaskLater(pl, () -> loupGarouManager.applyVote(getLoupGarouManager().getMostVoted()), 20 * 60);
        }
        if (loupGarouManager.getCurrentVotedPlayer() != null) {
            Player voted = loupGarouManager.getCurrentVotedPlayer().getPlayerManager().getPlayer();
            if (voted != null) {
                voted.setMaxHealth(loupGarouManager.getOriginalVotedHealth());
            }
            loupGarouManager.setCurrentVotedPlayer(null);
        }
    }

    @Override
    public void onDeath(Player player, Player killer) {
        Bukkit.getScheduler().runTaskLater(pl, () -> {
            player.spigot().respawn();
            player.teleport(new Location(pl.gameManager.spawn.getWorld(), pl.gameManager.spawn.getX(), pl.gameManager.spawn.getY() + 5, pl.gameManager.spawn.getZ()));
        }, 20* 2);
        loupGarouManager.startDeathTask(player, killer);
    }

    @Override
    public boolean isScheduledDeath() {
        return true;
    }

    @Override
    public boolean isEpisodeMode() {
        return true;
    }

    @Override
    public void onDamage(Player target, Player damager) {

    }

    @Override
    public void checkWin() {
        Set<Camps> lastCamps = new HashSet<>();
        for (Player players : Bukkit.getOnlinePlayers()) {
            PlayerManager playerManager = pl.getPlayerManager(players.getUniqueId());
            if (playerManager.isAlive()) {
                lastCamps.add(playerManager.getCamps());
            }
        }
        if (lastCamps.size() == 1) {
            Camps winners = lastCamps.stream().findFirst().get();
            Bukkit.getConsoleSender().sendMessage("Founded 1 last camp (" + winners + ")");
            win(winners);
        } else if (lastCamps.size() == 0) {
            win(Camps.EGALITE);
        }
    }

    @Override
    public ModeManager getModeManager() {
        return loupGarouManager;
    }

    @Override
    public void onNight() {
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§9La nuit vient de tomber.");
        Bukkit.broadcastMessage("");
        loupGarouManager.setLgChatTime(true);

        pl.getPlayerManagerOnlines().forEach(playerManager -> {
            if(playerManager.hasRole() && playerManager.isAlive()){
                if(playerManager.getRole() instanceof LGChatRole){
                    playerManager.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aLe chat des loups-garous est activé !");
                }
            }
        });
        Bukkit.getScheduler().runTaskLater(pl, () -> {
            loupGarouManager.setLgChatTime(false);
            pl.getPlayerManagerOnlines().forEach(playerManager -> {
                if(playerManager.hasRole() && playerManager.isAlive()){
                    if(playerManager.getRole() instanceof LGChatRole){
                        playerManager.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cLe chat des loups-garous est désactivé !");
                    }
                }
            });
        } , 20*30);
    }

    @Override
    public void onDay() {
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§eLe jour vient de se lever.");
        Bukkit.broadcastMessage("");
    }

    @Override
    public List<Camps> getCamps() {
        return Arrays.asList(Camps.LOUP_GAROU, Camps.VILLAGEOIS, Camps.NEUTRES, Camps.ANGE, Camps.IMITATEUR, Camps.ASSASSIN, Camps.LOUP_GAROU_BLANC);
    }

    public void win(Camps winner) {
        this.pl.gameManager.setDamage(false);

        Map<PlayerManager, Integer> damages = new HashMap<>();
        for (PlayerManager playerManager : pl.getAllPlayerManager().values()) {
            damages.put(playerManager, playerManager.getDamages());
        }
        Map<PlayerManager, Integer> kills = new HashMap<>();
        for (PlayerManager playerManager : pl.getAllPlayerManager().values()) {
            kills.put(playerManager, playerManager.getKills().size());
        }
        PlayerManager mostDamages = damages.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .findFirst().get().getKey();
        PlayerManager mostKills = kills.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .findFirst().get().getKey();

        Bukkit.broadcastMessage("§8§m-------------------------------------------------");
        Bukkit.broadcastMessage("§5");
        Bukkit.broadcastMessage("       " + winner.getWinMessage());
        Bukkit.broadcastMessage("       §cTop dégâts : " + mostDamages.getPlayerName() + " §l("
                + mostDamages.getDamages() / 2 + " §4❤§c§l)");
        Bukkit.broadcastMessage(
                "       §cTop kills : " + mostKills.getPlayerName() + " §l(" + mostKills.getKills().size() + ")");
        Bukkit.broadcastMessage("§5");
        Bukkit.broadcastMessage("       §6Merci d'avoir participé à cet host de §e§l" + pl.gameManager.getHostname());
        Bukkit.broadcastMessage("       §8Arrêt du serveur dans 30 secondes !");
        Bukkit.broadcastMessage("§8§m-------------------------------------------------");
        Bukkit.getOnlinePlayers().forEach(player -> TitleAPI.sendTitle(player, 20, 20, 20, winner.getWinMessage(), ""));

        Map<PlayerManager,Role> playersRoles = new HashMap<>();

        if (pl.gameManager.getModes().getMode() instanceof RoleMode<?>) {
            for (PlayerManager joueurs : pl.getAllPlayerManager().values()) {
                if (joueurs.hasRole()) {
                    playersRoles.put(joueurs, joueurs.getRole());
                }
            }
        }
        for (Map.Entry<PlayerManager, Role> e : playersRoles.entrySet()) {
            Bukkit.broadcastMessage((e.getKey().isAlive() ? "§6§l" : "§6§m") + e.getKey().getPlayerName() + " : " + e.getValue().getRoleName() + e.getKey().getCamps().getCompoColor() +" (Camps: " + e.getKey().getCamps().name() + ")");
        }

        Bukkit.getScheduler().runTaskLater(this.pl, () -> {
            if (this.pl.getConfig().getBoolean("bungeecord")) {
                if (this.pl.getConfig().getString("server-redirection") != null && !this.pl
                        .getConfig().getString("server-redirection").equalsIgnoreCase("null"))
                    Bukkit.getOnlinePlayers().forEach(p -> BungeeAPI.ConnectBungeeServer(p,
                            this.pl.getConfig().getString("server-redirection")));
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
        }, 30 * 20L);
        GameState.setCurrentState(GameState.ENDED);
    }

    @Override
    public String getCommandName() {
        return "lg";
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {

        List<ModeSubCommand> subCommands = new ArrayList<>();
        subCommands.add(new CmdCompo(pl));
        subCommands.add(new CmdVote(pl, loupGarouManager));
        subCommands.add(new CmdMe(pl));
        subCommands.add(new CmdDesc(pl));
        subCommands.add(new CmdList(pl));
        this.getRoles().stream().filter(role -> role instanceof RoleCommand).map(role -> ((RoleCommand) role).getSubCommands()).forEach(subCommands::addAll);

        return subCommands;
    }

    @Override
    public void giveRoles() {

    }

    @Override
    public List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        for (LGRoles role : LGRoles.values()) {
            try {
                roles.add(role.getRole().newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return roles;
    }

    @Override
    public String getDocLink() {
        return "https://app.gitbook.com/o/gIfv3yiEFOHSE0Hmzjwj/s/Usl2FNq1oqkquO0kBOdt/";
    }

    @Override
    public IQuickInventory getGui() {
        return new LoupGarouGui(this);
    }

    @Override
    public int getRoleAnnouncement() {
        return announceRoles;
    }

    public List<LoupGarouSpecialEvent> getSpecialEventList() {
        return specialEventList;
    }

    @Override
    public void setRoleAnnouncement(int roleAnnouncement) {
        this.announceRoles = roleAnnouncement;
    }

    public LoupGarouManager getLoupGarouManager() {
        return loupGarouManager;
    }

    public boolean isLgSolitaire() {
        return lgSolitaire;
    }

    public void setLgSolitaire(boolean lgSolitaire) {
        this.lgSolitaire = lgSolitaire;
    }
}
