package fr.lastril.uhchost.player;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.modemanager.*;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.FormatTime;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PlayerManager {

	private final UUID uuid;

	private final Map<String, Integer> cooldowns;

	private final List<UUID> kills;

	private boolean alive, playedGame, useHuitimePorte, invinsible;

	private WolfPlayerManager wolfPlayerManager;

	private final DSPlayerManager dsPlayerManager;

	private final BleachPlayerManager bleachPlayerManager;

	private final TaupePlayerManager taupePlayerManager;

	private final MarketPlayerManager marketPlayerManager;

	private final UhcHost pl;

	private Player player;

	private final String playerName;

	private PlayerState playerstats;

	ItemStack[] items , armors;

	private int damages;

	private Role role;
	private Camps camps;
	private Location lastDisconnection, deathLocation, stunLocation;
	private long deathTime;
	private boolean stunned;

	public PlayerManager(UUID uuid) {
		this.uuid = uuid;
		this.playerstats = PlayerState.WAITING;
		this.player = Bukkit.getPlayer(uuid);
		this.pl = UhcHost.getInstance();
		this.alive = false;
		this.playedGame = false;
		this.lastDisconnection = player.getLocation();
		this.cooldowns = new HashMap<>();
		this.kills = new ArrayList<>();
		this.playerName = player.getName();
		this.bleachPlayerManager = new BleachPlayerManager(this);
		this.dsPlayerManager = new DSPlayerManager(this);
		this.taupePlayerManager = new TaupePlayerManager(this);
		this.wolfPlayerManager = new WolfPlayerManager(this);
		this.marketPlayerManager = new MarketPlayerManager(this);
		this.invinsible = false;
		UhcHost.getInstance().getAllWolfPlayerManager().put(uuid, getWolfPlayerManager());
	}

	public BleachPlayerManager getBleachPlayerManager() {
		return bleachPlayerManager;
	}

	public void removeCooldowns() {
		Map<String, Integer> newCooldowns = new HashMap<>();
		for (Map.Entry<String, Integer> e : this.cooldowns.entrySet()) {
			int newCooldown = e.getValue()-1;
			if(newCooldown == 0){
				if(getPlayer() != null){
					getPlayer().sendMessage(Messages.COOLDOWNPREFIX.getMessage() + "§aLe cooldown§e " + e.getKey() + "§a est à 0. Vous pouvez réutiliser ce pouvoir.");
				}
				continue;
			}
			if(newCooldown < 0){
				continue;
			}
			newCooldowns.put(e.getKey(), newCooldown);
		}
		this.cooldowns.clear();
		this.cooldowns.putAll(newCooldowns);
	}

	public void addCooldowns() {
		Map<String, Integer> newCooldowns = new HashMap<>();
		for (Map.Entry<String, Integer> e : this.cooldowns.entrySet()) {
			Bukkit.getConsoleSender().sendMessage("Cooldown Add for " + this.getPlayerName() +", Key: " + e.getKey() + " , value: " + new FormatTime(e.getValue()));
			int newCooldown = e.getValue()+1;
			newCooldowns.put(e.getKey(), newCooldown);
		}
		this.cooldowns.clear();
		this.cooldowns.putAll(newCooldowns);
	}

	public TaupePlayerManager getTaupePlayerManager() {
		return taupePlayerManager;
	}

	public MarketPlayerManager getMarketPlayerManager() {
		return marketPlayerManager;
	}

	public void sendTimer(Player player, int timer, ItemStack item) {
		int cooldown = timer;

		new BukkitRunnable() {
			int realCooldown = cooldown;
			int ticks = 20;
			@Override
			public void run() {
				if(ticks == 0){
					realCooldown--;
					ticks = 20;
				}
				if(player.getItemInHand().isSimilar(item)){
					String msgCooldown = "§aCooldown restant : §e" + new FormatTime(realCooldown).toString();
					ActionBar.sendMessage(player, msgCooldown);
				}
				if (realCooldown == 0) {
					cancel();
				}
				ticks--;
			}

		}.runTaskTimer(UhcHost.getInstance(), 0, 1);
	}

	public Map<String, Integer> getCooldowns() {
		return cooldowns;
	}

	public DSPlayerManager getDSPlayerManager() {
		return dsPlayerManager;
	}

	public boolean isInvinsible() {
		return invinsible;
	}

	public void setInvinsible(boolean invinsible) {
		this.invinsible = invinsible;
	}

	public void clearCooldowns() {
		this.cooldowns.clear();
	}

	public void addDamages(int newDamage) {
		this.damages += newDamage;
	}

	public int getDamages() {
		return damages;
	}

	public void setDamages(int damages) {
		this.damages = damages;
	}

	private int getRoleCooldown(String key) {
		return this.cooldowns.getOrDefault(key, 0);
	}

	private void setRoleCooldown(String key, int value) {
		this.cooldowns.put(key, value);
	}

	public int getRoleCooldownHeal() {
		return this.getRoleCooldown("heal");
	}

	public void setRoleCooldownHeal(int roleCooldownHeal) {
		this.setRoleCooldown("heal", roleCooldownHeal);
	}

	public List<UUID> getKills() {
		return kills;
	}

	public int getRoleCooldownEnnetsu(){
		return this.getRoleCooldown("ennestu");
	}

	public void setRoleCooldownEnnetsu(int i){
		this.setRoleCooldown("ennestu", i);
	}

	public int getRoleCooldownRyujinJakka(){
		return this.getRoleCooldown("RyujinJakka");
	}

	public void setRoleCooldownRyujinJakka(int i){
		this.setRoleCooldown("RyujinJakka", i);
	}

	public int getRoleCooldownSuzumebachi(){
		return this.getRoleCooldown("Suzumebachi");
	}

	public void setRoleCooldownSuzumebachi(int i){
		this.setRoleCooldown("Suzumebachi", i);
	}

	public void setRoleCooldownGegetsuburiGrab(int i) {
		this.setRoleCooldown("Gegetsuburi Grab", i);
	}

	public int getRoleCooldownGegetsuburiGrab(){
		return this.getRoleCooldown("Gegetsuburi Grab");
	}

	public void setRoleCooldownGegetsuburiDamage(int i) {
		this.setRoleCooldown("Gegetsuburi Dégâts", i);
	}

	public int getRoleCooldownGegetsuburiDamage(){
		return this.getRoleCooldown("Gegetsuburi Dégâts");
	}

	public void setRoleCooldownUnohanaHeal(int i) {
		this.setRoleCooldown("Soins", i);
	}

	public int getRoleCooldownUnohanaHeal(){
		return this.getRoleCooldown("Soins");
	}

	public void setRoleCooldownMinazuki(int i) {
		this.setRoleCooldown("Minazuki", i);
	}

	public int getRoleCooldownMinazuki(){
		return this.getRoleCooldown("Minazuki");
	}

	public void setRoleCooldownCristal(int i) {
		this.setRoleCooldown("Cristal", i);
	}

	public int getRoleCooldownCristal(){
		return this.getRoleCooldown("Cristal");
	}

	public void setRoleCooldownWabisuke(int i) {
		this.setRoleCooldown("Wabisuke", i);
	}

	public int getRoleCooldownWabisuke(){
		return this.getRoleCooldown("Wabisuke");
	}

	public void setRoleCooldownItegumo(int i) {
		this.setRoleCooldown("Itegumo", i);
	}

	public int getRoleCooldownItegumo(){
		return this.getRoleCooldown("Itegumo");
	}

	public void setRoleCooldownWave(int i) {
		this.setRoleCooldown("Wave", i);
	}

	public int getRoleCooldownWave(){
		return this.getRoleCooldown("Wave");
	}

	public void setRoleCooldownSnap(int i) {
		this.setRoleCooldown("Snap", i);
	}

	public int getRoleCooldownSnap(){
		return this.getRoleCooldown("Snap");
	}

	public void setRoleCooldownSakura(int i) {
		this.setRoleCooldown("Sakura", i);
	}

	public int getRoleCooldownSakura(){
		return this.getRoleCooldown("Sakura");
	}

	public void setRoleCooldownSenbonzakura(int i) {
		this.setRoleCooldown("Senbonzakura", i);
	}

	public int getRoleCooldownSenbonzakura(){
		return this.getRoleCooldown("Senbonzakura");
	}

	public void setRoleCooldownRyodan(int i) {
		this.setRoleCooldown("Ryodan", i);
	}

	public int getRoleCooldownRyodan(){
		return this.getRoleCooldown("Ryodan");
	}

	public void setRoleCooldownNanao(int i) {
		this.setRoleCooldown("Suppression", i);
	}

	public int getRoleCooldownNanao(){
		return this.getRoleCooldown("Suppression");
	}

	public void setRoleCooldownKazeshini(int i) {
		this.setRoleCooldown("Kazeshini", i);
	}

	public int getRoleCooldownKazeshini(){
		return this.getRoleCooldown("Kazeshini");
	}

	public void setRoleCooldownChaines(int i) {
		this.setRoleCooldown("Chaînes", i);
	}

	public int getRoleCooldownChaines(){
		return this.getRoleCooldown("Chaînes");
	}

	public void setRoleCooldownHiver(int i) {
		this.setRoleCooldown("Hiver", i);
	}

	public int getRoleCooldownHiver(){
		return this.getRoleCooldown("Hiver");
	}

	public void setRoleCooldownHyorinmaru(int i) {
		this.setRoleCooldown("Hyorinmaru", i);
	}

	public int getRoleCooldownHyorinmaru(){
		return this.getRoleCooldown("Hyorinmaru");
	}

	public void setRoleCooldownOeil(int i) {
		this.setRoleCooldown("Oeil", i);
	}

	public int getRoleCooldownOeil(){
		return this.getRoleCooldown("Oeil");
	}

	public void setRoleCooldownHaineko(int i) {
		this.setRoleCooldown("Haineko", i);
	}

	public int getRoleCooldownHaineko(){
		return this.getRoleCooldown("Haineko");
	}

	public void setRoleCooldownToshiroZone(int i) {
		this.setRoleCooldown("ToshiroZone", i);
	}

	public int getRoleCooldownToshiroZone(){
		return this.getRoleCooldown("ToshiroZone");
	}

	public void setRoleCooldownSoutien(int i) {
		this.setRoleCooldown("Soutien", i);
	}

	public int getRoleCooldownSoutien(){
		return this.getRoleCooldown("Soutien");
	}

	public void setRoleCooldownShikai(int i) {
		this.setRoleCooldown("Shikai", i);
	}

	public int getRoleCooldownShikai(){
		return this.getRoleCooldown("Shikai");
	}

	public void setRoleCooldownHakuda(int i) {
		this.setRoleCooldown("Hakuda", i);
	}

	public int getRoleCooldownHakuda(){
		return this.getRoleCooldown("Hakuda");
	}

	public void setRoleCooldownRenvoie(int i) {
		this.setRoleCooldown("Renvoie", i);
	}

	public int getRoleCooldownRenvoie(){
		return this.getRoleCooldown("Renvoie");
	}

	public void setRoleCooldownDanseDuVent(int i) {
		this.setRoleCooldown("Danse du vent", i);
	}

	public int getRoleCooldownDanseDuVent(){
		return this.getRoleCooldown("Danse du vent");
	}

	public void setRoleCooldownKageoni(int i) {
		this.setRoleCooldown("Kageoni", i);
	}

	public int getRoleCooldownKageoni(){
		return this.getRoleCooldown("Kageoni");
	}

	public void setRoleCooldownRyusenka(int i) {
		this.setRoleCooldown("Ryusenka", i);
	}

	public int getRoleCooldownRyusenka(){
		return this.getRoleCooldown("Ryusenka");
	}

	public void setRoleCooldownHozukimaru(int i) {
		this.setRoleCooldown("Hozukimaru", i);
	}

	public int getRoleCooldownHozukimaru(){
		return this.getRoleCooldown("Hozukimaru");
	}

	public void setRoleCooldownGetsugaTensho(int i) {
		this.setRoleCooldown("Getsuga Tensho", i);
	}

	public int getRoleCooldownGetsugaTensho(){
		return this.getRoleCooldown("Getsuga Tensho");
	}

	public void setRoleCooldownHollowMask(int i) {
		this.setRoleCooldown("Hollow Mask", i);
	}

	public int getRoleCooldownHollowMask(){
		return this.getRoleCooldown("Hollow Mask");
	}

	public void setRoleCooldownVasto(int i) {
		this.setRoleCooldown("Vasto", i);
	}

	public int getRoleCooldownVasto(){
		return this.getRoleCooldown("Vasto");
	}

	public void setRoleCooldownBrazo(int i) {
		this.setRoleCooldown("Brazo", i);
	}

	public int getRoleCooldownBrazo(){
		return this.getRoleCooldown("Brazo");
	}

	public void setRoleCooldownMuerte(int i) {
		this.setRoleCooldown("Muerte", i);
	}

	public int getRoleCooldownMuerte(){
		return this.getRoleCooldown("Muerte");
	}

	public void setRoleCooldownCriticalSado(int i) {
		this.setRoleCooldown("Coup critique", i);
	}

	public int getRoleCooldownCriticalSado(){
		return this.getRoleCooldown("Coup critique");
	}

	public void setRoleCooldownSodeNoShirayuki(int i) {
		this.setRoleCooldown("Sode No Shirayuki", i);
	}

	public int getRoleCooldownSodeNoShirayuki(){
		return this.getRoleCooldown("Sode No Shirayuki");
	}

	public void setRoleCooldownClone(int i) {
		this.setRoleCooldown("Clone", i);
	}

	public int getRoleCooldownClone(){
		return this.getRoleCooldown("Clone");
	}

	public void setRoleCooldownBenihime(int i) {
		this.setRoleCooldown("Benihime", i);
	}

	public int getRoleCooldownBenihime(){
		return this.getRoleCooldown("Benihime");
	}

	public void setRoleCooldownRika(int i) {
		this.setRoleCooldown("Rika", i);
	}

	public int getRoleCooldownRika(){
		return this.getRoleCooldown("Rika");
	}

	public void setRoleCooldownMur(int i) {
		this.setRoleCooldown("Mur", i);
	}

	public int getRoleCooldownMur(){
		return this.getRoleCooldown("Mur");
	}

	public void setRoleCooldownLynx(int i) {
		this.setRoleCooldown("Lynx", i);
	}

	public int getRoleCooldownLynx(){
		return this.getRoleCooldown("Lynx");
	}

	public void setRoleCooldownKido(int i) {
		this.setRoleCooldown("Kido", i);
	}

	public int getRoleCooldownKido(){
		return this.getRoleCooldown("Kido");
	}

	public void setRoleCooldownOkami(int i) {
		this.setRoleCooldown("Kido", i);
	}

	public int getRoleCooldownOkami(){
		return this.getRoleCooldown("Kido");
	}

	public void setRoleCooldownCascada(int i) {
		this.setRoleCooldown("Cascada", i);
	}

	public int getRoleCooldownCascada(){
		return this.getRoleCooldown("Cascada");
	}

	public void setRoleCooldownAiles(int i) {
		this.setRoleCooldown("Ailes", i);
	}

	public int getRoleCooldownAiles(){
		return this.getRoleCooldown("Ailes");
	}

	public void setRoleCooldownLanzaDelRelampago(int i) {
		this.setRoleCooldown("Lanza Del Relampago", i);
	}

	public int getRoleCooldownLanzaDelRelampago(){
		return this.getRoleCooldown("Lanza Del Relampago");
	}

	public void setRoleCooldownFormeLiberer(int i) {
		this.setRoleCooldown("Forme Libéré", i);
	}

	public int getRoleCooldownFormeLiberer(){
		return this.getRoleCooldown("Forme Libéré");
	}

	public void setRoleCooldownCeroFaible(int i) {
		this.setRoleCooldown("Cero Faible", i);
	}

	public int getRoleCooldownCeroFaible(){
		return this.getRoleCooldown("Cero Faible");
	}

	public void setRoleCooldownCeroMoyen(int i) {
		this.setRoleCooldown("Cero Moyen", i);
	}

	public int getRoleCooldownCeroMoyen(){
		return this.getRoleCooldown("Cero Moyen");
	}

	public void setRoleCooldownCeroFort(int i) {
		this.setRoleCooldown("Cero Fort", i);
	}

	public int getRoleCooldownCeroFort(){
		return this.getRoleCooldown("Cero Fort");
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public PlayerState getPlayerstats() {
		return this.playerstats;
	}

	public void setPlayerstats(PlayerState playerstats) {
		this.playerstats = playerstats;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isAlive() {
		return alive;
	}

	public Camps getCamps() {
		return camps;
	}

	public String getPlayerName() {
		return playerName;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
		this.camps = role.getCamp();
	}

	public WolfPlayerManager getWolfPlayerManager() {
		return wolfPlayerManager;
	}

	public void setWolfPlayerManager(WolfPlayerManager wolfPlayerManager) {
		this.wolfPlayerManager = wolfPlayerManager;
	}

	public boolean isDead() {
		return this.deathLocation != null;
	}

	public boolean isOnline() {
		return this.getPlayer() != null;
	}

	public boolean hasRole() {
		return getRole() != null;
	}

	public Location getDeathLocation() {
		return deathLocation;
	}

	public void setDeathLocation(Location deathLocation) {
		this.deathLocation = deathLocation;
		this.deathTime = System.currentTimeMillis();
	}

	public Location getLastDisconnection() {
		return this.lastDisconnection;
	}

	public void setLastDisconnection(Location lastDisconnection) {
		this.lastDisconnection = lastDisconnection;
	}

	public boolean isStunned() {
		return stunned;
	}

	public void setStunned(boolean stunned) {
		this.stunned = stunned;
	}

	public void stun(Location loc){
		this.stunLocation = loc;
		this.stunned = true;
	}

	public boolean isUseHuitimePorte() {
		return useHuitimePorte;
	}

	public void setUseHuitimePorte(boolean useHuitimePorte) {
		this.useHuitimePorte = useHuitimePorte;
	}

	public void setCamps(Camps camps) {
		this.camps = camps;
	}

	public Location getStunLocation() {
		return stunLocation;
	}

	public boolean isPlayedGame() {
		return playedGame;
	}

	public void setPlayedGame(boolean playedGame) {
		this.playedGame = playedGame;
	}

    public boolean isModerator(){
		return getPlayer().isOp();
	}

	public void addKill(UUID killed) {
		kills.add(killed);
		UhcHost.debug("added kill to " + playerName +". Amount Kills: " + kills.size());
	}

	public long getDeathTime() {
		return deathTime;
	}

	public ItemStack[] getArmors() {
		return armors;
	}

	public void setArmors(ItemStack[] armors) {
		this.armors = armors;
	}

	public ItemStack[] getItems() {
		return items;
	}

	public void setItems(ItemStack[] items) {
		this.items = items;
	}



}
