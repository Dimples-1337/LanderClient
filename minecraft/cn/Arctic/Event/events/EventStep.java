package cn.Arctic.Event.events;

import cn.Arctic.Event.Event;
import cn.Arctic.Event.EventCategory;

public class EventStep extends Event {
	
    private double stepHeight;
    private double realHeight;
    private boolean active;
    private boolean pre;
    private float height;
    private final EventCategory eventType;

    public EventStep(EventCategory eventType, float height) {
        this.eventType = eventType;
        this.height = height;
    }
    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void fire(boolean state, double stepHeight, double realHeight) {
        this.pre = state;
        this.stepHeight = stepHeight;
        this.realHeight = realHeight;
    }

    public void fire(boolean state, double stepHeight) {
        this.pre = state;
        this.realHeight = stepHeight;
        this.stepHeight = stepHeight;
    }

    public boolean isPre() {
        return pre;
    }

    public double getStepHeight() {
        return this.stepHeight;
    }

    public boolean isActive() {
        return this.active;
    }
    public EventCategory getEventType() {
        return this.eventType;
    }

    public void setStepHeight(double stepHeight) {
        this.stepHeight = stepHeight;
    }

    public void setActive(boolean bypass) {
        this.active = bypass;
    }

    public double getRealHeight() {
        return this.realHeight;
    }

    public void setRealHeight(double realHeight) {
        this.realHeight = realHeight;
    }

}
