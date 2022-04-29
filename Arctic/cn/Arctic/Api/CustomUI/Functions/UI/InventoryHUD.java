package cn.Arctic.Api.CustomUI.Functions.UI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

import org.lwjgl.opengl.GL11;

import cn.Arctic.Api.CustomUI.HUDApi;
import cn.Arctic.Font.CFontRenderer;
import cn.Arctic.Font.FontLoaders;
import cn.Arctic.Font.me.superskidder.FontLoaders2;
import cn.Arctic.GUI.ShaderBlur;
import cn.Arctic.Module.modules.GUI.HUD;
import cn.Arctic.Util.math.MathUtil;
import cn.Arctic.Util.render.RenderUtil;

public class InventoryHUD extends HUDApi {

    int lastA = 0;
    int lastW = 0;
    int lastS = 0;
    int lastD = 0;
    double lastX = 0;
    double lastZ = 0;
    CFontRenderer ufr = FontLoaders.NMSL19;
	
	public InventoryHUD() {
		super("InventoryHUD", 5, 250);
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
	   	 CFontRenderer font4 = FontLoaders.NMSL16;
	        Color fillWithOpacity = new Color(180, 180, 180, 180);
	        Color outlineWithOpacity = new Color(250, 200, 200, 255);
	        //shaded box
	        ShaderBlur.blurAreaBoarder(this.x+1,this.y+1, 160, 54, 70);
	        RenderUtil.drawRect(this.x + 1, this.y - 12, this.x + 161, this.y+1,new Color(0, 0, 0).getRGB());
	        RenderUtil.drawGradientSideways(this.x + 1, this.y - 12, this.x + 161, this.y+1, new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),140).getRGB(), new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue()).getRGB());
	        Gui.drawRect(this.x + 1, this.y - 12, this.x + 20, this.y+1,new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),200).getRGB());
	        FontLoaders2.msFont18.drawString("Inventory HUD",this.x+50,this.y-9,Color.white.getRGB());
	        FontLoaders.click24.drawString("0",this.x+4,this.y-8, new Color(255, 255, 255).getRGB());

	        float boxWidth = 165;
	        RenderUtil.rectangle(this.x + 1, this.y + 1, this.x + 161, this.y + 55, new Color(0,0,0,80).getRGB());
	        
//	        RenderUtil.rectangleBordered(this.x+18 - 0.5D, this.y +2 - 0.3D, this.x -146+ boxWidth + 0.5D, this.y + 54 + 0.3D, 0.5D, Colors.getColor(255), Colors.getColor(10));
//	        RenderUtil.rectangleBordered(this.x+37 - 0.5D, this.y +2 - 0.3D, this.x -130+ boxWidth + 0.5D, this.y + 54 + 0.3D, 0.5D, Colors.getColor(255), Colors.getColor(10));
//	        RenderUtil.rectangleBordered(this.x+55 - 0.5D, this.y +2 - 0.3D, this.x -112+ boxWidth + 0.5D, this.y + 54 + 0.3D, 0.5D, Colors.getColor(255), Colors.getColor(10));
//	        RenderUtil.rectangleBordered(this.x+73 - 0.5D, this.y +2 - 0.3D, this.x -94+ boxWidth + 0.5D, this.y + 54 + 0.3D, 0.5D, Colors.getColor(255), Colors.getColor(10));
//	        RenderUtil.rectangleBordered(this.x+91 - 0.5D, this.y +2 - 0.3D, this.x -76+ boxWidth + 0.5D, this.y + 54 + 0.3D, 0.5D, Colors.getColor(255), Colors.getColor(10));
//	        RenderUtil.rectangleBordered(this.x+108 - 0.5D, this.y +2 - 0.3D, this.x -56+ boxWidth + 0.5D, this.y + 54 + 0.3D, 0.5D, Colors.getColor(255), Colors.getColor(10));
//	        RenderUtil.rectangleBordered(this.x+126 - 0.5D, this.y +2 - 0.3D, this.x -38+ boxWidth + 0.5D, this.y + 54 + 0.3D, 0.5D, Colors.getColor(255), Colors.getColor(10));
//	        RenderUtil.rectangleBordered(this.x+144 - 0.5D, this.y +2 - 0.3D, this.x -20+ boxWidth + 0.5D, this.y + 54 + 0.3D, 0.5D, Colors.getColor(255), Colors.getColor(10));
//	        RenderUtil.rectangleBordered(this.x+2 - 0.5D, this.y +19 - 0.3D, this.x -5+ boxWidth + 0.5D, this.y + 20 + 0.3D, 0.5D, Colors.getColor(255), Colors.getColor(10));
//	        RenderUtil.rectangleBordered(this.x+2 - 0.5D, this.y +39 - 0.3D, this.x -5+ boxWidth + 0.5D, this.y + 37 + 0.3D, 0.5D, Colors.getColor(255), Colors.getColor(10));
	        
	        //RenderUtil.rectangle((double)((float)(sr.getScaledWidth() / 2 - boxWidth + 6) + 4.5F), (double)(yOffset / 4 + boxHeight + 8), (double)(sr.getScaledWidth() / 2 - boxWidth + 35), (double)(yOffset / 4 + boxHeight + 9), Colors.getColor(17));
	        //items
	        GlStateManager.pushMatrix();
	        RenderHelper.enableGUIStandardItemLighting();
	        ItemStack[] items = mc.player.inventory.mainInventory;
	        for (int size = items.length, item = 9; item < size; ++item) {
	            final int slotX = (int) (this.x + (item) % 9 * 18);
	            final int slotY = (int) (this.y + 2 + (item / 9 - 1) * 18);
	            this.mc.getRenderItem().renderItemOverlays(FontLoaders2.msFont14, items[item], slotX, slotY);
	            this.mc.getRenderItem().renderItemAndEffectIntoGUI(items[item], slotX + 1, slotY);
	        }
	        RenderHelper.disableStandardItemLighting();
	        mc.getRenderItem().zLevel = 0.0F;
	        GlStateManager.popMatrix();
	}
}
