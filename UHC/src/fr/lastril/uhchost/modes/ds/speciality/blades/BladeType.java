package fr.lastril.uhchost.modes.ds.speciality.blades;

import fr.lastril.uhchost.modes.ds.speciality.blades.types.BlackBlade;
import fr.lastril.uhchost.modes.ds.speciality.blades.types.GrayBlade;
import fr.lastril.uhchost.modes.ds.speciality.blades.types.YellowBlade;
import fr.lastril.uhchost.player.PlayerManager;

public enum BladeType {

    BLACK("Noir", BlackBlade.class, 1),
    GRAY("Gris", GrayBlade.class, 8),
    //GREEN("Vert", GreenBlade.class, 2),
    //PINK("Rose", PinkBlade.class, 9),
    YELLOW("Jaune", YellowBlade.class, 11);

    private final String name;

    private final Class clazz;

    private final int data;

    BladeType(String name, Class clazz, int data) {
        this.name = name;
        this.clazz = clazz;
        this.data = data;
    }

    public String getName() {
        return this.name;
    }

    public Class getClazz() {
        return this.clazz;
    }

    public int getData() {
        return this.data;
    }

    public Blades getRoleInstance(PlayerManager uuid) {
        try {
            return (Blades)getClazz().getConstructors()[0].newInstance(new Object[] { uuid });
        } catch (Exception e) {
            return null;
        }
    }

}
