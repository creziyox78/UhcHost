package fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdIzanagi;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdSharingan;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.GenjutsuItem;
import fr.lastril.uhchost.modes.naruto.v2.items.SusanoItem;
import fr.lastril.uhchost.modes.naruto.v2.izanami.IzanamiGoal;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Madara;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Obito;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Naruto;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Sasuke;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.PlayerKillEvent;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Itachi extends Role implements NarutoV2Role, CmdIzanagi.IzanagiUser, SusanoItem.SusanoUser, RoleCommand, GenjutsuItem.GenjutsuUser, CmdSharingan.SharinganUser {

    private static final int HEALTH_WHEN_SASUKE_DEATH = 2*2;
    private static final int DISTANCE_NARUTO_OROCHIMARU = 5;
    private IzanamiGoal izanamiGoal;

    private int sharinganUses;
    private boolean hasUsedIzanagi;

    private boolean killedUchiwa;

    private boolean hasUsedIzanami;

    private int tsukuyomiUses;
    private boolean completeIzanami = false;

    public Itachi() {
        super.addRoleToKnow(Kisame.class);
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new SusanoItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new GenjutsuItem(main).toItemStack());
    }

    @Override
    public void afterRoles(Player player) {
        player.setMaxHealth(24);
        player.setHealth(player.getMaxHealth());
        Bukkit.getScheduler().runTaskTimer(main, () -> {
            if(!killedUchiwa && isCompleteIzanami()){
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*5, 0, false, false));
            }
        }, 0, 20*60);
    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    protected void onDay(Player player) {

    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {

    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName())
                .setTexture(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTY0YjkyMWNhOGQ1NjIwMzk4MGU4MDcxNDNlYjZmM2NjMzAwNDExY2JkNDY0MDY1YmNjZGU1Njc4ZDJlYTE4YiJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.AKATSUKI;
    }

    @Override
    public String getRoleName() {
        return "Itachi";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(joueur.getCamps() == Camps.OROCHIMARU){
            for (Entity entity : player.getNearbyEntities(DISTANCE_NARUTO_OROCHIMARU, DISTANCE_NARUTO_OROCHIMARU, DISTANCE_NARUTO_OROCHIMARU)) {
                if(entity instanceof Player){
                    Player players = (Player) entity;
                    if(players.getGameMode() != GameMode.SPECTATOR){
                        PlayerManager joueurs = main.getPlayerManager(players.getUniqueId());
                        if (joueurs.hasRole() && joueurs.getRole() instanceof Naruto) {
                            joueur.setCamps(Camps.SHINOBI);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"§aVous avez rencontré Naruto, donc vous rejoignez le camps des Shinobi.");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onKillUchiwa(PlayerKillEvent event){
        Player killer = event.getKiller();
        PlayerManager joueur = main.getPlayerManager(killer.getUniqueId());
        Player deadPlayer = event.getDeadPlayer();
        if (joueur.isAlive() && joueur.hasRole()) {
            if (joueur.getRole() instanceof Itachi) {
                PlayerManager deadJoueur = main.getPlayerManager(deadPlayer.getUniqueId());
                if(deadJoueur.getRole() instanceof Madara || deadJoueur.getRole() instanceof Itachi || deadJoueur.getRole() instanceof Sasuke || deadJoueur.getRole() instanceof Obito){
                    killedUchiwa = true;
                    killer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous venez de tuer un Uchiwa, vous n'avez plus§7Blindness I§a.");
                }
            }
        }
    }

    public int getSharinganUses() {
        return sharinganUses;
    }

    public void addSharinganUse() {
        this.sharinganUses++;
    }

    @Override
    public boolean hasUsedIzanagi() {
        return hasUsedIzanagi;
    }

    @Override
    public void setHasUsedIzanagi(boolean hasUsedIzanagi) {
        this.hasUsedIzanagi = hasUsedIzanagi;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdIzanagi(main), new CmdSharingan(main));
    }

    @Override
    public void useTsukuyomi() {
        tsukuyomiUses++;
    }

    @Override
    public int getTsukuyomiUses() {
        return tsukuyomiUses;
    }

    @Override
    public void useIzanami() {
        hasUsedIzanami = true;
    }

    @Override
    public boolean hasUsedIzanami() {
        return hasUsedIzanami;
    }

    @Override
    public boolean hasKilledUchiwa() {
        return killedUchiwa;
    }

    @Override
    public IzanamiGoal getIzanamiGoal() {
        return izanamiGoal;
    }

    @Override
    public boolean isCompleteIzanami() {
        return completeIzanami;
    }

    @Override
    public void setCompleteIzanami(boolean completeIzanami) {
        this.completeIzanami = completeIzanami;
    }

    @Override
    public void setIzanamiGoal(IzanamiGoal izanamiGoal) {
        this.izanamiGoal = izanamiGoal;
    }

    @Override
	public Chakra getChakra() {
		return Chakra.KATON;
	}

}

