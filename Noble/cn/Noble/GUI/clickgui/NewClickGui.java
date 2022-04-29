package cn.Noble.GUI.clickgui;

import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.input.*;
import java.awt.*;
import java.io.IOException;

import org.lwjgl.opengl.*;

import cn.Noble.Client;
import cn.Noble.Api.CustomUI.HUDScreen;
import cn.Noble.Font.CFontRenderer;
import cn.Noble.Font.FontLoaders;
import cn.Noble.GUI.Buttons.FlatButton;
import cn.Noble.GUI.Buttons.SimpleButton;
import cn.Noble.Manager.ModuleManager;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.render.RenderUtils;
import cn.Noble.Values.Mode;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;
import cn.Noble.Values.Value;
import net.minecraft.util.*;

public class NewClickGui extends GuiScreen implements GuiYesNoCallback
{
	public static ModuleType currentModuleType;
	public static Module currentModule;
	public static float startX;
	public static float startY;
	public int moduleStart = 0;
	public int valueStart = 0;
	boolean previousmouse = true;
	boolean mouse;
	public Opacity opacity = new Opacity(0);
	public int opacityx = 255;
	public float moveX = 0.0F;
	public float moveY = 0.0F;

	public float lastPercent;
	public float percent;
	public float percent2;
	public float lastPercent2;

	public float outro;
	public float lastOutro;

	public int mouseWheel;

	public int mouseX;
	public int mouseY;

	static {
		currentModuleType = ModuleType.Combat;
		currentModule = ModuleManager.getModulesInType(currentModuleType).size() != 0
				? (Module) ModuleManager.getModulesInType(currentModuleType).get(0)
				: null;
		startX = 100.0F;
		startY = 100.0F;
	}
	
	@Override
	public void initGui() {
        super.initGui();
		ScaledResolution sc = new ScaledResolution(mc);
//		this.buttonList.add(new FlatButton(1, sc.getScaledWidth()-sc.getScaledWidth()+10, sc.getScaledHeight()-sc.getScaledHeight()+10, 140, 115, "Edit HUD", new Color(10,10,10).getRGB()));
		if (this.mc.world != null) {
			this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
		}
	}
	
