package cn.Arctic.GUI.clickgui;

import java.awt.Color;
import java.util.ArrayList;

import com.google.common.collect.Lists;

import cn.Arctic.Client;
import cn.Arctic.Font.CFontRenderer;
import cn.Arctic.Font.FontLoaders;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.modules.GUI.HUD;
import cn.Arctic.Util.render.RenderUtil;
import cn.Arctic.values.Value;
import net.minecraft.client.gui.Gui;

public class Button
{
    public Module cheat;
    CFontRenderer font;
    public Window parent;
    public int x;
    public int y;
    public int index;
    public int remander;
    public double opacity;
    public ArrayList<ValueButton> buttons;
    public boolean expand;
    public Button(final Module cheat, final int x, final int y) {
        super();
        this.font = FontLoaders.NMSL18;
        this.opacity = 0.0;
        this.buttons = Lists.newArrayList();
        this.cheat = cheat;
        this.x = x;
        this.y = y;
        int y2 = y + 14;
        for (final Value v : cheat.getValues()) {
            this.buttons.add(new ValueButton(v, x + 5, y2));
            y2 += 15;
        }
        this.buttons.add(new KeyBindButton(cheat, x + 5, y2));
    }
    
    public void render(final int mouseX, final int mouseY) {
        if (this.index != 0) {
            final Button b2 = this.parent.buttons.get(this.index - 1);
            this.y = b2.y + 15 + (b2.expand ? (15 * b2.buttons.size()) : 0);
        }
        for (int i = 0; i < this.buttons.size(); ++i) {
            this.buttons.get(i).y = this.y + 14 + 15 * i;
            this.buttons.get(i).x = this.x + 5;
        }
        Gui.drawRect(this.x - 5, this.y - 5, this.x + 85, this.y + this.font.getStringHeight(this.cheat.getName()) + 3, new Color(20, 20, 20,220).getRGB());
        if (this.cheat.isEnabled()) {
            int rainbowTick=0;
            rainbowTick+=100;
            int rainbow2 = RenderUtil.astolfoRainbow(1,10,rainbowTick);
			RenderUtil.R2DUtils.drawRect((float)(this.x - 5), (float)(this.y - 5.3), (float)(this.x + 85), (float)(this.y + this.font.getStringHeight(this.cheat.getName()) + 2.7),new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),120).getRGB());
            this.font.drawString(Client.getModuleName(this.cheat), (float)this.x, (float)this.y-0.5f, new Color(255,255,255).getRGB());
        }
        else {
            this.font.drawString(Client.getModuleName(this.cheat), (float)this.x, (float)this.y-0.5f, new Color(108, 108, 108).getRGB());
        }
//        if (!this.expand && this.buttons.size() > 1) {
//            FontLoaders.NovICON20.drawString("G", (float)(this.x + 76), (float)(this.y + 1), new Color(108, 108, 108).getRGB());
//        }
        if (this.expand) {
            this.buttons.forEach(b -> b.render(mouseX, mouseY));
        }
    }
    
    public void key(final char typedChar, final int keyCode) {
        this.buttons.forEach(b -> b.key(typedChar, keyCode));
    }
    
    public void click(final int mouseX, final int mouseY, final int button) {
        if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + this.font.getStringHeight(this.cheat.getName()) + 4) {
            if (button == 0) {
                this.cheat.setEnabled(!this.cheat.isEnabled());
            }
            if (button == 1 && !this.buttons.isEmpty()) {
                this.expand = !this.expand;
            }
        }
        if (this.expand) {
            this.buttons.forEach(b -> b.click(mouseX, mouseY, button));
        }
    }
    
    public void setParent(final Window parent) {
        this.parent = parent;
        for (int i = 0; i < this.parent.buttons.size(); ++i) {
            if (this.parent.buttons.get(i) == this) {
                this.index = i;
                this.remander = this.parent.buttons.size() - i;
                break;
            }
        }
    }

}
