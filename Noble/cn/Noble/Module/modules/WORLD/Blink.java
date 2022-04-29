/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Module.modules.WORLD;

import com.mojang.authlib.GameProfile;

import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventPacketSend;
import cn.Noble.Event.events.EventRender3D;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.Timer.TimerUtil;
import cn.Noble.Util.gl.GLUtils;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class Blink
extends Module {
    private final LinkedBlockingQueue<Packet> packets;
    private EntityOtherPlayerMP fakePlayer;
    private boolean disableLogger;
    private final LinkedList<double[]> positions;
    private final Option<Boolean> pulseValue;
    private final Numbers<Double> pulseDelayValue;
    private final TimerUtil pulseTimer;
    
    public Blink() {
        super("Blink", new String[] { "blink" }, ModuleType.Player);
        this.packets = new LinkedBlockingQueue<Packet>();
        this.fakePlayer = null;
        this.positions = new LinkedList<double[]>();
        this.pulseValue = new Option<Boolean>("Pulse", false);
        this.pulseDelayValue = new Numbers<Double>("PulseDelay", 1000.0, 500.0, 5000.0, 100.0);
        this.pulseTimer = new TimerUtil();
        this.addValues(this.pulseValue, this.pulseDelayValue);
    }
    
    @Override
    public void onEnable() {
        if (Blink.mc.player == null) {
            return;
        }
        if (!this.pulseValue.getValue()) {
            (this.fakePlayer = new EntityOtherPlayerMP(Blink.mc.world, Blink.mc.player.getGameProfile())).clonePlayer(Blink.mc.player, true);
            this.fakePlayer.copyLocationAndAnglesFrom(Blink.mc.player);
            this.fakePlayer.rotationYawHead = Blink.mc.player.rotationYawHead;
            Blink.mc.world.addEntityToWorld(-1337, this.fakePlayer);
        }
        synchronized (this.positions) {
            this.positions.add(new double[] { Blink.mc.player.posX, Blink.mc.player.getEntityBoundingBox().minY + Blink.mc.player.getEyeHeight() / 2.0f, Blink.mc.player.posZ });
            this.positions.add(new double[] { Blink.mc.player.posX, Blink.mc.player.getEntityBoundingBox().minY, Blink.mc.player.posZ });
        }
        this.pulseTimer.reset();
    }
    
    @Override
    public void onDisable() {
        if (Blink.mc.player == null) {
            return;
        }
        this.blink();
        if (this.fakePlayer != null) {
            Blink.mc.world.removeEntityFromWorld(this.fakePlayer.getEntityId());
            this.fakePlayer = null;
        }
    }
    
    @EventHandler
    public void onPacket(final EventPacketSend event) {
        final Packet<?> packet = (Packet<?>)event.getPacket();
        if (Blink.mc.player == null || this.disableLogger) {
            return;
        }
        if (packet instanceof C03PacketPlayer) {
            event.setCancelled(true);
        }
        if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook || packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C0APacketAnimation || packet instanceof C0BPacketEntityAction || packet instanceof C02PacketUseEntity) {
            event.setCancelled(true);
            this.packets.add(packet);
        }
    }
    
    @EventHandler
    public void onUpdate(final EventPreUpdate event) {
        synchronized (this.positions) {
            this.positions.add(new double[] { Blink.mc.player.posX, Blink.mc.player.getEntityBoundingBox().minY, Blink.mc.player.posZ });
        }
        if (this.pulseValue.getValue() && this.pulseTimer.hasTimePassed(this.pulseDelayValue.getValue().longValue())) {
            this.blink();
            this.pulseTimer.reset();
        }
    }
    
    @EventHandler
    public void onRender3D(final EventRender3D event) {
        final Color color = new Color(20,20,20);
        synchronized (this.positions) {
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GL11.glDisable(2929);
            Blink.mc.entityRenderer.disableLightmap();
            GL11.glBegin(3);
            GLUtils.glColor(color.getRGB());
            final double renderPosX = Blink.mc.getRenderManager().viewerPosX;
            final double renderPosY = Blink.mc.getRenderManager().viewerPosY;
            final double renderPosZ = Blink.mc.getRenderManager().viewerPosZ;
            for (final double[] pos : this.positions) {
                GL11.glVertex3d(pos[0] - renderPosX, pos[1] - renderPosY, pos[2] - renderPosZ);
            }
            GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
            GL11.glEnd();
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glPopMatrix();
        }
    }
    
    @EventHandler
    private void onRender2d() {
        this.setSuffix("Packet:" + this.packets.size());
    }
    
    private void blink() {
        try {
            this.disableLogger = true;
            while (!this.packets.isEmpty()) {
                Blink.mc.getNetHandler().getNetworkManager().sendPacket(this.packets.take());
            }
            this.disableLogger = false;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.disableLogger = false;
        }
        synchronized (this.positions) {
            this.positions.clear();
        }
    }
}

