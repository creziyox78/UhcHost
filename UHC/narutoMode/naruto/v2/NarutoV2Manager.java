package fr.lastril.uhchost.modes.naruto.v2;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.modes.ModeManager;
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
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.*;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.*;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Danzo;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Gaara;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Jugo;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Sasuke;
import fr.lastril.uhchost.modes.naruto.v2.sakonukon.SakonUkonManager;
import fr.lastril.uhchost.player.PlayerManager;
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
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class NarutoV2Manager extends ModeManager implements Listener {

    private static final int SUSANO_ATTACK_DELAY = 20;

    private final UhcHost main;
    /**
     * KAMUI
     */
    private final World kamuiWorld;
    private final Location kamuiLocation;
    private final List<UUID> samehada, inShosenjutsu, inSusano;
    private final Map<UUID, Long> lastSusanoAttack;
    private final List<PlayerManager> nofallPlayerManager;
    private final SakonUkonManager sakonUkonManager;
    private final Class<?>[] canBeHokage = new Class<?>[]{Naruto.class, Sai.class, Minato.class,
            Kakashi.class, Jiraya.class, Tsunade.class, Hinata.class, Neji.class, GaiMaito.class,
            Shikamaru.class, Sasuke.class};
    private final Class<?>[] canNotSeeHokage = new Class<?>[]{Temari.class, YondameRaikage.class, KillerBee.class,
            ZetsuNoir.class, ZetsuBlanc.class, Sakon.class, Ukon.class, Kidomaru.class, Tayuya.class,
            Jirobo.class, Jugo.class, Gaara.class};
    private BijuManager bijuManager;
    private PlayerManager hokage, boosted, jubi;
    private int duration;
    private boolean reveal, jubiUsed = false, meteoresExplode, canUseJubi = true, danzoKilledHokage;

    public NarutoV2Manager(UhcHost main) {
        this.main = main;
        this.kamuiWorld = main.getServer().createWorld(WorldCreator.name("kamui"));
        this.kamuiLocation = new Location(this.kamuiWorld, 25108, 13, 25015);
        this.samehada = new ArrayList<>();
        this.inShosenjutsu = new ArrayList<>();
        this.nofallPlayerManager = new ArrayList<>();
        this.inSusano = new ArrayList<>();
        this.sakonUkonManager = new SakonUkonManager(this);
        this.lastSusanoAttack = new HashMap<>();
        main.getServer().getPluginManager().registerEvents(this, main);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (GameState.isInGame(main.getState())) {
                    for (PlayerManager playerManager : main.getPlayerManagersAlives()) {
                        if (playerManager.hasRole() && playerManager.isAlive()) {
                            if (playerManager.getPlayer() != null) {
                                Player player = playerManager.getPlayer();
                                if (playerManager.getRole() instanceof NarutoV2Role) {
                                    NarutoV2Role narutoV2Role = (NarutoV2Role) playerManager.getRole();
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
                                                if (player.getInventory().getBoots().getEnchantmentLevel(Enchantment.DEPTH_STRIDER) <= 1) {
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
        }.runTaskTimer(main, 0, 2);
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
        UhcHost.debug("added " + uuid + " in shosenjutsu, now to : " + this.inShosenjutsu);
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        double distance = event.getFrom().distance(event.getTo());
        boolean hasMoved = distance >= 0.2;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (hasMoved && this.inShosenjutsu.contains(player.getUniqueId())) {
            UhcHost.debug(player.getUniqueId() + " (" + player.getName() + ") in shosenjutsu (" + this.inShosenjutsu + ") moved so clear everyone");
            for (UUID uuid : this.inShosenjutsu) {
                Player shosenjutsu = Bukkit.getPlayer(uuid);
                if (shosenjutsu != null) {
                    shosenjutsu.removePotionEffect(PotionEffectType.REGENERATION);
                    shosenjutsu.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous avez bougé donc votre régénération ne fait plus effet.");
                }
            }
            this.inShosenjutsu.clear();
        }
    }

    public World getKamuiWorld() {
        return kamuiWorld;
    }

    public void sendToKamui(Player player, Location originalLocation, Player sender) {
        if (player.getWorld() != this.kamuiWorld) {
            player.teleport(this.kamuiLocation);
            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous vous êtes téléporté dans le monde de Kamui.");

            new BukkitRunnable() {

                int timer = 60 * 5;

                @Override
                public void run() {
                    if (player.getWorld() != NarutoV2Manager.this.kamuiWorld) {
                        cancel();
                    }
                    Player players = Bukkit.getPlayer(player.getUniqueId());
                    PlayerManager senderPlayerManager = main.getPlayerManager(sender.getUniqueId());
                    if (!senderPlayerManager.isAlive()) {
                        if (players.getWorld() == NarutoV2Manager.this.getKamuiWorld()) {
                            Location loc = new Location(originalLocation.getWorld(), originalLocation.getX(), originalLocation.getWorld().getHighestBlockYAt(originalLocation), originalLocation.getZ());
                            originalLocation.getChunk().load();
                            players.teleport(loc);
                            players.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez été retéléporté dans le monde normal.");
                            cancel();
                        }
                    }
                    if (players != null) {
                        ActionBar.sendMessage(players, "§9Kamui §7: §3" + new FormatTime(timer));
                    }

                    if (timer == 0) {
                        if (players != null) {
                            if (players.getWorld() == NarutoV2Manager.this.getKamuiWorld()) {
                                Location loc = new Location(originalLocation.getWorld(), originalLocation.getX(), originalLocation.getWorld().getHighestBlockYAt(originalLocation), originalLocation.getZ());
                                originalLocation.getChunk().load();
                                players.teleport(loc);
                                players.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez été retéléporté dans le monde normal.");
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
        if (event.getEntity() instanceof FallingBlock) {
            FallingBlock fallingBlock = (FallingBlock) event.getEntity();
            if (fallingBlock.getCustomName() != null) {
                if (fallingBlock.getCustomName().equals(MeteoresGenerator.METEORE_KEY)) {
                    block.setType(Material.AIR);
                    if (!meteoresExplode) {
                        if (fallingBlock.getWorld() != main.getNarutoV2Manager().getKamuiWorld())
                            WorldUtils.createBeautyExplosion(fallingBlock.getLocation(), 30);
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            PlayerManager targetPlayerManager = main.getPlayerManager(players.getUniqueId());
                            if (players.getWorld() == fallingBlock.getWorld()) {
                                if (players.getGameMode() != GameMode.SPECTATOR && targetPlayerManager.isAlive()) {
                                    if (!(targetPlayerManager.getRole() instanceof Madara)) {
                                        if (players.getLocation().distance(fallingBlock.getLocation()) <= 29) {
                                            if (targetPlayerManager.getRole() instanceof Danzo) {
                                                Danzo danzo = (Danzo) targetPlayerManager.getRole();
                                                danzo.useVie(targetPlayerManager, 100);
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
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.getWorld() == this.kamuiWorld) {
            event.setCancelled(true);
            player.sendMessage(Messages.error("Vous ne pouvez pas casser de blocs dans le " + this.getPlaceInKamuiWorld(block.getLocation())));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.getWorld() == this.kamuiWorld) {
            event.setCancelled(true);
            player.sendMessage(Messages.error("Vous ne pouvez pas poser de blocs dans le " + this.getPlaceInKamuiWorld(block.getLocation())));
        }
    }

    public String getPlaceInKamuiWorld(Location location) {
        return location.getX() < 25030 ? "jump de Gaara" : "monde de Kamui";
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerFall(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if (player.getWorld() == this.kamuiWorld || (playerManager == jubi && isJubiUsed())) {
                event.setCancelled(event.getCause() == EntityDamageEvent.DamageCause.FALL);
            }
            if (getNofallPlayerManager().contains(playerManager) && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }
            if (playerManager.hasRole() && playerManager.isAlive()) {
                if (playerManager.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoV2Role = (NarutoV2Role) playerManager.getRole();
                    if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                        if (narutoV2Role.getChakra() != null && narutoV2Role.getChakra() == Chakra.FUTON) {
                            event.setDamage(event.getDamage() / 1.2);
                        }
                    }

                }
            }
            if (hokage != null) {
                if (hokage == playerManager || boosted == playerManager) {
                    event.setDamage(event.getDamage() / 1.1);
                }
            }

        }
    }

    @EventHandler
    public void onHokageDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (playerManager == hokage) {
            setHokage(null);
            Bukkit.getScheduler().runTaskLater(main, () -> {
                Bukkit.broadcastMessage("§e§lL'Hokage étant mort, un autre PlayerManager a été choisis pour être Hokage.");
                if (!danzoKilledHokage) {
                    annonceHokage();
                }
            }, 20 * 60 * 5);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBowSusano(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if (playerManager.hasRole()) {
                if (playerManager.getRole() instanceof Sasuke) {
                    ItemStack item = event.getBow();
                    ItemStack susano = new SusanoBow().toItemStack();
                    susano.setDurability(item.getDurability());
                    if (item.isSimilar(susano)) {
                        if (this.inSusano.contains(player.getUniqueId())) {
                            long delayLastAttack = System.currentTimeMillis() - this.lastSusanoAttack.getOrDefault(player.getUniqueId(), 0L);
                            if (delayLastAttack <= SUSANO_ATTACK_DELAY * 1000) {
                                event.setCancelled(true);
                                player.getWorld().playSound(player.getLocation(), Sound.ANVIL_BREAK, 1f, 1f);
                                player.sendMessage(Messages.cooldown((int) (SUSANO_ATTACK_DELAY - (delayLastAttack / 1000))));
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
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            Player damager = (Player) event.getDamager();
            PlayerManager damagerPlayerManager = main.getPlayerManager(damager.getUniqueId());
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
                            damager.sendMessage(Messages.cooldown((int) (SUSANO_ATTACK_DELAY - (delayLastAttack / 1000))));
                        } else {
                            this.lastSusanoAttack.put(damager.getUniqueId(), System.currentTimeMillis());
                        }
                    } else {
                        damager.setItemInHand(null);
                        damager.sendMessage(Messages.error("Vous ne pouvez plus utiliser votre susano"));
                    }
                }
            }
            if (jubi == damagerPlayerManager && jubiUsed) {
                int value = UhcHost.getRandom().nextInt(100);
                if (value <= 10) {
                    duration = 0;
                    for (PotionEffect effect : damager.getActivePotionEffects()) {
                        if (damager.hasPotionEffect(PotionEffectType.SPEED)) {
                            duration = effect.getDuration();
                            damager.removePotionEffect(PotionEffectType.SPEED);
                        }
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 3, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 0, false, false));
                    if (player.getWorld() != getKamuiWorld())
                        player.getWorld().createExplosion(player.getLocation(), 2.0f);
                    damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 2, false, false));
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration - 20 * 5, 1, false, false));
                        }
                    }.runTaskLater(main, 20 * 5 + 1);
                }
                if (value <= 20) {
                    player.setFireTicks(20 * 8);
                }
            }
            if (hokage != null) {
                if (hokage == playerManager || boosted == playerManager) {
                    event.setDamage(event.getDamage() / 1.1);
                }
                if (hokage == damagerPlayerManager || boosted == damagerPlayerManager) {
                    event.setDamage(event.getDamage() + ((10 / 100) * event.getDamage()));
                }
            }


        }
    }

    public void killSafely(OfflinePlayer player) {
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        playerManager.setAlive(false);
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.playSound(players.getLocation(), Sound.WITHER_DEATH, 1f, 1f);
        }
        if (playerManager.hasRole()) {
            Bukkit.broadcastMessage("§3§m----------------------------------");
            Bukkit.broadcastMessage("§b§l" + player.getName() + "§7 est mort, son rôle était " + playerManager.getRole().getCamp().getCompoColor() + playerManager.getRole().getRoleName() + "§7.");
            Bukkit.broadcastMessage("§3§m----------------------------------");
        } else {
            Bukkit.broadcastMessage("§3§m----------------------------------");
            Bukkit.broadcastMessage("§b§l" + player.getName() + "§7 est mort.");
            Bukkit.broadcastMessage("§3§m----------------------------------");
        }
    }

    public void onDamage(Player player, Player damager) {
        PlayerManager damagerPlayerManager = main.getPlayerManager(damager.getUniqueId());

        if (this.inSusano.contains(damager.getUniqueId())) {
            player.setFireTicks(8 * 20);
        }
        if (damagerPlayerManager.hasRole()) {
            if (damagerPlayerManager.getRole() instanceof NarutoV2Role) {
                NarutoV2Role narutoV2Role = (NarutoV2Role) damagerPlayerManager.getRole();
                if (isSword(damager.getItemInHand())) {
                    int value = UhcHost.getRandom().nextInt(99);
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
            if (damagerPlayerManager.getRole() instanceof RasenganItem.RasenganUser) {
                RasenganItem.RasenganUser rasenganUser = (RasenganItem.RasenganUser) damagerPlayerManager.getRole();
                if (rasenganUser.isAttackBoosted()) {
                    if (player.getWorld() != getKamuiWorld())
                        player.getWorld().createExplosion(player.getLocation(), 1.0f);
                    rasenganUser.setAttackBoosted(false);
                    main.getSoundUtils().playSoundDistance(player.getLocation(), 5, "atlantis.narutorasengan");
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
        List<PlayerManager> playerManagers = new ArrayList<>();
        List<PlayerManager> canNotSeeHokage = new ArrayList<>();
        setReveal(false);
        setBoosted(null);
        for (Class<?> role : this.canBeHokage) {
            playerManagers.addAll(super.getPlayerManagersWithRole((Class<? extends Role>) role).stream().filter(PlayerManager::isAlive)
                    .collect(Collectors.toList()));
        }
        if (playerManagers.isEmpty()) {
            UhcHost.debug("No one can be hokage, so return the function.");
            Bukkit.broadcastMessage("§cAucun PlayerManager n'est apte pour devenir Hokage.");
            return;
        }
        PlayerManager picked = playerManagers.get(UhcHost.RANDOM.nextInt(playerManagers.size()));


        for (Class<?> role : this.canNotSeeHokage) {
            canNotSeeHokage.addAll(super.getPlayerManagersWithRole((Class<? extends Role>) role).stream().filter(PlayerManager::isAlive)
                    .collect(Collectors.toList()));
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!canNotSeeHokage.contains(main.getPlayerManager(player.getUniqueId()))) {
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§6Le Conseil de Konoha et le Conseil du Daimyô du Feu ont choisis le nouveau Hokage. Il s'agit de§e " + picked.getPlayerName() + "§6.");
                PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                if (playerManager.hasRole() && playerManager.isAlive()) {
                    if (playerManager.getRole() instanceof Danzo) {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVoici le rôle de l'Hokage: " + picked.getRoleName() + ".");
                    }
                }
            } else {
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
        for (PlayerManager playerManager : main.getNarutoV2Manager().getPlayerManagersWithRole(Danzo.class)) {
            picked = playerManager;
        }
        setReveal(false);
        setBoosted(null);
        if (picked != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!canNotSeeHokage.contains(main.getPlayerManager(player.getUniqueId()))) {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§6Le Conseil de Konoha et le Conseil du Daimyô du Feu ont choisis le nouveau Hokage. Il s'agit de§e " + picked.getPlayerName() + "§6.");
                    PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                    if (playerManager.hasRole() && playerManager.isAlive()) {
                        if (playerManager.getRole() instanceof Danzo) {
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVoici le rôle de l'Hokage: " + picked.getRoleName() + ".");
                        }
                    }
                } else {
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
                + "§e • /ns boost <PlayerManager> : Permet d'octroyer 10% de Force et de Résistance en plus. \n"
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
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (liquid.getWorld() == this.kamuiWorld) {
            if (liquid.getLocation().getX() < 25030) {
                //JUMP GAARA
                event.setCancelled(true);
            } else {
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        liquid.setType(Material.AIR);
                    }
                }.runTaskLater(main, 20 * 60);
            }
        }

        if (jubi != null && jubi.isAlive() && jubiUsed) {
            if (jubi.getPlayer().getLocation().distance(player.getLocation()) <= 15 && playerManager != jubi) {
                if (event.getBucket() == Material.LAVA_BUCKET) {
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

    public List<UUID> getSusano() {
        return inSusano;
    }

    public Map<UUID, Long> getLastSusanoAttack() {
        return lastSusanoAttack;
    }

    public PlayerManager getHokage() {
        return hokage;
    }

    public void setHokage(PlayerManager hokage) {
        this.hokage = hokage;
    }

    public List<PlayerManager> getNofallPlayerManager() {
        return nofallPlayerManager;
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

    public boolean isCanUseJubi() {
        return canUseJubi;
    }

    public void setCanUseJubi(boolean canUseJubi) {
        this.canUseJubi = canUseJubi;
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
}
