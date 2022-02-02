package fr.lastril.uhchost.modes.lg.roles.solo;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Imitateur extends Role implements LGRole {

    private boolean killed;

    public Imitateur() {

    }

    @Override
    public void afterRoles(Player player) {
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        playerManager.setCamps(Camps.ANGE);
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
        if(killed)
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (main.gameManager.episodeEvery*20)/2, 0, false, false));
    }

    @Override
    public void onPlayerDeathRealy(PlayerManager player, ItemStack[] items, ItemStack[] armors, Player killer, Location deathLocation) {
        PlayerManager killerManager = main.getPlayerManager(killer.getUniqueId());
        if (killerManager.hasRole()) {
            if (killerManager.getRole() instanceof Imitateur) {
                Imitateur imitateur = (Imitateur) killerManager.getRole();
                if (!imitateur.hasKilled()) {
                    killer.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage()
                            + "§bVous venez de tuer quelqu'un pour la 1ère fois. Vous n'avez plus Résistance et héritez du rôle de la personne que vous avez tué. §c§lCependant, vous devez toujours gagner seul !");
                    killerManager.setRole(player.getRole());
                }
            }
        }
    }

    @Override
    public void onNewDay(Player player) {
    }

    @Override
    public String getRoleName() {
        return "Imitateur";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "lg.yml");
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjUxMmE3ZWI2ZDMxNmU2NDNmMWViM2FhMDU1NDAxMDQ1ZmE3NDA4NmZiOGMzMmFiZWNhMmFhMzVhMTE3OWIifX19");
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
