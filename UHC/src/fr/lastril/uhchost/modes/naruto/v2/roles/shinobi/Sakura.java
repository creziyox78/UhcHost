package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdShosenJutsu;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.KatsuyuItem;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Sasuke;
import fr.lastril.uhchost.modes.naruto.v2.tasks.SakuraTask;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.PlayerKillEvent;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Arrays;
import java.util.List;

public class Sakura extends Role implements NarutoV2Role, CmdShosenJutsu.ShosenJutsuUser, RoleCommand, RoleListener {

    private static final double healthWhenSasukeDeath = 4D * 2D;

    private boolean knowSasuke, sasukeKill;

    public Sakura() {
    }

    @EventHandler
    public void onKill(PlayerKillEvent event){
        Player killer = event.getKiller();
        PlayerManager joueur = main.getPlayerManager(killer.getUniqueId());
        if(joueur.hasRole()){
            if(joueur.getRole() instanceof Sasuke){
                sasukeKill = true;
            }
        }
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        for(PlayerManager joueur : narutoV2Manager.getPlayerManagersWithRole(Sasuke.class)){
            if(joueur.isAlive()){
                if(joueur.getPlayer() != null){
                    Player sasuke = joueur.getPlayer();
                    if(player.getLocation().distance(sasuke.getLocation()) <= 20){
                        if(knowSasuke == false && sasukeKill){
                            knowSasuke = true;
                            super.addRoleToKnow(Sasuke.class);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§6Sasuke§e a tué quelqu'un et vous venez de le croiser. Voic son identité: " + sasuke.getName());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void giveItems(Player player) {

    }



    @Override
    protected void onNight(Player player) {

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
                if (joueur.getRole() instanceof Sasuke) {
                    sakura.setMaxHealth(sakura.getMaxHealth() - healthWhenSasukeDeath);
                    sakura.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Sasuke est mort, vous perdez donc " + (healthWhenSasukeDeath / 2) + " cœurs.");
                } else if (joueur.getRole() instanceof Tsunade) {
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
}
