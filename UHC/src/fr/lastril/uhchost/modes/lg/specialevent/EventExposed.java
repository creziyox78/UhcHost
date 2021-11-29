package fr.lastril.uhchost.modes.lg.specialevent;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.lg.LoupGarouSpecialEvent;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class EventExposed extends LoupGarouSpecialEvent {

    private EventExposed(int startMinute, int endMinute){
        super("Exposed", 25, startMinute, endMinute);
    }

    public EventExposed() {
        this(65, 80);
    }

    @Override
    public void runEvent(){

        List<Role> getRoles = new ArrayList<>();
        List<Role> exposedRole = new ArrayList<>();
        PlayerManager randomPlayer = main.getRandomPlayerManagerAlive();
        System.out.println("[Exposed - LG] Selected random player");
        exposedRole.add(randomPlayer.getRole());
        for(Class<? extends Role> roleClass: main.gameManager.getComposition()){
            try {
                Role role = roleClass.newInstance();
                if(!role.getRoleName().equalsIgnoreCase(randomPlayer.getRole().getRoleName()))
                    getRoles.add(role);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        exposedRole.add(getRoles.get(UhcHost.getRANDOM().nextInt(getRoles.size())));
        exposedRole.add(getRoles.get(UhcHost.getRANDOM().nextInt(getRoles.size())));
        StringBuilder message = new StringBuilder("");
        for (int i = 0; i < 3; i++) {
            int index = UhcHost.getRANDOM().nextInt(exposedRole.size());
            message.append(exposedRole.get(index).getRoleName()).append(", ");
            exposedRole.remove(index);
        }
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), "mob.guardian.curse", 1, 1);
        });
        System.out.println("[Exposed - LG] Getted 2 random role.");
        Bukkit.broadcastMessage("§9[Exposed]§b " + randomPlayer.getPlayerName() + "§8 possède un rôle parmis les 3 suivants :§e " + message);
    }





}
