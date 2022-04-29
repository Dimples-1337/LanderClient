/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Event.events;

import cn.Noble.Event.Event;
import net.minecraft.network.Packet;

public class EventPacketSend
extends Event {
    public Packet packet;

    public EventPacketSend(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}

