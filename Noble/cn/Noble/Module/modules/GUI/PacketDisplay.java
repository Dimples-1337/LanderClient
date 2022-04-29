package cn.Noble.Module.modules.GUI;

import java.awt.Color;

import org.lwjgl.opengl.Display;

import cn.Noble.Client;
import cn.Noble.Event.EventBus;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventKey;
import cn.Noble.Event.events.EventPacketRecieve;
import cn.Noble.Event.events.EventPacketSend;
import cn.Noble.Event.events.EventRender2D;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Font.CFontRenderer;
import cn.Noble.Font.FontLoaders;
import cn.Noble.Manager.Manager;
import cn.Noble.Manager.ModuleManager;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.Chat.Helper;
import cn.Noble.Util.Timer.TimerUtil;
import cn.Noble.Util.math.MathUtil;
import cn.Noble.Util.render.RenderUtil;
import cn.Noble.Values.Mode;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;
import cn.Noble.Values.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.Packet;

public class PacketDisplay extends Module {
	
	private TimerUtil sendTimer;
	private TimerUtil rcvTimer;
	
	private int sendTimes;
	private int rcvTimes;
	
	public PacketDisplay() {
		super("PacketDisplay", ModuleType.Render);
		this.sendTimer = new TimerUtil();
		this.rcvTimer = new TimerUtil();
	}
	
	@EventHandler
	private void onPacketSend(EventPacketSend event) {
		if(event.getPacket() instanceof Packet) sendTimes++;
	}
	
	@EventHandler
	private void onPacketRCV(EventPacketRecieve event) {
		if(event.getPacket() instanceof Packet) rcvTimes++;
	}
	
	@EventHandler
	private void onUpdate(EventPreUpdate event) {
        if (this.sendTimer.hasReached(1000)) {
            this.sendTimes = 0;
            this.sendTimer.reset();
        }
        if (this.rcvTimer.hasReached(1000)) {
            this.rcvTimes = 0;
            this.rcvTimer.reset();
        }
	}
}
