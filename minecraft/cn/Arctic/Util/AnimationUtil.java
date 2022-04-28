package cn.Arctic.Util;

public class AnimationUtil {
	
    private static float defaultSpeed = 0.125f;
    
	   public static float calculateCompensation(float target, float current, long delta, int speed) {
	      float diff = current - target;
	      if(delta < 1L) {
	         delta = 1L;
	      }

	      double xD;
	      if(diff > (float)speed) {
	         xD = (double)((long)speed * delta / 16L) < 0.25D?0.5D:(double)((long)speed * delta / 16L);
	         current = (float)((double)current - xD);
	         if(current < target) {
	            current = target;
	         }
	      } else if(diff < (float)(-speed)) {
	         xD = (double)((long)speed * delta / 16L) < 0.25D?0.5D:(double)((long)speed * delta / 16L);
	         current = (float)((double)current + xD);
	         if(current > target) {
	            current = target;
	         }
	      } else {
	         current = target;
	      }

	      return current;
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

		public static double getAnimationState(double animation, double finalState,double speed) {
	        float add = (float)(0.01 * speed);
	        animation = animation < finalState ? (Math.min(animation + add, finalState)) : (Math.max(animation - add, finalState));
	        return animation;
	    }
	}

