package cn.Arctic.Event.events;

import cn.Arctic.Event.Event;

public class EventUpdate extends Event {
	 private boolean isPre;
	    private float yaw;
	    private float pitch;
	    public static double x;
	    public static double y;
	    public static double z;
	    private boolean onground;
	    public boolean onGround;
	    private boolean alwaysSend;
	    private boolean sneaking;
	    public static float YAW, PITCH, PREVYAW, PREVPITCH;
	    public static boolean SNEAKING;
	    public void fire(double x, double y, double z, float yaw, float pitch, boolean sneaking, boolean ground) {
	        this.isPre = true;
	        this.yaw = yaw;
	        this.pitch = pitch;
	        this.y = y;
	        this.x = x;
	        this.z = z;
	        this.onground = ground;
	        this.sneaking = sneaking;
	    }

	    public void fire() {
	    	PREVYAW = YAW;
	    	PREVPITCH = PITCH;
	    	YAW = this.yaw;
	    	PITCH = this.pitch;
	    	SNEAKING = this.sneaking;
	        this.isPre = false;
	    }

	    public boolean isPre() {
	        return isPre;
	    }

	    public boolean isPost() {
	        return !isPre;
	    }

	    public float getYaw() {
	        return this.yaw;
	    }

	    public float getPitch() {
	        return this.pitch;
	    }

	    public double getX() {
	        return this.x;
	    }
	    public static double getY() {
	        return y;
	    }
	    public double getZ() {
	        return this.z;
	    }
	    public boolean isSneaking(){
	    	return this.sneaking;
	    }
	    public boolean isOnground() {
	        return this.onground;
	    }
	    public void setSneaking(boolean sneaking){
	    	this.sneaking = sneaking;
	    }
	    public boolean shouldAlwaysSend() {
	        return this.alwaysSend;
	    }

	    public void setYaw(float yaw) {
	        this.yaw = yaw;
	    }

	    public void setPitch(float pitch) {
	        this.pitch = pitch;
	    }

	    public void setX(double x) {
	        this.x = x;
	    }
	    public void setY(double y) {
	        this.y = y;
	    }
	    public void setZ(double z) {
	        this.z = z;
	    }

	    public void setGround(boolean ground) {
	        this.onground = ground;
	    }

	    public void setAlwaysSend(boolean alwaysSend) {
	        this.alwaysSend = alwaysSend;
	    }

	    public void setOnGround(boolean onground) {
	        this.onground = onground;
	    }
}
