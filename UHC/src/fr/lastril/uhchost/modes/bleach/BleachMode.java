package fr.lastril.uhchost.modes.bleach;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.guis.modes.bleach.BleachCompositionGui;
import fr.lastril.uhchost.inventory.guis.modes.bleach.BleachGui;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.bleach.commands.CmdCero;
import fr.lastril.uhchost.modes.bleach.roles.ArrancarRole;
import fr.lastril.uhchost.modes.bleach.roles.BLRole;
import fr.lastril.uhchost.modes.command.*;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.BungeeAPI;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.TitleAPI;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.packets.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;

public class BleachMode extends Mode implements ModeConfig, RoleAnnounceMode, ModeCommand, RoleMode<BLRole> {

    private final UhcHost main;
    private final BleachManager bleachManager;
    private int announceRoles = 60;
    private int phase = 1;

    public BleachMode() {
        super(Modes.BLEACH);
        this.main = UhcHost.getInstance();
        this.bleachManager = new BleachManager(main);
    }

    @Override
    public void tick(int timer) {
        if(timer == 0){
            setPhase(1);
        }
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
            if(role instanceof RolePacket){
                RolePacket packetListener = (RolePacket) role;
                Bukkit.getConsoleSender().sendMessage("Register packetRoleListener: " + role.getRoleName());
            }
        }

    }


    @Override
    public void onDeath(OfflinePlayer player, Player killer) {
        bleachManager.onDeath(player, killer);
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

    private int getPourcentageEffect(Player player, int pourcentagePerLvl, PotionEffectType potionEffectType){
        int pourcentageEffect = 0;
        for(PotionEffect potionEffect : player.getActivePotionEffects()){
            if(potionEffect.getType() == potionEffectType){
                pourcentageEffect += (potionEffect.getAmplifier() + 1) * pourcentagePerLvl;
            }
        }
        return pourcentageEffect;
    }

    @Override
    public void sendInfo(Player player) {

        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        player.sendMessage("§f» §m------------------------------------------§f «");
        player.sendMessage(" ");
        player.sendMessage("§f • §cForce » §f" + (bleachPlayerManager.getStrengthPourcentage() + getPourcentageEffect(player, 20, PotionEffectType.INCREASE_DAMAGE)) + "%");
        player.sendMessage("§f • §9Résistance » §f" + (bleachPlayerManager.getResistancePourcentage() + getPourcentageEffect(player, 20, PotionEffectType.DAMAGE_RESISTANCE)) + "%");
        player.sendMessage("§f • §bSpeed » §f" + (bleachPlayerManager.getSpeedPourcentage() + getPourcentageEffect(player, 20, PotionEffectType.SPEED)) +  "%");
        player.sendMessage("§f • §aNoFall » §f" + (bleachPlayerManager.hasNoFall() ? "§aOui" : "§cNon"));
        if(playerManager.hasRole() && playerManager.getRole() instanceof ArrancarRole){
            ArrancarRole arrancarRole = (ArrancarRole) playerManager.getRole();
            player.sendMessage("§f • §eQuartz minés » §f" + bleachPlayerManager.getNbQuartzMined() + "/" + arrancarRole.getNbQuartz());
            player.sendMessage("§f • §eForme libérée » §f" + new FormatTime(bleachPlayerManager.getFormeLibererDurationRemining()).toFormatString());
        }
        player.sendMessage(" ");
        player.sendMessage("§f • §eCooldowns :");
        for (Map.Entry<String, Integer> e : playerManager.getCooldowns().entrySet()) {
            if(e.getValue() > 0){
                player.sendMessage("§f       - §6" + e.getKey() + " » §c" + new FormatTime(e.getValue()));
            }
        }
        player.sendMessage(" ");
        player.sendMessage("§f» §m------------------------------------------§f «");
    }

    @Override
    public ModeManager getModeManager() {
        return bleachManager;
    }

    @Override
    public void onNight() {

    }

    @Override
    public void onDay() {

    }

    @Override
    public List<Camps> getCamps() {
        return Arrays.asList(Camps.SHINIGAMIS, Camps.ARRANCARS);
    }

    @Override
    public IQuickInventory getCurrentCompoGui() {
        return new BleachCompositionGui(Camps.SHINIGAMIS);
    }

    @Override
    public IQuickInventory getGui() {
        return new BleachGui(this);
    }

    @Override
    public void giveRoles() {

    }

    @Override
    public List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        for (BleachRoles role : BleachRoles.values()) {
            try {
                roles.add(role.getRole().newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return roles;
    }

    @Override
    public void checkWin() {
        Set<Camps> lastCamps = new HashSet<>();
        for (Player players : Bukkit.getOnlinePlayers()) {
            PlayerManager playerManager = main.getPlayerManager(players.getUniqueId());
            if (playerManager.isAlive() && playerManager.getCamps() != null) {
                lastCamps.add(playerManager.getCamps());
            }
        }
        if (lastCamps.size() == 1) {
            Camps winners = lastCamps.stream().findFirst().get();
            Bukkit.getConsoleSender().sendMessage("Founded 1 last camp (" + winners + ")");
            win(winners);
        } else if (lastCamps.size() == 0) {

        }
    }

    public void win(Camps winner) {
        this.main.gameManager.setDamage(false);

        Map<PlayerManager, Integer> damages = new HashMap<>();
        for (PlayerManager playerManager : main.getAllPlayerManager().values()) {
            damages.put(playerManager, playerManager.getDamages());
        }
        Map<PlayerManager, Integer> kills = new HashMap<>();
        for (PlayerManager playerManager : main.getAllPlayerManager().values()) {
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
        Bukkit.broadcastMessage("       §6Merci d'avoir participé à cet host de §e§l" + main.gameManager.getHostname());
        Bukkit.broadcastMessage("       §8Arrêt du serveur dans 30 secondes !");
        Bukkit.broadcastMessage("§8§m-------------------------------------------------");
        Bukkit.getOnlinePlayers().forEach(player -> TitleAPI.sendTitle(player, 20, 20, 20, winner.getWinMessage(), ""));

        Map<PlayerManager,Role> playersRoles = new HashMap<>();

        if (main.gameManager.getModes().getMode() instanceof RoleMode<?>) {
            for (PlayerManager joueurs : main.getAllPlayerManager().values()) {
                if (joueurs.hasRole()) {
                    playersRoles.put(joueurs, joueurs.getRole());
                }
            }
        }
        for (Map.Entry<PlayerManager, Role> e : playersRoles.entrySet()) {
            Bukkit.broadcastMessage((e.getKey().isAlive() ? "§6§l" : "§6§m") + e.getKey().getPlayerName() + " : " + e.getValue().getRoleName() + e.getKey().getCamps().getCompoColor() +" (Camps: " + e.getKey().getCamps().name() + ")");
        }

        Bukkit.getScheduler().runTaskLater(this.main, () -> {
            if (this.main.getConfig().getBoolean("bungeecord")) {
                if (this.main.getConfig().getString("server-redirection") != null && !this.main
                        .getConfig().getString("server-redirection").equalsIgnoreCase("null"))
                    Bukkit.getOnlinePlayers().forEach(p -> BungeeAPI.ConnectBungeeServer(p,
                            this.main.getConfig().getString("server-redirection")));
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
        }, 30 * 20L);
    }

    @Override
    public String getDocLink() {
        return "https://bleach-uhc-1.gitbook.io/bleach-uhc/";
    }

    @Override
    public int getRoleAnnouncement() {
        return announceRoles;
    }

    @Override
    public void setRoleAnnouncement(int roleAnnouncement) {
        announceRoles = roleAnnouncement;
    }

    @Override
    public String getCommandName() {
        return "b";
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        List<ModeSubCommand> subCommands = new ArrayList<>();
        subCommands.add(new CmdCompo(main));
        subCommands.add(new CmdDesc(main));
        subCommands.add(new CmdMe(main));
        subCommands.add(new CmdInfo(main));
        subCommands.add(new CmdCero(main));
        this.getRoles().stream().filter(role -> role instanceof RoleCommand).map(role -> ((RoleCommand) role).getSubCommands()).forEach(subCommands::addAll);
        return subCommands;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }
}
