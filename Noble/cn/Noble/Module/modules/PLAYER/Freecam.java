/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Module.modules.PLAYER;

import java.awt.Color;

import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventCollideWithBlock;
import cn.Noble.Event.events.EventPacketRecieve;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Freecam
extends Module {
    private EntityOtherPlayerMP copy;
    private double x;
    private double y;
    private double z;

    public Freecam() {
        super("Freecam", new String[]{"outofbody"}, ModuleType.Player);
        this.setColor(new Color(221, 214, 51).getRGB());
    }

    @Override
    public void onEnable() {
        this.copy = new EntityOtherPlayerMP(this.mc.world, this.mc.player.getGameProfile());
        this.copy.clonePlayer(this.mc.player, true);
        this.copy.setLocationAndAngles(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ, this.mc.player.rotationYaw, this.mc.player.rotationPitch);
        this.copy.rotationYawHead = this.mc.player.rotationYawHead;
        this.copy.setEntityId(-1337);
        this.copy.setSneaking(this.mc.player.isSneaking());
        this.mc.world.addEntityToWorld(this.copy.getEntityId(), this.copy);
        this.x = this.mc.player.posX;
        this.y = this.mc.player.posY;
        this.z = this.mc.player.posZ;
        super.onEnable();
    }
    @EventHandler
    private void onPreMotion(EventPreUpdate e) {
        this.mc.player.capabilities.isFlying = true;
        this.mc.player.noClip = true;
        this.mc.player.capabilities.setFlySpeed(0.1f);
        e.setCancelled(true);
    }

    @EventHandler
    private void onPacketSend(EventPacketRecieve e) {
        if (e.getPacket() instanceof C03PacketPlayer) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onBB(EventCollideWithBlock e) {
        e.setBoundingBox(null);
    }

    @Override
    public void onDisable() {
        this.mc.player.setAIMoveSpeed(0.0F);
        this.mc.player.setLocationAndAngles(this.copy.posX, this.copy.posY, this.copy.posZ, this.copy.rotationYaw, this.copy.rotationPitch);
        this.mc.player.rotationYawHead = this.copy.rotationYawHead;
        this.mc.world.removeEntityFromWorld(this.copy.getEntityId());
        this.mc.player.setSneaking(this.copy.isSneaking());
        this.copy = null;
        this.mc.renderGlobal.loadRenderers();
        this.mc.player.setPosition(this.x, this.y, this.z);
        this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.player.posX, this.mc.player.posY + 0.01, this.mc.player.posZ, this.mc.player.onGround));
        this.mc.player.capabilities.isFlying = false;
        this.mc.player.noClip = false;
        this.mc.world.removeEntityFromWorld(-1);
        super.onDisable();
    }
}

