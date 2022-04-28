/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.Util.Chat;


import cn.Arctic.Client;
import cn.Arctic.Util.CombatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.Packet;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class Helper {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static void sendMessageOLD(String msg) {
        Object[] arrobject = new Object[2];
        Client.instance.getClass();
        arrobject[0] = (Object)((Object)EnumChatFormatting.BLUE) + "Lander" + (Object)((Object)EnumChatFormatting.GRAY) + ": ";
        arrobject[1] = msg;
        Helper.mc.player.addChatMessage(new ChatComponentText(String.format("%s%s", arrobject)));
    }

    public static void sendMessage(Object message) {
        new ChatUtils.ChatMessageBuilder(true, true).appendText(message + "").setColor(EnumChatFormatting.GRAY).build().displayClientSided();
    }

    public static void sendMessageWithoutPrefix(Object message) {
        new ChatUtils.ChatMessageBuilder(false, true).appendText(message +  "").setColor(EnumChatFormatting.GRAY).build().displayClientSided();
    }

    public static boolean onServer(String server) {
        if (!mc.isSingleplayer() && Helper.mc.getCurrentServerData().serverIP.toLowerCase().contains(server)) {
            return true;
        }
        return false;
    }
    
    public static void sendMessageLooK(String msg) {
        Object[] arrobject = new Object[2];
        Client.instance.getClass();
        arrobject[0] = (Object)((Object)EnumChatFormatting.RESET) + "[LookTP]" + (Object)((Object)EnumChatFormatting.WHITE) + " ";
        arrobject[1] = msg;
        Helper.mc.player.addChatMessage(new ChatComponentText(String.format("%s%s", arrobject)));
    }

	public static ClientPlayerEntity player() {
		// TODO Auto-generated method stub
		return Minecraft.getMinecraft().player;
	}

	public static WorldClient world() {
		// TODO Auto-generated method stub
		return Minecraft.getMinecraft().world;
	}
public static 	CombatUtils cby;

	public static Object combatUtils() {
		// TODO Auto-generated method stub
		return cby;
	}

	public static void sendPacket(Packet p){
    	mc.getNetHandler().addToSendQueue(p);
    }

	public static Minecraft mc() {
		// TODO Auto-generated method stub
		return Minecraft.getMinecraft();
	}
}

