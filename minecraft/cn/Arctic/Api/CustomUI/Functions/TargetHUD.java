package cn.Arctic.Api.CustomUI.Functions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cn.Arctic.Api.CustomUI.HUDApi;
import cn.Arctic.Font.CFontRenderer;
import cn.Arctic.Font.FontLoaders;
import cn.Arctic.Font.me.superskidder.FontLoaders2;
import cn.Arctic.GUI.ShaderBlur;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.modules.COMBAT.Aura;
import cn.Arctic.Module.modules.GUI.HUD;
import cn.Arctic.Util.math.MathUtil;
import cn.Arctic.Util.render.RenderUtil;

public class TargetHUD extends HUDApi {
	public static float AnimotaiX;
	public static float AnimotaiSpeed;
	   float anim;
	public TargetHUD() {
		super("TargetHUD", 5, 250);
		this.setEnabled(true);
	}

	@Override
	public void onRender() {
		motionRender();
	}
	
	@Override
	public void InScreenRender() {
        RenderUtil.drawRoundedRect(x, y, x + 100, y + 36, 2.5f, new Color(255, 255, 255).getRGB());
        FontLoaders.NMSL18.drawString("TargetHUD",x + 2,y + 5,new Color(0,0,0).getRGB());
	}
	
	private void motionRender() {
		EntityLivingBase pl = null;
		if (Aura.curTarget == null) {
			if (mc.currentScreen instanceof GuiChat) {
				pl = mc.player;
			}
		} else {
			pl = Aura.curTarget;
		}

		if (Aura.curTarget == null && !(mc.currentScreen instanceof GuiChat) || pl == null) return;
		int rainbowTick=0;
        rainbowTick+=100;
        double healthwidth=50;
        double saa=105;
        EntityLivingBase entity = Aura.curTarget;
        int rainbow2 = RenderUtil.astolfoRainbow(1,10,rainbowTick);
		ShaderBlur.blurAreaBoarder(this.x, this.y, 120, 40, 70);
		try {

			NetworkPlayerInfo networkplayerinfo1 = mc.player.sendQueue.getPlayerInfo(pl.getUniqueID());
			mc.getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
			Gui.drawScaledCustomSizeModalRect(x + 5, y + 5, 8F, 8F, 8, 8, 32, 32, 64F, 64F);

			if (((EntityPlayer) pl).isWearing(EnumPlayerModelParts.HAT)) {
				Gui.drawScaledCustomSizeModalRect(x + 5, y + 5, 8F, 8F, 8, 8, 32, 32, 64F, 64F);
			}
		} catch (Exception e) {
			RenderUtil.drawPlayerHead(pl.getName(),x + 5,y + 5,8,8);
		}
		RenderUtil.drawBorderedRect(x, y, x + 120, y + 40,3, rainbow2, new Color(0,0,0,80).getRGB());
		//Name
		FontLoaders2.msFont18.drawString(pl.getName(),this.x+40,this.y+5,new Color(255,255,255).getRGB());
		
	     double data1 = pl.getTotalArmorValue()*5.0;
	        String php1 = String.format("%.1f",data1);
	        RenderUtil.drawRect((float) (this.x + saa-65), this.y + 27, this.x + saa + healthwidth-40, this.y + 35, new Color(0, 0, 0,140).getRGB());
	        RenderUtil.drawGradientSideways(this.x + saa-65, this.y + 27, this.x + saa-65+pl.getTotalArmorValue()*3.74, this.y + 35,new Color(63, 122,223,100).getRGB(), new Color(63, 122,223).getRGB());
	        FontLoaders2.msFont14.drawString(php1+"%",this.x+62,this.y+27,new Color(255,255,255).getRGB());
	        
	        double data = pl.getHealth()*5.0;
	        String php = String.format("%.1f",data);
	         
	        RenderUtil.drawRect((float) (this.x + saa-65), this.y + 17, this.x + saa + healthwidth-40, this.y + 25, new Color(0, 0, 0,140).getRGB());
	        //RenderUtil.drawRect((float) (this.x + saa +anim-60), this.y + 17, this.x + saa + AnimotaiSpeed-40, this.y + 25,new Color(55+Aura.curTarget.hurtTime * 20, 00, 0).getRGB());
	        RenderUtil.drawGradientSideways(this.x + saa-65, this.y + 17, this.x + saa+anim-40, this.y + 25,new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),100).getRGB(), new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue()).getRGB());
	        FontLoaders2.msFont14.drawString(php+"%",this.x+62,this.y+17,new Color(255,255,255).getRGB());
	        anim=(float) (anim>healthwidth ?healthwidth * entity.getHealth() / entity.getMaxHealth(): (float) RenderUtil.getAnimationState(anim, healthwidth * entity.getHealth() / entity.getMaxHealth(), 6));
	       AnimotaiSpeed =(float) (AnimotaiSpeed>healthwidth ?healthwidth * entity.getHealth() / entity.getMaxHealth(): (float) RenderUtil.getAnimationState(AnimotaiSpeed, healthwidth * entity.getHealth() / entity.getMaxHealth(),9-Aura.curTarget.hurtTime*0.8));
	
	}
}
