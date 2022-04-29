/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Module.modules.UHC;

import java.awt.Color;

import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.FoodStats;

public class GhostHand
extends Module {

    public GhostHand() {
        super("Ghost Hand", new String[]{"GhostHand"}, ModuleType.Combat);
        this.setColor(new Color(208, 30, 142).getRGB());
    }


}

