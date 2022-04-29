/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Module.modules.MOVE;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cn.Noble.Client;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventCollideWithBlock;
import cn.Noble.Event.events.EventPacketRecieve;
import cn.Noble.Event.events.EventPacketSend;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.Player.PlayerUtil;
import cn.Noble.Util.Timer.TimerUtil;
import cn.Noble.Values.Mode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class Jesus
extends Module {
    private boolean wasWater;
    private int ticks;
    private Mode<Enum> mode;
    
    public Jesus() {
        super("WaterWalk", new String[] { "waterwalk", "waterfloat", "liquidwalk", "waterfloat" }, ModuleType.Movement);
        this.wasWater = false;
        this.ticks = 0;
        this.mode = new Mode<Enum>("Mode", JMode.values(), JMode.Dolphin);
        this.setColor(new Color(188, 233, 248).getRGB());
        this.addValues(this.mode);
    }
    
    @Override
    public void onEnable() {
        this.wasWater = false;
        super.onEnable();
    }
    
    private boolean canJeboos() {
        return this.mc.player.fallDistance < 3.0f && !this.mc.gameSettings.keyBindJump.isPressed() && !this.isOnLiquid() && !this.mc.player.isSneaking();
    }
    
    boolean shouldJesus() {
        final double x = this.mc.player.posX;
        final double y = this.mc.player.posY;
        final double z = this.mc.player.posZ;
        final ArrayList pos = new ArrayList((Collection)Arrays.asList(new BlockPos(x + 0.3, y, z + 0.3), new BlockPos(x - 0.3, y, z + 0.3), new BlockPos(x + 0.3, y, z - 0.3), new BlockPos(x - 0.3, y, z - 0.3)));
        for (final Object po : pos) {
            if (this.mc.world.getBlockState((BlockPos)po).getBlock() instanceof BlockLiquid && this.mc.world.getBlockState((BlockPos)po).getProperties().get((Object)BlockLiquid.LEVEL) instanceof Integer) {
                if ((int)this.mc.world.getBlockState((BlockPos)po).getProperties().get((Object)BlockLiquid.LEVEL) > 4) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }
    
    @EventHandler
    public void onPre(final EventPreUpdate e) {
        this.setSuffix(this.mode.getValue());
        if (this.mode.getValue() == JMode.Dolphin) {
            if (this.mc.player.isInWater() && !this.mc.player.isSneaking() && this.shouldJesus()) {
                this.mc.player.motionY = 0.09;
            }
            if (e.getType() == 1) {
                return;
            }
            if (this.mc.player.onGround || this.mc.player.isOnLadder()) {
                this.wasWater = false;
            }
            if (this.mc.player.motionY > 0.0 && this.wasWater) {
                if (this.mc.player.motionY <= 0.11) {
                    mc.player.motionY *= 1.2671;
                }
                mc.player.motionY += 0.05172;
            }
            if (this.isInLiquid() && !this.mc.player.isSneaking()) {
                if (this.ticks < 3) {
                    this.mc.player.motionY = 0.13;
                    ++this.ticks;
                    this.wasWater = false;
                }
                else {
                    this.mc.player.motionY = 0.5;
                    this.ticks = 0;
                    this.wasWater = true;
                }
            }
        }
        else if (this.mode.getValue() == JMode.Solid && this.isOnLiquid() && !this.mc.player.isSneaking() && !this.mc.gameSettings.keyBindJump.isPressed()) {
            this.mc.player.motionY = 0.05;
            this.mc.player.onGround = true;
        }
    }
    
    private boolean isInLiquid() {
        if (this.mc.player == null) {
            return false;
        }
        for (int x = MathHelper.floor_double(this.mc.player.boundingBox.minX); x < MathHelper.floor_double(this.mc.player.boundingBox.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(this.mc.player.boundingBox.minZ); z < MathHelper.floor_double(this.mc.player.boundingBox.maxZ) + 1; ++z) {
                final BlockPos pos = new BlockPos(x, (int)this.mc.player.boundingBox.minY, z);
                final Block block = this.mc.world.getBlockState(pos).getBlock();
                if (block != null && !(block instanceof BlockAir)) {
                    return block instanceof BlockLiquid;
                }
            }
        }
        return false;
    }
    
    public void setMotion(final double speed) {
        double forward = mc.player.movementInput.moveForward;
        double strafe = mc.player.movementInput.moveStrafe;
        float yaw = this.mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            this.mc.player.motionX = 0.0;
            this.mc.player.motionZ = 0.0;
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            this.mc.player.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            this.mc.player.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }
    
    @EventHandler
    public void onPacket(final EventPacketSend e) {
        if (this.mode.getValue() == JMode.Solid && e.getPacket() instanceof C03PacketPlayer && this.canJeboos() && this.isOnLiquid()) {
            final C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
            packet.y = ((this.mc.player.ticksExisted % 2 == 0) ? (packet.y + 0.01) : (packet.y - 0.01));
        }
    }
    
    @EventHandler
    public void onBB(final EventCollideWithBlock e) {
        if (this.mode.getValue() == JMode.Solid && e.getBlock() instanceof BlockLiquid && this.canJeboos()) {
            e.setBoundingBox(new AxisAlignedBB(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ(), e.getPos().getX() + 1.0, e.getPos().getY() + 1.0, e.getPos().getZ() + 1.0));
        }
    }
    
    public boolean isOnLiquid() {
        boolean onLiquid = false;
        final AxisAlignedBB playerBB = this.mc.player.getEntityBoundingBox();
        final WorldClient world = this.mc.world;
        final int y = (int)playerBB.offset(0.0, -0.01, 0.0).minY;
        for (int x = MathHelper.floor_double(playerBB.minX); x < MathHelper.floor_double(playerBB.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(playerBB.minZ); z < MathHelper.floor_double(playerBB.maxZ) + 1; ++z) {
                final Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null) {
                    if (!(block instanceof BlockAir)) {
                        if (!(block instanceof BlockLiquid)) {
                            return false;
                        }
                        onLiquid = true;
                    }
                }
            }
        }
        return onLiquid;
    }
    
    enum JMode
    {
        Solid, 
        Dolphin;
    }
}

