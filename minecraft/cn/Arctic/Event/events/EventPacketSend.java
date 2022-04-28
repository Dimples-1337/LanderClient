/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.Event.events;

import cn.Arctic.Event.Event;
import net.minecraft.network.Packet;

public class EventPacketSend
extends Event {
    public static Packet packet;
    public static Packet packet2;

    public EventPacketSend(Packet packet) {
        this.packet = packet;
    }

    public static Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}

