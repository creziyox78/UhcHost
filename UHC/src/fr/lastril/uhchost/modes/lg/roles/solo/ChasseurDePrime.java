package fr.lastril.uhchost.modes.lg.roles.solo;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.enums.WorldState;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.normal.Chat;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ChasseurDePrime extends Role implements LGRole {

    private PlayerManager target;
    private boolean killedTarget;

    @Override
    public void giveItems(Player player) {

    }

    @Override
    public void afterRoles(Player player) {
        main.getPlayerManager(player.getUniqueId()).setCamps(Camps.CHASSEUR_DE_PRIME);
    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    protected void onDay(Player player) {
    }

    @Override
    public void onPlayerDeathRealy(PlayerManager player, ItemStack[] items, ItemStack[] armors, Player killer, Location deathLocation) {
        UhcHost.debug("§9Checking \"chasseur de prime\" kill...");
        if(target == player){
            UhcHost.debug("§9The player is \"chasseur de prime\" target");
            if(killer != null){
                PlayerManager killerManager = main.getPlayerManager(killer.getUniqueId());
                if(killerManager.hasRole() && killerManager.getRole() instanceof ChasseurDePrime){
                    UhcHost.debug("§9The killer is \"chasseur de prime\". Giving effect...");
                    super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.DAY);
                    if(WorldState.isWorldState(WorldState.JOUR))
                        super.day(killer);
                    setKilledTarget(true);
                    killer.setMaxHealth(killer.getMaxHealth() + 4D);
                    killer.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aVous venez de tuer votre cible. Vous recevez l'effet§c Force 1§e le jour§a et 2 coeurs permanents supplémentaire.");
                    UhcHost.debug("§9Terminated \"chasseur de prime\".");
                    return;
                }
            }
            Player chasseur = super.getPlayer();
            UhcHost.debug("§9Killer is not \"chasseur de prime\". Sending new target..");
            while (target == null || target == main.getPlayerManager(getPlayerId()) || target == player){
                target = main.getRandomPlayerManagerAlive();
            }
            UhcHost.debug("§9New target is " + target.getPlayerName());
            if(chasseur != null){
                chasseur.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§bVoici votre nouvelle cible: " + target.getPlayerName());
            }
        }
    }

    @Override
    public void onNewEpisode(Player player) {
        if(main.getGamemanager().episode == 3){
            while (target == null || target == main.getPlayerManager(player.getUniqueId())){
                target = main.getRandomPlayerManagerAlive();
            }
            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§bVoici votre nouvelle cible: " + target.getPlayerName());
        }
        if(main.getGamemanager().episode == 5){
            if(!isKilledTarget()){
                Bukkit.broadcastMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§3Le§9 Chasseur de prime§3 qui est " + player.getName() + ", n'as pas réussi sa mission.");
            }
        }
    }

    @Override
    public void onNewDay(Player player) {

    }

    @Override
    public String sendList() {
        if(target != null){
            return Messages.LOUP_GAROU_PREFIX.getMessage() + "§bVotre cible: " + target.getPlayerName();
        } else {
            if(super.getPlayerId() != null){
                while (target == null || target == main.getPlayerManager(super.getPlayerId())){
                    target = main.getRandomPlayerManagerAlive();
                }
            }
            if(target != null){
                return Messages.LOUP_GAROU_PREFIX.getMessage() + "§bVotre cible: " + target.getPlayerName();
            }
        }
        return Messages.LOUP_GAROU_PREFIX.getMessage() + "§bVotre cible:§cPersonne ";
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGY2NGJjOTI3ZDVhN2NjNGNlMzM0NTU5OTNmM2Y5MzY3YTBjYmZlZTU4MjJkNTUyOWYyZDg0NzU0MTFmMyJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.NEUTRES;
    }

    @Override
    public String getRoleName() {
        return "Chasseur de Prime";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "lg.yml");
    }

    public void setKilledTarget(boolean killedTarget) {
        this.killedTarget = killedTarget;
    }

    public boolean isKilledTarget() {
        return killedTarget;
    }
}
