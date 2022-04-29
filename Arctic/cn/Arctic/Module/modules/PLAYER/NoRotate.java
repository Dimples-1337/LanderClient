package cn.Arctic.Module.modules.PLAYER;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventPacketRecieve;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.Chat.Helper;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate extends Module {

	public NoRotate() {
		super("NoRotate", new String[] {""},ModuleType.Player);
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
