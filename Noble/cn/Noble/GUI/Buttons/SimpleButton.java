/*
 * Decompiled with CFR 0.150.
 */
package cn.Noble.GUI.Buttons;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import cn.Noble.Client;
import cn.Noble.Font.FontLoaders;
import cn.Noble.GUI.AnimationUtils;
import cn.Noble.Util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public final class SimpleButton extends GuiButton {
	private int color = 170;
	private double animation = 0.0;

	public SimpleButton(int buttonId, int x, int y, int width, int height, String buttonText) {
		super(buttonId, x - (int) ((double) Minecraft.getMinecraft().fontRendererObj.getStringWidth(buttonText) / 2.0),
				y, width, height, buttonText);
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {

		GL11.glEnable(GL11.GL_BLEND);
		
		RenderUtil.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height,
				new Color(20, 20, 20, 120).getRGB());
		
		if (this.isMouseOver()) {
			RenderUtil.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width,
					this.yPosition + this.height, new Color(140, 140, 140, 120).getRGB());
		}
		
		FontLoaders.CNMD18.drawCenteredString("Cancle", this.xPosition + this.width,
				this.yPosition + this.height, new Color(255, 255, 255).getRGB());
		
		GL11.glDisable(GL11.GL_BLEND);
	}
}
