package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.commands.soeur.CmdSoeurName;
import fr.lastril.uhchost.modes.lg.commands.soeur.CmdSoeurRole;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.TextComponentBuilder;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
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

public class Soeur extends Role implements LGRole, RoleCommand {

    private boolean otherDead;
    private PlayerManager playerKiller;
    private final PotionEffect resistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*2, 0, false, false);

    public Soeur() {
        super.addRoleToKnow(Soeur.class);
    }


    public String getSkullValue() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhlZGE4YjhmOTUyNzA2MzU3ZTZjMjJmMTZmYmU2NjJjZDIxMWI5NmNmZmU5ZWJiOWY3OWRmYzAyZjQwYjgifX19";
    }

    @Override
    public void giveItems(Player player) {
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
        return "Soeur";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this,this.getClass().getName());
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVkNTFmYjVhOTE4ZTMzYzA0YmIyMzIzNjE0N2QzNTU1OWRlMzJhNDQ4MTcwZjFhM2NjYWFlNmRjYTYzY2I2In19fQ==");
    }

    @Override
    public void checkRunnable(Player player) {
        if (main.gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager) {
            LoupGarouManager loupGarouManager = (LoupGarouManager) main.gameManager.getModes().getMode().getModeManager();
            for (PlayerManager playerManager : loupGarouManager.getPlayerManagersWithRole(Soeur.class)) {
                if (playerManager.getPlayer() != player) {
                    if (playerManager.getPlayer() != null) {
                        Player soeur = playerManager.getPlayer();
                        if(soeur.getWorld() == player.getWorld()){
                            if (player.getLocation().distance(soeur.getLocation()) <= 20) {
                                player.addPotionEffect(resistance);
                                soeur.addPotionEffect(resistance);
                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }

    @Override
    public void onPlayerDeathRealy(PlayerManager player, ItemStack[] items, ItemStack[] armors, Player killer, Location deathLocation) {
        PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(player.getUuid());
        if (PlayerManager.getRole() instanceof Soeur) {
            if (PlayerManager.getPlayer() != this.getPlayer()) {
                if (this.getPlayer() != null) {
                    Player otherSoeur = this.getPlayer();
                    otherDead = true;
                    otherSoeur.sendMessage("Votre soeur (" + player.getPlayerName() + ") est morte ! Vous pouvez décider de voir le pseudo du tueur ou son rôle pendant 10 secondes.");
                    TextComponent textComponent = new TextComponent(new TextComponentBuilder("§a§l[Pseudo]").setClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg soeur_pseudo").toText());
                    textComponent.addExtra(new TextComponent(" "));
                    textComponent.addExtra(new TextComponentBuilder("§5§l[Rôle]").setClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg soeur_role").toText());
                    textComponent.addExtra(new TextComponent(" "));
                    otherSoeur.spigot().sendMessage(textComponent);
                    playerKiller = main.getPlayerManager(killer.getUniqueId());
                    Bukkit.getScheduler().runTaskLater(main, () -> otherDead = false, 20*10);
                }
            }
        }
    }

    public PlayerManager getPlayerKiller() {
        return playerKiller;
    }

    public void setOtherDead(boolean otherDead) {
        this.otherDead = otherDead;
    }

    public boolean isOtherDead() {
        return otherDead;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdSoeurName(main), new CmdSoeurRole(main));
    }
}
