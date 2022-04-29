/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Event.events;


import cn.Noble.Event.Event;
import net.minecraft.client.Minecraft;

public class EventStrafe
extends Event {
	
    private float strafe;
    private float forward;
    private float friction;

    public EventStrafe(float strafe, float forward, float friction) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
    }
    
    public float getForward() {
		return forward;
	}
    
    public float getFriction() {
		return friction;
	}
    
    public float getStrafe() {
		return strafe;
	}
}

