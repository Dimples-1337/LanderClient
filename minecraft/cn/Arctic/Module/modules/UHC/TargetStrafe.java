package cn.Arctic.Module.modules.UHC;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cn.Arctic.Client;
import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventMove;
import cn.Arctic.Event.events.EventRender3D;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.COMBAT.Aura;
import cn.Arctic.Module.modules.MOVE.Flight;
import cn.Arctic.Module.modules.MOVE.Speed;
import cn.Arctic.Util.math.MathUtil;
import cn.Arctic.Util.render.RenderUtil;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class TargetStrafe extends Module
{
    public static Numbers<Double> range;
    public Entity target;
    public static float hue;
    private double degree;
    private float groundY;
    public static  Option<Boolean> check = new Option<Boolean>("check", true);
    public static  Option<Boolean> auto= new Option<Boolean>("auto", true);
    public static  Option<Boolean> esp = new Option<Boolean>("esp", true);
    public static Numbers<Double> spaceRange = new Numbers<Double>("HightRange", 5.0, 0.1, 7.0, 0.1);
    public static int index;
	public static float currentYaw;
    private boolean left;
    private float rAnims;
    private List<Entity> targets;

    public TargetStrafe() {
        super("TargetStrafe", new String[] { "TargetStrafe" }, ModuleType.Combat);
 
        this.esp = new Option<Boolean>("Esp", true);
        this.degree = 0.0;
        this.left = true;
        this.targets = new ArrayList<Entity>();
        this.addValues(TargetStrafe.range, this.spaceRange, this.check, this.auto, this.esp);
        
           
            
    }

    @Override
    public void onEnable() {
        this.targets.clear();
        this.degree = 0.0;
        this.left = true;
        this.target = null;
        this.rAnims = 0.0f;
    }

    @EventHandler
    public void on3D(final EventRender3D e) {
        final Aura ka = (Aura) ModuleManager.getModuleByClass(Aura.class);
        if (this.esp.getValue() && ka.isEnabled()) {
            this.drawESP(e);
        }
    }

    private void drawESP(final EventRender3D render) {
        this.esp(Aura.curTarget, EventRender3D.getInstance().getPartialTicks(), TargetStrafe.range.getValue());
    }
    public static double interpolate(final double current, final double old, final double scale) {
        return old + (current - old) * scale;
    }

    private void drawCirle(final Entity entity, final Color color, final float partialTicks , double rad) {
    	float points = 90F;
        GlStateManager.enableDepth();
        for (double il = 0; il < 4.9E-324; il += 4.9E-324) {
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glEnable(2881);
            GL11.glEnable(2832);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glHint(3154, 4354);
            GL11.glHint(3155, 4354);
            GL11.glHint(3153, 4354);
            GL11.glDisable(2929);
            GL11.glLineWidth(6.0f);
            GL11.glBegin(3);
            final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
            final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
            final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;
            final double pix2 = 6.283185307179586;
            float speed = 5000f;
            float baseHue = System.currentTimeMillis() % (int) speed;
            baseHue /= speed;
            for (int i = 0; i <= 90; ++i) {
                float max = (i + (float) (il * 8)) / points;
                float hue = max + baseHue;
                while (hue > 1) {
                    hue -= 1;
                }
                final float r = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getRed();
                final float g = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getGreen();
                final float b = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getBlue();
                GL11.glColor3f(r, g, b);
                GL11.glVertex3d(x + rad * Math.cos(i * pix2 / points), y + il, z + rad * Math.sin(i * pix2 / points));
            }
            GL11.glEnd();
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glDisable(2881);
            GL11.glEnable(2832);
            GL11.glEnable(3553);
            GL11.glPopMatrix();
            GlStateManager.color(255, 255, 255);
        }
    }

    @EventHandler
    public void esp(final Entity player, final float partialTicks, final double rad) {
    	float points = 90F;
        GlStateManager.enableDepth();
        if (Aura.curTarget == null) {
            return;
        }
        final Minecraft mc = TargetStrafe.mc;
        if (Minecraft.player.onGround) {
            final Minecraft mc2 = TargetStrafe.mc;
            this.groundY = (float)Minecraft.player.posY;
        }
        final int countMod = 0;
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
        GL11.glDisable(2929);
        GL11.glLineWidth(5.0f);
        GL11.glBegin(3);
        final double x1 = Aura.curTarget.lastTickPosX + (Aura.curTarget.posX - Aura.curTarget.lastTickPosX) * partialTicks - TargetStrafe.mc.getRenderManager().viewerPosX;
        final double y1 = Aura.curTarget.lastTickPosY + (Aura.curTarget.posY - Aura.curTarget.lastTickPosY) * partialTicks - TargetStrafe.mc.getRenderManager().viewerPosY;
        final double z1 = Aura.curTarget.lastTickPosZ + (Aura.curTarget.posZ - Aura.curTarget.lastTickPosZ) * partialTicks - TargetStrafe.mc.getRenderManager().viewerPosZ;
        final int color = Color.WHITE.getRGB();
        final Color rainbowcolors = Color.getHSBColor(TargetStrafe.hue / 255.0f, 0.4f, 0.8f);
        final double pix2 = 6.283185307179586;
        for (double il = 0; il < 4.9E-324; il += 4.9E-324) {
            final double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
            final double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
            final double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;
            float speed = 5000f;
            float baseHue = System.currentTimeMillis() % (int) speed;
            baseHue /= speed;
            for (int i = 0; i <= 90; ++i) {
                float max = (i + (float) (il * 8)) / points;
                float hue = max + baseHue;
                while (hue > 1) {
                    hue -= 1;
                }
                final float r = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getRed();
                final float g = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getGreen();
                final float b = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getBlue();
                GL11.glColor3f(r, g, b);
                GL11.glVertex3d(x + rad * Math.cos(i * pix2 / points), y + il, z + rad * Math.sin(i * pix2 / points));
            }
            GL11.glEnd();
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glDisable(2881);
            GL11.glEnable(2832);
            GL11.glEnable(3553);
            GL11.glPopMatrix();
            GlStateManager.color(255, 255, 255);
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        RenderUtil.stopDrawing();
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    @EventHandler
    private void onUpdate(final EventPreUpdate e) {
        if (this.target != null && ModuleManager.getModuleByClass(Aura.class).isEnabled() && ModuleManager.getModuleByClass(Speed.class).isEnabled() && this.auto.getValue()) {
            mc.gameSettings.keyBindForward.Doing = true;
        } else {
            mc.gameSettings.keyBindForward.Doing = false;
        }
        final Client instance3 = Client.instance;
        Client.instance.getModuleManager();
        final Aura ka = (Aura) ModuleManager.getModuleByClass(Aura.class);
        final Client instance4 = Client.instance;
        Client.instance.getModuleManager();
        final Speed speed = (Speed) ModuleManager.getModuleByClass(Speed.class);
        if (ka.isEnabled()) {
            this.target = Aura.curTarget;
        } else {
            this.target = null;
        }
    }

    @EventHandler(priority = 2)
    private void onMove(final EventMove e) {
        if (this.canStrafe()) {
            final Client instance = Client.instance;
            Client.instance.getModuleManager();
            final Speed speedM = (Speed)ModuleManager.getModuleByClass(Speed.class);
            final double speed = speedM.moveSpeed;
            final Minecraft mc = TargetStrafe.mc;
            final double n = Minecraft.player.posZ - this.target.posZ;
            final Minecraft mc2 = TargetStrafe.mc;
            this.degree = Math.atan2(n, Minecraft.player.posX - this.target.posX);
            final double degree = this.degree;
            double n3;
            if (this.left) {
                final double n2 = speed;
                final Minecraft mc3 = TargetStrafe.mc;
                n3 = n2 / Minecraft.player.getDistanceToEntity(this.target);
            }
            else {
                final double n4 = speed;
                final Minecraft mc4 = TargetStrafe.mc;
                n3 = -(n4 / Minecraft.player.getDistanceToEntity(this.target));
            }
            this.degree = degree + n3;
            double x = this.target.posX + TargetStrafe.range.getValue() * Math.cos(this.degree);
            double z = this.target.posZ + TargetStrafe.range.getValue() * Math.sin(this.degree);
            if ((boolean)this.check.getValue() && this.needToChange(x, z)) {
                this.left = !this.left;
                final double degree2 = this.degree;
                final double n5 = 2.0;
                double n7;
                if (this.left) {
                    final double n6 = speed;
                    final Minecraft mc5 = TargetStrafe.mc;
                    n7 = n6 / Minecraft.player.getDistanceToEntity(this.target);
                }
                else {
                    final double n8 = speed;
                    final Minecraft mc6 = TargetStrafe.mc;
                    n7 = -(n8 / Minecraft.player.getDistanceToEntity(this.target));
                }
                this.degree = degree2 + n5 * n7;
                x = this.target.posX + TargetStrafe.range.getValue() * Math.cos(this.degree);
                z = this.target.posZ + TargetStrafe.range.getValue() * Math.sin(this.degree);
            }
            EventMove.setX(speed * -Math.sin((float)Math.toRadians(MathUtil.toDegree(x, z))));
            EventMove.setZ(speed * Math.cos((float)Math.toRadians(MathUtil.toDegree(x, z))));
        }
    }

    public boolean canStrafe() {
        final Aura ka = (Aura)ModuleManager.getModuleByClass(Aura.class);
        final Speed speed = (Speed)ModuleManager.getModuleByClass(Speed.class);
        final Flight Flight = (Flight)ModuleManager.getModuleByClass(Flight.class);
        return ka.isEnabled() && Aura.curTarget != null && target != null && (speed.isEnabled() || Flight.isEnabled());
    }

    public boolean needToChange(final double x, final double z) {
        final Minecraft mc = TargetStrafe.mc;
        if (Minecraft.player.isCollidedHorizontally) {
            final Minecraft mc2 = TargetStrafe.mc;
            if (Minecraft.player.ticksExisted % 2 == 0) {
                return true;
            }
        }
        final Minecraft mc3 = TargetStrafe.mc;
        int i = (int)(Minecraft.player.posY + 4.0);
        while (i >= 0) {
            final BlockPos playerPos = new BlockPos(x, i, z);
            final Minecraft mc4 = TargetStrafe.mc;
            if (!Minecraft.world.getBlockState(playerPos).getBlock().equals(Blocks.lava)) {
                final Minecraft mc5 = TargetStrafe.mc;
                if (!Minecraft.world.getBlockState(playerPos).getBlock().equals(Blocks.fire)) {
                    final Minecraft mc6 = TargetStrafe.mc;
                    if (!Minecraft.world.isAirBlock(playerPos)) {
                        return false;
                    }
                    --i;
                    continue;
                }
            }
            return true;
        }
        return true;
    }

    static {
        TargetStrafe.range = new Numbers<Double>("Range", 3.0, 0.1, 6.0, 0.1);
        TargetStrafe.hue = 0.0f;
    }
}
