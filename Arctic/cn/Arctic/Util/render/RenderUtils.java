package cn.Arctic.Util.render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import com.mojang.authlib.GameProfile;

import cn.Arctic.Util.Wrapper;
import cn.Arctic.Util.math.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderUtils {
    private static final Frustum frustum = new Frustum();
    public static float delta;
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static double interpolate(double newPos, double oldPos) {
        return oldPos + (newPos - oldPos) * (double)Wrapper.mc.timer.renderPartialTicks;
    }
   
    public static void drawRoundRect(double d, double e, double g, double h, int color) {
        drawRect(d + 1, e, g - 1, h, color);
        drawRect(d, e + 1, d + 1, h - 1, color);
        drawRect(d + 1, e + 1, d + 0.5, e + 0.5, color);
        drawRect(d + 1, e + 1, d + 0.5, e + 0.5, color);
        drawRect(g - 1, e + 1, g - 0.5, e + 0.5, color);
        drawRect(g - 1, e + 1, g, h - 1, color);
        drawRect(d + 1, h - 1, d + 0.5, h - 0.5, color);
        drawRect(g - 1, h - 1, g - 0.5, h - 0.5, color);
    }
    public static void drawPlayerHead(String playerName, int x, int y, int width, int height) {
        for (Object player : Minecraft.getMinecraft().world.getLoadedEntityList()) {
            if (player instanceof EntityPlayer) {
                EntityPlayer ply = (EntityPlayer) player;
                if (playerName.equalsIgnoreCase(ply.getName())) {
                    GameProfile profile = new GameProfile(ply.getUniqueID(), ply.getName());
                    NetworkPlayerInfo networkplayerinfo1 = new NetworkPlayerInfo(profile);
                    new ScaledResolution(Minecraft.getMinecraft());
                    GL11.glDisable((int) 2929);
                    GL11.glEnable((int) 3042);
                    GL11.glDepthMask((boolean) false);
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                    GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
                    if (ply.isWearing(EnumPlayerModelParts.HAT)) {
                        Gui.drawScaledCustomSizeModalRect(x, y, width, height, width, height, width * 4, height * 4, width * 8, height * 8);//drawScaledCustomSizeModalRect
                    }
                    GL11.glDepthMask((boolean) true);
                    GL11.glDisable((int) 3042);
                    GL11.glEnable((int) 2929);
                }
            }
        }
    }

	public static void drawFilledCircle(double x, double y, double r, int c, int id) {
		float f = (float) (c >> 24 & 0xff) / 255F;
		float f1 = (float) (c >> 16 & 0xff) / 255F;
		float f2 = (float) (c >> 8 & 0xff) / 255F;
		float f3 = (float) (c & 0xff) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(GL11.GL_POLYGON);
		if (id == 1) {
			GL11.glVertex2d(x, y);
			for (int i = 0; i <= 90; i++) {
				double x2 = Math.sin((i * 3.141526D / 180)) * r;
				double y2 = Math.cos((i * 3.141526D / 180)) * r;
				GL11.glVertex2d(x - x2, y - y2);
			}
		} else if (id == 2) {
			GL11.glVertex2d(x, y);
			for (int i = 90; i <= 180; i++) {
				double x2 = Math.sin((i * 3.141526D / 180)) * r;
				double y2 = Math.cos((i * 3.141526D / 180)) * r;
				GL11.glVertex2d(x - x2, y - y2);
			}
		} else if (id == 3) {
			GL11.glVertex2d(x, y);
			for (int i = 270; i <= 360; i++) {
				double x2 = Math.sin((i * 3.141526D / 180)) * r;
				double y2 = Math.cos((i * 3.141526D / 180)) * r;
				GL11.glVertex2d(x - x2, y - y2);
			}
		} else if (id == 4) {
			GL11.glVertex2d(x, y);
			for (int i = 180; i <= 270; i++) {
				double x2 = Math.sin((i * 3.141526D / 180)) * r;
				double y2 = Math.cos((i * 3.141526D / 180)) * r;
				GL11.glVertex2d(x - x2, y - y2);
			}
		} else {
			for (int i = 0; i <= 360; i++) {
				double x2 = Math.sin((i * 3.141526D / 180)) * r;
				double y2 = Math.cos((i * 3.141526D / 180)) * r;
				GL11.glVertex2f((float) (x - x2), (float) (y - y2));
			}
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}



    public static int getRandomRGB(double min, double max, float alpha) {
        return new Color((float) MathUtil.randomDouble(min, max), (float)MathUtil.randomDouble(min, max), (float)MathUtil.randomDouble(min, max), alpha).getRGB();
    }
    
    public static void resetColor() {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawModalRectWithCustomSizedTexture(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        float f = 1.0f / textureWidth;
        float f1 = 1.0f / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0).tex(u * f, (v + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0).tex((u + width) * f, (v + height) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0.0).tex((u + width) * f, v * f1).endVertex();
        worldrenderer.pos(x, y, 0.0).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }
    
    public static void drawImage(ResourceLocation image, double x, double y, double width, double height, int color) {
        GlStateManager.pushMatrix();
        resetColor();
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        Color color1 = new Color(color);
        GL11.glColor4f((float)color1.getRed() / 255.0f, (float)color1.getGreen() / 255.0f, (float)color1.getBlue() / 255.0f, (float)color1.getAlpha() / 255.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        RenderUtils.drawModalRectWithCustomSizedTexture((float)x, (float)y, 0.0f, 0.0f, (float)width, (float)height, (float)width, (float)height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GlStateManager.popMatrix();
    }

    public static int withTransparency(int rgb, float alpha) {
        float r2 = (float)(rgb >> 16 & 255) / 255.0f;
        float g2 = (float)(rgb >> 8 & 255) / 255.0f;
        float b2 = (float)(rgb >> 0 & 255) / 255.0f;
        return new Color(r2, g2, b2, alpha).getRGB();
    }

    public static int getHexRGB(int hex) {
        return -16777216 | hex;
    }
    
    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return RenderUtils.isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    private static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustum.setPosition(current.posX, current.posY, current.posZ);
        return frustum.isBoundingBoxInFrustum(bb);
    }


    public static void pre() {
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
    }

    public static void post() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glColor3d(1.0, 1.0, 1.0);
    }
    public static void drawRoundedRect(float x, float y, float x2, float y2, final float round, final int color) {
        x += (float)(round / 2.0f + 0.5);
        y += (float)(round / 2.0f + 0.5);
        x2 -= (float)(round / 2.0f + 0.5);
        y2 -= (float)(round / 2.0f + 0.5);
        Gui.drawRect((int)x, (int)y, (int)x2, (int)y2, color);
        circle(x2 - round / 2.0f, y + round / 2.0f, round, color);
        circle(x + round / 2.0f, y2 - round / 2.0f, round, color);
        circle(x + round / 2.0f, y + round / 2.0f, round, color);
        circle(x2 - round / 2.0f, y2 - round / 2.0f, round, color);
        Gui.drawRect((int)(x - round / 2.0f - 0.5f), (int)(y + round / 2.0f), (int)x2, (int)(y2 - round / 2.0f), color);
        Gui.drawRect((int)x, (int)(y + round / 2.0f), (int)(x2 + round / 2.0f + 0.5f), (int)(y2 - round / 2.0f), color);
        Gui.drawRect((int)(x + round / 2.0f), (int)(y - round / 2.0f - 0.5f), (int)(x2 - round / 2.0f), (int)(y2 - round / 2.0f), color);
        Gui.drawRect((int)(x + round / 2.0f), (int)y, (int)(x2 - round / 2.0f), (int)(y2 + round / 2.0f + 0.5f), color);
    }
    public static void circle(final float x, final float y, final float radius, final int fill) {
        arc(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void circle(final float x, final float y, final float radius, final Color fill) {
        arc(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void arc(final float x, final float y, final float start, final float end, final float radius, final int color) {
        arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void arc(final float x, final float y, final float start, final float end, final float radius, final Color color) {
        arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void arcEllipse(final float x, final float y, float start, float end, final float w, final float h, final int color) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        float temp = 0.0f;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        final Tessellator var15 = Tessellator.getInstance();
        final WorldRenderer var16 = var15.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var12, var13, var14, var11);
        if (var11 > 0.5f) {
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (float i = end; i >= start; i -= 4.0f) {
                final float ldx = (float)Math.cos(i * 3.141592653589793 / 180.0) * w * 1.001f;
                final float ldy = (float)Math.sin(i * 3.141592653589793 / 180.0) * h * 1.001f;
                GL11.glVertex2f(x + ldx, y + ldy);
            }
            GL11.glEnd();
            GL11.glDisable(2848);
        }
        GL11.glBegin(6);
        for (float i = end; i >= start; i -= 4.0f) {
            final float ldx = (float)Math.cos(i * 3.141592653589793 / 180.0) * w;
            final float ldy = (float)Math.sin(i * 3.141592653589793 / 180.0) * h;
            GL11.glVertex2f(x + ldx, y + ldy);
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    public static double getAnimationState(double animation, final double finalState, final double speed) {
        final float add = (float) (RenderUtils.delta * speed);
        if (animation < finalState) {
            if (animation + add < finalState) {
                animation += add;
            } else {
                animation = finalState;
            }
        } else if (animation - add > finalState) {
            animation -= add;
        } else {
            animation = finalState;
        }
        return animation;
    }

    public static void arcEllipse(final float x, final float y, float start, float end, final float w, final float h, final Color color) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        float temp = 0.0f;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        final Tessellator var9 = Tessellator.getInstance();
        final WorldRenderer var10 = var9.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        if (color.getAlpha() > 0.5f) {
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (float i = end; i >= start; i -= 4.0f) {
                final float ldx = (float)Math.cos(i * 3.141592653589793 / 180.0) * w * 1.001f;
                final float ldy = (float)Math.sin(i * 3.141592653589793 / 180.0) * h * 1.001f;
                GL11.glVertex2f(x + ldx, y + ldy);
            }
            GL11.glEnd();
            GL11.glDisable(2848);
        }
        GL11.glBegin(6);
        for (float i = end; i >= start; i -= 4.0f) {
            final float ldx = (float)Math.cos(i * 3.141592653589793 / 180.0) * w;
            final float ldy = (float)Math.sin(i * 3.141592653589793 / 180.0) * h;
            GL11.glVertex2f(x + ldx, y + ldy);
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawBordered(double x2, double y2, double width, double height, double length, int innerColor, int outerColor) {
        Gui.drawRect(x2, y2, x2 + width, y2 + height, innerColor);
        Gui.drawRect(x2 - length, y2, x2, y2 + height, outerColor);
        Gui.drawRect(x2 - length, y2 - length, x2 + width, y2, outerColor);
        Gui.drawRect(x2 + width, y2 - length, x2 + width + length, y2 + height + length, outerColor);
        Gui.drawRect(x2 - length, y2 + height, x2 + width, y2 + height + length, outerColor);
    }
    public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.glBlendFunc((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height, Color color) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.glBlendFunc((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getRed() / 255.0f), (float)1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }
    public static void drawBordered1(double x2, double y2, double width, double height, double length, int innerColor, int outerColor) {
        Gui.drawRect(x2, y2, x2 + width, y2 + height, innerColor);
        Gui.drawRect(x2, y2, x2, y2, outerColor);
    }
  

    public static boolean isInFrustumView(Entity ent) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        double x2 = RenderUtils.interpolate(current.posX, current.lastTickPosX);
        double y2 = RenderUtils.interpolate(current.posY, current.lastTickPosY);
        double z2 = RenderUtils.interpolate(current.posZ, current.lastTickPosZ);
        frustum.setPosition(x2, y2, z2);
        if (!frustum.isBoundingBoxInFrustum(ent.getEntityBoundingBox()) && !ent.ignoreFrustumCheck) {
            return false;
        }
        return true;
    }

    public static final ScaledResolution getScaledRes() {
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        return scaledRes;
    }

    public static void rectangle(double left, double top, double right, double bottom, int color) {
        double var5;
        if (left < right) {
            var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            var5 = top;
            top = bottom;
            bottom = var5;
        }
        float var6 = (float)(color >> 24 & 255) / 255.0f;
        float var7 = (float)(color >> 16 & 255) / 255.0f;
        float var8 = (float)(color >> 8 & 255) / 255.0f;
        float var9 = (float)(color & 255) / 255.0f;
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var7, var8, var9, var6);
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(left, bottom, 0.0);
        worldRenderer.addVertex(right, bottom, 0.0);
        worldRenderer.addVertex(right, top, 0.0);
        worldRenderer.addVertex(left, top, 0.0);
        Tessellator.getInstance().draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void disableLighting() {
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(3553);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
    }

    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void drawGradientRect(float x2, float y2, float x1, float y1, int topColor, int bottomColor) {
        RenderUtils.enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        RenderUtils.glColor(topColor);
        GL11.glVertex2f(x2, y1);
        GL11.glVertex2f(x1, y1);
        RenderUtils.glColor(bottomColor);
        GL11.glVertex2f(x1, y2);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        RenderUtils.disableGL2D();
    }

    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 255) / 255.0f;
        float red = (float)(hex >> 16 & 255) / 255.0f;
        float green = (float)(hex >> 8 & 255) / 255.0f;
        float blue = (float)(hex & 255) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void drawGradientBordere(float x2, float y2, float x1, float y1, float lineWidth, int border, int bottom, int top) {
        RenderUtils.enableGL2D();
        RenderUtils.drawGradientRect(x2, y2, x1, y1, top, bottom);
        RenderUtils.glColor(border);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x2, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y2);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        RenderUtils.disableGL2D();
    }

    public static void drawBorderedRect(float x2, float y2, float x22, float y22, float l1, int col1, int col2) {
        RenderUtils.drawRect(x2, y2, x22, y22, col2);
        float f2 = (float)(col1 >> 24 & 255) / 255.0f;
        float f22 = (float)(col1 >> 16 & 255) / 255.0f;
        float f3 = (float)(col1 >> 8 & 255) / 255.0f;
        float f4 = (float)(col1 & 255) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glColor4f(f22, f3, f4, f2);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y22);
        GL11.glVertex2d(x22, y22);
        GL11.glVertex2d(x22, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x22, y2);
        GL11.glVertex2d(x2, y22);
        GL11.glVertex2d(x22, y22);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    

    public static void drawRect(double d, double y, double e, double y2, int col1) {
        float f2 = (float)(col1 >> 24 & 255) / 255.0f;
        float f22 = (float)(col1 >> 16 & 255) / 255.0f;
        float f3 = (float)(col1 >> 8 & 255) / 255.0f;
        float f4 = (float)(col1 & 255) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glColor4f(f22, f3, f4, f2);
        GL11.glBegin(7);
        GL11.glVertex2d(e, y);
        GL11.glVertex2d(d, y);
        GL11.glVertex2d(d, y2);
        GL11.glVertex2d(e, y2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static class R3DUtils {
        public static void startDrawing() {
            GL11.glEnable(3042);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            Wrapper.mc.entityRenderer.setupCameraTransform(Wrapper.mc.timer.renderPartialTicks, 0);
        }

        public static void stopDrawing() {
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glDisable(2848);
            GL11.glDisable(3042);
            GL11.glEnable(2929);
        }

        public static void drawOutlinedBox(AxisAlignedBB box2) {
            if (box2 == null) {
                return;
            }
            Wrapper.mc.entityRenderer.setupCameraTransform(Wrapper.mc.timer.renderPartialTicks, 0);
            GL11.glBegin(3);
            GL11.glVertex3d(box2.minX, box2.minY, box2.minZ);
            GL11.glVertex3d(box2.maxX, box2.minY, box2.minZ);
            GL11.glVertex3d(box2.maxX, box2.minY, box2.maxZ);
            GL11.glVertex3d(box2.minX, box2.minY, box2.maxZ);
            GL11.glVertex3d(box2.minX, box2.minY, box2.minZ);
            GL11.glEnd();
            GL11.glBegin(3);
            GL11.glVertex3d(box2.minX, box2.maxY, box2.minZ);
            GL11.glVertex3d(box2.maxX, box2.maxY, box2.minZ);
            GL11.glVertex3d(box2.maxX, box2.maxY, box2.maxZ);
            GL11.glVertex3d(box2.minX, box2.maxY, box2.maxZ);
            GL11.glVertex3d(box2.minX, box2.maxY, box2.minZ);
            GL11.glEnd();
            GL11.glBegin(1);
            GL11.glVertex3d(box2.minX, box2.minY, box2.minZ);
            GL11.glVertex3d(box2.minX, box2.maxY, box2.minZ);
            GL11.glVertex3d(box2.maxX, box2.minY, box2.minZ);
            GL11.glVertex3d(box2.maxX, box2.maxY, box2.minZ);
            GL11.glVertex3d(box2.maxX, box2.minY, box2.maxZ);
            GL11.glVertex3d(box2.maxX, box2.maxY, box2.maxZ);
            GL11.glVertex3d(box2.minX, box2.minY, box2.maxZ);
            GL11.glVertex3d(box2.minX, box2.maxY, box2.maxZ);
            GL11.glEnd();
        }

        public static void drawBoundingBox(AxisAlignedBB aabb) {
            WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
            Tessellator tessellator = Tessellator.getInstance();
            Wrapper.mc.entityRenderer.setupCameraTransform(Wrapper.mc.timer.renderPartialTicks, 0);
            worldRenderer.startDrawingQuads();
            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
            tessellator.draw();
            worldRenderer.startDrawingQuads();
            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
            tessellator.draw();
            worldRenderer.startDrawingQuads();
            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
            tessellator.draw();
            worldRenderer.startDrawingQuads();
            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
            tessellator.draw();
            worldRenderer.startDrawingQuads();
            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
            tessellator.draw();
            worldRenderer.startDrawingQuads();
            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
            worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
            worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
            worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
            worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
            tessellator.draw();
        }
    }
    public static void prepareScissorBox(float x, float y, float x2, float y2) {

        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        GL11.glScissor((int)((int)(x * (float)factor)), (int)((int)(((float)scale.getScaledHeight() - y2) * (float)factor)), (int)((int)((x2 - x) * (float)factor)), (int)((int)((y2 - y) * (float)factor)));
    }
    public static  void drawRainbowRect(double x, double y, double x2, double y2) {
        double width = (x2 - x);
        for (int i = 0; i <= (width); i++) {
            Color color = new Color(Color.HSBtoRGB((float)((Wrapper.getPlayer().ticksExisted / width)/2 + (Math.sin(i / width * 0.6))) % 1.0f, 0.5f, 1.0f));
            drawRect(x+i, y, x+i+1,y2,color.getRGB());
        }
    }
    public static void enableGUIStandardItemLighting() {
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        GlStateManager.enableLighting();
    }
    public static void disableGUIStandardItemLighting() {
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
    }







    


}