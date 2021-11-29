package fr.lastril.uhchost.modes.lg;

import fr.lastril.uhchost.UhcHost;
import org.bukkit.Bukkit;

public abstract class LoupGarouSpecialEvent {

    private final String name;
    private int chance, startMinute, endMinute;
    protected final UhcHost main;

    public LoupGarouSpecialEvent(String name, int chance, int startMinute, int endMinute){
        this.name = name;
        this.main = UhcHost.getInstance();
        this.chance = chance;
        this.startMinute = startMinute;
        this.endMinute = endMinute;
    }

    public abstract void runEvent();

    public void runTask() {
        int randomMinute = getRandomMinute();
        System.out.println("[LG - Events] Name: " + getName() + " | chance: " + getChance() + " | Minute: " + randomMinute);
        Bukkit.getScheduler().runTaskLater(main, () -> {
            int value = UhcHost.getRANDOM().nextInt(1, 100);
            System.out.println("[LG - Events] Name: " + getName() + " | value: " + value);
            if(value <= getChance())
                runEvent();
        }, 20L  * randomMinute);
    }

    public int getRandomMinute(){
        return UhcHost.getRANDOM().nextInt(startMinute, endMinute);
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public int getChance() {
        return chance;
    }

    public String getName() {
        return name;
    }
}
