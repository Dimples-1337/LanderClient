/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.Module.modules.COMBAT;



import java.awt.Color;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventTick;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;

public class FastPlace
extends Module {
    public FastPlace() {
        super("FastPlace", new String[]{"fplace", "fc"}, ModuleType.World);
        this.setColor(new Color(226, 197, 78).getRGB());
    }

    @EventHandler
    private void onTick(EventTick e) {
        this.mc.rightClickDelayTimer = 0;
    }
}

