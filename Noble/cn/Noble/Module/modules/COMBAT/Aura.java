package cn.Noble.Module.modules.COMBAT;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.print.attribute.standard.MediaSize.Other;
import javax.vecmath.Vector2f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import HanabiClassSub.Class246;
import HanabiClassSub.LiquidRender;
import cn.Noble.Client;
import cn.Noble.Event.Angle;
import cn.Noble.Event.AngleUtility;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.Vector.Vector3;
import cn.Noble.Event.events.EventPacketRecieve;
import cn.Noble.Event.events.EventPacketSend;
import cn.Noble.Event.events.EventRender2D;
import cn.Noble.Event.events.EventRender3D;
import cn.Noble.Event.events.EventStrafe;
import cn.Noble.Event.events.EventTick;
import cn.Noble.Event.events.Update.EventPostUpdate;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Font.CFontRenderer;
import cn.Noble.Font.FontLoaders;
import cn.Noble.GUI.NewNotification.NotificationPublisher;
import cn.Noble.GUI.NewNotification.NotificationType;
import cn.Noble.Manager.FriendManager;
import cn.Noble.Manager.ModuleManager;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Module.modules.MOVE.Scaffold;
import cn.Noble.Module.modules.PLAYER.Teams;
import cn.Noble.Util.Colors;
import cn.Noble.Util.Music;
import cn.Noble.Util.Chat.Helper;
import cn.Noble.Util.Player.PlayerUtil;
import cn.Noble.Util.RayCast.RayCastUtil;
import cn.Noble.Util.Timer.Stopwatch;
import cn.Noble.Util.Timer.TimerUtil;
import cn.Noble.Util.gl.GLUtils;
import cn.Noble.Util.math.MathUtil;
import cn.Noble.Util.math.RotationUtil;
import cn.Noble.Util.render.FadeUtil;
import cn.Noble.Util.render.RenderUtil;
import cn.Noble.Util.render.RenderUtils;
import cn.Noble.Util.rotaions.RotationUtils;
import cn.Noble.Values.Mode;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class Aura extends Module {

	public static EntityLivingBase curTarget;
	private List<Entity> targets = new ArrayList();
	private int index;

	private Mode<Enum> mode = new Mode<Enum>("Mode", AuraMode.values(), AuraMode.Single);
	private Numbers<Double> cps = new Numbers<Double>("CPS", 10.0, 1.0, 20.0, 0.5);
	private Numbers<Double> range = new Numbers<Double>("Range", 4.5, 1.0, 6.0, 0.1);
	private Numbers<Double> switchDelay = new Numbers<Double>("SwitchDelay", 500.0, 1.0, 5000.0, 1.0);
	private Option<Boolean> pre = new Option<Boolean>("PreAttack", false);
	private Option<Boolean> rot = new Option<Boolean>("Rotation", true);
	private Option<Boolean> esp = new Option<Boolean>("ESP", false);
	private Option<Boolean> autoblock = new Option<Boolean>("Autoblock", true);
	private Option<Boolean> centre = new Option<Boolean>("Go Centre", false);
	private Option<Boolean> players = new Option<Boolean>("Players", true);
	private Option<Boolean> friend = new Option<Boolean>("FriendFliter", true);
	private Option<Boolean> animals = new Option<Boolean>("Animals", true);
	private Option<Boolean> mobs = new Option<Boolean>("Mobs", false);
	private Option<Boolean> invis = new Option<Boolean>("Invisibles", false);
	private Option<Boolean> interact = new Option<Boolean>("Interact", false);
	private Option<Boolean> death = new Option<Boolean>("DeathCheck", true);

	private boolean isBlocking;
	private Comparator<Entity> angleComparator = Comparator.comparingDouble(e2 -> e2.getDistanceToEntity(mc.player));

	private TimerUtil attackTimer = new TimerUtil();

	private TimerUtil switchTimer = new TimerUtil();

	private Object texture;
	float anim = 100;

	public Aura() {
		super("KillAura", new String[] { "ka", "aura", "killa" }, ModuleType.Combat);
		this.addValues(this.mode, this.cps, this.range, this.switchDelay, this.pre, this.rot, this.esp, this.autoblock,
				this.centre, this.players, this.friend, this.animals, this.mobs, this.invis, this.interact, this.death);
	}

	@Override
	public void onDisable() {
		this.curTarget = null;
		this.targets.clear();
		if (this.autoblock.getValue().booleanValue() && this.hasSword() && this.mc.player.isBlocking()) {
			this.unBlock();
		}
	}

	@Override
	public void onEnable() {
		this.curTarget = null;
		this.index = 0;
	}

	public static double random(double min, double max) {
		Random random = new Random();
		return min + (double) ((int) (random.nextDouble() * (max - min)));
	}

	private boolean shouldAttack() {
		return this.attackTimer.hasReached(1000.0 / (this.cps.getValue() + MathUtil.randomDouble(-1.0, 1.0)));
	}

	@EventHandler
	public void onRender(EventRender3D event) {
		if (!(Aura.curTarget != null))
			return;
		Color color = new Color(100, 30, 30, 10);

		if (Aura.curTarget.hurtResistantTime > 0) {
			color = new Color(190, 30, 30, 30);
		}
		if (this.esp.getValue()) {

			if (this.mode.getValue() == AuraMode.Switch) {
				for (Entity ent : this.targets) {

					Color col = new Color(170, 30, 30, 30);

					if (ent == this.curTarget) {
						col = new Color(30, 200, 30, 50);
					}

					if (ent.hurtResistantTime > 0) {
						col = new Color(255, 30, 30, 50);
						ent.hurtResistantTime = 0;
					}

					RenderUtil.drawFilledESP(ent, col);
				}
			}

			Entity player = Aura.curTarget;
			float partialTicks = event.getPartialTicks();

			GL11.glPushMatrix();
			GL11.glDisable(3553);
			RenderUtil.startDrawing();
			GL11.glDisable(2929);
			GL11.glDepthMask(false);
			GL11.glLineWidth(4.0f);
			GL11.glBegin(3);
			final double pix2 = 6.283185307179586;
			for (int i = 0; i <= 10; ++i) {

				RenderUtil.glColor(FadeUtil.fade(FadeUtil.BLUE.getColor()).getRGB());


			}
			GL11.glEnd();
			GL11.glDepthMask(true);
			GL11.glEnable(2929);
			RenderUtil.stopDrawing();
			GL11.glEnable(3553);
			GL11.glPopMatrix();
		}
	}

	private boolean hasSword() {
		if (mc.player.inventory.getCurrentItem() != null) {
			if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemSword) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@EventHandler
	private void onTick(EventTick event) {
		if (this.death.getValue() && mc.player != null) {
			if ((!mc.player.isEntityAlive() || (mc.currentScreen != null && mc.currentScreen instanceof GuiGameOver))) {
				this.setEnabled(false);
				NotificationPublisher.queue("Auto Disable", "Aura Disabled For Death.", NotificationType.INFO, 2000);
				return;
			}
			if (mc.player.ticksExisted <= 1) {
				this.setEnabled(false);
				NotificationPublisher.queue("Auto Disable", "Aura Disabled For Death.", NotificationType.INFO, 2000);
				return;
			}
		}
	}

	private void block() {
		if (mc.player.getHeldItem().getItem() instanceof ItemSword) {
			KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
			if (this.mc.playerController.sendUseItem(this.mc.player, this.mc.world,
					this.mc.player.inventory.getCurrentItem())) {
				this.mc.getItemRenderer().resetEquippedProgress2();
			}
			this.isBlocking = true;
		}

		if (this.interact.getValue()) {
			mc.getConnection()
					.sendPacket((Packet) new C02PacketUseEntity(curTarget, C02PacketUseEntity.Action.INTERACT));
			mc.getConnection()
					.sendPacket((Packet) new C02PacketUseEntity(curTarget, C02PacketUseEntity.Action.INTERACT));
		}
	}

	private void unBlock() {
		if (mc.player.getHeldItem().getItem() instanceof ItemSword && isBlocking) {
			KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
			this.mc.playerController.onStoppedUsingItem(this.mc.player);
			this.isBlocking = false;
		}
	}

	@EventHandler
	private void onUpdate(EventPreUpdate event) {
		this.setSuffix(this.mode.getValue());

		if (curTarget != null)
			mc.player.setSprinting(false);

		if (curTarget == null && autoblock.getValue()) {
			if (hasSword()) {
				unBlock();
			}
		}
		if (hasSword() && this.curTarget != null && autoblock.getValue() && !isBlocking) {
			this.block();
		}
		this.targets = this.getTargets(range.getValue());

		targets.sort(this.angleComparator);

		if (this.targets.size() > 1 && this.mode.getValue() == AuraMode.Switch) {
			if (mc.player.ticksExisted % 2 == 1) {

				if (curTarget == null) {
					curTarget = (EntityLivingBase) this.targets.get(0);
				}

				if (curTarget.hurtTime > 0 || switchTimer.hasReached(200)) {
					++this.index;
					this.switchTimer.reset();
				}
			}
		}

		if (this.mc.player.ticksExisted % switchDelay.getValue().intValue() == 0 && this.targets.size() > 1
				&& this.mode.getValue() == AuraMode.Single) {

			if (curTarget.getDistanceToEntity(mc.player) > range.getValue()) {
				++index;
			} else if (curTarget.isDead) {
				++index;
			}
		}

		if (curTarget != null) {
			if (this.pre.getValue() && this.shouldAttack()) {
				if (this.hasSword() && this.mc.player.isBlocking() && this.isValidEntity(this.curTarget)) {
					unBlock();
				}
				this.attack();
				this.attackTimer.reset();
				if (!mc.player.isBlocking() && this.hasSword() && autoblock.getValue().booleanValue()) {
					this.block();
				}
			}
			curTarget = null;
		}

		if (!this.targets.isEmpty()) {
			if (this.index >= this.targets.size()) {
				this.index = 0;
			}
			curTarget = (EntityLivingBase) this.targets.get(this.index);

			if (!(this.mode.getValue() == AuraMode.Switch)) {
				if (rot.getValue()) {
					mc.player.rotationYawHead = this.getRotationFormEntity(curTarget)[0];
					mc.player.rotationPitchHead = this.getRotationFormEntity(curTarget)[1];
					mc.player.renderYawOffset = this.getRotationFormEntity(curTarget)[0];
					mc.player.prevRenderYawOffset = this.getRotationFormEntity(curTarget)[0];
					event.setYaw(this.getRotationFormEntity(curTarget)[0]);
					event.setPitch(this.getRotationFormEntity(curTarget)[1]);
				}
			}
		}
	}

	public float[] getRotationFormEntity(EntityLivingBase target) {
		AngleUtility angleUtility = new AngleUtility(6.0f, 60.0f, 3.0f, 30.0f);
		Vector3<Double> enemyCoords = new Vector3<Double>(target.posX, target.posY, target.posZ);
		double X = mc.player.posX;
		double Y = mc.player.posY;
		Vector3<Double> myCoords = new Vector3<Double>(X, Y, mc.player.posZ);
		Angle dstAngle = angleUtility.calculateAngle(enemyCoords, myCoords);
		Angle smoothedAngle = angleUtility.smoothAngle(dstAngle, dstAngle);
		return new float[] { smoothedAngle.getYaw(), smoothedAngle.getPitch() };
	}

	@EventHandler
	private void onUpdatePost(EventPostUpdate e) {
		if (this.pre.getValue())
			return;
		if (curTarget != null) {
			if (this.shouldAttack()) {
				if (this.hasSword() && this.mc.player.isBlocking() && this.isValidEntity(this.curTarget)) {
					unBlock();
				}
				this.attack();
				this.attackTimer.reset();
			}
			if (!mc.player.isBlocking() && this.hasSword() && autoblock.getValue().booleanValue()) {
				this.block();
			}
		}
	}

	@EventHandler
	private void onStrafe(EventStrafe event) {
		if (!this.centre.getValue() || this.curTarget == null)
			return;

		float yaw = this.getRotationFormEntity(curTarget)[0];
		float strafe = event.getStrafe();
		float forward = mc.player.getDistanceToEntity(curTarget) < 0.17 ? 0 : event.getForward();
		float friction = event.getFriction();

		float f = strafe * strafe + forward * forward;

		if (f >= 1.0E-4F) {
			f = MathHelper.sqrt_float(f);

			if (f < 1.0F) {
				f = 1.0F;
			}

			f = friction / f;
			strafe *= f;
			forward *= f;

			float yawSin = MathHelper.sin((float) (yaw * Math.PI / 180F));
			float yawCos = MathHelper.cos((float) (yaw * Math.PI / 180F));

			mc.player.motionX += strafe * yawCos - forward * yawSin;
			mc.player.motionZ += forward * yawCos + strafe * yawSin;
		}
		event.setCancelled(true);
	}

	private void attack() {
		mc.player.swingItem();
		mc.player.onEnchantmentCritical(this.curTarget);
		mc.player.sendQueue.addToSendQueue(new C02PacketUseEntity(this.curTarget, C02PacketUseEntity.Action.ATTACK));
	}

	public List<Entity> getTargets(Double value) {
		return mc.world.loadedEntityList.stream()
				.filter(e -> (double) mc.player.getDistanceToEntity((Entity) e) <= value && isValidEntity((Entity) e))
				.collect(Collectors.toList());
	}

	private boolean isValidEntity(Entity ent) {

		if (ent == mc.player)
			return false;

		if (mc.player.getDistanceToEntity(ent) > range.getValue())
			return false;

		if (ent.isInvisible() && !invis.getValue())
			return false;

		if (!ent.isEntityAlive())
			return false;

		if (FriendManager.isFriend(ent.getName()))
			return false;

		if (ent instanceof EntityPlayer && FriendManager.isFriend(ent.getDisplayName().getUnformattedText())
				&& this.friend.getValue())
			return false;

		if ((ent instanceof EntityMob || ent instanceof EntityGhast || ent instanceof EntityGolem
				|| ent instanceof EntityDragon || ent instanceof EntitySlime) && mobs.getValue())
			return true;

		if ((ent instanceof EntitySquid || ent instanceof EntityBat || ent instanceof EntityVillager)
				&& animals.getValue())
			return true;

		if (ent instanceof EntityAnimal && animals.getValue())
			return true;

		AntiBot ab = (AntiBot) Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
		if (ab.isEntityBot(ent))
			return false;

		if (ent instanceof EntityPlayer && players.getValue() && !Teams.isOnSameTeam(ent))
			return true;

		return false;
	}

	static enum AuraMode {
		Switch, Single,
	}
}
