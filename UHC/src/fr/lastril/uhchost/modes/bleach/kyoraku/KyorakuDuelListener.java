package fr.lastril.uhchost.modes.bleach.kyoraku;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class KyorakuDuelListener implements Listener {

    private final KyorakuDuelManager kyorakuDuelManager;
    private final UhcHost main;
    private Player player1, player2;
    private final Map<Player, Collection<PotionEffect>> playerEffectMap = new HashMap<>();

    public KyorakuDuelListener(KyorakuDuelManager kyorakuDuelManager){
        this.kyorakuDuelManager = kyorakuDuelManager;
        this.main = UhcHost.getInstance();
    }

    @EventHandler
    public void onStartDuelKyoraku(KyorakuStartDuelEvent event){
        if(kyorakuDuelManager.getRulesFight() == KyorakuDuelManager.RulesFight.RULES_FIGHT_2){
            playerEffectMap.put(player1, player1.getActivePotionEffects());
            playerEffectMap.put(player2, player2.getActivePotionEffects());
            player1.getActivePotionEffects().forEach(potionEffect -> player1.removePotionEffect(potionEffect.getType()));
            player2.getActivePotionEffects().forEach(potionEffect -> player2.removePotionEffect(potionEffect.getType()));
        } else if(kyorakuDuelManager.getRulesFight() == KyorakuDuelManager.RulesFight.RULES_FIGHT_3){

        }
    }

    @EventHandler
    public void onEndDuelKyoraku(KyorakuEndDuelEvent event){
        if(kyorakuDuelManager.getRulesFight() == KyorakuDuelManager.RulesFight.RULES_FIGHT_2){
            playerEffectMap.get(event.getWinner()).forEach(potionEffect -> event.getWinner().addPotionEffect(potionEffect));
            event.getWinner().sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§aVous avez remporté le duel ! Vous récupéré vos effets !");
        }
        Bukkit.getScheduler().runTaskLater(main, () -> {
            kyorakuDuelManager.teleportWinnerInPreviousLocation(event.getWinner());
            kyorakuDuelManager.deleteArena(kyorakuDuelManager.getArena().getCenter());
            HandlerList.unregisterAll(this);
        }, 20*10);
        event.getWinner().sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§aVous aller être re-téléporté à votre position dans 10 secondes.");
    }


    @EventHandler
    public void onEatApple(PlayerItemConsumeEvent event){
        Player player = event.getPlayer();
        if(event.getItem().getType() == Material.GOLDEN_APPLE) {
            if(kyorakuDuelManager.getRulesFight() == KyorakuDuelManager.RulesFight.RULES_FIGHT_1){
                if(player == player1 || player == player2){
                    event.getPlayer().sendMessage("§cLa règle de ce combat vous interdit de manger des pommes en or !");
                    event.setCancelled(true);
                }
            }
        }
    }


    @EventHandler
    public void onBreakBlock(BlockBreakEvent event){
        Block block = event.getBlock();
        if(kyorakuDuelManager.getArena().contains(block) && block.getType() == Material.STAINED_GLASS){
            event.setCancelled(true);
        }
    }

}
