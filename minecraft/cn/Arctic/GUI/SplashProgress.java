package cn.Arctic.GUI;

import java.awt.Color;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import cn.Arctic.Client;
import cn.Arctic.Font.CFontRenderer;
import cn.Arctic.Font.FontLoaders;
import cn.Arctic.Font.me.superskidder.FontLoaders2;
import cn.Arctic.Util.AnimationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;

public class SplashProgress {

	private static final int MAX = 30;
	private static int PROGRESS;
	private static ResourceLocation splash;
	private static FontRenderer ufr;
	static float calcs;
    public static int alpha = 255;
    public static boolean glide;
	public static void update() {
		if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) {
			return;
		}
		drawSplash(Minecraft.getTextureManager());
	}

	public static void setProgress(int givenProgress) {
		PROGRESS = givenProgress;
		update();
	}

	public static void drawSplash(TextureManager tm) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		int scaleFactor = scaledResolution.getScaleFactor();

		Framebuffer framebuffer = new Framebuffer(ScaledResolution.getScaledWidth() * scaleFactor, ScaledResolution.getScaledHeight() * scaleFactor, true);
		framebuffer.bindFramebuffer(false);

		GlStateManager.matrixMode(GL11.GL_PROJECTION);
		GlStateManager.loadIdentity();
		GlStateManager.ortho(0.0D, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.loadIdentity();
		GlStateManager.translate(0.0F, 0.0F, -2000.0F);
		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		GlStateManager.disableDepth();
		GlStateManager.enableTexture2D();

		if (splash == null) {
			splash = new ResourceLocation("Lander1/Lander.png");
		}
		tm.bindTexture(splash);
		GlStateManager.resetColor();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, Display.getWidth(), Display.getHeight(), ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), Display.getWidth(), Display.getHeight());
		drawProgress();
		framebuffer.unbindFramebuffer();
		framebuffer.framebufferRender(ScaledResolution.getScaledWidth() * scaleFactor, ScaledResolution.getScaledHeight() * scaleFactor);
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(516, 0.1F);
		Minecraft.getMinecraft().updateDisplay();

	}

	private static void drawProgress() {
		if (Minecraft.getMinecraft().gameSettings == null || Minecraft.getMinecraft().getTextureManager() == null) {
			return;
		}

		if (ufr == null) {
			ufr = FontLoaders2.msFont30;
		}
		double nProgress = PROGRESS;
		float calc = (float) ((nProgress / MAX) * 150);
		calcs = (float) AnimationUtil.getAnimationState(calcs, calc, 750);//750
		Gui.drawFilledCircleq(ScaledResolution.getScaledWidth() / 2 - 75, ScaledResolution.getScaledHeight() / 2 + 35, 5, new Color(255,255,255,alpha));
		Gui.drawFilledCircleq(ScaledResolution.getScaledWidth() / 2 + 75, ScaledResolution.getScaledHeight() / 2 + 35, 5, new Color(255,255,255,alpha));
		Gui.drawRect(ScaledResolution.getScaledWidth() / 2 - 75, ScaledResolution.getScaledHeight() / 2 + 30, ScaledResolution.getScaledWidth() / 2 + 75, ScaledResolution.getScaledHeight() / 2 + 40, new Color(255,255,255,alpha).getRGB());
		Gui.drawFilledCircleq(ScaledResolution.getScaledWidth() / 2 - 75, ScaledResolution.getScaledHeight() / 2 + 35, 4, new Color(0,0,0));
		Gui.drawFilledCircleq(ScaledResolution.getScaledWidth() / 2 + 75, ScaledResolution.getScaledHeight() / 2 + 35, 4, new Color(0,0,0));
		Gui.drawRect(ScaledResolution.getScaledWidth() / 2 - 75, ScaledResolution.getScaledHeight() / 2 + 31, ScaledResolution.getScaledWidth() / 2 + 75, ScaledResolution.getScaledHeight() / 2 + 39, new Color(0, 0, 0).getRGB());

		Gui.drawRect(0, 0, 0, 0, 0);
		ufr.drawString(Client.name, ScaledResolution.getScaledWidth() / 2 - ufr.getStringWidth(Client.name) / 2 - 3, ScaledResolution.getScaledHeight() / 2 - 20, new Color(255,255,255,alpha).getRGB());
		Gui.drawRect(0, 0, 0, 0, 0);

		GlStateManager.resetColor(); 
		GlStateManager.resetColor();
		Gui.drawFilledCircleq(ScaledResolution.getScaledWidth() / 2 - 75 + calcs * 3 - 15, ScaledResolution.getScaledHeight() / 2 + 35, 3, new Color(255,255,255,alpha));//Load
		Gui.drawFilledCircleq(ScaledResolution.getScaledWidth() / 2 - 75, ScaledResolution.getScaledHeight() / 2 + 35, 3, new Color(255,255,255,alpha));
		Gui.drawRect(ScaledResolution.getScaledWidth() / 2 - 75, ScaledResolution.getScaledHeight() / 2 + 32, ScaledResolution.getScaledWidth() / 2 - 75 + calcs * 3 - 15, ScaledResolution.getScaledHeight() / 2 + 38, new Color(255, 255, 255,alpha).getRGB());
		if(glide && alpha > 1){
			alpha -= 20;
		}
	}


}
