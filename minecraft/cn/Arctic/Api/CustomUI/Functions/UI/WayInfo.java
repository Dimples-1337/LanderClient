package cn.Arctic.Api.CustomUI.Functions.UI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

import org.lwjgl.opengl.GL11;

import cn.Arctic.Api.CustomUI.HUDApi;
import cn.Arctic.Font.CFontRenderer;
import cn.Arctic.Font.FontLoaders;
import cn.Arctic.Util.math.MathUtil;
import cn.Arctic.Util.render.RenderUtil;

public class WayInfo extends HUDApi {

    int lastA = 0;
    int lastW = 0;
    int lastS = 0;
    int lastD = 0;
    double lastX = 0;
    double lastZ = 0;
    CFontRenderer ufr = FontLoaders.NMSL19;
	
	public WayInfo() {
		super("WayInfo", 5, 250);
		this.setEnabled(true);
	}

	@Override
	public void onRender() {
		motionRender();
	}
	
	@Override
	public void InScreenRender() {
		motionRender();
	}
	
	private void motionRender() {
		GL11.glEnable(GL11.GL_BLEND);
		RenderUtil.drawBorderedRect(x, y, x + 80, y + 80, 2, new Color(30, 30, 30, 190).getRGB(), new Color(30, 30, 30, 90).getRGB());
        double prevX = mc.player.posX - mc.player.prevPosX;
        double prevZ = mc.player.posZ - mc.player.prevPosZ;
        double lastDist = Math.sqrt(prevX * prevX + prevZ * prevZ);
        double motionX = mc.player.moveStrafing * (lastDist * 200);
        double motionZ = mc.player.moveForward  * (lastDist * 200);
        lastX += (motionX - lastX) / 4;
        lastZ += (motionZ - lastZ) / 4;
        RenderUtil.drawCircle(x + 40 + (int)lastX / 3, y + 40 + (int)lastZ / 3,  7, 50, new Color(255, 255, 255, 190).getRGB());
        RenderUtil.drawFilledCircle(x + 40, y + 40, 3, new Color(255, 255, 255, 190).getRGB());
        GL11.glDisable(GL11.GL_BLEND);
//        GlStateManager.color(1, 1, 1);

	    double xDist = mc.player.posX - mc.player.lastTickPosX;
	    double zDist = mc.player.posZ - mc.player.lastTickPosZ;
	    double moveSpeed = Math.sqrt(xDist * xDist + zDist * zDist) * 2;
	    double speed = MathUtil.round(moveSpeed * 10, 2);
        ufr.drawString(speed + "Block/sec", x + 10, y + 83, -1);
	}
}
