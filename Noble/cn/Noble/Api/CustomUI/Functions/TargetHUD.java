package cn.Noble.Api.CustomUI.Functions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

import cn.Noble.Client;
import cn.Noble.Api.CustomUI.HUDApi;
import cn.Noble.Api.CustomUI.HUDScreen;
import cn.Noble.Font.CFontRenderer;
import cn.Noble.Font.FontLoaders;
import cn.Noble.GUI.AnimationUtils;
import cn.Noble.Manager.ModuleManager;
import cn.Noble.Module.modules.COMBAT.Aura;
import cn.Noble.Util.Timer.Stopwatch;
import cn.Noble.Util.render.ColorUtils;
import cn.Noble.Util.render.RenderUtil;

public class TargetHUD extends HUDApi {

	private final Stopwatch animationStopwatch = new Stopwatch();
    public static EntityLivingBase curTarget = Aura.curTarget;
	private double healthBarWidth;
	public double hue;

    public TargetHUD() {
        super("TargetHUD",100,150);
        this.setEnabled(true);
    }

    @Override
    public void onRender() {
        if (Aura.curTarget == null || (!Client.instance.getModuleManager().getModuleByClass(Aura.class).isEnabled() && !(Minecraft.getMinecraft().currentScreen instanceof HUDScreen))) {
            return;
        }
        
		if (Aura.curTarget != null) {
				if(Aura.curTarget instanceof EntityPlayer) {
				Melody();
				} else {
					Melody();
		}
	}    
}
    
