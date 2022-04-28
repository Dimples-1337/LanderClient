package cn.Arctic.Event.events;

import cn.Arctic.Event.Event;

public class EventPushBlock extends Event {
    boolean isPre;
    public EventPushBlock(boolean pre) {
        this.isPre = pre;
    }
    public void fire(boolean bo){
        isPre=bo;
    }
    public boolean isPre() {
        return isPre;
    }

}
