package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
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

public class EnfantSauvage extends Role implements LGRole {

    private PlayerManager modele;

    @Override
    public void giveItems(Player player) {

    }

    @Override
    public String sendList() {
        return "§eVotre modèle : " + modele.getPlayerName();
    }

    @Override
    public void afterRoles(Player player) {
        while (modele == null || modele == main.getPlayerManager(player.getUniqueId())){
            modele = main.getRandomPlayerManagerAlive();
        }
        player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§bVoici votre modèle qui a été choisi aléatoirement: " + modele.getPlayerName());
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
        return "Enfant Sauvage";
    }

    @Override
    public String getDescription() {
        return main.getLGRoleDescription(this,this.getClass().getName());
    }


    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDFiODMwZWI0MDgyYWNlYzgzNmJjODM1ZTQwYTExMjgyYmI1MTE5MzMxNWY5MTE4NDMzN2U4ZDM1NTU1ODMifX19");
    }

    @Override
    public void checkRunnable(Player player) {

    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }

    public String getSkullValue() {
        return null;
    }

    @Override
    public void onPlayerDeathRealy(PlayerManager player, ItemStack[] items, ItemStack[] armors, Player killer, Location deathLocation) {
        if (player == modele) {
            PlayerManager enfant = UhcHost.getInstance().getPlayerManager(getPlayerId());
            enfant.getWolfPlayerManager().setCamp(Camps.LOUP_GAROU);
            enfant.getWolfPlayerManager().setTransformed(true);
            if (super.getPlayer() != null) {
                super.getPlayer().sendMessage("Votre modèle est mort, vous êtes donc transformé et devez gagner avec les Loups-Garous.");
                LoupGarouManager loupGarouManager = (LoupGarouManager) main.gameManager.getModes().getMode().getModeManager();
                loupGarouManager.sendNewLG();
            }
        }
    }
}
