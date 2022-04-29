package cn.Arctic.Module.modules.MOVE;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventMove;
import cn.Arctic.Event.events.EventPacket;
import cn.Arctic.Event.events.EventStep;
import cn.Arctic.Event.events.EventUpdate;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.GUI.NewNotification.Notification;
import cn.Arctic.GUI.NewNotification.NotificationPublisher;
import cn.Arctic.GUI.NewNotification.NotificationType;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.PLAYER.Timer;
import cn.Arctic.Util.MoveUtils;
import cn.Arctic.Util.Player.PlayerUtil;
import cn.Arctic.Util.Timer.TimeHelper;
import cn.Arctic.Util.math.MathUtil;
import cn.Arctic.Util.math.RotationUtil;
import cn.Arctic.Util.rotaions.Rotation;
import cn.Arctic.Util.rotaions.RotationUtils;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import cn.Arctic.values.Value;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class Speed extends Module {
	private double zDist;
	private double xDist;
	TimeHelper ticks = new TimeHelper();
	TimeHelper other = new TimeHelper();
	float blockPitch;
    public static transient float lastYaw;
    public static transient float lastPitch;
    public static transient float lastRandX;
    public static transient float lastRandY;
    public static transient float lastRandZ;
    public static transient BlockPos lastBlockPos;
    public static transient EnumFacing lastFacing;
	TimeHelper ticksX = new TimeHelper();
	public static Mode<Enum> mode = new Mode<Enum>("Mode",SpeedMode.values(), SpeedMode.Hypixel);
	public Numbers<Double> Base_Timer_Min = new Numbers("Speed_Base Timer Min",0.98d, 0.4d, 3.0d, 0.01d);
	public Numbers <Double> Base_Timer_Max = new Numbers("Speed_Base Timer Max",1.17d, 0.4d, 3.0d, 0.01d);
	public Numbers<Double> TimerTick = new Numbers("Speed_TimerTick",77.2d, 0.0d, 200.0d, 1.00d);
	public Value<Double> ICESPEED = new Numbers("Speed_Ice Multifier",1.07d, 1.0d, 2.0d, 0.01d);
	public Value<Double> PotionMultifier = new Numbers("Speed_PotionMultifier",0.15d, 0.0d, 0.2d, 0.01d);
	public Value<Double> AutoDisable = new Numbers("Speed_AutoDisable",477.7d, 50.0d, 750.0d, 50d);
	public Option<Boolean> AutoDisable_S = new Option<Boolean>("Speed_AutoDisable",false);
	public Option<Boolean> Ability = new Option<Boolean>("Speed_AbilitySpeed",false);
    public Numbers<Double> Timer = new Numbers<Double>("Timer",1.0,0.1,2.0,0.1);
	

	public double moveSpeed;
	public int stage;
	public boolean shouldslow = false;
	private double distance;
	public static Timer timer = new Timer();
	private TimeHelper lastCheck = new TimeHelper();
	public static double waterSpeed;

	public static double multifierX;
	public static double multifierZ;
	public static boolean isTargetStrafing;

	public int level = 1;
	double less, stair;
	public double slow;
	boolean collided = false, lessSlow;
	boolean ICE = false;
	public boolean cooldown;
	public boolean groundswitch;
	private boolean lessslow,shouldSlow;
	private boolean shouldSpoof = false;

	public S27PacketExplosion toP = null;

	public static double getRandomInRange(double min, double max) {
		Random random = new Random();
		double range = max - min;
		double scaled = random.nextDouble() * range;
		if (scaled > max) {
			scaled = max;
		}
		double shifted = scaled + min;

		if (shifted > max) {
			shifted = max;
		}
		return shifted;
	}

	public Speed() {
		super("Speed",  new String[] { "Speed" }, ModuleType.Movement);
		addValues(mode,Timer);
	}

	@EventHandler
	public void donative2(EventPreUpdate e) {
		double xDist = Minecraft.player.posX - Minecraft.player.prevPosX;
		double zDist = Minecraft.player.posZ - Minecraft.player.prevPosZ;
		this.distance = Math.sqrt(xDist * xDist + zDist * zDist);

		if (mc.player.isCollidedHorizontally && !mc.player.isSneaking()
				&& !ModuleManager.getModuleByClass(Scaffold.class).isEnabled()) {
			double pX = Minecraft.getMinecraft().player.lastReportedPosX;
			double pY = Minecraft.getMinecraft().player.lastReportedPosY
					+ (double) Minecraft.getMinecraft().player.getEyeHeight();
			double pZ = Minecraft.getMinecraft().player.lastReportedPosZ;
			double eX = Minecraft.getMinecraft().player.posX;
			double eY = Minecraft.getMinecraft().player.posY
					+ (double) Minecraft.getMinecraft().player.getEyeHeight();
			double eZ = Minecraft.getMinecraft().player.posZ;
			double dX = pX - eX;
			double dY = pY - eY;
			double dZ = pZ - eZ;
			double dH = Math.sqrt(Math.pow(dX, 2.0) + Math.pow(dZ, 2.0));
			double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
			double pitch = Math.toDegrees(Math.atan2(dH, dY));
			
			e.yaw = (float) yaw;

			mc.player.rotationYawHead = (float) yaw;

		}

	}

	@EventHandler
	public void onPacket(EventPacket e) {
		if (e.getPacket() instanceof S27PacketExplosion) {
			S27PacketExplosion pe = (S27PacketExplosion) e.packet;

			if (pe.getStrength() == 0 && pe.getAffectedBlockPositions().isEmpty()) {

			}

		}
		if (e.getPacket() instanceof S12PacketEntityVelocity) {
			if (((S12PacketEntityVelocity) e.getPacket()).entityID == Minecraft.player.getEntityId()) {

			}
		}
	}

	public Block getBlock(AxisAlignedBB bb) {
		int y = (int) bb.minY;

		for (int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; ++x) {
			for (int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; ++z) {
				Block block = Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock();
				if (block != null) {
					return block;
				}
			}
		}

		return null;
	}

	@EventHandler(4)
	public void onUpdate(EventPreUpdate e) {
		if (Jesus.isInLiquid()
				|| ModuleManager.getModuleByClass(Flight.class).isEnabled() || Minecraft.player.getAir() < 300) {
			return;
		
		}


			final List<Double> BypassOffset = Arrays.asList(0.125, 0.25, 0.375, 0.625, 0.75, 0.015625, 0.5, 0.06250,
					0.875, 0.18750);

			final double d3 = e.y % 1.0;
			BypassOffset.sort(Comparator.comparingDouble(PreY -> Math.abs(PreY - d3)));

			double acc = e.y - d3 + BypassOffset.get(0);

			if (Math.abs(BypassOffset.get(0) - d3) < 0.005) {

				e.y = acc;
			

			} else {

				final List<Double> BypassOffset2 = Arrays.asList(.715, .945, .09, .155, .14, .045, .63, .31);

				double d3_ = e.y % 1.0;
				BypassOffset2.sort(Comparator.comparingDouble(PreY -> Math.abs(PreY - d3_)));

				acc = e.y - d3_ + BypassOffset2.get(0);

				if (Math.abs(BypassOffset2.get(0) - d3_) < 0.005) {

					e.y = acc;

				}

			} 

		

	}

	@EventHandler
	public void onUpdate(EventUpdate event) {

		if (Minecraft.player == null)
			return;

		if (Jesus.isInLiquid()
				|| ModuleManager.getModuleByClass(Flight.class).isEnabled() || Minecraft.player.getAir() < 300) {
			return;
		
		}

	}

	public static boolean isOnIce() {
		final Block blockUnder = getBlockUnder();
		return blockUnder instanceof BlockIce || blockUnder instanceof BlockPackedIce;
	}

	public static Block getBlockUnder() {
		final ClientPlayerEntity player = mc.player;
		return mc.world.getBlockState(
				new BlockPos(player.posX, StrictMath.floor(player.getEntityBoundingBox().minY) - 1.0, player.posZ))
				.getBlock();
	}

	 @EventHandler
	    public void onPre(EventPreUpdate e) {
	        if (mode.getValue()==SpeedMode.Hypixel) {
	            float blockPitch = mc.player.rotationPitchHead;
	            if (PlayerUtil.isMoving()) {
	                if (MoveUtils.isOnGround(0.01) && mc.player.isCollidedVertically) {
	                    MoveUtils.setMotion(Math.max(0.2743, MoveUtils.getBaseMoveSpeed() * 0.9f));
	                    mc.player.jump();
	                } else {
	                    if (!(mc.gameSettings.keyBindAttack.isKeyDown() || mc.gameSettings.keyBindDrop.isKeyDown())) {
	                    	 MoveUtils.setMotion(MoveUtils.getSpeed() * 1.0006d);
	                    }
	                }
	            }
	            if (mc.gameSettings.keyBindAttack.isKeyDown() || mc.gameSettings.keyBindDrop.isKeyDown() ||  ModuleManager.getModuleByClass(Scaffold.class).isEnabled()) {
	                return;
	            }
	            lastYaw = getRotation(90, 120- MathUtil.getRandomInRange(-6.8f, 6.8f))[0];
	            lastPitch = getRotation(90, 120- MathUtil.getRandomInRange(-6.8f, 6.8f))[1];
	            lastYaw += (float) (Math.random() * 8 - 4);
	            lastPitch += (float) (Math.random() * 12 - 6);
	            e.setYaw(lastYaw);
	            e.setPitch(lastPitch);
	            new Rotation(lastYaw, lastPitch);
	        }
	    }

	 public float[] getRotation(float minturnspeed, float maxturnspeed) {
	        if (mc.gameSettings.keyBindForward.isKeyDown()) {
	            if (MovementInput.moveStrafe == 0.0) {
	                return RotationUtil.getRotateForScaffold(mc.player.rotationYaw - 0.0f, blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
	            } else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
	                return RotationUtil.getRotateForScaffold(mc.player.rotationYaw - 225.0f - 180, blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
	            } else if (mc.gameSettings.keyBindRight.isKeyDown()) {
	                return RotationUtil.getRotateForScaffold(mc.player.rotationYaw - 135.0f - 180, blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
	            }
	        } else if (mc.gameSettings.keyBindBack.isKeyDown()) {
	            if (MovementInput.moveStrafe == 0.0) {
	                return RotationUtil.getRotateForScaffold(mc.player.rotationYaw - 180, blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
	            } else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
	                return RotationUtil.getRotateForScaffold(mc.player.rotationYaw - 315.0f - 180, blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
	            } else if (mc.gameSettings.keyBindRight.isKeyDown()) {
	                return RotationUtil.getRotateForScaffold(mc.player.rotationYaw - 45.0f - 180, blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
	            }
	        } else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
	            return RotationUtil.getRotateForScaffold(mc.player.rotationYaw - 270.0f - 180, blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
	        } else if (mc.gameSettings.keyBindRight.isKeyDown()) {
	            return RotationUtil.getRotateForScaffold(mc.player.rotationYaw - 90.0f - 180, blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
	        }
	        return RotationUtil.getRotateForScaffold(mc.player.rotationYaw - 0.0f, blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
	    }

	private double getBaseSpeed() {
		final ClientPlayerEntity player = mc.player;
		double base = player.isSneaking() ? 0.06630000288486482
				: (mc.player.isSprinting() ? 0.2872999905467033 : 0.22100000083446503);
		final PotionEffect moveSpeed = player.getActivePotionEffect(Potion.moveSpeed);
		final PotionEffect moveSlowness = player.getActivePotionEffect(Potion.moveSlowdown);
		if (moveSpeed != null) {
			base *= 1.0 + PotionMultifier.getValue().doubleValue() * (moveSpeed.getAmplifier() + 1);
		}
		if (moveSlowness != null) {
			base *= 1.0 + PotionMultifier.getValue().doubleValue() * (moveSlowness.getAmplifier() + 1);
		}
		if (player.isInWater()) {
			base *= 0.5203619984250619;
			final int depthStriderLevel = EnchantmentHelper.getDepthStriderModifier(mc.player);
			if (depthStriderLevel > 0) {

				double[] DEPTH_STRIDER_VALUES = new double[] { 1.0, 1.4304347400741908, 1.7347825295420374,
						1.9217391028296074 };

				base *= DEPTH_STRIDER_VALUES[depthStriderLevel];
			}
		} else if (player.isInLava()) {
			base *= 0.5203619984250619;
		}
		return base;
	}

	@EventHandler
	public void onstep(EventStep event) {
		if (Jesus.isInLiquid() 
				|| ModuleManager.getModuleByClass(Flight.class).isEnabled() || Minecraft.player.getAir() < 300) {
			return;
		}

		final double Y = Minecraft.player.getEntityBoundingBox().minY - Minecraft.player.posY;
		if (Y > 0.7) {
			this.less = 0.0;
		}
		if (Y == 0.5) {
			this.stair = 0.75;
		}
	}

	@EventHandler
	public void onEvent(EventPacket ep) {
		if (Jesus.isInLiquid()
				|| ModuleManager.getModuleByClass(Flight.class).isEnabled() || Minecraft.player.getAir() < 300) {
			return;
		}

		Packet<?> p = ep.getPacket();
		if (p instanceof S08PacketPlayerPosLook) {
			S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook) ep.getPacket();
		
			NotificationPublisher.queue("Lag Check", "Speed Lag Disabled.", NotificationType.WARN, 2500);

			setEnabledonlagback(false);

			lastCheck.reset();
		}

		if (AutoDisable_S.getValue() && ticksX.delay(AutoDisable.getValue().intValue() * 15)) {
			NotificationPublisher.queue("Lag Check", "Speed Lag Disabled.", NotificationType.WARN, 2500);
			
			setEnabledonlagback(false);

			ticksX.reset();
		}
	}

	public static float getDirection() {
		Minecraft.getMinecraft();
		float yaw = Minecraft.player.rotationYawHead;
		Minecraft.getMinecraft();
		float forward = Minecraft.player.moveForward;
		Minecraft.getMinecraft();
		float strafe = Minecraft.player.moveStrafing;
		float XZ = 0;
		yaw += (forward < 0.0F ? 180 : 0);
		if (strafe < 0.0F) {
			yaw += (forward < 0.0F ? -45 : forward == 0.0F ? 90 : 45);
		}
		if (strafe > 0.0F) {
			yaw -= (forward < 0.0F ? -45 : forward == 0.0F ? 90 : 45);
		}

		if (strafe > 0.0F) {
			XZ = 0.001F;
		} else {
			XZ = 0.0F;
		}

		return yaw * 0.017453292F + XZ;
	}


	public static double roundToPlace(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public double round(double var1, int var3) {
		if (var3 < 0) {
			throw new IllegalArgumentException();
		} else {
			BigDecimal var4 = new BigDecimal(var1);
			var4 = var4.setScale(var3, RoundingMode.HALF_UP);
			return var4.doubleValue();
		}
	}


	@Override
	public void onDisable() {
		if (Minecraft.player == null || Minecraft.world == null)
			return;
		ICE = false;
		Minecraft.player.motionX *= 1.0;
		Minecraft.player.motionZ *= 1.0;
		net.minecraft.util.Timer.timerSpeed = 1.0f;

		Minecraft.player.speedInAir = 0.02f;
		super.onDisable();
	}

	public void onEnable() {
		if (Minecraft.player == null || Minecraft.world == null)
			return;
		if(mode.getValue()==SpeedMode.Hypixel) {
		this.mc.timer.timerSpeed = Timer.getValue().floatValue();
		}
		distance = 0;
		moveSpeed = MathUtil.getBaseMovementSpeed();
		this.slow = 1.0E7;
		this.less = 0.0;
		this.stage = 2;
		waterSpeed = 0.1;
		lessSlow = false;
		Minecraft.player.motionX *= 0.0D;
		Minecraft.player.motionZ *= 0.0D;

		ticksX.reset();

		cooldown = true;

		shouldSpoof = false;

		super.onEnable();
	}
	public enum SpeedMode {
		Hypixel;
	}

}
