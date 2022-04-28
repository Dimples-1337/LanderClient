package cn.Arctic.Module.modules.MOVE;


import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.Step.EventPreStep;
import cn.Arctic.Event.events.EventMove;
import cn.Arctic.Event.events.EventPacket;
import cn.Arctic.Event.events.EventPacketRecieve;
import cn.Arctic.Event.events.EventRender2D;
import cn.Arctic.Event.events.EventTick;
import cn.Arctic.Event.events.Update.EventPostUpdate;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Font.CFontRenderer;
import cn.Arctic.Font.FontLoaders;
import cn.Arctic.Font.me.superskidder.FontLoaders2;
import cn.Arctic.GUI.NewNotification.Notification;
import cn.Arctic.GUI.NewNotification.NotificationPublisher;
import cn.Arctic.GUI.NewNotification.NotificationType;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.GUI.HUD;
import cn.Arctic.Util.MSTimer;
import cn.Arctic.Util.MoveUtils;
import cn.Arctic.Util.Chat.Helper;
import cn.Arctic.Util.Inventory.InventoryUtils;
import cn.Arctic.Util.Player.PlayerUtil;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.Util.math.MathUtil;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;

public class Flight extends Module {
	private final LinkedBlockingQueue<Packet> packetList = new LinkedBlockingQueue();
	public static Mode<Enum> mode = new Mode<Enum>("Mode",FlyMode.values(), FlyMode.Hypixel);
	public static Mode<Enum> SMode = new Mode<Enum>("SetPosMode",PosMode.values(), PosMode.Pre);
	public static Mode<Enum> DMode = new Mode<Enum>("DamageMode",DamageMode.values(), DamageMode.Hypixel);
	private Numbers<Double> delay = new Numbers("FLYDelay", 3.0, 0.0, 20.0, 1.0);
	private Option <Boolean> vanillaKickBypassValue = new Option <Boolean> ("VanillaKickBypass",false);
	public Mode<Enum> JMode = new Mode<Enum>("JumpMode",JumpMode.values(), JumpMode.Normal);
	private Option<Boolean> lagcheck = new Option<Boolean>("LagCheck",true);
	private Option<Boolean> dragonvalue = new Option<Boolean>("Dragon",false);
	private Option<Boolean> motion = new Option<Boolean>("SetMotion",true);
	private Option<Boolean> pos = new Option<Boolean>("SetPos",true);
	public Option<Boolean> blink = new Option<Boolean>("Blink",false);
	private Numbers<Double> blinktimerdesu = new Numbers("BlinkDelay",1000.0, 1, 5000,1);
	private Numbers<Double> SPEED = new Numbers("Speed", 2.0, 0.25, 5, 0.25);
	private Numbers <Double> hypPearlSpeed = new Numbers("Hyp-Speed", 1, 0, 4,0);
	private Option<Boolean> bob = new Option<Boolean>("Bobbing", false);
	public Numbers<Double> boost = new Numbers("Boost", 2.5, 0, 5, 0.1);
	private Numbers<Double> motionY = new Numbers("MotionY", 0.42F, 0.42F, 0.42F, 0.1);
	private Numbers<Double> timer = new Numbers("Timer", 1.0, 0.1, 3, 0.1);
	private Numbers<Double> wattingtimer = new Numbers( "WattingTimer",240.0, 0, 1000, 10);
	private Numbers<Double> aacspeed = new Numbers("AACSpeed", 0.3, 0, 1, 0.01);
	private Option<Boolean> par = new Option<Boolean>("Particle", false);
	private Option<Boolean> watting = new Option<Boolean>("Watting", false);
	public Option<Boolean> swing = new Option<Boolean>("Swing",false);
	public static Option<Boolean> uhc = new Option<Boolean>("UHC",false);
	public static Option<Boolean> c06 = new Option<Boolean>("RotationPacket",false);
	public Option<Boolean> ground = new Option<Boolean>("GroundCheck",false);
	public Option<Boolean> auracheck = new Option<Boolean>("AuraCheck",false);
	public Option<Boolean> mark = new Option<Boolean>("Mark",false);
	 private Option<Boolean> DYNAMIC = new Option<Boolean>("DYNAMIC", true);
	
	private EntityDragon dragon;
	private boolean canPearFly = false;
    private boolean packetPear = false;
    private final MSTimer hypTimer = new MSTimer();
    private final MSTimer blinkTimer = new MSTimer();
	public static double oldY;
	int counter, level;
	private double flyHeight;
	private boolean dragoncrea = false;
	private int zoom;
	private TimerUtil deactivationDelay = new TimerUtil();
	 private final MSTimer hypWaitTimer = new MSTimer();
	double moveSpeed, lastDist;
	boolean b2;
	boolean fly;
	private double startY;
	private double aacJump;
	private boolean noground;
	TimerUtil C13timer = new TimerUtil();
	private final MSTimer groundTimer = new MSTimer();
	double y = 0.0;
	double x = 0.0;
	double z = 0.0;
	 public static boolean useFont;

