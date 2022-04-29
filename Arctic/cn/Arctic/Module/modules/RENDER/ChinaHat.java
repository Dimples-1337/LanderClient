package cn.Arctic.Module.modules.RENDER;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventRender3D;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Numbers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ChinaHat extends Module {
	public static Numbers<Double> sideValue = new Numbers<Double>("side",45.0, 40.0, 50.0, 1.0);
	public static Numbers<Double> stackValue = new Numbers<Double>("stack",200.0, 200.0, 500.0, 1.0);
	public static Numbers<Double> colorRedValue = new Numbers<Double>("Red",255.0, 0.0, 255.0, 1.0);
	public static Numbers<Double> colorGreenValue = new Numbers<Double>("Green",255.0, 0.0, 255.0, 1.0);
	public static Numbers<Double> colorBlueValue = new Numbers<Double>("Blue",255.0, 0.0, 255.0, 1.0);
	public static Numbers<Double> colorAlphaValue = new Numbers<Double>("Alpha",200.0, 0.0, 255.0, 1.0);
	public static Mode<Enum> modmod = new Mode("Mode", (Enum[]) SBMODE.values(), (Enum)SBMODE.rainbow);
    public static float hue = 0.0F;
	public ChinaHat() {
		super("ChinaHat", new String[]{"ChinaHat"}, ModuleType.Render);
		this.setColor(Color.BLUE.getRGB());
		this.addValues(sideValue,stackValue,colorRedValue,colorGreenValue,colorBlueValue,colorAlphaValue,modmod);
	}

    public static Color generateColor() {
        Color color = new Color(255, 255, 255, 255);
        double x = colorRedValue.getValue();
        double y = colorGreenValue.getValue();
        double z = colorBlueValue.getValue();
        double r = colorAlphaValue.getValue();
        Color rainbowcolors2 = Color.getHSBColor(hue / 255.0f, 1f, 1f);
        if(modmod.getValue() == SBMODE.custom) {
            color = new Color((int)x,(int)y,(int)z,(int)r); 
        }else if(modmod.getValue() == SBMODE.rainbow) {
        	color = new Color(rainbowcolors2.getRed(), 190, 255,(int)r);
        }
        hue += 5.0 / 15.0F;
        if (hue > 255.0F) {
            hue = 0.0F;
        }
        return color;
    }



    @EventHandler
    public void onRender3D(EventRender3D event){
		ArrayList<EntityPlayer> validEnt = new ArrayList<EntityPlayer>();
		if (validEnt.size() > 100) {
			validEnt.clear();
		}
		for (EntityLivingBase player2 : this.mc.world.playerEntities) {
			if (player2.isEntityAlive()) {
				if (player2.isInvisible()) {
					if (!validEnt.contains(player2))
						continue;
					validEnt.remove(player2);
					continue;
				}
					if (player2 == this.mc.player) {
						if (!validEnt.contains(player2))
							continue;
						validEnt.remove(player2);
						continue;
					}
				
				if (validEnt.size() > 100)
					break;
				if (validEnt.contains(player2))
					continue;
				validEnt.add((EntityPlayer) player2);
				continue;
			}
			if (!validEnt.contains(player2))
				continue;
			validEnt.remove(player2);
		}
        mc.getRenderManager();
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0)
            return;
        drawCircle(Minecraft.getMinecraft().player , generateColor().getRGB() , event);
    }

    public static void  drawCircle(EntityLivingBase entity , int color ,EventRender3D e){
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * e.getPartialTicks() - Minecraft.getMinecraft().getRenderManager().renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * e.getPartialTicks() - Minecraft.getMinecraft().getRenderManager().renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * e.getPartialTicks() - Minecraft.getMinecraft().getRenderManager().renderPosZ;
        float radius = 0.8F;
        double side = sideValue.getValue();
        float base = 0;
        float height = 0.4F;
        double stack = stackValue.getValue();

        GL11.glPushMatrix();
        GL11.glTranslated(x, y + 2.2D, z);
        GL11.glRotatef(-entity.width, 0.0F, 1.0F, 0.0F);
        glColor(color);
        enableSmoothLine(1.0F);
        Cylinder c = new Cylinder();
        GL11.glRotatef(90F, 1.0F, 0F, 0F);
        c.setDrawStyle(100011);
        c.draw(base, radius, height, (int)(side), (int)(stack));

        disableSmoothLine();
        GL11.glPopMatrix();
    }

    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, (alpha == 0.0F) ? 1.0F : alpha);
    }

    public static void enableSmoothLine(float width) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(width);
    }

    public static void disableSmoothLine() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    enum SBMODE {
    	custom,rainbow;
	}
}