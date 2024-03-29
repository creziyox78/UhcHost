package fr.lastril.uhchost.modes.bleach.roles.arrancars.fraccions;

import fr.lastril.uhchost.modes.bleach.ceros.AbstractCero;
import fr.lastril.uhchost.modes.bleach.ceros.CeroType;
import fr.lastril.uhchost.modes.bleach.roles.ArrancarRole;
import fr.lastril.uhchost.modes.bleach.roles.CeroUser;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.entity.Player;

import java.util.List;

public class Vega extends Role implements ArrancarRole, CeroUser {
    @Override
    public int getNbQuartz() {
        return 0;
    }

    @Override
    public void onTransformationFirst() {

    }

    @Override
    public void onUnTransformationFirst() {

    }

    @Override
    public boolean canUseCero(CeroType ceroType) {
        return false;
    }

    @Override
    public void onUseCero(CeroType ceroType) {

    }

    @Override
    public int getCeroRedValue() {
        return 0;
    }

    @Override
    public int getCeroGreenValue() {
        return 0;
    }

    @Override
    public int getCeroBlueValue() {
        return 0;
    }

    @Override
    public List<AbstractCero> getCero() {
        return null;
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
    public QuickItem getItem() {
        return null;
    }

    @Override
    public Camps getCamp() {
        return Camps.ARRANCARS;
    }

    @Override
    public String getRoleName() {
        return "Vega";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }
}
