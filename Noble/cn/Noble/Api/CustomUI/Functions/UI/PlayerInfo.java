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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

import cn.Noble.Api.CustomUI.HUDApi;
import cn.Noble.Font.FontLoaders;
import cn.Noble.GUI.ShaderBlur;
import cn.Noble.Util.render.ColorUtils;
import cn.Noble.Util.render.RenderUtil;

public class PlayerInfo extends HUDApi {

	public PlayerInfo() {
		super("PlayerInfo", 414, 413);
		this.setEnabled(true);
	}

	@Override
	public void onRender() {
		
		if (mc.player == null)
			return;
		
		ClientPlayerEntity player = mc.player;
		//health
		float heal = (int) player.getHealth();
		int hpColor = ColorUtils.getHealthColor(player.getHealth(), player.getMaxHealth()).getRGB();
		double hpPercentage = heal / player.getMaxHealth();
		hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
		
		double hpWidth = 130 * hpPercentage;
		//armor
		double arPercentage = player.getTotalArmorValue() / player.getMaxHealth();
		int arColor = ColorUtils.getHealthColor(player.getTotalArmorValue(), player.getMaxHealth()).getRGB();
		arPercentage = MathHelper.clamp_double(arPercentage, 0.0, 1.0);
		double arWidth = 85 * arPercentage;
		
		ShaderBlur.blurAreaBoarder(this.x, this.y, 130, 40, 70);
		RenderUtil.drawBorderedRect(x, y, x + 130, y + 40, 2, new Color(30, 30, 30, 80).getRGB(), new Color(198, 139, 255, 50).getRGB());
		//health
		RenderUtil.drawBorderedRect(x, y + 40, x + (float)hpWidth, y + 41, 2, hpColor, hpColor);
		//armor
		if(arWidth != 0)
			RenderUtil.drawBorderedRect(x + 40, y + 34, x + 40 + (float)arWidth, y + 35, 2, arColor, arColor);
		
		GlStateManager.color(1, 1, 1, 1);
		
		FontLoaders.NMSL18.drawString("HP: " + player.getHealth(), x + 40, y + 4, -1);
		
		mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(4), (int) (x + 38),
				(int) (y + 14));
		mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(3), (int) (x + 38 + 16),
				(int) (y + 14));
		mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(2), (int) (x + 38  + 2 * 16),
				(int) (y + 14));
		mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(1), (int) (x + 38  + 3 * 16),
				(int) (y + 14));
		this.mc.getRenderItem().zLevel = 0.0F;
		
		Object texture = ((AbstractClientPlayer) player).getLocationSkin();
		mc.getTextureManager().bindTexture((ResourceLocation) texture);
		Gui.drawScaledCustomSizeModalRect((int) (x + 3), (int) (y + 3.5), 8, 8, 8, 8, 33, 33, 64, 64);
		
		FontLoaders.NMSL18.drawString("Facing: " + mc.player.getHorizontalFacing().toString().toUpperCase(), x, y + 45, -1);
		FontLoaders.NMSL18.drawString("FPS: " + mc.getDebugFPS(), x + 67, y + 45, -1);
	}
	
	@Override
	public void InScreenRender() {
		if (mc.player == null)
			return;
		
		ClientPlayerEntity player = mc.player;
		//health
		float heal = (int) player.getHealth();
		int hpColor = ColorUtils.getHealthColor(player.getHealth(), player.getMaxHealth()).getRGB();
		double hpPercentage = heal / player.getMaxHealth();
		hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
		
		double hpWidth = 130 * hpPercentage;
		//armor
		double arPercentage = player.getTotalArmorValue() / player.getMaxHealth();
		int arColor = ColorUtils.getHealthColor(player.getTotalArmorValue(), player.getMaxHealth()).getRGB();
		arPercentage = MathHelper.clamp_double(arPercentage, 0.0, 1.0);
		double arWidth = 85 * arPercentage;
		
		ShaderBlur.blurAreaBoarder(this.x, this.y, 130, 40, 200);
		RenderUtil.drawBorderedRect(x, y, x + 130, y + 40, 2, new Color(30, 30, 30, 80).getRGB(), new Color(198, 139, 255, 50).getRGB());
		//health
		RenderUtil.drawBorderedRect(x, y + 40, x + (float)hpWidth, y + 41, 2, hpColor, hpColor);
		//armor
		if(arWidth != 0)
			RenderUtil.drawBorderedRect(x + 40, y + 34, x + 40 + (float)arWidth, y + 35, 2, arColor, arColor);
		
		GlStateManager.color(1, 1, 1, 1);
		
		FontLoaders.NMSL18.drawString("HP: " + player.getHealth(), x + 40, y + 4, -1);
		
		mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(4), (int) (x + 38),
				(int) (y + 14));
		mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(3), (int) (x + 38 + 16),
				(int) (y + 14));
		mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(2), (int) (x + 38  + 2 * 16),
				(int) (y + 14));
		mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(1), (int) (x + 38  + 3 * 16),
				(int) (y + 14));
		this.mc.getRenderItem().zLevel = 0.0F;
		
		Object texture = ((AbstractClientPlayer) player).getLocationSkin();
		mc.getTextureManager().bindTexture((ResourceLocation) texture);
		Gui.drawScaledCustomSizeModalRect((int) (x + 3), (int) (y + 3.5), 8, 8, 8, 8, 33, 33, 64, 64);

		FontLoaders.NMSL18.drawString("Facing: " + mc.player.getHorizontalFacing().toString().toUpperCase(), x, y + 45, -1);
		FontLoaders.NMSL18.drawString("FPS: " + mc.getDebugFPS(), x + 67, y + 45, -1);
	}
}
