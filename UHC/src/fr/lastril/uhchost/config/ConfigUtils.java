package fr.lastril.uhchost.config;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.rules.world.LootsRules;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigUtils {

    public UhcHost pl;

    public ConfigUtils(UhcHost pl) {
        this.pl = pl;
    }

    public void saveConfig(PlayerConfig hostConfig){
        FileConfiguration config = this.pl.getConfig();
        String path = "hostconfigs." + hostConfig.getConfigName() + ".";
        //config.set(path + "teamsOf", Integer.valueOf(hostConfig.getTeamOf()));
        config.set(path + "maxPlayers", hostConfig.getSlot());
        config.set(path + "pvptime", hostConfig.getPvpTime());
        config.set(path + "bordertime", (int) hostConfig.getBorderTime());
        config.set(path + "borderspeed", (int) hostConfig.getBorderSpeed());
        config.set(path + "startbordersize", (int) hostConfig.getStartBorderSize());
        config.set(path + "finalbordersize", (int) hostConfig.getFinalBorderSize());
        config.set(path + "notchApple", hostConfig.isNotchApple());
        config.set(path + "allPotionsEnable", hostConfig.isAllPotionsEnable());
        config.set(path + "nether", hostConfig.isNether());
        //config.set(path + "netherEndTime", Integer.valueOf(hostConfig.getNetherEndTime()));
        //config.set(path + "fightTelportation", Boolean.valueOf(hostConfig.isTeleport()));
        //config.set(path + "fightTelportationTime", Integer.valueOf(hostConfig.getTeleportTime()));
        path = "hostconfigs." + hostConfig.getConfigName() + ".inventory.";
        ConfigurationSection inventory = config.createSection("hostconfigs." + hostConfig.getConfigName() + ".inventory");
        for (ItemStack itemStacks : hostConfig.getInventory()) {
            ConfigurationSection section;
            if (itemStacks == null)
                continue;
            int exists = 0;
            for (String s : inventory.getKeys(false)) {
                if (s.contains(itemStacks.getType().toString()))
                    exists++;
            }
            if (exists == 0) {
                section = inventory.createSection(itemStacks.getType().toString());
            } else {
                section = inventory.createSection(itemStacks.getType().toString() + exists);
            }
            if (itemStacks.getItemMeta().getDisplayName() != null)
                section.set(".name", itemStacks.getItemMeta().getDisplayName());
            if (itemStacks.getItemMeta().getLore() != null)
                section.set(".lores", itemStacks.getItemMeta().getLore());
            section.set(".amount", itemStacks.getAmount());
            if (itemStacks.getDurability() != 0)
                section.set(".data", itemStacks.getDurability());
            if (itemStacks.getEnchantments() != null)
                for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry : itemStacks.getEnchantments().entrySet())
                    section.set(".enchantements." + enchantmentIntegerEntry.getKey().getName(), enchantmentIntegerEntry.getValue());
        }
        List<String> scenarios = new ArrayList<>();
        hostConfig.getScenarios().forEach(s -> scenarios.add(s.getName()));
        path = "hostconfigs." + hostConfig.getConfigName() + ".scenarios";
        config.set(path, scenarios);
        config.createSection("hostconfigs." + hostConfig.getConfigName() + ".lootsrules");
        config.set("hostconfigs." + hostConfig.getConfigName() + ".lootsrules.apple", LootsRules.getInstance().getLoot(Material.APPLE));
        config.set("hostconfigs." + hostConfig.getConfigName() + ".lootsrules.feather", LootsRules.getInstance().getLoot(Material.FEATHER));
        config.set("hostconfigs." + hostConfig.getConfigName() + ".lootsrules.flint", LootsRules.getInstance().getLoot(Material.FLINT));
        config.set("hostconfigs." + hostConfig.getConfigName() + ".lootsrules.string", LootsRules.getInstance().getLoot(Material.STRING));
        List<String> deniedPotions = new ArrayList<>();
        this.pl.gameManager.getDeniedPotions().forEach(potion -> deniedPotions.add(potion.getType() + "," + potion.getLevel() + "," + potion.isSplash()));
        config.set("hostconfigs." + hostConfig.getConfigName() + ".deniedPotions", deniedPotions);
        this.pl.saveConfig();
    }

}
