package fr.lastril.uhchost.modes.naruto.v2.roles.zabuza;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleListener;
import fr.maygo.uhc.modes.crafter.role.crafter.When;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.Camouflage;
import fr.maygo.uhc.modes.naruto.v2.items.swords.KubikiribochoSword;
import fr.maygo.uhc.obj.PlayerManager;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Zabuza extends Role implements NarutoV2Role, RoleListener {

    private final double DISTANCE = 20;
    private final int chance = 10;

    private boolean camoufled;

    public Zabuza() {
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addRoleToKnow(Haku.class);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new KubikiribochoSword().toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Camouflage(main).toItemStack());
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.hasRole() && PlayerManager.isAlive()) {
            if (PlayerManager.getRole() instanceof Zabuza) {
                Zabuza zabuza = (Zabuza) PlayerManager.getRole();
                if (zabuza.isCamoufled()) {
                    zabuza.hidePlayer(player);
                }
            }
        }
    }

    @EventHandler
    public void onDamageWithSword(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
                if (PlayerManager.hasRole()) {
                    if (PlayerManager.getRole() instanceof Zabuza) {
                        Zabuza zabuza = (Zabuza) PlayerManager.getRole();
                        if (zabuza.isCamoufled()) {
                            PlayerManager.setRoleCooldownCamouflage(5 * 60);
                            zabuza.setCamoufled(false);
                            zabuza.showPlayer(player);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous êtes§c invisible§e (même avec votre armure) aux yeux de tous maintenant.");
                        }
                    }
                }
            }
            Player damager = (Player) event.getDamager();
            PlayerManager damagerPlayerManager = main.getPlayerManager(damager.getUniqueId());
            if (damagerPlayerManager.hasRole()) {
                if (damagerPlayerManager.getRole() instanceof Zabuza) {
                    int pourcentage = UhcHost.getRandom().nextInt(100);
                    if (pourcentage <= chance) {
                        if (damager.getHealth() + event.getFinalDamage() >= damager.getMaxHealth())
                            damager.setHealth(damager.getMaxHealth());
                        else
                            damager.setHealth(damager.getHealth() + event.getFinalDamage());
                    }
                    Zabuza zabuza = (Zabuza) damagerPlayerManager.getRole();
                    if (zabuza.isCamoufled()) {
                        damagerPlayerManager.setRoleCooldownCamouflage(5 * 60);
                        zabuza.setCamoufled(false);
                        zabuza.showPlayer(damager);
                        damager.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous êtes§a visible§e aux yeux de tous maintenant.");
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Haku) {
                if (super.getPlayer() != null) {
                    Player zabuza = super.getPlayer();
                    zabuza.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§3Haku§e vient de mourir. Vous obtenez§b Speed 2§e pendant 10 minutes ainsi que§f Résistance permanent§e.");
                    zabuza.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
                    if (zabuza.hasPotionEffect(PotionEffectType.SPEED))
                        zabuza.removePotionEffect(PotionEffectType.SPEED);
                    zabuza.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 10, 1, false, false));
                    Bukkit.getScheduler().runTaskLater(main, () -> {
                        if (zabuza != null) {
                            if (zabuza.hasPotionEffect(PotionEffectType.SPEED))
                                zabuza.removePotionEffect(PotionEffectType.SPEED);
                            zabuza.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
                        }
                    }, 20 * 10 * 60 + 2);
                }
            }
        }
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setSkullOwner("Tarah_")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.ZABUZA_HAKU;
    }

    @Override
    public String getRoleName() {
        return "Zabuza";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public Chakra getChakra() {
        return null;
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {
        Player player = super.getPlayer();
        Player target = PlayerManager.getPlayer();
        if (player != null && target != null) {
            if (player.getWorld() == target.getWorld()) {
                double distance = player.getLocation().distance(target.getLocation());
                if (distance <= DISTANCE) {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§a" + PlayerManager.getRole().getRoleName()
                            + " vient d'utiliser une technique !");
                }
            }
        }
    }

    @Override
    public void onDamage(Player damager, Player target) {

    }

    public boolean isCamoufled() {
        return camoufled;
    }

    public void setCamoufled(boolean camoufled) {
        this.camoufled = camoufled;
    }

    public void hidePlayer(Player player) {

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
        PacketPlayOutEntityEquipment handPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 0, null);
        PacketPlayOutEntityEquipment helmetPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 1, null);
        PacketPlayOutEntityEquipment chestPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 2, null);
        PacketPlayOutEntityEquipment legPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 3, null);
        PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, null);
        for (PlayerManager target : main.getPlayerManagersOnlines()) {
            Player reciever = target.getPlayer();
            if (reciever != null && reciever != player) {
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(handPacket);
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(helmetPacket);
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(chestPacket);
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(legPacket);
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(bootsPacket);
            }
        }
    }

    public void showPlayer(Player player) {
        PacketPlayOutEntityEquipment handPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 0, CraftItemStack.asNMSCopy(player.getItemInHand()));
        PacketPlayOutEntityEquipment helmetPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 1, CraftItemStack.asNMSCopy(player.getInventory().getHelmet()));
        PacketPlayOutEntityEquipment chestPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 2, CraftItemStack.asNMSCopy(player.getInventory().getChestplate()));
        PacketPlayOutEntityEquipment legPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 3, CraftItemStack.asNMSCopy(player.getInventory().getLeggings()));
        PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, CraftItemStack.asNMSCopy(player.getInventory().getBoots()));

        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        for (PlayerManager target : main.getPlayerManagersOnlines()) {
            Player reciever = target.getPlayer();
            if (reciever != null && reciever != player) {
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(handPacket);
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(helmetPacket);
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(chestPacket);
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(legPacket);
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(bootsPacket);
            }
        }
    }
}
