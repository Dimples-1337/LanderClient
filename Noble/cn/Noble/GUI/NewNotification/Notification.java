package cn.Noble.GUI.NewNotification;

import cn.Noble.Font.CFontRenderer;
import cn.Noble.Util.Timer.TimerUtil;
import cn.Noble.Util.animate.Translate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public final class Notification {
    public static final int HEIGHT = 30;
    private final String title;
    private final String content;
    private final int time;
    private final NotificationType type;
    private final TimerUtil timer;
    private final Translate translate;
    private final CFontRenderer fontRenderer;
    public double scissorBoxWidth;
    public boolean showTime;

    public Notification(String title, String content, NotificationType type, CFontRenderer fr , int time, boolean showTime) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.type = type;
        this.showTime = showTime;
        this.timer = new TimerUtil();
        this.fontRenderer = fr;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        this.translate = new Translate(sr.getScaledWidth() - this.getWidth(), sr.getScaledHeight() - 30);
    }
    
    public Notification(String title, String content, NotificationType type, CFontRenderer fr , int time) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.type = type;
        this.showTime = false;
        this.timer = new TimerUtil();
        this.fontRenderer = fr;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        this.translate = new Translate(sr.getScaledWidth() - this.getWidth(), sr.getScaledHeight() - 30);
    }

    public float getWidth() {
        return Math.max(100, Math.max(this.fontRenderer.getStringWidth(this.title), this.fontRenderer.getStringWidth(this.content)) + 10);
    }

    public final String getTitle() {
        return this.title;
    }

    public final String getContent() {
        return this.content;
    }

    public final int getTime() {
        return this.time;
    }
    
    public final double getDBTime() {
    	final double lol = this.time/1000;
        return lol;
    }

    public final NotificationType getType() {
        return this.type;
    }

    public final TimerUtil getTimer() {
        return this.timer;
    }

    public final Translate getTranslate() {
        return this.translate;
    }
}

