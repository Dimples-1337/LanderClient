/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.Module.modules.MOVE;

import java.awt.Color;

import cn.Arctic.Client;
import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventMove;
import cn.Arctic.Event.events.EventTick;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.GUI.notifications.Notification;
import cn.Arctic.GUI.notifications.Notification.Type;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.MoveUtils;
import cn.Arctic.Util.Player.PlayerUtil;
import cn.Arctic.Util.Timer.TimeHelper;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class AntiFall extends Module {

	private TimerUtil timer = new TimerUtil();
	private boolean saveMe;
	boolean AAAA = false;
	double mario = 0.0;
	private Mode<Enum> mode = new Mode("Mode", AntiMode.values(), AntiMode.Hypixel);
	public Numbers<Double> distance = new Numbers("Distance", 5.0, 4.0, (Number) 10.0, (Number) 1.0);
	private Numbers<Double> MotionHigh = new Numbers("MotionHigh", 1.2, 0.5, (Number) 10.0, (Number) 0.1);
	public Option<Boolean> Void = new Option("Void", true);
	
	private TimerUtil hypFallTimer = new TimerUtil();

	public AntiFall() {
		super("AntiFall", new String[]{"avoid"}, ModuleType.Movement);
		this.addValues(this.mode, distance, this.MotionHigh, Void);
	}

	@EventHandler
	private void onUpdate(EventTick e) {
        ClientPlayerEntity player = mc.player;
        if (mc.player != null && player.fallDistance > (Double)this.distance.getValue() && !player.capabilities.isFlying && this.hypFallTimer.hasReached(200) && !this.isBlockUnder()) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(player.posX, player.posY + this.distance.getValue() + 1.0, player.posZ, false));
            this.hypFallTimer.reset();
        }
	}

	@EventHandler
	public void onMove(EventMove e) {
		if (this.mode.getValue() == AntiMode.Watchdog) {
			if (((Boolean) Void.getValue()).booleanValue()
					&& (double) mc.player.fallDistance >= (Double) distance.getValue() && mc.player.motionY <= 0.0
					&& (!this.AAAA || mc.player.posY <= this.mario)
					&& mc.world
							.getCollidingBoundingBoxes((Entity) mc.player,
									mc.player.getEntityBoundingBox().offset(0.0, 0.0, 0.0).expand(0.0, 0.0, 0.0))
							.isEmpty()
					&& mc.world.getCollidingBoundingBoxes((Entity) mc.player,
							mc.player.getEntityBoundingBox().offset(0.0, -10002.25, 0.0).expand(0.0, -10003.75, 0.0))
							.isEmpty()) {
				mc.player.motionY = 1.75;
				mc.player.motionX = 0.0;
				mc.player.motionZ = 0.0;
				e.setX(0.0);
				e.setZ(0.0);
				this.mario = mc.player.posY;
				this.AAAA = true;
			} else if (!((Boolean) Void.getValue()).booleanValue()
					&& (double) mc.player.fallDistance >= (Double) distance.getValue() && mc.player.motionY <= 0.0
					&& (!this.AAAA || mc.player.posY <= this.mario)) {
				mc.player.motionY = 1.75;
				mc.player.motionX = 0.0;
				mc.player.motionZ = 0.0;
				e.setX(0.0);
				e.setZ(0.0);
				this.mario = mc.player.posY;
				this.AAAA = true;
			}
			if (mc.player.onGround) {
				this.mario = 0.0;
				this.AAAA = false;
			}
		}
		if (this.mode.getValue() == AntiMode.Redesky) {
			if ((double) mc.player.fallDistance >= (Double) distance.getValue() && mc.player.motionY <= 0.0
					&& (!this.AAAA || mc.player.posY <= this.mario)
					&& mc.world
							.getCollidingBoundingBoxes((Entity) mc.player,
									mc.player.getEntityBoundingBox().offset(0.0, 0.0, 0.0).expand(0.0, 0.0, 0.0))
							.isEmpty()
					&& mc.world.getCollidingBoundingBoxes((Entity) mc.player,
							mc.player.getEntityBoundingBox().offset(0.0, -10002.25, 0.0).expand(0.0, -10003.75, 0.0))
							.isEmpty()) {
				mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX,
						mc.player.posY, mc.player.posZ, true));
				mc.getNetHandler()
						.addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(0.5, 0.0, 0.5, true));
				mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX,
						mc.player.posY, mc.player.posZ, true));
				mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX,
						mc.player.posY + 5.0, mc.player.posZ, true));
				mc.getNetHandler()
						.addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(0.5, 0.0, 0.5, true));
				mc.getNetHandler()
						.addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX + 0.5,
								mc.player.posY, mc.player.posZ + 0.5, true));
				this.AAAA = true;
			}
			if (mc.player.onGround) {
				this.mario = 0.0;
				this.AAAA = false;
			}
		}
		if (this.mode.getValue() == AntiMode.Motion) {
			if ((double) mc.player.fallDistance >= (Double) distance.getValue() && mc.player.motionY <= 0.0
					&& (!this.AAAA || mc.player.posY <= this.mario)
					&& mc.world
							.getCollidingBoundingBoxes((Entity) mc.player,
									mc.player.getEntityBoundingBox().offset(0.0, 0.0, 0.0).expand(0.0, 0.0, 0.0))
							.isEmpty()
					&& mc.world.getCollidingBoundingBoxes((Entity) mc.player,
							mc.player.getEntityBoundingBox().offset(0.0, -10002.25, 0.0).expand(0.0, -10003.75, 0.0))
							.isEmpty()) {
				e.setY(this.MotionHigh.getValue());
				mc.player.motionY = (Double) this.MotionHigh.getValue();
				mc.player.motionX *= 2.0;
				mc.player.motionZ *= 2.0;
				e.setX(0.0);
				e.setZ(0.0);
//				mc.getConnection().sendPacket(new C03PacketPlayer(true));
//				mc.player.onGround = true;
				MoveUtils.setSpeedWithEvent(e, 3.0);
				this.mario = mc.player.posY;
				this.AAAA = true;
			}
			if (mc.player.onGround) {
				this.mario = 0.0;
				this.AAAA = false;
			}
		}
	}

	private boolean isBlockUnder() {
		if (mc.player.posY < 0.0) {
			return false;
		}
		for (int off = 0; off < (int) mc.player.posY + 2; off += 2) {
			AxisAlignedBB bb = mc.player.boundingBox.offset(0.0, (double) (-off), 0.0);
			if (mc.world.getCollidingBoundingBoxes((Entity) mc.player, bb).isEmpty())
				continue;
			return true;
		}
		return false;
	}

	enum AntiMode {
		Hypixel, Watchdog, Motion, Redesky;
	}
}