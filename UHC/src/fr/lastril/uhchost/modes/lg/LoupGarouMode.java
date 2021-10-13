package fr.lastril.uhchost.modes.lg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.inventory.guis.modes.LoupGarouGui;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeCommand;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.lg.roles.lg.InfectPereDesLoups;
import fr.lastril.uhchost.modes.lg.roles.lg.LoupGarou;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.BungeeAPI;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
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
    private int announceRoles = 30;
    private final LoupGarouManager loupGarouManager;

    public LoupGarouMode() {
        super(Modes.LOUP_GAROU);
        this.pl = UhcHost.getInstance();
        this.loupGarouManager = new LoupGarouManager(pl);
    }

    @Override
    public void tick(int timer) {
        if(announceRoles == 0){
            annonceRoles();
        }
    }

    public void annonceRoles(){
        List<Class<? extends Role>> compo = pl.getGamemanager().getComposition();
        /*long missedRoles = pl.getPlayerManagerAlives().stream().filter(joueur -> joueur.isAlive() && joueur.isPlayedGame()).count() - compo.size();
        Bukkit.broadcastMessage("§6§lAttribution des rôles !");
        if (missedRoles > 0) {
            for (int i = 0; i < missedRoles; i++) {
                Role role = mode.getRoles().get(UhcHost.getRANDOM().nextInt(mode.getRoles().size()));
                compo.add(role.getClass());
            }
        }*/

        List<Role> roles = new ArrayList<>();
        for (PlayerManager joueur : pl.getPlayerManagerAlives().stream().filter(joueur -> joueur.isAlive() && joueur.isPlayedGame()).collect(Collectors.toList())) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(joueur.getUuid());
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
                player.sendMessage("§7§m------------------------------------------");
                player.sendMessage("§7Vous êtes " + role.getCamp().getCompoColor() + "" + role.getRoleName() + "§7.");
                player.sendMessage(role.getDescription() != null ? "§7" + role.getDescription() : "§c§lDESCRIPTION NULL");
                player.sendMessage("§7§m------------------------------------------");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 10);
                role.stuff(player);
            }
            role.setPlayerID(offlinePlayer.getUniqueId());
            joueur.setRole(role);
            compo.remove(index);
            roles.add(role);
        }
        /*try {
            for (PlayerManager joueur : pl.getPlayerManagerAlives()) {
                if (joueur.getPlayer() != null) {
                    Player player = joueur.getPlayer();
                    player.setHealth(player.getMaxHealth());
                    if (joueur.getRole() != null) {
                        joueur.getRole().afterRoles(player);
                        if (!joueur.getRole().getRoleToKnow().isEmpty()) {
                            for (Class<? extends Role> roleToKnow : joueur.getRole().getRoleToKnow()) {
                                if (!main.getNarutoManager().getJoueursWithRole(roleToKnow).isEmpty()) {
                                    player.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§eLe(s) joueur(s) possédant le rôle §6" + roleToKnow.newInstance().getRoleName() + "§e sont :");
                                    for (Joueur joueurHasRole : main.getNarutoManager().getJoueursWithRole(roleToKnow)) {
                                        player.sendMessage("§6- " + joueurHasRole.getPlayerName());
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
        return false;
    }

    @Override
    public void onDamage(Player target, Player damager) {

    }

    @Override
    public void checkWin() {
        Set<Camps> lastCamps = new HashSet<>();
        for (Player players : Bukkit.getOnlinePlayers()) {
            PlayerManager joueur = pl.getPlayerManager(players.getUniqueId());
            if (joueur.isAlive()) {
                lastCamps.add(joueur.getCamps());
            }
        }
        if (lastCamps.size() == 1) {
            Camps winners = lastCamps.stream().findFirst().get();
            Bukkit.getConsoleSender().sendMessage("Founded 1 last camp (" + winners + ")");
            win(winners);
        } else if(lastCamps.size() == 0){
            win(Camps.EGALITE);
        }
    }

    public void win(Camps winner) {
        this.pl.gameManager.setDamage(false);
        Bukkit.broadcastMessage(I18n.tl("endGame", new String[0]));
        Bukkit.broadcastMessage(I18n.tl("winOfPlayer", winner.name()));
        Bukkit.broadcastMessage(I18n.tl("rebootSoon", new String[0]));
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
    public Gui getGui(Player player) {
        return new LoupGarouGui(player, this);
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
