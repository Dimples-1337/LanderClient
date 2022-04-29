/*
 * Decompiled with CFR 0.150.
 */
package cn.Noble.GUI.Buttons;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import cn.Noble.Client;
import cn.Noble.Font.CFontRenderer;
import cn.Noble.Font.FontLoaders;
import cn.Noble.GUI.AnimationUtils;
import cn.Noble.Util.Timer.TimerUtil;
import cn.Noble.Util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public final class ClientButton extends GuiButton {

	private int r;
	private int g;
	private int b;
	private int alpha;
	private ResourceLocation image;

	private int r1;
	private int g1;
	private int b1;
	private int alpha1;

	TimerUtil timer = new TimerUtil();
	TimerUtil timer1 = new TimerUtil();

	private int reachTime;

	public ClientButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText,
			ResourceLocation image, int reachTime, Color color) {
		super(buttonId, x, y, 10, 12, buttonText);

		Color color2 = new Color(120, 120, 200, 255);

		this.width = widthIn;
		this.height = heightIn;

		this.r = color.getRed();
		this.g = color.getGreen();
		this.b = color.getBlue();
		this.alpha = color.getAlpha();
		this.image = image;

		this.r1 = color2.getRed();
		this.g1 = color2.getGreen();
		this.b1 = color2.getBlue();
		this.alpha1 = color2.getAlpha();

		this.reachTime = reachTime;
	}

	public ClientButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText,
			ResourceLocation image, int reachTime, Color color, Color color2) {
		super(buttonId, x, y, 10, 12, buttonText);

		this.width = widthIn;
		this.height = heightIn;

		this.r = color.getRed();
		this.g = color.getGreen();
		this.b = color.getBlue();
		this.alpha = color.getAlpha();
		this.image = image;

		this.reachTime = reachTime;

		this.r1 = color2.getRed();
		this.g1 = color2.getGreen();
		this.b1 = color2.getBlue();
		this.alpha1 = color2.getAlpha();
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {

		int wi = 0;
		this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
				&& mouseY < this.yPosition + this.height;
		
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);
		
		RenderUtil.drawFastRoundedRect(this.xPosition, this.yPosition, this.xPosition + this.width,
				this.yPosition + this.height, 1, new Color(r, g, b, alpha).getRGB());
		if (this.isMouseOver()) {
			timer1.reset();
			for (int i = 0; i <= this.width; i++) {
				if (this.timer.hasReached(i * reachTime)) {
					wi++;
				}
			}
			RenderUtil.drawFastRoundedRect(this.xPosition, this.yPosition + this.height - 2, this.xPosition + wi,
					this.yPosition + this.height, 1, new Color(r1, g1, b1, alpha1).getRGB());
		} else if (!this.isMouseOver()) {
			timer.reset();
			wi = this.width;
			for (int i = 0; i < this.width; i++) {
				if (this.timer1.hasReached(i * reachTime)) {
					wi--;
				}
			}
			RenderUtil.drawFastRoundedRect(this.xPosition, this.yPosition  + this.height - 2, this.xPosition + wi,
					this.yPosition + this.height, 1, new Color(r1, g1, b1, alpha1).getRGB());
		}
		
		if (this.image != null)
			RenderUtil.drawImage(image, this.xPosition + this.height / 3, this.yPosition + this.height / 3,
					this.height / 2.6, this.height / 2.6);
		
        GL11.glColor3f(2.55f, 2.55f, 2.55f);
        this.mouseDragged(mc, mouseX, mouseY);
        GL11.glPushMatrix();
        GL11.glPushAttrib(1048575);
        GL11.glScaled(1.0, 1.0, 1.0);
		
		CFontRenderer font = FontLoaders.CNMD18;
		font.drawCenteredString(this.displayString, this.xPosition + this.width / 2,
				this.yPosition + (this.height) / 2 - 3, -1);
		
        GL11.glPopAttrib();
        GL11.glPopMatrix();
	}
}
