package cn.Noble.GUI.clickgui;

public class Opacity
{
    private float opacity;
    private long lastMS;
    
    public Opacity(final int opacity) {
        super();
        this.opacity = (float)opacity;
        this.lastMS = System.currentTimeMillis();
    }
    
    public void interpolate(final float targetOpacity) {
        final long currentMS = System.currentTimeMillis();
        final long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        this.opacity = AnimationUtil.calculateCompensation(targetOpacity, this.opacity, delta, 20);
    }
    
    public void interp(final float targetOpacity, final int speed) {
        final long currentMS = System.currentTimeMillis();
        final long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        this.opacity = AnimationUtil.calculateCompensation(targetOpacity, this.opacity, delta, speed);
    }
    
    public float getOpacity() {
        return (float)(int)this.opacity;
    }
    
    public void setOpacity(final float opacity) {
        this.opacity = opacity;
    }
}
