/*
 * Decompiled with CFR 0_132.
 */
/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Module.modules.GUI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.ibm.icu.text.SimpleDateFormat;

import cn.Noble.Client;
import cn.Noble.MamagerRegistry;
import cn.Noble.Api.CustomUI.HUDManager;
import cn.Noble.Api.CustomUI.Functions.PacketGraph;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventChat;
import cn.Noble.Event.events.EventKey;
import cn.Noble.Event.events.EventPacketRecieve;
import cn.Noble.Event.events.EventPacketSend;
import cn.Noble.Event.events.EventRender2D;
import cn.Noble.Font.CFontRenderer;
import cn.Noble.Font.FontLoaders;
import cn.Noble.GUI.ShaderBlur;
import cn.Noble.Manager.ModuleManager;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.Colors;
import cn.Noble.Util.Chat.Helper;
import cn.Noble.Util.Timer.TimerUtil;
import cn.Noble.Util.math.MathUtil;
import cn.Noble.Util.render.ColorUtils;
import cn.Noble.Util.render.FadeUtil;
import cn.Noble.Util.render.RenderUtil;
import cn.Noble.Util.render.RenderUtils;
import cn.Noble.Values.Mode;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.optifine.player.CapeUtils;

public class HUD extends Module {
	public static enum FontMode {
		Normal, CFontBold, ConsolasBold, Unknown;
	}

	public static float hue;

	private PacketGraph packetGraph = (PacketGraph) HUDManager.getApiByName("PacketGraph");

	public Option<Boolean> wc = new Option<Boolean>("WorldColor", false);
	public static Numbers<Double> wr = new Numbers<Double>("WorldRed", 255.0, 0.0, 255.0, 1.0);
	public static Numbers<Double> wg = new Numbers<Double>("WorldGreen", 0.0, 0.0, 255.0, 1.0);
	public static Numbers<Double> wb = new Numbers<Double>("WorldBlue", 0.0, 0.0, 255.0, 1.0);
	public static Option<Boolean> arrayLeft = new Option<Boolean>("LeftArrayList", true);
	public static Option<Boolean> suffix = new Option<Boolean>("Suffix", true);
	public static Option<Boolean> rendertabui = new Option<Boolean>("TabGui", true);
	public static Option<Boolean> chat = new Option<Boolean>("AlphaChat", true);
	public static Mode<Enum> font = new Mode("Font", (Enum[]) FontMode.values(), (Enum) FontMode.Normal);

