package cn.Arctic.GUI.clickgui;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import cn.Arctic.Font.CFontRenderer;
import cn.Arctic.Font.FontLoaders;
import cn.Arctic.Module.Module;
import net.minecraft.client.gui.Gui;


public class KeyBindButton extends ValueButton
{
    public static Module cheat;
    public double opacity;
    public static boolean bind;
    CFontRenderer font;
    
    public KeyBindButton(final Module cheat, final int x, final int y) {
        super(null, x, y);
        this.opacity = 0.0;
        this.font = FontLoaders.NMSL18;
        this.custom = true;
        this.bind = false;
        this.cheat = cheat;
    }
    
    @Override
    public void render(final int mouseX, final int mouseY) {
        Gui.drawRect(0.0, 0.0, 0.0, 0.0, 0);
        Gui.drawRect(this.x - 10, this.y - 5, this.x + 80, this.y + 11, new Color(20, 20, 20,190).getRGB());
        FontLoaders.NMSL18.drawString("Bind", (float)(this.x - 5), (float)(this.y + 2), new Color(108, 108, 108).getRGB());
        this.mfont.drawString(String.valueOf(this.bind ? "" : "") + Keyboard.getKeyName(this.cheat.getKey()), (float)(this.x + 76 - this.mfont.getStringWidth(Keyboard.getKeyName(this.cheat.getKey()))), (float)(this.y + 2), new Color(108, 108, 108, 87).getRGB());
    }
    
    @Override
    public void key(final char typedChar, final int keyCode) {
        if (this.bind) {
            this.cheat.setKey(keyCode);
            if (keyCode == 1) {
                this.cheat.setKey(0);
            }
            ClickUi.binding = false;
            this.bind = false;
        }
        super.key(typedChar, keyCode);
    }
    
    @Override
    public void click(final int mouseX, final int mouseY, final int button) {
        if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + this.font.getStringHeight(this.cheat.getName()) + 5 && button == 0) {
            final boolean b = !this.bind;
            this.bind = b;
            ClickUi.binding = b;
        }
        super.click(mouseX, mouseY, button);
    }
}
