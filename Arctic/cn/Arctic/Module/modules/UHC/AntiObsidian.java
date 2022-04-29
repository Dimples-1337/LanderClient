/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.Module.modules.UHC;

import java.awt.Color;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.BlockUtils;
import cn.Arctic.Util.math.RotationUtil;
import cn.Arctic.values.Option;
import cn.Arctic.values.Value;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.FoodStats;
import net.minecraft.util.Vec3;

public class AntiObsidian
extends Module {
    public AntiObsidian() {
        super("AntiObsidian", new String[] { "AntiObsidian" }, ModuleType.World);
        super.addValues(new Value[0]);
        this.setColor(new Color(208, 30, 142).getRGB());
    }
    
    @EventHandler
    public void OnUpdate(final EventPreUpdate e) {
        BlockPos obsidianpos = new BlockPos(new Vec3(AntiObsidian.mc.player.posX, AntiObsidian.mc.player.posY + 1.0, AntiObsidian.mc.player.posZ));
        Block obsidianblock = AntiObsidian.mc.world.getBlockState(obsidianpos).getBlock();
        if (obsidianblock == Block.getBlockById(49)) {
            obsidianpos = new BlockPos(new Vec3(AntiObsidian.mc.player.posX, AntiObsidian.mc.player.posY - 1.0, AntiObsidian.mc.player.posZ));
            obsidianblock = AntiObsidian.mc.world.getBlockState(obsidianpos).getBlock();
            if (obsidianblock != Block.getBlockById(49)) {
                BlockUtils.updateTool(obsidianpos);
                float[] rot = RotationUtil.getRotationFromPosition(AntiObsidian.mc.player.posX + 0.5, AntiObsidian.mc.player.posY - 1.0 + 0.5, AntiObsidian.mc.player.posZ + 0.5);
                if (rot == null) {
                    return;
                }
                e.setYaw(rot[0]);
                e.setPitch(rot[1]);
                AntiObsidian.mc.playerController.onPlayerDamageBlock(obsidianpos, EnumFacing.UP);
            }
            else {
                BlockPos block1pos = new BlockPos(new Vec3(AntiObsidian.mc.player.posX, AntiObsidian.mc.player.posY + 1.0, AntiObsidian.mc.player.posZ - 1.0));
                BlockPos block2pos = new BlockPos(new Vec3(AntiObsidian.mc.player.posX, AntiObsidian.mc.player.posY, AntiObsidian.mc.player.posZ - 1.0));
                Block block = AntiObsidian.mc.world.getBlockState(block1pos).getBlock();
                if (block != Block.getBlockById(0)) {
                    BlockUtils.updateTool(block1pos);
                    float[] rot2 = RotationUtil.getRotationFromPosition(AntiObsidian.mc.player.posX + 0.5, AntiObsidian.mc.player.posY + 1.0 + 0.5, AntiObsidian.mc.player.posZ - 1.0 + 0.5);
                    if (rot2 == null) {
                        return;
                    }
                    e.setYaw(rot2[0]);
                    e.setPitch(rot2[1]);
                    AntiObsidian.mc.playerController.onPlayerDamageBlock(block1pos, EnumFacing.EAST);
                }
                else {
                    BlockUtils.updateTool(block2pos);
                    float[] rot2 = RotationUtil.getRotationFromPosition(AntiObsidian.mc.player.posX + 0.5, AntiObsidian.mc.player.posY + 0.5, AntiObsidian.mc.player.posZ - 1.0 + 0.5);
                    if (rot2 == null) {
                        return;
                    }
                    e.setYaw(rot2[0]);
                    e.setPitch(rot2[1]);
                    AntiObsidian.mc.playerController.onPlayerDamageBlock(block2pos, EnumFacing.EAST);
                }
            }
        }
    }
}

