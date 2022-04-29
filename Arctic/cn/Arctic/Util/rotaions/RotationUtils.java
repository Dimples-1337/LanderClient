package cn.Arctic.Util.rotaions;

import java.util.List;

import cn.Arctic.Util.MoveUtils;
import cn.Arctic.Util.Wrapper;
import cn.Arctic.Util.Player.PlayerUtil;
import cn.Arctic.Util.math.Location;
import cn.Arctic.Util.math.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtils {
	static Minecraft mc = Minecraft.getMinecraft();
    public static Rotation serverRotation = new Rotation(0F, 0F);

//	private Entity raycastEntity(double range, float yaw, float pitch, EntityFilter entityFilter) {
//    	Entity renderViewEntity = mc.renderViewEntity;
//
//        if (renderViewEntity != null && mc.world != null) {
//        	double blockReachDistance = range;
//        	Vec3 eyePosition = renderViewEntity.getPositionEyes(1f);
//
//            double yawCos = Math.cos(-yaw * 0.017453292f - Math.PI);
//            double yawSin = Math.sin(-yaw * 0.017453292f - Math.PI);
//            double pitchCos = (-Math.cos(-pitch * 0.017453292f));
//            double pitchSin = Math.sin(-pitch * 0.017453292f);
//
//            Vec3 entityLook = new Vec3((yawSin * pitchCos), pitchSin, (yawCos * pitchCos));
//            Vec3 vector = eyePosition.addVector(entityLook.xCoord * blockReachDistance, entityLook.yCoord * blockReachDistance, entityLook.zCoord * blockReachDistance);
//            List<Entity> entityList = mc.world.getEntitiesInAABBexcluding(renderViewEntity, renderViewEntity.boundingBox.
//            		addCoord(entityLook.xCoord * blockReachDistance, entityLook.yCoord * blockReachDistance, entityLook.zCoord * blockReachDistance).
//            		expand(1.0, 1.0, 1.0), null);
//
//            //renderViewEntity != null && (!(renderViewEntity instanceof EntityPlayer) || !((EntityPlayer)renderViewEntity).isSpectator()) && renderViewEntity.canBeCollidedWith()
//            
//            Entity pointedEntity = null;
//
//            for (int i = 0; i <= entityList.size(); i++) {
//            	Entity entity = entityList.get(i);
//                if (!entityFilter.canRaycast(entity));
//                    continue;
//
//                float collisionBorderSize = entity.getCollisionBorderSize();
//                AxisAlignedBB axisAlignedBB = entity.getCollisionBorderSize().expand(collisionBorderSize, collisionBorderSize, collisionBorderSize);
//
//                val movingObjectPosition = axisAlignedBB.calculateIntercept(eyePosition, vector);
//
//                if (axisAlignedBB.isVecInside(eyePosition)) {
//                    if (blockReachDistance >= 0.0) {
//                        pointedEntity = entity;
//                        blockReachDistance = 0.0;
//                    }
//                } else if (movingObjectPosition != null) {
//                    val eyeDistance = eyePosition.distanceTo(movingObjectPosition.hitVec);
//
//                    if (eyeDistance < blockReachDistance || blockReachDistance == 0.0) {
//                        if (entity == renderViewEntity.ridingEntity && !renderViewEntity.canRiderInteract()) {
//                            if (blockReachDistance == 0.0)
//                                pointedEntity = entity;
//                        } else {
//                            pointedEntity = entity;
//                            blockReachDistance = eyeDistance;
//                        }
//                    }
//                }
//            }
//
//            return pointedEntity;
//        }
//
//        return null;
//    }
//
//	interface EntityFilter {
//		boolean canRaycast(Entity entity);
//	}

	public static float[] getRotations(EntityLivingBase ent) {
		double x = ent.posX;
		double z = ent.posZ;
		double y = ent.posY + ent.getEyeHeight() / 2.0F;
		return getRotationFromPosition(x, z, y);
	}

	public static Vec3 getEyesPos() {
		return new Vec3(Minecraft.getMinecraft().player.posX,
				Minecraft.getMinecraft().player.posY + (double) Minecraft.getMinecraft().player.getEyeHeight(),
				Minecraft.getMinecraft().player.posZ);
	}

//	public static boolean isFaced(Entity targetEntity, double blockReachDistance) {
//		return raycastEntity(blockReachDistance, entity -> targetEntity != null && targetEntity.equals(entity)) != null;
//	}

	public static float[] getNeededRotations(Vec3 vec) {
		Vec3 eyesPos = RotationUtils.getEyesPos();
		double diffX = vec.xCoord - eyesPos.xCoord + 0.5;
		double diffY = vec.yCoord - eyesPos.yCoord + 0.5;
		double diffZ = vec.zCoord - eyesPos.zCoord + 0.5;
		double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
		float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
		float pitch = (float) ((-Math.atan2(diffY, diffXZ)) * 180.0 / 3.141592653589793);
		float[] arrf = new float[] { MathHelper.wrapAngleTo180_float(yaw),
				Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed() ? 90.0f
						: MathHelper.wrapAngleTo180_float(pitch) };
		return arrf;
	}

	public static void faceVectorPacketInstant(Vec3 vec) {
		float[] rotations = RotationUtils.getNeededRotations(vec);
		Minecraft.getMinecraft();
		Minecraft.getMinecraft();
		Minecraft.getMinecraft().player.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(rotations[0],
				rotations[1], Minecraft.getMinecraft().player.onGround));
	}

	public static float[] getRotationsexcel(Entity entity) {
		double diffY;
		if (entity == null) {
			return null;
		}
		Minecraft.getMinecraft();
		double diffX = entity.posX - Minecraft.getMinecraft().player.posX;
		Minecraft.getMinecraft();
		double diffZ = entity.posZ - Minecraft.getMinecraft().player.posZ;
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase elb = (EntityLivingBase) entity;
			Minecraft.getMinecraft();
			Minecraft.getMinecraft();
			diffY = elb.posY + ((double) elb.getEyeHeight() - 0.4)
					- (Minecraft.getMinecraft().player.posY + (double) Minecraft.getMinecraft().player.getEyeHeight());
		} else {
			Minecraft.getMinecraft();
			Minecraft.getMinecraft();
			diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0
					- (Minecraft.getMinecraft().player.posY + (double) Minecraft.getMinecraft().player.getEyeHeight());
		}
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
		float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
		return new float[] { yaw, pitch };
	}

	public static float[] getPredictedRotations(EntityLivingBase ent) {
		double x = ent.posX + (ent.posX - ent.lastTickPosX);
		double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
		double y = ent.posY + ent.getEyeHeight() / 2.0F;
		return getRotationFromPosition(x, z, y);
	}

	public static float[] getRotationsByBoundingBox(EntityLivingBase ent) {
		AxisAlignedBB boundingBox = ent.boundingBox;
		double[] randomBounding = new double[] { (boundingBox.maxX - boundingBox.minX) / 4,
				(boundingBox.maxY - boundingBox.minY) / 4, (boundingBox.maxZ - boundingBox.minZ) / 4 };
		double x = ent.posX + (ent.posX - ent.lastTickPosX);
		double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
		double y = ent.posY + ent.getEyeHeight() / 2.0F;
		return getRotationFromPosition(x, z, y);
	}

	public static float[] getRotationByBoundingBox(Entity ent, float maxRange) {
		AxisAlignedBB boundingBox = ent.boundingBox;
		double boundingX = (boundingBox.maxX - boundingBox.minX) / 4;
		double boundingY = (boundingBox.maxY - boundingBox.minY) / 8;
		double boundingZ = (boundingBox.maxZ - boundingBox.minZ) / 4;
		double orPosX = ent.boundingBox.minX, orPosY = ent.boundingBox.minY, orPosZ = ent.boundingBox.minZ;
		////// orPosX+=randomNumber(boundingX,-boundingX);
		// orPosY+=randomNumber(boundingY,-boundingY);
		// orPosZ+=randomNumber(boundingZ,-boundingZ);
		double pX = Minecraft.getMinecraft().player.boundingBox.minX;
		double pY = Minecraft.getMinecraft().player.boundingBox.minY
				+ (Minecraft.getMinecraft().player.boundingBox.minY + Minecraft.getMinecraft().player.boundingBox.maxY)
						/ 2;
		double pZ = Minecraft.getMinecraft().player.boundingBox.minZ;
		double dX = pX - orPosX;
		double dZ = pZ - orPosZ;
		double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
		Location BestPos = new Location(ent.boundingBox.minX, orPosY, ent.boundingBox.minZ);
		Location myEyePos = new Location(Minecraft.getMinecraft().player.boundingBox.minX,
				Minecraft.getMinecraft().player.boundingBox.minY + (double) Wrapper.getPlayer().getEyeHeight(),
				Minecraft.getMinecraft().player.boundingBox.minZ);
		double diffY;
		for (diffY = ent.boundingBox.minY + 0.7D; diffY < ent.boundingBox.maxY - 0.1D; diffY += 0.1D) {
			if (myEyePos.distanceTo(new Location(ent.boundingBox.minX, diffY, ent.boundingBox.minZ)) < myEyePos
					.distanceTo(BestPos)) {
				BestPos = new Location(ent.boundingBox.minX, diffY, ent.boundingBox.minZ);
			}
		}
		if (myEyePos.distanceTo(BestPos) > maxRange) {
			diffY = BestPos.getY() - (Minecraft.getMinecraft().player.boundingBox.minY
					+ (double) Minecraft.getMinecraft().player.getEyeHeight());
			double dist = MathHelper.sqrt_double(dX * dX + dZ * dZ);
			float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
			return new float[] { (float) yaw, pitch };
		} else {
			diffY = BestPos.getY() - (Minecraft.getMinecraft().player.boundingBox.minY
					+ (double) Minecraft.getMinecraft().player.getEyeHeight());
			double dist = MathHelper.sqrt_double(dX * dX + dZ * dZ);
			float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
			return new float[] { (float) yaw, pitch };
		}

		// return new float[]{(float)yaw, (float)(90.0 - pitch)};
	}

	static double randomNumber(double var1, double var2) {
		return (Math.random() * (double) (var1 - var2)) + var2;
	}

	public static float getPitchChange1(float pitch, Entity entity, double posY) {
		double deltaX = entity.posX - Minecraft.getMinecraft().player.posX;
		double deltaZ = entity.posZ - Minecraft.getMinecraft().player.posZ;
		double deltaY = posY - 2.2D + entity.getEyeHeight() - Minecraft.getMinecraft().player.posY;
		double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
		double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
		return -MathHelper.wrapAngleTo180_float(pitch - (float) pitchToEntity) - 2.5F;
	}

	public static float getYawChange2(float yaw, double posX, double posZ) {
		double deltaX = posX - Minecraft.getMinecraft().player.posX;
		double deltaZ = posZ - Minecraft.getMinecraft().player.posZ;
		double yawToEntity = 0;
		if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
			if (deltaX != 0)
				yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		} else if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
			if (deltaX != 0)
				yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		} else {
			if (deltaZ != 0)
				yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
		}

		return MathHelper.wrapAngleTo180_float(-(yaw - (float) yawToEntity));
	}

	public static float[] getAverageRotations(List<EntityLivingBase> targetList) {
		double posX = 0.0D;
		double posY = 0.0D;
		double posZ = 0.0D;
		for (Entity ent : targetList) {
			posX += ent.posX;
			posY += ent.boundingBox.maxY - 2.0D;
			posZ += ent.posZ;
		}
		posX /= targetList.size();
		posY /= targetList.size();
		posZ /= targetList.size();

		return new float[] { getRotationFromPosition(posX, posZ, posY)[0],
				getRotationFromPosition(posX, posZ, posY)[1] };
	}

	public static float getStraitYaw() {
		float YAW = MathHelper.wrapAngleTo180_float(mc.player.rotationYaw);
		if (YAW < 45 && YAW > -45) {
			YAW = 0;
		} else if (YAW > 45 && YAW < 135) {
			YAW = 90f;
		} else if (YAW > 135 || YAW < -135) {
			YAW = 180;
		} else {
			YAW = -90f;
		}
		return YAW;
	}

	public static float getYawChange(float yaw, double posX, double posZ) {
		double deltaX = posX - Minecraft.getMinecraft().player.posX;
		double deltaZ = posZ - Minecraft.getMinecraft().player.posZ;
		double yawToEntity = 0;
		if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
			if (deltaX != 0)
				yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		} else if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
			if (deltaX != 0)
				yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		} else {
			if (deltaZ != 0)
				yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
		}

		return MathHelper.wrapAngleTo180_float(-(yaw - (float) yawToEntity));
	}

	public static float getPitchChange(float pitch, Entity entity, double posY) {
		double deltaX = entity.posX - Minecraft.getMinecraft().player.posX;
		double deltaZ = entity.posZ - Minecraft.getMinecraft().player.posZ;
		double deltaY = posY - 2.2D + entity.getEyeHeight() - Minecraft.getMinecraft().player.posY;
		double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
		double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
		return -MathHelper.wrapAngleTo180_float(pitch - (float) pitchToEntity) - 2.5F;
	}

	public static boolean canEntityBeSeen(Entity e) {
		Vec3 vec1 = new Vec3(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);

		AxisAlignedBB box = e.getEntityBoundingBox();
		Vec3 vec2 = new Vec3(e.posX, e.posY + (e.getEyeHeight() / 1.32F), e.posZ);
		double minx = e.posX - 0.25;
		double maxx = e.posX + 0.25;
		double miny = e.posY;
		double maxy = e.posY + Math.abs(e.posY - box.maxY);
		double minz = e.posZ - 0.25;
		double maxz = e.posZ + 0.25;
		boolean see = mc.world.rayTraceBlocks(vec1, vec2) == null ? true : false;
		if (see)
			return true;
		vec2 = new Vec3(maxx, miny, minz);
		see = mc.world.rayTraceBlocks(vec1, vec2) == null ? true : false;
		if (see)
			return true;
		vec2 = new Vec3(minx, miny, minz);
		see = mc.world.rayTraceBlocks(vec1, vec2) == null ? true : false;

		if (see)
			return true;
		vec2 = new Vec3(minx, miny, maxz);
		see = mc.world.rayTraceBlocks(vec1, vec2) == null ? true : false;
		if (see)
			return true;
		vec2 = new Vec3(maxx, miny, maxz);
		see = mc.world.rayTraceBlocks(vec1, vec2) == null ? true : false;
		if (see)
			return true;

		vec2 = new Vec3(maxx, maxy, minz);
		see = mc.world.rayTraceBlocks(vec1, vec2) == null ? true : false;

		if (see)
			return true;
		vec2 = new Vec3(minx, maxy, minz);

		see = mc.world.rayTraceBlocks(vec1, vec2) == null ? true : false;
		if (see)
			return true;
		vec2 = new Vec3(minx, maxy, maxz - 0.1);
		see = mc.world.rayTraceBlocks(vec1, vec2) == null ? true : false;
		if (see)
			return true;
		vec2 = new Vec3(maxx, maxy, maxz);
		see = mc.world.rayTraceBlocks(vec1, vec2) == null ? true : false;
		if (see)
			return true;

		return false;
	}

	public static float[] getBowAngles(Entity entity) {
		double xDelta = entity.posX - entity.lastTickPosX;
		double zDelta = entity.posZ - entity.lastTickPosZ;
		double d = Minecraft.getMinecraft().player.getDistanceToEntity(entity);
		d -= d % 0.8;
		double xMulti = 1.0;
		double zMulti = 1.0;
		boolean sprint = entity.isSprinting();
		xMulti = d / 0.8 * xDelta * (sprint ? 1.25 : 1.0);
		zMulti = d / 0.8 * zDelta * (sprint ? 1.25 : 1.0);
		double x = entity.posX + xMulti - Minecraft.getMinecraft().player.posX;
		double z = entity.posZ + zMulti - Minecraft.getMinecraft().player.posZ;
		double y = Minecraft.getMinecraft().player.posY + (double) Minecraft.getMinecraft().player.getEyeHeight()
				- (entity.posY + (double) entity.getEyeHeight());
		double dist = Minecraft.getMinecraft().player.getDistanceToEntity(entity);
		float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90.0f;
		float pitch = (float) Math.toDegrees(Math.atan2(y, dist));
		return new float[] { yaw, pitch };
	}

	public static float[] getRotationFromPosition(double x, double z, double y) {
		double xDiff = x - Minecraft.getMinecraft().player.posX;
		double zDiff = z - Minecraft.getMinecraft().player.posZ;
		double yDiff = y - Minecraft.getMinecraft().player.posY - 1.2;
		double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
		float pitch = (float) (-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
		return new float[] { yaw, pitch };
	}

	public static float[] getRotationFromPosition(double[] pos) {
		double xDiff = pos[0] - Minecraft.getMinecraft().player.posX;
		double zDiff = pos[1] - Minecraft.getMinecraft().player.posZ;
		double yDiff = pos[2] - Minecraft.getMinecraft().player.posY - 1.2;
		double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
		float pitch = (float) (-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
		return new float[] { yaw, pitch };
	}

	public static float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
		float g = 0.006f;
		float sqrt = velocity * velocity * velocity * velocity
				- g * (g * (d3 * d3) + 2.0f * d1 * (velocity * velocity));
		return (float) Math
				.toDegrees(Math.atan(((double) (velocity * velocity) - Math.sqrt(sqrt)) / (double) (g * d3)));
	}

	public static float getYawChange(double posX, double posZ) {
		double deltaX = posX - Minecraft.getMinecraft().player.posX;
		double deltaZ = posZ - Minecraft.getMinecraft().player.posZ;
		double yawToEntity = deltaZ < 0.0 && deltaX < 0.0 ? 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX))
				: (deltaZ < 0.0 && deltaX > 0.0 ? -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX))
						: Math.toDegrees(-Math.atan(deltaX / deltaZ)));
		return MathHelper.wrapAngleTo180_float(-(Minecraft.getMinecraft().player.rotationYaw - (float) yawToEntity));
	}

	public static float getYawChangeGiven(double posX, double posZ, float yaw) {
		double deltaX = posX - Minecraft.getMinecraft().player.posX;
		double deltaZ = posZ - Minecraft.getMinecraft().player.posZ;
		double yawToEntity = deltaZ < 0.0 && deltaX < 0.0 ? 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX))
				: (deltaZ < 0.0 && deltaX > 0.0 ? -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX))
						: Math.toDegrees(-Math.atan(deltaX / deltaZ)));
		return MathHelper.wrapAngleTo180_float(-(yaw - (float) yawToEntity));
	}

	public static float getPitchChange(Entity entity, double posY) {
		double deltaX = entity.posX - Minecraft.getMinecraft().player.posX;
		double deltaZ = entity.posZ - Minecraft.getMinecraft().player.posZ;
		double deltaY = posY - 2.2 + (double) entity.getEyeHeight() - Minecraft.getMinecraft().player.posY;
		double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
		double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
		return -MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().player.rotationPitch - (float) pitchToEntity)
				- 2.5f;
	}

	public static float getNewAngle(float angle) {
		if ((angle %= 360.0f) >= 180.0f) {
			angle -= 360.0f;
		}
		if (angle < -180.0f) {
			angle += 360.0f;
		}
		return angle;
	}

	public static float getDistanceBetweenAngles(float angle1, float angle2) {
		float angle = Math.abs(angle1 - angle2) % 360.0f;
		if (angle > 180.0f) {
			angle = 360.0f - angle;
		}
		return angle;
	}

	public static float[] getRotationsForAura(Entity entity, double maxRange) {
		double diffX = entity.posX - Minecraft.getMinecraft().player.posX;
		double diffZ = entity.posZ - Minecraft.getMinecraft().player.posZ;
		Location BestPos = new Location(entity.posX, entity.posY, entity.posZ);
		Minecraft.getMinecraft();
		double var10002 = Minecraft.getMinecraft().player.posX;
		Minecraft.getMinecraft();
		double var10003 = Minecraft.getMinecraft().player.posY
				+ (double) Minecraft.getMinecraft().player.getEyeHeight();
		Minecraft.getMinecraft();
		Location myEyePos = new Location(var10002, var10003, Minecraft.getMinecraft().player.posZ);

		for (double i = entity.boundingBox.minY + 0.7D; i < entity.boundingBox.maxY - 0.1D; i += 0.1D) {
			Location location = new Location(entity.posX, i, entity.posZ);
			if (myEyePos.distanceTo(location) < myEyePos.distanceTo(BestPos)) {
				BestPos = new Location(entity.posX, i, entity.posZ);
			}
		}

		if (myEyePos.distanceTo(BestPos) > maxRange) {
			return null;
		} else {
			double var10000 = BestPos.getY();
			Minecraft.getMinecraft();
			double var10001 = Minecraft.getMinecraft().player.posY;
			Minecraft.getMinecraft();
			double diffY = var10000 - (var10001 + (double) Minecraft.getMinecraft().player.getEyeHeight());
			double dist = (double) MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
			float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
			float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
			return new float[] { yaw, pitch };
		}
	}

	public static float[] getRotationsEntity(EntityLivingBase entity) {
		if (PlayerUtil.isOnHypixel() && MoveUtils.isMoving()) {
			return RotationUtils.getRotations(entity.posX + MathUtil.randomNumber(0.03, -0.03),
					entity.posY + (double) entity.getEyeHeight() - 0.4 + MathUtil.randomNumber(0.07, -0.07),
					entity.posZ + MathUtil.randomNumber(0.03, -0.03));
		}
		return RotationUtils.getRotations(entity.posX, entity.posY + (double) entity.getEyeHeight() - 0.4, entity.posZ);
	}

	public static float[] getRotations(double posX, double posY, double posZ) {
		ClientPlayerEntity player = Minecraft.getMinecraft().player;
		double x = posX - player.posX;
		double y = posY - (player.posY + (double) player.getEyeHeight());
		double z = posZ - player.posZ;
		double dist = MathHelper.sqrt_double(x * x + z * z);
		float yaw = (float) (Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
		float pitch = (float) (-(Math.atan2(y, dist) * 180.0 / Math.PI));
		return new float[] { yaw, pitch };
	}
}
