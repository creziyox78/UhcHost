package fr.lastril.uhchost.modes.naruto.v2.roles.taka;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.api.items.Livre;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleListener;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.biju.JubiItem;
import fr.maygo.uhc.modes.naruto.v2.items.swords.KubikiribochoSword;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.Itachi;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.Nagato;
import fr.maygo.uhc.modes.naruto.v2.roles.orochimaru.Orochimaru;
import fr.maygo.uhc.modes.naruto.v2.roles.zabuza.Zabuza;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Suigetsu extends Role implements NarutoV2Role, RoleListener {

    private final int chance = 10;
    private boolean invisible = false, orochimaruDead, itachiDead;

    public Suigetsu() {
        super.addRoleToKnow(Orochimaru.class);
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        Block block = player.getLocation().getBlock();
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.hasRole() && PlayerManager.isAlive()) {
            if (PlayerManager.getRole() instanceof Suigetsu) {
                Suigetsu suigestu = (Suigetsu) PlayerManager.getRole();
                if (block.isLiquid() || block.getLocation().add(0, 1, 0).getBlock().isLiquid()) {
                    if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    if (player.hasPotionEffect(PotionEffectType.WATER_BREATHING))
                        player.removePotionEffect(PotionEffectType.WATER_BREATHING);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 1, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20 * 5, 0, false, false));
                    EntityEquipment equipement = player.getEquipment();
                    if (equipement.getHelmet() == null && equipement.getChestplate() == null && equipement.getLeggings() == null
                            && equipement.getBoots() == null) {
                        player.addPotionEffect(
                                new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
                        suigestu.setInvisible(true);
                    } else {
                        suigestu.setInvisible(false);
                        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
                            player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    }

                } else {
                    suigestu.setInvisible(false);
                    if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
                        player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 0, false, false));
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Suigetsu) {
                    Suigetsu suigestu = (Suigetsu) PlayerManager.getRole();
                    if (!suigestu.isInvisible()) {
                        UhcHost.debug("Not invisible Suigestu");
                        int value = UhcHost.getRandom().nextInt(100);
                        UhcHost.debug("Value Suegestu: " + value);
                        if (value <= 10) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }

                }
            }
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                PlayerManager damagerPlayerManager = main.getPlayerManager(damager.getUniqueId());
                if (damagerPlayerManager.hasRole()) {
                    if (damagerPlayerManager.getRole() instanceof Suigetsu) {
                        Suigetsu suigestu = (Suigetsu) damagerPlayerManager.getRole();
                        UhcHost.debug("Damager Suigestu");
                        if (suigestu.isInvisible()) {
                            UhcHost.debug("Invisible Suigestu");
                            event.setCancelled(true);
                            return;
                        }
                        int pourcentage = UhcHost.getRandom().nextInt(100);
                        if (pourcentage <= chance) {
                            if (damager.getHealth() + event.getFinalDamage() >= damager.getMaxHealth())
                                damager.setHealth(damager.getMaxHealth());
                            else
                                damager.setHealth(damager.getHealth() + event.getFinalDamage());
                        }
                    }
                }
            } else if (event.getDamager() instanceof Projectile) {
                if (PlayerManager.hasRole()) {
                    if (PlayerManager.getRole() instanceof Suigetsu) {
                        Suigetsu suigestu = (Suigetsu) PlayerManager.getRole();
                        if (suigestu.isInvisible()) {
                            event.setCancelled(true);
                        }
                    }
                }

            }

        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
            if (PlayerManager.getRole() instanceof Suigetsu) {
                event.setCancelled(invisible);
            }
        }
    }

    @EventHandler
    public void Craft(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();
        if (result.getType() == Material.NETHER_STAR) {
            if (result.isSimilar(new JubiItem(main).toItemStack())) {
                if (super.getPlayer() != null) {
                    Player jugoPlayer = super.getPlayer();
                    main.getPlayerManager(jugoPlayer.getUniqueId()).setCamps(Camps.SHINOBI);
                    jugoPlayer.sendMessage("§5Jûbi§e vient d'être invoqué. Vous gagnez désormais avec les§a Shinobis.");
                }
            }
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Zabuza) {
                if (super.getPlayer() != null) {
                    Player suigetsu = super.getPlayer();
                    suigetsu.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§3Zabuza§e vient de mourir. Vous recevez donc l'épée§3 Kubikiribôchô§e ainsi que de ses propriétés.");
                    main.getInventoryUtils().giveItemSafely(suigetsu, new KubikiribochoSword().toItemStack());
                }
            }
            if (PlayerManager.getRole() instanceof Orochimaru) {
                if (super.getPlayer() != null) {
                    if (itachiDead) {
                        Player sasukePlayer = super.getPlayer();
                        itachiDead = true;
                        main.getPlayerManager(sasukePlayer.getUniqueId()).setCamps(Camps.AKATSUKI);
                        sasukePlayer.sendMessage("§cVous gagnez avec l'§cAkatsuki car Itachi et Orochimaru sont morts. Voici l'identité de Nagato et de Sasuke: ");
                        for (PlayerManager nagato : main.getNarutoV2Manager().getPlayerManagersWithRole(Nagato.class)) {
                            sasukePlayer.sendMessage("§c - Nagato : " + nagato.getPlayerName());
                        }
                        for (PlayerManager sasuke : main.getNarutoV2Manager().getPlayerManagersWithRole(Sasuke.class)) {
                            sasukePlayer.sendMessage("§6 - Sasuke : " + sasuke.getPlayerName());
                        }
                    } else {
                        orochimaruDead = true;
                        Player sasukePlayer = super.getPlayer();
                        main.getPlayerManager(sasukePlayer.getUniqueId()).setCamps(Camps.TAKA);
                        sasukePlayer.sendMessage("§5Orochimaru§e vient de mourir, vous gagnez uniquement avec votre camp. Voici l'identité de Sasuke: ");
                        for (PlayerManager sasuke : main.getNarutoV2Manager().getPlayerManagersWithRole(Sasuke.class)) {
                            sasukePlayer.sendMessage("§6 - Sasuke : " + sasuke.getPlayerName());
                        }
                    }

                }
            } else if (PlayerManager.getRole() instanceof Itachi) {
                if (super.getPlayer() != null) {
                    if (orochimaruDead) {
                        Player sasukePlayer = super.getPlayer();
                        itachiDead = true;
                        main.getPlayerManager(sasukePlayer.getUniqueId()).setCamps(Camps.AKATSUKI);
                        sasukePlayer.sendMessage("§cItachi§e vient de mourir, vous gagnez avec l'§cAkatsuki. Voici l'identité de Nagato: ");
                        for (PlayerManager nagato : main.getNarutoV2Manager().getPlayerManagersWithRole(Nagato.class)) {
                            sasukePlayer.sendMessage("§c - Nagato : " + nagato.getPlayerName());
                        }
                    } else {
                        itachiDead = true;
                    }
                }
            }

        }
    }

    @Override
    public void giveItems(Player player) {
        Livre livre = new Livre(Enchantment.DEPTH_STRIDER, 3);
        main.getInventoryUtils().giveItemSafely(player, livre.toItemStack());
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDgzODgxZDMzYWZmYTliZTZlMTAwODJjOTE4NTJmMDViMWQ2MjgzMTU1ODI2ODgyNmY2OWYzNzE5YTNjODQ3MSJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.TAKA;
    }

    @Override
    public String getRoleName() {
        return "Suigetsu";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    @Override
    public Chakra getChakra() {
        return Chakra.SUITON;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    @Override
    public void afterRoles(Player player) {
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        PlayerManager.setCamps(Camps.OROCHIMARU);
    }
}
