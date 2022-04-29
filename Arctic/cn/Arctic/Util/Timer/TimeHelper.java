package cn.Arctic.Util.Timer;

public class TimeHelper {
    public long lastMs;
    
    public TimeHelper() {
        super();
        this.lastMs = 0L;
    }
    
    public boolean isDelayComplete(final double d) {
        return System.currentTimeMillis() - this.lastMs > d;
    }
    
    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
    
    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }
    
    public long getLastMs() {
        return this.lastMs;
    }
    
    public void setLastMs(final int i) {
        this.lastMs = System.currentTimeMillis() + i;
    }
    
    public boolean hasReached(final long milliseconds) {
        return this.getCurrentMS() - this.lastMs >= milliseconds;
    }
    
    public boolean hasReached(final float timeLeft) {
        return this.getCurrentMS() - this.lastMs >= timeLeft;
    }
    
    public boolean delay(final double nextDelay) {
        return System.currentTimeMillis() - this.lastMs >= nextDelay;
    }
}

