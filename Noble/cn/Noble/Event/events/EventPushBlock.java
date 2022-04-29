package cn.Noble.Event.events;

import cn.Noble.Event.Event;

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
