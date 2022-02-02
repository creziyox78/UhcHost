package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import com.sun.xml.internal.bind.v2.model.annotation.Quick;
import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.bleach.items.sword.Wabisuke;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class Kira extends Role implements RoleListener, ShinigamiRole {

    private final Map<PlayerManager, Integer> effectMap = new HashMap<>();
    private final Map<PlayerManager, Integer> cooldownDamages = new HashMap<>();

    @Override
    public void afterRoles(Player player) {
        Bukkit.getOnlinePlayers().forEach(target -> Bukkit.getScheduler().runTaskTimer(main, () -> {
            PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
            if(cooldownDamages.containsKey(targetManager)){
                UhcHost.debug("Cooldown damages for " + targetManager.getPlayerName() + ": " + cooldownDamages.get(targetManager));
                cooldownDamages.put(targetManager, cooldownDamages.get(targetManager) - 1);
                if(cooldownDamages.get(targetManager) <= 0){
                    effectMap.put(targetManager, 0);
                }
            } else {
                cooldownDamages.put(targetManager, 5);
            }
        }, 20L, 20L));

    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Wabisuke(main).toItemStack());
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                .setName(getCamp().getCompoColor() + getRoleName())
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjdiZmVjZTUwZjRiZjIzYTA0ZjNiNWYxNDFhNTI4MmUxMWVlZjZiNmY2Yjk1MGY3NjMwNjU1MjkwODA3ZTU1NiJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Kira";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @Override
    public void onDamage(Player damager, Player target) {
        PlayerManager damagerManager = main.getPlayerManager(damager.getUniqueId());
        PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
        if(damagerManager.getRole() instanceof Kira){
            Kira kira = (Kira) damagerManager.getRole();
            if(!kira.effectMap.containsKey(targetManager)){
                kira.effectMap.put(targetManager, 1);
            }
            UhcHost.debug("Hit on player " + targetManager.getPlayerName() + ": " + effectMap.get(targetManager));
            kira.effectMap.put(targetManager, effectMap.get(targetManager) + 1);
            kira.cooldownDamages.put(targetManager, 5);
            if(effectMap.get(targetManager) == 5){
                target.sendMessage("§6Vous avez été touché par l’épée de Kira \"§eWabisuke\"");
                damager.sendMessage("§6Vous donnez au joueur "+target.getName()+" l’effet Slowness 1.");
                if(target.hasPotionEffect(PotionEffectType.SLOW))
                    target.removePotionEffect(PotionEffectType.SLOW);
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 0, false, false));
            } else if(effectMap.get(targetManager) < 10 && effectMap.get(targetManager) > 5){
                if(target.hasPotionEffect(PotionEffectType.SLOW))
                    target.removePotionEffect(PotionEffectType.SLOW);
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 0, false, false));
            } else if(effectMap.get(targetManager) == 10){
                target.sendMessage("§6Vous avez été touché par l’épée de Kira");
                damager.sendMessage("§6Vous donnez au joueur "+target.getName()+" l’effet Slowness 2.");
                if(target.hasPotionEffect(PotionEffectType.SLOW))
                    target.removePotionEffect(PotionEffectType.SLOW);
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 1, false, false));
            } else if(effectMap.get(targetManager) >= 11){
                if(target.hasPotionEffect(PotionEffectType.SLOW))
                    target.removePotionEffect(PotionEffectType.SLOW);
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 1, false, false));
            }
        }
    }
}
