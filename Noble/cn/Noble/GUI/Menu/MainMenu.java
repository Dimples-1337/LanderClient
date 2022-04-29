package cn.Noble.GUI.Menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.spectator.PlayerMenuObject;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import viamcp.ViaMCP;
import viamcp.gui.GuiProtocolSelector;
import viamcp.protocols.ProtocolCollection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;

import cn.Noble.Client;
import cn.Noble.Font.CFontRenderer;
import cn.Noble.Font.FontLoaders;
import cn.Noble.Font.me.superskidder.FontLoaders2;
import cn.Noble.GUI.Buttons.ClientButton;
import cn.Noble.GUI.Buttons.FlatButton;
import cn.Noble.GUI.Buttons.SimpleButton;
import cn.Noble.GUI.login.GuiAltManager;
import cn.Noble.GUI.particle.MenuParticle;
import cn.Noble.GUI.particle.ParticleUtils;
import cn.Noble.Util.Colors;
import cn.Noble.Util.Timer.TimeHelper;
import cn.Noble.Util.Timer.TimerUtil;
import cn.Noble.Util.animate.Animation;
import cn.Noble.Util.render.RenderUtil;
import cn.Noble.Util.render.RenderUtils;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MainMenu extends GuiScreen implements GuiYesNoCallback {

	private TimerUtil restTimer = new TimerUtil();
	
	private ArrayList<MenuParticle> particles = new ArrayList<MenuParticle>();
	private Random RANDOM = new Random();
	private int particleCount = 7000;

//	@Override
//	public void initGui() {
//		super.initGui();
//
//		Client.shouldShaderBack = true;
//		
//		particles.clear();
//		this.restTimer.reset();
//
////		this.buttonList.add(new ClientButton(0, 35, this.height / 2 - 67, 130, 25, "Singleplayer", null, 2,
////				new Color(20, 20, 20, 80), new Color(-12621684)));
////		this.buttonList.add(new ClientButton(1, 35, this.height / 2 - 40, 130, 25, "Multiplayer", null, 2,
////				new Color(20, 20, 20, 80), new Color(-12621684)));
////		this.buttonList.add(new ClientButton(2, 35, this.height / 2 - 13, 130, 25, "Alt Manager", null, 2,
////				new Color(20, 20, 20, 80), new Color(-12621684)));
////		this.buttonList.add(new ClientButton(3, 35, this.height / 2 + 15, 130, 25, "Settings", null, 2,
////				new Color(20, 20, 20, 80), new Color(-12621684)));
//		this.buttonList.add(new ClientButton(0, 350, this.height / 2 - 70, 50, 50, "",
//				new ResourceLocation("Melody/icon/singleplayer.png"), 3, new Color(20, 20, 20, 0)));
//		this.buttonList.add(new ClientButton(1, 400, this.height / 2 - 70, 50, 50, "",
//				new ResourceLocation("Melody/icon/multiplayer.png"), 3, new Color(20, 20, 20, 0)));
//		this.buttonList.add(new ClientButton(2, 450, this.height / 2 - 70, 50, 50, "",
//				new ResourceLocation("Melody/icon/alt.png"), 3, new Color(20, 20, 20, 0)));
//		this.buttonList.add(new ClientButton(3, 500, this.height / 2 - 70, 50, 50, "",
//				new ResourceLocation("Melody/icon/options.png"), 3, new Color(20, 20, 20, 0)));
//		this.buttonList.add(new ClientButton(4, 560, this.height / 2 - 70, 50, 50, "",
//				new ResourceLocation("Melody/icon/exit.png"), 3, new Color(20, 20, 20, 0)));
//		
//		anim.on();
//		for (int iii = 0; iii < particleCount; iii++) {
//			double randomX = -2 + (2 - -2) * RANDOM.nextDouble();
//			double randomY = -2 + (2 - -2) * RANDOM.nextDouble();
//			double randomXm = -0 + (width - -0) * RANDOM.nextDouble();
//			double randomYm = -0 + (height - -0) * RANDOM.nextDouble();
//			double randomDepthm = RANDOM.nextDouble() + 0.1;
//			int mX = 0;
//			int mY = 0;
//			MenuParticle part = new MenuParticle(randomXm + 0, randomYm + 0, randomDepthm + 0, true)
//					.addMotion(randomX + mX / 4, randomY + mY / 4);
//			part.alpha = 0.15f;
//			particles.add(part);
//		}
//	}
//
//	@Override
//	protected void actionPerformed(GuiButton button) {
//		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
//		switch (button.id) {
//		case 0: {
//			this.mc.displayGuiScreen(new GuiSelectWorld(this));
//			break;
//		}
//		case 1: {
//			this.mc.displayGuiScreen(new GuiMultiplayer(this));
//			break;
//		}
//		case 2: {
//			this.mc.displayGuiScreen(new GuiAltManager());
//			break;
//		}
//		case 3: {
//			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
//			break;
//		}
//		case 4: {
//			System.exit(1);
//			break;
//		}
//		}
//	}
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    	if(keyCode == 27) {
    		this.mc.displayGuiScreen((MainMenu)null);
    	}
    }
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GlStateManager.disableAlpha();
        GlStateManager.enableAlpha();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 274;
        int j = this.width / 2 - i / 2;
        int k = 30;
        //this.drawGradientRect(0, 0, this.width-700, this.height-350, -2130706420, 16777215);
        //RenderUtil.drawRoundedRect(250, 10, this.width-708, this.height-450, new Color(255, 255, 255).getRGB(), new Color(255, 255, 255).getRGB());
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawDefaultBackground();
        ScaledResolution scaledRes;
        boolean isOverExit = mouseX > this.width - 27 && mouseX < this.width - 27 + 13 && mouseY > 10 && mouseY < 24;
        boolean isOverSingleplayer = mouseX > this.width / 2 - 45 && mouseX < this.width / 2 + 45 && mouseY > this.height / 2 - 25 && mouseY < this.height / 2 - 5;
        boolean isOverMultiplayer = mouseX > this.width / 2 - 45 && mouseX < this.width / 2 + 45 && mouseY > this.height / 2 && mouseY < this.height / 2 + 20;
        boolean isOverAltManager = mouseX > this.width / 2 - 45 && mouseX < this.width / 2 + 45 && mouseY > this.height / 2 + 25 && mouseY < this.height / 2 + 45;
        boolean isOverSettings = mouseX > this.width - 100 && mouseX < this.width - 82 + FontLoaders2.msFont16.getStringWidth("Settings") && mouseY > 10 && mouseY < 22;
        boolean isOverLanguage = mouseX > this.width - 180 && mouseX < this.width - 162 + FontLoaders2.msFont16.getStringWidth("Language") && mouseY > 10 && mouseY < 22;
        int size = 130;
        //GlStateManager.pushMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        ScaledResolution sr = scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String time2 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //Gui.drawScaledCustomSizeModalRect(0, 0, (float)0.0f, (float)0.0f, (int)scaledRes.getScaledWidth(), (int)scaledRes.getScaledHeight(), (int)scaledRes.getScaledWidth(), (int)scaledRes.getScaledHeight(), (float)scaledRes.getScaledWidth(), (float)scaledRes.getScaledHeight());
        //FontLoaders.msyu16.drawString("Lander", (double)(this.width / 2 - FontLoaders.msyu16.getStringWidth("ETB Client") / 2), (double)(this.height / 2 - 70), new Color(255, 255, 255, 180).getRGB());
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtil.drawRoundedRect((float)(this.width / 2 - 45), (float)(this.height / 2 - 25), (float)(this.width / 2 + 45), (float)(this.height / 2 - 5), (int)new Color(255, 255, 255, 100).getRGB(), (int)(isOverSingleplayer ? new Color(255, 255, 255, 160).getRGB() : new Color(255, 255, 255, 100).getRGB()));
        RenderUtil.drawRoundedRect((float)(this.width / 2 - 45), (float)(this.height / 2), (float)(this.width / 2 + 45), (float)(this.height / 2 + 20), (int)new Color(255, 255, 255, 100).getRGB(), (int)(isOverMultiplayer ? new Color(255, 255, 255, 160).getRGB() : new Color(255, 255, 255, 100).getRGB()));
        RenderUtil.drawRoundedRect((float)(this.width / 2 - 45), (float)(this.height / 2 + 25), (float)(this.width / 2 + 45), (float)(this.height / 2 + 45), (int)new Color(255, 255, 255, 100).getRGB(), (int)(isOverAltManager ? new Color(255, 255, 255, 160).getRGB() : new Color(255, 255, 255, 100).getRGB()));
        FontLoaders2.msFont16.drawStringWithShadow("SinglePlayer", (float)(this.width / 2 - FontLoaders2.msFont16.getStringWidth("SinglePlayer") / 2), (float)(this.height / 2 - 18), new Color(255, 255, 255).getRGB());
        FontLoaders2.msFont16.drawStringWithShadow("MultiPlayer", (float)(this.width / 2 - FontLoaders2.msFont16.getStringWidth("MultiPlayer") / 2), (float)(this.height / 2 + 7), new Color(255, 255, 255).getRGB());
        FontLoaders2.msFont16.drawStringWithShadow("AltManager", (float)(this.width / 2 - FontLoaders2.msFont16.getStringWidth("AltManager") / 2), (float)(this.height / 2 + 32), new Color(255, 255, 255).getRGB());
        if (isOverSettings) {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.9f);
        } else {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.7f);
        }
        RenderUtil.drawIcon((float)(this.width - 99), (float)11.0f, (int)13, (int)13, (ResourceLocation)new ResourceLocation("Melody/icon/options.png"));
        FontLoaders2.msFont16.drawStringWithShadow("Settings", (float)(this.width - 82), (float) 14.0, isOverSettings ? new Color(255, 255, 255, 229).getRGB() : new Color(255, 255, 255, 178).getRGB());
        if (isOverLanguage) {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.9f);
        } else {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.7f);
        }
        RenderUtil.drawIcon((float)(this.width - 180), (float)10.0f, (int)15, (int)15, (ResourceLocation)new ResourceLocation("Melody/icon/language.png"));
        FontLoaders2.msFont16.drawStringWithShadow("Language", (float)(this.width - 162), (float) 14.0, isOverLanguage ? new Color(255, 255, 255, 229).getRGB() : new Color(255, 255, 255, 178).getRGB());
        if (isOverExit) {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.9f);
        } else {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.7f);
        }
        RenderUtil.drawIcon((float)(this.width - 27), (float)10.5f, (int)13, (int)13, (ResourceLocation)new ResourceLocation("Melody/icon/exit.png"));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        String text1 = "Base by Andy_Melody Version: " + Client.instance.version + "    Alt: " + mc.session.getUsername();
        String text2 = "Hello! Username: " + Client.username;
        //FontLoaders.msyu16.drawString("Coderight by @Mojang", 2.0, (double)(this.height - 10), new Color(255, 255, 255, 178).getRGB());
        GlStateManager.popMatrix();
        String[] v8 = {"- Changelog "+Client.version,"*温馨提示Base是Andy的Melody，并非西瓜花钱买Melody码字","*就以为自己是Melody原作者的气泡水base","-太他妈不要脸了！！"};
        int v9 = 5;
        for (String s : v8) {
      	  String a12 = s.charAt(0) == '-' ? EnumChatFormatting.RED + s + EnumChatFormatting.RESET : s.charAt(0) == '+' ? EnumChatFormatting.GREEN + s + EnumChatFormatting.RESET : s.charAt(0) == '*' ? EnumChatFormatting.AQUA + s + EnumChatFormatting.RESET : s.charAt(0) == '#' ? EnumChatFormatting.YELLOW + s + EnumChatFormatting.RESET : s;
           FontLoaders2.msFont16.drawStringWithShadow(a12, 3, v9 - 2, Colors.getColor(255, 255, 255, 180));
            v9 += Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 1;
          }
        int color = Colors.getColor(255, 255, 255, 170);
        int add = Colors.getColor(0, 255, 0, 170);
        int imp = Colors.getColor(0, 255, 255, 170);
        int del = Colors.getColor(255, 0, 0, 170);
        
        FontLoaders2.msFont14.drawString(text1, 2, height - FontLoaders2.msFont14.FONT_HEIGHT - 2, new Color(255, 255, 255).getRGB());
        FontLoaders2.msFont14.drawString(text2, 2, height - FontLoaders2.msFont14.FONT_HEIGHT - 10, new Color(255, 255, 255).getRGB());
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        boolean isOverLanguage;
        boolean isOverExit = mouseX > this.width - 27 && mouseX < this.width - 27 + 13 && mouseY > 10 && mouseY < 24;
        boolean isOverSingleplayer = mouseX > this.width / 2 - 45 && mouseX < this.width / 2 + 45 && mouseY > this.height / 2 - 25 && mouseY < this.height / 2 - 5;
        boolean isOverMultiplayer = mouseX > this.width / 2 - 45 && mouseX < this.width / 2 + 45 && mouseY > this.height / 2 && mouseY < this.height / 2 + 20;
        boolean isOverAltManager = mouseX > this.width / 2 - 45 && mouseX < this.width / 2 + 45 && mouseY > this.height / 2 + 25 && mouseY < this.height / 2 + 45;
        boolean isOverSettings = mouseX > this.width - 100 && mouseX < this.width - 82&& mouseY > 10 && mouseY < 22;
        boolean bl = isOverLanguage = mouseX > this.width - 180 && mouseX < this.width - 162 + FontLoaders2.msFont16.getStringWidth("Language") && mouseY > 10 && mouseY < 22;
        if (mouseButton == 0 && isOverSingleplayer) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.create((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if (mouseButton == 0 && isOverMultiplayer) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.create((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (mouseButton == 0 && isOverAltManager) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.create((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
            this.mc.displayGuiScreen(new GuiAltManager());
        }
        if (mouseButton == 0 && isOverSettings) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.create((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
            this.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.mc.gameSettings));
        }
        if (mouseButton == 0 && isOverExit) {
        	System.exit(0);
        }
        if (mouseButton == 0 && isOverLanguage) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.create((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
            this.mc.displayGuiScreen((GuiScreen)new GuiLanguage((GuiScreen)this, this.mc.gameSettings, this.mc.mcLanguageManager));
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	Animation anim = new Animation() {
		public int getMaxTime() {
			return 0;
		};

		@Override
		public void render() {
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			GlStateManager.enableBlend();
			GlStateManager.disableTexture2D();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.color(1f, 1f, 1f, (float) time / 10.0f);
			worldrenderer.begin(7, DefaultVertexFormats.POSITION);
			worldrenderer.pos((double) 0, (double) height + 1, 0.0D).endVertex();
			worldrenderer.pos((double) width, (double) height + 1, 0.0D).endVertex();
			worldrenderer.pos((double) width, (double) 0, 0.0D).endVertex();
			worldrenderer.pos((double) 0, (double) 0, 0.0D).endVertex();
			tessellator.draw();
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		}

	};

	private void open(String url) throws Exception {
		String osName = System.getProperty("os.name", "");

		if (osName.startsWith("Windows")) {
			Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + url);
		}

		else if (osName.startsWith("Mac OS")) {
			Class fileMgr = Class.forName("com.apple.eio.FileManager");
			Method openURL = fileMgr.getDeclaredMethod("openURL", String.class);
			openURL.invoke(null, url);
		}

		else {
			String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
			String browser = null;

			for (int count = 0; count < browsers.length && browser == null; count++) {
				if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0) {
					browser = browsers[count];
				}
			}

			if (browser == null) {
				throw new RuntimeException("No Browser Was Found.");
			} else {
				Runtime.getRuntime().exec(new String[] { browser, url });
			}
		}
	}
}
