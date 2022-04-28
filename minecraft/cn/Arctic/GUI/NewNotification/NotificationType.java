/*
 * Decompiled with CFR 0.150.
 */
package cn.Arctic.GUI.NewNotification;

import net.minecraft.util.ResourceLocation;

import java.awt.Color;

public enum NotificationType {
    SUCCESS(new Color(6348946).getRGB(), new ResourceLocation("Melody/notification/success.png")),
    INFO(new Color(6590631).getRGB(), new ResourceLocation("Melody/notification/info.png")),
    WARN(new Color(6590631).getRGB(), new ResourceLocation("Melody/notification/warning.png")),
    ERROR(new Color(0xFF2F2F).getRGB(), new ResourceLocation("Melody/notification/error.png"));
	

    private final int color;
    private final ResourceLocation icon;

    public ResourceLocation getIcon() {
        return icon;
    }

    NotificationType(int color, ResourceLocation icon) {
        this.color = color;
        this.icon = icon;
    }

    public final int getColor() {
        return this.color;
    }
}

