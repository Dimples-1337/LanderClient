package cn.Noble.Module.modules.WORLD;

import java.awt.Color;

import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventPacketRecieve;
import cn.Noble.Event.events.EventTick;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.Logger;
import cn.Noble.Values.Numbers;
import net.minecraft.client.*;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

public class WorldTime extends Module
{
    public static Numbers<Double> Time;
    
    public WorldTime() {
        super("WorldTime", new String[] { "WorldTime", "WorldTime" }, ModuleType.World);
        this.Time = new Numbers<Double>("Time", 18000.0, 0.0, 24000.0, 1.0);
        super.addValues(this.Time);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @EventHandler
    public void EventPacketSend(EventPacketRecieve e) {
    	if(mc.world == null) return;
        if (e.getPacket() instanceof S03PacketTimeUpdate) {
            e.setCancelled(false);
            mc.world.setWorldTime(this.Time.getValue().longValue());
        }
        mc.world.setWorldTime(this.Time.getValue().longValue());
    }
    
    @EventHandler
    public void onTick(EventTick event) {
        if(mc.world == null) return;
        mc.world.setWorldTime(this.Time.getValue().longValue());
        if(!(mc.world.getWorldTime() == this.Time.getValue().longValue())) {
        	mc.world.setWorldTime(this.Time.getValue().longValue());
        }
    }
}
