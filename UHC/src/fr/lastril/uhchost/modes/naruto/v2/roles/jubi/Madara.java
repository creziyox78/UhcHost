package fr.lastril.uhchost.modes.naruto.v2.roles.jubi;

import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.biju.Biju;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdDetect;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.ChibakuTenseiItem;
import fr.lastril.uhchost.modes.naruto.v2.items.MadaraItem;
import fr.lastril.uhchost.modes.naruto.v2.items.SusanoItem;
import fr.lastril.uhchost.modes.naruto.v2.items.biju.JubiItem;
import fr.lastril.uhchost.modes.naruto.v2.items.swords.GunbaiSword;
import fr.lastril.uhchost.modes.naruto.v2.roles.JubiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Madara extends Role implements NarutoV2Role, JubiItem.JubiUser, SusanoItem.SusanoUser, ChibakuTenseiItem.ChibakuTenseiUser, RoleCommand, JubiRole {

    private static final int DETECTION_TIME = 5*60;

    private int detectUses;
    private boolean usedTengaiShinsei, boost;
    private Biju bijuTracked;

    //private double recupKuramaPoint = 0, recupShukakuPoint = 0, recupGyukiPoint = 0;

    public Madara() {
        super.addEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        super.addRoleToKnow(Obito.class);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new SusanoItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new GunbaiSword().toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new MadaraItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new ChibakuTenseiItem(main).toItemStack());
    }

    @Override
    public void afterRoles(Player player) {
        player.setMaxHealth(24);
        player.setHealth(player.getMaxHealth());
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        if(narutoV2Manager.getNarutoV2Config().isBiju()){
            setBijuTracked(narutoV2Manager.getBijuManager().getBijuListClass().get(0));
            Bukkit.getScheduler().runTaskTimer(main, () -> {
                for(PlayerManager joueur : narutoV2Manager.getPlayerManagersWithRole(Madara.class)){
                    if(joueur.getPlayer() != null){
                        Player tracker = joueur.getPlayer();
                        Biju biju = getBijuTracked();
                        if(biju != null){
                            if(biju.getJoueurPicked() != null && biju.getJoueurPicked().isAlive()){
                                tracker.setCompassTarget(biju.getJoueurPicked().getPlayer().getLocation());
                            } else {
                                tracker.setCompassTarget(biju.getSafeSpawnLocation());
                            }
                        }
                    }

                }
            }, 20, 10);
        }

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
                .setTexture(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODcwYzQzNWI1NDgwNmIyZDU4NzhlMDk3NWExM2ZiOTU1ZGFiMmZiNjg4ZjUwYjAxMTM1NzY0OGY4N2E0Y2QxIn19fQ==");
    }

    @Override
    public Camps getCamp() {
        return Camps.JUBI;
    }

    @Override
    public String getRoleName() {
        return "Madara";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

	@Override
	public Chakra getChakra() {
		return Chakra.KATON;
	}



    @Override
    public boolean hasTengaiShinsei() {
        return true;
    }

    @Override
    public boolean hasUsedTengaiShinsei() {
        return usedTengaiShinsei;
    }

    @Override
    public void useTengaiShinsei() {
        usedTengaiShinsei = true;
    }

    @Override
    public String getPlayerName(){
        return this.getRoleName();
    }

    public int getDetectUses() {
        return detectUses;
    }

    public void useDetect() {
        this.detectUses++;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdDetect(main));
    }

    public static int getDetectionTime() {
        return DETECTION_TIME;
    }

    public void setBijuTracked(Biju bijuTracked) {
        this.bijuTracked = bijuTracked;
    }

    public Biju getBijuTracked() {
        return bijuTracked;
    }

    public boolean isBoost() {
        return boost;
    }

    public void setBoost(boolean boost) {
        this.boost = boost;
    }
}
