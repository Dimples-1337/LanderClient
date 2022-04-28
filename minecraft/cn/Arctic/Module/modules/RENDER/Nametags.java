package cn.Arctic.Module.modules.RENDER;



import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventRender3D;
import cn.Arctic.Font.CFontRenderer;
import cn.Arctic.Font.me.superskidder.FontLoaders2;
import cn.Arctic.Manager.FriendManager;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.COMBAT.AntiBot;
import cn.Arctic.Module.modules.COMBAT.Aura;
import cn.Arctic.Module.modules.GUI.HUD;
import cn.Arctic.Module.modules.PLAYER.Teams;
import cn.Arctic.Util.render.RenderUtil;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.src.Config;


public class Nametags extends Module {
	private Mode<Enum> mode = new Mode("Mode", (Enum[]) TagMode.values(), (Enum) TagMode.New);
	private Mode<Enum> health = new Mode("Health Mode", (Enum[]) HealthMode.values(),
			(Enum) HealthMode.Hearts);
	private Option<Boolean> armor = new Option<Boolean>("Armor", true);
	private Option<Boolean> dura = new Option<Boolean>("Durability", true);
	private Numbers<Double> scale = new Numbers<Double>("Scale", 3.0, 1.0, 10.0, 0.1);
	private ArrayList<Entity> entities = new ArrayList();
	private Option<Boolean> players = new Option<Boolean>("Players", true);
	private Option<Boolean> animals = new Option<Boolean>("Animals", true);
	private Option<Boolean> mobs = new Option<Boolean>("Mobs", false);
	private Option<Boolean> invis = new Option<Boolean>("Invisibles", false);
	private Option<Boolean> self = new Option<Boolean>("Self", false);
	private Option<Boolean> horse = new Option<Boolean>("Horse", false);
	private int i = 0;
	public Nametags() {
		super("Nametags", new String[] { "tags" }, ModuleType.Render);
		this.setColor(new Color(29, 187, 102).getRGB());
		this.addValues(this.mode, this.health, this.armor, this.dura, this.invis, self,this.scale);
	}

	@EventHandler
	private void onRender(EventRender3D render) {
		if (mode.getValue() == TagMode.New) {
			ArrayList<EntityPlayer> validEnt = new ArrayList<EntityPlayer>();
			if (validEnt.size() > 100) {
				validEnt.clear();
			}
			for (EntityLivingBase player2 : this.mc.world.playerEntities) {
				if (player2.isEntityAlive()) {
					if (player2.isInvisible() && !this.invis.getValue().booleanValue()) {
						if (!validEnt.contains(player2))
							continue;
						validEnt.remove(player2);
						continue;
					}
					if (!self.getValue()) {
						if (player2 == this.mc.player) {
							if (!validEnt.contains(player2))
								continue;
							validEnt.remove(player2);
							continue;
						}
					}
					if (validEnt.size() > 100)
						break;
					if (validEnt.contains(player2))
						continue;
					validEnt.add((EntityPlayer) player2);
					continue;
				}
				if (!validEnt.contains(player2))
					continue;
				validEnt.remove(player2);
			}
			validEnt.forEach(player -> {
				float x = (float) (player.lastTickPosX
						+ (player.posX - player.lastTickPosX) * (double) render.getPartialTicks()
						- RenderManager.renderPosX);
				float y = (float) (player.lastTickPosY
						+ (player.posY - player.lastTickPosY) * (double) render.getPartialTicks()
						- RenderManager.renderPosY);
				float z = (float) (player.lastTickPosZ
						+ (player.posZ - player.lastTickPosZ) * (double) render.getPartialTicks()
						- RenderManager.renderPosZ);
				this.renderNametag((EntityPlayer) player, x, y, z);
			});
		} else if (mode.getValue() == TagMode.Old) {
			for (Object o : this.mc.world.playerEntities) {
				EntityPlayer p = (EntityPlayer) o;
				if(!this.self.getValue()) {
				if(p == mc.player) continue;
				}
					double pX = p.lastTickPosX + (p.posX - p.lastTickPosX) * mc.timer.renderPartialTicks
							- mc.getRenderManager().renderPosX;
					double pY = p.lastTickPosY + (p.posY - p.lastTickPosY) * mc.timer.renderPartialTicks
							- mc.getRenderManager().renderPosY;
					double pZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * mc.timer.renderPartialTicks
							- mc.getRenderManager().renderPosZ;
					renderNameTag(p, String.valueOf(p.getDisplayName()), pX, pY, pZ);
			}
		}

	}

