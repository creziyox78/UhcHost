package fr.lastril.uhchost.modes.lg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.guis.modes.lg.LoupGarouGui;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.CmdCompo;
import fr.lastril.uhchost.modes.command.CmdMe;
import fr.lastril.uhchost.modes.command.ModeCommand;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.commands.CmdDesc;
import fr.lastril.uhchost.modes.lg.commands.CmdVote;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.BungeeAPI;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LoupGarouMode extends Mode implements ModeCommand, RoleMode<LGRole>, ModeConfig, RoleAnnounceMode {

    private final UhcHost pl;
    private final LoupGarouManager loupGarouManager;
    private int announceRoles = 30;

    public LoupGarouMode() {
        super(Modes.LOUP_GAROU);
        this.pl = UhcHost.getInstance();
        this.loupGarouManager = new LoupGarouManager(pl);
    }

    @Override
    public void tick(int timer) {
        if (announceRoles == 0) {
            annonceRoles();
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
        for (PlayerManager playerManager : pl.getPlayerManagerAlives()) {
            if (playerManager.getPlayer() != null) {
                Player player = playerManager.getPlayer();
                player.setHealth(player.getMaxHealth());
                playerManager.getRole().afterRoles(player);
            }
        }
        /*try {
            for (PlayerManager PlayerManager : pl.getPlayerManagerAlives()) {
                if (PlayerManager.getPlayer() != null) {
                    Player player = PlayerManager.getPlayer();
                    player.setHealth(player.getMaxHealth());
                    if (PlayerManager.getRole() != null) {
                        PlayerManager.getRole().afterRoles(player);
                        if (!PlayerManager.getRole().getRoleToKnow().isEmpty()) {
                            for (Class<? extends Role> roleToKnow : PlayerManager.getRole().getRoleToKnow()) {
                                if (!main.getNarutoManager().getPlayerManagersWithRole(roleToKnow).isEmpty()) {
                                    player.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§eLe(s) PlayerManager(s) possédant le rôle §6" + roleToKnow.newInstance().getRoleName() + "§e sont :");
                                    for (PlayerManager PlayerManagerHasRole : main.getNarutoManager().getPlayerManagersWithRole(roleToKnow)) {
                                        player.sendMessage("§6- " + PlayerManagerHasRole.getPlayerName());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }*/


        for (Role role : roles) {
            if (role instanceof RoleListener) {
                RoleListener roleListener = (RoleListener) role;
                roleListener.registerListener(pl, pl.getServer().getPluginManager());
                Bukkit.getConsoleSender().sendMessage("Register roleListener: " + role.getRoleName());
            }
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
        loupGarouManager.startDeathTask(player);
    }

    @Override
    public boolean isScheduledDeath() {
        return false;
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

    public void win(Camps winner) {
        this.pl.gameManager.setDamage(false);
        Bukkit.broadcastMessage(I18n.tl("endGame"));
        Bukkit.broadcastMessage(I18n.tl("winOfPlayer", winner.name()));
        Bukkit.broadcastMessage(I18n.tl("rebootSoon"));
        Bukkit.getScheduler().runTaskLater(this.pl, () -> {
            if (this.pl.getConfig().getBoolean("bungeecord")) {
                if (this.pl.getConfig().getString("server-redirection") != null && !this.pl
                        .getConfig().getString("server-redirection").equalsIgnoreCase("null"))
                    Bukkit.getOnlinePlayers().forEach(p -> BungeeAPI.ConnectBungeeServer(p,
                            this.pl.getConfig().getString("server-redirection")));
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
        }, 30 * 20L);
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

    @Override
    public void setRoleAnnouncement(int roleAnnouncement) {
        this.announceRoles = roleAnnouncement;
    }

    public LoupGarouManager getLoupGarouManager() {
        return loupGarouManager;
    }
}