	public HUD() {
		super("HUD", new String[] { "gui" }, ModuleType.Render);
		this.setEnabled(true);
		this.setRemoved(true);
		this.addValues(font, rendertabui, chat, arrayLeft, suffix, wc, wr, wg, wb);
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@EventHandler
	public void Render2d(final EventRender2D e) {
//		ShaderBlur.blurAreaBoarder(200, 200, 60, 60, 100);
////		RenderUtils.drawLoadingCircle(100, 100);
////		RenderUtil.drawRect(200F, 200D, 260D, 260D, new Color(200, 200, 200, 50).getRGB());
//		
//		FontLoaders.CNMD45.drawString("B", 222, 206, Colors.YELLOW.c);
//		FontLoaders.CNMD45.drawString("B", 206, 206, Colors.WHITE.c);
//		FontLoaders.CNMD45.drawString("M", 215, 227, Colors.AQUA.c);
//		FontLoaders.CNMD45.drawString("C", 227, 227, Colors.AQUA.c);
		
		Client.instance.drawNotifications();

		hue += (float) 6.0 / 5.0f;
		if (hue > 255.0f) {
			hue = 0.0f;
		}
	}

	@EventHandler
	public void onPacketGraphServer(EventPacketRecieve event) {
		if (event.getPacket() instanceof Packet) {
			PacketGraph.onServerPacket();
		}
	}

	@EventHandler
	public void onPacketGraphClient(EventPacketSend event) {
		if (event.getPacket() instanceof Packet) {
			PacketGraph.onClientPacket();
		}
	}

	@EventHandler
	private void onRenderInfo(EventRender2D event) {
//		this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
		ScaledResolution mainWindow = mc.getMainWindow();
		float infoY = (mc.currentScreen != null && (mc.currentScreen instanceof GuiChat)) ? -15 : -2;
		
		if(mc.gameSettings.showDebugInfo) return;
		
		FontLoaders.CNMD45.drawString(Client.name, 10, 8, FadeUtil.fade(FadeUtil.PURPLE.getColor()).getRGB());
		
		FontLoaders.NMSL18.drawString(
				"X: " + (int) mc.player.posX + "  Y: " + (int) mc.player.posY + "  Z: " + (int) mc.player.posZ, 3,
				mainWindow.getScaledHeight() - 10 + infoY, -1);

		this.drawModuleArrayList();
		
		
		this.drawPotionStatus(mc.getMainWindow());
	}
	
	private void drawModuleArrayList() {
		CFontRenderer fontRenderer = FontLoaders.CNMD18;
		int scaledWidth = 13;
		int index = 35;

		ArrayList<Module> displayModules = new ArrayList<>();
		Client.instance.getModuleManager();
		for (Module m : ModuleManager.getModules()) {
			if (m.isEnabled()) {
				displayModules.add(m);
			}
		}
		displayModules.sort((o1, o2) -> (int) fontRenderer.getStringWidth(o2.getName() + o2.getSuffix())
				- (int) fontRenderer.getStringWidth(o1.getName() + o1.getSuffix()));

		for (Module module : displayModules) {
			
			if (HUD.arrayLeft.getValue())
				fontRenderer.drawString(module.getName() + module.getSuffix(), scaledWidth - 2, index,
						new Color(255, 255, 255, 188).getRGB());

			else
				fontRenderer.drawString(module.getName() + module.getSuffix(),
						scaledWidth - fontRenderer.getStringWidth(module.getName() + module.getSuffix()) - 2, index,
						new Color(255, 255, 255, 188).getRGB());

			index += fontRenderer.getHeight() + 3;
		}
	}
	
	private void onPlayerInfo() {
		
		int x = 7;
		int y = 40;
		
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
		
		ShaderBlur.blurAreaBoarder(x, y, 130, 40, 70);
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
	
	private void drawPotionStatus(ScaledResolution sr) {
		List<PotionEffect> potions = new ArrayList<>();
		for (Object o : mc.player.getActivePotionEffects())
			potions.add((PotionEffect) o);
		potions.sort(Comparator.comparingDouble(effect -> -mc.fontRendererObj
				.getStringWidth(I18n.format((Potion.potionTypes[effect.getPotionID()]).getName()))));

		float pY = (mc.currentScreen != null && (mc.currentScreen instanceof GuiChat)) ? -15 : -2;
		for (PotionEffect effect : potions) {
			Potion potion = Potion.potionTypes[effect.getPotionID()];
			String name = I18n.format(potion.getName());
			String PType = "";
			if (effect.getAmplifier() == 1) {
				name = name + " II";
			} else if (effect.getAmplifier() == 2) {
				name = name + " III";
			} else if (effect.getAmplifier() == 3) {
				name = name + " IV";
			}
			if ((effect.getDuration() < 600) && (effect.getDuration() > 300)) {
				PType = PType + "\2476 " + Potion.getDurationString(effect);
			} else if (effect.getDuration() < 300) {
				PType = PType + "\247c " + Potion.getDurationString(effect);
			} else if (effect.getDuration() > 600) {
				PType = PType + "\2477 " + Potion.getDurationString(effect);
			}
			mc.fontRendererObj.drawStringWithShadow(name,
					sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(name + PType),
					sr.getScaledHeight() - 9 + pY, potion.getLiquidColor());
			mc.fontRendererObj.drawStringWithShadow(PType,
					sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(PType), sr.getScaledHeight() - 9 + pY, -1);
			pY -= 9;
		}
	}
}
