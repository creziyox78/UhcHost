package fr.lastril.uhchost.modes.lg.specialevent;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.lg.LoupGarouSpecialEvent;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventZizanie extends LoupGarouSpecialEvent {
    public EventZizanie() {
        super("Zizanie", 25, 75, 105);
    }

    @Override
    public void runEvent() {
        PlayerManager villageois, lg, solo;
        int lenght = (int) main.gameManager.getModes().getMode().getModeManager().getPlayerManagersWithCamps(Camps.VILLAGEOIS).stream().filter(PlayerManager::isAlive).count();
        if(lenght != 0){
            int value = UhcHost.getRANDOM().nextInt(lenght);
            List<PlayerManager> villageoisList = main.gameManager.getModes().getMode().getModeManager().getPlayerManagersWithCamps(Camps.VILLAGEOIS).stream().filter(PlayerManager::isAlive).collect(Collectors.toList());
            villageois = villageoisList.get(value);
            if(villageois != null){
                System.out.println("[Zizanie] Get Villager player: " + villageois.getPlayerName());
                lenght = (int) main.gameManager.getModes().getMode().getModeManager().getPlayerManagersWithCamps(Camps.LOUP_GAROU).stream().filter(PlayerManager::isAlive).count();
                if(lenght != 0){
                    value = UhcHost.getRANDOM().nextInt(lenght);
                    List<PlayerManager> lgList = main.gameManager.getModes().getMode().getModeManager().getPlayerManagersWithCamps(Camps.LOUP_GAROU).stream().filter(PlayerManager::isAlive).collect(Collectors.toList());
                    lg = lgList.get(value);
                    if(lg != null){
                        System.out.println("[Zizanie] Get LG player: " + lg.getPlayerName());
                        List<PlayerManager> notLgList = main.gameManager.getModes().getMode().getModeManager().getPlayerManagersWithCamps(Camps.LOUP_GAROU).stream().filter(PlayerManager::isAlive).collect(Collectors.toList());
                        List<PlayerManager> notVillageoisList = main.gameManager.getModes().getMode().getModeManager().getPlayerManagersWithCamps(Camps.VILLAGEOIS).stream().filter(PlayerManager::isAlive).collect(Collectors.toList());
                        List<PlayerManager> notNeutralList = main.gameManager.getModes().getMode().getModeManager().getPlayerManagersWithCamps(Camps.NEUTRES).stream().filter(PlayerManager::isAlive).collect(Collectors.toList());
                        List<PlayerManager> soloList = new ArrayList<>();
                        main.getPlayerManagerAlives().forEach(playerManager -> {
                            if(!notLgList.contains(playerManager) && !notNeutralList.contains(playerManager) && !notVillageoisList.contains(playerManager))
                                soloList.add(playerManager);
                        });


                        lenght = soloList.size();
                        if(lenght != 0){
                            value = UhcHost.getRANDOM().nextInt(lenght);

                        }
                        solo = soloList.get(value);
                        System.out.println("[Zizanie] Get Solo player: " + solo.getPlayerName());

                        if(solo != null){
                            solo.setCamps(Camps.VILLAGEOIS);
                            solo.getWolfPlayerManager().setZizanied(Camps.VILLAGEOIS);
                            lg.setCamps(Camps.ZIZANIE);
                            lg.getWolfPlayerManager().setZizanied(Camps.ZIZANIE);
                            villageois.setCamps(Camps.LOUP_GAROU);
                            villageois.getWolfPlayerManager().setZizanied(Camps.LOUP_GAROU);
                            System.out.println("[Zizanie] Changed all players camps !");
                            if(solo.getPlayer() != null){
                                solo.getPlayer().sendMessage("§9[Zizanie]§b Une zizanie vient d'arriver ! " +
                                        "Vous devez maintenant gagner avec les villageois !");
                                solo.getPlayer().playSound(lg.getPlayer().getLocation(), Sound.ENDERMAN_SCREAM, 1, 1);
                            }

                            if(lg.getPlayer() != null){
                                lg.getPlayer().sendMessage("§9[Zizanie]§b Une zizanie vient d'arriver ! " +
                                        "Vous devez maintenant gagner avec seul !");
                                lg.getPlayer().playSound(lg.getPlayer().getLocation(), Sound.ENDERMAN_SCREAM, 1, 1);
                            }

                            if(villageois.getPlayer() != null){
                                villageois.getPlayer().sendMessage("§9[Zizanie]§b Une zizanie vient d'arriver ! " +
                                        "Vous devez maintenant gagner avec les loups-garou !");
                                villageois.getPlayer().playSound(lg.getPlayer().getLocation(), Sound.ENDERMAN_SCREAM, 1, 1);
                            }


                            if(solo.getWolfPlayerManager().isInCouple())
                                solo.setCamps(Camps.COUPLE);
                            if(lg.getWolfPlayerManager().isInCouple())
                                lg.setCamps(Camps.COUPLE);
                            if(villageois.getWolfPlayerManager().isInCouple())
                                villageois.setCamps(Camps.COUPLE);
                        }
                    }

                }
            }
        }













    }
}