	private void Melody2() {
		Object texture;
		EntityLivingBase player = Aura.curTarget;
		float heal = (int) player.getHealth();

		FontRenderer fr = mc.fontRendererObj;
		Aura ka = (Aura) Client.instance.getModuleManager().getModuleByClass(Aura.class);
		if (player instanceof EntityLivingBase && player != null && ka.isEnabled()) {

			ScaledResolution sr = new ScaledResolution(mc);
			int healthColor = ColorUtils.getHealthColor(player.getHealth(), player.getMaxHealth()).getRGB();
			double hpPercentage = heal / player.getMaxHealth();
			hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
			double hpWidth = 100 * hpPercentage;
			double arPercentage = player.getTotalArmorValue() / player.getMaxHealth();
			arPercentage = MathHelper.clamp_double(arPercentage, 0.0, 1.0);
			double arWidth = 152 * arPercentage;

			String name = player.getName();
			double h = this.hue + 20;
			double h2 = this.hue + 100;
			if (h > 255.0f) {
				h = 0.0f;
			}
			if (h2 > 255.0f) {
				h2 -= 255.0f;
			}
			Color color33 = Color.getHSBColor((float) (h / 255.0f), (float) 0.9f, (float) 1.0f);
			Color color332 = Color.getHSBColor((float) (h2 / 255.0f), (float) 0.9f, (float) 1.0f);
			int color1 = color33.getRGB();
			int color2 = color332.getRGB();
			int color3 = ((int) (healthColor * hpPercentage));
			float x = this.x;
			float y = this.y;

			RenderUtil.drawBorderedRect(x-45, y, x + 115.0f, y + 46.0f, 0.5f, new Color(200, 190, 190, 255).getRGB(),
					new Color(30, 30, 30, 255).getRGB());

			RenderUtil.drawBorderedRect(x, y + 37.8f, (float) (x + hpWidth), y + 34.8f, 0.5f, (int) color3,
					(int) healthColor);
			
			RenderUtil.drawBorderedRect(x, y + 14.9f, (float) (x + 100), y + 31.1f, 0.5f, new Color(150,150,150,100).getRGB(),
					new Color(150,150,150,30).getRGB());
			
			RenderUtil.drawBorderedRect(x-40, y + 41.0f, (float) (x + 150)-40, y + 42.8f, 0.5f, Color.DARK_GRAY.getRGB(),
					Color.DARK_GRAY.getRGB());
			
			RenderUtil.drawBorderedRect(x-40, y + 41.0f, (float) (x + arWidth)-40, y + 42.8f, 0.5f, Color.cyan.getRGB(),
					Color.cyan.getRGB());

			mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(0), (int) (x + 2), (int) (y + 15));
			mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(4), (int) (x + 0 + 1 * 20), (int) (y + 15));
			mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(3), (int) (x + 0 + 2 * 20), (int) (y + 15));
			mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(2), (int) (x + 0 + 3 * 20), (int) (y + 15));
			mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(1), (int) (x + 0 + 4 * 20), (int) (y + 15));
			this.mc.getRenderItem().zLevel = 0.0F;

			fr.drawStringWithShadow(name + "(" + heal + ")", x + 0.0f, y + 2.0f, -1);
			texture = ((AbstractClientPlayer) player).getLocationSkin();
			mc.getTextureManager().bindTexture((ResourceLocation) texture);

			Gui.drawScaledCustomSizeModalRect((int) (x - 38.0), (int) (y + 5.5), 8, 8, 8, 8, 33, 33, 64, 64);

		} else {
			player = null;
		}

	}

	private void Melody() {
		if (Client.instance.getModuleManager().getModuleByClass(Aura.class).isEnabled() && !Aura.curTarget.isDead) {
			FontRenderer font2 = this.mc.fontRendererObj;
			
			float health = Aura.curTarget.getHealth();
			double hpPercentage = health / Aura.curTarget.getMaxHealth();
			hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
			double hpWidth = 92.0 * hpPercentage;
			int healthColor = ColorUtils.getHealthColor(Aura.curTarget.getHealth(), Aura.curTarget.getMaxHealth())
					.getRGB();
					
			int armorColor = ColorUtils.getArmorColor(Aura.curTarget.getTotalArmorValue(), Aura.curTarget.getMaxHealth()).getRGB();
			
			Object texture;
			
			float right = this.x;
			float height = this.y + 70;
			
			if (Aura.curTarget != null) {
				if (this.animationStopwatch.elapsed(15L)) {
					this.healthBarWidth = AnimationUtils.animate(hpWidth, this.healthBarWidth, 0.353f);
					this.animationStopwatch.reset();
				}
				//BG
				Gui.drawRect(right, height - 50, right + 130, height - 90, new Color(0, 0, 0, 180).getRGB());
				
				font2.drawString(Aura.curTarget.getName(), right + 30, height - 90, 16777215);
				font2.drawString(
						"HP:" + (int) ((EntityLivingBase) Aura.curTarget).getHealth() + "/"
								+ (int) ((EntityLivingBase) Aura.curTarget).getMaxHealth() + " " + "Hurt:"
								+ (Aura.curTarget.hurtTime > 0),
						right + 30, height - 72, new Color(255, 255, 255).getRGB());
				font2.drawString("Dist: " + mc.player.getDistanceToEntity(Aura.curTarget), right + 30, height - 63,
						new Color(255, 255, 255).getRGB());
				RenderUtil.drawEntityOnScreen((int) (right + 14), (int) (height - 54), (int) 15, (float) 2.0f,
						(float) 15.0f, (EntityLivingBase) ((EntityLivingBase) Aura.curTarget));
				RenderUtil.drawRoundedRect(right + 30.0f, height - 72.0f, right + 28.0f + (float) this.healthBarWidth,
						height - 77.5f, healthColor, healthColor);
				RenderUtil.drawRoundedRect(right, height - 48.5f, right + 130.0f, height - 50.5f, armorColor,
						armorColor);
				RenderUtil.drawRoundedRect(right - 1, height - 49, right + 131, height - 91,
						new Color(0, 0, 0, 0).getRGB(), new Color(255, 0, 0, 0).getRGB());
			}
		}
	}

    @Override
    public void InScreenRender(){
        RenderUtil.drawRoundedRect(x, y, x + 100, y + 36, 2.5f, new Color(255, 255, 255).getRGB());
        FontLoaders.NMSL18.drawString("TargetHUD",x + 2,y + 5,new Color(0,0,0).getRGB());
    }
}
