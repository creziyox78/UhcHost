package fr.lastril.uhchost.game.rules.enums;

import org.bukkit.Material;

public enum GameplayRulesList {

    ROLLERCOASTER(true, "RollerCoaster", Material.RAILS),
    STRIPMINING(true, "StripMining", Material.DIAMOND_PICKAXE),
    POKEHOLING(true, "PokeHoling", Material.DIAMOND_SPADE),
    MININGSOUND(true, "Miner au son", Material.JUKEBOX),
    MININGENTITES(true, "Miner aux entitées", Material.MOB_SPAWNER),
    DIGDOWN(false, "DigDown au Meetup", Material.IRON_SPADE),
    TRAPS(false, "Pièges", Material.TRAP_DOOR),
    STALK(false, "Stalk", Material.CHAINMAIL_BOOTS),
    STEAL(false, "Voler", Material.FURNACE),
    CROSSTEAM(false, "CrossTeam", Material.BANNER),
    KILLCAVE(false, "KillCave", Material.IRON_SWORD);

    private boolean enabled;
    private final String name;
    private final Material material;

    GameplayRulesList(boolean enabled, String name, Material material){
        this.enabled = enabled;
        this.name = name;
        this.material = material;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }
}
