/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.Module.modules.WORLD;

import java.awt.Color;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventPacketRecieve;
import cn.Arctic.Event.events.EventTick;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.values.Numbers;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

public class WorldTime
        extends Module {
    public static Numbers<Double> Time = new Numbers<Double>("Time", 18000.0, 0.0, 24000.0, 1.0);

    public WorldTime() {
        super("WorldTime", new String[]{"WorldTime", "WorldTime"}, ModuleType.World);
        this.setColor(new Color(198, 253, 191).getRGB());
        super.addValues(Time);

    }

    @Override
    public void onEnable() {

    	
        super.onEnable();
    }

    @EventHandler
    public void EventPacketSend(EventPacketRecieve e) {
        if (e.getPacket() instanceof S03PacketTimeUpdate) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTick(EventTick event) {
        mc.world.setWorldTime(18000);
    }


}

