package fr.lastril.uhchost.modes.naruto.v2.biju;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.items.biju.*;
import fr.lastril.uhchost.modes.naruto.v2.roles.BijuUser;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Madara;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Obito;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.display.PacketDisplay;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class BijuManager extends BukkitRunnable implements Listener {

	private final UhcHost main;

	private boolean craftedJubi = false, recupKurama, recupGyuki, recupShukaku;

	private final Map<Class<? extends Biju>, PlayerManager> hotesBiju;

	private final Map<PlayerManager, Integer> recupBiju;

	private final Map<UUID, PacketDisplay> recupDisplay;

	private final List<ItemStack> bijuItem;

	private final List<PlayerManager> noFall;

	private final List<Biju> bijuListClass;

	private final List<PlayerManager> isobuResistance;

	private final List<PlayerManager> sanGokuNoLava;

	private final List<PlayerManager> saikenHote;

	private final List<PlayerManager> kokuoHote;

	private final NarutoV2Manager narutoV2Manager;

	public BijuManager(UhcHost main, NarutoV2Manager narutoV2Manager) {
		this.hotesBiju = new HashMap<>();
		this.main = main;
		this.noFall = new ArrayList<>();
		this.isobuResistance = new ArrayList<>();
		this.sanGokuNoLava = new ArrayList<>();
		this.saikenHote = new ArrayList<>();
		this.bijuItem = new ArrayList<>();
		this.kokuoHote = new ArrayList<>();
		this.bijuListClass = new ArrayList<>();
		this.recupBiju = new HashMap<>();
		this.recupDisplay = new HashMap<>();
		for(BijuList bijuEnum : BijuList.values()){
			hotesBiju.put(bijuEnum.getBiju(), null);
		}
		this.narutoV2Manager = narutoV2Manager;
		main.getServer().getPluginManager().registerEvents(this, main);
		main.getServer().getScheduler().runTaskLater(main, this, 0);
		registerJubiCraft(main.getServer());


	}

	private void registerJubiCraft(Server server){
		ShapedRecipe jubi = new ShapedRecipe(new JubiItem(main).toItemStack());
		jubi.shape(".*;", "!%#", ":::");
		jubi.setIngredient('.', new MatatabiItem(main).toItemStack().getData());
		jubi.setIngredient('*', new IsobuItem(main).toItemStack().getData());
		jubi.setIngredient(';', new SonGokuItem(main).toItemStack().getData());
		jubi.setIngredient('!', new KokuoItem(main).toItemStack().getData());
		jubi.setIngredient('%', new SaikenItem(main).toItemStack().getData());
		jubi.setIngredient('#', new ChomeiItem(main).toItemStack().getData());
		server.addRecipe(jubi);
		UhcHost.debug("Registered Jubi craft Item");
	}

	@EventHandler
	public void Craft(CraftItemEvent event) {
		ItemStack result = event.getRecipe().getResult();
		Player player = (Player) event.getWhoClicked();
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if(result != null){
			if(result.getType() == Material.NETHER_STAR){
				UhcHost.debug("Check Jubi Craft");
				if(result.isSimilar(new JubiItem(main).toItemStack())){
					if(joueur.getRole() instanceof JubiItem.JubiUser){
						UhcHost.debug("Checked Item crafted");
						Bukkit.broadcastMessage(" ");
						Bukkit.broadcastMessage("§d§lJûbi vient d'être invoqué ! Faites attention à son nouvel hôte !");
						Bukkit.broadcastMessage(" ");
						setCraftedJubi(true);
						setJubiTablist();
						for(Player players : Bukkit.getOnlinePlayers()) {
							players.playSound(players.getLocation(), Sound.ENDERDRAGON_DEATH, Integer.MAX_VALUE, 1);
						}
						//if(isRecupGyuki() && isRecupKurama() && isRecupShukaku()){

						 /* } else {
							player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous n'avez récupérer l'ADN de tous les Bijû.");
							event.setCancelled(true);
						}*/
					} else {
						player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous ne possèdez pas assez de puissance pour invoquer Jûbi.");
						event.setCancelled(true);
					}

				}
			}
		}
	}

	private void setJubiTablist(){
		for(PlayerManager joueur : narutoV2Manager.getPlayerManagersWithRole(Obito.class)){
			if(joueur.getPlayer() != null){
				joueur.getPlayer().setPlayerListName("§4"+joueur.getPlayerName());
			}
		}
		for(PlayerManager joueur : narutoV2Manager.getPlayerManagersWithRole(Madara.class)){
			if(joueur.getPlayer() != null){
				joueur.getPlayer().setPlayerListName("§4"+joueur.getPlayerName());
			}
		}
	}


	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			if (event.getCause() == DamageCause.FALL) {
				Player player = (Player) event.getEntity();
				PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
				if (noFall.contains(joueur))
					event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onDamageHote(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
			if(isobuResistance.contains(joueur)){
				int value = UhcHost.getRANDOM().nextInt(100);
				UhcHost.debug("Isobu hote value: " + value);
				if(value <= 5){
					event.setCancelled(true);
				}
			}
			if(event.getDamager() instanceof Player){
				Player damager = (Player) event.getDamager();
				PlayerManager damagerJoueur = main.getPlayerManager(damager.getUniqueId());
				if(saikenHote.contains(damagerJoueur)){
					int value = UhcHost.getRANDOM().nextInt(100);
					UhcHost.debug("Saiken hote value: " + value);
					if(value <= 10){
						if(damager.getHealth() + 1 >= damager.getMaxHealth()){
							damager.setHealth(damager.getMaxHealth());
						} else{
							damager.setHealth(damager.getHealth() + 1);
						}
						player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2*20, 0));
						player.getWorld().createExplosion(player.getLocation(), 1.0f);
					}
				}
				if(kokuoHote.contains(damagerJoueur)){
					int value = UhcHost.getRANDOM().nextInt(100);
					UhcHost.debug("Kokuo hote value: " + value);
					if(value <= 15){
						player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 3, false, false));
						player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*5, 0, false, false));
					}
				}
				if(sanGokuNoLava.contains(damagerJoueur)){
					int value = UhcHost.getRANDOM().nextInt(100);
					UhcHost.debug("SanGoku hote value: " + value);
					if(value <= 50){
						player.setFireTicks(20*8);
					}
				}

			}
		}
	}

	@EventHandler
	public void onPlayerBucket(PlayerBucketEmptyEvent event) {
		Block liquid = event.getBlockClicked().getRelative(event.getBlockFace());
		Player player = event.getPlayer();
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if(!sanGokuNoLava.isEmpty()){
			if(event.getBucket() == Material.LAVA_BUCKET && liquid.getLocation().distance(sanGokuNoLava.get(0).getPlayer().getLocation()) <= 15 && !sanGokuNoLava.contains(joueur)) {
				player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cL'hôte de San Gokû vous empêche de poser de la lave à côté de lui.");
				event.setCancelled(true);
			}
		}
	}

	@Override
	public void run() {
		for(Class<? extends Biju> biju : hotesBiju.keySet()){
			Biju bijuInstance = null;
			try {
				bijuInstance = biju.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			if(bijuInstance != null){
				int value = UhcHost.getRANDOM().nextInt(40, 60);
				UhcHost.debug("Spawn moment: " + value + " min");
				bijuListClass.add(bijuInstance);
				bijuItem.add(bijuInstance.getItem().toItemStack());
				bijuInstance.setFirstSpawn(value);
				bijuInstance.runTaskTimer(main, value*20*60, 20);
			}
		}
	}

	public boolean isAlreadyHote(PlayerManager joueur){
		for(PlayerManager joueurs : hotesBiju.values()){
			if(joueurs == joueur || isRoleHote(joueur)){
				return true;
			}
		}
		return false;
	}

	private boolean isRoleHote(PlayerManager joueur){
		if(joueur.getRole() instanceof BijuUser){
			return true;
		}
		return false;
	}

	public boolean isCraftedJubi(){
		return craftedJubi;
	}

	public void setCraftedJubi(boolean craftedJubi){
		this.craftedJubi = craftedJubi;
	}

	public List<PlayerManager> getNoFall() {
		return noFall;
	}

	public Map<Class<? extends Biju>, PlayerManager> getHotesBiju() {
		return hotesBiju;
	}

	public List<PlayerManager> getIsobuResistance() {
		return isobuResistance;
	}

	public List<PlayerManager> getSanGokuNoLava() {
		return sanGokuNoLava;
	}

	public List<PlayerManager> getSaikenHote() {
		return saikenHote;
	}

	public List<ItemStack> getBijuItem(){
		return bijuItem;
	}

	public List<PlayerManager> getKokuoHote() {
		return kokuoHote;
	}

	public Map<PlayerManager, Integer> getRecupBiju() {
		return recupBiju;
	}

	public Map<UUID, PacketDisplay> getRecupDisplay() {
		return recupDisplay;
	}

	public List<Biju> getBijuListClass() {
		return bijuListClass;
	}

	public boolean isRecupKurama() {
		return recupKurama;
	}

	public void setRecupKurama(boolean recupKurama) {
		this.recupKurama = recupKurama;
	}

	public boolean isRecupGyuki() {
		return recupGyuki;
	}

	public void setRecupGyuki(boolean recupGyuki) {
		this.recupGyuki = recupGyuki;
	}

	public boolean isRecupShukaku() {
		return recupShukaku;
	}

	public void setRecupShukaku(boolean recupShukaku) {
		this.recupShukaku = recupShukaku;
	}
}
