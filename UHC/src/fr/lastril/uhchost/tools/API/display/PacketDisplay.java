package fr.lastril.uhchost.tools.API.display;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketDisplay {

    private Location loc;
    private String text;
    private boolean gravity, basePlate, invisible, customNameVisible, marker;

    private EntityArmorStand stand;

    public PacketDisplay(Location loc, String text) {
        this(loc, text, false, false, true, true, true);
    }

    public PacketDisplay(Location loc, String text, boolean gravity, boolean basePlate, boolean invisible, boolean customNameVisible, boolean marker) {
        this.loc = loc;
        this.text = text;
        this.gravity = gravity;
        this.basePlate = basePlate;
        this.invisible = invisible;
        this.customNameVisible = customNameVisible;
        this.marker = marker;
    }

    public Location getLoc() {
        return loc;
    }

    public String getText() {
        return text;
    }

    public PacketDisplay display(Player player){
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        World nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();
        stand = new EntityArmorStand(nmsWorld);
        stand.setGravity(this.gravity);
        stand.setLocation(loc.getX(), loc.getY() + (this.marker ? 2 : 0), loc.getZ(), loc.getYaw(), loc.getPitch());
        stand.setBasePlate(this.basePlate);
        stand.setInvisible(this.invisible);
        stand.setCustomName(text);
        stand.setCustomNameVisible(this.customNameVisible);
        stand.n(this.marker); //MARKER
        PacketPlayOutSpawnEntityLiving packetSpawn = new PacketPlayOutSpawnEntityLiving(stand);
        nmsPlayer.playerConnection.sendPacket(packetSpawn);
        return this;
    }

    public PacketDisplay destroy(Player player){
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        PacketPlayOutEntityDestroy packetDestroy = new PacketPlayOutEntityDestroy(stand.getId());
        nmsPlayer.playerConnection.sendPacket(packetDestroy);
        return this;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public void teleport(Location loc, Player player){
        this.setLoc(loc);
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        stand.setLocation(loc.getX(), loc.getY() + (this.marker ? 2 : 0), loc.getZ(), loc.getYaw(), loc.getPitch());
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(stand);
        nmsPlayer.playerConnection.sendPacket(packet);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void rename(String text, Player player){
        this.setText(text);
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        stand.setCustomName(text);
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), false);
        nmsPlayer.playerConnection.sendPacket(packet);
    }

    public boolean isGravity() {
        return gravity;
    }

    public void setGravity(boolean gravity) {
        this.gravity = gravity;
    }

    public boolean isBasePlate() {
        return basePlate;
    }

    public void setBasePlate(boolean basePlate) {
        this.basePlate = basePlate;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isMarker() {
        return marker;
    }

    public void setMarker(boolean marker) {
        this.marker = marker;
    }

    public EntityArmorStand getStand() {
        return stand;
    }

    public void setStand(EntityArmorStand stand) {
        this.stand = stand;
    }

    public boolean isCustomNameVisible() {
        return customNameVisible;
    }

    public void setCustomNameVisible(boolean customNameVisible, Player player) {
        this.customNameVisible = customNameVisible;
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        stand.setCustomNameVisible(customNameVisible);
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), false);
        nmsPlayer.playerConnection.sendPacket(packet);
    }
}
