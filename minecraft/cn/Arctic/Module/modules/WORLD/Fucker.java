package cn.Arctic.Module.modules.WORLD;

import java.util.Iterator;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.Timer.TimerUtil;
import net.minecraft.block.BlockBed;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;

public class Fucker extends Module {
	private TimerUtil stopWatch = new TimerUtil();

	public Fucker() {
		super("Fucker", new String[]{"Fucker"}, ModuleType.World);
	}

	@EventHandler
	public void onPre(EventPreUpdate e) {
			this.destroy(e);
	}

	public float[] getRotationsNeededBlock(double x, double y, double z) {
		double diffX = x + 0.5D - mc.player.posX;
		double diffZ = z + 0.5D - mc.player.posZ;
		double diffY = y + 0.5D - (mc.player.posY + (double) mc.player.getEyeHeight());
		double dist = (double) MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0D / Math.PI));
		return new float[] { mc.player.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.player.rotationYaw),
				mc.player.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.player.rotationPitch) };
	}

	private void destroy(EventPreUpdate event) {
		Iterator positions = BlockPos.getAllInBox(this.mc.player.getPosition().subtract(new Vec3i(5, 5, 5)),
				this.mc.player.getPosition().add(new Vec3i(5, 5, 5))).iterator();
		BlockPos bedPos = null;
		if (positions != null && positions.next() != null) {
			while (positions.hasNext()) {
				bedPos = (BlockPos) positions.next();
				if (this.mc.world.getBlockState(bedPos).getBlock() instanceof BlockBed) {
					float[] rot = this.getRotationsNeededBlock((double) bedPos.getX(), (double) bedPos.getY(),
							(double) bedPos.getZ());
					event.setYaw(rot[0]);
					event.setPitch(rot[1]);

					if (this.stopWatch.hasReached(500)) {
						this.mc.player.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, bedPos, EnumFacing.DOWN));
						this.mc.player.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, bedPos, EnumFacing.DOWN));
						this.mc.player.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, bedPos, EnumFacing.DOWN));
						this.mc.player.swingItem();
						this.stopWatch.reset();
					}
				}
			}
		}
	}
}
