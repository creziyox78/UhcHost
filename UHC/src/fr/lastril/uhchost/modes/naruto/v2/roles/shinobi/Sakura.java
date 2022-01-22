package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdShosenJutsu;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.KatsuyuItem;
import fr.lastril.uhchost.modes.naruto.v2.tasks.SakuraTask;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Sakura extends Role implements NarutoV2Role, CmdShosenJutsu.ShosenJutsuUser, RoleCommand, RoleListener {

    private Location userLoc, targetLoc;
    private UUID targetId;

    public Sakura() {
    }

    @Override
    public void giveItems(Player player) {

    }



    @Override
    protected void onNight(Player player) {

    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);

        if(userLoc != null){
            userLoc = player.getLocation();
            if(userLoc.getWorld() == player.getLocation())
            if(userLoc.distance(player.getLocation()) >= 0.2){
                if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
                NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
                if(narutoV2Manager.isInShosenJutsu(player.getUniqueId()))
                    narutoV2Manager.removeInShosenJutsu(player.getUniqueId());
            }
        }
        if(targetLoc != null){
            if(targetLoc.getWorld() == player.getLocation().getWorld()){
                if(targetLoc.distance(player.getLocation()) >= 0.2){
                    if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
                    NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
                    if(narutoV2Manager.isInShosenJutsu(targetId))
                        narutoV2Manager.removeInShosenJutsu(targetId);
                }
            }

        }
        if(Bukkit.getPlayer(targetId) != null){
            targetLoc = Bukkit.getPlayer(targetId).getLocation();
        }



    }

    @Override
    public void afterRoles(Player player) {
        new SakuraTask(player.getUniqueId()).runTask(main);
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjUxYjc2ZTA2MTJmMTk4NDJmN2JkM2IyMzEyMzU2Y2QxZDRkYzRkN2E0ZWY2OTM1ZTQ4MjY2YjRiMWI0NDcxNSJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Sakura";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {
    }

    @Override
    public void onPlayerDeath(Player player) {
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if (super.getPlayer() != null) {
            Player sakura = super.getPlayer();
            if (joueur.hasRole()) {
                 if (joueur.getRole() instanceof Tsunade) {
                    main.getInventoryUtils().giveItemSafely(sakura, new KatsuyuItem(main).toItemStack());
                    sakura.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Tsunade est mort, vous héritez donc de l'item Katsuyu.");
                }
            }
        }

    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdShosenJutsu(main));
    }

	@Override
	public Chakra getChakra() {
		return Chakra.DOTON;
	}

    @Override
    public UUID getTargetId() {
        return targetId;
    }

    @Override
    public void setTargetId(UUID uuid) {
        targetId = uuid;
    }
}
