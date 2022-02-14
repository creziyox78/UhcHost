package fr.lastril.uhchost.player;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.player.modemanager.DSPlayerManager;
import fr.lastril.uhchost.player.modemanager.WolfPlayerManager;
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

	private boolean alive, playedGame, useHuitimePorte;

	private WolfPlayerManager wolfPlayerManager;

	private final DSPlayerManager dsPlayerManager;

	private final BleachPlayerManager bleachPlayerManager;

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
		this.setWolfPlayerManager(new WolfPlayerManager(this));
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

	public DSPlayerManager getDSPlayerManager() {
		return dsPlayerManager;
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

	public int getRoleCooldownRinnegan() {
		return this.getRoleCooldown("rinnegan");
	}

	public void setRoleCooldownRinnegan(int roleCooldownRinnegan) {
		this.setRoleCooldown("rinnegan", roleCooldownRinnegan);
	}

	public List<UUID> getKills() {
		return kills;
	}

	public int getRoleCooldownJubi() {
		return this.getRoleCooldown("jubi");
	}

	public void setRoleCooldownJubi(int roleCooldownJubi) {
		this.setRoleCooldown("jubi", roleCooldownJubi);
	}

	public int getRoleCooldownBiju() {
		return this.getRoleCooldown("biju");
	}

	public void setRoleCooldownBiju(int roleCooldownBiju) {
		this.setRoleCooldown("biju", roleCooldownBiju);
	}

	public int getRoleCooldownKonan() {
		return this.getRoleCooldown("konan");
	}

	public void setRoleCooldownKonan(int roleCooldownKonan) {
		this.setRoleCooldown("konan", roleCooldownKonan);
	}

	public int getRoleCooldownCorpsRapiece() {
		return this.getRoleCooldown("corps_rapiece");
	}

	public void setRoleCooldownCorpsRapiece(int roleCooldownCorpsRapiece) {
		this.setRoleCooldown("corps_rapiece", roleCooldownCorpsRapiece);
	}

	public int getRoleCooldownFluteDemoniaque() {
		return this.getRoleCooldown("flute_demoniaque");
	}

	public void setRoleCooldownFluteDemoniaque(int roleCooldownFluteDemoniaque) {
		this.setRoleCooldown("flute_demoniaque", roleCooldownFluteDemoniaque);
	}

	public int getRoleCooldownMarqueMaudite() {
		return this.getRoleCooldown("marque_maudite");
	}

	public void setRoleCooldownMarqueMaudite(int roleCooldownMarqueMaudite) {
		this.setRoleCooldown("marque_maudite", roleCooldownMarqueMaudite);
	}


	public int getRoleCooldownTroisPortes() {
		return this.getRoleCooldown("trois_portes");
	}

	public void setRoleCooldownTroisPortes(int roleCooldownTroisPortes) {
		this.setRoleCooldown("trois_portes", roleCooldownTroisPortes);
	}

	public int getRoleCooldownSixPortes() {
		return this.getRoleCooldown("six_portes");
	}

	public void setRoleCooldownSixPortes(int roleCooldownSixPortes) {
		this.setRoleCooldown("six_portes", roleCooldownSixPortes);
	}

	public int getRoleCooldownKaguraShingan() {
		return this.getRoleCooldown("kagura_shingan");
	}

	public void setRoleCooldownKaguraShingan(int roleCooldownKaguraShingan) {
		this.setRoleCooldown("kagura_shingan", roleCooldownKaguraShingan);
	}

	public int getRoleCooldownTendo() {
		return this.getRoleCooldown("tendo");
	}

	public int getRoleCooldownDirtProtection() {
		return this.getRoleCooldown("dirt_protection");
	}

	public void setRoleCooldownDirtProtection(int roleCooldownDirtProtection) {
		this.setRoleCooldown("dirt_protection", roleCooldownDirtProtection);
	}

	public int getRoleCooldownSanbi() {
		return this.getRoleCooldown("sanbi");
	}

	public void setRoleCooldownSanbi(int roleCooldownSanbi) {
		this.setRoleCooldown("sanbi", roleCooldownSanbi);
	}

	public int getRoleCooldownFireElement() {
		return this.getRoleCooldown("fire_element");
	}

	public void setRoleCooldownFireElement(int roleCooldownFireElement) {
		this.setRoleCooldown("fire_element", roleCooldownFireElement);
	}

	public int getRoleCooldownKurama() {
		return this.getRoleCooldown("kurama");
	}

	public void setRoleCooldownKurama(int roleCooldownKurama) {
		this.setRoleCooldown("kurama", roleCooldownKurama);
	}

	public int getRoleCooldownKyodaigumo() {
		return this.getRoleCooldown("kyodaigumo");
	}

	public void setRoleCooldownKyodaigumo(int roleCooldownKyodaigumo) {
		this.setRoleCooldown("kyodaigumo", roleCooldownKyodaigumo);
	}

	public int getRoleCooldownHuitieme() {
		return this.getRoleCooldown("huitieme_porte");
	}

	public void setRoleCooldownTendo(int roleCooldownTendo) {
		setRoleCooldown("tendo", roleCooldownTendo);
	}

	public void setRoleCooldownHuitieme(int roleCooldownHuitieme) {
		this.setRoleCooldown("huitieme_porte", roleCooldownHuitieme);
	}

	public int getRoleCooldownShurikenJustu() {
		return this.getRoleCooldown("shuriken_jutsu");
	}

	public void setRoleCooldownShurikenJustu(int roleCooldownShurikenJustu) {
		this.setRoleCooldown("shuriken_jutsu", roleCooldownShurikenJustu);
	}

	public int getRoleCooldownCamouflage() {
		return this.getRoleCooldown("camouflage");
	}

	public void setRoleCooldownCamouflage(int roleCooldownCamouflage) {
		this.setRoleCooldown("camouflage", roleCooldownCamouflage);
	}

	public int getRoleCooldownHyoton() {
		return this.getRoleCooldown("hyoton");
	}

	public void setRoleCooldownHyoton(int roleCooldownHyoton) {
		this.setRoleCooldown("hyoton", roleCooldownHyoton);
	}

	public void setRoleCooldownSenjutsu(int roleCooldownSenjutsu) {
		this.setRoleCooldown("senjutsu", roleCooldownSenjutsu);
	}

	public int getRoleCooldownCopy() {
		return this.getRoleCooldown("copy");
	}

	public void setRoleCooldownCopy(int roleCooldownCopy) {
		this.setRoleCooldown("copy", roleCooldownCopy);
	}

	public int getRoleCooldownKiba() {
		return this.getRoleCooldown("kiba");
	}

	public void setRoleCooldownKiba(int roleCooldownKiba) {
		this.setRoleCooldown("kiba", roleCooldownKiba);
	}

	public int getRoleCooldownHachibi() {
		return this.getRoleCooldown("kiba");
	}

	public void setRoleCooldownHachibi(int roleCooldownHachibi) {
		this.setRoleCooldown("hachibi", roleCooldownHachibi);
	}

	public int getRoleCooldownAkamaru() {
		return this.getRoleCooldown("akamaru");
	}

	public void setRoleCooldownAkamaru(int roleCooldownAkamaru) {
		this.setRoleCooldown("akamaru", roleCooldownAkamaru);
	}

	public int getRoleCooldownBouletHumain() {
		return this.getRoleCooldown("boulet_humain");
	}

	public void setRoleCooldownBouletHumain(int roleCooldownBouletHumain) {
		this.setRoleCooldown("boulet_humain", roleCooldownBouletHumain);
	}

	public int getRoleCooldownByakugan() {
		return this.getRoleCooldown("byakugan");
	}

	public void setRoleCooldownByakugan(int roleCooldownByakugan) {
		this.setRoleCooldown("byakugan", roleCooldownByakugan);
	}

	public int getRoleCooldownDecuplementPartiel() {
		return this.getRoleCooldown("decuplement_partiel");
	}

	public void setRoleCooldownDecuplementPartiel(int roleCooldownDecuplementPartiel) {
		this.setRoleCooldown("decuplement_partiel", roleCooldownDecuplementPartiel);
	}

	public int getRoleCooldownGyuki() {
		return this.getRoleCooldown("gyuki");
	}

	public void setRoleCooldownGyuki(int roleCooldownGyuki) {
		this.setRoleCooldown("gyuki", roleCooldownGyuki);
	}

	public int getRoleCooldownHakke() {
		return this.getRoleCooldown("hakke");
	}

	public void setRoleCooldownHakke(int roleCooldownHakke) {
		this.setRoleCooldown("hakke", roleCooldownHakke);
	}

	public int getRoleCooldownParchemin() {
		return this.getRoleCooldown("parchemin");
	}

	public void setRoleCooldownParchemin(int roleCooldownParchemin) {
		this.setRoleCooldown("parchemin", roleCooldownParchemin);
	}

	public int getRoleCooldownEventail() {
		return this.getRoleCooldown("eventail");
	}

	public void setRoleCooldownEventail(int roleCooldownEventail) {
		this.setRoleCooldown("eventail", roleCooldownEventail);
	}

	public int getRoleCooldownByakugo() {
		return this.getRoleCooldown("byakugo");
	}

	public void setRoleCooldownByakugo(int roleCooldownByakugo) {
		this.setRoleCooldown("byakugo", roleCooldownByakugo);
	}

	public int getRoleCooldownNinjutsu() {
		return this.getRoleCooldown("ninjutsu");
	}

	public void setRoleCooldownNinjutsu(int roleCooldownNinjutsu) {
		this.setRoleCooldown("ninjutsu", roleCooldownNinjutsu);
	}

	public int getRoleCooldownSusano() {
		return this.getRoleCooldown("susano");
	}

	public void setRoleCooldownSusano(int roleCooldown) {
		this.setRoleCooldown("susano", roleCooldown);
	}

	public int getRoleCooldownSenjutsu() {
		return this.getRoleCooldown("senjutsu");
	}

	public int getRoleCooldownSharingan() {
		return this.getRoleCooldown("sharingan");
	}

	public void setRoleCooldownSharingan(int roleCooldown) {
		this.setRoleCooldown("sharingan", roleCooldown);
	}

	public int getRoleCooldownRasengan() {
		return this.getRoleCooldown("rasengan");
	}

	public void setRoleCooldownRasengan(int roleCooldownRasengan) {
		this.setRoleCooldown("rasengan", roleCooldownRasengan);
	}

	public int getRoleCooldownKatsuyu() {
		return this.getRoleCooldown("katsuyu");
	}

	public void setRoleCooldownKatsuyu(int roleCooldownKatsuyu) {
		this.setRoleCooldown("katsuyu", roleCooldownKatsuyu);
	}

	public int getRoleCooldownShosenjutsu() {
		return this.getRoleCooldown("shosenjutsu");
	}

	public void setRoleCooldownShosenjutsu(int roleCooldownShosenjutsu) {
		this.setRoleCooldown("shosenjutsu", roleCooldownShosenjutsu);
	}

	public int getRoleCooldownSaiMonture() {
		return this.getRoleCooldown("sai_monture");
	}

	public void setRoleCooldownSaiMonture(int roleCooldownSaiMonture) {
		this.setRoleCooldown("sai_monture", roleCooldownSaiMonture);
	}

	public int getRoleCooldownSaiTigres() {
		return this.getRoleCooldown("sai_tigres");
	}

	public void setRoleCooldownSaiTigres(int roleCooldownSaiTigres) {
		this.setRoleCooldown("sai_tigres", roleCooldownSaiTigres);
	}

	public int getRoleCooldownShurikenJustuTP() {
		return this.getRoleCooldown("shuriken_justu_tp");
	}

	public void setRoleCooldownShurikenJustuTP(int roleCooldownShurikenJustuTP) {
		this.setRoleCooldown("shuriken_justu_tp", roleCooldownShurikenJustuTP);
	}

	public int getRoleCooldownPakkun() {
		return this.getRoleCooldown("pakkun");
	}

	public void setRoleCooldownPakkun(int roleCooldownPakkun) {
		this.setRoleCooldown("pakkun", roleCooldownPakkun);
	}

	public int getRoleCooldownNueesArdentes() {
		return this.getRoleCooldown("nuees_ardentes");
	}

	public void setRoleCooldownNueesArdentes(int roleCooldownNueesArdentes) {
		this.setRoleCooldown("nuees_ardentes", roleCooldownNueesArdentes);
	}

	public int getRoleCooldownSonohoka() {
		return this.getRoleCooldown("sonohoka");
	}

	public void setRoleCooldownSonohoka(int roleCooldownSonohoka) {
		this.setRoleCooldown("sonohoka", roleCooldownSonohoka);
	}

	public int getRoleCooldownSenpo() {
		return this.getRoleCooldown("senpo");
	}

	public void setRoleCooldownSenpo(int roleCooldownSonohoka) {
		this.setRoleCooldown("senpo", roleCooldownSonohoka);
	}

	public int getRoleCooldownArisuma() {
		return this.getRoleCooldown("arisuma");
	}

	public void setRoleCooldownArisuma(int roleCooldownArisuma) {
		this.setRoleCooldown("arisuma", roleCooldownArisuma);
	}

	public int getRoleCooldownC3() {
		return this.getRoleCooldown("deidara_c3");
	}

	public void setRoleCooldownC3(int roleCooldownC3) {
		this.setRoleCooldown("deidara_c3", roleCooldownC3);
	}

	public int getRoleCooldownC1() {
		return this.getRoleCooldown("deidara_c1");
	}

	public void setRoleCooldownC1(int roleCooldownC1) {
		this.setRoleCooldown("deidara_c1", roleCooldownC1);
	}

	public int getRoleCooldownC2() {
		return this.getRoleCooldown("deidara_c2");
	}

	public void setRoleCooldownC2(int roleCooldownC2) {
		this.setRoleCooldown("deidara_c2", roleCooldownC2);
	}

	public int getRoleCooldownDeidara() {
		return this.getRoleCooldown("deidara");
	}

	public void setRoleCooldownDeidara(int roleCooldownDeidara) {
		this.setRoleCooldown("deidara", roleCooldownDeidara);
	}

	public int getRoleCooldownRituel() {
		return this.getRoleCooldown("rituel");
	}

	public void setRoleCooldownRituel(int i) {
		this.setRoleCooldown("rituel", i);
	}

	public int getRoleCooldownBarriereProtectrice() {
		return this.getRoleCooldown("barriere_protectrice");
	}

	public void setRoleCooldownBarriereProtectrice(int i) {
		this.setRoleCooldown("barriere_protectrice", i);
	}

	public void setRoleCooldownAttaque(int i) {
		this.setRoleCooldown("attaque", i);
	}

	public int getRoleCooldownAttaque() {
		return this.getRoleCooldown("attaque");
	}

	public int getRoleCooldownYari() {
		return this.getRoleCooldown("yari");
	}

	public void setRoleCooldownYari(int i) {
		this.setRoleCooldown("yari", i);
	}

	public int getRoleCooldownBakuhatsu() {
		return this.getRoleCooldown("bakuhatsu");
	}

	public void setRoleCooldownBakuhatsu(int i) {
		this.setRoleCooldown("bakuhatsu", i);
	}

	public int getRoleCooldownMetamorphose() {
		return this.getRoleCooldown("metamorphose");
	}

	public void setRoleCooldownMetamorphose(int i) {
		this.setRoleCooldown("metamorphose", i);
	}

	public int getRoleCooldownKamiToku() {
		return this.getRoleCooldown("kami_toku");
	}

	public void setRoleCooldownKamiToku(int i) {
		this.setRoleCooldown("kami_toku", i);
	}

	public int getRoleCooldownBatafurai() {
		return this.getRoleCooldown("batafurai");
	}

	public void setRoleCooldownBatafurai(int i) {
		this.setRoleCooldown("batafurai", i);
	}

	public int getRoleCooldownOiseau() {
		return this.getRoleCooldown("oiseau");
	}

	public void setRoleCooldownOiseau(int i) {
		this.setRoleCooldown("oiseau", i);
	}

	public int getRoleCooldownChissoku() {
		return this.getRoleCooldown("chissoku");
	}

	public void setRoleCooldownChissoku(int i) {
		this.setRoleCooldown("chissoku", i);
	}

	public int getRoleCooldownBanshoTenin() {
		return this.getRoleCooldown("bansho_tenin");
	}

	public void setRoleCooldownBanshoTenin(int i) {
		this.setRoleCooldown("bansho_tenin", i);
	}

	public int getRoleCooldownShukaku() {
		return this.getRoleCooldown("shukaku");
	}

	public void setRoleCooldownShukaku(int i) {
		this.setRoleCooldown("shukaku", i);
	}

	public int getRoleCooldownShinraTensei() {
		return this.getRoleCooldown("shinra_tensei");
	}

	public void setRoleCooldownShinraTensei(int i) {
		this.setRoleCooldown("shinra_tensei", i);
	}

	public int getRoleCooldownFix() {
		return this.getRoleCooldown("fix");
	}

	public void setRoleCooldownFix(int i) {
		this.setRoleCooldown("fix", i);
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
