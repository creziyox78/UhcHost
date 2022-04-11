package fr.lastril.uhchost.modes.bleach.roles.shinigamis;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.commands.CmdMugetsu;
import fr.lastril.uhchost.modes.bleach.items.GetsugaTensho;
import fr.lastril.uhchost.modes.bleach.items.HollowMask;
import fr.lastril.uhchost.modes.bleach.items.Vasto;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IchigoKurosaki extends Role implements ShinigamiRole, RoleCommand {

    private int getsugaCharge = 0, ticks = 7;
    private boolean inMask, white, usedMugetsu;

    public IchigoKurosaki(){
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new GetsugaTensho(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new HollowMask(main).toItemStack());
        if(isWhite()){
            main.getInventoryUtils().giveItemSafely(player, new Vasto(main).toItemStack());
        }
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        ticks--;
        if(ticks == 0){
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof IchigoKurosaki){
                if(bleachPlayerManager.canUsePower()){
                    if(isChargingGetsuga(player)){
                        if(playerManager.getRoleCooldownGetsugaTensho() <= 0){
                            getsugaCharge++;
                        }
                        ticks = 7;
                        if(getsugaCharge == 2){
                            effectGetsuga(player);
                            playerManager.setRoleCooldownGetsugaTensho(150);
                        }
                    }
                }
            }
        }
    }

    private boolean isChargingGetsuga(Player player){
        return player.getItemInHand().isSimilar(new GetsugaTensho(main).toItemStack()) && player.isBlocking();
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                .setName(getCamp().getCompoColor() + getRoleName())
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDUzYjRmZjMyZTRkOTEyYWQ1ODk1YjZjMzdhMzUyZjYxYWY5ZTQxZDI0N2E4NzliNWY0OWE2MzUyZmM4NiJ9fX0=");

    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Ichigo Kurosaki";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    public void effectGetsuga(Player player){
        int radius = 1;
        Location initialLocation = player.getLocation().clone();
        initialLocation.setPitch(0.0f);
        if(inMask)
            radius = 2;
        Vector direction = initialLocation.getDirection();

        List<List<Location>> shape = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            List<Location> line = new ArrayList<>();

            Vector front = direction.clone().multiply(i);

            line.add(initialLocation.clone().add(front));
            for (int j = 0; j <= radius; j++) {
                Vector right = ClassUtils.getRightHeadDirection(player).multiply(j), left = ClassUtils.getLeftHeadDirection(player).multiply(j);


                line.add(initialLocation.clone().add(front.clone().add(right)));
                line.add(initialLocation.clone().add(front.clone().add(left)));
            }
            shape.add(line);
        }


        Wave wave = new Wave(main, initialLocation.toVector(), shape);
    }

    public void setGetsugaCharge(int getsugaCharge) {
        this.getsugaCharge = getsugaCharge;
    }

    public int getGetsugaCharge() {
        return getsugaCharge;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdMugetsu(main));
    }


    private class Wave extends BukkitRunnable {

        private final Plugin plugin;
        private final Vector origin;
        private final List<List<Location>> shape;
        private int index;

        public Wave(Plugin plugin, Vector origin, List<List<Location>> shape) {
            this.plugin = plugin;
            this.origin = origin;
            this.shape = shape;
            this.start(2);
        }

        /**
         * Starts The Timer
         *
         * @param delay
         */
        private void start(int delay) {
            super.runTaskTimer(this.plugin, 0, delay);
        }

        /**
         * Stops The Timer
         */
        protected void stop() {
            cancel();
        }

        @Override
        public void run() {
            if(index >= shape.size()){
                cancel();
                return;
            }
            for (Location loc : shape.get(index)) {
                FallingBlock fb = loc.getWorld().spawnFallingBlock(loc, loc.getBlock().getType(), loc.getBlock().getData());
                fb.setVelocity(new Vector(0, .3, 0));
                loc.getBlock().setType(Material.AIR);
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if(WorldUtils.getDistanceBetweenTwoLocations(players.getLocation(), loc) <= 1){
                        if(isInMask()){
                            players.damage(2D*4D);
                            ClassUtils.ripulseSpecificEntityFromLocation(players, loc, 3, 2);
                        } else {
                            players.damage(2D*2D);
                            ClassUtils.ripulseSpecificEntityFromLocation(players, loc, 3, 1);
                        }
                        players.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§6Vous avez été touchés par un “Getsuga Tenshou” !");

                    }
                }
            }
            index++;
        }
    }

    public void setInMask(boolean inMask) {
        this.inMask = inMask;
    }

    public boolean isInMask() {
        return inMask;
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }

    public boolean hasUsedMugetsu() {
        return usedMugetsu;
    }

    public void setUsedMugetsu(boolean usedMugetsu) {
        this.usedMugetsu = usedMugetsu;
    }
}
