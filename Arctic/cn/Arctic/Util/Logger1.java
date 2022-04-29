/*
 * Decompiled with CFR 0.150.
 */
package cn.Arctic.Util;

import cn.Arctic.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public final class Logger1 {
    protected static final Minecraft mc = Minecraft.getMinecraft();

    public static void log(String msg) {
        if (Logger1.mc.player != null && Logger1.mc.world != null) {
            StringBuilder tempMsg = new StringBuilder();
            for (String line : msg.split("\n")) {
                tempMsg.append(line).append("\u00a77");
            }
            Logger1.mc.player.addChatMessage(new ChatComponentText("\u00a7c[" + Client.name + "]\u00a77: " + tempMsg.toString()));
        }
       
        
    }

	public static void logToggleMessage(String string, Boolean value) {
		log(String.format("%s%s%s has been %s%s.", EnumChatFormatting.GOLD, EnumChatFormatting.GRAY, (EnumChatFormatting.GREEN + "enabled"),(EnumChatFormatting.RED + "disabled"), EnumChatFormatting.GRAY));
		
	}
}

