package fr.lastril.uhchost.modes.naruto.v2.roles.jubi;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.biju.Biju;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdIzanagi;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.*;
import fr.lastril.uhchost.modes.naruto.v2.items.biju.JubiItem;
import fr.lastril.uhchost.modes.naruto.v2.izanami.IzanamiGoal;
import fr.lastril.uhchost.modes.naruto.v2.roles.JubiRole;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Itachi;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Sasuke;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.PlayerKillEvent;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.WorldUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Obito extends Role implements NarutoV2Role, RoleCommand, KamuiItem.KamuiUser, CmdIzanagi.IzanagiUser, SusanoItem.SusanoUser, JubiItem.JubiUser, RoleListener, JubiRole, GenjutsuItem.GenjutsuUser {

    private boolean usedIzanami, useNinjutsu, hasUseNinjutsu, hasUsedIzanagi;

    private int tsukuyomiUses, timeInvisibleRemining = 60;

    private double damageResistance;

    private Biju bijuTracked;

    private boolean killedUchiwa, completeIzanami = false;

    private final Map<UUID, Location> intialLocations;
    private IzanamiGoal izanamiGoal;

    //private double recupKuramaPoint = 0, recupShukakuPoint = 0, recupGyukiPoint = 0;

    public Obito() {
        this.intialLocations = new HashMap<>();
        super.addEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false),
                When.START);
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addRoleToKnow(Madara.class);
    }

    public void setBijuTracked(Biju bijuTracked) {
        this.bijuTracked = bijuTracked;
    }

    public Biju getBijuTracked() {
        return bijuTracked;
    }

    @Override
    public void giveItems(Player player) {
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        main.getInventoryUtils().giveItemSafely(player, new KamuiItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new NinjutsuItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new GenjutsuItem(main).toItemStack());
        if(narutoV2Manager.getNarutoV2Config().isBiju())
            main.getInventoryUtils().giveItemSafely(player, new TrackerBijuItem(main).toItemStack());
    }

    @Override
    public void checkRunnable(Player player) {
    	super.checkRunnable(player);
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if (joueur.isAlive() && joueur.hasRole()) {
            if (joueur.getRole() instanceof Obito) {
                damageResistance = (player.getMaxHealth() - player.getHealth());
            }
        }
    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {
        Player killer = event.getKiller();
        PlayerManager joueur = main.getPlayerManager(killer.getUniqueId());
        Player deadPlayer = event.getDeadPlayer();
        PlayerManager deadJoueur = main.getPlayerManager(deadPlayer.getUniqueId());
        if (joueur.isAlive() && joueur.hasRole()) {
            if (joueur.getRole() instanceof Obito) {
                if (killer.getHealth() + (3D * 2D) >= killer.getMaxHealth())
                    killer.setHealth(killer.getMaxHealth());
                else
                    joueur.getPlayer().setHealth(killer.getHealth() + (3D * 2D));

                if(deadJoueur.getRole() instanceof Madara || deadJoueur.getRole() instanceof Itachi || deadJoueur.getRole() instanceof Sasuke || deadJoueur.getRole() instanceof Obito){
                    killedUchiwa = true;
                    killer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous venez de tuer un Uchiwa, vous n'avez plus§7Blindness I§a.");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.isAlive() && joueur.hasRole()) {
                if (joueur.getRole() instanceof Obito) {
                    Obito obito = (Obito) joueur.getRole();
                    if (obito.isUseNinjutsu() && !event.isCancelled()) {
                        double newDamage = (event.getDamage() - ((damageResistance / 40) * event.getDamage()));
                        if (newDamage <= 0) {
                            newDamage = 1;
                        }
                        event.setDamage(newDamage);
                    }
                }
            }

        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager joueur = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                PlayerManager damagerJoueur = UhcHost.getInstance().getPlayerManager(damager.getUniqueId());
                if (damagerJoueur.getRole() instanceof Obito) {
                    Obito obito = (Obito) damagerJoueur.getRole();
                    if (obito.isUseNinjutsu()) {
                        event.setCancelled(true);
                    }
                }
                if (joueur.getRole() instanceof Obito) {
                    double newDamage = (event.getDamage() - ((damageResistance / 40) * event.getDamage()));
                    if (newDamage <= 0) {
                        newDamage = 1;
                    }
                    event.setDamage(newDamage);
                }

            } else if (event.getDamager() instanceof Projectile) {
                Projectile damager = (Projectile) event.getDamager();
                if(damager.getShooter() instanceof Player){
                    Player shooter = (Player) damager.getShooter();
                    PlayerManager damagerJoueur = main.getPlayerManager(shooter.getUniqueId());
                    if (damagerJoueur.getRole() instanceof Obito) {
                        Obito obito = (Obito) damagerJoueur.getRole();
                        if (obito.isUseNinjutsu()) {
                            event.setCancelled(true);
                        } else {
                            double newDamage = (event.getDamage() - ((damageResistance / 40) * event.getDamage()));
                            if (newDamage <= 0) {
                                newDamage = 1;
                            }
                            event.setDamage(newDamage);
                        }

                    }
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBucket(PlayerBucketEmptyEvent event) {
        Player damager = event.getPlayer();
        if (event.getBucket() == Material.WATER_BUCKET)
            return;
        PlayerManager joueur = UhcHost.getInstance().getPlayerManager(damager.getUniqueId());
        if (joueur.getRole() instanceof Obito) {
            Obito obito = (Obito) joueur.getRole();
            event.setCancelled(obito.isUseNinjutsu());
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerManager joueur = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
        if (joueur.hasRole()) {
            if (joueur.getRole() instanceof Obito && player.getGameMode() == GameMode.SURVIVAL) {
                Obito obito = (Obito) joueur.getRole();
                if (obito.isUseNinjutsu()) {
                    if (event.getTo().distance(event.getFrom()) > 0.2) {
                        WorldUtils.spawnParticle(player.getLocation(), EnumParticle.REDSTONE);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerManager joueur = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
            if (joueur.getRole() instanceof Obito) {
                Obito obito = (Obito) joueur.getRole();
                event.setCancelled(obito.isUseNinjutsu());
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
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWRiYWEzMDI4OGE3MTBhMjE0MjhiOWQwNzY5NjQ0NWVlM2UxMzIxNzg4MDczOGM3OGUyOGU5YjhjOWM5MmQ5ZSJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.JUBI;
    }

    @Override
    public String getRoleName() {
        return "Obito";
    }

    @Override
    public String getDescription() {
        return "\n§7Son but est de gagner avec §dMadara§7. \n" +
                " \n " +
                "§7§lItems :\n" +
                " \n " +
                "§7• Il dispose d'un item nommé \"§8Ninjutsu Spatio-Temporel§7\", il lui permet de devenir invisible même en portant son armure. Son pouvoir dure 1 minute, il peut le désactiver à tout moment, il possède un délai de 5 minutes, cependant lorsqu’il utilise son pouvoir, on pourra voir ses traces de pas (particules de redstone)\n" +
                " \n " +
                "§7• Il dispose d’un autre item nommé “§8Kamui”§7, celui-ci lui affiche un menu dans lequel il a deux choix, le premier est nommé “§8Arimasu§7”, celui-ci lui permet de le téléporter dans le monde lié à §8Kamui§7 et ceci pendant 10 minutes. Le deuxième est nommé “§8Sonohoka§7”, il lui permet de téléporter un joueur dans un rayon de 20 blocs autour de lui, dans le monde lié à §8Kamui§7 et cela pendant 5 minutes. “§8Arimasu§7” possède un délai de 5 minutes, “§7Sonohoka§8” possède un délai de 15 minutes. Dans le monde de §8Kamui§7 on ne peut pas poser de blocs, sauf les seaux de laves et d'eaux qui s'enlèvent automatiquement au bout d'une minute. On ne peut pas prendre de dégâts de chutes. Il peut utiliser la commande §8/ns yameru§7 pour téléporter lui et le joueur (s’il en a téléporté un) dans le monde normal, suite à sa commande ou si le temps est écoulé, ils seront téléportés à l’endroit exact où ils se situaient avant qu’§dObito§7 utilise son pouvoir.\n" +
                " \n " +
                "• Il dispose d’un item nommé \"§8Genjutsu§7\", Lorsqu’il l’utilise, un menu s’affiche avec 3 pouvoirs différents (voir ci-dessous)\n" +
                " \n " +
                "§7• Il dispose d’une boussole nommée \"§8Biju§7\", Lorsqu’il clique sur celle-ci, un menu s’ouvre avec affiché les 6 Biju, lorsqu’il clique sur l’un d’entre eux, il commence à traquer celui-ci ou le réceptacle (joueur possédant le Biju), il obtient aussi dans son chat à quel moment va apparaître le Biju ainsi que ces coordonnées.\n" +
                " \n " +
                "§7§lCommandes :\n" +
                " \n " +
                "§8→ /ns izanagi§7, celle-ci lui permet de recevoir 5 pommes d’or et d’être entièrement régénéré, cependant il perd §c1 cœur§7 permanent, et il ne pourra plus utiliser le §8Susano§7, s'il avait réussi à ramasser l'item auparavant, il peut l’utiliser qu’une seule fois dans la partie.\n" +
                " \n " +
                "§7§lParticularités :\n" +
                " \n " +
                "§7• Il dispose de la liste de tous les membres de l'§cAkatsuki§7 et de l’identité de §dMadara§7.\n" +
                " \n " +
                "§7• Il dispose des effets §bVitesse 1§7 et §6Résistance au feu 1§7.\n" +
                " \n " +
                "§7• Lorsqu’il se trouve avec un nombre de vie plus bas, il obtient un certain pourcentage de résistance, il obtient 2% de résistance par demi-cœur en moins, donc s’il se trouve à la moitié de sa vie, il possède 20% de résistance (l’équivalent de l’effet Résistance 1), il peut aussi aller jusqu’à 38% de résistance maximum (1 demi-cœur).\n" +
                " \n " +
                "§7• Il possède la particularité de parler avec §dMadara§7 dans le chat, il lui suffit simplement d’écrire dans le chat avec l’aide du préfixe \"§8!§7\" pour pouvoir communiquer avec lui.\n" +
                " \n " +
                "§7• Lorsqu’il tue un joueur, il régénère de §c3 cœurs§7.\n" +
                " \n " +
                "• Il dispose de la nature de Chakra : §cKaton\n" +
                " \n " +
                "§7§lGenjutsu :\n" +
                " \n " +
                "§7Tsukuyomi : Lorsqu’il l’utilise, tous les joueurs se trouvant dans un rayon de 20 blocs autour de lui seront immobilisés pendant 8 secondes, ce pouvoir est utilisable 2 fois dans la partie, il ne peut pas taper les personnes figées.\n" +
                " \n " +
                "Attaque : Lorsqu’il l’utilise, il a une liste de tous les joueurs se trouvant dans un rayon de 20 blocs autour de lui, lorsqu’il choisit un joueur, il sera téléporté derrière sa cible, il possède un délai de 5 minutes.\n" +
                " \n " +
                "Izanami : Lorsqu'il l’utilise, il a une liste de tous les joueurs se trouvant dans un rayon de 20 blocs autour de lui, lorsqu'il choisit un joueur, il devra accomplir 2 objectifs sur le joueur ciblé et celui-ci devra accomplir 1 objectif dont il ne sera pas au courant (A, B et C), les objectifs sont choisit de manière aléatoire sur deux listes différentes qui se trouve ci-dessous, et à la suite de ces objectifs il rejoindra le camp du joueur qui lui a infligé §8Izanami§7 qu’on va appeler le joueur §8Izanami§7. Voici les objectifs qui concernent lui et le joueur ciblé :\n" +
                "- Infliger un total de 15 coups d’épée au joueur ciblé\n" +
                "- Recevoir un coup d’épée de la part du joueur ciblé\n" +
                "- Donner une pomme dorée au joueur ciblé\n" +
                "- Forcer le joueur ciblé à lui donner une pomme dorée\n" +
                "- Rester à côté du joueur ciblé pendant un total de 5 minutes\n" +
                "- Poser un seau de lave sous les pieds du joueur ciblé\n" +
                "Ensuite voici les objectifs qui concernent uniquement le joueur ciblé à son égard :\n" +
                "- Marcher 1 kilomètre\n" +
                "- Tuer un joueur\n" +
                "- Manger 5 pommes d’orées\n" +
                "Lorsqu’il clique à nouveau §8Izanami§7 après l’avoir utilisé une fois, il peut voir l’évolution des objectifs.\n" +
                "Une fois §8Izanami§7 terminer, le joueur ciblé rejoindra le camp du joueur §8Izanami§7 et lui recevra l’effet §lCécité 1§7 pendant 5 secondes toutes les minutes pendant tout le reste de la partie.";
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

    @Override
    public String sendList() {
        String list = Messages.NARUTO_PREFIX.getMessage() + "Voici la liste entière de l'Akatsuki : \n";
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return null;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        for (PlayerManager joueur : narutoV2Manager.getPlayerManagersWithCamps(Camps.AKATSUKI)) {
            if (joueur.isAlive()) list += "§c- " + joueur.getPlayerName() +"\n";
        }
        return list;
    }

    @Override
    public void afterRoles(Player player) {
        player.sendMessage(sendList());
        Bukkit.getScheduler().runTaskTimer(main, () -> {
            if(!killedUchiwa && isCompleteIzanami()){
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*5, 0, false, false));
            }
        }, 0, 20*60);
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        if(narutoV2Manager.getNarutoV2Config().isBiju()){
            setBijuTracked(narutoV2Manager.getBijuManager().getBijuListClass().get(0));
            Bukkit.getScheduler().runTaskTimer(main, () -> {
                for(PlayerManager joueur : narutoV2Manager.getPlayerManagersWithRole(Obito.class)){
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




            /*if (!UhcHost.getInstance().getConfiguration().containsRoleInComposition(Minato.class) && !UhcHost.getInstance().getConfiguration().containsRoleInComposition(Naruto.class)) {
                main.getNarutoV2Manager().getBijuManager().setRecupKurama(true);
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aMinato et Naruto§e ne font pas partie de la composition. Vous n'avez pas besoin de récupérer l'ADN de leur Bijû.");
            } else {
                for(Joueur joueur : main.getNarutoV2Manager().getJoueursWithRole(Minato.class)){
                    if(super.getPlayer() != null){
                        new KuramaRecupTask(main.getJoueur(this.getPlayer().getUniqueId()), joueur, main).runTaskTimer(main, 0, 1);
                    }
                }
                for(Joueur joueur : main.getNarutoV2Manager().getJoueursWithRole(Naruto.class)){
                    if(super.getPlayer() != null){
                        new KuramaRecupTask(main.getJoueur(this.getPlayer().getUniqueId()), joueur, main).runTaskTimer(main, 0, 1);
                    }
                }
            }

            if (!UhcHost.getInstance().getConfiguration().containsRoleInComposition(KillerBee.class)) {
                main.getNarutoV2Manager().getBijuManager().setRecupGyuki(true);
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aKiller Bee§e ne fait pas partie de la composition. Vous n'avez pas besoin de récupérer l'ADN de son Bijû.");
            } else {
                for(Joueur joueur : main.getNarutoV2Manager().getJoueursWithRole(KillerBee.class)){
                    if(super.getPlayer() != null){
                        new GyukiRecupTask(main.getJoueur(this.getPlayer().getUniqueId()), joueur, main).runTaskTimer(main, 0, 1);
                    }
                }

            }

            if (!UhcHost.getInstance().getConfiguration().containsRoleInComposition(Gaara.class)) {
                main.getNarutoV2Manager().getBijuManager().setRecupShukaku(true);
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eGaara ne fait pas partie de la composition. Vous n'avez pas besoin de récupérer l'ADN de son Bijû.");
            } else {
                for(Joueur joueur : main.getNarutoV2Manager().getJoueursWithRole(Gaara.class)){
                    if(super.getPlayer() != null){
                        new ShukakuRecupTask(main.getJoueur(this.getPlayer().getUniqueId()), joueur, main).runTaskTimer(main, 0, 1);
                    }
                }
            }*/

    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdIzanagi(main));
    }

    @Override
    public int getArimasuCooldown() {
        return 5 * 60;
    }

    @Override
    public int getSonohokaCooldown() {
        return 15 * 60;
    }

    @Override
    public Map<UUID, Location> getInitialsLocation() {
        return this.intialLocations;
    }

    @Override
    public double getSonohokaDistance() {
        return 20;
    }

    @Override
    public boolean hasUsedIzanagi() {
        return hasUsedIzanagi;
    }

    @Override
    public void setHasUsedIzanagi(boolean hasUsedIzanagi) {
        this.hasUsedIzanagi = hasUsedIzanagi;
    }

    public boolean isUseNinjutsu() {
        return useNinjutsu;
    }

    public void setUseNinjutsu(boolean useNinjutsu) {
    	
        this.useNinjutsu = useNinjutsu;
        if(useNinjutsu){
            //main.getInvisibleTeam().addEntry(this.getPlayer().getName());
        }else{
            //main.getInvisibleTeam().removeEntry(this.getPlayer().getName());
        }
    }

    public int getTimeInvisibleRemining() {
        return timeInvisibleRemining;
    }

    public void setTimeInvisibleRemining(int timeInvisibleRemining) {
        this.timeInvisibleRemining = timeInvisibleRemining;
    }

    public boolean isHasUseNinjutsu() {
        return hasUseNinjutsu;
    }

    public void setHasUseNinjutsu(boolean hasUseNinjutsu) {
        this.hasUseNinjutsu = hasUseNinjutsu;
    }

    public double getDamageResistance() {
        return damageResistance;
    }

    public void setDamageResistance(double damageResistance) {
        this.damageResistance = damageResistance;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.KATON;
    }


    @Override
    public void useTsukuyomi() {
        tsukuyomiUses++;
    }

    @Override
    public int getTsukuyomiUses() {
        return tsukuyomiUses;
    }

    @Override
    public void useIzanami() {
        usedIzanami = true;
    }

    @Override
    public boolean hasUsedIzanami() {
        return usedIzanami;
    }

    @Override
    public boolean hasKilledUchiwa() {
        return killedUchiwa;
    }

    @Override
    public boolean isCompleteIzanami() {
        return completeIzanami;
    }

    @Override
    public void setCompleteIzanami(boolean completeIzanami) {
        this.completeIzanami = completeIzanami;
    }

    @Override
    public IzanamiGoal getIzanamiGoal() {
        return izanamiGoal;
    }

    @Override
    public void setIzanamiGoal(IzanamiGoal izanamiGoal) {
        this.izanamiGoal = izanamiGoal;
    }
}
