package cn.Arctic.GUI.Menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
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

import cn.Arctic.Client;
import cn.Arctic.Font.CFontRenderer;
import cn.Arctic.Font.me.superskidder.FontLoaders2;
import cn.Arctic.GUI.Buttons.ClientButton;
import cn.Arctic.GUI.Buttons.FlatButton;
import cn.Arctic.GUI.Buttons.SimpleButton;
import cn.Arctic.GUI.login.GuiAltManager;
import cn.Arctic.GUI.particle.MenuParticle;
import cn.Arctic.GUI.particle.ParticleUtils;
import cn.Arctic.Module.modules.GUI.HUD;
import cn.Arctic.Util.Colors;
import cn.Arctic.Util.Timer.TimeHelper;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.Util.animate.Animation;
import cn.Arctic.Util.render.RenderUtil;
import cn.Arctic.Util.render.RenderUtils;

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
	

	@Override
	public void initGui() {
		super.initGui();

		Client.shouldShaderBack = true;
		
		particles.clear();
		this.restTimer.reset();

//		
//		this.buttonList.add(new ClientButton(0, 420, this.height / 2 - 67, 130, 25, "Singleplayer", null, 2,
//				new Color(20,20,20,80), new Color(-12621684)));
//		this.buttonList.add(new ClientButton(1, 420, this.height / 2 - 40, 130, 25, "Multiplayer", null, 2,
//				new Color(20, 20, 20, 80), new Color(-12621684)));
//		this.buttonList.add(new ClientButton(2, 420, this.height / 2 - 13, 130, 25, "Alt Manager", null, 2,
//				new Color(20, 20, 20, 80), new Color(-12621684)));
//		this.buttonList.add(new ClientButton(3, 420, this.height / 2 + 15, 130, 25, "Settings", null, 2,
//				new Color(20, 20, 20, 80), new Color(-12621684)));
//		this.buttonList.add(new ClientButton(99, 420, this.height / 2 + 43, 130, 25, "Exit", null, 2,
//				new Color(20, 20, 20, 80), new Color(-12621684)));
		RenderUtil.drawRect(0, 420, this.height / 2 - 67, 130,new Color(0, 0, 0).getRGB());
        RenderUtil.drawGradientSideways(0, 420, this.height / 2 - 67, 130, new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),140).getRGB(), new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue()).getRGB());
		this.buttonList.add(new ClientButton(0, 420, this.height / 2 - 67, 130, 25, "Singleplayer",
				new ResourceLocation("Melody/icon/singleplayer.png"), 3, new Color(20, 20, 20, 0)));
		this.buttonList.add(new ClientButton(1, 420, this.height / 2 - 40, 130, 25, "Multiplayer",
				new ResourceLocation("Melody/icon/multiplayer.png"), 3, new Color(20, 20, 20, 0)));
		this.buttonList.add(new ClientButton(2, 420, this.height / 2 - 13, 130, 25, "Alt Manager",
				new ResourceLocation("Melody/icon/alt.png"), 3, new Color(20, 20, 20, 0)));
		this.buttonList.add(new ClientButton(3, 420, this.height / 2 + 15, 130, 25, "Settings",
				new ResourceLocation("Melody/icon/options.png"), 3, new Color(20, 20, 20, 0)));
		this.buttonList.add(new ClientButton(99, 420, this.height / 2 + 43, 130, 25, "Exit",
				new ResourceLocation("Melody/icon/quit.png"), 3, new Color(20, 20, 20, 0)));


		anim.on();
		for (int iii = 0; iii < particleCount; iii++) {
			double randomX = -2 + (2 - -2) * RANDOM.nextDouble();
			double randomY = -2 + (2 - -2) * RANDOM.nextDouble();
			double randomXm = -0 + (width - -0) * RANDOM.nextDouble();
			double randomYm = -0 + (height - -0) * RANDOM.nextDouble();
			double randomDepthm = RANDOM.nextDouble() + 0.1;
			int mX = 0;
			int mY = 0;
			MenuParticle part = new MenuParticle(randomXm + 0, randomYm + 0, randomDepthm + 0, true)
					.addMotion(randomX + mX / 4, randomY + mY / 4);
			part.alpha = 0.15f;
			particles.add(part);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		switch (button.id) {
		case 0: {
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
			break;
		}
		case 1: {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
			break;
		}
		case 2: {
			this.mc.displayGuiScreen(new GuiAltManager());
			break;
		}
		case 3: {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
			break;
		}
		case 4: {
			this.mc.displayGuiScreen(new GuiProtocolSelector(this));
			break;
		}
		case 99: {
			this.mc.displayGuiScreen(new GuiGoodBye());
			break;
		}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();

		FontRenderer font2 = FontLoaders2.msFont18;
		FontRenderer font3 = FontLoaders2.msFont16;
		FontRenderer font1 = FontLoaders2.msFont18;

		drawDefaultBackground();
		
		if(this.restTimer.hasReached(300000000)) {
			mc.displayGuiScreen(new GuiResting());
		}

		font1.drawString("YANEX CLIENT", 446, this.height / 2 - 90, Colors.WHITE.c);
		RenderUtils.drawImage(new ResourceLocation("Melody/icon/Lander.png"),420, this.height / 2 - 142, 130, 45);

//		RenderUtil.drawFastRoundedRect(width - 153, height - 43, width - 8, height - 5, 18,
//				new Color(100, 180, 255, 20).getRGB());
//
//		RenderUtil.drawRoundedRect(this.width - 188, 12, this.width - 12, 52, new Color(20, 20, 20, 30).getRGB());

		font2.drawCenteredString("Logged in as: " + EnumChatFormatting.BLUE + mc.getSession().getUsername(), this.width - 918, 498, Colors.GREY.c);

		String version = ProtocolCollection.getProtocolById(ViaMCP.getInstance().getVersion()).getName();
		font2.drawCenteredString("ViaMCP: " + EnumChatFormatting.GREEN + version, this.width - 32, 498, Colors.GREY.c);

		// MouseParticle

		anim.render();
		if (!particles.isEmpty()) {
			GlStateManager.pushMatrix();
			// TODO Menu Particles
			for (MenuParticle particle : particles) {
				particle.update(mouseX, mouseY, particles);
				if (particle.alpha < 0.1f) {
					particle.remove = true;
				}
			}
			Iterator<MenuParticle> iter = particles.iterator();
			while (iter.hasNext()) {
				MenuParticle part = iter.next();
				if (part.remove) {
					iter.remove();
				}
			}
			GlStateManager.enableBlend();
			GlStateManager.disableTexture2D();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			boolean c = true;
			for (MenuParticle particle : particles) {
				GlStateManager.color(0.5f, 0.6f, 1f, particle.alpha);
				double x = particle.x;
				double y = particle.y;

				worldrenderer.begin(7, DefaultVertexFormats.POSITION);
				worldrenderer.pos((double) x, (double) y + 1, 0.0D).endVertex();
				worldrenderer.pos((double) x + 0.5, (double) y + 1, 0.0D).endVertex();
				worldrenderer.pos((double) x + 0.5, (double) y, 0.0D).endVertex();
				worldrenderer.pos((double) x, (double) y, 0.0D).endVertex();
				tessellator.draw();
			}
			GlStateManager.popMatrix();
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (MenuParticle part : particles) {
			float angle = (float) Math.toDegrees(Math.atan2(mouseY - part.y, mouseX - part.x));
			if (angle < 0) {
				angle += 360;
			}
			double xDist = mouseX - part.x;
			double yDist = mouseY - part.y;
			double dist = Math.sqrt(xDist * xDist + yDist * yDist);
			double mX = Math.cos(Math.toRadians(angle));
			double mY = Math.sin(Math.toRadians(angle));
			if (dist < 20) {
				dist = 20;
			}
			part.motionX -= mX * 200 / (dist / 2);
			part.motionY -= mY * 200 / (dist / 2);
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
