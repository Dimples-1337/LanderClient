/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.Event.events.Update;

import cn.Arctic.Event.Event;

public class EventPreUpdate
extends Event {
	
	public float yaw;
    private float pitch;
    public static double y;
    public double x;
	public double z;
    public static boolean ground;

	public EventPreUpdate(double x, double y, double z, float yaw, float pitch, boolean ground) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.x = x;
		this.y = y;
		this.z = z;
		this.ground = ground;
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
    
	public double getX() {
		return this.x;
	}
	
    public double getY() {
        return this.y;
    }
	
	public double getZ() {
		return this.z;
	}

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isOnground() {
        return this.ground;
    }

    public void setOnground(boolean ground) {
        this.ground = ground;
    }
}

