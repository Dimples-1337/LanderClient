package cn.Arctic.Module.modules.RENDER;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventTick;
import cn.Arctic.GUI.clickgui.ClickUi;
import cn.Arctic.GUI.clickgui.Lander2.GuiClickUI;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.values.Mode;
import net.minecraft.client.Minecraft;

public class ClickGUI
extends Module {
	public static boolean rainbow;
	public static int rainbowcolor;
	public static Mode<Enum> cmode = new Mode<Enum>("Mode", GuiMode.values(), GuiMode.Normal);
//	private Numbers<Double> r = new Numbers<Double>("Red", "Red", 120.0, 0.0, 255.0, 5.0);
//	private Numbers<Double> g = new Numbers<Double>("Green", "Green", 120.0, 0.0, 255.0, 5.0);
//	private Numbers<Double> b = new Numbers<Double>("Blue", "Blue", 255.0, 0.0, 255.0, 5.0);
	public static int memoriseX = 30;
    public static int memoriseY = 30;
    public static int memoriseWheel = 0;
    public static List<Module> memoriseML = new CopyOnWriteArrayList<>();
    public static ModuleType memoriseCatecory = null;
    public ClickGUI() {
        super("ClickGUI", new String[]{"clickui"}, ModuleType.Render);
        this.addValues(cmode);
    }

    @Override
    public void onEnable() {
    	
    	if(this.cmode.value.equals(GuiMode.Basic)) {
       mc.displayGuiScreen(new ClickUi());
    	}else if(this.cmode.value.equals(GuiMode.Normal)) {
    		mc.displayGuiScreen(new GuiClickUI());
    	}
        this.setEnabled(false);
        super.onEnable();
    }
    
    @EventHandler
    public void onTick(EventTick e) {
    	int rainbowTick = 0;
        Color rainbow = new Color(Color.HSBtoRGB((float)((double)Minecraft.getMinecraft().player.ticksExisted / 50.0 + Math.sin((double)rainbowTick / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f));
        rainbowcolor = rainbow.getRGB();
//        rainbowcolor = new Color(r.getValue().intValue(),g.getValue().intValue(),b.getValue().intValue()).getRGB();
    }
    
    public void onDisable() {
	// TODO Auto-generated method stub
    	super.onDisable();
    }
    public static enum GuiMode{
    	Basic,
    	Normal;
 
    }
}

