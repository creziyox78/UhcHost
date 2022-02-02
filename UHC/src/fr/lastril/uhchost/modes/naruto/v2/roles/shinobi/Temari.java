package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.EventailItem;
import fr.lastril.uhchost.modes.naruto.v2.items.KamatariItem;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Temari extends Role implements NarutoV2Role {

    private Wolf wolf;

    private static final double EVENTAIL_DISTANCE = 20;

    public Temari() {
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new EventailItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new KamatariItem(main).toItemStack());
    }

    @Override
    public void afterRoles(Player player) {
        player.setMaxHealth(16);
        player.setHealth(player.getMaxHealth());
    }

    @Override
    public void checkRunnable(Player player) {
    	super.checkRunnable(player);
        if(player.getItemInHand() != null){
            ItemStack item = player.getItemInHand();
            if(item.hasItemMeta() && item.getItemMeta().hasDisplayName()){
                if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§bÉventail")){
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
                    return;
                }
            }
        }
        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
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


    /**
     * TEXTURE : eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZThiOGZmY2I1YmE3NTBlMWIyMGFmMmNkY2Q4ZGFiNTk4OWJjMzkwYjUwNjI1MTlmMjVmYWM2YjlhMzg3NDdlNSJ9fX0=
     *
     * @return
     */
    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName())
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZThiOGZmY2I1YmE3NTBlMWIyMGFmMmNkY2Q4ZGFiNTk4OWJjMzkwYjUwNjI1MTlmMjVmYWM2YjlhMzg3NDdlNSJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Temari";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "naruto.yml");
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

	@Override
	public Chakra getChakra() {
		return Chakra.FUTON;
	}

    public static double getEventailDistance() {
        return EVENTAIL_DISTANCE;
    }

    public Wolf getWolf() {
        return wolf;
    }

    public void setWolf(Wolf wolf) {
        this.wolf = wolf;
    }
}
