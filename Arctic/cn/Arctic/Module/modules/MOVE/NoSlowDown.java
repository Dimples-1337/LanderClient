package cn.Arctic.Module.modules.MOVE;


//import com.Maki.module.modules.combat.Aura;

import java.awt.Color;

import cn.Arctic.Client;
import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.Update.EventPostUpdate;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.COMBAT.Aura;
import cn.Arctic.Util.Player.PlayerUtil;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.values.Mode;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

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