package fr.lastril.uhchost.modes.lg.roles.lg;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.roles.LGChatRole;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.lg.roles.RealLG;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class LoupGarouAmnesique extends Role implements LGRole, RealLG, LGChatRole {

    private final List<PlayerManager> loupGarouList = new ArrayList<>();
    private boolean damaged = false;

    public LoupGarouAmnesique() {
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.NIGHT);
        super.addEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 1, 0, false, false), When.AT_KILL);
        super.addEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 1, 0, false, false), When.AT_KILL);
    }

    @Override
    public String getRoleName() {
        return "Loup-Garou Amnésique";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this,this.getClass().getName());
    }

    @Override
    public void afterRoles(Player player) {
        if (sendList() != null)
            player.sendMessage(sendList());
    }

    @Override
    public String sendList() {
        if (damaged) {
            if (main.gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager) {
                LoupGarouManager loupGarouManager = (LoupGarouManager) main.gameManager.getModes().getMode().getModeManager();
                return loupGarouManager.sendLGList();
            }
        }
        return null;
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
        return null;
        //return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY5MjQ4NmM5ZDZlNGJiY2UzZDVlYTRiYWFmMGNmN2JiZDQ5OTQ3OWQ4ZTM5YTM1NjBiYjZjOGM4YmYxYjZkYSJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.LOUP_GAROU;
    }

    @Override
    public void onDamage(Player damager, Player target) {
        PlayerManager damagerManager = main.getPlayerManager(damager.getUniqueId());
        PlayerManager playerManager = main.getPlayerManager(target.getUniqueId());
        if (!damaged) {
            if (damagerManager.hasRole() && playerManager.hasRole()) {
                if (damagerManager.getCamps() == Camps.LOUP_GAROU && playerManager.getRole() instanceof LoupGarouAmnesique) {
                    damaged = true;
                    Bukkit.getScheduler().runTaskLater(main, () -> {
                        playerManager.getPlayer().sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cTiens tiens... Un Loup-Garou vous avez tapper précédemment. Vous venez de vous souvenir que vous êtes un Loup-Garou. Vous devez donc gagner avec ces derniers.");
                        playerManager.setCamps(Camps.LOUP_GAROU);
                    }, 20 * 5);
                }
            }
        }

    }

    public boolean isDamaged() {
        return damaged;
    }

    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    @Override
    public boolean canSee() {
        return damaged;
    }

    @Override
    public boolean canSend() {
        return damaged;
    }

    @Override
    public boolean sendPlayerName() {
        return false;
    }
}
