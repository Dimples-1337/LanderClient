package cn.Arctic.GUI.clickgui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import cn.Arctic.GUI.particle.ParticleUtils;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.AnimationUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;



public class ClickUi extends GuiScreen
{
	public static ParticleUtils par;
    public static ArrayList<Window> windows;
    public double opacity;
    public int scrollVelocity;
    public static boolean binding;
    private float animpos;
    
    public ClickUi() {
        super();
        this.opacity = 0.0;
        this.animpos = 75.0f;
        if (ClickUi.windows.isEmpty()) {
            int x = 5;
            for (final ModuleType c : ModuleType.values()) {
                ClickUi.windows.add(new Window(c, x, 5));
                x += 105;
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.animpos = AnimationUtil.moveUD(this.animpos, 1.0f, 0.1f, 0.1f);
        this.drawDefaultBackground();
        GlStateManager.rotate(this.animpos, 0.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, this.animpos, 0.0f);
        this.opacity = ((this.opacity + 10.0 < 200.0) ? (this.opacity += 10.0) : 200.0);
 //       par.drawParticles(mouseX, mouseY);
        GlStateManager.pushMatrix();
        final ScaledResolution scaledRes = new ScaledResolution(this.mc);
        final float scale = scaledRes.getScaleFactor() / (float)Math.pow(scaledRes.getScaleFactor(), 2.0);
        ClickUi.windows.forEach(w -> w.render(mouseX, mouseY));
        GlStateManager.popMatrix();
        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            this.scrollVelocity = ((wheel < 0) ? -120 : ((wheel > 0) ? 120 : 0));
        }
        ClickUi.windows.forEach(w -> w.mouseScroll(mouseX, mouseY, this.scrollVelocity));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        ClickUi.windows.forEach(w -> w.click(mouseX, mouseY, mouseButton));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1 && !ClickUi.binding) {
            this.mc.displayGuiScreen(null);
            return;
        }
        ClickUi.windows.forEach(w -> w.key(typedChar, keyCode));
    }
    
    @Override
    public void initGui() {
		if (this.mc.world != null) {
			this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
		}
        super.initGui();
    }
    
    @Override
    public void onGuiClosed() {
        this.mc.entityRenderer.switchUseShader();
    }
    
    public synchronized void sendToFront(final Window window) {
        int panelIndex = 0;
        for (int i = 0; i < ClickUi.windows.size(); ++i) {
            if (ClickUi.windows.get(i) == window) {
                panelIndex = i;
                break;
            }
        }
        final Window t = ClickUi.windows.get(ClickUi.windows.size() - 1);
        ClickUi.windows.set(ClickUi.windows.size() - 1, ClickUi.windows.get(panelIndex));
        ClickUi.windows.set(panelIndex, t);
    }
    
    static {
        ClickUi.windows = Lists.newArrayList();
        ClickUi.binding = false;
    }
}
