package fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.MarionnettismeItem;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Sasori extends Role implements NarutoV2Role {

    private final List<UUID> revived;
    private final List<UUID> marionnetteAlive;
    private final int distance = 15;
    private final Map<UUID, PotionEffect> marionnetteEffect;
    private final List<PotionEffect> potionEffectTypes = new ArrayList<>();

    public Sasori() {
        this.marionnetteEffect = new HashMap<>();
        potionEffectTypes.add(new PotionEffect(PotionEffectType.REGENERATION, 20*3, 0, false, false));
        potionEffectTypes.add(new PotionEffect(PotionEffectType.JUMP, 20*3, 3, false, false));
        potionEffectTypes.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*3, 0, false, false));
        potionEffectTypes.add(new PotionEffect(PotionEffectType.SPEED, 20*3, 0, false, false));
        potionEffectTypes.add(new PotionEffect(PotionEffectType.SPEED, 20*3, 1, false, false));
        potionEffectTypes.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*3, 0, false, false));
        potionEffectTypes.add(new PotionEffect(PotionEffectType.HEALTH_BOOST, 20*3, 0, false, false));
        potionEffectTypes.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 20*3, 0, false, false));
        potionEffectTypes.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*3, 0, false, false));
        potionEffectTypes.add(new PotionEffect(PotionEffectType.WATER_BREATHING, 20*3, 0, false, false));
        potionEffectTypes.add(new PotionEffect(PotionEffectType.INVISIBILITY, 20*3, 0, false, false));
        super.addRoleToKnow(Deidara.class);
        this.revived = new ArrayList<>();
        this.marionnetteAlive = new ArrayList<>();

    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new MarionnettismeItem(main).toItemStack());
    }

    @Override
    public void afterRoles(Player player) {
        player.setMaxHealth(30);
        player.setHealth(player.getMaxHealth());
    }

    @Override
    public void onPlayerDeath(Player player) {
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        if(marionnetteAlive.contains(player.getUniqueId())){
            marionnetteAlive.remove(player.getUniqueId());
            for(PlayerManager joueur : narutoV2Manager.getPlayerManagersWithRole(Sasori.class)){
                if(joueur.getPlayer() != null){
                    joueur.getPlayer().setMaxHealth(joueur.getPlayer().getMaxHealth() + 2D);
                    joueur.getPlayer().setHealth(joueur.getPlayer().getHealth() + 2D);
                }
            }
        }
        if(marionnetteEffect.containsKey(player.getUniqueId()))
            removeMarionnetteEffect(player);
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        for(Entity entity : player.getNearbyEntities(distance, distance, distance)){
            if(entity instanceof Player){
                Player nearPlayer = (Player) entity;
                if(nearPlayer.getGameMode() != GameMode.SPECTATOR){
                    PlayerManager joueur = main.getPlayerManager(nearPlayer.getUniqueId());
                    if(joueur.hasRole() && joueur.isAlive()){
                        if(marionnetteEffect.containsKey(nearPlayer.getUniqueId())){
                            if(player.hasPotionEffect(marionnetteEffect.get(nearPlayer.getUniqueId()).getType()))
                                player.removePotionEffect(marionnetteEffect.get(nearPlayer.getUniqueId()).getType());
                            player.addPotionEffect(marionnetteEffect.get(nearPlayer.getUniqueId()));
                        }
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
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWE4ZTJhMDY4M2NhY2FhNTU2OGM3Yzg3MmVkZDY0YWZmNzdkYjcxYTQ0ZmZmNzA5MjlmMDY1NjRjOGVjNjYxNCJ9fX0==");
    }

    @Override
    public Camps getCamp() {
        return Camps.AKATSUKI;
    }

    @Override
    public String getRoleName() {
        return "Sasori";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
    }

    public List<UUID> getRevived() {
        return revived;
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }
    
    @Override
	public Chakra getChakra() {
		return null;
	}

    public List<UUID> getMarionnetteAlive() {
        return marionnetteAlive;
    }

    public void addMarionnetteEffect(Player player){
        int index = UhcHost.getRANDOM().nextInt(potionEffectTypes.size());
        if(index < 0)
            index = 0;
        else if(index == potionEffectTypes.size())
            index = potionEffectTypes.size() - 1;
        marionnetteEffect.put(player.getUniqueId(), potionEffectTypes.get(index));
        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Voici l'effet que vous donner à Sasori en étant proche de lui:§6 " + potionEffectTypes.get(index).getType().getName());
        if(super.getPlayer() != null){
            super.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Voici l'effet que votre marionnette vous donne en étant proche de vous:§6 " + potionEffectTypes.get(index).getType().getName() + (potionEffectTypes.get(index).getAmplifier() + 1));
        }
        potionEffectTypes.remove(index);

    }

    public void removeMarionnetteEffect(Player player){
        potionEffectTypes.add(marionnetteEffect.get(player.getUniqueId()));
        marionnetteEffect.remove(player.getUniqueId());
    }

}
