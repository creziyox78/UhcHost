package fr.lastril.uhchost.modes.naruto.v2.roles.zabuza;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.Camouflage;
import fr.lastril.uhchost.modes.naruto.v2.items.swords.KubikiribochoSword;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Zabuza extends Role implements NarutoV2Role, RoleListener {

    private final double DISTANCE = Integer.MAX_VALUE;
    private int chance = 10;

    private boolean camoufled;

    public Zabuza(){
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
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(joueur.hasRole() && joueur.isAlive()){
            if(joueur.getRole() instanceof Zabuza){
                Zabuza zabuza = (Zabuza) joueur.getRole();
                if(zabuza.isCamoufled()){
                    zabuza.hidePlayer(player);
                }
            }
        }
    }

    @EventHandler
    public void onDamageWithSword(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player){
            if(event.getEntity() instanceof Player){
                Player player = (Player) event.getEntity();
                PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
                if(joueur.hasRole()){
                    if(joueur.getRole() instanceof Zabuza){
                        Zabuza zabuza = (Zabuza) joueur.getRole();
                        if(zabuza.isCamoufled()){
                            joueur.setRoleCooldownCamouflage(5*60);
                            zabuza.setCamoufled(false);
                            zabuza.showPlayer(player);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous êtes§c invisible§e (même avec votre armure) aux yeux de tous maintenant.");
                        }
                    }
                }
            }
            Player damager = (Player) event.getDamager();
            PlayerManager damagerJoueur = main.getPlayerManager(damager.getUniqueId());
            if(damagerJoueur.hasRole()){
                if(damagerJoueur.getRole() instanceof Zabuza){
                    int pourcentage = UhcHost.getRANDOM().nextInt(100);
                    if(pourcentage <= chance){
                        if(damager.getHealth() + event.getFinalDamage() >= damager.getMaxHealth())
                            damager.setHealth(damager.getMaxHealth());
                        else
                            damager.setHealth(damager.getHealth() + event.getFinalDamage());
                    }
                    Zabuza zabuza = (Zabuza) damagerJoueur.getRole();
                    if(zabuza.isCamoufled()){
                        damagerJoueur.setRoleCooldownCamouflage(5*60);
                        zabuza.setCamoufled(false);
                        zabuza.showPlayer(damager);
                        damager.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous êtes§a visible§e aux yeux de tous maintenant.");
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerDeath(Player player) {
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(joueur.hasRole()){
            if(joueur.getRole() instanceof Haku){
                if(super.getPlayer() != null){
                    Player zabuza = super.getPlayer();
                    zabuza.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§3Haku§e vient de mourir. Vous obtenez§b Speed 2§e pendant 10 minutes ainsi que§f Résistance permanent§e.");
                    zabuza.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
                    if(zabuza.hasPotionEffect(PotionEffectType.SPEED))
                        zabuza.removePotionEffect(PotionEffectType.SPEED);
                    zabuza.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*60*10, 1, false, false));
                    Bukkit.getScheduler().runTaskLater(main, () -> {
                        if(zabuza != null){
                            if(zabuza.hasPotionEffect(PotionEffectType.SPEED))
                                zabuza.removePotionEffect(PotionEffectType.SPEED);
                            zabuza.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
                        }
                    }, 20*10*60 + 2);
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
    public void onPlayerUsedSpecificPower(PlayerManager joueur, String technique) {
        Player player = super.getPlayer();
        Player target = joueur.getPlayer();
        if (player != null && target != null) {
            if (player.getWorld() == target.getWorld()) {
                double distance = player.getLocation().distance(target.getLocation());
                if (distance <= DISTANCE) {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§a" + joueur.getPlayerName()
                            + " vient d'utiliser la technique§e \"" + technique +"\"§a.");
                }
            }
        }
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
        return main.getRoleDescription(this, this.getClass().getName());
    }

    @Override
    public Chakra getChakra() {
        return null;
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

    @Override
    public void onDamage(Player damager, Player target) {

    }

    public void setCamoufled(boolean camoufled) {
        this.camoufled = camoufled;
    }

    public boolean isCamoufled() {
        return camoufled;
    }

    public void hidePlayer(Player player){

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
        PacketPlayOutEntityEquipment handPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 0, null);
        PacketPlayOutEntityEquipment helmetPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 1, null);
        PacketPlayOutEntityEquipment chestPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 2, null);
        PacketPlayOutEntityEquipment legPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 3, null);
        PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, null);
        for(PlayerManager target: main.getPlayerManagerOnlines()){
            Player reciever = target.getPlayer();
            if(reciever != null && reciever != player){
                ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(handPacket);
                ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(helmetPacket);
                ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(chestPacket);
                ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(legPacket);
                ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(bootsPacket);
            }
        }
    }

    public void showPlayer(Player player){
        PacketPlayOutEntityEquipment handPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 0, CraftItemStack.asNMSCopy(player.getItemInHand()));
        PacketPlayOutEntityEquipment helmetPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 1, CraftItemStack.asNMSCopy(player.getInventory().getHelmet()));
        PacketPlayOutEntityEquipment chestPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 2, CraftItemStack.asNMSCopy(player.getInventory().getChestplate()));
        PacketPlayOutEntityEquipment legPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 3, CraftItemStack.asNMSCopy(player.getInventory().getLeggings()));
        PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, CraftItemStack.asNMSCopy(player.getInventory().getBoots()));

        if(player.hasPotionEffect(PotionEffectType.INVISIBILITY))
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        for(PlayerManager target: main.getPlayerManagerOnlines()){
            Player reciever = target.getPlayer();
            if(reciever != null && reciever != player){
                ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(handPacket);
                ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(helmetPacket);
                ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(chestPacket);
                ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(legPacket);
                ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(bootsPacket);
            }
        }
    }
}
