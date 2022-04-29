package cn.Noble.Event.events;

import cn.Noble.Event.Event;

public class EventEntityBorderSize
extends Event {
    private float borderSize;

    public EventEntityBorderSize(float borderSize) {
        this.borderSize = borderSize;
    }

    public float getBorderSize() {
        return this.borderSize;
    }

    public void setBorderSize(float borderSize) {
        this.borderSize = borderSize;
    }
}
