package cn.Noble.GUI.Menu;

import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import java.awt.*;
import net.minecraft.client.gui.*;
import java.io.*;

import cn.Noble.Client;
import cn.Noble.Font.CFontRenderer;
import cn.Noble.Font.FontLoaders;
import cn.Noble.GUI.particle.ParticleUtils;
import cn.Noble.Manager.ModuleManager;
import cn.Noble.Module.Module;
import cn.Noble.Util.Timer.TimerUtil;
import cn.Noble.Util.animate.Translate;
import cn.Noble.Util.render.RenderUtil;
import cn.Noble.Values.Option;
import cn.Noble.Values.Value;

public class GuiWelcome extends GuiScreen
{
    private final ParticleUtils particle;
    private int nmsl = 0;
    private static String Welcome;
    private static long startTime;
    int alpha;
    TimerUtil timer;
    private float currentX;
    private float targetX;
    private float currentY;
    private float targetY;
    Translate translat;
    int textalpha;
    double Anitext;
    
    public GuiWelcome() {
        this.particle = new ParticleUtils();
        this.alpha = 255;
        this.timer = new TimerUtil();
        this.textalpha = 255;
        this.Anitext = 0.0;
    }
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    	if(keyCode == 27) {
    		this.mc.displayGuiScreen((MainMenu)null);
    	}
    }
    @Override
	public void initGui() {
    	
    	Client.shouldShaderBack = true;
    	
    	if(nmsl<1) {
    		System.out.println("Welcome Back!");
    	}
		super.initGui();
	}
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (GuiWelcome.startTime == 0L) {
            GuiWelcome.startTime = System.currentTimeMillis();
        }
        final int h = new ScaledResolution(this.mc).getScaledHeight();
        final int w = new ScaledResolution(this.mc).getScaledWidth();
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final float xDiff = (mouseX - h / 2 - this.currentX) / sr.getScaleFactor();
        final float yDiff = (mouseY - w / 2 - this.currentY) / sr.getScaleFactor();
        this.currentX += xDiff * 0.3f;
        this.currentY += yDiff * 0.3f;
        final CFontRenderer fontwel2 = FontLoaders.CNMD20;
        final CFontRenderer fontwel3 = FontLoaders.CNMD35;
        this.drawDefaultBackground();
        fontwel3.drawCenteredString("Welcome back to Noble", sr.getScaledWidth() / 2.0f, sr.getScaledHeight() / 2.0f - 3.0f - (float)this.Anitext, new Color(255, 255, 255).getRGB());
        if (this.alpha != 255) {
            ++this.alpha;
        }
        if (this.alpha != 255) {
            ++this.alpha;
        }
        if (this.alpha != 255) {
            ++this.alpha;
        }
        if (this.alpha != 255) {
            ++this.alpha;
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.mc.displayGuiScreen(new MainMenu());
    }
    
	@Override
	public void onGuiClosed() {
		mc.entityRenderer.switchUseShader();
	}
    
    static {
        GuiWelcome.startTime = 0L;
    }
}
