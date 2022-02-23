package fr.lastril.uhchost.modes.lg.roles.lg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.roles.LGChatRole;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.lg.roles.RealLG;
import fr.lastril.uhchost.modes.lg.roles.village.Ancien;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.clickable_messages.ClickableMessage;
import fr.lastril.uhchost.tools.API.items.PotionItem;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class InfectPereDesLoups extends Role implements LGRole, RoleListener, RealLG, LGChatRole {

    private boolean hasInfected = false, infecte;

    public InfectPereDesLoups() {
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false),
                When.NIGHT);
        super.addEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false),
                When.START);
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 1, 0, false, false), When.AT_KILL);
        super.addEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 1, 0, false, false), When.AT_KILL);
    }

    @Override
    public String getRoleName() {
        return "Infect Père des Loups";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "lg.yml");
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new PotionItem(PotionType.INSTANT_HEAL, 1, true).toItemStack(2));
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
    public void afterRoles(Player player) {
        player.sendMessage(sendList());
    }

    @Override
    public String sendList() {
        if (main.gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager) {
            LoupGarouManager loupGarouManager = (LoupGarouManager) main.gameManager.getModes().getMode().getModeManager();
            return loupGarouManager.sendLGList();
        }
        return null;
    }

    @Override
    public void checkRunnable(Player player) {
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzBmZGYyZTg4ODM5OTNiYWE4Njc1M2Y3ZTdiMTMxM2NkMmE3NjljN2VjN2JhYTY4Mjc5NDIyNzdjYjdiYWJjMCJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.LOUP_GAROU;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (main.gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager) {
            LoupGarouManager loupGarouManager = (LoupGarouManager) main.gameManager.getModes().getMode().getModeManager();
            UhcHost.debug("Checking Infect rez...");
            if (!hasInfected) {
                UhcHost.debug("Infect can rez.");
                Player killer = event.getEntity().getKiller();
                Player player = event.getEntity();
                if (killer != null) {
                    UhcHost.debug("Killer is not null.");
                    infecte = true;
                    PlayerManager killerManager = main.getPlayerManager(killer.getUniqueId());
                    PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                    Bukkit.getScheduler().runTaskLater(main, () -> infecte = false, 20*6);
                    if (killerManager.hasRole() && killerManager.getRole() instanceof RealLG) {
                        UhcHost.debug("Killer is LG.");
                        if (playerManager.hasRole() && playerManager.getRole() instanceof Ancien) {
                            Ancien ancien = (Ancien) playerManager.getRole();
                            if (!ancien.isRevived()) {
                                UhcHost.debug("Dead is Ancien and can revive");
                                return;
                            }
                        } if(playerManager.getWolfPlayerManager().isProtect()){
                            UhcHost.debug("Dead is protected by garde.");
                            return;
                        }
                        UhcHost.debug("Print infection message");
                        if (super.getPlayer() != null) {
                            Player infect = super.getPlayer();
                            PlayerManager infectManager = main.getPlayerManager(infect.getUniqueId());
                            if(infectManager.isAlive() && playerManager.getWolfPlayerManager().getResurectType() == null){
                                new ClickableMessage(infect, onClick -> {

                                    if(infecte){
                                        loupGarouManager.addInfect(playerManager, loupGarouManager.isVaccination());
                                        onClick.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aVous avez bien infecté "
                                                + player.getName() + " !");
                                        hasInfected = true;
                                        UhcHost.debug("Player is infected !");
                                        playerManager.getRole().addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false),
                                                When.NIGHT);
                                        playerManager.getRole().addEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 1, 0, false, false), When.AT_KILL);
                                        playerManager.getRole().addEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 1, 0, false, false), When.AT_KILL);
                                    } else {
                                        onClick.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous ne pouvez plus infecter"
                                                + player.getName() + " !");
                                    }

                                }, "§c" + player.getName()
                                        + " est mort vous pouvez l'infecter en cliquant sur le message ! ",
                                        "§a Pour infecter §c" + player.getName());
                            }
                        }

                    }
                }
            }
        }

    }

    @Override
    public boolean canSee() {
        return true;
    }

    @Override
    public boolean canSend() {
        return true;
    }

    @Override
    public boolean sendPlayerName() {
        return false;
    }
}
