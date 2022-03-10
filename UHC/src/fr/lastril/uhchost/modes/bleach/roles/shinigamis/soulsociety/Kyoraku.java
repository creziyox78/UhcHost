package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.bleach.commands.CmdDuel;
import fr.lastril.uhchost.modes.bleach.commands.CmdNuit;
import fr.lastril.uhchost.modes.bleach.kyoraku.KyorakuDuelManager;
import fr.lastril.uhchost.modes.bleach.kyoraku.KyorakuEndDuelEvent;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Kyoraku extends Role implements RoleCommand, ShinigamiRole {

    private final KyorakuDuelManager kyorakuDuelManager;
    private boolean usedNuit;

    public Kyoraku(){
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), When.START);
        this.kyorakuDuelManager = new KyorakuDuelManager(this);
    }

    @Override
    public void giveItems(Player player) {

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
}
