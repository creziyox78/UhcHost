package fr.lastril.uhchost.modes.naruto.v2.roles.zabuza;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.HyotonItem;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.clickable_messages.ClickableMessage;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Haku extends Role implements NarutoV2Role, RoleListener {

    private boolean teleportedZabuza;

    private int phase = 1;

    private Location loc;

    private final List<Location> iceUnbreakabke = new ArrayList<>();

    public Haku(){
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addRoleToKnow(Zabuza.class);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new HyotonItem(main).toItemStack());
    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        if(phase == 2){
            if(player.getWorld() == loc.getWorld()){
                if(player.getLocation().distance(loc) < 18){
                    if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                        player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    if(player.hasPotionEffect(PotionEffectType.SPEED))
                        player.removePotionEffect(PotionEffectType.SPEED);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*3, 1, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*3, 0, false, false));
                }
            }

        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        }
    }

    @Override
    public void onDamage(Player damager, Player target) {
        if(!teleportedZabuza){
            PlayerManager joueur = main.getPlayerManager(target.getUniqueId());
            if(joueur.hasRole()){
                if(joueur.getRole() instanceof Zabuza){
                    if(target.getHealth() <= 4*2D){
                        if(super.getPlayer()!=null){
                            new ClickableMessage(super.getPlayer(), onClickMessage -> {
                                if(target!=null && joueur.isAlive()){
                                    super.getPlayer().teleport(target.getLocation());
                                    super.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                                    teleportedZabuza = true;
                                }
                            }, Messages.NARUTO_PREFIX.getMessage() + "§3Zabuza§c est proche de la mort !\n" + Messages.CLICK_HERE.getMessage());
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamageNaturally(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player target = (Player) event.getEntity();
            if(!teleportedZabuza){
                PlayerManager joueur = main.getPlayerManager(target.getUniqueId());
                if(joueur.hasRole()){
                    if(joueur.getRole() instanceof Zabuza){
                        if(target.getHealth() <= 4*2D){
                            if(super.getPlayer()!=null){
                                new ClickableMessage(super.getPlayer(), onClickMessage -> {
                                    if(target!=null && joueur.isAlive()){
                                        super.getPlayer().teleport(target.getLocation());
                                        super.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                                        teleportedZabuza = true;
                                    }
                                }, Messages.NARUTO_PREFIX.getMessage() + "§3Zabuza§c est proche de la mort !\n" + Messages.CLICK_HERE.getMessage());
                            }

                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamageWithArrow(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player){
            if(event.getDamager() instanceof Arrow){
                Player target = (Player) event.getEntity();
                if(!teleportedZabuza){
                    PlayerManager joueur = main.getPlayerManager(target.getUniqueId());
                    if(joueur.hasRole()){
                        if(joueur.getRole() instanceof Zabuza){
                            if(target.getHealth() <= 4*2D){
                                if(super.getPlayer()!=null){
                                    new ClickableMessage(super.getPlayer(), onClickMessage -> {
                                        if(target!=null && joueur.isAlive()){
                                            super.getPlayer().teleport(target.getLocation());
                                            super.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                                            teleportedZabuza = true;
                                        }
                                    }, Messages.NARUTO_PREFIX.getMessage() + "§3Zabuza§c est proche de la mort !\n" + Messages.CLICK_HERE.getMessage());
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(joueur.hasRole()){
            if(joueur.getRole() instanceof Haku){
                if(phase == 2){
                    if(player.getItemInHand() != null){
                        if(player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasDisplayName()){
                            if(player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§bHyôton")){
                                if(player.getLocation().distance(loc) < 18){
                                    Block target = player.getTargetBlock((Set<Material>)null, 50);
                                    float Yaw = player.getLocation().getYaw();
                                    float Pitch = player.getLocation().getPitch();
                                    Block real = getNearestEmptySpace(target, 2);
                                    if(real != null){
                                        Location loc = real.getLocation();
                                        if(target.getType() != Material.PACKED_ICE)
                                            loc.setY(loc.getY()+1);
                                        loc.setPitch(Pitch);
                                        loc.setYaw(Yaw);
                                        player.teleport(loc);
                                    }
                                } else {
                                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes trop loin du centre de votre sphère.");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public Block getNearestEmptySpace(Block b, int maxradius) {
        BlockFace[] faces = {BlockFace.UP, BlockFace.NORTH, BlockFace.EAST};
        BlockFace[][] orth = {{BlockFace.NORTH, BlockFace.EAST}, {BlockFace.UP, BlockFace.EAST}, {BlockFace.NORTH, BlockFace.UP}};
        for (int r = 0; r <= maxradius; r++) {
            for (int s = 0; s < 6; s++) {
                BlockFace f = faces[s%3];
                BlockFace[] o = orth[s%3];
                if (s >= 3)
                    f = f.getOppositeFace();
                Block c = b.getRelative(f, r);
                for (int x = -r; x <= r; x++) {
                    for (int y = -r; y <= r; y++) {
                        Block a = c.getRelative(o[0], x).getRelative(o[1], y);
                        if (a.getTypeId() == 0 && a.getRelative(BlockFace.UP).getTypeId() == 0)
                            return a;
                    }
                }
            }
        }
        return null;// no empty space within a cube of (2*(maxradius+1))^3
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if(iceUnbreakabke.contains(event.getBlock().getLocation()) && event.getBlock().getType() == Material.PACKED_ICE){
            event.setCancelled(true);
        }
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
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTgyNjFjMTg0MTNhNmE2ZTk0NzMwNmU3NWQ5NWNjMzY2MDlhZjQyNTFjN2Q5N2Q5NmI5N2RkNWFjOTRmMjdmMiJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.ZABUZA_HAKU;
    }

    @Override
    public String getRoleName() {
        return "Haku";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "naruto.yml");
    }

    @Override
    public Chakra getChakra() {
        return null;
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

    public void resetPhase(){
        phase = 1;
    }

    public void addPhase(){
        phase++;
    }

    public int getPhase() {
        return phase;
    }

    public List<Location> getIceUnbreakabke() {
        return iceUnbreakabke;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public Set<Location> sphere(Location location, int radius, boolean hollow){
        Set<Location> blocks = new HashSet<>();
        World world = location.getWorld();
        int X = location.getBlockX();
        int Y = location.getBlockY();
        int Z = location.getBlockZ();
        int radiusSquared = radius * radius;
        if(hollow){
            for (int x = X - radius; x <= X + radius; x++) {
                for (int y = Y - radius; y <= Y + radius; y++) {
                    for (int z = Z - radius; z <= Z + radius; z++) {
                        if ((X - x) * (X - x) + (Y - y) * (Y - y) + (Z - z) * (Z - z) <= radiusSquared) {
                            Location block = new Location(world, x, y, z);
                            blocks.add(block);
                        }
                    }
                }
            }
            return makeHollow(blocks, true);
        } else {
            for (int x = X - radius; x <= X + radius; x++) {
                for (int y = Y - radius; y <= Y + radius; y++) {
                    for (int z = Z - radius; z <= Z + radius; z++) {
                        if ((X - x) * (X - x) + (Y - y) * (Y - y) + (Z - z) * (Z - z) <= radiusSquared) {
                            Location block = new Location(world, x, y, z);
                            blocks.add(block);
                        }
                    }
                }
            }
            return blocks;
        }
    }

    private Set<Location> makeHollow(Set<Location> blocks, boolean sphere){
        Set<Location> edge = new HashSet<>();
        if(!sphere){
            for(Location l : blocks){
                World w = l.getWorld();
                int X = l.getBlockX();
                int Y = l.getBlockY();
                int Z = l.getBlockZ();
                Location front = new Location(w, X + 1, Y, Z);
                Location back = new Location(w, X - 1, Y, Z);
                Location left = new Location(w, X, Y, Z + 1);
                Location right = new Location(w, X, Y, Z - 1);
                if(!(blocks.contains(front) && blocks.contains(back) && blocks.contains(left) && blocks.contains(right))){
                    edge.add(l);
                }
            }
            return edge;
        } else {
            for(Location l : blocks){
                World w = l.getWorld();
                int X = l.getBlockX();
                int Y = l.getBlockY();
                int Z = l.getBlockZ();
                Location front = new Location(w, X + 1, Y, Z);
                Location back = new Location(w, X - 1, Y, Z);
                Location left = new Location(w, X, Y, Z + 1);
                Location right = new Location(w, X, Y, Z - 1);
                Location top = new Location(w, X, Y + 1, Z);
                Location bottom = new Location(w, X, Y - 1, Z);
                if(!(blocks.contains(front) && blocks.contains(back) && blocks.contains(left) && blocks.contains(right) && blocks.contains(top) && blocks.contains(bottom))){
                    edge.add(l);
                }
            }
            return edge;
        }
    }


}
