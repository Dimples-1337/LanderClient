package cn.Arctic.GUI.NewNotification;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import cn.Arctic.Client;
import cn.Arctic.Font.CFontRenderer;
import cn.Arctic.Font.FontLoaders;
import cn.Arctic.GUI.AnimationUtils;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.modules.GUI.HUD;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.Util.andy.RenderUtils;
import cn.Arctic.Util.animate.Translate;

public final class NotificationPublisher {
    private static final List<Notification> NOTIFICATIONS = new CopyOnWriteArrayList<Notification>();
    public static TimerUtil timer;

    public static void publish(ScaledResolution sr) {
        if (NOTIFICATIONS.isEmpty()) {
            return;
        }
        int srScaledHeight = sr.getScaledHeight() - 14;
        int scaledWidth = sr.getScaledWidth();
        int y = srScaledHeight - 30;
        Minecraft mc = Minecraft.getMinecraft();
        CFontRenderer fr = FontLoaders.NMSL20;
        CFontRenderer fr1 = FontLoaders.NMSL16;
        for (Notification notification : NOTIFICATIONS) {
            Translate translate = notification.getTranslate();
            float width = notification.getWidth();
            if (!notification.getTimer().elapsed(notification.getTime())) {
                notification.scissorBoxWidth = AnimationUtils.animate(width, notification.scissorBoxWidth, 0.1);
                translate.interpolate(scaledWidth - width, y, 0.15);
            } else {
                notification.scissorBoxWidth = AnimationUtils.animate(0.0, notification.scissorBoxWidth, 0.1);
                if (notification.scissorBoxWidth < 1.0) {
                    NOTIFICATIONS.remove(notification);
                }
                y += 30;
            }
            float translateX = (float)translate.getX();
            float translateY = (float)translate.getY();
            GL11.glPushMatrix();
            GL11.glEnable(3089);
            RenderUtils.prepareScissorBox((float)((double)scaledWidth - notification.scissorBoxWidth - notification.getWidth() / 4) - 1, translateY, scaledWidth + notification.getWidth() / 4, translateY + 30.0f);
            Gui.drawRect(translateX - notification.getWidth() / 4 - 5, translateY, scaledWidth, translateY + 30.0f, -1879048192);
            Gui.drawRect(translateX, translateY + 30.0f - 2.0f, (float) ((double)translateX + (double)width * ((double)((long)notification.getTime() - notification.getTimer().getElapsedTime()) / (double)notification.getTime())), translateY + 30.0f, notification.getType().getColor());
            RenderUtils.drawImage(notification.getType().getIcon(), translateX - 10 - width / 9, translateY + 5, 20, 20);
            fr.drawStringWithShadow(notification.getTitle(), translateX + 3.0f, translateY + 4.0f, -1);
            fr1.drawStringWithShadow(notification.getContent(), translateX + 3.0f, translateY + 17.0f, -1);
            GL11.glDisable(3089);
            GL11.glPopMatrix();
            y -= 30;
        }
    }

    public static void queue(String title, String content, NotificationType type, int time, boolean showTime) {
        Minecraft mc = Minecraft.getMinecraft();
        HUD hud = (HUD) Client.instance.getModuleManager().getModuleByClass(HUD.class);
        CFontRenderer fr = FontLoaders.NMSL16;
        NOTIFICATIONS.add(new Notification(title, content, type, fr, time, showTime));
    }
    
    public static void queue(String title, String content, NotificationType type, int time) {
        Minecraft mc = Minecraft.getMinecraft();
        HUD hud = (HUD) Client.instance.getModuleManager().getModuleByClass(HUD.class);
        CFontRenderer fr = FontLoaders.NMSL16;
        NOTIFICATIONS.add(new Notification(title, content, type, fr, time));
    }
}
