/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Event.events;

import cn.Noble.Event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventRender2D
extends Event {
    public float partialTicks;

    public EventRender2D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}

