/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Util.Timer;

public class TimerUtil {
	
    private long lastMS;
    private long time;
    private long ms = this.getCurrentMS();

    public TimerUtil() {
        super();
        this.setTime(-1L);
    }
    //StopWatch
    public final long getElapsedTime() {
        return this.getCurrentMS() - this.ms;
    }

    public final boolean elapsed(long milliseconds) {
        return this.getCurrentMS() - this.ms > milliseconds;
    }

    public final void resetStopWatch() {
        this.ms = this.getCurrentMS();
    }
    
    //Util
    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
    public  boolean hit(long milliseconds) {
    	return (getCurrentMS() - lastMS) >= milliseconds;
    }
    public boolean hasReached(double milliseconds) {
        if ((double)(this.getCurrentMS() - this.lastMS) >= milliseconds) {
            return true;
        }
        return false;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
        this.setTime(System.currentTimeMillis());
    }

    public boolean delay(float delay) {
        return (float) (getTime() - this.lastMS) >= delay;
    }
    public boolean isDelayComplete(long delay) {
        if (System.currentTimeMillis() - this.lastMS > delay) {
            return true;
        }
        return false;
    }
    public long getTime() {
        return System.nanoTime() / 1000000L;
    }
    
    public boolean hasTimePassed(final long MS) {
        return System.currentTimeMillis() >= this.getTime() + MS;
    }
    
    public long hasTimeLeft(final long MS) {
        return MS + this.getTime() - System.currentTimeMillis();
    }

	public void setTime(long time) {
		this.time = time;
	}
}

