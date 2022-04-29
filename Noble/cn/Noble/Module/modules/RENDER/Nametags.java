/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package cn.Noble.Module.modules.RENDER;

import java.awt.Color;
import java.util.ArrayList;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.src.Config;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cn.Noble.Client;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventPacketRecieve;
import cn.Noble.Event.events.EventRender3D;
import cn.Noble.Font.FontLoaders;
import cn.Noble.Manager.FriendManager;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Module.modules.COMBAT.AntiBot;
import cn.Noble.Module.modules.COMBAT.Aura;
import cn.Noble.Module.modules.PLAYER.Teams;
import cn.Noble.Util.render.ColorUtils;
import cn.Noble.Util.render.RenderUtil;
import cn.Noble.Util.render.gl.GLUtil;
import cn.Noble.Values.Option;

public class Nametags extends Module {

	private ArrayList<Entity> entities = new ArrayList();
	private int i = 0;
	private static String lol;
	private static boolean item;
	private static boolean armor;
	Option<Boolean> mcplayer = new Option<Boolean>("ShowYou", true);

	public Nametags() {
		super("Nametags", new String[] { "tags" }, ModuleType.Render);
		this.setEnabled(true);
		this.setColor(new Color(29, 187, 102).getRGB());
		this.addValues(mcplayer);
	}

	@EventHandler
	public void onRender(EventRender3D event) {
		Pattern COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
		for (Object o : mc.world.playerEntities) {
			EntityPlayer entity = (EntityPlayer) o;
			if (this.mcplayer.getValue() ? entity.isEntityAlive() : entity.isEntityAlive() && entity != mc.player) {
				double pX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks
						- mc.getRenderManager().renderPosX;
				double pY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks
						- mc.getRenderManager().renderPosY;
				double pZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks
						- mc.getRenderManager().renderPosZ;
				renderNameTag(entity,
						COLOR_PATTERN.matcher(entity.getDisplayName().getUnformattedText()).replaceAll(""), pX, pY, pZ);
			}

		}
		int OO = (int) mc.player.getDistance(0, 100, 0);
		double posX1 = 0 - mc.getRenderManager().renderPosX;
		double posY = 100 - mc.getRenderManager().renderPosY;
		double posZ = 0 - mc.getRenderManager().renderPosZ;
	}

	private void startDrawing(double x, double y, double z, double StringX, double StringY, double StringZ) {
		float var10001 = mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f;
		double size = Config.zoomMode ? (double) (getSize(StringX, StringY, StringZ) / 10.0f) * 1.6
				: (double) (getSize(StringX, StringY, StringZ) / 10.0f) * 4.8;
		GL11.glPushMatrix();
		RenderUtil.startDrawing();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glNormal3f((float) 0.0f, (float) 1.0f, (float) 0.0f);
		GL11.glRotatef((float) (-mc.getRenderManager().playerViewY), (float) 0.0f, (float) 1.0f, (float) 0.0f);
		GL11.glRotatef((float) mc.getRenderManager().playerViewX, (float) var10001, (float) 0.0f, (float) 0.0f);
		GL11.glScaled((double) (-0.01666666753590107 * size), (double) (-0.01666666753590107 * size),
				(double) (0.01666666753590107 * size));
	}

	private float getSize(double x, double y, double z) {
		return (float) (mc.player.getDistance(x, y, z)) / 4.0f <= 2.0f ? 2.0f
				: (float) (mc.player.getDistance(x, y, z)) / 4.0f;
	}

	private void stopDrawing() {
		RenderUtil.stopDrawing();
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		GlStateManager.popMatrix();
	}

