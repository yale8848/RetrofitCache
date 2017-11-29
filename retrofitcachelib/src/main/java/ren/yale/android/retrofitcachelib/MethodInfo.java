package ren.yale.android.retrofitcachelib;


import java.util.concurrent.TimeUnit;

/**
 * Created by yale on 2017/11/2.
 */

public class MethodInfo {

    private TimeUnit timeUnit;
    private int time;

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
