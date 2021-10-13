package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.scenario.gui.DiamondLimitGui;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DiamondLimit extends Scenario {

    private static int maxDiamond = 17;

    private final Map<PlayerManager, Integer> diamondLimit = new HashMap<>();

    private final UhcHost pl = UhcHost.getInstance();

    public DiamondLimit() {
        super("DiamondLimit", Arrays.asList(I18n.tl("scenarios.diamondlimit.lore", new String[0]),
                I18n.tl("scenarios.diamondlimit.lore1", new String[0])), Material.DIAMOND_ORE, DiamondLimitGui.class);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.DIAMOND_ORE) {
            Player player = event.getPlayer();
            PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
            if(diamondLimit.containsKey(playerManager)){
                if(diamondLimit.get(playerManager) >= maxDiamond){
                    event.setCancelled(true);
                    event.getBlock().setType(Material.AIR);
                    player.sendMessage(I18n.tl("scenarios.diamondlimit.message", new String[0]));
                    event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.GOLD_ORE));
                } else {
                    diamondLimit.put(playerManager, diamondLimit.get(playerManager) + 1);
                    ActionBar.sendMessage(player, "§bDiamant minés: " + diamondLimit.get(playerManager) + "/" + maxDiamond);
                }
            } else {
                diamondLimit.put(playerManager, 1);
                ActionBar.sendMessage(player, "§bDiamant minés: " + diamondLimit.get(playerManager) + "/" + maxDiamond);
            }
        }
    }

    public static int getMaxDiamond() {
        return maxDiamond;
    }

    public static void setMaxDiamond(int maxDiamond) {
        DiamondLimit.maxDiamond = maxDiamond;
    }
}
