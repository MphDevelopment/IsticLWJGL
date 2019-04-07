package System;


import java.util.function.Function;

public class Clock {
    public enum Mode {
        NANOSECONDS_ACCURACY,
        MILLISECONDS_ACCURACY
    }
    private static long ClockFunction(Mode mode) {
        switch (mode) {
            case NANOSECONDS_ACCURACY:return System.nanoTime();
            case MILLISECONDS_ACCURACY:return System.currentTimeMillis() * 1000000;
            default:return 0L;
        }
    }

    private Mode mode = Mode.NANOSECONDS_ACCURACY;
    private long t1 = 0;
    private long t2 = 0;

    public Clock(Mode mode) {
        this.mode = mode;
        t1 = ClockFunction(mode);
    }

    public Clock(){
        t1 = ClockFunction(mode);//System.nanoTime();
    }

    public Time restart(){
        t2 = ClockFunction(mode);//System.nanoTime();
        long elapsed = t2 - t1;
        t1 = t2;
        return new Time(elapsed);
    }

    public Time getElapsed(){
        t2 = ClockFunction(mode);//System.nanoTime();
        long elapsed = t2 - t1;
        return new Time(elapsed);
    }
}
