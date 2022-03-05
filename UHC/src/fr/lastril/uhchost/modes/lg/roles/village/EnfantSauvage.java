package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.enums.WorldState;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.commands.CmdModele;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.apache.commons.lang3.AnnotationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class EnfantSauvage extends Role implements LGRole, RoleCommand {

    private PlayerManager modele;

    @Override
    public void giveItems(Player player) {

    }

    @Override
    public String sendList() {
        if(modele != null){
            String message ="§eVotre modèle : " + modele.getPlayerName() + "\n";
            PlayerManager enfant = UhcHost.getInstance().getPlayerManager(getPlayerId());
            if(enfant.getWolfPlayerManager().isTransformed()){
                if (main.gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager) {
                    LoupGarouManager loupGarouManager = (LoupGarouManager) main.gameManager.getModes().getMode().getModeManager();
                    message += loupGarouManager.sendLGList();
                }
            }
            return message;
        }
        return null;
    }

    @Override
    public void afterRoles(Player player) {
        Bukkit.getScheduler().runTaskLater(main, () -> {
            if(getModele() == null){
                while (modele == null || modele == main.getPlayerManager(player.getUniqueId())){
                    modele = main.getRandomPlayerManagerAlive();
                }
                UhcHost.debug("Enfant sauvage modele : " + modele.getPlayerName());
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§bVoici votre modèle qui a été choisi aléatoirement: " + modele.getPlayerName());
            }
        }, 20*5*60);
    }



    @Override
    public void onNight(Player player) {
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
        return "Enfant Sauvage";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "lg.yml");
    }


    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWFmNjZhYTVmY2UxMDNjNzBlODYzMzVhZGUzNDkwNTNjZjAwMGFkMDhkMDZhZmQ2MTRlNzJiOWU1ZmZmM2QzYSJ9fX0=");
    }

    @Override
    public void checkRunnable(Player player) {

    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }

    @Override
    public void onPlayerDeathRealy(PlayerManager player, ItemStack[] items, ItemStack[] armors, Player killer, Location deathLocation) {
        UhcHost.debug("Checking ES modele...");
        if (player == modele) {
            UhcHost.debug("Dead player is ES modele ! Transform to LG !");
            PlayerManager enfant = UhcHost.getInstance().getPlayerManager(getPlayerId());
            enfant.getWolfPlayerManager().setCamp(Camps.LOUP_GAROU);
            enfant.getWolfPlayerManager().setTransformed(true);
            super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0,false,false), When.NIGHT);
            super.addEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 1, 0, false, false), When.AT_KILL);
            super.addEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 1, 0, false, false), When.AT_KILL);

            if (super.getPlayer() != null) {
                super.getPlayer().sendMessage("Votre modèle est mort, vous êtes donc transformé et devez gagner avec les Loups-Garous. Faites /lg list pour avoir la liste des Loups-Garous.");
                if(WorldState.isWorldState(WorldState.NUIT))
                    super.night(super.getPlayer());
                enfant.setCamps(Camps.LOUP_GAROU);
                if(enfant.getWolfPlayerManager().isInCouple())
                    enfant.getWolfPlayerManager().setCamp(Camps.COUPLE);
                LoupGarouManager loupGarouManager = (LoupGarouManager) main.gameManager.getModes().getMode().getModeManager();
                loupGarouManager.sendNewLG();
            }
        }
    }

    public void setModele(PlayerManager modele) {
        this.modele = modele;
    }

    public PlayerManager getModele() {
        return modele;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdModele(main));
    }
}
