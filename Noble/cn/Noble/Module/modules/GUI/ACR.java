package cn.Noble.Module.modules.GUI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventRender2D;
import cn.Noble.Font.CFontRenderer;
import cn.Noble.Font.FontLoaders;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.render.RenderUtil;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class ACR extends Module{
	private Numbers<Double> r = new Numbers<Double>("Red", 255.0, 0.0, 255.0, 1.0);
	private Numbers<Double> g = new Numbers<Double>("Green", 255.0, 0.0, 255.0, 1.0);
	private Numbers<Double> b = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 1.0);
	private Option<Boolean> players = new Option<Boolean> ("Player", true);
	private Option<Boolean> mobs = new Option<Boolean> ("Mobs", false);
	private Option<Boolean> animals = new Option<Boolean> ("Animals", false);
	private Option<Boolean> invisibles = new Option<Boolean> ("Invisible", false);
    public ACR() {
        super("OnScreenRadar", new String[] {"radar", "screenradar", "onscreenradar", "pointesp"}, ModuleType.Render);
        this.setColor(new Color(159, 190, 192).getRGB());
        this.addValues(r,g,b,players,mobs,animals,invisibles);
    }
    
    @EventHandler
    public void onScreen(EventRender2D event) {
    	ScaledResolution a = new ScaledResolution(mc);
        GlStateManager.pushMatrix();
        double size = 50;
        double xOffset = a.getScaledWidth() / 2 - 24.5;
        double yOffset = a.getScaledHeight() / 2 - 25.2;
        double playerOffsetX = mc.player.posX;
        double playerOffSetZ = mc.player.posZ;
        for (int i = 0; i < mc.world.loadedEntityList.size(); ++i) {
            Entity gay = mc.world.loadedEntityList.get(i);
            if (
                (
                    (mobs.getValue() && (gay instanceof EntityMob || gay instanceof EntitySlime || gay instanceof EntityVillager)) ||
                    (animals.getValue() && (gay instanceof EntityAnimal || gay instanceof EntitySquid)) ||
                    (players.getValue() && (gay instanceof EntityPlayer && gay != mc.player))
                ) &&
                (invisibles.getValue() || !gay.isInvisible())
            ) {
                double loaddist = 0.2;
                float pTicks = mc.timer.renderPartialTicks;
                double pos1 = (((gay.posX + (gay.posX - gay.lastTickPosX) * pTicks) - playerOffsetX) * loaddist);
                double pos2 = (((gay.posZ + (gay.posZ - gay.lastTickPosZ) * pTicks) - playerOffSetZ) * loaddist);
                double cos = Math.cos(mc.player.rotationYaw * (Math.PI * 2 / 360));
                double sin = Math.sin(mc.player.rotationYaw * (Math.PI * 2 / 360));
                double rotY = -(pos2 * cos - pos1 * sin);
                double rotX = -(pos1 * cos + pos2 * sin);
                double var7 = 0 - rotX;
                double var9 = 0 - rotY;
                if (MathHelper.sqrt_double(var7 * var7 + var9 * var9) < size / 2 - 4) {
                    float angle = (float) (Math.atan2(rotY - 0, rotX - 0) * 180 / Math.PI);
                    double x = ((size / 2) * Math.cos(Math.toRadians(angle))) + xOffset + size / 2;
                    double y = ((size / 2) * Math.sin(Math.toRadians(angle))) + yOffset + size / 2;
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(x, y, 0);
                    GlStateManager.rotate(angle, 0, 0, 1);
                    GlStateManager.scale(1.5, 1.0, 1.0);
                    drawESPCircle(0, 0, 3.2, 3);
                    drawESPCircle(0, 0, 3.0, 3);
                    drawESPCircle(0, 0, 2.5, 3);
                    drawESPCircle(0, 0, 2.0, 3);
                    drawESPCircle(0, 0, 1.5, 3);
                    drawESPCircle(0, 0, 1.0, 3);
                    drawESPCircle(0, 0, 0.5, 3);
                    GlStateManager.popMatrix();
                }
            }
        }
        GlStateManager.popMatrix();
    }
    
    public void glColor() {
        float alpha = (float)0.3;
        float red = this.r.getValue().floatValue()/255;
        float green = this.g.getValue().floatValue()/255;
        float blue = this.b.getValue().floatValue()/255;
        GlStateManager.color(red, green, blue, alpha);
    }
    public void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    public void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    public void drawESPCircle(double cx, double cy, double r, double n) {
        GL11.glPushMatrix();
        cx *= 2.0;
        cy *= 2.0;
        double b = 6.2831852 / n;
        double p = Math.cos(b);
        double s = Math.sin(b);
        double x = r *= 2.0;
        double y = 0.0;
        enableGL2D();
        GL11.glScaled(0.5, 0.5, 0.5);
        GlStateManager.color(0, 0, 0);
        GlStateManager.resetColor();
        glColor();
        GL11.glBegin(2);
        double ii = 0;
        while (ii < n) {
            GL11.glVertex2d(x + cx, y + cy);
            double t = x;
            x = p * x - s * y;
            y = s * t + p * y;
            ii++;
        }
        GL11.glEnd();
        GL11.glScaled(2.0, 2.0, 2.0);
        disableGL2D();
        
        GlStateManager.disableBlend();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.enableAlpha();
//        GlStateManager.popMatrix();
//        GL11.glPopMatrix();
        
        GlStateManager.color(1, 1, 1, 1);
        GL11.glPopMatrix();
    }
}
