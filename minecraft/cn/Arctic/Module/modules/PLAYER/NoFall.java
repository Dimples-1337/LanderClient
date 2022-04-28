/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package cn.Arctic.Module.modules.PLAYER;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventPacketSend;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Option;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class NoFall extends Module {

	private Mode<Enum> mode = new Mode<Enum>("Mode", NoFallMode.values(), NoFallMode.Hypixel);
	public Option<Boolean> Cvoid = new Option<Boolean>("CheckVoid", true);

	public NoFall() {
		super("NoFall", new String[] { "NoFall" }, ModuleType.Player);
		super.addValues(this.mode, this.Cvoid);
	}
	
	enum NoFallMode {
		Hypixel,
		SpoofGround;
	}

	@EventHandler
	public final void onPreMotion(EventPreUpdate e) {

		if (this.mode.getValue() == NoFallMode.Hypixel) {
			if (mc.player.capabilities.isFlying || mc.player.capabilities.disableDamage || mc.player.motionY >= 0.0d)
				return;

			if (mc.player.fallDistance > 3.0f)
				if ((!Cvoid.getValue() || this.isBlockUnder())) {
					mc.getConnection().sendPacketNoEvent(new C03PacketPlayer(true));
				}
		}

		if (this.mode.getValue() == NoFallMode.SpoofGround) {
			e.setOnground(true);
		}
	}

	@EventHandler
	public final void onPacket(EventPacketSend event) {
		if (this.mode.getValue() == NoFallMode.Hypixel) {
			if (event.getPacket() instanceof C03PacketPlayer) {
				if (mc.player.capabilities.isFlying || mc.player.capabilities.disableDamage
						|| mc.player.motionY >= 0.0d)
					return;
				C03PacketPlayer e = (C03PacketPlayer) event.getPacket();
				if (e.isMoving()) {
					if ((mc.player.fallDistance > 2.0f) && (!Cvoid.getValue() || this.isBlockUnder())) {
						event.setCancelled(true);
						mc.getConnection().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(
								e.getPositionX(), e.getPositionY(), e.getPositionZ(), e.isOnGround()));
					}
				}
			}
		}
	}

	private boolean isBlockUnder() {
		for (int offset = 0; offset < mc.player.posY + mc.player.getEyeHeight(); offset += 2) {
			AxisAlignedBB boundingBox = mc.player.getEntityBoundingBox().offset(0, -offset, 0);
			if (!mc.world.getCollidingBoundingBoxes(mc.player, boundingBox).isEmpty()) {
				return true;
			}
		}
		return false;
	}
}
