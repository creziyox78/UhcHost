package fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru;

import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.BarriereItem;
import fr.lastril.uhchost.modes.naruto.v2.items.MarqueMauditeSakonUkonItem;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Sakon extends Role implements NarutoV2Role {

    public Sakon() {
        super.addRoleToKnow(Orochimaru.class);
        super.addRoleToKnow(Ukon.class);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new MarqueMauditeSakonUkonItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new BarriereItem(main).toItemStack());
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if (joueur.isAlive()) {
            for (PlayerManager targetJoueur : narutoV2Manager.getPlayerManagersWithRole(Ukon.class)) {
                if (targetJoueur.isAlive()) {
                    Player target = targetJoueur.getPlayer();
                    if (target != null) {
                        if(player.getWorld() == target.getWorld()){
                            if (player.getLocation().distance(target.getLocation()) <= 15) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 4, 0, false, false));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerDeath(Player player) {
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        for (PlayerManager targetJoueur : narutoV2Manager.getPlayerManagersWithRole(Ukon.class)) {
            if (targetJoueur.isAlive()) {
                Player target = targetJoueur.getPlayer();
                if (target != null) {
                    if (player.getLocation().distance(target.getLocation()) <= 15) {
                        target.damage(100);
                    }
                }
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
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName())
                .setTexture(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU0M2ZjYTExNjYzMzAwOGY0NjMyYWEyYzgzMDVjNjM5YTBjYzY1YmU2OGIxZTAyMzFjNTE0YzRmMTM0Nzg4NiJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.OROCHIMARU;
    }

    @Override
    public String getRoleName() {
        return "Sakon";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

    @Override
    public Chakra getChakra() {
        return null;
    }
}
