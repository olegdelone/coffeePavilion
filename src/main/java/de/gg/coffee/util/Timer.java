package de.gg.coffee.util;

public class Timer {
    private long start = System.currentTimeMillis();

    public static Timer registerStart() {
        return new Timer();
    }

    public long registerStop() {
       return System.currentTimeMillis() - this.start;
    }

    public long getStart() {
        return start;
    }

}
