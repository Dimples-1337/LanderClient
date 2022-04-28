/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.Event.events;


import cn.Arctic.Event.Event;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;

public class EventRender2D
extends Event {
	 public ScaledResolution sr;
    private float partialTicks;

    public EventRender2D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public ScaledResolution getSR() {
        return this.sr;
    }


}

