/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Util.math;

import java.util.Random;

import javax.vecmath.Vector3d;

import cn.Noble.Event.Angle;
import cn.Noble.Event.AngleUtility;
import cn.Noble.Util.Chat.Helper;
import cn.Noble.Util.rotaions.Rotation;
import cn.Noble.Util.rotaions.VecRotation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class RotationUtil {
    public static float[] prevRotations;
    private static Minecraft mc;
    public static Random random;
    public static double x;
    public static double y;
    public static double z;

    static {
        RotationUtil.mc = Minecraft.getMinecraft();
        RotationUtil.random = new Random();
        RotationUtil.x = RotationUtil.random.nextDouble();
        RotationUtil.y = RotationUtil.random.nextDouble();
        RotationUtil.z = RotationUtil.random.nextDouble();
        RotationUtil.prevRotations = new float[2];
    }
    
	public static float pitch() {
        return Helper.mc.player.rotationPitch;
    }

    public static void pitch(float pitch) {
        Helper.mc.player.rotationPitch = pitch;
    }

    public static float yaw() {
        return Helper.mc.player.rotationYaw;
    }

    public static void yaw(float yaw) {
        Helper.mc.player.rotationYaw = yaw;
    }
    public static boolean canEntityBeSeen(Entity e){
    	Vec3 vec1 = new Vec3(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY + Minecraft.getMinecraft().player.getEyeHeight(),Minecraft.getMinecraft().player.posZ);

    	AxisAlignedBB box = e.getEntityBoundingBox();
        Vec3 vec2 = new Vec3(e.posX, e.posY + (e.getEyeHeight()/1.32F),e.posZ);	
        double minx = e.posX - 0.25;
        double maxx = e.posX + 0.25;
        double miny = e.posY;
        double maxy = e.posY + Math.abs(e.posY - box.maxY) ;
        double minz = e.posZ - 0.25;
        double maxz = e.posZ + 0.25;
        boolean see =  Minecraft.getMinecraft().world.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(maxx,miny,minz);	
	    see = Minecraft.getMinecraft().world.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(minx,miny,minz);	
	    see = Minecraft.getMinecraft().world.rayTraceBlocks(vec1, vec2) == null? true:false;
	  
	    if(see)
	    	return true;
	    vec2 = new Vec3(minx,miny,maxz);	
	    see = Minecraft.getMinecraft().world.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(maxx,miny,maxz);	
	    see = Minecraft.getMinecraft().world.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    
	    vec2 = new Vec3(maxx, maxy,minz);	
	    see = Minecraft.getMinecraft().world.rayTraceBlocks(vec1, vec2) == null? true:false;
	  
	    if(see)
	    	return true;
	    vec2 = new Vec3(minx, maxy,minz);	
	
	    see = Minecraft.getMinecraft().world.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(minx, maxy,maxz - 0.1);	
	    see = Minecraft.getMinecraft().world.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(maxx, maxy,maxz);	
	    see = Minecraft.getMinecraft().world.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    
	
    	return false;
    }
    public static float[] getPredictedRotations(EntityLivingBase ent) {
        double x = ent.posX + (ent.posX - ent.lastTickPosX);
        double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
        double y = ent.posY + ent.getEyeHeight() / 2.0F;
        return getRotationFromPosition(x, z, y);
    }
    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.getMinecraft().player.posX;
        double zDiff = z - Minecraft.getMinecraft().player.posZ;
        double yDiff = y - Minecraft.getMinecraft().player.posY - 1.2;

        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
        return new float[]{yaw, pitch};
    }

    public static float[] faceTarget(Entity target, float p_706252, float p_706253, boolean miss) {
        double var6;
        double var4 = target.posX - Helper.mc.player.posX;
        double var8 = target.posZ - Helper.mc.player.posZ;
        if (target instanceof EntityLivingBase) {
            EntityLivingBase var10 = (EntityLivingBase)target;
            var6 = var10.posY + (double)var10.getEyeHeight() - (Helper.mc.player.posY + (double)Helper.mc.player.getEyeHeight());
        } else {
            var6 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (Helper.mc.player.posY + (double)Helper.mc.player.getEyeHeight());
        }
        Random rnd = new Random();
        double var14 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float var12 = (float)(Math.atan2(var8, var4) * 180.0 / 3.141592653589793) - 90.0f;
        float var13 = (float)(- Math.atan2(var6 - (target instanceof EntityPlayer ? 0.25 : 0.0), var14) * 180.0 / 3.141592653589793);
        float pitch = RotationUtil.changeRotation(Helper.mc.player.rotationPitch, var13, p_706253);
        float yaw = RotationUtil.changeRotation(Helper.mc.player.rotationYaw, var12, p_706252);
        return new float[]{yaw, pitch};
    }
    public static float getYawChange(float yaw, double posX, double posZ) {
        double deltaX = posX - Minecraft.getMinecraft().player.posX;
        double deltaZ = posZ - Minecraft.getMinecraft().player.posZ;
        double yawToEntity = 0;
        if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
        	if(deltaX != 0)
            yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
        	if(deltaX != 0)
            yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else {
        	if(deltaZ != 0)
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }

        return MathHelper.wrapAngleTo180_float(-(yaw- (float) yawToEntity));
    }

    public static float getPitchChange(float pitch, Entity entity, double posY) {
        double deltaX = entity.posX - Minecraft.getMinecraft().player.posX;
        double deltaZ = entity.posZ - Minecraft.getMinecraft().player.posZ;
        double deltaY = posY - 2.2D + entity.getEyeHeight() - Minecraft.getMinecraft().player.posY;
        double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(pitch - (float) pitchToEntity) - 2.5F;
    }
    public static float getYawChange(double posX, double posZ) {
        double deltaX = posX - Minecraft.getMinecraft().player.posX;
        double deltaZ = posZ - Minecraft.getMinecraft().player.posZ;
        double yawToEntity;
        if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
            yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
            yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return MathHelper.wrapAngleTo180_float(-(Minecraft.getMinecraft().player.rotationYaw - (float) yawToEntity));
    }
    public static float getYawChangeGiven(double posX, double posZ, float yaw) {
        Minecraft.getMinecraft();
        double deltaX = posX - Minecraft.getMinecraft().player.posX;
        Minecraft.getMinecraft();
        double deltaZ = posZ - Minecraft.getMinecraft().player.posZ;
        double yawToEntity;
        if (deltaZ < 0.0D && deltaX < 0.0D) {
           yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else if (deltaZ < 0.0D && deltaX > 0.0D) {
           yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else {
           yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }

        return MathHelper.wrapAngleTo180_float(-(yaw - (float)yawToEntity));
     }



    public static float getPitchChange(Entity entity, double posY) {
        double deltaX = entity.posX - Minecraft.getMinecraft().player.posX;
        double deltaZ = entity.posZ - Minecraft.getMinecraft().player.posZ;
        double deltaY = posY - 2.2D + entity.getEyeHeight() - Minecraft.getMinecraft().player.posY;
        double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().player.rotationPitch - (float) pitchToEntity) - 2.5F;
    }
    public static float changeRotation(float p_706631, float p_706632, float p_706633) {
        float var4 = MathHelper.wrapAngleTo180_float(p_706632 - p_706631);
        if (var4 > p_706633) {
            var4 = p_706633;
        }
        if (var4 < - p_706633) {
            var4 = - p_706633;
        }
        return p_706631 + var4;
    }

    public static double[] getRotationToEntity(Entity entity) {
        double pX = Helper.mc.player.posX;
        double pY = Helper.mc.player.posY + (double)Helper.mc.player.getEyeHeight();
        double pZ = Helper.mc.player.posZ;
        double eX = entity.posX;
        double eY = entity.posY + (double)(entity.height / 2.0f);
        double eZ = entity.posZ;
        double dX = pX - eX;
        double dY = pY - eY;
        double dZ = pZ - eZ;
        double dH = Math.sqrt(Math.pow(dX, 2.0) + Math.pow(dZ, 2.0));
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        double pitch = Math.toDegrees(Math.atan2(dH, dY));
        return new double[]{yaw, 90.0 - pitch};
    }

    public static float[] getRotations2(EntityLivingBase ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + (double)(ent.getEyeHeight() / 2.0F);
        return getRotationFromPosition(x, z, y);
     }
    public static float[] getRotations(Entity entity) {
        double diffY;
        if (entity == null) {
            return null;
        }
        double diffX = entity.posX - Helper.mc.player.posX;
        double diffZ = entity.posZ - Helper.mc.player.posZ;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase elb = (EntityLivingBase)entity;
            diffY = elb.posY + ((double)elb.getEyeHeight() - 0.4) - (Helper.mc.player.posY + (double)Helper.mc.player.getEyeHeight());
        } else {
            diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0 - (Helper.mc.player.posY + (double)Helper.mc.player.getEyeHeight());
        }
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }
    public static float[] getRotationsLigma(EntityLivingBase ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + ent.getEyeHeight() / 2.0F;
        return getRotationFromPosition(x, z, y);
    }
    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 0.0f;
        }
        return angle3;
    }

    public static float[] grabBlockRotations(BlockPos pos) {
        return RotationUtil.getVecRotation(Helper.mc.player.getPositionVector().addVector(0.0, Helper.mc.player.getEyeHeight(), 0.0), new Vec3((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5));
    }

    public static float[] getVecRotation(Vec3 position) {
        return RotationUtil.getVecRotation(Helper.mc.player.getPositionVector().addVector(0.0, Helper.mc.player.getEyeHeight(), 0.0), position);
    }

    public static float[] getVecRotation(Vec3 origin, Vec3 position) {
        Vec3 difference = position.subtract(origin);
        double distance = difference.normalize().lengthVector();
        float yaw = (float)Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0f;
        float pitch = (float)(- Math.toDegrees(Math.atan2(difference.yCoord, distance)));
        return new float[]{yaw, pitch};
    }

    public static int wrapAngleToDirection(float yaw, int zones) {
        int angle = (int)((double)(yaw + (float)(360 / (2 * zones))) + 0.5) % 360;
        if (angle < 0) {
            angle += 360;
        }
        return angle / (360 / zones);
    }
	public static float[] getRotationsInsane(Entity entity, double maxRange) {
	      if (entity == null) {
		         System.out.println("Fuck you ! Entity is nul!");
		         return null;
		      } else {
		         double diffX = entity.posX - Minecraft.getMinecraft().player.posX;
		         double diffZ = entity.posZ - Minecraft.getMinecraft().player.posZ;
		         Location BestPos = new Location(entity.posX, entity.posY, entity.posZ);
		         Location myEyePos = new Location(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY + (double)Minecraft.getMinecraft().player.getEyeHeight(), Minecraft.getMinecraft().player.posZ);

		         double diffY;
		         for(diffY = entity.boundingBox.minY + 0.7D; diffY < entity.boundingBox.maxY - 0.1D; diffY += 0.1D) {
		            if (myEyePos.distanceTo(new Location(entity.posX, diffY, entity.posZ)) < myEyePos.distanceTo(BestPos)) {
		               BestPos = new Location(entity.posX, diffY, entity.posZ);
		            }
		         }

		         if (myEyePos.distanceTo(BestPos) >= maxRange) {
		            return null;
		         } else {
		            diffY = BestPos.getY() - (Minecraft.getMinecraft().player.posY + (double)Minecraft.getMinecraft().player.getEyeHeight());
		            double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		            float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
		            float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
		            return new float[]{yaw, pitch};
		         }
		      }
		   }

    public static float[] getRotationsForAura(Entity entity, double maxRange) {
        double diffY;
        if (entity == null) {
            System.out.println("Fuck you ! Entity is nul!");
            return null;
        }
        Minecraft.getMinecraft();
        double diffX = entity.posX - Minecraft.getMinecraft().player.posX;
        Minecraft.getMinecraft();
        double diffZ = entity.posZ - Minecraft.getMinecraft().player.posZ;
        Location BestPos = new Location(entity.posX, entity.posY, entity.posZ);
        Minecraft.getMinecraft();
        Location myEyePos = new Location(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY + (double)Minecraft.getMinecraft().player.getEyeHeight(), Minecraft.getMinecraft().player.posZ);
        for (diffY = entity.boundingBox.minY + 0.7; diffY < entity.boundingBox.maxY - 0.1; diffY += 0.1) {
            if (myEyePos.distanceTo(new Location(entity.posX, diffY, entity.posZ)) >= myEyePos.distanceTo(BestPos)) continue;
            BestPos = new Location(entity.posX, diffY, entity.posZ);
        }
        if (myEyePos.distanceTo(BestPos) >= maxRange) {
            return null;
        }
        diffY = BestPos.getY() - (Minecraft.getMinecraft().player.posY + (double)Minecraft.getMinecraft().player.getEyeHeight());
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static float[] getEntityRotations(EntityLivingBase target, float[] lastrotation, boolean aac, int smooth) {
        Minecraft mc = Minecraft.getMinecraft();
        AngleUtility angleUtility = new AngleUtility(aac, smooth);
        Vector3d enemyCoords = new Vector3d(target.posX, target.posY + (double)target.getEyeHeight(), target.posZ);
        Vector3d myCoords = new Vector3d(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY + (double)Minecraft.getMinecraft().player.getEyeHeight(), Minecraft.getMinecraft().player.posZ);
        Angle dstAngle = angleUtility.calculateAngle(enemyCoords, myCoords, target);
        Angle srcAngle = new Angle(lastrotation[0], lastrotation[1]);
        Angle smoothedAngle = angleUtility.smoothAngle(dstAngle, srcAngle);
        float yaw = smoothedAngle.getYaw();
        float pitch = smoothedAngle.getPitch();
        float yaw2 = MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().player.rotationYaw);
        yaw = Minecraft.getMinecraft().player.rotationYaw + yaw2;
        return new float[]{yaw, pitch};
    }
    
    public static float[] getEntityRotations(final Entity entity, final float[] array, final int n) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        final AngleUtility rotationUtils = new AngleUtility((float)n);
        final Angle smoothAngle = rotationUtils.smoothAngle(rotationUtils.calculateAngle(new Vector3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ), new Vector3d(minecraft.player.posX, minecraft.player.posY + minecraft.player.getEyeHeight(), minecraft.player.posZ)), new Angle(array[0], array[1]));
        return new float[] { minecraft.player.rotationYaw + MathHelper.wrapAngleTo180_float(smoothAngle.getYaw() - minecraft.player.rotationYaw), smoothAngle.getPitch() };
    }

    public static float[] Rotate(EntityLivingBase ent) {
        final double x = ent.posX - Minecraft.getMinecraft().player.posX;
        double y = ent.posY - Minecraft.getMinecraft().player.posY;
        final double z = ent.posZ - Minecraft.getMinecraft().player.posZ;
        y /= Minecraft.getMinecraft().player.getDistanceToEntity(ent);
        final float yaw = (float) (-(Math.atan2(x, z) * 57.29577951308232));
        final float pitch = (float) (-(Math.asin(y) * 57.29577951308232));
        return new float[]{yaw, pitch};
    }

    public static void faceVectorPacketInstant(final Vec3 vec) {
        final float[] rotations = getNeededRotations(vec);
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        final Minecraft mc = Minecraft.getMinecraft();
        final NetHandlerPlayClient sendQueue = Minecraft.getMinecraft().player.sendQueue;
        final float playerYaw = rotations[0];
        final float playerPitch = rotations[1];
        final Minecraft mc2 = Minecraft.getMinecraft();
        sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(playerYaw, playerPitch, Minecraft.getMinecraft().player.onGround));
    }
    
    public static float[] getNeededRotations(final Vec3 vec) {
        final Vec3 eyesPos = getEyesPos();
        final double diffX = vec.xCoord - eyesPos.xCoord + 0.5;
        final double diffY = vec.yCoord - eyesPos.yCoord + 0.5;
        final double diffZ = vec.zCoord - eyesPos.zCoord + 0.5;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.atan2(diffY, diffXZ) * 180.0 / 3.141592653589793);
        final float[] arrf = { MathHelper.wrapAngleTo180_float(yaw), Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed() ? 90.0f : MathHelper.wrapAngleTo180_float(pitch) };
        return arrf;
    }
    
    public static Vec3 getEyesPos() {
        final Minecraft mc = Minecraft.getMinecraft();
        final double posX = Minecraft.getMinecraft().player.posX;
        final Minecraft mc2 = Minecraft.getMinecraft();
        final double posY = Minecraft.getMinecraft().player.posY;
        final Minecraft mc3 = Minecraft.getMinecraft();
        final double y = posY + Minecraft.getMinecraft().player.getEyeHeight();
        final Minecraft mc4 = Minecraft.getMinecraft();
        return new Vec3(posX, y, Minecraft.getMinecraft().player.posZ);
    }

    public static VecRotation faceBlock(final BlockPos blockPos) {
        if (blockPos == null) {
            return null;
        }
        VecRotation vecRotation = null;
        for (double xSearch = 0.1; xSearch < 0.9; xSearch += 0.1) {
            for (double ySearch = 0.1; ySearch < 0.9; ySearch += 0.1) {
                for (double zSearch = 0.1; zSearch < 0.9; zSearch += 0.1) {
                    final Vec3 eyesPos = new Vec3(RotationUtil.mc.player.posX, RotationUtil.mc.player.getEntityBoundingBox().minY + RotationUtil.mc.player.getEyeHeight(), RotationUtil.mc.player.posZ);
                    final Vec3 posVec = new Vec3(blockPos).addVector(xSearch, ySearch, zSearch);
                    final double dist = eyesPos.distanceTo(posVec);
                    final double diffX = posVec.xCoord - eyesPos.xCoord;
                    final double diffY = posVec.yCoord - eyesPos.yCoord;
                    final double diffZ = posVec.zCoord - eyesPos.zCoord;
                    final double diffXZ = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
                    final Rotation rotation = new Rotation(MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f), MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)))));
                    final Vec3 rotationVector = getVectorForRotation(rotation);
                    final Vec3 vector = eyesPos.addVector(rotationVector.xCoord * dist, rotationVector.yCoord * dist, rotationVector.zCoord * dist);
                    final MovingObjectPosition obj = RotationUtil.mc .world.rayTraceBlocks(eyesPos, vector, false, false, true);
                    if (obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                        final VecRotation currentVec = new VecRotation(posVec, rotation);
                        if (vecRotation == null || getRotationDifference(currentVec.getRotation()) < getRotationDifference(vecRotation.getRotation())) {
                            vecRotation = currentVec;
                        }
                    }
                }
            }
        }
        return vecRotation;
    }
    
    public static Vec3 getVectorForRotation(final Rotation rotation) {
        final float yawCos = MathHelper.cos(-rotation.getYaw() * 0.017453292f - 3.1415927f);
        final float yawSin = MathHelper.sin(-rotation.getYaw() * 0.017453292f - 3.1415927f);
        final float pitchCos = -MathHelper.cos(-rotation.getPitch() * 0.017453292f);
        final float pitchSin = MathHelper.sin(-rotation.getPitch() * 0.017453292f);
        return new Vec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
    }
    
    public static double getRotationDifference(final Rotation a, final Rotation b) {
        return Math.hypot(getAngleDifference(a.getYaw(), b.getYaw()), a.getPitch() - b.getPitch());
    }
    
    private static float getAngleDifference(final float a, final float b) {
        return ((a - b) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }
    
    public static double getRotationDifference(final Rotation rotation) {
        return (RotationUtil.prevRotations == null) ? 0.0 : getRotationDifference(rotation, new Rotation(RotationUtil.prevRotations[0], RotationUtil.prevRotations[1]));
    }

    public static Rotation limitAngleChange(final Rotation currentRotation, final Rotation targetRotation, final float turnSpeed) {
        final float yawDifference = getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        final float pitchDifference = getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());
        return new Rotation(currentRotation.getYaw() + ((yawDifference > turnSpeed) ? turnSpeed : Math.max(yawDifference, -turnSpeed)), currentRotation.getPitch() + ((pitchDifference > turnSpeed) ? turnSpeed : Math.max(pitchDifference, -turnSpeed)));
    }

    private static float[] getRotationsByVec(final Vec3 origin, final Vec3 position) {
        final Vec3 difference = position.subtract(origin);
        final double distance = difference.flat().lengthVector();
        final float yaw = (float)Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(difference.yCoord, distance)));
        return new float[] { yaw, pitch };
    }

    public static float[] getRotationBlock(final BlockPos pos) {
        return getRotationsByVec(Minecraft.player.getPositionVector().addVector(0.0, Minecraft.player.getEyeHeight(), 0.0), new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }
}

