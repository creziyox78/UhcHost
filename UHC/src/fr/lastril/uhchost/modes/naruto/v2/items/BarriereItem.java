package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.Sakon;
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.Ukon;
import fr.lastril.uhchost.modes.naruto.v2.sakonukon.SakonUkonManager;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.Cuboid;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class BarriereItem extends QuickItem {

    private final UhcHost main;
    private NarutoV2Manager narutoV2Manager;

    public BarriereItem(UhcHost main) {
        super(Material.NETHER_STAR);
        this.main = main;
        super.setName("§5Barrière Protectrice");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
                if (isRightRole(joueur.getRole())) {
                    SakonUkonManager sakonUkonManager = narutoV2Manager.getSakonUkonManager();
                    if (!sakonUkonManager.hasUsedCage()) {
                        if (sakonUkonManager.getPowersWaits().containsKey("BARRIERE")) {
                            UUID clicker = sakonUkonManager.getPowersWaits().get("BARRIERE");
                            if (!clicker.equals(player.getUniqueId())) {
                                Player first = Bukkit.getPlayer(clicker);
                                PlayerManager joueurFirst = main.getPlayerManager(first.getUniqueId());

                                if (first.getLocation().distance(player.getLocation()) <= 40) {

                                    Location firstCorner = player.getLocation();
                                    firstCorner.setY(0);


                                    Location secondCorner = first.getLocation();
                                    secondCorner.setY(256);

                                    this.buildPrisonGlass(firstCorner, secondCorner);

                                    sakonUkonManager.setHasUsedCage(true);

                                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                                    first.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                                } else {
                                    player.sendMessage(Messages.error("Vous devez être à 40 blocs de distance de " + (joueur.getRole() instanceof Sakon ? "Ukon" : "Sakon")));
                                    first.sendMessage(Messages.error("Vous devez être à 40 blocs de distance de " + (joueurFirst.getRole() instanceof Sakon ? "Ukon" : "Sakon")));
                                }

                                sakonUkonManager.getPowersWaits().remove("BARRIERE");
                            }
                        } else {
                            sakonUkonManager.addPowerWait("BARRIERE", player, joueur.getRole() instanceof Sakon ? "Ukon" : "Sakon");
                        }
                    } else {
                        player.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                    }
                } else {
                    player.sendMessage(Messages.not("Sakon ou Ukon"));
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            }
        });
    }

    private boolean isRightRole(Role role) {
        return role instanceof Sakon || role instanceof Ukon;
    }


    private void buildPrisonGlass(Location firstCorner, Location secondCorner) {
        Map<Location, Pair<Material, Byte>> originalBlocks = new HashMap<>();
        List<Block> wallsBlocks = getWalls(firstCorner, secondCorner);

        narutoV2Manager.getSakonUkonManager().getUnbreakableCageBlocks().addAll(wallsBlocks.stream().map(Block::getLocation).collect(Collectors.toList()));

        /*wallsBlocks.forEach(block -> {
            originalBlocks.put(block.getLocation(), new Pair<>(block.getType(), block.getData()));
            block.setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 10, false);
        });

        new BukkitRunnable() {

            @Override
            public void run() {
                for (Map.Entry<Location, Pair<Material, Byte>> e : originalBlocks.entrySet()) {
                    e.getKey().getBlock().setTypeIdAndData(e.getValue().getFirst().getId(), e.getValue().getSecond(), false);
                }
                main.getNarutoV2Manager().getSakonUkonManager().getUnbreakableCageBlocks().clear();
            }
        }.runTaskLater(main, 20 * 60 * 5);*/

    }

    public static List<Block> getWalls(Location firstCorner, Location secondCorner){
        List<Block> wallsBlocks = new ArrayList<>();

        Location min = getMinimumLocation(firstCorner.getWorld(), firstCorner, secondCorner);
        Location max = getMaximumLocation(firstCorner.getWorld(), firstCorner, secondCorner);

        //X PLANE
        wallsBlocks.addAll(new Cuboid(
                new Location(firstCorner.getWorld(), min.getX(), firstCorner.getY(), firstCorner.getZ()),
                new Location(secondCorner.getWorld(), min.getX(), secondCorner.getY(), secondCorner.getZ())).getBlocks());
        wallsBlocks.addAll(new Cuboid(
                new Location(firstCorner.getWorld(), max.getX(), firstCorner.getY(), firstCorner.getZ()),
                new Location(secondCorner.getWorld(), max.getX(), secondCorner.getY(), secondCorner.getZ())).getBlocks());

        //Z PLANE
        wallsBlocks.addAll(new Cuboid(
                new Location(firstCorner.getWorld(), firstCorner.getX(), firstCorner.getY(), min.getZ()),
                new Location(secondCorner.getWorld(), secondCorner.getX(), secondCorner.getY(), min.getZ())).getBlocks());
        wallsBlocks.addAll(new Cuboid(
                new Location(firstCorner.getWorld(), firstCorner.getX(), firstCorner.getY(), max.getZ()),
                new Location(secondCorner.getWorld(), secondCorner.getX(), secondCorner.getY(), max.getZ())).getBlocks());

        //TOP
        wallsBlocks.addAll(new Cuboid(
                new Location(firstCorner.getWorld(), firstCorner.getX(), max.getY(), firstCorner.getZ()),
                new Location(secondCorner.getWorld(), secondCorner.getX(), max.getY(), secondCorner.getZ())).getBlocks());

        //GROUND
        wallsBlocks.addAll(new Cuboid(
                new Location(firstCorner.getWorld(), firstCorner.getX(), min.getY(), firstCorner.getZ()),
                new Location(secondCorner.getWorld(), secondCorner.getX(), min.getY(), secondCorner.getZ())).getBlocks());
        return wallsBlocks;
    }

    public static Location getMinimumLocation(World world, Location loc1, Location loc2) {
        return new Location(
                world,
                Math.min(loc1.getX(), loc2.getX()),
                Math.min(loc1.getY(), loc2.getY()),
                Math.min(loc1.getZ(), loc2.getZ())
        );
    }

    public static Location getMaximumLocation(World world, Location loc1, Location loc2) {
        return new Location(
                world,
                Math.max(loc1.getX(), loc2.getX()),
                Math.max(loc1.getY(), loc2.getY()),
                Math.max(loc1.getZ(), loc2.getZ())
        );
    }

}
