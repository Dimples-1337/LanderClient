package cn.Arctic.Module.modules.RENDER;


import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.IOException;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventAttack;
import cn.Arctic.Event.events.EventPacket;
import cn.Arctic.Event.events.EventPacketSend;
import cn.Arctic.Event.events.EventRender2D;
import cn.Arctic.Manager.SoundManger;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.COMBAT.Aura;
import cn.Arctic.Util.Colors2;
import cn.Arctic.Util.Music;
import cn.Arctic.Util.render.ColorUtils;
import cn.Arctic.Util.render.RenderUtil;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;

public class Crosshair extends Module
{
    private boolean dragging;
    float hue;
    private Option<Boolean> DYNAMIC;
    private Option<Boolean> Rainbow = new Option<Boolean>("Rainbow", false);
    public static Numbers<Double> GAP;
    private Numbers<Double> WIDTH;
    public static Numbers<Double> SIZE;
	public static Numbers<Double> r = new Numbers<Double>("Red", 255.0, 0.0, 255.0, 1.0);
	public static Numbers<Double> g = new Numbers<Double>("Green", 255.0, 0.0, 255.0, 1.0);
	public static Numbers<Double> b = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 1.0);
	public static Numbers<Double> a = new Numbers<Double>("Alpha", 160.0, 0.0, 255.0, 1.0);
    
    static {
        Crosshair.GAP = new Numbers<Double>("gap", 5.0, 0.25, 15.0, 0.25);
        Crosshair.SIZE = new Numbers<Double>("size", 7.0, 0.25, 15.0, 0.25);
    }
    
    public Crosshair() {
        super("Crosshair", new String[] { "Crosshair" }, ModuleType.Render);
        this.DYNAMIC = new Option<Boolean>("Dynamic", true);
        this.WIDTH = new Numbers<Double>("Width", 2.0, 0.25, 10.0, 0.25);
        this.addValues(this.DYNAMIC,this.Rainbow,Crosshair.GAP, this.WIDTH, Crosshair.SIZE, r, g, b, a);
    }
    
    @EventHandler
    public void onGui(final EventRender2D e) {
        if (mc.gameSettings.thirdPersonView == 2 || mc.gameSettings.thirdPersonView == 1) {
            return;
        }
        final int red = r.getValue().intValue();
        final int green = g.getValue().intValue();
        final int blue = b.getValue().intValue();
        final int alph = a.getValue().intValue();
        final double gap = Crosshair.GAP.getValue();
        final double width = this.WIDTH.getValue();
        final double size = Crosshair.SIZE.getValue();
        final ScaledResolution scaledRes = new ScaledResolution(Crosshair.mc);
        if (this.Rainbow.getValue() == true) {
            RenderUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 - width, scaledRes.getScaledHeight() / 2 - gap - size - (this.isMoving() ? 2 : 0), scaledRes.getScaledWidth() / 2 + 1.0f + width, scaledRes.getScaledHeight() / 2 - gap - (this.isMoving() ? 2 : 0), 0.5, ColorUtils.rainbow(1, 1).getRGB(), new Color(0, 0, 0, alph).getRGB());
            RenderUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 - width, scaledRes.getScaledHeight() / 2 + gap + 1.0 + (this.isMoving() ? 2 : 0) - 0.15, scaledRes.getScaledWidth() / 2 + 1.0f + width, scaledRes.getScaledHeight() / 2 + 1 + gap + size + (this.isMoving() ? 2 : 0) - 0.15, 0.5, ColorUtils.rainbow(1, 1).getRGB(), new Color(0, 0, 0, alph).getRGB());
            RenderUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 - gap - size - (this.isMoving() ? 2 : 0) + 0.15, scaledRes.getScaledHeight() / 2 - width, scaledRes.getScaledWidth() / 2 - gap - (this.isMoving() ? 2 : 0) + 0.15, scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5, ColorUtils.rainbow(1, 1).getRGB(), new Color(0, 0, 0, alph).getRGB());
            RenderUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 + 1 + gap + (this.isMoving() ? 2 : 0), scaledRes.getScaledHeight() / 2 - width, scaledRes.getScaledWidth() / 2 + size + gap + 1.0 + (this.isMoving() ? 2 : 0), scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5, ColorUtils.rainbow(1, 1).getRGB(), new Color(0, 0, 0, alph).getRGB());
        } else {
            RenderUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 - width, scaledRes.getScaledHeight() / 2 - gap - size - (this.isMoving() ? 2 : 0), scaledRes.getScaledWidth() / 2 + 1.0f + width, scaledRes.getScaledHeight() / 2 - gap - (this.isMoving() ? 2 : 0), 0.5, Colors2.getColor(red, green, blue, alph), new Color(0, 0, 0, alph).getRGB());
            RenderUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 - width, scaledRes.getScaledHeight() / 2 + gap + 1.0 + (this.isMoving() ? 2 : 0) - 0.15, scaledRes.getScaledWidth() / 2 + 1.0f + width, scaledRes.getScaledHeight() / 2 + 1 + gap + size + (this.isMoving() ? 2 : 0) - 0.15, 0.5, Colors2.getColor(red, green, blue, alph), new Color(0, 0, 0, alph).getRGB());
            RenderUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 - gap - size - (this.isMoving() ? 2 : 0) + 0.15, scaledRes.getScaledHeight() / 2 - width, scaledRes.getScaledWidth() / 2 - gap - (this.isMoving() ? 2 : 0) + 0.15, scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5, Colors2.getColor(red, green, blue, alph), new Color(0, 0, 0, alph).getRGB());
            RenderUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 + 1 + gap + (this.isMoving() ? 2 : 0), scaledRes.getScaledHeight() / 2 - width, scaledRes.getScaledWidth() / 2 + size + gap + 1.0 + (this.isMoving() ? 2 : 0), scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5, Colors2.getColor(red, green, blue, alph), new Color(0, 0, 0, alph).getRGB());
        }
        
        if((mc.objectMouseOver.entityHit !=null && Mouse.isButtonDown(0))||Aura.curTarget !=null) {
        	 try { 
        		 new SoundManger().hit(); 
        		 } catch (IOException sb) { 
        		 sb.printStackTrace(); 
        		 }
        	  GL11.glPushMatrix();
              GlStateManager.enableBlend();
              GlStateManager.disableTexture2D();
              GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
              GL11.glEnable(GL11.GL_LINE_SMOOTH);
              GL11.glLineWidth(1f);
              GL11.glBegin(3);
              GL11.glVertex2d(scaledRes.scaledWidth / 2f + gap+2, scaledRes.scaledHeight / 2f + gap+2);
              GL11.glVertex2d(scaledRes.scaledWidth / 2f + gap + size, scaledRes.scaledHeight / 2f + gap + size);
              GL11.glEnd();
              GL11.glBegin(3);
              GL11.glVertex2d(scaledRes.scaledWidth / 2f - gap-2, scaledRes.scaledHeight / 2f - gap-2);
              GL11.glVertex2d(scaledRes.scaledWidth / 2f - gap - size, scaledRes.scaledHeight / 2f - gap - size);
              GL11.glEnd();
              GL11.glBegin(3);
              GL11.glVertex2d(scaledRes.scaledWidth / 2f - gap-2, scaledRes.scaledHeight / 2f + gap+2);
              GL11.glVertex2d(scaledRes.scaledWidth / 2f - gap - size, scaledRes.scaledHeight / 2f + gap + size);
              GL11.glEnd();
              GL11.glBegin(3);
              GL11.glVertex2d(scaledRes.scaledWidth / 2f + gap+2, scaledRes.scaledHeight / 2f - gap-2);
              GL11.glVertex2d(scaledRes.scaledWidth / 2f + gap + size, scaledRes.scaledHeight / 2f - gap - size);
              GL11.glEnd();
              GlStateManager.enableTexture2D();
              GlStateManager.disableBlend();
              GL11.glPopMatrix();
              
              if(Mouse.isButtonDown(2)) {
              mc.player.sendChatMessage(".f add "+mc.objectMouseOver.entityHit.getName());
              }
    }
    }
    
    
    public boolean isMoving() {
        if (this.DYNAMIC.getValue()) {
            final Minecraft mc = Crosshair.mc;
            if (!mc.player.isCollidedHorizontally) {
                final Minecraft mc2 = Crosshair.mc;
                if (!mc.player.isSneaking()) {
                    final MovementInput movementInput = mc.player.movementInput;
                    if (MovementInput.moveForward == 0.0f) {
                        final MovementInput movementInput2 = mc.player.movementInput;
                        if (MovementInput.moveStrafe == 0.0f) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public void onEnable() {

    }
}
