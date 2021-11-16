package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.enums.WorldState;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.lg.roles.lg.LoupGarouPerfide;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.API.particles.ParticleEffect;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PetiteFille extends Role implements LGRole {

    private static final int DISTANCE = 100;

    public PetiteFille() {
        super.addEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    public String getSkullValue() {
        return null;
    }

    @Override
    public void giveItems(Player player) {
        player.getInventory().addItem(new ItemStack(Material.TNT, 5));
    }

    @Override
    public void onNight(Player player) {
        player.sendMessage("§9Liste des PlayerManagers se trouvant dans un rayon de " + DISTANCE + " blocs :");
        for (Entity entity : player.getNearbyEntities(DISTANCE, DISTANCE, DISTANCE)) {
            if (entity instanceof Player) {
                Player target = (Player) entity;
                if (target.getGameMode() != GameMode.SPECTATOR) {
                    player.sendMessage("§9- " + target.getName());
                }
            }
        }
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
    public String getRoleName() {
        return "Petite Fille";
    }

    @Override
    public String getDescription() {
        return main.getLGRoleDescription(this,this.getClass().getName());
    }

    @Override
    public QuickItem getItem() {
        return null;
        //return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName("§a"+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2I3MzRmODdkZDdkZjViZTJhNzZmMjUwNjc4NmIzOWE2NDY2ZTQyOTJkMTllZmI0ZTk5ODk4MWNlYjg5MSJ9fX0=");
    }

    @Override
    public void checkRunnable(Player player) {
        if (main.gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager) {
            LoupGarouManager loupGarouManager = (LoupGarouManager) main.gameManager.getModes().getMode().getModeManager();
            if (main.gameManager.getWorldState() == WorldState.NIGHT) {
                if (isWithoutArmor(player)) {
                    for (PlayerManager playerManager : loupGarouManager.getPlayerManagersWithRole(LoupGarouPerfide.class)) {
                        if (playerManager.getPlayer() != null) {
                            Player perfide = playerManager.getPlayer();
                            for (int i = 0; i < 10; i++) {
                                ParticleEffect.playEffect(perfide, EnumParticle.REDSTONE, player.getLocation());
                            }
                        }
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 0, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
                } else {
                    if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
                        player.removePotionEffect(PotionEffectType.INVISIBILITY);
                }
            } else {
                if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }

    }

    public boolean isWithoutArmor(Player player) {
        EntityEquipment equipement = player.getEquipment();
        return equipement.getHelmet() == null && equipement.getChestplate() == null && equipement.getLeggings() == null && equipement.getBoots() == null;
    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }

}
