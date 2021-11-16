package fr.lastril.uhchost.modes.lg.roles.solo;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Voleur extends Role implements LGRole {

    private boolean killed;

    public Voleur() {
        super.addEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    public String getSkullValue() {
        return null;
    }

    @Override
    public void giveItems(Player player) {
    }

    @Override
    public void onNight(Player player) {
    }

    @Override
    public void onDay(Player player) {
    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onKill(OfflinePlayer player, Player killer) {
        PlayerManager killerManager = main.getPlayerManager(killer.getUniqueId());
        PlayerManager playerManager = main.getPlayerManager(killer.getUniqueId());
        if (killerManager.hasRole()) {
            if (killerManager.getRole() instanceof Voleur) {
                Voleur voleur = (Voleur) killerManager.getRole();
                if (!voleur.hasKilled()) {
                    killer.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage()
                            + "§bVous venez de tuer quelqu'un pour la 1ère fois. Vous n'avez plus Résistance et héritez du rôle de la personne que vous avez tué.");
                    killerManager.setRole(playerManager.getRole());

                }
            }
        }
    }

    @Override
    public void onNewDay(Player player) {
    }

    @Override
    public String getRoleName() {
        return "Voleur";
    }

    @Override
    public String getDescription() {
        return main.getLGRoleDescription(this,this.getClass().getName());
    }

    @Override
    public QuickItem getItem() {
        return null;
        //return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName("§3"+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjZkOWRmZjBiZTkzNTlmODExODIyYmZlNGJkOGRkNDBkMDVhZmNjZGJkMTE0ZGUwODFlMWJmNDY5MzA3MWVmYiJ9fX0=");
    }

    @Override
    public void checkRunnable(Player player) {

    }

    @Override
    public Camps getCamp() {
        return Camps.NEUTRES;
    }

    public boolean hasKilled() {
        return killed;
    }

    public void setKilled(boolean killed) {
        this.killed = killed;
    }
}
