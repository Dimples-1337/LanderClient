/*
 * Decompiled with CFR 0.148.
 */
package cn.Arctic.Event.events;


import cn.Arctic.Event.Event;
import net.minecraft.network.Packet;

public final class SendPacketEvent
extends  Event {
    private final Packet<?> packet;

    public SendPacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }
}

