/*
 * Decompiled with CFR 0_132.
 *
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package cn.Noble.Module.modules.RENDER;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import HanabiClassSub.LiquidRender;
import cn.Noble.Client;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventRender2D;
import cn.Noble.Event.events.EventRender3D;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Manager.FriendManager;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Module.modules.COMBAT.Aura;
import cn.Noble.Module.modules.PLAYER.Teams;
import cn.Noble.Util.Chat.Helper;
import cn.Noble.Util.math.Vec3f;
import cn.Noble.Util.render.RenderUtil;
import cn.Noble.Values.Mode;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;

public class ESP
		extends Module {
    private Numbers<Double> skeletonwidth;
    private static Map<EntityPlayer, float[][]> entities = new HashMap<EntityPlayer, float[][]>();
    private Option<Boolean> skeleton;
    public Mode<Enum> mode = new Mode<Enum>("mode", TwoD.values(), TwoD.Box3D);
    public Numbers<Double> r = new Numbers<Double>("Red", 255.0, 0.0, 255.0, 1.0);
    public Numbers<Double> g = new Numbers<Double>("Green", 255.0, 0.0, 255.0, 1.0);
    public Numbers<Double> b = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 1.0);
    public Numbers<Double> a = new Numbers<Double>("Alpha", 40.0, 0.0, 255.0, 1.0);
    public Option<Boolean> HEALTH = new Option<Boolean>("Health", false);
    public Option<Boolean> player = new Option<Boolean>("Players", true);
    public Option<Boolean> mobs = new Option<Boolean>("Mobs", false);
    public Option<Boolean> animals = new Option<Boolean>("Animals", false);
    public Option<Boolean> armorstand = new Option<Boolean>("ArmorStand", false);
    public Option<Boolean> antiinvis = new Option<Boolean>("AntiInvis", true);
    private Map<EntityLivingBase, double[]> entityConvertedPointsMap;
    
    public ESP() {
        super("ESP", new String[0], ModuleType.Render);
        this.skeletonwidth = new Numbers<Double>("SkeletonWidth", 1.0, 0.5, 10.0, 0.1);
        this.skeleton = new Option<Boolean>("Skeleton", false);
        this.addValues(mode, r, g, b, a, HEALTH, player, antiinvis, mobs, animals, armorstand, skeleton, skeletonwidth);
        this.entityConvertedPointsMap = new HashMap<EntityLivingBase, double[]>();
        ArrayList settings = new ArrayList();
    }
    
    public static void addEntity(EntityPlayer e, ModelPlayer model) {
        ESP.entities.put(e, new float[][] { { model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ }, { model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ }, { model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ }, { model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ }, { model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ } });
    }
    
    @EventHandler
    public void onRender(final EventRender3D event) {
        if (mode.getValue().equals(TwoD.Box2D)) {
            this.doOther2DESP(event);
        }
        for (final Entity entity : ESP.mc.world.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                final EntityLivingBase entityLiving = (EntityLivingBase)entity;
//                this.drawSkeleton(event, (EntityPlayer)entity);
                if (mode.getValue().equals(TwoD.Box3D) && qualifiesESP(entity)) {
                    if (FriendManager.isFriend(entity.getDisplayName().getUnformattedText())) {
                        LiquidRender.drawEntityBox(entity, new Color((entityLiving.hurtTime > 0) ? 255 : 0, (entityLiving.hurtTime > 0) ? 0 : 130, (entityLiving.hurtTime > 0) ? 0 : 255, a.getValue().intValue()), false);
                    }
                    else {
                        LiquidRender.drawEntityBox(entity, new Color((entityLiving.hurtTime > 0) ? 255 : r.getValue().intValue(), (entityLiving.hurtTime > 0) ? 0 : g.getValue().intValue(), (entityLiving.hurtTime > 0) ? 0 : b.getValue().intValue(), a.getValue().intValue()), false);
                    }
                }
                if (!mode.getValue().equals(TwoD.Box3D2) || !qualifiesESP(entity)) {
                    continue;
                }
                LiquidRender.drawEntityBox(entity, new Color((entityLiving.hurtTime > 0) ? 255 : r.getValue().intValue(), (entityLiving.hurtTime > 0) ? 0 : g.getValue().intValue(), (entityLiving.hurtTime > 0) ? 0 : b.getValue().intValue(), a.getValue().intValue()), true);
            }
        }
        try {
            this.updatePositions();
        }
        catch (Exception ex) {}
    }
    
    public static double getIncremental(final double val, final double inc) {
        final double one = 1.0 / inc;
        return Math.round(val * one) / one;
    }
    
    public static Color blendColors(final float[] fractions, final Color[] colors, final float progress) {
        final Object color = null;
        if (fractions == null) {
            throw new IllegalArgumentException("Fractions can't be null");
        }
        if (colors == null) {
            throw new IllegalArgumentException("Colours can't be null");
        }
        if (fractions.length != colors.length) {
            throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
        }
        final int[] indicies = getFractionIndicies(fractions, progress);
        final float[] range = { fractions[indicies[0]], fractions[indicies[1]] };
        final Color[] colorRange = { colors[indicies[0]], colors[indicies[1]] };
        final float max = range[1] - range[0];
        final float value = progress - range[0];
        final float weight = value / max;
        return blend(colorRange[0], colorRange[1], 1.0f - weight);
    }
    
    public static Color blend(final Color color1, final Color color2, final double ratio) {
        final float r = (float)ratio;
        final float ir = 1.0f - r;
        final float[] rgb1 = new float[3];
        final float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        }
        else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        }
        else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        }
        else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(red, green, blue);
        }
        catch (IllegalArgumentException exp) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            System.out.println(String.valueOf(String.valueOf(nf.format(red))) + "; " + nf.format(green) + "; " + nf.format(blue));
            exp.printStackTrace();
        }
        return color3;
    }
    
    public static int[] getFractionIndicies(float[] fractions, float progress) {
        final int[] range = new int[2];
        int startPoint;
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {}
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }
    
    @EventHandler
    public void onRender3D(EventRender3D e) {
        if (!this.skeleton.getValue()) {
            return;
        }
        this.startEnd(true);
        GL11.glEnable(2903);
        GL11.glDisable(2848);
        ESP.entities.keySet().removeIf(this::doesntContain);
        ESP.mc.world.playerEntities.forEach(player -> this.drawSkeleton(e, player));
        Gui.drawRect(0, 0, 0, 0, 0);
        this.startEnd(false);
    }
    
    private boolean doesntContain(EntityPlayer var0) {
        return !ESP.mc.world.playerEntities.contains(var0);
    }
    
    private Vec3 getVec3(final EventRender3D event, final EntityPlayer var0) {
        final float timer = event.getPartialTicks();
        final double x = var0.lastTickPosX + (var0.posX - var0.lastTickPosX) * timer;
        final double y = var0.lastTickPosY + (var0.posY - var0.lastTickPosY) * timer;
        final double z = var0.lastTickPosZ + (var0.posZ - var0.lastTickPosZ) * timer;
        return new Vec3(x, y, z);
    }
    
    private void drawSkeleton(final EventRender3D event, final EntityPlayer e) {
        Color color = new Color(FriendManager.isFriend(e.getDisplayName().getUnformattedText()) ? -8401409 : (e.getDisplayName().getUnformattedText().equals(mc.player.getName()) ? -6684775 : new Color(16775672).getRGB()));
        if (!e.isInvisible()) {
            final float[][] entPos = ESP.entities.get(e);
            if (entPos != null && e.isEntityAlive() && !e.isDead && e != mc.player && !e.isPlayerSleeping()) {
                GL11.glPushMatrix();
                GL11.glLineWidth(this.skeletonwidth.getValue().floatValue());
                GlStateManager.color((float)(color.getRed() / 255), (float)(color.getGreen() / 255), (float)(color.getBlue() / 255), 1.0f);
                final Vec3 vec = this.getVec3(event, e);
                final double x = vec.xCoord - mc.getRenderManager().renderPosX;
                final double y = vec.yCoord - mc.getRenderManager().renderPosY;
                final double z = vec.zCoord - mc.getRenderManager().renderPosZ;
                GL11.glTranslated(x, y, z);
                final float xOff = e.prevRenderYawOffset + (e.renderYawOffset - e.prevRenderYawOffset) * event.getPartialTicks();
                GL11.glRotatef(-xOff, 0.0f, 1.0f, 0.0f);
                GL11.glTranslated(0.0, 0.0, e.isSneaking() ? -0.235 : 0.0);
                final float yOff = e.isSneaking() ? 0.6f : 0.75f;
                GL11.glPushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glTranslated(-0.125, (double)yOff, 0.0);
                if (entPos[3][0] != 0.0f) {
                    GL11.glRotatef(entPos[3][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
                }
                if (entPos[3][1] != 0.0f) {
                    GL11.glRotatef(entPos[3][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
                }
                if (entPos[3][2] != 0.0f) {
                    GL11.glRotatef(entPos[3][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
                }
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, (double)(-yOff), 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glTranslated(0.125, (double)yOff, 0.0);
                if (entPos[4][0] != 0.0f) {
                    GL11.glRotatef(entPos[4][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
                }
                if (entPos[4][1] != 0.0f) {
                    GL11.glRotatef(entPos[4][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
                }
                if (entPos[4][2] != 0.0f) {
                    GL11.glRotatef(entPos[4][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
                }
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, (double)(-yOff), 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glTranslated(0.0, 0.0, e.isSneaking() ? 0.25 : 0.0);
                GL11.glPushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glTranslated(0.0, e.isSneaking() ? -0.05 : 0.0, e.isSneaking() ? -0.01725 : 0.0);
                GL11.glPushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glTranslated(-0.375, yOff + 0.55, 0.0);
                if (entPos[1][0] != 0.0f) {
                    GL11.glRotatef(entPos[1][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
                }
                if (entPos[1][1] != 0.0f) {
                    GL11.glRotatef(entPos[1][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
                }
                if (entPos[1][2] != 0.0f) {
                    GL11.glRotatef(-entPos[1][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
                }
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, -0.5, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glTranslated(0.375, yOff + 0.55, 0.0);
                if (entPos[2][0] != 0.0f) {
                    GL11.glRotatef(entPos[2][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
                }
                if (entPos[2][1] != 0.0f) {
                    GL11.glRotatef(entPos[2][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
                }
                if (entPos[2][2] != 0.0f) {
                    GL11.glRotatef(-entPos[2][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
                }
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, -0.5, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glRotatef(xOff - e.rotationYawHead, 0.0f, 1.0f, 0.0f);
                GL11.glPushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glTranslated(0.0, yOff + 0.55, 0.0);
                if (entPos[0][0] != 0.0f) {
                    GL11.glRotatef(entPos[0][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
                }
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, 0.3, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                GL11.glRotatef(e.isSneaking() ? 25.0f : 0.0f, 1.0f, 0.0f, 0.0f);
                GL11.glTranslated(0.0, e.isSneaking() ? -0.16175 : 0.0, e.isSneaking() ? -0.48025 : 0.0);
                GL11.glPushMatrix();
                GL11.glTranslated(0.0, (double)yOff, 0.0);
                GL11.glBegin(3);
                GL11.glVertex3d(-0.125, 0.0, 0.0);
                GL11.glVertex3d(0.125, 0.0, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glTranslated(0.0, (double)yOff, 0.0);
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, 0.55, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glTranslated(0.0, yOff + 0.55, 0.0);
                GL11.glBegin(3);
                GL11.glVertex3d(-0.375, 0.0, 0.0);
                GL11.glVertex3d(0.375, 0.0, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }
    
    private void startEnd(final boolean revert) {
        if (revert) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GL11.glEnable(2848);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.blendFunc(770, 771);
            GL11.glHint(3154, 4354);
        }
        else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GL11.glDisable(2848);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
        GlStateManager.depthMask(!revert);
    }
    
    private void updatePositions() {
        this.entityConvertedPointsMap.clear();
        final float pTicks = mc.timer.renderPartialTicks;
        for (final Entity e2 : mc.world.getLoadedEntityList()) {
            if (e2 instanceof EntityPlayer) {
                final EntityPlayer ent;
                if ((ent = (EntityPlayer)e2) == ESP.mc.player) {
                    continue;
                }
                double x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks - ESP.mc.getRenderManager().viewerPosX + 0.36;
                double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - ESP.mc.getRenderManager().viewerPosY;
                double z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks - ESP.mc.getRenderManager().viewerPosZ + 0.36;
                final double topY = y += ent.height + 0.15;
                final double[] convertedPoints = RenderUtil.convertTo2D(x, y, z);
                final double[] convertedPoints2 = RenderUtil.convertTo2D(x - 0.36, y, z - 0.36);
                final double xd = 0.0;
                if (convertedPoints2[2] < 0.0) {
                    continue;
                }
                if (convertedPoints2[2] >= 1.0) {
                    continue;
                }
                x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks - ESP.mc.getRenderManager().viewerPosX - 0.36;
                z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks - ESP.mc.getRenderManager().viewerPosZ - 0.36;
                final double[] convertedPointsBottom = RenderUtil.convertTo2D(x, y, z);
                y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - ESP.mc.getRenderManager().viewerPosY - 0.05;
                final double[] convertedPointsx = RenderUtil.convertTo2D(x, y, z);
                x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks - ESP.mc.getRenderManager().viewerPosX - 0.36;
                z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks - ESP.mc.getRenderManager().viewerPosZ + 0.36;
                final double[] convertedPointsTop1 = RenderUtil.convertTo2D(x, topY, z);
                final double[] convertedPointsx2 = RenderUtil.convertTo2D(x, y, z);
                x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks - ESP.mc.getRenderManager().viewerPosX + 0.36;
                z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks - ESP.mc.getRenderManager().viewerPosZ + 0.36;
                final double[] convertedPointsz = RenderUtil.convertTo2D(x, y, z);
                x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks - ESP.mc.getRenderManager().viewerPosX + 0.36;
                z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks - ESP.mc.getRenderManager().viewerPosZ - 0.36;
                final double[] convertedPointsTop2 = RenderUtil.convertTo2D(x, topY, z);
                final double[] convertedPointsz2 = RenderUtil.convertTo2D(x, y, z);
                this.entityConvertedPointsMap.put(ent, new double[] { convertedPoints[0], convertedPoints[1], 0.0, convertedPoints[2], convertedPointsBottom[0], convertedPointsBottom[1], convertedPointsBottom[2], convertedPointsx[0], convertedPointsx[1], convertedPointsx[2], convertedPointsx2[0], convertedPointsx2[1], convertedPointsx2[2], convertedPointsz[0], convertedPointsz[1], convertedPointsz[2], convertedPointsz2[0], convertedPointsz2[1], convertedPointsz2[2], convertedPointsTop1[0], convertedPointsTop1[1], convertedPointsTop1[2], convertedPointsTop2[0], convertedPointsTop2[1], convertedPointsTop2[2] });
            }
        }
    }
    
    public boolean qualifiesESP(final Entity e2) {
        return e2 != mc.player && !e2.equals(Aura.curTarget) && ((e2 instanceof EntityPlayer && player.getValue()) || (e2 instanceof EntityMob && mobs.getValue()) || (e2 instanceof EntityAnimal && animals.getValue()) || (e2 instanceof EntityVillager && animals.getValue()) || (e2 instanceof EntityArmorStand && armorstand.getValue()));
    }
    
    private void doOther2DESP(final EventRender3D e) {
        for (final EntityPlayer entity : mc.world.playerEntities) {
            if (qualifiesESP(entity)) {
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glDisable(2929);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.enableBlend();
                GL11.glBlendFunc(770, 771);
                GL11.glDisable(3553);
                final double renderPosX = mc.getRenderManager().viewerPosX;
                final double renderPosY = mc.getRenderManager().viewerPosY;
                final double renderPosZ = mc.getRenderManager().viewerPosZ;
                final float partialTicks = e.getPartialTicks();
                final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - renderPosX;
                final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - renderPosY;
                final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - renderPosZ;
                final float DISTANCE = mc.player.getDistanceToEntity(entity);
                Math.min(DISTANCE * 0.15f, 0.15f);
                float SCALE = 0.035f;
                SCALE /= 2.0f;
                entity.isChild();
                GlStateManager.translate((float)x, (float)y + entity.height + 0.5f - (entity.isChild() ? (entity.height / 2.0f) : 0.0f), (float)z);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                ESP.mc.getRenderManager();
                GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                GL11.glScalef(-SCALE, -SCALE, -SCALE);
                final Tessellator tesselator = Tessellator.getInstance();
                tesselator.getWorldRenderer();
                final float HEALTH = entity.getHealth();
                int COLOR = -1;
                if (HEALTH > 20.0) {
                    COLOR = -65292;
                }
                else if (HEALTH >= 10.0) {
                    COLOR = -16711936;
                }
                else if (HEALTH >= 3.0) {
                    COLOR = -23296;
                }
                else {
                    COLOR = -65536;
                }
                new Color(0, 0, 0);
                final double thickness = 1.5f + DISTANCE * 0.01f;
                final double xLeft = -20.0;
                final double xRight = 20.0;
                final double yUp = 27.0;
                final double yDown = 130.0;
                Color color = new Color(255, 255, 255);
                if (entity.hurtTime > 0) {
                    color = new Color(255, 0, 0);
                }
                else if (Teams.isOnSameTeam(entity)) {
                    color = new Color(0, 255, 0);
                }
                else if (entity.isInvisible()) {}
                drawBorderedRect((float)xLeft, (float)yUp, (float)xRight, (float)yDown, (float)thickness + 0.5f, Color.BLACK.getRGB(), 0);
                drawBorderedRect((float)xLeft, (float)yUp, (float)xRight, (float)yDown, (float)thickness, color.getRGB(), 0);
                drawBorderedRect((float)xLeft - 3.0f - DISTANCE * 0.2f, (float)yDown - (float)(yDown - yUp), (float)xLeft - 2.0f, (float)yDown, 0.15f, Color.BLACK.getRGB(), new Color(100, 100, 100).getRGB());
                drawBorderedRect((float)xLeft - 3.0f - DISTANCE * 0.2f, (float)yDown - (float)(yDown - yUp) * Math.min(1.0f, entity.getHealth() / 20.0f), (float)xLeft - 2.0f, (float)yDown, 0.15f, Color.BLACK.getRGB(), COLOR);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GlStateManager.disableBlend();
                GL11.glDisable(3042);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glNormal3f(1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
            }
        }
    }
    
    public static void drawBorderedRect(final float x, final float y, final float x2, final float y2, final float l1, final int col1, final int col2) {
        drawRect(x, y, x2, y2, col2);
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void drawRect(final float g, final float h, final float i, final float j, final int col1) {
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(7);
        GL11.glVertex2d((double)i, (double)h);
        GL11.glVertex2d((double)g, (double)h);
        GL11.glVertex2d((double)g, (double)j);
        GL11.glVertex2d((double)i, (double)j);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    private /* synthetic */ void lambda$onRender3D$0(final EventRender3D e, final EntityPlayer player) {
        this.drawSkeleton(e, player);
    }
    
    enum TwoD
    {
        Box3D, 
        Box3D2, 
        Box2D;
    }

}

