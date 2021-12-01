package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.commands.CmdChoose;
import fr.lastril.uhchost.modes.lg.roles.LGChatRole;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class ChienLoup extends Role implements LGRole, RoleCommand, LGChatRole {

    private boolean choosen;
    private Camps choosenCamp;

    public ChienLoup() {
        super.addEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void afterRoles(Player player) {
        Bukkit.getScheduler().runTaskLater(main, () -> {
            if(!choosen){
                choosen = true;
                setChoosenCamp(Camps.VILLAGEOIS);
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aVous avez décidé de gagner avec les villageois. " +
                        "Vous n'avez donc aucun effet et vous apparaîtrez dans la liste des Loups-Garou.");
            }
        },20*60);
    }

    @Override
    public String getRoleName() {
        return "Chien-Loup";
    }

    @Override
    public String getDescription() {
        return main.getLGRoleDescription(this,this.getClass().getName());
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
    public void checkRunnable(Player player) {
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTU0ODFkNjEwZWMwNmE1NDM4NDBmNzZiOGNjOWRjNTFmZDgwMjAwYTVmYTg5NGZkZjRlOWUzMDE5MDRhZjQ4ZSJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.NEUTRES;
    }

    public Camps getChoosenCamp() {
        return choosenCamp;
    }

    public void setChoosenCamp(Camps choosen) {
        if(this.player != null){
            main.getPlayerManager(this.player).setCamps(choosen);
        }
        this.choosenCamp = choosen;
    }

    public boolean isChoosen() {
        return choosen;
    }

    public void setChoosen(boolean choosen) {
        this.choosen = choosen;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdChoose(main));
    }

    @Override
    public boolean canSee() {
        return false;
    }

    @Override
    public boolean canSend() {
        return false;
    }

    @Override
    public boolean sendPlayerName() {
        return false;
    }
}
