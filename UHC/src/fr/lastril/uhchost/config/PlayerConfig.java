package fr.lastril.uhchost.config;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.config.modes.NarutoV2Config;
import fr.lastril.uhchost.enums.BiomeState;
import fr.lastril.uhchost.game.rules.EnchantmentRules;
import fr.lastril.uhchost.game.rules.StuffRules;
import fr.lastril.uhchost.game.rules.world.BlocsRules;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.scenario.Scenarios;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlayerConfig {

    private UhcHost pl;

    private UUID owner;

    private static int idConfig = 0;

    private String configName;

    private Modes modes;

    private NarutoV2Config narutoV2Config;

    private int slot;

    private List<Scenario> scenarios;

    private  int episodeEvery;

    private BiomeState biomeState;

    private long pvpTime;

    private long borderTime;

    private long borderSpeed;

    private long startBorderSize;

    private long finalBorderSize;

    private double cycleTime;

    private boolean notchApple;

    private boolean nether;

    private boolean allPotionsEnable;

    private List<Potion> deniedPotions;

    private BlocsRules blocsRules;

    private EnchantmentRules enchantmentRules;

    private StuffRules stuffRules;

    private boolean viewHealth;

    private List<String> composition;

    private ItemStack helmet, chestplate, leggings, boots;

    private ItemStack[] inventory;

    private int roleAnnouncement;

    public PlayerConfig(UhcHost pl, UUID owner,String configName, Modes modes, NarutoV2Config narutoV2Config,
                        int slot, List<Scenario> scenarios, int episodeEvery, BiomeState biomeState,
                        long pvpTime, int borderTime, int borderSpeed, int startBorderSize, int finalBorderSize, double cycleTime, int roleAnnouncement,
                        boolean notchApple, boolean nether, boolean allPotionsEnable, List<Potion> deniedPotions, BlocsRules blocsRules, EnchantmentRules enchantmentRules, StuffRules stuffRules, boolean viewHealth, List<String> composition, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack[] inventory) {
        this.pl = pl;
        this.owner = owner;
        this.configName = configName;
        this.modes = modes;
        this.narutoV2Config = narutoV2Config;
        this.slot = slot;
        this.scenarios = scenarios;
        this.episodeEvery = episodeEvery;
        this.biomeState = biomeState;
        this.pvpTime = pvpTime;
        this.cycleTime = cycleTime;
        this.roleAnnouncement = roleAnnouncement;
        this.notchApple = notchApple;
        this.nether = nether;
        this.allPotionsEnable = allPotionsEnable;
        this.deniedPotions = deniedPotions;
        this.blocsRules = blocsRules;
        this.enchantmentRules = enchantmentRules;
        this.stuffRules = stuffRules;
        this.viewHealth = viewHealth;
        this.composition = composition;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.inventory = inventory;
        this.borderTime = borderTime;
        this.borderSpeed = borderSpeed;
        this.startBorderSize = startBorderSize;
        this.finalBorderSize = finalBorderSize;
    }

    public PlayerConfig(){
        this(UhcHost.getInstance(), null,"§cLG Défaut", Modes.LG, new NarutoV2Config(50, true), 30,
                Arrays.asList(Scenarios.CUTCLEAN.getScenario(), Scenarios.DIAMONDLIMIT.getScenario()),
                20*60, BiomeState.ROOFED_FOREST,
                20*60, 60*60, 2, 1200, 200,10, 20*60, false, false, true, Arrays.asList(), new BlocsRules(UhcHost.getInstance()), new EnchantmentRules(), new StuffRules(), false, Arrays.asList(""), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack[0]);
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public static int getIdConfig() {
        return idConfig;
    }

    public static void setIdConfig(int idConfig) {
        PlayerConfig.idConfig = idConfig;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Modes getModes() {
        return modes;
    }

    public void setModes(Modes modes) {
        this.modes = modes;
    }

    public NarutoV2Config getNarutoV2Config() {
        return narutoV2Config;
    }

    public void setNarutoV2Config(NarutoV2Config narutoV2Config) {
        this.narutoV2Config = narutoV2Config;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

    public int getEpisodeEvery() {
        return episodeEvery;
    }

    public void setEpisodeEvery(int episodeEvery) {
        this.episodeEvery = episodeEvery;
    }

    public BiomeState getBiomeState() {
        return biomeState;
    }

    public void setBiomeState(BiomeState biomeState) {
        this.biomeState = biomeState;
    }

    public long getPvpTime() {
        return pvpTime;
    }

    public void setPvpTime(long pvpTime) {
        this.pvpTime = pvpTime;
    }

    public double getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(double cycleTime) {
        this.cycleTime = cycleTime;
    }

    public boolean isNotchApple() {
        return notchApple;
    }

    public void setNotchApple(boolean notchApple) {
        this.notchApple = notchApple;
    }

    public boolean isNether() {
        return nether;
    }

    public void setNether(boolean nether) {
        this.nether = nether;
    }

    public boolean isAllPotionsEnable() {
        return allPotionsEnable;
    }

    public void setAllPotionsEnable(boolean allPotionsEnable) {
        this.allPotionsEnable = allPotionsEnable;
    }

    public List<Potion> getDeniedPotions() {
        return deniedPotions;
    }

    public void setDeniedPotions(List<Potion> deniedPotions) {
        this.deniedPotions = deniedPotions;
    }

    public BlocsRules getBlocsRules() {
        return blocsRules;
    }

    public void setBlocsRules(BlocsRules blocsRules) {
        this.blocsRules = blocsRules;
    }

    public EnchantmentRules getEnchantmentRules() {
        return enchantmentRules;
    }

    public void setEnchantmentRules(EnchantmentRules enchantmentRules) {
        this.enchantmentRules = enchantmentRules;
    }

    public StuffRules getStuffRules() {
        return stuffRules;
    }

    public void setStuffRules(StuffRules stuffRules) {
        this.stuffRules = stuffRules;
    }

    public boolean isViewHealth() {
        return viewHealth;
    }

    public void setViewHealth(boolean viewHealth) {
        this.viewHealth = viewHealth;
    }

    public List<String> getComposition() {
        return composition;
    }

    public void setComposition(List<String> composition) {
        this.composition = composition;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public void setInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public int getRoleAnnouncement() {
        return roleAnnouncement;
    }

    public void setRoleAnnouncement(int roleAnnouncement) {
        this.roleAnnouncement = roleAnnouncement;
    }

    public long getBorderTime() {
        return borderTime;
    }

    public void setBorderTime(long borderTime) {
        this.borderTime = borderTime;
    }

    public long getBorderSpeed() {
        return borderSpeed;
    }

    public void setBorderSpeed(long borderSpeed) {
        this.borderSpeed = borderSpeed;
    }

    public long getStartBorderSize() {
        return startBorderSize;
    }

    public void setStartBorderSize(long startBorderSize) {
        this.startBorderSize = startBorderSize;
    }

    public long getFinalBorderSize() {
        return finalBorderSize;
    }

    public void setFinalBorderSize(long finalBorderSize) {
        this.finalBorderSize = finalBorderSize;
    }
}
