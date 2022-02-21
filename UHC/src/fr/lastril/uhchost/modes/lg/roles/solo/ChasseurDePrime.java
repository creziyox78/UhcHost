package fr.lastril.uhchost.modes.lg.roles.solo;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.normal.Chat;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChasseurDePrime extends Role implements LGRole {

    private PlayerManager target;

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
        if(killer != null){
            PlayerManager killerManager = main.getPlayerManager(killer.getUniqueId());
            if(killerManager.hasRole() && killerManager.getRole() instanceof ChasseurDePrime){
                ChasseurDePrime chasseurDePrime = (ChasseurDePrime) killerManager.getRole();
                if(chasseurDePrime.target == player){
                    killer.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "");
                }
            }
        }

    }

    @Override
    public void onNewEpisode(Player player) {
        if(main.getGamemanager().episode >= 3){
            while (target == null || target == main.getPlayerManager(player.getUniqueId())){
                target = main.getRandomPlayerManagerAlive();
            }
            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§bVoici votre nouvelle cible: " + target.getPlayerName());
        }
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
}
