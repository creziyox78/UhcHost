package fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdIzanagi;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdSharingan;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.GenjutsuItem;
import fr.lastril.uhchost.modes.naruto.v2.items.SusanoItem;
import fr.lastril.uhchost.modes.naruto.v2.izanami.IzanamiGoal;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Madara;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Obito;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Naruto;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Sasuke;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.PlayerKillEvent;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Itachi extends Role implements NarutoV2Role, CmdIzanagi.IzanagiUser, SusanoItem.SusanoUser, RoleCommand, GenjutsuItem.GenjutsuUser, CmdSharingan.SharinganUser {

    private static final int HEALTH_WHEN_SASUKE_DEATH = 2*2;
    private static final int DISTANCE_NARUTO_OROCHIMARU = 5;
    private IzanamiGoal izanamiGoal;

    private int sharinganUses;
    private boolean hasUsedIzanagi;

    private boolean killedUchiwa;

    private boolean hasUsedIzanami;

    private int tsukuyomiUses;
    private boolean completeIzanami = false;

    public Itachi() {
        super.addRoleToKnow(Kisame.class);
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new SusanoItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new GenjutsuItem(main).toItemStack());
    }

    @Override
    public void afterRoles(Player player) {
        player.setMaxHealth(24);
        player.setHealth(player.getMaxHealth());
        Bukkit.getScheduler().runTaskTimer(main, () -> {
            if(!killedUchiwa && isCompleteIzanami()){
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*5, 0, false, false));
            }
        }, 0, 20*60);
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
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTY0YjkyMWNhOGQ1NjIwMzk4MGU4MDcxNDNlYjZmM2NjMzAwNDExY2JkNDY0MDY1YmNjZGU1Njc4ZDJlYTE4YiJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.AKATSUKI;
    }

    @Override
    public String getRoleName() {
        return "Itachi";
    }

    @Override
    public String getDescription() {
        return "\n§7Son but est de gagner avec l’§cAkatsuki§7. \n" +
                " \n " +
                "§7§lItems : \n" +
                " \n " +
                "§7• Il dispose d’un item nommé \"§8Genjutsu§7\", lorsqu’il l’utilise, un menu s’affiche avec 3 pouvoirs différents (voir ci-dessous)\n" +
                " \n " +
                "§7• Il dispose d’un item nommé \"§8Susano§7\", celui-ci lui permet d'enflammer un ennemi dès qu'il le frappe, il gagne également l’effet §9Résistance 1§7, il reçoit aussi une épée Tranchant 7, il peut frapper avec son épée une fois toutes les 20 secondes, ce pouvoir dure 5 minutes et possède un délai d'utilisation de 20 minutes.\n" +
                " \n " +
                "§7§lCommandes :\n" +
                " \n " +
                "§8→ /ns sharingan <Joueur>§7, celle-ci lui permet d’avoir toutes les informations possible à propos du joueur : Son rôle, s’il a tué des joueurs, si oui le nombre et qui il a tué et combien de pomme d’or il lui reste, il peut l'utiliser seulement 2 fois dans la partie.\n" +
                " \n " +
                "§8→ /ns Izanagi§7, celle-ci lui permet de recevoir 5 pommes d’or et d’être entièrement régénéré, cependant il perd §c1 cœur§7 permanent, et il ne pourra plus utiliser le §8Susano§7, il peut l’utiliser qu’une seule fois.\n" +
                " \n " +
                "§7§lParticularités :\n" +
                " \n " +
                "§7• §dMadara§7, §dObito§7 et §6Sasuke peuvent utiliser le \"§8Genjutsu§7\" s’ils le ramassent. \n" +
                " \n " +
                "§7• S’il se fait ressusciter par §5Orochimaru§7 ou §5Kabuto§7, lorsque §aNaruto§7 sera dans un rayon de 5 blocs autour de lui, un message apparaîtra dans son chat et il passera du côté des §aShinobi§7. \n" +
                " \n " +
                "§7• Il dispose des effets §bVitesse 1§7, §6Résistance au feu 1§7 et §c2 cœurs §7supplémentaires. \n" +
                " \n " +
                "§7• A la mort de §6Sasuke§7 il obtient l’effet §8Faiblesse 1§7 et perd §c2 cœurs§7 permanents. \n" +
                " \n " +
                "§7• Il connaît l’identité de §cKisame§7.\n" +
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
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(joueur.getCamps() == Camps.OROCHIMARU){
            for (Entity entity : player.getNearbyEntities(DISTANCE_NARUTO_OROCHIMARU, DISTANCE_NARUTO_OROCHIMARU, DISTANCE_NARUTO_OROCHIMARU)) {
                if(entity instanceof Player){
                    Player players = (Player) entity;
                    if(players.getGameMode() != GameMode.SPECTATOR){
                        PlayerManager joueurs = main.getPlayerManager(players.getUniqueId());
                        if (joueurs.hasRole() && joueurs.getRole() instanceof Naruto) {
                            joueur.setCamps(Camps.SHINOBI);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"§aVous avez rencontré Naruto, donc vous rejoignez le camps des Shinobi.");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onKillUchiwa(PlayerKillEvent event){
        Player killer = event.getKiller();
        PlayerManager joueur = main.getPlayerManager(killer.getUniqueId());
        Player deadPlayer = event.getDeadPlayer();
        if (joueur.isAlive() && joueur.hasRole()) {
            if (joueur.getRole() instanceof Itachi) {
                PlayerManager deadJoueur = main.getPlayerManager(deadPlayer.getUniqueId());
                if(deadJoueur.getRole() instanceof Madara || deadJoueur.getRole() instanceof Itachi || deadJoueur.getRole() instanceof Sasuke || deadJoueur.getRole() instanceof Obito){
                    killedUchiwa = true;
                    killer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous venez de tuer un Uchiwa, vous n'avez plus§7Blindness I§a.");
                }
            }
        }
    }

    @Override
    public void onPlayerDeath(Player player) {
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if (joueur.hasRole()) {
            if(joueur.getRole() instanceof Sasuke){
                if (super.getPlayer() != null) {
                    Player itachi = super.getPlayer();
                    itachi.setMaxHealth(itachi.getMaxHealth() - HEALTH_WHEN_SASUKE_DEATH);
                    itachi.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false));
                    itachi.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Sasuke est mort, vous perdez donc "+(HEALTH_WHEN_SASUKE_DEATH/2)+" cœurs et vous obtenez l'effet Faiblesse 1");
                }
            }
        }
    }

    public int getSharinganUses() {
        return sharinganUses;
    }

    public void addSharinganUse() {
        this.sharinganUses++;
    }

    @Override
    public boolean hasUsedIzanagi() {
        return hasUsedIzanagi;
    }

    @Override
    public void setHasUsedIzanagi(boolean hasUsedIzanagi) {
        this.hasUsedIzanagi = hasUsedIzanagi;
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdIzanagi(main), new CmdSharingan(main));
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
        hasUsedIzanami = true;
    }

    @Override
    public boolean hasUsedIzanami() {
        return hasUsedIzanami;
    }

    @Override
    public boolean hasKilledUchiwa() {
        return killedUchiwa;
    }

    @Override
    public IzanamiGoal getIzanamiGoal() {
        return izanamiGoal;
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
    public void setIzanamiGoal(IzanamiGoal izanamiGoal) {
        this.izanamiGoal = izanamiGoal;
    }

    @Override
	public Chakra getChakra() {
		return Chakra.KATON;
	}

}

