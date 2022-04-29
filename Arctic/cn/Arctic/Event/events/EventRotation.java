package cn.Arctic.Event.events;

import cn.Arctic.Event.Event;

public class EventRotation extends Event {
    private float yaw;
    private float pitch;

    public EventRotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }

    public EventRotation getAngle() {
        this.yaw = this.yaw % 360.0f;
        this.pitch = this.pitch % 360.0f;
        while (this.yaw <= -180.0f) {
            this.yaw = this.yaw + 360.0f;
        }
        while (this.pitch <= -180.0f) {
            this.pitch = this.pitch + 360.0f;
        }
        while (this.yaw > 180.0f) {
            this.yaw = this.yaw - 360.0f;
        }
        while (this.pitch > 180.0f) {
            this.pitch = this.pitch - 360.0f;
        }
        return this;
    }
}
