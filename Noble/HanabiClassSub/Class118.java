package HanabiClassSub;

import java.time.format.*;
import java.text.*;
import java.awt.*;
import org.lwjgl.opengl.*;

import cn.Noble.Event.events.EventRender2D;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Values.Value;
import net.minecraft.entity.player.*;
import net.minecraft.client.*;
import net.minecraft.entity.boss.*;
import net.minecraft.item.*;
import net.minecraft.client.gui.*;
import net.minecraft.potion.*;
import net.minecraft.client.resources.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import java.util.*;

public class Class118 extends Module{
    public Class118(String name, String[] alias, ModuleType type) {
		super(name, alias, type);
		// TODO 自动生成的构造函数存根
	}

	public Value<Boolean> arraylist;
    public Value<Boolean> logo;
    public static Value<Boolean> hotbar;
    public Value<Boolean> music;
    public Value<Boolean> potion;
    public Value<Boolean> noti;
    public static Value<Double> musicPosX;
    public static Value<Double> musicPosY;
    public static Value<Double> musicPosYlyr;
    SimpleDateFormat f;
    SimpleDateFormat f2;
    private float animationY2;
    Map<Potion, Double> timerMap;
    private int x;
    
    public void onReload() {
        class Class14 extends Thread
        {
            final Class118 val$hud;
            final Class118 this$0;
            
            Class14(final Class118 this$0, final Class118 val$hud) {
                this.this$0 = this$0;
                this.val$hud = val$hud;
            }
            
//            @Override
//            public void run() {
//                this.val$hud.set(false);
//                try {
//                    Thread.sleep(500L);
//                }
//                catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//                this.val$hud.set(true);
//            }
        }
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static int rainbow(final int n) {
        return Color.getHSBColor((float)(Math.ceil((System.currentTimeMillis() + n) / 20.0) % 360.0 / 360.0), 0.8f, 0.7f).getRGB();
    }
    
    public void drawRoundedRect(float n, float n2, float n3, float n4, final int n5, final int n6) {
        enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(n *= 2.0f, (n2 *= 2.0f) + 1.0f, (n4 *= 2.0f) - 2.0f, n5);
        drawVLine((n3 *= 2.0f) - 1.0f, n2 + 1.0f, n4 - 2.0f, n5);
        drawHLine(n + 2.0f, n3 - 3.0f, n2, n5);
        drawHLine(n + 2.0f, n3 - 3.0f, n4 - 1.0f, n5);
        drawHLine(n + 1.0f, n + 1.0f, n2 + 1.0f, n5);
        drawHLine(n3 - 2.0f, n3 - 2.0f, n2 + 1.0f, n5);
        drawHLine(n3 - 2.0f, n3 - 2.0f, n4 - 2.0f, n5);
        drawHLine(n + 1.0f, n + 1.0f, n4 - 2.0f, n5);
        Class246.drawRect(n + 1.0f, n2 + 1.0f, n3 - 1.0f, n4 - 1.0f, n6);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }
    
    public void color(final int n) {
        GL11.glColor4f((n >> 16 & 0xFF) / 255.0f, (n >> 8 & 0xFF) / 255.0f, (n & 0xFF) / 255.0f, (n >> 24 & 0xFF) / 255.0f);
    }
    
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    } 
    
    public static void drawRoundedRect2(float n, float n2, float n3, float n4, final int n5, final int n6) {
        enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(n *= 2.0f, (n2 *= 2.0f) + 1.0f, (n4 *= 2.0f) - 2.0f, n5);
        drawVLine((n3 *= 2.0f) - 1.0f, n2 + 1.0f, n4 - 2.0f, n5);
        drawHLine(n + 2.0f, n3 - 3.0f, n2, n5);
        drawHLine(n + 2.0f, n3 - 3.0f, n4 - 1.0f, n5);
        drawHLine(n + 1.0f, n + 1.0f, n2 + 1.0f, n5);
        drawHLine(n3 - 2.0f, n3 - 2.0f, n2 + 1.0f, n5);
        drawHLine(n3 - 2.0f, n3 - 2.0f, n4 - 2.0f, n5);
        drawHLine(n + 1.0f, n + 1.0f, n4 - 2.0f, n5);
        Class246.drawRect(n + 1.0f, n2 + 1.0f, n3 - 1.0f, n4 - 1.0f, n6);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }
    
    public static void drawHLine(float n, float n2, final float n3, final int n4) {
        if (n2 < n) {
            final float n5 = n;
            n = n2;
            n2 = n5;
        }
        Class246.drawRect(n, n3, n2 + 1.0f, n3 + 1.0f, n4);
    }
    
    public static void drawVLine(final float n, float n2, float n3, final int n4) {
        if (n3 < n2) {
            final float n5 = n2;
            n2 = n3;
            n3 = n5;
        }
        Class246.drawRect(n, n2 + 1.0f, n + 1.0f, n3, n4);
    }
    
    private static int lambda$new$0(final Map.Entry entry) {
        return entry.getKey().toString().hashCode();
    }
}