	private String getHealth(EntityPlayer player) {
		DecimalFormat numberFormat = new DecimalFormat("0.00#");
		return this.health.getValue() == HealthMode.Percentage
				? numberFormat.format(player.getHealth() * 5.0f + player.getAbsorptionAmount() * 5.0f)
				: numberFormat.format(player.getHealth() + player.getAbsorptionAmount());
		// return this.health.getValue() == HealthMode.Percentage ?
		// numberFormat.format(player.getHealth() * 5.0f + player.getAbsorptionAmount()
		// * 5.0f) : numberFormat.format(player.getHealth() / 2.0f +
		// player.getAbsorptionAmount() / 2.0f);
	}

	public void renderNameTag(EntityPlayer entity, String tag, double pX, double pY, double pZ) {
		float var10 = mc.player.getDistanceToEntity(entity) / 6.0F;
		if (var10 < 0.8F) {
			var10 = 0.8F;
		}
		pY += entity.isSneaking() ? 0.5D : 0.7D;
		float var11 = (float) (var10 * this.scale.getValue().doubleValue());
		var11 /= 100.0F;
		tag = mc.player.getName();//
		String var12 = "";
		AntiBot ab = new AntiBot();
		if (ab.isBot(entity)) {
			var12 = "\u00a79[BOT]";
		} else {
			var12 = "";
		}
		
		
		String var13 = "";
		Teams teams = (Teams) ModuleManager.getModuleByClass(Teams.class);
		if (!Aura.isOnSameTeam(entity) && !teams.isEnabled()) {
			var13 = "";
		} else {
			var13 = "\u00a7b[TEAM]";
		}

		if ((var13 + var12).equals("")) {
			var13 = "\u00a7a";
		}

		String var14 = var13 + var12 + tag;
		String var15 = "\u00a77HP:" + (int) entity.getHealth();
		 float DISTANCE = mc.player.getDistanceToEntity(entity);
		String var99 =" "+ "["+(int)DISTANCE + "m"+"]";
		GL11.glPushMatrix();
		GL11.glTranslatef((float) pX, (float) pY + 1.4F, (float) pZ);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-RenderManager.playerViewY, 0.0F, 2.0F, 0.0F);
		GL11.glRotatef(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-var11, -var11, var11);
		setGLCap(2896, false);
		setGLCap(2929, false);
		int var16 = FontLoaders2.msFont18.getStringWidth(var14) / 2;
		setGLCap(3042, true);
		GL11.glBlendFunc(770, 771);
		this.drawBorderedRectNameTag((float) (-var16 - 2), (float) (-(FontLoaders2.msFont18.FONT_HEIGHT + 9)),
				(float) (var16 + 2), 2.0F, 1.0F, this.reAlpha(Color.BLACK.getRGB(), 0.3F),
				this.reAlpha(Color.BLACK.getRGB(), 0.3F));
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		FontLoaders2.msFont18.drawString(var14, -var16, -(FontLoaders2.msFont18.FONT_HEIGHT + 8), -1);
		FontLoaders2.msFont18.drawString(var15+var99, -FontLoaders2.msFont18.getStringWidth(var15+var99) / 2,
				-(FontLoaders2.msFont18.FONT_HEIGHT - 2), -1);
		int var17 = (new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),HUD.h.getValue().intValue())).getRGB();
		if (entity.getHealth() > 20.0F) {
			var17 = -65292;
		}

		float var18 = (float) Math.ceil((double) (entity.getHealth() + entity.getAbsorptionAmount()));
		float var19 = var18 / (entity.getMaxHealth() + entity.getAbsorptionAmount());
		Gui.drawRect((float) var16 + var19 * 40.0F - 40.0F + 2.0F, 2.0F, (float) (-var16) - 1.98F, 0.9F, var17);
		GL11.glPushMatrix();
		int var20 = 0;
		ItemStack[] var24 = entity.inventory.armorInventory;
		int var23 = entity.inventory.armorInventory.length;

		ItemStack var21;
		for (int var22 = 0; var22 < var23; ++var22) {
			var21 = var24[var22];
			if (var21 != null) {
				var20 -= 11;
			}
		}

		if (entity.getHeldItem() != null) {
			var20 -= 8;
			var21 = entity.getHeldItem().copy();
			if (((ItemStack) var21).hasEffect() && (((ItemStack) var21).getItem() instanceof ItemTool
					|| ((ItemStack) var21).getItem() instanceof ItemArmor)) {
				((ItemStack) var21).stackSize = 1;
			}

			this.renderItemStack((ItemStack) var21, var20, -35);
			var20 += 20;
		}

		ItemStack[] var25 = entity.inventory.armorInventory;
		int var28 = entity.inventory.armorInventory.length;

		for (var23 = 0; var23 < var28; ++var23) {
			ItemStack var27 = var25[var23];
			if (var27 != null) {
				ItemStack var26 = var27.copy();
				if (var26.hasEffect()
						&& (var26.getItem() instanceof ItemTool || var26.getItem() instanceof ItemArmor)) {
					var26.stackSize = 1;
				}
				this.renderItemStack(var26, var20, -35);
				var20 += 20;
			}
		}

		GL11.glPopMatrix();
		revertAllCaps();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	public static void revertAllCaps() {
		for (Iterator localIterator = glCapMap.keySet().iterator(); localIterator.hasNext();) {
			int cap = ((Integer) localIterator.next()).intValue();
			revertGLCap(cap);
		}
	}

	public static void revertGLCap(int cap) {
		Boolean origCap = (Boolean) glCapMap.get(Integer.valueOf(cap));
		if (origCap != null) {
			if (origCap.booleanValue()) {
				GL11.glEnable(cap);
			} else {
				GL11.glDisable(cap);
			}
		}
	}

	public static int reAlpha(int color, float alpha) {
		Color c = new Color(color);
		float r = ((float) 1 / 255) * c.getRed();
		float g = ((float) 1 / 255) * c.getGreen();
		float b = ((float) 1 / 255) * c.getBlue();
		return new Color(r, g, b, alpha).getRGB();
	}

	public void OldrenderItemStack(ItemStack var1, int var2, int var3) {
		GL11.glPushMatrix();
		GL11.glDepthMask(true);
		GlStateManager.clear(256);
		RenderHelper.enableStandardItemLighting();
		super.mc.getRenderItem().zLevel = -150.0F;
		whatTheFuckOpenGLThisFixesItemGlint();
		super.mc.getRenderItem().renderItemAndEffectIntoGUI(var1, var2, var3);
		super.mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, var1, var2, var3);
		super.mc.getRenderItem().zLevel = 0.0F;
		RenderHelper.disableStandardItemLighting();
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

	public void whatTheFuckOpenGLThisFixesItemGlint() {
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

	private static Map<Integer, Boolean> glCapMap = new HashMap();

	public static void setGLCap(int cap, boolean flag) {
		glCapMap.put(Integer.valueOf(cap), Boolean.valueOf(GL11.glGetBoolean(cap)));
		if (flag) {
			GL11.glEnable(cap);
		} else {
			GL11.glDisable(cap);
		}
	}

	public void drawBorderedRectNameTag(float var1, float var2, float var3, float var4, float var5, int var6,
			int var7) {
		Gui.drawRect(var1, var2, var3, var4, var7);
		float var8 = (float) (var6 >> 24 & 255) / 255.0F;
		float var9 = (float) (var6 >> 16 & 255) / 255.0F;
		float var10 = (float) (var6 >> 8 & 255) / 255.0F;
		float var11 = (float) (var6 & 255) / 255.0F;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(var9, var10, var11, var8);
		GL11.glLineWidth(var5);
		GL11.glBegin(1);
		GL11.glVertex2d((double) var1, (double) var2);
		GL11.glVertex2d((double) var1, (double) var4);
		GL11.glVertex2d((double) var3, (double) var4);
		GL11.glVertex2d((double) var3, (double) var2);
		GL11.glVertex2d((double) var1, (double) var2);
		GL11.glVertex2d((double) var3, (double) var2);
		GL11.glVertex2d((double) var1, (double) var4);
		GL11.glVertex2d((double) var3, (double) var4);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	private void drawNames(EntityPlayer player) {
		float DISTANCE = mc.player.getDistanceToEntity(player);
		String var98 ="["+(int)DISTANCE + "m"+"]"+" ";
		float xP = 2.2f;
		float width = (float) this.getWidth(this.getPlayerName(player)) / 2.0f + xP;
		float w = width = (float) ((double) width + ((double) (this.getWidth(" " + this.getHealth(player)) / 2) + 2.5));
		float nw = -width - xP;
		float offset = this.getWidth(this.getPlayerName(player)) + 4;
		if (this.health.getValue() == HealthMode.Percentage) {
			RenderUtil.drawBorderedRect(nw, -3.0f, width, 10.0f, 1.0f, new Color(20, 20, 20, 180).getRGB(),
					new Color(10, 10, 10, 200).getRGB());
		} else {
			if(DISTANCE<10) {
			RenderUtil.drawBorderedRect(nw -17.0f, -3.0f, width+5, 10.0f, 1.0f, new Color(20, 20, 20, 180).getRGB(),
					new Color(10, 10, 10, 80).getRGB());
			}
			if(DISTANCE>10&&DISTANCE<100) {
				RenderUtil.drawBorderedRect(nw -23.0f, -3.0f, width+5, 10.0f, 1.0f, new Color(20, 20, 20, 180).getRGB(),
						new Color(10, 10, 10, 80).getRGB());
				}
			if(DISTANCE>=100) {
				RenderUtil.drawBorderedRect(nw -28.0f, -3.0f, width+5, 10.0f, 1.0f, new Color(20, 20, 20, 180).getRGB(),
						new Color(10, 10, 10, 80).getRGB());
				}
		}
		GlStateManager.disableDepth();
		offset = this.health.getValue() == HealthMode.Percentage
				? (offset += (float) (this.getWidth(this.getHealth(player)) + this.getWidth(" %") - 1))
				: (offset += (float) (this.getWidth(this.getHealth(player)) + this.getWidth(" ") - 1));
		this.drawString(this.getPlayerName(player), w - offset, 0.0f, 16777215);
		
		if(DISTANCE < 10) {
		this.drawString(var98, w-22 - offset, 0.0f, new Color(0,255,0).getRGB());
		}
		if(DISTANCE >=10&&DISTANCE<=30) {
			this.drawString(var98, w-28 - offset, 0.0f, new Color(0,255,0).getRGB());
			}
		if(DISTANCE > 30&&DISTANCE<100) {
			this.drawString(var98, w-28 - offset, 0.0f, new Color(255,255,0).getRGB());
			}
		if(DISTANCE >=100) {
			this.drawString(var98, w-34 - offset, 0.0f, new Color(255,0,0).getRGB());
		}
		if (player.getHealth() == 10.0f) {
			int n = 16776960;
		}
		int color = player.getHealth() > 10.0f
				? RenderUtil.blend(new Color(-16711936), new Color(-256),
						1.0f / player.getHealth() / 2.0f * (player.getHealth() - 10.0f)).getRGB()
				: RenderUtil.blend(new Color(-256), new Color(-65536), 0.1f * player.getHealth()).getRGB();
		if (this.health.getValue() == HealthMode.Percentage) {
			this.drawString(String.valueOf(this.getHealth(player)) + "%",
					w - (float) this.getWidth(String.valueOf(this.getHealth(player)) + " %"), 0.0f, color);
		} else {
			this.drawString("["+this.getHealth(player)+"]",
					w-5 - (float) this.getWidth(this.getHealth(player) + ""), 0.0f, color);
		}
		GlStateManager.enableDepth();
	}

	private void drawString(String text, float x, float y, int color) {
		FontLoaders2.msFont18.drawStringWithShadow(text, x, y, color);
	}

	private int getWidth(String text) {
		return FontLoaders2.msFont18.getStringWidth(text);
	}

	private void startDrawing(float x, float y, float z, EntityPlayer player) {
		float var10001 = this.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f;
		double size = Config.zoomMode ? (double) (this.getSize(player) / 10.0f) * this.scale.getValue() * 0.5
				: (double) (this.getSize(player) / 10.0f) * this.scale.getValue() * 1.5;
		GL11.glPushMatrix();
		RenderUtil.startDrawing();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glNormal3f((float) 0.0f, (float) 1.0f, (float) 0.0f);
		GL11.glRotatef((float) (-this.mc.getRenderManager().playerViewY), (float) 0.0f, (float) 1.0f, (float) 0.0f);
		GL11.glRotatef((float) this.mc.getRenderManager().playerViewX, (float) var10001, (float) 0.0f, (float) 0.0f);
		GL11.glScaled((double) (-0.01666666753590107 * size), (double) (-0.01666666753590107 * size),
				(double) (0.01666666753590107 * size));
	}

	private void stopDrawing() {
		RenderUtil.stopDrawing();
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		GlStateManager.popMatrix();
	}

	private void renderNametag(EntityPlayer player, float x, float y, float z) {
		y = (float) ((double) y + (1.55 + (player.isSneaking() ? 0.5 : 0.7)));
		this.startDrawing(x, y, z, player);
		this.drawNames(player);
		GL11.glColor4d((double) 1.0, (double) 1.0, (double) 1.0, (double) 1.0);
		if (this.armor.getValue().booleanValue()) {
			this.renderArmor(player);
		}
		//mc.fontRendererObj.drawStringWithShadow(player.getHeldItem().getDisplayName(),x - 35,(int) mc.thePlayer.renderOffsetY,-1);
		this.stopDrawing();
	}

	private void renderArmor(EntityPlayer player) {
		ItemStack armourStack;
		ItemStack[] renderStack = player.inventory.armorInventory;
		int length = renderStack.length;
		int xOffset = 0;
		ItemStack[] arritemStack = renderStack;
		int n = arritemStack.length;
		int n2 = 0;
		while (n2 < n) {
			ItemStack aRenderStack = arritemStack[n2];
			armourStack = aRenderStack;
			if (armourStack != null) {
				xOffset -= 8;
			}
			++n2;
		}
		if (player.getHeldItem() != null) {
			xOffset -= 8;
			ItemStack stock = player.getHeldItem().copy();
			if (stock.hasEffect() && (stock.getItem() instanceof ItemTool || stock.getItem() instanceof ItemArmor)) {
				stock.stackSize = 1;
			}
			this.renderItemStack(stock, xOffset, -20);
			xOffset += 16;
		}
		renderStack = player.inventory.armorInventory;
		int index = 3;
		while (index >= 0) {
			armourStack = renderStack[index];
			if (armourStack != null) {
				this.renderItemStack(armourStack, xOffset, -20);
				xOffset += 16;
			}
			--index;
		}
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
	}

	private String getPlayerName(EntityPlayer player) {
		String name = player.getDisplayName().getFormattedText();
		if (FriendManager.isFriend(player.getName())) {
			name = "\u00a7b" + FriendManager.getAlias(player.getName());
		}

		Teams teams = (Teams) ModuleManager.getModuleByClass(Teams.class);
		if (Aura.isOnSameTeam(player) && teams.isEnabled()) {
			name = "\247a[Teams]" + player.getDisplayName().getFormattedText();
		}

		AntiBot ab = new AntiBot();

		//if (ab.isServerBot(player) && ModuleManager.getModuleByClass(AntiBot.class).isEnabled()) {
		//	name = "\2479[Bot] " + EnumChatFormatting.WHITE + player.getDisplayName().getFormattedText();
		//}
		return name;
	}

	private float getSize(EntityPlayer player) {
		return this.mc.player.getDistanceToEntity(player) / 4.0f <= 2.0f ? 2.0f
				: this.mc.player.getDistanceToEntity(player) / 4.0f;
	}

	private void renderItemStack(ItemStack stack, int x, int y) {
		GlStateManager.pushMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.clear(256);
		RenderHelper.enableStandardItemLighting();
		this.mc.getRenderItem().zLevel = -150.0f;
		GlStateManager.disableDepth();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
		this.mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, x, y);
		this.mc.getRenderItem().zLevel = 0.0f;
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableCull();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.disableLighting();
		double s = 0.5;
		GlStateManager.scale(s, s, s);
		GlStateManager.disableDepth();
		this.renderEnchantText(stack, x, y);
		GlStateManager.enableDepth();
		GlStateManager.scale(2.0f, 2.0f, 2.0f);
		GlStateManager.popMatrix();
	}

	private void renderEnchantText(ItemStack stack, int x, int y) {
		int unbreakingLevel2;
		int enchantmentY = y - 24;
		if (stack.getEnchantmentTagList() != null && stack.getEnchantmentTagList().tagCount() >= 6) {
			FontLoaders2.msFont18.drawStringWithShadow("god", x * 2, enchantmentY, 16711680);
			return;
		}
		if (stack.getItem() instanceof ItemArmor) {
			int protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
			int projectileProtectionLevel = EnchantmentHelper
					.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack);
			int blastProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId,
					stack);
			int fireProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack);
			int thornsLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
			int unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
			int damage = stack.getMaxDamage() - stack.getItemDamage();
			if (this.dura.getValue().booleanValue()) {
				FontLoaders2.msFont18.drawStringWithShadow("" + damage, x * 2, y, 16777215);
			}
			if (protectionLevel > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("prot" + protectionLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (projectileProtectionLevel > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("proj" + projectileProtectionLevel, x * 2, enchantmentY,
						-1);
				enchantmentY += 8;
			}
			if (blastProtectionLevel > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("bp" + blastProtectionLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (fireProtectionLevel > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("frp" + fireProtectionLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (thornsLevel > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("th" + thornsLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (unbreakingLevel > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("unb" + unbreakingLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
		}
		if (stack.getItem() instanceof ItemBow) {
			int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
			int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
			int flameLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
			unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
			if (powerLevel > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("pow" + powerLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (punchLevel > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("pun" + punchLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (flameLevel > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("flame" + flameLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (unbreakingLevel2 > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("unb" + unbreakingLevel2, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
		}
		if (stack.getItem() instanceof ItemSword) {
			int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
			int knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
			int fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
			unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
			if (sharpnessLevel > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("sh" + sharpnessLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (knockbackLevel > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("kb" + knockbackLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (fireAspectLevel > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("fire" + fireAspectLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (unbreakingLevel2 > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("unb" + unbreakingLevel2, x * 2, enchantmentY, -1);
			}
		}
		if (stack.getItem() instanceof ItemTool) {
			int unbreakingLevel22 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
			int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
			int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
			int silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack);
			if (efficiencyLevel > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("eff" + efficiencyLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (fortuneLevel > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("fo" + fortuneLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (silkTouch > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("silk" + silkTouch, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (unbreakingLevel22 > 0) {
				FontLoaders2.msFont18.drawStringWithShadow("ub" + unbreakingLevel22, x * 2, enchantmentY, -1);
			}
		}
		if (stack.getItem() == Items.golden_apple && stack.hasEffect()) {
			FontLoaders2.msFont18.drawStringWithShadow("god", x * 2, enchantmentY, -1);
		}
	}

	static enum HealthMode {
		Hearts, Percentage;
	}

	static enum TagMode {
		New, Old;
	}
}
