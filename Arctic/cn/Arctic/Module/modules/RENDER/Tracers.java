/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package cn.Arctic.Module.modules.RENDER;

import java.awt.Color;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventRender3D;
import cn.Arctic.Manager.FriendManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.math.MathUtil;
import cn.Arctic.Util.render.RenderUtil;

public class Tracers
extends Module {
    public Tracers() {
        super("Tracers", new String[]{"lines", "tracer"}, ModuleType.Render);
        this.setColor(new Color(60, 136, 166).getRGB());
    }
    @Override
    public void onEnable() {
    	// TODO Auto-generated method stub
    	super.onEnable();
    }
public void onDisable() {
	// TODO Auto-generated method stub
	super.onDisable();
}
    @EventHandler
    private void on3DRender(EventRender3D e) {
        for (Object o : this.mc.world.loadedEntityList) {
            double[] arrd;
            Entity entity = (Entity)o;
            if (!entity.isEntityAlive() || !(entity instanceof EntityPlayer) || entity == this.mc.player) continue;
            double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)e.getPartialTicks() -  mc.getRenderManager().renderPosX;
            double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)e.getPartialTicks() -  mc.getRenderManager().renderPosY;
            double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)e.getPartialTicks() -  mc.getRenderManager().renderPosZ;
            boolean old = this.mc.gameSettings.viewBobbing;
            RenderUtil.startDrawing();
            this.mc.gameSettings.viewBobbing = false;
            this.mc.entityRenderer.setupCameraTransform(this.mc.timer.renderPartialTicks, 2);
            this.mc.gameSettings.viewBobbing = old;
            float color = (float)Math.round(255.0 - this.mc.player.getDistanceSqToEntity(entity) * 255.0 / MathUtil.square((double)this.mc.gameSettings.renderDistanceChunks * 2.5)) / 255.0f;
            if (FriendManager.isFriend(entity.getDisplayName().getUnformattedText())) {
                double[] arrd2 = new double[3];
                arrd2[0] = 0.0;
                arrd2[1] = 1.0;
                arrd = arrd2;
                arrd2[2] = 1.0;
            } else {
                double[] arrd3 = new double[3];
                arrd3[0] = color;
                arrd3[1] = 1.0f - color;
                arrd = arrd3;
                arrd3[2] = 0.0;
            }
            this.drawLine(entity, arrd, posX, posY, posZ);
            RenderUtil.stopDrawing();
        }
    }

    private void drawLine(Entity entity, double[] color, double x, double y, double z) {
        float distance = this.mc.player.getDistanceToEntity(entity);
        float xD = distance / 48.0f;
        if (xD >= 1.0f) {
            xD = 1.0f;
        }
        boolean entityesp = false;
        GL11.glEnable((int)2848);
        if (color.length >= 4) {
            if (color[3] <= 0.1) {
                return;
            }
            GL11.glColor4d((double)color[0], (double)color[1], (double)color[2], (double)color[3]);
        } else {
            GL11.glColor3d((double)color[0], (double)color[1], (double)color[2]);
        }
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)1);
        GL11.glVertex3d((double)0.0, (double)this.mc.player.getEyeHeight(), (double)0.0);
        GL11.glVertex3d((double)x, (double)y, (double)z);
        GL11.glEnd();
        GL11.glDisable((int)2848);
    }
}

