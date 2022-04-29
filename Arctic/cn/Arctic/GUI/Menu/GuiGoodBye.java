package cn.Arctic.GUI.Menu;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.Random;

import cn.Arctic.Client;
import cn.Arctic.Font.me.superskidder.FontLoaders2;


public class GuiGoodBye extends GuiScreen {
    private static String Welcome;
    private static boolean verifyed;
    private static long startTime = 0L;
    int alpha = 0;
    String[] bye2 = {
    		"\"Love you three thousand times.\"",
    		"\"Thanks for playing, hope to interface again.\""
    };
    String[] bye = {
            "\"Love you three thousand times.\"",
            "\"Thanks for playing, hope to interface again.\""
    };
    Random r = new Random();
    String bye2r = bye2[r.nextInt(bye2.length)];
    String byer = bye[r.nextInt(bye.length)];
    private float currentX, targetX, currentY, targetY;
    int textalpha = 255;
    double Anitext = 0;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        int h = new ScaledResolution(this.mc).getScaledHeight();
        int w = new ScaledResolution(this.mc).getScaledWidth();
        float xDiff = ((mouseX - h / 2) - currentX) / sr.getScaleFactor();
        float yDiff = ((mouseY - w / 2) - currentY) / sr.getScaleFactor();
        currentX += xDiff * 0.3F;
        currentY += yDiff * 0.3F;
        if (startTime == 0L) startTime = System.currentTimeMillis();
        FontRenderer fontwel2 = FontLoaders2.msFont18;
        FontRenderer fontwel = FontLoaders2.msFont36;

        GlStateManager.translate(currentX / 100, currentY / 100, 0);
        this.drawDefaultBackground();
    	Client.shouldShaderBack = true;

        GlStateManager.translate(currentX / 15, currentY / 15, 0);



        GlStateManager.translate(-currentX / 100, -currentY / 100, 0);

        fontwel.drawStringWithShadow(bye2r, (float) sr.getScaledWidth() / 2, (float) sr.getScaledHeight() / 2 - 3f - (float) Anitext, new Color(255, 255, 255).getRGB());
        fontwel2.drawStringWithShadow(byer, (float) sr.getScaledWidth() / 2, (float) sr.getScaledHeight() / 2 + fontwel.getHeight() - (float) Anitext, new Color(255, 255, 255).getRGB());
        GlStateManager.translate(0, 0, 0);
        Gui.drawRect(-100, -100, sr.getScaledWidth() + 100, sr.getScaledHeight() + 100, new Color(0, 0, 0, alpha).getRGB());
        if (startTime + 2000 <= System.currentTimeMillis()) {
            if (alpha != 255) {
                alpha++;
            }

            if (alpha != 255) {
                alpha++;
            }

            if (alpha != 255) {
                alpha++;
            }
        }
        if (alpha >= 255 && startTime + 3000 <= System.currentTimeMillis()) {
            mc.shutdown();
        }
    }
}
