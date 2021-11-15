package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.roles.LGFacadeRole;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.clickable_messages.ClickableMessage;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Soeur extends Role implements LGRole {

    private static final PotionEffect ressistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 0, false, false);

    public Soeur() {
        super.addRoleToKnow(Soeur.class);
    }


    public String getSkullValue() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhlZGE4YjhmOTUyNzA2MzU3ZTZjMjJmMTZmYmU2NjJjZDIxMWI5NmNmZmU5ZWJiOWY3OWRmYzAyZjQwYjgifX19";
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
    public void onNewDay(Player player) {
    }

    @Override
    public String getRoleName() {
        return "Soeur";
    }

    @Override
    public String getDescription() {
        return " Vous n'avez pas de pouvoir particulier.";
    }

    @Override
    public QuickItem getItem() {
        return null;
        //return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName("§a"+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVkNTFmYjVhOTE4ZTMzYzA0YmIyMzIzNjE0N2QzNTU1OWRlMzJhNDQ4MTcwZjFhM2NjYWFlNmRjYTYzY2I2In19fQ==");
    }

    @Override
    public void checkRunnable(Player player) {
        if (main.gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager) {
            LoupGarouManager loupGarouManager = (LoupGarouManager) main.gameManager.getModes().getMode().getModeManager();
            for (PlayerManager playerManager : loupGarouManager.getPlayerManagersWithRole(Soeur.class)) {
                if (playerManager.getPlayer() != player) {
                    if (playerManager.getPlayer() != null) {
                        Player soeur = playerManager.getPlayer();
                        if (player.getLocation().distance(soeur.getLocation()) <= 20) {
                            player.addPotionEffect(ressistance);
                            soeur.addPotionEffect(ressistance);
                        }
                    }
                }
            }
        }

    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }

    @Override
    public void onPlayerDeathRealy(PlayerManager player, ItemStack[] items, ItemStack[] armors, Player killer, Location deathLocation) {
        PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(player.getUuid());
        if (PlayerManager.getRole() instanceof Soeur) {
            if (PlayerManager.getPlayer() != this.getPlayer()) {
                if (this.getPlayer() != null) {
                    Player soeur = this.getPlayer();
                    soeur.sendMessage("Votre soeur (" + player.getPlayerName() + ") est morte ! Vous pouvez décider de voir le pseudo du tueur ou son rôle.");
                    new ClickableMessage(soeur, onClick -> {
                        //VOIR PSEUDO
                        soeur.sendMessage("Le tueur de votre soeur est " + killer.getName() + " !");
                    }, "§eVoir le pseudo ");
                    new ClickableMessage(soeur, onClick -> {
                        //VOIR RÔLE
                        PlayerManager playerManager = main.getPlayerManager(killer.getUniqueId());
                        if (playerManager.getRole() instanceof LGFacadeRole) {
                            LGFacadeRole lgFacadeRole = (LGFacadeRole) playerManager.getRole();
                        }
                        soeur.sendMessage("Le rôle du tueur de votre soeur  est " + playerManager.getRole().getRoleName() + " !");
                    }, "§eVoir le rôle ");
                }
            }
        }
    }


}
