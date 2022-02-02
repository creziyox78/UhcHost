package fr.lastril.uhchost.modes.naruto.v2;


import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.config.modes.NarutoV2Config;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.naruto.v2.biju.BijuManager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.generation.MeteoresGenerator;
import fr.lastril.uhchost.modes.naruto.v2.items.RasenganItem;
import fr.lastril.uhchost.modes.naruto.v2.items.bow.SusanoBow;
import fr.lastril.uhchost.modes.naruto.v2.items.swords.SusanoSword;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.ZetsuBlanc;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.ZetsuNoir;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Madara;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Obito;
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.*;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.*;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Danzo;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Gaara;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Jugo;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Sasuke;
import fr.lastril.uhchost.modes.naruto.v2.roles.zabuza.Haku;
import fr.lastril.uhchost.modes.naruto.v2.roles.zabuza.Zabuza;
import fr.lastril.uhchost.modes.naruto.v2.sakonukon.SakonUkonManager;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.world.WorldUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class NarutoV2Manager extends ModeManager implements Listener {

    private static final int SUSANO_ATTACK_DELAY = 20;

    private final UhcHost main;

    private BijuManager bijuManager;
    private final NarutoV2Config narutoV2Config;

    /** KAMUI */
    private final World kamuiWorld;
    private final Location kamuiLocation;

    private final List<UUID> samehada, inShosenjutsu, inSusano;

    private final Map<UUID, Long> lastSusanoAttack;

    private PlayerManager hokage, boosted, jubi;

    private final List<PlayerManager> nofallJoueur;

    private int duration;

    private boolean reveal, jubiUsed = false, meteoresExplode, canUseJubi = true, danzoKilledHokage;

    private final SakonUkonManager sakonUkonManager;

    private Class<?>[] canBeHokage = new Class<?>[] {Naruto.class, Sai.class, Minato.class,
    	Kakashi.class, Jiraya.class, Tsunade.class, Hinata.class, Neji.class, GaiMaito.class,
    	Shikamaru.class, Sasuke.class};

    private Class<?>[] canNotSeeHokage = new Class<?>[] {Temari.class, YondameRaikage.class, KillerBee.class,
    	ZetsuNoir.class, ZetsuBlanc.class, Sakon.class, Ukon.class, Kidomaru.class, Tayuya.class,
    	Jirobo.class, Jugo.class, Gaara.class};

    public NarutoV2Manager(UhcHost main) {
        this.main = main;
        this.narutoV2Config = new NarutoV2Config(50, true);
        this.kamuiWorld = main.getServer().createWorld(WorldCreator.name("kamui"));
        Bukkit.getWorld("kamui").setGameRuleValue("doMobSpawning", "false");
        Bukkit.getWorld("kamui").setGameRuleValue("showDeathMessages", "false");
        this.kamuiLocation = new Location(this.kamuiWorld, 25108, 13, 25015);
        this.samehada = new ArrayList<>();
        this.inShosenjutsu = new ArrayList<>();
        this.nofallJoueur = new ArrayList<>();
        this.inSusano = new ArrayList<>();
        this.sakonUkonManager = new SakonUkonManager(this);
        this.lastSusanoAttack = new HashMap<>();
        main.getServer().getPluginManager().registerEvents(this, main);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(GameState.isState(GameState.STARTED)){
                    for(PlayerManager joueur : main.getPlayerManagerAlives()){
                        if (joueur.hasRole() && joueur.isAlive()) {
                            if(joueur.getPlayer() != null){
                                Player player = joueur.getPlayer();
                                if (joueur.getRole() instanceof NarutoV2Role) {
                                    NarutoV2Role narutoV2Role = (NarutoV2Role) joueur.getRole();
                                    if (narutoV2Role.getChakra() != null && narutoV2Role.getChakra() == Chakra.DOTON) {
                                        if (player.getLocation().getY() <= 50) {
                                            if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING))
                                                player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                                            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 5, 0, false, false));
                                            player.setWalkSpeed((float) 0.202);
                                        } else {
                                            player.setWalkSpeed((float) 0.2);
                                        }
                                    } else if (narutoV2Role.getChakra() != null && narutoV2Role.getChakra() == Chakra.SUITON) {
                                        if (player.getLocation().getBlock().isLiquid()) {
                                            if (player.getInventory().getBoots() != null && player.getInventory().getBoots().getType() != Material.AIR) {
                                                if (player.getInventory().getBoots().getEnchantmentLevel(Enchantment.DEPTH_STRIDER) <= 1){
                                                    player.getInventory().getBoots().addEnchantment(Enchantment.DEPTH_STRIDER, 1);
                                                }
                                            }

                                        } else {
                                            if (player.getInventory().getBoots() != null && player.getInventory().getBoots().getType() == Material.AIR) {
                                                if (player.getInventory().getBoots().containsEnchantment(Enchantment.DEPTH_STRIDER) && player.getInventory().getBoots().getEnchantmentLevel(Enchantment.DEPTH_STRIDER) == 1)
                                                    player.getInventory().getBoots().removeEnchantment(Enchantment.DEPTH_STRIDER);
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }


            }
        }.runTaskTimer(main, 0, 5);
    }

    public boolean isInSamehada(UUID uniqueId) {
        return this.samehada.contains(uniqueId);
    }

    public void addInSamehada(UUID uniqueId) {
        this.samehada.add(uniqueId);
    }

    public void removeInSamehada(UUID uniqueId) {
        this.samehada.remove(uniqueId);
    }

    public void addInShosenJutsu(UUID uuid) {
        this.inShosenjutsu.add(uuid);
        UhcHost.debug("added " + uuid + " in shosenjutsu, now to : " + this.inShosenjutsu.toString());
    }

    public void removeInShosenJutsu(UUID uuid) {
        this.inShosenjutsu.remove(uuid);
        UhcHost.debug("removed " + uuid + " in shosenjutsu, now to : " + this.inShosenjutsu.toString());
    }

    public boolean isInShosenJutsu(UUID uuid){
        return this.inShosenjutsu.contains(uuid);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(GameState.isState(GameState.STARTED)){
            if(main.getGamemanager().getModes() == Modes.NARUTO){
                event.setCancelled(true);
                if(playerManager.hasRole() && playerManager.isAlive()){
                    if(event.getMessage().startsWith("!")) {
                        if(playerManager.getRole() instanceof Madara || playerManager.getRole() instanceof Obito || playerManager.getRole() instanceof Haku || playerManager.getRole() instanceof Zabuza){
                            if(playerManager.getRole() instanceof Madara){
                                for(PlayerManager mate : getPlayerManagersWithRole(Obito.class)){
                                    if(mate.getPlayer() != null){
                                        mate.getPlayer().sendMessage(playerManager.getRole().getCamp().getCompoColor() + playerManager.getRole().getRoleName() + " » " + event.getMessage().replaceFirst("!", ""));
                                    }
                                }
                            }
                            if(playerManager.getRole() instanceof Obito){
                                for(PlayerManager mate : getPlayerManagersWithRole(Madara.class)){
                                    if(mate.getPlayer() != null){
                                        mate.getPlayer().sendMessage(playerManager.getRole().getCamp().getCompoColor() + playerManager.getRole().getRoleName()+ " » " + event.getMessage().replaceFirst("!", ""));
                                    }

                                }
                            }
                            if(playerManager.getRole() instanceof Zabuza){
                                for(PlayerManager mate : getPlayerManagersWithRole(Haku.class)){
                                    if(mate.getPlayer() != null){
                                        mate.getPlayer().sendMessage(playerManager.getRole().getCamp().getCompoColor() + playerManager.getRole().getRoleName() + " » " + event.getMessage().replaceFirst("!", ""));
                                    }
                                }
                            }
                            if(playerManager.getRole() instanceof Haku){
                                for(PlayerManager mate : getPlayerManagersWithRole(Zabuza.class)){

                                    if(mate.getPlayer() != null){
                                        mate.getPlayer().sendMessage(playerManager.getRole().getCamp().getCompoColor() + playerManager.getRole().getRoleName() + " » " + event.getMessage().replaceFirst("!", ""));
                                    }
                                }
                            }
                            player.sendMessage(playerManager.getRole().getCamp().getCompoColor() + playerManager.getRole().getRoleName() + " » " + event.getMessage().replaceFirst("!", ""));
                        }

                    }
                }
            }
        }
    }



    public World getKamuiWorld() {
        return kamuiWorld;
    }

    public void sendToKamui(Player player, Location originalLocation, Player sender){
        if(player.getWorld() != this.kamuiWorld){
            player.teleport(this.kamuiLocation);
            player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous vous êtes téléporté dans le monde de Kamui.");

            new BukkitRunnable(){

                int timer = 60*2;

                @Override
                public void run() {
                    if(player.getWorld() != NarutoV2Manager.this.kamuiWorld){
                        cancel();
                    }
                    Player players = Bukkit.getPlayer(player.getUniqueId());
                    PlayerManager senderJoueur = main.getPlayerManager(sender.getUniqueId());
                    if(!senderJoueur.isAlive()){
                        if(players.getWorld() == NarutoV2Manager.this.getKamuiWorld()){
                            Location loc = new Location(originalLocation.getWorld(), originalLocation.getX(), originalLocation.getWorld().getHighestBlockYAt(originalLocation), originalLocation.getZ());
                            originalLocation.getChunk().load();
                            players.teleport(loc);
                            players.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous avez été retéléporté dans le monde normal.");
                            cancel();
                        }
                    }
                    if(players != null){
                       ActionBar.sendMessage(players, "§9Kamui §7: §3"+new FormatTime(timer));
                    }

                    if(timer == 0){
                        if(players != null){
                            if(players.getWorld() == NarutoV2Manager.this.getKamuiWorld()){
                                Location loc = new Location(originalLocation.getWorld(), originalLocation.getX(), originalLocation.getWorld().getHighestBlockYAt(originalLocation), originalLocation.getZ());
                                originalLocation.getChunk().load();
                                players.teleport(loc);
                                players.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous avez été retéléporté dans le monde normal.");
                            }
                        }
                        cancel();
                    }

                    timer--;
                }
            }.runTaskTimer(main, 0, 20);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockUpdate(EntityChangeBlockEvent event) {
        Block block = event.getBlock();
        if(event.getEntity() instanceof FallingBlock){
            FallingBlock fallingBlock = (FallingBlock) event.getEntity();
            if(fallingBlock.getCustomName() != null ){
                if(fallingBlock.getCustomName().equals(MeteoresGenerator.METEORE_KEY)){
                    block.setType(Material.AIR);
                    if(!meteoresExplode){
                        if(fallingBlock.getWorld() != this.getKamuiWorld()) WorldUtils.createBeautyExplosion(fallingBlock.getLocation(), 30);
                        for(Player players : Bukkit.getOnlinePlayers()){
                            PlayerManager targetJoueur = main.getPlayerManager(players.getUniqueId());
                            if(players.getWorld() == fallingBlock.getWorld()){
                                if(players.getGameMode() != GameMode.SPECTATOR && targetJoueur.isAlive()){
                                    if(!(targetJoueur.getRole() instanceof Madara)){
                                        if(players.getLocation().distance(fallingBlock.getLocation()) <= 29){
                                            if(targetJoueur.getRole() instanceof Danzo){
                                                Danzo danzo = (Danzo) targetJoueur.getRole();
                                                danzo.useVie(targetJoueur, 100);
                                            } else {
                                                players.damage(100D);
                                            }
                                        }

                                    }
                                }
                            }
                        }
                        meteoresExplode = true;
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getWorld() == this.kamuiWorld) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Messages.error("Vous ne pouvez pas casser de blocs dans le "+this.getPlaceInKamuiWorld(block.getLocation())));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getWorld() == this.kamuiWorld) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Messages.error("Vous ne pouvez pas poser de blocs dans le "+this.getPlaceInKamuiWorld(block.getLocation())));
        }
    }

    public String getPlaceInKamuiWorld(Location location){
        return location.getX() < 25030 ? "jump de Gaara" : "monde de Kamui";
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerFall(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (player.getWorld() == this.kamuiWorld || (joueur == jubi && isJubiUsed())) {
                event.setCancelled(event.getCause() == EntityDamageEvent.DamageCause.FALL);
            }
            if(getNofallJoueur().contains(joueur) && event.getCause() == EntityDamageEvent.DamageCause.FALL){
                event.setCancelled(true);
            }
            if (joueur.hasRole() && joueur.isAlive()) {
                if (joueur.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoV2Role = (NarutoV2Role) joueur.getRole();
                    if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                        if (narutoV2Role.getChakra() != null && narutoV2Role.getChakra() == Chakra.FUTON) {
                            event.setDamage(event.getDamage() / 1.2);
                        }
                    }

                }
            }
            if(hokage != null) {
            	if(hokage == joueur || boosted == joueur) {
                	event.setDamage(event.getDamage() / 1.1);
                }
            }

        }
    }

    @EventHandler
    public void onHokageDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(joueur == hokage){
            setHokage(null);
            Bukkit.getScheduler().runTaskLater(main, () -> {
                Bukkit.broadcastMessage("§e§lL'Hokage étant mort, un autre joueur a été choisis pour être Hokage.");
                if(!danzoKilledHokage){
                    annonceHokage();
                }
            }, 20*60*5);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBowSusano(EntityShootBowEvent event) {
    	if(event.getEntity() instanceof Player) {
    		Player player = (Player) event.getEntity();
    		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
    		if(joueur.hasRole()) {
    			if(joueur.getRole() instanceof Sasuke) {
    				ItemStack item = event.getBow();
    				ItemStack susano = new SusanoBow().toItemStack();
    				susano.setDurability(item.getDurability());
    				if(item.isSimilar(susano)) {
    					if(this.inSusano.contains(player.getUniqueId())) {
    						long delayLastAttack = System.currentTimeMillis() - this.lastSusanoAttack.getOrDefault(player.getUniqueId(), 0L);
    						if (delayLastAttack <= SUSANO_ATTACK_DELAY * 1000) {
    	                        event.setCancelled(true);
    	                        player.getWorld().playSound(player.getLocation(), Sound.ANVIL_BREAK, 1f, 1f);
    	                        player.sendMessage(Messages.cooldown((int) (SUSANO_ATTACK_DELAY-(delayLastAttack / 1000))));
    	                    } else {
    	                    	this.lastSusanoAttack.put(player.getUniqueId(), System.currentTimeMillis());
    	                    }
    					} else {
    	                    player.setItemInHand(null);
    	                    player.sendMessage(Messages.error("Vous ne pouvez plus utiliser votre susano"));
    					}
    				}
    			}
    		}
    	}

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
        	Player player = (Player) event.getEntity();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            Player damager = (Player) event.getDamager();
            PlayerManager damagerJoueur = main.getPlayerManager(damager.getUniqueId());
            if (damager.getItemInHand() != null) {
                ItemStack item = damager.getItemInHand();
                ItemStack susano = new SusanoSword().toItemStack();
                susano.setDurability(item.getDurability());
                if (item.isSimilar(susano)) {
                    if (this.inSusano.contains(damager.getUniqueId())) {
                        long delayLastAttack = System.currentTimeMillis() - this.lastSusanoAttack.getOrDefault(damager.getUniqueId(), 0L);
                        if (delayLastAttack <= SUSANO_ATTACK_DELAY * 1000) {
                            event.setCancelled(true);
                            damager.getWorld().playSound(damager.getLocation(), Sound.ANVIL_BREAK, 1f, 1f);
                            damager.sendMessage(Messages.cooldown((int) (SUSANO_ATTACK_DELAY-(delayLastAttack / 1000))));
                        } else {
                            this.lastSusanoAttack.put(damager.getUniqueId(), System.currentTimeMillis());
                        }
                    }else{
                        damager.setItemInHand(null);
                        damager.sendMessage(Messages.error("Vous ne pouvez plus utiliser votre susano"));
                    }
                }
            }
            if(jubi == damagerJoueur && jubiUsed) {
            	int value = UhcHost.getRANDOM().nextInt(100);
            	if(value <= 25) {
            		/*duration = 0;
            		for (PotionEffect effect : damager.getActivePotionEffects()) {
            			if (damager.hasPotionEffect(PotionEffectType.SPEED)) {
            				duration = effect.getDuration();
            				damager.removePotionEffect(PotionEffectType.SPEED);
            			}
            		}*/
            		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 3, false, false));
            		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*5, 0, false, false));
            		if(player.getWorld() != getKamuiWorld())
            		    player.getWorld().createExplosion(player.getLocation(), 2.0f);

            		/*damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*5, 2, false, false));
            		new BukkitRunnable() {

						@Override
						public void run() {
							damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration - 20*5, 1, false, false));
						}
					}.runTaskLater(main, 20*5 + 1);*/
            	}
            	if(value <= 20) {
            		player.setFireTicks(20* 8);
            	}
            }
            if(hokage != null) {
            	if(hokage == joueur || boosted == joueur) {
                	event.setDamage(event.getDamage() / 1.1);
                }
                if(hokage == damagerJoueur|| boosted == damagerJoueur) {
                	event.setDamage(event.getDamage() + ((10/100)*event.getDamage()));
                }
            }


        }
    }

    public void killSafely(OfflinePlayer player){
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        joueur.setAlive(false);
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.playSound(players.getLocation(), Sound.WITHER_DEATH, 1f, 1f);
        }
        if (joueur.hasRole()) {
            Bukkit.broadcastMessage("§3§m----------------------------------");
            Bukkit.broadcastMessage("§b§l" + player.getName() + "§7 est mort, son rôle était "+joueur.getRole().getCamp().getCompoColor()+joueur.getRole().getRoleName()+"§7.");
            Bukkit.broadcastMessage("§3§m----------------------------------");
        } else {
            Bukkit.broadcastMessage("§3§m----------------------------------");
            Bukkit.broadcastMessage("§b§l" + player.getName() + "§7 est mort.");
            Bukkit.broadcastMessage("§3§m----------------------------------");
        }
    }

    public void onDamage(Player player, Player damager) {
        PlayerManager damagerJoueur = main.getPlayerManager(damager.getUniqueId());

        if (this.inSusano.contains(damager.getUniqueId())) {
            player.setFireTicks(8 * 20);
        }
        if (damagerJoueur.hasRole()) {
            if (damagerJoueur.getRole() instanceof NarutoV2Role) {
                NarutoV2Role narutoV2Role = (NarutoV2Role) damagerJoueur.getRole();
                if (isSword(damager.getItemInHand())) {
                    int value = UhcHost.getRANDOM().nextInt(99);
                    if (narutoV2Role.getChakra() != null && narutoV2Role.getChakra() == Chakra.RAITON) {
                        if (value == 0) {
                            player.getLocation().getWorld().strikeLightning(player.getLocation());
                        }
                    } else if (narutoV2Role.getChakra() != null && narutoV2Role.getChakra() == Chakra.KATON) {
                        if (value <= 1) {
                            player.setFireTicks(8 * 20);
                        }
                    }
                }
            }
            if (damagerJoueur.getRole() instanceof RasenganItem.RasenganUser) {
                RasenganItem.RasenganUser rasenganUser = (RasenganItem.RasenganUser) damagerJoueur.getRole();
                if (rasenganUser.isAttackBoosted()) {
                    if(player.getWorld() != getKamuiWorld())
                        player.getWorld().createExplosion(player.getLocation(), 1.0f);
                    rasenganUser.setAttackBoosted(false);
                }
            }
        }
    }

    private boolean isSword(ItemStack item) {
        return (item.getType() == Material.DIAMOND_SWORD || item.getType() == Material.GOLD_SWORD
                || item.getType() == Material.IRON_SWORD || item.getType() == Material.STONE_SWORD
                || item.getType() == Material.WOOD_SWORD);
    }


    public void annonceHokage() {
    	List<PlayerManager> joueurs = new ArrayList<>();
    	List<PlayerManager> canNotSeeHokage = new ArrayList<>();
    	setReveal(false);
    	setBoosted(null);
    	for (Class<?> role : this.canBeHokage) {
			joueurs.addAll(getPlayerManagersWithRole((Class<? extends Role>) role).stream().filter(PlayerManager::isAlive)
					.collect(Collectors.toList()));
		}
    	if(joueurs.isEmpty()) {
    	    UhcHost.debug("No one can be hokage, so return the function.");
    	    Bukkit.broadcastMessage("§cAucun joueur n'est apte pour devenir Hokage.");
    	    return;
        }
    	PlayerManager picked = joueurs.get(UhcHost.getRANDOM().nextInt(joueurs.size()));


    	for(Class<?> role : this.canNotSeeHokage) {
    		canNotSeeHokage.addAll(getPlayerManagersWithRole((Class<? extends Role>) role).stream().filter(PlayerManager::isAlive)
					.collect(Collectors.toList()));
    	}
    	for(Player player : Bukkit.getOnlinePlayers()) {
    		if(!canNotSeeHokage.contains(main.getPlayerManager(player.getUniqueId()))) {
    			player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§6Le Conseil de Konoha et le Conseil du Daimyô du Feu ont choisis le nouveau Hokage. Il s'agit de§e " + picked.getPlayerName() + "§6.");
    			PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
    			if(joueur.hasRole() && joueur.isAlive()) {
    				if(joueur.getRole() instanceof Danzo) {
    					player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVoici le rôle de l'Hokage: " + picked.getRole().getRoleName() + ".");
    				}
    			}
    		}
    		else {
    			player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cL'Hokage a été désigné mais vous ne connaissez pas son identité.");

    		}

    	}

    	if (picked.getPlayer() != null) {
    		picked.getPlayer().sendMessage(hokageDescription());
    		UhcHost.debug("Picked Hokage: " + picked.getPlayerName());
    	} else {
			UhcHost.debug("the picked for Hokage (" + picked.getPlayerName() + ") is not online.");
		}
    	hokage = picked;

    }

    public void danzoHokage() {
        List<PlayerManager> canNotSeeHokage = new ArrayList<>();
        PlayerManager picked = null;
        for(PlayerManager joueur : getPlayerManagersWithRole(Danzo.class)){
            picked = joueur;
        }
        setReveal(false);
        setBoosted(null);
        if(picked != null){
            for(Player player : Bukkit.getOnlinePlayers()) {
                if(!canNotSeeHokage.contains(main.getPlayerManager(player.getUniqueId()))) {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§6Le Conseil de Konoha et le Conseil du Daimyô du Feu ont choisis le nouveau Hokage. Il s'agit de§e " + picked.getPlayerName() + "§6.");
                    PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
                    if(joueur.hasRole() && joueur.isAlive()) {
                        if(joueur.getRole() instanceof Danzo) {
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVoici le rôle de l'Hokage: " + picked.getRole().getRoleName() + ".");
                        }
                    }
                }
                else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cL'Hokage a été désigné mais vous ne connaissez pas son identité.");
                }
            }
            if (picked.getPlayer() != null) {
                picked.getPlayer().sendMessage(hokageDescription());
                UhcHost.debug("Picked Hokage: " + picked.getPlayerName());
            } else {
                UhcHost.debug("the picked for Hokage (" + picked.getPlayerName() + ") is not online.");
            }
            hokage = picked;
        }
    }

    private String hokageDescription() {
		return " \n"
				+ "§8§m----------------------------\n"
				+ "§eVous êtes l'§6Hokage§e de cette partie. \n"
				+ " \n"
				+ "§c§lImportant :§e Vous conservez vos pouvoirs, vos aptitudes ainsi que vos objets. \n"
				+ " \n"
				+ "§e • Vous disposez de 10% de Force et de Résistance supplémentaires. \n"
				+ " \n"
				+ "§e • /ns boost <joueur> : Permet d'octroyer 10% de Force et de Résistance en plus. \n"
				+ " \n"
				+ "§e • /ns reveal : Permet d'avoir 4 pseudos dont 2 faisant partie du camp des Shinobis, 1 d'un camp opposé au Shinobis et 1 de manière aléatoire. §c(Vous pouvez avoir plusieurs fois le même pseudo). \n"
				+ " \n"
				+ "§6Les commandes sont utilisables 1 fois chacune dans la partie."
				+ "§8§m----------------------------"
				;
    }

    @EventHandler
    public void onPlayerBucket(PlayerBucketEmptyEvent event) {
        Block liquid = event.getBlockClicked().getRelative(event.getBlockFace());

        Player player = event.getPlayer();
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if (liquid.getWorld() == this.kamuiWorld) {
            if(liquid.getLocation().getX() < 25030){
                //JUMP GAARA
                event.setCancelled(true);
            }else{
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        liquid.setType(Material.AIR);
                    }
                }.runTaskLater(main, 20 * 60);
            }
        }

        if(jubi != null && jubi.isAlive() && jubiUsed) {
        	if(jubi.getPlayer().getLocation().distance(player.getLocation()) <= 15 && joueur != jubi) {
        		if(event.getBucket() == Material.LAVA_BUCKET) {
        			event.setCancelled(true);
        			player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cL'hôte de Jûbi vous empêche de poser de la lave prêt de lui.");
        		}
        	}
        }
    }

    public void addInSusano(UUID uniqueId) {
        this.inSusano.add(uniqueId);
        new BukkitRunnable() {

            int timer = 5 * 60;

            @Override
            public void run() {
                if (timer == 0) {
                    NarutoV2Manager.this.inSusano.remove(uniqueId);
                    cancel();
                    return;
                }
                Player player = main.getServer().getPlayer(uniqueId);
                if (player != null) {
                    ActionBar.sendMessage(player, "§bSusano §7: §3" + new FormatTime(timer));
                }
                timer--;
            }
        }.runTaskTimer(main, 0, 20);
    }

    public void addInSusanoDouble(UUID uniqueId) {
        this.inSusano.add(uniqueId);
        new BukkitRunnable() {

            int timer = 10 * 60;

            @Override
            public void run() {
                if (timer == 0) {
                    NarutoV2Manager.this.inSusano.remove(uniqueId);
                    cancel();
                    return;
                }
                Player player = main.getServer().getPlayer(uniqueId);
                if (player != null) {
                    ActionBar.sendMessage(player, "§bSusano §7: §3" + new FormatTime(timer));
                }
                timer--;
            }
        }.runTaskTimer(main, 0, 20);
    }

    public List<UUID> getSusano(){
    	return inSusano;
    }

    public Map<UUID, Long> getLastSusanoAttack() {
        return lastSusanoAttack;
    }

	public PlayerManager getHokage() {
		return hokage;
	}

	public List<PlayerManager> getNofallJoueur(){
        return nofallJoueur;
    }

	public void setHokage(PlayerManager hokage) {
		this.hokage = hokage;
	}

	public PlayerManager getBoosted() {
		return boosted;
	}

	public void setBoosted(PlayerManager boosted) {
		this.boosted = boosted;
	}

	public boolean isReveal() {
		return reveal;
	}

	public void setReveal(boolean reveal) {
		this.reveal = reveal;
	}

	public PlayerManager getJubi() {
		return jubi;
	}

	public void setJubi(PlayerManager jubi) {
		this.jubi = jubi;
	}

    public void setCanUseJubi(boolean canUseJubi) {
        this.canUseJubi = canUseJubi;
    }

    public boolean isCanUseJubi() {
        return canUseJubi;
    }

    public boolean isJubiUsed() {
		return jubiUsed;
	}

	public void setJubiUsed(boolean jubiUsed) {
		this.jubiUsed = jubiUsed;
	}

	public BijuManager getBijuManager() {
		return bijuManager;
	}

	public void setBijuManager(BijuManager bijuManager) {
		this.bijuManager = bijuManager;
	}

    public SakonUkonManager getSakonUkonManager() {
        return sakonUkonManager;
    }

    public boolean isDanzoKilledHokage() {
        return danzoKilledHokage;
    }

    public void setDanzoKilledHokage(boolean danzoKilledHokage) {
        this.danzoKilledHokage = danzoKilledHokage;
    }

    public NarutoV2Config getNarutoV2Config() {
        return narutoV2Config;
    }
}