	private void renderNameTag(EntityPlayer entity, String tag, double pX, double pY, double pZ) {
		if (!entity.isInvisible()) {
			Pattern COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
			FontRenderer fr = mc.fontRendererObj;
			float size = mc.player.getDistanceToEntity(entity) / 10.0f;
			if (size < 1.1f) {
				size = 1.1f;
			}
			pY += (entity.isSneaking() ? 0.5D : 0.7D);
			float scale = size * 1.8f;
			scale /= 100f;
			tag = EnumChatFormatting.getTextWithoutFormattingCodes(
					COLOR_PATTERN.matcher(entity.getDisplayName().getUnformattedText()).replaceAll(""));

			String teamPrefx = "";
			if (mc.world != null) {
				Scoreboard scoreboard = this.mc.world.getScoreboard();
				if (scoreboard.getPlayersTeam(entity.getCommandSenderEntity().getName()) != null) {
					teamPrefx = scoreboard.getPlayersTeam(entity.getCommandSenderEntity().getName()).getColorPrefix();
				}
			}

			String bot = "";
			AntiBot ab = new AntiBot();
			String team = "";
			String friend = "";
			if (FriendManager.isFriend(entity.getName())) {
				friend = EnumChatFormatting.DARK_PURPLE + "[Friend]";
			}
			if (ab.isEntityBot(entity)) {
				bot = "§3[Bot]";
			}
			if (Teams.isOnSameTeam(entity)) {
				team = "\247a[Team]";
			} else {
				team = "";
			}
			lol = friend + team + bot + teamPrefx + tag;
			double plyHeal = entity.getHealth();
			String hp = "HP:" + (int) plyHeal;
			String diString = (int) entity.getDistanceToEntity(mc.player) + "m ";
			GL11.glPushMatrix();
			GL11.glTranslatef((float) pX, (float) pY + 1.4F, (float) pZ);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0F, 2.0F, 0.0F);
			GL11.glRotatef(mc.getRenderManager().playerViewX, 2.0F, 0.0F, 0.0F);
			GL11.glScalef(-scale, -scale, scale);
			GLUtil.setGLCap(2896, false);
			GLUtil.setGLCap(2929, false);

//			float width2 = FontLoaders.NMSL20.getWidth(diString + lol) / 2 + 2.2f;
			float width2 = mc.fontRendererObj.getStringWidth(diString + lol) / 2 + 2.2f;
			float nw = -width2 - 2.2f;

			GLUtil.setGLCap(3042, true);
			GL11.glBlendFunc(770, 771);

			Color health = ColorUtils.getHealthColor(entity.getHealth(), entity.getMaxHealth());
			float yy = (item || armor) ? -30.0F : -18.0F;
			RenderUtil.drawBorderedRect(nw, yy, width2, -0.1f, 1, new Color(25, 25, 25, 11).getRGB(),
					new Color(25, 25, 25, 101).getRGB());
			RenderUtil.drawBorderedRect(nw, -1F, width2, -0.1f, 1, health.getRGB(), health.getRGB());

			FontLoaders.NMSL20.drawString(diString + lol, (int) (nw + 4.0f), (int) (-12f), -1);
//			mc.fontRendererObj.drawString(diString + lol, (int) (nw + 4.0f), (int) (-12f), -1);

			GL11.glPushMatrix();
			GL11.glScaled(0.6f, 0.6f, 0.6f);
			GL11.glScaled(1, 1, 1);
			GL11.glScaled(1.2d, 1.2d, 1.2d);

			int xOffset = 0;
			for (ItemStack armourStack : entity.inventory.armorInventory) {
				if (armourStack != null)
					xOffset -= 11;
			}
			Object renderStack;
			if (entity.getHeldItem() != null) {
				item = true;
				xOffset -= 8;
				renderStack = entity.getHeldItem().copy();
				if ((((ItemStack) renderStack).hasEffect())
						&& (((((ItemStack) renderStack).getItem() instanceof ItemTool))
								|| ((((ItemStack) renderStack).getItem() instanceof ItemArmor))))
					((ItemStack) renderStack).stackSize = 1;
				renderItemStack((ItemStack) renderStack, xOffset, -39);
				xOffset += 20;
			} else {
				item = false;
			}
			for (ItemStack armourStack : entity.inventory.armorInventory) {
				if (armourStack != null) {
					armor = true;
					ItemStack renderStack1 = armourStack.copy();
					if ((renderStack1.hasEffect()) && (((renderStack1.getItem() instanceof ItemTool))
							|| ((renderStack1.getItem() instanceof ItemArmor))))
						renderStack1.stackSize = 1;
					renderItemStack(renderStack1, xOffset, -39);
					xOffset += 20;
				} else {
					armor = false;
				}
			}
			GL11.glPopMatrix();
			GLUtil.revertAllCaps();
			GL11.glPopMatrix();
		}

	}

	public void renderItemStack(ItemStack stack, int x, int y) {
		GL11.glPushMatrix();
		GL11.glDepthMask(true);
		GlStateManager.clear(256);
		net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
		this.mc.getRenderItem().zLevel = -150.0F;
		whatTheFuckOpenGLThisFixesItemGlint();
		this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
		this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, stack, x, y);
		this.mc.getRenderItem().zLevel = 0.0F;
		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
		GlStateManager.disableCull();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.disableLighting();
		GlStateManager.scale(0.5D, 0.5D, 0.5D);
		GlStateManager.disableDepth();
		GlStateManager.enableDepth();
		GlStateManager.scale(2.0F, 2.0F, 2.0F);
		GL11.glPopMatrix();
	}

	private void whatTheFuckOpenGLThisFixesItemGlint() {
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableTexture2D();
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
	}

	public void drawBorderedRectNameTag(final float x, final float y, final float x2, final float y2, final float l1,
			final int col1, final int col2) {
		RenderUtil.drawRect(x, y, x2, y2, col2);
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
}
