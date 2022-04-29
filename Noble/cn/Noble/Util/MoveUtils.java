package cn.Noble.Util;

import cn.Noble.Event.events.EventMove;
import cn.Noble.Util.Chat.Helper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class MoveUtils {
	private static Minecraft mc = Minecraft.getMinecraft();

	public static boolean isOnGround(double height) {
		if (!mc.world.getCollidingBoundingBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0D, -height, 0.0D))
				.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public static int getSpeedEffect() {
		if (mc.player.isPotionActive(Potion.moveSpeed))
			return mc.player.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
		else
			return 0;
	}

	public static int getJumpEffect() {
		if (mc.player.isPotionActive(Potion.jump))
			return mc.player.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
		else
			return 0;
	}

	public static void setMotion(double speed) {
		double forward = mc.player.movementInput.moveForward;
		double strafe = mc.player.movementInput.moveStrafe;
		float yaw = mc.player.rotationYaw;
		if ((forward == 0.0D) && (strafe == 0.0D)) {
		} else {
			if (forward != 0.0D) {
				if (strafe > 0.0D) {
					yaw += (forward > 0.0D ? -45 : 45);
				} else if (strafe < 0.0D) {
					yaw += (forward > 0.0D ? 45 : -45);
				}
				strafe = 0.0D;
				if (forward > 0.0D) {
					forward = 1;
				} else if (forward < 0.0D) {
					forward = -1;
				}
			}
			mc.player.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0F))
					+ strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F));
			mc.player.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0F))
					- strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F));
		}
	}

	public static Block getBlockUnderPlayer(EntityPlayer inPlayer, double height) {
		return Minecraft.getMinecraft().world
				.getBlockState(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ)).getBlock();
	}

	public static void setSpeed(EventMove moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe,
			double pseudoForward) {
		double forward = pseudoForward;
		double strafe = pseudoStrafe;
		double shift = 0;
		float yaw = pseudoYaw;
		if (forward != 0.0) {
			if (strafe > 0.0) {
				yaw += (float) (forward > 0.0 ? -45 : 45);
			} else if (strafe < 0.0) {
				yaw += (float) (forward > 0.0 ? 45 : -45);
			}
			strafe = 0.0;
			if (forward > 0.0) {
				forward = 1.0;
			} else if (forward < 0.0) {
				forward = -1.0;
			}
		}
		if (strafe > 0.0) {
			strafe = 1.0;
		} else if (strafe < 0.0) {
			strafe = -1.0;
		}
		double mx = Math.cos(Math.toRadians(yaw + 90.0f));
		double mz = Math.sin(Math.toRadians(yaw + 90.0f));
		moveEvent.x = forward * moveSpeed * mx + strafe * moveSpeed * mz + shift;
		moveEvent.z = forward * moveSpeed * mz - strafe * moveSpeed * mx - shift;
	}

	public static void setSpeed(EventMove moveEvent, double moveSpeed) {
		MoveUtils.setSpeed(moveEvent, moveSpeed, Minecraft.getMinecraft().player.rotationYaw,
				(double) mc.player.movementInput.moveStrafe * 0.9, mc.player.movementInput.moveForward);
	}

	public static double defaultSpeed() {
		double baseSpeed = 0.2873;
		if (Minecraft.getMinecraft().player.isPotionActive(Potion.moveSpeed)) {
			final int amplifier = Minecraft.getMinecraft().player.getActivePotionEffect(Potion.moveSpeed)
					.getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
		}
		return baseSpeed;
	}

	public static float getSpeed() {
		return (float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
	}

	public static void strafe() {
		strafe((double) getSpeed());
	}

	public static boolean isMoving() {
		return mc.player != null
				&& (mc.player.movementInput.moveForward != 0F || mc.player.movementInput.moveStrafe != 0F);
	}

	public static boolean hasMotion() {
		return mc.player.motionX != 0D && mc.player.motionZ != 0D && mc.player.motionY != 0D;
	}

	public static void strafe(Double double1) {
		if (!isMoving())
			return;
		final double yaw = getDirection();
		mc.player.motionX = -Math.sin(yaw) * double1;
		mc.player.motionZ = Math.cos(yaw) * double1;
	}

	public static void forward(final double length) {
		final double yaw = Math.toRadians(mc.player.rotationYaw);
		mc.player.setPosition(mc.player.posX + (-Math.sin(yaw) * length), mc.player.posY,
				mc.player.posZ + (Math.cos(yaw) * length));
	}

	public static double getDirection() {
		float rotationYaw = mc.player.rotationYaw;
		if (mc.player.moveForward < 0F)
			rotationYaw += 180F;
		float forward = 1F;
		if (mc.player.moveForward < 0F)
			forward = -0.5F;
		else if (mc.player.moveForward > 0F)
			forward = 0.5F;
		if (mc.player.moveStrafing > 0F)
			rotationYaw -= 90F * forward;
		if (mc.player.moveStrafing < 0F)
			rotationYaw += 90F * forward;
		return Math.toRadians(rotationYaw);
	}

	public static void setSpeedWithShift(double speed) {
		double forward = MoveUtils.mc.player.movementInput.moveForward;
		double strafe = MoveUtils.mc.player.movementInput.moveStrafe;
		double shift = (Math.random() - Math.asin(Math.random())) * 0.01;
		float yaw = MoveUtils.mc.player.rotationYaw;

		if (forward == 0.0 && strafe == 0.0) {
			MoveUtils.mc.player.motionX = 0;
			MoveUtils.mc.player.motionZ = 0;
		} else {
			if (forward != 0.0) {
				if (strafe > 0.0) {
					yaw += (float) (forward > 0.0 ? -45 : 45);
				} else if (strafe < 0.0) {
					yaw += (float) (forward > 0.0 ? 45 : -45);
				}
				strafe = 0.0;
				if (forward > 0.0) {
					forward = 1.0;
				} else if (forward < 0.0) {
					forward = -1.0;
				}
			}
//			Helper.sendMessage(shift);
			mc.player.motionX = forward * speed * Math.cos((double) Math.toRadians((double) (yaw + 90.0f)))
					+ strafe * speed * Math.sin((double) Math.toRadians((double) (yaw + 90.0f))) + shift;

			mc.player.motionZ = forward * speed * Math.sin((double) Math.toRadians((double) (yaw + 90.0f)))
					- strafe * speed * Math.cos((double) Math.toRadians((double) (yaw + 90.0f))) - shift;
		}
	}
	
	public static void setSpeedNoShift(double speed) {
		double forward = MoveUtils.mc.player.movementInput.moveForward;
		double strafe = MoveUtils.mc.player.movementInput.moveStrafe;
		float yaw = MoveUtils.mc.player.rotationYaw;

		if (forward == 0.0 && strafe == 0.0) {
			MoveUtils.mc.player.motionX = 0;
			MoveUtils.mc.player.motionZ = 0;
		} else {
			if (forward != 0.0) {
				if (strafe > 0.0) {
					yaw += (float) (forward > 0.0 ? -45 : 45);
				} else if (strafe < 0.0) {
					yaw += (float) (forward > 0.0 ? 45 : -45);
				}
				strafe = 0.0;
				if (forward > 0.0) {
					forward = 1.0;
				} else if (forward < 0.0) {
					forward = -1.0;
				}
			}
//			Helper.sendMessage(shift);
			mc.player.motionX = forward * speed * Math.cos((double) Math.toRadians((double) (yaw + 90.0f)))
					+ strafe * speed * Math.sin((double) Math.toRadians((double) (yaw + 90.0f)));

			mc.player.motionZ = forward * speed * Math.sin((double) Math.toRadians((double) (yaw + 90.0f)))
					- strafe * speed * Math.cos((double) Math.toRadians((double) (yaw + 90.0f)));
		}
	}

	public static double getBaseMoveSpeed() {
		double baseSpeed = 0.1675;
		if (MoveUtils.mc.player.isPotionActive(Potion.moveSpeed)) {
			baseSpeed *= 1.0
					+ 0.2 * (double) (MoveUtils.mc.player.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
		}
		return baseSpeed;
	}

	public static double getJumpBoostModifier(double baseJumpHeight) {
		if (MoveUtils.mc.player.isPotionActive(Potion.jump)) {
			int amplifier = MoveUtils.mc.player.getActivePotionEffect(Potion.jump).getAmplifier();
			baseJumpHeight += (double) ((float) (amplifier + 1) * 0.1f);
		}
		return baseJumpHeight;
	}

	public static boolean isInLiquid() {
		if (MoveUtils.mc.player == null) {
			return false;
		}
		if (MoveUtils.mc.player.isInWater()) {
			return true;
		}
		boolean var1 = false;
		int var2 = (int) MoveUtils.mc.player.getEntityBoundingBox().minY;
		for (double var3 = Math.floor((double) MoveUtils.mc.player.getEntityBoundingBox().minX); var3 < Math
				.floor((double) MoveUtils.mc.player.getEntityBoundingBox().maxX) + 1; ++var3) {
			for (double var4 = Math.floor((double) MoveUtils.mc.player.getEntityBoundingBox().minZ); var4 < Math
					.floor((double) MoveUtils.mc.player.getEntityBoundingBox().maxZ) + 1; ++var4) {
				Block var5 = MoveUtils.mc.world.getBlockState(new BlockPos(var3, var2, var4)).getBlock();
				if (var5 == null || var5.getMaterial() == Material.air)
					continue;
				if (!(var5 instanceof BlockLiquid)) {
					return false;
				}
				var1 = true;
			}
		}
		return var1;
	}

	public static void setSpeedWithEvent(EventMove event, double speed) {
		double forward = mc.player.movementInput.moveForward;
		double strafe = mc.player.movementInput.moveStrafe;
		float yaw = mc.player.rotationYaw;
		if ((forward == 0.0D) && (strafe == 0.0D)) {
			event.setX(0.0D);
			event.setZ(0.0D);
		} else {
			if (forward != 0.0D) {
				if (strafe > 0.0D) {
					yaw += (forward > 0.0D ? -45 : 45);
				} else if (strafe < 0.0D) {
					yaw += (forward > 0.0D ? 45 : -45);
				}
				strafe = 0.0D;
				if (forward > 0.0D) {
					forward = 1;
				} else if (forward < 0.0D) {
					forward = -1;
				}
			}
			event.setX(mc.player.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 88.0D))
					+ strafe * speed * Math.sin(Math.toRadians(yaw + 87.9000815258789D)));
			event.setZ(mc.player.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 88.0D))
					- strafe * speed * Math.cos(Math.toRadians(yaw + 87.9000815258789D)));

		}
	}

	public static boolean MovementInput() {
		if (!(mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindLeft.pressed
				|| mc.gameSettings.keyBindRight.pressed
				|| mc.gameSettings.keyBindBack.pressed)) {
			return false;
		}
		return true;
	}

	public static boolean isAirUnder(Entity ent) {
		return mc.world.getBlockState(new BlockPos(ent.posX, ent.posY - 1.0, ent.posZ))
				.getBlock() == Blocks.air;
	}

	public static boolean isMovingKeyBindingActive() {
		GameSettings set = mc.gameSettings;
		return set.keyBindForward.pressed || set.keyBindLeft.pressed || set.keyBindRight.pressed || set.keyBindBack.pressed;
	}
}
