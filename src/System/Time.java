package System;

public final class Time implements Comparable<Time> {
    public static final Time Zero = new Time(0);

    private long nanoseconds;

    public static Time seconds(long seconds){
        return new Time(seconds * 1000000000);
    }

    public static Time milliseconds(long milliseconds){
        return new Time(milliseconds * 1000000);
    }

    public static Time microseconds(long microseconds){
        return new Time(microseconds * 1000);
    }

    @Deprecated
    public static Time nanoseconds(long nanoseconds){
        return new Time(nanoseconds);
    }

    public Time(long value){
        this.nanoseconds = value;
    }

    public Time(Time t){
        this.nanoseconds = t.nanoseconds;
    }

    public long getSeconds() {
        return (long)(nanoseconds / (1000000000.0));
    }

    public long getMilliseconds(){
        return  (long)(nanoseconds / (1000000.0)) - this.getSeconds() * 1000;
    }

    public long getMicroseconds(){
        return nanoseconds / (1000) - this.getMilliseconds() * 1000 - this.getSeconds() * 1000000;
    }

    @Deprecated
    public long getNanoseconds(){
        return nanoseconds;
    }

    public double asSeconds(){
        return (double)nanoseconds / (1000000000.0);
    }

    public double asMilliseconds() {
        return (double)nanoseconds / (1000000.0);
    }

    public double asMicroseconds(){
        return (double)nanoseconds / (1000.0);
    }

    @Deprecated
    public double asNanoseconds(){
        return (double)nanoseconds;
    }

    public void mul(double fact) {
        this.nanoseconds *= fact;
        if (this.nanoseconds < 0)
            this.nanoseconds = 0;
    }

    public void add(Time time){
        this.nanoseconds += time.nanoseconds;
    }

    public void reduce(Time time){
        this.nanoseconds -= time.nanoseconds;
        if (this.nanoseconds < 0)
            this.nanoseconds = 0;
    }

    @Override
    public Time clone(){
        return new Time(nanoseconds);
    }

    @Override
    public int compareTo(Time o) {
        if (this.nanoseconds > o.nanoseconds)
            return +1;
        else if (this.nanoseconds < o.nanoseconds)
            return -1;
        else
            return 0;
    }
}
