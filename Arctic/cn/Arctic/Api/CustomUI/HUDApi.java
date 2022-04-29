package cn.Arctic.Api.CustomUI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public class HUDApi {
	
    public String name;
    public int x;
    public int y;
    public boolean enabled;

    public static boolean useISR;
    
    public Minecraft mc = Minecraft.getMinecraft();

    public HUDApi(String name,int x,int y){
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public void onRender(){
    	
    }

    public void InScreenRender(){
    	this.useISR = true;
    }

    public String getName() {
        return this.name;
    }

    public boolean isEnabled(){
        return this.enabled;
    }

    public void setEnabled(boolean b){
        this.enabled = b;
    }

    public void setXY(int newX,int newY){
        this.x = newX;
        this.y = newY;
    }
}
