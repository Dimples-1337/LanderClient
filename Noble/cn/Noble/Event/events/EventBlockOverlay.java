/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Event.events;

import cn.Noble.Event.Event;
import net.minecraft.util.AxisAlignedBB;

public class EventBlockOverlay
extends Event {
    AxisAlignedBB axisalignedbb;

    public EventBlockOverlay(AxisAlignedBB axisalignedbb) {
        this.axisalignedbb = axisalignedbb;
    }

    public AxisAlignedBB getBB() {
        return this.axisalignedbb;
    }

    public void setBB(AxisAlignedBB axisalignedbb) {
        this.axisalignedbb = axisalignedbb;
    }
}

