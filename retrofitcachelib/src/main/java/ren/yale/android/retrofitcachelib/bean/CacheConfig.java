package ren.yale.android.retrofitcachelib.bean;


import java.util.concurrent.TimeUnit;

/**
 * Created by yale on 2018/1/23.
 */

public class CacheConfig {

    private TimeUnit timeUnit = TimeUnit.NANOSECONDS;
    private Long time = 0L;
    private boolean forceCacheNoNet = true;

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean isForceCacheNoNet() {
        return forceCacheNoNet;
    }

    public void setForceCacheNoNet(boolean forceCacheNoNet) {
        this.forceCacheNoNet = forceCacheNoNet;
    }
}
