package cn.Arctic.Api.CustomUI;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Predicate;

import com.google.common.collect.Lists;

import cn.Arctic.Font.FontLoaders;
import cn.Arctic.Manager.FileManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public class HUDScreen extends GuiScreen {

    public static ArrayList<HUDWindow> HUDWindows = Lists.newArrayList();
    public int scrollVelocity;

    public HUDScreen() {
        if (HUDWindows.isEmpty()) {
            int n22 = 0;
            while (n22 < HUDManager.getApis().size()) {
                HUDApi c2 = HUDManager.getApis().get(n22);
                HUDWindows.add(new HUDWindow(c2,c2.x,c2.y));
                ++n22;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            this.scrollVelocity = wheel < 0 ? -120 : (wheel > 0 ? 130 : 0);
        }
        
        FontLoaders.NMSL20.drawStringWithShadow("RightClick To Toggle.", width/2 - FontLoaders.NMSL20.getStringWidth("RightClick To Toggle.")/2 , height-height + 9, new Color(255,20,0).getRGB());

        HUDWindows.forEach(w2 -> w2.render(mouseX,mouseY));
        HUDManager.getApis().forEach(w2 -> w2.InScreenRender());
        
        HUDWindows.forEach(w2 -> w2.mouseScroll(mouseX,mouseY,scrollVelocity));

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        HUDWindows.forEach(w2 -> w2.click(mouseX, mouseY, mouseButton));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed(){
        String x = "";
        for (HUDApi m : HUDManager.getApis()) {
            x = String.valueOf(x) + String.format("%s:%s:%s:%s%s", m.getName(), m.x, m.y, m.enabled, System.lineSeparator());
        }
        FileManager.save("HUD.cfg", x, false);
    }
}
