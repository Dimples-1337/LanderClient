package cn.Arctic.Event.events;


import com.mojang.realmsclient.dto.RealmsServer.State;

import cn.Arctic.Event.Event;
import net.minecraft.network.Packet;

public class EventPacket extends Event {
	public static Packet packet;
    private boolean cancelled;
    private final State state;
    public static boolean isOutGoing;

    public EventPacket(Packet<?> packet, State state) {
        this.state = state;
        this.packet = packet;
    }
    
    public void set(boolean out) {
    	isOutGoing = out;
    }

    public State getState() {
        return state;
    }
    public static Packet getPacket() {
        return packet;
    }

    public void setCancelled(boolean state) {
        this.cancelled = state;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setPacket(Packet p) {
        this.packet = p;
    }
    public enum State {
        INCOMING,
        OUTGOING
    }
}
