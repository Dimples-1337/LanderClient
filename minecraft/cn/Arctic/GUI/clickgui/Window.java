package cn.Arctic.GUI.clickgui;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import cn.Arctic.Font.CFontRenderer;
import cn.Arctic.Font.FontLoaders;
import cn.Arctic.Font.me.superskidder.FontLoaders2;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.GUI.HUD;
import cn.Arctic.Util.render.RenderUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class Window
{
    FontRenderer font;
    public ModuleType category;
    public ArrayList<Button> buttons;
    public boolean drag;
    public boolean extended;
    public int x;
    public int y;
    public int expand;
    public int dragX;
    public int dragY;
    public int max;
    public int scroll;
    public int scrollTo;
    public double angel;
    
    public Window(final ModuleType category, final int x, final int y) {
        super();
        this.font = FontLoaders2.msFont18;
        this.buttons = Lists.newArrayList();
        this.category = category;
        this.x = x;
        this.y = y;
        this.max = 120;
        int y2 = y + 22;
        for (final Module c : ModuleManager.getModules()) {
            if (c.getType() != category) {
                continue;
            }
            this.buttons.add(new Button(c, x + 5, y2));
            y2 += 15;
        }
        for (final Button b2 : this.buttons) {
            b2.setParent(this);
        }
    }
    
    public void render(final int mouseX, final int mouseY) {
        int current = 0;
        for (final Button b3 : this.buttons) {
            if (b3.expand) {
                for (final ValueButton v : b3.buttons) {
                    current += 15;
                }
            }
            current += 15;
        }
        final int height = 15 + current;
        if (this.extended) {
            this.expand = ((this.expand + 5 < height) ? (this.expand += 5) : height);
            this.angel = ((this.angel + 20.0 < 180.0) ? (this.angel += 20.0) : 180.0);
        }
        else {
            this.expand = ((this.expand - 5 > 0) ? (this.expand -= 5) : 0);
            this.angel = ((this.angel - 20.0 > 0.0) ? (this.angel -= 20.0) : 0.0);
        }
        RenderUtil.drawRect((float)(this.x - 2), (float)this.y, (float)(this.x + 92), (float)(this.y + 17),new Color(0, 0, 0).getRGB());
        RenderUtil.drawGradientSideways((float)(this.x - 2), (float)this.y, (float)(this.x + 92), (float)(this.y + 17), new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),100).getRGB(), new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue()).getRGB());
        this.font.drawString(this.category.name(), (float)(this.x + 20), (float)(this.y + 6), new Color(255,255,255).getRGB());
        if (this.category.name().equals("Combat")) {
        	Gui.drawRect((float)(this.x-1.5), (float)this.y, (float)(this.x + 17), (float)(this.y + 17),new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),100).getRGB());
            FontLoaders.click24.drawString("1", (float)(this.x + 2), (float)(this.y + 5), new Color(255, 255, 255).getRGB());
        }
        if (this.category.name().equals("Render")) {
        	Gui.drawRect((float)(this.x -1.5), (float)this.y, (float)(this.x + 17), (float)(this.y + 17),new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),100).getRGB());
            FontLoaders.click24.drawString("0", (float)(this.x + 2), (float)(this.y + 5), new Color(255, 255, 255).getRGB());
        }
        if (this.category.name().equals("Movement")) {
        	Gui.drawRect((float)(this.x -1.5), (float)this.y, (float)(this.x + 17), (float)(this.y + 17),new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),100).getRGB());
            FontLoaders.click24.drawString("5", (float)(this.x + 2), (float)(this.y + 5), new Color(255, 255, 255).getRGB());
        }
        if (this.category.name().equals("Player")) {
        	Gui.drawRect((float)(this.x -1.5), (float)this.y, (float)(this.x + 17), (float)(this.y + 17),new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),100).getRGB());
            FontLoaders.click24.drawString("6", (float)(this.x + 2), (float)(this.y + 5), new Color(255, 255, 255).getRGB());
        }
        if (this.category.name().equals("World")) {
        	Gui.drawRect((float)(this.x -1.5), (float)this.y, (float)(this.x + 17), (float)(this.y + 17),new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),100).getRGB());
            FontLoaders.click24.drawString("3", (float)(this.x + 2), (float)(this.y + 5), new Color(255, 255, 255).getRGB());
        }
        if (this.category.name().equals("Gui")) {
        	Gui.drawRect((float)(this.x -1.5), (float)this.y, (float)(this.x + 17), (float)(this.y + 17),new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),100).getRGB());
            FontLoaders.click24.drawString("2", (float)(this.x + 2), (float)(this.y + 5), new Color(255, 255, 255).getRGB());
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.x + 90 - 10), (float)(this.y + 5), 0.0f);
        GlStateManager.rotate((float)this.angel, 0.0f, 0.0f, -1.0f);
        GlStateManager.translate((float)(-this.x + 90 - 10), (float)(-this.y + 5), 0.0f);
        GlStateManager.popMatrix();
        if (this.expand > 0) {
            this.buttons.forEach(b2 -> b2.render(mouseX, mouseY));
        }
        if (this.drag) {
            if (!Mouse.isButtonDown(0)) {
                this.drag = false;
            }
            this.x = mouseX - this.dragX;
            this.y = mouseY - this.dragY;
            this.buttons.get(0).y = this.y + 22 - this.scroll;
            for (final Button b4 : this.buttons) {
                b4.x = this.x + 5;
            }
        }
    }
    
    public void key(final char typedChar, final int keyCode) {
        this.buttons.forEach(b2 -> b2.key(typedChar, keyCode));
    }
    
    public void mouseScroll(final int mouseX, final int mouseY, final int amount) {
        if (mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y - 2 && mouseY < this.y + 17 + this.expand) {
            this.scrollTo -= (int)(float)(amount / 120 * 28);
        }
    }
    
    public void click(final int mouseX, final int mouseY, final int button) {
        if (mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y - 2 && mouseY < this.y + 17) {
            if (button == 1) {
                this.extended = !this.extended;
            }
            if (button == 0) {
                this.drag = true;
                this.dragX = mouseX - this.x;
                this.dragY = mouseY - this.y;
            }
        }
        if (this.extended) {
            this.buttons.stream().filter(b2 -> b2.y < this.y + this.expand).forEach(b2 -> b2.click(mouseX, mouseY, button));
        }
    }
}
