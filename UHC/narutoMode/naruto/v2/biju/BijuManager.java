package fr.lastril.uhchost.modes.naruto.v2.biju;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.items.biju.*;
import fr.lastril.uhchost.modes.naruto.v2.roles.BijuUser;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Madara;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Obito;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
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
    private final Map<Class<? extends Biju>, PlayerManager> hotesBiju;
    private final Map<PlayerManager, Integer> recupBiju;
    private final List<ItemStack> bijuItem;
    private final List<PlayerManager> noFall;
    private final List<Biju> bijuListClass;
    private final List<PlayerManager> isobuResistance;
    private final List<PlayerManager> sanGokuNoLava;
    private final List<PlayerManager> saikenHote;
    private final List<PlayerManager> kokuoHote;
    private boolean craftedJubi = false, recupKurama, recupGyuki, recupShukaku;

    public BijuManager(UhcHost main) {
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
        for (BijuList bijuEnum : BijuList.values()) {
            hotesBiju.put(bijuEnum.getBiju(), null);
        }
        main.getServer().getPluginManager().registerEvents(this, main);
        main.getServer().getScheduler().runTaskLater(main, this, 0);
        registerJubiCraft(main.getServer());


    }

    private void registerJubiCraft(Server server) {
        ShapedRecipe jubi = new ShapedRecipe(new JubiItem(main).toItemStack());
        jubi.shape(".*;", "!%#", ":::");
        jubi.setIngredient('.', new MatatabiItem(main).toItemStack().getData());
        jubi.setIngredient('*', new IsobuItem(main).toItemStack().getData());
        jubi.setIngredient(';', new SonGokuItem(main).toItemStack().getData());
        jubi.setIngredient('!', new KokuoItem(main).toItemStack().getData());
        jubi.setIngredient('%', new SaikenItem(main).toItemStack().getData());
        jubi.setIngredient('#', new ChomeiItem(main).toItemStack().getData());
        server.addRecipe(jubi);
    }

    @EventHandler
    public void Craft(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();
        Player player = (Player) event.getWhoClicked();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (result != null) {
            if (result.getType() == Material.NETHER_STAR) {
                if (result.isSimilar(new JubiItem(main).toItemStack())) {
                    if (playerManager.getRole() instanceof JubiItem.JubiUser) {
                        Bukkit.broadcastMessage(" ");
                        Bukkit.broadcastMessage("§d§lJûbi vient d'être invoqué ! Faites attention à son nouvel hôte !");
                        Bukkit.broadcastMessage(" ");
                        setCraftedJubi(true);
                        setJubiTablist();
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

    private void setJubiTablist() {
        for (PlayerManager playerManager : main.getNarutoV2Manager().getPlayerManagersWithRole(Obito.class)) {
            if (playerManager.getPlayer() != null) {
                playerManager.getPlayer().setPlayerListName("§4" + playerManager.getPlayerName());
            }
        }
        for (PlayerManager playerManager : main.getNarutoV2Manager().getPlayerManagersWithRole(Madara.class)) {
            if (playerManager.getPlayer() != null) {
                playerManager.getPlayer().setPlayerListName("§4" + playerManager.getPlayerName());
            }
        }
    }


    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getCause() == DamageCause.FALL) {
                Player player = (Player) event.getEntity();
                PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                if (noFall.contains(playerManager))
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamageHote(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if (isobuResistance.contains(playerManager)) {
                int value = UhcHost.getRANDOM().nextInt(100);
                if (value <= 5) {
                    event.setCancelled(true);
                }
            }
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                PlayerManager damagerPlayerManager = main.getPlayerManager(damager.getUniqueId());
                if (saikenHote.contains(damagerPlayerManager)) {
                    int value = UhcHost.getRANDOM().nextInt(100);
                    if (value <= 15) {
                        if (damager.getHealth() + 1 >= damager.getMaxHealth()) {
                            damager.setHealth(damager.getMaxHealth());
                        } else {
                            damager.setHealth(damager.getHealth() + 1);
                        }
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2 * 20, 0));
                        player.getWorld().createExplosion(player.getLocation(), 1.0f);
                    }
                }
                if (kokuoHote.contains(damagerPlayerManager)) {
                    int value = UhcHost.getRANDOM().nextInt(100);
                    if (value <= 15) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 3, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 0, false, false));
                    }
                }
                if (sanGokuNoLava.contains(damagerPlayerManager)) {
                    int value = UhcHost.getRANDOM().nextInt(100);
                    if (value <= 50) {
                        player.setFireTicks(20 * 8);
                    }
                }

            }
        }
    }

    @EventHandler
    public void onPlayerBucket(PlayerBucketEmptyEvent event) {
        Block liquid = event.getBlockClicked().getRelative(event.getBlockFace());
        Player player = event.getPlayer();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (!sanGokuNoLava.isEmpty()) {
            if (event.getBucket() == Material.LAVA_BUCKET && liquid.getLocation().distance(sanGokuNoLava.get(0).getPlayer().getLocation()) <= 15 && !sanGokuNoLava.contains(playerManager)) {
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cL'hôte de San Gokû vous empêche de poser de la lave à côté de lui.");
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void run() {
        for (Class<? extends Biju> biju : hotesBiju.keySet()) {
            Biju bijuInstance = null;
            try {
                bijuInstance = biju.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            if (bijuInstance != null) {
                int value = UhcHost.getRANDOM().nextInt(40, 60);
                bijuListClass.add(bijuInstance);
                bijuItem.add(bijuInstance.getItem().toItemStack());
                bijuInstance.setFirstSpawn(value);
                bijuInstance.runTaskTimer(main, value * 20 * 60, 20);
            }
        }
    }

    public boolean isAlreadyHote(PlayerManager playerManager) {
        for (PlayerManager PlayerManagers : hotesBiju.values()) {
            if (PlayerManagers == playerManager || isRoleHote(playerManager)) {
                return true;
            }
        }
        return false;
    }

    private boolean isRoleHote(PlayerManager playerManager) {
        return playerManager.getRole() instanceof BijuUser;
    }

    public Map<Class<? extends Biju>, PlayerManager> getHotesBiju() {
        return hotesBiju;
    }

    public Map<PlayerManager, Integer> getRecupBiju() {
        return recupBiju;
    }

    public List<ItemStack> getBijuItem() {
        return bijuItem;
    }

    public List<PlayerManager> getNoFall() {
        return noFall;
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

    public List<PlayerManager> getKokuoHote() {
        return kokuoHote;
    }

    public boolean isCraftedJubi() {
        return craftedJubi;
    }

    public void setCraftedJubi(boolean craftedJubi) {
        this.craftedJubi = craftedJubi;
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
