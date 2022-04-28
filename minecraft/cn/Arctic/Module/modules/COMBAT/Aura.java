package cn.Arctic.Module.modules.COMBAT;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import cn.Arctic.Client;
import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventRender3D;
import cn.Arctic.Event.events.Update.EventPostUpdate;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Manager.FriendManager;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.MOVE.Scaffold;
import cn.Arctic.Module.modules.PLAYER.Teams;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.Util.math.MathUtil;
import cn.Arctic.Util.math.RotationUtil;
import cn.Arctic.Util.render.RenderUtil;
import cn.Arctic.Util.rotaions.RotationUtils;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;



public class Aura
        extends Module {
    public static float rotationPitch;
    public List targets = new ArrayList(0);
    public static EntityLivingBase curTarget = null;
    private TimerUtil switchTimer = new TimerUtil();
    private static Numbers<Double> aps = new Numbers<Double>("CPS", 12.0, 1.0, 20.0, 0.1);
    public static Numbers<Double> reach = new Numbers<Double>("Reach", 4.5, 1.0, 6.0, 0.1);
    public static Numbers<Double> Delay = new Numbers<Double>("SwitchDelay", 15.0, 1.0,5000.0, 0.1);
    public static Numbers<Double> fov = new Numbers<Double>("fov", 180.0, 1.0, 360.0, 0.1);
    private Option<Boolean> blocking = new Option<Boolean>("AutoBlock", true);
    private Option<Boolean> players = new Option<Boolean>("Players", true);
    private Option<Boolean> animals = new Option<Boolean>("Animals", true);
    private Option<Boolean> mobs = new Option<Boolean>("Mobs", false);
    private Option<Boolean> invis = new Option<Boolean>("Invisibles", false);
    private Numbers<Double> crack = new Numbers("CrackSize", Double.valueOf(1.0D), Double.valueOf(0.0D), Double.valueOf(10.0D), Double.valueOf(1.0D));
    public static Option<Boolean> walls = new Option<Boolean>("walls", true);
    private Mode<Enum> espmode = new Mode("ESP", (Enum[])EMode.values(), (Enum)EMode.Jello);
    private Mode<Enum> Priority = new Mode("Priority", (Enum[]) priority.values(), (Enum) priority.Health);
    public static Mode<Enum> mode = new Mode("Mode", (Enum[]) AuraMode.values(), (Enum) AuraMode.Switch);
    private TimerUtil blockTimer = new TimerUtil();
    public static boolean doBlock = false;
    private boolean unBlock = false;
    private int index;
    private TimerUtil timer = new TimerUtil();


    public Aura() {
    	
        super("KillAura", new String[] { "ka", "aura", "killa" }, ModuleType.Combat);
        Random random = new Random();
        this.addValues(aps, reach, Delay, fov, crack,this.blocking, this.players, this.animals, this.mobs, this.invis, walls, this.espmode, this.Priority, mode);
    }

    private boolean shouldAttack() {
        return this.timer.hasReached(1000 / aps.getValue());
    }
	private void block(boolean interact) {
		if (mc.player.getHeldItem().getItem() instanceof ItemSword) {
			KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
			if (this.mc.playerController.sendUseItem(this.mc.player, this.mc.world,
					this.mc.player.inventory.getCurrentItem())) {
				this.mc.getItemRenderer().resetEquippedProgress2();
			}
			this.doBlock = true;
		}

		if (interact) {
			mc.getNetHandler().addToSendQueue((Packet) new C02PacketUseEntity(curTarget, C02PacketUseEntity.Action.INTERACT));
			mc.getNetHandler().addToSendQueue
					((Packet) new C02PacketUseEntity(curTarget, C02PacketUseEntity.Action.INTERACT));
		}
	}

	private void unblock() {
		if (mc.player.getHeldItem().getItem() instanceof ItemSword && doBlock) {
			KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
			this.mc.playerController.onStoppedUsingItem(this.mc.player);
			this.doBlock = false;
		}
	}


    @EventHandler
    private void onPreUpdate(EventPreUpdate event) {
        this.setSuffix(this.mode.getValue());
        targets = sortList(loadTarget(reach.getValue(),(float)(double)fov.getValue()));
        if (this.mc.player.getHealth() <= 0 && this.mode.getValue() == AuraMode.Single && this.targets.size() > 0) {
            ++this.index;
        }
        if (this.mode.getValue() == AuraMode.Switch && this.targets.size() > 0 && switchTimer.hasReached(Delay.getValue())){
            ++this.index;
            switchTimer.reset();
        }
        if (!this.targets.isEmpty()) {
            if (this.index >= this.targets.size()) {
                this.index = 0;
            }
        }
        this.doBlock = false;
        if(!targets.isEmpty()) { curTarget = (EntityLivingBase) targets.get(index); }
        if (!targets.isEmpty() && curTarget != null) {
            if (this.index >= this.targets.size()) {
                this.index = 0;
            }
            mc.player.rotationYawHead = RotationUtils.getRotations(curTarget)[0];
            mc.player.rotationPitchHead = RotationUtils.getRotations(curTarget)[1];
            mc.player.renderYawOffset = RotationUtils.getRotations(curTarget)[0];

            event.setYaw(RotationUtils.getRotations(curTarget)[0]);
            event.setPitch(RotationUtils.getRotations(curTarget)[1]);
        } else {
            this.targets.clear();
            this.curTarget = null;
            if (this.unBlock && blocking.getValue()) {
                this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                this.mc.player.itemInUseCount = 0;
                this.unBlock = false;
            }
        }
        if(ModuleManager.getModuleByClass(Scaffold.class).isEnabled()) {
        	setEnabled(false);
        }
    }

    private void doAttack() {
        int delayValue = (int) (1000 / this.aps.getValue().intValue() + MathUtil.randomDouble(-2.0, 2.0));

        if ((double) Minecraft.player.getDistanceToEntity(curTarget) <= this.reach.getValue() + 0.4 && this.blockTimer.delay(delayValue - 1)) {
            this.blockTimer.reset();

            if (Minecraft.player.isBlocking() || Minecraft.player.getHeldItem() != null && Minecraft.player.getHeldItem().getItem() instanceof ItemSword && this.blocking.getValue().booleanValue()) {
                this.mc.getNetHandler().addToSendQueue((Packet) new C07PacketPlayerDigging(
                        C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                this.unBlock = false;
            }
            if (!Minecraft.player.isBlocking() && !this.blocking.getValue().booleanValue()
                    && Minecraft.player.itemInUseCount > 0) {
                Minecraft.player.itemInUseCount = 0;
            }
            this.attack();
            this.doBlock = true;
        }

    }
    @EventHandler
    public void onPost(EventPostUpdate event) {
    	 int crackSize = ((Double) this.crack.getValue()).intValue();int i2 = 0;	
        this.sortList(targets);
        if (this.curTarget != null && this.shouldAttack()) {
            this.doAttack();
            this.newAttack();
        }
        if (curTarget != null && (Minecraft.player.getHeldItem() != null && Minecraft.player.getHeldItem().getItem() instanceof ItemSword && this.blocking.getValue().booleanValue() || Minecraft.player.isBlocking()) && this.doBlock) {
            Minecraft.player.itemInUseCount = Minecraft.player.getHeldItem().getMaxItemUseDuration();
            this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, Minecraft.player.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
            this.unBlock = true;
        }
        while (i2 < crackSize) {
            this.mc.effectRenderer.emitParticleAtEntity(curTarget, EnumParticleTypes.CRIT_MAGIC);
            i2++;
        }
    }

    private void attack() {
        Minecraft.player.swingItem();
        this.doBlock = true;
        this.mc.player.sendQueue.addToSendQueue(new C02PacketUseEntity(curTarget, C02PacketUseEntity.Action.ATTACK));
        if (Minecraft.player.isBlocking() && this.blocking.getValue().booleanValue()
                && Minecraft.player.inventory.getCurrentItem() != null
                && Minecraft.player.inventory.getCurrentItem().getItem() instanceof ItemSword) {
            this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255,
                    Minecraft.player.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
            this.unBlock = true;
        }
        if (!Minecraft.player.isBlocking() && !this.blocking.getValue().booleanValue()
                && Minecraft.player.itemInUseCount > 0) {
            Minecraft.player.itemInUseCount = 0;
        }
    }

    private void newAttack() {
        if (Minecraft.player.isBlocking()) {
            for (int i = 0; i <= 2; i++) {
                this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(0, 0, 0), 255, Minecraft.player.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
            }
        }
        if (Minecraft.player.isBlocking()) {
            for (int i = 0; i <= 2; i++) {
                this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255,
                        Minecraft.player.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
            }
        }
        if (Minecraft.player.isBlocking() && this.timer.delay(100)) {
            for (int i = 0; i <= 2; i++) {
                this.mc.getNetHandler().addToSendQueue((Packet) new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }
        if (!Minecraft.player.isBlocking() && !this.blocking.getValue().booleanValue() && Minecraft.player.itemInUseCount > 0) {
            Minecraft.player.itemInUseCount = 0;
        }
    }

    public List<EntityLivingBase> loadTarget(double Range, float fov) {
        ArrayList list = new ArrayList();
        for (Object o : mc.world.loadedEntityList) {
            if (!(o instanceof EntityLivingBase)) continue;
            EntityLivingBase entityLivingBase = (EntityLivingBase)o;
            float yaw = RotationUtils.getBowAngles((Entity)entityLivingBase)[1];
            double yawDistance = RotationUtil.getDistanceBetweenAngles((float)yaw, (float)Minecraft.player.rotationYaw);
            if (entityLivingBase instanceof ClientPlayerEntity || !entityLivingBase.isEntityAlive() || (double)Minecraft.player.getDistanceToEntity((Entity)entityLivingBase) > Range || yawDistance > (double)fov || !this.isValidEntity(entityLivingBase)) continue;
            list.add((Object)entityLivingBase);
        }
        return list;
    }

    private boolean isValidEntity(EntityLivingBase ent) {
        if (ent == mc.player) {
            return false;
        }
        if (!RotationUtil.canEntityBeSeen(ent) && !(Boolean) walls.getValue()) {
            return false;
        }
        if (!ent.isEntityAlive()) {
            return false;
        }
        AntiBot ab = (AntiBot) Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
        if(ab.isBot(ent)){
            return false;
        }
        if(Teams.isOnSameTeam(ent)){
            return false;
        }
        if (FriendManager.isFriend(ent.getName())) {
            return false;
        }
        if (ent instanceof EntityPlayer && this.players.getValue().booleanValue() && !Teams.isOnSameTeam(ent)) {
            return true;
        }
        if (ent instanceof EntityMob && this.mobs.getValue().booleanValue()) {
            return true;
        }
        if (ent instanceof EntityAnimal && this.animals.getValue().booleanValue()) {
            return true;
        }
        if (ent.isInvisible() && !this.invis.getValue().booleanValue()) {
            return true;
        }
        return false;
    }
    @EventHandler
    public void onRender(EventRender3D render) {
        if (curTarget == null || this.espmode.getValue() == EMode.None)
            return;
        Color color = (curTarget.hurtTime > 0) ? new Color(-1618884) : new Color(-13330213), color2 = color;
          if (curTarget != null && this.espmode.getValue() == EMode.Box) {
            mc.getRenderManager();
            double x = curTarget.lastTickPosX + (curTarget.posX - curTarget.lastTickPosX) * mc.timer.renderPartialTicks - RenderManager.renderPosX;
            mc.getRenderManager();
            double y = curTarget.lastTickPosY + (curTarget.posY - curTarget.lastTickPosY) * mc.timer.renderPartialTicks - RenderManager.renderPosY;
            mc.getRenderManager();
            double z = curTarget.lastTickPosZ + (curTarget.posZ - curTarget.lastTickPosZ) * mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            if (curTarget instanceof EntityPlayer) {
                double width = (curTarget.getEntityBoundingBox()).maxX - (curTarget.getEntityBoundingBox()).minX;
                double height2 = (curTarget.getEntityBoundingBox()).maxY - (curTarget.getEntityBoundingBox()).minY + 0.25D;
                float red = (curTarget.hurtTime > 0) ? 1.0F : 0.0F;
                float green = (curTarget.hurtTime > 0) ? 0.2F : 0.5F;
                float blue = (curTarget.hurtTime > 0) ? 0.0F : 1.0F;
                float alpha = 0.2F;
                float lineRed = (curTarget.hurtTime > 0) ? 1.0F : 0.0F;
                float lineGreen = (curTarget.hurtTime > 0) ? 0.2F : 0.5F;
                float lineBlue = (curTarget.hurtTime > 0) ? 0.0F : 1.0F;
                float lineAlpha = 1.0F;
                float lineWdith = 2.0F;
                RenderUtil.drawEntityESP(x, y, z, width, height2, red, green, blue, alpha, lineRed, lineGreen, lineBlue, lineAlpha, lineWdith);
            } else {
                double width = (curTarget.getEntityBoundingBox()).maxX - (curTarget.getEntityBoundingBox()).minX + 0.1D;
                double height3 = (curTarget.getEntityBoundingBox()).maxY - (curTarget.getEntityBoundingBox()).minY + 0.25D;
                float red = (curTarget.hurtTime > 0) ? 1.0F : 0.0F;
                float green = (curTarget.hurtTime > 0) ? 0.2F : 0.5F;
                float blue = (curTarget.hurtTime > 0) ? 0.0F : 1.0F;
                float alpha = 0.2F;
                float lineRed = (curTarget.hurtTime > 0) ? 1.0F : 0.0F;
                float lineGreen = (curTarget.hurtTime > 0) ? 0.2F : 0.5F;
                float lineBlue = (curTarget.hurtTime > 0) ? 0.0F : 1.0F;
                float lineAlpha = 1.0F;
                float lineWdith = 2.0F;
                RenderUtil.drawEntityESP(x, y, z, width, height3, red, green, blue, alpha, lineRed, lineGreen, lineBlue, lineAlpha, lineWdith);
            }
        } else if (this.espmode.getValue() == EMode.LiquidBounce) {
            double x;
            double y;
            double z;
            double width;
            double height;
            float red;
            float green;
            float blue;
            float alpha;
            float lineRed;
            float lineGreen;
            float lineBlue;
            float lineAlpha;
            float lineWdith;
            this.mc.getRenderManager();
            x = Aura.curTarget.lastTickPosX + (Aura.curTarget.posX - Aura.curTarget.lastTickPosX) * this.mc.timer.renderPartialTicks - RenderManager.renderPosX;
            this.mc.getRenderManager();
            y = Aura.curTarget.lastTickPosY + (Aura.curTarget.posY - Aura.curTarget.lastTickPosY) * this.mc.timer.renderPartialTicks - RenderManager.renderPosY;
            this.mc.getRenderManager();
            z = Aura.curTarget.lastTickPosZ + (Aura.curTarget.posZ - Aura.curTarget.lastTickPosZ) * this.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            if (Aura.curTarget instanceof EntityPlayer) {
                x -= 0.5;
                z -= 0.5;
                y += Aura.curTarget.getEyeHeight() + 0.35 - (Aura.curTarget.isSneaking() ? 0.25 : 0.0);
                final double mid = 0.5;
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                final double rotAdd = -0.25 * (Math.abs(Aura.curTarget.rotationPitch) / 90.0f);
                GL11.glTranslated(x + mid, y + mid, z + mid);
                GL11.glRotated((double) (-Aura.curTarget.rotationYaw % 360.0f), 0.0, 1.0, 0.0);
                GL11.glTranslated(-(x + mid), -(y + mid), -(z + mid));
                GL11.glDisable(3553);
                GL11.glEnable(2848);
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
                GL11.glLineWidth(2.0f);
                RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 0.05, z + 1.0));
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f);
                RenderUtil.drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 0.05, z + 1.0));
                GL11.glDisable(2848);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
                GL11.glPopMatrix();
            } else {
                width = Aura.curTarget.getEntityBoundingBox().maxZ - Aura.curTarget.getEntityBoundingBox().minZ;
                height = 0.1;
                red = 0.0f;
                green = 0.5f;
                blue = 1.0f;
                alpha = 0.5f;
                lineRed = 0.0f;
                lineGreen = 0.5f;
                lineBlue = 1.0f;
                lineAlpha = 1.0f;
                lineWdith = 2.0f;
                RenderUtil.drawEntityESP(x, y + Aura.curTarget.getEyeHeight() + 0.25, z, width, height, red, green, blue, alpha, lineRed, lineGreen, lineBlue, lineAlpha, lineWdith);
            }
        }
            else if (espmode.getValue() == EMode.Jello) {
      			final double x1 = curTarget.lastTickPosX + (curTarget.posX - curTarget.lastTickPosX) * render.getPartialTicks() - mc.getRenderManager().viewerPosX;
      			final double y1 = curTarget.lastTickPosY + (curTarget.posY - curTarget.lastTickPosY) * render.getPartialTicks() - mc.getRenderManager().viewerPosY;
      			final double z1 = curTarget.lastTickPosZ + (curTarget.posZ - curTarget.lastTickPosZ) * render.getPartialTicks() - mc.getRenderManager().viewerPosZ;
      			if (curTarget.KillAuraESPAnimation > curTarget.getEyeHeight() + 0.4 || curTarget.KillAuraESPAnimation < 0.0) {
      				curTarget.isUp = !curTarget.isUp;
      			}
      			if (curTarget.isUp) {
      				EntityLivingBase target = curTarget;
      				double auraHitAnimationsSetup = target.KillAuraESPAnimation;
      				double n = 3.0;
      				target.KillAuraESPAnimation = auraHitAnimationsSetup + n / Minecraft.getDebugFPS();
      			}
      			else {
      				EntityLivingBase target2 = curTarget;
      				double auraHitAnimationsSetup2 = target2.KillAuraESPAnimation;
      				double n2 = 3.0;
      				target2.KillAuraESPAnimation = auraHitAnimationsSetup2 - n2 / Minecraft.getDebugFPS();
      			}
      			if (curTarget.isUp) {
      				for (int i = 0; i < 100; ++i) {
      					this.Jelloesp((Entity)curTarget, x1, y1 + curTarget.KillAuraESPAnimation - i * 0.005, z1, 1f);
      				}
      			}
      			else {
      				for (int i = 0; i < 100; ++i) {
      					this.Jelloesp((Entity)curTarget, x1, y1 + curTarget.KillAuraESPAnimation + i * 0.005, z1, 1f);
      				}
      			}
      		}
    }
        
   
        
          
          public void Jelloesp(final Entity player, final double x, final double y, final double z, final float alpha) {
      		Cylinder c = new Cylinder();
      		GL11.glPushMatrix();
      		GL11.glDisable(2896);
      		GL11.glDisable(3553);
      		GL11.glEnable(3042);
      		GL11.glBlendFunc(770, 771);
      		GL11.glDisable(2929);
      		GL11.glEnable(2848);
      		GL11.glDepthMask(true);
      		GlStateManager.translate(x, y, z);
      		GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
      		GlStateManager.rotate(180.0f, 90.0f, 0.0f, 2.0f);
      		GlStateManager.rotate(180.0f, 0.0f, 90.0f, 90.0f);
      		c.setDrawStyle(100011);
      		c.draw(0.8f, 0.8f, -0.0f, 360, 0);
      		GL11.glDisable(2848);
      		GL11.glEnable(2929);
      		GL11.glDisable(3042);
      		GL11.glEnable(2896);
      		GL11.glEnable(3553);
      		GL11.glPopMatrix();
      	}

    @Override
    public void onEnable() {
        this.targets.clear();
        curTarget = null;
        index = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.targets.clear();
        curTarget = null;
        Minecraft.player.itemInUseCount = 0;
        mc.player.renderYawOffset = mc.player.rotationYaw;
        rotationPitch = 0.0f;

        super.onDisable();
    }

    private List<EntityLivingBase> sortList(List<EntityLivingBase> weed) {
        if(this.Priority.getValue() == priority.Range) { weed.sort((o1, o2) -> (int) (o1.getDistanceToEntity(mc.player) * 1000 - o2.getDistanceToEntity(mc.player) * 1000)); }if(this.Priority.getValue() == priority.Fov) { weed.sort(Comparator.comparingDouble(o -> RotationUtil.getDistanceBetweenAngles(mc.player.rotationPitch, RotationUtil.getRotations(o)[0]))); }if(this.Priority.getValue() == priority.Angle) { weed.sort((o1, o2) -> { float[] rot1 = RotationUtil.getRotations(o1);float[] rot2 = RotationUtil.getRotations(o2);return (int) ((mc.player.rotationYaw - rot1[0]) - (mc.player.rotationYaw - rot2[0])); }); }if(this.Priority.getValue() == priority.Health) { weed.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth())); }if(this.Priority.getValue() == priority.Armor) { weed.sort(Comparator.comparingInt(o -> (o instanceof EntityPlayer ? ((EntityPlayer) o).inventory.getTotalArmorValue() : (int) o.getHealth()))); }
        return weed;
    }
    public static boolean isOnSameTeam(Entity entity) {
		if (!Client.instance.getModuleManager().getModuleByClass(Aura.class).isEnabled())
			return false;
		if (Minecraft.getMinecraft().player.getDisplayName().getUnformattedText().startsWith("\247")) {
			if (Minecraft.getMinecraft().player.getDisplayName().getUnformattedText().length() <= 2
					|| entity.getDisplayName().getUnformattedText().length() <= 2) {
				return false;
			}
			if (Minecraft.getMinecraft().player.getDisplayName().getUnformattedText().substring(0, 2)
					.equals(entity.getDisplayName().getUnformattedText().substring(0, 2))) {
				return true;
			}
		}
		return false;
	}

    static enum EMode {
        Box,
        None,
        RainBow,
        LiquidBounce,
        Jello;
    }
    

    static enum priority {
        Range, Fov, Angle, Health, Armor;
    }

    static enum AuraMode {
        Single,Switch;
    }
}

