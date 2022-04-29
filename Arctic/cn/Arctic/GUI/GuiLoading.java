package cn.Arctic.GUI;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import viamcp.ViaMCP;
import viamcp.protocols.ProtocolCollection;

import java.awt.*;
import java.util.Random;

import cn.Arctic.Font.CFontRenderer;
import cn.Arctic.Font.FontLoaders;
import cn.Arctic.GUI.Menu.MainMenu;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.Util.animate.Translate;
import cn.Arctic.Util.render.RenderUtil;



public class GuiLoading extends GuiScreen {
    private static String Welcome;
    private static boolean verifyed;
    private static long startTime = 0L;
    Translate translat;
    int alpha = 0;
    TimerUtil timer = new TimerUtil();
    String[] bye2 = {
    		"Ready To Login"
    };
//    String[] bye = {
//    		"\"Welcome to Lander\"",
//    		"\"Welcome to Lander.\""
//    };
    Random r = new Random();
    String bye2r = bye2[r.nextInt(bye2.length)];
  //  String byer = bye[r.nextInt(bye.length)];
    private float currentX, targetX, currentY, targetY;
    int textalpha = 255;
    double Anitext = 10;

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
        CFontRenderer fontwel2 = FontLoaders.NMSL18;
        CFontRenderer fontwel = FontLoaders.NMSL35;

        //GlStateManager.translate(currentX / 100, currentY / 100, 0);

        mc.displayGuiScreen(new MainMenu());

      //  GlStateManager.translate(currentX / 15, currentY / 15, 0);




      //  GlStateManager.translate(-currentX / 100, -currentY / 100, 0);

        fontwel.drawCenteredString("Ready To Login", ScaledResolution.getScaledWidth() / 2.0f,
				ScaledResolution.getScaledHeight() / 2.0f - 3.0f - (float) this.Anitext, new Color(255, 255, 255).getRGB());
        // fontwel2.drawStringWithShadow(bye2r, (float) sr.getScaledWidth() / 2, (float) sr.getScaledHeight() / 2 + fontwel.getStringHeight("Welcome") - (float) Anitext, new Color(255, 255, 255).getRGB(), textalpha);
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
        	  try {
        		  new ViaMCP().start();
              } catch (Exception e) {
                  e.printStackTrace();
              }
        }
    }
}
