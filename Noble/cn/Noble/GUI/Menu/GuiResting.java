package cn.Noble.GUI.Menu;

import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import java.awt.*;
import net.minecraft.client.gui.*;
import java.io.*;
import java.util.Calendar;
import java.util.Date;

import com.ibm.icu.text.SimpleDateFormat;

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

public class GuiResting extends GuiScreen
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
    
    public GuiResting() {
        this.particle = new ParticleUtils();
        this.alpha = 255;
        this.timer = new TimerUtil();
        this.textalpha = 255;
        this.Anitext = 0.0;
    }
    
    @Override
	public void initGui() {
    	
    	Client.shouldShaderBack = true;

		super.initGui();
	}
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
    	
    	Calendar c = Calendar.getInstance();
    	int hour = c.get(Calendar.HOUR_OF_DAY);
    	int min = c.get(Calendar.MINUTE);
    	int sec = c.get(Calendar.SECOND);
    	String time = hour + " : " + min + " : " + sec;
    	
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final CFontRenderer titleFont = FontLoaders.CNMD35;
        final CFontRenderer timeFont = FontLoaders.NMSL28;
        
        this.drawDefaultBackground();
        titleFont.drawCenteredString("Click or Tap the Keyboard to Continue.", sr.getScaledWidth() / 2.0f, sr.getScaledHeight() / 2.0f - 15.0f - (float)this.Anitext, new Color(255, 255, 255, 170).getRGB());
        timeFont.drawCenteredString(time, sr.getScaledWidth() / 2.0f, sr.getScaledHeight() / 2.0f + 10.0f - (float)this.Anitext, new Color(180, 180, 180, 130).getRGB());
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.mc.displayGuiScreen(new MainMenu());
    }
    
    @Override
    public void handleKeyboardInput() throws IOException {
    	this.mc.displayGuiScreen(new MainMenu());
    	super.handleKeyboardInput();
    }
    
	@Override
	public void onGuiClosed() {
		mc.entityRenderer.switchUseShader();
	}
    
    static {
        GuiResting.startTime = 0L;
    }
}
