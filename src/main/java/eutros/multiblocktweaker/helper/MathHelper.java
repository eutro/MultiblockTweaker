package eutros.multiblocktweaker.helper;

public class MathHelper {

    @SafeVarargs
    public static <T extends Comparable<T>> T max(T max, T... toCompare) {
        for (T t : toCompare) {
            if (t.compareTo(max) > 0) {
                max = t;
            }
        }
        return max;
    }

    @SafeVarargs
    public static <T extends Comparable<T>> T min(T min, T... toCompare) {
        for (T t : toCompare) {
            if (t.compareTo(min) < 0) {
                min = t;
            }
        }
        return min;
    }

}
