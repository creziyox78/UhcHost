package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;


import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.ToileItem;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Sasuke;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.UUID;

public class Sai extends Role implements NarutoV2Role, RoleListener {

    private boolean hasUsedFuinjutsu;
    private UUID scelled;
    private int horseID = -1;

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new ToileItem(main, this).toItemStack());
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
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWY2NGRhZWUzNTVkNTMxNDI0OWQyYTgzMTZmNjU5ZjEyZjU1MzdjNzdjMjE0ZDJkYzI1MDc1MmU1YWY4M2NjMyJ9fX0");
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Saï";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {
    }

    @Override
    public void onKill(OfflinePlayer player, Player killer) {
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        if(joueur.getRole() instanceof Sasuke){
            killer.setMaxHealth(killer.getMaxHealth()+(2D*2D));
            killer.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous avez tué Sasuke, par conséquant, vous obtenez 2 cœurs supplémentaires.");
            if (!narutoV2Manager.getPlayerManagersWithRole(Sakura.class).isEmpty()) {
                killer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVoici l'identité de Sakura :");
                for (PlayerManager joueurHasRole : narutoV2Manager.getPlayerManagersWithRole(Sakura.class)) {
                    killer.sendMessage("§6- " + joueurHasRole.getPlayerName());
                }
            }
        }
    }

    @EventHandler
    public void onSilverFishDamageSai(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if(event.getDamager() instanceof Silverfish){
                Silverfish silverfish = (Silverfish) event.getDamager();
                if(joueur.hasRole()){
                    if(joueur.getRole() instanceof Sai){
                        if(silverfish.getCustomName().equalsIgnoreCase("§7Poisson d'argents")){
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }


    @Override
    public void checkRunnable(Player player) {
    	super.checkRunnable(player);
    	PlayerManager joueur = main.getPlayerManager(player.getUniqueId());


        if (this.scelled != null) {
            Player scelled = Bukkit.getPlayer(this.scelled);
            if (scelled != null) {
                if(scelled.getWorld() == player.getWorld()){
                    if (player.getLocation().distance(scelled.getLocation()) > 30) {
                        scelled.teleport(player.getLocation());
                    }
                }
            }
        }
    }

    public boolean hasUsedFuinjutsu() {
        return hasUsedFuinjutsu;
    }

    public void setHasUsedFuinjutsu(boolean hasUsedFuinjutsu) {
        this.hasUsedFuinjutsu = hasUsedFuinjutsu;
    }

    public UUID getScelled() {
        return scelled;
    }

    public void setScelled(UUID scelled) {
        this.scelled = scelled;
    }

    @Override
    public void onPlayerDeath(Player player) {
        if (this.scelled != null) {
            Player scelled = Bukkit.getPlayer(this.scelled);
            if (scelled != null) {
                PlayerManager scelledJoueur = main.getPlayerManager(scelled.getUniqueId());
                scelledJoueur.setAlive(true);
                scelled.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aSaï est mort, vous êtes libéré !");
                scelled.setGameMode(GameMode.SURVIVAL);
                scelled.teleport(player.getLocation());

            }
        }
    }

    @EventHandler
    public void onPlayerMountHorse(EntityMountEvent event){
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if(event.getMount().getEntityId() == this.horseID){
                if(!(joueur.getRole() instanceof Sai)){
                    player.sendMessage(Messages.error("Vous ne pouvez pas monter le cheval de Saï !"));
                    event.setCancelled(true);
                }
            }
        }
    }

    public int getHorseID() {
        return horseID;
    }

    public void setHorseID(int horseID) {
        this.horseID = horseID;
    }

	@Override
	public Chakra getChakra() {
		return Chakra.SUITON;
	}
}
