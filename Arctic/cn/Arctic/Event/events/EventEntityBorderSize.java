package cn.Arctic.Event.events;

import cn.Arctic.Event.Event;

public class EventEntityBorderSize extends Event {
    private float borderSize;

    public EventEntityBorderSize(float borderSize) {
        this.borderSize = borderSize;
    }

    public float getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(float borderSize) {
        this.borderSize = borderSize;
    }
}
