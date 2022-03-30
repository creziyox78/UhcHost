package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.items.Hozukimaru;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Ikkaku extends Role implements ShinigamiRole {

    private boolean kenpachiDead, revenged;
    private int revengeTimeRemining;

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.isAlive() && playerManager.hasRole()){
            if(playerManager.getRole() instanceof Ikkaku){
                for(PlayerManager kenpachi : main.getGamemanager().getModes().getMode().getModeManager().getPlayerManagersWithRole(KenpachiZaraki.class)){
                    if(kenpachi.isAlive()){
                        Player kenpachiPlayer = kenpachi.getPlayer();
                        if(kenpachiPlayer != null){
                            if(player.getWorld() == kenpachiPlayer.getWorld()){
                                if(player.getLocation().distance(kenpachiPlayer.getLocation()) <= 30){
                                    if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                                        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*3, 0, false, false));
                                }
                            }
                        }
                    } else {
                        if(!revenged){
                            revengeTimeRemining--;
                            if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*3, 0, false, false));
                            if(revengeTimeRemining == 0){
                                main.getInventoryUtils().giveItemSafely(player, new QuickItem(Material.GOLDEN_APPLE, 3).toItemStack());
                            }
                        }
                    }
                }
            }
        }
        if(!revenged && kenpachiDead){
            if(player.hasPotionEffect(PotionEffectType.SPEED))
                player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*3, 1, false, false));
            if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*3, 1, false, false));
        }
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new QuickItem(Material.IRON_LEGGINGS).addEnchant(Enchantment.DURABILITY, 3, true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Hozukimaru(main).toItemStack());
    }

    @Override
    public void onKill(OfflinePlayer player, Player killer) {

        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.hasRole() && playerManager.getRole() instanceof KenpachiZaraki){
            if(super.getPlayer() != null){
                main.getInventoryUtils().giveItemSafely(super.getPlayer(), new QuickItem(Material.COMPASS)
                        .setLore("",
                                "§7Position du tueur de Kenpachi.",
                                "§cCliquez à nouveau pour actualiser",
                                "§cla position !")
                        .glow(true).onClick(onClick -> {
                    Player player1 = onClick.getPlayer();
                    if(player1 == super.getPlayer()){
                        if(killer != null){
                            super.getPlayer().setCompassTarget(killer.getLocation());
                            super.getPlayer().sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§aLa position à été mise à jour. Dimension du joueur : " + killer.getWorld().getName());
                        }
                    }
                }).toItemStack());
                super.getPlayer().sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§9Le tueur de Kenpachi est "+killer.getName()+", tuez-le si vous ne voulez pas devoir gagner seul.");
            }
        }
        PlayerManager killerManager = main.getPlayerManager(killer.getUniqueId());
        if(killerManager.hasRole() && killerManager.getRole() instanceof Ikkaku){
            Ikkaku ikkaku = (Ikkaku) killerManager.getRole();
            if(ikkaku.revengeTimeRemining > 0){
                ikkaku.revenged = true;
                killer.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§aVous avez réussi à venger Kenpachi !");
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
        return null;
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Ikkaku";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }
}
