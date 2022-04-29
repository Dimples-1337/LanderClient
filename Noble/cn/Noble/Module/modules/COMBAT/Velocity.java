/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Module.modules.COMBAT;

import java.awt.*;

import cn.Noble.Client;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventPacketRecieve;
import cn.Noble.Event.events.EventPacketSend;
import cn.Noble.Event.events.EventTick;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.Logger;
import cn.Noble.Util.MoveUtils;
import cn.Noble.Util.Chat.Helper;
import cn.Noble.Util.Timer.TimerUtil;
import cn.Noble.Values.Mode;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends Module {
    private Numbers<Double> Vertical = new Numbers<Double>("Vertical", 0.0, 0.0, 100.0, 5.0);
    private Numbers<Double> Horizontal = new Numbers<Double>("Horizontal", 0.0, 0.0, 100.0, 5.0);
    private Mode<Enum> mode = new Mode("Mode", (Enum[])ThisMode.values(), (Enum)ThisMode.Normal);
    private Option<Boolean> WaterCheck = new Option<>("WaterCheck",false);
    private TimerUtil velocityTimer = new TimerUtil();
    public Velocity() {
        super("Velocity", new String[]{"AntiVelocity", "antiknockback", "antikb"}, ModuleType.Combat);
        this.addValues(this.mode,WaterCheck,this.Vertical,Horizontal);
        this.setColor(new Color(0xD968FF).getRGB());
    }
    @EventHandler
    private void onPacket(EventPacketRecieve e) {
        this.setSuffix(this.mode.getValue());
        if (WaterCheck.getValue() && mc.player.isInWater()) {
            return;
        }
        if (e.getPacket() instanceof S12PacketEntityVelocity || e.getPacket() instanceof S27PacketExplosion) {
            if(this.mode.getValue() == ThisMode.Normal) {
            	if (this.Vertical.getValue().equals(0.0) && this.Horizontal.getValue().equals(0.0)) {
                    e.setCancelled(true);
                } else {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
                    packet.motionX *= (this.Horizontal.getValue() / 100.0);
                    packet.motionY *= (this.Vertical.getValue() / 100.0);
                    packet.motionZ *= (this.Horizontal.getValue() / 100.0);
                }
            }
        }
        if (e.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
            if (this.mode.getValue() == ThisMode.AAC) {
                if (velocityTimer.hasTimePassed(80L)) {
                    packet.motionX *= Horizontal.getValue() / 100.0;
                    packet.motionZ *= Horizontal.getValue() / 100.0;
                }
            }
            if (this.mode.getValue() == ThisMode.Matrix) {
                if (mc.player.onGround && MoveUtils.isMoving() && SpeedBs(mc.player) > 2) {
                    packet.motionX *= Horizontal.getValue() / 100.0;
                    packet.motionZ *= Horizontal.getValue() / 100.0;
                }
            }
            if (this.mode.getValue() == ThisMode.OnGround) {
                if (mc.player.onGround) {
                    if (this.Vertical.getValue().equals(0.0) && this.Horizontal.getValue().equals(0.0)) {
                        e.setCancelled(true);
                    } else {
                        packet.motionX *= (this.Horizontal.getValue() / 100.0);
                        packet.motionY *= (this.Vertical.getValue() / 100.0);
                        packet.motionZ *= (this.Horizontal.getValue() / 100.0);
                    }
                }
            }
        }
    }
    
    public static double SpeedBs(Entity entity) {
        double xDif = entity.posX - entity.prevPosX;
        double zDif = entity.posZ - entity.prevPosZ;
        double lastDist = Math.sqrt(xDif * xDif + zDif * zDif) * 20.0D;
        return Math.round(lastDist);
    }
    
    enum ThisMode {
        Normal,AAC,Matrix,OnGround
    }
}
