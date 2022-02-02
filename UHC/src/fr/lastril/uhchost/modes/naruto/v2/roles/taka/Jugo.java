package fr.lastril.uhchost.modes.naruto.v2.roles.taka;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.MarqueMauditeItem;
import fr.lastril.uhchost.modes.naruto.v2.items.biju.JubiItem;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Itachi;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Nagato;
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.Kimimaro;
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.Orochimaru;
import fr.lastril.uhchost.modes.naruto.v2.tasks.JugoSkinsTask;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.Reflection;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class Jugo extends Role implements NarutoV2Role, RoleListener, MarqueMauditeItem.MarqueMauditeUser {

    private final ScoreboardTeam scoreboardTeam;

    private boolean orochimaruDead, itachiDead, seeSkins = true, inMarqueMaudite;

    public Jugo() {
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        super.addRoleToKnow(Kimimaro.class);

        this.scoreboardTeam = new ScoreboardTeam(new Scoreboard(), "nametag");
        this.scoreboardTeam.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new MarqueMauditeItem(main).toItemStack());
    }

    @Override
    public void afterRoles(Player player) {
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        joueur.setCamps(Camps.OROCHIMARU);

        this.sendNametagsTeam(player);

        new JugoSkinsTask(main, this).runTaskTimer(main, 20 * 60 * 20, 20 * 60 * 20);
    }

    @EventHandler
    public void Craft(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();
        if(result.getType() == Material.NETHER_STAR){
            if (result.isSimilar(new JubiItem(main).toItemStack())) {
                if (super.getPlayer() != null) {
                    Player jugoPlayer = super.getPlayer();
                    main.getPlayerManager(jugoPlayer.getUniqueId()).setCamps(Camps.SHINOBI);
                    jugoPlayer.sendMessage("§5Jûbi§e vient d'être invoqué. Vous gagnez désormais avec les§a Shinobis.");
                }
            }
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if (player.getUniqueId().equals(this.getPlayerId())) {
            if (joueur.isAlive()) {
                this.sendNametagsTeam(player);
                if (!seeSkins || inMarqueMaudite) {
                    this.hideFullyPlayers(player);
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "L'indentité des joueurs vous est brouillés.");
                }
            }
        } else {
            if (!seeSkins || inMarqueMaudite) {
                if (joueur.isAlive()) {
                    if (this.getPlayer() != null){
                        this.hideFullyPlayer(this.getPlayer(), player);
                    }
                }
            }
        }
    }

    private List<PlayerManager> getPlayersToHide() {
        return main.getPlayerManagerAlives()
                .stream()
                .filter(PlayerManager::isOnline)
                .filter(joueur -> !joueur.getUuid().equals(super.getPlayerId()))
                .filter(joueur -> !(joueur.getRole() instanceof Sasuke))
                .filter(joueur -> !(joueur.getRole() instanceof Kimimaro))
                .collect(Collectors.toList());
    }

    private List<String> getPlayersNamesToHide() {
        return this.getPlayersToHide()
                .stream()
                .map(PlayerManager::getPlayerName)
                .collect(Collectors.toList());
    }

    @Override
    public void onPlayerDeath(Player player) {
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(main.getGamemanager().getModes() != Modes.NARUTO) return;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        if (joueur.hasRole()) {
            if (joueur.getRole() instanceof Orochimaru) {
                if (super.getPlayer() != null) {
                    if (itachiDead) {
                        Player sasukePlayer = super.getPlayer();
                        itachiDead = true;
                        main.getPlayerManager(sasukePlayer.getUniqueId()).setCamps(Camps.AKATSUKI);
                        sasukePlayer.sendMessage("§cVous gagnez avec l'§cAkatsuki car Itachi et Orochimaru sont morts. Voici l'identité de Nagato et Sasuke: ");
                        for (PlayerManager nagato : narutoV2Manager.getPlayerManagersWithRole(Nagato.class)) {
                            sasukePlayer.sendMessage("§c - Nagato : " + nagato.getPlayerName());
                        }
                        for (PlayerManager sasuke : narutoV2Manager.getPlayerManagersWithRole(Sasuke.class)) {
                            sasukePlayer.sendMessage("§6 - Sasuke : " + sasuke.getPlayerName());
                        }
                    } else {
                        orochimaruDead = true;
                        Player sasukePlayer = super.getPlayer();
                        main.getPlayerManager(sasukePlayer.getUniqueId()).setCamps(Camps.TAKA);
                        sasukePlayer.sendMessage("§5Orochimaru§e vient de mourir, vous gagnez uniquement avec votre camp. Voici l'identité de Sasuke: ");
                        for (PlayerManager sasuke : narutoV2Manager.getPlayerManagersWithRole(Sasuke.class)) {
                            sasukePlayer.sendMessage("§6 - Sasuke : " + sasuke.getPlayerName());
                        }
                    }

                }
            } else if (joueur.getRole() instanceof Itachi) {
                if (super.getPlayer() != null) {
                    if (orochimaruDead) {
                        Player sasukePlayer = super.getPlayer();
                        itachiDead = true;
                        main.getPlayerManager(sasukePlayer.getUniqueId()).setCamps(Camps.AKATSUKI);
                        sasukePlayer.sendMessage("§cItachi§e vient de mourir, vous gagnez avec l'§cAkatsuki. Voici l'identité de Nagato: ");
                        for (PlayerManager nagato : narutoV2Manager.getPlayerManagersWithRole(Nagato.class)) {
                            sasukePlayer.sendMessage("§c - Nagato : " + nagato.getPlayerName());
                        }
                    } else {
                        itachiDead = true;
                    }
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
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTFlODM5MmJhNmNlNjE1Mjc1NzRjMWRiOTRlZmI5YTM3MGY2YzUyYzM1NmQzNzkxNjI3ZmZmYjZhMjkxZWRmMCJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.TAKA;
    }

    @Override
    public String getRoleName() {
        return "Jugô";
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

    public void sendNametagsTeam(Player player) {
        PacketPlayOutScoreboardTeam packetCreate = new PacketPlayOutScoreboardTeam(this.scoreboardTeam, 0);
        Reflection.sendPacket(player, packetCreate);
    }

    public void hidePlayersNameTags(Player player, List<String> names) {
        PacketPlayOutScoreboardTeam packetPlayers = new PacketPlayOutScoreboardTeam(this.scoreboardTeam, names, 3);
        Reflection.sendPacket(player, packetPlayers);
    }

    public void hidePlayersSkins(Player player, List<Player> players) {
        for (Player target : players) {
            this.hidePlayerSkin(player, target);
        }
    }

    private void hidePlayerSkin(Player player, Player target) {
        //IdentityChanger.changeSkinForPlayer(target, new SasukeSkin(), player, main.getJoueur(target.getUniqueId()).getOriginalSkin());
    }

    public void resetPlayersSkins(Player player, List<Player> players) {
        for (Player target : players) {
            //IdentityChanger.changeSkinForPlayer(target, main.getJoueur(target.getUniqueId()).getOriginalSkin(), player, null);
        }
    }

    public void hideFullyPlayers(Player player){
        this.hidePlayersSkins(player, this.getPlayersToHide().stream().map(PlayerManager::getPlayer).collect(Collectors.toList()));
        new BukkitRunnable(){

            @Override
            public void run() {
                //AFTER CHANGED ALL SKINS
                Jugo.this.hidePlayersNameTags(player, Jugo.this.getPlayersNamesToHide());
            }
        }.runTaskLater(main, 11);
    }

    public void hideFullyPlayer(Player player, Player target){
        this.hidePlayerSkin(player, target);
        new BukkitRunnable(){

            @Override
            public void run() {
                //AFTER CHANGED ALL SKINS
                Jugo.this.hidePlayersNameTags(player, Jugo.this.getPlayersNamesToHide());
            }
        }.runTaskLater(main, 11);
    }


    public void deleteNametagsTeam(Player player) {
        PacketPlayOutScoreboardTeam packetDelete = new PacketPlayOutScoreboardTeam(this.scoreboardTeam, 1);
        Reflection.sendPacket(player, packetDelete);
    }

    public boolean isSeeSkins() {
        return seeSkins;
    }

    public void setSeeSkins(boolean seeSkins) {
        this.seeSkins = seeSkins;
        if (seeSkins) {
            if (this.getPlayer() != null) {
                this.deleteNametagsTeam(this.getPlayer());
                this.sendNametagsTeam(this.getPlayer());
                this.resetPlayersSkins(this.getPlayer(), this.getPlayersToHide().stream().map(PlayerManager::getPlayer).collect(Collectors.toList()));
            }
        } else {
            if (this.getPlayer() != null) {
                this.hideFullyPlayers(this.getPlayer());
            }
        }
    }

    @Override
    public void onUseMarqueMaudite(UhcHost main, Player player, PlayerManager joueur) {
        if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60 * 5, 1, false, false));

        if (player.hasPotionEffect(PotionEffectType.SPEED))
            player.removePotionEffect(PotionEffectType.SPEED);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 5, 1, false, false));

        player.setMaxHealth(player.getMaxHealth() + (3D * 2D));
        player.setHealth(player.getHealth() + (3D * 2D));

        joueur.setRoleCooldownMarqueMaudite(60 * 20);
        joueur.sendTimer(player, joueur.getRoleCooldownMarqueMaudite(), player.getItemInHand());

        this.inMarqueMaudite = true;

        if(seeSkins) {
            this.hideFullyPlayers(player);
            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "L'indentité des joueurs vous est brouillé.");
        }

        new BukkitRunnable() {

            @Override
            public void run() {
                if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);

                if (player.hasPotionEffect(PotionEffectType.SPEED))
                    player.removePotionEffect(PotionEffectType.SPEED);

                player.setMaxHealth(player.getMaxHealth() - (3D * 2D) - (1.5D * 2));
                Jugo.super.givePermanentEffects(player);

                Jugo.this.inMarqueMaudite = false;
                if(Jugo.this.seeSkins) {
                    Jugo.this.deleteNametagsTeam(player);
                    Jugo.this.sendNametagsTeam(player);
                    Jugo.this.resetPlayersSkins(player, Jugo.this.getPlayersToHide().stream().map(PlayerManager::getPlayer).collect(Collectors.toList()));
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "L'indentité des joueurs ne vous est plus brouillé.");
                }
            }
        }.runTaskLater(main, 20 * 60 * 5);
        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());

    }
}
