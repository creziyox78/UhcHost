package fr.lastril.uhchost.modes.lg.roles.solo;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.commands.ange.CmdDechu;
import fr.lastril.uhchost.modes.lg.commands.ange.gardien.CmdGardien;
import fr.lastril.uhchost.modes.lg.commands.ange.gardien.CmdRegen;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.TextComponentBuilder;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Ange extends Role implements LGRole, RoleCommand {

    private PlayerManager cible;
    private boolean choose, regen;
    private Form form;

    @Override
    public void giveItems(Player player) {
    }

    @Override
    public void onNight(Player player) {
    }

    @Override
    public String sendList() {
        if(form != null && hasChoose()){
            return "§eVotre cible : " + cible.getPlayerName() + "\n" +
                    "§eVotre forme : " + form.name().toLowerCase();
        }
        return null;
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
    public void onPlayerDeathRealy(PlayerManager player, ItemStack[] items, ItemStack[] armors, Player killer, Location deathLocation) {
        if(player.getRole() instanceof Ange){
            Ange ange = (Ange) player.getRole();
            if(ange.getCible().isAlive()){
                ange.getCible().setCamps(ange.cible.getRole().getCamp());
                if(ange.getCible().getWolfPlayerManager().isInCouple())
                    ange.getCible().setCamps(Camps.COUPLE);
            }
        }
        if(player == cible){
            if(form == Form.GARDIEN){
                Player ange = super.getPlayer();
                if(ange != null){
                    ange.setMaxHealth(ange.getMaxHealth() - 2D*3D);
                    ange.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVotre protégé(e) vient de mourir. Vous perdez donc 3 coeurs et avez l'effet§7 Weakness I§c permanent.");
                    if(ange.hasPotionEffect(PotionEffectType.WEAKNESS))
                        ange.removePotionEffect(PotionEffectType.WEAKNESS);
                    ange.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false));
                }
            } else if(form == Form.DECHU){
                PlayerManager playerManager = main.getPlayerManager(killer.getUniqueId());
                if(playerManager.hasRole()){
                    if(playerManager.getRole() instanceof Ange){
                        Ange ange = (Ange) playerManager.getRole();
                        Player angePlayer = ange.getPlayer();
                        if(angePlayer != null){
                            angePlayer.setMaxHealth(angePlayer.getMaxHealth() + 2D*3D);
                            angePlayer.setHealth(angePlayer.getHealth() + 2D*3D);
                            angePlayer.playSound(angePlayer.getLocation(), Sound.LEVEL_UP, 1 , 1.0F);
                            angePlayer.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aVous venez de tuer votre cible. Vous recevez donc 3 coeurs supplémentaires.");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void afterRoles(Player player) {
        TextComponent angechoose = new TextComponent("§7§oChoisir mon rôle: ");
        angechoose.addExtra(new TextComponent(new TextComponentBuilder("§c§l[Ange Déchu]").setClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg ange_dechu").toText()));
        angechoose.addExtra(new TextComponent(" ou "));
        angechoose.addExtra(new TextComponent(new TextComponentBuilder("§a§l[Ange Gardien]").setClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg ange_gardien").toText()));
        player.spigot().sendMessage(angechoose);
        while (cible == null || cible == main.getPlayerManager(player.getUniqueId())){
            cible = main.getRandomPlayerManagerAlive();
            cible.setCamps(Camps.ANGE);
            if(cible.getWolfPlayerManager().isInCouple())
                cible.setCamps(Camps.COUPLE);
        }
    }

    @Override
    public String getRoleName() {
        return "Ange";
    }

    @Override
    public String getDescription() {
        return main.getLGRoleDescription(this,this.getClass().getName());
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTU2OThmMWY3NjEwMWU4MTExNDdmOTY5NWQwNmJlNjBjNzI4NjRiNTBlMTM5ZjY1ZWY5YzFmNmNiYjBmOTRlYSJ9fX0=");
    }

    @Override
    public void checkRunnable(Player player) {

    }

    public boolean hasChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    @Override
    public Camps getCamp() {
        return Camps.ANGE;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdGardien(main), new CmdDechu(main), new CmdRegen(main));
    }

    public enum Form{
        DECHU, GARDIEN
    }

    public boolean hasRegen() {
        return regen;
    }

    public void setRegen(boolean regen) {
        this.regen = regen;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Form getForm() {
        return form;
    }

    public PlayerManager getCible() {
        return cible;
    }
}