	//Blink
	private TimerUtil blinktimer = new TimerUtil();
	private final LinkedList<double[]> positions = new LinkedList<double[]>();
	private boolean disableLogger;
	private int a;
	private int width;
    MSTimer flytimer = new MSTimer();
	public Flight() {
		super("Fly", new String[] { "flight","fly","airwalk" }, ModuleType.Movement);
		this.addValues(mode,SMode,DMode,JMode,lagcheck,delay,auracheck,blink,blinktimerdesu,SPEED,motion,pos,bob,swing,boost,aacspeed,par,dragonvalue,timer,motionY,watting,wattingtimer,uhc,ground,c06,mark);
	}

	public void MineplexDamage() {
		for(int i = 0; i < 20; ++i) {
			for(int j = 0; (double)j < (double) PlayerUtil.getMaxFallDist() / 0.060100000351667404D + 1.0D; ++j) {
				mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX, mc.player.posY + 0.060100000351667404D, mc.player.posZ, false));
				mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX, mc.player.posY + 5.000000237487257E-4D, mc.player.posZ, false));
			}
		}
		mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer(true));
	}
	public void FloatDamage() {
		for (int i = 0;i < 6;i++) {
            for (double y : new double[] {0.04,
                    0.00079999923706,
                    0.04079999923707,
                    0.00159999847412,
                    0.04159999847413,
                    0.00239999771118,
                    0.04239999771119,
                    0.00319999694824,
                    0.04319999694825,
                    0.0039999961853,
                    0.0439999961853,
                    0.00479999542236,
                    0.04479999542237,
                    0.00559999465942,
                    0.04559999465943,
                    0.00639999389648,
                    0.04639999389649,
                    0.00719999313354,
                    0.04719999313355,
                    0.0079999923706,
                    0.0479999923706,
                    0.00879999160767,
                    0.04879999160766,
                    0.00959999084472,
                    0.04959999084473,
                    0.01039999008178//, 0.0
                }) {
                mc.player.sendQueue.sendQueueWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX, mc.player.posY + y, mc.player.posZ, false));
            }
        }
        mc.player.sendQueue.sendQueueWithoutEvent(new C03PacketPlayer(true));

	}

	public void UHCdamage() {
		NetHandlerPlayClient netHandler = mc.getNetHandler();
		ClientPlayerEntity player = mc.player;
		double x = player.posX;
		double y = player.posY;
		double z = player.posZ;
		int i = 0;
		while ((double) i < ((double) PlayerUtil.getMaxFallDist() + 1.0) / 0.056) {
			if(c06.getValue()) {
				netHandler.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(x, y + (double) 0.0601f, z, mc.player.rotationYaw, mc.player.rotationPitch,false));
				netHandler.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(x, y + (double) 5.0E-4f, z, mc.player.rotationYaw, mc.player.rotationPitch,false));
				netHandler.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(x,
						y + (double) 0.005f + 6.01000003516674E-8, z, mc.player.rotationYaw, mc.player.rotationPitch, false));
			}else{
				netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + (double) 0.0601f, z, false));
				netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + (double) 5.0E-4f, z, false));
				netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x,
						y + (double) 0.005f + 6.01000003516674E-8, z, false));
			}
			++i;
		}
		if(c06.getValue()){
			netHandler.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
		}else {
			netHandler.addToSendQueue(new C03PacketPlayer(true));
		}
	}

	public void goToGround() {
		if (this.flyHeight > 300.0D) {
			return;
		}
		double minY = mc.player.posY - this.flyHeight;
		if (minY <= 0.0D) {
			return;
		}
		for (double y = mc.player.posY; y > minY;) {
			y -= 8.0D;
			if (y < minY) {
				y = minY;
			}
			C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(
					mc.player.posX, y, mc.player.posZ, true);
			mc.player.sendQueue.addToSendQueue(packet);
		}
		for (double y = minY; y < mc.player.posY;) {
			y += 8.0D;
			if (y > mc.player.posY) {
				y = mc.player.posY;
			}
			C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(
					mc.player.posX, y, mc.player.posZ, true);
			mc.player.sendQueue.addToSendQueue(packet);
		}
	}

	@EventHandler
	public void onPacket(EventPacketRecieve e) {
		if(!isEnabled())
			return;
		if(lagcheck.getValue()) {
			if (e.getPacket() instanceof S08PacketPlayerPosLook && this.deactivationDelay.delay(2000)) {
				S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook) e.getPacket();
				pac.yaw = mc.player.rotationYaw;
				pac.pitch = mc.player.rotationPitch;
				Helper.sendMessage("LagBack!");
				NotificationPublisher.queue("Lag Check", "Flight Lag Disabled.", NotificationType.WARN, 2500);
				this.setEnabled(false);
			}
		}
	}
	


	@EventHandler
	public void onPacket(EventPacket event) {
		if(!isEnabled() || !blink.getValue())
			return;

		final Packet<?> packet = event.getPacket();
		if(this.mode.getValue() == FlyMode.Hypixel) {
        final C03PacketPlayer playerPacket = (C03PacketPlayer) packet;
        playerPacket.onGround = true;
        
		}

		if (mc.player == null || disableLogger)
			return;

		if (packet instanceof C03PacketPlayer) // Cancel all movement stuff
			event.setCancelled(true);

			if (packet instanceof C03PacketPlayer ||
					packet instanceof C08PacketPlayerBlockPlacement ||
					packet instanceof C0APacketAnimation ||
					packet instanceof C0BPacketEntityAction || packet instanceof C02PacketUseEntity) {
				event.setCancelled(true);

				packetList.add(packet);
			}
	}

	@EventHandler
	public void onBlink(EventPreUpdate e){
		if(!isEnabled() || !blink.getValue())
			return;

		synchronized (positions) {
			positions.add(new double[]{mc.player.posX, mc.player.getEntityBoundingBox().minY, mc.player.posZ});
		}

		if (blinktimer.delay(blinktimerdesu.getValue().floatValue()) || blinktimerdesu.getValue() <= 1.0) {
			blink();
			blinktimer.reset();
		}
	}

	private void blink() {
		if(!blink.getValue())
			return;

		try {
			disableLogger = true;

			final Iterator<Packet> packetIterator = packetList.iterator();
			for(; packetIterator.hasNext(); ) {
				mc.getNetHandler().addToSendQueue(packetIterator.next());
				packetIterator.remove();
			}

			disableLogger = false;
		}catch(final Exception e) {
			e.printStackTrace();
			disableLogger = false;
		}

		synchronized(positions) {
			positions.clear();
		}
	}

	@Override
	public void onEnable() {
		  mc.player.addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(), 10000, 1));
		if (this.mc.player == null) {
			mc.player.motionY += motionY.getValue().floatValue();
			return;
		}

		y = 0.0;
		oldY = 0;

		if(!mc.player.onGround && ground.getValue()) {
			noground = true;
			this.setEnabled(false);
			return;
		}

		if(swing.getValue()){
			mc.player.swingItem();
		}

		this.startY = Minecraft.player.posY;
		this.aacJump = -3.8D;
		this.zoom = 50;

		if(blink.getValue()) {
			synchronized (positions) {
				positions.add(new double[]{mc.player.posX, mc.player.getEntityBoundingBox().minY + (mc.player.getEyeHeight() / 2), mc.player.posZ});
				positions.add(new double[]{mc.player.posX, mc.player.getEntityBoundingBox().minY, mc.player.posZ});
			}

			blinktimer.reset();
		}

		if(mc.player.onGround) {
			if (DMode.getValue() == DamageMode.Hypixel2) {
				if (uhc.getValue()) {
					UHCdamage();
				} else {
					for (int i = 0; i < 20; ++i) {
						mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.player.posX, Minecraft.player.posY, Minecraft.player.posZ, true));
					}
					for (double fallDistance = 3.0125; fallDistance > 0.0; fallDistance -= 0.0624986421) {
						mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.player.posX, Minecraft.player.posY + 0.0, Minecraft.player.posZ, false));
						mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.player.posX, Minecraft.player.posY + 0.04159999847413, Minecraft.player.posZ, false));
						mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.player.posX, Minecraft.player.posY + 0.00239999771118, Minecraft.player.posZ, false));
						mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.player.posX, Minecraft.player.posY + 0.04239999771119, Minecraft.player.posZ, false));
					}
					mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.player.posX, Minecraft.player.posY, Minecraft.player.posZ, true));
				}
			} else if (DMode.getValue() == DamageMode.Hypixel) {
				if (uhc.getValue()) {
					UHCdamage();
				} else {
					damage();
				}
			} else if (DMode.getValue() == DamageMode.MinePlex) {
				MineplexDamage();
			}
			else if (DMode.getValue() == DamageMode.FloatHypixel) {
				FloatDamage();
			}
			
		}

		if (this.mode.getValue() == FlyMode.Hypixel) {

			PlayerCapabilities playerAbilities = new PlayerCapabilities();
			playerAbilities.allowFlying = false;
			playerAbilities.isFlying = true;
			playerAbilities.isCreativeMode = mc.player.capabilities.isCreativeMode;
			playerAbilities.disableDamage = mc.player.capabilities.disableDamage;
			playerAbilities.setFlySpeed(mc.player.capabilities.getFlySpeed());
			playerAbilities.setPlayerWalkSpeed(mc.player.capabilities.getWalkSpeed());
			mc.player.sendQueue.addToSendQueueSilent(new C0FPacketConfirmTransaction(0, (short) (-1), false));
			mc.player.sendQueue.getNetworkManager().sendPacketNoEvent(new C13PacketPlayerAbilities(playerAbilities));
			fly = true;
			if(!watting.getValue()){
				fly = false;
				if(motion.getValue()){
					MoveUtils.setMotion(0.42F + MoveUtils.getSpeedEffect() * 0.05);
				}
				if(JMode.getValue() == JumpMode.Custom){
					mc.player.motionY = motionY.getValue() * 0.01;
					startY = startY + motionY.getValue() * 0.01;
					oldY = (mc.player.posY + motionY.getValue() * 0.01);
					if(pos.getValue()){
						mc.player.posY += motionY.getValue() * 0.01;
					}
					
				}else if(JMode.getValue() == JumpMode.JumpBoost){
					mc.player.motionY = MoveUtils.getJumpBoostModifier(0.04879999160766);
					startY = startY + MoveUtils.getJumpBoostModifier(0.00879999160767);
					oldY = (mc.player.posY + MoveUtils.getJumpBoostModifier(0.0479999923706));
					if(pos.getValue()) {
						mc.player.posY += MoveUtils.getJumpBoostModifier(0.04719999313355);
					}
				}else if(JMode.getValue() == JumpMode.Normal){
					mc.player.motionY = 0.00319999694824;
					startY = startY + 0.04319999694825;
					oldY = (mc.player.posY + 0.0039999961853);
					if(pos.getValue()) {
						mc.player.posY += 0.0439999961853;
					}
				}
			}else{
				new java.util.Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						fly = false;
						if(motion.getValue()){
							MoveUtils.setMotion(0.42F + MoveUtils.getSpeedEffect() * 0.05);
						}
						if(JMode.getValue() == JumpMode.Custom){
							mc.player.motionY = motionY.getValue().floatValue() * 0.01;
							startY = startY + motionY.getValue().floatValue() * 0.01;
							oldY = (mc.player.posY + motionY.getValue().floatValue() * 0.01);
							if(pos.getValue()){
								mc.player.posY += motionY.getValue().floatValue() * 0.01;
							}
						}else if(JMode.getValue() == JumpMode.JumpBoost){
							mc.player.motionY = MoveUtils.getJumpBoostModifier(0.39999994);
							startY = startY + MoveUtils.getJumpBoostModifier(0.39999994);
							oldY = (mc.player.posY + MoveUtils.getJumpBoostModifier(0.39999994));
							if(pos.getValue()) {
								mc.player.posY += MoveUtils.getJumpBoostModifier(0.39999994);
							}
						}else if(JMode.getValue() == JumpMode.Normal){
							mc.player.motionY = 0.00319999694824;
							startY = startY + 0.04319999694825;
							oldY = (mc.player.posY + 0.0039999961853);
							if(pos.getValue()) {
								mc.player.posY += 0.0439999961853;
							}
						}
						this.cancel();
					}
				}, wattingtimer.getValue().longValue());
			}
		}
		if(mode.getValue() == FlyMode.Pearlfly){    //Take a Pearl
			canPearFly = false;
	        if(InventoryUtils.findPearl() == -1){   //Check having Pearl
	        	NotificationPublisher.queue("Lag Check", "You not have Pearl", NotificationType.WARN, 2500);
	            canPearFly = false;
	        }else {
	            //Use a Ender Pearl
	            int blockSlot = InventoryUtils.findPearl();

	            int oldSlot = mc.player.inventory.currentItem;

	            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(blockSlot - 36));    //鍒囨崲鐗╁搧鍖�

	            //Use a Ender Pearl
	            mc.player.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(mc.player.rotationYaw,90, mc.player.onGround));
	            mc.player.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.player.inventory.getCurrentItem()));
	            mc.player.sendQueue.addToSendQueue(new C09PacketHeldItemChange(oldSlot));
	            canPearFly = true;
	            hypTimer.reset();
	        }
		}

		level = 1;
		dragoncrea = false;
		moveSpeed = 0;
		b2 = true;
		lastDist = 0;
	}
	

	public static void damage() {
		NetHandlerPlayClient netHandler = mc.getNetHandler();
		ClientPlayerEntity player = mc.player;
		double x = player.posX;
		double y = player.posY;
		double z = player.posZ;
		int i = 0;
		while ((double) i < (double) PlayerUtil.getMaxFallDist() / 0.0555) {
			netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0601, z, false));
			netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.003, z, false));
			++i;
		}
		netHandler.addToSendQueue(new C03PacketPlayer(true));
	}

	@Override
	public void onDisable() {
		 mc.player.removePotionEffect(Potion.moveSpeed.getId());
		mc.player.jumpMovementFactor = 0.0f;

		if(noground) {
			noground = false;
			return;
		}

		counter = 0;
		C13timer.reset();

		if(blink.getValue()) {
			blink();
		}

		if (this.dragonvalue.getValue()) {
			mc.world.removeEntity(dragon);
		}

		if(motion.getValue()){
			MoveUtils.setMotion(0D);
		}else{
			mc.player.motionX = 0D;
			mc.player.motionZ = 0D;
		}

		mc.timer.timerSpeed = 1;
		level = 1;
		moveSpeed = 0.1;
		b2 = false;
		lastDist = 0;
	}


	public void updateFlyHeight() {
		double h = 1.0D;
		AxisAlignedBB box = mc.player.getEntityBoundingBox().expand(0.0625D, 0.0625D, 0.0625D);
		for (this.flyHeight = 0.0D; this.flyHeight < mc.player.posY; this.flyHeight += h) {
			AxisAlignedBB nextBox = box.offset(0.0D, -this.flyHeight, 0.0D);
			if (mc.world.checkBlockCollision(nextBox)) {
				if (h < 0.0625D) {
					break;
				}
				this.flyHeight -= h;
				h /= 2.0D;
			}
		}
	}

	public static boolean isOnGround(double height) {
		if (!Minecraft.world.getCollidingBoundingBoxes(Minecraft.player,
				Minecraft.player.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	@EventHandler
	public void onStep(EventPreStep e){
		if(!isEnabled())
			return;
		if(mode.getValue() == FlyMode.Hypixel) {
			e.setHeight(0F);
		}
	}

	@EventHandler
	private void onUpdate(final EventPreUpdate e) {
		if(!isEnabled())
			return;
		this.setSuffix(this.mode.getValue() + (mode.getValue() == FlyMode.Hypixel ? " - " + this.SMode.getValue() : ""));
		if (this.bob.getValue()) {
			Minecraft.player.cameraYaw = 0.090909086f;
		}

		double speed = Math.max(SPEED.getValue(), getBaseMoveSpeed());
		
		if(mode.getValue() == FlyMode.Pearlfly){
			if(!canPearFly){
                setEnabled(false);
                return;
            }

            if(!hypTimer.hasTimePassed((delay.getValue().longValue() * 35) * 100)) {
            	MoveUtils.strafe(0.0D);
            }

			
			this.mc.player.motionY = this.mc.player.movementInput.jump ? 1.0 : (this.mc.player.movementInput.sneak ? -1.0 : 0.0);
			if (PlayerUtil.isMoving()) {
				this.mc.player.setSpeed(3.0);
			} else {
				this.mc.player.setSpeed(0.0);
			}
			mc.player.capabilities.allowFlying = false;
		}
		

  if (this.mode.getValue() == FlyMode.Vanilla) {
			this.mc.player.motionY = this.mc.player.movementInput.jump ? 1.0 : (this.mc.player.movementInput.sneak ? -1.0 : 0.0);
			if (PlayerUtil.isMoving()) {
				this.mc.player.setSpeed(3.0);
			} else {
				this.mc.player.setSpeed(0.0);
			}
			mc.player.capabilities.allowFlying = false;
		}else if(mode.getValue() == FlyMode.Hypixel){
			if(SMode.getValue() != PosMode.Pre)
				return;

			this.setPositions();
		}
	}

	@EventHandler
	public void onUpdate(EventPostUpdate e){
		if(SMode.getValue() != PosMode.Post || this.mode.getValue() != FlyMode.Hypixel)
			return;

		this.setPositions();
	}

	@EventHandler
	public void onTick(EventTick e){
		if(SMode.getValue() != PosMode.Tick || this.mode.getValue() != FlyMode.Hypixel)
			return;

		this.setPositions();
	}

	public void setPositions(){
		counter++;
		switch (counter) {
			case 0: {
				mc.player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
				break;
			}
			case 1: {
				mc.player.setPosition(mc.player.posX, mc.player.posY + 0.0001, mc.player.posZ);
				break;
			}
			case 2: {
				mc.player.setPosition(mc.player.posX, mc.player.posY + 0.0001, mc.player.posZ);
				break;
			}
			case 3: {
				mc.player.setPosition(mc.player.posX, mc.player.posY - 0.0002, mc.player.posZ);
				break;
			}
			case 4: {
				mc.player.setPosition(mc.player.posX, mc.player.posY + 0.0001, mc.player.posZ);
				break;
			}
			case 5: {
				mc.player.setPosition(mc.player.posX, mc.player.posY + 0.0001, mc.player.posZ);
				break;
			}
			case 6: {
				mc.player.setPosition(mc.player.posX, mc.player.posY + 0.0001, mc.player.posZ);
				break;
			}
			case 7: {
				mc.player.setPosition(mc.player.posX, mc.player.posY - 0.0003, mc.player.posZ);
				break;
			}
			case 8: {
				mc.player.setPosition(mc.player.posX, mc.player.posY + 0.0001, mc.player.posZ);
				break;
			}
			case 9: {
				mc.player.setPosition(mc.player.posX, mc.player.posY + 0.0001, mc.player.posZ);
				break;
			}
			case 10: {
				mc.player.setPosition(mc.player.posX, mc.player.posY + 0.0001, mc.player.posZ);
				break;
			}
			case 11: {
				mc.player.setPosition(mc.player.posX, mc.player.posY + 0.0001, mc.player.posZ);
				break;
			}
			case 12: {
				mc.player.setPosition(mc.player.posX, mc.player.posY - 0.0004, mc.player.posZ);
				counter = 0;
				break;
			}
		}
		mc.player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
		mc.player.motionY = 0;
	}

	@EventHandler
	public void onDist(EventPostUpdate event){
		if (this.mode.getValue() == FlyMode.Hypixel) {
			double xDist = Minecraft.getMinecraft().player.posX - Minecraft.getMinecraft().player.prevPosX;
			double zDist = Minecraft.getMinecraft().player.posZ - Minecraft.getMinecraft().player.prevPosZ;
			lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
		}
	}

	@EventHandler
	public void onC13(EventTick e){
		if(C13timer.delay(3000)){
			PlayerCapabilities playerAbilities = new PlayerCapabilities();
			playerAbilities.allowFlying = false;
			playerAbilities.isFlying = true;
			playerAbilities.isCreativeMode = mc.player.capabilities.isCreativeMode;
			playerAbilities.disableDamage = mc.player.capabilities.disableDamage;
			playerAbilities.setFlySpeed(mc.player.capabilities.getFlySpeed());
			playerAbilities.setPlayerWalkSpeed(mc.player.capabilities.getWalkSpeed());
			mc.player.sendQueue.addToSendQueueSilent(new C0FPacketConfirmTransaction(0, (short) (-1), false));
			mc.player.sendQueue.getNetworkManager().sendPacketNoEvent(new C13PacketPlayerAbilities(playerAbilities));
			C13timer.reset();
		}
	}

	@EventHandler
	public void onEvent(EventPostUpdate e) {
		if(!isEnabled())
			return;
		if (this.mode.getValue() == FlyMode.Hypixel) {
			if (fly) {
				mc.player.motionX = 0;
				mc.player.motionZ = 0;
				mc.player.jumpMovementFactor = 0;
				mc.player.onGround = false;
			}
		}
		if (this.dragonvalue.getValue() && !this.dragoncrea) {
			this.dragoncrea = true;
			dragon = new EntityDragon(mc.world);
			dragon.setCustomNameTag("Aslier");
			mc.world.addEntityToWorld(666, dragon);
			mc.player.ridingEntity = dragon;
		} else if (this.dragonvalue.getValue()) {
			final double posX4 = mc.player.posX;
			final double posX = posX4 - MathHelper.cos(mc.player.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
			final double posZ2 = mc.player.posZ;
			final double posZ = posZ2 - MathHelper.sin(mc.player.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
			final float f = 0.4f;
			final float n2 = -MathHelper.sin(mc.player.rotationYaw / 180.0f * 3.1415927f);
			final double motionX = n2 * MathHelper.cos(mc.player.rotationPitch / 180.0f * 3.1415927f) * f;
			final float cos = MathHelper.cos(mc.player.rotationYaw / 180.0f * 3.1415927f);
			final double motionZ = cos * MathHelper.cos(mc.player.rotationPitch / 180.0f * 3.1415927f) * f;
			final double xCoord = posX + motionX;
			final double zCoord = posZ + motionZ;
			dragon.rotationPitch = mc.player.rotationPitch;
			dragon.rotationYaw = mc.player.rotationYawHead - 180;
			dragon.setRotationYawHead(mc.player.rotationYawHead);
			dragon.setPosition(xCoord, mc.player.posY - 2, zCoord);
		}
		if (this.par.getValue()) {
			final double posX4 = mc.player.posX;
			final double posX = posX4 - MathHelper.cos(mc.player.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
			final double posY = mc.player.posY;
			final double posZ2 = mc.player.posZ;
			final double posZ = posZ2 - MathHelper.sin(mc.player.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
			final float f = 0.4f;
			final float n2 = -MathHelper.sin(mc.player.rotationYaw / 180.0f * 3.1415927f);
			final double motionX = n2 * MathHelper.cos(mc.player.rotationPitch / 180.0f * 3.1415927f) * f;
			final float cos = MathHelper.cos(mc.player.rotationYaw / 180.0f * 3.1415927f);
			final double motionZ = cos * MathHelper.cos(mc.player.rotationPitch / 180.0f * 3.1415927f) * f;
			final double motionY = -MathHelper.sin((mc.player.rotationPitch + 2.0f) / 180.0f * 3.1415927f) * f;
			for (int i = 0; i < 90; ++i) {
				final WorldClient world = this.mc.world;
				final EnumParticleTypes particleType = (i % 4 == 0) ? (EnumParticleTypes.CRIT_MAGIC)
						: (new Random().nextBoolean() ? EnumParticleTypes.HEART : EnumParticleTypes.ENCHANTMENT_TABLE);
				final double xCoord = posX + motionX;
				final double yCoord = posY + motionY;
				final double zCoord = posZ + motionZ;
				final double motionX2 = mc.player.motionX;
				final double motionY2 = mc.player.motionY;
				world.spawnParticle(particleType, xCoord, yCoord, zCoord, motionX2, motionY2, mc.player.motionZ, 0);
			}
		}

      
	}

	 private void handleVanillaKickBypass() {
	        if(!vanillaKickBypassValue.get() || !groundTimer.hasTimePassed(1000)) return;

	        final double ground = calculateGround();

	        for(double posY = mc.player.posY; posY > ground; posY -= 8D) {
	            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX, posY, mc.player.posZ, true));

	            if(posY - 8D < ground) break; // Prevent next step
	        }

	        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX, ground, mc.player.posZ, true));


	        for(double posY = ground; posY < mc.player.posY; posY += 8D) {
	            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX, posY, mc.player.posZ, true));

	            if(posY + 8D > mc.player.posY) break; // Prevent next step
	        }

	        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX, mc.player.posY, mc.player.posZ, true));

	        groundTimer.reset();
	    }
	@EventHandler
	public void onMove(EventMove e) {
		if(!isEnabled())
			return;
		if (this.mode.getValue() == FlyMode.Hypixel) {
            if (mc.player.onGround) {
                FloatDamage();
                mc.player.motionY = 0.30444444f;
            } else {
                mc.player.motionY = 0;
            }
            if (MoveUtils.isMoving()) {
                MoveUtils.setSpeed(e,0.4d);
            }
            mc.player.setPosition(x , y + 0.00042321f, z);
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.00042321f, z, false));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
			if (!fly) {
				if (this.zoom > 0 && boost.getValue().floatValue() > 0.0F) {
					if(timer.getValue() != 1){
						mc.timer.timerSpeed = timer.getValue().floatValue();
					}
					if (this.zoom < 5) {
						float percent = (float) (this.zoom / 10);
						if ((double) percent > 1) {
							percent = 1.0F;
						}
						if(timer.getValue() != 1){
							mc.timer.timerSpeed = timer.getValue().floatValue();
						}
					}
				} else {
					mc.timer.timerSpeed = 1.0F;
				}
				--this.zoom;
				float forward = Minecraft.player.movementInput.moveForward;
				float strafe = Minecraft.player.movementInput.moveStrafe;
				float yaw = mc.player.rotationYaw;
				double mx = Math.cos(Math.toRadians((double) (yaw + 90.0F)));
				double mz = Math.sin(Math.toRadians((double) (yaw + 90.0F)));
				if (!PlayerUtil.isMoving()) {
					MoveUtils.setSpeed(e, 0.0);
					return;
				}

				if (forward == 0.0F && strafe == 0.0F) {
					e.x = 0.0D;
					e.z = 0.0D;
				} else if (forward != 0.0F) {
					if (strafe >= 1.0F) {
						yaw += (float) (forward > 0.0F ? -45 : 45);
						strafe = 0.0F;
					} else if (strafe <= -1.0F) {
						yaw += (float) (forward > 0.0F ? 45 : -45);
						strafe = 0.0F;
					}

					if (forward > 0.0F) {
						forward = 1.0F;
					} else if (forward < 0.0F) {
						forward = -1.0F;
					}
				}
				if (b2) {
					if (level != 1 || Minecraft.getMinecraft().player.moveForward == 0.0F
							&& Minecraft.getMinecraft().player.moveStrafing == 0.0F) {
						if (level == 2) {
							level = 3;
							moveSpeed *=  boost.getValue().floatValue();
						} else if (level == 3) {
							level = 4;
							double difference = (mc.player.ticksExisted % 2 == 0 ? 0.0103D : 0.0123D)
									* (lastDist - MathUtil.getBaseMovementSpeed());
							moveSpeed = lastDist - difference;
						} else {
							if (Minecraft.getMinecraft().world
									.getCollidingBoundingBoxes(Minecraft.getMinecraft().player,
											Minecraft.getMinecraft().player.boundingBox.offset(0.0D,
													Minecraft.getMinecraft().player.motionY, 0.0D))
									.size() > 0 || Minecraft.getMinecraft().player.isCollidedVertically) {
								level = 1;
							}
							moveSpeed = lastDist - lastDist / 159.0D;
						}
					} else {
						level = 2;
						int amplifier = Minecraft.getMinecraft().player.isPotionActive(Potion.moveSpeed)
								? Minecraft.getMinecraft().player.getActivePotionEffect(Potion.moveSpeed)
								.getAmplifier() + 1
								: 0;
						double boost = Minecraft.getMinecraft().player.isPotionActive(Potion.moveSpeed) ? 1.56
								: 2.034;
						moveSpeed = boost * MathUtil.getBaseMovementSpeed();
					}
					moveSpeed = Math.max(moveSpeed, MathUtil.getBaseMovementSpeed());

					e.x = (double) forward * moveSpeed * Math.cos(Math.toRadians(yaw + 90))
							+ (double) strafe * moveSpeed * Math.sin(Math.toRadians(yaw + 90));
					e.z = (double) forward * moveSpeed * Math.sin(Math.toRadians(yaw + 90))
							- (double) strafe * moveSpeed * Math.cos(Math.toRadians(yaw + 90));
					if (forward == 0.0F && strafe == 0.0F) {
						e.x = 0.0D;
						e.z = 0.0D;
					}
				}
				/**
				 if (!mc.Player.moving()) {
				 MovementUtils.setSpeed(e, 0.0);
				 return;
				 }

				 if (!mc.Player.moving()) {
				 e.x = 0;
				 e.z = 0;
				 return;
				 }
				 if (this.zoom > 0 && boost.getValue().floatValue() > 0.0F) {
				 if(timer.getValue() != 1){
				 mc.timer.timerSpeed = timer.getValue().floatValue();
				 }
				 if (this.zoom < 5) {
				 float percent = (float) (this.zoom / 10);
				 if ((double) percent > 1) {
				 percent = 1.0F;
				 }
				 if(timer.getValue() != 1){
				 mc.timer.timerSpeed = timer.getValue().floatValue();
				 }
				 }
				 } else {
				 mc.timer.timerSpeed = 1.0F;
				 }
				 --this.zoom;
				 final double amplifier = 1.0 + (mc.Player.isPotionActive(Potion.moveSpeed) ? (0.2 * (mc.Player.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1)) : 0.0);
				 final double baseSpeed = 0.291 * amplifier;
				 switch (this.boostHypixelState) {
				 case 1: {
				 this.moveSpeed = (mc.Player.isPotionActive(Potion.moveSpeed) ? 1.56 : 2.04) * baseSpeed;
				 this.boostHypixelState = 2;
				 break;
				 }
				 case 2: {
				 this.moveSpeed *= boost.getValue();
				 this.boostHypixelState = 3;
				 break;
				 }
				 case 3: {
				 this.moveSpeed = this.lastDist - ((mc.Player.ticksExisted % 2 == 0) ? 0.0103 : 0.0123) * (this.lastDist - baseSpeed);
				 this.boostHypixelState = 4;
				 break;
				 }
				 default: {
				 this.moveSpeed = this.lastDist - this.lastDist / 159.8;
				 break;
				 }
				 }

				 moveSpeed = Math.max(moveSpeed, 0.3D);
				 MovementUtils.setSpeed(e, this.moveSpeed);
				 */
			}
		} 
		
		else if (this.mode.getValue() == Flight.FlyMode.HytFly) {
			yClip(mc.player.posY + 0.3);//0.5
			hClip(3.71); //3 4.71
			hClip2(2);
			vClip2(10.84);//10 10.84
			setSpeed(0.5);
			mc.timer.timerSpeed = 0.5f;//0.3f
			mc.player.motionY = 0;
        }
		
	}
	 private double calculateGround() {
	        final AxisAlignedBB playerBoundingBox = mc.player.getEntityBoundingBox();
	        double blockHeight = 1D;

	        for(double ground = mc.player.posY; ground > 0D; ground -= blockHeight) {
	            final AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.maxX, ground + blockHeight, playerBoundingBox.maxZ, playerBoundingBox.minX, ground, playerBoundingBox.minZ);

	            if(mc.world.checkBlockCollision(customBox)) {
	                if(blockHeight <= 0.05D)
	                    return ground + blockHeight;

	                ground += blockHeight;
	                blockHeight = 0.05D;
	            }
	        }

	        return 0F;
	    }
	public void  vClip2(double d) {
   		mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX, mc.player.posY+d, mc.player.posZ, false));
   	}
   	public void hClip(double d) {
   		double playerYaw = MoveUtils.getDirection();
   		mc.player.setPosition(mc.player.posX + d * -Math.sin(playerYaw), mc.player.posY, mc.player.posZ + d * Math.cos(playerYaw));
   	}
   	public void  hClip2(double d) {
   		double playerYaw = MoveUtils.getDirection();;
   		mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX + d * -Math.sin(playerYaw), mc.player.posY, mc.player.posZ + d * Math.cos(playerYaw), false));
   	}
   	public void  yClip(double d) {
   		mc.player.setPosition(mc.player.posX, d, mc.player.posZ);
   	}

	
	public void  setSpeed(double _speed) {
		double playerYaw = MoveUtils.getDirection();;
		mc.player.motionX = _speed * -Math.sin(playerYaw);
		mc.player.motionZ = _speed * Math.cos(playerYaw);
	} 
	double getBaseMoveSpeed() {
		double baseSpeed = 0.275;
		if (this.mc.player.isPotionActive(Potion.moveSpeed)) {
			int amplifier = this.mc.player.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (double) (amplifier + 1);
		}
		return baseSpeed;
	}
	public enum FlyMode {
		Vanilla, Hypixel,HytFly,Pearlfly;
	}

	enum DamageMode {
		Hypixel, Hypixel2, MinePlex,FloatHypixel, None
	}

	enum PosMode {
		Pre, Post, Tick
	}

	enum JumpMode {
		Custom, Normal, JumpBoost
	}
}