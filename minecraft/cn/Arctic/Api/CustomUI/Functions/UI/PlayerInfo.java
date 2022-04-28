package cn.Arctic.Api.CustomUI.Functions.UI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.main.Main;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import cn.Arctic.Api.CustomUI.HUDApi;
import cn.Arctic.Font.FontLoaders;
import cn.Arctic.Font.me.superskidder.FontLoaders2;
import cn.Arctic.GUI.ShaderBlur;
import cn.Arctic.Util.render.ColorUtils;
import cn.Arctic.Util.render.RenderUtil;
import cn.Arctic.Util.render.RenderUtils;

public class PlayerInfo extends HUDApi {
	public static long sb = System.currentTimeMillis();
	public PlayerInfo() {
		super("PlayerInfo", 414, 413);
		this.setEnabled(true);
	}
	public static String getRemoteIp() {
        String serverIp = "Singleplayer";

        if (Minecraft.getMinecraft().world.isRemote) {
            final ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
            if(serverData != null)
                serverIp = serverData.serverIP;
        }

        return serverIp;
    }
	

	@Override
	public void onRender() {
		long time2=System.currentTimeMillis( ) - sb;
		int days = (int)((time2 /= 1000L) / 86400L);
		 int hours = (int)((time2 -= (long)(86400 * days)) / 3600L);
	        int minutes = (int)((time2 -= (long)(3600 * hours)) / 60L);
	        int seconds = (int)(time2 -= (long)(60 * minutes));
		
	        
		if (mc.player == null)
			return;
		
		ClientPlayerEntity player = mc.player;
		Main main = new Main();
		//health
		float heal = (int) player.getHealth();
//		int hpColor = ColorUtils.getHealthColor(player.getHealth(), player.getMaxHealth()).getRGB();
    	double hpPercentage = heal / player.getMaxHealth();
//		hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
		
		double hpWidth = 145 * hpPercentage;
		//armor
//		double arPercentage = player.getTotalArmorValue() / player.getMaxHealth();
//		int arColor = ColorUtils.getHealthColor(player.getTotalArmorValue(), player.getMaxHealth()).getRGB();
//		arPercentage = MathHelper.clamp_double(arPercentage, 0.0, 1.0);
		//double arWidth = 85 * arPercentage;
		int rainbowTick=0;
        rainbowTick+=100;
        int rainbow2 = RenderUtil.astolfoRainbow(1,10,rainbowTick);
		ShaderBlur.blurAreaBoarder(this.x, this.y, 145, 40, 70);
		RenderUtil.drawBorderedRect(x, y, x + 145, y + 40,3, rainbow2, new Color(0,0,0,80).getRGB());
		
		//health
		
		//RenderUtil.drawBorderedRect(x, y, x + 145, y -1, 2, rainbow2, rainbow2);
		//armor
//		if(arWidth != 0)
//			RenderUtil.drawBorderedRect(x + 40, y + 40, x + 40 + (float)arWidth, y + 35, 2, arColor, arColor);
		
		GlStateManager.color(1, 1, 1, 1);
		FontLoaders2.msFont16.drawString("Name: " + player.getName(), x + 3, y + 3, -1);
		FontLoaders2.msFont16.drawString("Sevrer: " + getRemoteIp(), x + 3, y + 12, -1);
		if(getRemoteIp()!="Singleplayer") {
		FontLoaders2.msFont16.drawString("Play Time: " + new SimpleDateFormat(hours+":"+minutes+":"+seconds).format(new Date()), x + 3, y+21, -1);
		} else {
			FontLoaders2.msFont16.drawString("PlayerTime: " + "Please enter the server", x + 3, y + 21, -1);
		}
		FontLoaders2.msFont16.drawString("System Time: " + new SimpleDateFormat("yyyy:HH:mm:ss").format(new Date()), x + 3, y+30, -1);
		
//		mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(4), (int) (x + 38),
//				(int) (y + 14));
//		mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(3), (int) (x + 38 + 16),
//				(int) (y + 14));
//		mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(2), (int) (x + 38  + 2 * 16),
//				(int) (y + 14));
//		mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(1), (int) (x + 38  + 3 * 16),
//				(int) (y + 14));
//		this.mc.getRenderItem().zLevel = 0.0F;
		
		Object texture = ((AbstractClientPlayer) player).getLocationSkin();
		mc.getTextureManager().bindTexture((ResourceLocation) texture);
		//Gui.drawScaledCustomSizeModalRect((int) (x + 3), (int) (y + 3.5), 8, 8, 8, 8, 33, 33, 64, 64);
		
	}
	
	@Override
	public void InScreenRender() {
		onRender();
		
}
}
