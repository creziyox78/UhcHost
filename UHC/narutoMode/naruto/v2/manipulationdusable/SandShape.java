package fr.lastril.uhchost.modes.naruto.v2.manipulationdusable;

;
import fr.maygo.uhc.modes.naruto.v2.roles.solo.Gaara;
import org.bukkit.entity.Player;

public abstract class SandShape {

    protected final UhcHost main;

    private final String name;
    private final ShapeType shapeType;

    public SandShape(String name, ShapeType shapeType) {
        this.main = UhcHost.getInstance();
        this.name = name;
        this.shapeType = shapeType;
    }

    public abstract boolean useCapacity(Player player, Gaara gaara);

    public abstract int getSandPrice();

    public String getName() {
        return name;
    }

    public ShapeType getShapeType() {
        return shapeType;
    }
}
