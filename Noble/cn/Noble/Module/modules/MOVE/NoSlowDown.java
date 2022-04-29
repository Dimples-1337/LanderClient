package cn.Noble.Module.modules.MOVE;

import java.awt.Color;

import cn.Noble.Event.EventUseItem;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.Update.EventPostUpdate;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Module.modules.COMBAT.Aura;
import cn.Noble.Util.Player.PlayerUtil;
import cn.Noble.Util.Timer.TimerUtil;
import cn.Noble.Values.Mode;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;
import cn.Noble.Values.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.EntityLivingBase;

import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;

public class NoSlowDown extends Module {
   
	public NoSlowDown() {
        super("NoSlow", new String[]{"noslowdown"}, ModuleType.Movement);
        this.setColor(new Color(216, 253, 100).getRGB());
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        if (Minecraft.getMinecraft().player.isBlocking()) {
//        	mc.getConnection().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }

    @EventHandler
    private void onUpdate(EventPostUpdate e) {
        if (Minecraft.getMinecraft().player.isBlocking()) {
//            mc.getConnection().sendPacket(new C08PacketPlayerBlockPlacement(mc.player.inventory.getCurrentItem()));
        }
    }
}