    @Override
    protected void actionPerformed(GuiButton button) {
    	switch(button.id) {
    	case 1:{
    		mc.displayGuiScreen(new HUDScreen());
    		}
    	}
    }

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
	}
	
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (Mouse.isButtonDown(0) && mouseX >= 5 && mouseX <= 50 && mouseY <= height - 5 && mouseY >= height - 50) {
            mc.displayGuiScreen(new HUDScreen());
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
        RenderUtil.drawImage(new ResourceLocation("Melody/EditHUD.png"), 9, height - 51, 42, 42);
		
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		if (mc.currentScreen != null)
			if (!(mc.currentScreen instanceof NewClickGui)) {
				lastOutro = outro;
				if (outro < 1.7) {
					outro += 0.1f;

					outro += ((1.7 - outro) / (3f)) - 0.001;
				}
				if (outro > 1.7) {
					outro = 1.7f;
				}
				if (outro < 1) {
					outro = 1;
				}
			}
		if (mc.currentScreen != null)
			if ((mc.currentScreen != null) && !(mc.currentScreen instanceof NewClickGui))
				return;
		lastPercent = percent;
		lastPercent2 = percent2;
		if (percent > .98) {
			percent += ((.98 - percent) / (1.45f)) - 0.001;
		}
		if (percent <= .98) {
			if (percent2 < 1) {
				percent2 += ((1 - percent2) / (2.8f)) + 0.002;
			}
		}
		if (this.isHovered(startX, startY - 25.0F, startX + 400.0F, startY + 25.0F, mouseX, mouseY)
				&& Mouse.isButtonDown(0)) {
			if (this.moveX == 0.0F && this.moveY == 0.0F) {
				this.moveX = (float) mouseX - startX;
				this.moveY = (float) mouseY - startY;
			} else {
				startX = (float) mouseX - this.moveX;
				startY = (float) mouseY - this.moveY;
			}

			this.previousmouse = true;
		} else if (this.moveX != 0.0F || this.moveY != 0.0F) {
			this.moveX = 0.0F;
			this.moveY = 0.0F;
		}

		this.opacity.interpolate((float) this.opacityx);
		RenderUtil.drawRect(startX, startY, startX + 60.0F, startY + 320.0F,
				(new Color(40, 40, 40, (int) this.opacity.getOpacity())).getRGB());
		RenderUtil.drawRect(startX + 60.0F, startY, startX + 200.0F, startY + 320.0F,
				(new Color(31, 31, 31, (int) this.opacity.getOpacity())).getRGB());
		RenderUtil.drawRect(startX + 200.0F, startY, startX + 420.0F, startY + 320.0F,
				(new Color(40, 40, 40, (int) this.opacity.getOpacity())).getRGB());

		RenderUtil.drawRainbowRect(startX, startY, startX + 419, startY + 1);
		
		int m;
		for (m = 0; m < ModuleType.values().length; ++m) {
			ModuleType[] mY = ModuleType.values();
			if (mY[m] != currentModuleType) {	
				if (mY[m].toString() == "Combat") {
					FontLoaders.click40.drawString("1", startX + 20,startY + 20 + m * 40,new Color(255, 255, 255).getRGB());
				}if (mY[m].toString() == "Movement") {
					FontLoaders.click40.drawString("5",startX + 20,startY + 20 + m * 40,new Color(255, 255, 255).getRGB());
				}if (mY[m].toString() == "Player") {
					FontLoaders.click40.drawString("6",startX + 20,startY + 20 + m * 40,new Color(255, 255, 255).getRGB());
				}if (mY[m].toString() == "Render") {
					FontLoaders.click40.drawString("0",startX + 20,startY + 20 + m * 40,new Color(255, 255, 255).getRGB());
				}if (mY[m].toString() == "World") {
					FontLoaders.click40.drawString("3",startX + 20,startY + 20 + m * 40,new Color(255, 255, 255).getRGB());
				}
			} else {
				RenderUtil.drawRoundedRect(startX + 15.0F,   startY + 16.0F + (float) (m * 40),   startX + 17.0F,
						startY + 39.0F + (float) (m * 40), new Color(101, 81, 255).getRGB(), new Color(101, 81, 255).getRGB());
				if (mY[m].toString() == "Combat") {
					FontLoaders.click40.drawString("1", startX + 20,startY + 20 + m * 40,new Color(255, 255, 255).getRGB());
				}if (mY[m].toString() == "Movement") {
					FontLoaders.click40.drawString("5",startX + 20,startY + 20 + m * 40,new Color(255, 255, 255).getRGB());
				}if (mY[m].toString() == "Player") {
					FontLoaders.click40.drawString("6",startX + 20,startY + 20 + m * 40,new Color(255, 255, 255).getRGB());
				}if (mY[m].toString() == "Render") {
					FontLoaders.click40.drawString("0",startX + 20,startY + 20 + m * 40,new Color(255, 255, 255).getRGB());
				}if (mY[m].toString() == "World") {
					FontLoaders.click40.drawString("3",startX + 20,startY + 20 + m * 40,new Color(255, 255, 255).getRGB());
				}
			}

			try {
				if (this.isCategoryHovered(startX + 15.0F, startY + 15.0F + (float) (m * 40), startX + 45.0F,
						startY + 45.0F + (float) (m * 40), mouseX, mouseY) && Mouse.isButtonDown(0)) {
					currentModuleType = mY[m];
					currentModule = ModuleManager.getModulesInType(currentModuleType).size() != 0
							? (Module) ModuleManager.getModulesInType(currentModuleType).get(0)
							: null;
					this.moduleStart = 0;
				}
			} catch (Exception var23) {
				System.err.println(var23);
			}
		}

		mouseWheel = Mouse.getDWheel();
		if (this.isCategoryHovered(startX + 60.0F, startY, startX + 200.0F, startY + 320.0F, mouseX, mouseY)) {
			if (mouseWheel < 0 && this.moduleStart < ModuleManager.getModulesInType(currentModuleType).size() - 1) {
				++this.moduleStart;
			}

			if (mouseWheel > 0 && this.moduleStart > 0) {
				--this.moduleStart;
			}
		}

		if (this.isCategoryHovered(startX + 200.0F, startY, startX + 420.0F, startY + 320.0F, mouseX, mouseY)) {
			if (mouseWheel < 0 && this.valueStart < currentModule.getValues().size() - 1) {
				++this.valueStart;
			}

			if (mouseWheel > 0 && this.valueStart > 0) {
				--this.valueStart;
			}
		}

		//Draw Slide
		RenderUtil.drawRoundedRect(startX + 199.0F, startY - 110 + moduleStart - 60 + (float) (m * 40),   startX + 201.0F,
				startY - 50 + moduleStart + 10 + (float) (m * 40), new Color(101, 81, 255).getRGB(), new Color(101, 81, 255,180).getRGB());
		
		//Draw Type
		FontLoaders.NMSL16.drawString(
				currentModule == null ? currentModuleType.toString() : currentModuleType.toString(),
				startX + 70.0F, startY + 12.5F, (new Color(248, 248, 248)).getRGB());
		RenderUtil.drawRoundedRect(startX + 60.0F, startY+2 , startX + 200.0F, startY + 25.5F, new Color(40,40,40,100).getRGB(), new Color(40,40,40,100).getRGB());
		
		//Draw Modules!
		if (currentModule != null) {
			float var24 = startY + 30.0F;
			int i;
			for (i = 0; i < ModuleManager.getModulesInType(currentModuleType).size(); ++i) {
				Module value = (Module) ModuleManager.getModulesInType(currentModuleType).get(i);
				if (var24 > startY + 300.0F) {
					break;
				}
				RenderUtil.drawRoundRect(startX+195, var24 , startX+65, var24+20, new Color(40,40,40,255).getRGB());
				if (i >= this.moduleStart) {
					FontLoaders.NMSL16.drawString(value.getName(), startX + 86.0F, var24 + 8.0F,
							(new Color(248, 248, 248, (int) this.opacity.getOpacity())).getRGB());
					if (!value.isEnabled()) {
						RenderUtil.drawFilledCircle((double) (startX + 75.0F), (double) (var24 + 10.0F), 3.0D,
								(new Color(255, 0, 0)).getRGB(), 50);
					} else {
						RenderUtil.drawFilledCircle((double) (startX + 75.0F), (double) (var24 + 10.0F), 3.0D,
								(new Color(0, 255, 0)).getRGB(), 50);
					}

					//Button XY
					if (this.isSettingsButtonHovered(startX + 90.0F, var24+2,
							startX + 183.0F,
							var24 + 16.0F + (float) FontLoaders.NMSL20.getHeight(), mouseX, mouseY)) {
						if (!this.previousmouse && Mouse.isButtonDown(0)) {
							if (value.isEnabled()) {
								value.setEnabled(false);
							} else {
								value.setEnabled(true);
							}

							this.previousmouse = true;
						}

						if (!this.previousmouse && Mouse.isButtonDown(1)) {
							this.previousmouse = true;
						}
					}

					if (!Mouse.isButtonDown(0)) {
						this.previousmouse = false;
					}

					if (this.isSettingsButtonHovered(startX + 90.0F, var24,
							startX + 100.0F + (float) FontLoaders.NMSL20.getStringWidth(value.getName()),
							var24 + 8.0F + (float) FontLoaders.NMSL20.getHeight(), mouseX, mouseY)
							&& Mouse.isButtonDown(1)) {
						currentModule = value;
						this.valueStart = 0;
					}

					var24 += 25.0F;
				}
			}

			var24 = startY + 30.0F;
			for (i = 0; i < currentModule.getValues().size() && var24 <= startY + 300.0F; ++i) {
				if (i >= this.valueStart) {
					Value var25 = (Value) currentModule.getValues().get(i);
					float x;
					if (var25 instanceof Numbers) {
						x = startX + 300.0F;
						double current = (double) (68.0F
								* (((Number) var25.getValue()).floatValue()
										- ((Numbers) var25).getMinimum().floatValue())
								/ (((Numbers) var25).getMaximum().floatValue()
										- ((Numbers) var25).getMinimum().floatValue()));
						RenderUtil.drawRect(x - 6.0F, var24 + 2.0F, (float) ((double) x + 75.0D), var24 + 3.0F,
								(new Color(50, 50, 50, (int) this.opacity.getOpacity())).getRGB());
						RenderUtil.drawRect(x - 6.0F, var24 + 2.0F, (float) ((double) x + current + 6.5D), var24 + 3.0F,
								(new Color(61, 141, 255, (int) this.opacity.getOpacity())).getRGB());
						RenderUtil.drawRect((float) ((double) x + current + 2.0D), var24,
								(float) ((double) x + current + 7.0D), var24 + 5.0F,
								(new Color(61, 141, 255, (int) this.opacity.getOpacity())).getRGB());
						FontLoaders.NMSL18.drawStringWithShadow(var25.getName() + ": " + var25.getValue(),
								startX + 210.0F, var24, -1);
						if (!Mouse.isButtonDown(0)) {
							this.previousmouse = false;
						}

						if (this.isButtonHovered(x, var24 - 2.0F, x + 100.0F, var24 + 7.0F, mouseX, mouseY)
								&& Mouse.isButtonDown(0)) {
							if (!this.previousmouse && Mouse.isButtonDown(0)) {
								current = ((Numbers) var25).getMinimum().doubleValue();
								double max = ((Numbers) var25).getMaximum().doubleValue();
								double inc = ((Numbers) var25).getIncrement().doubleValue();
								double valAbs = (double) mouseX - ((double) x + 1.0D);
								double perc = valAbs / 68.0D;
								perc = Math.min(Math.max(0.0D, perc), 1.0D);
								double valRel = (max - current) * perc;
								double val = current + valRel;
								val = (double) Math.round(val * (1.0D / inc)) / (1.0D / inc);
								((Numbers) var25).setValue(Double.valueOf(val));
							}

							if (!Mouse.isButtonDown(0)) {
								this.previousmouse = false;
							}
						}

						var24 += 20.0F;
					}

					if (var25 instanceof Option) {
						x = startX + 300.0F;
						FontLoaders.NMSL18.drawStringWithShadow(var25.getName(), startX + 210.0F, var24, -1);
						RenderUtil.drawRect(x + 56.0F, var24, x + 76.0F, var24 + 1.0F,
								(new Color(255, 255, 255, (int) this.opacity.getOpacity())).getRGB());
						RenderUtil.drawRect(x + 56.0F, var24 + 8.0F, x + 76.0F, var24 + 9.0F,
								(new Color(255, 255, 255, (int) this.opacity.getOpacity())).getRGB());
						RenderUtil.drawRect(x + 56.0F, var24, x + 57.0F, var24 + 9.0F,
								(new Color(255, 255, 255, (int) this.opacity.getOpacity())).getRGB());
						RenderUtil.drawRect(x + 77.0F, var24, x + 76.0F, var24 + 9.0F,
								(new Color(255, 255, 255, (int) this.opacity.getOpacity())).getRGB());
						if (((Boolean) var25.getValue()).booleanValue()) {
							RenderUtil.drawRect(x + 67.0F, var24 + 2.0F, x + 75.0F, var24 + 7.0F,
									(new Color(61, 141, 255, (int) this.opacity.getOpacity())).getRGB());
						} else {
							RenderUtil.drawRect(x + 58.0F, var24 + 2.0F, x + 65.0F, var24 + 7.0F,
									(new Color(150, 150, 150, (int) this.opacity.getOpacity())).getRGB());
						}

						if (this.isCheckBoxHovered(x + 56.0F, var24, x + 76.0F, var24 + 9.0F, mouseX, mouseY)) {
							if (!this.previousmouse && Mouse.isButtonDown(0)) {
								this.previousmouse = true;
								this.mouse = true;
							}

							if (this.mouse) {
								var25.setValue(Boolean.valueOf(!((Boolean) var25.getValue()).booleanValue()));
								this.mouse = false;
							}
						}

						if (!Mouse.isButtonDown(0)) {
							this.previousmouse = false;
						}

						var24 += 20.0F;
					}

					if (var25 instanceof Mode) {
						x = startX + 300.0F;
						FontLoaders.NMSL18.drawStringWithShadow(var25.getName(), startX + 210.0F, var24+2, -1);
						RenderUtil.drawRect(x - 5.0F, var24 - 5.0F, x + 90.0F, var24 + 15.0F,
								(new Color(56, 56, 56, (int) this.opacity.getOpacity())).getRGB());
						RenderUtil.drawBorderRect((double) (x - 5.0F), (double) (var24 - 5.0F), (double) (x + 90.0F),
								(double) (var24 + 15.0F),
								(new Color(101, 81, 255, (int) this.opacity.getOpacity())).getRGB(), 2.0D);
						FontLoaders.NMSL18
								.drawStringWithShadow(((Mode) var25).getModeAsString(), x + 40.0F - (float) (FontLoaders.NMSL18.getStringWidth(((Mode) var25).getModeAsString()) / 2),
										var24+2, -1);
						if (this.isStringHovered(x, var24 - 5.0F, x + 100.0F, var24 + 15.0F, mouseX, mouseY)) {
							if (Mouse.isButtonDown(0) && !this.previousmouse) {
								Enum var26 = (Enum) ((Mode) var25).getValue();
								int next = var26.ordinal() + 1 >= ((Mode) var25).getModes().length ? 0
										: var26.ordinal() + 1;
								var25.setValue(((Mode) var25).getModes()[next]);
								this.previousmouse = true;
							}

							if (!Mouse.isButtonDown(0)) {
								this.previousmouse = false;
							}
						}

						var24 += 25.0F;
					}
				}
			}
		}
	}

	@Override
	public void onGuiClosed() {
		this.opacity.setOpacity(0.0F);
		mc.entityRenderer.switchUseShader();
		Client.instance.saveSettings();
	}

	public boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		return (float) mouseX >= f && (float) mouseX <= g && (float) mouseY >= y && (float) mouseY <= y2;
	}

	public boolean isSettingsButtonHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		return (float) mouseX >= x && (float) mouseX <= x2 && (float) mouseY >= y && (float) mouseY <= y2;
	}

	public boolean isButtonHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		return (float) mouseX >= f && (float) mouseX <= g && (float) mouseY >= y && (float) mouseY <= y2;
	}

	public boolean isCheckBoxHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		return (float) mouseX >= f && (float) mouseX <= g && (float) mouseY >= y && (float) mouseY <= y2;
	}

	public boolean isCategoryHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		return (float) mouseX >= x && (float) mouseX <= x2 && (float) mouseY >= y && (float) mouseY <= y2;
	}

	public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		return (float) mouseX >= x && (float) mouseX <= x2 && (float) mouseY >= y && (float) mouseY <= y2;
	}
}
