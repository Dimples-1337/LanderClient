package cn.Arctic.Api.CustomUI;

import com.google.common.collect.Lists;

import cn.Arctic.Util.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;

public class HUDWindow {
	
    public HUDApi api;
    public boolean drag;
    public int x;
    public int y;
    public int dragX;
    public int dragY;

    public HUDWindow(HUDApi api, int x2, int y2) {
        this.api = api;
        this.x = x2;
        this.y = y2;
    }

    public void render(int mouseX, int mouseY) {
        if(api.enabled) {
            RenderUtil.drawFilledCircle(api.x - 4, api.y - 4, 4, new Color(0, 177, 35).getRGB());
        }else {
            RenderUtil.drawFilledCircle(api.x - 4, api.y - 4, 4, new Color(45, 49, 45).getRGB());
        }
        if (this.drag) {
            if (!Mouse.isButtonDown(0)) {
                this.drag = false;
            }
            this.x = mouseX - this.dragX;
            this.y = mouseY - this.dragY;

            api.setXY(x,y);
        }
    }

    public void mouseScroll(int mouseX, int mouseY, int amount) {
    }

    public void click(int mouseX, int mouseY, int button) {
        if (mouseX > api.x - 8 && mouseX < api.x && mouseY > api.y - 8 && mouseY < api.y) {
            if (button == 0) {
                this.drag = true;
                this.dragX = mouseX - this.x;
                this.dragY = mouseY - this.y;
            }
            if(button == 1){
                api.enabled = !api.enabled;
            }
        }
    }
}