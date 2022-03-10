package fr.lastril.uhchost.modes.bleach.kyoraku;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Kyoraku;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class KyorakuDuelManager {

    private PlayerManager playerManager1, playerManager2;
    private Kyoraku kyoraku;
    private Location centerArena;
    private final UhcHost main = UhcHost.getInstance();
    private RulesFight rulesFight;
    private Cuboid arena;
    private final Map<Player, Location> playerLocation = new HashMap<>();

    public KyorakuDuelManager(Kyoraku kyoraku) {
        this.kyoraku = kyoraku;
    }

    private void saveLocation(Player player1, Player player2){
        playerLocation.put(player1, player1.getLocation());
        playerLocation.put(player2, player2.getLocation());
    }

    public void setPlayerManager1(PlayerManager playerManager1) {
        this.playerManager1 = playerManager1;
    }

    public void setPlayerManager2(PlayerManager playerManager2) {
        this.playerManager2 = playerManager2;
    }

    public void teleportPlayersInArena(Player player1, Player player2){
        saveLocation(player1, player2);
        setPlayerManager1(main.getPlayerManager(player1.getUniqueId()));
        setPlayerManager2(main.getPlayerManager(player2.getUniqueId()));
        player1.teleport(new Location(centerArena.getWorld(), centerArena.getX() + 11, centerArena.getY() + 2, centerArena.getZ() + 11, 135, 0));
        player2.teleport(new Location(centerArena.getWorld(), centerArena.getX() - 11, centerArena.getY() + 2, centerArena.getZ() - 11, -45, 0));
        Bukkit.getPluginManager().registerEvents(new KyorakuDuelListener(this), main);
        Bukkit.getPluginManager().callEvent(new KyorakuStartDuelEvent(this, player1, player2));
        player2.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§9Kyoraku vient de lancer un duel contre vous ! Voici la règle du combat : §6" + getRulesFight().getMessage());
        player1.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§9Voici la règle du combat : §6" + getRulesFight().getMessage());
    }

    public void teleportWinnerInPreviousLocation(Player winner){
        winner.teleport(playerLocation.get(winner));
    }

    public void deleteArena(Location location){
        new Cuboid(location, 15, 5).forEach(block -> block.setType(Material.AIR));
    }

    public void generateArena(Location location){
        arena = new Cuboid(location, 15, 5);
        centerArena = arena.getCenter();
        arena.getBlocks().forEach(block -> {
            block.setType(Material.STAINED_GLASS);
            block.setData((byte)15);
        });
        Cuboid emptyCuboid = new Cuboid(location, 14, 3);
        emptyCuboid.forEach(block -> block.setType(Material.AIR));
    }

    public void setRulesFight(int number) {
        for(RulesFight rulesFight : RulesFight.values()){
            if(rulesFight.number == number){
                this.rulesFight = rulesFight;
            }
        }
    }

    public PlayerManager getPlayerManager1() {
        return playerManager1;
    }

    public PlayerManager getPlayerManager2() {
        return playerManager2;
    }

    public Cuboid getArena() {
        return arena;
    }

    public enum RulesFight{
        RULES_FIGHT_1("§eVous ne pouvez pas utiliser de pomme en or.", 1),
        RULES_FIGHT_2("§eVos effets ont été retirés pendant le combat.", 2),
        RULES_FIGHT_3("§eVous ne pouvez pas utiliser vos pouvoirs.", 3)
        ;

        private final String message;
        private final int number;

        RulesFight(String message, int number){
            this.message = message;
            this.number = number;
        }

        public String getMessage() {
            return message;
        }

        public int getNumber() {
            return number;
        }
    }

    public RulesFight getRulesFight() {
        return rulesFight;
    }
}
