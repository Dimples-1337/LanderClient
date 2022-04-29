package cn.Noble.Module.modules.UHC;

import java.awt.*;

import cn.Noble.Client;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventPacketSend;
import cn.Noble.Event.events.Update.EventPostUpdate;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Values.Mode;
import cn.Noble.Values.Numbers;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;

public class SpeedMine extends Module{
    private boolean bzs;
    private float bzx;
    public BlockPos blockPos;
    public EnumFacing facing;
    
    public SpeedMine() {
        super("SpeedMine", new String[] { "SpeedMine", "antifall" }, ModuleType.World);
        this.bzs = false;
        this.bzx = 0.0f;
        this.setColor(new Color(223, 233, 233).getRGB());
    }
    
    public Block getBlock(final double x, final double y, final double z) {
        final BlockPos bp = new BlockPos(x, y, z);
        return this.mc.world.getBlockState(bp).getBlock();
    }
    
    @EventHandler
    public void onDamageBlock(final EventPacketSend event) {
        if (event.getPacket() instanceof C07PacketPlayerDigging && !this.mc.playerController.extendedReach() && this.mc.playerController != null) {
            final C07PacketPlayerDigging c07PacketPlayerDigging = (C07PacketPlayerDigging)event.getPacket();
            if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                this.bzs = true;
                this.blockPos = c07PacketPlayerDigging.getPosition();
                this.facing = c07PacketPlayerDigging.getFacing();
                this.bzx = 0.0f;
            }
            else if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                this.bzs = false;
                this.blockPos = null;
                this.facing = null;
            }
        }
    }
    
    @EventHandler
    public void onUpdate(final EventPreUpdate event) {
        if (this.mc.playerController.extendedReach()) {
            this.mc.playerController.blockHitDelay = 0;
        }
        else if (this.bzs) {
            final Block block = this.mc.world.getBlockState(this.blockPos).getBlock();
            final float bzx = this.bzx;
            final Block block2 = block;
            final ClientPlayerEntity player = this.mc.player;
            this.bzx = bzx + (float)(block2.getPlayerRelativeBlockHardness(player, this.mc.world, this.blockPos) * 1.4);
            if (this.bzx >= 1.0f) {
                this.mc.world.setBlockState(this.blockPos, Blocks.air.getDefaultState(), 11);
                this.mc.player.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.facing));
                this.bzx = 0.0f;
                this.bzs = false;
            }
        }
        this.mc.player.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 100, 0));
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.player.removePotionEffect(Potion.digSpeed.getId());
    }
}
