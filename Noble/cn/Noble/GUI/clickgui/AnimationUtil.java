package cn.Noble.GUI.clickgui;

public class AnimationUtil
{
    private static float defaultSpeed;
    
    public AnimationUtil() {
        super();
    }
    
    public static float mvoeUD(final float current, final float end, final float minSpeed) {
        return moveUD(current, end, AnimationUtil.defaultSpeed, minSpeed);
    }
    
    public static float moveUD(final float current, final float end, final float smoothSpeed, final float minSpeed) {
        float movement = (end - current) * smoothSpeed;
        if (movement > 0.0f) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        }
        else if (movement < 0.0f) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }
        return current + movement;
    }
    
    public static float calculateCompensation(final float target, float current, long delta, final int speed) {
        final float diff = current - target;
        if (delta < 1L) {
            delta = 1L;
        }
        if (diff > speed) {
            final double xD = (speed * delta / 16L < 0.25) ? 0.5 : ((double)(speed * delta / 16L));
            current -= (float)xD;
            if (current < target) {
                current = target;
            }
        }
        else if (diff < -speed) {
            final double xD = (speed * delta / 16L < 0.25) ? 0.5 : ((double)(speed * delta / 16L));
            current += (float)xD;
            if (current > target) {
                current = target;
            }
        }
        else {
            current = target;
        }
        return current;
    }
    
    static {
        AnimationUtil.defaultSpeed = 0.125f;
    }
}
