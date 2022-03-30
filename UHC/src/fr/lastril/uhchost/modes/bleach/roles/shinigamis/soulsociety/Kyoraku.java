package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.bleach.commands.CmdDuel;
import fr.lastril.uhchost.modes.bleach.commands.CmdNuit;
import fr.lastril.uhchost.modes.bleach.items.Kageoni;
import fr.lastril.uhchost.modes.bleach.kyoraku.KyorakuDuelManager;
import fr.lastril.uhchost.modes.bleach.kyoraku.KyorakuEndDuelEvent;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Kyoraku extends Role implements RoleCommand, ShinigamiRole, RoleListener {

    private final KyorakuDuelManager kyorakuDuelManager;
    private boolean usedNuit, inInvisible, notAttack;

    public Kyoraku(){
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), When.START);
        this.kyorakuDuelManager = new KyorakuDuelManager(this);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Kageoni(main).toItemStack());
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        if(inInvisible && !notAttack){
            ClassUtils.hidePlayerWithArmor(player, true, 2, true);
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof Kyoraku){
                bleachPlayerManager.setStrengthPourcentage(20);
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
    public void onPlayerDeath(Player player) {
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(getKyorakuDuelManager().getPlayerManager1() == playerManager){
            UhcHost.debug("Player 1" + getKyorakuDuelManager().getPlayerManager1().getPlayer());
            Bukkit.getPluginManager().callEvent(new KyorakuEndDuelEvent(kyorakuDuelManager, getKyorakuDuelManager().getPlayerManager2().getPlayer(), player));
        }
        if(getKyorakuDuelManager().getPlayerManager2() == playerManager){
            Bukkit.getPluginManager().callEvent(new KyorakuEndDuelEvent(kyorakuDuelManager, getKyorakuDuelManager().getPlayerManager1().getPlayer(), player));
        }
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
        return "Kyoraku";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdDuel(main), new CmdNuit(main));
    }

    public KyorakuDuelManager getKyorakuDuelManager() {
        return kyorakuDuelManager;
    }

    public void setUsedNuit(boolean usedNuit) {
        this.usedNuit = usedNuit;
    }

    public boolean isUsedNuit() {
        return usedNuit;
    }

    public boolean isInInvisible() {
        return inInvisible;
    }

    public void setInInvisible(boolean inInvisible) {
        this.inInvisible = inInvisible;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
            Player player = (Player) event.getEntity();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            Player damager = (Player) event.getDamager();
            PlayerManager damagerManager = main.getPlayerManager(damager.getUniqueId());
            if(damagerManager.getRole() instanceof Kyoraku){
                Kyoraku kyoraku = (Kyoraku) damagerManager.getRole();
                if(!kyoraku.notAttack && kyoraku.inInvisible){
                    kyoraku.notAttack = true;
                    kyoraku.inInvisible = false;
                    Bukkit.getScheduler().runTaskLater(main, () -> kyoraku.notAttack = false, 20*10);
                }
            }
            if(playerManager.hasRole() && playerManager.getRole() instanceof Kyoraku){
                Kyoraku kyoraku = (Kyoraku) playerManager.getRole();
                if(kyoraku.inInvisible){
                    event.setCancelled(true);
                }
            }
        }
    }
}
