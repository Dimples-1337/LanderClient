package cn.Noble.GUI.notifications;

import java.awt.Color;

import javax.management.timer.Timer;

import org.lwjgl.opengl.GL11;

import cn.Noble.Client;
import cn.Noble.Font.CFontRenderer;
import cn.Noble.Font.FontLoaders;
import cn.Noble.Util.Logger;
import cn.Noble.Util.Timer.TimeHelper;
import cn.Noble.Util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

public class Notification
{
	private Type type;
    private String message;
    private TimeHelper timer;
    private double lastY;
    private double posY;
    private double cnmcnmcnm;
    private double height;
    private double animationX;
    private int color;
    private int imageWidth;
    private ResourceLocation image;
    private long stayTime;
    Minecraft mc = Minecraft.getMinecraft();
	private long start;
	private long end;
    public Notification(final String message, final Type type ,final int time) {
        this.message = message;
        (this.timer = new TimeHelper()).reset();
        CFontRenderer font = FontLoaders.NMSL21;
        this.cnmcnmcnm = font.getStringHeight(message);
        this.height = 20.0;
        this.animationX = this.cnmcnmcnm;
        this.stayTime = time;
        this.imageWidth = 13;
        this.posY = -1.0;
    }
    

	public void show() {
		start = System.currentTimeMillis();
	}

	private long getTime() {
		return System.currentTimeMillis() - start;
	}
	public boolean isShown() {
		return getTime() <= end;
	}
	public void draw(final double getY, final double lastY) {
    	this.lastY = lastY;
        CFontRenderer font = FontLoaders.NMSL21;
        this.animationX = RenderUtil.getAnimationState(this.animationX, this.isFinished() ? this.cnmcnmcnm : 0.0, Math.max(this.isFinished() ? 400 : 10, Math.abs(this.animationX - (this.isFinished() ? this.cnmcnmcnm : 0.0)) * 10.5));
        if (this.posY == -1.0) {
            this.posY = getY;
        }
        else {
            this.posY = RenderUtil.getAnimationState(this.posY, getY, 1050.0);
        }
        final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        final int x1 = (int)(res.getScaledWidth()/2+126-font.getStringHeight(message))+15;
        final int x2 = (int)(res.getScaledWidth()/2+126);
        final int y1 = (int) (this.posY-cnmcnmcnm+animationX);
        final int y2 = (int)(y1 +animationX);

        RenderUtil.drawRoundedRect( (float)(x1-29), (float)(y1 + this.height / 2.5)-14, (float)(x1-18+font.getStringWidth(this.message)), (float)(y1 + this.height / 2.5)-4+font.getStringHeight(this.message), 
        		1,new Color(40, 40, 40, 130).getRGB());
        font.drawString(this.message, (float)(x1-23), (float)(y1 + this.height / 2.5)-9, new Color(250,250,250,100).getRGB());
        
    }
    
    public boolean shouldDelete() {
        return this.isFinished() && this.animationX >= this.cnmcnmcnm;
    }
    
    private boolean isFinished() {
        return this.timer.isDelayComplete(this.stayTime) && this.posY == this.lastY;
    }
    
    public double getHeight() {
        return this.height;
    }
    
    public enum Type
    {
        SUCCESS("SUCCESS", 0), 
        INFO("INFO", 1), 
        WARNING("WARNING", 2), 
        ERROR("ERROR", 3);
        
        private Type(final String s, final int n) {
        }
    }
}



