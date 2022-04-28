package cn.Arctic.Util.render;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.optifine.shaders.Shaders;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.Minecraft;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;

import com.mojang.authlib.GameProfile;

import cn.Arctic.Client;
import cn.Arctic.Event.events.EventRender3D;
import cn.Arctic.Module.modules.RENDER.ESP;
import cn.Arctic.Util.Chat.Helper;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.Util.math.Vec2f;
import cn.Arctic.Util.math.Vec3f;
import cn.Arctic.Util.render.gl.GLClientState;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.List;

public class RenderUtil {
	private static final Frustum frustum = new Frustum();
	public static float delta;
	public static Minecraft mc = Minecraft.getMinecraft();
	public static final Tessellation tessellator;
	private static final List<Integer> csBuffer;
	private static final Consumer<Integer> ENABLE_CLIENT_STATE;
	private static final Consumer<Integer> DISABLE_CLIENT_STATE;
	
	private static TimerUtil addTimer = new TimerUtil();
	private static TimerUtil upTimer = new TimerUtil();

	static {
		tessellator = Tessellation.createExpanding(4, 1.0f, 2.0f);
		csBuffer = new ArrayList<Integer>();
		ENABLE_CLIENT_STATE = GL11::glEnableClientState;
		DISABLE_CLIENT_STATE = GL11::glEnableClientState;
	}
    public static int astolfoRainbow(int delay,int offset, int index) {
		double rainbowDelay = Math.ceil(System.currentTimeMillis() + ((long) delay * index)) / offset;
		return Color.getHSBColor((double)((float)((rainbowDelay %= 360.0) / 360.0)) < 0.5 ? -((float)(rainbowDelay / 360.0)) : (float)(rainbowDelay / 360.0), 0.5F, 1.0F).getRGB();
	}
	public static boolean isInViewFrustrum(AxisAlignedBB bb) {
		Entity current = Minecraft.getMinecraft().getRenderViewEntity();
		frustum.setPosition(current.posX, current.posY, current.posZ);
		return frustum.isBoundingBoxInFrustum(bb);
	}

	public static boolean isInViewFrustrum(Entity entity) {
		return RenderUtil.isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
	}

	public RenderUtil() {
		super();
	}
	public static void drawHollowBox(float x, float y, float x1, float y1, float thickness, int color) {
		RenderUtil.drawHorizontalLine(x, y, x1, thickness, color);
		RenderUtil.drawHorizontalLine(x, y1, x1, thickness, color);
		RenderUtil.drawVerticalLine(x, y, y1, thickness, color);
		RenderUtil.drawVerticalLine(x1 - thickness, y, y1, thickness, color);
	}

	public static void drawHorizontalLine(float x, float y, float x1, float thickness, int color) {
		RenderUtil.drawRect2(x, y, x1, y + thickness, color);
	}

	public static void drawRect2(double x, double y, double x2, double y2, int color) {
		RenderUtil.drawRect((float) x, y, x2, y2, color);
	}

	public static void drawVerticalLine(float x, float y, float y1, float thickness, int color) {
		RenderUtil.drawRect2(x, y, x + thickness, y1, color);
	}

