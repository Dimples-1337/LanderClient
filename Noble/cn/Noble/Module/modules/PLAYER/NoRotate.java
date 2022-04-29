package cn.Noble.Module.modules.PLAYER;

import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventPacketRecieve;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.Chat.Helper;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate extends Module {

	public NoRotate() {
		super("NoRotate", ModuleType.Player);
	}
	
	@EventHandler
	public void onRecievePacket(EventPacketRecieve e){
		if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) e.getPacket();
            packet.yaw = mc.player.rotationYaw;
            packet.pitch = mc.player.rotationPitch;
        }
	}
}
