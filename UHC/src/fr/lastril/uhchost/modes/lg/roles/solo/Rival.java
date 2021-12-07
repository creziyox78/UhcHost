package fr.lastril.uhchost.modes.lg.roles.solo;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Rival extends Role implements LGRole {



    @Override
    public void giveItems(Player player) {

    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    protected void onDay(Player player) {

    }

    @Override
    public void onNewEpisode(Player player) {
        if(main.gameManager.episode == 6){
            StringBuilder listString = new StringBuilder(Messages.LOUP_GAROU_PREFIX.getMessage() + "§eParmis ces 6 pseudos");
            List<PlayerManager> listRival = new ArrayList<>();

            List<PlayerManager> list = new ArrayList<>(main.getPlayerManagerAlives());
            main.getPlayerManagerAlives().forEach(playerManager -> {
                if(playerManager.getWolfPlayerManager().isInCouple()){
                    list.remove(playerManager);
                }
            });
            for (int i = 0; i < 3; i++) {
                int index = UhcHost.getRANDOM().nextInt(list.size());
                PlayerManager value = list.get(index);
                listRival.add(value);
                list.remove(value);
            }
            main.getPlayerManagerAlives().forEach(playerManager -> {
                if(playerManager.getWolfPlayerManager().isInCouple())
                    listRival.add(playerManager);
            });
            int numberOfElements = listRival.size();
            for (int i = 0; i < numberOfElements; i++) {
                int index = UhcHost.getRANDOM().nextInt(listRival.size());
                listString.append("§c- ").append(listRival.get(index).getPlayerName()).append("\n");
                listRival.remove(index);
            }
            player.sendMessage(listString.toString());
        }
    }

    @Override
    public void onNewDay(Player player) {

    }

    @Override
    public void onPlayerDeathRealy(PlayerManager player, ItemStack[] items, ItemStack[] armors, Player killer, Location deathLocation) {
        PlayerManager killerManager = main.getPlayerManager(killer.getUniqueId());
        if (killerManager.hasRole()) {
            if (killerManager.getRole() instanceof Rival) {
                if(player.getWolfPlayerManager().isInCouple()){
                    killerManager.setCamps(Camps.COUPLE);
                    killerManager.getWolfPlayerManager().setOtherCouple(player.getWolfPlayerManager().getOtherCouple());
                    main.getPlayerManager(player.getWolfPlayerManager().getOtherCouple()).getWolfPlayerManager().setOtherCouple(killerManager.getUuid());
                    killer.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§Vous venez de voler le couple ! Vous devez maintenant gagner avec ce dernier !");
                    killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 1, 1);
                }
            }
        }
    }

    @Override
    public QuickItem getItem() {
        return null;
    }

    @Override
    public Camps getCamp() {
        return Camps.NEUTRES;
    }

    @Override
    public String getRoleName() {
        return "Rival";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
    }
}
