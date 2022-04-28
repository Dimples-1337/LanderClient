package cn.Arctic.Module.modules.GUI;

import java.awt.Color;

import org.lwjgl.opengl.Display;

import cn.Arctic.Client;
import cn.Arctic.Event.EventBus;
import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventKey;
import cn.Arctic.Event.events.EventPacketRecieve;
import cn.Arctic.Event.events.EventPacketSend;
import cn.Arctic.Event.events.EventRender2D;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Font.CFontRenderer;
import cn.Arctic.Font.FontLoaders;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.Chat.Helper;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.Util.math.MathUtil;
import cn.Arctic.Util.render.RenderUtil;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import cn.Arctic.values.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.Packet;

public class PacketDisplay extends Module {
	
	private TimerUtil sendTimer;
	private TimerUtil rcvTimer;
	
	private int sendTimes;
	private int rcvTimes;
	
	public PacketDisplay() {
		super("PacketDisplay",new String[] {""}, ModuleType.Render);
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
