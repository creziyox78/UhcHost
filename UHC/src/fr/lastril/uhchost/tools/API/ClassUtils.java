package fr.lastril.uhchost.tools.API;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ClassUtils {

    public static String getDirectionOf(Location ploc, Location to) {
        if(ploc == null || to == null) return "?";
        if(ploc.getWorld() != to.getWorld()) return "?";
        ploc.setY(0.0D);
        to.setY(0.0D);

        String[] arrows = {"⬆", "⬈", "➡", "⬊", "⬇", "⬋", "⬅", "⬉", "⬆"};
        Vector d = ploc.getDirection();

        Vector v = to.subtract(ploc).toVector().normalize();

        double a = Math.toDegrees(Math.atan2(d.getX(), d.getZ()));
        a -= Math.toDegrees(Math.atan2(v.getX(), v.getZ()));

        a = ((int)(a + 22.5D) % 360);

        if (a < 0.0D) {
            a += 360.0D;
        }

		/*String color = "§4";
		if(ploc.distance(to) > 750 && ploc.distance(to) < 1000) {
			color = "§c";
		}else if(ploc.distance(to) > 500 && ploc.distance(to) < 750) {
			color = "§6";
		}else if(ploc.distance(to) > 250 && ploc.distance(to) < 500) {
			color = "§e";
		}else if(ploc.distance(to) < 250) {
			color = "§a";
		}*/
        return arrows[((int) a / 45)];
    }



}
