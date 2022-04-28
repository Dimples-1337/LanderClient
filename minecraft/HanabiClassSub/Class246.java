package HanabiClassSub;


import com.mojang.authlib.GameProfile;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

public class Class246 {
   public static float delta;




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
      GL11.glColor3d(1.0D, 1.0D, 1.0D);
   }

   public static void drawArc(float var0, float var1, double var2, int var4, int var5, double var6, int var8) {
      var2 *= 2.0D;
      var0 *= 2.0F;
      var1 *= 2.0F;
      float var9 = (float)(var4 >> 24 & 255) / 255.0F;
      float var10 = (float)(var4 >> 16 & 255) / 255.0F;
      float var11 = (float)(var4 >> 8 & 255) / 255.0F;
      float var12 = (float)(var4 & 255) / 255.0F;
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glDepthMask(true);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glHint(3155, 4354);
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      GL11.glLineWidth((float)var8);
      GL11.glEnable(2848);
      GL11.glColor4f(var10, var11, var12, var9);
      GL11.glBegin(3);

      for(int var13 = var5; (double)var13 <= var6; ++var13) {
         double var14 = Math.sin((double)var13 * 3.141592653589793D / 180.0D) * var2;
         double var16 = Math.cos((double)var13 * 3.141592653589793D / 180.0D) * var2;
         GL11.glVertex2d((double)var0 + var14, (double)var1 + var16);
      }

      GL11.glEnd();
      GL11.glDisable(2848);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glHint(3155, 4352);
   }

   public static int rainbow(int var0) {
      double var1 = Math.ceil((double)(System.currentTimeMillis() + (long)var0) / 20.0D);
      var1 %= 360.0D;
      return Color.getHSBColor((float)(var1 / 360.0D), 0.8F, 0.7F).brighter().getRGB();
   }

   public static Color rainbowEffect(int var0) {
      float var1 = (float)(System.nanoTime() + (long)var0) / 2.0E10F % 1.0F;
      Color var2 = new Color((int)Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(var1, 1.0F, 1.0F))), 16));
      return new Color((float)var2.getRed() / 255.0F, (float)var2.getGreen() / 255.0F, (float)var2.getBlue() / 255.0F, (float)var2.getAlpha() / 255.0F);
   }

   public static void drawFullscreenImage(ResourceLocation var0) {
      ScaledResolution var1 = new ScaledResolution(Minecraft.getMinecraft());
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3008);
      Minecraft.getMinecraft().getTextureManager().bindTexture(var0);
      Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0F, 0.0F, var1.getScaledWidth(), var1.getScaledHeight(), (float)var1.getScaledWidth(), (float)var1.getScaledHeight());
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
      GL11.glEnable(3008);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void drawPlayerHead(String var0, int var1, int var2, int var3, int var4) {
      Iterator var5 = Minecraft.getMinecraft().world.playerEntities.iterator();

      while(var5.hasNext()) {
         EntityPlayer var6 = (EntityPlayer)var5.next();
         if (var0.equalsIgnoreCase(var6.getName())) {
            GameProfile var7 = new GameProfile(var6.getUniqueID(), var6.getName());
            NetworkPlayerInfo var8 = new NetworkPlayerInfo(var7);
            new ScaledResolution(Minecraft.getMinecraft());
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glDepthMask(false);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().getTextureManager().bindTexture(var8.getLocationSkin());
            Gui.drawModalRectWithCustomSizedTexture(var1, var2, 0.0F, 0.0F, var3, var4, (float)var3, (float)var4);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glEnable(2929);
         }
      }

   }

   public static double getAnimationState(double var0, double var2, double var4) {
      float var6 = (float)((double)delta * var4);
      if (var0 < var2) {
         if (var0 + (double)var6 < var2) {
            var0 += (double)var6;
         } else {
            var0 = var2;
         }
      } else if (var0 - (double)var6 > var2) {
         var0 -= (double)var6;
      } else {
         var0 = var2;
      }

      return var0;
   }

   public static void drawLoadingCircle() {
      float var0 = (float)((double)System.currentTimeMillis() * 0.1D % 400.0D);
      float var1 = 0.5F;
      ScaledResolution var2 = new ScaledResolution(Minecraft.getMinecraft());
      float var3 = (float)var2.getScaledWidth() / 16.0F;
      drawCircle((float)var2.getScaledWidth() / 2.0F, (float)var2.getScaledHeight() / 2.0F, var3, new Color(-8418163), 5.0F, 0.0F, 1.0F);
      drawCircle((float)var2.getScaledWidth() / 2.0F, (float)var2.getScaledHeight() / 2.0F, var3, new Color(-13330213), 7.0F, var0, var1);
   }

   public static void drawCircle(int var0, int var1, int var2, Color var3) {
      byte var4 = 70;
      double var5 = 6.283185307179586D / (double)var4;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glShadeModel(7425);
      GL11.glBegin(2);

      for(int var7 = 0; var7 < var4; ++var7) {
         float var8 = (float)((double)var2 * Math.cos((double)var7 * var5));
         float var9 = (float)((double)var2 * Math.sin((double)var7 * var5));
         GL11.glColor4f((float)var3.getRed() / 255.0F, (float)var3.getGreen() / 255.0F, (float)var3.getBlue() / 255.0F, (float)var3.getAlpha() / 255.0F);
         GL11.glVertex2f((float)var0 + var8, (float)var1 + var9);
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawCircle(float var0, float var1, float var2, Color var3) {
      byte var4 = 70;
      double var5 = 6.283185307179586D / (double)var4;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glLineWidth(1.0F);
      GL11.glShadeModel(7425);
      GL11.glBegin(2);

      for(int var7 = 0; var7 < var4; ++var7) {
         float var8 = (float)((double)var2 * Math.cos((double)var7 * var5));
         float var9 = (float)((double)var2 * Math.sin((double)var7 * var5));
         GL11.glColor4f((float)var3.getRed() / 255.0F, (float)var3.getGreen() / 255.0F, (float)var3.getBlue() / 255.0F, (float)var3.getAlpha() / 255.0F);
         GL11.glVertex2f(var0 + var8, var1 + var9);
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawCircle(float var0, float var1, float var2, Color var3, float var4, float var5, float var6) {
      byte var7 = 100;
      double var8 = (double)(var6 * 2.0F) * 3.141592653589793D / (double)var7;
      float var10 = 0.0F;
      float var11 = 0.0F;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glLineWidth(var4);
      GL11.glShadeModel(7425);
      GL11.glBegin(2);

      int var12;
      for(var12 = (int)var5; (float)var12 < var5 + (float)var7; ++var12) {
         var10 = (float)((double)var2 * Math.cos((double)var12 * var8));
         var11 = (float)((double)var2 * Math.sin((double)var12 * var8));
         GL11.glColor4f((float)var3.getRed() / 255.0F, (float)var3.getGreen() / 255.0F, (float)var3.getBlue() / 255.0F, (float)var3.getAlpha() / 255.0F);
         GL11.glVertex2f(var0 + var10, var1 + var11);
      }

      for(var12 = (int)(var5 + (float)var7); var12 > (int)var5; --var12) {
         var10 = (float)((double)var2 * Math.cos((double)var12 * var8));
         var11 = (float)((double)var2 * Math.sin((double)var12 * var8));
         GL11.glColor4f((float)var3.getRed() / 255.0F, (float)var3.getGreen() / 255.0F, (float)var3.getBlue() / 255.0F, (float)var3.getAlpha() / 255.0F);
         GL11.glVertex2f(var0 + var10, var1 + var11);
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawFilledCircle(int var0, int var1, float var2, Color var3) {
      byte var4 = 100;
      double var5 = 6.283185307179586D / (double)var4;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glBegin(6);

      for(int var7 = 0; var7 < var4; ++var7) {
         float var8 = (float)((double)var2 * Math.sin((double)var7 * var5));
         float var9 = (float)((double)var2 * Math.cos((double)var7 * var5));
         GL11.glColor4f((float)var3.getRed() / 255.0F, (float)var3.getGreen() / 255.0F, (float)var3.getBlue() / 255.0F, (float)var3.getAlpha() / 255.0F);
         GL11.glVertex2f((float)var0 + var8, (float)var1 + var9);
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawFilledCircle(float var0, float var1, float var2, Color var3) {
      byte var4 = 50;
      double var5 = 6.283185307179586D / (double)var4;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glBegin(6);

      for(int var7 = 0; var7 < var4; ++var7) {
         float var8 = (float)((double)var2 * Math.sin((double)var7 * var5));
         float var9 = (float)((double)var2 * Math.cos((double)var7 * var5));
         GL11.glColor4f((float)var3.getRed() / 255.0F, (float)var3.getGreen() / 255.0F, (float)var3.getBlue() / 255.0F, (float)var3.getAlpha() / 255.0F);
         GL11.glVertex2f(var0 + var8, var1 + var9);
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawFilledCircle(int var0, int var1, float var2, int var3) {
      float var4 = (float)(var3 >> 24 & 255) / 255.0F;
      float var5 = (float)(var3 >> 16 & 255) / 255.0F;
      float var6 = (float)(var3 >> 8 & 255) / 255.0F;
      float var7 = (float)(var3 & 255) / 255.0F;
      byte var8 = 50;
      double var9 = 6.283185307179586D / (double)var8;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glBlendFunc(770, 771);
      GL11.glBegin(6);

      for(int var11 = 0; var11 < var8; ++var11) {
         float var12 = (float)((double)var2 * Math.sin((double)var11 * var9));
         float var13 = (float)((double)var2 * Math.cos((double)var11 * var9));
         GL11.glColor4f(var5, var6, var7, var4);
         GL11.glVertex2f((float)var0 + var12, (float)var1 + var13);
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawFilledCircle(float var0, float var1, float var2, int var3) {
      float var4 = (float)(var3 >> 24 & 255) / 255.0F;
      float var5 = (float)(var3 >> 16 & 255) / 255.0F;
      float var6 = (float)(var3 >> 8 & 255) / 255.0F;
      float var7 = (float)(var3 & 255) / 255.0F;
      byte var8 = 50;
      double var9 = 6.283185307179586D / (double)var8;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glBegin(6);

      for(int var11 = 0; var11 < var8; ++var11) {
         float var12 = (float)((double)var2 * Math.sin((double)var11 * var9));
         float var13 = (float)((double)var2 * Math.cos((double)var11 * var9));
         GL11.glColor4f(var5, var6, var7, var4);
         GL11.glVertex2f(var0 + var12, var1 + var13);
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawFilledCircle(int var0, int var1, float var2, int var3, int var4, int var5, int var6, int var7) {
      float var8 = (float)(var3 >> 24 & 255) / 255.0F;
      float var9 = (float)(var3 >> 16 & 255) / 255.0F;
      float var10 = (float)(var3 >> 8 & 255) / 255.0F;
      float var11 = (float)(var3 & 255) / 255.0F;
      byte var12 = 50;
      double var13 = 6.283185307179586D / (double)var12;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glBegin(6);

      for(int var15 = 0; var15 < var12; ++var15) {
         float var16 = (float)((double)var2 * Math.sin((double)var15 * var13));
         float var17 = (float)((double)var2 * Math.cos((double)var15 * var13));
         float var18 = (float)var0 + var16;
         float var19 = (float)var1 + var17;
         if (var18 < (float)var4) {
            var18 = (float)var4;
         }

         if (var18 > (float)var6) {
            var18 = (float)var6;
         }

         if (var19 < (float)var5) {
            var19 = (float)var5;
         }

         if (var19 > (float)var7) {
            var19 = (float)var7;
         }

         GL11.glColor4f(var9, var10, var11, var8);
         GL11.glVertex2f(var18, var19);
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static String getShaderCode(InputStreamReader var0) {
      String var1 = "";

      try {
         String var2;
         BufferedReader var3;
         for(var3 = new BufferedReader(var0); (var2 = var3.readLine()) != null; var1 = var1 + var2 + "\n") {
         }

         var3.close();
      } catch (IOException var4) {
         var4.printStackTrace();
         System.exit(-1);
      }

      return var1.toString();
   }

   public static void drawImage(ResourceLocation var0, int var1, int var2, int var3, int var4) {
      drawImage(var0, var1, var2, var3, var4, 1.0F);
   }

   public static void drawImage(ResourceLocation var0, float var1, float var2, float var3, float var4) {
      drawImage(var0, (int)var1, (int)var2, (int)var3, (int)var4, 1.0F);
   }

   public static void drawImage(ResourceLocation var0, int var1, int var2, int var3, int var4, float var5) {
      new ScaledResolution(Minecraft.getMinecraft());
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDepthMask(false);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, var5);
      Minecraft.getMinecraft().getTextureManager().bindTexture(var0);
      Gui.drawModalRectWithCustomSizedTexture(var1, var2, 0.0F, 0.0F, var3, var4, (float)var3, (float)var4);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
   }

   public static void drawOutlinedRect(int var0, int var1, int var2, int var3, int var4, Color var5, Color var6) {
      drawRect((float)var0, (float)var1, (float)var2, (float)var3, var6.getRGB());
      drawRect((float)var0, (float)var1, (float)var2, (float)(var1 + var4), var5.getRGB());
      drawRect((float)var0, (float)(var3 - var4), (float)var2, (float)var3, var5.getRGB());
      drawRect((float)var0, (float)(var1 + var4), (float)(var0 + var4), (float)(var3 - var4), var5.getRGB());
      drawRect((float)(var2 - var4), (float)(var1 + var4), (float)var2, (float)(var3 - var4), var5.getRGB());
   }

   public static void drawOutlinedRect(float var0, float var1, float var2, float var3, float var4, int var5, int var6) {
      drawRect(var0, var1, var2, var3, var6);
      drawRect(var0, var1, var2, var1 + var4, var5);
      drawRect(var0, var3 - var4, var2, var3, var5);
      drawRect(var0, var1 + var4, var0 + var4, var3 - var4, var5);
      drawRect(var2 - var4, var1 + var4, var2, var3 - var4, var5);
   }

   public static void drawImage(ResourceLocation var0, int var1, int var2, int var3, int var4, Color var5) {
      new ScaledResolution(Minecraft.getMinecraft());
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDepthMask(false);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glColor4f((float)var5.getRed() / 255.0F, (float)var5.getBlue() / 255.0F, (float)var5.getRed() / 255.0F, 1.0F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(var0);
      Gui.drawModalRectWithCustomSizedTexture(var1, var2, 0.0F, 0.0F, var3, var4, (float)var3, (float)var4);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
   }

   public static void doGlScissor(int var0, int var1, int var2, int var3) {
      Minecraft var4 = Minecraft.getMinecraft();
      int var5 = 1;
      int var6 = var4.gameSettings.guiScale;
      if (var6 == 0) {
         var6 = 1000;
      }

      while(var5 < var6 && var4.displayWidth / (var5 + 1) >= 320 && var4.displayHeight / (var5 + 1) >= 240) {
         ++var5;
      }

      GL11.glScissor(var0 * var5, var4.displayHeight - (var1 + var3) * var5, var2 * var5, var3 * var5);
   }

   public static void drawRect(float var0, float var1, float var2, float var3, int var4) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glPushMatrix();
      color(var4);
      GL11.glBegin(7);
      GL11.glVertex2d((double)var2, (double)var1);
      GL11.glVertex2d((double)var0, (double)var1);
      GL11.glVertex2d((double)var0, (double)var3);
      GL11.glVertex2d((double)var2, (double)var3);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void color(int var0) {
      float var1 = (float)(var0 >> 24 & 255) / 255.0F;
      float var2 = (float)(var0 >> 16 & 255) / 255.0F;
      float var3 = (float)(var0 >> 8 & 255) / 255.0F;
      float var4 = (float)(var0 & 255) / 255.0F;
      GL11.glColor4f(var2, var3, var4, var1);
   }

   public static int createShader(String var0, int var1) throws Exception {
      byte var2 = 0;

      int var5;
      try {
         var5 = ARBShaderObjects.glCreateShaderObjectARB(var1);
         if (var5 == 0) {
            return 0;
         }
      } catch (Exception var4) {
         ARBShaderObjects.glDeleteObjectARB(var2);
         throw var4;
      }

      ARBShaderObjects.glShaderSourceARB(var5, var0);
      ARBShaderObjects.glCompileShaderARB(var5);
      if (ARBShaderObjects.glGetObjectParameteriARB(var5, 35713) == 0) {
         throw new RuntimeException("Error creating shader:");
      } else {
         return var5;
      }
   }

   public void drawCircle(int var1, int var2, float var3, int var4) {
      float var5 = (float)(var4 >> 24 & 255) / 255.0F;
      float var6 = (float)(var4 >> 16 & 255) / 255.0F;
      float var7 = (float)(var4 >> 8 & 255) / 255.0F;
      float var8 = (float)(var4 & 255) / 255.0F;
      boolean var9 = GL11.glIsEnabled(3042);
      boolean var10 = GL11.glIsEnabled(2848);
      boolean var11 = GL11.glIsEnabled(3553);
      if (!var9) {
         GL11.glEnable(3042);
      }

      if (!var10) {
         GL11.glEnable(2848);
      }

      if (var11) {
         GL11.glDisable(3553);
      }

      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(var6, var7, var8, var5);
      GL11.glBegin(9);

      for(int var12 = 0; var12 <= 360; ++var12) {
         GL11.glVertex2d((double)var1 + Math.sin((double)var12 * 3.141526D / 180.0D) * (double)var3, (double)var2 + Math.cos((double)var12 * 3.141526D / 180.0D) * (double)var3);
      }

      GL11.glEnd();
      if (var11) {
         GL11.glEnable(3553);
      }

      if (!var10) {
         GL11.glDisable(2848);
      }

      if (!var9) {
         GL11.glDisable(3042);
      }

   }

   public static void outlineOne() {
      GL11.glPushAttrib(1048575);
      GL11.glDisable(3008);
      GL11.glDisable(3553);
      GL11.glDisable(2896);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(4.0F);
      GL11.glEnable(2848);
      GL11.glEnable(2960);
      GL11.glClear(1024);
      GL11.glClearStencil(15);
      GL11.glStencilFunc(512, 1, 15);
      GL11.glStencilOp(7681, 7681, 7681);
      GL11.glPolygonMode(1032, 6913);
   }

   public static void outlineTwo() {
      GL11.glStencilFunc(512, 0, 15);
      GL11.glStencilOp(7681, 7681, 7681);
      GL11.glPolygonMode(1032, 6914);
   }

   public static void outlineThree() {
      GL11.glStencilFunc(514, 1, 15);
      GL11.glStencilOp(7680, 7680, 7680);
      GL11.glPolygonMode(1032, 6913);
   }

   public static void outlineFour() {
      GL11.glDepthMask(false);
      GL11.glDisable(2929);
      GL11.glEnable(10754);
      GL11.glPolygonOffset(1.0F, -2000000.0F);
      GL11.glColor4f(0.9529412F, 0.6117647F, 0.07058824F, 1.0F);
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
   }

   public static void outlineFive() {
      GL11.glPolygonOffset(1.0F, 2000000.0F);
      GL11.glDisable(10754);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(2960);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glEnable(3042);
      GL11.glEnable(2896);
      GL11.glEnable(3553);
      GL11.glEnable(3008);
      GL11.glPopAttrib();
   }

   public static void drawTracerLine(double var0, double var2, double var4, float var6, float var7, float var8, float var9, float var10) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3042);
      GL11.glLineWidth(var10);
      GL11.glColor4f(var6, var7, var8, var9);
      GL11.glBegin(2);
      GL11.glVertex3d(0.0D, 0.0D + (double)Minecraft.getMinecraft().player.getEyeHeight(), 0.0D);
      GL11.glVertex3d(var0, var2, var4);
      GL11.glEnd();
      GL11.glDisable(3042);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void checkSetupFBO() {
      Framebuffer var0 = Minecraft.getMinecraft().getFramebuffer();
      if (var0 != null && var0.depthBuffer > -1) {
         EXTFramebufferObject.glDeleteRenderbuffersEXT(var0.depthBuffer);
         int var1 = EXTFramebufferObject.glGenRenderbuffersEXT();
         EXTFramebufferObject.glBindRenderbufferEXT(36161, var1);
         EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
         EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, var1);
         EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, var1);
         var0.depthBuffer = -1;
      }

   }

   public static void drawRoundedRect(float var0, float var1, float var2, float var3, float var4, int var5) {
      var0 = (float)((double)var0 + (double)(var4 / 2.0F) + 0.5D);
      var1 = (float)((double)var1 + (double)(var4 / 2.0F) + 0.5D);
      var2 = (float)((double)var2 - ((double)(var4 / 2.0F) + 0.5D));
      var3 = (float)((double)var3 - ((double)(var4 / 2.0F) + 0.5D));
      drawRect(var0, var1, var2, var3, var5);
      circle(var2 - var4 / 2.0F, var1 + var4 / 2.0F, var4, var5);
      circle(var0 + var4 / 2.0F, var3 - var4 / 2.0F, var4, var5);
      circle(var0 + var4 / 2.0F, var1 + var4 / 2.0F, var4, var5);
      circle(var2 - var4 / 2.0F, var3 - var4 / 2.0F, var4, var5);
      drawRect(var0 - var4 / 2.0F - 0.5F, var1 + var4 / 2.0F, var2, var3 - var4 / 2.0F, var5);
      drawRect(var0, var1 + var4 / 2.0F, var2 + var4 / 2.0F + 0.5F, var3 - var4 / 2.0F, var5);
      drawRect(var0 + var4 / 2.0F, var1 - var4 / 2.0F - 0.5F, var2 - var4 / 2.0F, var3 - var4 / 2.0F, var5);
      drawRect(var0 + var4 / 2.0F, var1, var2 - var4 / 2.0F, var3 + var4 / 2.0F + 0.5F, var5);
   }

   public static void circle(float var0, float var1, float var2, int var3) {
      arc(var0, var1, 0.0F, 360.0F, var2, var3);
   }

   public static void circle(float var0, float var1, float var2, Color var3) {
      arc(var0, var1, 0.0F, 360.0F, var2, var3);
   }

   public static void arc(float var0, float var1, float var2, float var3, float var4, int var5) {
      arcEllipse(var0, var1, var2, var3, var4, var4, var5);
   }

   public static void arc(float var0, float var1, float var2, float var3, float var4, Color var5) {
      arcEllipse(var0, var1, var2, var3, var4, var4, var5);
   }

   public static void arcEllipse(float var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
      float var10 = 0.0F;
      if (var2 > var3) {
         var10 = var3;
         var3 = var2;
         var2 = var10;
      }

      float var11 = (float)(var6 >> 24 & 255) / 255.0F;
      float var12 = (float)(var6 >> 16 & 255) / 255.0F;
      float var13 = (float)(var6 >> 8 & 255) / 255.0F;
      float var14 = (float)(var6 & 255) / 255.0F;
      Tessellator var15 = Tessellator.getInstance();
      WorldRenderer var16 = var15.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(var12, var13, var14, var11);
      float var7;
      float var8;
      float var9;
      if (var11 > 0.5F) {
         GL11.glEnable(2848);
         GL11.glLineWidth(2.0F);
         GL11.glBegin(3);

         for(var9 = var3; var9 >= var2; var9 -= 4.0F) {
            var8 = (float)Math.cos((double)var9 * 3.141592653589793D / 180.0D) * var4 * 1.001F;
            var7 = (float)Math.sin((double)var9 * 3.141592653589793D / 180.0D) * var5 * 1.001F;
            GL11.glVertex2f(var0 + var8, var1 + var7);
         }

         GL11.glEnd();
         GL11.glDisable(2848);
      }

      GL11.glBegin(6);

      for(var9 = var3; var9 >= var2; var9 -= 4.0F) {
         var8 = (float)Math.cos((double)var9 * 3.141592653589793D / 180.0D) * var4;
         var7 = (float)Math.sin((double)var9 * 3.141592653589793D / 180.0D) * var5;
         GL11.glVertex2f(var0 + var8, var1 + var7);
      }

      GL11.glEnd();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void arcEllipse(float var0, float var1, float var2, float var3, float var4, float var5, Color var6) {
      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
      float var10 = 0.0F;
      if (var2 > var3) {
         var10 = var3;
         var3 = var2;
         var2 = var10;
      }

      Tessellator var11 = Tessellator.getInstance();
      WorldRenderer var12 = var11.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color((float)var6.getRed() / 255.0F, (float)var6.getGreen() / 255.0F, (float)var6.getBlue() / 255.0F, (float)var6.getAlpha() / 255.0F);
      float var7;
      float var8;
      float var9;
      if ((float)var6.getAlpha() > 0.5F) {
         GL11.glEnable(2848);
         GL11.glLineWidth(2.0F);
         GL11.glBegin(3);

         for(var9 = var3; var9 >= var2; var9 -= 4.0F) {
            var8 = (float)Math.cos((double)var9 * 3.141592653589793D / 180.0D) * var4 * 1.001F;
            var7 = (float)Math.sin((double)var9 * 3.141592653589793D / 180.0D) * var5 * 1.001F;
            GL11.glVertex2f(var0 + var8, var1 + var7);
         }

         GL11.glEnd();
         GL11.glDisable(2848);
      }

      GL11.glBegin(6);

      for(var9 = var3; var9 >= var2; var9 -= 4.0F) {
         var8 = (float)Math.cos((double)var9 * 3.141592653589793D / 180.0D) * var4;
         var7 = (float)Math.sin((double)var9 * 3.141592653589793D / 180.0D) * var5;
         GL11.glVertex2f(var0 + var8, var1 + var7);
      }

      GL11.glEnd();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void startDrawing() {
       GL11.glEnable((int)3042);
       GL11.glEnable((int)3042);
       GL11.glBlendFunc((int)770, (int)771);
       GL11.glEnable((int)2848);
       GL11.glDisable((int)3553);
       GL11.glDisable((int)2929);
       Minecraft.getMinecraft().entityRenderer.setupCameraTransform(Minecraft.getMinecraft().timer.renderPartialTicks, 0);
   }

   public static void stopDrawing() {
      GL11.glDisable(3042);
      GL11.glEnable(3553);
      GL11.glDisable(2848);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
   }

   public static void drawBorderedRect(float var0, float var1, float var2, float var3, float var4, int var5, int var6) {
      drawRect(var0, var1, var2, var3, var6);
      float var7 = (float)(var5 >> 24 & 255) / 255.0F;
      float var8 = (float)(var5 >> 16 & 255) / 255.0F;
      float var9 = (float)(var5 >> 8 & 255) / 255.0F;
      float var10 = (float)(var5 & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glPushMatrix();
      GL11.glColor4f(var8, var9, var10, var7);
      GL11.glLineWidth(var4);
      GL11.glBegin(1);
      GL11.glVertex2d((double)var0, (double)var1);
      GL11.glVertex2d((double)var0, (double)var3);
      GL11.glVertex2d((double)var2, (double)var3);
      GL11.glVertex2d((double)var2, (double)var1);
      GL11.glVertex2d((double)var0, (double)var1);
      GL11.glVertex2d((double)var2, (double)var1);
      GL11.glVertex2d((double)var0, (double)var3);
      GL11.glVertex2d((double)var2, (double)var3);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
   }

   public static Color blend(Color var0, Color var1, double var2) {
      float var4 = (float)var2;
      float var5 = 1.0F - var4;
      float[] var6 = new float[3];
      float[] var7 = new float[3];
      float[] var10000 = (float[])var0.getColorComponents(var6);
      var10000 = (float[])var1.getColorComponents(var7);
      Color var8 = new Color(var6[0] * var4 + var7[0] * var5, var6[1] * var4 + var7[1] * var5, var6[2] * var4 + var7[2] * var5);
      return var8;
   }
}
