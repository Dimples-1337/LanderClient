package cn.Arctic.Module.modules.MOVE;

import java.awt.*;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventMove;
import cn.Arctic.Event.events.EventPacketRecieve;
import cn.Arctic.Event.events.EventTick;
import cn.Arctic.Event.events.Update.EventPostUpdate;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.GUI.NewNotification.NotificationPublisher;
import cn.Arctic.GUI.NewNotification.NotificationType;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.UHC.TargetStrafe;
import cn.Arctic.Util.MoveUtils;
import cn.Arctic.Util.Player.PlayerUtil;
import cn.Arctic.Util.math.MathUtil;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Option;
import net.minecraft.client.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.*;
import net.minecraft.client.network.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.client.multiplayer.*;
import net.minecraft.util.*;
import net.minecraft.potion.*;

public class Longjump extends Module
{
	public Mode modeSetting = new Mode("Mode", (Enum[]) modes.values(), (Enum) modes.WatchDog);
	public Option dis = new Option<Boolean>("AutoDisable", true);

	private int air;

	private int enableTime;

	public Longjump() {
		super("LongJump", new String[] { "longjump", "lj" }, ModuleType.Movement);
		this.addValues(this.modeSetting, this.dis);
	}

	public void onEnable() {
		air = 0;
		if (modeSetting.getValue() == modes.WatchDog) {
			enableTime = 0;
			mc.player.jump();
		}
		if (modeSetting.getValue() == modes.AAC) {
			mc.player.jump();
		}
	}

	public void onDisable() {

	}

	@EventHandler
	public void onUpdate(EventPreUpdate e) {
		this.setSuffix(this.modeSetting.getValue());
		if (!mc.player.onGround) {
			air++;
		} else if (air > 3 && (Boolean) dis.getValue()) {
			this.setEnabled(false);
		}
		if (mc.player.onGround && !(Boolean) dis.getValue()) {
			enableTime = 0;
			mc.player.jump();
		}
		if (modeSetting.getValue() == modes.WatchDog) {
			enableTime++;
			mc.player.motionY += 0.006F;
		}
		if (modeSetting.getValue() == modes.AAC) {
			mc.player.jumpMovementFactor = 0.12F;
			mc.player.motionY += 0.03F;
		}
	}

	@EventHandler
	public void onUpdate(EventPostUpdate e) {
		if (modeSetting.getValue() == modes.WatchDog) {
			if (this.mc.player.motionY < -0.0) {
				final ClientPlayerEntity player = this.mc.player;
				player.motionY *= Math.min(0.6 + this.enableTime / 30.0, 0.95);
			}
		}
	}

	@EventHandler
	public void onMove(EventMove e) {
		if (modeSetting.getValue() == modes.WatchDog) {
			double moveSpeed = Math.max(
					MoveUtils.getBaseMoveSpeed() * (2.45 - (MoveUtils.getSpeedEffect() * 0.03) - (enableTime / 70)),
					MoveUtils.getBaseMoveSpeed());
				this.setMotion(e, moveSpeed);
		}
	}

	@EventHandler
	public void onPacket(EventPacketRecieve e) {
		Packet packet = e.getPacket();
		if (packet instanceof S08PacketPlayerPosLook) {
			NotificationPublisher.queue("Lag Check", "LongJump Lag Disabled.", NotificationType.WARN, 2500);
			this.setEnabled(false);
		}
	}
	
	public void setMotion(EventMove em, double speed) {
		double forward = mc.player.movementInput.moveForward;
		double strafe = mc.player.movementInput.moveStrafe;
		float yaw = mc.player.rotationYaw;
		if (forward == 0 && strafe == 0) {
			em.setX(0);
			em.setZ(0);
		} else {
			if (forward != 0) {
				if (strafe > 0) {
					yaw += (float) (forward > 0 ? -45 : 45);
				} else if (strafe < 0) {
					yaw += (float) (forward > 0 ? 45 : -45);
				}
				strafe = 0;
				if (forward > 0) {
					forward = 1;
				} else if (forward < 0) {
					forward = -1;
				}
			}
			em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90))
					+ strafe * speed * Math.sin(Math.toRadians(yaw + 90)));
			em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90))
					- strafe * speed * Math.cos(Math.toRadians(yaw + 90)));
		}
	}

	static enum modes {
		WatchDog, AAC;
	}
}
