package fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdOsama;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdSharingan;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.ChibakuTenseiItem;
import fr.lastril.uhchost.modes.naruto.v2.items.Oiseautem;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Obito;
import fr.lastril.uhchost.modes.naruto.v2.tasks.NagatoCooldownsTask;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Nagato extends Role implements NarutoV2Role, RoleListener, RoleCommand, CmdSharingan.SharinganUser, ChibakuTenseiItem.ChibakuTenseiUser {

    private static final int REGENERATION_DELAY = 30, RESISTANCE_CHANCE = 5, VELOCITY_DISTANCE = 20, FLY_TIME = 10;

    private long lastCombat;
    private int sharinganUses;

    private boolean useOsama;

    private final List<PlayerManager> akatsukiList = new ArrayList<>();

    public Nagato() {
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    public static int getFlyTime() {
        return FLY_TIME;
    }

    public void setUseOsama(boolean useOsama){
        this.useOsama = useOsama;
    }

    public boolean isUseOsama(){
        return useOsama;
    }

    @Override
    public void giveItems(Player player) {
        new NagatoCooldownsTask(main, this).runTaskTimer(main, 0, 20*5);

        main.getInventoryUtils().giveItemSafely(player, new ChibakuTenseiItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Oiseautem(main).toItemStack());
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
    public void checkRunnable(Player player) {
    	super.checkRunnable(player);
        long delayLastCombat = System.currentTimeMillis() - this.lastCombat;
        if (delayLastCombat > REGENERATION_DELAY * 1000) {
            if(!player.hasPotionEffect(PotionEffectType.REGENERATION)) player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, false, false));
        }else{
            for(PotionEffect effect : player.getActivePotionEffects()){
                if(effect.getType().getId() == 10){
                    if(effect.getAmplifier() < 1){
                        if(player.hasPotionEffect(PotionEffectType.REGENERATION)) player.removePotionEffect(PotionEffectType.REGENERATION);
                    }
                }
            }

        }
    }

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if(event.getDamager() instanceof Player){
                Player damager = (Player) event.getDamager();
                PlayerManager damagerJoueur = main.getPlayerManager(damager.getUniqueId());
                if(joueur.hasRole() && joueur.getRole() instanceof Nagato){
                    Nagato nagato = (Nagato) joueur.getRole();
                    nagato.setLastCombat(System.currentTimeMillis());
                    int chance = UhcHost.getRANDOM().nextInt(100);
                    UhcHost.debug("nagato's resistance chance : "+chance+"/"+RESISTANCE_CHANCE);
                    if(chance <= RESISTANCE_CHANCE){
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 25*20, 0, false, false));
                    }
                }
                if(damagerJoueur.hasRole() && damagerJoueur.getRole() instanceof Nagato){
                    Nagato nagato = (Nagato) damagerJoueur.getRole();
                    nagato.setLastCombat(System.currentTimeMillis());
                }
            } else if(event.getDamager() instanceof Projectile){
                Projectile projectile = (Projectile) event.getDamager();
                if(projectile.getShooter() instanceof Player){
                    if(joueur.hasRole() && joueur.getRole() instanceof Nagato){
                        Nagato nagato = (Nagato) joueur.getRole();
                        nagato.setLastCombat(System.currentTimeMillis());
                    }
                }
            }
        }
    }


    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjkyMjY0NmE4OTQ1YjVjNDAwZTkwNDZjN2JhMjA4MGZjNmQ0N2ExNjUxMjEyNmI4OWJmNzc3ZDg0MjllZTU1NiJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public void afterRoles(Player player) {
        player.setMaxHealth(2D*12);
        player.setHealth(player.getMaxHealth());
        player.sendMessage(sendList());
    }


    @Override
    public Camps getCamp() {
        return Camps.AKATSUKI;
    }

    @Override
    public String getRoleName() {
        return "Nagato";
    }

    @Override
    public String sendList() {
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return null;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        String list = Messages.NARUTO_PREFIX.getMessage() + "Voici la liste entière de l'Akatsuki : \n";
        for (PlayerManager joueur : narutoV2Manager.getPlayerManagersWithCamps(Camps.AKATSUKI)) {
            akatsukiList.add(joueur);
        }
        for(PlayerManager joueur : narutoV2Manager.getPlayerManagersWithRole(Obito.class)){
            akatsukiList.add(joueur);
        }
        int numberOfElements = akatsukiList.size();
        for (int i = 0; i < numberOfElements; i++) {
            int index = UhcHost.getRANDOM().nextInt(akatsukiList.size());
            list += "§c- " + akatsukiList.get(index).getPlayerName() + "\n";
            akatsukiList.remove(index);
        }

        return list;
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
		return Chakra.SUITON;
	}

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdSharingan(main), new CmdOsama(main));
    }

    public long getLastCombat() {
        return lastCombat;
    }

    public void setLastCombat(long lastCombat) {
        this.lastCombat = lastCombat;
    }

    @Override
    public int getSharinganUses() {
        return sharinganUses;
    }

    @Override
    public void addSharinganUse() {
        sharinganUses++;
    }

    public int getCooldownDistance(){
        return 20;
    }

    public static int getVelocityDistance() {
        return VELOCITY_DISTANCE;
    }

    @Override
    public boolean hasTengaiShinsei() {
        return false;
    }

    @Override
    public boolean hasUsedTengaiShinsei() {
        //USELESS
        return false;
    }

    @Override
    public void useTengaiShinsei() {
        //USELESS
    }

    @Override
    public String getPlayerName(){
        return this.getRoleName();
    }
}
