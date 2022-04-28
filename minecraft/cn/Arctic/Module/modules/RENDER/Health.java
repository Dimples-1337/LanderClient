package cn.Arctic.Module.modules.RENDER;


import java.awt.Color;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventRender2D;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.values.Numbers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;


public class Health extends Module
{
    private int width;
    
    public Health() {
        super("Health", new String[] { "Healthy" }, ModuleType.Render);
    }
    
    @EventHandler
    private void renderHud(final EventRender2D event) {
        final Minecraft mc = Health.mc;
        if (Minecraft.player.getHealth() >= 0.0f) {
            final Minecraft mc2 = Health.mc;
            if (Minecraft.player.getHealth() < 10.0f) {
                this.width = 3;
            }
        }
        final Minecraft mc3 = Health.mc;
        if (Minecraft.player.getHealth() >= 10.0f) {
            final Minecraft mc4 = Health.mc;
            if (Minecraft.player.getHealth() < 100.0f) {
                this.width = 6;
            }
        }
        final FontRenderer fontRendererObj = Health.mc.fontRendererObj;
        final String string = new StringBuilder().append(MathHelper.ceiling_float_int(Minecraft.player.getHealth())).toString();
        final float x = (float)(new ScaledResolution(Health.mc).getScaledWidth() / 2 - this.width);
        final float y = new ScaledResolution(Health.mc).getScaledHeight() / 2 - 13 - (float)(double)Crosshair.SIZE.getValue() - (float)(double)Crosshair.GAP.getValue();
        final Minecraft mc5 = Health.mc;
        fontRendererObj.drawStringWithShadow(string, x, y, (Minecraft.player.getHealth() <= 10.0f) ? new Color(255, 0, 0).getRGB() : new Color(0, 255, 0).getRGB());
    }
}

 