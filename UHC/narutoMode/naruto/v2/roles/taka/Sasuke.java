package fr.lastril.uhchost.modes.naruto.v2.roles.taka;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.events.player.PlayerKillEvent;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.RoleListener;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdIzanagi;
import fr.maygo.uhc.modes.naruto.v2.commands.CmdIzanagi.IzanagiUser;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.GenjutsuItem.GenjutsuUser;
import fr.maygo.uhc.modes.naruto.v2.items.RinneganItem;
import fr.maygo.uhc.modes.naruto.v2.items.SusanoItem;
import fr.maygo.uhc.modes.naruto.v2.items.SusanoItem.SusanoUser;
import fr.maygo.uhc.modes.naruto.v2.items.biju.JubiItem;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.Itachi;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.Nagato;
import fr.maygo.uhc.modes.naruto.v2.roles.orochimaru.Orochimaru;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Sasuke extends Role implements NarutoV2Role, SusanoUser, IzanagiUser, RoleCommand, RoleListener, GenjutsuUser {

    private boolean orochimaruDead, itachiDead, killedItachi;

    private boolean hasUsedIzanagi;

    private boolean hasUsedIzanami;

    private int tsukuyomiUses;

    public Sasuke() {
        super.addRoleToKnow(Orochimaru.class);
        super.addEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new SusanoItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new RinneganItem(main).toItemStack());
    }

    @EventHandler
    public void onKillItachi(PlayerKillEvent event) {
        Player killer = event.getPlayer();
        Player player = event.getDeadPlayer();
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        PlayerManager killerPlayerManager = main.getPlayerManager(killer.getUniqueId());
        if (PlayerManager.hasRole() && killerPlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Itachi && killerPlayerManager.getRole() instanceof Sasuke) {
                setKilledItachi(true);
                killer.setMaxHealth(killer.getMaxHealth() + (2D * 3D));
                killer.setHealth(killer.getHealth() + (2D * 3D));
                killer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous venez de tuer Itachi. Vous recevez 3 coeurs supplémentaires ainsi que la possibilité d'utilisé l'épée Susano d'Itachi.");

            }
        }
    }

    @EventHandler
    public void Craft(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();
        if (result.getType() == Material.NETHER_STAR) {
            if (result.isSimilar(new JubiItem(main).toItemStack())) {
                if (super.getPlayer() != null) {
                    Player jugoPlayer = super.getPlayer();
                    main.getPlayerManager(jugoPlayer.getUniqueId()).setCamps(Camps.SHINOBI);
                    jugoPlayer.sendMessage("§5Jûbi§e vient d'être invoqué. Vous gagnez désormais avec les§a Shinobis.");
                }
            }
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGFlYmNhMzcyOWJhOTgwOTM1OTg4OGNiNDY4MTM1YmI0OTNjOGQxOGM5OGI5NWMxNTdlZTk5MDQyOWExMCJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.TAKA;
    }

    @Override
    public String getRoleName() {
        return "Sasuke";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Orochimaru) {
                if (super.getPlayer() != null) {
                    if (itachiDead) {
                        Player sasukePlayer = super.getPlayer();
                        itachiDead = true;
                        main.getPlayerManager(sasukePlayer.getUniqueId()).setCamps(Camps.AKATSUKI);
                        sasukePlayer.sendMessage("§cVous gagnez avec l'§cAkatsuki car Itachi et Orochimaru sont morts. Voici l'identité de Nagato et des membres de Taka: ");

                        for (PlayerManager nagato : main.getNarutoV2Manager().getPlayerManagersWithRole(Nagato.class)) {
                            sasukePlayer.sendMessage("§c - Nagato : " + nagato.getPlayerName());
                        }
                        for (PlayerManager sasuke : main.getNarutoV2Manager().getPlayerManagersWithRole(Jugo.class)) {
                            sasukePlayer.sendMessage("§6 - Jugo : " + sasuke.getPlayerName());
                        }
                        for (PlayerManager sasuke : main.getNarutoV2Manager().getPlayerManagersWithRole(Karin.class)) {
                            sasukePlayer.sendMessage("§6 - Karin : " + sasuke.getPlayerName());
                        }
                        for (PlayerManager sasuke : main.getNarutoV2Manager().getPlayerManagersWithRole(Suigetsu.class)) {
                            sasukePlayer.sendMessage("§6 - Suigetsu : " + sasuke.getPlayerName());
                        }
                    } else {
                        orochimaruDead = true;
                        Player sasukePlayer = super.getPlayer();
                        main.getPlayerManager(sasukePlayer.getUniqueId()).setCamps(Camps.TAKA);
                        sasukePlayer.sendMessage("§5Orochimaru§e vient de mourir, vous gagnez uniquement avec votre camp. Voici les PlayerManagers dans votre camp: ");
                        for (PlayerManager PlayerManagers : main.getNarutoV2Manager().getPlayerManagersWithCamps(getCamp())) {
                            sasukePlayer.sendMessage("§6 - " + PlayerManagers.getPlayerName() + " : " + PlayerManagers.getRoleName());
                        }
                    }

                }
            } else if (PlayerManager.getRole() instanceof Itachi) {
                if (super.getPlayer() != null) {
                    if (orochimaruDead) {
                        Player sasukePlayer = super.getPlayer();
                        itachiDead = true;
                        main.getPlayerManager(sasukePlayer.getUniqueId()).setCamps(Camps.AKATSUKI);
                        sasukePlayer.sendMessage("§cItachi§e vient de mourir, vous gagnez avec l'§cAkatsuki. Voici l'identité de Nagato: ");
                        for (PlayerManager nagato : main.getNarutoV2Manager().getPlayerManagersWithRole(Nagato.class)) {
                            sasukePlayer.sendMessage("§c - " + nagato.getPlayerName());
                        }
                    } else {
                        itachiDead = true;
                    }
                }
            }

        }
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    @Override
    public Chakra getChakra() {
        return Chakra.KATON;
    }

    public boolean isOrochimaruDead() {
        return orochimaruDead;
    }

    public void setOrochimaruDead(boolean orochimaruDead) {
        this.orochimaruDead = orochimaruDead;
    }

    public boolean isItachiDead() {
        return itachiDead;
    }

    public void setItachiDead(boolean itachiDead) {
        this.itachiDead = itachiDead;
    }

    @Override
    public void afterRoles(Player player) {
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        PlayerManager.setCamps(Camps.OROCHIMARU);
        player.setMaxHealth(player.getMaxHealth() + (2D * 5D));
        player.setHealth(player.getMaxHealth());
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdIzanagi(main));
    }

    @Override
    public boolean hasUsedIzanagi() {
        return hasUsedIzanagi;
    }

    @Override
    public void setHasUsedIzanagi(boolean hasUsedIzanagi) {
        this.hasUsedIzanagi = hasUsedIzanagi;
    }

    public boolean isKilledItachi() {
        return killedItachi;
    }

    public void setKilledItachi(boolean killedItachi) {
        this.killedItachi = killedItachi;
    }

    @Override
    public void useTsukuyomi() {
        tsukuyomiUses++;
    }

    @Override
    public int getTsukuyomiUses() {
        return tsukuyomiUses;
    }

    public void setTsukuyomiUses(int tsukuyomiUses) {
        this.tsukuyomiUses = tsukuyomiUses;
    }

    @Override
    public void useIzanami() {
        hasUsedIzanami = true;
    }

    @Override
    public boolean hasUsedIzanami() {
        return hasUsedIzanami;
    }

    public boolean isHasUsedIzanami() {
        return hasUsedIzanami;
    }

    public void setHasUsedIzanami(boolean hasUsedIzanami) {
        this.hasUsedIzanami = hasUsedIzanami;
    }

}