	public static Vec3 interpolateRender(EntityPlayer player) {
		float part = Minecraft.getMinecraft().timer.renderPartialTicks;
		double interpX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) part;
		double interpY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) part;
		double interpZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) part;
		return new Vec3(interpX, interpY, interpZ);
	}

	public static void drawScaledRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height,
			float tileWidth, float tileHeight) {
		Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
	}

	public static void drawIcon(float x, float y, int sizex, int sizey, ResourceLocation resourceLocation) {
		GL11.glPushMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		GL11.glEnable((int) 3042);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glEnable((int) 2848);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(516, 0.1f);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		GL11.glTranslatef((float) x, (float) y, (float) 0.0f);
		RenderUtil.drawScaledRect(0, 0, 0.0f, 0.0f, sizex, sizey, sizex, sizey, sizex, sizey);
		GlStateManager.disableAlpha();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableLighting();
		GlStateManager.disableRescaleNormal();
		GL11.glDisable((int) 2848);
		GlStateManager.disableBlend();
		GL11.glPopMatrix();
	}

	public static void doGlScissor(int x, int y, int width, int height) {
		Minecraft mc = Minecraft.getMinecraft();
		int scaleFactor = 1;
		int k = mc.gameSettings.guiScale;
		if (k == 0) {
			k = 1000;
		}
		while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320
				&& mc.displayHeight / (scaleFactor + 1) >= 240) {
			++scaleFactor;
		}
		GL11.glScissor((int) (x * scaleFactor), (int) (mc.displayHeight - (y + height) * scaleFactor),
				(int) (width * scaleFactor), (int) (height * scaleFactor));
	}

	public static void drawFullCircle(int cx, int cy, double r, int segments, float lineWidth, int part, int c) {
		GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
		r *= 2.0;
		cx *= 2;
		cy *= 2;
		float f2 = (float) (c >> 24 & 255) / 255.0f;
		float f22 = (float) (c >> 16 & 255) / 255.0f;
		float f3 = (float) (c >> 8 & 255) / 255.0f;
		float f4 = (float) (c & 255) / 255.0f;
		GL11.glEnable((int) 3042);
		GL11.glLineWidth((float) lineWidth);
		GL11.glDisable((int) 3553);
		GL11.glEnable((int) 2848);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glColor4f((float) f22, (float) f3, (float) f4, (float) f2);
		GL11.glBegin((int) 3);
		int i = segments - part;
		while (i <= segments) {
			double x = Math.sin((double) i * 3.141592653589793 / 180.0) * r;
			double y = Math.cos((double) i * 3.141592653589793 / 180.0) * r;
			GL11.glVertex2d((double) ((double) cx + x), (double) ((double) cy + y));
			++i;
		}
		GL11.glEnd();
		GL11.glDisable((int) 2848);
		GL11.glEnable((int) 3553);
		GL11.glDisable((int) 3042);
		GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
	}

	public static void drawCircle(double x, double y, double radius, int c) {
		float f2 = (float) (c >> 24 & 255) / 255.0f;
		float f22 = (float) (c >> 16 & 255) / 255.0f;
		float f3 = (float) (c >> 8 & 255) / 255.0f;
		float f4 = (float) (c & 255) / 255.0f;
		GlStateManager.alphaFunc(516, 0.001f);
		GlStateManager.color(f22, f3, f4, f2);
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		Tessellator tes = Tessellator.getInstance();
		double i = 0.0;
		while (i < 360.0) {
			double f5 = Math.sin(i * 3.141592653589793 / 180.0) * radius;
			double f6 = Math.cos(i * 3.141592653589793 / 180.0) * radius;
			GL11.glVertex2d((double) ((double) f3 + x), (double) ((double) f4 + y));
			i += 1.0;
		}
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.alphaFunc(516, 0.1f);
	}

	public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
		GL11.glPushMatrix();
		cx *= 2.0F;
		cy *= 2.0F;
		float f = (c >> 24 & 0xFF) / 255.0F;
		float f1 = (c >> 16 & 0xFF) / 255.0F;
		float f2 = (c >> 8 & 0xFF) / 255.0F;
		float f3 = (c & 0xFF) / 255.0F;
		float theta = (float) (6.2831852D / num_segments);
		float p = (float) Math.cos(theta);
		float s = (float) Math.sin(theta);
		float x = r *= 2.0F;
		float y = 0.0F;
		enableGL2D();
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(2);
		int ii = 0;
		while (ii < num_segments) {
			GL11.glVertex2f(x + cx, y + cy);
			float t = x;
			x = p * x - s * y;
			y = s * t + p * y;
			ii++;
		}
		GL11.glEnd();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		disableGL2D();
		GlStateManager.color(1, 1, 1, 1);
		GL11.glPopMatrix();
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

	public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
		float f = (float) (col1 >> 24 & 255) / 255.0f;
		float f1 = (float) (col1 >> 16 & 255) / 255.0f;
		float f2 = (float) (col1 >> 8 & 255) / 255.0f;
		float f3 = (float) (col1 & 255) / 255.0f;
		float f4 = (float) (col2 >> 24 & 255) / 255.0f;
		float f5 = (float) (col2 >> 16 & 255) / 255.0f;
		float f6 = (float) (col2 >> 8 & 255) / 255.0f;
		float f7 = (float) (col2 & 255) / 255.0f;
		GL11.glEnable((int) 3042);
		GL11.glDisable((int) 3553);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glEnable((int) 2848);
		GL11.glShadeModel((int) 7425);
		GL11.glPushMatrix();
		GL11.glBegin((int) 7);
		GL11.glColor4f((float) f1, (float) f2, (float) f3, (float) f);
		GL11.glVertex2d((double) left, (double) top);
		GL11.glVertex2d((double) left, (double) bottom);
		GL11.glColor4f((float) f5, (float) f6, (float) f7, (float) f4);
		GL11.glVertex2d((double) right, (double) bottom);
		GL11.glVertex2d((double) right, (double) top);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable((int) 3553);
		GL11.glDisable((int) 3042);
		GL11.glDisable((int) 2848);
		GL11.glShadeModel((int) 7424);
	}

	public static void color(int color) {
		float f = (float) (color >> 24 & 255) / 255.0f;
		float f1 = (float) (color >> 16 & 255) / 255.0f;
		float f2 = (float) (color >> 8 & 255) / 255.0f;
		float f3 = (float) (color & 255) / 255.0f;
		GL11.glColor4f((float) f1, (float) f2, (float) f3, (float) f);
	}

	public static int width() {
		return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
	}

	public static void drawImage(ResourceLocation image, int x, int y, double width, double height) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask((boolean) false);
		OpenGlHelper.glBlendFunc((int) 770, (int) 771, (int) 1, (int) 0);
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, (float) 0.0f, (float) 0.0f, (int) width, (int) height,
				(float) width, (float) height);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
	}

	public static void rectTexture(float x, float y, float w, float h, Texture texture, int color) {
		if (texture == null) {
			return;
		}
		GlStateManager.color(0, 0, 0);
		GL11.glColor4f(0, 0, 0, 0);

		x = Math.round(x);
		w = Math.round(w);
		y = Math.round(y);
		h = Math.round(h);

		float var11 = (float) (color >> 24 & 255) / 255.0F;
		float var6 = (float) (color >> 16 & 255) / 255.0F;
		float var7 = (float) (color >> 8 & 255) / 255.0F;
		float var8 = (float) (color & 255) / 255.0F;

		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(var6, var7, var8, var11);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		texture.bind();

		float tw = (w / texture.getTextureWidth()) / (w / texture.getImageWidth());
		float th = (h / texture.getTextureHeight()) / (h / texture.getImageHeight());

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(x, y);
		GL11.glTexCoord2f(0, th);
		GL11.glVertex2f(x, y + h);
		GL11.glTexCoord2f(tw, th);
		GL11.glVertex2f(x + w, y + h);
		GL11.glTexCoord2f(tw, 0);
		GL11.glVertex2f(x + w, y);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void checkSetupFBO() {
		Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
		if (fbo != null && fbo.depthBuffer > -1) {
			EXTFramebufferObject.glDeleteRenderbuffersEXT((int) fbo.depthBuffer);
			int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
			EXTFramebufferObject.glBindRenderbufferEXT((int) 36161, (int) stencil_depth_buffer_ID);
			EXTFramebufferObject.glRenderbufferStorageEXT((int) 36161, (int) 34041,
					(int) Minecraft.getMinecraft().displayWidth, (int) Minecraft.getMinecraft().displayHeight);
			EXTFramebufferObject.glFramebufferRenderbufferEXT((int) 36160, (int) 36128, (int) 36161,
					(int) stencil_depth_buffer_ID);
			EXTFramebufferObject.glFramebufferRenderbufferEXT((int) 36160, (int) 36096, (int) 36161,
					(int) stencil_depth_buffer_ID);
			fbo.depthBuffer = -1;
		}
	}

	public static void outlineOne() {
		GL11.glPushAttrib((int) 1048575);
		GL11.glDisable((int) 3008);
		GL11.glDisable((int) 3553);
		GL11.glDisable((int) 2896);
		GL11.glEnable((int) 3042);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glLineWidth((float) 4.0f);
		GL11.glEnable((int) 2848);
		GL11.glEnable((int) 2960);
		GL11.glClear((int) 1024);
		GL11.glClearStencil((int) 15);
		GL11.glStencilFunc((int) 512, (int) 1, (int) 15);
		GL11.glStencilOp((int) 7681, (int) 7681, (int) 7681);
		GL11.glPolygonMode((int) 1032, (int) 6913);
	}

	public static void outlineTwo() {
		GL11.glStencilFunc((int) 512, (int) 0, (int) 15);
		GL11.glStencilOp((int) 7681, (int) 7681, (int) 7681);
		GL11.glPolygonMode((int) 1032, (int) 6914);
	}

	public static void outlineThree() {
		GL11.glStencilFunc((int) 514, (int) 1, (int) 15);
		GL11.glStencilOp((int) 7680, (int) 7680, (int) 7680);
		GL11.glPolygonMode((int) 1032, (int) 6913);
	}

	public static void outlineFour() {
		GL11.glDepthMask((boolean) false);
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 10754);
		GL11.glPolygonOffset((float) 1.0f, (float) -2000000.0f);
		GL11.glColor4f((float) 0.9529412f, (float) 0.6117647f, (float) 0.07058824f, (float) 1.0f);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
	}

	public static void outlineFive() {
		GL11.glPolygonOffset((float) 1.0f, (float) 2000000.0f);
		GL11.glDisable((int) 10754);
		GL11.glEnable((int) 2929);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 2960);
		GL11.glDisable((int) 2848);
		GL11.glHint((int) 3154, (int) 4352);
		GL11.glEnable((int) 3042);
		GL11.glEnable((int) 2896);
		GL11.glEnable((int) 3553);
		GL11.glEnable((int) 3008);
		GL11.glPopAttrib();
	}

	public static void entityESPBox(EntityPlayer e, Color color, EventRender3D event) {
		double posX = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double) event.getPartialTicks()
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double posY = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double) event.getPartialTicks()
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double posZ = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) event.getPartialTicks()
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		AxisAlignedBB box = AxisAlignedBB.fromBounds(posX - (double) e.width, posY, posZ - (double) e.width,
				posX + (double) e.width, posY + (double) e.height + 0.2, posZ + (double) e.width);
		if (e instanceof EntityLivingBase) {
			box = AxisAlignedBB.fromBounds(posX - (double) e.width + 0.2, posY, posZ - (double) e.width + 0.2,
					posX + (double) e.width - 0.2, posY + (double) e.height + (e.isSneaking() ? 0.02 : 0.2),
					posZ + (double) e.width - 0.2);
		}
		GL11.glLineWidth(2.5f);
		GL11.glColor4f((float) ((float) color.getRed() / 255.0f), (float) ((float) color.getGreen() / 255.0f),
				(float) ((float) color.getBlue() / 255.0f), (float) 1f);
		RenderUtil.drawOutlinedBoundingBox(box);
	}

	public static int createShader(String shaderCode, int shaderType) throws Exception {
		int shader;
		block4: {
			shader = 0;
			try {
				shader = ARBShaderObjects.glCreateShaderObjectARB((int) shaderType);
				if (shader != 0)
					break block4;
				return 0;
			} catch (Exception exc) {
				ARBShaderObjects.glDeleteObjectARB((int) shader);
				throw exc;
			}
		}
		ARBShaderObjects.glShaderSourceARB((int) shader, (CharSequence) shaderCode);
		ARBShaderObjects.glCompileShaderARB((int) shader);
		if (ARBShaderObjects.glGetObjectParameteriARB((int) shader, (int) 35713) == 0) {
			throw new RuntimeException("Error creating shader:");
		}
		return shader;
	}

	public static void glColor(int hex) {
		float alpha = (hex >> 24 & 0xFF) / 255.0F;
		float red = (hex >> 16 & 0xFF) / 255.0F;
		float green = (hex >> 8 & 0xFF) / 255.0F;
		float blue = (hex & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
	}

	public static String getShaderCode(InputStreamReader file) {
		String shaderSource = "";
		try {
			String line;
			BufferedReader reader = new BufferedReader(file);
			while ((line = reader.readLine()) != null) {
				shaderSource = String.valueOf(shaderSource) + line + "\n";
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return shaderSource.toString();
	}

	public static void pre3D() {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
	}

	public static int height() {
		return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
	}

	public static double interpolate(double newPos, double oldPos) {
		return oldPos + (newPos - oldPos) * (double) Minecraft.getMinecraft().timer.renderPartialTicks;
	}

	public static void drawRect666(double d, double e, double f2, double f3, double red, double green, double blue,
			double alpha) {
		ESP esp = (ESP) Client.instance.getModuleManager().getModuleByClass(ESP.class);
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GL11.glPushMatrix();
		GL11.glLineWidth((float) 4.5f);
		GL11.glBegin((int) 7);
		GL11.glVertex2d((double) f2, (double) e);
		GL11.glVertex2d((double) d, (double) e);
		GL11.glVertex2d((double) d, (double) f3);
		GL11.glVertex2d((double) f2, (double) f3);
		GL11.glEnd();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GL11.glPopMatrix();
	}

	public static boolean isInFrustumView(Entity ent) {
		Entity current = Minecraft.getMinecraft().getRenderViewEntity();
		double x = RenderUtil.interpolate(current.posX, current.lastTickPosX);
		double y = RenderUtil.interpolate(current.posY, current.lastTickPosY);
		double z = RenderUtil.interpolate(current.posZ, current.lastTickPosZ);
		frustum.setPosition(x, y, z);
		if (!frustum.isBoundingBoxInFrustum(ent.getEntityBoundingBox()) && !ent.ignoreFrustumCheck) {
			return false;
		}
		return true;
	}

	public static double getAnimationState(double animation, double finalState, double speed) {
		float add = (float) (0.01 * speed);
		if (animation < finalState) {
			if (animation + add < finalState)
				animation += add;
			else
				animation = finalState;
		} else {
			if (animation - add > finalState)
				animation -= add;
			else
				animation = finalState;
		}
		return animation;
	}

	public static double interpolation(final double newPos, final double oldPos) {
		return oldPos + (newPos - oldPos) * Helper.mc.timer.renderPartialTicks;
	}

	public static int getHexRGB(final int hex) {
		return 0xFF000000 | hex;
	}

	public static void drawRect(float g, double d, double x2, double e, int col1) {
		float f2 = (float) (col1 >> 24 & 255) / 255.0f;
		float f22 = (float) (col1 >> 16 & 255) / 255.0f;
		float f3 = (float) (col1 >> 8 & 255) / 255.0f;
		float f4 = (float) (col1 & 255) / 255.0f;
		GL11.glPushMatrix();
		GL11.glEnable((int) 3042);
		GL11.glDisable((int) 3553);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glEnable((int) 2848);
		GL11.glColor4f((float) f22, (float) f3, (float) f4, (float) f2);
		GL11.glBegin((int) 7);
		GL11.glVertex2d((double) x2, (double) d);
		GL11.glVertex2d((double) g, (double) d);
		GL11.glVertex2d((double) g, (double) e);
		GL11.glVertex2d((double) x2, (double) e);
		GL11.glEnd();
		GL11.glEnable((int) 3553);
		GL11.glDisable((int) 3042);
		GL11.glDisable((int) 2848);
		GL11.glPopMatrix();
	}

	public static void drawFilledESP(Entity entity, Color color) {
		
		double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks
				- mc.getRenderManager().renderPosX;
		double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks
				- mc.getRenderManager().renderPosY;
		double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks
				- mc.getRenderManager().renderPosZ;
		final double width = entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX;
		final double height = entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY + 0.25;
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glPushMatrix();
		drawEntityESP(x, y, z, width, height, color.getRed() / 255.0f, color.getGreen() / 255.0f,
				color.getBlue() / 255.0f, color.getAlpha() / 255.0f, 1.0f, 1.0f, 1.0f, 0.5f, 1.2f);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
		
	}

	public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green,
			float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
		GL11.glPushMatrix();
		GL11.glEnable((int) 3042);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glDisable((int) 3553);
		GL11.glEnable((int) 2848);
		GL11.glDisable((int) 2929);
		GL11.glDepthMask((boolean) false);
		GL11.glColor4f((float) red, (float) green, (float) blue, (float) alpha);
		RenderUtil.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
		GL11.glLineWidth((float) lineWdith);
		GL11.glColor4f((float) lineRed, (float) lineGreen, (float) lineBlue, (float) lineAlpha);
		RenderUtil
				.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
		GL11.glDisable((int) 2848);
		GL11.glEnable((int) 3553);
		GL11.glEnable((int) 2929);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glPopMatrix();
	}

	public static void drawBoundingBox(AxisAlignedBB aa) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		tessellator.draw();
	}

	public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(3, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(3, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(1, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		tessellator.draw();
	}

	public static void drawCustomImage(int x, int y, int width, int height, ResourceLocation image) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 3042);
		GL11.glDepthMask((boolean) false);
		OpenGlHelper.glBlendFunc((int) 770, (int) 771, (int) 1, (int) 0);
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, (float) 0.0f, (float) 0.0f, (int) width, (int) height,
				(float) width, (float) height);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
	}

	public static void drawBorderedRect(final float x, final float y, final float x2, final float y2, final float l1,
			final int col1, final int col2) {
		Gui.drawRect(x, y, x2, y2, col2);
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
		GL11.glVertex2d((double) x, (double) y);
		GL11.glVertex2d((double) x, (double) y2);
		GL11.glVertex2d((double) x2, (double) y2);
		GL11.glVertex2d((double) x2, (double) y);
		GL11.glVertex2d((double) x, (double) y);
		GL11.glVertex2d((double) x2, (double) y);
		GL11.glVertex2d((double) x, (double) y2);
		GL11.glVertex2d((double) x2, (double) y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void drawFilledCircle(float xx, float yy, float radius, int col) {
		float f = (col >> 24 & 0xFF) / 255.0F;
		float f1 = (col >> 16 & 0xFF) / 255.0F;
		float f2 = (col >> 8 & 0xFF) / 255.0F;
		float f3 = (col & 0xFF) / 255.0F;

		int sections = 50;
		double dAngle = 6.283185307179586D / sections;

		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glBegin(6);
		for (int i = 0; i < sections; i++) {
			float x = (float) (radius * Math.sin(i * dAngle));
			float y = (float) (radius * Math.cos(i * dAngle));

			GL11.glColor4f(f1, f2, f3, f);
			GL11.glVertex2f(xx + x, yy + y);
		}
		GlStateManager.color(0.0F, 0.0F, 0.0F);
		GL11.glEnd();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GL11.glPopMatrix();
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

	public static void startDrawing() {
		GL11.glEnable(3042);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		Helper.mc.entityRenderer.setupCameraTransform(Helper.mc.timer.renderPartialTicks, 0);
	}

	public static void stopDrawing() {
		GL11.glDisable(3042);
		GL11.glEnable(3553);
		GL11.glDisable(2848);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
	}

	public static Color blend(final Color color1, final Color color2, final double ratio) {
		final float r = (float) ratio;
		final float ir = 1.0f - r;
		final float[] rgb1 = new float[3];
		final float[] rgb2 = new float[3];
		color1.getColorComponents(rgb1);
		color2.getColorComponents(rgb2);
		final Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir,
				rgb1[2] * r + rgb2[2] * ir);
		return color3;
	}

	public static void drawLine(final Vec2f start, final Vec2f end, final float width) {
		drawLine(start.getX(), start.getY(), end.getX(), end.getY(), width);
	}

	public static void drawLine(final Vec3f start, final Vec3f end, final float width) {
		drawLine((float) start.getX(), (float) start.getY(), (float) start.getZ(), (float) end.getX(),
				(float) end.getY(), (float) end.getZ(), width);
	}

	public static void drawLine(final float x, final float y, final float x1, final float y1, final float width) {
		drawLine(x, y, 0.0f, x1, y1, 0.0f, width);
	}

	public static void drawLine(final float x, final float y, final float z, final float x1, final float y1,
			final float z1, final float width) {
		GL11.glLineWidth(width);
		setupRender(true);
		setupClientState(GLClientState.VERTEX, true);
		RenderUtil.tessellator.addVertex(x, y, z).addVertex(x1, y1, z1).draw(3);
		setupClientState(GLClientState.VERTEX, false);
		setupRender(false);
	}
	public static void setupClientState(final GLClientState state, final boolean enabled) {
		RenderUtil.csBuffer.clear();
		if (state.ordinal() > 0) {
			RenderUtil.csBuffer.add(state.getCap());
		}
		RenderUtil.csBuffer.add(32884);
		RenderUtil.csBuffer.forEach(enabled ? RenderUtil.ENABLE_CLIENT_STATE : RenderUtil.DISABLE_CLIENT_STATE);
	}

	public static void setupRender(final boolean start) {
		if (start) {
			GlStateManager.enableBlend();
			GL11.glEnable(2848);
			GlStateManager.disableDepth();
			GlStateManager.disableTexture2D();
			GlStateManager.blendFunc(770, 771);
			GL11.glHint(3154, 4354);
		} else {
			GlStateManager.disableBlend();
			GlStateManager.enableTexture2D();
			GL11.glDisable(2848);
			GlStateManager.enableDepth();
		}
		GlStateManager.depthMask(!start);
	}

	public static void drawSolidBlockESP(double x, double y, double z, float red, float green, float blue,
			float alpha) {
		GL11.glPushMatrix();
		GL11.glEnable((int) 3042);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glDisable((int) 3553);
		GL11.glEnable((int) 2848);
		GL11.glDisable((int) 2929);
		GL11.glDepthMask((boolean) false);
		GL11.glColor4f((float) red, (float) green, (float) blue, (float) alpha);
		RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glDisable((int) 2848);
		GL11.glEnable((int) 3553);
		GL11.glEnable((int) 2929);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glPopMatrix();
	}

	public static class R2DUtils {
		public static void enableGL2D() {
			GL11.glDisable((int) 2929);
			GL11.glEnable((int) 3042);
			GL11.glDisable((int) 3553);
			GL11.glBlendFunc((int) 770, (int) 771);
			GL11.glDepthMask((boolean) true);
			GL11.glEnable((int) 2848);
			GL11.glHint((int) 3154, (int) 4354);
			GL11.glHint((int) 3155, (int) 4354);
		}

		public static void disableGL2D() {
			GL11.glEnable((int) 3553);
			GL11.glDisable((int) 3042);
			GL11.glEnable((int) 2929);
			GL11.glDisable((int) 2848);
			GL11.glHint((int) 3154, (int) 4352);
			GL11.glHint((int) 3155, (int) 4352);
		}

		public static void draw2DCorner(Entity e, double posX, double posY, double posZ, int color) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(posX, posY, posZ);
			GL11.glNormal3f((float) 0.0f, (float) 0.0f, (float) 0.0f);
			GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
			GlStateManager.scale(-0.1, -0.1, 0.1);
			GL11.glDisable((int) 2896);
			GL11.glDisable((int) 2929);
			GL11.glEnable((int) 3042);
			GL11.glBlendFunc((int) 770, (int) 771);
			GlStateManager.depthMask(true);
			R2DUtils.drawRect(7.0, -20.0, 7.300000190734863, -17.5, color);
			R2DUtils.drawRect(-7.300000190734863, -20.0, -7.0, -17.5, color);
			R2DUtils.drawRect(4.0, -20.299999237060547, 7.300000190734863, -20.0, color);
			R2DUtils.drawRect(-7.300000190734863, -20.299999237060547, -4.0, -20.0, color);
			R2DUtils.drawRect(-7.0, 3.0, -4.0, 3.299999952316284, color);
			R2DUtils.drawRect(4.0, 3.0, 7.0, 3.299999952316284, color);
			R2DUtils.drawRect(-7.300000190734863, 0.8, -7.0, 3.299999952316284, color);
			R2DUtils.drawRect(7.0, 0.5, 7.300000190734863, 3.299999952316284, color);
			R2DUtils.drawRect(7.0, -20.0, 7.300000190734863, -17.5, color);
			R2DUtils.drawRect(-7.300000190734863, -20.0, -7.0, -17.5, color);
			R2DUtils.drawRect(4.0, -20.299999237060547, 7.300000190734863, -20.0, color);
			R2DUtils.drawRect(-7.300000190734863, -20.299999237060547, -4.0, -20.0, color);
			R2DUtils.drawRect(-7.0, 3.0, -4.0, 3.299999952316284, color);
			R2DUtils.drawRect(4.0, 3.0, 7.0, 3.299999952316284, color);
			R2DUtils.drawRect(-7.300000190734863, 0.8, -7.0, 3.299999952316284, color);
			R2DUtils.drawRect(7.0, 0.5, 7.300000190734863, 3.299999952316284, color);
			GL11.glDisable((int) 3042);
			GL11.glEnable((int) 2929);
			GlStateManager.popMatrix();
		}

		public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
			R2DUtils.enableGL2D();
			GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
			R2DUtils.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
			R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
			R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
			R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
			R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
			R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
			R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
			R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
			R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
			GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
			R2DUtils.disableGL2D();
			Gui.drawRect(0, 0, 0, 0, 0);
		}

		public static void drawRect(double x2, double y2, double x1, double y1, int color) {
			R2DUtils.enableGL2D();
			R2DUtils.glColor(color);
			R2DUtils.drawRect(x2, y2, x1, y1);
			R2DUtils.disableGL2D();
		}

		private static void drawRect(double x2, double y2, double x1, double y1) {
			GL11.glBegin((int) 7);
			GL11.glVertex2d((double) x2, (double) y1);
			GL11.glVertex2d((double) x1, (double) y1);
			GL11.glVertex2d((double) x1, (double) y2);
			GL11.glVertex2d((double) x2, (double) y2);
			GL11.glEnd();
		}

		public static void glColor(int hex) {
			float alpha = (float) (hex >> 24 & 255) / 255.0f;
			float red = (float) (hex >> 16 & 255) / 255.0f;
			float green = (float) (hex >> 8 & 255) / 255.0f;
			float blue = (float) (hex & 255) / 255.0f;
			GL11.glColor4f((float) red, (float) green, (float) blue, (float) alpha);
		}

		public static void drawRect(float x, float y, float x1, float y1, int color) {
			R2DUtils.enableGL2D();
			glColor(color);
			R2DUtils.drawRect(x, y, x1, y1);
			R2DUtils.disableGL2D();
		}

		public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int borderColor) {
			R2DUtils.enableGL2D();
			glColor(borderColor);
			R2DUtils.drawRect(x + width, y, x1 - width, y + width);
			R2DUtils.drawRect(x, y, x + width, y1);
			R2DUtils.drawRect(x1 - width, y, x1, y1);
			R2DUtils.drawRect(x + width, y1 - width, x1 - width, y1);
			R2DUtils.disableGL2D();
		}

		public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
			R2DUtils.enableGL2D();
			GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
			R2DUtils.drawVLine(x *= 2.0f, y *= 2.0f, y1 *= 2.0f, borderC);
			R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y, y1, borderC);
			R2DUtils.drawHLine(x, x1 - 1.0f, y, borderC);
			R2DUtils.drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
			R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
			GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
			R2DUtils.disableGL2D();
		}

		public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
			R2DUtils.enableGL2D();
			GL11.glShadeModel((int) 7425);
			GL11.glBegin((int) 7);
			glColor(topColor);
			GL11.glVertex2f((float) x, (float) y1);
			GL11.glVertex2f((float) x1, (float) y1);
			glColor(bottomColor);
			GL11.glVertex2f((float) x1, (float) y);
			GL11.glVertex2f((float) x, (float) y);
			GL11.glEnd();
			GL11.glShadeModel((int) 7424);
			R2DUtils.disableGL2D();
		}

		public static void drawHLine(float x, float y, float x1, int y1) {
			if (y < x) {
				float var5 = x;
				x = y;
				y = var5;
			}
			R2DUtils.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
		}

		public static void drawVLine(float x, float y, float x1, int y1) {
			if (x1 < y) {
				float var5 = y;
				y = x1;
				x1 = var5;
			}
			R2DUtils.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
		}

		public static void drawHLine(float x, float y, float x1, int y1, int y2) {
			if (y < x) {
				float var5 = x;
				x = y;
				y = var5;
			}
			R2DUtils.drawGradientRect(x, x1, y + 1.0f, x1 + 1.0f, y1, y2);
		}

		public static void drawRect(float x, float y, float x1, float y1) {
			GL11.glBegin((int) 7);
			GL11.glVertex2f((float) x, (float) y1);
			GL11.glVertex2f((float) x1, (float) y1);
			GL11.glVertex2f((float) x1, (float) y);
			GL11.glVertex2f((float) x, (float) y);
			GL11.glEnd();
		}
	}

	public static void drawBorderedRect1(float x, double d, double x2, double e, float l1, int col1, int col2) {
		RenderUtil.drawRect(x, d, x2, e, col2);
		float f = (float) (col1 >> 24 & 255) / 255.0f;
		float f2 = (float) (col1 >> 16 & 255) / 255.0f;
		float f3 = (float) (col1 >> 8 & 255) / 255.0f;
		float f4 = (float) (col1 & 255) / 255.0f;
		GL11.glEnable((int) 3042);
		GL11.glDisable((int) 3553);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glEnable((int) 2848);
		GL11.glPushMatrix();
		GL11.glColor4f((float) f2, (float) f3, (float) f4, (float) f);
		GL11.glLineWidth((float) l1);
		GL11.glBegin((int) 1);
		GL11.glVertex2d((double) x, (double) d);
		GL11.glVertex2d((double) x, (double) e);
		GL11.glVertex2d((double) x2, (double) e);
		GL11.glVertex2d((double) x2, (double) d);
		GL11.glVertex2d((double) x, (double) d);
		GL11.glVertex2d((double) x2, (double) d);
		GL11.glVertex2d((double) x, (double) e);
		GL11.glVertex2d((double) x2, (double) e);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable((int) 3553);
		GL11.glDisable((int) 3042);
		GL11.glDisable((int) 2848);
	}

	public static void drawEntityOnScreen(int p_147046_0_, int p_147046_1_, int p_147046_2_, float p_147046_3_,
			float p_147046_4_, EntityLivingBase p_147046_5_) {
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) p_147046_0_, (float) p_147046_1_, (float) 40.0f);
		GlStateManager.scale((float) (-p_147046_2_), (float) p_147046_2_, (float) p_147046_2_);
		GlStateManager.rotate((float) 180.0f, (float) 0.0f, (float) 0.0f, (float) 1.0f);
		float var6 = p_147046_5_.renderYawOffset;
		float var7 = p_147046_5_.rotationYaw;
		float var8 = p_147046_5_.rotationPitch;
		float var9 = p_147046_5_.prevRotationYawHead;
		float var10 = p_147046_5_.rotationYawHead;
		GlStateManager.rotate((float) 135.0f, (float) 0.0f, (float) 1.0f, (float) 0.0f);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate((float) -135.0f, (float) 0.0f, (float) 1.0f, (float) 0.0f);
		GlStateManager.rotate((float) (-((float) Math.atan((double) (p_147046_4_ / 40.0f))) * 20.0f), (float) 1.0f,
				(float) 0.0f, (float) 0.0f);
		p_147046_5_.renderYawOffset = (float) Math.atan((double) (p_147046_3_ / 40.0f)) * -14.0f;
		p_147046_5_.rotationYaw = (float) Math.atan((double) (p_147046_3_ / 40.0f)) * -14.0f;
		p_147046_5_.rotationPitch = -((float) Math.atan((double) (p_147046_4_ / 40.0f))) * 15.0f;
		p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
		p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
		GlStateManager.translate((float) 0.0f, (float) 0.0f, (float) 0.0f);
		RenderManager var11 = Minecraft.getMinecraft().getRenderManager();
		var11.setPlayerViewY(180.0f);
		var11.setRenderShadow(false);
		var11.renderEntityWithPosYaw((Entity) p_147046_5_, 0.0, 0.0, 0.0, 0.0f, 1.0f);
		var11.setRenderShadow(true);
		p_147046_5_.renderYawOffset = var6;
		p_147046_5_.rotationYaw = var7;
		p_147046_5_.rotationPitch = var8;
		p_147046_5_.prevRotationYawHead = var9;
		p_147046_5_.rotationYawHead = var10;
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture((int) OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture((int) OpenGlHelper.defaultTexUnit);
	}

	public static void drawArc(float n, float n2, double n3, final int n4, final int n5, final double n6,
			final int n7) {
		n3 *= 2.0;
		n *= 2.0f;
		n2 *= 2.0f;
		final float n8 = (n4 >> 24 & 0xFF) / 255.0f;
		final float n9 = (n4 >> 16 & 0xFF) / 255.0f;
		final float n10 = (n4 >> 8 & 0xFF) / 255.0f;
		final float n11 = (n4 & 0xFF) / 255.0f;
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glDepthMask(true);
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glHint(3155, 4354);
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		GL11.glLineWidth((float) n7);
		GL11.glEnable(2848);
		GL11.glColor4f(n9, n10, n11, n8);
		GL11.glBegin(3);
		int n12 = n5;
		while (n12 <= n6) {
			GL11.glVertex2d(n + Math.sin(n12 * 3.141592653589793 / 180.0) * n3,
					n2 + Math.cos(n12 * 3.141592653589793 / 180.0) * n3);
			++n12;
		}
		GL11.glEnd();
		GL11.glDisable(2848);
		GL11.glScalef(2.0f, 2.0f, 2.0f);
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glHint(3155, 4352);
	}

	public static void drawWolframEntityESP(EntityLivingBase entity, int rgb, double posX, double posY, double posZ) {
		GL11.glPushMatrix();
		GL11.glTranslated(posX, posY, posZ);
		GL11.glRotatef(-entity.rotationYaw, 0.0F, 1.0F, 0.0F);
		setColor(rgb);
		enableGL3D(1.0F);
		Cylinder c = new Cylinder();
		GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
		c.setDrawStyle(100011);
		c.draw(0.5F, 0.5F, entity.height + 0.1F, 18, 1);
		disableGL3D();
		GL11.glPopMatrix();
	}

	public static void setColor(int colorHex) {
		float alpha = (float) (colorHex >> 24 & 255) / 255.0F;
		float red = (float) (colorHex >> 16 & 255) / 255.0F;
		float green = (float) (colorHex >> 8 & 255) / 255.0F;
		float blue = (float) (colorHex & 255) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha == 0.0F ? 1.0F : alpha);
	}

	public static void enableGL3D(float lineWidth) {
		GL11.glDisable(3008);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glEnable(2884);
		Shaders.disableLightmap();
		Shaders.disableFog();
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glHint(3155, 4354);
		GL11.glLineWidth(lineWidth);
	}

	public static void disableGL3D() {
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDisable(3042);
		GL11.glEnable(3008);
		GL11.glDepthMask(true);
		GL11.glCullFace(1029);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glHint(3155, 4352);
	}

	public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 3042);
		GL11.glDisable((int) 3553);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glDepthMask((boolean) true);
		GL11.glEnable((int) 2848);
		GL11.glHint((int) 3154, (int) 4354);
		GL11.glHint((int) 3155, (int) 4354);
		GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
		drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
		drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
		drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
		drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
		drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
		drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
		drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
		drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
		Gui.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
		GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
		GL11.glEnable((int) 3553);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
		GL11.glDisable((int) 2848);
		GL11.glHint((int) 3154, (int) 4352);
		GL11.glHint((int) 3155, (int) 4352);
		Gui.drawRect(0, 0, 0, 0, 0);
	}

	public static void drawRoundedRect(float x, float y, float x1, float y1, int color) {
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 3042);
		GL11.glDisable((int) 3553);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glDepthMask((boolean) true);
		GL11.glEnable((int) 2848);
		GL11.glHint((int) 3154, (int) 4354);
		GL11.glHint((int) 3155, (int) 4354);
		GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
		drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, color);
		drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, color);
		drawHLine(x + 2.0f, x1 - 3.0f, y, color);
		drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, color);
		drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, color);
		drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, color);
		drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, color);
		drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, color);
		Gui.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, color);
		GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
		GL11.glEnable((int) 3553);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
		GL11.glDisable((int) 2848);
		GL11.glHint((int) 3154, (int) 4352);
		GL11.glHint((int) 3155, (int) 4352);
		Gui.drawRect(0, 0, 0, 0, 0);
	}

	public static void drawHLine(float x, float y, float x1, int y1) {
		if (y < x) {
			float var5 = x;
			x = y;
			y = var5;
		}
		RenderUtil.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
	}

	public static void drawVLine(float x, float y, float x1, int y1) {
		if (x1 < y) {
			float var5 = y;
			y = x1;
			x1 = var5;
		}
		RenderUtil.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
	}

	public static void drawblock(double a, double a2, double a3, int a4, int a5, float a6) {
		float a7 = (float) (a4 >> 24 & 255) / 255.0f;
		float a8 = (float) (a4 >> 16 & 255) / 255.0f;
		float a9 = (float) (a4 >> 8 & 255) / 255.0f;
		float a10 = (float) (a4 & 255) / 255.0f;
		float a11 = (float) (a5 >> 24 & 255) / 255.0f;
		float a12 = (float) (a5 >> 16 & 255) / 255.0f;
		float a13 = (float) (a5 >> 8 & 255) / 255.0f;
		float a14 = (float) (a5 & 255) / 255.0f;
		org.lwjgl.opengl.GL11.glPushMatrix();
		org.lwjgl.opengl.GL11.glEnable((int) 3042);
		org.lwjgl.opengl.GL11.glBlendFunc((int) 770, (int) 771);
		org.lwjgl.opengl.GL11.glDisable((int) 3553);
		org.lwjgl.opengl.GL11.glEnable((int) 2848);
		org.lwjgl.opengl.GL11.glDisable((int) 2929);
		org.lwjgl.opengl.GL11.glDepthMask((boolean) false);
		org.lwjgl.opengl.GL11.glColor4f((float) a8, (float) a9, (float) a10, (float) a7);
		drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
		org.lwjgl.opengl.GL11.glLineWidth((float) a6);
		org.lwjgl.opengl.GL11.glColor4f((float) a12, (float) a13, (float) a14, (float) a11);
		drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
		org.lwjgl.opengl.GL11.glDisable((int) 2848);
		org.lwjgl.opengl.GL11.glEnable((int) 3553);
		org.lwjgl.opengl.GL11.glEnable((int) 2929);
		org.lwjgl.opengl.GL11.glDepthMask((boolean) true);
		org.lwjgl.opengl.GL11.glDisable((int) 3042);
		org.lwjgl.opengl.GL11.glPopMatrix();
	}

	public static int reAlpha(int color, float alpha) {
		Color c = new Color(color);
		float r = 0.003921569F * c.getRed();
		float g = 0.003921569F * c.getGreen();
		float b = 0.003921569F * c.getBlue();
		return (new Color(r, g, b, alpha)).getRGB();
	}

	public static void scissor(double x, double y, double width, double height) {
		ScaledResolution sr = new ScaledResolution(mc);
		final double scale = sr.getScaleFactor();
		y = sr.getScaledHeight() - y;
		x *= scale;
		y *= scale;
		width *= scale;
		height *= scale;
		GL11.glScissor((int) x, (int) (y - height), (int) width, (int) height);
	}

	public static void drawFastRoundedRect(float x0, float y0, float x1, float y1, float radius, int color) {
		if (x0 == x1 || y0 == y1)
			return;
		final int Semicircle = 18;
		final float f = 90.0f / Semicircle;
		final float f2 = (color >> 24 & 0xFF) / 255.0f;
		final float f3 = (color >> 16 & 0xFF) / 255.0f;
		final float f4 = (color >> 8 & 0xFF) / 255.0f;
		final float f5 = (color & 0xFF) / 255.0f;
		GL11.glDisable(2884);
		GL11.glDisable(3553);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(f3, f4, f5, f2);
		GL11.glBegin(5);
		GL11.glVertex2f(x0 + radius, y0);
		GL11.glVertex2f(x0 + radius, y1);
		GL11.glVertex2f(x1 - radius, y0);
		GL11.glVertex2f(x1 - radius, y1);
		GL11.glEnd();
		GL11.glBegin(5);
		GL11.glVertex2f(x0, y0 + radius);
		GL11.glVertex2f(x0 + radius, y0 + radius);
		GL11.glVertex2f(x0, y1 - radius);
		GL11.glVertex2f(x0 + radius, y1 - radius);
		GL11.glEnd();
		GL11.glBegin(5);
		GL11.glVertex2f(x1, y0 + radius);
		GL11.glVertex2f(x1 - radius, y0 + radius);
		GL11.glVertex2f(x1, y1 - radius);
		GL11.glVertex2f(x1 - radius, y1 - radius);
		GL11.glEnd();
		GL11.glBegin(6);
		float f6 = x1 - radius;
		float f7 = y0 + radius;
		GL11.glVertex2f(f6, f7);
		int j = 0;
		for (j = 0; j <= Semicircle; ++j) {
			final float f8 = j * f;
			GL11.glVertex2f((float) (f6 + radius * Math.cos(Math.toRadians(f8))),
					(float) (f7 - radius * Math.sin(Math.toRadians(f8))));
		}
		GL11.glEnd();
		GL11.glBegin(6);
		f6 = x0 + radius;
		f7 = y0 + radius;
		GL11.glVertex2f(f6, f7);
		for (j = 0; j <= Semicircle; ++j) {
			final float f9 = j * f;
			GL11.glVertex2f((float) (f6 - radius * Math.cos(Math.toRadians(f9))),
					(float) (f7 - radius * Math.sin(Math.toRadians(f9))));
		}
		GL11.glEnd();
		GL11.glBegin(6);
		f6 = x0 + radius;
		f7 = y1 - radius;
		GL11.glVertex2f(f6, f7);
		for (j = 0; j <= Semicircle; ++j) {
			final float f10 = j * f;
			GL11.glVertex2f((float) (f6 - radius * Math.cos(Math.toRadians(f10))),
					(float) (f7 + radius * Math.sin(Math.toRadians(f10))));
		}
		GL11.glEnd();
		GL11.glBegin(6);
		f6 = x1 - radius;
		f7 = y1 - radius;
		GL11.glVertex2f(f6, f7);
		for (j = 0; j <= Semicircle; ++j) {
			final float f11 = j * f;
			GL11.glVertex2f((float) (f6 + radius * Math.cos(Math.toRadians(f11))),
					(float) (f7 + radius * Math.sin(Math.toRadians(f11))));
		}
		GL11.glEnd();
		GL11.glEnable(3553);
		GL11.glEnable(2884);
		GL11.glDisable(3042);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawRoundedRect(float x, float y, float x2, float y2, final float round, final int color) {
		x += (float) (round / 2.0f + 0.5);
		y += (float) (round / 2.0f + 0.5);
		x2 -= (float) (round / 2.0f + 0.5);
		y2 -= (float) (round / 2.0f + 0.5);
		Gui.drawRect((int) x, (int) y, (int) x2, (int) y2, color);
		circle(x2 - round / 2.0f, y + round / 2.0f, round, color);
		circle(x + round / 2.0f, y2 - round / 2.0f, round, color);
		circle(x + round / 2.0f, y + round / 2.0f, round, color);
		circle(x2 - round / 2.0f, y2 - round / 2.0f, round, color);
		Gui.drawRect((int) (x - round / 2.0f - 0.5f), (int) (y + round / 2.0f), (int) x2, (int) (y2 - round / 2.0f),
				color);
		Gui.drawRect((int) x, (int) (y + round / 2.0f), (int) (x2 + round / 2.0f + 0.5f), (int) (y2 - round / 2.0f),
				color);
		Gui.drawRect((int) (x + round / 2.0f), (int) (y - round / 2.0f - 0.5f), (int) (x2 - round / 2.0f),
				(int) (y2 - round / 2.0f), color);
		Gui.drawRect((int) (x + round / 2.0f), (int) y, (int) (x2 - round / 2.0f), (int) (y2 + round / 2.0f + 0.5f),
				color);
	}

	public static void circle(final float x, final float y, final float radius, final Color fill) {
		arc(x, y, 0.0f, 360.0f, radius, fill);
	}

	public static void circle(final float x, final float y, final float radius, final int fill) {
		arc(x, y, 0.0f, 360.0f, radius, fill);
	}

	public static void arc(final float x, final float y, final float start, final float end, final float radius,
			final int color) {
		arcEllipse(x, y, start, end, radius, radius, color);
	}

	public static void arc(final float x, final float y, final float start, final float end, final float radius,
			final Color color) {
		arcEllipse(x, y, start, end, radius, radius, color);
	}

	public static void arcEllipse(final float x, final float y, float start, float end, final float w, final float h,
			final int color) {
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
				final float ldx = (float) Math.cos(i * 3.141592653589793 / 180.0) * w * 1.001f;
				final float ldy = (float) Math.sin(i * 3.141592653589793 / 180.0) * h * 1.001f;
				GL11.glVertex2f(x + ldx, y + ldy);
			}
			GL11.glEnd();
			GL11.glDisable(2848);
		}
		GL11.glBegin(6);
		for (float i = end; i >= start; i -= 4.0f) {
			final float ldx = (float) Math.cos(i * 3.141592653589793 / 180.0) * w;
			final float ldy = (float) Math.sin(i * 3.141592653589793 / 180.0) * h;
			GL11.glVertex2f(x + ldx, y + ldy);
		}
		GL11.glEnd();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void arcEllipse(final float x, final float y, float start, float end, final float w, final float h,
			final Color color) {
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
		GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f,
				color.getAlpha() / 255.0f);
		if (color.getAlpha() > 0.5f) {
			GL11.glEnable(2848);
			GL11.glLineWidth(2.0f);
			GL11.glBegin(3);
			for (float i = end; i >= start; i -= 4.0f) {
				final float ldx = (float) Math.cos(i * 3.141592653589793 / 180.0) * w * 1.001f;
				final float ldy = (float) Math.sin(i * 3.141592653589793 / 180.0) * h * 1.001f;
				GL11.glVertex2f(x + ldx, y + ldy);
			}
			GL11.glEnd();
			GL11.glDisable(2848);
		}
		GL11.glBegin(6);
		for (float i = end; i >= start; i -= 4.0f) {
			final float ldx = (float) Math.cos(i * 3.141592653589793 / 180.0) * w;
			final float ldy = (float) Math.sin(i * 3.141592653589793 / 180.0) * h;
			GL11.glVertex2f(x + ldx, y + ldy);
		}
		GL11.glEnd();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static float smooth(float current, float end, float smoothSpeed, float minSpeed) {
		float movement = (end - current) * smoothSpeed;
		if (movement > 0) {
			movement = Math.max(minSpeed, movement);
			movement = Math.min(end - current, movement);
		} else if (movement < 0) {
			movement = Math.min(-minSpeed, movement);
			movement = Math.max(end - current, movement);
		}
		return current + movement;
	}

	public static void post3D() {
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		GL11.glColor4f(1, 1, 1, 1);
	}

	public static double interpolate(final double current, final double old, final double scale) {
		return old + (current - old) * scale;
	}

	public static void drawBorderRect(double x, double y, double x1, double y1, int color, double lwidth) {
		drawHLine(x, y, x1, y, (float) lwidth, color);
		drawHLine(x1, y, x1, y1, (float) lwidth, color);
		drawHLine(x, y1, x1, y1, (float) lwidth, color);
		drawHLine(x, y1, x, y, (float) lwidth, color);
	}

	public static void drawHLine(double x, double y, double x1, double y1, float width, int color) {
		float var11 = (color >> 24 & 0xFF) / 255.0F;
		float var6 = (color >> 16 & 0xFF) / 255.0F;
		float var7 = (color >> 8 & 0xFF) / 255.0F;
		float var8 = (color & 0xFF) / 255.0F;
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(var6, var7, var8, var11);
		GL11.glPushMatrix();
		GL11.glLineWidth(width);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x1, y1);
		GL11.glEnd();

		GL11.glLineWidth(1);

		GL11.glPopMatrix();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.color(1, 1, 1, 1);

	}

	public static void drawPlayerHead(final String playerName, final int x, final int y, final int width,
			final int height) {
		for (final Object player : Minecraft.getMinecraft().world.getLoadedEntityList()) {
			if (player instanceof EntityPlayer) {
				final EntityPlayer ply = (EntityPlayer) player;
				if (!playerName.equalsIgnoreCase(ply.getDisplayName().getUnformattedText())) {
					continue;
				}
				final GameProfile profile = new GameProfile(ply.getUniqueID(),
						ply.getDisplayName().getUnformattedText());
				final NetworkPlayerInfo networkplayerinfo1 = new NetworkPlayerInfo(profile);
				final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
				GL11.glDisable(2929);
				GL11.glEnable(3042);
				GL11.glDepthMask(false);
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
				Minecraft.getMinecraft().getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
				Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
				GL11.glDepthMask(true);
				GL11.glDisable(3042);
				GL11.glEnable(2929);
			}
		}
	}

	public static Color rainbow(long time, float count, float fade) {
		float hue = ((float) time + (-10.0F + count) * 2.0E8F) / 3.0E9F % -360.0F;
		long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 0.35F, 1.0F)), 16);
		Color c = new Color((int) color);
		return new Color((float) c.getRed() / 255.0F * fade, (float) c.getGreen() / 255.0F * fade,
				(float) c.getBlue() / 255.0F * fade, (float) c.getAlpha() / 255.0F);
	}

	public static double[] convertTo2D(final double x, final double y, final double z) {
		final FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
		final IntBuffer viewport = BufferUtils.createIntBuffer(16);
		final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
		final FloatBuffer projection = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(2982, modelView);
		GL11.glGetFloat(2983, projection);
		GL11.glGetInteger(2978, viewport);
		final boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport,
				screenCoords);
		double[] arrd3;
		if (result) {
			final double[] arrd2 = arrd3 = new double[] { screenCoords.get(0),
					Display.getHeight() - screenCoords.get(1), 0.0 };
			arrd2[2] = screenCoords.get(2);
		} else {
			arrd3 = null;
		}
		return arrd3;
	}

	public static int NMSLBlack() {

//		while (true) {

		int black = 0;

//			if (black == 0) {
				upTimer.reset();
				for (int i = 0; i < 230; i++) {
					if (addTimer.hasReached(i)) {
						black += 229;
					}
				}
//				return black;
//			}
			if (black == 230) {
				addTimer.reset();
				black = 230;
				for (int i = 0; i < 230; i++) {
					if (upTimer.hasReached(i)) {
						black -= 229;
					}
				}
				return black;
			}
			return 0;
//		}
	}

    public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor,
            int borderColor) {
rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
rectangle(x + width, y, x1 - width, y + width, borderColor);

GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
rectangle(x, y, x + width, y1, borderColor);

GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
rectangle(x1 - width, y, x1, y1, borderColor);

GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
rectangle(x + width, y1 - width, x1 - width, y1, borderColor);

GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
}
    public static void rectangle(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            double var5 = top;
            top = bottom;
            bottom = var5;
        }
        float var11 = (color >> 24 & 0xFF) / 255.0F;
        float var6 = (color >> 16 & 0xFF) / 255.0F;
        float var7 = (color >> 8 & 0xFF) / 255.0F;
        float var8 = (color & 0xFF) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, bottom, 0.0D).endVertex();
        worldRenderer.pos(right, bottom, 0.0D).endVertex();
        worldRenderer.pos(right, top, 0.0D).endVertex();
        worldRenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void renderItemStack(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().zLevel = -150.0f;
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, stack, x, y);
        Minecraft.getMinecraft().getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        double s = 0.5;
        GlStateManager.scale(s, s, s);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }
}
