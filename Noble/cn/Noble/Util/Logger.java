/*

* Decompiled with CFR 0.150.
 */
package cn.Noble.Util;

import cn.Noble.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public final class Logger {
    protected static final Minecraft mc = Minecraft.getMinecraft();

    public static void log(String msg) {
        if (Logger.mc.player != null && Logger.mc.world != null) {
            StringBuilder tempMsg = new StringBuilder();
            for (String line : msg.split("\n")) {
                tempMsg.append(line).append("\u00a77");
            }
            Logger.mc.player.addChatMessage(new ChatComponentText("\u00a7c[" + Client.name + "]\u00a77: " + tempMsg.toString()));
        }
    }
}

