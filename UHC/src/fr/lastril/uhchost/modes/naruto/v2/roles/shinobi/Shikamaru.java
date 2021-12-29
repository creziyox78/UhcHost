package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.enums.WorldState;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.IntonItem;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.display.PacketDisplay;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Shikamaru extends Role implements NarutoV2Role, RoleListener {

    private static final int DELAY_EFFECTS = 5*60;
    private static final List<PotionEffect> effects = Arrays.asList(
            new PotionEffect(PotionEffectType.WEAKNESS, 60*20, 0, false, false),
            new PotionEffect(PotionEffectType.SLOW, 60*20, 0, false, false)
            );
    public static final int STUN_TIME = 10;
    private long lastTechniqueUse;
    private int manipulationUses;
    private boolean usedEtreinteMortelle, usedRecherche;

    private final Map<UUID, PacketDisplay> inOeil;

    public Shikamaru() {
        super.addRoleToKnow(Choji.class);
        this.inOeil = new HashMap<>();
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new IntonItem(main).toItemStack());
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName())
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTI3MTkzMDJkZTg2YWJiOTFhMGJiMTgyOWIyM2QxNWU2MTIwY2JhYjhhYWM1YTE4M2NjNGIxNTczYzIzNDMyOCJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Shikamaru";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

    public void useTechnique(Player player){
        if(System.currentTimeMillis() - this.lastTechniqueUse <= DELAY_EFFECTS*1000){
            for(PotionEffect effect: effects){
                player.removePotionEffect(effect.getType());
            }
            effects.forEach(player::addPotionEffect);
            player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"§cVous avez trop utilisé vos pouvoirs vous recevez les effets :\n§c- Faiblesse\n§c- Lenteur");
        }
        this.lastTechniqueUse = System.currentTimeMillis();
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        inOeil.forEach((uuid, armorStand) -> {
            Player target = Bukkit.getPlayer(uuid);
            PlayerManager joueur = main.getPlayerManager(uuid);
            if(target == null && armorStand.isCustomNameVisible() && !joueur.isAlive()) armorStand.setCustomNameVisible(false, player);
        });
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(inOeil.containsKey(player.getUniqueId())){
            PacketDisplay packetDisplay = inOeil.get(player.getUniqueId());

            if(super.getPlayer() != null){
                Player shikamaru = super.getPlayer();
                if(main.getPlayerManager(player.getUniqueId()).isAlive()){
                    if (!packetDisplay.isCustomNameVisible()) {
                        packetDisplay.setCustomNameVisible(true, shikamaru);
                    }
                    if(!packetDisplay.getText().equals(WorldUtils.getBeautyHealth(player)+" ❤")){
                        packetDisplay.rename(WorldUtils.getBeautyHealth(player)+" ❤", shikamaru);
                    }
                    packetDisplay.teleport(player.getLocation(), shikamaru);
                } else {
                    packetDisplay.setCustomNameVisible(false, shikamaru);
                }
            }
        }
    }

    public double getPlayersDistance() {
        return main.getGamemanager().getWorldState() == WorldState.DAY ? 15 : 30;
    }

    public int getManipulationUses() {
        return manipulationUses;
    }

    public void addManipulationUses() {
        manipulationUses++;
    }

    public boolean hasUsedEtreinteMortelle() {
        return usedEtreinteMortelle;
    }

    public void setUsedEtreinteMortelle(boolean usedEtreinteMortelle) {
        this.usedEtreinteMortelle = usedEtreinteMortelle;
    }

    public Map<UUID, PacketDisplay> getInOeil() {
        return inOeil;
    }

    public void oeil(Player shikamaru, Player target){
        if(main.getPlayerManager(target.getUniqueId()).isAlive()){
            PacketDisplay display = new PacketDisplay(target.getLocation(), WorldUtils.getBeautyHealth(target)+" ❤");

            display.display(shikamaru);

            inOeil.put(target.getUniqueId(), display);
        }
    }

    public boolean hasUsedRecherche() {
        return usedRecherche;
    }

    public void setUsedRecherche(boolean usedRecherche) {
        this.usedRecherche = usedRecherche;
    }

	@Override
	public Chakra getChakra() {
		return null;
	}
}
