package fr.lastril.uhchost.test.utils;

public class MathUtils {

    public static double randomDouble(double min, double max) {
        return (Math.random() < 0.5D) ? ((1.0D - Math.random()) * (max - min) + min) : (Math.random() * (max - min) + min);
    }

}
