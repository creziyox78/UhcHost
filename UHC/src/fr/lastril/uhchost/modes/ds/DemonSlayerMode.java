package fr.lastril.uhchost.modes.ds;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.guis.modes.ds.DSCurrentCompositionGui;
import fr.lastril.uhchost.inventory.guis.modes.ds.DemonSlayerGui;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.*;
import fr.lastril.uhchost.modes.ds.roles.DSRole;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DemonSlayerMode extends Mode implements ModeCommand, RoleMode<DSRole>, ModeConfig, RoleAnnounceMode {

    private int announceRoles = 20*60;
    private final UhcHost main;

    public DemonSlayerMode() {
        super(Modes.DS);
        this.main = UhcHost.getInstance();
    }

    @Override
    public void tick(int timer) {
        if (announceRoles == 0) {
            annonceRoles();
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
    public void onDeath(OfflinePlayer player, Player killer) {

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
    public void sendInfo(Player player) {

    }

    @Override
    public void checkWin() {

    }

    @Override
    public ModeManager getModeManager() {
        return null;
    }

    @Override
    public void onNight() {

    }

    @Override
    public void onDay() {

    }

    @Override
    public List<Camps> getCamps() {
        return Arrays.asList(Camps.DEMONS, Camps.SLAYER);
    }

    @Override
    public IQuickInventory getCurrentCompoGui() {
        return new DSCurrentCompositionGui(Camps.DEMONS);
    }

    @Override
    public IQuickInventory getGui() {
        return new DemonSlayerGui(this);
    }

    @Override
    public String getCommandName() {
        return "ds";
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        List<ModeSubCommand> subCommands = new ArrayList<>();
        subCommands.add(new CmdCompo(main));
        subCommands.add(new CmdMe(main));
        subCommands.add(new CmdDesc(main));
        return subCommands;
    }

    @Override
    public int getRoleAnnouncement() {
        return announceRoles;
    }

    @Override
    public void setRoleAnnouncement(int roleAnnouncement) {
        this.announceRoles = roleAnnouncement;
    }

    @Override
    public void giveRoles() {

    }

    @Override
    public List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        for (DSRoles role : DSRoles.values()) {
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
        return null;
    }

    public void annonceRoles() {
        List<Class<? extends Role>> compo = main.getGamemanager().getComposition();
        long missedRoles = main.getPlayerManagerAlives().stream().filter(PlayerManager -> PlayerManager.isAlive() && PlayerManager.isPlayedGame()).count() - compo.size();
        Bukkit.broadcastMessage("§6§lAttribution des rôles !");
        if (missedRoles > 0) {
            for (int i = 0; i < missedRoles; i++) {
                Role role = getRoles().get(UhcHost.getRANDOM().nextInt(getRoles().size()));
                compo.add(role.getClass());
            }
        }
        List<Role> roles = new ArrayList<>();
        for (PlayerManager playerManager : main.getPlayerManagerAlives().stream().filter(PlayerManager -> PlayerManager.isAlive() && PlayerManager.isPlayedGame()).collect(Collectors.toList())) {
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
                roleListener.registerListener(main, main.getServer().getPluginManager());
                Bukkit.getConsoleSender().sendMessage("Register roleListener: " + role.getRoleName());
            }
        }

    }
}
