package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.modes.bleach.commands.CmdTempete;
import fr.lastril.uhchost.modes.bleach.items.SakuraItem;
import fr.lastril.uhchost.modes.bleach.items.Senbonzakura;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.Cuboid;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.WorldUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Byakuya extends Role implements RoleCommand, ShinigamiRole {

    private final List<BukkitTask> particlesTasks = new ArrayList<>();
    private final List<PlayerManager> inZone = new ArrayList<>();
    private boolean byakuyaInZone; //TODO Damage effect slow
    private boolean usedZone;
    private Location center;

    public Byakuya(){
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new SakuraItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Senbonzakura(main).toItemStack());
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
        return null;
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Byakuya";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    public void createZone(Location loc){
        Cuboid cuboid = new Cuboid(loc, 15);
        center = cuboid.getCenter();
        cuboid.expand(Cuboid.CuboidDirection.Up, 2);

        for (Block block : cuboid.getBlocks()){
            BukkitTask task = Bukkit.getScheduler().runTaskTimer(main, () ->
                    WorldUtils.spawnColoredParticle(block.getLocation(), EnumParticle.REDSTONE, 255, 192, 203),0,20);
            particlesTasks.add(task);
        }
        usedZone = true;
        BukkitTask zoneTask = Bukkit.getScheduler().runTaskTimer(main, () ->
                main.getPlayerManagerOnlines().forEach(playerManager -> {
                    Player player = playerManager.getPlayer();
                    if(playerManager.isAlive() && player != null) {
                        if(cuboid.contains(player)) {
                            if(!checkIfByakuya(playerManager)){
                                onEnterZone(playerManager);
                            } else {
                                applyBonusZone(player);
                            }
                        } else {
                            if(inZone.contains(playerManager)){
                                if(!checkIfByakuya(playerManager)){
                                    onExitZone(playerManager);
                                }
                            }
                        }
                    }
                }), 40, 40);
        particlesTasks.add(zoneTask);
    }

    public void deleteZone(){
        particlesTasks.forEach(BukkitTask::cancel);
        particlesTasks.clear();
        usedZone = false;
        byakuyaInZone = false;
        repulseAllPlayer(center);
    }

    private void repulseAllPlayer(Location location){
        ClassUtils.ripulseEntityFromLocation(location, 10, 2, 6);
    }

    private void applyBonusZone(Player player){
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*5, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*5, 0, false, false));
        byakuyaInZone = true;
    }

    private boolean checkIfByakuya(PlayerManager playerManager){
        return playerManager.hasRole() && playerManager.getRole() instanceof Byakuya;
    }

    private void onEnterZone(PlayerManager playerManager){
        inZone.add(playerManager);
        Player player = playerManager.getPlayer();
        applyMalusZone(player);
        player.sendMessage("§6Vous entrez dans la zone de Byakuya.");
    }

    private void applyMalusZone(Player player){
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*30, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20*10, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*60, 0, false, false));
    }

    private void onExitZone(PlayerManager playerManager){
        inZone.remove(playerManager);
        Player player = playerManager.getPlayer();
        applyMalusZone(player);
        player.sendMessage("§6Vous sortez de la zone de Byakuya.");
    }

    public boolean hasUsedZone(){
        return usedZone;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdTempete(main));
    }
}
