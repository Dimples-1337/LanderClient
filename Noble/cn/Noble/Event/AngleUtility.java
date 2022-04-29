package cn.Noble.Event;

import java.util.Random;

import javax.vecmath.Vector3d;

import org.newdawn.slick.AngelCodeFont;

import cn.Noble.Event.Vector.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;

public class AngleUtility extends Event{

	public static AngleUtility instance;
	private static float minYawSmoothing;
	private static float maxYawSmoothing;
	private static float minPitchSmoothing;
	private static float maxPitchSmoothing;
	private static Vector3<Float> delta;
	private static Angle smoothedAngle;
	private static AngleUtility angu;
	private static Random random;
    private boolean aac;
    private float smooth;
	private static float height = 1.5f;

	 public AngleUtility(float minYawSmoothing, float maxYawSmoothing, float minPitchSmoothing, float maxPitchSmoothing) {
	        this.minYawSmoothing = minYawSmoothing;
	        this.maxYawSmoothing = maxYawSmoothing;
	        this.minPitchSmoothing = minPitchSmoothing;
	        this.maxPitchSmoothing = maxPitchSmoothing;
	        this.random = new Random();
	        this.delta = new Vector3<>(0F, 0F, 0F);
	        this.smoothedAngle = new Angle(0F, 0F);
	    }

	    public AngleUtility(boolean aac, float smooth) {
	        this.aac = aac;
	        this.smooth = smooth;
	        random = new Random();
	    }
	    
	    public AngleUtility(float smooth) {
	        this.smooth = smooth;
	        random = new Random();
	    }
	 
	 public static float[] getAngleBlockpos(BlockPos target) {
	        double xDiff = target.getX() - Minecraft.getMinecraft().player.posX;
	        double yDiff = target.getY() - Minecraft.getMinecraft().player.posY;
	        double zDiff = target.getZ() - Minecraft.getMinecraft().player.posZ;
	        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
	        float pitch = (float) ((-Math.atan2(
	                target.getY() + (double) -1 - (Minecraft.getMinecraft().player.posY + (double) Minecraft.getMinecraft().player.getEyeHeight()),
	                Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);

	        if (yDiff > -0.2 && yDiff < 0.2) {
	            pitch = (float) ((-Math.atan2(
	                    target.getY() + (double) -1 - (Minecraft.getMinecraft().player.posY + (double) Minecraft.getMinecraft().player.getEyeHeight()),
	                    Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
	        } else if (yDiff > -0.2) {
	            pitch = (float) ((-Math.atan2(
	                    target.getY() + (double) -1 - (Minecraft.getMinecraft().player.posY + (double) Minecraft.getMinecraft().player.getEyeHeight()),
	                    Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
	        } else if (yDiff < 0.3) {
	            pitch = (float) ((-Math.atan2(
	                    target.getY() + (double) -1 - (Minecraft.getMinecraft().player.posY + (double) Minecraft.getMinecraft().player.getEyeHeight()),
	                    Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
	        }

	        return new float[]{yaw, pitch};
	    }

	    public float randomFloat(float min, float max) {
	        return min + (this.random.nextFloat() * (max - min));
	    }

	    public Angle calculateAngle(Vector3<Double> destination, Vector3<Double> source) {
	        Angle angles = new Angle(0.0F, 0.0F);
	        //Height of where you want to aim at on the entity.
	        float height = 1.2F;
	        this.delta
	        .setX(destination.getX().floatValue() - source.getX().floatValue())
	        .setY((destination.getY().floatValue() + height) - (source.getY().floatValue() + height))
	        .setZ(destination.getZ().floatValue() - source.getZ().floatValue());
	        double hypotenuse = Math.hypot(this.delta.getX().doubleValue(), this.delta.getZ().doubleValue());
	        float yawAtan = ((float) Math.atan2(this.delta.getZ().floatValue(), this.delta.getX().floatValue()));
	        float pitchAtan = ((float) Math.atan2(this.delta.getY().floatValue(), hypotenuse));
	        float deg = ((float) (180 / Math.PI));
	        float yaw = ((yawAtan * deg) - 90F);
	        float pitch = -(pitchAtan * deg);
	        return angles.setYaw(yaw).setPitch(pitch).constrantAngle();
	    }
	    
	    
	    public Angle smoothAngle(Angle destination, Angle source) {
	        return this.smoothedAngle
	                .setYaw(source.getYaw() - destination.getYaw())
	                .setPitch(source.getPitch() - destination.getPitch())
	                .constrantAngle()
	                .setYaw(source.getYaw() - this.smoothedAngle.getYaw() / 100 * randomFloat(minYawSmoothing, maxYawSmoothing))
	                .setPitch(source.getPitch() - this.smoothedAngle.getPitch() / 90 * randomFloat(minPitchSmoothing, maxPitchSmoothing))
	                .constrantAngle();
	    }

	    public Angle calculateAngle(Vector3d class1600, Vector3d class1601) {
	    	Angle class1602 = new Angle();
	            class1600.x -= class1601.x;
	            class1600.y -= class1601.y;
	            class1600.z -= class1601.z;
	            class1602.setYaw((float)(Math.atan2(class1600.z, class1600.x) * 57.29577951308232) - 90.0f);
	            class1602.setPitch(-(float)(Math.atan2(class1600.y, Math.hypot(class1600.x, class1600.z)) * 57.29577951308232));
	            return class1602.constrantAngle();
	        }
	    
	    public Angle calculateAngle(Vector3d destination, Vector3d source, EntityLivingBase target) {
	        Angle angles = new Angle();
	        destination.x += (double)(this.aac ? angu.randomFloat(-0.75f, 0.75f) : 0.0f) - source.x;
	        destination.y += (double)(this.aac ? angu.randomFloat(-0.25f, 0.5f) : 0.0f) - source.y;
	        destination.z += (double)(this.aac ? angu.randomFloat(-0.75f, 0.75f) : 0.0f) - source.z;
	        angles.setYaw((float)(Math.atan2(destination.z, destination.x) * 57.29577951308232) - 90.0f);
	        double hypotenuse = Math.hypot(destination.x, destination.z);
	        angles.setPitch(- (float)(Math.atan2(destination.y, hypotenuse) * 57.29577951308232));
	        return angles.constrantAngle();
	    }

	    public static float angleDifference(float a, float b) {
	        float c = Math.abs(a % 360.0f - b % 360.0f);
	        c = Math.min(c, 360.0f - c);
	        return c;
	    }

	    public static Angle smoothAngle(final Angle destination, final Angle source, final float i, final float j) {
	        return AngleUtility.smoothedAngle.setYaw(source.getYaw() - destination.getYaw()).setPitch(source.getPitch() - destination.getPitch()).constrantAngle().setYaw(source.getYaw() - AngleUtility.smoothedAngle.getYaw() / 100.0f * instance.randomFloat(AngleUtility.minYawSmoothing, AngleUtility.maxYawSmoothing)).setPitch(source.getPitch() - AngleUtility.smoothedAngle.getPitch() / 100.0f * instance.randomFloat(AngleUtility.minPitchSmoothing, AngleUtility.maxPitchSmoothing)).constrantAngle();
	    }
}
