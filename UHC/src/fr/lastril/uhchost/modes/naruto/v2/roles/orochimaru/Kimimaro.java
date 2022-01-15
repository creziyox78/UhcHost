package fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.MarqueMauditeItem;
import fr.lastril.uhchost.modes.naruto.v2.items.ShikotsumyakuItem;
import fr.lastril.uhchost.modes.naruto.v2.items.swords.EpeeOsItem;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Kimimaro extends Role implements NarutoV2Role, MarqueMauditeItem.MarqueMauditeUser, RoleListener {

    private int epeeOsUses;
    private boolean usedForetOs;

    public Kimimaro() {
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addRoleToKnow(Orochimaru.class, Sakon.class, Ukon.class, Kidomaru.class, Tayuya.class, Jirobo.class);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new MarqueMauditeItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new ShikotsumyakuItem(main).toItemStack());
    }

    @Override
    public void afterRoles(Player player) {
        player.setMaxHealth(24);
        player.setHealth(player.getMaxHealth());
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWFiMjMxMTU4NDU1ZThiNDlhNzUxZWQ3YTBlM2U1NWM0MWZkZWJmODRlOWRhMGQwMGE3ZDY1ZDRkN2U2ZTU2YiJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.OROCHIMARU;
    }

    @Override
    public String getRoleName() {
        return "Kimimaro";
    }

    @Override
    public String getDescription() {
        return "\n§7Son but est de gagner avec le camp d’§5Orochimaru§7. \n" +
                " \n " +
                "§7§lItems :\n" +
                " \n " +
                "§7• Il dispose d’un item nommé “§8Shikotsumyaku§7”, lorsqu’il l’utilise un menu s’affiche avec plusieurs options :\n" +
                "Epée en os : lorsqu’il utilise son pouvoir, il obtient un os aussi fort qu’une épée en diamant Tranchant 4, cependant son os disparaît 1 minute après l’avoir utilisé. Il peut utiliser ce pouvoir 5 fois dans la partie.\n" +
                " \n " +
                "Forêt d’os : Lorsqu’il l’utilise, une forêt géante à base de quartz fait son apparition sur un rayon de 50 blocs tout autour de sa position, il peut le faire une seule fois dans la partie, celle-ci disparaît au bout de 3 minutes.\n" +
                " \n " +
                "§7• Il dispose d’un item nommé “§8Marque Maudite§7”, celui-ci lui permet de recevoir les effets §9Résistance 1§7, §dRégénération 1§7 et §c3 cœurs§7 supplémentaires régénérés, cependant il perd son effet de §bVitesse 1§7 et reçoit l’effet §8Lenteur 1§7, son pouvoir dure 2 minutes, suite à ses deux minutes il reçoit l’effet §5Faiblesse 1§7, perd son effet de §cForce 1§7, garde son effet de §8Lenteur 1§7, tout cela pendant 5 minutes et il possède un délai de 20 minutes.\n" +
                " \n " +
                "§7§lParticularités :\n" +
                " \n " +
                "§7• Il dispose des effets §cForce 1§7 et §bVitesse 1§7.\n" +
                " \n " +
                "§7• Il dispose de l’identité de §5Sakon§7, §5Ukon§7, §5Kidômaru§7, §5Tayuya§7 et §5Jirôbô§7.";
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

	@Override
	public Chakra getChakra() {
		return null;
	}

    /**
     * @author Maygo
     *
     * @param main
     * @param player
     * @param joueur
     */
    @Override
    public void onUseMarqueMaudite(UhcHost main, Player player, PlayerManager joueur) {

        if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 60 * 2, 0, false, false));

        if (player.hasPotionEffect(PotionEffectType.SPEED))
            player.removePotionEffect(PotionEffectType.SPEED);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 60 * 7, 0, false, false));

        if (player.hasPotionEffect(PotionEffectType.REGENERATION))
            player.removePotionEffect(PotionEffectType.REGENERATION);

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 60 * 2, 0, false, false));



        player.setMaxHealth(player.getMaxHealth() + (3D*2D));
        player.setHealth(player.getHealth() + (3D*2D));

        joueur.setRoleCooldownMarqueMaudite(60 * 20);
        joueur.sendTimer(player, joueur.getRoleCooldownMarqueMaudite(), player.getItemInHand());
        new BukkitRunnable() {

            @Override
            public void run() {

                if (player.hasPotionEffect(PotionEffectType.WEAKNESS))
                    player.removePotionEffect(PotionEffectType.WEAKNESS);
                if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 60 * 5, 0, false, false));

                player.setMaxHealth(player.getMaxHealth() - (3D*2D));

                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
                        Kimimaro.super.givePermanentEffects(player);
                    }
                }.runTaskLater(main, 20 * 60 * 5);
            }
        }.runTaskLater(main, 20 * 60 * 2);

        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
    }

    public int getEpeeOsUses() {
        return epeeOsUses;
    }

    public void addEpeeOsUse() {
        this.epeeOsUses++;
    }

    public boolean hasUsedForetOs() {
        return usedForetOs;
    }

    public void setUsedForetOs(boolean usedForetOs) {
        this.usedForetOs = usedForetOs;
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event){
        ItemStack os = new QuickItem(Material.BONE).setName("§cEpée en os").toItemStack();
        ItemStack epee = new EpeeOsItem(os).getItem();
        if(event.getItemDrop().getItemStack().isSimilar(new EpeeOsItem(epee).getItem())){
            event.setCancelled(true);
        }
    }
}
