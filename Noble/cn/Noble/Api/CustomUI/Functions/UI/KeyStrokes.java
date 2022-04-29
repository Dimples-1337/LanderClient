package cn.Noble.Api.CustomUI.Functions.UI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

import org.lwjgl.opengl.GL11;

import cn.Noble.Api.CustomUI.HUDApi;
import cn.Noble.Font.CFontRenderer;
import cn.Noble.Font.FontLoaders;
import cn.Noble.Util.render.RenderUtil;

public class KeyStrokes extends HUDApi {

    int lastA = 0;
    int lastW = 0;
    int lastS = 0;
    int lastD = 0;
    double lastX = 0;
    double lastZ = 0;
    CFontRenderer ufr = FontLoaders.NMSL19;
	
	public KeyStrokes() {
		super("KeyStrokes", 66, 247);
		this.setEnabled(true);
	}

	@Override
	public void onRender() {
		keyRender();
	}
	
	@Override
	public void InScreenRender() {
		keyRender();
	}
	
	private void keyRender() {
		GameSettings set = mc.gameSettings;
		int press = new Color(230, 230, 230, 120).getRGB();
		int unPress = new Color(30, 30, 30, 120).getRGB();
		
		//rectColor
        int colorW = set.keyBindForward.pressed ? press : unPress;
        int colorS = set.keyBindBack.pressed ? press : unPress;
        int colorA = set.keyBindLeft.pressed ? press : unPress;
        int colorD = set.keyBindRight.pressed ? press : unPress;
        
        int colorL = set.keyBindAttack.pressed ? press : unPress;
        int colorR = set.keyBindUseItem.pressed ? press : unPress;
        
        //stringColor
        int colorW1 = set.keyBindForward.pressed ? unPress : press;
        int colorS1 = set.keyBindBack.pressed ? unPress : press;
        int colorA1 = set.keyBindLeft.pressed ? unPress : press;
        int colorD1 = set.keyBindRight.pressed ? unPress : press;
        
        int colorLMB = set.keyBindAttack.pressed ? unPress : press;
        int colorRMB = set.keyBindUseItem.pressed ? unPress : press;
        
        //w
        RenderUtil.drawBorderedRect(x, y, x + 27, y + 27, 2, colorW, colorW);
        FontLoaders.NMSL20.drawString("W", x + 9, y + 9, colorW1);
        //s
        RenderUtil.drawBorderedRect(x, y + 32, x + 27, y + 59, 2, colorS, colorS);
        FontLoaders.NMSL20.drawString("S", x + 10, y + 32 + 9, colorS1);
        //a
        RenderUtil.drawBorderedRect(x - 32, y + 32, x - 5, y + 59, 2, colorA, colorA);
        FontLoaders.NMSL20.drawString("A", x - 32 + 9, y + 32 + 9, colorA1);
        //d
        RenderUtil.drawBorderedRect(x + 32, y + 32, x + 59, y + 59, 2, colorD, colorD);
        FontLoaders.NMSL20.drawString("D", x + 32 + 9, y + 32 + 9, colorD1);
        //mouse left
        RenderUtil.drawBorderedRect(x - 32, y + 64, x + 12, y + 85, 2, colorL, colorL);
        FontLoaders.NMSL20.drawString("LMB", x - 32 + 12, y + 64 + 6, colorLMB);
        //mouse right
        RenderUtil.drawBorderedRect(x + 18, y + 64, x + 59, y + 85, 2, colorR, colorR);
        FontLoaders.NMSL20.drawString("RMB", x + 18 + 10, y + 64 + 6, colorRMB);
        
	}
	
    public int flop(int a, int b){
        return b - a;
    }
}
