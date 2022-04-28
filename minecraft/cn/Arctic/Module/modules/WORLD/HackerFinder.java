package cn.Arctic.Module.modules.WORLD;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.GUI.NewNotification.NotificationPublisher;
import cn.Arctic.GUI.NewNotification.NotificationType;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.COMBAT.Aura;
import cn.Arctic.Util.MoveUtils;
import cn.Arctic.Util.Chat.Helper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class HackerFinder extends Module {
	private int bufferNoFall;
	private int bufferFlight;
	private int bufferSpeed;

	public HackerFinder() {
		super("HackerDetecter", new String[] { "HackerFinder" }, ModuleType.World);
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	public static float getDistanceToGround(Entity e) {
		float a = (float) e.posY;
		while (a > 0.0f) {
			int[] stairs = new int[] { 53, 67, 108, 109, 114, 128, 134, 135, 136, 156, 163, 164, 180 };
			int[] exemptIds = new int[] { 6, 27, 28, 30, 31, 32, 37, 38, 39, 40, 50, 51, 55, 59, 63, 65, 66, 68, 69, 70,
					72, 75, 76, 77, 83, 92, 93, 94, 104, 105, 106, 115, 119, 131, 132, 143, 147, 148, 149, 150, 157,
					171, 175, 176, 177 };
			Block block = Minecraft.getMinecraft().world.getBlockState(new BlockPos(e.posX, a - 1.0f, e.posZ))
					.getBlock();
			if (!(block instanceof BlockAir)) {
				if (Block.getIdFromBlock(block) == 44 || Block.getIdFromBlock(block) == 126) {
					return (float) (e.posY - (double) a - 0.5) < 0.0f ? 0.0f : (float) (e.posY - (double) a - 0.5);
				}
				int[] array = stairs;
				int length = array.length;
				int i = 0;
				while (i < length) {
					int id = array[i];
					if (Block.getIdFromBlock(block) == id) {
						return (float) (e.posY - (double) a - 1.0) < 0.0f ? 0.0f : (float) (e.posY - (double) a - 1.0);
					}
					++i;
				}
				int[] array2 = exemptIds;
				int length2 = array2.length;
				int j = 0;
				while (j < length2) {
					int id = array2[j];
					if (Block.getIdFromBlock(block) == id) {
						return (float) (e.posY - (double) a) < 0.0f ? 0.0f : (float) (e.posY - (double) a);
					}
					++j;
				}
				return (float) (e.posY - (double) a - 1.0);
			}
			a -= 1.0f;
		}
		return 0.0f;
	}

	@EventHandler
	public void onPreMotion(EventPreUpdate event) {
		mc.world.loadedEntityList.forEach(o -> {
			Entity p = o;
			if (p != mc.player && !p.isDead && p instanceof EntityPlayer) {
				if (HackerFinder.getDistanceToGround(p) > 4.0f && p.onGround && p.posY < p.prevPosY && !p.isSilent()) {
					++this.bufferNoFall;
				}
				if (this.bufferNoFall >= 10) {
					NotificationPublisher.queue("HackerDetecte", String.valueOf(p.getName()) + "could be Cheating [NoFall]!", NotificationType.WARN, 4000);
					Aura.curTarget = (EntityLivingBase) p;
					this.bufferNoFall = 0;
				}
				if (p.lastTickPosY < p.posY - 0.4 && !p.isSilent()) {
					++this.bufferFlight;
				}
				if (this.bufferFlight >= 50) {
					NotificationPublisher.queue("HackerDetecte", String.valueOf(p.getName()) + "Could be Cheating [Flight]!", NotificationType.WARN, 4000);
					Aura.curTarget = (EntityLivingBase) p;
					this.bufferFlight = 0;
				}
				if (p.lastTickPosX < p.posX - 0.7) {
					++this.bufferSpeed;
				}
				if (p.posX > p.lastTickPosX + 0.7) {
					++this.bufferSpeed;
				}
				if (p.lastTickPosZ < p.posZ - 0.7) {
					++this.bufferSpeed;
				}
				if (p.posX > p.lastTickPosX + 0.7) {
					++this.bufferSpeed;
				}
				if (this.bufferSpeed > 14) {
					NotificationPublisher.queue("HackerDetecte", String.valueOf(p.getName()) + "Could be Cheating [NoFall]!", NotificationType.WARN, 4000);
					Aura.curTarget = (EntityLivingBase) p;
					this.bufferSpeed = 0;
				}
			}
		});
	}
}
